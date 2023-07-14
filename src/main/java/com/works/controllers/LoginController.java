package com.works.controllers;

import com.works.props.User;
import com.works.services.TinkEncDec;
import com.works.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class LoginController {
    final TinkEncDec tinkEncDec;
    final HttpServletRequest request;
    final HttpServletResponse response;
    final UserService service;

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/userLogin")
    public String userLogin(User user) {
        User u = service.loginUser(user);

        if (u != null) {
            request.getSession().setAttribute("user",u);
            String cryptuid = tinkEncDec.encrypt(""+u.getUid());//gelen kullanıcı idsini hazır sınıf sayesinde şifreliyor
            if ( user.getRemember() != null && user.getRemember().equals("on") ) {
                Cookie cookie = new Cookie("user", ""+cryptuid );
                cookie.setMaxAge(60 * 60);
                response.addCookie(cookie);
            }
            return "redirect:/home";
        }
        return "redirect:/";
    }


    @GetMapping("/logout")
    public String logout() {
        request.getSession().removeAttribute("user");//session'ı kurulmuş olan kulanıcının session'ını program kısmında yok eder
       Cookie cookie = new Cookie("user", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

}