package CommunicationLayer;

import DTO.*;
import DomainLayer.LoggerManager;
import ServiceLayer.ResponseT;
import ServiceLayer.SystemService;
import jdk.jshell.spi.ExecutionControl;

import java.io.IOException;
import java.util.*;

public class Server {

    private static Server server = null;
    private SystemService service;
    public static Server getInstance(){
        if(server == null){
            server = new Server();
        }
        return server;
    }

    private Server(){
        service = new SystemService();
    }


    public ResponseT<String> initializeMarket(){
        return service.initializeMarket();
    }

    public ResponseT<String> enterMarket(){
        return service.enterMarket();
    }

    public ResponseT<List<String>> getAllGuests(){
        return service.getAllGuests();
    }
    public ResponseT<List<String>> getAllMembers(){
        return service.getAllMembers();
    }

    public ResponseT<List<String>> getAllLoggedInMembers(){
        return service.getAllLoggedInMembers();
    }

    public ResponseT<List<String>> getAllStoresNames(){
        return service.getAllStoresNames();
    }

    public ResponseT<Boolean> exitMarket(String userName){
        return service.exitMarket(userName);
    }

    public ResponseT<Boolean> register(String guestUserName, String newMemberUserName, String password){
        return service.register(guestUserName, newMemberUserName, password);
    }

    public ResponseT<String> login(String guestUserName, String MemberUserName, String password){
        return service.login(guestUserName, MemberUserName, password);
    }

    public ResponseT<StoreDTO> getStoreInfo(String userName, String storeName){
        return service.getStoreInfo(userName, storeName);
    }

    public ResponseT<ProductDTO> getProductInfoFromStore(String userName, String storeName, String productName){
        return service.getProductInfoFromStore(userName, storeName, productName);
    }

    public ResponseT<List<ProductDTO>> getProductInfoFromMarketByName(String userName, String productName){
        return service.getProductInfoFromMarketByName(userName, productName);
    }

    public ResponseT<List<ProductDTO>> getProductInfoFromMarketByCategory(String userName, String categoryName){
        return service.getProductInfoFromMarketByCategory(userName, categoryName);
    }

    public ResponseT<List<ProductDTO>> getProductInfoFromMarketByKeyword(String userName, String keyword){
        return service.getProductInfoFromMarketByKeyword(userName, keyword);
    }

    public ResponseT<List<ProductDTO>> filterByPrice(String userName, List<ProductDTO> productsInfo, Integer minPrice, Integer maxPrice){
        return service.filterByPrice(userName, productsInfo, minPrice, maxPrice);
    }

    public ResponseT<List<ProductDTO>> filterByCategory(String userName, List<ProductDTO> productsInfo, String categoryName){
        return service.filterByCategory(userName, productsInfo, categoryName);
    }

//    public ResponseT<List<ProductDTO>> filterByProductRate(String userName, List<ProductDTO> productsInfo, Integer productRate){
//        return service.filterByProductRate(userName, productsInfo, productRate);
//    }
//
//    public ResponseT<List<ProductDTO>> filterByStoreRate(String userName, List<ProductDTO> productsInfo, Integer storeRate){
//        return service.filterByStoreRate(userName, productsInfo, storeRate);
//    }

