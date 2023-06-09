package AcceptanceTests;

import ServiceLayer.ResponseT;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Bridge {

    //I.1
    public String initializeMarket() throws Exception;

    //II.1.1
    public String enterMarket() throws Exception;

    //II.1.2 && II.3
    public boolean exitMarket(String userName) throws Exception;

    //II.1.3
    public boolean register(String guestUserName, String newMemberUserName, String password) throws Exception;

    //II.1.4
    public String login(String guestUserName, String MemberUserName, String password) throws Exception;

    //II.3.1
    public String memberLogOut(String memberUserName) throws Exception;

    //II.3.2
    public String createStore(String memberUserName, String newStoreName) throws Exception;

    //II.4.1.1
    public boolean addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, Double price, String description, int amount) throws Exception;
    //II.4.1.2
    public boolean removeProductFromStock(String memberUserName, String storeName, String productName) throws Exception;

    public boolean addCategory(String userName, String categoryName, String storeName) throws Exception;

    public boolean getCategory(String userName, String categoryName,String storeName) throws Exception;

    //II.4.1.3
    public boolean updateProductName(String memberUserName, String storeName, String productName, String newName) throws Exception;
    public boolean updateProductPrice(String memberUserName, String storeName, String productName, Double price) throws Exception;
    public boolean updateProductDescription(String memberUserName, String storeName, String productName, String newDescription) throws Exception;
    public boolean updateProductAmount(String memberUserName, String storeName, String productName, int amount) throws Exception;

//    int getProductPrice(String s); // delete

//    String getProductDescription(String s); // delete

    // TODO: add the func to market class or add a field to ProductDTO class
    // maybe we need to add userName parameter
    int getProductAmount(String storeName, String s) throws Exception; // in stock

    //II.4.4
    public boolean appointOtherMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName) throws Exception;

//    List<String> getStoreOwners(String storeName); // delete

    String getOwnerAppointer(String OwnerName, String storeName) throws Exception;

    public List<String> getStoreProducts(String userName, String storeName) throws Exception;

    public boolean appointOtherMemberAsStoreManager(String memberUserName, String storeName, String newOwnerUserName) throws Exception;

