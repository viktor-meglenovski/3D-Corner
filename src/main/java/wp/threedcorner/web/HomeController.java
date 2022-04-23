package wp.threedcorner.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wp.threedcorner.service.SearchService;

import java.security.Principal;

@Controller
@RequestMapping(value = {"/", "/home"})
public class HomeController {
    private final SearchService searchService;

    public HomeController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public String getHomePage(Principal principal, Model model) {
        if(principal!=null){
        model.addAttribute("projects",searchService.sortByLikes());
        model.addAttribute("bodyContent", "home");
        }
        else{
            model.addAttribute("bodyContent", "home-all");
        }
        return "master-template";
    }

    @GetMapping("/access_denied")
    public String getAccessDeniedPage(Model model) {
        model.addAttribute("bodyContent", "error");
        return "master-template";
    }

}
