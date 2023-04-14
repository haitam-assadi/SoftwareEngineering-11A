package AcceptanceTests;

import DomainLayer.DTO.ProductDTO;
import DomainLayer.DTO.StoreDTO;

import java.util.Collection;
import java.util.List;

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

    public boolean addCategory(String userName, String categoryName);

    public boolean getCategory(String categoryName);

    //II.4.1.3
    public boolean updateProductName(String memberUserName, String storeName, String productName, String newName);
    public boolean updateProductPrice(String memberUserName, String storeName, String productName, int price);
    public boolean updateProductDescription(String memberUserName, String storeName, String productName, String newDescription);
    public boolean updateProductAmount(String memberUserName, String storeName, String productName, int amount);

    int getProductPrice(String s);

    String getProductDescription(String s);

    int getProductAmount(String s);

    //II.4.4
    public boolean appointMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName);

    List<String> getStoreOwners(String storeName);

    String getOwnerAppointer(String OwnerName, String storeName);
}
