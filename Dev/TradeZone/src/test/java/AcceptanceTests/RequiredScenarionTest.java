package AcceptanceTests;


import org.junit.jupiter.api.*;

import java.lang.ref.PhantomReference;
import java.util.List;
import java.util.Map;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class RequiredScenarionTest {

    private ProxyBridge proxy;
    private String guest1UserName;
    private String guest2UserName;
    private String guest3UserName;

    private String user1 = "requser1";
    private String user2 = "requser2";
    private String user3 = "requser3";
    private String password = "Aa12345678";


    @BeforeAll
    public void setUp(){
        System.out.println("setUp function");
        proxy = new ProxyBridge(new RealBridge());
        try {
            proxy.initializeMarket();
            guest1UserName = proxy.enterMarket();
            guest2UserName = proxy.enterMarket();
            guest3UserName = proxy.enterMarket();
            proxy.register(guest1UserName,user1,password);
            proxy.register(guest2UserName,user2,password);
            proxy.register(guest3UserName,user3,password);

        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void requiredScenario(){
        try {
            String store1Name = "store1";
            String product1Name = "product1";
            String product2Name = "product2";
            String product3Name = "product3";
            String product4Name = "product4";
            String product5Name = "product5";
            String category = "category";
            proxy.login(guest1UserName,user1,password);
            proxy.login(guest2UserName,user2,password);
            proxy.login(guest3UserName,user3,password);
            proxy.createStore(user1,store1Name);
            Assertions.assertTrue(proxy.addNewProductToStock(user1,store1Name,product1Name,category, 100.0, "desc", 50));
            Assertions.assertThrows(Exception.class, () -> proxy.addNewProductToStock(user2,store1Name,product2Name,category, 100.0, "desc", 50));
            Assertions.assertThrows(Exception.class, () -> proxy.addNewProductToStock(user3,store1Name,product3Name,category, 100.0, "desc", 50));


            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(user1,store1Name, user2));
            Assertions.assertTrue(proxy.addNewProductToStock(user2,store1Name,product2Name,category, 100.0, "desc", 50));


            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(user2,store1Name, user3));
            Assertions.assertTrue(proxy.fillOwnerContract(user1,store1Name,user3,true));
            Assertions.assertTrue(proxy.addNewProductToStock(user3,store1Name,product3Name,category, 100.0, "desc", 50));

            // user3 didn't remove then user 2 is still owner
            Assertions.assertThrows(Exception.class, () -> proxy.removeOwnerByHisAppointer(user3, store1Name,user2));
            Assertions.assertTrue(proxy.addNewProductToStock(user2,store1Name,product4Name,category, 100.0, "desc", 50));


            // user3 did remove user2 then user 2 is not owner
            Assertions.assertTrue(proxy.removeOwnerByHisAppointer(user1, store1Name,user2));
            Assertions.assertThrows(Exception.class, () -> proxy.addNewProductToStock(user2,store1Name,product5Name,category, 100.0, "desc", 50));


        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }

    }
}
