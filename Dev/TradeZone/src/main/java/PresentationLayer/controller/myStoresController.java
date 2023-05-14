package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.BagDTO;
import DTO.StoreDTO;
import ServiceLayer.ResponseT;
import PresentationLayer.model.Alert;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
            return "cart";
        }
        myStores = response.getValue();
//        delete();
        model.addAttribute("name", controller.getName());
        model.addAttribute("hasRole", controller.getHasRole());
        model.addAttribute("logged", controller.getLogged());
        model.addAttribute("success", alert.isSuccess());
        model.addAttribute("fail", alert.isFail());
        model.addAttribute("message", alert.getMessage());
        model.addAttribute("isEmpty", myStores.isEmpty());
        model.addAttribute("myStores", myStores);
//        model.addAttribute("totalPrice", getTotalPrice(bags));
        alert = new Alert();
        return "role_page";
    }

    private void delete(){
        myStores = new HashMap<>();
        List<StoreDTO> list = new ArrayList<>();
        list.add(server.getStoreInfo(controller.getName(), "user1_first_store").getValue());
        myStores.put("founder", list);
        list = new ArrayList<>();
        list.add(server.getStoreInfo(controller.getName(), "user1_first_store").getValue());
        list.add(server.getStoreInfo(controller.getName(), "user1_second_store").getValue());
        myStores.put("owner", list);
    }
}
