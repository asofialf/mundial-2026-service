package mundial.predictor.apuestas.repository.impl;

import mundial.predictor.apuestas.repository.IMatchRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
public class MatchRepositoryImpl implements IMatchRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MatchRepositoryImpl(JdbcTemplate jdbcTemplate,
                                    NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    @Override
    public Map<String, Object> createMatch(int homeTeamId, int awayTeamId, String kickOffTime, int matchStatusId, int matchStageId, String opensAt, String closesAt, int isLocked) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("homeTeamId", homeTeamId, Types.INTEGER);
            parameters.addValue("awayTeamId", awayTeamId, Types.INTEGER);
            parameters.addValue("kickOffTime", kickOffTime, Types.VARCHAR);
            parameters.addValue("matchStatusId", matchStatusId, Types.INTEGER);
            parameters.addValue("matchStageId", matchStageId, Types.INTEGER);
            parameters.addValue("opensAt", opensAt, Types.VARCHAR);
            parameters.addValue("closesAt", closesAt, Types.VARCHAR);
            parameters.addValue("isLocked", isLocked, Types.INTEGER);

            String sql = "EXEC sp_create_match @home_team_id = :homeTeamId, @away_team_id = :awayTeamId, @kickoff_time = :kickOffTime, @match_status_id = :matchStatusId, @match_stage_id = :matchStageId, @opens_at = :opensAt, @closes_at =:closesAt, @is_locked =:isLocked";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al crear el partido: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al crear el partido: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Map<String, Object> updateMatchResult(int matchId, int homeScore, int awayScore, int penalties, int winnerTeamId, int matchStatusId, int isLocked){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("matchId", matchId, Types.INTEGER);
            parameters.addValue("homeScore", homeScore, Types.INTEGER);
            parameters.addValue("awayScore", awayScore, Types.INTEGER);
            parameters.addValue("penalties", penalties, Types.INTEGER);
            parameters.addValue("winnerTeamId", winnerTeamId, Types.INTEGER);
            parameters.addValue("matchStatusId", matchStatusId, Types.INTEGER);
            parameters.addValue("isLocked", isLocked, Types.INTEGER);

            String sql = "EXEC sp_update_match_results @match_id = :matchId, @home_score = :homeScore, @away_score = :awayScore, @penalties = :penalties, @winner_team_id = :winnerTeamId, @match_status_id = :matchStatusId, @is_locked =:isLocked";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al actualizar el partido: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al actualizar el partido: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Map<String, Object>> getMatches(){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            String sql = "SELECT * FROM matches";

            return namedParameterJdbcTemplate.queryForList(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener los partidos: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener los partidos: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Map<String, Object>> getMatchesByHomeTeam(int countryId){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            String sql = "SELECT * FROM matches WHERE home_team_id = :countryId";

            return namedParameterJdbcTemplate.queryForList(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener los partidos: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener los partidos: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Map<String, Object>> getMatchesByAwayTeam(int countryId){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            String sql = "SELECT * FROM matches WHERE away_team_id = :countryId";

            return namedParameterJdbcTemplate.queryForList(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener los partidos: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener los partidos: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Map<String, Object>> getMatchesByDate(String date){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("date", date);

            String sql = "SELECT * FROM matches " +
                    "WHERE CAST(kickoff_time AS DATE) = :date";

            return namedParameterJdbcTemplate.queryForList(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener los partidos: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener los partidos: " + ex.getMessage(), ex);
        }
    }


    @Override
    public List<Map<String, Object>> getMatchesUnlocked(){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            String sql = "SELECT * FROM matches WHERE is_locked = 0";

            return namedParameterJdbcTemplate.queryForList(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener los partidos: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener los partidos: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Map<String, Object>> getMatchesLocked(){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            String sql = "SELECT * FROM matches WHERE is_locked = 1";

            return namedParameterJdbcTemplate.queryForList(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener los partidos: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener los partidos: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Map<String, Object> unlockMatch(int matchId){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("matchId", matchId, Types.INTEGER);
            String sql = "UPDATE matches SET is_locked = 0 WHERE match_id = :matchId; SELECT * FROM matches WHERE match_id = :matchId";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener los partidos: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener los partidos: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Map<String, Object> lockMatch(int matchId){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("matchId", matchId, Types.INTEGER);
            String sql = "UPDATE matches SET is_locked = 1 WHERE match_id = :matchId; SELECT * FROM matches WHERE match_id = :matchId";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener los partidos: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener los partidos: " + ex.getMessage(), ex);
        }
    }
}
