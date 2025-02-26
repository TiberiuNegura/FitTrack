package centurionii.UserService.repo;

import centurionii.UserService.entity.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findUserByFirstNameAndLastName(String firstName, String lastName);
    Optional<AppUser> findUserByFirstNameAndLastNameAndPassword(String firstName, String lastName, String password);
}
