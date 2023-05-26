package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.StoreDTO;
import ServiceLayer.ResponseT;
import PresentationLayer.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class SearchStoreController {
    private Server server = Server.getInstance();
    private GeneralModel controller;
    StoreDTO store;
    Alert alert = Alert.getInstance();

    @GetMapping("/searchStore")
    public String searchStorePage(HttpServletRequest request, Model model) {
//        controller = new GeneralModel();
        store = null;
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getHeader("referer").contentEquals(request.getRequestURL()))
                store = (StoreDTO) request.getSession().getAttribute("searchStore");
        }
        model.addAttribute("controller", controller);
        model.addAttribute("alert", alert.copy());
        model.addAttribute("store", store);
        alert.reset();
        return "searchStore";
    }

    @PostMapping("/searchStore")
    public String searchStore(HttpServletRequest request, @RequestParam String storeName) {
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
        }
        ResponseT<StoreDTO> response = server.getStoreInfo(controller.getName(), storeName);
        if(response.ErrorOccurred){
            store = null;
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/searchStore";
        }
        store = response.getValue();
        request.getSession().setAttribute("searchStore", store);
        return "redirect:/searchStore";
    }
}
