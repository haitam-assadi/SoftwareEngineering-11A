package DatabaseTests;

import DTO.ProductDTO;
import DTO.StoreDTO;
import DomainLayer.Store;
import PresentationLayer.SpringbootHtmlApplication;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.ref.PhantomReference;
import java.util.List;
import java.util.Map;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class CartTests {

    private ProxyBridge proxy= new ProxyBridge(new RealBridge());
    private String user1;
    private String user2;
    private String user3;
    private String user4;
    String member1Name = "member1name";
    String member1Password = "Aa12345678";
    String member2Name = "member2name";
    String member2Password = "Bb129271346";

    String member3Name = "member3name";
    String member3Password = "kK129B71346";

    String store1Name = "store1";
    String store1bName = "store1b";

    String product1_store1 = "product1_store1";
    String category1_product1_store1 = "category1_product1_store1";
    String product2_store1 = "product2_store1";
    String category2_product2_store1 = "category2_product2_store1";

    String product1_store1b = "product1_store1b";
    String product2_store1b = "product2_store1b";
    String category_store1b = "category_store1b";

    String store2Name = "store2";
    String product1_store2 = "product1_store2";
    String product2_store2 = "product2_store2";
    String product3_store2 = "product3_store2";
    String category_store2 = "category_store2";

    String store2bName = "store2b";

    String store3Name = "store3";

    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("setup");
        proxy= new ProxyBridge(new RealBridge());
        proxy.loadData();
        user1 = proxy.enterMarket(); // founder
        user2 = proxy.enterMarket(); // founder
        user3 = proxy.enterMarket(); // member
        user4 = proxy.enterMarket(); // guest
        proxy.register(user1, member1Name, member1Password);
        proxy.register(user2, member2Name, member2Password);
        proxy.register(user3, member3Name, member3Password);
        user1 = proxy.login(user1, member1Name, member1Password);
        user2 = proxy.login(user2, member2Name, member2Password);
        user3 = proxy.login(user3, member3Name, member3Password);

        proxy.createStore(member1Name, store1Name);
        proxy.createStore(member1Name, store1bName);
        proxy.createStore(member2Name, store2Name);
        proxy.createStore(member2Name, store2bName); // store for test close store

        proxy.addNewProductToStock(member1Name, store1Name, product1_store1, category1_product1_store1, 111.1, "description_product1_store1", 100);
        proxy.addNewProductToStock(member1Name, store1Name, product2_store1, category2_product2_store1, 222.2, "description", 200);

        proxy.addNewProductToStock(member1Name, store1bName, product1_store1b, category_store1b, 333.3, "description_product1_store1b", 300);
        proxy.addNewProductToStock(member1Name, store1bName, product2_store1b, category_store1b, 444.4, "description", 400);

        proxy.addNewProductToStock(member2Name, store2Name, product1_store2, category_store2, 555.5, "description_product1_store2", 500);
        proxy.addNewProductToStock(member2Name, store2Name, product2_store2, category_store2, 666.6, "description", 600);
        proxy.addNewProductToStock(member2Name, store2Name, product3_store2, category_store2, 777.7, "description", 700);

        proxy.addNewProductToStock(member2Name, store2bName, product3_store2, category_store2, 777.7, "description", 700);

//        // TODO: delete
//        Assertions.assertTrue(proxy.addToCart(user3, store1Name, product2_store1, 40));
//
//        Assertions.assertTrue(proxy.addToCart(user4, store1Name, product2_store1, 40));
//        Assertions.assertTrue(proxy.addToCart(user4, store2Name, product2_store2, 4));
    }

    @Test
    public void cartTest(){
        try {
            proxy.closeStore(member2Name, store2bName);
            proxy.removeProductFromStock(member2Name, store2Name, product3_store2);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            // member: add to cart
                // success
            Assertions.assertTrue(proxy.addToCart(user3, store1Name, product1_store1, 10));
            Assertions.assertTrue(proxy.addToCart(user3, store1Name, product2_store1, 20));
            Assertions.assertTrue(proxy.addToCart(user3, store1bName, product1_store1b, 30));
                // fail
            Assertions.assertThrows(Exception.class, ()->proxy.addToCart(user3, store1Name, product1_store1, 10)); // already in the cart
            Assertions.assertThrows(Exception.class, ()->proxy.addToCart(user3, store2Name, product1_store2, 1000)); // there are 500 units in the stock
            Assertions.assertThrows(Exception.class, ()->proxy.addToCart(user3, store2Name, product1_store2, -1)); // negative amount
            Assertions.assertThrows(Exception.class, ()->proxy.addToCart(user3, store1Name, product1_store2, 10)); // wrong store name
            Assertions.assertThrows(Exception.class, ()->proxy.addToCart(user3, store2Name, product2_store1b, 10)); // wrong product name
            Assertions.assertThrows(Exception.class, ()->proxy.addToCart(user3, store2bName, product3_store2, 10)); // closed store
            Assertions.assertThrows(Exception.class, ()->proxy.addToCart(user3, store2Name, product3_store2, 15)); // product has been removed from stock

            // guest: add to cart
                // success
            Assertions.assertTrue(proxy.addToCart(user4, store1Name, product1_store1, 11));
            Assertions.assertTrue(proxy.addToCart(user4, store1Name, product2_store1, 22));
            Assertions.assertTrue(proxy.addToCart(user4, store1bName, product1_store1b, 33));
            Assertions.assertTrue(proxy.addToCart(user4, store2Name, product2_store2, 4));
                // fail
            Assertions.assertThrows(Exception.class, ()->proxy.addToCart(user4, store1Name, product1_store1, 10)); // already in the cart
            Assertions.assertThrows(Exception.class, ()->proxy.addToCart(user4, store2Name, product1_store2, 1000)); // there are 500 units in the stock
            Assertions.assertThrows(Exception.class, ()->proxy.addToCart(user4, store2Name, product1_store2, -1)); // negative amount
            Assertions.assertThrows(Exception.class, ()->proxy.addToCart(user4, store1Name, product1_store2, 10)); // wrong store name
            Assertions.assertThrows(Exception.class, ()->proxy.addToCart(user4, store2Name, product2_store1b, 10)); // wrong product name
            Assertions.assertThrows(Exception.class, ()->proxy.addToCart(user4, store2bName, product3_store2, 10)); // closed store
            Assertions.assertThrows(Exception.class, ()->proxy.addToCart(user4, store2Name, product3_store2, 15)); // product has been removed from stock


            // get bag
                // member:
            Assertions.assertEquals(2, proxy.getBag(user3, store1Name).size());
            Assertions.assertEquals(1, proxy.getBag(user3, store1bName).size());
            //Assertions.assertThrows(Exception.class, ()->proxy.getBag(user3, store2Name));

                // guest:
            Assertions.assertEquals(2, proxy.getBag(user4, store1Name).size());
            Assertions.assertEquals(1, proxy.getBag(user4, store1bName).size());
            Assertions.assertEquals(1, proxy.getBag(user4, store2Name).size());
            //Assertions.assertThrows(Exception.class, ()->proxy.getBag(user4, store2bName));


            // get cart content
                // member:
            Assertions.assertEquals(0, proxy.getCartContent(user1).size()); // empty cart
            Assertions.assertEquals(3, proxy.getCartContent(user3).size());
            Assertions.assertTrue(proxy.getCartContent(user3).containsKey(store1Name));
            Assertions.assertTrue(proxy.getCartContent(user3).containsKey(store1bName));
            Assertions.assertEquals(2, proxy.getCartContent(user3).get(store1Name).size());
            Assertions.assertEquals(1, proxy.getCartContent(user3).get(store1bName).size());
            Assertions.assertTrue(proxy.getCartContent(user3).get(store1Name).contains(product1_store1));
            Assertions.assertTrue(proxy.getCartContent(user3).get(store1Name).contains(product2_store1));
            Assertions.assertTrue(proxy.getCartContent(user3).get(store1bName).contains(product1_store1b));

                // guest:
            Assertions.assertEquals(3, proxy.getCartContent(user4).size());
            Assertions.assertTrue(proxy.getCartContent(user4).containsKey(store1Name));
            Assertions.assertTrue(proxy.getCartContent(user4).containsKey(store1bName));
            Assertions.assertTrue(proxy.getCartContent(user4).containsKey(store2Name));
            Assertions.assertEquals(2, proxy.getCartContent(user4).get(store1Name).size());
            Assertions.assertEquals(1, proxy.getCartContent(user4).get(store1bName).size());
            Assertions.assertEquals(1, proxy.getCartContent(user4).get(store2Name).size());
            Assertions.assertTrue(proxy.getCartContent(user3).get(store1Name).contains(product1_store1));
            Assertions.assertTrue(proxy.getCartContent(user3).get(store1Name).contains(product2_store1));
            Assertions.assertTrue(proxy.getCartContent(user3).get(store1bName).contains(product1_store1b));
            //Assertions.assertTrue(proxy.getCartContent(user3).get(store2Name).contains(product1_store2));


            // get product amount in cart
                // member:
            Assertions.assertEquals(10, proxy.getProductAmountInCart(user3, store1Name, product1_store1));
            Assertions.assertEquals(20, proxy.getProductAmountInCart(user3, store1Name, product2_store1));
            Assertions.assertEquals(30, proxy.getProductAmountInCart(user3, store1bName, product1_store1b));

                // guest:
            Assertions.assertEquals(11, proxy.getProductAmountInCart(user4, store1Name, product1_store1));
            Assertions.assertEquals(22, proxy.getProductAmountInCart(user4, store1Name, product2_store1));
            Assertions.assertEquals(33, proxy.getProductAmountInCart(user4, store1bName, product1_store1b));
            Assertions.assertEquals(4, proxy.getProductAmountInCart(user4, store2Name, product2_store2));


            // remove product from cart
                // member:
            //Assertions.assertThrows(Exception.class, ()->proxy.removeProductFromCart(user3, store2Name, product2_store2));
            Assertions.assertTrue(proxy.removeProductFromCart(user3, store1Name, product1_store1));
            Assertions.assertTrue(proxy.removeProductFromCart(user3, store1bName, product1_store1b));
            Assertions.assertTrue(proxy.getCartContent(user3).containsKey(store1Name));
            Assertions.assertFalse(proxy.getCartContent(user3).containsKey(store1bName));
            Assertions.assertTrue(proxy.getCartContent(user3).get(store1Name).contains(product2_store1));

                // guest:
            Assertions.assertTrue(proxy.removeProductFromCart(user4, store1Name, product1_store1));
            Assertions.assertTrue(proxy.removeProductFromCart(user4, store1bName, product1_store1b));
            Assertions.assertTrue(proxy.getCartContent(user4).containsKey(store1Name));
            Assertions.assertFalse(proxy.getCartContent(user4).containsKey(store1bName));
            Assertions.assertTrue(proxy.getCartContent(user4).containsKey(store2Name));
            Assertions.assertTrue(proxy.getCartContent(user4).get(store1Name).contains(product2_store1));
            Assertions.assertTrue(proxy.getCartContent(user4).get(store2Name).contains(product2_store2));


            // change product amount in cart
                // member:
            Assertions.assertTrue(proxy.changeProductAmountInCart(user3, store1Name, product2_store1, 40));
            Assertions.assertEquals(40, proxy.getProductAmountInCart(user3, store1Name, product2_store1));
            Assertions.assertThrows(Exception.class, ()->proxy.changeProductAmountInCart(user3, store1Name, product2_store1, 1000)); // there are 200 units in stock
            Assertions.assertThrows(Exception.class, ()->proxy.changeProductAmountInCart(user3, store1Name, product2_store1, -1000)); // negative amount
            Assertions.assertThrows(Exception.class, ()->proxy.changeProductAmountInCart(user3, store1Name, product1_store1, 5)); // product has been removed from cart

                // guest:
            Assertions.assertTrue(proxy.changeProductAmountInCart(user4, store1Name, product2_store1, 40));
            Assertions.assertEquals(40, proxy.getProductAmountInCart(user4, store1Name, product2_store1));
            Assertions.assertThrows(Exception.class, ()->proxy.changeProductAmountInCart(user4, store1Name, product2_store1, 1000)); // there are 200 units in stock
            Assertions.assertThrows(Exception.class, ()->proxy.changeProductAmountInCart(user4, store1Name, product2_store1, -1000)); // negative amount
            Assertions.assertThrows(Exception.class, ()->proxy.changeProductAmountInCart(user4, store1Name, product1_store1, 5)); // product has been removed from cart


            // get cart price before discount
            Assertions.assertEquals(0, proxy.getCartPriceBeforeDiscount(user1)); // empty cart
            Assertions.assertEquals(8888, proxy.getCartPriceBeforeDiscount(user3));
            Assertions.assertEquals(11554.4, proxy.getCartPriceBeforeDiscount(user4));
            Assertions.assertThrows(Exception.class, ()->proxy.getCartPriceBeforeDiscount("new user")); // user does not exist

        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    private void logOutMembers()throws Exception{
        proxy.memberLogOut(member1Name);
        proxy.memberLogOut(member2Name);
        proxy.memberLogOut(member3Name);
    }
    private void initSystemServiceAndLoadDataAndLogIn() throws Exception {
        proxy= new ProxyBridge(new RealBridge());
        proxy.loadData();
        user1 = proxy.enterMarket();
        user2 = proxy.enterMarket();
        user3 = proxy.enterMarket();
//        user4 = proxy.enterMarket();
        user1 = proxy.login(user1, member1Name, member1Password);
        user2 = proxy.login(user2, member2Name, member2Password);
        user3 = proxy.login(user3, member3Name, member3Password);
    }

}
