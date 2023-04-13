package AcceptanceTests;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class AcceptanceTests {

    private ProxyBridge proxy;

    @Mock
    private Bridge bridge;

    @BeforeAll
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        proxy = new ProxyBridge();
    }

    //enter market test
    @Test
    public void enter_market_success(){
        try {
            List<String> gusts = proxy.getAllgusts(); /*getAllguests method should return the current
                                                    online guests in the system*/
            int len = gusts.size();
            String user = proxy.enterMarket();
            int cart = proxy.getUserCart(user);/* gets the cart id */
            gusts = proxy.getAllgusts();
            Assertions.assertNotNull(user);
            Assertions.assertEquals(len + 1, gusts.size());
            Assertions.assertTrue(gusts.contains(user));
            Assertions.assertNotEquals(-1, cart);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //exit market guest test
    @Test
    public void exit_market_guest_success(){
        try{
            List<String> gusts = proxy.getAllgusts(); /*getAllguests method should return the current
                                                    online guests in the system*/
            if(gusts.size()>0) {//redundant check
                String userName = gusts.get(0);//?? should check this point
                int len = gusts.size();
                proxy.exitMarket(userName);
                int cart = proxy.getUserCart(userName);
                gusts = proxy.getAllgusts();
                Assertions.assertEquals(-1, cart);
                Assertions.assertEquals(len - 1, gusts.size());
                Assertions.assertFalse(gusts.contains(userName));
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //exit market guest test
    @Test
    public void exit_market_member_success(){
        try{
            List<String> members = proxy.getAllOnlineMembers();
            if (members.size()>0){//redundant check
                String userName = members.get(0);//assume that the member is the first one?????
                int len = members.size();
                proxy.exitMarket(userName);
                int cart = proxy.getUserCart(userName);
                members = proxy.getAllOnlineMembers();
                Assertions.assertNotEquals(-1,cart);
                Assertions.assertEquals(len - 1, members.size());
                Assertions.assertFalse(members.contains(userName));
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //register test
    @Test
    public void register_success(){
        try{
            List<String> guests = proxy.getAllgusts();
            String userName = guests.get(0);
            int cart = proxy.getUserCart(userName);
            proxy.register(userName,"Moslem Asaad","12345");
            int validate_cart = proxy.getUserCart("Moslem Asaad");
            guests = proxy.getAllgusts();
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
            List<String> members = proxy.getAllMembers();
            List<String> guests = proxy.getAllgusts();
            if(guests.size()>1) {
                String userName = guests.get(0);
                String userName1 = guests.get(1);
                int member_len = members.size();
                int guest_len = guests.size();
                members = proxy.getAllMembers();
                guests = proxy.getAllgusts();
                proxy.register(userName, "Moslem Asaad", "12345");
                proxy.register(userName1, "Moslem Asaad", "kkolaw");
                Assertions.assertEquals(member_len + 1, members.size());
                Assertions.assertEquals(guest_len - 1, guests.size());
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //login Test
    @Test
    public void login_success(){
        try{
            List<String> members = proxy.getAllMembers();
            List<String> guests = proxy.getAllgusts();
            if(guests.size()>0){
                String userName = guests.get(0);
                String memberName = members.get(0);
                String password = proxy.getMemberPassword(memberName);
                proxy.login(userName,memberName,password);
                guests = proxy.getAllgusts();
                Assertions.assertTrue(proxy.getAllOnlineMembers().contains(memberName));
                Assertions.assertFalse(guests.contains(userName));
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_failed_wrong_password(){
        try{
            List<String> members = proxy.getAllMembers();
            List<String> guests = proxy.getAllgusts();
            if(guests.size()>0){
                String userName = guests.get(0);
                String memberName = members.get(0);
                proxy.login(userName,memberName,"");
                guests = proxy.getAllgusts();
                Assertions.assertFalse(proxy.getAllOnlineMembers().contains(memberName));
                Assertions.assertTrue(guests.contains(userName));
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
                String userName = guests.get(0);
                proxy.login(userName,"","asldh");
                guests = proxy.getAllgusts();
                Assertions.assertFalse(proxy.getAllOnlineMembers().contains(""));
                Assertions.assertTrue(guests.contains(userName));
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


}
