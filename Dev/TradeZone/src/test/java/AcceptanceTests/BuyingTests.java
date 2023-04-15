package AcceptanceTests;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
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

    

}
