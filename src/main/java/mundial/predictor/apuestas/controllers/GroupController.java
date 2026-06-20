package mundial.predictor.apuestas.controllers;

import mundial.predictor.apuestas.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    private GroupService groupService;

    /**
     ** GET /api/group/all
     ** Devuelve los 12 grupos con sus equipos anidados.
     **/
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllGroups() {
        try {
            List<Map<String, Object>> groups = groupService.getAllGroupsWithCountries();
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
