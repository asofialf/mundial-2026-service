package mundial.predictor.apuestas.services;

import mundial.predictor.apuestas.repository.IGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroupService {
    @Autowired
    private IGroupRepository groupRepository;

    /**
     * Agrupa las filas planas de la BD (un row por equipo) en una lista de
     * grupos con sus países anidados:
     * [{ groupId, name, countries: [{ countryId, name, fifaCode, image }, ...] }, ...]
     */
    public List<Map<String, Object>> getAllGroupsWithCountries() {
        List<Map<String, Object>> rows = groupRepository.getAllGroupsWithCountries();

        Map<Integer, Map<String, Object>> groupsById = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            int groupId = ((Number) row.get("group_id")).intValue();

            Map<String, Object> group = groupsById.computeIfAbsent(groupId, id -> {
                Map<String, Object> g = new LinkedHashMap<>();
                g.put("groupId", id);
                g.put("name", row.get("group_name"));
                g.put("countries", new ArrayList<Map<String, Object>>());
                return g;
            });

            Map<String, Object> country = new LinkedHashMap<>();
            country.put("countryId", row.get("country_id"));
            country.put("name", row.get("country_name"));
            country.put("fifaCode", row.get("fifa_code"));
            country.put("image", row.get("image"));

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> countries = (List<Map<String, Object>>) group.get("countries");
            countries.add(country);
        }

        return new ArrayList<>(groupsById.values());
    }
}
