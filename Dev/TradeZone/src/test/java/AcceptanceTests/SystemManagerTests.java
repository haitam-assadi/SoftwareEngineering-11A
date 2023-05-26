package AcceptanceTests;
import org.junit.jupiter.api.*;

import java.util.List;
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class SystemManagerTests {

    String managerName;
    String managerPass;
    private String user;

    String memberName = "member1";
    String memberPass = "member1Pass";
    private ProxyBridge proxy= new ProxyBridge(new RealBridge());

    @BeforeAll
    public void setUp() throws Exception {
        managerName = proxy.initializeMarket();
        if(managerName.isEmpty()){
            System.out.println("exception thrown");
        }
        managerPass = "systemmanager1Pass";
        user = proxy.enterMarket();
        proxy.register(user,memberName,memberPass);
    }

    @Test
    public void login_manager_success(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_manager_fail_not_exit(){
        try{
            user = proxy.enterMarket();
            Assertions.assertThrows(Exception.class,()-> proxy.login(user, "not_manager",managerPass));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_manager_fail_wrong_pass(){
        try{
            user = proxy.enterMarket();
            Assertions.assertThrows(Exception.class,()-> proxy.login(user, managerPass,"wrongPass"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void assign_other_system_manager_success(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            Assertions.assertTrue(proxy.AppointMemberAsSystemManager(managerName,memberName));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void assign_other_system_manager_fail_not_member(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            Assertions.assertThrows(Exception.class,()->proxy.AppointMemberAsSystemManager(managerName,"not_member"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void assign_other_system_manager_fail_member_with_rule(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            user = proxy.enterMarket();
            Assertions.assertEquals(memberName,proxy.login(user,memberName,memberPass));
            Assertions.assertEquals("member1store",proxy.createStore(memberName,"member1store"));
            Assertions.assertThrows(Exception.class,()->proxy.AppointMemberAsSystemManager(managerName,memberName));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_member_success(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            Assertions.assertTrue(proxy.removeMemberBySystemManager(managerName,memberName));
            Assertions.assertFalse(proxy.getAllMembers().contains(memberName));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_member_with_rule_fail(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            user = proxy.enterMarket();
            Assertions.assertEquals(memberName,proxy.login(user,memberName,memberPass));
            Assertions.assertEquals("member1store",proxy.createStore(memberName,"member1store"));
            Assertions.assertThrows(Exception.class,()->proxy.removeMemberBySystemManager(managerName,memberName));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }







}
