package wp.threedcorner.service.impl;

import wp.threedcorner.model.User;
import wp.threedcorner.model.enumerations.Role;
import wp.threedcorner.model.exceptions.InvalidUsernameOrPasswordException;
import wp.threedcorner.model.exceptions.PasswordsDoNotMatchException;
import wp.threedcorner.model.exceptions.UsernameAlreadyExistsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wp.threedcorner.repository.UserRepository;
import wp.threedcorner.service.UserService;
import wp.threedcorner.config.Constants;

import java.io.File;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s).orElseThrow(()->new UsernameNotFoundException(s));
    }


    @Override
    public User register(String username, String password, String repeatPassword, String name, String surname, Role userRole) {
        if (username==null || username.isEmpty()  || password==null || password.isEmpty())
            throw new InvalidUsernameOrPasswordException();
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if(this.userRepository.findByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);
        User user = new User(username,passwordEncoder.encode(password),name,surname,userRole);
        File f=new File(Constants.rootPath+username);
        f.mkdir();
        return userRepository.save(user);
    }
}
