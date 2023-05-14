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
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


@Controller
public class CartController {
    private Server server = Server.getInstance();
    private GeneralController controller = GeneralController.getInstance();
    private List<Bag> bags;
    Alert alert = new Alert();

    @GetMapping("/cart")
    public String cart(@ModelAttribute User user, Model model) {
        if(alert == null){
            alert = new Alert();
        }
        ResponseT<List<BagDTO>> response  = server.getCartContent(controller.getName());
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            model.addAttribute("name", controller.getName());
            model.addAttribute("hasRole", controller.getHasRole());
            model.addAttribute("logged", controller.getLogged());
            model.addAttribute("success", alert.isSuccess());
            model.addAttribute("fail", alert.isFail());
            model.addAttribute("message", alert.getMessage());
            return "cart";
        }
        bags = buildCart(response.value);
        model.addAttribute("name", controller.getName());
        model.addAttribute("hasRole", controller.getHasRole());
        model.addAttribute("logged", controller.getLogged());
        model.addAttribute("success", alert.isSuccess());
        model.addAttribute("fail", alert.isFail());
        model.addAttribute("message", alert.getMessage());
        model.addAttribute("isEmpty", bags.isEmpty());
        model.addAttribute("bags", bags);
        model.addAttribute("totalPrice", getTotalPrice(bags));
        alert = new Alert();
        return "cart";
    }

    @PostMapping("/updateProductAmount")
    public String updateProductAmount(@ModelAttribute ItemDetails itemDetails, Model model) {
        int amount = itemDetails.getAmount();
        String productName = itemDetails.getName();
        String storeName = itemDetails.getStoreName();
        ResponseT<Boolean> response = server.changeProductAmountInCart(controller.getName(), storeName, productName, amount);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/cart";
        }
        if(response.getValue()){ // updated
            alert.setSuccess(true);
            alert.setMessage("product amount has updated successfully");
        }
        else{ // not updated
            alert.setFail(true);
            alert.setMessage("Failed to update the product amount");
        }
        return "redirect:/cart";
    }

    @PostMapping("/removeFromCart")
    public String removeFromCart(@ModelAttribute ItemDetails itemDetails) {
        String productName = itemDetails.getName();
        String storeName = itemDetails.getStoreName();
        ResponseT<Boolean> response = server.removeFromCart(controller.getName(), storeName, productName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/cart";
        }
        if(response.getValue()){ // removed
            alert.setSuccess(true);
            alert.setMessage("product has removed successfully");
        }
        else{
            alert.setFail(true);
            alert.setMessage("Failed to remove the product");
        }
        return "redirect:/cart";

    }

    private List<Bag> buildCart(List<BagDTO> cartDTO){
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

    private double getTotalPrice(List<Bag> bags){
        double total = 0;
        for(Bag b : bags){
            total += b.getTotalPrice();
        }
        total = round(total, 2);
        return total;
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
