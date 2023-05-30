package AcceptanceTests;

import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class RandomScenarios {



    private ProxyBridge proxy;

    @BeforeAll
    public void setUp(){
        System.out.println("setUp function");
        proxy = new ProxyBridge(new RealBridge());
        try {
            proxy.initializeMarket();

        }catch (Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void random_guest_actions_success(){
        try {
            String guestUserName = proxy.enterMarket();
            String StoreName = "user1_first_store";
            String storeProduct1= "user1_first_store_product1";
            String storeProduct2 = "user1_first_store_product2";
            String storeProduct3 = "user1_first_store_product3";
            String storeCategory1 = "user1_first_store_category1";
            Assertions.assertNotEquals("",guestUserName);

            List<String> store_products =  proxy.getStoreProducts(guestUserName, StoreName);
            Assertions.assertEquals(6, store_products.size());
            Assertions.assertTrue(store_products.contains(storeProduct1));
            Assertions.assertTrue(store_products.contains(storeProduct2));

            Map<String, List<String>> productInfo =  proxy.getProductInfoFromMarketByName(guestUserName, storeProduct3);
            Assertions.assertEquals(storeProduct3, productInfo.values().stream().toList().get(0).get(0));

            Map<String, List<String>> productInfoByCategory = proxy.getProductInfoFromMarketByCategory(guestUserName, storeCategory1);
            Assertions.assertTrue(productInfoByCategory.values().stream().toList().get(0).contains(storeProduct1));
            Assertions.assertTrue(productInfoByCategory.values().stream().toList().get(0).contains(storeProduct2));
            Assertions.assertTrue(productInfoByCategory.values().stream().toList().get(0).contains(storeProduct3));
            Assertions.assertFalse(productInfoByCategory.values().stream().toList().get(0).contains("this_should_not_be_a_product_name"));
            Assertions.assertTrue(proxy.register(guestUserName, "newMemberUserNameForTest_rgas1", "ValidPassword1"));
            Assertions.assertTrue(proxy.addToCart(guestUserName,StoreName, storeProduct1, 3));
            Assertions.assertTrue(proxy.addToCart(guestUserName,StoreName, storeProduct2, 5));
            //TODO: add get cart content assertions

            Assertions.assertTrue(proxy.exitMarket(guestUserName));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


    @Test
    public void random_member_actions_success(){
        try {
            String guestUserName = proxy.enterMarket();
            String temp_guest;
            String memberUserName = "user_test1";
            String password = "ValidPassword1";
            String StoreName = "user1_first_store";
            String storeProduct1= "user1_first_store_product1";
            String storeProduct2 = "user1_first_store_product2";
            String storeProduct3 = "user1_first_store_product3";
            String storeProduct4 = "user1_first_store_product4";
            String storeCategory1 = "user1_first_store_category1";
            Assertions.assertNotEquals("",guestUserName);
            Assertions.assertTrue(proxy.register(guestUserName, memberUserName, password));
            Assertions.assertEquals(memberUserName, proxy.login(guestUserName, memberUserName, password));


            List<String> store_products =  proxy.getStoreProducts(memberUserName, StoreName);
            Assertions.assertEquals(6, store_products.size());
            Assertions.assertTrue(store_products.contains(storeProduct1));
            Assertions.assertTrue(store_products.contains(storeProduct2));

            Map<String, List<String>> productInfo =  proxy.getProductInfoFromMarketByName(memberUserName, storeProduct3);
            Assertions.assertEquals(storeProduct3, productInfo.values().stream().toList().get(0).get(0));

            // user Logout and then logs in again
            temp_guest = proxy.memberLogOut(memberUserName);
            Assertions.assertNotEquals("", temp_guest);
            Assertions.assertEquals(memberUserName, proxy.login(temp_guest, memberUserName, password));


            Map<String, List<String>> productInfoByCategory = proxy.getProductInfoFromMarketByCategory(memberUserName, storeCategory1);
            Assertions.assertTrue(productInfoByCategory.values().stream().toList().get(0).contains(storeProduct1));
            Assertions.assertTrue(productInfoByCategory.values().stream().toList().get(0).contains(storeProduct2));
            Assertions.assertTrue(productInfoByCategory.values().stream().toList().get(0).contains(storeProduct3));
            Assertions.assertFalse(productInfoByCategory.values().stream().toList().get(0).contains(storeProduct4));
            Assertions.assertTrue(proxy.addToCart(memberUserName,StoreName, storeProduct1, 3));
            Assertions.assertTrue(proxy.addToCart(memberUserName,StoreName, storeProduct2, 5));
            //TODO: add get cart content assertions


            temp_guest = proxy.memberLogOut(memberUserName);
            Assertions.assertNotEquals("", temp_guest);
            Assertions.assertTrue(proxy.exitMarket(temp_guest));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


    @Test
    public void random_guest_and_member_actions_success(){
        try {
            String guestUserName = proxy.enterMarket();
            String temp_guest;
            String memberUserName = "user_test2";
            String password = "ValidPassword1";
            String StoreName = "user1_first_store";
            String storeProduct1= "user1_first_store_product1";
            String storeProduct2 = "user1_first_store_product2";
            String storeProduct3 = "user1_first_store_product3";
            String storeProduct4 = "user1_first_store_product4";
            String storeCategory1 = "user1_first_store_category1";
            Assertions.assertNotEquals("",guestUserName);
            Assertions.assertTrue(proxy.register(guestUserName, memberUserName, password));
            Assertions.assertEquals(memberUserName, proxy.login(guestUserName, memberUserName, password));

            String other_guest = proxy.enterMarket();
            Assertions.assertNotEquals("",other_guest);


            //member
            List<String> store_products_for_member =  proxy.getStoreProducts(memberUserName, StoreName);
            Assertions.assertEquals(6, store_products_for_member.size());
            Assertions.assertTrue(store_products_for_member.contains(storeProduct1));
            Assertions.assertTrue(store_products_for_member.contains(storeProduct2));


            //guest
            List<String> store_products_for_guest =  proxy.getStoreProducts(other_guest, StoreName);
            Assertions.assertEquals(6, store_products_for_guest.size());
            Assertions.assertTrue(store_products_for_guest.contains(storeProduct1));
            Assertions.assertTrue(store_products_for_guest.contains(storeProduct2));



            //member
            Map<String, List<String>> productInfo =  proxy.getProductInfoFromMarketByName(memberUserName, storeProduct3);
            Assertions.assertEquals(storeProduct3, productInfo.values().stream().toList().get(0).get(0));


            //guest
            Map<String, List<String>> productInfoForGuest =  proxy.getProductInfoFromMarketByName(other_guest, storeProduct3);
            Assertions.assertEquals(storeProduct3, productInfoForGuest.values().stream().toList().get(0).get(0));


            //member
            // user Logout and then logs in again
            temp_guest = proxy.memberLogOut(memberUserName);
            Assertions.assertNotEquals("", temp_guest);
            Assertions.assertEquals(memberUserName, proxy.login(temp_guest, memberUserName, password));

            //member
            Map<String, List<String>> productInfoByCategory = proxy.getProductInfoFromMarketByCategory(memberUserName, storeCategory1);
            Assertions.assertTrue(productInfoByCategory.values().stream().toList().get(0).contains(storeProduct1));
            Assertions.assertTrue(productInfoByCategory.values().stream().toList().get(0).contains(storeProduct2));
            Assertions.assertTrue(productInfoByCategory.values().stream().toList().get(0).contains(storeProduct3));
            Assertions.assertFalse(productInfoByCategory.values().stream().toList().get(0).contains(storeProduct4));
            Assertions.assertTrue(proxy.addToCart(memberUserName,StoreName, storeProduct1, 3));
            Assertions.assertTrue(proxy.addToCart(memberUserName,StoreName, storeProduct2, 5));
            //TODO: add get cart content assertions




            //guest
            Map<String, List<String>> productInfoByCategoryForGuest = proxy.getProductInfoFromMarketByCategory(other_guest, storeCategory1);
            Assertions.assertTrue(productInfoByCategoryForGuest.values().stream().toList().get(0).contains(storeProduct1));
            Assertions.assertTrue(productInfoByCategoryForGuest.values().stream().toList().get(0).contains(storeProduct2));
            Assertions.assertTrue(productInfoByCategoryForGuest.values().stream().toList().get(0).contains(storeProduct3));
            Assertions.assertFalse(productInfoByCategoryForGuest.values().stream().toList().get(0).contains(storeProduct4));
            Assertions.assertTrue(proxy.addToCart(other_guest,StoreName, storeProduct1, 3));
            Assertions.assertTrue(proxy.addToCart(other_guest,StoreName, storeProduct2, 5));
            //TODO: add get cart content assertions


            temp_guest = proxy.memberLogOut(memberUserName);
            Assertions.assertNotEquals("", temp_guest);
            Assertions.assertTrue(proxy.exitMarket(temp_guest));


            Assertions.assertTrue(proxy.exitMarket(other_guest));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


    @Test
    public void member_more_specific_actions_actions_success(){
        try {
            String guestUserName = proxy.enterMarket();
            String temp_guest;
            String memberUserName = "user_test3";
            String password = "ValidPassword3";
            String StoreName = "user1_first_store";
            String storeProduct1= "user1_first_store_product1";
            String storeProduct2 = "user1_first_store_product2";
            String storeProduct3 = "user1_first_store_product3";
            String storeProduct4 = "user1_first_store_product4";
            String storeCategory1 = "user1_first_store_category1";
            Assertions.assertNotEquals("",guestUserName);
            Assertions.assertTrue(proxy.register(guestUserName, memberUserName, password));
            Assertions.assertEquals(memberUserName, proxy.login(guestUserName, memberUserName, password));

            String newStoreName = memberUserName+"_newstore";
            String userFirstStoreProduct1 = newStoreName+"_product1";
            String userFirstStoreProduct2 = newStoreName+"-product2";
            String userFirstStoreProduct3 = newStoreName+"_product3";
            String userFirstStoreCategory1 = newStoreName+"_category1";
            String userFirstStoreCategory2 = newStoreName+"_category2";

            Assertions.assertEquals(newStoreName ,proxy.createStore(memberUserName, newStoreName));
            Assertions.assertTrue(proxy.addNewProductToStock(memberUserName, newStoreName, userFirstStoreProduct1, userFirstStoreCategory1, 400.34, "product1_des", 204));
            Assertions.assertTrue(proxy.addNewProductToStock(memberUserName, newStoreName, userFirstStoreProduct2, userFirstStoreCategory1, 87.21, "product1_des", 37));
            Assertions.assertTrue(proxy.addNewProductToStock(memberUserName, newStoreName, userFirstStoreProduct3, userFirstStoreCategory2, 808.52, "product1_des", 6));


            //member continue performing old actions on old stores
            List<String> store_products =  proxy.getStoreProducts(memberUserName, StoreName);
            Assertions.assertEquals(6, store_products.size());
            Assertions.assertTrue(store_products.contains(storeProduct1));

            Map<String, List<String>> productInfo =  proxy.getProductInfoFromMarketByName(memberUserName, storeProduct3);
            Assertions.assertEquals(storeProduct3, productInfo.values().stream().toList().get(0).get(0));

            // user Logout and then logs in again
            temp_guest = proxy.memberLogOut(memberUserName);
            Assertions.assertNotEquals("", temp_guest);
            Assertions.assertEquals(memberUserName, proxy.login(temp_guest, memberUserName, password));


            Map<String, List<String>> productInfoByCategory = proxy.getProductInfoFromMarketByCategory(memberUserName, storeCategory1);
            Assertions.assertTrue(productInfoByCategory.values().stream().toList().get(0).contains(storeProduct1));
            Assertions.assertTrue(proxy.addToCart(memberUserName,StoreName, storeProduct1, 3));
            Assertions.assertTrue(proxy.addToCart(memberUserName,StoreName, storeProduct2, 5));


            // guest performs action on memebers new store
            String other_guest = proxy.enterMarket();
            Assertions.assertNotEquals("",other_guest);

            List<String> store_products_for_guest =  proxy.getStoreProducts(other_guest, newStoreName);
            Assertions.assertEquals(3, store_products_for_guest.size());
            Assertions.assertTrue(store_products_for_guest.contains(userFirstStoreProduct2));

            Map<String, List<String>> product_info_for_guest =  proxy.getProductInfoFromMarketByName(other_guest, userFirstStoreProduct1);
            Assertions.assertEquals(userFirstStoreProduct1, product_info_for_guest.values().stream().toList().get(0).get(0));

            product_info_for_guest =  proxy.getProductInfoFromMarketByName(other_guest, userFirstStoreProduct3);
            Assertions.assertEquals(userFirstStoreProduct3, product_info_for_guest.values().stream().toList().get(0).get(0));



            Map<String, List<String>> productInfoByCategoryForGuest = proxy.getProductInfoFromMarketByCategory(other_guest, userFirstStoreCategory1);
            Assertions.assertTrue(productInfoByCategoryForGuest.values().stream().toList().get(0).contains(userFirstStoreProduct2));

            productInfoByCategoryForGuest = proxy.getProductInfoFromMarketByCategory(other_guest, userFirstStoreCategory2);
            Assertions.assertTrue(productInfoByCategoryForGuest.values().stream().toList().get(0).contains(userFirstStoreProduct3));
            Assertions.assertTrue(proxy.addToCart(other_guest,newStoreName, userFirstStoreProduct3, 6));
            Assertions.assertTrue(proxy.addToCart(other_guest,newStoreName, userFirstStoreProduct2, 30));


            temp_guest = proxy.memberLogOut(memberUserName);
            Assertions.assertNotEquals("", temp_guest);
            Assertions.assertTrue(proxy.exitMarket(temp_guest));

            Assertions.assertTrue(proxy.exitMarket(other_guest));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }



}
