package AcceptanceTests;
import ServiceLayer.ResponseT;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLOutput;
import java.util.List;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class GuestTests {

    private ProxyBridge proxy;

    @BeforeAll
    public void setUp() throws Exception {
        proxy = new ProxyBridge(new RealBridge());
        if(!proxy.initializeMarket().getValue()){
            throw new Exception(" didn't manage to initialize Market");
        }
    }

    //enter market test
    @Test
    public void enter_market_success(){
            List<String> guests = proxy.getAllGuests().getValue();
            int len = guests.size();

            ResponseT<String> userResponse = proxy.enterMarket();
            Assertions.assertFalse(userResponse.ErrorOccurred);
            String guestUserName = userResponse.getValue();

            guests = proxy.getAllGuests().getValue();

            Assertions.assertNotNull(guestUserName);
            Assertions.assertEquals(len + 1, guests.size());
            Assertions.assertTrue(guests.contains(guestUserName));
    }
/*
    @Test
    public void enter_market_2users_success(){
        try {
            List<String> gusts = proxy.getAllGuests();
            int len = gusts.size();
            String user = proxy.enterMarket();
            String user1 = proxy.enterMarket();
            int cart = proxy.getUserCart(user);
            int cart1 = proxy.getUserCart(user);
            gusts = proxy.getAllGuests();
            Assertions.assertNotEquals(user,user1); // the default guest name is different
            Assertions.assertEquals(len + 2, gusts.size());
            Assertions.assertTrue(gusts.contains(user) && gusts.contains(user1));
            Assertions.assertNotEquals(-1, cart);
            Assertions.assertNotEquals(-1, cart1);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //exit market guest test
    @Test
    public void exit_market_guest_success(){
        try{
            String userName = proxy.enterMarket();
            int cart = proxy.getUserCart(userName);
            List<String> gusts = proxy.getAllGuests();
            if(gusts.size()>0) {//redundant check
                int len = gusts.size();
                proxy.exitMarket(userName);
                gusts = proxy.getAllGuests();
                Assertions.assertEquals(-1, cart);
                Assertions.assertEquals(len - 1, gusts.size());
                Assertions.assertFalse(gusts.contains(userName));
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //register test
    @Test
    public void register_success(){
        try{
            List<String> guests = proxy.getAllGuests();
            String userName = proxy.enterMarket();
            int cart = proxy.getUserCart(userName);
            proxy.register(userName,"Moslem Asaad","12345");
            int validate_cart = proxy.getUserCart("Moslem Asaad");
            guests = proxy.getAllGuests();
            List<String> members = proxy.getAllMembers();
            Assertions.assertEquals(cart,validate_cart);
            Assertions.assertTrue(members.contains("MoslemAsaad"));
            Assertions.assertFalse(guests.contains(userName));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void register_failed(){
        try{
            List<String> guests = proxy.getAllGuests();
            String userName = proxy.enterMarket();
            String userName1 = proxy.enterMarket();
            List<String> members = proxy.getAllMembers();
            if(guests.size()>1) {
                int member_len = members.size();
                int guest_len = guests.size();
                guests = proxy.getAllGuests();
                proxy.register(userName, "Moslem Asaad", "12345");
                proxy.register(userName1, "Moslem Asaad", "kkolaw");
                Assertions.assertEquals(member_len + 1, members.size());
                Assertions.assertEquals(guest_len - 1, guests.size());
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
*/

}
