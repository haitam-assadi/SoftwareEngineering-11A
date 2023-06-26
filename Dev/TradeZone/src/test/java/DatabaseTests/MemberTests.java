package DatabaseTests;

import DTO.MemberDTO;
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

    //systemManagerCloseStore - one bug fixed, but noticed another bug, and comment saying no need for this req

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


            Assertions.assertTrue(user1.equals(member1Name));
            Assertions.assertTrue(user2.equals(member2Name));
            Assertions.assertThrows(Exception.class, ()->proxy.login(user3,member3Name,member3Password));
            proxy.register(user3, member3Name, member3Password);
            user3 =proxy.login(user3, member3Name, member3Password);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            ////// create stores
            proxy.createStore(member1Name, store1Name);
            proxy.createStore(member1Name, store1bName);

            proxy.createStore(member2Name, store2Name);
            proxy.createStore(member2Name, store2bName);
            proxy.createStore(member3Name, store3Name);



            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            List<String> storeNames = proxy.getAllStoresNames();
            Assertions.assertTrue(storeNames.contains(store1Name));
            Assertions.assertTrue(storeNames.contains(store2Name));
            Assertions.assertTrue(storeNames.contains(store3Name));
            Assertions.assertTrue(storeNames.contains(store1bName));
            Assertions.assertTrue(storeNames.contains(store2bName));

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
            proxy.addNewProductToStock(member2Name, store2Name, product2_store2, category_store2, 666.6, "description_product2_store2", 600);
            proxy.addNewProductToStock(member2Name, store2Name, product3_store2, category_store2, 777.7, "description_product3_store2", 700);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member1Name, store1Name, product1_store1, category1_product1_store1, 111.1, "description", 100));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member1Name, store1Name, product2_store1, category2_product2_store1, 222.2, "description", 200));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member1Name, store1bName, product1_store1b, category_store1b, 333.3, "description", 300));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member1Name, store1bName, product2_store1b, category_store1b, 444.4, "description", 400));

            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member2Name, store2Name, product1_store2, category_store2, 555.5, "description", 500));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member2Name, store2Name, product2_store2, category_store2, 666.6, "description", 600));
            Assertions.assertThrows(Exception.class, ()->proxy.addNewProductToStock(member2Name, store2Name, product3_store2, category_store2, 777.7, "description", 700));


            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertTrue(proxy.getStoreProducts(member1Name,store1Name).contains(product1_store1));
            Assertions.assertTrue(proxy.getStoreProducts(member1Name,store1Name).contains(product2_store1));
            Assertions.assertTrue(proxy.getStoreProducts(member1Name,store1bName).contains(product1_store1b));
            Assertions.assertTrue(proxy.getStoreProducts(member1Name,store1bName).contains(product1_store1b));

            Assertions.assertTrue(proxy.getStoreProducts(member2Name,store2Name).contains(product1_store2));
            Assertions.assertTrue(proxy.getStoreProducts(member2Name,store2Name).contains(product2_store2));
            Assertions.assertTrue(proxy.getStoreProducts(member2Name,store2Name).contains(product3_store2));

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            ProductDTO productDTO = proxy.getProductInfoFromStore(user4,store1Name ,product1_store1);

            Assertions.assertTrue(productDTO.name.equals(product1_store1));
            Assertions.assertTrue(productDTO.storeName.equals(store1Name));
            Assertions.assertTrue(productDTO.description.equals("description_product1_store1"));
            Assertions.assertTrue(productDTO.price.equals(111.1));
            Assertions.assertTrue(productDTO.categories.contains(category1_product1_store1));

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

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

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            List<ProductDTO> map =  proxy.getProductInfoFromMarketByCategory(user4, category_store2);

            Assertions.assertTrue(map.size()==3);
            for (ProductDTO productDTO1: map){
                if(productDTO1.name.equals(product1_store2)){
                    ProductDTO member2Name_productDTO2 =productDTO1;
                    Assertions.assertTrue(member2Name_productDTO2.name.equals(product1_store2));
                    Assertions.assertTrue(member2Name_productDTO2.storeName.equals(store2Name));
                    Assertions.assertTrue(member2Name_productDTO2.description.equals("description_product1_store2"));
                    Assertions.assertTrue(member2Name_productDTO2.price.equals(555.5));
                    Assertions.assertTrue(member2Name_productDTO2.categories.contains(category_store2));
                }
                else if(productDTO1.name.equals(product2_store2)){
                    Assertions.assertEquals(productDTO1.name, product2_store2);
                    ProductDTO member2Name_productDTO2 =productDTO1;
                    Assertions.assertTrue(member2Name_productDTO2.name.equals(product2_store2));
                    Assertions.assertTrue(member2Name_productDTO2.storeName.equals(store2Name));
                    Assertions.assertTrue(member2Name_productDTO2.description.equals("description_product2_store2"));
                    Assertions.assertTrue(member2Name_productDTO2.price.equals(666.6));
                    Assertions.assertTrue(member2Name_productDTO2.categories.contains(category_store2));
                } else if(productDTO1.name.equals(product3_store2)){
                    Assertions.assertEquals(productDTO1.name, product3_store2);
                    ProductDTO member2Name_productDTO2 =productDTO1;
                    Assertions.assertTrue(member2Name_productDTO2.name.equals(product3_store2));
                    Assertions.assertTrue(member2Name_productDTO2.storeName.equals(store2Name));
                    Assertions.assertTrue(member2Name_productDTO2.description.equals("description_product3_store2"));
                    Assertions.assertTrue(member2Name_productDTO2.price.equals(777.7));
                    Assertions.assertTrue(member2Name_productDTO2.categories.contains(category_store2));
                }
                else Assertions.fail("mistake");
            }

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            map =  proxy.getProductInfoFromMarketByKeyword(user4, "store2");

            Assertions.assertTrue(map.size()==3);
            for (ProductDTO productDTO1: map){
                if(productDTO1.name.equals(product1_store2)){
                    ProductDTO member2Name_productDTO2 =productDTO1;
                    Assertions.assertTrue(member2Name_productDTO2.name.equals(product1_store2));
                    Assertions.assertTrue(member2Name_productDTO2.storeName.equals(store2Name));
                    Assertions.assertTrue(member2Name_productDTO2.description.equals("description_product1_store2"));
                    Assertions.assertTrue(member2Name_productDTO2.price.equals(555.5));
                    Assertions.assertTrue(member2Name_productDTO2.categories.contains(category_store2));
                }
                else if(productDTO1.name.equals(product2_store2)){
                    Assertions.assertEquals(productDTO1.name, product2_store2);
                    ProductDTO member2Name_productDTO2 =productDTO1;
                    Assertions.assertTrue(member2Name_productDTO2.name.equals(product2_store2));
                    Assertions.assertTrue(member2Name_productDTO2.storeName.equals(store2Name));
                    Assertions.assertTrue(member2Name_productDTO2.description.equals("description_product2_store2"));
                    Assertions.assertTrue(member2Name_productDTO2.price.equals(666.6));
                    Assertions.assertTrue(member2Name_productDTO2.categories.contains(category_store2));
                } else if(productDTO1.name.equals(product3_store2)){
                    Assertions.assertEquals(productDTO1.name, product3_store2);
                    ProductDTO member2Name_productDTO2 =productDTO1;
                    Assertions.assertTrue(member2Name_productDTO2.name.equals(product3_store2));
                    Assertions.assertTrue(member2Name_productDTO2.storeName.equals(store2Name));
                    Assertions.assertTrue(member2Name_productDTO2.description.equals("description_product3_store2"));
                    Assertions.assertTrue(member2Name_productDTO2.price.equals(777.7));
                    Assertions.assertTrue(member2Name_productDTO2.categories.contains(category_store2));
                }
                else Assertions.fail("mistake");
            }

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();


            ProductDTO member2Name_productDTO2 = proxy.getProductInfoFromStore(user4,store2Name ,product1_store2);
            Assertions.assertTrue(member2Name_productDTO2.name.equals(product1_store2));
            Assertions.assertTrue(member2Name_productDTO2.storeName.equals(store2Name));
            Assertions.assertTrue(member2Name_productDTO2.description.equals("description_product1_store2"));
            Assertions.assertTrue(member2Name_productDTO2.price.equals(555.5));
            Assertions.assertTrue(member2Name_productDTO2.categories.contains(category_store2));

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();


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

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.getProductInfoFromMarketByName(user4,product1_store1b);
            Assertions.assertTrue(proxy.closeStore(member1Name,store1Name));
            Assertions.assertFalse(proxy.getStoreInfo(member1Name, store1Name).isActive);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertThrows(Exception.class,()->proxy.closeStore(member1Name,store1Name));
            Assertions.assertFalse(proxy.getStoreInfo(member1Name, store1Name).isActive);





        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void register_success2(){
        try{
            register3MembersCreateStoresAndProducts();
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllGuests().size()==1);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllMembers().size()==3);
            Assertions.assertTrue(proxy.getAllMembers().contains(member1Name));
            Assertions.assertTrue(proxy.getAllMembers().contains(member2Name));
            Assertions.assertTrue(proxy.getAllMembers().contains(member3Name));

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllOnlineMembers().size()==3);
            Assertions.assertTrue(proxy.getAllOnlineMembers().contains(member1Name));
            Assertions.assertTrue(proxy.getAllOnlineMembers().contains(member2Name));
            Assertions.assertTrue(proxy.getAllOnlineMembers().contains(member3Name));
            proxy= new ProxyBridge(new RealBridge());
            proxy.loadData();

            Assertions.assertTrue(proxy.getAllMembers().size()==3);
            Assertions.assertTrue(proxy.getAllMembers().contains(member1Name));
            Assertions.assertTrue(proxy.getAllMembers().contains(member2Name));
            Assertions.assertTrue(proxy.getAllMembers().contains(member3Name));

            Assertions.assertTrue(proxy.getAllOnlineMembers().size()==3);
            Assertions.assertTrue(proxy.getAllOnlineMembers().contains(member1Name));
            Assertions.assertTrue(proxy.getAllOnlineMembers().contains(member2Name));
            Assertions.assertTrue(proxy.getAllOnlineMembers().contains(member3Name));


            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllStoresNames().size()==5);

            Assertions.assertTrue(proxy.getAllStoresNames().contains(store1Name));
            Assertions.assertTrue(proxy.getAllStoresNames().contains(store1bName));
            Assertions.assertTrue(proxy.getAllStoresNames().contains(store2Name));
            Assertions.assertTrue(proxy.getAllStoresNames().contains(store2bName));
            Assertions.assertTrue(proxy.getAllStoresNames().contains(store3Name));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertThrows(Exception.class, ()-> proxy.login(user1, member1Name, member1Password));
            Assertions.assertThrows(Exception.class, ()-> proxy.login(user2, member2Name, member2Password));
            Assertions.assertThrows(Exception.class, ()-> proxy.login(user3, member3Name, member3Password));



            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            validateGetStoreInfoForMember1Stores();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            validateGetStoreInfoForMember2Stores();
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();



            getProductsInfoFromStore();
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            getProductsInfoFromStore_1();
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();



            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product1_store1).equals(100));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product2_store1).equals(200));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product1_store1b).equals(300));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product2_store1b).equals(400));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product1_store2).equals(500));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product2_store2).equals(600));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product3_store2).equals(700));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            getProductsInfoFromMarket();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            getProductsInfoFromMarket_1();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            getProductsInfoFromMarketByCategory();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            getProductsInfoFromMarketByCategory_1();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            getProductsInfoFromMarketByKeyWord();


            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            proxy.addToCart(user4, store1Name, product1_store1,10);
            proxy.addToCart(user4, store1Name, product2_store1,20);
            proxy.addToCart(user4, store1bName, product1_store1b,30);
            proxy.addToCart(user4, store1bName, product2_store1b,40);
            proxy.addToCart(user4, store2Name, product1_store2,50);
            proxy.addToCart(user4, store2Name, product2_store2,60);
            proxy.addToCart(user4, store2Name, product3_store2,70);


            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product1_store1).equals(100));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product2_store1).equals(200));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product1_store1b).equals(300));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product2_store1b).equals(400));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product1_store2).equals(500));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product2_store2).equals(600));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product3_store2).equals(700));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.closeStore(member1Name, store1Name);
            proxy.closeStore(member2Name, store2Name);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            validateGetStoreInfoForMember1StoresAfterStoreIsClosed();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            validateGetStoreInfoForMember2StoresAfterCloseStore();



            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.realBridge.systemService.firstManagerInitializer();
            //systemmanager1 , systemmanager1Pass
            proxy.login(user4, "systemmanager1", "systemmanager1Pass");

            proxy.systemManagerCloseStore("systemmanager1", store1bName);



        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void register_success3(){
        try{
            register3MembersCreateStoresAndProducts();
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.removeProductFromStock(member1Name, store1Name, product2_store1);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.removeProductFromStock(member1Name, store1bName, product1_store1b);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.removeProductFromStock(member2Name, store2Name, product3_store2);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product1_store1).equals(100));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertThrows(Exception.class,()-> proxy.getProductAmountInStore(user4, store1Name, product2_store1));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertThrows(Exception.class,()->proxy.getProductAmountInStore(user4, store1bName, product1_store1b));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product2_store1b).equals(400));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product1_store2).equals(500));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product2_store2).equals(600));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertThrows(Exception.class,()->proxy.getProductAmountInStore(user4, store2Name, product3_store2));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            validateGetStoreInfoForMember1StoresAfterRemoveProducts();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            validateGetStoreInfoForMember2StoresAfterRemoveProducts();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            getProductsInfoFromStoreAfterRemoveProduct();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            getProductsInfoFromStoreAfterRemoveProduct_1();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            getProductsInfoFromMarketAfterRemoveProducts();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            getProductsInfoFromMarketAfterRemoveProducts_1();


            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            getProductsInfoFromMarketByCategoryAfterRemoveProduct();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();


            proxy.addToCart(user4, store1Name, product1_store1,10);
            Assertions.assertThrows(Exception.class, ()-> proxy.addToCart(user4, store1Name, product2_store1,20));
            Assertions.assertThrows(Exception.class, ()-> proxy.addToCart(user4, store1bName, product1_store1b,30));
            proxy.addToCart(user4, store1bName, product2_store1b,40);
            proxy.addToCart(user4, store2Name, product1_store2,50);
            proxy.addToCart(user4, store2Name, product2_store2,60);
            Assertions.assertThrows(Exception.class, ()-> proxy.addToCart(user4, store2Name, product3_store2,70));


            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product1_store1).equals(100));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertThrows(Exception.class,()-> proxy.getProductAmountInStore(user4, store1Name, product2_store1));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertThrows(Exception.class,()->proxy.getProductAmountInStore(user4, store1bName, product1_store1b));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product2_store1b).equals(400));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product1_store2).equals(500));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product2_store2).equals(600));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertThrows(Exception.class,()->proxy.getProductAmountInStore(user4, store2Name, product3_store2));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(proxy.getRuleForStore(store1Name, member1Name),1);
            Assertions.assertEquals(proxy.getRuleForStore(store1bName, member1Name),1);
            Assertions.assertEquals(proxy.getRuleForStore(store2Name, member2Name),1);
            Assertions.assertEquals(proxy.getRuleForStore(store2bName, member2Name),1);
            Assertions.assertEquals(proxy.getRuleForStore(store3Name, member3Name),1);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(proxy.getRuleForStore(store1Name, member1Name),1);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(proxy.getRuleForStore(store1bName, member1Name),1);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(proxy.getRuleForStore(store2Name, member2Name),1);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(proxy.getRuleForStore(store2bName, member2Name),1);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(proxy.getRuleForStore(store3Name, member3Name),1);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.realBridge.systemService.firstManagerInitializer();
            //systemmanager1 , systemmanager1Pass
            proxy.login(user4, "systemmanager1", "systemmanager1Pass");
            MemberDTO memberDTO = proxy.getMemberInfo("systemmanager1", member1Name);
            Assertions.assertTrue(memberDTO.username.equals(member1Name));
            Assertions.assertTrue(memberDTO.memberStores.keySet().size()==1);
            Assertions.assertTrue(memberDTO.memberStores.keySet().stream().toList().get(0).equals("StoreFounder"));
            Assertions.assertTrue(memberDTO.memberStores.values().stream().toList().get(0).size()==2);

            memberDTO = proxy.getMemberInfo("systemmanager1", member2Name);
            Assertions.assertTrue(memberDTO.username.equals(member2Name));

            memberDTO = proxy.getMemberInfo("systemmanager1", member3Name);
            Assertions.assertTrue(memberDTO.username.equals(member3Name));


            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Map<String,List<StoreDTO>> map = proxy.myStores(member1Name);
            Assertions.assertTrue(map.keySet().size()==1);
            Assertions.assertTrue(map.keySet().stream().toList().get(0).equals("StoreFounder"));
            Assertions.assertTrue(map.values().stream().toList().get(0).size()==2);

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void register_success4(){
        try{
            register3MembersCreateStoresAndProducts();
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.updateProductDescription(member1Name, store1Name, product2_store1,"new_description_product2_store1");
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.updateProductDescription(member1Name, store1bName, product1_store1b,"new_description_product1_store1b");
            proxy.updateProductDescription(member2Name, store2Name, product3_store2,"new_description_product3_store2");
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.updateProductPrice(member1Name, store1Name, product1_store1,987.1);
            proxy.updateProductPrice(member1Name, store1bName, product2_store1b,987.2);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.updateProductPrice(member2Name, store2Name, product2_store2, 987.3);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product1_store1).equals(100));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product2_store1).equals(200));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product1_store1b).equals(300));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product2_store1b).equals(400));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product1_store2).equals(500));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product2_store2).equals(600));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product3_store2).equals(700));

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            validateGetStoreInfoForMember1StoresAfterUpdateProduct();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            validateGetStoreInfoForMember2StoresAfterUpdateProduct();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            getProductsInfoFromStoreAfterUpdateProduct();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            getProductsInfoFromStoreAfterUpdateProduct_1();
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            getProductsInfoFromMarketAfterUpdateProduct();
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            getProductsInfoFromMarketAfterUpdateProduct_1();
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            getProductsInfoFromMarketByCategoryAfterUpdateProduct();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            getProductsInfoFromMarketByCategoryAfterUpdateProduct_1();
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            proxy.addToCart(user4, store1Name, product1_store1,10);
            proxy.addToCart(user4, store1Name, product2_store1,20);
            proxy.addToCart(user4, store1bName, product1_store1b,30);
            proxy.addToCart(user4, store1bName, product2_store1b,40);
            proxy.addToCart(user4, store2Name, product1_store2,50);
            proxy.addToCart(user4, store2Name, product2_store2,60);
            proxy.addToCart(user4, store2Name, product3_store2,70);


            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product1_store1).equals(100));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product2_store1).equals(200));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product1_store1b).equals(300));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product2_store1b).equals(400));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product1_store2).equals(500));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product2_store2).equals(600));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product3_store2).equals(700));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.updateProductAmount(member1Name, store1Name, product2_store1, 654);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.updateProductAmount(member1Name, store1bName, product1_store1b, 999);
            proxy.updateProductAmount(member2Name, store2Name, product1_store2, 333);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.updateProductAmount(member2Name, store2Name, product2_store2, 555);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();


            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product1_store1).equals(100));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product2_store1).equals(654));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product1_store1b).equals(999));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product2_store1b).equals(400));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product1_store2).equals(333));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product2_store2).equals(555));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product3_store2).equals(700));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();


            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product1_store1).equals(100));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product2_store1).equals(654));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product1_store1b).equals(999));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product2_store1b).equals(400));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product1_store2).equals(333));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product2_store2).equals(555));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product3_store2).equals(700));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            validateGetStoreInfoForMember1StoresAfterUpdateProductAmount();
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            validateGetStoreInfoForMember2StoresAfterUpdateProductAmount();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            getProductsInfoFromStoreAfterUpdateProduct();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            getProductsInfoFromStoreAfterUpdateProduct_1();
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            getProductsInfoFromMarketAfterUpdateProduct();
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            getProductsInfoFromMarketAfterUpdateProduct_1();
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            getProductsInfoFromMarketByCategoryAfterUpdateProduct();

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            getProductsInfoFromMarketByCategoryAfterUpdateProduct_1();
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            proxy.addToCart(user4, store1Name, product1_store1,100);
            proxy.addToCart(user4, store1Name, product2_store1,654);
            proxy.addToCart(user4, store1bName, product1_store1b,999);
            proxy.addToCart(user4, store1bName, product2_store1b,400);
            proxy.addToCart(user4, store2Name, product1_store2,333);
            proxy.addToCart(user4, store2Name, product2_store2,555);
            proxy.addToCart(user4, store2Name, product3_store2,700);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product1_store1).equals(100));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product2_store1).equals(654));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product1_store1b).equals(999));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product2_store1b).equals(400));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product1_store2).equals(333));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product2_store2).equals(555));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product3_store2).equals(700));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            proxy.addToCart(user4, store1Name, product1_store1,100);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.addToCart(user4, store1Name, product2_store1,654);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.addToCart(user4, store1bName, product1_store1b,999);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.addToCart(user4, store1bName, product2_store1b,400);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.addToCart(user4, store2Name, product1_store2,333);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.addToCart(user4, store2Name, product2_store2,555);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.addToCart(user4, store2Name, product3_store2,700);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(proxy.getRuleForStore(store1Name, member1Name),1);
            Assertions.assertEquals(proxy.getRuleForStore(store1bName, member1Name),1);
            Assertions.assertEquals(proxy.getRuleForStore(store2Name, member2Name),1);
            Assertions.assertEquals(proxy.getRuleForStore(store2bName, member2Name),1);
            Assertions.assertEquals(proxy.getRuleForStore(store3Name, member3Name),1);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(proxy.getRuleForStore(store1Name, member1Name),1);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(proxy.getRuleForStore(store1bName, member1Name),1);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(proxy.getRuleForStore(store2Name, member2Name),1);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(proxy.getRuleForStore(store2bName, member2Name),1);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertEquals(proxy.getRuleForStore(store3Name, member3Name),1);
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            proxy.realBridge.systemService.firstManagerInitializer();
            //systemmanager1 , systemmanager1Pass
            proxy.login(user4, "systemmanager1", "systemmanager1Pass");
            MemberDTO memberDTO = proxy.getMemberInfo("systemmanager1", member1Name);
            Assertions.assertTrue(memberDTO.username.equals(member1Name));
            Assertions.assertTrue(memberDTO.memberStores.keySet().size()==1);
            Assertions.assertTrue(memberDTO.memberStores.keySet().stream().toList().get(0).equals("StoreFounder"));
            Assertions.assertTrue(memberDTO.memberStores.values().stream().toList().get(0).size()==2);

            memberDTO = proxy.getMemberInfo("systemmanager1", member2Name);
            Assertions.assertTrue(memberDTO.username.equals(member2Name));

            memberDTO = proxy.getMemberInfo("systemmanager1", member3Name);
            Assertions.assertTrue(memberDTO.username.equals(member3Name));


            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Map<String,List<StoreDTO>> map = proxy.myStores(member1Name);
            Assertions.assertTrue(map.keySet().size()==1);
            Assertions.assertTrue(map.keySet().stream().toList().get(0).equals("StoreFounder"));
            Assertions.assertTrue(map.values().stream().toList().get(0).size()==2);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            ////////////////////


            proxy.addToCart(user4, store1Name, product1_store1,10);
            proxy.addToCart(user4, store1Name, product2_store1,10);
            proxy.addToCart(user4, store1bName, product1_store1b,10);
            proxy.addToCart(user4, store1bName, product2_store1b,10);
            proxy.addToCart(user4, store2Name, product1_store2,10);
            proxy.addToCart(user4, store2Name, product2_store2,10);
            proxy.addToCart(user4, store2Name, product3_store2,10);
            proxy.purchaseCartByCreditCard(user4, "213242345","06", "2028",user4,"364","396183642", user4,"country", "address", "city", "345");

            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product1_store1).equals(90));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product2_store1).equals(644));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product1_store1b).equals(989));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product2_store1b).equals(390));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product1_store2).equals(323));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product2_store2).equals(545));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product3_store2).equals(690));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();


            proxy.addToCart(user4, store1Name, product1_store1,10);
            proxy.addToCart(user4, store1Name, product2_store1,10);
            proxy.addToCart(user4, store1bName, product1_store1b,10);
            proxy.addToCart(user4, store1bName, product2_store1b,10);
            proxy.addToCart(user4, store2Name, product1_store2,10);
            proxy.addToCart(user4, store2Name, product2_store2,10);
            proxy.addToCart(user4, store2Name, product3_store2,10);
            proxy.purchaseCartByCreditCard(user4, "213242345","06", "2028",user4,"364","396183642", user4,"country", "address", "city", "345");
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();



            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product1_store1).equals(80));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product2_store1).equals(634));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product1_store1b).equals(979));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product2_store1b).equals(380));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product1_store2).equals(313));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product2_store2).equals(535));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product3_store2).equals(680));


            proxy.addToCart(member2Name, store1Name, product1_store1,10);
            proxy.addToCart(member2Name, store1Name, product2_store1,10);
            proxy.addToCart(member2Name, store1bName, product1_store1b,10);
            proxy.addToCart(member2Name, store1bName, product2_store1b,10);
            proxy.addToCart(member2Name, store2Name, product1_store2,10);
            proxy.addToCart(member2Name, store2Name, product2_store2,10);
            proxy.addToCart(member2Name, store2Name, product3_store2,10);
            proxy.purchaseCartByCreditCard(member2Name, "213242345","06", "2028",user4,"364","396183642", user4,"country", "address", "city", "345");

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product1_store1).equals(70));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product2_store1).equals(624));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product1_store1b).equals(969));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product2_store1b).equals(370));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product1_store2).equals(303));
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product2_store2).equals(525));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product3_store2).equals(670));
            proxy.addToCart(member2Name, store1Name, product1_store1,10);
            proxy.addToCart(member2Name, store1Name, product2_store1,10);
            proxy.addToCart(member2Name, store1bName, product1_store1b,10);
            proxy.addToCart(member2Name, store1bName, product2_store1b,10);
            proxy.addToCart(member2Name, store2Name, product1_store2,10);
            proxy.addToCart(member2Name, store2Name, product2_store2,10);
            proxy.addToCart(member2Name, store2Name, product3_store2,10);


            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            proxy.purchaseCartByCreditCard(member2Name, "213242345","06", "2028",user4,"364","396183642", user4,"country", "address", "city", "345");
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product1_store1).equals(60));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1Name, product2_store1).equals(614));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product1_store1b).equals(959));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store1bName, product2_store1b).equals(360));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product1_store2).equals(293));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product2_store2).equals(515));
            Assertions.assertTrue(proxy.getProductAmountInStore(user4, store2Name, product3_store2).equals(660));


        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


    private void register3MembersCreateStoresAndProducts() throws Exception {
        proxy.register(user1, member1Name, member1Password);
        proxy.register(user2, member2Name, member2Password);
        proxy.register(user3, member3Name, member3Password);
        user1 = proxy.login(user1, member1Name, member1Password);
        user2 = proxy.login(user2, member2Name, member2Password);
        user3 =proxy.login(user3, member3Name, member3Password);

        proxy.createStore(member1Name, store1Name);
        proxy.createStore(member1Name, store1bName);

        proxy.createStore(member2Name, store2Name);
        proxy.createStore(member2Name, store2bName);
        proxy.createStore(member3Name, store3Name);


        proxy.addNewProductToStock(member1Name, store1Name, product1_store1, category1_product1_store1, 111.1, "description_product1_store1", 100);
        proxy.addNewProductToStock(member1Name, store1Name, product2_store1, category2_product2_store1, 222.2, "description_product2_store1", 200);
        proxy.addNewProductToStock(member1Name, store1bName, product1_store1b, category_store1b, 333.3, "description_product1_store1b", 300);
        proxy.addNewProductToStock(member1Name, store1bName, product2_store1b, category_store1b, 444.4, "description_product2_store1b", 400);

        proxy.addNewProductToStock(member2Name, store2Name, product1_store2, category_store2, 555.5, "description_product1_store2", 500);
        proxy.addNewProductToStock(member2Name, store2Name, product2_store2, category_store2, 666.6, "description_product2_store2", 600);
        proxy.addNewProductToStock(member2Name, store2Name, product3_store2, category_store2, 777.7, "description_product3_store2", 700);

    }

    private void validateGetStoreInfoForMember2StoresAfterUpdateProductAmount() throws Exception {

        StoreDTO storeDTO2 = proxy.getStoreInfo(member2Name,store2Name);
        Assertions.assertTrue(storeDTO2.storeName.equals(store2Name));
        Assertions.assertTrue(storeDTO2.founderName.equals(member2Name));
        Assertions.assertTrue(storeDTO2.ownersNames.isEmpty());
        Assertions.assertTrue(storeDTO2.managersNames.isEmpty());
        Assertions.assertTrue(storeDTO2.isActive);
        Assertions.assertTrue(storeDTO2.productsInfoAmount.keySet().size()==3);
        Assertions.assertTrue(storeDTO2.productsInfoAmount.values().contains(333) && storeDTO2.productsInfoAmount.values().contains(555) && storeDTO2.productsInfoAmount.values().contains(700));
        ProductDTO productDTO1=null;
        ProductDTO productDTO2=null;
        ProductDTO productDTO3=null;
        for(ProductDTO productDTOTemp: storeDTO2.productsInfoAmount.keySet()){

            if(productDTOTemp.name.equals(product1_store2))
                productDTO1=productDTOTemp;

            if(productDTOTemp.name.equals(product2_store2))
                productDTO2=productDTOTemp;

            if(productDTOTemp.name.equals(product3_store2))
                productDTO3=productDTOTemp;
        }
        Assertions.assertTrue(productDTO1 != null);
        Assertions.assertTrue(productDTO2 != null);
        Assertions.assertTrue(productDTO3 != null);


        Assertions.assertTrue(productDTO1.name.equals(product1_store2));
        Assertions.assertTrue(productDTO1.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO1.price.equals(555.5));
        Assertions.assertTrue(productDTO1.description.equals("description_product1_store2"));
        Assertions.assertTrue(productDTO1.categories.contains(category_store2));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);


        Assertions.assertTrue(productDTO2.name.equals(product2_store2));
        Assertions.assertTrue(productDTO2.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO2.price.equals(987.3));
        Assertions.assertTrue(productDTO2.description.equals("description_product2_store2"));
        Assertions.assertTrue(productDTO2.categories.contains(category_store2));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);


        Assertions.assertTrue(productDTO3.name.equals(product3_store2));
        Assertions.assertTrue(productDTO3.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO3.price.equals(777.7));
        Assertions.assertTrue(productDTO3.description.equals("new_description_product3_store2"));
        Assertions.assertTrue(productDTO3.categories.contains(category_store2));
        Assertions.assertTrue(productDTO3.productDiscountPolicies.size()==0);


    }

    private void validateGetStoreInfoForMember1StoresAfterUpdateProductAmount() throws Exception {

        StoreDTO storeDTO1 = proxy.getStoreInfo(member1Name,store1Name);
        Assertions.assertTrue(storeDTO1.storeName.equals(store1Name));
        Assertions.assertTrue(storeDTO1.founderName.equals(member1Name));
        Assertions.assertTrue(storeDTO1.ownersNames.isEmpty());
        Assertions.assertTrue(storeDTO1.managersNames.isEmpty());
        Assertions.assertTrue(storeDTO1.isActive);
        Assertions.assertTrue(storeDTO1.productsInfoAmount.keySet().size()==2);
        Assertions.assertTrue(storeDTO1.productsInfoAmount.values().contains(100) && storeDTO1.productsInfoAmount.values().contains(654));
        ProductDTO productDTO1=null;
        ProductDTO productDTO2=null;
        for(ProductDTO productDTOTemp: storeDTO1.productsInfoAmount.keySet()){

            if(productDTOTemp.name.equals(product1_store1))
                productDTO1=productDTOTemp;

            if(productDTOTemp.name.equals(product2_store1))
                productDTO2=productDTOTemp;
        }
        Assertions.assertTrue(productDTO1 != null);
        Assertions.assertTrue(productDTO2 != null);

        Assertions.assertTrue(productDTO1.name.equals(product1_store1));
        Assertions.assertTrue(productDTO1.storeName.equals(store1Name));
        Assertions.assertTrue(productDTO1.price.equals(987.1));
        Assertions.assertTrue(productDTO1.description.equals("description_product1_store1"));
        Assertions.assertTrue(productDTO1.categories.contains(category1_product1_store1));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);


        Assertions.assertTrue(productDTO2.name.equals(product2_store1));
        Assertions.assertTrue(productDTO2.storeName.equals(store1Name));
        Assertions.assertTrue(productDTO2.price.equals(222.2));
        Assertions.assertTrue(productDTO2.description.equals("new_description_product2_store1"));
        Assertions.assertTrue(productDTO2.categories.contains(category2_product2_store1));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);



        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();


        StoreDTO storeDTO1b = proxy.getStoreInfo(member1Name,store1bName);
        Assertions.assertTrue(storeDTO1b.storeName.equals(store1bName));
        Assertions.assertTrue(storeDTO1b.founderName.equals(member1Name));
        Assertions.assertTrue(storeDTO1b.ownersNames.isEmpty());
        Assertions.assertTrue(storeDTO1b.managersNames.isEmpty());
        Assertions.assertTrue(storeDTO1b.isActive);
        Assertions.assertTrue(storeDTO1b.productsInfoAmount.keySet().size()==2);
        Assertions.assertTrue(storeDTO1b.productsInfoAmount.values().contains(999) && storeDTO1b.productsInfoAmount.values().contains(400));
        productDTO1=null;
        productDTO2=null;


        for(ProductDTO productDTOTemp: storeDTO1b.productsInfoAmount.keySet()){

            if(productDTOTemp.name.equals(product1_store1b))
                productDTO1=productDTOTemp;

            if(productDTOTemp.name.equals(product2_store1b))
                productDTO2=productDTOTemp;
        }

        Assertions.assertTrue(productDTO1 != null);
        Assertions.assertTrue(productDTO2 != null);



        Assertions.assertTrue(productDTO1.name.equals(product1_store1b));
        Assertions.assertTrue(productDTO1.storeName.equals(store1bName));
        Assertions.assertTrue(productDTO1.price.equals(333.3));
        Assertions.assertTrue(productDTO1.description.equals("new_description_product1_store1b"));
        Assertions.assertTrue(productDTO1.categories.contains(category_store1b));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);


        Assertions.assertTrue(productDTO2.name.equals(product2_store1b));
        Assertions.assertTrue(productDTO2.storeName.equals(store1bName));
        Assertions.assertTrue(productDTO2.price.equals(987.2));
        Assertions.assertTrue(productDTO2.description.equals("description_product2_store1b"));
        Assertions.assertTrue(productDTO2.categories.contains(category_store1b));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);

    }

    private void getProductsInfoFromMarketByCategoryAfterUpdateProduct_1() throws Exception {
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store1AfterUpdateProduct(proxy.getProductInfoFromMarketByCategory(user4, category1_product1_store1).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store1AfterUpdateProduct(proxy.getProductInfoFromMarketByCategory(user4, category2_product2_store1).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        List<ProductDTO> store1bProducts = proxy.getProductInfoFromMarketByCategory(user4, category_store1b);
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        ProductDTO product1store1b = null;
        ProductDTO product2store1b = null;
        for(ProductDTO productDTO: store1bProducts){
            if (productDTO.name.equals(this.product1_store1b))
                product1store1b=productDTO;

            if (productDTO.name.equals(this.product2_store1b))
                product2store1b=productDTO;
        }
        Assertions.assertTrue(product1store1b!=null);
        Assertions.assertTrue(product2store1b!=null);
        validateProductIsProduct1_store1bAfterUpdateProduct(product1store1b);
        validateProductIsProduct2_store1bAfterUpdateProduct(product2store1b);


        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();

        List<ProductDTO> store2Products = proxy.getProductInfoFromMarketByCategory(user4, category_store2);
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        ProductDTO product1store2 = null;
        ProductDTO product2store2 = null;
        ProductDTO product3store2 = null;


        for(ProductDTO productDTO: store2Products){
            if (productDTO.name.equals(this.product1_store2))
                product1store2=productDTO;

            if (productDTO.name.equals(this.product2_store2))
                product2store2=productDTO;

            if (productDTO.name.equals(this.product3_store2))
                product3store2=productDTO;
        }

        Assertions.assertTrue(product1store2!=null);
        Assertions.assertTrue(product2store2!=null);
        Assertions.assertTrue(product3store2!=null);
        validateProductIsProduct1_store2(product1store2);
        validateProductIsProduct2_store2AfterUpdateProduct(product2store2);
        validateProductIsProduct3_store2AfterUpdateProduct(product3store2);

    }

    private void getProductsInfoFromMarketByCategoryAfterUpdateProduct() throws Exception {
        validateProductIsProduct1_store1AfterUpdateProduct(proxy.getProductInfoFromMarketByCategory(user4, category1_product1_store1).get(0));
        validateProductIsProduct2_store1AfterUpdateProduct(proxy.getProductInfoFromMarketByCategory(user4, category2_product2_store1).get(0));
        List<ProductDTO> store1bProducts = proxy.getProductInfoFromMarketByCategory(user4, category_store1b);
        ProductDTO product1store1b = null;
        ProductDTO product2store1b = null;
        for(ProductDTO productDTO: store1bProducts){
            if (productDTO.name.equals(this.product1_store1b))
                product1store1b=productDTO;

            if (productDTO.name.equals(this.product2_store1b))
                product2store1b=productDTO;
        }
        Assertions.assertTrue(product1store1b!=null);
        Assertions.assertTrue(product2store1b!=null);
        validateProductIsProduct1_store1bAfterUpdateProduct(product1store1b);
        validateProductIsProduct2_store1bAfterUpdateProduct(product2store1b);




        List<ProductDTO> store2Products = proxy.getProductInfoFromMarketByCategory(user4, category_store2);
        ProductDTO product1store2 = null;
        ProductDTO product2store2 = null;
        ProductDTO product3store2 = null;


        for(ProductDTO productDTO: store2Products){
            if (productDTO.name.equals(this.product1_store2))
                product1store2=productDTO;

            if (productDTO.name.equals(this.product2_store2))
                product2store2=productDTO;

            if (productDTO.name.equals(this.product3_store2))
                product3store2=productDTO;
        }

        Assertions.assertTrue(product1store2!=null);
        Assertions.assertTrue(product2store2!=null);
        Assertions.assertTrue(product3store2!=null);
        validateProductIsProduct1_store2(product1store2);
        validateProductIsProduct2_store2AfterUpdateProduct(product2store2);
        validateProductIsProduct3_store2AfterUpdateProduct(product3store2);

    }

    private void getProductsInfoFromMarketAfterUpdateProduct_1() throws Exception {
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store1AfterUpdateProduct(proxy.getProductInfoFromMarketByName(user4, product1_store1).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store1AfterUpdateProduct(proxy.getProductInfoFromMarketByName(user4, product2_store1).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store1bAfterUpdateProduct(proxy.getProductInfoFromMarketByName(user4, product1_store1b).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store1bAfterUpdateProduct(proxy.getProductInfoFromMarketByName(user4, product2_store1b).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store2(proxy.getProductInfoFromMarketByName(user4, product1_store2).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store2AfterUpdateProduct(proxy.getProductInfoFromMarketByName(user4, product2_store2).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct3_store2AfterUpdateProduct(proxy.getProductInfoFromMarketByName(user4, product3_store2).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();

    }
    private void getProductsInfoFromMarketAfterUpdateProduct() throws Exception {
        validateProductIsProduct1_store1AfterUpdateProduct(proxy.getProductInfoFromMarketByName(user4, product1_store1).get(0));
        validateProductIsProduct2_store1AfterUpdateProduct(proxy.getProductInfoFromMarketByName(user4, product2_store1).get(0));
        validateProductIsProduct1_store1bAfterUpdateProduct(proxy.getProductInfoFromMarketByName(user4, product1_store1b).get(0));
        validateProductIsProduct2_store1bAfterUpdateProduct(proxy.getProductInfoFromMarketByName(user4, product2_store1b).get(0));
        validateProductIsProduct1_store2(proxy.getProductInfoFromMarketByName(user4, product1_store2).get(0));
        validateProductIsProduct2_store2AfterUpdateProduct(proxy.getProductInfoFromMarketByName(user4, product2_store2).get(0));
        validateProductIsProduct3_store2AfterUpdateProduct(proxy.getProductInfoFromMarketByName(user4, product3_store2).get(0));

    }

    private void getProductsInfoFromStoreAfterUpdateProduct_1() throws Exception {
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store1AfterUpdateProduct(proxy.getProductInfoFromStore(user4, store1Name, product1_store1));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store1AfterUpdateProduct(proxy.getProductInfoFromStore(user4, store1Name, product2_store1));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store1bAfterUpdateProduct(proxy.getProductInfoFromStore(user4, store1bName, product1_store1b));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store1bAfterUpdateProduct(proxy.getProductInfoFromStore(user4, store1bName, product2_store1b));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store2(proxy.getProductInfoFromStore(user4, store2Name, product1_store2));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store2AfterUpdateProduct(proxy.getProductInfoFromStore(user4, store2Name, product2_store2));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct3_store2AfterUpdateProduct(proxy.getProductInfoFromStore(user4, store2Name, product3_store2));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();

    }

    private void getProductsInfoFromStoreAfterUpdateProduct() throws Exception {
        validateProductIsProduct1_store1AfterUpdateProduct(proxy.getProductInfoFromStore(user4, store1Name, product1_store1));
        validateProductIsProduct2_store1AfterUpdateProduct(proxy.getProductInfoFromStore(user4, store1Name, product2_store1));
        validateProductIsProduct1_store1bAfterUpdateProduct(proxy.getProductInfoFromStore(user4, store1bName, product1_store1b));
        validateProductIsProduct2_store1bAfterUpdateProduct(proxy.getProductInfoFromStore(user4, store1bName, product2_store1b));
        validateProductIsProduct1_store2(proxy.getProductInfoFromStore(user4, store2Name, product1_store2));
        validateProductIsProduct2_store2AfterUpdateProduct(proxy.getProductInfoFromStore(user4, store2Name, product2_store2));
        validateProductIsProduct3_store2AfterUpdateProduct(proxy.getProductInfoFromStore(user4, store2Name, product3_store2));

    }

    private void validateGetStoreInfoForMember2StoresAfterUpdateProduct() throws Exception {

        StoreDTO storeDTO2 = proxy.getStoreInfo(member2Name,store2Name);
        Assertions.assertTrue(storeDTO2.storeName.equals(store2Name));
        Assertions.assertTrue(storeDTO2.founderName.equals(member2Name));
        Assertions.assertTrue(storeDTO2.ownersNames.isEmpty());
        Assertions.assertTrue(storeDTO2.managersNames.isEmpty());
        Assertions.assertTrue(storeDTO2.isActive);
        Assertions.assertTrue(storeDTO2.productsInfoAmount.keySet().size()==3);
        Assertions.assertTrue(storeDTO2.productsInfoAmount.values().contains(500) && storeDTO2.productsInfoAmount.values().contains(600) && storeDTO2.productsInfoAmount.values().contains(700));
        ProductDTO productDTO1=null;
        ProductDTO productDTO2=null;
        ProductDTO productDTO3=null;
        for(ProductDTO productDTOTemp: storeDTO2.productsInfoAmount.keySet()){

            if(productDTOTemp.name.equals(product1_store2))
                productDTO1=productDTOTemp;

            if(productDTOTemp.name.equals(product2_store2))
                productDTO2=productDTOTemp;

            if(productDTOTemp.name.equals(product3_store2))
                productDTO3=productDTOTemp;
        }
        Assertions.assertTrue(productDTO1 != null);
        Assertions.assertTrue(productDTO2 != null);
        Assertions.assertTrue(productDTO3 != null);


        Assertions.assertTrue(productDTO1.name.equals(product1_store2));
        Assertions.assertTrue(productDTO1.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO1.price.equals(555.5));
        Assertions.assertTrue(productDTO1.description.equals("description_product1_store2"));
        Assertions.assertTrue(productDTO1.categories.contains(category_store2));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);


        Assertions.assertTrue(productDTO2.name.equals(product2_store2));
        Assertions.assertTrue(productDTO2.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO2.price.equals(987.3));
        Assertions.assertTrue(productDTO2.description.equals("description_product2_store2"));
        Assertions.assertTrue(productDTO2.categories.contains(category_store2));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);


        Assertions.assertTrue(productDTO3.name.equals(product3_store2));
        Assertions.assertTrue(productDTO3.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO3.price.equals(777.7));
        Assertions.assertTrue(productDTO3.description.equals("new_description_product3_store2"));
        Assertions.assertTrue(productDTO3.categories.contains(category_store2));
        Assertions.assertTrue(productDTO3.productDiscountPolicies.size()==0);


    }

    private void validateGetStoreInfoForMember1StoresAfterUpdateProduct() throws Exception {

        StoreDTO storeDTO1 = proxy.getStoreInfo(member1Name,store1Name);
        Assertions.assertTrue(storeDTO1.storeName.equals(store1Name));
        Assertions.assertTrue(storeDTO1.founderName.equals(member1Name));
        Assertions.assertTrue(storeDTO1.ownersNames.isEmpty());
        Assertions.assertTrue(storeDTO1.managersNames.isEmpty());
        Assertions.assertTrue(storeDTO1.isActive);
        Assertions.assertTrue(storeDTO1.productsInfoAmount.keySet().size()==2);
        Assertions.assertTrue(storeDTO1.productsInfoAmount.values().contains(100) && storeDTO1.productsInfoAmount.values().contains(200));
        ProductDTO productDTO1=null;
        ProductDTO productDTO2=null;
        for(ProductDTO productDTOTemp: storeDTO1.productsInfoAmount.keySet()){

            if(productDTOTemp.name.equals(product1_store1))
                productDTO1=productDTOTemp;

            if(productDTOTemp.name.equals(product2_store1))
                productDTO2=productDTOTemp;
        }
        Assertions.assertTrue(productDTO1 != null);
        Assertions.assertTrue(productDTO2 != null);

        Assertions.assertTrue(productDTO1.name.equals(product1_store1));
        Assertions.assertTrue(productDTO1.storeName.equals(store1Name));
        Assertions.assertTrue(productDTO1.price.equals(987.1));
        Assertions.assertTrue(productDTO1.description.equals("description_product1_store1"));
        Assertions.assertTrue(productDTO1.categories.contains(category1_product1_store1));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);


        Assertions.assertTrue(productDTO2.name.equals(product2_store1));
        Assertions.assertTrue(productDTO2.storeName.equals(store1Name));
        Assertions.assertTrue(productDTO2.price.equals(222.2));
        Assertions.assertTrue(productDTO2.description.equals("new_description_product2_store1"));
        Assertions.assertTrue(productDTO2.categories.contains(category2_product2_store1));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);



        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();


        StoreDTO storeDTO1b = proxy.getStoreInfo(member1Name,store1bName);
        Assertions.assertTrue(storeDTO1b.storeName.equals(store1bName));
        Assertions.assertTrue(storeDTO1b.founderName.equals(member1Name));
        Assertions.assertTrue(storeDTO1b.ownersNames.isEmpty());
        Assertions.assertTrue(storeDTO1b.managersNames.isEmpty());
        Assertions.assertTrue(storeDTO1b.isActive);
        Assertions.assertTrue(storeDTO1b.productsInfoAmount.keySet().size()==2);
        Assertions.assertTrue(storeDTO1b.productsInfoAmount.values().contains(300) && storeDTO1b.productsInfoAmount.values().contains(400));
        productDTO1=null;
        productDTO2=null;


        for(ProductDTO productDTOTemp: storeDTO1b.productsInfoAmount.keySet()){

            if(productDTOTemp.name.equals(product1_store1b))
                productDTO1=productDTOTemp;

            if(productDTOTemp.name.equals(product2_store1b))
                productDTO2=productDTOTemp;
        }

        Assertions.assertTrue(productDTO1 != null);
        Assertions.assertTrue(productDTO2 != null);



        Assertions.assertTrue(productDTO1.name.equals(product1_store1b));
        Assertions.assertTrue(productDTO1.storeName.equals(store1bName));
        Assertions.assertTrue(productDTO1.price.equals(333.3));
        Assertions.assertTrue(productDTO1.description.equals("new_description_product1_store1b"));
        Assertions.assertTrue(productDTO1.categories.contains(category_store1b));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);


        Assertions.assertTrue(productDTO2.name.equals(product2_store1b));
        Assertions.assertTrue(productDTO2.storeName.equals(store1bName));
        Assertions.assertTrue(productDTO2.price.equals(987.2));
        Assertions.assertTrue(productDTO2.description.equals("description_product2_store1b"));
        Assertions.assertTrue(productDTO2.categories.contains(category_store1b));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);

    }

    private void getProductsInfoFromMarketByCategoryAfterRemoveProduct() throws Exception {
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store1(proxy.getProductInfoFromMarketByCategory(user4, category1_product1_store1).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        Assertions.assertTrue(proxy.getProductInfoFromMarketByCategory(user4, category2_product2_store1).size()==0);
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        List<ProductDTO> store1bProducts = proxy.getProductInfoFromMarketByCategory(user4, category_store1b);
        Assertions.assertTrue(store1bProducts.size()==1);
        ProductDTO product2store1b = store1bProducts.get(0);
        validateProductIsProduct2_store1b(product2store1b);

        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        List<ProductDTO> store2Products = proxy.getProductInfoFromMarketByCategory(user4, category_store2);
        Assertions.assertTrue(store2Products.size()==2);
        ProductDTO product1store2 = null;
        ProductDTO product2store2 = null;


        for(ProductDTO productDTO: store2Products){
            if (productDTO.name.equals(this.product1_store2))
                product1store2=productDTO;

            if (productDTO.name.equals(this.product2_store2))
                product2store2=productDTO;

        }

        Assertions.assertTrue(product1store2!=null);
        Assertions.assertTrue(product2store2!=null);
        validateProductIsProduct1_store2(product1store2);
        validateProductIsProduct2_store2(product2store2);
    }

    private void getProductsInfoFromMarketAfterRemoveProducts() throws Exception {
        validateProductIsProduct1_store1(proxy.getProductInfoFromMarketByName(user4, product1_store1).get(0));
        Assertions.assertTrue(proxy.getProductInfoFromMarketByName(user4, product2_store1).size()==0);
        Assertions.assertTrue(proxy.getProductInfoFromMarketByName(user4, product1_store1b).size()==0);
        validateProductIsProduct2_store1b(proxy.getProductInfoFromMarketByName(user4, product2_store1b).get(0));
        validateProductIsProduct1_store2(proxy.getProductInfoFromMarketByName(user4, product1_store2).get(0));
        validateProductIsProduct2_store2(proxy.getProductInfoFromMarketByName(user4, product2_store2).get(0));
        Assertions.assertTrue(proxy.getProductInfoFromMarketByName(user4, product3_store2).size()==0);

    }

    private void getProductsInfoFromMarketAfterRemoveProducts_1() throws Exception {
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store1(proxy.getProductInfoFromMarketByName(user4, product1_store1).get(0));

        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        Assertions.assertTrue(proxy.getProductInfoFromMarketByName(user4, product2_store1).size()==0);
        Assertions.assertTrue(proxy.getProductInfoFromMarketByName(user4, product1_store1b).size()==0);

        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        Assertions.assertTrue(proxy.getProductInfoFromMarketByName(user4, product1_store1b).size()==0);

        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store1b(proxy.getProductInfoFromMarketByName(user4, product2_store1b).get(0));

        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store2(proxy.getProductInfoFromMarketByName(user4, product1_store2).get(0));

        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store2(proxy.getProductInfoFromMarketByName(user4, product2_store2).get(0));

        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        Assertions.assertTrue(proxy.getProductInfoFromMarketByName(user4, product3_store2).size()==0);
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();

    }

    private void getProductsInfoFromStoreAfterRemoveProduct() throws Exception {
        validateProductIsProduct1_store1(proxy.getProductInfoFromStore(user4, store1Name, product1_store1));
        Assertions.assertThrows(Exception.class, ()-> proxy.getProductInfoFromStore(user4, store1Name, product2_store1));
        Assertions.assertThrows(Exception.class, ()-> proxy.getProductInfoFromStore(user4, store1bName, product1_store1b));
        validateProductIsProduct2_store1b(proxy.getProductInfoFromStore(user4, store1bName, product2_store1b));
        validateProductIsProduct1_store2(proxy.getProductInfoFromStore(user4, store2Name, product1_store2));
        validateProductIsProduct2_store2(proxy.getProductInfoFromStore(user4, store2Name, product2_store2));
        Assertions.assertThrows(Exception.class, ()-> proxy.getProductInfoFromStore(user4, store2Name, product3_store2));

    }

    private void getProductsInfoFromStoreAfterRemoveProduct_1() throws Exception {
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store1(proxy.getProductInfoFromStore(user4, store1Name, product1_store1));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        Assertions.assertThrows(Exception.class, ()-> proxy.getProductInfoFromStore(user4, store1Name, product2_store1));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        Assertions.assertThrows(Exception.class, ()-> proxy.getProductInfoFromStore(user4, store1bName, product1_store1b));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store1b(proxy.getProductInfoFromStore(user4, store1bName, product2_store1b));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store2(proxy.getProductInfoFromStore(user4, store2Name, product1_store2));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store2(proxy.getProductInfoFromStore(user4, store2Name, product2_store2));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        Assertions.assertThrows(Exception.class, ()-> proxy.getProductInfoFromStore(user4, store2Name, product3_store2));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
    }

    private void validateGetStoreInfoForMember2StoresAfterRemoveProducts() throws Exception {

        StoreDTO storeDTO2 = proxy.getStoreInfo(member2Name,store2Name);
        Assertions.assertTrue(storeDTO2.storeName.equals(store2Name));
        Assertions.assertTrue(storeDTO2.founderName.equals(member2Name));
        Assertions.assertTrue(storeDTO2.ownersNames.isEmpty());
        Assertions.assertTrue(storeDTO2.managersNames.isEmpty());
        Assertions.assertTrue(storeDTO2.isActive);
        Assertions.assertTrue(storeDTO2.productsInfoAmount.keySet().size()==2);
        Assertions.assertTrue(storeDTO2.productsInfoAmount.values().contains(500) && storeDTO2.productsInfoAmount.values().contains(600));
        ProductDTO productDTO1=null;
        ProductDTO productDTO2=null;
        for(ProductDTO productDTOTemp: storeDTO2.productsInfoAmount.keySet()){

            if(productDTOTemp.name.equals(product1_store2))
                productDTO1=productDTOTemp;

            if(productDTOTemp.name.equals(product2_store2))
                productDTO2=productDTOTemp;


        }
        Assertions.assertTrue(productDTO1 != null);
        Assertions.assertTrue(productDTO2 != null);


        Assertions.assertTrue(productDTO1.name.equals(product1_store2));
        Assertions.assertTrue(productDTO1.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO1.price.equals(555.5));
        Assertions.assertTrue(productDTO1.description.equals("description_product1_store2"));
        Assertions.assertTrue(productDTO1.categories.contains(category_store2));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);


        Assertions.assertTrue(productDTO2.name.equals(product2_store2));
        Assertions.assertTrue(productDTO2.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO2.price.equals(666.6));
        Assertions.assertTrue(productDTO2.description.equals("description_product2_store2"));
        Assertions.assertTrue(productDTO2.categories.contains(category_store2));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);


    }

    private void validateGetStoreInfoForMember1StoresAfterRemoveProducts() throws Exception {

        StoreDTO storeDTO1 = proxy.getStoreInfo(member1Name,store1Name);
        Assertions.assertTrue(storeDTO1.storeName.equals(store1Name));
        Assertions.assertTrue(storeDTO1.founderName.equals(member1Name));
        Assertions.assertTrue(storeDTO1.ownersNames.isEmpty());
        Assertions.assertTrue(storeDTO1.managersNames.isEmpty());
        Assertions.assertTrue(storeDTO1.isActive);
        Assertions.assertTrue(storeDTO1.productsInfoAmount.keySet().size()==1);
        Assertions.assertTrue(storeDTO1.productsInfoAmount.values().contains(100));
        ProductDTO productDTO1=storeDTO1.productsInfoAmount.keySet().stream().toList().get(0);

        Assertions.assertTrue(productDTO1.name.equals(product1_store1));
        Assertions.assertTrue(productDTO1.storeName.equals(store1Name));
        Assertions.assertTrue(productDTO1.price.equals(111.1));
        Assertions.assertTrue(productDTO1.description.equals("description_product1_store1"));
        Assertions.assertTrue(productDTO1.categories.contains(category1_product1_store1));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);

        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();


        StoreDTO storeDTO1b = proxy.getStoreInfo(member1Name,store1bName);
        Assertions.assertTrue(storeDTO1b.storeName.equals(store1bName));
        Assertions.assertTrue(storeDTO1b.founderName.equals(member1Name));
        Assertions.assertTrue(storeDTO1b.ownersNames.isEmpty());
        Assertions.assertTrue(storeDTO1b.managersNames.isEmpty());
        Assertions.assertTrue(storeDTO1b.isActive);
        Assertions.assertTrue(storeDTO1b.productsInfoAmount.keySet().size()==1);
        Assertions.assertTrue(storeDTO1b.productsInfoAmount.values().contains(400));

        ProductDTO productDTO2=storeDTO1b.productsInfoAmount.keySet().stream().toList().get(0);

        Assertions.assertTrue(productDTO2.name.equals(product2_store1b));
        Assertions.assertTrue(productDTO2.storeName.equals(store1bName));
        Assertions.assertTrue(productDTO2.price.equals(444.4));
        Assertions.assertTrue(productDTO2.description.equals("description_product2_store1b"));
        Assertions.assertTrue(productDTO2.categories.contains(category_store1b));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);

    }

    private void getProductsInfoFromMarketByCategory_1() throws Exception {
        validateProductIsProduct1_store1(proxy.getProductInfoFromMarketByCategory(user4, category1_product1_store1).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store1(proxy.getProductInfoFromMarketByCategory(user4, category2_product2_store1).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        List<ProductDTO> store1bProducts = proxy.getProductInfoFromMarketByCategory(user4, category_store1b);

        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        ProductDTO product1store1b = null;
        ProductDTO product2store1b = null;
        for(ProductDTO productDTO: store1bProducts){
            if (productDTO.name.equals(this.product1_store1b))
                product1store1b=productDTO;

            if (productDTO.name.equals(this.product2_store1b))
                product2store1b=productDTO;
        }
        Assertions.assertTrue(product1store1b!=null);
        Assertions.assertTrue(product2store1b!=null);
        validateProductIsProduct1_store1b(product1store1b);
        validateProductIsProduct2_store1b(product2store1b);

        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();


        List<ProductDTO> store2Products = proxy.getProductInfoFromMarketByCategory(user4, category_store2);
        ProductDTO product1store2 = null;
        ProductDTO product2store2 = null;
        ProductDTO product3store2 = null;


        for(ProductDTO productDTO: store2Products){
            if (productDTO.name.equals(this.product1_store2))
                product1store2=productDTO;

            if (productDTO.name.equals(this.product2_store2))
                product2store2=productDTO;

            if (productDTO.name.equals(this.product3_store2))
                product3store2=productDTO;
        }

        Assertions.assertTrue(product1store2!=null);
        Assertions.assertTrue(product2store2!=null);
        Assertions.assertTrue(product3store2!=null);
        validateProductIsProduct1_store2(product1store2);
        validateProductIsProduct2_store2(product2store2);
        validateProductIsProduct3_store2(product3store2);

    }

    private void getProductsInfoFromMarketByCategory() throws Exception {
        validateProductIsProduct1_store1(proxy.getProductInfoFromMarketByCategory(user4, category1_product1_store1).get(0));
        validateProductIsProduct2_store1(proxy.getProductInfoFromMarketByCategory(user4, category2_product2_store1).get(0));
        List<ProductDTO> store1bProducts = proxy.getProductInfoFromMarketByCategory(user4, category_store1b);
        ProductDTO product1store1b = null;
        ProductDTO product2store1b = null;
        for(ProductDTO productDTO: store1bProducts){
            if (productDTO.name.equals(this.product1_store1b))
                product1store1b=productDTO;

            if (productDTO.name.equals(this.product2_store1b))
                product2store1b=productDTO;
        }
        Assertions.assertTrue(product1store1b!=null);
        Assertions.assertTrue(product2store1b!=null);
        validateProductIsProduct1_store1b(product1store1b);
        validateProductIsProduct2_store1b(product2store1b);




        List<ProductDTO> store2Products = proxy.getProductInfoFromMarketByCategory(user4, category_store2);
        ProductDTO product1store2 = null;
        ProductDTO product2store2 = null;
        ProductDTO product3store2 = null;


        for(ProductDTO productDTO: store2Products){
            if (productDTO.name.equals(this.product1_store2))
                product1store2=productDTO;

            if (productDTO.name.equals(this.product2_store2))
                product2store2=productDTO;

            if (productDTO.name.equals(this.product3_store2))
                product3store2=productDTO;
        }

        Assertions.assertTrue(product1store2!=null);
        Assertions.assertTrue(product2store2!=null);
        Assertions.assertTrue(product3store2!=null);
        validateProductIsProduct1_store2(product1store2);
        validateProductIsProduct2_store2(product2store2);
        validateProductIsProduct3_store2(product3store2);

    }

    private void getProductsInfoFromMarketByKeyWord() throws Exception {


        List<ProductDTO> store1bProducts = proxy.getProductInfoFromMarketByKeyword(user4, store1bName);
        ProductDTO product1store1b = null;
        ProductDTO product2store1b = null;
        for(ProductDTO productDTO: store1bProducts){
            if (productDTO.name.equals(this.product1_store1b))
                product1store1b=productDTO;

            if (productDTO.name.equals(this.product2_store1b))
                product2store1b=productDTO;
        }
        Assertions.assertTrue(product1store1b!=null);
        Assertions.assertTrue(product2store1b!=null);
        validateProductIsProduct1_store1b(product1store1b);
        validateProductIsProduct2_store1b(product2store1b);




        List<ProductDTO> store2Products = proxy.getProductInfoFromMarketByKeyword(user4, store2Name);
        ProductDTO product1store2 = null;
        ProductDTO product2store2 = null;
        ProductDTO product3store2 = null;


        for(ProductDTO productDTO: store2Products){
            if (productDTO.name.equals(this.product1_store2))
                product1store2=productDTO;

            if (productDTO.name.equals(this.product2_store2))
                product2store2=productDTO;

            if (productDTO.name.equals(this.product3_store2))
                product3store2=productDTO;
        }

        Assertions.assertTrue(product1store2!=null);
        Assertions.assertTrue(product2store2!=null);
        Assertions.assertTrue(product3store2!=null);
        validateProductIsProduct1_store2(product1store2);
        validateProductIsProduct2_store2(product2store2);
        validateProductIsProduct3_store2(product3store2);

    }

    private void getProductsInfoFromMarket() throws Exception {
        validateProductIsProduct1_store1(proxy.getProductInfoFromMarketByName(user4, product1_store1).get(0));
        validateProductIsProduct2_store1(proxy.getProductInfoFromMarketByName(user4, product2_store1).get(0));
        validateProductIsProduct1_store1b(proxy.getProductInfoFromMarketByName(user4, product1_store1b).get(0));
        validateProductIsProduct2_store1b(proxy.getProductInfoFromMarketByName(user4, product2_store1b).get(0));
        validateProductIsProduct1_store2(proxy.getProductInfoFromMarketByName(user4, product1_store2).get(0));
        validateProductIsProduct2_store2(proxy.getProductInfoFromMarketByName(user4, product2_store2).get(0));
        validateProductIsProduct3_store2(proxy.getProductInfoFromMarketByName(user4, product3_store2).get(0));

    }

    private void getProductsInfoFromMarket_1() throws Exception {
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store1(proxy.getProductInfoFromMarketByName(user4, product1_store1).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store1(proxy.getProductInfoFromMarketByName(user4, product2_store1).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store1b(proxy.getProductInfoFromMarketByName(user4, product1_store1b).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store1b(proxy.getProductInfoFromMarketByName(user4, product2_store1b).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store2(proxy.getProductInfoFromMarketByName(user4, product1_store2).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store2(proxy.getProductInfoFromMarketByName(user4, product2_store2).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct3_store2(proxy.getProductInfoFromMarketByName(user4, product3_store2).get(0));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();

    }

    private void getProductsInfoFromStore() throws Exception {
        validateProductIsProduct1_store1(proxy.getProductInfoFromStore(user4, store1Name, product1_store1));
        validateProductIsProduct2_store1(proxy.getProductInfoFromStore(user4, store1Name, product2_store1));
        validateProductIsProduct1_store1b(proxy.getProductInfoFromStore(user4, store1bName, product1_store1b));
        validateProductIsProduct2_store1b(proxy.getProductInfoFromStore(user4, store1bName, product2_store1b));
        validateProductIsProduct1_store2(proxy.getProductInfoFromStore(user4, store2Name, product1_store2));
        validateProductIsProduct2_store2(proxy.getProductInfoFromStore(user4, store2Name, product2_store2));
        validateProductIsProduct3_store2(proxy.getProductInfoFromStore(user4, store2Name, product3_store2));

    }

    private void getProductsInfoFromStore_1() throws Exception {
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store1(proxy.getProductInfoFromStore(user4, store1Name, product1_store1));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store1(proxy.getProductInfoFromStore(user4, store1Name, product2_store1));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store1b(proxy.getProductInfoFromStore(user4, store1bName, product1_store1b));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store1b(proxy.getProductInfoFromStore(user4, store1bName, product2_store1b));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct1_store2(proxy.getProductInfoFromStore(user4, store2Name, product1_store2));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct2_store2(proxy.getProductInfoFromStore(user4, store2Name, product2_store2));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();
        validateProductIsProduct3_store2(proxy.getProductInfoFromStore(user4, store2Name, product3_store2));
        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();

    }

    private void validateProductIsProduct1_store2(ProductDTO productDTO1){
        Assertions.assertTrue(productDTO1.name.equals(product1_store2));
        Assertions.assertTrue(productDTO1.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO1.price.equals(555.5));
        Assertions.assertTrue(productDTO1.description.equals("description_product1_store2"));
        Assertions.assertTrue(productDTO1.categories.contains(category_store2));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);


    }
    private void validateProductIsProduct2_store2(ProductDTO productDTO2){


        Assertions.assertTrue(productDTO2.name.equals(product2_store2));
        Assertions.assertTrue(productDTO2.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO2.price.equals(666.6));
        Assertions.assertTrue(productDTO2.description.equals("description_product2_store2"));
        Assertions.assertTrue(productDTO2.categories.contains(category_store2));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);

    }
    private void validateProductIsProduct3_store2(ProductDTO productDTO3){


        Assertions.assertTrue(productDTO3.name.equals(product3_store2));
        Assertions.assertTrue(productDTO3.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO3.price.equals(777.7));
        Assertions.assertTrue(productDTO3.description.equals("description_product3_store2"));
        Assertions.assertTrue(productDTO3.categories.contains(category_store2));
        Assertions.assertTrue(productDTO3.productDiscountPolicies.size()==0);

    }

    private void validateProductIsProduct1_store1b(ProductDTO productDTO1){

        Assertions.assertTrue(productDTO1.name.equals(product1_store1b));
        Assertions.assertTrue(productDTO1.storeName.equals(store1bName));
        Assertions.assertTrue(productDTO1.price.equals(333.3));
        Assertions.assertTrue(productDTO1.description.equals("description_product1_store1b"));
        Assertions.assertTrue(productDTO1.categories.contains(category_store1b));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);


    }

    private void validateProductIsProduct2_store1b(ProductDTO productDTO2){

        Assertions.assertTrue(productDTO2.name.equals(product2_store1b));
        Assertions.assertTrue(productDTO2.storeName.equals(store1bName));
        Assertions.assertTrue(productDTO2.price.equals(444.4));
        Assertions.assertTrue(productDTO2.description.equals("description_product2_store1b"));
        Assertions.assertTrue(productDTO2.categories.contains(category_store1b));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);

    }

    private void validateProductIsProduct2_store1(ProductDTO productDTO2){

        Assertions.assertTrue(productDTO2.name.equals(product2_store1));
        Assertions.assertTrue(productDTO2.storeName.equals(store1Name));
        Assertions.assertTrue(productDTO2.price.equals(222.2));
        Assertions.assertTrue(productDTO2.description.equals("description_product2_store1"));
        Assertions.assertTrue(productDTO2.categories.contains(category2_product2_store1));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);

    }

    private void validateProductIsProduct3_store2AfterUpdateProduct(ProductDTO productDTO3){


        Assertions.assertTrue(productDTO3.name.equals(product3_store2));
        Assertions.assertTrue(productDTO3.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO3.price.equals(777.7));
        Assertions.assertTrue(productDTO3.description.equals("new_description_product3_store2"));
        Assertions.assertTrue(productDTO3.categories.contains(category_store2));
        Assertions.assertTrue(productDTO3.productDiscountPolicies.size()==0);

    }

    private void validateProductIsProduct2_store2AfterUpdateProduct(ProductDTO productDTO2){


        Assertions.assertTrue(productDTO2.name.equals(product2_store2));
        Assertions.assertTrue(productDTO2.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO2.price.equals(987.3));
        Assertions.assertTrue(productDTO2.description.equals("description_product2_store2"));
        Assertions.assertTrue(productDTO2.categories.contains(category_store2));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);

    }

    private void validateProductIsProduct2_store1bAfterUpdateProduct(ProductDTO productDTO2){

        Assertions.assertTrue(productDTO2.name.equals(product2_store1b));
        Assertions.assertTrue(productDTO2.storeName.equals(store1bName));
        Assertions.assertTrue(productDTO2.price.equals(987.2));
        Assertions.assertTrue(productDTO2.description.equals("description_product2_store1b"));
        Assertions.assertTrue(productDTO2.categories.contains(category_store1b));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);

    }

    private void validateProductIsProduct1_store1bAfterUpdateProduct(ProductDTO productDTO1){

        Assertions.assertTrue(productDTO1.name.equals(product1_store1b));
        Assertions.assertTrue(productDTO1.storeName.equals(store1bName));
        Assertions.assertTrue(productDTO1.price.equals(333.3));
        Assertions.assertTrue(productDTO1.description.equals("new_description_product1_store1b"));
        Assertions.assertTrue(productDTO1.categories.contains(category_store1b));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);


    }
    private void validateProductIsProduct2_store1AfterUpdateProduct(ProductDTO productDTO2){

        Assertions.assertTrue(productDTO2.name.equals(product2_store1));
        Assertions.assertTrue(productDTO2.storeName.equals(store1Name));
        Assertions.assertTrue(productDTO2.price.equals(222.2));
        Assertions.assertTrue(productDTO2.description.equals("new_description_product2_store1"));
        Assertions.assertTrue(productDTO2.categories.contains(category2_product2_store1));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);

    }

    private void validateProductIsProduct1_store1AfterUpdateProduct(ProductDTO productDTO1){

        Assertions.assertTrue(productDTO1.name.equals(product1_store1));
        Assertions.assertTrue(productDTO1.storeName.equals(store1Name));
        Assertions.assertTrue(productDTO1.price.equals(987.1));
        Assertions.assertTrue(productDTO1.description.equals("description_product1_store1"));
        Assertions.assertTrue(productDTO1.categories.contains(category1_product1_store1));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);

    }

    private void validateProductIsProduct1_store1(ProductDTO productDTO1){

        Assertions.assertTrue(productDTO1.name.equals(product1_store1));
        Assertions.assertTrue(productDTO1.storeName.equals(store1Name));
        Assertions.assertTrue(productDTO1.price.equals(111.1));
        Assertions.assertTrue(productDTO1.description.equals("description_product1_store1"));
        Assertions.assertTrue(productDTO1.categories.contains(category1_product1_store1));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);

    }
    private void validateGetStoreInfoForMember2StoresAfterCloseStore() throws Exception {

        StoreDTO storeDTO2 = proxy.getStoreInfo(member2Name,store2Name);
        Assertions.assertTrue(storeDTO2.storeName.equals(store2Name));
        Assertions.assertTrue(storeDTO2.founderName.equals(member2Name));
        Assertions.assertTrue(storeDTO2.ownersNames.isEmpty());
        Assertions.assertTrue(storeDTO2.managersNames.isEmpty());
        Assertions.assertFalse(storeDTO2.isActive);
        Assertions.assertTrue(storeDTO2.productsInfoAmount.keySet().size()==3);
        Assertions.assertTrue(storeDTO2.productsInfoAmount.values().contains(500) && storeDTO2.productsInfoAmount.values().contains(600) && storeDTO2.productsInfoAmount.values().contains(700));


    }

    private void validateGetStoreInfoForMember2Stores() throws Exception {

        StoreDTO storeDTO2 = proxy.getStoreInfo(member2Name,store2Name);
        Assertions.assertTrue(storeDTO2.storeName.equals(store2Name));
        Assertions.assertTrue(storeDTO2.founderName.equals(member2Name));
        Assertions.assertTrue(storeDTO2.ownersNames.isEmpty());
        Assertions.assertTrue(storeDTO2.managersNames.isEmpty());
        Assertions.assertTrue(storeDTO2.isActive);
        Assertions.assertTrue(storeDTO2.productsInfoAmount.keySet().size()==3);
        Assertions.assertTrue(storeDTO2.productsInfoAmount.values().contains(500) && storeDTO2.productsInfoAmount.values().contains(600) && storeDTO2.productsInfoAmount.values().contains(700));
        ProductDTO productDTO1=null;
        ProductDTO productDTO2=null;
        ProductDTO productDTO3=null;
        for(ProductDTO productDTOTemp: storeDTO2.productsInfoAmount.keySet()){

            if(productDTOTemp.name.equals(product1_store2))
                productDTO1=productDTOTemp;

            if(productDTOTemp.name.equals(product2_store2))
                productDTO2=productDTOTemp;

            if(productDTOTemp.name.equals(product3_store2))
                productDTO3=productDTOTemp;
        }
        Assertions.assertTrue(productDTO1 != null);
        Assertions.assertTrue(productDTO2 != null);
        Assertions.assertTrue(productDTO3 != null);


        Assertions.assertTrue(productDTO1.name.equals(product1_store2));
        Assertions.assertTrue(productDTO1.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO1.price.equals(555.5));
        Assertions.assertTrue(productDTO1.description.equals("description_product1_store2"));
        Assertions.assertTrue(productDTO1.categories.contains(category_store2));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);


        Assertions.assertTrue(productDTO2.name.equals(product2_store2));
        Assertions.assertTrue(productDTO2.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO2.price.equals(666.6));
        Assertions.assertTrue(productDTO2.description.equals("description_product2_store2"));
        Assertions.assertTrue(productDTO2.categories.contains(category_store2));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);


        Assertions.assertTrue(productDTO3.name.equals(product3_store2));
        Assertions.assertTrue(productDTO3.storeName.equals(store2Name));
        Assertions.assertTrue(productDTO3.price.equals(777.7));
        Assertions.assertTrue(productDTO3.description.equals("description_product3_store2"));
        Assertions.assertTrue(productDTO3.categories.contains(category_store2));
        Assertions.assertTrue(productDTO3.productDiscountPolicies.size()==0);


    }

    private void validateGetStoreInfoForMember1StoresAfterStoreIsClosed() throws Exception {
        StoreDTO storeDTO1 = proxy.getStoreInfo(member1Name,store1Name);
        Assertions.assertTrue(storeDTO1.storeName.equals(store1Name));
        Assertions.assertTrue(storeDTO1.founderName.equals(member1Name));
        Assertions.assertTrue(storeDTO1.ownersNames.isEmpty());
        Assertions.assertTrue(storeDTO1.managersNames.isEmpty());
        Assertions.assertFalse(storeDTO1.isActive);
        Assertions.assertTrue(storeDTO1.productsInfoAmount.keySet().size()==2);
        Assertions.assertTrue(storeDTO1.productsInfoAmount.values().contains(100) && storeDTO1.productsInfoAmount.values().contains(200));


        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();



        StoreDTO storeDTO1b = proxy.getStoreInfo(member1Name,store1bName);
        Assertions.assertTrue(storeDTO1b.storeName.equals(store1bName));
        Assertions.assertTrue(storeDTO1b.founderName.equals(member1Name));
        Assertions.assertTrue(storeDTO1b.ownersNames.isEmpty());
        Assertions.assertTrue(storeDTO1b.managersNames.isEmpty());
        Assertions.assertTrue(storeDTO1b.isActive);
        Assertions.assertTrue(storeDTO1b.productsInfoAmount.keySet().size()==2);
        Assertions.assertTrue(storeDTO1b.productsInfoAmount.values().contains(300) && storeDTO1b.productsInfoAmount.values().contains(400));

    }

    private void validateGetStoreInfoForMember1Stores() throws Exception {

        StoreDTO storeDTO1 = proxy.getStoreInfo(member1Name,store1Name);
        Assertions.assertTrue(storeDTO1.storeName.equals(store1Name));
        Assertions.assertTrue(storeDTO1.founderName.equals(member1Name));
        Assertions.assertTrue(storeDTO1.ownersNames.isEmpty());
        Assertions.assertTrue(storeDTO1.managersNames.isEmpty());
        Assertions.assertTrue(storeDTO1.isActive);
        Assertions.assertTrue(storeDTO1.productsInfoAmount.keySet().size()==2);
        Assertions.assertTrue(storeDTO1.productsInfoAmount.values().contains(100) && storeDTO1.productsInfoAmount.values().contains(200));
        ProductDTO productDTO1=null;
        ProductDTO productDTO2=null;
        for(ProductDTO productDTOTemp: storeDTO1.productsInfoAmount.keySet()){

            if(productDTOTemp.name.equals(product1_store1))
                productDTO1=productDTOTemp;

            if(productDTOTemp.name.equals(product2_store1))
                productDTO2=productDTOTemp;
        }
        Assertions.assertTrue(productDTO1 != null);
        Assertions.assertTrue(productDTO2 != null);

        Assertions.assertTrue(productDTO1.name.equals(product1_store1));
        Assertions.assertTrue(productDTO1.storeName.equals(store1Name));
        Assertions.assertTrue(productDTO1.price.equals(111.1));
        Assertions.assertTrue(productDTO1.description.equals("description_product1_store1"));
        Assertions.assertTrue(productDTO1.categories.contains(category1_product1_store1));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);


        Assertions.assertTrue(productDTO2.name.equals(product2_store1));
        Assertions.assertTrue(productDTO2.storeName.equals(store1Name));
        Assertions.assertTrue(productDTO2.price.equals(222.2));
        Assertions.assertTrue(productDTO2.description.equals("description_product2_store1"));
        Assertions.assertTrue(productDTO2.categories.contains(category2_product2_store1));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);



        logOutMembers();
        initSystemServiceAndLoadDataAndLogIn();


        StoreDTO storeDTO1b = proxy.getStoreInfo(member1Name,store1bName);
        Assertions.assertTrue(storeDTO1b.storeName.equals(store1bName));
        Assertions.assertTrue(storeDTO1b.founderName.equals(member1Name));
        Assertions.assertTrue(storeDTO1b.ownersNames.isEmpty());
        Assertions.assertTrue(storeDTO1b.managersNames.isEmpty());
        Assertions.assertTrue(storeDTO1b.isActive);
        Assertions.assertTrue(storeDTO1b.productsInfoAmount.keySet().size()==2);
        Assertions.assertTrue(storeDTO1b.productsInfoAmount.values().contains(300) && storeDTO1b.productsInfoAmount.values().contains(400));
        productDTO1=null;
        productDTO2=null;


        for(ProductDTO productDTOTemp: storeDTO1b.productsInfoAmount.keySet()){

            if(productDTOTemp.name.equals(product1_store1b))
                productDTO1=productDTOTemp;

            if(productDTOTemp.name.equals(product2_store1b))
                productDTO2=productDTOTemp;
        }

        Assertions.assertTrue(productDTO1 != null);
        Assertions.assertTrue(productDTO2 != null);



        Assertions.assertTrue(productDTO1.name.equals(product1_store1b));
        Assertions.assertTrue(productDTO1.storeName.equals(store1bName));
        Assertions.assertTrue(productDTO1.price.equals(333.3));
        Assertions.assertTrue(productDTO1.description.equals("description_product1_store1b"));
        Assertions.assertTrue(productDTO1.categories.contains(category_store1b));
        Assertions.assertTrue(productDTO1.productDiscountPolicies.size()==0);


        Assertions.assertTrue(productDTO2.name.equals(product2_store1b));
        Assertions.assertTrue(productDTO2.storeName.equals(store1bName));
        Assertions.assertTrue(productDTO2.price.equals(444.4));
        Assertions.assertTrue(productDTO2.description.equals("description_product2_store1b"));
        Assertions.assertTrue(productDTO2.categories.contains(category_store1b));
        Assertions.assertTrue(productDTO2.productDiscountPolicies.size()==0);

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
        user4 = proxy.enterMarket();
        user1 = proxy.login(user1, member1Name, member1Password);
        user2 = proxy.login(user2, member2Name, member2Password);
        user3 =proxy.login(user3, member3Name, member3Password);
    }


}