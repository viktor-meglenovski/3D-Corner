package wp.threedcorner.web;

import com.sun.istack.Nullable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.model.Project;
import wp.threedcorner.model.dto.LikeProjectDto;
import wp.threedcorner.service.CommentService;
import wp.threedcorner.service.ProjectService;
import wp.threedcorner.service.SoftwareService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private final SoftwareService softwareService;
    private final CommentService commentService;

    public ProjectController(ProjectService projectService, SoftwareService softwareService, CommentService commentService) {
        this.projectService = projectService;
        this.softwareService = softwareService;
        this.commentService = commentService;
    }

    @GetMapping("/create")
    public String getCreateProjectPage(Model model) {
        model.addAttribute("isEdit",false);
        model.addAttribute("software",softwareService.findAll());
        model.addAttribute("bodyContent", "project/add-edit-project");
        return "master-template";
    }
    @GetMapping("/view/{id}")
    public String viewProject(Principal principal, Model model, @PathVariable Long id){
        model.addAttribute("project",projectService.findById(id));
        model.addAttribute("isLiked",projectService.checkIfLikedByUser(principal.getName(),id));
        model.addAttribute("comments",projectService.getComments(id));
        model.addAttribute("bodyContent", "project/view-project");
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
    public String editProject(Principal principal, @PathVariable Long id, Model model){
        Project toEdit=projectService.getEditProject(principal.getName(),id);
        model.addAttribute("existing",toEdit);
        model.addAttribute("isEdit",true);
        model.addAttribute("software",softwareService.findAll());
        model.addAttribute("bodyContent", "project/add-edit-project");
        return "master-template";
    }
    @PostMapping("/edit/{id}")
    public String editProject(Model model,
                              Principal principal,
                              @PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam String description,
                              @RequestParam List<Long> software,
                              @RequestParam("main-image") MultipartFile mainImage,
                              @RequestParam("other-images") List<MultipartFile> images){
        projectService.editProject(id,principal.getName(),name,description,mainImage,images,software);
        return "redirect:/project/view/"+id;
    }
    @GetMapping(value = "/like/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody LikeProjectDto likeProject(Principal principal, @PathVariable Long id){
        boolean isLikedNow=projectService.likeProject(id,principal.getName());
        int likes=projectService.getNumberOfLikes(id);
        LikeProjectDto result=new LikeProjectDto(isLikedNow,likes);
        return result;
    }
    @GetMapping(value = "/deleteimage", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Boolean deleteImage(Principal principal, @RequestParam Long projectId, @RequestParam Long imageId){
        return projectService.deleteImage(principal.getName(),projectId,imageId);
    }
    @GetMapping("/likes/{id}") public String getLikes(Model model, @PathVariable Long id){
        model.addAttribute("likes",projectService.getLikes(id));
        return "project/view-likes";
    }
    @PostMapping("/comment/{id}")
    public String commentProject(Principal principal,
                                 @PathVariable Long id,
                                 @RequestParam String comment){
        projectService.commentProject(id,principal.getName(),comment);
        return "redirect:/project/view/"+id;
    }
    @GetMapping("/comment/delete")
    public String deleteComment(Principal principal,
                                @RequestParam Long id,
                                @RequestParam Long projectId){
        commentService.deleteComment(principal.getName(),id);
        return "redirect:/project/view/"+projectId;
    }
}
