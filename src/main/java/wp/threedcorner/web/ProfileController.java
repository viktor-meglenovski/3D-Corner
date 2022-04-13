package wp.threedcorner.web;

import com.sun.istack.Nullable;
import org.h2.engine.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.model.User;
import wp.threedcorner.service.ProjectService;
import wp.threedcorner.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;
    private final ProjectService projectService;

    public ProfileController(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }

    @GetMapping
    public String getMyProfile(Principal principal,Model model){
        User u=userService.findByUsername(principal.getName());
        model.addAttribute("user",u);
        model.addAttribute("projects",projectService.findAllProjectsForUser(u));
        model.addAttribute("likes",userService.totalLikes(principal.getName()));
        model.addAttribute("bodyContent","profile/my-profile");
        return "master-template";
    }
    @GetMapping("/view/{id}")
    public String viewProfile(Principal principal, Model model, @PathVariable String id)
    {
        User u=userService.findByUsername(principal.getName());
        if(u.getUsername().equals(id))
            return "redirect:/profile";
        model.addAttribute("user",u);
        model.addAttribute("projects",projectService.findAllProjectsForUser(u));
        model.addAttribute("likes",userService.totalLikes(id));
        model.addAttribute("bodyContent","profile/view-profile");
        return "master-template";
    }
    @GetMapping("/edit")
    public String getEditProfile(Principal principal, Model model){
        User u=userService.findByUsername(principal.getName());
        model.addAttribute("user",u);
        model.addAttribute("bodyContent","profile/edit-profile");
        return "master-template";
    }
    @PostMapping("/edit")
    public String saveChanges(Principal principal,
                              @RequestParam String name,
                              @RequestParam String surname,
                              @RequestParam("image-upload") @Nullable MultipartFile profilePicture)
    {
        userService.editProfile(principal.getName(),name,surname,profilePicture);
        return "redirect:/profile";
    }
}
