package DomainLayer;

import DTO.*;
import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.Controller.StoreMapper;
import DataAccessLayer.DALService;
import DomainLayer.Controllers.StoreController;
import DomainLayer.Controllers.UserController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jdk.jshell.spi.ExecutionControl;
import net.minidev.json.JSONObject;

import java.io.*;
import java.util.*;

public class Market {
    UserController userController;
    StoreController storeController;
    PaymentService paymentService;
    ShipmentService shipmentService;

    public static boolean dbFlag;
    public Market(){
        Market.dbFlag = false;                          //DEFAULT
        this.userController = new UserController();
        this.storeController = new StoreController();
//        paymentService = new PaymentService("https://php-server-try.000webhostapp.com/");
//        shipmentService = new ShipmentService("https://php-server-try.000webhostapp.com/");
    }
    public void setPaymentService(PaymentService paymentService){
        this.paymentService=paymentService;
    }
    public void setShipmentService(ShipmentService shipmentService){
        this.shipmentService=shipmentService;
    }
    //TODO: Implement this requirements: 2.5, 3.3(maybe), 4.12(for storeManager)
    //TODO: closeStore req is not implemented as it should be , i(Ahmad) didn't pay attention to close stores handling..


    public String firstManagerInitializer() throws Exception {
        return userController.firstManagerInitializer();
    }

    public String enterMarket(){
        return userController.loginAsGuest();
    }
    public boolean externalConnectionsHandShake() throws Exception {
        paymentService.handshake();
        shipmentService.handshake();
        return true;
    }

    public List<String> getAllGuests(/*String managerName*/){
        return userController.getAllGuests();
    }

    public List<String> getAllMembers() throws Exception {
        return userController.getAllMambers();
    }
    public List<String> getAllLoggedInMembers() throws Exception {
        return userController.getAllLoggedInMembers();
    }
    public List<String> getAllStoresNames() throws Exception { // TODO: add to market and service
        return storeController.getAllStoresNames();
    }


    public boolean exitMarket(String userName) throws Exception {
        return userController.exitMarket(userName);
    }

    public boolean register(String guestUserName, String newMemberUserName, String password) throws Exception {
        return userController.register(guestUserName, newMemberUserName, password);
    }
    public String login(String guestUserName, String MemberUserName, String password) throws Exception {
        return userController.login(guestUserName, MemberUserName, password);
    }
    public String memberLogOut(String memberUserName) throws Exception {
        return userController.memberLogOut(memberUserName);
    }

    public StoreDTO getStoreInfo(String userName, String storeName) throws Exception {
        userController.assertIsGuestOrLoggedInMember(userName);
        boolean active = storeController.assertisActive(storeName);
        if(active) return storeController.getStoreInfo(storeName);
        else {
            Store store = storeController.getStore(storeName);
            if(!userController.IsOwnerOrSystemManager(userName,store))
                throw new Exception("store :" + storeName + "is closed");
            else
                return storeController.getStoreInfo(storeName);
        }
    }
    public ProductDTO getProductInfoFromStore(String userName, String storeName, String productName) throws Exception {
        userController.assertIsGuestOrLoggedInMember(userName);
        return storeController.getProductInfoFromStore(storeName, productName);
    }

    public Integer getProductAmountInStore(String userName, String storeName, String productName) throws Exception {
        userController.assertIsGuestOrLoggedInMember(userName);
        return storeController.getProductAmountInStore(storeName, productName);
    }


    public List<ProductDTO> getProductInfoFromMarketByName(String userName, String productName) throws Exception {
        userController.assertIsGuestOrLoggedInMember(userName);
        return storeController.getProductInfoFromMarketByName(productName);
    }

    public List<ProductDTO> getProductInfoFromMarketByCategory(String userName, String categoryName) throws Exception {
        userController.assertIsGuestOrLoggedInMember(userName);
        return storeController.getProductInfoFromMarketByCategory(categoryName);
    }

    public List<ProductDTO> getProductInfoFromMarketByKeyword(String userName, String keyword) throws Exception{
        userController.assertIsGuestOrLoggedInMember(userName);
        return storeController.getProductInfoFromMarketByKeyword(keyword);
    }

