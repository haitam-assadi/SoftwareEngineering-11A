package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.StoreDTO;
import PresentationLayer.model.GeneralModel;
import ServiceLayer.ResponseT;
import PresentationLayer.model.Alert;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MyStoresController {
    private Server server = Server.getInstance();
    private GeneralModel controller;
    private Map<String, List<StoreDTO>> myStores;
    Alert alert = Alert.getInstance();

    @GetMapping("/myStores")
    public String myStores(HttpServletRequest request, Model model){
        myStores = null; // or new ... ???
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
//            myStores = (Map<String, List<StoreDTO>>) request.getSession().getAttribute("myStores");
        }
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
        request.getSession().setAttribute("myStores", myStores);
        alert.reset();
        return "myStores";
    }

//    @GetMapping("/role_page_closeStore") // TODO: delete
//    public String closeStoreAlert(){
//        alert.setSuccess(true);
//        alert.setMessage("The store was successfully closed");
//        return "redirect:/myStores";
//    }

    @PostMapping("/createStore")
//    @ModelAttribute Store store
    public String createStore(HttpServletRequest request, @RequestParam String storeName){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
//            myStores = (Map<String, List<StoreDTO>>) request.getSession().getAttribute("myStores");
        }
        ResponseT<StoreDTO> response = server.createStore(controller.getName(), storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/myStores";
        }
        alert.setSuccess(true);
        alert.setMessage("Store " + storeName + " has been created");
        return "redirect:/myStores";
    }
}
