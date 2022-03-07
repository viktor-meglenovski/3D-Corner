package wp.threedcorner.service;

import wp.threedcorner.model.User;
import wp.threedcorner.model.enumerations.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User register(String username, String password, String repeatPassword, String name, String surname, Role role);
}
