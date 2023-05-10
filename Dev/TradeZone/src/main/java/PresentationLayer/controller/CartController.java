package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.BagDTO;
import DTO.ProductDTO;
import ServiceLayer.ResponseT;
import PresentationLayer.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {
    private Server server = Server.getInstance();
    private GeneralController controller = GeneralController.getInstance();
    @GetMapping("/cart")
    public String cart(@ModelAttribute User user, Model model) {
        ResponseT<List<BagDTO>> response  = server.getCartContent(controller.getName());
        if(response.ErrorOccurred){
            model.addAttribute("message", response.errorMessage);
            return "error";
        }
        List<Bag> bags = new ArrayList<>();
        for(BagDTO bagDTO : response.value){
            List<Product> products = new ArrayList<>();
            for(ProductDTO productDTO : bagDTO.bagContent.keySet()){
                int amount = bagDTO.bagContent.get(productDTO);
                Product product = new Product(productDTO.name, productDTO.price, productDTO.description, amount);
                products.add(product);
            }
            Bag bag = new Bag(bagDTO.storeBag, products);
            bags.add(bag);
        }
        model.addAttribute("isEmpty", bags.isEmpty());
        model.addAttribute("bags", bags);
        return "cart";
    }
}
