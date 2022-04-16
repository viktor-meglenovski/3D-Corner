package wp.threedcorner.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.config.Constants;
import wp.threedcorner.model.*;
import wp.threedcorner.model.exceptions.NoAuthorityException;
import wp.threedcorner.model.exceptions.ProjectDoesNotExistException;
import wp.threedcorner.model.exceptions.UserDoesNotExistException;
import wp.threedcorner.repository.*;
import wp.threedcorner.service.ImageService;
import wp.threedcorner.service.ProjectService;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;
    private final SoftwareRepository softwareRepository;
    private final ImageService imageService;

    public ProjectServiceImpl(UserRepository userRepository, ProjectRepository projectRepository, ImageRepository imageRepository, CommentRepository commentRepository, SoftwareRepository softwareRepository, ImageService imageService) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.imageRepository = imageRepository;
        this.commentRepository = commentRepository;
        this.softwareRepository = softwareRepository;
        this.imageService = imageService;
    }


    @Override
    public void createProject(String name, String description, String username, MultipartFile mainImage, List<MultipartFile> images, List<Long> software) {
        User author=userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        Project p=new Project();
        p.setName(name);
        p.setDescription(description);
        p.setAuthor(author);
        p.setLikes(new HashSet<>());
        p.setComments(new ArrayList<>());
        p.setCreated(LocalDateTime.now());
        Set<Software> softwares= new HashSet<>(softwareRepository.findAllById(software));
        p.setSoftware(softwares);
        p.setImages(new HashSet<>());
        projectRepository.save(p);

        //create a new folder for the project
        File f=new File(Constants.userRootPath +username+"/"+p.getId()+"_"+p.getName().replace(" ","_"));
        f.mkdir();
        p.setLocation(Constants.userRootPath+username+"/"+p.getId()+"_"+p.getName());

        //save the main image
        Image mainImg=imageService.saveImage(mainImage,Constants.userRootPath +username+"/"+p.getId()+"_"+p.getName().replace(" ","_")+"/"+ mainImage.getOriginalFilename(),
                "/user_uploads/"+username+"/"+p.getId()+"_"+p.getName().replace(" ","_")+"/"+mainImage.getOriginalFilename());
        p.setMainImage(mainImg);

        //save the other images
        for(MultipartFile file: images){
            if(!file.isEmpty())
            {
                Image otherImg=imageService.saveImage(file,Constants.userRootPath +username+"/"+p.getId()+"_"+p.getName().replace(" ","_")+"/"+ file.getOriginalFilename(),
                        "/user_uploads/"+username+"/"+p.getId()+"_"+p.getName().replace(" ","_")+"/"+file.getOriginalFilename());
                p.getImages().add(otherImg);
            }
        }

        //save project
        projectRepository.save(p);
    }

    @Override
    public Project getEditProject(String username, Long id) {
        Project p=projectRepository.findById(id).orElseThrow(ProjectDoesNotExistException::new);
        if(p.getAuthor().getUsername().equals(username)){
            return p;
        }else throw new NoAuthorityException();
    }

    @Override
    public void editProject(Long projectId, String username,String name, String description, MultipartFile mainImage, List<MultipartFile> images,List<Long> software) {

        Project p=projectRepository.findById(projectId).orElseThrow(ProjectDoesNotExistException::new);
        User u=userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        if(p.getAuthor()!=u)
            throw new NoAuthorityException();
        p.setName(name);
        p.setDescription(description);
        p.setCreated(LocalDateTime.now());
        Set<Software> softwares= new HashSet<>(softwareRepository.findAllById(software));
        p.setSoftware(softwares);

        //save the main image
        if(!mainImage.isEmpty())
        {
            imageService.deletePhysicalImage(p.getMainImage().getLocation());
            Image mainImg=imageService.saveImage(mainImage,Constants.userRootPath +username+"/"+p.getId()+"_"+p.getName().replace(" ","_")+"/"+ mainImage.getOriginalFilename(),
                    "/user_uploads/"+username+"/"+p.getId()+"_"+p.getName().replace(" ","_")+"/"+mainImage.getOriginalFilename());
            p.setMainImage(mainImg);
        }

        //save the other images
        for(MultipartFile file: images){
            if(!file.isEmpty()){
                Image otherImg=imageService.saveImage(file,Constants.userRootPath +username+"/"+p.getId()+"_"+p.getName().replace(" ","_")+"/"+ file.getOriginalFilename(),
                        "/user_uploads/"+username+"/"+p.getId()+"_"+p.getName().replace(" ","_")+"/"+file.getOriginalFilename());
                p.getImages().add(otherImg);
            }
        }

        //save project
        projectRepository.save(p);
    }

    @Override
    public void deleteProject(Long projectId,String username) {
        Project p=projectRepository.findById(projectId).orElseThrow(ProjectDoesNotExistException::new);
        if(p.getAuthor().getUsername().equals(username)){

            Image mainImage=p.getMainImage();

            //delete the images
            for(Image image : p.getImages())
                imageService.deleteImageFromDatabase(image);

            //delete the folder
            File folder=new File(p.getLocation());
            if(folder.exists())
            {
                for(File f : folder.listFiles())
                    f.delete();
                folder.delete();
            }
            projectRepository.delete(p);
            imageService.deleteImageFromDatabase(mainImage);
        }else throw new NoAuthorityException();
    }

    @Override
    public boolean likeProject(Long projectId, String username) {
        Project p=projectRepository.findById(projectId).orElseThrow(ProjectDoesNotExistException::new);
        User u=userRepository.getById(username);
        if(p.getLikes().contains(u)){
            p.getLikes().remove(u);
            projectRepository.save(p);
            return false;
        }
        else{
            p.getLikes().add(u);
            projectRepository.save(p);
            return true;
        }
    }

    @Override
    public void commentProject(Long projectId, String username, String comment) {
        User u= userRepository.getById(username);
        Project p=projectRepository.findById(projectId).orElseThrow(ProjectDoesNotExistException::new);
        Comment c=new Comment(comment,LocalDateTime.now(),p,u);
        commentRepository.save(c);
    }

    @Override
    public int getNumberOfLikes(Long projectId) {
        Project p=projectRepository.findById(projectId).orElseThrow(ProjectDoesNotExistException::new);
        return p.getLikes().size();
    }

    @Override
    public Boolean deleteImage(String username, Long projectId, Long imageId) {
        User u=userRepository.getById(username);
        Project p=projectRepository.findById(projectId).orElseThrow(ProjectDoesNotExistException::new);
        if(!u.equals(p.getAuthor()))
            return false;
        Image i=imageRepository.getById(imageId);
        imageService.deletePhysicalImage(i.getLocation());
        if(p.getImages().contains(i)){
            p.getImages().remove(i);
            imageService.deleteImageFromDatabase(i);
        }
        projectRepository.save(p);
        return true;
    }

    @Override
    public List<Project> findAllProjectsForUser(User user) {
        return projectRepository.findAllByAuthor(user);
    }

    @Override
    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow(ProjectDoesNotExistException::new);
    }

    @Override
    public Set<User> getLikes(Long id) {
        return projectRepository.findById(id).orElseThrow(ProjectDoesNotExistException::new).getLikes();
    }

    @Override
    public boolean checkIfLikedByUser(String username, Long id) {
        Project p=projectRepository.findById(id).orElseThrow(ProjectDoesNotExistException::new);
        User u=userRepository.getById(username);
        return p.getLikes().contains(u);
    }

    @Override
    public List<Comment> getComments(Long id) {
        Project p=projectRepository.findById(id).orElseThrow(ProjectDoesNotExistException::new);
        return commentRepository.findAllByProject(p);
    }

    @Override
    public void deleteSotwareFromProjects(Software software) {
        projectRepository.findAllBySoftware(software).stream()
                .forEach(x->{
                    x.getSoftware().remove(software);
                    projectRepository.save(x);});
    }


}