    public ResponseT<Boolean> addToCart(String userName, String storeName, String productName, Integer amount){
        return service.addToCart(userName, storeName, productName, amount);
    }
    public ResponseT<Boolean> removeFromCart(String userName, String storeName, String productName){
        return service.removeFromCart(userName, storeName, productName);
    }
    public ResponseT<Boolean> changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount){
        return service.changeProductAmountInCart(userName, storeName, productName, newAmount);
    }
    public ResponseT<List<BagDTO>> getCartContent(String userName){
        return service.getCartContent(userName);
    }
    public ResponseT<String> memberLogOut(String memberUserName){
        return service.memberLogOut(memberUserName);
    }
    public ResponseT<StoreDTO> createStore(String memberUserName, String newStoreName){
        return service.createStore(memberUserName, newStoreName);
    }
    public ResponseT<Boolean> addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, Double price, String description, Integer amount){
        return service.addNewProductToStock(memberUserName, storeName, nameProduct, category, price, description, amount);
    }
    public ResponseT<Boolean> removeProductFromStock(String memberUserName, String storeName, String productName){
        return service.removeProductFromStock(memberUserName, storeName, productName);
    }
    public ResponseT<Boolean> updateProductDescription(String memberUserName, String storeName, String productName, String newProductDescription){
        return service.updateProductDescription(memberUserName, storeName, productName, newProductDescription);
    }

    public ResponseT<Boolean> updateProductAmount(String memberUserName, String storeName, String productName, Integer newAmount){
        return service.updateProductAmount(memberUserName, storeName, productName, newAmount);
    }

    public ResponseT<Boolean> updateProductPrice(String memberUserName, String storeName, String productName, Double newPrice){
        return service.updateProductPrice(memberUserName, storeName, productName, newPrice);
    }

    public ResponseT<Boolean> appointOtherMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName){
        return service.appointOtherMemberAsStoreOwner(memberUserName, storeName, newOwnerUserName);
    }

    public ResponseT<Boolean> appointOtherMemberAsStoreManager(String memberUserName, String storeName, String newManagerUserName){
        return service.appointOtherMemberAsStoreManager(memberUserName, storeName, newManagerUserName);
    }

    public ResponseT<Boolean> AppointMemberAsSystemManager(String managerName,String otherMemberName){
        return service.AppointMemberAsSystemManager(managerName, otherMemberName);
    }

    // TODO: change params
