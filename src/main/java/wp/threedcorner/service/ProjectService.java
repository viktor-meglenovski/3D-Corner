package wp.threedcorner.service;

import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.model.*;

import java.util.List;
import java.util.Set;

public interface ProjectService {
    public void createProject(String name, String description, String username, MultipartFile mainImage, List<MultipartFile> images, List<Long> software);
    public Project getEditProject(String username, Long id);
    public void editProject(Long projectId, String username,String name, String description, MultipartFile mainImage, List<MultipartFile> images,List<Long> software);
    public void deleteProject(Long projectId,String username);
    public boolean likeProject(Long projectId, String username);
    public void commentProject(Long projectId,String username, String comment);
    public int getNumberOfLikes(Long projectId);
    public Boolean deleteImage(String username, Long projectId, Long imageId);
    public List<Project> findAllProjectsForUser(User user);
    public Project findById(Long id);
    public Set<User> getLikes(Long id);
    public boolean checkIfLikedByUser(String username, Long id);
    public List<Comment> getComments(Long id);
    public void deleteSotwareFromProjects(Software software);
}
