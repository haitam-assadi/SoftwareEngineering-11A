package DatabaseTests;

import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)

public class BagConstraintTest {

    private ProxyBridge proxy= new ProxyBridge(new RealBridge());
    private String user1;
    private String user2;
    String member1Name = "member1name";
    String member1Password = "Aa12345678";
    String member2Name = "member2name";
    String member2Password = "Bb129271346";
    String store1Name = "store1";
    String product1_store1 = "product1_store1";
    String product2_store1 = "product2_store1";
    String product3_store1 = "product3_store1";
    String category1 = "category1";
    String category2 = "category2";


    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("setup");
        proxy= new ProxyBridge(new RealBridge());
        proxy.loadData();
//        proxy.initializeMarket();
        user1 = proxy.enterMarket(); // founder
        user2 = proxy.enterMarket(); // member

        proxy.register(user1, member1Name, member1Password);
        proxy.register(user2, member2Name, member2Password);

        user1 = proxy.login(user1, member1Name, member1Password);
        user2 = proxy.login(user2, member2Name, member2Password);

        proxy.createStore(member1Name, store1Name);

        proxy.addNewProductToStock(member1Name, store1Name, product1_store1, category1, 111.1, "description_product1_store1", 100);
        proxy.addNewProductToStock(member1Name, store1Name, product2_store1, category2, 222.2, "description", 200);
        proxy.addNewProductToStock(member1Name, store1Name, product3_store1, category2, 333.3, "description", 300);
    }

    @Test
    public void bagConstraintTest(){
        try {
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            // ******************** max product amount ********************
            proxy.addToCart(user2, store1Name, product1_store1, 30);
            // create:
            Assertions.assertEquals(1, proxy.createMaxProductAmountAllContentBagConstraint(user1, store1Name, product1_store1,
                                10, false)); // add constraint without activation
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(1, proxy.getAllBagConstraints(user1, store1Name).size());
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

            // activate:
            Assertions.assertTrue(proxy.addConstraintAsPaymentPolicy(user1, store1Name, 1));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(1, proxy.getAllPaymentPolicies(user1, store1Name).size());

            // purchase fail because there are 30 of product1 and it has to be max 10:
            Assertions.assertThrows(Exception.class, ()->proxy.purchaseCartByCreditCard(user2, "123456789", "7", "2024", user2,
                    "123", "123456789", user2, "address", "city", "country", "100200"));
            proxy.changeProductAmountInCart(user2, store1Name, product1_store1, 2);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            // purchase success:
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(user2, "123456789", "7", "2024", user2,
                    "123", "123456789", user2, "address", "city", "country", "100200"));
            // remove:
            Assertions.assertTrue(proxy.removeConstraintFromPaymentPolicies(user1, store1Name, 1));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

            // ******************** min product amount ********************
            proxy.addToCart(user2, store1Name, product1_store1, 3);
            // create:
            Assertions.assertEquals(2, proxy.createMinProductAmountAllContentBagConstraint(user1, store1Name, product1_store1,
                    5, false)); // add constraint without activation
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(2, proxy.getAllBagConstraints(user1, store1Name).size());
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

            // activate:
            Assertions.assertTrue(proxy.addConstraintAsPaymentPolicy(user1, store1Name, 2));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(1, proxy.getAllPaymentPolicies(user1, store1Name).size());

            // purchase fail because there are 3 of product1 and it has to be min 5:
            Assertions.assertThrows(Exception.class, ()->proxy.purchaseCartByCreditCard(user2, "123456789", "7", "2024", user2,
                    "123", "123456789", user2, "address", "city", "country", "100200"));
            proxy.changeProductAmountInCart(user2, store1Name, product1_store1, 15);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            // purchase success:
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(user2, "123456789", "7", "2024", user2,
                    "123", "123456789", user2, "address", "city", "country", "100200"));
            // remove:
            Assertions.assertTrue(proxy.removeConstraintFromPaymentPolicies(user1, store1Name, 2));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

            // ******************** product max time at day ********************
            // create:
            Assertions.assertEquals(3, proxy.createMaxTimeAtDayProductBagConstraint(user1, store1Name, product1_store1,
                    22, 30,  false)); // add constraint without activation
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(3, proxy.getAllBagConstraints(user1, store1Name).size());
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

            // activate:
            Assertions.assertTrue(proxy.addConstraintAsPaymentPolicy(user1, store1Name, 3));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(1, proxy.getAllPaymentPolicies(user1, store1Name).size());

            // remove:
            Assertions.assertTrue(proxy.removeConstraintFromPaymentPolicies(user1, store1Name, 3));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

            // ******************** product range of days ********************
            // create:
            Assertions.assertEquals(4, proxy.createRangeOfDaysProductBagConstraint(user1, store1Name, product1_store1,
                    2023, 7, 7, 2023, 8, 8, false)); // add constraint without activation
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(4, proxy.getAllBagConstraints(user1, store1Name).size());
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

            // activate:
            Assertions.assertTrue(proxy.addConstraintAsPaymentPolicy(user1, store1Name, 4));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(1, proxy.getAllPaymentPolicies(user1, store1Name).size());

            // remove:
            Assertions.assertTrue(proxy.removeConstraintFromPaymentPolicies(user1, store1Name, 4));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

            // ******************** category max time at day ********************
            // create:
            Assertions.assertEquals(5, proxy.createMaxTimeAtDayCategoryBagConstraint(user1, store1Name, category2,
                    23, 15,  false)); // add constraint without activation
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(5, proxy.getAllBagConstraints(user1, store1Name).size());
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

            // activate:
            Assertions.assertTrue(proxy.addConstraintAsPaymentPolicy(user1, store1Name, 5));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(1, proxy.getAllPaymentPolicies(user1, store1Name).size());

            // remove:
            Assertions.assertTrue(proxy.removeConstraintFromPaymentPolicies(user1, store1Name, 5));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

            // ******************** category range of days ********************
            // create:
            Assertions.assertEquals(6, proxy.createRangeOfDaysCategoryBagConstraint(user1, store1Name, category2,
                    2023, 7, 7, 2023, 8, 8, false)); // add constraint without activation
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(6, proxy.getAllBagConstraints(user1, store1Name).size());
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

            // activate:
            Assertions.assertTrue(proxy.addConstraintAsPaymentPolicy(user1, store1Name, 6));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(1, proxy.getAllPaymentPolicies(user1, store1Name).size());

            // remove:
            Assertions.assertTrue(proxy.removeConstraintFromPaymentPolicies(user1, store1Name, 6));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

            // ******************** AND ********************
            proxy.addToCart(user2, store1Name, product1_store1, 15);
            // create AND constraint: min 5 and max 10 of product1_store1
            Assertions.assertEquals(7, proxy.createAndBagConstraint(user1, store1Name, 1,
                    2, false)); // add constraint without activation
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(7, proxy.getAllBagConstraints(user1, store1Name).size());
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

            // activate:
            Assertions.assertTrue(proxy.addConstraintAsPaymentPolicy(user1, store1Name, 7));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(1, proxy.getAllPaymentPolicies(user1, store1Name).size());

            // purchase fail because user2 has 15 units of product1_store1:
            Assertions.assertThrows(Exception.class, ()->proxy.purchaseCartByCreditCard(user2, "123456789", "7", "2024", user2,
                    "123", "123456789", user2, "address", "city", "country", "100200"));
            proxy.changeProductAmountInCart(user2, store1Name, product1_store1, 7);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            // purchase success:
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(user2, "123456789", "7", "2024", user2,
                    "123", "123456789", user2, "address", "city", "country", "100200"));
            // remove:
            Assertions.assertTrue(proxy.removeConstraintFromPaymentPolicies(user1, store1Name, 7));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

            // ******************** OR ********************
            // create OR constraint: min 5 of product1_store1 or it is not allowed after certain hour
            Assertions.assertEquals(8, proxy.createOrBagConstraint(user1, store1Name, 2,
                    3, true)); // add constraint and activate
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(8, proxy.getAllBagConstraints(user1, store1Name).size());
            Assertions.assertEquals(1, proxy.getAllPaymentPolicies(user1, store1Name).size());

            // remove:
            Assertions.assertTrue(proxy.removeConstraintFromPaymentPolicies(user1, store1Name, 8));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

            // ******************** ONLY IF ********************
            // create ONLY IF constraint: product1_store1 is not allowed after certain hour only if there are min 5 units in cart
            Assertions.assertEquals(9, proxy.createOnlyIfBagConstraint(user1, store1Name, 3,
                    2, true)); // add constraint and activate it
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(9, proxy.getAllBagConstraints(user1, store1Name).size());
            Assertions.assertEquals(1, proxy.getAllPaymentPolicies(user1, store1Name).size());

            // remove:
            Assertions.assertTrue(proxy.removeConstraintFromPaymentPolicies(user1, store1Name, 9));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllPaymentPolicies(user1, store1Name).isEmpty());

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }


    private void logOutMembers()throws Exception{
        proxy.memberLogOut(member1Name);
        proxy.memberLogOut(member2Name);
    }
    private void initSystemServiceAndLoadDataAndLogIn() throws Exception {
        proxy= new ProxyBridge(new RealBridge());
        proxy.loadData();
        user1 = proxy.enterMarket();
        user2 = proxy.enterMarket();
        user1 = proxy.login(user1, member1Name, member1Password);
        user2 = proxy.login(user2, member2Name, member2Password);
    }
}
