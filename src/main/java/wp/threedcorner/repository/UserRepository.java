package wp.threedcorner.repository;

import wp.threedcorner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByUsername(String username);

//    UserProjection findByRole(Role role);
//
//    @Query("select u.username, u.name, u.surname from User u")
//    List<UserProjection> takeUsernameAndNameAndSurnameByProjection();
}
