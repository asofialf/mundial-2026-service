package mundial.predictor.apuestas.repository;

import java.util.List;
import java.util.Map;

public interface IUserRepository {
    Map<String, Object> createUser(String email, String password);
    Map<String, Object> createProfile(int userId, String name, String alias, String icon);
    List<Map<String, Object>> getUsers();
    Map<String, Object> getUserById(int userId);
    Map<String, Object> getUserForLogin(int loginType, String loginValue);
}

