package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.DealDTO;
import DTO.OwnerContractDTO;
import DTO.ProductDTO;
import DTO.StoreDTO;
import PresentationLayer.model.*;
import PresentationLayer.model.Policies.AllConstraints;
import PresentationLayer.model.Policies.BagConstraint;
import PresentationLayer.model.Policies.DiscountPolicy;
import ServiceLayer.ResponseT;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class StoreController {
    private Server server = Server.getInstance();
    private GeneralModel controller;
    StoreDTO store;
    String storeName;
    Map<String, List<Worker>> workers;
    List<Deal> deals;
    AllConstraints constraints;
    OwnerContracts ownerContracts;
    String currentPage = "stock";
    Alert alert = Alert.getInstance();

    @GetMapping("/store")
    public String showStore(HttpServletRequest request, Model model){
        store = null;
        workers = null;
        deals = null;
        constraints = null;
        ownerContracts = null;
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
            if(request.getSession().getAttribute("constraints") != null)
                constraints = (AllConstraints) request.getSession().getAttribute("constraints");
            if(request.getSession().getAttribute("ownerContracts") != null)
                ownerContracts = (OwnerContracts) request.getSession().getAttribute("ownerContracts");
        }

        if(controller.getRole(store.storeName) == -1) {
            alert.setFail(true);
            alert.setMessage(controller.getName() + " does not have role in " + store.storeName);
            return "redirect:/myStores";
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
            if(!checkPermissionPage(controller, store.storeName, currentPage))
                currentPage = "stock";
            if(currentPage.equals("workers"))
                workers = buildWorkers(controller.getName(), store);
        }
        request.getSession().setAttribute("store", store);
        request.getSession().setAttribute("workers", workers);

        model.addAttribute("controller", controller);
        model.addAttribute("alert", alert.copy());
        model.addAttribute("store", store);
        model.addAttribute("workers", workers);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("deals", deals);
        model.addAttribute("constraints", constraints);
        model.addAttribute("ownerContracts", ownerContracts);
        alert.reset();
//        currentPage = "stock"; // ???
        return "storeTemplates/store";
    }


    @PostMapping("/store")
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
        Map<ProductDTO, Integer> pAmount = getProductFromStore(store, product.getName());
        if(pAmount == null)
            return "redirect:/stock";
        ProductDTO p = pAmount.keySet().iterator().next();
        ResponseT<Boolean> response;

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
        if(product.getAmount() != pAmount.get(p)){
            response = server.updateProductAmount(controller.getName(), store.storeName, product.getName(), product.getAmount());
            if(response.ErrorOccurred){
                alert.setFail(true);
                alert.setMessage(response.errorMessage);
                return "redirect:/stock";
            }
        }
        alert.setSuccess(true);
        alert.setMessage("Product info has been updated successfully");
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
            alert.setMessage("Product has not been added to stock");
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
            alert.setMessage("Product has not been removed from stock");
        }
        return "redirect:/stock";
    }

//    ------------------------- WORKERS -------------------------
    @GetMapping("/workers")
    public String getWorkers(HttpServletRequest request){
//        workers = buildWorkers(store);
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }
        if(!controller.hasPermission(store.storeName, 3))
            return "redirect:/stock";
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
        String msg;
        ResponseT<Boolean> response;
        if(appoint.getAppointAs().equals("appointAsOwner")){
            appointedAs = "Store Owner";
            response = server.appointOtherMemberAsStoreOwner(controller.getName(), store.storeName, appoint.getMemberName());
            msg = "Your request to appoint " + appoint.getMemberName() + " has been successfully received";
        }
        else { // appointAsManager
            appointedAs = "Store Manager";
            response = server.appointOtherMemberAsStoreManager(controller.getName(), store.storeName, appoint.getMemberName());
            msg = appoint.getMemberName() + " is appointed as " + appointedAs;
        }
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/workers";
        }
//        workers = buildWorkers(store);
        alert.setSuccess(true);
        alert.setMessage(msg);
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
    @PostMapping("/changeManagerPermissions")
    public String changeManagerPermissions(HttpServletRequest request, @RequestParam String managerName, @RequestParam(required = false) String[] perms){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }
        List<Integer> permsIDs = new ArrayList<>();
        if(perms != null){
            for(String id : perms){
                permsIDs.add(Integer.parseInt(id));
            }
        }

        ResponseT<Boolean> response = server.updateManagerPermissionsForStore(controller.getName(), store.storeName, managerName, permsIDs);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/workers";
        }
        alert.setSuccess(true);
        alert.setMessage(managerName + " permissions are updated successfully");
        return "redirect:/workers";
    }


//    ------------------------- DEALS -------------------------
    @GetMapping("/deals")
    public String getDeals(HttpServletRequest request){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }

        if(!controller.hasPermission(store.storeName, 1))
            return "redirect:/stock";

