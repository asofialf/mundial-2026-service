package mundial.predictor.apuestas.services;

import mundial.predictor.apuestas.repository.IConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConfigService {
    @Autowired
    private IConfigRepository configRepository;

    /** Devuelve todos los settings como mapa plano { setting_key -> setting_value }. */
    public Map<String, String> getAllSettingsAsMap() {
        Map<String, String> map = new java.util.LinkedHashMap<>();
        for (Map<String, Object> row : configRepository.getAllSettings()) {
            map.put(String.valueOf(row.get("setting_key")), String.valueOf(row.get("setting_value")));
        }
        return map;
    }

    public Map<String, Object> updateSetting(String key, String value) {
        return configRepository.upsertSetting(key, value);
    }
}
