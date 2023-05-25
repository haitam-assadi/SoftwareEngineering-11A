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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class SearchStoreController {
    private Server server = Server.getInstance();
    private GeneralController controller = GeneralController.getInstance();
    StoreDTO store;
    Alert alert = Alert.getInstance();

    @GetMapping("/searchStore")
    public String searchStorePage(Model model) {
        model.addAttribute("controller", controller);
        model.addAttribute("alert", alert.copy());
        model.addAttribute("store", store);
        alert.reset();
        return "searchStore";
    }

    @PostMapping("/searchStore")
//    @ModelAttribute Store store1
    public String searchStore(@RequestParam String storeName, Model model) {
        ResponseT<StoreDTO> response = server.getStoreInfo(controller.getName(), storeName);
        if(response.ErrorOccurred){
            store = null;
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/searchStore";
        }
        store = response.getValue();
        return "redirect:/searchStore";
    }
}
