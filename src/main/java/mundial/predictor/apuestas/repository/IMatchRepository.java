package mundial.predictor.apuestas.repository;

import java.util.List;
import java.util.Map;

public interface IMatchRepository {
    Map<String, Object> createMatch(int homeTeamId, int awayTeamId, String kickOffTime, int matchStatusId, int matchStageId, String opensAt, String closesAt, int isLocked);
    Map<String, Object> updateMatchResult(int matchId, int homeScore, int awayScore, int penalties, int winnerTeamId, int matchStatusId, int isLocked);
    List<Map<String, Object>> getMatches();
    Map<String, Object> getMatchById(int matchId);
    List<Map<String, Object>> getMatchesByHomeTeam(int countryId);
    List<Map<String, Object>> getMatchesByAwayTeam(int countryId);
    List<Map<String, Object>> getMatchesByDate(String date);
    List<Map<String, Object>> getMatchesUnlocked();
    List<Map<String, Object>> getMatchesLocked();
    Map<String, Object> unlockMatch(int matchId);
    Map<String, Object> lockMatch(int matchId);
}