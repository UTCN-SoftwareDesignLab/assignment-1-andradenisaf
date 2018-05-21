package application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping(value = "/admin")
    public String adminPage(Model model) {
        return "admin";
    }

    @GetMapping(value = "/secretary")
    public String secretaryPage(Model model) {
        return "secretary";
    }

    @GetMapping(value = "/doctor")
    public String doctorPage(Model model) {
        return "doctor";
    }

    @GetMapping(value = "/login")
    public String loginPage(Model model) {
        return "login";
    }

}
