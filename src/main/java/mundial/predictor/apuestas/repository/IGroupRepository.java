package mundial.predictor.apuestas.repository;

import java.util.List;
import java.util.Map;

public interface IGroupRepository {
    /** Filas planas: group_id, group_name, country_id, country_name, fifa_code, image. */
    List<Map<String, Object>> getAllGroupsWithCountries();
}
