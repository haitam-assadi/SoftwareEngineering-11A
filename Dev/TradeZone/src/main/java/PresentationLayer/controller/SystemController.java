package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.DealDTO;
import DTO.MemberDTO;
import DTO.StoreDTO;
import PresentationLayer.model.*;
import PresentationLayer.model.System;
import ServiceLayer.ResponseT;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class SystemController {
    private Server server = Server.getInstance();
    private GeneralModel controller;
    System system; // = new System()
    String currentPage = "allStores"; // currentPage = {allStores, allUsers, systemManagers}
    Store store;
    Member member;
//    Alert alert = Alert.getInstance();
    Alert alert;

    @GetMapping("/system")
    public String systemManager(HttpServletRequest request, Model model){
//        system = null;
        store = null;
        member = null;
        currentPage = "allStores";
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            alert = controller.getAlert();
            if(request.getSession().getAttribute("currentSystemPage") != null)
                currentPage = (String) request.getSession().getAttribute("currentSystemPage");
            if(request.getSession().getAttribute("storeModel") != null)
                store = (Store) request.getSession().getAttribute("storeModel");
            if(request.getSession().getAttribute("member") != null)
                member = (Member) request.getSession().getAttribute("member");
            if(request.getSession().getAttribute("system") != null)
                system = (System) request.getSession().getAttribute("system");
            else
                system = new System();
        }
        if(currentPage.equals("allStores")){
            ResponseT<List<String>> response = server.getAllStoresNames();
            if(response.ErrorOccurred){
                alert.setFail(true);
                alert.setMessage(response.errorMessage);
            }
            else
                system.setAllStores(response.getValue());
        }
        else if(currentPage.equals("systemManagers")){
            ResponseT<Set<String>> response = server.getAllSystemManagers(controller.getName());
            if(response.ErrorOccurred){
                alert.setFail(true);
                alert.setMessage(response.errorMessage);
            }
            else
                system.setAllSystemManagers(response.getValue());
        }
        else if(currentPage.equals("allUsers")){
            ResponseT<List<String>> response = server.getAllGuests();
            if(response.ErrorOccurred){
                alert.setFail(true);
                alert.setMessage(response.errorMessage);
            }
            else
                system.setAllGuests(response.getValue());

            response = server.getAllMembers();
            if(response.ErrorOccurred){
                alert.setFail(true);
                alert.setMessage(response.errorMessage);
            }
            else
                system.setAllMembers(response.getValue());

            response = server.getAllLoggedInMembers();
            if(response.ErrorOccurred){
                alert.setFail(true);
                alert.setMessage(response.errorMessage);
            }
            else
                system.setAllOnlineMembers(response.getValue());
        }

        model.addAttribute("controller", controller);
        model.addAttribute("alert", alert.copy());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("system", system);
        model.addAttribute("store", store);
        model.addAttribute("member", member);
        alert.reset();
        request.getSession().setAttribute("controller", controller);
        request.getSession().setAttribute("system", system);
        request.getSession().setAttribute("storeModel", null); // ???
        request.getSession().setAttribute("member", null); // ???
//        alert.reset();
        return "SystemTemplates/system";
    }

    @GetMapping("/allStores")
    public String getAllStores(HttpServletRequest request){
        currentPage = "allStores";
        request.getSession().setAttribute("currentSystemPage", currentPage);
        return "redirect:/system";
    }

    @PostMapping("/storeDeals")
    public String getStoreDeals(HttpServletRequest request, @RequestParam String storeName){
        // TODO: uncomment all lines
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
        }
        ResponseT<List<DealDTO>> response = server.getStoreDeals(controller.getName(), storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/system";
        }
        List<Deal> deals = GeneralModel.buildDeals(response.getValue());
        ResponseT<StoreDTO> response1 = server.getStoreInfo(controller.getName(), storeName);
        if(response1.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response1.errorMessage);
            return "redirect:/system";
        }

