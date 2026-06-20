package mundial.predictor.apuestas.repository.impl;

import mundial.predictor.apuestas.repository.IGroupRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class GroupRepositoryImpl implements IGroupRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GroupRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> getAllGroupsWithCountries() {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            String sql =
                "SELECT g.group_id AS group_id, g.name AS group_name, " +
                "       c.country_id AS country_id, c.name AS country_name, " +
                "       c.fifa_code AS fifa_code, c.image AS image " +
                "FROM groups g " +
                "JOIN group_countries gc ON gc.group_id = g.group_id " +
                "JOIN countries c ON c.country_id = gc.country_id " +
                "ORDER BY g.group_id, c.name";

            return namedParameterJdbcTemplate.queryForList(sql, parameters);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error al obtener los grupos: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado al obtener los grupos: " + ex.getMessage(), ex);
        }
    }
}
