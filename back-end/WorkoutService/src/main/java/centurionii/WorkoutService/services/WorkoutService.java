package centurionii.WorkoutService.services;

import centurionii.WorkoutService.entities.Workout;
import centurionii.WorkoutService.repos.WorkoutRepository;
import centurionii.WorkoutService.utils.JwtUtil;
import centurionii.WorkoutService.utils.ObjectMapper;
import centurionii.WorkoutService.utils.ValidationHelpers.MuscleGroups;
import centurionii.WorkoutService.utils.WorkoutInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public ResponseEntity<?> saveWorkout(Map<String, Object> body, String authHeader) {
        String exerciseName = (String) body.get("exerciseName");
        String muscleGroup = (String) body.get("muscleGroup");
        String date = (String) body.get("date");
        String sets = (String) body.get("sets");

        if (!MuscleGroups.isGroupPresent(muscleGroup)) {
            return new ResponseEntity<>("Invalid muscle group", HttpStatus.BAD_REQUEST);
        }

        int[] dates = Arrays.stream(date.split("-"))
                .mapToInt(Integer::parseInt)
                .toArray();

        long userId = getUserIdFromJwt(authHeader);

        if (userId == -1) {
            return new ResponseEntity<>("User session id invalid", HttpStatus.NOT_FOUND);
        }

        Workout workout = new Workout(userId, muscleGroup, exerciseName, dates[0], dates[1], dates[2], sets);

        return ResponseEntity.ok(ObjectMapper.objectToMap(workoutRepository.save(workout)));
    }

    public ResponseEntity<?> getAllWorkoutsByDate(String date, String authHeader) {
        int[] dates = Arrays.stream(date.split("-"))
                .mapToInt(Integer::parseInt)
                .toArray();

        long userId = getUserIdFromJwt(authHeader);

        if (userId == -1) {
            return new ResponseEntity<>("User session id invalid", HttpStatus.NOT_FOUND);
        }

        Optional<List<Workout>> foundWorkouts = workoutRepository.findAllByDayAndMonthAndYearAndUserId(dates[0], dates[1], dates[2], userId);

        if (foundWorkouts.isEmpty()) {
            return new ResponseEntity<>("No workouts on this date", HttpStatus.NOT_FOUND);
        }

        Map<String, Object> workouts = new HashMap<>();

        List<Workout> workoutsList = foundWorkouts.get();

        for (int index = 0; index < workoutsList.size(); index++) {
            workouts.put(String.valueOf(index), ObjectMapper.objectToMap(workoutsList.get(index)));
        }

        return ResponseEntity.ok(workouts);
    }

    public Optional<Workout> findWorkoutById(Long workoutId) {
        try {
            return workoutRepository.findById(workoutId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public ResponseEntity<?> updateWorkout(Map<String, Object> body, String authHeader) {
        Long workoutId = body.get("id") != null ? ((Number) body.get("id")).longValue() : null;

        WorkoutInfo workoutInfo = ObjectMapper.mapReqBodyToWorkoutInfo(body);

        long userId = getUserIdFromJwt(authHeader);

        if (workoutId == null) {
            return new ResponseEntity<>("Null workout id from client", HttpStatus.BAD_REQUEST);
        }

        if (userId == -1) {
            return new ResponseEntity<>("User session id invalid", HttpStatus.NOT_FOUND);
        }

        Optional<Workout> existingWorkout = workoutRepository.findById(workoutId);

        if (existingWorkout.isEmpty() || existingWorkout.get().getUserId() != userId) {
            return new ResponseEntity<>("Workout not found or doesn't belong to the current user", HttpStatus.NOT_FOUND);
        }

        Workout workoutToUpdate = existingWorkout.get();
        workoutToUpdate.setExerciseName(workoutInfo.exerciseName());
        workoutToUpdate.setMuscleGroup(workoutInfo.muscleGroup());
        workoutToUpdate.setDate(workoutInfo.date());
        workoutToUpdate.setSets(workoutInfo.sets());

        Optional<Workout> updatedWorkout = save(workoutToUpdate);

        if (updatedWorkout.isEmpty()) {
            return new ResponseEntity<>("Couldn't update workout", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(ObjectMapper.objectToMap(updatedWorkout.get()));
    }

    public Optional<Workout> findWorkoutByIdAndUserId(Long workoutId, Long userId) {
        try {
            return workoutRepository.findByIdAndUserId(workoutId, userId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public ResponseEntity<?> deleteWorkoutById(String id) {
        Long workoutId = id != null ? Long.parseLong(id) : null;

        if (workoutId == null) {
            return new ResponseEntity<>("Bad workout id from client", HttpStatus.BAD_REQUEST);
        }
        workoutRepository.deleteById(workoutId);

        return ResponseEntity.ok(Map.of());
    }

    public Optional<Workout> save(Workout workout) {
        try {
            return Optional.of(workoutRepository.save(workout));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public long getUserIdFromJwt(String authHeader) {
        String token = authHeader.substring(7);
        Claims claims = JwtUtil.validateToken(token);

        String userId = claims.getSubject();

        return Long.parseLong(userId);
    }
}
