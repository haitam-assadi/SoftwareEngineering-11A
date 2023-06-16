package AcceptanceTests;

import org.junit.jupiter.api.*;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class NotificationsTests {
    private ProxyBridge proxy= new ProxyBridge(new RealBridge());;
    private String userName1 = "username1";
    private String pass1 = "password1";

    private String userName2 = "username2";

    private String pass2 = "password2";

    private String storeName = "new store";

    @BeforeAll
    public void setUp() throws Exception {
        System.out.println("setup");
        if(proxy.initializeMarket().isEmpty()){
            System.out.println("exception thrown");
        }
    }

    @Test
    public void send_message_for_logged_in_user_success() throws Exception {              //LIVE
        try{
            String user1 = proxy.enterMarket();
            Assertions.assertEquals(userName1, proxy.login(user1, userName1, pass1));
            String message = "New message sent";
            proxy.send(userName1, message);
            Assertions.assertEquals(1, proxy.getLiveMessages(userName1).size());
            Assertions.assertEquals(message, proxy.getLiveMessages(userName1).get(0));
            proxy.memberLogOut(userName1);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void send_message_for_offline_user_success() throws Exception {              //PENDING
        try{
            String user2 = proxy.enterMarket();
            String message = "New message sent";
            proxy.send(userName1, message);
            Assertions.assertEquals(1, proxy.getAppendingMessages(userName1).size());
            Assertions.assertEquals(message, proxy.getAppendingMessages(userName1).get(0));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void close_store_and_notify_logged_in_worker_success(){
        try{
            String user3 = proxy.enterMarket();
            Assertions.assertEquals(userName1, proxy.login(user3, userName1, pass1));
            String storeName2 = "new store 2";
            proxy.createStore(userName1, storeName2);       //STORE FOUNDER
            String user2 = proxy.enterMarket();
            Assertions.assertEquals(userName2, proxy.login(user2, userName2, pass2));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(userName1, storeName2, userName2));
            Assertions.assertTrue(proxy.closeStore(userName1, storeName2));
            Assertions.assertEquals(1, proxy.getLiveMessages(userName2).size());
            proxy.memberLogOut(userName1);
            proxy.memberLogOut(userName2);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void close_store_and_notify_offline_worker_success(){
        try{
            String user4 = proxy.enterMarket();
            Assertions.assertTrue(proxy.register(user4, userName1, pass1));
            Assertions.assertEquals(userName1, proxy.login(user4, userName1, pass1));
            proxy.createStore(userName1, storeName);       //STORE FOUNDER
            String user2 = proxy.enterMarket();
            Assertions.assertTrue(proxy.register(user2, userName2, pass2));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(userName1, storeName, userName2));
            Assertions.assertTrue(proxy.closeStore(userName1, storeName));
            Assertions.assertEquals(1, proxy.getAppendingMessages(userName2).size());
            proxy.memberLogOut(userName1);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

}
