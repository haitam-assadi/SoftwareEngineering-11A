package UnitTests;

import DomainLayer.Market;
import DomainLayer.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class NotificationsTests {

    private String userName1 = "username1";

    private String pass1 = "password1";

    private String userName2 = "username2";

    private String pass2 = "password2";

    private Market market;

    private String storeName = "new Store";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        market = new Market();
    }

    @Test
    public void send_message_for_logged_in_user_success() throws Exception {              //LIVE
        try{
            String user1 = market.enterMarket();
            Assertions.assertTrue(market.register(user1, userName1, pass1));
            Assertions.assertEquals(userName1, market.login(user1, userName1, pass1));
            String message = "New message sent";
            market.send(userName1, message);
            Assertions.assertEquals(1, market.getLiveMessages(userName1).size());
            Assertions.assertEquals(message, market.getLiveMessages(userName1).get(0));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void send_message_for_offline_user_success() throws Exception {              //PENDING
        try{
            String user1 = market.enterMarket();
            Assertions.assertTrue(market.register(user1, userName1, pass1));
            String message = "New message sent";
            market.send(userName1, message);
            Assertions.assertEquals(1, market.getAppendingMessages(userName1).size());
            Assertions.assertEquals(message, market.getAppendingMessages(userName1).get(0));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void close_store_and_notify_logged_in_worker_success(){
        try{
            String user1 = market.enterMarket();
            Assertions.assertTrue(market.register(user1, userName1, pass1));
            Assertions.assertEquals(userName1, market.login(user1, userName1, pass1));
            market.createStore(userName1, storeName);       //STORE FOUNDER
            String user2 = market.enterMarket();
            Assertions.assertTrue(market.register(user2, userName2, pass2));
            Assertions.assertEquals(userName2, market.login(user2, userName2, pass2));
            Assertions.assertTrue(market.appointOtherMemberAsStoreOwner(userName1, storeName, userName2));
            Assertions.assertEquals(1, market.getLiveMessages(userName2).size());
            Assertions.assertTrue(market.closeStore(userName1, storeName));
            Assertions.assertEquals(2, market.getLiveMessages(userName2).size());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void close_store_and_notify_offline_worker_success(){
        try{
            String user1 = market.enterMarket();
            Assertions.assertTrue(market.register(user1, userName1, pass1));
            Assertions.assertEquals(userName1, market.login(user1, userName1, pass1));
            market.createStore(userName1, storeName);       //STORE FOUNDER
            String user2 = market.enterMarket();
            Assertions.assertTrue(market.register(user2, userName2, pass2));
            Assertions.assertTrue(market.appointOtherMemberAsStoreOwner(userName1, storeName, userName2));
            Assertions.assertTrue(market.closeStore(userName1, storeName));
            Assertions.assertEquals(2, market.getAppendingMessages(userName2).size());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
}
