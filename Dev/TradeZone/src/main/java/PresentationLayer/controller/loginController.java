package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.ProductDTO;
import PresentationLayer.model.User;
import ServiceLayer.ResponseT;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class loginController {
    private Server server = Server.getInstance();
    private GeneralController controller = GeneralController.getInstance();

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("isError", false);
        model.addAttribute("error_message", "");
        model.addAttribute("name", controller.getName());
        return "login";
    }

    @PostMapping("/login")
    public String loginDemand(@ModelAttribute User user, Model model) {
        List<ProductDTO> products = new ArrayList<>();
        ResponseT<String> response = server.login(controller.getName(), user.getUsername(), user.getPassword());
        if(response.ErrorOccurred){
            model.addAttribute("isError", true);
            model.addAttribute("error_message", response.errorMessage);
            model.addAttribute("name", controller.getName());
            return "login";
        }
        controller.setName(response.value);
        ResponseT hasRole = server.hasRole(controller.getName());
        if(hasRole.ErrorOccurred){
            model.addAttribute("message", response.errorMessage);
            return "error";
        }
        controller.setHasRole((Boolean)hasRole.getValue());
        controller.setLogged(true);
        model.addAttribute("logged", controller.getLogged());
        model.addAttribute("hasRole", controller.getHasRole());
        model.addAttribute("name", controller.getName());
        model.addAttribute("products", products);
        model.addAttribute("message", "");
        return "index";
    }
}
