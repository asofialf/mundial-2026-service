package mundial.predictor.apuestas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Calcula la tabla de puntajes combinando: usuarios, grupos/países reales
 * (GroupService), partidos (MatchService) y predicciones (PredictionService).
 *
 * LIMITACIÓN: no existe una tabla de standings de grupo en la BD — se
 * calculan aquí a partir de los resultados de partidos de fase de grupos
 * (home_score/away_score) y la composición real de grupos. El criterio de
 * desempate es simplificado: puntos -> diferencia de goles -> goles a
 * favor (no incluye head-to-head ni fair play oficiales de FIFA).
 */
@Service
public class LeaderboardService {
    @Autowired private UserService userService;
    @Autowired private GroupService groupService;
    @Autowired private MatchService matchService;
    @Autowired private PredictionService predictionService;

    private static class StandingRow {
        int countryId, points, goalsFor, goalsAgainst;
        StandingRow(int countryId) { this.countryId = countryId; }
        int goalDiff() { return goalsFor - goalsAgainst; }
    }

    public List<Map<String, Object>> getLeaderboard() {
        List<Map<String, Object>> groups = groupService.getAllGroupsWithCountries();
        List<Map<String, Object>> matches = matchService.getMatches();
        List<Map<String, Object>> users = userService.getUsers();

        Map<Integer, Integer> countryToGroup = mapCountryToGroup(groups);
        Map<Integer, List<StandingRow>> standingsByGroup = computeStandings(groups, matches, countryToGroup);
        Set<Integer> actualBestThirds = computeBestThirds(standingsByGroup);
        Map<Integer, Map<String, Object>> matchesById = indexById(matches, "match_id", "matchId");

        List<Map<String, Object>> rows = new ArrayList<>();
        for (Map<String, Object> user : users) {
            int userId = intVal(user, "user_id", "userId");
            if (userId < 0) continue;

            String alias = strVal(user, "alias", "profile_name", "email");

            List<Map<String, Object>> groupPreds = predictionService.getUserGroupPrediction(userId);
            List<Map<String, Object>> thirdPreds = predictionService.getUserBestThird(userId);
            List<Map<String, Object>> knockoutPreds = predictionService.getUserKnockoutPredictions(userId);

            int groupPts = scoreGroupPredictions(groupPreds, standingsByGroup);
            int thirdPts = scoreBestThirdPredictions(thirdPreds, actualBestThirds);
            int knockoutPts = scoreKnockoutPredictions(knockoutPreds, matchesById);

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("userId", userId);
            row.put("alias", alias);
            row.put("totalPoints", groupPts + thirdPts + knockoutPts);
            rows.add(row);
        }

        rows.sort((a, b) -> ((Integer) b.get("totalPoints")).compareTo((Integer) a.get("totalPoints")));
        for (int i = 0; i < rows.size(); i++) {
            rows.get(i).put("rank", i + 1);
        }
        return rows;
    }

    private Map<Integer, Integer> mapCountryToGroup(List<Map<String, Object>> groups) {
        Map<Integer, Integer> map = new HashMap<>();
        for (Map<String, Object> group : groups) {
            int groupId = intVal(group, "groupId");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> countries = (List<Map<String, Object>>) group.get("countries");
            for (Map<String, Object> country : countries) {
                map.put(intVal(country, "countryId"), groupId);
            }
        }
        return map;
    }

    private Map<Integer, List<StandingRow>> computeStandings(List<Map<String, Object>> groups, List<Map<String, Object>> matches, Map<Integer, Integer> countryToGroup) {
        Map<Integer, Map<Integer, StandingRow>> byGroup = new LinkedHashMap<>();
        for (Map<String, Object> group : groups) {
            int groupId = intVal(group, "groupId");
            Map<Integer, StandingRow> rows = new LinkedHashMap<>();
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> countries = (List<Map<String, Object>>) group.get("countries");
            for (Map<String, Object> country : countries) {
                int countryId = intVal(country, "countryId");
                rows.put(countryId, new StandingRow(countryId));
            }
            byGroup.put(groupId, rows);
        }

        for (Map<String, Object> match : matches) {
            int stageId = intVal(match, "match_stage_id", "matchStageId");
            if (stageId != ScoringRules.GROUP_STAGE_ID) continue;

            Integer homeScore = nullableInt(match, "home_score", "homeScore");
            Integer awayScore = nullableInt(match, "away_score", "awayScore");
            if (homeScore == null || awayScore == null) continue;

            int homeTeamId = intVal(match, "home_team_id", "homeTeamId");
            int awayTeamId = intVal(match, "away_team_id", "awayTeamId");
            Integer groupId = countryToGroup.get(homeTeamId);
            if (groupId == null || !groupId.equals(countryToGroup.get(awayTeamId))) continue;

            Map<Integer, StandingRow> rows = byGroup.get(groupId);
            if (rows == null) continue;
            StandingRow home = rows.get(homeTeamId);
            StandingRow away = rows.get(awayTeamId);
            if (home == null || away == null) continue;

            home.goalsFor += homeScore; home.goalsAgainst += awayScore;
            away.goalsFor += awayScore; away.goalsAgainst += homeScore;

            if (homeScore > awayScore) home.points += 3;
            else if (homeScore < awayScore) away.points += 3;
            else { home.points += 1; away.points += 1; }
        }

        Comparator<StandingRow> byStanding = Comparator
                .comparingInt((StandingRow r) -> r.points).reversed()
                .thenComparing(Comparator.comparingInt((StandingRow r) -> r.goalDiff()).reversed())
                .thenComparing(Comparator.comparingInt((StandingRow r) -> r.goalsFor).reversed());

        Map<Integer, List<StandingRow>> result = new LinkedHashMap<>();
        byGroup.forEach((groupId, rows) -> {
            List<StandingRow> sorted = new ArrayList<>(rows.values());
            sorted.sort(byStanding);
            result.put(groupId, sorted);
        });
        return result;
    }

