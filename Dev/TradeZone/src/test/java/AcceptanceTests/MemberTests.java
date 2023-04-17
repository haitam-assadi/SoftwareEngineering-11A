package AcceptanceTests;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class MemberTests {

    private ProxyBridge proxy= new ProxyBridge(new RealBridge());;
    private String user;

    @BeforeAll
    public void setUp() throws Exception {
        if(!proxy.initializeMarket()){
            System.out.println("exception thrown");
        }
        user = proxy.enterMarket();//guest default user name
        Assertions.assertNotEquals(user , "");
        System.out.println(user);
        try {
            proxy.register(user,"moslema","abc123456");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //login Test
    @Test
    public void login_success(){
        try{
            List<String> guests = proxy.getAllGuests();
            if(guests.size()>0){
                proxy.login(user,"Moslem Asaad","12345");
                guests = proxy.getAllGuests();
                Assertions.assertTrue(proxy.getAllOnlineMembers().contains("Moslem Asaad"));
                Assertions.assertFalse(guests.contains(user));
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_failed_wrong_password(){
        try{
            List<String> guests = proxy.getAllGuests();
            if(guests.size()>0){
                proxy.login(user,"Moslem Asaad","");
                guests = proxy.getAllGuests();
                Assertions.assertFalse(proxy.getAllOnlineMembers().contains("Moslem Asaad"));
                Assertions.assertTrue(guests.contains(user));
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_failed_member_not_exist(){
        try{
            List<String> guests = proxy.getAllGuests();
            if(guests.size()>0){
                proxy.login(user,"Obiq","12345");
                guests = proxy.getAllGuests();
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
            List<String> members = proxy.getAllOnlineMembers();
            String newGuestName = proxy.memberLogOut("Moslem Asaad");
            Assertions.assertFalse(members.contains("Moslem Asaad"));
            Assertions.assertNotEquals("Moslem Asaad",newGuestName);
            Assertions.assertTrue(newGuestName.contains("Guest"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void logout_not_online_member_fail(){
        try {
            String newGuestName = proxy.memberLogOut("Moslem Asaad");
            List<String> members = proxy.getAllOnlineMembers();
            Assertions.assertFalse(members.contains("Moslem Asaad"));
            Assertions.assertNotEquals("Moslem Asaad",newGuestName);
            Assertions.assertTrue(newGuestName.contains("Guest"));
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
            String storeFounder = proxy.getStoreFounderName(userName, storeName);
            Assertions.assertEquals(userName, storeFounder);
            String appointer = proxy.getOwnerAppointer(storeFounder,storeName);
            Assertions.assertTrue(appointer.isEmpty() || appointer == null);
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
            String storeFounder = proxy.getStoreFounderName(userName, storeName);
            Assertions.assertEquals(userName, storeFounder);
            int len = stores.size();
            String storeName1 = proxy.createStore(userName, "verit");//should return any thing but not verit
            Assertions.assertNotEquals(len + 1,proxy.getAllStores());
            Assertions.assertNotEquals(storeName1,storeName);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void create_2stores_success(){
        try {
            String userName = "Moslem Asaad";
            proxy.login(user, userName, "12345");
            List<String> stores = proxy.getAllStores();
            String storeName = proxy.createStore(userName, "verit");
            Assertions.assertTrue(stores.contains(storeName));
            String storeFounder = proxy.getStoreFounderName(userName, storeName);
            Assertions.assertEquals(userName, storeFounder);
            String appointer = proxy.getOwnerAppointer(storeFounder,storeName);
            Assertions.assertTrue(appointer.isEmpty() || appointer == null);
            storeName = proxy.createStore(userName, "Moslem Store");
            Assertions.assertTrue(stores.contains(storeName));
            storeFounder = proxy.getStoreFounderName(userName, storeName);
            Assertions.assertEquals(userName, storeFounder);
            appointer = proxy.getOwnerAppointer(storeFounder,storeName);
            Assertions.assertTrue(appointer.isEmpty() || appointer == null);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }







}