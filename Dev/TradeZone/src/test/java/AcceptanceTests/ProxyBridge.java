package AcceptanceTests;

import DomainLayer.PaymentService;
import DomainLayer.ShipmentService;
import ServiceLayer.ResponseT;

import java.util.*;

public class ProxyBridge implements Bridge{
    public RealBridge realBridge;

    public ProxyBridge(){
        realBridge = null;
    }

    public ProxyBridge(RealBridge realBridge){
        this.realBridge = realBridge;
    }


    @Override
    public String initializeMarket() throws Exception{
        if(realBridge!=null){
            return realBridge.initializeMarket();
        }
        return "";
    }

    @Override
    public String enterMarket() throws Exception{
        if(realBridge!=null){
            return realBridge.enterMarket();
        }
        return "";
    }

    public void setPaymentService(PaymentService paymentService){
        realBridge.setPaymentService(paymentService);
    }
    public void setShipmentService(ShipmentService shipmentService){
        realBridge.setShipmentService(shipmentService);
    }

    @Override
    public boolean exitMarket(String userName) throws Exception{
        if(realBridge!=null){
            return realBridge.exitMarket(userName);
        }
        return false;
    }

    @Override
    public boolean register(String guestUserName, String newMemberUserName, String password) throws Exception {
        if(realBridge!=null){
            return realBridge.register(guestUserName, newMemberUserName, password);
        }
        return false;
    }

    @Override
    public String login(String guestUserName, String MemberUserName, String password) throws Exception {
        if(realBridge!=null){
            return realBridge.login(guestUserName, MemberUserName, password);
        }
        return "";
    }

    @Override
    public String memberLogOut(String memberUserName)  throws Exception {
        if(realBridge!=null){
            return realBridge.memberLogOut(memberUserName);
        }
        return "";
    }

    @Override
    public String createStore(String memberUserName, String newStoreName) throws Exception{
        if(realBridge!=null){
            return realBridge.createStore(memberUserName,newStoreName);
        }
        return "";
    }

    @Override
    public boolean  addNewProductToStock(String memberUserName, String storeName, String product_name,String category, Double price, String description, int amount) throws Exception{
        if(realBridge!=null){
        return realBridge.addNewProductToStock(memberUserName, storeName, product_name, category,price, description,amount);
    }
        return false;
    }

    @Override
    public boolean removeProductFromStock(String memberUserName, String storeName, String productName) throws Exception {
        if(realBridge!=null){
            return realBridge.removeProductFromStock(memberUserName, storeName, productName);
        }
        return false;
    }

    @Override
    public boolean addCategory(String userName, String categoryName, String storeName) throws Exception {
        if(realBridge!=null){
            realBridge.addCategory(userName,categoryName, storeName);
        }
        return false;
    }

    @Override
    public boolean getCategory(String userName, String categoryName,String storeName) throws Exception {
        if(realBridge!=null){
            realBridge.getCategory(userName, categoryName, storeName);
        }
        return false;
    }

    @Override
    public boolean updateProductName(String memberUserName, String storeName, String productName, String newName) throws Exception {
        if(realBridge!=null){
            return realBridge.updateProductName(memberUserName, storeName, productName, newName);
        }
        return false;
    }

    @Override
    public boolean updateProductPrice(String memberUserName, String storeName, String productName, Double price) throws Exception {
        if(realBridge!=null){
            return realBridge.updateProductPrice(memberUserName, storeName, productName, price);
        }
        return false;
    }

    @Override
    public boolean updateProductDescription(String memberUserName, String storeName, String productName, String newDescription) throws Exception {
        if(realBridge!=null){
            return realBridge.updateProductDescription(memberUserName, storeName, productName, newDescription);
        }
        return false;
    }

    @Override
    public boolean updateProductAmount(String memberUserName, String storeName, String productName, int amount) throws Exception {
        if(realBridge!=null){
            return realBridge.updateProductAmount(memberUserName, storeName, productName, amount);
        }
        return false;
    }

    @Override
    public int getProductAmount(String storeName, String s) throws Exception {
        if(realBridge!=null){
            return realBridge.getProductAmount(storeName, s);
        }
        return -1;
    }

