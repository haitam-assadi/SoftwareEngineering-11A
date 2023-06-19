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
public class MemberTests {


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
        user1 = proxy.enterMarket();
        user2 = proxy.enterMarket();
        user3 = proxy.enterMarket();
        user4 = proxy.enterMarket();
    }

    //login Test
    @Test
    public void register_success(){
        try{
            proxy.register(user1, member1Name, member1Password);
            proxy.register(user2, member2Name, member2Password);
            proxy= new ProxyBridge(new RealBridge());
            proxy.loadData();
            user1 = proxy.enterMarket();
            user2 = proxy.enterMarket();
            user3 = proxy.enterMarket();
            Assertions.assertThrows(Exception.class, ()->proxy.register(user1, member1Name, member1Password));
            Assertions.assertThrows(Exception.class, ()->proxy.register(user2, member2Name, member2Password));
            user1 = proxy.login(user1, member1Name, member1Password);
            user2 = proxy.login(user2, member2Name, member2Password);

            // TODO: isOnline is not loaded or it is loaded but we dont updated logged in members map
            Assertions.assertTrue(user1.equals(member1Name));
            Assertions.assertTrue(user2.equals(member2Name));
            Assertions.assertThrows(Exception.class, ()->proxy.login(user3,member3Name,member3Password));
            proxy.register(user3, member3Name, member3Password);
            user3 =proxy.login(user3, member3Name, member3Password);

            initSystemServiceAndLoadDataAndLogIn();

            ////// create stores
            proxy.createStore(member1Name, store1Name);
            proxy.createStore(member1Name, store1bName);

            proxy.createStore(member2Name, store2Name);
            proxy.createStore(member2Name, store2bName);
            proxy.createStore(member3Name, store3Name);




            initSystemServiceAndLoadDataAndLogIn();

            System.out.println("dsfkhjsdf");
            //TODO: proxy.getAllStoresNames() didnt return all stores names, it did return used stores only


            Assertions.assertThrows(Exception.class, ()->proxy.createStore(member1Name, store1Name));
            Assertions.assertThrows(Exception.class, ()->proxy.createStore(member1Name, store1bName));
            Assertions.assertThrows(Exception.class, ()->proxy.createStore(member2Name, store2Name));
            Assertions.assertThrows(Exception.class, ()->proxy.createStore(member2Name, store2bName));
            Assertions.assertThrows(Exception.class, ()->proxy.createStore(member3Name, store3Name));



            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member1Name, store2Name, product1_store2, category_store2, 157.5, "description", 300));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member3Name, store2Name, product1_store2, category_store2, 157.5, "description", 300));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member1Name, store2bName, product1_store2, category_store2, 157.5, "description", 300));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member3Name, store2bName, product1_store2, category_store2, 157.5, "description", 300));


            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member2Name, store1Name, product1_store2, category_store2, 157.5, "description", 300));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member3Name, store1Name, product1_store2, category_store2, 157.5, "description", 300));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member2Name, store1bName, product1_store2, category_store2, 157.5, "description", 300));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member3Name, store1bName, product1_store2, category_store2, 157.5, "description", 300));


            StoreDTO storeDTO1 = proxy.getStoreInfo(member1Name,store1Name);
            Assertions.assertTrue(storeDTO1.storeName.equals(store1Name));
            Assertions.assertTrue(storeDTO1.founderName.equals(member1Name));
            Assertions.assertTrue(storeDTO1.ownersNames.isEmpty());
            Assertions.assertTrue(storeDTO1.managersNames.isEmpty());
            Assertions.assertTrue(storeDTO1.productsInfoAmount.keySet().isEmpty());
            Assertions.assertTrue(storeDTO1.isActive);


            StoreDTO storeDTO2 = proxy.getStoreInfo(member2Name,store2Name);
            Assertions.assertTrue(storeDTO2.storeName.equals(store2Name));
            Assertions.assertTrue(storeDTO2.founderName.equals(member2Name));
            Assertions.assertTrue(storeDTO2.ownersNames.isEmpty());
            Assertions.assertTrue(storeDTO2.managersNames.isEmpty());
            Assertions.assertTrue(storeDTO2.productsInfoAmount.keySet().isEmpty());
            Assertions.assertTrue(storeDTO2.isActive);


            proxy.addNewProductToStock(member1Name, store1Name, product1_store1, category1_product1_store1, 111.1, "description_product1_store1", 100);
            proxy.addNewProductToStock(member1Name, store1Name, product2_store1, category2_product2_store1, 222.2, "description", 200);
            proxy.addNewProductToStock(member1Name, store1bName, product1_store1b, category_store1b, 333.3, "description_product1_store1b", 300);
            proxy.addNewProductToStock(member1Name, store1bName, product2_store1b, category_store1b, 444.4, "description", 400);

            proxy.addNewProductToStock(member2Name, store2Name, product1_store2, category_store2, 555.5, "description_product1_store2", 500);
            proxy.addNewProductToStock(member2Name, store2Name, product2_store2, category_store2, 666.6, "description", 600);
            proxy.addNewProductToStock(member2Name, store2Name, product3_store2, category_store2, 777.7, "description", 700);


            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member1Name, store1Name, product1_store1, category1_product1_store1, 111.1, "description", 100));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member1Name, store1Name, product2_store1, category2_product2_store1, 222.2, "description", 200));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member1Name, store1bName, product1_store1b, category_store1b, 333.3, "description", 300));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member1Name, store1bName, product2_store1b, category_store1b, 444.4, "description", 400));

            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member2Name, store2Name, product1_store2, category_store2, 555.5, "description", 500));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member2Name, store2Name, product2_store2, category_store2, 666.6, "description", 600));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member2Name, store2Name, product3_store2, category_store2, 777.7, "description", 700));


            Assertions.assertTrue(proxy.getStoreProducts(member1Name,store1Name).contains(product1_store1));
            Assertions.assertTrue(proxy.getStoreProducts(member1Name,store1Name).contains(product2_store1));
            Assertions.assertTrue(proxy.getStoreProducts(member1Name,store1bName).contains(product1_store1b));
            Assertions.assertTrue(proxy.getStoreProducts(member1Name,store1bName).contains(product1_store1b));

            Assertions.assertTrue(proxy.getStoreProducts(member2Name,store2Name).contains(product1_store2));
            Assertions.assertTrue(proxy.getStoreProducts(member2Name,store2Name).contains(product2_store2));
            Assertions.assertTrue(proxy.getStoreProducts(member2Name,store2Name).contains(product3_store2));

            ProductDTO productDTO = proxy.getProductInfoFromStore(user4,store1Name ,product1_store1);
            Assertions.assertTrue(productDTO.name.equals(product1_store1));
            Assertions.assertTrue(productDTO.storeName.equals(store1Name));
            Assertions.assertTrue(productDTO.description.equals("description_product1_store1"));
            Assertions.assertTrue(productDTO.price.equals(111.1));
            Assertions.assertTrue(productDTO.categories.contains(category1_product1_store1));
            StoreDTO storeDTO = proxy.getStoreInfo(member1Name,store1Name);
            productDTO=null;
            for (ProductDTO p1: storeDTO.productsInfoAmount.keySet())
                if(p1.name.equals(product1_store1))
                    productDTO=p1;

            Assertions.assertTrue(productDTO!=null);
            Assertions.assertTrue(storeDTO.productsInfoAmount.get(productDTO).equals(100));
            Assertions.assertTrue(productDTO.name.equals(product1_store1));
            Assertions.assertTrue(productDTO.storeName.equals(store1Name));
            Assertions.assertTrue(productDTO.description.equals("description_product1_store1"));
            Assertions.assertTrue(productDTO.price.equals(111.1));
            Assertions.assertTrue(productDTO.categories.contains(category1_product1_store1));

            List<ProductDTO> map =  proxy.getProductInfoFromMarketByCategory(user4, product1_store1);
            System.out.println("dsfdfg");
            System.out.println(map.size());
            //TODO: getProductInfoFromMarketByCategory didnt return any product
            // after fixing we need to check it and check get product by keyword
            initSystemServiceAndLoadDataAndLogIn();


            ProductDTO member2Name_productDTO2 = proxy.getProductInfoFromStore(user4,store2Name ,product1_store2);
            Assertions.assertTrue(member2Name_productDTO2.name.equals(product1_store2));
            Assertions.assertTrue(member2Name_productDTO2.storeName.equals(store2Name));
            Assertions.assertTrue(member2Name_productDTO2.description.equals("description_product1_store2"));
            Assertions.assertTrue(member2Name_productDTO2.price.equals(555.5));
            Assertions.assertTrue(member2Name_productDTO2.categories.contains(category_store2));
            StoreDTO member2Name_storeDTO = proxy.getStoreInfo(member2Name,store2Name);
            member2Name_productDTO2=null;
            for (ProductDTO p1: member2Name_storeDTO.productsInfoAmount.keySet())
                if(p1.name.equals(product1_store2))
                    member2Name_productDTO2=p1;

            Assertions.assertTrue(productDTO!=null);
            Assertions.assertTrue(member2Name_storeDTO.productsInfoAmount.get(member2Name_productDTO2).equals(500));
            Assertions.assertTrue(member2Name_productDTO2.name.equals(product1_store2));
            Assertions.assertTrue(member2Name_productDTO2.storeName.equals(store2Name));
            Assertions.assertTrue(member2Name_productDTO2.description.equals("description_product1_store2"));
            Assertions.assertTrue(member2Name_productDTO2.price.equals(555.5));
            Assertions.assertTrue(member2Name_productDTO2.categories.contains(category_store2));

            initSystemServiceAndLoadDataAndLogIn();
            proxy.getProductInfoFromMarketByName(user4,product1_store1b);




        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


    private void initSystemServiceAndLoadDataAndLogIn() throws Exception {
        proxy= new ProxyBridge(new RealBridge());
        proxy.loadData();
        user1 = proxy.enterMarket();
        user2 = proxy.enterMarket();
        user3 = proxy.enterMarket();
        user4 = proxy.enterMarket();
        user1 = proxy.login(user1, member1Name, member1Password);
        user2 = proxy.login(user2, member2Name, member2Password);
        user3 =proxy.login(user3, member3Name, member3Password);
    }




    /*
    //login Test
    @Test
    public void login1_success(){
        try{

            user = proxy.enterMarket();
            String user2 = proxy.enterMarket();
            List<String>  members = proxy.getAllOnlineMembers();
            proxy.register(user,"haitamassadih","pASS123456");
            proxy.register(user2,"adelwattad","pASS123456");

            proxy.login(user,"haitamassadih","pASS123456");
            proxy.login(user2,"adelwattad","pASS123456");

            Assertions.assertEquals(2,proxy.getAllOnlineMembers().size());

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


    @Test
    public void login_failed_wrong_password(){

        try{
            user = proxy.enterMarket();
            List<String> guests = proxy.getAllGuests();
            if(guests.size()>0){
                Assertions.assertThrows(Exception.class,()-> proxy.login(user, member1Name,""));
                guests = proxy.getAllGuests();
                Assertions.assertFalse(proxy.getAllOnlineMembers().contains(member1Name));
                Assertions.assertTrue(guests.contains(user));
                proxy.exitMarket(user);
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void login_failed_member_not_exist(){
        try{

            user = proxy.enterMarket();
            List<String> guests = proxy.getAllGuests();
            if(guests.size()>0){
                Assertions.assertThrows(Exception.class, ()-> proxy.login(user,"Obiq", member1Password));
                guests = proxy.getAllGuests();
                Assertions.assertFalse(proxy.getAllOnlineMembers().contains("Obiq"));
                Assertions.assertTrue(guests.contains(user));
                proxy.exitMarket(user);
            }
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //exit market guest test
    @Test
    public void exit_market_member_success(){
        try{

            user = proxy.enterMarket();
            proxy.login(user, member1Name, member1Password);
            String userName = member1Name;
            List<String> members = proxy.getAllOnlineMembers();
            int len = members.size();
            proxy.exitMarket(userName);

            members = proxy.getAllOnlineMembers();
            Assertions.assertEquals(len - 1, members.size());//??
            Assertions.assertFalse(members.contains(userName));//??
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //member logout
    @Test
    public void logout_success(){
        try {

            user = proxy.enterMarket();
            proxy.login(user, member1Name, member1Password);
            String newGuestName = proxy.memberLogOut(member1Name);
            List<String> members = proxy.getAllOnlineMembers();
            Assertions.assertFalse(members.contains(member1Name));
            Assertions.assertNotEquals(member1Name,newGuestName);
            proxy.exitMarket(newGuestName);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void logout_not_online_member_fail(){
        try {

            user = proxy.enterMarket();
            proxy.login(user, member1Name, member1Password);
            proxy.memberLogOut(member1Name);
            proxy.memberLogOut(member1Name);
            Assertions.fail("logged out user managed to log out");
        }catch (Exception e){

        }
    }

    @Test
    public void logout_double_fail(){
        try {

            user=proxy.enterMarket();
            proxy.login(user, member1Name, member1Password);
            String newGuestName = proxy.memberLogOut(member1Name);
            List<String> members = proxy.getAllOnlineMembers();
            int len = members.size();
            Assertions.assertThrows(Exception.class,()-> proxy.memberLogOut(member1Name));
            members = proxy.getAllOnlineMembers();
            Assertions.assertEquals(len,members.size());
            Assertions.assertFalse(members.contains(member1Name));
            Assertions.assertNotEquals(member1Name,newGuestName);
            proxy.exitMarket(newGuestName);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


    //create store test
    @Test
    public void create_store_success(){
        try {

            user = proxy.enterMarket();
            String userName = member1Name;
            proxy.login(user, userName, member1Password);
            String storeName = proxy.createStore(userName, "verit");
            List<String> stores = proxy.getAllStoresNames();
            Assertions.assertTrue(stores.contains(storeName));
            String storeFounder = proxy.getStoreFounderName(userName, storeName);
            Assertions.assertEquals(userName, storeFounder);

            user = proxy.memberLogOut(member1Name);
            proxy.exitMarket(user);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void create_store_repeated_storeName_fail(){
        try {

            user = proxy.enterMarket();
            String userName = member1Name;
            proxy.login(user, userName, member1Password);
            String storeName = proxy.createStore(userName, "verit2");
            List<String> stores = proxy.getAllStoresNames();
            Assertions.assertTrue(stores.contains(storeName));
            String storeFounder = proxy.getStoreFounderName(userName, storeName);
            Assertions.assertEquals(userName, storeFounder);
            int len = stores.size();
            String storeName1 = proxy.createStore(userName, "verit21");//should return any thing but not verit
            Assertions.assertEquals(len + 1,proxy.getAllStoresNames().size());
            Assertions.assertNotEquals(storeName1,storeName);
            user = proxy.memberLogOut(member1Name);
            proxy.exitMarket(user);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void create_2stores_success(){
        try {

            user = proxy.enterMarket();
            String userName = member1Name;
            proxy.login(user, userName, member1Password);
            String storeName = proxy.createStore(userName, "verit3");

            List<String> stores = proxy.getAllStoresNames();
            Assertions.assertTrue(stores.contains(storeName));
            String storeFounder = proxy.getStoreFounderName(userName, storeName);
            Assertions.assertEquals(userName, storeFounder);
            storeName = proxy.createStore(userName, "Moslem Store1");
            stores = proxy.getAllStoresNames();
            Assertions.assertTrue(stores.contains(storeName));
            storeFounder = proxy.getStoreFounderName(userName, storeName);
            Assertions.assertEquals(userName, storeFounder);
            user = proxy.memberLogOut(member1Name);
            proxy.exitMarket(user);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }





*/


}