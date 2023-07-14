package com.works.controllers;

import com.works.props.User;
import com.works.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SettingsController {
    final UserService service;
    @GetMapping("/settings")
    public String settings(){
        return "settings";
    }
    @PostMapping("/userSave")
    public String userSave(User user){


        int status = service.userSave(user);
        if (status >0){
            return "redirect:/home";
        }
        return "redirect:/settings";
    }
}
