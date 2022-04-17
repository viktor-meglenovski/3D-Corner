package wp.threedcorner.service.impl;

import org.springframework.stereotype.Service;
import wp.threedcorner.model.Project;
import wp.threedcorner.model.User;
import wp.threedcorner.repository.ProjectRepository;
import wp.threedcorner.repository.UserRepository;
import wp.threedcorner.service.SearchService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public SearchServiceImpl(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public List<User> searchUsers(String text) {
        if(text.equals(""))
            return new ArrayList<>();
        return userRepository.findAll().stream()
                .filter(x->x.getUsername().contains(text))
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> searchProjects(String text) {
        if(text.equals(""))
            return new ArrayList<>();
        return projectRepository.findAll().stream()
                .filter(x->x.getName().contains(text)|| x.getDescription().contains(text))
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> sortByLikes() {
        return projectRepository.findAll().stream().sorted(Comparator.comparing((Project x)->x.getLikes().size()).reversed()).collect(Collectors.toList());
    }
}