//        String referer = request.getHeader("referer");
//        TODO: uncomment
        ResponseT<List<DealDTO>> response = server.getStoreDeals(controller.getName(), store.storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/store";
        }
        deals = GeneralModel.buildDeals(response.getValue());
//        deals = delete(); // TODO: delete this line + delete() func
        currentPage = "deals";
        request.getSession().setAttribute("deals", deals);
        request.getSession().setAttribute("currentPage", currentPage);
//        return "redirect:" + referer;
        return "redirect:/store";
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
        deals1.add(new Deal(store.storeName, "24/5/23", controller.getName(), products, 100, productPriceMultipleAmount, productFinalPriceWithDiscount));
        products.put("product2", amount_price);
        products.put("product3", amount_price);
        deals1.add(new Deal(store.storeName, "24/6/23", controller.getName(), products, 200, productPriceMultipleAmount, productFinalPriceWithDiscount));
        deals1.add(new Deal(store.storeName, "24/7/23", controller.getName(), products, 300, productPriceMultipleAmount, productFinalPriceWithDiscount));

        return deals1;
    }

    //    ------------------------- BAG CONSTRAINTS -------------------------
    @GetMapping("/bagConstraints")
    public String getBagConstraints(HttpServletRequest request){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
//            if(request.getSession().getAttribute("constraints") != null)
//                constraints = (AllConstraints) request.getSession().getAttribute("constraints");
//            else
//                constraints = new AllConstraints(store.storeName);
        }

        if(!controller.hasPermission(store.storeName, 5))
            return "redirect:/stock";

        constraints = new AllConstraints(store.storeName);
        ResponseT<List<String>> response = server.getAllBagConstraints(controller.getName(), store.storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/store";
        }
        constraints.setAllBagConstraints(response.getValue());
        response = server.getAllPaymentPolicies(controller.getName(), store.storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/store";
        }
        constraints.setActiveBagConstraints(response.getValue());
        currentPage = "bagConstraints";
        request.getSession().setAttribute("constraints", constraints);
        request.getSession().setAttribute("currentPage", currentPage);
        return "redirect:/store";
    }

    @PostMapping("/addBagConstraint")
    public String addBagConstraint(HttpServletRequest request, @ModelAttribute BagConstraint constraint){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }
        ResponseT<Integer> response;
        boolean activate = constraint.getActivate() != null;
        if(constraint.getConstType() == null || constraint.getConstType().equals("")){
            alert.setFail(true);
            alert.setMessage("You have to choose one of the constraints to add");
            return "redirect:/bagConstraints";
        }
        if(constraint.getConstType().equals("type1")){
            response = server.createMaxTimeAtDayProductBagConstraint(controller.getName(), constraint.getStoreName(),
                    constraint.getProductName(0), constraint.getHour(0), constraint.getMinutes(0), activate);
        }
        else if(constraint.getConstType().equals("type2")){
            response = server.createRangeOfDaysProductBagConstraint(controller.getName(), constraint.getStoreName(),
                    constraint.getProductName(1), constraint.getFromYear(0), constraint.getFromMonth(0), constraint.getFromDay(0),
                    constraint.getToYear(0), constraint.getToMonth(0), constraint.getToDay(0), activate);
        }
        else if(constraint.getConstType().equals("type3")){
            response = server.createMaxTimeAtDayCategoryBagConstraint(controller.getName(), constraint.getStoreName(),
                    constraint.getCategoryName(0), constraint.getHour(1), constraint.getMinutes(1), activate);
        }
        else if(constraint.getConstType().equals("type4")){
            response = server.createRangeOfDaysCategoryBagConstraint(controller.getName(), constraint.getStoreName(),
                    constraint.getCategoryName(1), constraint.getFromYear(1), constraint.getFromMonth(1), constraint.getFromDay(1),
                    constraint.getToYear(1), constraint.getToMonth(1), constraint.getToDay(1), activate);
        }
        else if(constraint.getConstType().equals("type5")){
            response = server.createMinProductAmountAllContentBagConstraint(controller.getName(), constraint.getStoreName(),
                    constraint.getProductName(2), constraint.getMin(0), activate);
        }
        else if(constraint.getConstType().equals("type6")){
            response = server.createMaxProductAmountAllContentBagConstraint(controller.getName(), constraint.getStoreName(),
                    constraint.getProductName(3), constraint.getMax(0), activate);
        }
        else if(constraint.getConstType().equals("type7")){
            response = server.createAndBagConstraint(controller.getName(), constraint.getStoreName(),
                    constraint.getFirstID(0),constraint.getSecondID(0), activate);
        }
        else if(constraint.getConstType().equals("type8")){
            response = server.createOrBagConstraint(controller.getName(), constraint.getStoreName(),
                    constraint.getFirstID(1),constraint.getSecondID(1), activate);
        }
        else { //if(constraint.getConstType().equals("type9")){
            response = server.createOnlyIfBagConstraint(controller.getName(), constraint.getStoreName(),
                    constraint.getFirstID(2),constraint.getSecondID(2), activate);
        }
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/bagConstraints";
        }
        alert.setSuccess(true);
        alert.setMessage("New constraint has been added successfully to " + constraint.getStoreName());
        return "redirect:/bagConstraints";
    }

    @PostMapping("/activate")
    public String activate(HttpServletRequest request, @RequestParam String choice, @RequestParam int id){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }
        ResponseT<Boolean> response;
        String msg;
        if(choice.equals("activate")){
            msg = "activated";
            response = server.addConstraintAsPaymentPolicy(controller.getName(), store.storeName, id);
        }
        else{ // (choice.equals("deactivate"))
            msg = "deactivated";
            response = server.removeConstraintFromPaymentPolicies(controller.getName(), store.storeName, id);
        }
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/bagConstraints";
        }
        alert.setSuccess(true);
        alert.setMessage("Constraint has " + msg + " successfully");
        return "redirect:/bagConstraints";
    }


    //    ------------------------- DISCOUNT POLICIES -------------------------
    @GetMapping("/discountPolicies")
    public String getDiscountPolicies(HttpServletRequest request){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
//            if(request.getSession().getAttribute("constraints") != null)
//                constraints = (AllConstraints) request.getSession().getAttribute("constraints");
//            else
//                constraints = new AllConstraints(store.storeName);
        }

        if(!controller.hasPermission(store.storeName, 4))
            return "redirect:/stock";

        constraints = new AllConstraints(store.storeName);
        ResponseT<List<String>> response = server.getAllBagConstraints(controller.getName(), store.storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/store";
        }
        constraints.setAllBagConstraints(response.getValue());

        response = server.getAllCreatedDiscountPolicies(controller.getName(), store.storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/store";
        }
        constraints.setAllDiscountPolicies(response.getValue());

        response = server.getAllStoreDiscountPolicies(controller.getName(), store.storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/store";
        }
        constraints.setActiveDiscountPolicies(response.getValue());

        currentPage = "discountPolicies";
        request.getSession().setAttribute("constraints", constraints);
        request.getSession().setAttribute("currentPage", currentPage);
        return "redirect:/store";
    }

    @PostMapping("/addDiscountPolicy")
    public String addDiscountPolicy(HttpServletRequest request, @ModelAttribute DiscountPolicy discount){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }
        ResponseT<Integer> response;
        int constraintId = -1;
        boolean activate = discount.getActivate() != null;
        if(discount.getConstraintId() != null && !discount.getConstraintId().equals(""))
            constraintId = Integer.parseInt(discount.getConstraintId());

        if(discount.getDiscountType() == null || discount.getDiscountType().equals("")){
            alert.setFail(true);
            alert.setMessage("You have to choose one of the policies to add");
            return "redirect:/discountPolicies";
        }
        if(discount.getDiscountType().equals("type1")){
            response = server.createProductDiscountPolicyWithConstraint(controller.getName(), discount.getStoreName(),
                    discount.getProductName(), discount.getPercent(0), constraintId, activate);
        }
        else if(discount.getDiscountType().equals("type2")){
            response = server.createCategoryDiscountPolicyWithConstraint(controller.getName(), discount.getStoreName(),
                    discount.getCategoryName(), discount.getPercent(1), constraintId, activate);
        }
        else if(discount.getDiscountType().equals("type3")){
            response = server.createAllStoreDiscountPolicyWithConstraint(controller.getName(), discount.getStoreName(),
                    discount.getPercent(2), constraintId, activate);
        }
        else if(discount.getDiscountType().equals("type4")){
            response = server.createMaxValDiscountPolicyWithConstraint(controller.getName(), discount.getStoreName(),
                    discount.getFirstID(0), discount.getSecondID(0), constraintId, activate);
        }
        else { // if(constraint.getConstType().equals("type5")){
            response = server.createAdditionDiscountPolicyWithConstraint(controller.getName(), discount.getStoreName(),
                    discount.getFirstID(1), discount.getSecondID(1), constraintId, activate);
        }

        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/discountPolicies";
        }
        alert.setSuccess(true);
        alert.setMessage("New Discount policy has been added successfully to " + discount.getStoreName());
        return "redirect:/discountPolicies";
    }


    @PostMapping("/activateDiscount")
    public String activateDiscount(HttpServletRequest request, @RequestParam String choice, @RequestParam int id){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }
        ResponseT<Boolean> response;
        String msg;
        if(choice.equals("activate")){
            msg = "activated";
            response = server.addAsStoreDiscountPolicy(controller.getName(), store.storeName, id);
        }
        else{ // (choice.equals("deactivate"))
            msg = "deactivated";
            response = server.removeFromStoreDiscountPolicies(controller.getName(), store.storeName, id);
        }
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/discountPolicies";
        }
        alert.setSuccess(true);
        alert.setMessage("Constraint has " + msg + " successfully");
        return "redirect:/discountPolicies";
    }

    //    ------------------------- MY CONTRACTS -------------------------

    @GetMapping("/myContracts")
    public String getMyContracts(HttpServletRequest request){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }
        ownerContracts = new OwnerContracts(controller.getName(), store.storeName);
        ResponseT<List<OwnerContractDTO>> response = server.getMyCreatedContracts(controller.getName(), store.storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/store";
        }
        ownerContracts.setInProgressContracts(response.getValue());

        response = server.getAlreadyDoneContracts(controller.getName(), store.storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/store";
        }
        ownerContracts.setDoneContracts(response.getValue());
        currentPage = "myContracts";

        request.getSession().setAttribute("ownerContracts", ownerContracts);
        request.getSession().setAttribute("currentPage", currentPage);
        return "redirect:/store";
    }


    //    ------------------------- OTHER CONTRACTS -------------------------

    @GetMapping("/otherContracts")
    public String getOtherContracts(HttpServletRequest request){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }
        ownerContracts = new OwnerContracts(controller.getName(), store.storeName);
        ResponseT<List<OwnerContractDTO>> response = server.getPendingContractsForOwner(controller.getName(), store.storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/store";
        }
        ownerContracts.setPendingContracts(response.getValue());
        currentPage = "otherContracts";

        request.getSession().setAttribute("ownerContracts", ownerContracts);
        request.getSession().setAttribute("currentPage", currentPage);
        return "redirect:/store";
    }


    @PostMapping("/fillContract")
    public String fillOwnerContract(HttpServletRequest request, @RequestParam String storeName,
                                    @RequestParam String newOwner, @RequestParam boolean decision){
        if(request.getSession().getAttribute("controller") != null){
            controller = (GeneralModel) request.getSession().getAttribute("controller");
            if(request.getSession().getAttribute("store") != null)
                store = (StoreDTO) request.getSession().getAttribute("store");
        }
        ResponseT<Boolean> response = server.fillOwnerContract(controller.getName(), storeName, newOwner, decision);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return "redirect:/otherContracts";
        }
        alert.setSuccess(true);
        alert.setMessage("Your decision has been received !");
        return "redirect:/otherContracts";
    }


    //    ------------------------- PRIVATE METHODS -------------------------

    private Map<String, List<Worker>> buildWorkers(String ownerName, StoreDTO store){
        Map<String, List<Worker>> workers = new LinkedHashMap<>();
        List<Worker> names = new ArrayList<>();
        names.add(new Worker(store.founderName, "Founder"));
        workers.put("Founder", names);
        names = new ArrayList<>();
        for(String owner : store.ownersNames){
            names.add(new Worker(owner, "Owner"));
        }
        workers.put("Owners", names);

        ResponseT<List<String>> response = server.getAllPermissions(ownerName, store.storeName);
        if(response.ErrorOccurred){
            alert.setFail(true);
            alert.setMessage(response.errorMessage);
            return new LinkedHashMap<>();
        }
        List<String> allPermissions = response.getValue();

        names = new ArrayList<>();
        for(String manager : store.managersNames){
            ResponseT<List<Integer>> responseManager = server.getManagerPermissionsForStore(ownerName, store.storeName, manager);
            if(responseManager.ErrorOccurred){
                alert.setFail(true);
                alert.setMessage(responseManager.errorMessage);
                return new LinkedHashMap<>();
            }
            List<Integer> managerPerms = responseManager.getValue();
            Worker worker = new Worker(manager, "Manager");
            worker.buildAndSetPermissions(allPermissions, managerPerms);
            names.add(worker);
        }
        workers.put("Managers", names);
        return workers;
    }

    private Map<ProductDTO, Integer> getProductFromStore(StoreDTO store, String productName){
        for(ProductDTO p : store.productsInfoAmount.keySet()){
            if(p.name.equals(productName)){
                Map<ProductDTO, Integer> map = new HashMap<>();
                map.put(p, store.productsInfoAmount.get(p));
                return map;
            }
        }
        return null;
    }

    private boolean checkPermissionPage(GeneralModel controller, String storeName, String currentPage) {
        if(currentPage.equals("workers") && !controller.hasPermission(storeName, 3))
            return false;
        else if(currentPage.equals("deals") && !controller.hasPermission(storeName, 1))
            return false;
        else if(currentPage.equals("bagConstraints") && !controller.hasPermission(storeName, 5))
            return false;
        else if(currentPage.equals("discountPolicies") && !controller.hasPermission(storeName, 4))
            return false;
        else
            return true;
    }

}
