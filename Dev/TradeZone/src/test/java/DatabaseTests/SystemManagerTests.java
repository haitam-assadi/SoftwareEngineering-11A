package DatabaseTests;

import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class SystemManagerTests {
    private ProxyBridge proxy= new ProxyBridge(new RealBridge());
    private String user0;
    private String user1;
    private String user2;
    private String user3;
    private String user4;

    String systemManagerName = "systemmanager1";
    String systemManagerPassword = "systemmanager1Pass";
    String member1Name = "member1name";
    String member1Password = "Aa12345678";
    String member2Name = "member2name";
    String member2Password = "Bb129271346";
    String member3Name = "member3name";
    String member3Password = "kK129B71346";
    String member4Name = "member4name";
    String member4Password = "kK129B71346";
    String store1Name = "store1";
    String store2Name = "store2";
    String product1_store1 = "product1_store1";
    String product1_store2 = "product1_store2";
    String category = "category";

    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("setup");
        proxy= new ProxyBridge(new RealBridge());
//        proxy.loadData();
        proxy.initializeMarket();
        user0 = proxy.enterMarket(); // system manager
        user1 = proxy.enterMarket(); // founder
        user2 = proxy.enterMarket(); // logged in member to remove
        user3 = proxy.enterMarket(); // logged out member to remove
        user4 = proxy.enterMarket(); // member to be appointed as system manager

        proxy.register(user1, member1Name, member1Password);
        proxy.register(user2, member2Name, member2Password);
        proxy.register(user3, member3Name, member3Password);
        proxy.register(user4, member4Name, member4Password);

        user0 = proxy.login(user0, systemManagerName, systemManagerPassword);
        user1 = proxy.login(user1, member1Name, member1Password);
        user2 = proxy.login(user2, member2Name, member2Password);
        user3 = proxy.login(user3, member3Name, member3Password);

        proxy.createStore(member1Name, store1Name);
        proxy.createStore(member1Name, store2Name);

        proxy.addNewProductToStock(member1Name, store1Name, product1_store1, category, 111.1, "description_product1_store1", 100);
        proxy.addNewProductToStock(member1Name, store2Name, product1_store2, category, 222.2, "description", 200);
    }

    @Test
    public void systemManagerTest(){
        try{
            proxy.addToCart(user2, store1Name, product1_store1, 10);
            proxy.addToCart(user3, store1Name, product1_store1, 20);
            proxy.addToCart(user3, store2Name, product1_store2, 30);

            proxy.memberLogOut(user3);

//            logOutMembers();
//            initSystemServiceAndLoadDataAndLogIn();

            // remove member:
            proxy.removeMemberBySystemManager(user0, user2); // user2 is logged in
            proxy.removeMemberBySystemManager(user0, user3); // user3 is logged out

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            // user2 tests:
            Assertions.assertFalse(proxy.getAllMembers().contains(user2));
            Assertions.assertFalse(proxy.getAllOnlineMembers().contains(user2));
            Assertions.assertThrows(Exception.class, ()->proxy.getBag(user2, store1Name));
            Assertions.assertThrows(Exception.class, ()->proxy.getCartContent(user2));

            // user3 tests:
            Assertions.assertFalse(proxy.getAllMembers().contains(user3));
            Assertions.assertThrows(Exception.class, ()->proxy.getBag(user3, store1Name));
            Assertions.assertThrows(Exception.class, ()->proxy.getBag(user3, store2Name));
            Assertions.assertThrows(Exception.class, ()->proxy.getCartContent(user3));

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            // appoint system manager:
            Assertions.assertTrue(proxy.AppointMemberAsSystemManager(user0, member4Name));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllSystemManagers(user0).contains(member4Name));

        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    private void logOutMembers()throws Exception{
        proxy.memberLogOut(systemManagerName);
        proxy.memberLogOut(member1Name);
//        proxy.memberLogOut(member2Name); // TODO: maybe this should be deleted
//        proxy.memberLogOut(member3Name);
    }
    private void initSystemServiceAndLoadDataAndLogIn() throws Exception {
        proxy= new ProxyBridge(new RealBridge());
        proxy.loadData();
        user0 = proxy.enterMarket();
        user1 = proxy.enterMarket();
//        user2 = proxy.enterMarket(); // TODO: maybe this should be deleted
//        user4 = proxy.enterMarket();
//        user3 = proxy.enterMarket();
        user0 = proxy.login(user0, systemManagerName, systemManagerPassword);
        user1 = proxy.login(user1, member1Name, member1Password);
//        user2 = proxy.login(user2, member2Name, member2Password); // TODO: maybe this should be deleted
//        user3 = proxy.login(user3, member3Name, member3Password);
    }
}
