package AcceptanceTests;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class MemberTests {

    private ProxyBridge proxy;
    private String user;
    @Mock
    private Bridge bridge;

    @BeforeAll
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        proxy = new ProxyBridge();
        if(!proxy.initializeMarket()){
            throw new Exception(""); // should change
        }
        user = proxy.enterMarket();//guest default user name
        if(!proxy.register(user,"Moslem Asaad","12345")){
            throw new Exception("");
        }
    }

    //login Test
    @Test
    public void login_success(){
        try{
            List<String> guests = proxy.getAllgusts();
            if(guests.size()>0){
                proxy.login(user,"Moslem Asaad","12345");
                guests = proxy.getAllgusts();
                Assertions.assertTrue(proxy.getAllOnlineMembers().contains("Moslem Asaad"));
                Assertions.assertFalse(guests.contains(user));
                Assertions.assertNotEquals(-1,proxy.getUserCart("Moslem Asaad"));
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_failed_wrong_password(){
        try{
            List<String> guests = proxy.getAllgusts();
            if(guests.size()>0){
                int cart = proxy.getUserCart(user);
                proxy.login(user,"Moslem Asaad","");
                guests = proxy.getAllgusts();
                Assertions.assertFalse(proxy.getAllOnlineMembers().contains("Moslem Asaad"));
                Assertions.assertTrue(guests.contains(user));
                Assertions.assertEquals(cart,proxy.getUserCart(user));
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_failed_member_not_exist(){
        try{
            List<String> guests = proxy.getAllgusts();
            if(guests.size()>0){
                proxy.login(user,"Obiq","12345");
                guests = proxy.getAllgusts();
                Assertions.assertFalse(proxy.getAllOnlineMembers().contains("Obiq"));
                Assertions.assertTrue(guests.contains(user));
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //exit market guest test
    @Test
    public void exit_market_member_success(){
        try{
            proxy.login(user,"Moslem Asaad","12345");
            String userName = "Moslem Asaad";
            List<String> members = proxy.getAllOnlineMembers();
            int len = members.size();
            proxy.exitMarket(userName);
            int cart = proxy.getUserCart(userName);
            Assertions.assertNotEquals(-1,cart);
            Assertions.assertEquals(len - 1, members.size());//??
            Assertions.assertFalse(members.contains(userName));//??
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //member logout
    @Test
    public void logout_success(){
        try {
            proxy.login(user, "Moslem Asaad", "12345");
            int cart = proxy.getUserCart("Moslem Asaad");
            List<String> members = proxy.getAllOnlineMembers();
            String newGuestName = proxy.memberLogOut("Moslem Asaad");
            int cart1 = proxy.getUserCart(newGuestName);
            Assertions.assertFalse(members.contains("Moslem Asaad"));
            Assertions.assertNotEquals("Moslem Asaad",newGuestName);
            Assertions.assertTrue(newGuestName.contains("Guest"));
            Assertions.assertNotEquals(cart,cart1);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void logout_not_online_member_fail(){
        try {
            int cart = proxy.getUserCart("Moslem Asaad");
            String newGuestName = proxy.memberLogOut("Moslem Asaad");
            List<String> members = proxy.getAllOnlineMembers();
            int cart1 = proxy.getUserCart(newGuestName);
            Assertions.assertFalse(members.contains("Moslem Asaad"));
            Assertions.assertNotEquals("Moslem Asaad",newGuestName);
            Assertions.assertTrue(newGuestName.contains("Guest"));
            Assertions.assertNotEquals(cart,cart1);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void logout_double_fail(){
        try {
            proxy.login(user, "Moslem Asaad", "12345");
            String newGuestName = proxy.memberLogOut("Moslem Asaad");
            List<String> members = proxy.getAllOnlineMembers();
            int len = members.size();
            String newGuestName1 = proxy.memberLogOut("Moslem Asaad");
            Assertions.assertFalse(members.contains("Moslem Asaad"));
            Assertions.assertNotEquals("Moslem Asaad",newGuestName);
            Assertions.assertTrue(newGuestName.contains("Guest"));
            Assertions.assertNotEquals("Moslem Asaad",newGuestName1);
            Assertions.assertEquals(newGuestName,newGuestName1);
            Assertions.assertEquals(len,members.size());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //create store test
    @Test
    public void create_store_success(){
        try {
            String userName = "Moslem Asaad";
            proxy.login(user, userName, "12345");
            List<String> stores = proxy.getAllStores();
            String storeName = proxy.createStore(userName, "verit");
            Assertions.assertTrue(stores.contains(storeName));
            String storeFounder = proxy.getStoreFounder(storeName);
            Assertions.assertEquals(userName, storeFounder);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void create_store_repeated_storeName_fail(){
        try {
            String userName = "Moslem Asaad";
            proxy.login(user, userName, "12345");
            List<String> stores = proxy.getAllStores();
            String storeName = proxy.createStore(userName, "verit");
            Assertions.assertTrue(stores.contains(storeName));
            String storeFounder = proxy.getStoreFounder(storeName);
            Assertions.assertEquals(userName, storeFounder);
            int len = stores.size();
            String storeName1 = proxy.createStore(userName, "verit");//should return any thing but not verit
            Assertions.assertNotEquals(len + 1,proxy.getAllStores());
            Assertions.assertNotEquals(storeName1,storeName);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //II.4.1
    //add product test
    @Test
    public void add_new_product_success(){
        try{
            String userName = "Moslem Asaad";
            proxy.login(user, userName, "12345");
            String storeName = proxy.createStore(userName, "Moslem store");
            proxy.addNewProductToStock(userName,storeName,"iphone 14",3000,50);
            Assertions.assertTrue(proxy.getStoreProducts(storeName).contains("iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_new_product_repeated_productName_fail(){
        try{
            String userName = "Moslem Asaad";
            proxy.login(user, userName, "12345");
            String storeName = proxy.createStore(userName, "Moslem store");
            proxy.addNewProductToStock(userName,storeName,"iphone 14",3000,50);
            List<String> products = proxy.getStoreProducts(storeName);
            int len = products.size();
            proxy.addNewProductToStock(userName,storeName,"iphone 14",1500,10);
            Assertions.assertTrue(products.contains("iphone 14"));
            Assertions.assertEquals(len,proxy.getStoreProducts(storeName).size());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //delete product test
    @Test
    public void delete_product_test_success(){
        try{
            String userName = "Moslem Asaad";
            proxy.login(user, userName, "12345");
            String storeName = proxy.createStore(userName, "Moslem store");
            proxy.addNewProductToStock(userName,storeName,"iphone 14",3000,50);
            proxy.removeProductFromStock(userName,storeName,"iphone 14");
            Assertions.assertFalse(proxy.getStoreProducts(storeName).contains("iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void delete_product_test_not_exist_fail(){
        try{
            String userName = "Moslem Asaad";
            proxy.login(user, userName, "12345");
            String storeName = proxy.createStore(userName, "Moslem store");
            proxy.addNewProductToStock(userName,storeName,"iphone 14",3000,50);
            List<String> products = proxy.getStoreProducts(storeName);
            int len = products.size();
            Assertions.assertFalse(proxy.removeProductFromStock(userName,storeName,"iphone 11"));
            Assertions.assertEquals(len,proxy.getStoreProducts(storeName).size());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }




}
