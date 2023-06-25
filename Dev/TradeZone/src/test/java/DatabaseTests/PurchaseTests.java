package DatabaseTests;

import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class PurchaseTests {
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
    String product2_store2 = "product2_store2";
    String category1 = "category1";
    String category2 = "category2";

    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("setup");
        proxy= new ProxyBridge(new RealBridge());
//        proxy.loadData();
        proxy.initializeMarket();
        user0 = proxy.enterMarket(); // system manager
        user1 = proxy.enterMarket(); // founder
        user2 = proxy.enterMarket(); // member
        user3 = proxy.enterMarket(); // guest
//        user4 = proxy.enterMarket();

        proxy.register(user1, member1Name, member1Password);
        proxy.register(user2, member2Name, member2Password);
//        proxy.register(user3, member3Name, member3Password);

        user0 = proxy.login(user0, systemManagerName, systemManagerPassword);
        user1 = proxy.login(user1, member1Name, member1Password);
        user2 = proxy.login(user2, member2Name, member2Password);
//        user3 = proxy.login(user3, member3Name, member3Password);

        proxy.createStore(member1Name, store1Name);
        proxy.createStore(member1Name, store2Name);

        proxy.addNewProductToStock(member1Name, store1Name, product1_store1, category1, 111.1, "description_product1_store1", 100);
        proxy.addNewProductToStock(member1Name, store2Name, product1_store2, category2, 222.2, "description", 200);
        proxy.addNewProductToStock(member1Name, store2Name, product2_store2, category2, 333.3, "description", 300);
    }

    @Test
    public void purchaseTest(){
        try {
            // member:
            proxy.addToCart(user2, store1Name, product1_store1, 10);
            proxy.addToCart(user2, store2Name, product1_store2, 20);
            proxy.addToCart(user2, store2Name, product2_store2, 30);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertTrue(proxy.purchaseCartByCreditCard(user2, "123456789", "7", "2024", user2,
                        "123", "123456789", user2, "address", "city", "country", "100200"));

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertTrue(proxy.getCartContent(user2).isEmpty());
            Assertions.assertTrue(proxy.getBag(user2, store1Name).isEmpty());
            Assertions.assertTrue(proxy.getBag(user2, store2Name).isEmpty());
            Assertions.assertEquals(100-10, proxy.getProductAmount(user1,store1Name, product1_store1));
            Assertions.assertEquals(200-20, proxy.getProductAmount(user1,store2Name, product1_store2));
            Assertions.assertEquals(300-30, proxy.getProductAmount(user1,store2Name, product2_store2));

            proxy.updateProductAmount(user1, store1Name, product1_store1, 30);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(30, proxy.getProductAmount(user1,store1Name, product1_store1));

                // check store deals:
            Assertions.assertEquals(1, proxy.getStoreDeals(user1, store1Name).size());
            Assertions.assertEquals(1, proxy.getStoreDeals(user1, store2Name).size());

                // check member deals:
            Assertions.assertEquals(2, proxy.getMemberDeals(systemManagerName, user2).size());

            // guest:
            proxy.addToCart(user3, store1Name, product1_store1, 10);
            proxy.addToCart(user3, store2Name, product1_store2, 20);
            proxy.addToCart(user3, store2Name, product2_store2, 30);

            Assertions.assertTrue(proxy.purchaseCartByCreditCard(user3, "123456789", "10", "2024", user3,
                    "111", "123456789", user3, "address2", "city2", "country2", "100200"));

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertTrue(proxy.getCartContent(user3).isEmpty());
            Assertions.assertTrue(proxy.getBag(user3, store1Name).isEmpty());
            Assertions.assertTrue(proxy.getBag(user3, store2Name).isEmpty());
            Assertions.assertEquals(30-10, proxy.getProductAmount(user0,store1Name, product1_store1));
            Assertions.assertEquals(200-20-20, proxy.getProductAmount(user0,store2Name, product1_store2));
            Assertions.assertEquals(300-30-30, proxy.getProductAmount(user0,store2Name, product2_store2));

            proxy.updateProductAmount(user1, store2Name, product1_store2, 50);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(50, proxy.getProductAmount(user0,store2Name, product1_store2));

                // check store deals:
            Assertions.assertEquals(2, proxy.getStoreDeals(user1, store1Name).size());
            Assertions.assertEquals(2, proxy.getStoreDeals(user1, store2Name).size());

        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    private void logOutMembers()throws Exception{
        proxy.memberLogOut(systemManagerName);
        proxy.memberLogOut(member1Name);
        proxy.memberLogOut(member2Name);
//        proxy.memberLogOut(member3Name);
    }
    private void initSystemServiceAndLoadDataAndLogIn() throws Exception {
        proxy= new ProxyBridge(new RealBridge());
        proxy.loadData();
        user0 = proxy.enterMarket();
        user1 = proxy.enterMarket();
        user2 = proxy.enterMarket();
        user3 = proxy.enterMarket();
        user0 = proxy.login(user0, systemManagerName, systemManagerPassword);
        user1 = proxy.login(user1, member1Name, member1Password);
        user2 = proxy.login(user2, member2Name, member2Password);
//        user3 = proxy.login(user3, member3Name, member3Password);
    }
}
