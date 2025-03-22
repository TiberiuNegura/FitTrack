package centurionii.auth.controller;

import centurionii.auth.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> reqBody) {
        try {
            String firstName = (String) reqBody.get("firstName");
            String lastName = (String) reqBody.get("lastName");
            String password = (String) reqBody.get("password");
            Float bodyWeight = reqBody.get("bodyWeight") != null ? ((Number) reqBody.get("bodyWeight")).floatValue() : null;
            Float height = reqBody.get("height") != null ? ((Number) reqBody.get("height")).floatValue() : null;
            Integer age = reqBody.get("age") != null ? ((Number) reqBody.get("age")).intValue() : null;

            return authService.registerAndUpdateWeight(firstName, lastName, password, bodyWeight, height, age);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> reqBody) {
        try {
            String firstName = (String) reqBody.get("firstName");
            String lastName = (String) reqBody.get("lastName");
            String password = (String) reqBody.get("password");

            return authService.getLoggedUser(firstName, lastName, password);

        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
