package mundial.predictor.apuestas.repository;

import java.util.List;
import java.util.Map;

public interface IPredictionRepository {
    Map<String, Object> createUserGroupPrediction(int userId, int groupId, int first_place_id, int second_place_id);
    List<Map<String, Object>> getUserGroupPrediction(int userId);
    Map<String, Object> updateUserGroupPrediction(int predictionId, int first_place_id, int second_place_id);
    Map<String, Object> deleteUserGroupPrediction(int predictionId);
    List<Map<String, Object>> getUserBestThird(int userId);
    Map<String, Object> createUserBestThird(int userId, int countryId);
}
