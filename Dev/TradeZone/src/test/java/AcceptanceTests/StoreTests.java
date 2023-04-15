package AcceptanceTests;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class StoreTests {
    private ProxyBridge proxy;
    private String user;
    private String store_founder;
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
        store_founder = proxy.login(user,"Moslem Asaad","12345");
        storeName = proxy.createStore(store_founder, "Moslem Store");
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
            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
            Assertions.assertTrue(proxy.getCategory(member_name2,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder,storeName,"iphone 14","Iphones",3000,"256 Gb",50);
            Assertions.assertTrue(proxy.getStoreProducts(store_founder,storeName).contains("iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_new_product_repeated_productName_fail(){
        try{
            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
            Assertions.assertTrue(proxy.getCategory(member_name2,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder,storeName,"iphone 14","Iphones",3000,"256 Gb",50);
            List<String> products = proxy.getStoreProducts(store_founder,storeName);
            int len = products.size();
            proxy.addNewProductToStock(store_founder,storeName,"iphone 14","Iphones",3000,"256 Gb",50);
            Assertions.assertTrue(products.contains("iphone 14"));
            Assertions.assertEquals(len,proxy.getStoreProducts(storeName,storeName).size());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //delete product test
    @Test
    public void delete_product_test_success(){
        try{
            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
            Assertions.assertTrue(proxy.getCategory(member_name2,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder,storeName,"iphone 14","Iphones",3000,"256 Gb",50);
            Assertions.assertTrue(proxy.removeProductFromStock(store_founder,storeName,"iphone 14"));
            Assertions.assertFalse(proxy.getStoreProducts(store_founder,storeName).contains("iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void delete_product_test_not_exist_product_fail(){
        try{
            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
            Assertions.assertTrue(proxy.getCategory(store_founder,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder,storeName,"iphone 14","Iphones",3000,"256 Gb",50);
            List<String> products = proxy.getStoreProducts(store_founder,storeName);
            int len = products.size();
            Assertions.assertFalse(proxy.removeProductFromStock(store_founder,storeName,"iphone 11"));
            Assertions.assertEquals(len,proxy.getStoreProducts(store_founder,storeName).size());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //update details test
    @Test
    public void update_product_name_success(){
        try {
            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder, storeName, "iphone 14", "Iphones", 3000, "256 Gb", 50);
            Assertions.assertTrue(proxy.updateProductName(store_founder, storeName, "Iphone 14", "iphone 14.1"));
            Assertions.assertTrue(proxy.getStoreProducts(store_founder,storeName).contains("iphone 14.1"));
            Assertions.assertFalse(proxy.getStoreProducts(store_founder,storeName).contains("Iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_name_fail1(){
        try {
            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder, storeName, "iphone 14", "Iphones", 3000, "256 Gb", 50);
            Assertions.assertFalse(proxy.updateProductName(store_founder, storeName, "Iphone 14", "Iphone 14"));//because the name is unique
            Assertions.assertFalse(proxy.getStoreProducts(store_founder,storeName).contains("Iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_price_success(){
        try {
            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder, storeName, "iphone 14", "Iphones", 3000, "256 Gb", 50);
            Assertions.assertTrue(proxy.updateProductPrice(store_founder,storeName,"Iphone 14",2500));
            Assertions.assertEquals(2500,proxy.getProductPrice("Iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_price_not_exist_product_fail(){
        try {
            Assertions.assertFalse(proxy.updateProductPrice(store_founder,storeName,"Iphone 14",2500));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_description_success(){
        try {
            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder, storeName, "iphone 14", "Iphones", 3000, "256 Gb", 50);
            Assertions.assertTrue(proxy.updateProductDescription(store_founder,storeName,"Iphone 14","white, 256 Gb"));
            Assertions.assertNotEquals(proxy.getProductDescription("Iphone 14"),"256 Gb");
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_description_not_exist_product_fail(){
        try {
            Assertions.assertFalse(proxy.updateProductDescription(store_founder,storeName,"Iphone 14","white, 256 Gb"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_amount_success(){
        try {
            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder, storeName, "iphone 14", "Iphones", 3000, "256 Gb", 50);
            Assertions.assertTrue(proxy.updateProductAmount(store_founder,storeName,"Iphone 14",150));
            Assertions.assertNotEquals(proxy.getProductAmount("Iphone 14"), 50);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_amount_not_exist_product_fail(){
        try {
            Assertions.assertFalse(proxy.updateProductAmount(store_founder,storeName,"Iphone 14",150));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


    //appoint member to be store owner test
    @Test
    public void appoint_member_as_store_owner_from_founder_success(){
        try{
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getStoreOwners(storeName).contains(member_name1));
            Assertions.assertEquals(proxy.getOwnerAppointer(member_name1,storeName),store_founder);
            //should check also managing policy to be as the founder/the owner that makes him owner
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_owner_from_owner_success(){
        try{
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(store_founder,storeName,member_name1));
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
            Assertions.assertFalse(proxy.appointMemberAsStoreOwner(store_founder,storeName,"koko"));
            //should check also managing policy to be as the founder/the owner that makes him owner
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //circular appointment

    @Test
    public void appoint_member_as_store_owner_1circular_fail(){
        try{
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertFalse(proxy.appointMemberAsStoreOwner(member_name1,storeName,member_name1));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    public void appoint_member_as_store_owner_2circular_fail(){
        try{
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertFalse(proxy.appointMemberAsStoreOwner(member_name1,storeName,store_founder));
            Assertions.assertNotEquals(proxy.getOwnerAppointer(store_founder,storeName),member_name1);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //circular appointment
    @Test
    public void appoint_member_as_store_owner_3circular_fail(){
        try{
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(member_name1,storeName,member_name2));
            Assertions.assertFalse(proxy.appointMemberAsStoreOwner(member_name2,storeName,store_founder));
            Assertions.assertNotEquals(proxy.getOwnerAppointer(store_founder,storeName),member_name2);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //appoint member to be store manager test
    @Test
    public void appoint_member_as_store_manager_from_founder_success(){
        try{
            Assertions.assertTrue(proxy.appointMemberAsStoreManager(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getStoreManagers(storeName).contains(member_name1));
            Assertions.assertEquals(proxy.getManagerAppointer(member_name1,storeName),store_founder);
            //should check also managing permissions
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_manager_from_OwnerNotFounder_success(){
        try{
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.appointMemberAsStoreManager(member_name1,storeName,member_name2));
            Assertions.assertTrue(proxy.getStoreManagers(storeName).contains(member_name2));
            Assertions.assertEquals(proxy.getManagerAppointer(member_name2,storeName),member_name1);
            //todo:should check also managing permissions
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_manager_the_same_owner_twice_fail(){
        try{
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.appointMemberAsStoreManager(member_name1,storeName,member_name2));
            int len = proxy.getStoreManagers(storeName).size();
            Assertions.assertFalse(proxy.appointMemberAsStoreManager(member_name1,storeName,member_name2));
            Assertions.assertEquals(len,proxy.getStoreManagers(storeName).size());
            //todo:should check also managing permissions
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_manager_2Different_owners_fail(){
        try{
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.appointMemberAsStoreManager(member_name1,storeName,member_name2));
            int len = proxy.getStoreManagers(storeName).size();
            Assertions.assertFalse(proxy.appointMemberAsStoreManager(store_founder,storeName,member_name2));
            Assertions.assertEquals(len,proxy.getStoreManagers(storeName).size());
            Assertions.assertEquals(member_name1,proxy.getManagerAppointer(member_name2,storeName));
            Assertions.assertNotEquals(store_founder,proxy.getManagerAppointer(member_name2,storeName));
            //should check also managing permissions
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //close store tests
    @Test
    public void founder_close_store_success(){
        try{
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(store_founder,storeName,member_name1));
            String closeNotification = proxy.closeStore(store_founder,storeName);
            Assertions.assertFalse(proxy.canGetStoreInfo(member_name2,storeName));
            Assertions.assertTrue(proxy.canGetStoreInfo(member_name1,storeName));
            Assertions.assertEquals(closeNotification,proxy.getStoreNotification(member_name1,storeName));//??? should check
            Assertions.assertTrue(proxy.getStoreOwners(storeName).contains(member_name1));
            Assertions.assertEquals(0,proxy.getStoreProducts(member_name2,storeName).size());
            //todo: should check the case of closed store but founder assigns owner or manager for it.
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void founder_close_store_owner_try_fail(){
        try{
            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder, storeName, "iphone 14", "Iphones", 3000, "256 Gb", 50);
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(store_founder,storeName,member_name1));
            String closeNotification = proxy.closeStore(member_name1,storeName);
            Assertions.assertTrue(proxy.canGetStoreInfo(member_name2,storeName));
            Assertions.assertTrue(proxy.canGetStoreInfo(member_name1,storeName));
            Assertions.assertEquals(1,proxy.getStoreProducts(member_name2,storeName).size());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //incumbents at store tests
    @Test
    public void incumbents_get_success(){
        try{
            Assertions.assertTrue(proxy.appointMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.appointMemberAsStoreManager(store_founder,storeName,member_name2));
            Map<Integer,List<String>> incumbents = proxy.getStoreRulesInfo(member_name1,storeName);
            List<String> owners = proxy.getStoreOwners(storeName);
            List<String> managers = proxy.getStoreManagers(storeName);
            Assertions.assertTrue(incumbents.get(0).contains(store_founder));
            for(String s: owners){
                Assertions.assertTrue(incumbents.get(1).contains(s));
            }
            for(String s: managers){
                Assertions.assertTrue(incumbents.get(2).contains(s));
            }
            //todo: give the managers permissions and check it
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }










}
