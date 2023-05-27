package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.ProductDTO;
import DTO.StoreDTO;
import PresentationLayer.model.*;
import ServiceLayer.ResponseT;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class StoreController {
    private Server server = Server.getInstance();
    private GeneralModel controller;
    StoreDTO store;
    String storeName;
    Map<String, List<Worker>> workers;
    List<Deal> deals;
    String currentPage = "stock";
    Alert alert = Alert.getInstance();

    @GetMapping("/store")
    public String showStore(HttpServletRequest request, Model model){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("currentPage") != null)
                currentPage = (String) request.getSession().getAttribute("currentPage");
            if(request.getSession().getAttribute("store") != null) {
                store = (StoreDTO) request.getSession().getAttribute("store");
                storeName = store.storeName;
            }
            if(request.getSession().getAttribute("workers") != null)
                workers = (Map<String, List<Worker>>) request.getSession().getAttribute("workers");
            if(request.getSession().getAttribute("deals") != null)
                deals = (List<Deal>) request.getSession().getAttribute("deals");
        }
        ResponseT<StoreDTO> response = server.getStoreInfo(controller.getName(), storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
//            model.addAttribute("controller", controller);
//            model.addAttribute("alert", alert.copy());
//            model.addAttribute("store", store);
//            model.addAttribute("workers", workers);
//            model.addAttribute("currentPage", currentPage);
//            model.addAttribute("deals", deals);
            return "redirect:/myStores";
        }
        else {
            store = response.getValue();
            storeName = store.storeName;
            workers = buildWorkers(store);
        }
        model.addAttribute("controller", controller);
        model.addAttribute("alert", alert.copy());
        model.addAttribute("store", store);
        model.addAttribute("workers", workers);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("deals", deals);
        alert.reset();
//        currentPage = "stock"; // ???
        return "storeTemplates/store";
    }


    @PostMapping("/store")
//    @ModelAttribute Store store1
    public String showStore(HttpServletRequest request, @RequestParam String storeName){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
        }
        ResponseT<StoreDTO> response = server.getStoreInfo(controller.getName(), storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
//            model.addAttribute("controller", controller);
//            model.addAttribute("alert", alert);
//            model.addAttribute("store", store);
//            return "redirect:/store";
        }
        else{
            store = response.getValue();
            this.storeName = store.storeName;
            currentPage = "stock";
            request.getSession().setAttribute("store", store);
            request.getSession().setAttribute("currentPage", currentPage);
        }
        return "redirect:/store";
    }

    @PostMapping("/closeStore")
    public String closeStore(HttpServletRequest request){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
        }
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
    public String getStock(HttpServletRequest request){
        currentPage = "stock";
        request.getSession().setAttribute("currentPage", currentPage);
        return "redirect:/store";
    }

    // updateProductInfo
    @PostMapping("/updateProductInfo")
    public String updateProductInfo(HttpServletRequest request, @ModelAttribute Product product){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }
        ProductDTO p = getProductFromStore(store, product.getName());
        ResponseT<Boolean> response;
        if(p == null)
            return "redirect:/stock";
        if(!product.getDescription().equals(p.description)){
            response = server.updateProductDescription(controller.getName(), store.storeName, product.getName(), product.getDescription());
            if(response.ErrorOccurred){
                alert.setFail(true);
                alert.setMessage(response.errorMessage);
                return "redirect:/stock";
            }
        }
        if(!product.getPrice().equals(p.price)){
            response = server.updateProductPrice(controller.getName(), store.storeName, product.getName(), product.getPrice());
            if(response.ErrorOccurred){
                alert.setFail(true);
                alert.setMessage(response.errorMessage);
                return "redirect:/stock";
            }
        }
//        if(product.getAmount() != p.amount){ // TODO: uncomment
            response = server.updateProductAmount(controller.getName(), store.storeName, product.getName(), product.getAmount());
            if(response.ErrorOccurred){
                alert.setFail(true);
                alert.setMessage(response.errorMessage);
                return "redirect:/stock";
            }
//        }
        alert.setSuccess(true);
        alert.setMessage("Product info updated successfully");
        return "redirect:/stock";
    }


    // addProductToStock
    @PostMapping("/addProductToStock")
    public String addProductToStock(HttpServletRequest request, @ModelAttribute Product product){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }
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
    public String removeProductFromStock(HttpServletRequest request, @ModelAttribute Product product){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }
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

