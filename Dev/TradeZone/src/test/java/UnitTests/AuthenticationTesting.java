package UnitTests;

import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.Controller.StoreMapper;
import DomainLayer.Cart;
import DomainLayer.Market;
import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class AuthenticationTesting {
    private Market market;

    private String user1;

    private String newUserName1 = "newuser1";

    private String newUserPass1 = "new user pass1";

    private String newUserName2 = "newuser2";

    private String newUserPass2 = "new user pass2";

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        market = new Market();
        Market.dbFlag = false;
        StoreMapper.initMapper();
        MemberMapper.initMapper();
        user1 = market.enterMarket();
    }

    //REGISTRATION TESTS:
    /*
        password should include at least 1 char, 1 num and length at least 8.
        username there is syntax constrains
    */

    @Test
    public void register_new_user_success(){
        try{
            Assertions.assertTrue(market.register(user1, newUserName1, newUserPass1));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void register_a_registered_user_failure(){
        try{
            Assertions.assertTrue(market.register(user1, newUserName1, newUserPass1));
            assertThrows(Exception.class,
                    () -> {Assertions.assertTrue(market.register(user1, newUserName1, newUserPass1));});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void register_user_with_membership_failure(){
        try{
            String user2 = market.enterMarket();
            Assertions.assertTrue(market.register(user1, newUserName1, newUserPass1));
            assertThrows(Exception.class,
                    () -> {Assertions.assertTrue(market.register(user2, newUserName1, newUserPass2));});

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void register_user_with_null_name_failure(){
        try{
            String null_name = null;
            assertThrows(Exception.class,
                    () -> {Assertions.assertTrue(market.register(user1, null_name, newUserPass1));});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void register_user_with_blank_name_failure(){
        try{
            String empty_name = "";
            assertThrows(Exception.class,
                    () -> {Assertions.assertTrue(market.register(user1, empty_name, newUserPass1));});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void register_user_with_space_in_name_failure(){
        try{
            String user2 = market.enterMarket();
            String space_name = " ";
            assertThrows(Exception.class,
                    () -> {Assertions.assertTrue(market.register(user2, "     " + newUserName1, newUserPass1));});
            assertThrows(Exception.class,
                    () -> {Assertions.assertTrue(market.register(user2, space_name, newUserPass2));});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void register_user_with_special_characters_in_name_failure(){
        try{
            assertThrows(Exception.class,
                    () -> {Assertions.assertTrue(market.register(user1, newUserName1 + "!", newUserPass1));});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void register_user_with_short_user_name_failure(){
        try{
            String short_name = "us";
            assertThrows(Exception.class,
                    () -> {Assertions.assertTrue(market.register(user1, short_name, newUserPass1));});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void register_user_with_short_password_failure(){
        try{
            String short_password = "Pass1";
            assertThrows(Exception.class,
                    () -> {Assertions.assertTrue(market.register(user1, newUserName1, short_password));});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void register_user_wit_password_not_including_capital_letter_failure(){
        try{
            String short_password = "passworddddd";
            assertThrows(Exception.class,
                    () -> {Assertions.assertTrue(market.register(user1, newUserName1, short_password));});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //LOGIN TESTS:

    @Test
    public void login_with_null_password_failure(){
        try{
            Assertions.assertTrue(market.register(user1, newUserName1, newUserPass1));
            assertThrows(Exception.class,
                    () -> {Assertions.assertEquals(newUserName1, market.login(user1, newUserName1, null));});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_with_empty_password_failure(){
        try{
            Assertions.assertTrue(market.register(user1, newUserName1, newUserPass1));
            assertThrows(Exception.class,
                    () -> {Assertions.assertEquals(newUserName1, market.login(user1, newUserName1, ""));});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_member_success(){
        try{
            Assertions.assertTrue(market.register(user1, newUserName1, newUserPass1));
            Assertions.assertEquals(newUserName1.toLowerCase(), market.login(user1, newUserName1, newUserPass1));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_with_unavailable_guest_name_failure(){
        try{
            String unavailable_user = "unavailable";
            assertThrows(Exception.class,
                    () -> {Assertions.assertEquals(newUserName1, market.login(unavailable_user, newUserName1, ""));});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_with_logged_in_user_name_failure(){
        try{
            Assertions.assertTrue(market.register(user1, newUserName1, newUserPass1));
            Assertions.assertEquals(newUserName1.toLowerCase(), market.login(user1, newUserName1, newUserPass1));
            assertThrows(Exception.class,
                    () -> {Assertions.assertEquals(newUserName1, market.login(user1, newUserName1, newUserPass1));});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_with_incorrect_password_failure(){
        try{
            String incorrectPass = "incorrect_pass";
            Assertions.assertTrue(market.register(user1, newUserName1, newUserPass1));
            assertThrows(Exception.class,
                    () -> {Assertions.assertEquals(newUserName1, market.login(user1, newUserName1, incorrectPass));});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_out_member_success(){
        try{
            String returnedGuesName = null;
            Assertions.assertTrue(market.register(user1, newUserName1, newUserPass1));
            Assertions.assertEquals(newUserName1.toLowerCase(), market.login(user1, newUserName1, newUserPass1));
            returnedGuesName = market.memberLogOut(newUserName1);
            Assertions.assertNotNull(returnedGuesName);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_out_without_being_logged_in_failure(){
        try{
            Assertions.assertTrue(market.register(user1, newUserName1, newUserPass1));
            assertThrows(Exception.class,
                    () -> {Assertions.assertNotNull(market.memberLogOut(newUserName1));});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
}