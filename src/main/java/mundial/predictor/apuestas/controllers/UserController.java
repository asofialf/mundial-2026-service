package mundial.predictor.apuestas.controllers;

import lombok.extern.slf4j.Slf4j;
import mundial.predictor.apuestas.repository.IUserRepository;
import mundial.predictor.apuestas.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     ** POST /api/user/create-user
     **/
    @PostMapping("/create-user")
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String email, @RequestParam String password) {
        try {
            if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                Map<String, Object> errorResponse = Map.of(
                        "status", "ERROR",
                        "message", "El correo electrónico no es válido."
                );
                return ResponseEntity.badRequest().body(errorResponse);
            }

            if (password == null || !password.matches("^(?=.*\\d).{8,}$")) {
                Map<String, Object> errorResponse = Map.of(
                        "status", "ERROR",
                        "message", "La contraseña debe tener al menos 8 caracteres y contener al menos un número."
                );
                return ResponseEntity.badRequest().body(errorResponse);
            }

            Map<String, Object> newUser = userService.createUser(email, password);
            return ResponseEntity.ok(newUser);

        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                    "status", "ERROR",
                    "message", "Error inesperado al crear el usuario: " + e.getMessage()
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }


    /**
     ** POST /api/user/create-user-profile
     **/
    @PostMapping("/create-user-profile")
    public ResponseEntity<Map<String, Object>> createProfile(@RequestParam int userId,
                                                          @RequestParam String name,
                                                             @RequestParam String alias,
                                                             @RequestParam String icon) {
        try {
            Map<String, Object> newProfile = userService.createProfile(userId, name, alias, icon);
            return ResponseEntity.ok(newProfile);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     ** GER /api/user/all-users
     **/
    @GetMapping("/all-users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        try {
            List<Map<String, Object>> users = userService.getUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     ** GER /api/user/all-users
     **/
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable int userId) {
        try {
            Map<String, Object> user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error en la base de datos: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + ex.getMessage(), ex);
        }
    }

    /**
     ** GER /api/user/get-user-for-login
     **/
    @GetMapping("/get-user-for-login")
    public ResponseEntity<Map<String, Object>> getUserForLogin(@Param("loginType") int loginType, @Param("loginValue") String loginValue) {
        try {
            Map<String, Object> userLogin = userService.getUserForLogin(loginType, loginValue);
            return ResponseEntity.ok(userLogin);
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error en la base de datos: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + ex.getMessage(), ex);
        }
    }
}
