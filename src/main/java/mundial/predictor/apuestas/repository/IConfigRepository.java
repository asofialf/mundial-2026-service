package mundial.predictor.apuestas.repository;

import java.util.List;
import java.util.Map;

public interface IConfigRepository {
    List<Map<String, Object>> getAllSettings();
    Map<String, Object> upsertSetting(String key, String value);
}
