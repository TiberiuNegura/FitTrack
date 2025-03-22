package centurionii.UserService.repo;

import centurionii.UserService.entity.WeightUpdate;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface WeightUpdateRepository extends CrudRepository<WeightUpdate, Long> {
    Optional<WeightUpdate> getWeightUpdateByMonthAndDayAndYearAndUserId(int month, int day, int year, long userId);

    List<WeightUpdate> getAllByUserId(long userId);
}
