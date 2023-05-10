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


@Controller
public class storeController {
    private Server server = Server.getInstance();
    private GeneralController controller = GeneralController.getInstance();

    @GetMapping("/searchStore")
    public String searchStore_1(@ModelAttribute Store store, Model model) {
        model.addAttribute("isError", true);
        model.addAttribute("message", "");
        return "searchStore";
    }

    @PostMapping("/searchStore")
    public String searchStore_2(@ModelAttribute Store store, Model model) {
        ResponseT<StoreDTO> response = server.getStoreInfo(controller.getName(), store.getStoreName());
        if(response.ErrorOccurred){
            model.addAttribute("isError", true);
            model.addAttribute("message", response.errorMessage);
            return "searchStore";
        }
        model.addAttribute("isError", false);
        model.addAttribute("message", "");
        model.addAttribute("storeName", response.value.storeName);
        model.addAttribute("founderName", response.value.founderName);
        model.addAttribute("ownersNames", response.value.ownersNames);
        model.addAttribute("managersNames", response.value.managersNames);
        model.addAttribute("products", response.value.productsInfo);
        return "searchStore";
    }

}
