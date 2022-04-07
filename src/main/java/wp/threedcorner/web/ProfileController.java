package wp.threedcorner.web;

import com.sun.istack.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.model.User;
import wp.threedcorner.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getMyProfile(Principal principal,Model model){
        User u=userService.findByUsername(principal.getName());
        model.addAttribute("user",u);
        model.addAttribute("bodyContent","profile/my-profile");
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
