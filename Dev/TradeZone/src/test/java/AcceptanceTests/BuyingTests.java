package AcceptanceTests;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class BuyingTests {

    private ProxyBridge proxy;
    @Mock
    private Bridge bridge;

    private String user;
    private String store_founder;
    private String guest_name;
    private String member_name;
    private String storeName1;
    private String storeName2;

    @BeforeAll
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        proxy = new ProxyBridge();
        if(!proxy.initializeMarket()){
            throw new Exception("");
        }
        user = proxy.enterMarket();//guest default user name
        if(!proxy.register(user,"Moslem Asaad","12345")){
            throw new Exception("");
        }
        store_founder = proxy.login(user,"Moslem Asaad","12345");

        storeName1 = proxy.createStore(store_founder, "Moslem Store");
        proxy.addCategory(store_founder, "Iphones", storeName1);
        proxy.addNewProductToStock(store_founder,storeName1,"iphone 14","Iphones",3000,"256 Gb",50);
        proxy.addNewProductToStock(store_founder,storeName1,"iphone 13","Iphones",2000,"256 Gb",35);
        proxy.addCategory(store_founder, "Gaming chairs", storeName1);
        proxy.addNewProductToStock(store_founder,storeName1,"gaming chair 1","Gaming chairs",600,"red",50);

        storeName2 = proxy.createStore(store_founder, "Baraa Store");
        proxy.addCategory(store_founder, "Iphones", storeName2);
        proxy.addNewProductToStock(store_founder,storeName2,"iphone 14","Iphones",3500,"256 Gb",100);
        proxy.addCategory(store_founder, "Gaming mouses", storeName2);
        proxy.addNewProductToStock(store_founder,storeName2,"gaming mouse 1","Gaming mouses",200,"black, RGB",70);


        guest_name = proxy.enterMarket();//guest default user name

        user = proxy.enterMarket();//guest default user name
        if(!proxy.register(user,"Harry","1111")){
            throw new Exception("");
        }
        member_name = proxy.login(user,"Harry","1111");

    }

    // is product name unique??
    @Test
    public void search_product_by_name_success(){
        try{
            // guest search
            Map<String, String> searched = proxy.getProductInfoFromMarketByName(guest_name, "iphone 14");
            Assertions.assertTrue(searched.containsKey("Moslem Store"));
            Assertions.assertTrue(searched.containsKey("Baraa Store"));
            Assertions.assertEquals(2, searched.size());

            // member search
            searched = proxy.getProductInfoFromMarketByName(member_name, "iphone 14");
            Assertions.assertTrue(searched.containsKey("Moslem Store"));
            Assertions.assertTrue(searched.containsKey("Baraa Store"));
            Assertions.assertEquals(2, searched.size());
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


    @Test
    public void search_product_by_name_does_not_exist(){
        try{
            // guest search
            Map<String, String> searched = proxy.getProductInfoFromMarketByName(guest_name, "samsung");
            Assertions.assertEquals(0, searched.size());

            // member search
            searched = proxy.getProductInfoFromMarketByName(member_name, "samsung");
            Assertions.assertEquals(0, searched.size());
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void search_product_by_category_success(){
        try{
            // guest search
            Map<String, List<String>> searched = proxy.getProductInfoFromMarketByCategory(guest_name, "Iphones");
            Assertions.assertTrue(searched.containsKey("Moslem Store"));
            Assertions.assertTrue(searched.containsKey("Baraa Store"));
            Assertions.assertEquals(2, searched.size());
            Assertions.assertEquals(2, searched.get("Moslem Store").size());
            Assertions.assertEquals(1, searched.get("Baraa Store").size());

            // member search
            searched = proxy.getProductInfoFromMarketByCategory(member_name, "Iphones");
            Assertions.assertTrue(searched.containsKey("Moslem Store"));
            Assertions.assertTrue(searched.containsKey("Baraa Store"));
            Assertions.assertEquals(2, searched.size());
            Assertions.assertEquals(2, searched.get("Moslem Store").size());
            Assertions.assertEquals(1, searched.get("Baraa Store").size());
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void search_product_by_category_does_not_exist(){
        try{
            // guest search
            Map<String, List<String>> searched = proxy.getProductInfoFromMarketByCategory(guest_name, "Galaxy");
            Assertions.assertEquals(0, searched.size());

            // member search
            searched = proxy.getProductInfoFromMarketByCategory(member_name, "Galaxy");
            Assertions.assertEquals(0, searched.size());
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void search_product_by_keyword_success(){
        try{
            // guest search
            Map<String, List<String>> searched = proxy.getProductInfoFromMarketByKeyword(guest_name, "gam");
            Assertions.assertTrue(searched.containsKey("Moslem Store"));
            Assertions.assertTrue(searched.containsKey("Baraa Store"));
            Assertions.assertTrue(searched.get("Moslem Store").contains("gaming chair 1"));
            Assertions.assertTrue(searched.get("Baraa Store").contains("gaming mouse 1"));
            Assertions.assertEquals(2, searched.size());
            Assertions.assertEquals(1, searched.get("Moslem Store").size());
            Assertions.assertEquals(1, searched.get("Baraa Store").size());

            // member search
            searched = proxy.getProductInfoFromMarketByKeyword(member_name, "gam");
            Assertions.assertTrue(searched.containsKey("Moslem Store"));
            Assertions.assertTrue(searched.containsKey("Baraa Store"));
            Assertions.assertTrue(searched.get("Moslem Store").contains("gaming chair 1"));
            Assertions.assertTrue(searched.get("Baraa Store").contains("gaming mouse 1"));
            Assertions.assertEquals(2, searched.size());
            Assertions.assertEquals(1, searched.get("Moslem Store").size());
            Assertions.assertEquals(1, searched.get("Baraa Store").size());
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void search_product_by_keyword_does_not_exist(){
        try{
            // guest search
            Map<String, List<String>> searched = proxy.getProductInfoFromMarketByKeyword(guest_name, "aaa");
            Assertions.assertEquals(0, searched.size());

            // member search
            searched = proxy.getProductInfoFromMarketByCategory(member_name, "aaa");
            Assertions.assertEquals(0, searched.size());
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void filter_products_by_price(){ // TODO: if the product name the unique send the map to the filter function
        try{
            // guest search
            Map<String, String> searched = proxy.getProductInfoFromMarketByName(guest_name, "iphone 14");
            Assertions.assertTrue(searched.containsKey("Moslem Store"));
            Assertions.assertTrue(searched.containsKey("Baraa Store"));
            Assertions.assertEquals(2, searched.size());

            // member search
            searched = proxy.getProductInfoFromMarketByName(member_name, "iphone 14");
            Assertions.assertTrue(searched.containsKey("Moslem Store"));
            Assertions.assertTrue(searched.containsKey("Baraa Store"));
            Assertions.assertEquals(2, searched.size());
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_product_to_cart_success(){
        // TODO: add assert equal(3, getBag.getProductAmount)
        // TODO: check all the fields of product
        try{
            // guest
            int productAmount = proxy.getProductAmount(storeName2, "gaming mouse 1");
            Assertions.assertTrue(proxy.addToCart(guest_name, storeName2, "gaming mouse 1", 3));
            Assertions.assertTrue(proxy.getBag(guest_name, storeName2).contains("gaming mouse 1"));
            Assertions.assertEquals(productAmount, proxy.getProductAmount(storeName2, "gaming mouse 1")); // product amount in stock is not changed

            // member
            productAmount = proxy.getProductAmount(storeName2, "gaming mouse 1");
            Assertions.assertTrue(proxy.addToCart(member_name, storeName2, "gaming mouse 1", 3));
            Assertions.assertTrue(proxy.getBag(member_name, storeName2).contains("gaming mouse 1"));
            Assertions.assertEquals(productAmount, proxy.getProductAmount(storeName2, "gaming mouse 1")); // product amount in stock is not changed
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_larger_amount_of_product_to_cart_fail(){
        try{
            // guest
            int productAmount = proxy.getProductAmount(storeName2, "gaming mouse 1");
            Assertions.assertFalse(proxy.addToCart(guest_name, storeName2, "gaming mouse 1", productAmount + 10));
            Assertions.assertFalse(proxy.getBag(guest_name, storeName2).contains("gaming mouse 1"));
            Assertions.assertEquals(productAmount, proxy.getProductAmount(storeName2, "gaming mouse 1")); // product amount in stock is not changed

            // member
            productAmount = proxy.getProductAmount(storeName2, "gaming mouse 1");
            Assertions.assertFalse(proxy.addToCart(member_name, storeName2, "gaming mouse 1", productAmount + 10));
            Assertions.assertFalse(proxy.getBag(guest_name, storeName2).contains("gaming mouse 1"));
            Assertions.assertEquals(productAmount, proxy.getProductAmount(storeName2, "gaming mouse 1")); // product amount in stock is not changed
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_product_does_not_exist_to_cart_fail(){
        try{
            // guest
            Assertions.assertFalse(proxy.addToCart(guest_name, storeName2, "mouse", 10));
            Assertions.assertFalse(proxy.getBag(guest_name, storeName2).contains("mouse"));

            // member
            Assertions.assertFalse(proxy.addToCart(member_name, storeName2, "mouse", 10));
            Assertions.assertFalse(proxy.getBag(guest_name, storeName2).contains("mouse"));
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_product_to_cart_after_deletion_fail(){
        try{
            proxy.removeProductFromStock(store_founder, storeName2, "iphone 14");
            // guest
            Assertions.assertFalse(proxy.addToCart(guest_name, storeName2, "iphone 14", 10));
            Assertions.assertFalse(proxy.getBag(guest_name, storeName2).contains("iphone 14"));

            // member
            Assertions.assertFalse(proxy.addToCart(member_name, storeName2, "iphone 14", 10));
            Assertions.assertFalse(proxy.getBag(member_name, storeName2).contains("iphone 14"));
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_product_from_store_does_not_exist_fail(){
        try{
            // guest
            Assertions.assertFalse(proxy.addToCart(guest_name, "bad store name", "gaming mouse 1", 10));
            Assertions.assertFalse(proxy.getBag(guest_name, "bad store name").contains("gaming mouse 1"));

            // member
            Assertions.assertFalse(proxy.addToCart(member_name, "bad store name", "gaming mouse 1", 10));
            Assertions.assertFalse(proxy.getBag(member_name, "bad store name").contains("gaming mouse 1"));
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_product_twice_to_cart_fail(){
        try{
            // guest
            Assertions.assertTrue(proxy.addToCart(guest_name, storeName2, "gaming mouse 1", 5));
            Assertions.assertFalse(proxy.addToCart(guest_name, storeName2, "gaming mouse 1", 2));
            //  TODO: add assert equal(?, getBag.getProductAmountInBag)

            // member
            Assertions.assertTrue(proxy.addToCart(member_name, storeName2, "gaming mouse 1", 5));
            Assertions.assertFalse(proxy.addToCart(member_name, storeName2, "gaming mouse 1", 2));
            //  TODO: add assert equal(?, getBag.getProductAmountInBag)
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //TODO: add test that change product price and check if changed in the cart
    @Test
    public void guest_get_cart_content_success(){
        try{
            proxy.addToCart(guest_name, storeName1, "iphone 14", 1);
            proxy.addToCart(guest_name, storeName1, "gaming chair 1", 3);
            proxy.addToCart(guest_name, storeName2, "iphone 14", 1);
            Map<String, List<String>> cart = proxy.getCartContent(guest_name);
            Assertions.assertEquals(2, cart.size()); // 2 bags
            Assertions.assertTrue(cart.containsKey(storeName1));
            Assertions.assertTrue(cart.containsKey(storeName2));
            Assertions.assertEquals(2, cart.get(storeName1).size());
            Assertions.assertEquals(1, cart.get(storeName2).size());
            Assertions.assertTrue(cart.get(storeName1).contains("iphone 14"));
            Assertions.assertTrue(cart.get(storeName1).contains("gaming chair 1"));
            Assertions.assertTrue(cart.get(storeName2).contains("iphone 14"));
            // TODO: add assert equal(?, getBag.getProductAmountInBag)

        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void member_get_cart_content_success(){
        try{
            proxy.addToCart(member_name, storeName1, "iphone 14", 1);
            proxy.addToCart(member_name, storeName1, "gaming chair 1", 3);
            proxy.addToCart(member_name, storeName2, "iphone 14", 1);
            Map<String, List<String>> cart = proxy.getCartContent(member_name);
            Assertions.assertEquals(2, cart.size()); // 2 bags
            Assertions.assertTrue(cart.containsKey(storeName1));
            Assertions.assertTrue(cart.containsKey(storeName2));
            Assertions.assertEquals(2, cart.get(storeName1).size());
            Assertions.assertEquals(1, cart.get(storeName2).size());
            Assertions.assertTrue(cart.get(storeName1).contains("iphone 14"));
            Assertions.assertTrue(cart.get(storeName1).contains("gaming chair 1"));
            Assertions.assertTrue(cart.get(storeName2).contains("iphone 14"));
            // TODO: add assert equal(?, getBag.getProductAmountInBag)

        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void get_empty_cart_content_success(){
        try{
            // guest
            Assertions.assertEquals(0, proxy.getCartContent(guest_name).size()); // empty

            // member
            Assertions.assertEquals(0, proxy.getCartContent(guest_name).size()); // empty
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void guest_remove_product_from_cart_success(){
        try {
            proxy.addToCart(guest_name, storeName1, "iphone 14", 1);
            proxy.addToCart(guest_name, storeName1, "gaming chair 1", 3);
            proxy.addToCart(guest_name, storeName2, "iphone 14", 1);
            Map<String, List<String>> cart = proxy.getCartContent(guest_name);
            Assertions.assertEquals(2, cart.size()); // 2 bags
            Assertions.assertTrue(proxy.removeProductFromCart(guest_name, storeName2, "iphone 14"));
            cart = proxy.getCartContent(guest_name);
            Assertions.assertEquals(1, cart.size()); // 1 bag
            Assertions.assertTrue(cart.containsKey(storeName1));
            Assertions.assertFalse(cart.containsKey(storeName2));
            Assertions.assertEquals(2, cart.get(storeName1).size());
            Assertions.assertTrue(cart.get(storeName1).contains("iphone 14"));
            Assertions.assertTrue(cart.get(storeName1).contains("gaming chair 1"));
            // TODO: add assert equal(?, getBag.getProductAmountInBag)
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void member_remove_product_from_cart_success(){
        try {
            proxy.addToCart(member_name, storeName1, "iphone 14", 1);
            proxy.addToCart(member_name, storeName1, "gaming chair 1", 3);
            proxy.addToCart(member_name, storeName2, "iphone 14", 1);
            Map<String, List<String>> cart = proxy.getCartContent(member_name);
            Assertions.assertEquals(2, cart.size()); // 2 bags
            Assertions.assertTrue(proxy.removeProductFromCart(member_name, storeName2, "iphone 14"));
            cart = proxy.getCartContent(member_name);
            Assertions.assertEquals(1, cart.size()); // 1 bag
            Assertions.assertTrue(cart.containsKey(storeName1));
            Assertions.assertFalse(cart.containsKey(storeName2));
            Assertions.assertEquals(2, cart.get(storeName1).size());
            Assertions.assertTrue(cart.get(storeName1).contains("iphone 14"));
            Assertions.assertTrue(cart.get(storeName1).contains("gaming chair 1"));
            // TODO: add assert equal(?, getBag.getProductAmountInBag)
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_product_does_not_exist_in_cart(){
        try{
            // guest
            proxy.addToCart(guest_name, storeName1, "iphone 14", 1);
            Assertions.assertFalse(proxy.removeProductFromCart(guest_name, storeName1, "gaming chair 1"));
            Map<String, List<String>> cart = proxy.getCartContent(guest_name);
            Assertions.assertEquals(1, cart.size()); // not changed
            Assertions.assertTrue(cart.containsKey(storeName1)); // not changed
            Assertions.assertTrue(cart.get(storeName1).contains("iphone 14")); // not changed

            // member
            proxy.addToCart(member_name, storeName1, "iphone 14", 1);
            Assertions.assertFalse(proxy.removeProductFromCart(member_name, storeName1, "gaming chair 1"));
            cart = proxy.getCartContent(member_name);
            Assertions.assertEquals(1, cart.size()); // not changed
            Assertions.assertTrue(cart.containsKey(storeName1)); // not changed
            Assertions.assertTrue(cart.get(storeName1).contains("iphone 14")); // not changed
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_product_from_cart_bad_store_name(){
        try{
            // guest
            proxy.addToCart(guest_name, storeName1, "iphone 14", 1);
            Assertions.assertFalse(proxy.removeProductFromCart(guest_name, "bad store name", "iphone 14"));
            Map<String, List<String>> cart = proxy.getCartContent(guest_name);
            Assertions.assertEquals(1, cart.size()); // not changed
            Assertions.assertTrue(cart.containsKey(storeName1)); // not changed
            Assertions.assertTrue(cart.get(storeName1).contains("iphone 14")); // not changed

            // member
            proxy.addToCart(member_name, storeName1, "iphone 14", 1);
            Assertions.assertFalse(proxy.removeProductFromCart(member_name, "bad store name", "iphone 14"));
            cart = proxy.getCartContent(member_name);
            Assertions.assertEquals(1, cart.size()); // not changed
            Assertions.assertTrue(cart.containsKey(storeName1)); // not changed
            Assertions.assertTrue(cart.get(storeName1).contains("iphone 14")); // not changed
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void change_product_amount_in_cart_success(){
        try
        {
            // guest
            proxy.addToCart(guest_name, storeName1, "iphone 14", 1);
            Assertions.assertEquals(1, proxy.getProductAmountInCart(guest_name, storeName1, "iphone 14"));
            Assertions.assertTrue(proxy.changeProductAmountInCart(guest_name, storeName1, "iphone 14", 5));
            Assertions.assertEquals(5, proxy.getProductAmountInCart(guest_name, storeName1, "iphone 14"));
            // TODO: check bag/cart total price

            // member
            proxy.addToCart(member_name, storeName1, "iphone 14", 1);
            Assertions.assertEquals(1, proxy.getProductAmountInCart(member_name, storeName1, "iphone 14"));
            Assertions.assertTrue(proxy.changeProductAmountInCart(member_name, storeName1, "iphone 14", 5));
            Assertions.assertEquals(5, proxy.getProductAmountInCart(member_name, storeName1, "iphone 14"));
            // TODO: check bag/cart total price

        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    public void change_product_amount_in_cart_bad_amount(){
        // guest
        proxy.addToCart(guest_name, storeName1, "iphone 14", 1);
        Assertions.assertEquals(1, proxy.getProductAmountInCart(guest_name, storeName1, "iphone 14"));
        // larger
        Assertions.assertFalse(proxy.changeProductAmountInCart(guest_name, storeName1, "iphone 14", 1000)); // we have 50 iphone 14 in storeName1
        Assertions.assertEquals(1, proxy.getProductAmountInCart(guest_name, storeName1, "iphone 14")); // not changed
        // negative
        Assertions.assertFalse(proxy.changeProductAmountInCart(guest_name, storeName1, "iphone 14", -10));
        Assertions.assertEquals(1, proxy.getProductAmountInCart(guest_name, storeName1, "iphone 14")); // not changed
        // TODO: check bag/cart total price

        // member
        proxy.addToCart(member_name, storeName1, "iphone 14", 1);
        Assertions.assertEquals(1, proxy.getProductAmountInCart(member_name, storeName1, "iphone 14"));
        // larger
        Assertions.assertFalse(proxy.changeProductAmountInCart(member_name, storeName1, "iphone 14", 1000)); // we have 50 iphone 14 in storeName1
        Assertions.assertEquals(1, proxy.getProductAmountInCart(member_name, storeName1, "iphone 14"));
        // negative
        Assertions.assertFalse(proxy.changeProductAmountInCart(member_name, storeName1, "iphone 14", -10));
        Assertions.assertEquals(1, proxy.getProductAmountInCart(member_name, storeName1, "iphone 14"));

        // TODO: check bag/cart total price
    }

    // change amount: bad store name, bad product name
    // check getProductAmount in previous functions
    // add function (in bridge) getProductPrice ?

}
