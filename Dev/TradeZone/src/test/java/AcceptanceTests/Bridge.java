package AcceptanceTests;

import java.util.List;
import java.util.Map;

public interface Bridge {

    //I.1
    public boolean initializeMarket();

    //II.1.1
    public String enterMarket();

    //II.1.2 && II.3
    public boolean exitMarket(String userName);

    //II.1.3
    public boolean register(String guestUserName, String newMemberUserName, String password);

    //II.1.4
    public String login(String guestUserName, String MemberUserName, String password);

    //II.3.1
    public String memberLogOut(String memberUserName);

    //II.3.2
    public String createStore(String memberUserName, String newStoreName);

    //II.4.1.1
    public boolean addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, int price, String description, int amount);
    //II.4.1.2
    public boolean removeProductFromStock(String memberUserName, String storeName, String productName);

    public boolean addCategory(String userName, String categoryName, String storeName);

    public boolean getCategory(String userName, String categoryName,String storeName);

    //II.4.1.3
    public boolean updateProductName(String memberUserName, String storeName, String productName, String newName);
    public boolean updateProductPrice(String memberUserName, String storeName, String productName, int price);
    public boolean updateProductDescription(String memberUserName, String storeName, String productName, String newDescription);
    public boolean updateProductAmount(String memberUserName, String storeName, String productName, int amount);

//    int getProductPrice(String s); // delete

//    String getProductDescription(String s); // delete

    // TODO: add the func to market class or add a field to ProductDTO class
    // maybe we need to add userName parameter
    int getProductAmount(String storeName, String s); // in stock

    //II.4.4
    public boolean appointMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName);

//    List<String> getStoreOwners(String storeName); // delete

    String getOwnerAppointer(String OwnerName, String storeName);

    public List<String> getStoreProducts(String userName, String storeName);

    public boolean appointMemberAsStoreManager(String memberUserName, String storeName, String newOwnerUserName);

//    List<String> getStoreManagers(String storeName); // delete

    String getManagerAppointer(String ManagerName, String storeName);

    //II.4.9
    public String closeStore(String memberUserName, String storeName);

    public boolean canGetStoreInfo(String userName, String storeName);

    String getStoreNotification(String memberName, String storeName);

    //II.4.11
    public Map<Integer,List<String>> getStoreRulesInfo(String ownerName,String storeName);


    // II.2.1
    public String getStoreFounderName(String userName, String storeName) throws Exception;

    public List<String> getStoreOwnersNames(String userName, String storeName);

    public List<String> getStoreManagersNames(String userName, String storeName);

    public Double getProductPrice(String userName, String storeName, String productName);

    public String  getProductDescription(String userName, String storeName, String productName);

    // II.2.2
    public Map<String, List<String>> getProductInfoFromMarketByName(String userName, String productName); // map <storeName, List<productName>>

    public Map<String, List<String>> getProductInfoFromMarketByCategory(String userName, String categoryName); // map <storeName, List<productName>>

    public Map<String, List<String>> getProductInfoFromMarketByKeyword(String userName, String keyword); // map <storeName, List<productName>>

    public Map<String, List<String>> filterByPrice(String userName, Map<String, List<String>> products, int minPrice, int maxPrice); // map <storeName, List<productName>>

    public Map<String, List<String>> filterByCategory(String userName, Map<String, List<String>> products, String categoryName); // map <storeName, List<productName>>


    // II.2.3 + II.2.4
    public boolean addToCart(String userName, String storeName, String productName, Integer amount);

    public List<String> getBag(String userName, String storeName); // list<produceName>

    public int getProductAmountInCart(String userName, String storeName, String productName);

    public Map<String, List<String>> getCartContent(String userName); // map: <string bag.storeName, list<productName>>

    public boolean removeProductFromCart(String userName, String storeName, String productName);

    public boolean changeProductAmountInCart (String userName, String storeName, String productName, Integer newAmount);

}