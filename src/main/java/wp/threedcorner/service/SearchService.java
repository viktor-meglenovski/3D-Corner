package wp.threedcorner.service;

import wp.threedcorner.model.Project;
import wp.threedcorner.model.User;

import java.util.List;

public interface SearchService {
    List<User> searchUsers(String text);
    List<Project> searchProjects(String text);
    List<Project> sortByLikes();
}
