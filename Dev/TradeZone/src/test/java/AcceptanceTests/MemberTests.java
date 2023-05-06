package AcceptanceTests;
import org.junit.jupiter.api.*;

import java.util.List;
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class MemberTests {


    String member1Name = "member1";
    String member1Password = "Aa123456";
    private ProxyBridge proxy= new ProxyBridge(new RealBridge());;
    private String user;

    @BeforeAll
    public void setUp() throws Exception {
        System.out.println("setup");
        if(!proxy.initializeMarket()){
            System.out.println("exception thrown");
        }
        user = proxy.enterMarket();
        proxy.register(user, member1Name, member1Password);
    }


    //login Test
    @Test
    public void login_success(){
        try{

            user = proxy.enterMarket();
            List<String> guests = proxy.getAllGuests();
            if(guests.size()>0){
                proxy.login(user, member1Name, member1Password);
                guests = proxy.getAllGuests();
                Assertions.assertTrue(proxy.getAllOnlineMembers().contains(member1Name));
                Assertions.assertFalse(guests.contains(user));
                user = proxy.memberLogOut(member1Name);
                proxy.exitMarket(user);
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


    @Test
    public void login_failed_wrong_password(){

        try{
            user = proxy.enterMarket();
            List<String> guests = proxy.getAllGuests();
            if(guests.size()>0){
                Assertions.assertThrows(Exception.class,()-> proxy.login(user, member1Name,""));
                guests = proxy.getAllGuests();
                Assertions.assertFalse(proxy.getAllOnlineMembers().contains(member1Name));
                Assertions.assertTrue(guests.contains(user));
                proxy.exitMarket(user);
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_failed_member_not_exist(){
        try{

            user = proxy.enterMarket();
            List<String> guests = proxy.getAllGuests();
            if(guests.size()>0){
                Assertions.assertThrows(Exception.class, ()-> proxy.login(user,"Obiq", member1Password));
                guests = proxy.getAllGuests();
                Assertions.assertFalse(proxy.getAllOnlineMembers().contains("Obiq"));
                Assertions.assertTrue(guests.contains(user));
                proxy.exitMarket(user);
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //exit market guest test
    @Test
    public void exit_market_member_success(){
        try{

            user = proxy.enterMarket();
            proxy.login(user, member1Name, member1Password);
            String userName = member1Name;
            List<String> members = proxy.getAllOnlineMembers();
            int len = members.size();
            proxy.exitMarket(userName);

            members = proxy.getAllOnlineMembers();
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

            user = proxy.enterMarket();
            proxy.login(user, member1Name, member1Password);
            String newGuestName = proxy.memberLogOut(member1Name);
            List<String> members = proxy.getAllOnlineMembers();
            Assertions.assertFalse(members.contains(member1Name));
            Assertions.assertNotEquals(member1Name,newGuestName);
            proxy.exitMarket(newGuestName);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void logout_not_online_member_fail(){
        try {

            user = proxy.enterMarket();
            proxy.login(user, member1Name, member1Password);
            proxy.memberLogOut(member1Name);
            proxy.memberLogOut(member1Name);
            Assertions.fail("logged out user managed to log out");
        }catch (Exception e){

        }
    }

    @Test
    public void logout_double_fail(){
        try {

            user=proxy.enterMarket();
            proxy.login(user, member1Name, member1Password);
            String newGuestName = proxy.memberLogOut(member1Name);
            List<String> members = proxy.getAllOnlineMembers();
            int len = members.size();
            Assertions.assertThrows(Exception.class,()-> proxy.memberLogOut(member1Name));
            members = proxy.getAllOnlineMembers();
            Assertions.assertEquals(len,members.size());
            Assertions.assertFalse(members.contains(member1Name));
            Assertions.assertNotEquals(member1Name,newGuestName);
            proxy.exitMarket(newGuestName);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


    //create store test
    @Test
    public void create_store_success(){
        try {

            user = proxy.enterMarket();
            String userName = member1Name;
            proxy.login(user, userName, member1Password);
            String storeName = proxy.createStore(userName, "verit");
            List<String> stores = proxy.getAllStoresNames();
            Assertions.assertTrue(stores.contains(storeName));
            String storeFounder = proxy.getStoreFounderName(userName, storeName);
            Assertions.assertEquals(userName, storeFounder);

            user = proxy.memberLogOut(member1Name);
            proxy.exitMarket(user);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void create_store_repeated_storeName_fail(){
        try {

            user = proxy.enterMarket();
            String userName = member1Name;
            proxy.login(user, userName, member1Password);
            String storeName = proxy.createStore(userName, "verit2");
            List<String> stores = proxy.getAllStoresNames();
            Assertions.assertTrue(stores.contains(storeName));
            String storeFounder = proxy.getStoreFounderName(userName, storeName);
            Assertions.assertEquals(userName, storeFounder);
            int len = stores.size();
            String storeName1 = proxy.createStore(userName, "verit21");//should return any thing but not verit
            Assertions.assertEquals(len + 1,proxy.getAllStoresNames().size());
            Assertions.assertNotEquals(storeName1,storeName);
            user = proxy.memberLogOut(member1Name);
            proxy.exitMarket(user);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void create_2stores_success(){
        try {

            user = proxy.enterMarket();
            String userName = member1Name;
            proxy.login(user, userName, member1Password);
            String storeName = proxy.createStore(userName, "verit3");

            List<String> stores = proxy.getAllStoresNames();
            Assertions.assertTrue(stores.contains(storeName));
            String storeFounder = proxy.getStoreFounderName(userName, storeName);
            Assertions.assertEquals(userName, storeFounder);
            storeName = proxy.createStore(userName, "Moslem Store1");
            stores = proxy.getAllStoresNames();
            Assertions.assertTrue(stores.contains(storeName));
            storeFounder = proxy.getStoreFounderName(userName, storeName);
            Assertions.assertEquals(userName, storeFounder);
            user = proxy.memberLogOut(member1Name);
            proxy.exitMarket(user);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }








}