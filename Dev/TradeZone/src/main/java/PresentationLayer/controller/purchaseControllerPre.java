package PresentationLayer.controller;

import CommunicationLayer.Server;
import PresentationLayer.model.GeneralModel;
import ServiceLayer.ResponseT;
import PresentationLayer.model.Alert;
import PresentationLayer.model.Purchase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class purchaseControllerPre {
    private Server server = Server.getInstance();
    private GeneralModel controller;
    boolean done = false;
    Alert alert = Alert.getInstance();

    @GetMapping("/purchase")
    public String purchase(HttpServletRequest request, Model model){
        done  = false;
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("done") != null)
                done = (boolean) request.getSession().getAttribute("done");
        }
        model.addAttribute("controller", controller);
        model.addAttribute("alert", alert.copy());
        model.addAttribute("done", done);
        alert.reset();
//        done = false;
        return "purchase";
    }

    @PostMapping("/purchase")
    public String purchase(HttpServletRequest request, @ModelAttribute Purchase purchase){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
        }
        ResponseT<Boolean> response = server.purchaseCartByCreditCard(
                controller.getName(), purchase.getCardNumber(), purchase.getMonth(),
                purchase.getYear(), purchase.getHolder(), purchase.getCvv(), purchase.getId(),
                purchase.getReceiverName(), purchase.getShipmentAddress(),
                purchase.getShipmentCity(), purchase.getShipmentCountry(), purchase.getZipCode());
        if(response.ErrorOccurred){
            done = false;
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            request.getSession().setAttribute("done", done); // TODO: ???
            return "redirect:/purchase";
        }
        if(response.getValue()){ // true
            done = true;
            alert.setSuccess(true);
            alert.setMessage("Your order has been successfully received!");
        }
        else{ // false
            done = false;
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
        }
        request.getSession().setAttribute("done", done); // TODO: ???
        return "redirect:/purchase";
    }
}
