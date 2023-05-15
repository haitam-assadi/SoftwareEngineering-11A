package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.BagDTO;
import DTO.StoreDTO;
import PresentationLayer.model.Search;
import PresentationLayer.model.Store;
import ServiceLayer.ResponseT;
import PresentationLayer.model.Alert;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class myStoresController {
    private Server server = Server.getInstance();
    private GeneralController controller = GeneralController.getInstance();
    private Map<String, List<StoreDTO>> myStores;
    Alert alert = new Alert();

    @GetMapping("/role_page")
    public String myStores(Model model) {
        if(alert == null){
            alert = new Alert();
        }
        ResponseT<Map<String,List<StoreDTO>>> response  = server.myStores(controller.getName());
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            model.addAttribute("name", controller.getName());
            model.addAttribute("hasRole", controller.getHasRole());
            model.addAttribute("logged", controller.getLogged());
            model.addAttribute("success", alert.isSuccess());
            model.addAttribute("fail", alert.isFail());
            model.addAttribute("message", alert.getMessage());
            return "role_page";
        }
        myStores = response.getValue();
        model.addAttribute("name", controller.getName());
        model.addAttribute("hasRole", controller.getHasRole());
        model.addAttribute("logged", controller.getLogged());
        model.addAttribute("success", alert.isSuccess());
        model.addAttribute("fail", alert.isFail());
        model.addAttribute("message", alert.getMessage());
        model.addAttribute("isEmpty", myStores.isEmpty());
        model.addAttribute("myStores", myStores);
        alert = new Alert();
        return "role_page";
    }

    @GetMapping("/role_page_closeStore")
    public String closeStoreAlert(){
        alert.setSuccess(true);
        alert.setMessage("The store was successfully closed");
        return "redirect:/role_page";
    }

    @PostMapping("/createStore")
    public String createStore(@ModelAttribute Store store, Model model){
        ResponseT<StoreDTO> response = server.createStore(controller.getName(), store.getStoreName());
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/role_page";
        }
        alert.setSuccess(true);
        alert.setMessage("Store " + store.getStoreName() + " created");
        return "redirect:/role_page";
    }
}
