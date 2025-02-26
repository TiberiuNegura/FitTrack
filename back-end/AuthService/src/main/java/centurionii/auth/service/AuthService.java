package centurionii.auth.service;

import centurionii.auth.entity.AppUser;
import centurionii.auth.repo.AuthRepository;
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
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final WeightUpdateRepository weightUpdateRepository;

    private final WeightUpdateService weightUpdateService;

    @Autowired
    AuthService(AuthRepository authRepository, WeightUpdateRepository weightUpdateRepository, WeightUpdateService weightUpdateService) {
        this.authRepository = authRepository;
        this.weightUpdateRepository = weightUpdateRepository;
        this.weightUpdateService = weightUpdateService;
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
                                                     Float bodyWeight, Float height, Integer age, HttpSession session) {

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

        WeightUpdate firstWeight = new WeightUpdate(appUser, currentMonth, currentDay, currentYear, appUser.getBodyWeight());

        this.weightUpdateRepository.save(firstWeight);

        session.setAttribute("userId", saveResult.get().getId());

        return ResponseEntity.ok(Map.of());
    }

    public Optional<AppUser> findUser(String firstName, String lastName) {
        return authRepository.findUserByFirstNameAndLastName(firstName, lastName);
    }

    public ResponseEntity<?> getLoggedUser(String firstName, String lastName, String password, HttpSession session) {
        if (firstName == null || lastName == null || password == null) {
            return new ResponseEntity<>("Missing required fields", HttpStatus.BAD_REQUEST);
        }

        String hashedPassword = hashPassword(password);

        Optional<AppUser> result = authRepository.findUserByFirstNameAndLastNameAndPassword(firstName, lastName, hashedPassword);

        if (result.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        session.setAttribute("userId", result.get().getId());

        return ResponseEntity.ok(Map.of());
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
