package mundial.predictor.apuestas.services;

import mundial.predictor.apuestas.repository.IPredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class PredictionService {
    @Autowired
    private IPredictionRepository predictionRepository;

    @Autowired
    private MatchService matchService;

    public Map<String, Object> createUserGroupPrediction(int userId, int groupId, int first_place_id, int second_place_id){
        return predictionRepository.createUserGroupPrediction(userId, groupId, first_place_id, second_place_id);
    }
    public List<Map<String, Object>> getUserGroupPrediction(int userId){
        return predictionRepository.getUserGroupPrediction(userId);
    }
    public Map<String, Object> updateUserGroupPrediction(int predictionId, int first_place_id, int second_place_id){
        return predictionRepository.updateUserGroupPrediction(predictionId, first_place_id, second_place_id);
    }
    public Map<String, Object> deleteUserGroupPrediction(int predictionId){
        return predictionRepository.deleteUserGroupPrediction(predictionId);
    }
    public List<Map<String, Object>> getUserBestThird(int userId){
        return predictionRepository.getUserBestThird(userId);
    }
    public Map<String, Object> createUserBestThird(int userId, int countryId){
        return predictionRepository.createUserBestThird(userId, countryId);
    }
    public Map<String, Object> updateUserBestThirdById(int userBestThirdId, int userId, int countryId){
        return predictionRepository.updateUserBestThirdById(userBestThirdId, userId, countryId);
    }

    public Map<String, Object> createUserKnockoutPrediction(int matchId, int userId, int scoreTeamA, int scoreTeamB, int advancingTeamId, boolean hasPenalties){
        assertMatchOpenForPrediction(matchId);
        return predictionRepository.createUserKnockoutPrediction(matchId, userId, scoreTeamA, scoreTeamB, advancingTeamId, hasPenalties);
    }
    public Map<String, Object> updateUserKnockoutPrediction(int predictionId, int matchId, int scoreTeamA, int scoreTeamB, int advancingTeamId, boolean hasPenalties){
        assertMatchOpenForPrediction(matchId);
        return predictionRepository.updateUserKnockoutPrediction(predictionId, scoreTeamA, scoreTeamB, advancingTeamId, hasPenalties);
    }
    public List<Map<String, Object>> getUserKnockoutPredictions(int userId){
        return predictionRepository.getUserKnockoutPredictions(userId);
    }
    public List<Map<String, Object>> getAllKnockoutPredictions(){
        return predictionRepository.getAllKnockoutPredictions();
    }

    /**
     * Validación SERVER-SIDE del bloqueo de predicciones — la del frontend
     * es solo cosmética (deshabilita el botón) y se puede saltar fácilmente
     * con DevTools, una pestaña vieja en caché, o llamando al endpoint
     * directo. Esta es la que realmente protege los datos.
     */
    private void assertMatchOpenForPrediction(int matchId) {
        Map<String, Object> match = matchService.getMatchById(matchId);
        if (match == null || match.isEmpty()) {
            throw new PredictionNotAllowedException("El partido indicado no existe.");
        }

        Object isLockedRaw = match.get("is_locked");
        if (isLockedRaw != null && ((Number) isLockedRaw).intValue() == 1) {
            throw new PredictionNotAllowedException("Este partido ya está bloqueado para predicciones.");
        }

        Object kickoffRaw = match.get("kickoff_time");
        if (kickoffRaw != null) {
            try {
                Instant kickoff = Instant.parse(kickoffRaw.toString());
                Instant oneHourBefore = kickoff.minusSeconds(3600);
                if (!Instant.now().isBefore(oneHourBefore)) {
                    throw new PredictionNotAllowedException(
                        "Ya no puedes predecir este partido: se bloqueó 1 hora antes del inicio."
                    );
                }
            } catch (java.time.format.DateTimeParseException ex) {
                // Si el formato de fecha no es parseable, no bloqueamos por esto
                // (evita falsos bloqueos por datos mal formados), pero el flag
                // is_locked sigue siendo la red de seguridad principal.
            }
        }
    }
}
