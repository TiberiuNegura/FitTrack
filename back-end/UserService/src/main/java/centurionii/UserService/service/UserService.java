package centurionii.UserService.service;

import centurionii.UserService.dto.WeightUpdateRequest;
import centurionii.UserService.entity.AppUser;
import centurionii.UserService.entity.WeightUpdate;
import centurionii.UserService.repo.UserRepository;
import centurionii.UserService.utils.JwtUtil;
import centurionii.UserService.utils.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final WeightUpdateService weightUpdateService;

    @Autowired
    UserService(UserRepository userRepository, WeightUpdateService weightUpdateService) {
        this.userRepository = userRepository;
        this.weightUpdateService = weightUpdateService;
    }

    public Optional<AppUser> save(AppUser appUser) {
        try {
            return Optional.of(userRepository.save(appUser));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<AppUser> findUser(String firstName, String lastName) {
        return userRepository.findUserByFirstNameAndLastName(firstName, lastName);
    }

    public ResponseEntity<?> findUserById(String authHeader) {
        long userId = JwtUtil.getUserIdFromJwt(authHeader);

        if (userId == -1) {
            return new ResponseEntity<>("Session id of current user not found", HttpStatus.NOT_FOUND);
        }

        Optional<AppUser> user = userRepository.findById((Long) userId);

        if (user.isPresent()) {
            return ResponseEntity.ok(ObjectMapper.objectToMap(user.get()));
        }

        return new ResponseEntity<>("Can't find current user", HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<?> saveNewWeight(Float newWeight, String date, String authHeader) {
        long userId = JwtUtil.getUserIdFromJwt(authHeader);

        int[] dates = Arrays.stream(date.split("-"))
                .mapToInt(Integer::parseInt)
                .toArray();

        if (newWeight == null || date.isEmpty()) {
            return new ResponseEntity<>("Missing required fields", HttpStatus.BAD_REQUEST);
        }

        if (userId == -1) {
            return new ResponseEntity<>("Session id of current user not found", HttpStatus.NOT_FOUND);
        }

        Optional<AppUser> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return new ResponseEntity<>("Can't find current user", HttpStatus.NOT_FOUND);
        }

        AppUser existingUser = user.get();
        existingUser.setBodyWeight(newWeight);

        WeightUpdate update = new WeightUpdate(existingUser.getId(), dates[1], dates[0], dates[2], newWeight);

        Optional<WeightUpdate> savedWeight = weightUpdateService.save(update);

        Optional<AppUser> savedUser = save(existingUser);

        if (savedUser.isEmpty()) {
            return new ResponseEntity<>("Something went wrong when updating the user with the new weight", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (savedWeight.isEmpty()) {
            return new ResponseEntity<>("Failed to save weight update", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(ObjectMapper.objectToMap(savedWeight.get()));

    }

    public ResponseEntity<?> createInitialWeightUpdate(WeightUpdateRequest weightUpdateRequest) {
        Optional<WeightUpdate> result = this.weightUpdateService.save(weightUpdateRequest);

        if(result.isEmpty()) {
            return new ResponseEntity<>("Something went bad when saving the initial weight update", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(ObjectMapper.objectToMap(result.get()));
    }

}
