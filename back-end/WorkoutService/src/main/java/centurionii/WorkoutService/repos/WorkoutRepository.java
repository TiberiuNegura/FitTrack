package centurionii.WorkoutService.repos;

import centurionii.WorkoutService.entities.Workout;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends CrudRepository<Workout, Long> {
    Optional<List<Workout>> findAllByDayAndMonthAndYearAndUserId(int day, int month, int year, long userId);

    Optional<Workout> findByIdAndUserId(Long workoutId, Long userId);
}