    @Override
    public boolean appointOtherMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName) throws Exception {
        if(realBridge!=null){
            return realBridge.appointOtherMemberAsStoreOwner(memberUserName, storeName, newOwnerUserName);
        }
        return false;
    }

    @Override
    public String getOwnerAppointer(String OwnerName, String storeName) throws Exception {
        if(realBridge!=null){
            return realBridge.getOwnerAppointer(OwnerName,storeName);
        }
        return "";
    }

    @Override
    public boolean appointOtherMemberAsStoreManager(String memberUserName, String storeName, String newOwnerUserName) throws Exception {
        if(realBridge!=null){
            return realBridge.appointOtherMemberAsStoreManager(memberUserName, storeName, newOwnerUserName);
        }
        return false;
    }

    @Override
    public String getManagerAppointer(String ManagerName, String storeName) throws Exception {
        if(realBridge!=null){
            return realBridge.getManagerAppointer(ManagerName,storeName);
        }
        return "";
    }

    @Override
    public boolean closeStore(String memberUserName, String storeName) throws Exception {
        if(realBridge!=null){
            return realBridge.closeStore(memberUserName,storeName);
        }
        return false;
//        return "";
    }

    @Override
    public boolean canGetStoreInfo(String userName, String storeName)throws Exception {
        if(realBridge!=null){
            return realBridge.canGetStoreInfo(userName,storeName);
        }
        return false;
    }

    @Override
    public Map<Integer, List<String>> getStoreRulesInfo(String ownerName, String storeName) throws Exception {
        if(realBridge!=null){
            return realBridge.getStoreRulesInfo(ownerName,storeName);
        }
        return new HashMap<>();
    }

    @Override
    public String getStoreFounderName(String userName, String storeName) throws Exception {
        if(realBridge!=null){
            return realBridge.getStoreFounderName(userName, storeName);
        }
        return "";
    }

    @Override
    public List<String> getStoreOwnersNames(String userName, String storeName) throws Exception {
        if(realBridge!=null){
            return realBridge.getStoreOwnersNames(userName, storeName);
        }
        return new LinkedList<>();
    }

    @Override
    public List<String> getStoreManagersNames(String userName, String storeName) throws Exception {
        if(realBridge!=null){
            return realBridge.getStoreManagersNames(userName, storeName);
        }
        return new LinkedList<>();
    }

    @Override
    public Double getProductPrice(String userName, String storeName, String productName) throws Exception {
        if(realBridge!=null){
            return realBridge.getProductPrice(userName, storeName, productName);
        }
        return -1.0;
    }

    @Override
    public String getProductDescription(String userName, String storeName, String productName) throws Exception {
        if(realBridge!=null){
            return realBridge.getProductDescription(userName, storeName, productName);
        }
        return "";
    }

    public List<String> getAllGuests() throws Exception {
        if(realBridge!=null){
            return realBridge.getAllGuests();
        }
        return new LinkedList<>();
    }

    public List<String> getAllOnlineMembers() throws Exception {
        if(realBridge!=null){
            return realBridge.getAllOnlineMembers();
        }
        return new LinkedList<>();
    }

    public List<String> getAllMembers() throws Exception {
        if(realBridge!=null){
            return realBridge.getAllMembers();
        }
        return new LinkedList<>();
    }

    public List<String> getAllStoresNames() throws Exception {
        if(realBridge!=null){
            return realBridge.getAllStoresNames();
        }
        return new LinkedList<>();
    }

    public List<String> getStoreProducts(String userName, String storeName) throws Exception {
        if(realBridge!=null){
            return realBridge.getStoreProducts(userName,storeName);
        }
        return new LinkedList<>();
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByName(String userName, String productName) throws Exception {
        if(realBridge!=null){
            return realBridge.getProductInfoFromMarketByName(userName, productName);
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByCategory(String userName, String categoryName) throws Exception {
        if(realBridge!=null){
            return realBridge.getProductInfoFromMarketByCategory(userName, categoryName);
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByKeyword(String userName, String keyword) throws Exception {
        if(realBridge!=null){
            return realBridge.getProductInfoFromMarketByKeyword(userName, keyword);
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, List<String>> filterByPrice(String userName, Map<String, List<String>> products, int minPrice, int maxPrice) throws Exception {
        if(realBridge!=null){
            return realBridge.filterByPrice(userName, products, minPrice, maxPrice);
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, List<String>> filterByCategory(String userName, Map<String, List<String>> products, String categoryName) throws Exception {
        if(realBridge!=null){
            return realBridge.filterByCategory(userName, products, categoryName);
        }
        return new HashMap<>();
    }

    @Override
    public boolean addToCart(String userName, String storeName, String productName, Integer amount) throws Exception {
        if(realBridge!=null){
            return realBridge.addToCart(userName, storeName, productName, amount);
        }
        return false;
    }

    @Override
    public List<String> getBag(String userName, String storeName) throws Exception {
        if(realBridge!=null){
            return realBridge.getBag(userName, storeName);
        }
        return new LinkedList<>();
    }

    @Override
    public int getProductAmountInCart(String userName, String storeName, String productName) throws Exception {
        if(realBridge!=null){
            return realBridge.getProductAmountInCart(userName, storeName, productName);
        }
        return -1;
    }

    @Override
    public Map<String, List<String>> getCartContent(String userName) throws Exception {
        if(realBridge!=null){
            return realBridge.getCartContent(userName);
        }
        return new HashMap<>();
    }

    @Override
    public boolean removeProductFromCart(String userName, String storeName, String productName) throws Exception {
        if(realBridge!=null){
            return realBridge.removeProductFromCart(userName, storeName, productName);
        }
        return false;
    }

    @Override
    public boolean changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount) throws Exception{
        if(realBridge!=null){
            return realBridge.changeProductAmountInCart(userName, storeName, productName, newAmount);
        }
        return false;
    }

    @Override
    public boolean removeOwnerByHisAppointer(String appointerUserName, String storeName, String ownerUserName) throws Exception {
        if(realBridge!=null){
            return realBridge.removeOwnerByHisAppointer(appointerUserName, storeName, ownerUserName);
        }
        return false;
    }

    @Override
    public boolean purchaseCartByCreditCard(String userName, String cardNumber, String month, String year, String holder, String cvv, String id, String receiverName, String shipmentAddress, String shipmentCity, String shipmentCountry, String zipCode) throws Exception {
        if(realBridge!=null){
            return realBridge.purchaseCartByCreditCard(userName, cardNumber, month, year, holder, cvv, id, receiverName, shipmentAddress, shipmentCity, shipmentCountry, zipCode);
        }
        return false;
    }

    @Override
    public Integer createMaxProductAmountAllContentBagConstraint(String memberUserName, String storeName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createMaxProductAmountAllContentBagConstraint(memberUserName, storeName, productName, amountLimit, addAsStorePaymentPolicy);
        }
        return -1;
    }

    @Override
    public Integer createMinProductAmountAllContentBagConstraint(String memberUserName, String storeName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createMinProductAmountAllContentBagConstraint(memberUserName, storeName, productName, amountLimit, addAsStorePaymentPolicy);
        }
        return -1;
    }

    @Override
    public Integer createMaxTimeAtDayProductBagConstraint(String memberUserName, String storeName, String productName, int hour, int minute, boolean addAsStorePaymentPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createMaxTimeAtDayProductBagConstraint(memberUserName, storeName, productName, hour, minute,  addAsStorePaymentPolicy);
        }
        return -1;
    }

    @Override
    public Integer createRangeOfDaysProductBagConstraint(String memberUserName, String storeName, String productName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createRangeOfDaysProductBagConstraint(memberUserName,storeName,productName,fromYear,fromMonth, fromDay, toYear, toMonth, toDay,addAsStorePaymentPolicy);
        }
        return -1;
    }

    @Override
    public Integer createMaxTimeAtDayCategoryBagConstraint(String memberUserName, String storeName, String categoryName, int hour, int minute, boolean addAsStorePaymentPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createMaxTimeAtDayCategoryBagConstraint(memberUserName,storeName,categoryName,hour,minute,addAsStorePaymentPolicy);
        }
        return -1;
    }

    @Override
    public Integer createRangeOfDaysCategoryBagConstraint(String memberUserName, String storeName, String categoryName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createRangeOfDaysCategoryBagConstraint(memberUserName,storeName,categoryName,fromYear,fromMonth, fromDay, toYear, toMonth, toDay,addAsStorePaymentPolicy);
        }
        return -1;
    }

    @Override
    public Integer createAndBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createAndBagConstraint(memberUserName,storeName,firstBagConstraintId,secondBagConstraintId,addAsStorePaymentPolicy);
        }
        return -1;
    }

    @Override
    public Integer createOrBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createOrBagConstraint(memberUserName,storeName,firstBagConstraintId,secondBagConstraintId,addAsStorePaymentPolicy);
        }
        return -1;
    }

    @Override
    public Integer createOnlyIfBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createOnlyIfBagConstraint(memberUserName,storeName,firstBagConstraintId,secondBagConstraintId,addAsStorePaymentPolicy);
        }
        return -1;
    }

    @Override
    public Boolean addConstraintAsPaymentPolicy(String memberUserName, String storeName, Integer bagConstraintId) throws Exception {
        if(realBridge!=null){
            return realBridge.addConstraintAsPaymentPolicy(memberUserName,storeName,bagConstraintId);
        }
        return false;
    }

    @Override
    public Boolean removeConstraintFromPaymentPolicies(String memberUserName, String storeName, Integer bagConstraintId) throws Exception {
        if(realBridge!=null){
            return realBridge.removeConstraintFromPaymentPolicies(memberUserName,storeName,bagConstraintId);
        }
        return false;
    }

    @Override
    public List<String> getAllPaymentPolicies(String memberUserName, String storeName) throws Exception {
        if(realBridge!=null){
            return realBridge.getAllPaymentPolicies(memberUserName,storeName);
        }
        return null;
    }

    @Override
    public Integer createProductDiscountPolicy(String memberUserName, String storeName, String productName, int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createProductDiscountPolicy(memberUserName,storeName,productName,discountPercentage,addAsStoreDiscountPolicy);
        }
        return -1;
    }

    @Override
    public Integer createProductDiscountPolicyWithConstraint(String memberUserName, String storeName, String productName, int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createProductDiscountPolicyWithConstraint(memberUserName,storeName,productName,discountPercentage,bagConstraintId,addAsStoreDiscountPolicy);
        }
        return -1;
    }

    @Override
    public Integer createCategoryDiscountPolicy(String memberUserName, String storeName, String categoryName, int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createCategoryDiscountPolicy(memberUserName,storeName,categoryName,discountPercentage,addAsStoreDiscountPolicy);
        }
        return -1;
    }

    @Override
    public Integer createCategoryDiscountPolicyWithConstraint(String memberUserName, String storeName, String categoryName, int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createCategoryDiscountPolicyWithConstraint(memberUserName,storeName,categoryName,discountPercentage,bagConstraintId,addAsStoreDiscountPolicy);
        }
        return -1;
    }

    @Override
    public Integer createAllStoreDiscountPolicy(String memberUserName, String storeName, int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createAllStoreDiscountPolicy(memberUserName,storeName,discountPercentage,addAsStoreDiscountPolicy);
        }
        return -1;
    }

    @Override
    public Integer createAllStoreDiscountPolicyWithConstraint(String memberUserName, String storeName, int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createAllStoreDiscountPolicyWithConstraint(memberUserName,storeName,discountPercentage,bagConstraintId,addAsStoreDiscountPolicy);
        }
        return -1;
    }

    @Override
    public Integer createAdditionDiscountPolicy(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, boolean addAsStoreDiscountPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createAdditionDiscountPolicy(memberUserName,storeName,firstDiscountPolicyId,secondDiscountPolicyId,addAsStoreDiscountPolicy);
        }
        return -1;
    }

    @Override
    public Integer createAdditionDiscountPolicyWithConstraint(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createAdditionDiscountPolicyWithConstraint(memberUserName,storeName,firstDiscountPolicyId,secondDiscountPolicyId,bagConstraintId,addAsStoreDiscountPolicy);
        }
        return -1;
    }

    @Override
    public Integer createMaxValDiscountPolicy(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, boolean addAsStoreDiscountPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createMaxValDiscountPolicy(memberUserName,storeName,firstDiscountPolicyId,secondDiscountPolicyId,addAsStoreDiscountPolicy);
        }
        return -1;
    }

    @Override
    public Integer createMaxValDiscountPolicyWithConstraint(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        if(realBridge!=null){
            return realBridge.createMaxValDiscountPolicyWithConstraint(memberUserName,storeName,firstDiscountPolicyId,secondDiscountPolicyId,bagConstraintId,addAsStoreDiscountPolicy);
        }
        return -1;
    }

    @Override
    public Boolean addAsStoreDiscountPolicy(String memberUserName, String storeName, Integer discountPolicyId) throws Exception {
        if(realBridge!=null){
            return realBridge.addAsStoreDiscountPolicy(memberUserName,storeName,discountPolicyId);
        }
        return false;
    }

    @Override
    public Boolean removeFromStoreDiscountPolicies(String memberUserName, String storeName, Integer discountPolicyId) throws Exception {
        if(realBridge!=null){
            return realBridge.removeFromStoreDiscountPolicies(memberUserName,storeName,discountPolicyId);
        }
        return false;
    }

    @Override
    public List<String> getAllCreatedDiscountPolicies(String memberUserName, String storeName) throws Exception {
        if(realBridge!=null){
            return realBridge.getAllCreatedDiscountPolicies(memberUserName,storeName);
        }
        return null;
    }

    @Override
    public List<String> getAllStoreDiscountPolicies(String memberUserName, String storeName) throws Exception {
        if(realBridge!=null){
            return realBridge.getAllStoreDiscountPolicies(memberUserName,storeName);
        }
        return null;
    }

    @Override
    public Boolean AppointMemberAsSystemManager(String managerName, String otherMemberName) throws Exception {
        if(realBridge!=null){
            return realBridge.AppointMemberAsSystemManager(managerName,otherMemberName);
        }
        return false;
    }

    @Override
    public Boolean removeMemberBySystemManager(String managerName, String memberName) throws Exception {
        if(realBridge!=null){
            return realBridge.removeMemberBySystemManager(managerName,memberName);
        }
        return false;
    }

    @Override
    public Set<String> getAllSystemManagers(String managerName) throws Exception {
        if(realBridge!=null){
            return realBridge.getAllSystemManagers(managerName);
        }
        return null;
    }

    @Override
    public Double getCartPriceBeforeDiscount(String memberUserName) throws Exception {
        if(realBridge!=null){
            return realBridge.getCartPriceBeforeDiscount(memberUserName);
        }
        return 0.0;
    }

    @Override
    public Double getCartPriceAfterDiscount(String memberUserName) throws Exception {
        if(realBridge!=null){
            return realBridge.getCartPriceAfterDiscount(memberUserName);
        }
        return 0.0;
    }



    @Override
    //return 1=storeFounder, 2=storeOwner, 3=storeManager, -1= noRule
    public Integer getRuleForStore(String storeName, String memberName) throws Exception {
        if(realBridge!=null){
            return realBridge.getRuleForStore(storeName,memberName);
        }
        return -1;
    }
    @Override
    public boolean updateManagerPermissionsForStore(String ownerUserName, String storeName, String managerUserName, List<Integer> newPermissions) throws Exception {
        if(realBridge!=null){
            return realBridge.updateManagerPermissionsForStore(ownerUserName, storeName, managerUserName, newPermissions);
        }
        return false;
    }

    @Override
    public List<Integer> getManagerPermissionsForStore(String ownerUserName, String storeName, String managerUserName) throws Exception {
        if(realBridge!=null){
            return realBridge.getManagerPermissionsForStore(ownerUserName, storeName, managerUserName);
        }
        return new ArrayList<>();
    }
}