package AcceptanceTests;
import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class SystemManagerTests {

    String managerName;
    String managerPass;
    private String user;

    String memberName = "member1";
    String memberPass = "member1Pass";

    String memberName2 = "member2";
    String memberPass2 = "member2Pass";
    String memberName3 = "member3";
    String memberPass3 = "member3Pass";

    String memberName4 = "member4";

    String memberPass4 = "member4Pass";
    private ProxyBridge proxy= new ProxyBridge(new RealBridge());

    @BeforeEach
    public void setUp() throws Exception {
        managerName = proxy.initializeMarket();
        if(managerName.isEmpty()){
            System.out.println("exception thrown");
        }
        managerPass = "systemmanager1Pass";
        user = proxy.enterMarket();
        proxy.register(user,memberName,memberPass);
        user = proxy.enterMarket();
        proxy.register(user,memberName2,memberPass2);
        user = proxy.enterMarket();
        proxy.register(user,memberName3,memberPass3);
        user = proxy.enterMarket();
        proxy.register(user,memberName4,memberPass4);
    }

    @AfterEach
    public void after(){
        proxy= new ProxyBridge(new RealBridge());
    }


    @Test
    public void login_manager_success(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            //Assertions.assertFalse(proxy.memberLogOut(managerName).isEmpty());
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
    public void assign_already_system_manager_fail(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            Assertions.assertTrue(proxy.AppointMemberAsSystemManager(managerName,memberName));
            Assertions.assertThrows(Exception.class,()-> proxy.AppointMemberAsSystemManager(memberName,managerName));
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
    public void assign_same_system_manager_fail(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            Assertions.assertThrows(Exception.class,()->proxy.AppointMemberAsSystemManager(managerName,managerName));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void assign_circular_system_manager_fail(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            Assertions.assertTrue(proxy.AppointMemberAsSystemManager(managerName,memberName));
            user = proxy.enterMarket();
            Assertions.assertEquals(memberName,proxy.login(user,memberName,memberPass));
            Assertions.assertTrue(proxy.AppointMemberAsSystemManager(memberName,memberName2));
            user = proxy.enterMarket();
            Assertions.assertEquals(memberName2,proxy.login(user,memberName2,memberPass2));
            Assertions.assertThrows(Exception.class,()->proxy.AppointMemberAsSystemManager(memberName2,managerName));
            Assertions.assertThrows(Exception.class,()->proxy.AppointMemberAsSystemManager(memberName2,memberName));
            Assertions.assertThrows(Exception.class,()->proxy.AppointMemberAsSystemManager(memberName2,memberName2));
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
            Assertions.assertEquals(memberName2,proxy.login(user,memberName2,memberPass2));
            Assertions.assertEquals("member1store",proxy.createStore(memberName2,"member1store"));
            Assertions.assertThrows(Exception.class,()->proxy.AppointMemberAsSystemManager(managerName,memberName2));
//            Assertions.assertFalse(proxy.memberLogOut(managerName).isEmpty());
            Assertions.assertFalse(proxy.memberLogOut(memberName2).isEmpty());

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //REQ 6.2

    @Test
    public void remove_member_success(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            Assertions.assertTrue(proxy.removeMemberBySystemManager(managerName,memberName3));
            Assertions.assertFalse(proxy.getAllMembers().contains(memberName3));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_member_without_being_system_manager_failure(){
        try{
            Exception exception = Assertions.assertThrows(Exception.class ,() -> proxy.removeMemberBySystemManager("not a system manager", memberName));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_unavailable_member_by_system_manager_failure(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            Exception exception = Assertions.assertThrows(Exception.class ,() -> proxy.removeMemberBySystemManager(managerName, "unavailable_member"));

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_store_founder_by_system_manager_failure(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            user = proxy.enterMarket();
            Assertions.assertEquals(memberName2,proxy.login(user,memberName2,memberPass2));
            Assertions.assertEquals("member2store2",proxy.createStore(memberName2,"member2store2"));
            Assertions.assertThrows(Exception.class,()->proxy.removeMemberBySystemManager(managerName,memberName2));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_store_owner_by_system_manager_failure(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            user = proxy.enterMarket();
            Assertions.assertEquals(memberName2,proxy.login(user,memberName2,memberPass2));
            Assertions.assertEquals("member2store2",proxy.createStore(memberName2,"member2store2"));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(memberName2, "member2store2", memberName3));
            Assertions.assertThrows(Exception.class,()->proxy.removeMemberBySystemManager(managerName,memberName3));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_store_manager_by_system_manager_failure(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            user = proxy.enterMarket();
            Assertions.assertEquals(memberName2,proxy.login(user,memberName2,memberPass2));
            Assertions.assertEquals("member2store2",proxy.createStore(memberName2,"member2store2"));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreManager(memberName2, "member2store2", memberName3));
            Assertions.assertThrows(Exception.class,()->proxy.removeMemberBySystemManager(managerName,memberName3));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_member_by_system_manager_without_being_logged_in_failure(){
        try{
            user = proxy.enterMarket();
            Exception exception = Assertions.assertThrows(Exception.class ,() -> proxy.removeMemberBySystemManager(managerName, memberName2));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_system_manager_by_another_system_manager_failure(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            Assertions.assertTrue(proxy.AppointMemberAsSystemManager(managerName, memberName2));
            Exception exception = Assertions.assertThrows(Exception.class ,() -> proxy.removeMemberBySystemManager(managerName, memberName2));
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
            Assertions.assertEquals(memberName2,proxy.login(user,memberName2,memberPass2));
            Assertions.assertEquals("member2store2",proxy.createStore(memberName2,"member2store2"));
            Assertions.assertThrows(Exception.class,()->proxy.removeMemberBySystemManager(managerName,memberName2));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void get_all_system_managers_success(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            Assertions.assertEquals(1,proxy.getAllSystemManagers(managerName).size());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void get_system_managers_get_info_store_success(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            user = proxy.enterMarket();
            Assertions.assertEquals(memberName,proxy.login(user,memberName,memberPass));
            Assertions.assertEquals("store1",proxy.createStore(memberName,"store1"));
            Assertions.assertEquals(memberName,proxy.getStoreFounderName(managerName,"store1"));
            proxy.addNewProductToStock(memberName,"store1","product1","cat1",10.5,"desc",50);
            Assertions.assertTrue(proxy.getStoreProducts(managerName,"store1").contains("product1"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void get_system_managers_get_info_store_fail(){
        try{
            user = proxy.enterMarket();
            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
            user = proxy.enterMarket();
            Assertions.assertEquals(memberName,proxy.login(user,memberName,memberPass));
            //Assertions.assertEquals("store1",proxy.createStore(memberName,"store1"));
            Assertions.assertThrows(Exception.class,()->proxy.getStoreFounderName(managerName,"store1"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

//    @Test
//    public void get_all_products_in_market_success(){
//        try{
//            user = proxy.enterMarket();
//            Assertions.assertEquals(managerName,proxy.login(user,managerName,managerPass));
//            user = proxy.enterMarket();
//            Assertions.assertEquals(memberName,proxy.login(user,memberName,memberPass));
//            Assertions.assertEquals("store1",proxy.createStore(memberName,"store1"));
//            proxy.addNewProductToStock(memberName,"store1","product1","cat1",10.5,"desc",50);
//            proxy.addNewProductToStock(memberName,"store1","product2","cat1",10.5,"desc",50);
//            proxy.addNewProductToStock(memberName,"store1","product3","cat1",10.5,"desc",50);
//            user = proxy.enterMarket();
//            Assertions.assertEquals(memberName2,proxy.login(user,memberName2,memberPass2));
//            Assertions.assertEquals("store2",proxy.createStore(memberName2,"store2"));
//            proxy.addNewProductToStock(memberName2,"store2","product4","cat2",10.5,"desc",50);
//            proxy.addNewProductToStock(memberName2,"store2","product5","cat2",10.5,"desc",50);
//            proxy.addNewProductToStock(memberName2,"store2","product6","cat2",10.5,"desc",50);
//
//        } catch (Exception e) {
//            Assertions.fail(e.getMessage());
//        }
//    }











}
