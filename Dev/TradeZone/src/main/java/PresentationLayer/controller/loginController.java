package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.BagDTO;
import DTO.ProductDTO;
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
import java.util.LinkedList;
import java.util.List;

import static PresentationLayer.controller.CartController.buildCart;

@Controller
public class loginController {
    private Server server = Server.getInstance();
    GeneralModel controller;

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        controller = new GeneralModel();
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
        }
        model.addAttribute("isError", false);
        model.addAttribute("error_message", "");
        model.addAttribute("name", controller.getName());
        return "login";
    }

    @PostMapping("/login")
    public String loginDemand(HttpServletRequest request, @ModelAttribute User user, Model model) {
        List<ProductDTO> products = new ArrayList<>();
//        controller = new GeneralModel();
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
        }
        ResponseT<String> response = server.login(controller.getName(), user.getUsername(), user.getPassword());
        if(response.ErrorOccurred){
            model.addAttribute("isError", true);
            model.addAttribute("error_message", response.errorMessage);
            model.addAttribute("name", controller.getName());
            return "login";
        }
        controller = new GeneralModel();
        controller.setName(response.value);
        controller.setLogged(true);

//        ResponseT<Boolean> hasRole = server.hasRole(controller.getName());
//        if(hasRole.ErrorOccurred){
//            model.addAttribute("isError", true);
//            model.addAttribute("error_message", response.errorMessage);
//            model.addAttribute("name", controller.getName());
//            return "login";
//        }
//        controller.setHasRole(hasRole.getValue());

        ResponseT<Boolean> isSystemManager = server.isSystemManager(controller.getName());
        controller.setSystemManager(!isSystemManager.ErrorOccurred);

        ResponseT<List<BagDTO>> cartResponse  = server.getCartContent(controller.getName());
        if(cartResponse.ErrorOccurred){
            model.addAttribute("isError", true);
            model.addAttribute("error_message", cartResponse.errorMessage);
            model.addAttribute("name", controller.getName());
            return "login";
        }
        controller.setCart(buildCart(cartResponse.value));
        model.addAttribute("logged", controller.getLogged());
        model.addAttribute("hasRole", controller.getHasRole());
        model.addAttribute("name", controller.getName());
        model.addAttribute("products", products);
        model.addAttribute("message", "");
        model.addAttribute("controller",controller);
        request.getSession().setAttribute("controller", controller);
        return "redirect:/";
//        return "index";
    }
}
