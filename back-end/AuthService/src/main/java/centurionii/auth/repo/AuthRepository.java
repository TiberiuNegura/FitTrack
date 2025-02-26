package centurionii.auth.repo;

import centurionii.auth.entity.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findUserByFirstNameAndLastName(String firstName, String lastName);
    Optional<AppUser> findUserByFirstNameAndLastNameAndPassword(String firstName, String lastName, String password);
}
