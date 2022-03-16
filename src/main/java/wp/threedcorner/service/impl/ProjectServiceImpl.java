package wp.threedcorner.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.config.Constants;
import wp.threedcorner.model.*;
import wp.threedcorner.model.exceptions.NoAuthorityException;
import wp.threedcorner.model.exceptions.ProjectDoesNotExistException;
import wp.threedcorner.model.exceptions.UserDoesNotExistException;
import wp.threedcorner.repository.CommentRepository;
import wp.threedcorner.repository.ImageRepository;
import wp.threedcorner.repository.ProjectRepository;
import wp.threedcorner.repository.UserRepository;
import wp.threedcorner.service.ProjectService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;

    public ProjectServiceImpl(UserRepository userRepository, ProjectRepository projectRepository, ImageRepository imageRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.imageRepository = imageRepository;
        this.commentRepository = commentRepository;
    }


    @Override
    public void createProject(String name, String description, String username, MultipartFile mainImage, List<MultipartFile> images,List<Software> software) {
        User author=userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        Project p=new Project();
        p.setName(name);
        p.setDescription(description);
        p.setAuthor(author);
        p.setLikes(new ArrayList<>());
        p.setComments(new ArrayList<>());
        p.setCreated(LocalDateTime.now());
        //ova dali software samo ID ili celi objekti gi dava od front na back da se proveri
        p.setSoftware(software);

        //create a new folder for the project
        File f=new File(Constants.rootPath+username+"/"+p.getId());
        f.mkdir();

        //save main image
        try {
            byte[] bytes = new byte[0];
            bytes = mainImage.getBytes();
            Path path = Paths.get(Constants.rootPath +username+"/"+p.getId()+"/"+ mainImage.getOriginalFilename());
            Files.write(path, bytes);
            Image image=new Image(path.toString());
            imageRepository.save(image);
            p.setMainImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //save other images
        for(MultipartFile file: images){
            try {
                byte[] bytes = new byte[0];
                bytes = file.getBytes();
                Path path = Paths.get(Constants.rootPath +username+"/"+p.getId()+"/"+ file.getOriginalFilename());
                Files.write(path, bytes);
                Image image=new Image(path.toString());
                imageRepository.save(image);
                p.getImages().add(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //save project
        projectRepository.save(p);
    }

    @Override
    public void editProject(Long projectId, String name, String description, Image mainImage, List<Image> images) {
        //todo - probably najkompliciran metod od site
    }

    @Override
    public void deleteProject(Long projectId,String username) {
        Project p=projectRepository.findById(projectId).orElseThrow(ProjectDoesNotExistException::new);
        if(p.getAuthor().getName().equals(username)){
            projectRepository.delete(p);
        }else throw new NoAuthorityException();
    }

    @Override
    public void likeProject(Long projectId, String username) {
        Project p=projectRepository.findById(projectId).orElseThrow(ProjectDoesNotExistException::new);
        User u=userRepository.getById(username);
        if(p.getLikes().contains(u))
            p.getLikes().remove(u);
        else
            p.getLikes().add(u);
        projectRepository.save(p);
    }

    @Override
    public void commentProject(Long projectId, String username, String comment) {
        User u= userRepository.getById(username);
        Project p=projectRepository.findById(projectId).orElseThrow(ProjectDoesNotExistException::new);
        Comment c=new Comment(comment,LocalDateTime.now(),p,u);
        commentRepository.save(c);
    }


}
