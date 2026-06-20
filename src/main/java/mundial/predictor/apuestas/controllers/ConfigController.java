package mundial.predictor.apuestas.controllers;

import mundial.predictor.apuestas.services.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/config")
public class ConfigController {
    @Autowired
    private ConfigService configService;

    /**
     ** GET /api/config/all
     ** Devuelve todos los flags de configuración como { setting_key: setting_value }.
     ** Claves usadas por el frontend: is_group_phase_active, is_knockout_phase_active,
     ** knockout_stage_<matchStageId>_active.
     **/
    @GetMapping("/all")
    public ResponseEntity<Map<String, String>> getAllSettings() {
        try {
            return ResponseEntity.ok(configService.getAllSettingsAsMap());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     ** PUT /api/config/update
     ** Crea o actualiza un flag de configuración (admin).
     **/
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateSetting(@RequestParam String key, @RequestParam String value) {
        try {
            return ResponseEntity.ok(configService.updateSetting(key, value));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
