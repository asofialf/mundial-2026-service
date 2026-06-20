package mundial.predictor.apuestas.services;

import mundial.predictor.apuestas.repository.IPredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PredictionService {
    @Autowired
    private IPredictionRepository predictionRepository;

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
        return predictionRepository.createUserKnockoutPrediction(matchId, userId, scoreTeamA, scoreTeamB, advancingTeamId, hasPenalties);
    }
    public Map<String, Object> updateUserKnockoutPrediction(int predictionId, int scoreTeamA, int scoreTeamB, int advancingTeamId, boolean hasPenalties){
        return predictionRepository.updateUserKnockoutPrediction(predictionId, scoreTeamA, scoreTeamB, advancingTeamId, hasPenalties);
    }
    public List<Map<String, Object>> getUserKnockoutPredictions(int userId){
        return predictionRepository.getUserKnockoutPredictions(userId);
    }
    public List<Map<String, Object>> getAllKnockoutPredictions(){
        return predictionRepository.getAllKnockoutPredictions();
    }
}
