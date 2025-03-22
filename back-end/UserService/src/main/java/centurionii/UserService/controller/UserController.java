package centurionii.UserService.controller;

import centurionii.UserService.dto.WeightUpdateRequest;
import centurionii.UserService.service.UserService;
import centurionii.UserService.service.WeightUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@Controller
public class UserController {
    private final UserService userService;
    private final WeightUpdateService weightUpdateService;

    @Autowired
    public UserController(UserService userService, WeightUpdateService weightUpdateService) {
        this.userService = userService;
        this.weightUpdateService = weightUpdateService;
    }

    @GetMapping("/user/current-user")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            return userService.findUserById(authHeader);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/get_updates")
    public ResponseEntity<?> getWeightUpdates(@RequestHeader("Authorization") String authHeader) {
        try {
            return weightUpdateService.getAllUpdatesByUser(authHeader);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/new_weight")
    public ResponseEntity<?> saveNewWeight(@RequestBody Map<String, Object> reqBody, @RequestHeader("Authorization") String authHeader) {
        try {
            Float newWeight = reqBody.get("weight") != null ? ((Number) reqBody.get("weight")).floatValue() : null;
            String date = (String) reqBody.get("date");

            return userService.saveNewWeight(newWeight, date, authHeader);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/weight-updates/initial")
    public ResponseEntity<?> createInitialWeight(@RequestBody WeightUpdateRequest request) {
        try {
            return userService.createInitialWeightUpdate(request);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
