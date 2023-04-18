package AcceptanceTests;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class BuyingTests {
    private ProxyBridge proxy;
    private String user;
    String moslemUserName = "moslem123";
    String moslemPassword = "Aa123456";
    private String store_founder;
    private String guest_name;
    private String member_name;
    private String storeName1;
    private String storeName2;
    private String owner;
    private String manager;

    @BeforeEach
    public void setUp() throws Exception {
        proxy = new ProxyBridge(new RealBridge());
        if(!proxy.initializeMarket()){
            throw new Exception("");
        }

        // store founder
        user = proxy.enterMarket();//guest default user name
        proxy.register(user,moslemUserName,moslemPassword);
        store_founder = proxy.login(user,moslemUserName,moslemPassword);

        storeName1 = proxy.createStore(store_founder, "Moslem Store");
        proxy.addCategory(store_founder, "Iphones", storeName1);
        proxy.addNewProductToStock(store_founder,storeName1,"iphone 14","Iphones",3000.0,"256 Gb",50);
        proxy.addNewProductToStock(store_founder,storeName1,"iphone 13","Iphones",2000.0,"256 Gb",35);
        proxy.addCategory(store_founder, "Gaming chairs", storeName1);
        proxy.addNewProductToStock(store_founder,storeName1,"gaming chair 1","Gaming chairs",600.0,"red",50);

        storeName2 = proxy.createStore(store_founder, "Baraa Store");
        proxy.addCategory(store_founder, "Iphones", storeName2);
        proxy.addNewProductToStock(store_founder,storeName2,"iphone 14","Iphones",3500.0,"256 Gb",100);
        proxy.addCategory(store_founder, "Gaming mouses", storeName2);
        proxy.addNewProductToStock(store_founder,storeName2,"gaming mouse 1","Gaming mouses",200.0,"black, RGB",70);

        // guest
        guest_name = proxy.enterMarket();//guest default user name

        // member
        user = proxy.enterMarket();//guest default user name
        proxy.register(user,"Harry123","Hh123456");
        member_name = proxy.login(user,"Harry123","Hh123456");

        // owner
        user = proxy.enterMarket();//guest default user name
        proxy.register(user,"Joe123","Jj456789");
        owner = proxy.login(user,"Joe123","Jj456789");

        // manager
        user = proxy.enterMarket();//guest default user name
        proxy.register(user,"Tom123","Tt123456");
        manager = proxy.login(user,"Tom123","Tt123456");

    }


    // II.2.1 ********** DONE **********
    @Test
    public void get_store_info_success(){
        try{
            proxy.appointOtherMemberAsStoreOwner(store_founder, storeName1, owner);
            proxy.appointOtherMemberAsStoreManager(store_founder, storeName1, manager);
            List<String> owners = new ArrayList<>();
            owners.add(owner);
            List<String> managers = new ArrayList<>();
            managers.add(manager);

            // guest
            Assertions.assertEquals(store_founder, proxy.getStoreFounderName(guest_name, storeName1));
            Assertions.assertEquals(owners, proxy.getStoreOwnersNames(guest_name, storeName1));
            Assertions.assertEquals(managers, proxy.getStoreManagersNames(guest_name, storeName1));
            List<String> products = proxy.getStoreProducts(guest_name, storeName1);
            Assertions.assertEquals(3, products.size());
            Assertions.assertTrue(products.contains("iphone 14"));
            Assertions.assertTrue(products.contains("iphone 13"));
            Assertions.assertTrue(products.contains("gaming chair 1"));

            //member
            Assertions.assertEquals(store_founder, proxy.getStoreFounderName(member_name, storeName1));
            Assertions.assertEquals(owners, proxy.getStoreOwnersNames(member_name, storeName1));
            Assertions.assertEquals(managers, proxy.getStoreManagersNames(member_name, storeName1));
            products = proxy.getStoreProducts(member_name, storeName1);
            Assertions.assertEquals(3, products.size());
            Assertions.assertTrue(products.contains("iphone 14"));
            Assertions.assertTrue(products.contains("iphone 13"));
            Assertions.assertTrue(products.contains("gaming chair 1"));

        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void get_store_info_bad_store_name_fail(){ // TODO: getProducts
        try{
            // guest
            Assertions.assertThrows(Exception.class, () -> proxy.getStoreFounderName(guest_name, "bad store name"));
            Assertions.assertThrows(Exception.class, () -> proxy.getStoreOwnersNames(guest_name, "bad store name"));
            Assertions.assertThrows(Exception.class, () -> proxy.getStoreManagersNames(guest_name, "bad store name"));

            //member
            Assertions.assertThrows(Exception.class, () -> proxy.getStoreFounderName(member_name, "bad store name"));
            Assertions.assertThrows(Exception.class, () -> proxy.getStoreOwnersNames(member_name, "bad store name"));
            Assertions.assertThrows(Exception.class, () -> proxy.getStoreManagersNames(member_name, "bad store name"));
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    public void get_product_info_success(){
        try{
            // guest
            Assertions.assertEquals(3000, proxy.getProductPrice(guest_name, storeName1, "iphone 14"));
            Assertions.assertEquals("256 Gb", proxy.getProductDescription(guest_name, storeName1, "iphone 14"));

            // member
            Assertions.assertEquals(3000, proxy.getProductPrice(member_name, storeName1, "iphone 14"));
            Assertions.assertEquals("256 Gb", proxy.getProductDescription(member_name, storeName1, "iphone 14"));
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void get_product_info_bad_store_name_fail(){
        // TODO: what is the return value in failure??
        try{
            // guest
            Assertions.assertThrows(Exception.class, () -> proxy.getProductPrice(guest_name, "bad store name", "iphone 14"));
            Assertions.assertThrows(Exception.class, () -> proxy.getProductDescription(guest_name, "bad store name", "iphone 14"));

            // member
            Assertions.assertThrows(Exception.class, () -> proxy.getProductPrice(member_name, "bad store name", "iphone 14"));
            Assertions.assertThrows(Exception.class, () -> proxy.getProductDescription(member_name, "bad store name", "iphone 14"));
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void get_product_info_bad_product_name_fail(){
        // TODO: what is the return value in failure??
        try{
            // guest
            Assertions.assertThrows(Exception.class, () -> proxy.getProductPrice(guest_name, storeName1, "aaa"));
            Assertions.assertThrows(Exception.class, () -> proxy.getProductDescription(guest_name, storeName1, "aaa"));

            // member
            Assertions.assertThrows(Exception.class, () -> proxy.getProductPrice(member_name, storeName1, "aaa"));
            Assertions.assertThrows(Exception.class, () -> proxy.getProductDescription(member_name, storeName1, "aaa"));
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    // II.2.2 ********** DONE **********
    @Test
    public void search_product_by_name_success(){
        try{
            // guest search
            Map<String, List<String>> searched = proxy.getProductInfoFromMarketByName(guest_name, "iphone 14");
            Assertions.assertTrue(searched.containsKey(storeName1));
            Assertions.assertTrue(searched.containsKey(storeName2));
            Assertions.assertEquals(2, searched.size());

            // member search
            searched = proxy.getProductInfoFromMarketByName(member_name, "iphone 14");
            Assertions.assertTrue(searched.containsKey(storeName1));
            Assertions.assertTrue(searched.containsKey(storeName2));
            Assertions.assertEquals(2, searched.size());
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void search_product_by_name_does_not_exist(){
        try{
            // guest search
            Map<String, List<String>> searched = proxy.getProductInfoFromMarketByName(guest_name, "samsung");
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
            Assertions.assertTrue(searched.containsKey(storeName1));
            Assertions.assertTrue(searched.containsKey(storeName2));
            Assertions.assertEquals(2, searched.size());
            Assertions.assertEquals(2, searched.get(storeName1).size());
            Assertions.assertEquals(1, searched.get(storeName2).size());

            // member search
            searched = proxy.getProductInfoFromMarketByCategory(member_name, "Iphones");
            Assertions.assertTrue(searched.containsKey(storeName1));
            Assertions.assertTrue(searched.containsKey(storeName2));
            Assertions.assertEquals(2, searched.size());
            Assertions.assertEquals(2, searched.get(storeName1).size());
            Assertions.assertEquals(1, searched.get(storeName2).size());
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

//    @Test
//    public void search_product_by_keyword_success(){
//        try{
//            // guest search
//            Map<String, List<String>> searched = proxy.getProductInfoFromMarketByKeyword(guest_name, "gam");
//            Assertions.assertTrue(searched.containsKey(storeName1));
//            Assertions.assertTrue(searched.containsKey(storeName2));
//            Assertions.assertTrue(searched.get(storeName1).contains("gaming chair 1"));
//            Assertions.assertTrue(searched.get(storeName2).contains("gaming mouse 1"));
//            Assertions.assertEquals(2, searched.size());
//            Assertions.assertEquals(1, searched.get(storeName1).size());
//            Assertions.assertEquals(1, searched.get(storeName2).size());
//
//            // member search
//            searched = proxy.getProductInfoFromMarketByKeyword(member_name, "gam");
//            Assertions.assertTrue(searched.containsKey(storeName1));
//            Assertions.assertTrue(searched.containsKey(storeName2));
//            Assertions.assertTrue(searched.get(storeName1).contains("gaming chair 1"));
//            Assertions.assertTrue(searched.get(storeName2).contains("gaming mouse 1"));
//            Assertions.assertEquals(2, searched.size());
//            Assertions.assertEquals(1, searched.get(storeName1).size());
//            Assertions.assertEquals(1, searched.get(storeName2).size());
//        } catch (Exception e){
//            Assertions.fail(e.getMessage());
//        }
//    }

//    @Test
//    public void search_product_by_keyword_does_not_exist(){
//        try{
//            // guest search
//            Map<String, List<String>> searched = proxy.getProductInfoFromMarketByKeyword(guest_name, "aaa");
//            Assertions.assertEquals(0, searched.size());
//
//            // member search
//            searched = proxy.getProductInfoFromMarketByCategory(member_name, "aaa");
//            Assertions.assertEquals(0, searched.size());
//        } catch (Exception e){
//            Assertions.fail(e.getMessage());
//        }
//    }

//    @Test
//    public void filter_products_by_price_success(){
//        try{
//            // guest
//            Map<String, List<String>> searched = proxy.getProductInfoFromMarketByName(guest_name, "iphone 14");
//            Map<String, List<String>> filtered = proxy.filterByPrice(guest_name, searched, 0, 3000);
//            Assertions.assertEquals(1, filtered.size());
//            Assertions.assertTrue(filtered.containsKey(storeName1));
//            Assertions.assertTrue(filtered.get(storeName1).contains("iphone 14"));
//
//            //member
//            searched = proxy.getProductInfoFromMarketByName(member_name, "iphone 14");
//            filtered = proxy.filterByPrice(member_name, searched, 0, 3000);
//            Assertions.assertEquals(1, filtered.size());
//            Assertions.assertTrue(filtered.containsKey(storeName1));
//            Assertions.assertTrue(filtered.get(storeName1).contains("iphone 14"));
//        } catch (Exception e){
//            Assertions.fail(e.getMessage());
//        }
//    }

//    @Test
//    public void filter_products_bad_price(){
//        try{
//            // guest
//            Map<String, List<String>> searched = proxy.getProductInfoFromMarketByName(guest_name, "iphone 14");
//            Map<String, List<String>> filtered = proxy.filterByPrice(guest_name, searched, -10, 3000);
//            Assertions.assertEquals(0, filtered.size()); // or error msg ???
//            filtered = proxy.filterByPrice(guest_name, searched, 3000, 0);
//            Assertions.assertEquals(0, filtered.size()); // or error msg ???
//
//            //member
//            searched = proxy.getProductInfoFromMarketByName(member_name, "iphone 14");
//            filtered = proxy.filterByPrice(member_name, searched, -10, 3000);
//            Assertions.assertEquals(0, filtered.size()); // or error msg ???
//            filtered = proxy.filterByPrice(member_name, searched, 3000, 0);
//            Assertions.assertEquals(0, filtered.size()); // or error msg ???
//
//        } catch (Exception e){
//            Assertions.fail(e.getMessage());
//        }
//    }

//    @Test
//    public void filter_products_by_category_success(){
//        try{
//            // guest
//            Map<String, List<String>> searched = proxy.getProductInfoFromMarketByKeyword(guest_name, "gaming");
//            Map<String, List<String>> filtered = proxy.filterByCategory(guest_name, searched, "Gaming chairs");
//            Assertions.assertEquals(1, filtered.size());
//            Assertions.assertTrue(filtered.containsKey(storeName1));
//            Assertions.assertTrue(filtered.get(storeName1).contains("gaming chair 1"));
//
//            //member
//            searched = proxy.getProductInfoFromMarketByKeyword(member_name, "gaming");
//            filtered = proxy.filterByCategory(member_name, searched, "Gaming chairs");
//            Assertions.assertEquals(1, filtered.size());
//            Assertions.assertTrue(filtered.containsKey(storeName1));
//            Assertions.assertTrue(filtered.get(storeName1).contains("gaming chair 1"));
//        } catch (Exception e){
//            Assertions.fail(e.getMessage());
//        }
//    }

//    @Test
//    public void filter_products_bad_category_name(){
//        try{
//            // guest
//            Map<String, List<String>> searched = proxy.getProductInfoFromMarketByKeyword(guest_name, "gaming");
//            Map<String, List<String>> filtered = proxy.filterByCategory(guest_name, searched, "Gaming");
//            Assertions.assertEquals(0, filtered.size()); // or error msg ???
//
//            //member
//            searched = proxy.getProductInfoFromMarketByKeyword(member_name, "gaming");
//            filtered = proxy.filterByCategory(member_name, searched, "Gaming");
//            Assertions.assertEquals(0, filtered.size()); // or error msg ???
//        } catch (Exception e){
//            Assertions.fail(e.getMessage());
//        }
//    }

    // II.2.3 ********** DONE **********
    @Test
    public void add_product_to_cart_success(){
        // TODO: check all the fields of product
        try{
            // guest
            int productAmount = proxy.getProductAmount(storeName2, "gaming mouse 1");
            Assertions.assertTrue(proxy.addToCart(guest_name, storeName2, "gaming mouse 1", 3));
            Assertions.assertTrue(proxy.getBag(guest_name, storeName2).contains("gaming mouse 1"));
            Assertions.assertEquals(3, proxy.getProductAmountInCart(guest_name, storeName2, "gaming mouse 1"));
            Assertions.assertEquals(productAmount, proxy.getProductAmount(storeName2, "gaming mouse 1")); // product amount in stock is not changed

            // member
            productAmount = proxy.getProductAmount(storeName2, "gaming mouse 1");
            Assertions.assertTrue(proxy.addToCart(member_name, storeName2, "gaming mouse 1", 3));
            Assertions.assertTrue(proxy.getBag(member_name, storeName2).contains("gaming mouse 1"));
            Assertions.assertEquals(3, proxy.getProductAmountInCart(member_name, storeName2, "gaming mouse 1"));
            Assertions.assertEquals(productAmount, proxy.getProductAmount(storeName2, "gaming mouse 1")); // product amount in stock is not changed
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_larger_amount_of_product_to_cart_fail(){
        try{
            // guest
//            int productAmount = proxy.getProductAmount(storeName2, "gaming mouse 1");
            Assertions.assertThrows(Exception.class,()-> proxy.addToCart(guest_name, storeName2, "gaming mouse 1", 200));
            Assertions.assertFalse(proxy.getBag(guest_name, storeName2).contains("gaming mouse 1"));
//            Assertions.assertEquals(productAmount, proxy.getProductAmount(storeName2, "gaming mouse 1")); // product amount in stock is not changed

            // member
//            productAmount = proxy.getProductAmount(storeName2, "gaming mouse 1");
            Assertions.assertThrows(Exception.class,()-> proxy.addToCart(member_name, storeName2, "gaming mouse 1", 200));
            Assertions.assertFalse(proxy.getBag(member_name, storeName2).contains("gaming mouse 1"));
//            Assertions.assertEquals(productAmount, proxy.getProductAmount(storeName2, "gaming mouse 1")); // product amount in stock is not changed
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_product_does_not_exist_to_cart_fail(){
        try{
            // guest
            Assertions.assertThrows(Exception.class,()-> proxy.addToCart(guest_name, storeName2, "mouse", 10));
            Assertions.assertFalse(proxy.getBag(guest_name, storeName2).contains("mouse"));

            // member
            Assertions.assertThrows(Exception.class,()-> proxy.addToCart(member_name, storeName2, "mouse", 10));
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
            Assertions.assertThrows(Exception.class,()-> proxy.addToCart(guest_name, storeName2, "iphone 14", 10));
            Assertions.assertFalse(proxy.getBag(guest_name, storeName2).contains("iphone 14"));

            // member
            Assertions.assertThrows(Exception.class,()-> proxy.addToCart(member_name, storeName2, "iphone 14", 10));
            Assertions.assertFalse(proxy.getBag(member_name, storeName2).contains("iphone 14"));
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_product_to_cart_bad_store_name_fail(){
        try{
            // guest
            Assertions.assertThrows(Exception.class,()-> proxy.addToCart(guest_name, "bad store name", "gaming mouse 1", 10));
            Assertions.assertFalse(proxy.getBag(guest_name, "bad store name").contains("gaming mouse 1"));

            // member
            Assertions.assertThrows(Exception.class,()-> proxy.addToCart(member_name, "bad store name", "gaming mouse 1", 10));
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
            Assertions.assertThrows(Exception.class,()-> proxy.addToCart(guest_name, storeName2, "gaming mouse 1", 2));
            Assertions.assertEquals(5, proxy.getProductAmountInCart(guest_name, storeName2, "gaming mouse 1"));

            // member
            Assertions.assertTrue(proxy.addToCart(member_name, storeName2, "gaming mouse 1", 5));
            Assertions.assertThrows(Exception.class,()-> proxy.addToCart(member_name, storeName2, "gaming mouse 1", 2));
            Assertions.assertEquals(5, proxy.getProductAmountInCart(member_name, storeName2, "gaming mouse 1"));

        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    // II.2.4 ********** DONE **********
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
            Assertions.assertEquals(1, proxy.getProductAmountInCart(guest_name, storeName1, "iphone 14"));
            Assertions.assertEquals(3, proxy.getProductAmountInCart(guest_name, storeName1, "gaming chair 1"));
            Assertions.assertEquals(1, proxy.getProductAmountInCart(guest_name, storeName2, "iphone 14"));
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
            Assertions.assertEquals(1, proxy.getProductAmountInCart(member_name, storeName1, "iphone 14"));
            Assertions.assertEquals(3, proxy.getProductAmountInCart(member_name, storeName1, "gaming chair 1"));
            Assertions.assertEquals(1, proxy.getProductAmountInCart(member_name, storeName2, "iphone 14"));
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
            Assertions.assertTrue(proxy.removeProductFromCart(guest_name, storeName2, "iphone 14"));
            cart = proxy.getCartContent(guest_name);
//            Assertions.assertEquals(1, cart.size()); // 1 bag
            Assertions.assertEquals(2, cart.size()); // change
            Assertions.assertTrue(cart.containsKey(storeName1));
//            Assertions.assertFalse(cart.containsKey(storeName2)); // delete storeName2 bag because it becomes empty after deletion
            Assertions.assertTrue(cart.containsKey(storeName2)); // change
            Assertions.assertEquals(2, cart.get(storeName1).size());
            Assertions.assertEquals(0, cart.get(storeName2).size()); // change
            Assertions.assertTrue(cart.get(storeName1).contains("iphone 14"));
            Assertions.assertTrue(cart.get(storeName1).contains("gaming chair 1"));
            Assertions.assertEquals(1, proxy.getProductAmountInCart(guest_name, storeName1, "iphone 14"));
            Assertions.assertEquals(3, proxy.getProductAmountInCart(guest_name, storeName1, "gaming chair 1"));
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
            Assertions.assertTrue(proxy.removeProductFromCart(member_name, storeName2, "iphone 14"));
            cart = proxy.getCartContent(member_name);
//            Assertions.assertEquals(1, cart.size()); // 1 bag
            Assertions.assertEquals(2, cart.size()); // change
            Assertions.assertTrue(cart.containsKey(storeName1));
//            Assertions.assertFalse(cart.containsKey(storeName2)); // delete storeName2 bag because it becomes empty after deletion
            Assertions.assertTrue(cart.containsKey(storeName2)); // change
            Assertions.assertEquals(2, cart.get(storeName1).size());
            Assertions.assertEquals(0, cart.get(storeName2).size()); // change
            Assertions.assertTrue(cart.get(storeName1).contains("iphone 14"));
            Assertions.assertTrue(cart.get(storeName1).contains("gaming chair 1"));
            Assertions.assertEquals(1, proxy.getProductAmountInCart(member_name, storeName1, "iphone 14"));
            Assertions.assertEquals(3, proxy.getProductAmountInCart(member_name, storeName1, "gaming chair 1"));
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_product_does_not_exist_in_cart(){
        try{
            // guest
            proxy.addToCart(guest_name, storeName1, "iphone 14", 1);
            Assertions.assertThrows(Exception.class,()-> proxy.removeProductFromCart(guest_name, storeName1, "iphone"));
            Map<String, List<String>> cart = proxy.getCartContent(guest_name);
            Assertions.assertEquals(1, cart.size()); // not changed
            Assertions.assertTrue(cart.containsKey(storeName1)); // not changed
            Assertions.assertTrue(cart.get(storeName1).contains("iphone 14")); // not changed

            // member
            proxy.addToCart(member_name, storeName1, "iphone 14", 1);
            Assertions.assertThrows(Exception.class,()-> proxy.removeProductFromCart(member_name, storeName1, "iphone"));
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
            Assertions.assertThrows(Exception.class,()-> proxy.removeProductFromCart(guest_name, "bad store name", "iphone 14"));
            Map<String, List<String>> cart = proxy.getCartContent(guest_name);
            Assertions.assertEquals(1, cart.size()); // not changed
            Assertions.assertTrue(cart.containsKey(storeName1)); // not changed
            Assertions.assertTrue(cart.get(storeName1).contains("iphone 14")); // not changed

            // member
            proxy.addToCart(member_name, storeName1, "iphone 14", 1);
            Assertions.assertThrows(Exception.class,()-> proxy.removeProductFromCart(member_name, "bad store name", "iphone 14"));
            cart = proxy.getCartContent(member_name);
            Assertions.assertEquals(1, cart.size()); // not changed
            Assertions.assertTrue(cart.containsKey(storeName1)); // not changed
            Assertions.assertTrue(cart.get(storeName1).contains("iphone 14")); // not changed
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void change_product_amount_in_cart_success(){ // TODO:
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
        try{
            // guest
            proxy.addToCart(guest_name, storeName1, "iphone 14", 1);
            // larger
            Assertions.assertThrows(Exception.class,()-> proxy.changeProductAmountInCart(guest_name, storeName1, "iphone 14", 1000)); // we have 50 iphone 14 in storeName1
            Assertions.assertEquals(1, proxy.getProductAmountInCart(guest_name, storeName1, "iphone 14")); // not changed
            // negative
            Assertions.assertThrows(Exception.class,()-> proxy.changeProductAmountInCart(guest_name, storeName1, "iphone 14", -10));
            Assertions.assertEquals(1, proxy.getProductAmountInCart(guest_name, storeName1, "iphone 14")); // not changed
            // TODO: check bag/cart total price

            // member
            proxy.addToCart(member_name, storeName1, "iphone 14", 1);
//            Assertions.assertEquals(1, proxy.getProductAmountInCart(member_name, storeName1, "iphone 14"));
            // larger
            Assertions.assertThrows(Exception.class,()-> proxy.changeProductAmountInCart(member_name, storeName1, "iphone 14", 1000)); // we have 50 iphone 14 in storeName1
            Assertions.assertEquals(1, proxy.getProductAmountInCart(member_name, storeName1, "iphone 14"));
            // negative
            Assertions.assertThrows(Exception.class,()-> proxy.changeProductAmountInCart(member_name, storeName1, "iphone 14", -10));
            Assertions.assertEquals(1, proxy.getProductAmountInCart(member_name, storeName1, "iphone 14"));

            // TODO: check bag/cart total price
        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    public void change_product_amount_in_cart_bad_store_name(){
        try
        {
            // guest
            proxy.addToCart(guest_name, storeName1, "iphone 14", 1);
            Assertions.assertThrows(Exception.class,()-> proxy.changeProductAmountInCart(guest_name, "bad store name", "iphone 14", 5));
            Assertions.assertEquals(1, proxy.getProductAmountInCart(guest_name, storeName1, "iphone 14"));
            // TODO: check bag/cart total price

            // member
            proxy.addToCart(member_name, storeName1, "iphone 14", 1);
            Assertions.assertThrows(Exception.class,()-> proxy.changeProductAmountInCart(member_name, "bad store name", "iphone 14", 5));
            Assertions.assertEquals(1, proxy.getProductAmountInCart(member_name, storeName1, "iphone 14"));
            // TODO: check bag/cart total price

        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    public void change_product_amount_in_cart_bad_product_name(){
        try
        {
            // guest
            proxy.addToCart(guest_name, storeName1, "iphone 14", 1);
            Assertions.assertThrows(Exception.class,()-> proxy.changeProductAmountInCart(guest_name, storeName1, "iphone 15", 5));
            Assertions.assertEquals(1, proxy.getProductAmountInCart(guest_name, storeName1, "iphone 14"));
            // TODO: check bag/cart total price

            // member
            proxy.addToCart(member_name, storeName1, "iphone 14", 1);
            Assertions.assertThrows(Exception.class,()-> proxy.changeProductAmountInCart(member_name, storeName1, "iphone 15", 5));
            Assertions.assertEquals(1, proxy.getProductAmountInCart(member_name, storeName1, "iphone 14"));
            // TODO: check bag/cart total price

        } catch (Exception e){
            Assertions.fail(e.getMessage());
        }

    }

    // II.2.5


}