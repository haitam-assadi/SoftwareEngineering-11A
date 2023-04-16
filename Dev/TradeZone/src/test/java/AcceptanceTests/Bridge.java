package AcceptanceTests;

import DomainLayer.DTO.ProductDTO;
import DomainLayer.DTO.StoreDTO;
import ServiceLayer.ResponseT;
import jdk.jshell.spi.ExecutionControl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Bridge {

    //I.1
    public ResponseT<Boolean> initializeMarket();

    //II.1.1
    public ResponseT<String> enterMarket();

    //II.1.2 && II.3
    public ResponseT<Boolean> exitMarket(String userName);

    //II.1.3
    public ResponseT<Boolean> register(String guestUserName, String newMemberUserName, String password);

    //II.1.4
    public ResponseT<String> login(String guestUserName, String MemberUserName, String password);

    //II.3.1
    public ResponseT<String> memberLogOut(String memberUserName);

    //II.3.2
    public ResponseT<String> createStore(String memberUserName, String newStoreName);

   //II.4.1.1
    public ResponseT<Boolean> addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, int price, String description, int amount);
   //II.4.1.2
    public ResponseT<Boolean> removeProductFromStock(String memberUserName, String storeName, String productName);

    public ResponseT<Boolean> addCategory(String userName, String categoryName, String storeName);

    public ResponseT<Boolean> getCategory(String userName, String categoryName,String storeName);

    //II.4.1.3
    public ResponseT<Boolean> updateProductName(String memberUserName, String storeName, String productName, String newName);
    public ResponseT<Boolean> updateProductPrice(String memberUserName, String storeName, String productName, int price);
    public ResponseT<Boolean> updateProductDescription(String memberUserName, String storeName, String productName, String newDescription);
    public ResponseT<Boolean> updateProductAmount(String memberUserName, String storeName, String productName, int amount);

    public ResponseT<Integer> getProductPrice(String s);

    public ResponseT<String> getProductDescription(String s);

    public ResponseT<Integer> getProductAmount(String storeName, String s);

    //II.4.4
    public ResponseT<Boolean> appointMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName);

    public ResponseT<List<String>> getStoreOwners(String storeName);

    public ResponseT<String> getOwnerAppointer(String OwnerName, String storeName);


    public ResponseT<List<String>> getStoreProducts(String userName, String storeName);



    public ResponseT<Boolean> appointMemberAsStoreManager(String memberUserName, String storeName, String newOwnerUserName);


    public ResponseT<List<String>> getStoreManagers(String storeName);

    public ResponseT<String> getManagerAppointer(String ManagerName, String storeName);

    //II.4.9
    public ResponseT<String> closeStore(String memberUserName, String storeName);

    public ResponseT<Boolean> canGetStoreInfo(String userName, String storeName);

    public ResponseT<String> getStoreNotification(String memberName, String storeName);

    //II.4.11
    public ResponseT<Map<Integer,List<String>>> getStoreRulesInfo(String ownerName,String storeName);





    // II.2.2
    public ResponseT<Map<String, String>> getProductInfoFromMarketByName(String userName, String productName); // map <storeName, productName>

    public ResponseT<Map<String, List<String>>> getProductInfoFromMarketByCategory(String userName, String categoryName); // map <storeName, List<productName>>

    public ResponseT<Map<String, List<String>>> getProductInfoFromMarketByKeyword(String userName, String keyword); // map <storeName, List<productName>>

    // II.2.3 + II.2.4
    public ResponseT<Boolean> addToCart(String userName, String storeName, String productName, Integer amount);

    public ResponseT<List<String>> getBag(String userName, String storeName);

    public ResponseT<Integer> getProductAmountInCart(String userName, String storeName, String productName);

    public ResponseT<Map<String, List<String>>> getCartContent(String userName); // map: <string bag.storeName, list<productName>>

    public ResponseT<Boolean> removeProductFromCart(String userName, String storeName, String productName);

    public ResponseT<Boolean> changeProductAmountInCart (String userName, String storeName, String productName, Integer newAmount);

}
