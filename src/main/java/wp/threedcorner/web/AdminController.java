package wp.threedcorner.web;

import com.sun.istack.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.service.SoftwareService;
import wp.threedcorner.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final SoftwareService softwareService;
    private final UserService userService;

    public AdminController(SoftwareService softwareService, UserService userService) {
        this.softwareService = softwareService;
        this.userService = userService;
    }

    @GetMapping
    public String getAdminPage(Model model){
        model.addAttribute("bodyContent", "admin/admin-panel");
        return "admin-master-template";
    }
    @GetMapping("/software")
    public String manageWithSoftware(Model model){
        model.addAttribute("software",softwareService.findAll());
        model.addAttribute("bodyContent", "admin/software");
        return "admin-master-template";
    }
    @GetMapping("/software/add")
    public String getAddSoftwarePage(Model model){
        model.addAttribute("isEdit",false);
        model.addAttribute("bodyContent", "admin/add-edit-software");
        return "admin-master-template";
    }
    @PostMapping("/software/add")
    public String postAddSoftware(@RequestParam String name,
                                      @RequestParam MultipartFile logo){
        this.softwareService.createSoftware(logo,name);
        return "redirect:/admin/software";
    }
    @GetMapping("/software/edit/{id}")
    public String getEditSoftwarePage(Model model, @PathVariable Long id){
        model.addAttribute("existing",softwareService.findById(id));
        model.addAttribute("isEdit",true);
        model.addAttribute("bodyContent", "admin/add-edit-software");
        return "admin-master-template";
    }
    @PostMapping("/software/edit/{id}")
    public String postEditSoftware(@PathVariable Long id,
                                   @RequestParam String name,
                                   @RequestParam @Nullable MultipartFile logo){
        this.softwareService.editSoftware(id,logo,name);
        return "redirect:/admin/software";
    }

    @GetMapping("/software/delete/{id}")
    public String deleteSoftware(@PathVariable Long id){
        this.softwareService.deleteSoftware(id);
        return "redirect:/admin/software";
    }


    @GetMapping("/users")
    public String manageWithUsers(Model model){
        model.addAttribute("users",userService.findAllUsers());
        model.addAttribute("bodyContent", "admin/users");
        return "admin-master-template";
    }
}
