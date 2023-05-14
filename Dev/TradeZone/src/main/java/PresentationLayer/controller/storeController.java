package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.StoreDTO;
import ServiceLayer.ResponseT;
import PresentationLayer.model.Alert;
import PresentationLayer.model.Appoint;
import PresentationLayer.model.Store;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class storeController {
    private Server server = Server.getInstance();
    private GeneralController controller = GeneralController.getInstance();
    StoreDTO store;
    Alert alert = new Alert();

    @PostMapping("/store")
    public String showStore(@ModelAttribute Store store1, Model model){
        ResponseT<StoreDTO> response = server.getStoreInfo(controller.getName(), store1.getStoreName());
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            model.addAttribute("name", controller.getName());
            model.addAttribute("hasRole", controller.getHasRole());
            model.addAttribute("logged", controller.getLogged());
            model.addAttribute("success", alert.isSuccess());
            model.addAttribute("fail", alert.isFail());
            model.addAttribute("message", alert.getMessage());
            return "store";
        }
        store = response.getValue();
        model.addAttribute("name", controller.getName());
        model.addAttribute("hasRole", controller.getHasRole());
        model.addAttribute("logged", controller.getLogged());
        model.addAttribute("success", alert.isSuccess());
        model.addAttribute("fail", alert.isFail());
        model.addAttribute("message", alert.getMessage());
        model.addAttribute("store", store);
        alert = new Alert();
        return "store";
    }

    @PostMapping("/appointMember")
    public String appointMember(@ModelAttribute Appoint appoint){
        String appointedAs;
        ResponseT<Boolean> response;
        if(appoint.getAppointAs().equals("appointAsOwner")){
            appointedAs = "Store Owner";
            response = server.appointOtherMemberAsStoreOwner(controller.getName(), store.storeName, appoint.getMemberName());
        }
        else { // appointAsManager
            appointedAs = "Store Manager";
            response = server.appointOtherMemberAsStoreManager(controller.getName(), store.storeName, appoint.getMemberName());
        }
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/store";
        }
        alert.setSuccess(true);
        alert.setMessage(appoint.getMemberName() + " is appointed as " + appointedAs);
        return "redirect:/store";
    }

}
