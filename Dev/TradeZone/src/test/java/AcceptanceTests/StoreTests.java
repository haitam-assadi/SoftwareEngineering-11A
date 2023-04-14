package AcceptanceTests;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class StoreTests {
    private ProxyBridge proxy;
    private String user;
    private String member_name;
    private String member_name1;
    private String member_name2;
    private String storeName;
    @Mock
    private Bridge bridge;

    @BeforeAll
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        proxy = new ProxyBridge();
        if(!proxy.initializeMarket()){
            throw new Exception(""); // should change
        }
        user = proxy.enterMarket();//guest default user name
        if(!proxy.register(user,"Moslem Asaad","12345")){
            throw new Exception("");
        }
        member_name = proxy.login(user,"Moslem Asaad","12345");
        storeName = proxy.createStore(member_name, "Moslem Store");
        user = proxy.enterMarket();//guest default user name
        if(!proxy.register(user,"Brain","asdf")){
            throw new Exception("");
        }
        member_name1 = proxy.login(user,"Brain","asdf");
        user = proxy.enterMarket();//guest default user name
        if(!proxy.register(user,"Harry","1111")){
            throw new Exception("");
        }
        member_name2 = "Harry";
    }

    //II.4.1
    //add product test
    @Test
    public void add_new_product_success(){
        try{
            Assertions.assertTrue(proxy.addCategory(member_name,"Iphones"));
            Assertions.assertTrue(proxy.getCategory("Iphones"));
            proxy.addNewProductToStock(member_name,storeName,"iphone 14","Iphones",3000,"256 Gb",50);
            Assertions.assertTrue(proxy.getStoreProducts(storeName).contains("iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_new_product_repeated_productName_fail(){
        try{
            Assertions.assertTrue(proxy.addCategory(member_name,"Iphones"));
            Assertions.assertTrue(proxy.getCategory("Iphones"));
            proxy.addNewProductToStock(member_name,storeName,"iphone 14","Iphones",3000,"256 Gb",50);
            List<String> products = proxy.getStoreProducts(storeName);
            int len = products.size();
            proxy.addNewProductToStock(member_name,storeName,"iphone 14","Iphones",3000,"256 Gb",50);
            Assertions.assertTrue(products.contains("iphone 14"));
            Assertions.assertEquals(len,proxy.getStoreProducts(storeName).size());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //delete product test
    @Test
    public void delete_product_test_success(){
        try{
            Assertions.assertTrue(proxy.addCategory(member_name,"Iphones"));
            Assertions.assertTrue(proxy.getCategory("Iphones"));
            proxy.addNewProductToStock(member_name,storeName,"iphone 14","Iphones",3000,"256 Gb",50);
            Assertions.assertTrue(proxy.removeProductFromStock(member_name,storeName,"iphone 14"));
            Assertions.assertFalse(proxy.getStoreProducts(storeName).contains("iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void delete_product_test_not_exist_product_fail(){
        try{
            Assertions.assertTrue(proxy.addCategory(member_name,"Iphones"));
            Assertions.assertTrue(proxy.getCategory("Iphones"));
            proxy.addNewProductToStock(member_name,storeName,"iphone 14","Iphones",3000,"256 Gb",50);
            List<String> products = proxy.getStoreProducts(storeName);
            int len = products.size();
            Assertions.assertFalse(proxy.removeProductFromStock(member_name,storeName,"iphone 11"));
            Assertions.assertEquals(len,proxy.getStoreProducts(storeName).size());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //update details test
    @Test
    public void update_product_name_success(){
        try {
            Assertions.assertTrue(proxy.addCategory(member_name,"Iphones"));
            proxy.addNewProductToStock(member_name, storeName, "iphone 14", "Iphones", 3000, "256 Gb", 50);
            Assertions.assertTrue(proxy.updateProductName(member_name, storeName, "Iphone 14", "iphone 14.1"));
            Assertions.assertTrue(proxy.getStoreProducts(storeName).contains("iphone 14.1"));
            Assertions.assertFalse(proxy.getStoreProducts(storeName).contains("Iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_name_fail1(){
        try {
            Assertions.assertTrue(proxy.addCategory(member_name,"Iphones"));
            proxy.addNewProductToStock(member_name, storeName, "iphone 14", "Iphones", 3000, "256 Gb", 50);
            Assertions.assertFalse(proxy.updateProductName(member_name, storeName, "Iphone 14", "Iphone 14"));//because the name is unique
            Assertions.assertFalse(proxy.getStoreProducts(storeName).contains("Iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_price_success(){
        try {
            Assertions.assertTrue(proxy.addCategory(member_name,"Iphones"));
            proxy.addNewProductToStock(member_name, storeName, "iphone 14", "Iphones", 3000, "256 Gb", 50);
            Assertions.assertTrue(proxy.updateProductPrice(member_name,storeName,"Iphone 14",2500));
            Assertions.assertEquals(2500,proxy.getProductPrice("Iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_price_not_exist_product_fail(){
        try {
            Assertions.assertFalse(proxy.updateProductPrice(member_name,storeName,"Iphone 14",2500));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_description_success(){
        try {
            Assertions.assertTrue(proxy.addCategory(member_name,"Iphones"));
            proxy.addNewProductToStock(member_name, storeName, "iphone 14", "Iphones", 3000, "256 Gb", 50);
            Assertions.assertTrue(proxy.updateProductDescription(member_name,storeName,"Iphone 14","white, 256 Gb"));
            Assertions.assertNotEquals(proxy.getProductDescription("Iphone 14"),"256 Gb");
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_description_not_exist_product_fail(){
        try {
            Assertions.assertFalse(proxy.updateProductDescription(member_name,storeName,"Iphone 14","white, 256 Gb"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_amount_success(){
        try {
            Assertions.assertTrue(proxy.addCategory(member_name,"Iphones"));
            proxy.addNewProductToStock(member_name, storeName, "iphone 14", "Iphones", 3000, "256 Gb", 50);
            Assertions.assertTrue(proxy.updateProductAmount(member_name,storeName,"Iphone 14",150));
            Assertions.assertNotEquals(proxy.getProductAmount("Iphone 14"), 50);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_amount_not_exist_product_fail(){
        try {
            Assertions.assertFalse(proxy.updateProductAmount(member_name,storeName,"Iphone 14",150));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


    //appoint member to be store owner test
    @Test
    public void appoint_member_as_store_owner_from_founder_success(){
        try{
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(member_name,storeName,member_name1));
            Assertions.assertTrue(proxy.getStoreOwners(storeName).contains(member_name1));
            Assertions.assertEquals(proxy.getOwnerAppointer(member_name1,storeName),member_name);
            //should check also managing policy to be as the founder/the owner that makes him owner
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_owner_from_owner_success(){
        try{
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(member_name,storeName,member_name1));
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(member_name1,storeName,member_name2));
            Assertions.assertTrue(proxy.getStoreOwners(storeName).contains(member_name2));
            Assertions.assertEquals(proxy.getOwnerAppointer(member_name2,storeName),member_name1);
            //should check also managing policy to be as the founder/the owner that makes him owner
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_owner_not_member_fail(){
        try{
            Assertions.assertFalse(proxy.appointMemberAsStoreOwner(member_name,storeName,"koko"));
            //should check also managing policy to be as the founder/the owner that makes him owner
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //circular appointment
    @Test
    public void appoint_member_as_store_owner_2circular_fail(){
        try{
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(member_name,storeName,member_name1));
            Assertions.assertFalse(proxy.appointMemberAsStoreOwner(member_name1,storeName,member_name));
            Assertions.assertNotEquals(proxy.getOwnerAppointer(member_name,storeName),member_name1);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //circular appointment
    @Test
    public void appoint_member_as_store_owner_3circular_fail(){
        try{
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(member_name,storeName,member_name1));
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(member_name1,storeName,member_name2));
            Assertions.assertFalse(proxy.appointMemberAsStoreOwner(member_name2,storeName,member_name));
            Assertions.assertNotEquals(proxy.getOwnerAppointer(member_name,storeName),member_name2);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }






}