//    public ResponseT<Boolean> changeManagerPermissions(String memberUserName, String storeName, String managerName, List<Integer> permsIDs){
//        // TODO: call service...
//        //        return service.changeManagerPermissions(memberUserName, storeName, permsIDs);
//        return new ResponseT<>(true);
//    }

    public ResponseT<Boolean> closeStore(String memberUserName, String storeName){
        return service.closeStore(memberUserName, storeName);
    }

    public ResponseT<List<MemberDTO>> getStoreWorkersInfo(String memberUserName, String storeName){
        return service.getStoreWorkersInfo(memberUserName, storeName);
    }

    public ResponseT<List<DealDTO>> getStoreDeals(String memberUserName, String storeName){
        return service.getStoreDeals(memberUserName, storeName);
    }

    public ResponseT<List<DealDTO>> getMemberDeals(String systemManagerUserName, String otherMemberUserName){
        return service.getMemberDeals(systemManagerUserName, otherMemberUserName);
    }

    public ResponseT<Boolean> purchaseCartByCreditCard(String userName, String cardNumber, String month, String year, String holder, String cvv, String id, String receiverName, String shipmentAddress, String shipmentCity, String shipmentCountry, String zipCode){
        return service.purchaseCartByCreditCard(userName, cardNumber, month, year, holder, cvv, id, receiverName, shipmentAddress, shipmentCity, shipmentCountry, zipCode);
    }

    public ResponseT<Boolean> removeOwnerByHisAppointer(String appointerUserName, String storeName, String ownerUserName){
        return service.removeOwnerByHisAppointer(appointerUserName, storeName, ownerUserName);
    }

    // ---------------------------------    BAG CONSTRAINTS ---------------------------------

    public ResponseT<Integer> createMaxProductAmountAllContentBagConstraint(String memberUserName, String storeName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) {
        return service.createMaxProductAmountAllContentBagConstraint(memberUserName, storeName, productName, amountLimit, addAsStorePaymentPolicy);
    }


    public ResponseT<Integer> createMinProductAmountAllContentBagConstraint(String memberUserName, String storeName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) {
        return service.createMinProductAmountAllContentBagConstraint(memberUserName, storeName, productName, amountLimit, addAsStorePaymentPolicy);
    }

    public ResponseT<Integer> createMaxTimeAtDayProductBagConstraint(String memberUserName, String storeName, String productName, int hour, int minute, boolean addAsStorePaymentPolicy) {
        return service.createMaxTimeAtDayProductBagConstraint(memberUserName, storeName, productName, hour, minute, addAsStorePaymentPolicy);
    }

    public ResponseT<Integer> createRangeOfDaysProductBagConstraint(String memberUserName, String storeName, String productName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy) {
        return service.createRangeOfDaysProductBagConstraint(memberUserName, storeName, productName, fromYear, fromMonth, fromDay, toYear, toMonth, toDay, addAsStorePaymentPolicy);
    }

    public ResponseT<Integer> createMaxTimeAtDayCategoryBagConstraint(String memberUserName, String storeName, String categoryName, int hour, int minute, boolean addAsStorePaymentPolicy) {
        return service.createMaxTimeAtDayCategoryBagConstraint(memberUserName, storeName, categoryName, hour, minute, addAsStorePaymentPolicy);
    }

    public ResponseT<Integer> createRangeOfDaysCategoryBagConstraint(String memberUserName, String storeName, String categoryName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy) {
        return service.createRangeOfDaysCategoryBagConstraint(memberUserName, storeName, categoryName, fromYear, fromMonth, fromDay, toYear, toMonth, toDay, addAsStorePaymentPolicy);
    }

    public ResponseT<Integer> createAndBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) {
        return service.createAndBagConstraint(memberUserName, storeName, firstBagConstraintId, secondBagConstraintId, addAsStorePaymentPolicy);
    }

    public ResponseT<Integer> createOrBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy){
       return service.createOrBagConstraint(memberUserName, storeName, firstBagConstraintId, secondBagConstraintId, addAsStorePaymentPolicy);
    }

    public ResponseT<Integer> createOnlyIfBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy){
        return service.createOnlyIfBagConstraint(memberUserName, storeName, firstBagConstraintId, secondBagConstraintId, addAsStorePaymentPolicy);
    }

    public ResponseT<Boolean> addConstraintAsPaymentPolicy(String memberUserName, String storeName, Integer bagConstraintId){ // activate
        return service.addConstraintAsPaymentPolicy(memberUserName, storeName, bagConstraintId);
    }

    public ResponseT<Boolean> removeConstraintFromPaymentPolicies(String memberUserName, String storeName, Integer bagConstraintId){ // deactivate
        return service.removeConstraintFromPaymentPolicies(memberUserName, storeName, bagConstraintId);
    }

    public ResponseT<List<String>> getAllBagConstraints(String memberUserName, String storeName){ // suggested bag constraints
        // TODO:
        return service.getAllBagConstraints(memberUserName, storeName);
//        List<String> list = new ArrayList<>();
//        list.add("1. min 3 iphones in bag");
//        list.add("2. max 10 milk in bag ");
//        list.add("3. after 23:00 milk not allowed");
//        list.add("4. after 23:00 alcohol not allowed");
//        return new ResponseT<>(list);
    }

    public ResponseT<List<String>> getAllPaymentPolicies(String memberUserName, String storeName){ // active bag constraints
        // TODO:
        return service.getAllPaymentPolicies(memberUserName, storeName);
//        List<String> list = new ArrayList<>();
//        list.add("2. max 10 milk in bag ");
//        list.add("3. after 23:00 milk not allowed");
//        return new ResponseT<>(list);
    }

    // ---------------------------------    DISCOUNT POLICIES ---------------------------------

    public ResponseT<Integer> createProductDiscountPolicyWithConstraint(String memberUserName, String storeName, String productName,  int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) {
        if(bagConstraintId == -1)
            return service.createProductDiscountPolicy(memberUserName, storeName, productName, discountPercentage, addAsStoreDiscountPolicy);
        else
            return service.createProductDiscountPolicyWithConstraint(memberUserName, storeName, productName, discountPercentage, bagConstraintId, addAsStoreDiscountPolicy);
    }

    public ResponseT<Integer> createCategoryDiscountPolicyWithConstraint(String memberUserName, String storeName, String categoryName,  int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy)  {
        if(bagConstraintId == -1)
            return service.createCategoryDiscountPolicy(memberUserName, storeName, categoryName, discountPercentage, addAsStoreDiscountPolicy);
        else
            return service.createCategoryDiscountPolicyWithConstraint(memberUserName, storeName, categoryName, discountPercentage, bagConstraintId, addAsStoreDiscountPolicy);
    }

    public ResponseT<Integer> createAllStoreDiscountPolicyWithConstraint(String memberUserName, String storeName, int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy)  {
        if(bagConstraintId == -1)
            return service.createAllStoreDiscountPolicy(memberUserName, storeName, discountPercentage, addAsStoreDiscountPolicy);
        else
            return service.createAllStoreDiscountPolicyWithConstraint(memberUserName, storeName, discountPercentage, bagConstraintId, addAsStoreDiscountPolicy);

    }

    public ResponseT<Integer> createAdditionDiscountPolicyWithConstraint(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) {
        if(bagConstraintId == -1)
            return service.createAdditionDiscountPolicy(memberUserName, storeName, firstDiscountPolicyId, secondDiscountPolicyId, addAsStoreDiscountPolicy);
        else
            return service.createAdditionDiscountPolicyWithConstraint(memberUserName, storeName, firstDiscountPolicyId, secondDiscountPolicyId, bagConstraintId, addAsStoreDiscountPolicy);
    }

    public ResponseT<Integer> createMaxValDiscountPolicyWithConstraint(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) {
        if(bagConstraintId == -1)
            return service.createMaxValDiscountPolicy(memberUserName, storeName, firstDiscountPolicyId, secondDiscountPolicyId, addAsStoreDiscountPolicy);
        else
            return service.createMaxValDiscountPolicyWithConstraint(memberUserName, storeName, firstDiscountPolicyId, secondDiscountPolicyId, bagConstraintId, addAsStoreDiscountPolicy);
    }


    public ResponseT<Boolean> addAsStoreDiscountPolicy(String memberUserName, String storeName, Integer discountPolicyId) { // activate
        return service.addAsStoreDiscountPolicy(memberUserName, storeName, discountPolicyId);
    }

    public ResponseT<Boolean> removeFromStoreDiscountPolicies(String memberUserName, String storeName, Integer discountPolicyId) { // deactivate
        return service.removeFromStoreDiscountPolicies(memberUserName, storeName, discountPolicyId);
    }

    public ResponseT<List<String>> getAllCreatedDiscountPolicies(String memberUserName, String storeName) { // suggested
//        TODO:
        return service.getAllCreatedDiscountPolicies(memberUserName, storeName);
//        List<String> list = new ArrayList<>();
//        list.add("1. min 3 iphones in bag");
//        list.add("2. max 10 milk in bag ");
//        list.add("3. after 23:00 milk not allowed");
//        list.add("4. after 23:00 alcohol not allowed");
//        return new ResponseT<>(list);
    }

    public ResponseT<List<String>> getAllStoreDiscountPolicies(String memberUserName, String storeName) { // active
        //        TODO:
        return service.getAllStoreDiscountPolicies(memberUserName, storeName);
//        List<String> list = new ArrayList<>();
//        list.add("2. max 10 milk in bag ");
//        list.add("3. after 23:00 milk not allowed");
//        return new ResponseT<>(list);
    }

    // ------------------------------------------------------------

    public ResponseT<Boolean> hasRole(String userName){
        return service.hasRole(userName);
    }

    public ResponseT<Map<String,List<StoreDTO>>> myStores(String memberUserName){
        return service.myStores(memberUserName);
    }

    public  ResponseT<Boolean> systemManagerCloseStore(String managerName, String storeName){
        return service.systemManagerCloseStore(managerName,storeName);
    }

    public ResponseT<Boolean> isSystemManager(String userName){
        return service.isSystemManager(userName);
    }

    public ResponseT<Integer> getProductAmountInStore(String userName, String storeName, String productName){
        return service.getProductAmountInStore(userName, storeName, productName);
    }

    public ResponseT<Set<String>> getAllSystemManagers(String managerName){
        return service.getAllSystemManagers(managerName);
    }

    public ResponseT<Boolean> removeMemberBySystemManager(String managerName,String memberName){
        return service.removeMemberBySystemManager(managerName, memberName);
    }

    public ResponseT<MemberDTO> getMemberInfo(String callerMemberName, String returnedMemberName) {
        return service.getMemberInfo(callerMemberName, returnedMemberName);
    }

    //return 1=storeFounder, 2=storeOwner, 3=storeManager, -1= noRule
    public ResponseT<Integer> getRuleForStore(String storeName, String memberName){
        return service.getRuleForStore(storeName, memberName);
    }

    public ResponseT<List<String>> getAllPermissions(String ownerUserName, String storeName) {
        return service.getAllPermissions(ownerUserName, storeName);
//        String[] perm = {"perm1", "perm2", "perm3", "perm4"};
//        return perm;
    }

    public ResponseT<List<Integer>> getManagerPermissionsForStore(String ownerUserName, String storeName, String managerUserName) {
        return service.getManagerPermissionsForStore(ownerUserName, storeName, managerUserName);
//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(3);
//        return list;
    }

    public ResponseT<Boolean> updateManagerPermissionsForStore(String ownerUserName, String storeName, String managerUserName, List<Integer> newPermissions){
        return service.updateManagerPermissionsForStore(ownerUserName, storeName, managerUserName, newPermissions);
    }

}