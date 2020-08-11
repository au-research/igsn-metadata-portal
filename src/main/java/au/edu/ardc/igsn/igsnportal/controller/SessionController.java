package au.edu.ardc.igsn.igsnportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class SessionController {

    @GetMapping("/login")
    public String login(
            @RequestParam(required = false) String redirect
    ) {
        String redirectTo = redirect != null ? redirect : "/";
        return "redirect:" + redirectTo;
    }

    @GetMapping("/logout")
    public String logout(
            @RequestParam(required = false) String redirect,
            HttpServletRequest request
    ) throws ServletException {
        String redirectTo = redirect != null ? redirect : "/";
        request.logout();
        return "redirect:" + redirectTo;
    }
}
