package mundial.predictor.apuestas.repository.impl;

import mundial.predictor.apuestas.repository.IPredictionRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
public class PredictionRepositoryImpl implements IPredictionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PredictionRepositoryImpl(JdbcTemplate jdbcTemplate,
                              NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    @Override
    public Map<String, Object> createUserGroupPrediction(int userId, int groupId, int first_place_id, int second_place_id){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("userId", userId, Types.INTEGER);
            parameters.addValue("groupId", groupId, Types.INTEGER);
            parameters.addValue("first_place_id", first_place_id, Types.INTEGER);
            parameters.addValue("second_place_id", second_place_id, Types.INTEGER);

            String sql = "EXEC sp_create_user_group_prediction @user_id = :userId, @group_id = :groupId, @first_place_id = :first_place_id, @second_place_id = :second_place_id";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener la información de la predicción de grupos: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener la predicción de grupos: " + ex.getMessage(), ex);
        }
    }
    @Override
    public List<Map<String, Object>> getUserGroupPrediction(int userId){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("userId", userId, Types.INTEGER);

            String sql = "EXEC sp_get_user_group_predictions @user_id = :userId";

            return namedParameterJdbcTemplate.queryForList(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener la información de la predicción: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener la información de la predicción: " + ex.getMessage(), ex);
        }
    }
    @Override
    public Map<String, Object> updateUserGroupPrediction(int predictionId, int first_place_id, int second_place_id){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("predictionId", predictionId, Types.INTEGER);
            parameters.addValue("first_place_id", first_place_id, Types.INTEGER);
            parameters.addValue("second_place_id", second_place_id, Types.INTEGER);

            String sql = "EXEC sp_update_user_group_prediction @prediction_id = :predictionId, @first_place_id = :first_place_id, @second_place_id = :second_place_id";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener la información de la predicción de grupos: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener la predicción de grupos: " + ex.getMessage(), ex);
        }
    }
    @Override
    public Map<String, Object> deleteUserGroupPrediction(int predictionId){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("predictionId", predictionId, Types.INTEGER);

            String sql = "EXEC sp_delete_user_group_prediction @prediction_id = :predictionId";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener la información de la predicción de grupos: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener la predicción de grupos: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Map<String, Object>> getUserBestThird(int userId){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("userId", userId, Types.INTEGER);

            String sql = "EXEC sp_get_user_best_third @user_id = :userId";

            return namedParameterJdbcTemplate.queryForList(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener la información de los usuarios: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener la información de los usuarios: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Map<String, Object> createUserBestThird(int userId, int countryId){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("userId", userId, Types.INTEGER);
            parameters.addValue("countryId", countryId, Types.INTEGER);

            String sql = "EXEC sp_create_user_best_third @user_id = :userId, @country_id = :countryId";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener la información de los usuarios: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener la información de los usuarios: " + ex.getMessage(), ex);
        }
    }
    @Override
    public Map<String, Object> updateUserBestThirdById(int userBestThirdId, int userId, int countryId){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("userBestThirdId", userBestThirdId, Types.INTEGER);
            parameters.addValue("userId", userId, Types.INTEGER);
            parameters.addValue("countryId", countryId, Types.INTEGER);

            String sql = "EXEC sp_update_user_best_third @user_best_third_id = :userBestThirdId, @user_id = :userId, @country_id = :countryId";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        }catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener la información de los usuarios: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener la información de los usuarios: " + ex.getMessage(), ex);
        }
    }

    // ── Predicciones de Eliminatorias ────────────────────────────
    // NUEVO: no existían en el esquema original. Tabla creada por
    // SchemaInitializer (ver config/SchemaInitializer.java).

    @Override
    public Map<String, Object> createUserKnockoutPrediction(int matchId, int userId, int scoreTeamA, int scoreTeamB, int advancingTeamId, boolean hasPenalties) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("matchId", matchId, Types.INTEGER);
            parameters.addValue("userId", userId, Types.INTEGER);
            parameters.addValue("scoreTeamA", scoreTeamA, Types.INTEGER);
            parameters.addValue("scoreTeamB", scoreTeamB, Types.INTEGER);
            parameters.addValue("advancingTeamId", advancingTeamId, Types.INTEGER);
            parameters.addValue("hasPenalties", hasPenalties, Types.BOOLEAN);

            String sql = "INSERT INTO user_knockout_predictions " +
                    "(match_id, user_id, score_team_a, score_team_b, advancing_team_id, has_penalties) " +
                    "OUTPUT inserted.* " +
                    "VALUES (:matchId, :userId, :scoreTeamA, :scoreTeamB, :advancingTeamId, :hasPenalties)";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al crear la predicción de eliminatoria: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al crear la predicción de eliminatoria: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Map<String, Object> updateUserKnockoutPrediction(int predictionId, int scoreTeamA, int scoreTeamB, int advancingTeamId, boolean hasPenalties) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("predictionId", predictionId, Types.INTEGER);
            parameters.addValue("scoreTeamA", scoreTeamA, Types.INTEGER);
            parameters.addValue("scoreTeamB", scoreTeamB, Types.INTEGER);
            parameters.addValue("advancingTeamId", advancingTeamId, Types.INTEGER);
            parameters.addValue("hasPenalties", hasPenalties, Types.BOOLEAN);

            String sql = "UPDATE user_knockout_predictions SET " +
                    "score_team_a = :scoreTeamA, score_team_b = :scoreTeamB, " +
                    "advancing_team_id = :advancingTeamId, has_penalties = :hasPenalties " +
                    "OUTPUT inserted.* " +
                    "WHERE prediction_id = :predictionId";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al actualizar la predicción de eliminatoria: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al actualizar la predicción de eliminatoria: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Map<String, Object>> getUserKnockoutPredictions(int userId) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("userId", userId, Types.INTEGER);

            String sql = "SELECT * FROM user_knockout_predictions WHERE user_id = :userId";

            return namedParameterJdbcTemplate.queryForList(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener las predicciones de eliminatoria: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener las predicciones de eliminatoria: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Map<String, Object>> getAllKnockoutPredictions() {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            String sql = "SELECT * FROM user_knockout_predictions";

            return namedParameterJdbcTemplate.queryForList(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener las predicciones de eliminatoria: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener las predicciones de eliminatoria: " + ex.getMessage(), ex);
        }
    }
}
