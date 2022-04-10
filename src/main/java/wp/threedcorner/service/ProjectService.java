package wp.threedcorner.service;

import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.model.Image;
import wp.threedcorner.model.Project;
import wp.threedcorner.model.Software;
import wp.threedcorner.model.User;

import java.util.List;

public interface ProjectService {
    public void createProject(String name, String description, String username, MultipartFile mainImage, List<MultipartFile> images, List<Long> software);
    public void editProject(Long projectId, String username,String name, String description, MultipartFile mainImage, List<MultipartFile> images,List<Long> software);
    public void deleteProject(Long projectId,String username);
    public void likeProject(Long projectId, String username);
    public void commentProject(Long projectId,String username, String comment);
    public int getNumberOfLikes(Long projectId);
    public void deleteImage(String username, Long projectId, Long imageId);
    public List<Project> findAllProjectsForUser(User user);

}
