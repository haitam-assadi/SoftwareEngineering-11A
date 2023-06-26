package DatabaseTests;

import DTO.DealDTO;
import DTO.OwnerContractDTO;
import DTO.ProductDTO;
import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class DiscountTests {

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
    public void discountTest(){
        try{

            proxy.register(user1, member1Name, member1Password);
            proxy.register(user2, member2Name, member2Password);

            user1 = proxy.login(user1, member1Name, member1Password);
            user2 = proxy.login(user2, member2Name, member2Password);

            proxy.register(user3, member3Name, member3Password);
            user3 =proxy.login(user3, member3Name, member3Password);


            ////// create stores
            proxy.createStore(member1Name, store1Name);
            proxy.createStore(member1Name, store1bName);

            proxy.createStore(member2Name, store2Name);
            proxy.createStore(member2Name, store2bName);

            proxy.createStore(member3Name, store3Name);

            proxy.appointOtherMemberAsStoreOwner(member1Name,store1Name,member2Name);
            proxy.appointOtherMemberAsStoreOwner(member1Name,store1Name,member3Name);

            proxy.fillOwnerContract(member2Name,store1Name,member3Name,true);


            proxy.addNewProductToStock(member1Name, store1Name, product1_store1, category1_product1_store1, 3000.0, "description_product1_store1", 100);
            proxy.addNewProductToStock(member1Name, store1Name, product2_store1, category2_product2_store1, 2000.0, "description", 200);
            proxy.addNewProductToStock(member1Name, store1bName, product1_store1b, category_store1b, 333.3, "description_product1_store1b", 300);
            proxy.addNewProductToStock(member1Name, store1bName, product2_store1b, category_store1b, 444.4, "description", 400);

            proxy.addNewProductToStock(member2Name, store2Name, product1_store2, category_store2, 555.5, "description_product1_store2", 500);
            proxy.addNewProductToStock(member2Name, store2Name, product2_store2, category_store2, 666.6, "description", 600);
            proxy.addNewProductToStock(member2Name, store2Name, product3_store2, category_store2, 777.7, "description", 700);

            Assertions.assertEquals(proxy.createProductDiscountPolicy(member1Name,store1Name,product1_store1,20,true),1);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertFalse(proxy.getAllStoreDiscountPolicies(member1Name,store1Name).isEmpty());
            ProductDTO productDTO = proxy.getProductInfoFromStore(member1Name,store1Name,product1_store1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.size()==1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.get(0).contains(product1_store1));

            proxy.addToCart(member1Name, store1Name, product1_store1,1);
            Assertions.assertEquals(proxy.getCartPriceBeforeDiscount(member1Name),3000.0);
            Assertions.assertEquals(proxy.getCartPriceAfterDiscount(member1Name),3000.0-(3000.0*0.2));

            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member1Name, "123456789", "7", "2024", user2,
                    "123", "123456789", user2, "address", "city", "country", "100200"));

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            List<DealDTO> dealDTOStore = proxy.getStoreDeals(member1Name,store1Name);
            DealDTO dealDTO = dealDTOStore.get(0);
            Assertions.assertEquals(dealDTO.productFinalPriceWithDiscount.get(product1_store1),3000.0-(3000.0*0.2));
            Assertions.assertEquals(dealDTO.totalPrice,3000.0-(3000.0*0.2));
            Assertions.assertEquals(dealDTO.products_prices.get(product1_store1),3000.0);
            Assertions.assertEquals(dealDTO.products_amount.get(product1_store1),1);
            Assertions.assertEquals(dealDTO.productPriceMultipleAmount.get(product1_store1),1*(3000.0));
            Assertions.assertEquals(dealDTO.storeName,store1Name);
            Assertions.assertEquals(dealDTO.username,member1Name);

            proxy.addToCart(member1Name, store1Name, product1_store1,1);
            proxy.removeProductFromCart(member1Name,store1Name,product1_store1);

            proxy.removeFromStoreDiscountPolicies(member1Name,store1Name,1);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertTrue(proxy.getAllStoreDiscountPolicies(member1Name,store1Name).isEmpty());
            productDTO = proxy.getProductInfoFromStore(member1Name,store1Name,product1_store1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.size()==0);

            Integer bagConstraint = proxy.createMaxProductAmountAllContentBagConstraint(member1Name,store1Name,product1_store1,200,true);
            System.out.println(bagConstraint);
            System.out.println("=========================================");
            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(proxy.createProductDiscountPolicyWithConstraint(member1Name,store1Name,product1_store1,20,bagConstraint,true),2);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertFalse(proxy.getAllStoreDiscountPolicies(member1Name,store1Name).isEmpty());
            productDTO = proxy.getProductInfoFromStore(member1Name,store1Name,product1_store1);
            Assertions.assertEquals(productDTO.productDiscountPolicies.size(),1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.get(0).contains(product1_store1));

            proxy.addToCart(member1Name, store1Name, product1_store1,1);
            Assertions.assertEquals(proxy.getCartPriceBeforeDiscount(member1Name),3000.0);
            Assertions.assertEquals(proxy.getCartPriceAfterDiscount(member1Name),3000.0-(3000.0*0.2));

            proxy.removeProductFromCart(member1Name,store1Name,product1_store1);

            proxy.removeFromStoreDiscountPolicies(member1Name,store1Name,2);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertTrue(proxy.getAllStoreDiscountPolicies(member1Name,store1Name).isEmpty());
            productDTO = proxy.getProductInfoFromStore(member1Name,store1Name,product1_store1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.size()==0);

            Assertions.assertEquals(proxy.createCategoryDiscountPolicy(member1Name,store1Name,category1_product1_store1,20,true),3);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertFalse(proxy.getAllStoreDiscountPolicies(member1Name,store1Name).isEmpty());
            productDTO = proxy.getProductInfoFromStore(member1Name,store1Name,product1_store1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.size()==1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.get(0).contains(product1_store1));

            proxy.addToCart(member1Name, store1Name, product1_store1,1);
            Assertions.assertEquals(proxy.getCartPriceBeforeDiscount(member1Name),3000.0);
            Assertions.assertEquals(proxy.getCartPriceAfterDiscount(member1Name),3000.0-(3000.0*0.2));

            proxy.removeProductFromCart(member1Name,store1Name,product1_store1);

            proxy.removeFromStoreDiscountPolicies(member1Name,store1Name,3);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertTrue(proxy.getAllStoreDiscountPolicies(member1Name,store1Name).isEmpty());
            productDTO = proxy.getProductInfoFromStore(member1Name,store1Name,product1_store1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.size()==0);

            bagConstraint = proxy.createMaxProductAmountAllContentBagConstraint(member1Name,store1Name,product1_store1,200,true);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(proxy.createCategoryDiscountPolicyWithConstraint(member1Name,store1Name,category1_product1_store1,20,bagConstraint,true),4);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertFalse(proxy.getAllStoreDiscountPolicies(member1Name,store1Name).isEmpty());
            productDTO = proxy.getProductInfoFromStore(member1Name,store1Name,product1_store1);
            Assertions.assertEquals(productDTO.productDiscountPolicies.size(),1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.get(0).contains(product1_store1));

            proxy.addToCart(member1Name, store1Name, product1_store1,1);
            Assertions.assertEquals(proxy.getCartPriceBeforeDiscount(member1Name),3000.0);
            Assertions.assertEquals(proxy.getCartPriceAfterDiscount(member1Name),3000.0-(3000.0*0.2));

            proxy.removeProductFromCart(member1Name,store1Name,product1_store1);

            proxy.removeFromStoreDiscountPolicies(member1Name,store1Name,4);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertTrue(proxy.getAllStoreDiscountPolicies(member1Name,store1Name).isEmpty());
            productDTO = proxy.getProductInfoFromStore(member1Name,store1Name,product1_store1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.size()==0);

            Assertions.assertEquals(proxy.createAllStoreDiscountPolicy(member1Name,store1Name,20,true),5);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertFalse(proxy.getAllStoreDiscountPolicies(member1Name,store1Name).isEmpty());
            productDTO = proxy.getProductInfoFromStore(member1Name,store1Name,product1_store1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.size()==1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.get(0).contains("20% discount on"));

            proxy.addToCart(member1Name, store1Name, product1_store1,1);
            Assertions.assertEquals(proxy.getCartPriceBeforeDiscount(member1Name),3000.0);
            Assertions.assertEquals(proxy.getCartPriceAfterDiscount(member1Name),3000.0-(3000.0*0.2));

            proxy.removeProductFromCart(member1Name,store1Name,product1_store1);

            proxy.removeFromStoreDiscountPolicies(member1Name,store1Name,5);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertTrue(proxy.getAllStoreDiscountPolicies(member1Name,store1Name).isEmpty());
            productDTO = proxy.getProductInfoFromStore(member1Name,store1Name,product1_store1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.size()==0);

            bagConstraint = proxy.createMaxProductAmountAllContentBagConstraint(member1Name,store1Name,product1_store1,200,true);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(proxy.createAllStoreDiscountPolicyWithConstraint(member1Name,store1Name,20,bagConstraint,true),6);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertFalse(proxy.getAllStoreDiscountPolicies(member1Name,store1Name).isEmpty());
            productDTO = proxy.getProductInfoFromStore(member1Name,store1Name,product1_store1);
            Assertions.assertEquals(productDTO.productDiscountPolicies.size(),1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.get(0).contains("20% discount on"));

            proxy.addToCart(member1Name, store1Name, product1_store1,1);
            Assertions.assertEquals(proxy.getCartPriceBeforeDiscount(member1Name),3000.0);
            Assertions.assertEquals(proxy.getCartPriceAfterDiscount(member1Name),3000.0-(3000.0*0.2));

            proxy.removeProductFromCart(member1Name,store1Name,product1_store1);

            proxy.removeFromStoreDiscountPolicies(member1Name,store1Name,6);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertTrue(proxy.getAllStoreDiscountPolicies(member1Name,store1Name).isEmpty());
            productDTO = proxy.getProductInfoFromStore(member1Name,store1Name,product1_store1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.size()==0);

            ///////////////////////////////addition//////////////////////////////////////////////////

            Integer discount1 = proxy.createProductDiscountPolicy(member1Name,store1Name,product1_store1,20,false);
            Integer discount2 = proxy.createProductDiscountPolicy(member1Name,store1Name,product2_store1,30,false);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            proxy.createAdditionDiscountPolicy(member1Name,store1Name,discount1,discount2,true);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertFalse(proxy.getAllStoreDiscountPolicies(member1Name,store1Name).isEmpty());
            productDTO = proxy.getProductInfoFromStore(member1Name,store1Name,product1_store1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.size()==1);
            Assertions.assertTrue(productDTO.productDiscountPolicies.get(0).contains(product1_store1));
            Assertions.assertTrue(productDTO.productDiscountPolicies.get(0).contains(product2_store1));

            proxy.addToCart(member1Name, store1Name,  product1_store1,1);
            proxy.addToCart(member1Name, store1Name, product2_store1,1);
            Assertions.assertEquals(proxy.getCartPriceBeforeDiscount(member1Name),5000.0);
            Assertions.assertEquals(proxy.getCartPriceAfterDiscount(member1Name),5000.0-((3000.0*0.2)+(2000.0*0.3)));



        }catch (Exception e){
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
        user4 = proxy.enterMarket();
        user1 = proxy.login(user1, member1Name, member1Password);
        user2 = proxy.login(user2, member2Name, member2Password);
        user3 =proxy.login(user3, member3Name, member3Password);
    }


}
