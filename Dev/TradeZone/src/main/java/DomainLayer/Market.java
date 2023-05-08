package DomainLayer;

import DTO.*;
import DomainLayer.BagConstraints.*;
import DomainLayer.Controllers.StoreController;
import DomainLayer.Controllers.UserController;
import jdk.jshell.spi.ExecutionControl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Market {
    UserController userController;
    StoreController storeController;
    PaymentService paymentService;
    ShipmentService shipmentService;

    public Market(){
        this.userController = new UserController();
        this.storeController = new StoreController();
        paymentService = new PaymentService("");
        shipmentService = new ShipmentService("");
    }
    //TODO: Implement this requirements: 2.5, 3.3(maybe), 4.12(for storeManager)
    //TODO: closeStore req is not implemented as it should be , i(Ahmad) didn't pay attention to close stores handling..
    public String enterMarket(){
        return userController.loginAsGuest();
    }
    public List<String> getAllGuests(){
        return userController.getAllGuests();
    }

    public List<String> getAllMembers(){
        return userController.getAllMambers();
    }
    public List<String> getAllLoggedInMembers(){
        return userController.getAllLoggedInMembers();
    }
    public List<String> getAllStoresNames(){ // TODO: add to market and service
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
        Store store = storeController.getStore(storeName);
        userController.assertIsOwner(userName,store);
        return storeController.getStoreInfo(storeName);
    }
    public ProductDTO getProductInfoFromStore(String userName, String storeName, String productName) throws Exception {
        userController.assertIsGuestOrLoggedInMember(userName);
        return storeController.getProductInfoFromStore(storeName, productName);
    }

    public List<ProductDTO> getProductInfoFromMarketByName(String userName, String productName) throws Exception {
        userController.assertIsGuestOrLoggedInMember(userName);
        return storeController.getProductInfoFromMarketByName(productName);
    }

    public List<ProductDTO> getProductInfoFromMarketByCategory(String userName, String categoryName) throws Exception {
        userController.assertIsGuestOrLoggedInMember(userName);
        return storeController.getProductInfoFromMarketByCategory(categoryName);
    }

    public List<ProductDTO> getProductInfoFromMarketByKeyword(String userName, String keyword) throws ExecutionControl.NotImplementedException {
        // TODO: low priority Don't test it
        throw new ExecutionControl.NotImplementedException("");
    }

    public List<ProductDTO> filterByPrice(String userName, List<ProductDTO> productsInfo, Integer minPrice, Integer maxPrice) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
    }

    public List<ProductDTO> filterByCategory(String userName, List<ProductDTO> productsInfo, String categoryName) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
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
        userController.assertIsMemberLoggedIn(memberUserName);
        Member member = userController.getMember(memberUserName);
        Store store = storeController.createStore(newStoreName);
        member.appointMemberAsStoreFounder(store);
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

    public boolean appointOtherMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName) throws Exception {
        Store store = storeController.getStore(storeName); // TODO: MAYBE WE NEED TO CHECK IF STORE IS ACTIVE
        return userController.appointOtherMemberAsStoreOwner(memberUserName,store,newOwnerUserName);
    }

    public boolean appointOtherMemberAsStoreManager(String memberUserName, String storeName, String newManagerUserName) throws Exception {
        Store store = storeController.getStore(storeName); // TODO: MAYBE WE NEED TO CHECK IF STORE IS ACTIVE
        return userController.appointOtherMemberAsStoreManager(memberUserName,store,newManagerUserName);
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
        return this.storeController.getStoreDeals(memberUserName, storeName);
    }

    public List<DealDTO> getMemberDeals(String systemManagerUserName, String otherMemberUserName) throws Exception {
        this.userController.checkMemberRole(systemManagerUserName, otherMemberUserName);
        return this.storeController.getMemberDeals(otherMemberUserName);
    }

    public boolean purchaseCartByCreditCard(String userName, String cardNumber, String month, String year, String holder, String cvv, String id, String receiverName,String shipmentAddress,String shipmentCity,String shipmentCountry,String zipCode) throws Exception {
        int transactionId=0;
        int supplyId = 0;
        try{
            userController.assertIsGuestOrLoggedInMember(userName);
            userController.validateStorePolicy(userName);
            userController.validateAllProductsAmounts(userName);
            Double price=userController.getCartPrice(userName);
            transactionId = paymentService.pay(price, cardNumber, month, year,holder, cvv, id);
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

    public boolean removeOwnerByHisAppointer(String appointerUserName, String storeName, String ownerUserName ) throws Exception {
        Store store = storeController.getStore(storeName); // TODO: MAYBE WE NEED TO CHECK IF STORE IS ACTIVE
        return userController.removeOwnerByHisAppointer(appointerUserName,store,ownerUserName);
    }









    ///////////

    /////////////


    //ProductBagConstraint
    public Integer createMaxTimeAtDayProductBagConstraint(String memberUserName, String storeName, String productName, int hour, int minute, boolean addAsStorePaymentPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createMaxTimeAtDayProductBagConstraint(memberUserName,storeName,productName,hour,minute ,addAsStorePaymentPolicy);
    }

    public Integer createRangeOfDaysProductBagConstraint(String memberUserName, String storeName, String productName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createRangeOfDaysProductBagConstraint(memberUserName,storeName,productName, fromYear,fromMonth,fromDay,toYear,toMonth,toDay, addAsStorePaymentPolicy);
    }



    //CategoryBagConstraint
    public Integer createMaxTimeAtDayCategoryBagConstraint(String memberUserName, String storeName, String categoryName, int hour, int minute, boolean addAsStorePaymentPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createMaxTimeAtDayCategoryBagConstraint(memberUserName,storeName,categoryName,hour,minute ,addAsStorePaymentPolicy);
    }

    public Integer createRangeOfDaysCategoryBagConstraint(String memberUserName, String storeName, String categoryName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createRangeOfDaysCategoryBagConstraint(memberUserName,storeName,categoryName, fromYear,fromMonth,fromDay,toYear,toMonth,toDay, addAsStorePaymentPolicy);
    }




    //AllContentBagConstraint
    public Integer createMaxProductAmountAllContentBagConstraint(String memberUserName, String storeName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createMaxProductAmountAllContentBagConstraint(memberUserName,storeName,productName,amountLimit,addAsStorePaymentPolicy);
    }

    public Integer createMinProductAmountAllContentBagConstraint(String memberUserName, String storeName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.createMinProductAmountAllContentBagConstraint(memberUserName,storeName,productName,amountLimit,addAsStorePaymentPolicy);
    }


    // and, or, only if bag constraints
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

    public String getAllPaymentPolicies(String memberUserName, String storeName) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.getAllPaymentPolicies(memberUserName,storeName);
    }

    public String getAllBagConstraints(String memberUserName, String storeName) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.getAllBagConstraints(memberUserName,storeName);
    }

    public boolean hasRole(String memberUserName) throws Exception {
        userController.assertIsMemberLoggedIn(memberUserName);
        return storeController.hasRole(memberUserName);
    }






}
