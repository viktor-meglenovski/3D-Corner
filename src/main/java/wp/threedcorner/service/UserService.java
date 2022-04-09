package wp.threedcorner.service;

import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.model.User;
import wp.threedcorner.model.enumerations.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User register(String username, String password, String repeatPassword, String name, String surname, Role role);
    User findByUsername(String username);
    User editProfile(String username, String name, String surname, MultipartFile profilePicture);
    List<User> findAllUsers();
}
