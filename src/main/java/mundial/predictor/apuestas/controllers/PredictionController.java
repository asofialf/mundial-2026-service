package mundial.predictor.apuestas.controllers;

import mundial.predictor.apuestas.services.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prediction")
public class PredictionController {
    @Autowired
    private PredictionService predictionService;

    /**
     ** POST /api/prediction/create-user-group_prediction
     **/
    @PostMapping("/create-user-group-prediction")
    public ResponseEntity<Map<String, Object>> createUserGroupPrediction(@RequestParam int userId,
                                                             @RequestParam int groupId,
                                                             @RequestParam int first_place_id,
                                                             @RequestParam int second_place_id) {
        try {
            Map<String, Object> newUserGroupPrediction = predictionService.createUserGroupPrediction(userId, groupId, first_place_id, second_place_id);
            return ResponseEntity.ok(newUserGroupPrediction);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     ** GET /api/prediction/get-user-group_prediction
     **/
    @GetMapping("/get-user-group-prediction")
    public ResponseEntity<List<Map<String, Object>>> getUserGroupPrediction(@RequestParam int userId) {
        try {
            List<Map<String, Object>> userGroupPrediction = predictionService.getUserGroupPrediction(userId);
            return ResponseEntity.ok(userGroupPrediction);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     ** PUT /api/prediction/update-user-group_prediction
     **/
    @PutMapping("/update-user-group-prediction")
    public ResponseEntity<Map<String, Object>> updateUserGroupPrediction(@RequestParam int predictionId,
                                                                         @RequestParam int first_place_id,
                                                                         @RequestParam int second_place_id) {
        try {
            Map<String, Object> updatedUserGroupPrediction = predictionService.updateUserGroupPrediction(predictionId, first_place_id, second_place_id);
            return ResponseEntity.ok(updatedUserGroupPrediction);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     ** DELETE /api/prediction/delete-user-group_prediction
     **/
    @DeleteMapping("/delete-user-group-prediction")
    public ResponseEntity<Map<String, Object>> deleteUserGroupPrediction(@RequestParam int predictionId) {
        try {
            Map<String, Object> deletedUserGroupPrediction = predictionService.deleteUserGroupPrediction(predictionId);
            return ResponseEntity.ok(deletedUserGroupPrediction);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     ** GET /api/prediction/get-user-best-third
     **/
    @GetMapping("/get-user-best-third")
    public ResponseEntity<List<Map<String, Object>>> getUserBestThird(@RequestParam int userId) {
        try {
            List<Map<String, Object>> userBestThird = predictionService.getUserBestThird(userId);
            return ResponseEntity.ok(userBestThird);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     ** POST /api/prediction/create-user-best-thrid
     **/
    @PostMapping("/create-user-best-thrid")
    public ResponseEntity<Map<String, Object>> createUserBestThird(@RequestParam int userId,
                                                                         @RequestParam int countryId) {
        try {
            Map<String, Object> newUserBestThird = predictionService.createUserBestThird(userId, countryId);
            return ResponseEntity.ok(newUserBestThird);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     ** PUT /api/prediction/update-user-best-thrid
     **/
    @PutMapping("/update-user-best-thrid")
    public ResponseEntity<Map<String, Object>> updateUserBestThird(@RequestParam int userBestThirdId,
                                                                   @RequestParam int userId,
                                                                   @RequestParam int countryId){
        try {
            Map<String, Object> newUserBestThird = predictionService.updateUserBestThirdById(userBestThirdId, userId, countryId);
            return ResponseEntity.ok(newUserBestThird);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ── Predicciones de Eliminatorias ────────────────────────────
    // NUEVO: el backend original no tenía persistencia para esto.

    /**
     ** POST /api/prediction/create-user-knockout-prediction
     **/
    @PostMapping("/create-user-knockout-prediction")
    public ResponseEntity<Map<String, Object>> createUserKnockoutPrediction(@RequestParam int matchId,
                                                                             @RequestParam int userId,
                                                                             @RequestParam int scoreTeamA,
                                                                             @RequestParam int scoreTeamB,
                                                                             @RequestParam int advancingTeamId,
                                                                             @RequestParam boolean hasPenalties) {
        try {
            Map<String, Object> newPrediction = predictionService.createUserKnockoutPrediction(matchId, userId, scoreTeamA, scoreTeamB, advancingTeamId, hasPenalties);
            return ResponseEntity.ok(newPrediction);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     ** PUT /api/prediction/update-user-knockout-prediction
     **/
    @PutMapping("/update-user-knockout-prediction")
    public ResponseEntity<Map<String, Object>> updateUserKnockoutPrediction(@RequestParam int predictionId,
                                                                             @RequestParam int scoreTeamA,
                                                                             @RequestParam int scoreTeamB,
                                                                             @RequestParam int advancingTeamId,
                                                                             @RequestParam boolean hasPenalties) {
        try {
            Map<String, Object> updatedPrediction = predictionService.updateUserKnockoutPrediction(predictionId, scoreTeamA, scoreTeamB, advancingTeamId, hasPenalties);
            return ResponseEntity.ok(updatedPrediction);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     ** GET /api/prediction/get-user-knockout-prediction
     **/
    @GetMapping("/get-user-knockout-prediction")
    public ResponseEntity<List<Map<String, Object>>> getUserKnockoutPrediction(@RequestParam int userId) {
        try {
            List<Map<String, Object>> predictions = predictionService.getUserKnockoutPredictions(userId);
            return ResponseEntity.ok(predictions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
