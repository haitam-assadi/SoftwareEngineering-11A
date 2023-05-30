package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.BagDTO;
import DTO.ProductDTO;
import DTO.StoreDTO;
import ServiceLayer.ResponseT;
import PresentationLayer.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Controller
public class CartController {
    private Server server = Server.getInstance();
    private GeneralModel controller;
    private List<Bag> bags;
    Alert alert = Alert.getInstance();

    @GetMapping("/cart")
    public String cart(HttpServletRequest request, Model model) {
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            bags = (List<Bag>) request.getSession().getAttribute("bag");
        }
        ResponseT<List<BagDTO>> response  = server.getCartContent(controller.getName());
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            model.addAttribute("controller", controller);
            model.addAttribute("alert", alert.copy());
            model.addAttribute("bags", new LinkedList<>());
            return "cart";
        }
        bags = buildCart(response.value);
        controller.setCart(bags);
        model.addAttribute("controller", controller);
        model.addAttribute("alert", alert.copy());
        model.addAttribute("bags", bags);
        request.getSession().setAttribute("controller", controller);
        request.getSession().setAttribute("bags", bags);
        alert.reset();
        return "cart";
    }

    public static List<Bag> buildCart(List<BagDTO> cartDTO){
        List<Bag> bags = new ArrayList<>();
        for(BagDTO bagDTO : cartDTO){
            List<Product> products = new ArrayList<>();
            for(ProductDTO productDTO : bagDTO.bagContent.keySet()){
                int amount = bagDTO.bagContent.get(productDTO);
                Product product = new Product(productDTO.name, productDTO.price, productDTO.description, amount);
                products.add(product);
            }
            Bag bag = new Bag(bagDTO.storeBag, products);
            bags.add(bag);
        }
        return bags;
    }
}
