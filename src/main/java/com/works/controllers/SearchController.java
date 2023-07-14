package com.works.controllers;

import com.works.services.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
SearchService service =new SearchService();
    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "") String q, Model model){
        model.addAttribute("users",service.users(q));
        model.addAttribute("search",q);
        return "search";
    }

}
