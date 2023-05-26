package PresentationLayer.controller;

import CommunicationLayer.Server;
import PresentationLayer.model.Alert;
import PresentationLayer.model.GeneralModel;
import PresentationLayer.model.ItemDetails;
import ServiceLayer.ResponseT;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class productController {
    private Server server = Server.getInstance();
    private GeneralModel controller;
    private Alert alert = Alert.getInstance();

    // addProductToCart
    @PostMapping("/add_to_cart")
    public String addToCart(@ModelAttribute ItemDetails itemDetails, HttpServletRequest request) {
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
        }
        String referer = request.getHeader("referer");
        int amount = itemDetails.getAmount();
        String productName = itemDetails.getName();
        String storeName = itemDetails.getStoreName();
        ResponseT<Boolean> response = server.addToCart(controller.getName(), storeName, productName, amount);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:" + referer;
        }
        alert.setSuccess(true);
        alert.setMessage("Added successfully");
        controller.updateCart();
        request.getSession().setAttribute("controller", controller);
        return "redirect:" + referer;
    }


    // removeProductFromCart
    @PostMapping("/removeFromCart")
    public String removeFromCart(@ModelAttribute ItemDetails itemDetails, HttpServletRequest request) {
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
        }
        String referer = request.getHeader("referer");
        String productName = itemDetails.getName();
        String storeName = itemDetails.getStoreName();
        ResponseT<Boolean> response = server.removeFromCart(controller.getName(), storeName, productName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:" + referer;
        }
        if(response.getValue()){ // removed
            alert.setSuccess(true);
            alert.setMessage("Removed successfully");
            controller.updateCart();
        }
        else{
            alert.setFail(true);
            alert.setMessage("Failed to remove the product");
        }
        request.getSession().setAttribute("controller", controller);
        return "redirect:" + referer;
    }

    // updateProductAmountInCart
    @PostMapping("/updateProductAmount")
    public String updateProductAmount(@ModelAttribute ItemDetails itemDetails, HttpServletRequest request) {
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
        }
        String referer = request.getHeader("referer");
        int amount = itemDetails.getAmount();
        String productName = itemDetails.getName();
        String storeName = itemDetails.getStoreName();
        ResponseT<Boolean> response = server.changeProductAmountInCart(controller.getName(), storeName, productName, amount);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:" + referer;
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
        request.getSession().setAttribute("controller", controller);
        return "redirect:" + referer;
    }

}