    private Set<Integer> computeBestThirds(Map<Integer, List<StandingRow>> standingsByGroup) {
        Comparator<StandingRow> byStanding = Comparator
                .comparingInt((StandingRow r) -> r.points).reversed()
                .thenComparing(Comparator.comparingInt((StandingRow r) -> r.goalDiff()).reversed())
                .thenComparing(Comparator.comparingInt((StandingRow r) -> r.goalsFor).reversed());

        List<StandingRow> thirds = new ArrayList<>();
        for (List<StandingRow> rows : standingsByGroup.values()) {
            if (rows.size() >= 3) thirds.add(rows.get(2));
        }
        thirds.sort(byStanding);

        Set<Integer> result = new HashSet<>();
        for (int i = 0; i < Math.min(8, thirds.size()); i++) {
            result.add(thirds.get(i).countryId);
        }
        return result;
    }

    private int scoreGroupPredictions(List<Map<String, Object>> picks, Map<Integer, List<StandingRow>> standingsByGroup) {
        int points = 0;
        for (Map<String, Object> pick : picks) {
            int groupId = intVal(pick, "group_id", "groupId");
            int firstPlaceId = intVal(pick, "first_place_id", "firstPlaceId");
            int secondPlaceId = intVal(pick, "second_place_id", "secondPlaceId");

            List<StandingRow> rows = standingsByGroup.get(groupId);
            if (rows == null || rows.size() < 2) continue;
            boolean anyPlayed = rows.stream().anyMatch(r -> r.points > 0 || r.goalsFor > 0 || r.goalsAgainst > 0);
            if (!anyPlayed) continue;

            int actualFirst = rows.get(0).countryId;
            int actualSecond = rows.get(1).countryId;

            if (firstPlaceId == actualFirst && secondPlaceId == actualSecond) {
                points += ScoringRules.PRIMERO_Y_SEGUNDO_EXACTO;
                continue;
            }

            Set<Integer> top2 = Set.of(actualFirst, actualSecond);
            if (top2.contains(firstPlaceId)) points += ScoringRules.EQUIPO_EN_TOP2_SIN_ORDEN;
            if (top2.contains(secondPlaceId)) points += ScoringRules.EQUIPO_EN_TOP2_SIN_ORDEN;
        }
        return points;
    }

    private int scoreBestThirdPredictions(List<Map<String, Object>> picks, Set<Integer> actualBestThirds) {
        int count = 0;
        for (Map<String, Object> pick : picks) {
            int countryId = intVal(pick, "country_id", "countryId");
            if (actualBestThirds.contains(countryId)) count++;
        }
        return count * ScoringRules.MEJOR_TERCERO_ACERTADO;
    }

    private int scoreKnockoutPredictions(List<Map<String, Object>> predictions, Map<Integer, Map<String, Object>> matchesById) {
        int points = 0;
        for (Map<String, Object> pred : predictions) {
            int matchId = intVal(pred, "match_id", "matchId");
            Map<String, Object> match = matchesById.get(matchId);
            if (match == null) continue;

            Integer homeScore = nullableInt(match, "home_score", "homeScore");
            Integer awayScore = nullableInt(match, "away_score", "awayScore");
            if (homeScore == null || awayScore == null) continue;

            int scoreTeamA = intVal(pred, "score_team_a", "scoreTeamA");
            int scoreTeamB = intVal(pred, "score_team_b", "scoreTeamB");
            int advancingTeamId = intVal(pred, "advancing_team_id", "advancingTeamId");
            boolean hasPenalties = boolVal(pred, "has_penalties", "hasPenalties");

            Integer winnerTeamId = nullableInt(match, "winner_team_id", "winnerTeamId");

            if (scoreTeamA == homeScore && scoreTeamB == awayScore) {
                points += ScoringRules.MARCADOR_EXACTO;
            } else if (winnerTeamId != null && advancingTeamId == winnerTeamId) {
                points += ScoringRules.CLASIFICADO_ACERTADO;
            }

            Integer penalties = nullableInt(match, "penalties");
            if (penalties != null && hasPenalties == (penalties == 1)) {
                points += ScoringRules.PENALES_ACERTADO;
            }
        }
        return points;
    }

    private Map<Integer, Map<String, Object>> indexById(List<Map<String, Object>> rows, String... idKeys) {
        Map<Integer, Map<String, Object>> map = new HashMap<>();
        for (Map<String, Object> row : rows) {
            int id = intVal(row, idKeys);
            if (id >= 0) map.put(id, row);
        }
        return map;
    }

    private int intVal(Map<String, Object> row, String... keys) {
        Integer v = nullableInt(row, keys);
        return v == null ? -1 : v;
    }

    private Integer nullableInt(Map<String, Object> row, String... keys) {
        for (String key : keys) {
            Object v = row.get(key);
            if (v instanceof Number) return ((Number) v).intValue();
        }
        return null;
    }

    private boolean boolVal(Map<String, Object> row, String... keys) {
        for (String key : keys) {
            Object v = row.get(key);
            if (v instanceof Boolean) return (Boolean) v;
            if (v instanceof Number) return ((Number) v).intValue() == 1;
        }
        return false;
    }

    private String strVal(Map<String, Object> row, String... keys) {
        for (String key : keys) {
            Object v = row.get(key);
            if (v != null) return String.valueOf(v);
        }
        return "Usuario";
    }
}
