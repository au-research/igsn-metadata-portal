package au.edu.ardc.igsn.igsnportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StyleGuideController {

    @GetMapping("/style-guide")
    public String show() {
        return "style-guide";
    }
}