//    List<String> getStoreManagers(String storeName); // delete

    String getManagerAppointer(String ManagerName, String storeName) throws Exception;

    //II.4.9
    public boolean closeStore(String memberUserName, String storeName) throws Exception;

    public boolean canGetStoreInfo(String userName, String storeName) throws Exception;

    //II.4.11
    public Map<Integer,List<String>> getStoreRulesInfo(String ownerName,String storeName) throws Exception;

    public List<String> getAllStoresNames() throws Exception;
    // II.2.1
    public String getStoreFounderName(String userName, String storeName) throws Exception;

    public List<String> getStoreOwnersNames(String userName, String storeName) throws Exception;

    public List<String> getStoreManagersNames(String userName, String storeName) throws Exception;

    public Double getProductPrice(String userName, String storeName, String productName) throws Exception;

    public String  getProductDescription(String userName, String storeName, String productName) throws Exception;

    // II.2.2
    public Map<String, List<String>> getProductInfoFromMarketByName(String userName, String productName) throws Exception; // map <storeName, List<productName>>

    public Map<String, List<String>> getProductInfoFromMarketByCategory(String userName, String categoryName) throws Exception; // map <storeName, List<productName>>

    public Map<String, List<String>> getProductInfoFromMarketByKeyword(String userName, String keyword) throws Exception; // map <storeName, List<productName>>

    public Map<String, List<String>> filterByPrice(String userName, Map<String, List<String>> products, int minPrice, int maxPrice) throws Exception; // map <storeName, List<productName>>

    public Map<String, List<String>> filterByCategory(String userName, Map<String, List<String>> products, String categoryName) throws Exception; // map <storeName, List<productName>>


    // II.2.3 + II.2.4
    public boolean addToCart(String userName, String storeName, String productName, Integer amount) throws Exception;

    public List<String> getBag(String userName, String storeName) throws Exception; // list<produceName>

    public int getProductAmountInCart(String userName, String storeName, String productName) throws Exception;

    public Map<String, List<String>> getCartContent(String userName) throws Exception; // map: <string bag.storeName, list<productName>>

    public boolean removeProductFromCart(String userName, String storeName, String productName) throws Exception;

    public boolean changeProductAmountInCart (String userName, String storeName, String productName, Integer newAmount) throws Exception;

    public boolean removeOwnerByHisAppointer(String appointerUserName, String storeName, String ownerUserName ) throws Exception;

    public boolean purchaseCartByCreditCard(String userName, String cardNumber, String month, String year, String holder, String cvv, String id, String receiverName,String shipmentAddress,String shipmentCity,String shipmentCountry,String zipCode) throws Exception;

    public Integer createMaxProductAmountAllContentBagConstraint(String memberUserName, String storeName, String productName, int amountLimit, boolean addAsStorePaymentPolicy)  throws Exception;

    public Integer createMinProductAmountAllContentBagConstraint(String memberUserName, String storeName, String productName, int amountLimit, boolean addAsStorePaymentPolicy)  throws Exception;

    public Integer createMaxTimeAtDayProductBagConstraint(String memberUserName, String storeName, String productName, int hour, int minute, boolean addAsStorePaymentPolicy)  throws Exception;

    public Integer createRangeOfDaysProductBagConstraint(String memberUserName, String storeName, String productName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy)  throws Exception;

    public Integer createMaxTimeAtDayCategoryBagConstraint(String memberUserName, String storeName, String categoryName, int hour, int minute, boolean addAsStorePaymentPolicy) throws Exception;

    public Integer createRangeOfDaysCategoryBagConstraint(String memberUserName, String storeName, String categoryName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy) throws Exception;

    public Integer createAndBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception;

    public Integer createOrBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception;

    public Integer createOnlyIfBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception;

    Boolean addConstraintAsPaymentPolicy(String memberUserName, String storeName, Integer bagConstraintId) throws Exception;

    Boolean removeConstraintFromPaymentPolicies(String memberUserName, String storeName, Integer bagConstraintId) throws Exception;

    List<String> getAllPaymentPolicies(String memberUserName, String storeName) throws Exception;

    Integer createProductDiscountPolicy(String memberUserName, String storeName, String productName, int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception;

    Integer createProductDiscountPolicyWithConstraint(String memberUserName, String storeName, String productName, int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception;

    Integer createCategoryDiscountPolicy(String memberUserName, String storeName, String categoryName, int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception;

    Integer createCategoryDiscountPolicyWithConstraint(String memberUserName, String storeName, String categoryName, int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception;

    Integer createAllStoreDiscountPolicy(String memberUserName, String storeName, int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception;

    Integer createAllStoreDiscountPolicyWithConstraint(String memberUserName, String storeName, int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception;

    Integer createAdditionDiscountPolicy(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, boolean addAsStoreDiscountPolicy) throws Exception;

    Integer createAdditionDiscountPolicyWithConstraint(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception;

    Integer createMaxValDiscountPolicy(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, boolean addAsStoreDiscountPolicy) throws Exception;

    Integer createMaxValDiscountPolicyWithConstraint(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception;

    Boolean addAsStoreDiscountPolicy(String memberUserName, String storeName, Integer discountPolicyId) throws Exception;

    Boolean removeFromStoreDiscountPolicies(String memberUserName, String storeName, Integer discountPolicyId) throws Exception;

    List<String> getAllCreatedDiscountPolicies(String memberUserName, String storeName) throws Exception;

    List<String> getAllStoreDiscountPolicies(String memberUserName, String storeName) throws Exception;

    public Boolean AppointMemberAsSystemManager(String managerName, String otherMemberName) throws Exception;

    public Boolean removeMemberBySystemManager(String managerName, String memberName) throws Exception;

    public Set<String> getAllSystemManagers(String managerName) throws Exception;

    Double getCartPriceBeforeDiscount(String memberUserName) throws Exception;

    Double getCartPriceAfterDiscount(String memberUserName) throws Exception;
}