//        List<Deal> deals = delete(); // TODO: delete this line and delete() func
        store = new Store(storeName, response1.getValue().isActive, deals); // TODO: response1.getValue().isActive ???
        request.getSession().setAttribute("storeModel", store);
        return "redirect:/system";
    }


    private List<Deal> delete(){
        List<Deal> deals1 = new LinkedList<>();
        Map<String, List<Double>> products = new HashMap<>();
        List<Double> amount_price = new LinkedList<>();
        amount_price.add(0, 5.0);
        amount_price.add(1, 35.0);
        products.put("product1", amount_price);
        Map<String, Double> productPriceMultipleAmount = new LinkedHashMap<>();
        Map<String, Double> productFinalPriceWithDiscount = new LinkedHashMap<>();
        productPriceMultipleAmount.put("product1", 5*35.0);
        productFinalPriceWithDiscount.put("product1", 20.0);
        deals1.add(new Deal("store 1", "24/5/23", controller.getName(), products, 100, productPriceMultipleAmount, productFinalPriceWithDiscount));
        products = new HashMap<>();
        products.put("product2", amount_price);
        products.put("product3", amount_price);
        deals1.add(new Deal("store 1", "24/6/23", "manager 2", products, 200, productPriceMultipleAmount, productFinalPriceWithDiscount));
        deals1.add(new Deal("store 1", "24/7/23", "manager 3", products, 300, productPriceMultipleAmount, productFinalPriceWithDiscount));

        return deals1;
    }

    @GetMapping("/allUsers")
    public String getAllUsers(HttpServletRequest request){
        currentPage = "allUsers";
        request.getSession().setAttribute("currentSystemPage", currentPage);
        return "redirect:/system";
    }

    @PostMapping("/memberDeals")
    public String getMemberDeals(HttpServletRequest request, @RequestParam String memberName){
        // TODO: uncomment all lines
//        if(request.getSession().getAttribute("controller") != null){
//            controller = (GeneralModel) request.getSession().getAttribute("controller");
//        }
//        ResponseT<List<DealDTO>> response = server.getMemberDeals(controller.getName(), memberName);
//        if(response.ErrorOccurred){
//            alert.setFail(true);
//            alert.setMessage(response.errorMessage);
//            return "redirect:/allUsers";
//        }
//        List<Deal> deals = GeneralModel.buildDeals(response.getValue());


//        List<Deal> deals = delete(); // TODO: delete this line and delete() func
//        member = new Member(memberName, deals);
//        request.getSession().setAttribute("member", member);
        return "redirect:/allUsers";
    }

    @PostMapping("/memberInfo")
    public String getMemberInfo(HttpServletRequest request, @RequestParam String memberName){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
        }
        ResponseT<MemberDTO> response = server.getMemberInfo(controller.getName(), memberName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/allUsers";
        }
        MemberDTO memberDTO = response.getValue();
        List<Deal> deals = GeneralModel.buildDeals(memberDTO.memberDeals); // TODO: uncomment
//        List<Deal> deals = delete(); // TODO: delete
        member = new Member(memberDTO.username, memberDTO.memberStores, deals, !server.isSystemManager(memberName).ErrorOccurred);
        request.getSession().setAttribute("member", member);
        return "redirect:/allUsers";
    }

    @PostMapping("/removeMember")
    public String removeMember(HttpServletRequest request, @RequestParam String memberName){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
        }
        ResponseT<Boolean> response = server.removeMemberBySystemManager(controller.getName(), memberName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return getMemberInfo(request, memberName); // TODO: ???
//            return "redirect:/allUsers";
        }
        alert.setSuccess(true);
        alert.setMessage("Member " + memberName + " is removed from the system");
        return "redirect:/allUsers";
    }

    @GetMapping("/systemManagers")
    public String getSystemManagers(HttpServletRequest request){
        currentPage = "systemManagers";
        request.getSession().setAttribute("currentSystemPage", currentPage);
        return "redirect:/system";
    }

    @PostMapping("/appointSystemManager")
    public String appointSManager(HttpServletRequest request, @RequestParam String memberName){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            alert = controller.getAlert();
        }
        ResponseT<Boolean> response = server.AppointMemberAsSystemManager(controller.getName(), memberName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
//            return "redirect:/systemManagers";
        }
        else{
            alert.setSuccess(true);
            alert.setMessage(memberName + " is appointed as System Manager");
        }
        request.getSession().setAttribute("controller", controller);
        return "redirect:/systemManagers";
    }
}
