package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.ProductDTO;
import PresentationLayer.model.Alert;
import PresentationLayer.model.GeneralModel;
import ServiceLayer.ResponseT;
import PresentationLayer.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class registerController {
    private Server server = Server.getInstance();
    private GeneralModel controller;
    private Alert alert = Alert.getInstance();

    @GetMapping("/register")
    public String register(HttpServletRequest request, Model model) {
        controller = new GeneralModel();
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
        }
        model.addAttribute("isError", false);
        model.addAttribute("error_message", "");
        model.addAttribute("name", controller.getName());
        return "register";
    }

    @PostMapping("/register")
    public String registerDemand(HttpServletRequest request, @ModelAttribute User user, Model model) {
        List<ProductDTO> products = new ArrayList<>();
        controller = new GeneralModel();
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
        }
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
        alert.setSuccess(true);
        alert.setMessage("Your registration was successful");
        return "redirect:/";
//        return "index";
    }
}
