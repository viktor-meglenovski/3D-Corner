package wp.threedcorner.web;

import com.sun.istack.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.model.Software;
import wp.threedcorner.service.ProjectService;
import wp.threedcorner.service.SoftwareService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private final SoftwareService softwareService;

    public ProjectController(ProjectService projectService, SoftwareService softwareService) {
        this.projectService = projectService;
        this.softwareService = softwareService;
    }

    @GetMapping("/create")
    public String getCreateProjectPage(Model model) {
        model.addAttribute("isEdit",false);
        model.addAttribute("software",softwareService.findAll());
        model.addAttribute("bodyContent", "project/add-edit-project");
        return "master-template";
    }
    @PostMapping("/create")
    public String createProject(Principal principal,
                                @RequestParam String name,
                                @RequestParam String description,
                                @RequestParam List<Long> software,
                                @RequestParam("main-image") MultipartFile mainImage,
                                @RequestParam("other-images") @Nullable List<MultipartFile> images) {
        projectService.createProject(name,description,principal.getName(),mainImage,images,software);
        return "redirect:/profile";
    }
    @GetMapping("/delete/{id}")
    public String deleteProject(Principal principal,
                                @PathVariable Long id){
        projectService.deleteProject(id,principal.getName());
        return "redirect:/profile";
    }
    @GetMapping("/edit/{id}")
    public String editProject(Model model,
                              Principal principal,
                              @PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam String description,
                              @RequestParam List<Long> software,
                              @RequestParam("main-image") MultipartFile mainImage,
                              @RequestParam("other-images") List<MultipartFile> images){
        projectService.editProject(id,principal.getName(),name,description,mainImage,images,software);
        model.addAttribute("bodyContent", "project/view-project");
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
