package centurionii.auth.service;

import centurionii.auth.clients.WeightUpdateServiceClient;
import centurionii.auth.entity.AppUser;
import centurionii.auth.repo.AuthRepository;
import centurionii.auth.utils.JwtUtil;
import centurionii.auth.utils.dto.WeightUpdateRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final WeightUpdateServiceClient weightUpdateServiceClient;

    @Autowired
    AuthService(AuthRepository authRepository, WeightUpdateServiceClient weightUpdateServiceClient) {
        this.authRepository = authRepository;
        this.weightUpdateServiceClient = weightUpdateServiceClient;
    }

    public Optional<AppUser> save(AppUser appUser) {
        try {
            return Optional.of(authRepository.save(appUser));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Transactional
    public ResponseEntity<?> registerAndUpdateWeight(String firstName, String lastName, String password,
                                                     Float bodyWeight, Float height, Integer age) {

        if (firstName == null || lastName == null || password == null || bodyWeight == null || height == null || age == null) {
            return new ResponseEntity<>("Missing required fields", HttpStatus.BAD_REQUEST);
        }

        if (findUser(firstName, lastName).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "User already exists"));
        }

        String hashedPassword = hashPassword(password);

        AppUser appUser = new AppUser(firstName, lastName, hashedPassword, bodyWeight, height, age);

        Optional<AppUser> saveResult = save(appUser);

        if (saveResult.isEmpty()) {
            return new ResponseEntity<>("Failed to save user", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        LocalDate currentDate = LocalDate.now();

        int currentDay = currentDate.getDayOfMonth();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        WeightUpdateRequest request = new WeightUpdateRequest(
                appUser.getId(),
                appUser.getBodyWeight(),
                currentMonth, currentDay, currentYear
        );

        this.weightUpdateServiceClient.createInitialWeight(request);

        String jwt = JwtUtil.generateToken(appUser.getId());
        System.out.println(jwt);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);

        return ResponseEntity.ok(response);
    }

    public Optional<AppUser> findUser(String firstName, String lastName) {
        return authRepository.findUserByFirstNameAndLastName(firstName, lastName);
    }

    public ResponseEntity<?> getLoggedUser(String firstName, String lastName, String password) {
        if (firstName == null || lastName == null || password == null) {
            return new ResponseEntity<>("Missing required fields", HttpStatus.BAD_REQUEST);
        }

        String hashedPassword = hashPassword(password);

        Optional<AppUser> result = authRepository.findUserByFirstNameAndLastNameAndPassword(firstName, lastName, hashedPassword);

        if (result.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        String jwt = JwtUtil.generateToken(result.get().getId());

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);

        return ResponseEntity.ok(response);
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest hasher = MessageDigest.getInstance("SHA-256");

            byte[] hashedPassword = hasher.digest(password.getBytes());

            return Hex.encodeHexString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing the password" + e.getMessage());
        }
    }
}
