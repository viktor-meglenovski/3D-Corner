package wp.threedcorner.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wp.threedcorner.service.SearchService;

@Controller
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public String search(@RequestParam String searchText, Model model){
        model.addAttribute("users",searchService.searchUsers(searchText));
        model.addAttribute("projects",searchService.searchProjects(searchText));
        model.addAttribute("searchText",searchText);
        model.addAttribute("bodyContent", "search-results");
        return "master-template";
    }
}
