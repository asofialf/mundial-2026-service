package mundial.predictor.apuestas.services;

import mundial.predictor.apuestas.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;

    public Map<String, Object> createUser(String email, String password){
        return userRepository.createUser(email, password);
    }
    public Map<String, Object> createProfile(int userId, String name, String alias, String icon){
        return userRepository.createProfile(userId, name, alias, icon);
    }
    public List<Map<String, Object>> getUsers(){
        return userRepository.getUsers();
    }
    public Map<String, Object> getUserById(int userId){
        return userRepository.getUserById(userId);
    }
    public Map<String, Object> getUserForLogin(int loginType, String loginValue){
        return userRepository.getUserForLogin(loginType, loginValue);
    }
    public Map<String, Object> changePassword(int userId, String oldPassword, String newPassword){
        return userRepository.changePassword(userId, oldPassword, newPassword);
    }
    public int getGroupPoints(int userId) {
        return userRepository.getGroupPoints(userId);
    }
}