    public List<ProductDTO> filterByPrice(String userName, List<ProductDTO> productsInfo, Integer minPrice, Integer maxPrice) throws Exception {
        userController.assertIsGuestOrLoggedInMember(userName);
        return storeController.filterByPrice(productsInfo, minPrice, maxPrice);
    }

    public List<ProductDTO> filterByCategory(String userName, List<ProductDTO> productsInfo, String categoryName) throws Exception {
        userController.assertIsGuestOrLoggedInMember(userName);
        return storeController.filterByCategory(productsInfo,categoryName);
    }

    public List<ProductDTO> filterByProductRate(String userName, List<ProductDTO> productsInfo, Integer productRate) throws ExecutionControl.NotImplementedException {
        // TODO: low priority Don't test it
        throw new ExecutionControl.NotImplementedException("");
    }

    public List<ProductDTO> filterByStoreRate(String userName, List<ProductDTO> productsInfo, Integer storeRate) throws ExecutionControl.NotImplementedException {
        // TODO: low priority Don't test it
        throw new ExecutionControl.NotImplementedException("");
    }

    public boolean addToCart(String userName, String storeName, String productName, Integer amount) throws Exception {
        userController.assertIsGuestOrLoggedInMember(userName);
        storeController.assertIsStore(storeName);
        storeController.isActiveStore(storeName);
        User user = userController.getUser(userName);
        return user.addToCart(storeController.getStore(storeName), productName, amount);
    }
    public boolean removeFromCart(String userName, String storeName, String productName) throws Exception {
        userController.assertIsGuestOrLoggedInMember(userName);
        storeController.assertIsStore(storeName);
        User user = userController.getUser(userName);
        return user.removeFromCart(storeController.getStore(storeName), productName);
    }
    public boolean changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount) throws Exception {
        userController.assertIsGuestOrLoggedInMember(userName);
        storeController.assertIsStore(storeName);
        User user = userController.getUser(userName);
        return user.changeProductAmountInCart(storeController.getStore(storeName), productName, newAmount);
    }
    public List<BagDTO> getCartContent(String userName) throws Exception {
        userController.assertIsGuestOrLoggedInMember(userName);
        User user = userController.getUser(userName);
        return user.getCartContent();
    }
    public StoreDTO createStore(String memberUserName, String newStoreName) throws Exception {
        userController.assertIsNotSystemManager(memberUserName);
        userController.assertIsMemberLoggedIn(memberUserName);
        Member member = userController.getMember(memberUserName);
        Store store = storeController.createStore(newStoreName);
        StoreFounder newFounder = member.appointMemberAsStoreFounder(store);
        if (Market.dbFlag) {
            DALService.saveStore(store, newFounder, member);
        }
        return store.getStoreInfo();
    }
    public boolean addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, Double price, String description, Integer amount) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.addNewProductToStock(memberUserName,storeName,nameProduct,category,price,description,amount);
    }
    public boolean removeProductFromStock(String memberUserName, String storeName, String productName) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.removeProductFromStock(memberUserName, storeName, productName);
    }
    public boolean updateProductDescription(String memberUserName, String storeName, String productName, String newProductDescription) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.updateProductDescription(memberUserName, storeName, productName, newProductDescription);
    }

    public boolean updateProductAmount(String memberUserName, String storeName, String productName, Integer newAmount) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.updateProductAmount(memberUserName, storeName, productName, newAmount);
    }

    public boolean updateProductPrice(String memberUserName, String storeName, String productName, Double newPrice) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.updateProductPrice(memberUserName, storeName, productName, newPrice);
    }

    public Boolean AppointMemberAsSystemManager(String managerName,String otherMemberName) throws Exception {
        return userController.AppointMemberAsSystemManager(managerName,otherMemberName);
    }

    public boolean appointOtherMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName) throws Exception {
        userController.assertIsNotSystemManager(newOwnerUserName);
        Store store = storeController.getStore(storeName);
        return userController.appointOtherMemberAsStoreOwner(memberUserName,store,newOwnerUserName);
    }

    public boolean removeOwnerByHisAppointer(String memberUserName, String storeName, String ownerUserName) throws Exception {
        Store store = storeController.getStore(storeName);
        return userController.removeOwnerByHisAppointer(memberUserName,store,ownerUserName);
    }

    public boolean fillOwnerContract(String memberUserName, String storeName, String newOwnerUserName, Boolean decisions) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        userController.assertIsMember(newOwnerUserName);
        Store store = storeController.getStore(storeName);
        return store.fillOwnerContract(memberUserName, newOwnerUserName, decisions);
    }

    public List<OwnerContractDTO> getAlreadyDoneContracts(String memberUserName, String storeName) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        Store store = storeController.getStore(storeName);
        return store.getAlreadyDoneContracts(memberUserName);
    }

    public List<OwnerContractDTO> getMyCreatedContracts(String memberUserName, String storeName) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        Store store = storeController.getStore(storeName);
        return store.getMyCreatedContracts(memberUserName);
    }

    public List<OwnerContractDTO> getPendingContractsForOwner(String memberUserName, String storeName) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        Store store = storeController.getStore(storeName);
        return store.getPendingContractsForOwner(memberUserName);
    }

    public boolean appointOtherMemberAsStoreManager(String memberUserName, String storeName, String newManagerUserName/*,list<Integer> permissions*/) throws Exception {
        userController.assertIsNotSystemManager(newManagerUserName);
        Store store = storeController.getStore(storeName); // TODO: MAYBE WE NEED TO CHECK IF STORE IS ACTIVE
        return userController.appointOtherMemberAsStoreManager(memberUserName,store,newManagerUserName);
    }

    public boolean updateManagerPermissionsForStore(String ownerUserName, String storeName, String managerUserName, List<Integer> newPermissions) throws Exception {
        userController.assertIsMemberLoggedIn(ownerUserName);
        userController.assertIsMember(managerUserName);
        Store store = storeController.getStore(storeName);
        return store.updateManagerPermissionsForStore(ownerUserName, managerUserName, newPermissions);
    }

    public List<Integer> getManagerPermissionsForStore(String ownerUserName, String storeName, String managerUserName) throws Exception {
        userController.assertIsMemberLoggedIn(ownerUserName);
        userController.assertIsMember(managerUserName);
        Store store = storeController.getStore(storeName);
        return store.getManagerPermissionsForStore(ownerUserName, managerUserName);
    }

    public List<String> getAllPermissions(String ownerUserName, String storeName) throws Exception {
        userController.assertIsMemberLoggedIn(ownerUserName);
        Store store = storeController.getStore(storeName);
        return store.getAllPermissions(ownerUserName);
    }


    public boolean changeManagerPermissions(String memberUserName, String storeName, String managerUserName) throws ExecutionControl.NotImplementedException {
        // TODO: low priority , DON'T test it, dont forget to change function parameters
        throw new ExecutionControl.NotImplementedException("");
    }

    public boolean closeStore(String memberUserName, String storeName) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.closeStore(memberUserName, storeName);
        //TODO: adel, not completed.
    }

    public List<MemberDTO> getStoreWorkersInfo(String memberUserName, String storeName) throws Exception {
        // TODO: low priority , DON'T test it, dont forget to change function parameters
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.getStoreWorkersInfo(memberUserName, storeName);
    }

    public List<DealDTO> getStoreDeals(String memberUserName, String storeName) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        boolean isSystemManager = userController.isSystemManager(memberUserName);
        return this.storeController.getStoreDeals(memberUserName, storeName, isSystemManager);
    }

    public List<DealDTO> getMemberDeals(String memberUserName, String otherMemberUserName) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        boolean isSystemManager = userController.isSystemManager(memberUserName);
        return this.userController.getUserDeals(memberUserName,otherMemberUserName,isSystemManager);
    }

    public boolean purchaseCartByCreditCard(String userName, String cardNumber, String month, String year, String holder, String cvv, String id, String receiverName,String shipmentAddress,String shipmentCity,String shipmentCountry,String zipCode) throws Exception {
        int transactionId=0;
        int supplyId = 0;
        try{
            userController.assertIsGuestOrLoggedInMember(userName);
            userController.validateStorePolicy(userName);
            userController.validateAllProductsAmounts(userName);
            userController.validateAllStoresIsActive(userName);
            Double priceBeforeDiscount = userController.getCartPriceBeforeDiscount(userName);
            Double priceAfterDiscount=userController.getCartPriceAfterDiscount(userName);
            transactionId = paymentService.pay(priceAfterDiscount, cardNumber, month, year,holder, cvv, id);
            supplyId = shipmentService.supply(receiverName, shipmentAddress, shipmentCity, shipmentCountry, zipCode);
            return userController.updateStockAmount(userName);
        }
        catch (shipmentException e){
            paymentService.cancelPay(transactionId);
            throw e;
        }
        catch (updateAmountException e){
            paymentService.cancelPay(transactionId);
            shipmentService.cancelSupply(supplyId);
            throw e;
        }
    }

    public Double getCartPriceBeforeDiscount(String memberUserName) throws Exception {
//        userController.assertIsMemberLoggedIn(memberUserName);
        userController.assertIsGuestOrLoggedInMember(memberUserName);
        return userController.getCartPriceBeforeDiscount(memberUserName);
    }

    public Double getCartPriceAfterDiscount(String memberUserName) throws Exception {
//        userController.assertIsMemberLoggedIn(memberUserName);
        userController.assertIsGuestOrLoggedInMember(memberUserName);
        return userController.getCartPriceAfterDiscount(memberUserName);
    }

    public Integer createMaxTimeAtDayProductBagConstraint(String memberUserName, String storeName, String productName, int hour, int minute, boolean addAsStorePaymentPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createMaxTimeAtDayProductBagConstraint(memberUserName,storeName,productName,hour,minute ,addAsStorePaymentPolicy);
    }

    public Integer createRangeOfDaysProductBagConstraint(String memberUserName, String storeName, String productName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createRangeOfDaysProductBagConstraint(memberUserName,storeName,productName, fromYear,fromMonth,fromDay,toYear,toMonth,toDay, addAsStorePaymentPolicy);
    }


    public Integer createMaxTimeAtDayCategoryBagConstraint(String memberUserName, String storeName, String categoryName, int hour, int minute, boolean addAsStorePaymentPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createMaxTimeAtDayCategoryBagConstraint(memberUserName,storeName,categoryName,hour,minute ,addAsStorePaymentPolicy);
    }

    public Integer createRangeOfDaysCategoryBagConstraint(String memberUserName, String storeName, String categoryName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createRangeOfDaysCategoryBagConstraint(memberUserName,storeName,categoryName, fromYear,fromMonth,fromDay,toYear,toMonth,toDay, addAsStorePaymentPolicy);
    }


    public Integer createMaxProductAmountAllContentBagConstraint(String memberUserName, String storeName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createMaxProductAmountAllContentBagConstraint(memberUserName,storeName,productName,amountLimit,addAsStorePaymentPolicy);
    }

    public Integer createMinProductAmountAllContentBagConstraint(String memberUserName, String storeName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createMinProductAmountAllContentBagConstraint(memberUserName,storeName,productName,amountLimit,addAsStorePaymentPolicy);
    }

    public Integer createAndBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createAndBagConstraint(memberUserName,storeName,firstBagConstraintId,secondBagConstraintId,addAsStorePaymentPolicy);
    }
    public Integer createOrBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createOrBagConstraint(memberUserName,storeName,firstBagConstraintId,secondBagConstraintId,addAsStorePaymentPolicy);
    }

    public Integer createOnlyIfBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createOnlyIfBagConstraint(memberUserName,storeName,firstBagConstraintId,secondBagConstraintId,addAsStorePaymentPolicy);
    }
    public boolean addConstraintAsPaymentPolicy(String memberUserName, String storeName, Integer bagConstraintId) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.addConstraintAsPaymentPolicy(memberUserName,storeName,bagConstraintId);
    }

    public boolean removeConstraintFromPaymentPolicies(String memberUserName, String storeName, Integer bagConstraintId) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.removeConstraintFromPaymentPolicies(memberUserName,storeName,bagConstraintId);
    }

    public List<String> getAllPaymentPolicies(String memberUserName, String storeName) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.getAllPaymentPolicies(memberUserName,storeName);
    }

    public List<String> getAllBagConstraints(String memberUserName, String storeName) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.getAllBagConstraints(memberUserName,storeName);
    }

    public Integer createProductDiscountPolicy(String memberUserName, String storeName, String productName,  int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createProductDiscountPolicy(memberUserName, storeName, productName, discountPercentage, addAsStoreDiscountPolicy);
    }

    public Integer createProductDiscountPolicyWithConstraint(String memberUserName, String storeName, String productName,  int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createProductDiscountPolicyWithConstraint(memberUserName, storeName, productName, discountPercentage, bagConstraintId, addAsStoreDiscountPolicy);
    }

    public Integer createCategoryDiscountPolicy(String memberUserName, String storeName, String categoryName,  int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createCategoryDiscountPolicy(memberUserName, storeName, categoryName, discountPercentage, addAsStoreDiscountPolicy);
    }

    public Integer createCategoryDiscountPolicyWithConstraint(String memberUserName, String storeName, String categoryName,  int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createCategoryDiscountPolicyWithConstraint(memberUserName, storeName, categoryName, discountPercentage, bagConstraintId, addAsStoreDiscountPolicy);
    }


    public Integer createAllStoreDiscountPolicy(String memberUserName, String storeName, int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createAllStoreDiscountPolicy(memberUserName, storeName, discountPercentage, addAsStoreDiscountPolicy);
    }

    public Integer createAllStoreDiscountPolicyWithConstraint(String memberUserName, String storeName, int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createAllStoreDiscountPolicyWithConstraint(memberUserName, storeName, discountPercentage, bagConstraintId, addAsStoreDiscountPolicy);
    }

    public Integer createAdditionDiscountPolicy(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, boolean addAsStoreDiscountPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createAdditionDiscountPolicy(memberUserName, storeName, firstDiscountPolicyId, secondDiscountPolicyId, addAsStoreDiscountPolicy);
    }

    public Integer createAdditionDiscountPolicyWithConstraint(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createAdditionDiscountPolicyWithConstraint(memberUserName, storeName, firstDiscountPolicyId, secondDiscountPolicyId, bagConstraintId, addAsStoreDiscountPolicy);
    }

    public Integer createMaxValDiscountPolicy(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, boolean addAsStoreDiscountPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createMaxValDiscountPolicy(memberUserName, storeName, firstDiscountPolicyId, secondDiscountPolicyId, addAsStoreDiscountPolicy);
    }

    public Integer createMaxValDiscountPolicyWithConstraint(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createMaxValDiscountPolicyWithConstraint(memberUserName, storeName, firstDiscountPolicyId, secondDiscountPolicyId, bagConstraintId, addAsStoreDiscountPolicy);
    }


    public boolean addAsStoreDiscountPolicy(String memberUserName, String storeName, Integer discountPolicyId) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.addAsStoreDiscountPolicy(memberUserName,storeName, discountPolicyId);
    }

    public boolean removeFromStoreDiscountPolicies(String memberUserName, String storeName, Integer discountPolicyId) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.removeFromStoreDiscountPolicies(memberUserName,storeName, discountPolicyId);
    }


    public List<String> getAllCreatedDiscountPolicies(String memberUserName, String storeName) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.getAllCreatedDiscountPolicies(memberUserName,storeName);
    }

    public List<String> getAllStoreDiscountPolicies(String memberUserName, String storeName) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.getAllStoreDiscountPolicies(memberUserName,storeName);
    }


    public boolean hasRole(String memberUserName) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.hasRole(memberUserName);
    }

    public Map<String,List<StoreDTO>> myStores(String memberUserName) throws Exception{
        userController.assertIsMemberLoggedIn(memberUserName);
        return userController.myStores(memberUserName);
    }

    public boolean systemManagerCloseStore(String managerName, String storeName) throws Exception{
        userController.assertIsMemberLoggedIn(managerName);
        userController.assertIsSystemManager(managerName);
        return storeController.systemManagerCloseStore(storeName);
    }

    public boolean isSystemManager(String userName) throws Exception{
        return userController.assertIsSystemManager(userName);
    }

    public Set<String> getAllSystemManagers(String managerName) throws Exception{
        return userController.getAllSystemManagers(managerName);
    }

    public boolean removeMemberBySystemManager(String managerName,String memberName) throws Exception{
        return userController.removeMemberBySystemManager(managerName,memberName);
    }

    public MemberDTO getMemberInfo(String callerMemberName, String returnedMemberName) throws Exception {
        userController.assertIsMemberLoggedIn(callerMemberName);
        userController.isMember(returnedMemberName);
        return userController.getMemberInfo(callerMemberName,returnedMemberName);
    }


    //return 1=storeFounder, 2=storeOwner, 3=storeManager, -1= noRule
    public int getRuleForStore(String storeName, String memberName) throws Exception {
        userController.assertIsMemberLoggedIn(memberName);
        return storeController.getRuleForStore(storeName,memberName);
    }


    public void takeDownSystemManagerAppointment(String storeName, String appointedMember) throws Exception {
        userController.takeDownSystemManagerAppointment(appointedMember);
        storeController.takeDownSystemManagerAppointment(storeName, appointedMember);
    }

    public List<String> checkForAppendingMessages(String guestName) throws Exception {
        List<String> messages = new ArrayList<>();
        List<String> loggedIn_members = getAllLoggedInMembers();
        if(loggedIn_members.contains(guestName)){
            Member member = userController.getMember(guestName);
            messages = member.checkForAppendingMessages();
        }
        return messages;
    }

    public List<String> getLiveMessages(String memberName) throws Exception {
        List<String> messages = new ArrayList<>();
        List<String> loggedIn_members = getAllLoggedInMembers();
        if(loggedIn_members.contains(memberName)){
            Member member = userController.getMember(memberName);
            messages = member.getLiveMessages();
        }
        return messages;
    }

    public void clearMessages(String name) throws Exception {
        List<String> loggedIn_members = getAllLoggedInMembers();
        if(loggedIn_members.contains(name)){
            Member member = userController.getMember(name);
            member.clearMessages();
        }
    }

    //FOR ACC TEST:

    public void send(String userName1, String message) throws Exception {
        userController.send(userName1, message);
    }

    public Set<String> getAppendingMessages(String userName1) {
        return userController.getAppendingMessages(userName1);
    }

    public String getJSONFromFile(String filename) {
        String jsonText = "";
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new FileReader(filename));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonText += line + "\n";
            }
            bufferedReader.close();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return jsonText;
    }

    //IF WE DECIDE TO IMPROVE SWITCHING BETWEEN INIT FILES     ???
    private void updateLoadStatusToJSONFile(String path, boolean status) throws Exception {
        JSONObject json = new JSONObject();
        try {
            boolean[][] arr = new boolean[1][1];
            arr[0] = new boolean[1];
            arr[0][0] = status;
            json.put("loaded", arr);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createSystemManager(String userName, String password) throws Exception {
        Member member = new Member(userName,Security.Encode(password));
        userController.addSystemManager(userName, member);
        SystemManager systemManager = new SystemManager(member);
        member.setSystemManager(systemManager);
        userController.putSystemManager(userName, systemManager);
        return userName;
    }

    public void initMarketParsing(){
        HashMap memberName_guesName = new HashMap();
        String strJson = getJSONFromFile("Dev/TradeZone/initFiles/init_1.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            JsonNode jsonNode = objectMapper.readTree(strJson);
            Iterator keys = jsonNode.fieldNames();
            while(keys.hasNext()){
                String current = (String) keys.next();
                ArrayNode arrayNode;
                switch(current) {
                    case "register":
                        String guest = "";
                        arrayNode = (ArrayNode) jsonNode.get("register");
                        System.out.println("arrayNode: " + arrayNode);
//                        updatedConfig = updatedConfig + "\"register\" : " + arrayNode + "\n";         ???
                        for(JsonNode node: arrayNode){
                            System.out.println("node: " + node);
                            if(node.isArray()){
                                guest = enterMarket();
                                String member_name = node.get(0).asText();
                                String member_pass = node.get(1).asText();
                                memberName_guesName.put(member_name, guest);
                                register(guest, member_name, member_pass);
                            }
                        }
                        break;
                    case "login":
                        arrayNode = (ArrayNode) jsonNode.get("login");
                        System.out.println("arrayNode: " + arrayNode);
                        for(JsonNode node: arrayNode){
                            System.out.println("node: " + node);
                            if(node.isArray()){
                                String member_name = node.get(0).asText();
                                String member_pass = node.get(1).asText();
                                String guest_name = (String) memberName_guesName.get(member_name);
                                login(guest_name, member_name, member_pass);
                            }
                        }
                        break;
                    case "create_store":
                        arrayNode = (ArrayNode) jsonNode.get("create_store");
                        System.out.println("arrayNode: " + arrayNode);
                        for(JsonNode node: arrayNode){
                            System.out.println("node: " + node);
                            if(node.isArray()){
                                String member_name = node.get(0).asText();
                                String store_name = node.get(1).asText();
                                createStore(member_name, store_name);
                            }
                        }
                        break;
                    case "appoint_as_store_owner":
                        arrayNode = (ArrayNode) jsonNode.get("appoint_as_store_owner");
                        System.out.println("arrayNode: " + arrayNode);
                        for(JsonNode node: arrayNode){
                            System.out.println("node: " + node);
                            if(node.isArray()){
                                String owner_name = node.get(0).asText();
                                String store_name = node.get(1).asText();
                                String new_owner_name = node.get(2).asText();
                                appointOtherMemberAsStoreOwner(owner_name, store_name, new_owner_name);
                            }
                        }
                        break;
                    case "logout":
                        arrayNode = (ArrayNode) jsonNode.get("logout");
                        System.out.println("arrayNode: " + arrayNode);
                        for(JsonNode node: arrayNode){
                            System.out.println("node: " + node);
                            if(node.isArray()){
                                String member_name = node.get(0).asText();
                                memberLogOut(member_name);
                            }
                        }
                        break;
                    case "system_manager":
                        arrayNode = (ArrayNode) jsonNode.get("system_manager");
                        System.out.println("arrayNode: " + arrayNode);
                        for(JsonNode node: arrayNode){
                            System.out.println("node: " + node);
                            if(node.isArray()){
                                String user_name = node.get(0).asText();
                                String user_pass = node.get(1).asText();
                                createSystemManager(user_name, user_pass);
                            }
                        }
                        break;
                    case "add_product":
                        arrayNode = (ArrayNode) jsonNode.get("add_product");
                        System.out.println("arrayNode: " + arrayNode);
                        for(JsonNode node: arrayNode){
                            System.out.println("node: " + node);
                            if(node.isArray()){
                                String member_name = node.get(0).asText();
                                String store_name = node.get(1).asText();
                                String product_name = node.get(2).asText();
                                String category_name = node.get(3).asText();
                                Double price = node.get(4).asDouble();
                                String description = node.get(5).asText();
                                int amount = node.get(6).asInt();
                                addNewProductToStock(member_name, store_name, product_name, category_name, price, description, amount);
                            }
                        }
                        break;
                    case "appoint_as_store_manager":
                        arrayNode = (ArrayNode) jsonNode.get("appoint_as_store_manager");
                        System.out.println("arrayNode: " + arrayNode);
                        for(JsonNode node: arrayNode){
                            System.out.println("node: " + node);
                            if(node.isArray()){
                                String member_name = node.get(0).asText();
                                String store_name = node.get(1).asText();
                                String new_manager_name = node.get(2).asText();
                                appointOtherMemberAsStoreManager(member_name, store_name, new_manager_name);                            }
                        }
                        break;
                    case "add_permissions":
                        //    public boolean updateManagerPermissionsForStore(String ownerUserName, String storeName,
                        //    String managerUserName, List<Integer> newPermissions) throws Exception {
                        arrayNode = (ArrayNode) jsonNode.get("add_permissions");
                        System.out.println("arrayNode: " + arrayNode);
                        for(JsonNode node: arrayNode){
                            System.out.println("node: " + node);
                            if(node.isArray()){
                                String member_name = node.get(0).asText();
                                String store_name = node.get(1).asText();
                                String manager_name = node.get(2).asText();
                                ArrayList<Integer> permissions = new ArrayList<Integer>();
                                if(node.get(3).isArray()){
                                    Iterator iter  = node.get(3).iterator();
                                    while(iter.hasNext()){
                                        JsonNode curr = (JsonNode) iter.next();
                                        permissions.add(curr.intValue());
                                    }
                                }
                                updateManagerPermissionsForStore(member_name, store_name, manager_name, permissions);
                            }
                        }
                        break;
                    default:
                        throw new RuntimeException("init file failed: Tag is not supported");
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadData() throws Exception {
        if (Market.dbFlag) {
            MemberMapper.getInstance().loadAllMembersNames();
            MemberMapper.getInstance().loadAllSystemManagers();
            MemberMapper.getInstance().loadAllOnlineMembersNames();
            StoreMapper.getInstance().loadAllStoresNames();
        }
    }

}
