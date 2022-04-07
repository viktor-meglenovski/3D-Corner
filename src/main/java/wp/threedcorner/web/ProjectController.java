package wp.threedcorner.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.model.Software;
import wp.threedcorner.service.ProjectService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/create")
    public String getCreateProjectPage(Model model) {
        //da se dodadat softverite
        model.addAttribute("bodyContent", "home");
        return "master-template";
    }
    @PostMapping("/create")
    public String createProject(Model model,
                                Principal principal,
                                @RequestParam String name,
                                @RequestParam String description,
                                @RequestParam List<Software> software,
                                @RequestParam MultipartFile mainImage,
                                @RequestParam List<MultipartFile> images) {
        projectService.createProject(name,description,principal.getName(),mainImage,images,software);
        model.addAttribute("bodyContent", "home");
        return "master-template";
    }
    @GetMapping("/delete")
    public String deleteProject(Principal principal,
                                @RequestParam Long projectId){
        projectService.deleteProject(projectId,principal.getName());
        //todo
        return "master-template";
    }
    @GetMapping("/edit")
    public String editProject(){
        //todo
        return "master-template";
    }
    @GetMapping("/like")
    public int likeProject(Principal principal,
                              @RequestParam Long projectId){
        //ajax povik da bidi ova, da go vraka samo noviot broj na lajkovi ili site koi lajknale?
        projectService.likeProject(projectId,principal.getName());
        return projectService.getNumberOfLikes(projectId);
    }
    @PostMapping("/comment")
    public String commentProject(Principal principal,
                                 @RequestParam Long projectId,
                                 @RequestParam String comment){
        projectService.commentProject(projectId,principal.getName(),comment);
        return "master-template";
    }
}
