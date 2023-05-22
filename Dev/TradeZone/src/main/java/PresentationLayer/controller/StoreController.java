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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class StoreController {
    private Server server = Server.getInstance();
    private GeneralController controller = GeneralController.getInstance();
    StoreDTO store;
    String storeName;
    Map<String, List<Worker>> workers;
    String currentPage = "stock";
    Alert alert = Alert.getInstance();

    @GetMapping("/store")
    public String showStore(Model model){
        ResponseT<StoreDTO> response = server.getStoreInfo(controller.getName(), storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            model.addAttribute("controller", controller);
            model.addAttribute("alert", alert.copy());
            model.addAttribute("store", store);
            model.addAttribute("workers", workers);
            model.addAttribute("currentPage", currentPage);
            return "storeTemplates/store";
        }
        store = response.getValue();
        storeName = store.storeName;
        workers = buildWorkers(store);
        model.addAttribute("controller", controller);
        model.addAttribute("alert", alert.copy());
        model.addAttribute("store", store);
        model.addAttribute("workers", workers);
        model.addAttribute("currentPage", currentPage);
        alert.reset();
//        currentPage = "stock"; // ???
        return "storeTemplates/store";
    }


    @PostMapping("/store")
    public String showStore(@ModelAttribute Store store1, Model model){
        ResponseT<StoreDTO> response = server.getStoreInfo(controller.getName(), store1.getStoreName());
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
//            model.addAttribute("controller", controller);
//            model.addAttribute("alert", alert);
//            model.addAttribute("store", store);
            return "redirect:/store";
        }
        store = response.getValue();
        storeName = store.storeName;
        currentPage = "stock";
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
        return "redirect:/store";
    }

//    ------------------------- STOCK -------------------------
    @GetMapping("/stock")
    public String getStock(Model model){


//        ResponseT<StoreDTO> response = server.getStoreInfo(controller.getName(), store1.getStoreName());
//        if(response.ErrorOccurred){
//            alert.setFail(true);
//            alert.setMessage(response.errorMessage);
////            model.addAttribute("controller", controller);
////            model.addAttribute("alert", alert);
////            model.addAttribute("store", store);
//            return "redirect:/store";
//        }
//        store = response.getValue();
//        storeName = store.storeName;
        currentPage = "stock";
        return "redirect:/store";
    }

    // addProductToStock
    @PostMapping("/addProductToStock")
    public String addProductToStock(@ModelAttribute Product product){
        ResponseT<Boolean> response = server.addNewProductToStock(controller.getName(), store.storeName,
                product.getName(), product.getCategory(), product.getPrice(),
                product.getDescription(), product.getAmount());
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/stock";
        }
        if(response.getValue()){
            alert.setSuccess(true);
            alert.setMessage("Product has been successfully added to stock");
        }
        else {
            alert.setFail(true);
            alert.setMessage("Product does not removed from stock");
        }
        return "redirect:/stock";
    }

    // removeProductFromStock
    @PostMapping("/removeProductFromStock")
    public String removeProductFromStock(@ModelAttribute Product product){
        ResponseT<Boolean> response = server.removeProductFromStock(controller.getName(), store.storeName, product.getName());
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/stock";
        }
        if(response.getValue()){
            alert.setSuccess(true);
            alert.setMessage("Product has been successfully removed from stock");
        }
        else {
            alert.setFail(true);
            alert.setMessage("Product does not removed from stock");
        }
        return "redirect:/stock";
    }

    // updateProductPrice
    @PostMapping("/updateProductPrice")
    public String updateProductPrice(@ModelAttribute Product product){
        ResponseT<Boolean> response = server.updateProductPrice(controller.getName(), store.storeName, product.getName(), product.getPrice());
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/stock";
        }
        if(response.getValue()){
            alert.setSuccess(true);
            alert.setMessage("Product price has been successfully updated");
        }
        else {
            alert.setFail(true);
            alert.setMessage("Product price does not updated");
        }
        return "redirect:/stock";
    }

    // updateProductAmountInStock
    // TODO:

//    ------------------------- WORKERS -------------------------
    @GetMapping("/workers")
    public String getWorkers(){
//        workers = buildWorkers(store);
        currentPage = "workers";
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
//        workers = buildWorkers(store);
        alert.setSuccess(true);
        alert.setMessage(appoint.getMemberName() + " is appointed as " + appointedAs);
        return "redirect:/store";
    }

    @PostMapping("/removeOwner")
    public String removeOwner(@RequestParam String memberName){
        ResponseT<Boolean> response = server.removeOwnerByHisAppointer(controller.getName(), store.storeName, memberName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/store";
        }
//        workers = buildWorkers(store);
        alert.setSuccess(true);
        alert.setMessage(memberName + " has been removed");
        return "redirect:/store";
    }


//    ------------------------- DEALS -------------------------
    @GetMapping("/deals")
    public String getSeals(){
        currentPage = "deals";
        return "redirect:/store";
    }

    private Map<String, List<Worker>> buildWorkers(StoreDTO store){
        Map<String, List<Worker>> workers = new LinkedHashMap<>();
        List<Worker> names = new ArrayList<>();
        names.add(new Worker(store.founderName, "Founder"));
        workers.put("Founder", names);
        names = new ArrayList<>();
        for(String owner : store.ownersNames){
            names.add(new Worker(owner, "Owner"));
        }
        workers.put("Owners", names);
        names = new ArrayList<>();
        for(String manager : store.managersNames){
//            permission = server.getManagerPermission(...)
            names.add(new Worker(manager, "Manager" /*permissions*/));
        }
        workers.put("Managers", names);
        return workers;
    }


}
