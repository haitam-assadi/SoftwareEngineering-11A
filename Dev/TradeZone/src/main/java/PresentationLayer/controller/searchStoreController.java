package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.StoreDTO;
import ServiceLayer.ResponseT;
import PresentationLayer.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
public class searchStoreController {
    private Server server = Server.getInstance();
    private GeneralController controller = GeneralController.getInstance();
    StoreDTO store;
    Alert alert = new Alert();

    @GetMapping("/searchStore")
    public String searchStorePage(Model model) {
        model.addAttribute("name", controller.getName());
        model.addAttribute("hasRole", controller.getHasRole());
        model.addAttribute("logged", controller.getLogged());
        model.addAttribute("success", alert.isSuccess());
        model.addAttribute("fail", alert.isFail());
        model.addAttribute("message", alert.getMessage());
        model.addAttribute("store", store);
        model.addAttribute("controller", controller);
        alert = new Alert();
        return "searchStore";
    }

    @PostMapping("/searchStore")
    public String searchStore(@ModelAttribute Store store1, Model model) {
        ResponseT<StoreDTO> response = server.getStoreInfo(controller.getName(), store1.getStoreName());
        if(response.ErrorOccurred){
            store = null;
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/searchStore";
        }
        store = response.getValue();
        return "redirect:/searchStore";
    }

    @PostMapping("/searchStoreAddToCart")
    public String searchStoreAddToCart (@ModelAttribute ItemDetails itemDetails, Model model, HttpServletRequest request) {
        int amount = itemDetails.getAmount();
        String productName = itemDetails.getName();
        String storeName = itemDetails.getStoreName();
        ResponseT<Boolean> response = server.addToCart(controller.getName(), storeName, productName, amount);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/searchStore"; // TODO: redirect:/ + request.getHeader(...)
        }
        alert.setSuccess(true);
        alert.setMessage("Added successfully");
        controller.updateCart();
        return "redirect:/searchStore";
    }

    @PostMapping("/searchStoreUpdateProductAmount")
    public String updateProductAmount(@ModelAttribute ItemDetails itemDetails, Model model) {
        int amount = itemDetails.getAmount();
        String productName = itemDetails.getName();
        String storeName = itemDetails.getStoreName();
        ResponseT<Boolean> response = server.changeProductAmountInCart(controller.getName(), storeName, productName, amount);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/searchStore";
        }
        if(response.getValue()){ // updated
            alert.setSuccess(true);
            alert.setMessage("product amount has updated successfully");
            controller.updateCart();
        }
        else{ // not updated
            alert.setFail(true);
            alert.setMessage("Failed to update the product amount");
        }
        return "redirect:/searchStore";
    }



}
