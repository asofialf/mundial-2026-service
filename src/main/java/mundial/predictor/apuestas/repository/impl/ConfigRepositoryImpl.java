package mundial.predictor.apuestas.repository.impl;

import mundial.predictor.apuestas.repository.IConfigRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ConfigRepositoryImpl implements IConfigRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ConfigRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> getAllSettings() {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            String sql = "SELECT setting_key, setting_value FROM app_settings";
            return namedParameterJdbcTemplate.queryForList(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener la configuración: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener la configuración: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Map<String, Object> upsertSetting(String key, String value) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("key", key, Types.VARCHAR);
            parameters.addValue("value", value, Types.VARCHAR);

            String updateSql = "UPDATE app_settings SET setting_value = :value WHERE setting_key = :key";
            int rowsUpdated = namedParameterJdbcTemplate.update(updateSql, parameters);

            if (rowsUpdated == 0) {
                String insertSql = "INSERT INTO app_settings (setting_key, setting_value) VALUES (:key, :value)";
                namedParameterJdbcTemplate.update(insertSql, parameters);
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("settingKey", key);
            result.put("settingValue", value);
            return result;
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al actualizar la configuración: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al actualizar la configuración: " + ex.getMessage(), ex);
        }
    }
}
