package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.StoreDTO;
import PresentationLayer.model.*;
import ServiceLayer.ResponseT;
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
    String storeName;
    Alert alert = new Alert();

    @GetMapping("/store")
    public String showStore(Model model){
        ResponseT<StoreDTO> response = server.getStoreInfo(controller.getName(), storeName);
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
        storeName = store.storeName;
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
        storeName = store.storeName;
//        model.addAttribute("name", controller.getName());
//        model.addAttribute("hasRole", controller.getHasRole());
//        model.addAttribute("logged", controller.getLogged());
//        model.addAttribute("success", alert.isSuccess());
//        model.addAttribute("fail", alert.isFail());
//        model.addAttribute("message", alert.getMessage());
//        model.addAttribute("store", store);
//        alert = new Alert();
        return "redirect:/store";
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

    @PostMapping("/closeStore")
    public String closeStore(){
        ResponseT<Boolean> response = server.closeStore(controller.getName(), store.storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/store";
        }
        alert.setSuccess(true);
        alert.setMessage("Store " + store.storeName + " closed");
        return "redirect:/role_page_closeStore";
    }

    @PostMapping("/updateProductPrice")
    public String updateProductPrice(@ModelAttribute Product product){
        ResponseT<Boolean> response = server.updateProductPrice(controller.getName(), store.storeName, product.getName(), product.getPrice());
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/store";
        }
        if(response.getValue()){
            alert.setSuccess(true);
            alert.setMessage("Product price has been successfully updated");
        }
        else {
            alert.setFail(true);
            alert.setMessage("Product price does not updated");
        }
        return "redirect:/store";
    }

    @PostMapping("/removeProduct")
    public String removeProduct(@ModelAttribute Product product){
        ResponseT<Boolean> response = server.removeProductFromStock(controller.getName(), store.storeName, product.getName());
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/store";
        }
        if(response.getValue()){
            alert.setSuccess(true);
            alert.setMessage("Product has been successfully removed from stock");
        }
        else {
            alert.setFail(true);
            alert.setMessage("Product does not removed from stock");
        }
        return "redirect:/store";
    }


}
