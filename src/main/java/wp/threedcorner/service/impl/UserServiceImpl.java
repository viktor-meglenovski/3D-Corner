package wp.threedcorner.service.impl;

import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.model.Image;
import wp.threedcorner.model.Project;
import wp.threedcorner.model.User;
import wp.threedcorner.model.enumerations.Role;
import wp.threedcorner.model.exceptions.InvalidUsernameOrPasswordException;
import wp.threedcorner.model.exceptions.PasswordsDoNotMatchException;
import wp.threedcorner.model.exceptions.UserDoesNotExistException;
import wp.threedcorner.model.exceptions.UsernameAlreadyExistsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wp.threedcorner.repository.UserRepository;
import wp.threedcorner.service.ImageService;
import wp.threedcorner.service.ProjectService;
import wp.threedcorner.service.UserService;
import wp.threedcorner.config.Constants;

import java.io.File;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final ProjectService projectService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ImageService imageService, ProjectService projectService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.projectService = projectService;
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

        User user = new User(username,passwordEncoder.encode(password),name,surname,userRole,imageService.getDefaultUserImage());
        File f=new File(Constants.userRootPath +username);
        f.mkdir();
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
    }

    @Override
    public User editProfile(String username, String name, String surname, MultipartFile profilePicture) {
        User u=userRepository.findByUsername(username)
                .orElseThrow(UserDoesNotExistException::new);
        u.setName(name);
        u.setSurname(surname);
        if(!profilePicture.isEmpty()){
            if(u.getProfilePicture().getId()!=1)
                imageService.deletePhysicalImage(u.getProfilePicture().getLocation());
            Image image=imageService.saveImage(profilePicture,
                    Constants.userRootPath +username+"/"+ profilePicture.getOriginalFilename(),
                    "/user_uploads/"+username+"/"+profilePicture.getOriginalFilename());
            u.setProfilePicture(image);
        }

        userRepository.save(u);
        return u;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findByRole(Role.ROLE_USER);
    }

    @Override
    public int totalLikes(String username) {
        User u=findByUsername(username);
        return projectService.findAllProjectsForUser(u).stream().mapToInt(x->x.getLikes().size()).sum();
    }

    @Override
    public void deleteUser(String id) {
        User u=findByUsername(id);
        //delete all projects
        List<Project> projects=projectService.findAllProjectsForUser(u);
        for(Project p : projects)
            projectService.deleteProject(p.getId(),"admin");

        //delete user folder
        File folder=new File("src//main//resources//static//user_uploads//"+u.getUsername());
        if(folder.exists())
        {
            for(File f : folder.listFiles())
                f.delete();
            folder.delete();
        }

        //delete user from database
        userRepository.delete(u);
    }
}
