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
public class registerController {
    private Server server = Server.getInstance();
    private GeneralController controller = GeneralController.getInstance();

    @GetMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        model.addAttribute("isError", false);
        model.addAttribute("error_message", "");
        model.addAttribute("name", controller.getName());
        return "register";
    }

    @PostMapping("/register")
    public String registerDemand(@ModelAttribute User user, Model model) {
        List<ProductDTO> products = new ArrayList<>();

        ResponseT<Boolean> response = server.register(controller.getName(), user.getUsername(), user.getPassword());

        if(response.ErrorOccurred){
            model.addAttribute("isError", true);
            model.addAttribute("error_message", response.errorMessage);
            model.addAttribute("name", controller.getName());
            return "register";
        }
        model.addAttribute("logged", controller.getLogged());
        model.addAttribute("name", controller.getName());
        model.addAttribute("products", products);
        model.addAttribute("message", "");
        return "index";
    }
}
