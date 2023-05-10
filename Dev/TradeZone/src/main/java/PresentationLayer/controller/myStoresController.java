package PresentationLayer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class myStoresController {
    @GetMapping("/role_page")
    public String register() {
        return "role_page";
    }
}
