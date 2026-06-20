package mundial.predictor.apuestas.repository.impl;

import mundial.predictor.apuestas.repository.IUserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements IUserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate,
                              NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Map<String, Object> createUser(String email, String password){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("email", email, Types.VARCHAR);
            parameters.addValue("password_hash", password, Types.VARCHAR);
            parameters.addValue("role_id", 2, Types.INTEGER);

            String sql = "EXEC sp_create_user @email = :email, @password_hash = :password_hash, @role_id = :role_id";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al crear el usuario: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al crear el usuario: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Map<String, Object> createProfile(int userId, String name, String alias, String icon){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("userId", userId, Types.INTEGER);
            parameters.addValue("name", name, Types.VARCHAR);
            parameters.addValue("alias", alias, Types.VARCHAR);
            parameters.addValue("icon", icon, Types.VARCHAR);

            String sql = "EXEC sp_create_profile @users_id = :userId, @name = :name, @alias = :alias, @icon = :icon";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al crear el perfil del usuario " + userId + ": " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al crear el perfil del usuario " + userId + ": " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Map<String, Object>> getUsers(){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();

            // LEFT JOIN a profiles para incluir el alias (lo necesita el leaderboard;
            // el endpoint original solo traía la tabla users, sin alias).
            // NOTA: se asume que la tabla se llama "profiles" (plural, como "users"
            // y "matches") — verificar contra el esquema real si esto falla.
            String sql = "SELECT u.*, p.alias AS alias, p.name AS profile_name, p.icon AS icon " +
                    "FROM users u LEFT JOIN profiles p ON p.users_id = u.user_id";

            return namedParameterJdbcTemplate.queryForList(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener la información de los usuarios: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener la información de los usuarios: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Map<String, Object> getUserById(int userId){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("userId", userId, Types.INTEGER);

            String sql = "EXEC sp_get_user_by_id @user_id = :userId";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener la información del usuario " + userId + ": " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener la información del usuario " + userId + ": " + ex.getMessage(), ex);
        }
    }

    @Override
    public Map<String, Object> getUserForLogin(int loginType, String loginValue){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("loginType", loginType, Types.INTEGER);
            parameters.addValue("loginValue", loginValue, Types.VARCHAR);

            String sql = "EXEC sp_get_user_for_login @login_type = :loginType, @login_value = :loginValue";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener la información de inicio de sesión: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener la información de inicio de sesión: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Map<String, Object> changePassword(int userId, String oldPassword, String newPassword){
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("userId", userId, Types.INTEGER);
            parameters.addValue("oldPassword", oldPassword, Types.VARCHAR);
            parameters.addValue("newPassword", newPassword, Types.VARCHAR);

            String sql = "EXEC sp_change_password @user_id = :userId, @old_password = :oldPassword, @new_password = :newPassword";

            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener la información de inicio de sesión: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener la información de inicio de sesión: " + ex.getMessage(), ex);
        }
    }
}
