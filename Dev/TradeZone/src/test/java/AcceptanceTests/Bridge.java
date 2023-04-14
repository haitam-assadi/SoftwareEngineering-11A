package AcceptanceTests;

import DomainLayer.DTO.ProductDTO;
import DomainLayer.DTO.StoreDTO;

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
    public boolean addNewProductToStock(String memberUserName, String storeName, String product_name,int price, int amount);

   //II.4.1.2
    public boolean removeProductFromStock(String memberUserName, String storeName, String productName);

    //II.4.1.3
}
