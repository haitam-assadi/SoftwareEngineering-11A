package PresentationLayer.controller;

import CommunicationLayer.Server;
import PresentationLayer.model.Alert;
import PresentationLayer.model.System;
import ServiceLayer.ResponseT;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SystemController {
    private Server server = Server.getInstance();
    private GeneralController controller = GeneralController.getInstance();
    System system = new System(); //  ???
    String currentPage = "allStores";
    Alert alert = Alert.getInstance();

    @GetMapping("/system")
    public String systemManager(Model model){
        ResponseT<List<String>> response = server.getAllStoresNames();
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
        }
        system.setAllStores(response.getValue());
        model.addAttribute("controller", controller);
        model.addAttribute("alert", alert.copy());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("system", system);
        return "SystemTemplates/system";
    }

    @GetMapping("/allStores")
    public String getAllStores(){
//        ResponseT<List<String>> response = server.getAllStoresNames();
//        if(response.ErrorOccurred){
//            alert.setFail(true);
//            alert.setMessage(response.errorMessage);
//            return "redirect:/system";
//        }
        currentPage = "allStores";
//        system.setAllStores(response.getValue());
        return "redirect:/system";
    }


}
