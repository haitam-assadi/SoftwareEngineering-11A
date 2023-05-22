package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.StoreDTO;
import PresentationLayer.model.Store;
import ServiceLayer.ResponseT;
import PresentationLayer.model.Alert;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MyStoresController {
    private Server server = Server.getInstance();
    private GeneralController controller = GeneralController.getInstance();
    private Map<String, List<StoreDTO>> myStores;
    Alert alert = Alert.getInstance();

    @GetMapping("/myStores")
    public String myStores(Model model) {
        ResponseT<Map<String,List<StoreDTO>>> response  = server.myStores(controller.getName());
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            model.addAttribute("controller", controller);
            model.addAttribute("alert", alert.copy());
            model.addAttribute("myStores", new HashMap<>());
            return "myStores";
        }
        myStores = response.getValue();
        model.addAttribute("controller", controller);
        model.addAttribute("alert", alert.copy());
        model.addAttribute("myStores", myStores);
        alert.reset();
        return "myStores";
    }

    @GetMapping("/role_page_closeStore") // TODO: delete
    public String closeStoreAlert(){
        alert.setSuccess(true);
        alert.setMessage("The store was successfully closed");
        return "redirect:/myStores";
    }

    @PostMapping("/createStore")
    public String createStore(@ModelAttribute Store store, Model model){
        ResponseT<StoreDTO> response = server.createStore(controller.getName(), store.getStoreName());
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/myStores";
        }
        alert.setSuccess(true);
        alert.setMessage("Store " + store.getStoreName() + " created");
        return "redirect:/myStores";
    }
}