//    // updateProductPrice
//    @PostMapping("/updateProductPrice")
//    public String updateProductPrice(@ModelAttribute Product product){
//        ResponseT<Boolean> response = server.updateProductPrice(controller.getName(), store.storeName, product.getName(), product.getPrice());
//        if(response.ErrorOccurred){
//            alert.setFail(true);
//            alert.setMessage(response.errorMessage);
//            return "redirect:/stock";
//        }
//        if(response.getValue()){
//            alert.setSuccess(true);
//            alert.setMessage("Product price has been successfully updated");
//        }
//        else {
//            alert.setFail(true);
//            alert.setMessage("Product price does not updated");
//        }
//        return "redirect:/stock";
//    }
//
//    // updateProductAmountInStock
//    @PostMapping("/updateProductAmountInStock")
//    public String updateProductAmountInStock(@ModelAttribute Product product){
//        ResponseT<Boolean> response = server.updateProductAmount(controller.getName(), store.storeName, product.getName(), product.getAmount());
//        if(response.ErrorOccurred){
//            alert.setFail(true);
//            alert.setMessage(response.errorMessage);
//            return "redirect:/stock";
//        }
//        if(response.getValue()){
//            alert.setSuccess(true);
//            alert.setMessage("Product amount has been successfully updated");
//        }
//        else {
//            alert.setFail(true);
//            alert.setMessage("Product amount does not updated");
//        }
//        return "redirect:/stock";
//    }

//    ------------------------- WORKERS -------------------------
    @GetMapping("/workers")
    public String getWorkers(HttpServletRequest request){
//        workers = buildWorkers(store);
        currentPage = "workers";
        request.getSession().setAttribute("currentPage", currentPage);
        return "redirect:/store";
    }

    @PostMapping("/appointMember")
    public String appointMember(HttpServletRequest request, @ModelAttribute Appoint appoint){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }
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
            return "redirect:/workers";
        }
//        workers = buildWorkers(store);
        alert.setSuccess(true);
        alert.setMessage(appoint.getMemberName() + " is appointed as " + appointedAs);
        return "redirect:/workers";
    }

    @PostMapping("/removeOwner")
    public String removeOwner(HttpServletRequest request, @RequestParam String memberName){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }
        ResponseT<Boolean> response = server.removeOwnerByHisAppointer(controller.getName(), store.storeName, memberName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/workers";
        }
//        workers = buildWorkers(store);
        alert.setSuccess(true);
        alert.setMessage(memberName + " has been removed");
        return "redirect:/workers";
    }

    // changePermissions
    //TODO:


//    ------------------------- DEALS -------------------------
    @GetMapping("/deals")
    public String getSeals(HttpServletRequest request){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }
        String referer = request.getHeader("referer");
//        TODO: uncomment
//        ResponseT<List<DealDTO>> response = server.getStoreDeals(controller.getName(), store.storeName);
//        if(response.ErrorOccurred){
//            alert.setFail(true);
//            alert.setMessage(response.errorMessage);
//            return "redirect:/store";
//        }
//        deals = buildDeals(response.getValue());
        deals = delete(); // TODO: delete this line + delete() func
        currentPage = "deals";
        request.getSession().setAttribute("deals", deals);
        request.getSession().setAttribute("currentPage", currentPage);
        return "redirect:" + referer;
//        return "redirect:/store";
    }

    private List<Deal> delete(){
        List<Deal> deals1 = new LinkedList<>();
        Map<String, List<Double>> products = new HashMap<>();
        List<Double> amount_price = new LinkedList<>();
        amount_price.add(0, 5.0);
        amount_price.add(1, 35.0);
        products.put("product1", amount_price);
        deals1.add(new Deal(store.storeName, "24/5/23", controller.getName(), products, 100));
        products.put("product2", amount_price);
        products.put("product3", amount_price);
        deals1.add(new Deal(store.storeName, "24/6/23", controller.getName(), products, 200));
        deals1.add(new Deal(store.storeName, "24/7/23", controller.getName(), products, 300));

        return deals1;
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

//    public static List<Deal> buildDeals(List<DealDTO> dealDTOS){
//        List<Deal> deals = new LinkedList<>();
//        for(DealDTO dealDTO : dealDTOS){
//            Map<String, List<Double>> products = new HashMap<>();
//            for(String productName : dealDTO.products_amount.keySet()){
//                List<Double> amount_price = new LinkedList<>();
//                amount_price.add(0, dealDTO.products_amount.get(productName)*1.0);
//                amount_price.add(1, dealDTO.products_prices.get(productName));
//                products.put(productName, amount_price);
//            }
//            Deal deal = new Deal(dealDTO.storeName, dealDTO.date, dealDTO.username, products, dealDTO.totalPrice);
//            deals.add(deal);
//        }
//        return deals;
//    }

    private ProductDTO getProductFromStore(StoreDTO store, String productName){
        for(ProductDTO p : store.productsInfo){
            if(p.name.equals(productName)){
                return p;
            }
        }
        return null;
    }

}
