package wp.threedcorner.service.impl;

import wp.threedcorner.model.User;
import wp.threedcorner.model.exceptions.InvalidArgumentsException;
import wp.threedcorner.model.exceptions.InvalidUserCredentialsException;
import org.springframework.stereotype.Service;
import wp.threedcorner.repository.UserRepository;
import wp.threedcorner.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if (username==null || username.isEmpty() || password==null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        return userRepository.findByUsernameAndPassword(username,
                password).orElseThrow(InvalidUserCredentialsException::new);
    }

}
