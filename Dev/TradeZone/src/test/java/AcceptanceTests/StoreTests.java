package AcceptanceTests;
import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class StoreTests {
    private ProxyBridge proxy;
    private String user;
    String moslemUserName = "moslem123";
    String moslemPassword = "Aa123456";
    private String store_founder;
    private String member_name1;
    private String member_name2;
    private String storeName;

    @BeforeEach
    public void setUp() throws Exception {
        proxy = new ProxyBridge(new RealBridge());
        if(proxy.initializeMarket().isEmpty()){
            throw new Exception(""); // should change
        }

        // founder
        user = proxy.enterMarket();//guest default user name
        proxy.register(user,moslemUserName,moslemPassword);
        store_founder = proxy.login(user,moslemUserName,moslemPassword);
        storeName = proxy.createStore(store_founder, "Moslem Store");


        user = proxy.enterMarket();//guest default user name
        proxy.register(user,"brain123","asdf123456");
        member_name1 = proxy.login(user,"brain123","asdf123456");


        user = proxy.enterMarket();//guest default user name
        proxy.register(user,"harry123","Hh1111456");
//        member_name2 = "harry123"; // delete?
        member_name2 = proxy.login(user, "harry123","Hh1111456");
    }

    //II.4.1
    //add product test
    @Test
    public void add_new_product_success(){
        try{
//            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
//            Assertions.assertTrue(proxy.getCategory(member_name2,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder,storeName,"iphone 14","Iphones",3000.0,"256 Gb",50);
            Assertions.assertTrue(proxy.getStoreProducts(store_founder,storeName).contains("iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_new_product_repeated_productName_fail(){
        try{
//            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
//            Assertions.assertTrue(proxy.getCategory(member_name2,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder,storeName,"iphone 14","Iphones",3000.0,"256 Gb",50);
            List<String> products = proxy.getStoreProducts(store_founder,storeName);
            int len = products.size();
            Assertions.assertThrows(Exception.class, () -> proxy.addNewProductToStock(store_founder,storeName,"iphone 14","Iphones",3000.0,"256 Gb",50));
            Assertions.assertTrue(products.contains("iphone 14"));
            Assertions.assertEquals(len,proxy.getStoreProducts(store_founder,storeName).size());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //delete product test
    @Test
    public void delete_product_test_success(){
        try{
//            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
//            Assertions.assertTrue(proxy.getCategory(member_name2,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder,storeName,"iphone 14","Iphones",3000.0,"256 Gb",50);
            Assertions.assertTrue(proxy.removeProductFromStock(store_founder,storeName,"iphone 14"));
            Assertions.assertFalse(proxy.getStoreProducts(store_founder,storeName).contains("iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void delete_product_test_not_exist_product_fail(){
        try{
//            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
//            Assertions.assertTrue(proxy.getCategory(store_founder,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder,storeName,"iphone 14","Iphones",3000.0,"256 Gb",50);
            List<String> products = proxy.getStoreProducts(store_founder,storeName);
            int len = products.size();
            Assertions.assertThrows(Exception.class, () -> proxy.removeProductFromStock(store_founder,storeName,"iphone 11"));
            Assertions.assertEquals(len,proxy.getStoreProducts(store_founder,storeName).size());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //update details test
//    @Test
//    public void update_product_name_success(){
//        try {
////            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
//            proxy.addNewProductToStock(store_founder, storeName, "iphone 14", "Iphones", 3000.0, "256 Gb", 50);
//            Assertions.assertTrue(proxy.updateProductName(store_founder, storeName, "Iphone 14", "iphone 14.1"));
//            Assertions.assertTrue(proxy.getStoreProducts(store_founder,storeName).contains("iphone 14.1"));
//            Assertions.assertFalse(proxy.getStoreProducts(store_founder,storeName).contains("Iphone 14"));
//        }catch (Exception e){
//            Assertions.fail(e.getMessage());
//        }
//    }

//    @Test
//    public void update_product_name_fail1(){
//        try {
//            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
//            proxy.addNewProductToStock(store_founder, storeName, "iphone 14", "Iphones", 3000.0, "256 Gb", 50);
//            Assertions.assertFalse(proxy.updateProductName(store_founder, storeName, "Iphone 14", "Iphone 14"));//because the name is unique
//            Assertions.assertFalse(proxy.getStoreProducts(store_founder,storeName).contains("Iphone 14"));
//        }catch (Exception e){
//            Assertions.fail(e.getMessage());
//        }
//    }

    @Test
    public void update_product_price_success(){
        try {
//            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder, storeName, "iphone 14", "Iphones", 3000.0, "256 Gb", 50);
            Assertions.assertTrue(proxy.updateProductPrice(store_founder,storeName,"iphone 14",2500.0));
            Assertions.assertEquals(2500,proxy.getProductPrice(store_founder, storeName, "Iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_price_not_exist_product_fail(){
        try {
            Assertions.assertThrows(Exception.class, () -> proxy.updateProductPrice(store_founder,storeName,"Iphone 14",2500.0));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_description_success(){
        try {
//            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder, storeName, "iphone 14", "Iphones", 3000.0, "256 Gb", 50);
            Assertions.assertTrue(proxy.updateProductDescription(store_founder,storeName,"iphone 14","white, 256 Gb"));
            Assertions.assertNotEquals(proxy.getProductDescription(store_founder, storeName, "iphone 14"),"256 Gb");
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_description_not_exist_product_fail(){
        try {
            Assertions.assertThrows(Exception.class, () -> proxy.updateProductDescription(store_founder,storeName,"iphone 14","white, 256 Gb"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_amount_success(){
        try {
//            Assertions.assertTrue(proxy.addCategory(store_founder,"Iphones",storeName));
            proxy.addNewProductToStock(store_founder, storeName, "iphone 14", "Iphones", 3000.0, "256 Gb", 50);
            Assertions.assertTrue(proxy.updateProductAmount(store_founder,storeName,"iphone 14",150));
            Assertions.assertNotEquals(50, proxy.getProductAmount(storeName,"iphone 14"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_product_amount_not_exist_product_fail(){
        try {
            Assertions.assertThrows(Exception.class, () -> proxy.updateProductAmount(store_founder,storeName,"Iphone 14",150));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


    // II.4.4
    //appoint member to be store owner test
    @Test
    public void appoint_member_as_store_owner_from_founder_success(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
//            Assertions.assertEquals(store_founder, proxy.getOwnerAppointer(member_name1,storeName));
            //should check also managing policy to be as the founder/the owner that makes him owner
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_owner_from_owner_success(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(member_name1,storeName,member_name2));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name2));
//            Assertions.assertEquals(proxy.getOwnerAppointer(member_name2,storeName),member_name1);
            //should check also managing policy to be as the founder/the owner that makes him owner
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_owner_not_member_fail(){
        try{
            Assertions.assertThrows(Exception.class, () -> proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,"koko"));
            //should check also managing policy to be as the founder/the owner that makes him owner
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //circular appointment

    @Test
    public void appoint_member_as_store_owner_1circular_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertThrows(Exception.class, () -> proxy.appointOtherMemberAsStoreOwner(member_name1,storeName,member_name1));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    public void appoint_member_as_store_owner_2circular_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertThrows(Exception.class, () -> proxy.appointOtherMemberAsStoreOwner(member_name1,storeName,store_founder));
            Assertions.assertNotEquals(member_name1, proxy.getOwnerAppointer(store_founder,storeName));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //circular appointment
    @Test
    public void appoint_member_as_store_owner_3circular_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(member_name1,storeName,member_name2));
            Assertions.assertThrows(Exception.class, () -> proxy.appointOtherMemberAsStoreOwner(member_name2,storeName,store_founder));
//            Assertions.assertNotEquals(member_name2, proxy.getOwnerAppointer(store_founder,storeName));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    // II.4.6
    //appoint member to be store manager test
    @Test
    public void appoint_member_as_store_manager_from_founder_success(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreManager(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getStoreManagersNames(store_founder, storeName).contains(member_name1));
//            Assertions.assertEquals(store_founder, proxy.getManagerAppointer(member_name1,storeName));
            //todo:should check also managing permissions
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_manager_from_OwnerNotFounder_success(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreManager(member_name1,storeName,member_name2));
            Assertions.assertTrue(proxy.getStoreManagersNames(store_founder, storeName).contains(member_name2));
//            Assertions.assertEquals(proxy.getManagerAppointer(member_name2,storeName),member_name1);
            //todo:should check also managing permissions
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_manager_the_same_owner_twice_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreManager(member_name1,storeName,member_name2));
            int len = proxy.getStoreManagersNames(store_founder, storeName).size();
            Assertions.assertThrows(Exception.class, () -> proxy.appointOtherMemberAsStoreManager(member_name1,storeName,member_name2));
            Assertions.assertEquals(len,proxy.getStoreManagersNames(store_founder, storeName).size());
            //todo:should check also managing permissions
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_manager_2Different_owners_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreManager(member_name1,storeName,member_name2));
            int len = proxy.getStoreManagersNames(store_founder, storeName).size();
            Assertions.assertThrows(Exception.class, () -> proxy.appointOtherMemberAsStoreManager(store_founder,storeName,member_name2));
            Assertions.assertEquals(len, proxy.getStoreManagersNames(store_founder, storeName).size());
//            Assertions.assertEquals(member_name1, proxy.getManagerAppointer(member_name2,storeName));
//            Assertions.assertNotEquals(store_founder, proxy.getManagerAppointer(member_name2,storeName));
            //should check also managing permissions
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    // II.4.9
    //close store tests
    @Test
    public void founder_close_store_success(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder, storeName, member_name1));
            Assertions.assertTrue(proxy.closeStore(store_founder, storeName));
            Assertions.assertThrows(Exception.class, () -> proxy.canGetStoreInfo(member_name2, storeName));
            Assertions.assertTrue(proxy.canGetStoreInfo(member_name1, storeName)); // TODO: uncomment after fixing canGetStoreInfo function
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1)); // TODO: uncomment after fixing getStoreInfo function
            Assertions.assertThrows(Exception.class, () -> proxy.getStoreProducts(member_name2, storeName)); // TODO: maybe we should check search function, not get store products
            //todo: should check the case of closed store but founder assigns owner or manager for it.
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void founder_close_store_owner_try_fail(){
        try{
            proxy.addNewProductToStock(store_founder, storeName, "iphone 14", "Iphones", 3000.0, "256 Gb", 50);
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder, storeName, member_name1));
            Assertions.assertThrows(Exception.class, () -> proxy.closeStore(member_name1, storeName));
            Assertions.assertTrue(proxy.canGetStoreInfo(member_name2, storeName));
            Assertions.assertTrue(proxy.canGetStoreInfo(member_name1, storeName));
            Assertions.assertEquals(1,proxy.getStoreProducts(member_name2, storeName).size());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    // II.4.11
    //incumbents at store tests
    @Test
    public void incumbents_get_success(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder, storeName, member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreManager(store_founder, storeName, member_name2));
            Map<Integer,List<String>> incumbents = proxy.getStoreRulesInfo(member_name1,storeName);
            List<String> owners = proxy.getStoreOwnersNames(store_founder, storeName);
            List<String> managers = proxy.getStoreManagersNames(store_founder, storeName);
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

    //II.4.5
    @Test
    public void remove_owner_by_founder_success(){
        try{
            proxy.appointOtherMemberAsStoreOwner(store_founder, storeName, member_name1);
            Assertions.assertTrue(proxy.removeOwnerByHisAppointer(store_founder,storeName,member_name1));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_owner_by_owner_success(){
        try{
            proxy.appointOtherMemberAsStoreOwner(store_founder, storeName, member_name1);
            proxy.appointOtherMemberAsStoreOwner(member_name1, storeName, member_name2);
            Assertions.assertTrue(proxy.removeOwnerByHisAppointer(member_name1,storeName,member_name2));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_owner_by_him_self_fail(){
        try{
            proxy.appointOtherMemberAsStoreOwner(store_founder, storeName, member_name1);
            Assertions.assertThrows(Exception.class,()-> proxy.removeOwnerByHisAppointer(store_founder,storeName,store_founder));
            Assertions.assertThrows(Exception.class,()-> proxy.removeOwnerByHisAppointer(member_name1,storeName,member_name1));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_owner_by_not_his_appointer_fail(){
        try{
            proxy.appointOtherMemberAsStoreOwner(store_founder, storeName, member_name1);
            proxy.appointOtherMemberAsStoreOwner(store_founder, storeName, member_name2);
            Assertions.assertThrows(Exception.class,()-> proxy.removeOwnerByHisAppointer(member_name1,storeName,member_name2));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }



    // II.6.4




}