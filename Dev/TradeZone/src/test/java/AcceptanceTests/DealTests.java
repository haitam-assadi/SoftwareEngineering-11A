package AcceptanceTests;

import DTO.DealDTO;
import DomainLayer.PaymentService;
import DomainLayer.ShipmentService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class DealTests {
    private ProxyBridge proxy;

    String managerName;
    String managerPass;
    private String user;
    String moslemUserName = "moslem123";
    String moslemPassword = "Aa123456";
    private String store_founder;
    private String guest_name;
    private String member1_name;
    private String member2_name;
    private String storeName1;
    private String storeName2;
    private String owner;
    private String manager;

    private String user1_cardNumber = "374245455400126";
    private String user1_month = "06";
    private String user1_year = "2025";
    private String user1_holder = "user1_holder";
    private String user1_cvv = "223";
    private String user1_id = "123456789";
    private String user1_receiverName = "user1_receiverName";
    private String user1_shipmentAddress = "user1_shipmentAddress";
    private String user1_shipmentCity = "user1_shipmentCity" ;
    private String user1_shipmentCountry = "user1_shipmentCountry";
    private String user1_zipCode = "0099";

    private String member1_cardNumber = "1111222233334444";
    private String member1_month = "04";
    private String member1_year = "2026";
    private String member1_holder = "member1_holder";
    private String member1_cvv = "111";
    private String member1_id = "987654321";
    private String member1_receiverName = "member1_receiverName";
    private String member1_shipmentAddress="member1_shipmentAddress";
    private String member1_shipmentCity = "member1_shipmentCity";
    private String member1_shipmentCountry= "member1_shipmentCountry";
    private String member1_zipCode = "0022";

    @Mock
    private PaymentService paymentService;

    @Mock
    private ShipmentService shipmentService;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        proxy = new ProxyBridge(new RealBridge());
        managerName = proxy.initializeMarket();

        if(managerName.isEmpty()){
            System.out.println("exception thrown");
        }

        // store founder
        user = proxy.enterMarket();//guest default user name
        proxy.register(user, moslemUserName, moslemPassword);
        store_founder = proxy.login(user, moslemUserName, moslemPassword);

        storeName1 = proxy.createStore(store_founder, "Moslem Store");
        proxy.addCategory(store_founder, "Iphones", storeName1);
        proxy.addNewProductToStock(store_founder, storeName1, "iphone 14", "Iphones", 3000.0, "256 Gb", 50);
        proxy.addNewProductToStock(store_founder, storeName1, "iphone 13", "Iphones", 2000.0, "256 Gb", 35);
        proxy.addCategory(store_founder, "Gaming chairs", storeName1);
        proxy.addNewProductToStock(store_founder, storeName1, "gaming chair1", "Gaming chairs", 600.0, "red", 50);

        storeName2 = proxy.createStore(store_founder, "Baraa Store");
        proxy.addCategory(store_founder, "Iphones", storeName2);
        proxy.addNewProductToStock(store_founder, storeName2, "iphone 14", "Iphones", 3500.0, "256 Gb", 100);
        proxy.addCategory(store_founder, "Gaming mouses", storeName2);
        proxy.addNewProductToStock(store_founder, storeName2, "gaming mouse 1", "Gaming mouses", 200.0, "black, RGB", 70);

        // guest
        guest_name = proxy.enterMarket();//guest default user name



        // member
        user = proxy.enterMarket();//guest default user name
        proxy.register(user, "Harry123", "Hh123456");
        member1_name = proxy.login(user, "Harry123", "Hh123456");

        user = proxy.enterMarket();//guest default user name
        proxy.register(user, "Harry12344", "Hh12345644");
        member2_name = proxy.login(user, "Harry12344", "Hh12345644");


        // owner
        user = proxy.enterMarket();//guest default user name
        proxy.register(user, "Joe123", "Jj456789");
        owner = proxy.login(user, "Joe123", "Jj456789");

        // manager
        user = proxy.enterMarket();//guest default user name
        proxy.register(user, "Tom123", "Tt123456");
        manager = proxy.login(user, "Tom123", "Tt123456");

        proxy.setPaymentService(this.paymentService);
        proxy.setShipmentService(this.shipmentService);
        Mockito.when(paymentService.pay(Mockito.anyDouble(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(1);
        Mockito.when(shipmentService.supply(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(1);
    }

    @Test
    public void purchase_with_deal_success(){
        try{
            proxy.addToCart(guest_name, storeName2, "gaming mouse 1", 3);
            proxy.addToCart(member1_name, storeName2, "iphone 14",1);

            Assertions.assertTrue(proxy.purchaseCartByCreditCard(guest_name,user1_cardNumber,user1_month,user1_year,user1_holder,user1_cvv,user1_id,user1_receiverName,user1_shipmentAddress,user1_shipmentCity,user1_shipmentCountry,user1_zipCode));
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member1_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));

            Assertions.assertTrue(proxy.getCartContent(guest_name).isEmpty());
            Assertions.assertTrue(proxy.getCartContent(member1_name).isEmpty());

            Assertions.assertFalse(proxy.getStoreDeals(store_founder,storeName2).isEmpty());
            Assertions.assertFalse(proxy.getMemberDeals(member1_name,member1_name).isEmpty());

            Assertions.assertEquals(proxy.getMemberDeals(member1_name,member1_name).get(0).totalPrice,proxy.getStoreDeals(store_founder,storeName2).get(1).totalPrice);
            List<DealDTO> memberDeals = proxy.getMemberDeals(member1_name,member1_name);
            List<DealDTO> storeDeals = proxy.getStoreDeals(store_founder,storeName2);

            Assertions.assertEquals(memberDeals.get(0).products_amount.get("iphone 14"),1);
            Assertions.assertEquals(storeDeals.get(1).products_amount.get("iphone 14"),1);
            Assertions.assertEquals(memberDeals.get(0).products_prices.get("iphone 14"),3500.0);
            Assertions.assertEquals(storeDeals.get(1).products_prices.get("iphone 14"),3500.0);
            Assertions.assertEquals(memberDeals.get(0).productFinalPriceWithDiscount.get("iphone 14"),3500);
            Assertions.assertEquals(storeDeals.get(1).productFinalPriceWithDiscount.get("iphone 14"),3500);
            Assertions.assertEquals(memberDeals.get(0).productPriceMultipleAmount.get("iphone 14"),3500);
            Assertions.assertEquals(storeDeals.get(1).productPriceMultipleAmount.get("iphone 14"),3500);

            Assertions.assertEquals(memberDeals.get(0).date,storeDeals.get(1).date);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void purchase_with_deal_for_one_member_with_amount_success(){
        try{
            proxy.addToCart(member1_name, storeName2, "iphone 14",4);

            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member1_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));

            Assertions.assertTrue(proxy.getCartContent(member1_name).isEmpty());

            Assertions.assertFalse(proxy.getStoreDeals(store_founder,storeName2).isEmpty());
            Assertions.assertFalse(proxy.getMemberDeals(member1_name,member1_name).isEmpty());

            Assertions.assertEquals(proxy.getMemberDeals(member1_name,member1_name).get(0).totalPrice,proxy.getStoreDeals(store_founder,storeName2).get(0).totalPrice);
            List<DealDTO> memberDeals = proxy.getMemberDeals(member1_name,member1_name);
            List<DealDTO> storeDeals = proxy.getStoreDeals(store_founder,storeName2);

            Assertions.assertEquals(memberDeals.get(0).products_amount.get("iphone 14"),4);
            Assertions.assertEquals(storeDeals.get(0).products_amount.get("iphone 14"),4);
            Assertions.assertEquals(memberDeals.get(0).products_prices.get("iphone 14"),3500.0);
            Assertions.assertEquals(storeDeals.get(0).products_prices.get("iphone 14"),3500.0);
            Assertions.assertEquals(memberDeals.get(0).productFinalPriceWithDiscount.get("iphone 14"),3500*4);
            Assertions.assertEquals(storeDeals.get(0).productFinalPriceWithDiscount.get("iphone 14"),3500*4);
            Assertions.assertEquals(memberDeals.get(0).productPriceMultipleAmount.get("iphone 14"),3500*4);
            Assertions.assertEquals(storeDeals.get(0).productPriceMultipleAmount.get("iphone 14"),3500*4);

            Assertions.assertEquals(memberDeals.get(0).date,storeDeals.get(0).date);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void purchase_with_deal_for_lot_of_members_with_amount_success(){
        try{
            proxy.addToCart(member1_name, storeName2, "iphone 14",4);
            proxy.addToCart(member2_name,storeName2,"iphone 14",2);

            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member1_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member2_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));


            Assertions.assertTrue(proxy.getCartContent(member1_name).isEmpty());
            Assertions.assertTrue(proxy.getCartContent(member2_name).isEmpty());

            Assertions.assertFalse(proxy.getStoreDeals(store_founder,storeName2).isEmpty());
            Assertions.assertFalse(proxy.getMemberDeals(member1_name,member1_name).isEmpty());
            Assertions.assertFalse(proxy.getMemberDeals(member2_name,member2_name).isEmpty());

            Assertions.assertEquals(proxy.getMemberDeals(member1_name,member1_name).get(0).totalPrice,proxy.getStoreDeals(store_founder,storeName2).get(0).totalPrice);
            List<DealDTO> member1Deals = proxy.getMemberDeals(member1_name,member1_name);
            List<DealDTO> member2Deals = proxy.getMemberDeals(member2_name,member2_name);
            List<DealDTO> storeDeals = proxy.getStoreDeals(store_founder,storeName2);

            Assertions.assertEquals(member1Deals.get(0).products_amount.get("iphone 14"),4);
            Assertions.assertEquals(member2Deals.get(0).products_amount.get("iphone 14"),2);
            Assertions.assertEquals(storeDeals.get(0).products_amount.get("iphone 14"),4);
            Assertions.assertEquals(storeDeals.get(1).products_amount.get("iphone 14"),2);

            Assertions.assertEquals(member1Deals.get(0).products_prices.get("iphone 14"),3500.0);
            Assertions.assertEquals(member2Deals.get(0).products_prices.get("iphone 14"),3500.0);
            Assertions.assertEquals(storeDeals.get(0).products_prices.get("iphone 14"),3500.0);
            Assertions.assertEquals(storeDeals.get(1).products_prices.get("iphone 14"),3500.0);

            Assertions.assertEquals(member1Deals.get(0).productFinalPriceWithDiscount.get("iphone 14"),3500*4);
            Assertions.assertEquals(member2Deals.get(0).productFinalPriceWithDiscount.get("iphone 14"),3500*2);
            Assertions.assertEquals(storeDeals.get(0).productFinalPriceWithDiscount.get("iphone 14"),3500*4);
            Assertions.assertEquals(storeDeals.get(1).productFinalPriceWithDiscount.get("iphone 14"),3500*2);

            Assertions.assertEquals(member1Deals.get(0).productPriceMultipleAmount.get("iphone 14"),3500*4);
            Assertions.assertEquals(member2Deals.get(0).productPriceMultipleAmount.get("iphone 14"),3500*2);
            Assertions.assertEquals(storeDeals.get(0).productPriceMultipleAmount.get("iphone 14"),3500*4);
            Assertions.assertEquals(storeDeals.get(1).productPriceMultipleAmount.get("iphone 14"),3500*2);

            Assertions.assertEquals(member1Deals.get(0).date,storeDeals.get(0).date);
            Assertions.assertEquals(member2Deals.get(0).date,storeDeals.get(0).date);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void purchase_with_deal_for_lot_of_members_and_stores_with_amount_success(){
        try{
            proxy.addToCart(member1_name, storeName1, "iphone 14",4);
            proxy.addToCart(member2_name,storeName2,"iphone 14",2);

            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member1_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member2_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));


            Assertions.assertTrue(proxy.getCartContent(member1_name).isEmpty());
            Assertions.assertTrue(proxy.getCartContent(member2_name).isEmpty());

            Assertions.assertFalse(proxy.getStoreDeals(store_founder,storeName1).isEmpty());
            Assertions.assertFalse(proxy.getStoreDeals(store_founder,storeName2).isEmpty());
            Assertions.assertFalse(proxy.getMemberDeals(member1_name,member1_name).isEmpty());
            Assertions.assertFalse(proxy.getMemberDeals(member2_name,member2_name).isEmpty());

            Assertions.assertEquals(proxy.getMemberDeals(member1_name,member1_name).get(0).totalPrice,proxy.getStoreDeals(store_founder,storeName1).get(0).totalPrice);
            Assertions.assertEquals(proxy.getMemberDeals(member2_name,member2_name).get(0).totalPrice,proxy.getStoreDeals(store_founder,storeName2).get(0).totalPrice);

            List<DealDTO> member1Deals = proxy.getMemberDeals(member1_name,member1_name);
            List<DealDTO> member2Deals = proxy.getMemberDeals(member2_name,member2_name);
            List<DealDTO> store1Deals = proxy.getStoreDeals(store_founder,storeName1);
            List<DealDTO> store2Deals = proxy.getStoreDeals(store_founder,storeName2);

            Assertions.assertEquals(member1Deals.get(0).products_amount.get("iphone 14"),4);
            Assertions.assertEquals(member2Deals.get(0).products_amount.get("iphone 14"),2);
            Assertions.assertEquals(store1Deals.get(0).products_amount.get("iphone 14"),4);
            Assertions.assertEquals(store2Deals.get(0).products_amount.get("iphone 14"),2);

            Assertions.assertEquals(member1Deals.get(0).products_prices.get("iphone 14"),3000.0);
            Assertions.assertEquals(member2Deals.get(0).products_prices.get("iphone 14"),3500.0);
            Assertions.assertEquals(store1Deals.get(0).products_prices.get("iphone 14"),3000.0);
            Assertions.assertEquals(store2Deals.get(0).products_prices.get("iphone 14"),3500.0);

            Assertions.assertEquals(member1Deals.get(0).productFinalPriceWithDiscount.get("iphone 14"),3000*4);
            Assertions.assertEquals(member2Deals.get(0).productFinalPriceWithDiscount.get("iphone 14"),3500*2);
            Assertions.assertEquals(store1Deals.get(0).productFinalPriceWithDiscount.get("iphone 14"),3000*4);
            Assertions.assertEquals(store2Deals.get(0).productFinalPriceWithDiscount.get("iphone 14"),3500*2);

            Assertions.assertEquals(member1Deals.get(0).productPriceMultipleAmount.get("iphone 14"),3000*4);
            Assertions.assertEquals(member2Deals.get(0).productPriceMultipleAmount.get("iphone 14"),3500*2);
            Assertions.assertEquals(store1Deals.get(0).productPriceMultipleAmount.get("iphone 14"),3000*4);
            Assertions.assertEquals(store2Deals.get(0).productPriceMultipleAmount.get("iphone 14"),3500*2);

            Assertions.assertEquals(member1Deals.get(0).date,store1Deals.get(0).date);
            Assertions.assertEquals(member2Deals.get(0).date,store2Deals.get(0).date);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void purchase_with_deal_discount_success(){
        try{
            proxy.createProductDiscountPolicy(store_founder,storeName2,"iphone 14",20,true);
            Assertions.assertFalse(proxy.getAllStoreDiscountPolicies(store_founder,storeName2).isEmpty());



            proxy.addToCart(member1_name, storeName2, "iphone 14",4);
            proxy.addToCart(member2_name,storeName2,"iphone 14",2);

            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member1_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member2_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));


            Assertions.assertTrue(proxy.getCartContent(member1_name).isEmpty());
            Assertions.assertTrue(proxy.getCartContent(member2_name).isEmpty());

            Assertions.assertFalse(proxy.getStoreDeals(store_founder,storeName2).isEmpty());
            Assertions.assertFalse(proxy.getMemberDeals(member1_name,member1_name).isEmpty());
            Assertions.assertFalse(proxy.getMemberDeals(member2_name,member2_name).isEmpty());

            Assertions.assertEquals(proxy.getMemberDeals(member1_name,member1_name).get(0).totalPrice,proxy.getStoreDeals(store_founder,storeName2).get(0).totalPrice);
            List<DealDTO> member1Deals = proxy.getMemberDeals(member1_name,member1_name);
            List<DealDTO> member2Deals = proxy.getMemberDeals(member2_name,member2_name);
            List<DealDTO> storeDeals = proxy.getStoreDeals(store_founder,storeName2);

            Assertions.assertEquals(member1Deals.get(0).products_amount.get("iphone 14"),4);
            Assertions.assertEquals(member2Deals.get(0).products_amount.get("iphone 14"),2);
            Assertions.assertEquals(storeDeals.get(0).products_amount.get("iphone 14"),4);
            Assertions.assertEquals(storeDeals.get(1).products_amount.get("iphone 14"),2);

            Assertions.assertEquals(member1Deals.get(0).products_prices.get("iphone 14"),3500.0);
            Assertions.assertEquals(member2Deals.get(0).products_prices.get("iphone 14"),3500.0);
            Assertions.assertEquals(storeDeals.get(0).products_prices.get("iphone 14"),3500.0);
            Assertions.assertEquals(storeDeals.get(1).products_prices.get("iphone 14"),3500.0);

            Assertions.assertEquals(member1Deals.get(0).productFinalPriceWithDiscount.get("iphone 14"),(3500-(3500*0.2))*4);
            Assertions.assertEquals(member2Deals.get(0).productFinalPriceWithDiscount.get("iphone 14"),(3500-(3500*0.2))*2);
            Assertions.assertEquals(storeDeals.get(0).productFinalPriceWithDiscount.get("iphone 14"),(3500-(3500*0.2))*4);
            Assertions.assertEquals(storeDeals.get(1).productFinalPriceWithDiscount.get("iphone 14"),(3500-(3500*0.2))*2);

            Assertions.assertEquals(member1Deals.get(0).productPriceMultipleAmount.get("iphone 14"),3500*4);
            Assertions.assertEquals(member2Deals.get(0).productPriceMultipleAmount.get("iphone 14"),3500*2);
            Assertions.assertEquals(storeDeals.get(0).productPriceMultipleAmount.get("iphone 14"),3500*4);
            Assertions.assertEquals(storeDeals.get(1).productPriceMultipleAmount.get("iphone 14"),3500*2);

            Assertions.assertEquals(member1Deals.get(0).date,storeDeals.get(0).date);
            Assertions.assertEquals(member2Deals.get(0).date,storeDeals.get(0).date);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void purchase_with_deal_for_lot_of_members_and_stores_with_amount_with_discount_success(){
        try{
            proxy.createProductDiscountPolicy(store_founder,storeName2,"iphone 14",20,true);
            Assertions.assertFalse(proxy.getAllStoreDiscountPolicies(store_founder,storeName2).isEmpty());


            proxy.addToCart(member1_name, storeName1, "iphone 14",4);
            proxy.addToCart(member2_name,storeName2,"iphone 14",2);

            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member1_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member2_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));


            Assertions.assertTrue(proxy.getCartContent(member1_name).isEmpty());
            Assertions.assertTrue(proxy.getCartContent(member2_name).isEmpty());

            Assertions.assertFalse(proxy.getStoreDeals(store_founder,storeName1).isEmpty());
            Assertions.assertFalse(proxy.getStoreDeals(store_founder,storeName2).isEmpty());
            Assertions.assertFalse(proxy.getMemberDeals(member1_name,member1_name).isEmpty());
            Assertions.assertFalse(proxy.getMemberDeals(member2_name,member2_name).isEmpty());

            Assertions.assertEquals(proxy.getMemberDeals(member1_name,member1_name).get(0).totalPrice,proxy.getStoreDeals(store_founder,storeName1).get(0).totalPrice);
            Assertions.assertEquals(proxy.getMemberDeals(member2_name,member2_name).get(0).totalPrice,proxy.getStoreDeals(store_founder,storeName2).get(0).totalPrice);

            List<DealDTO> member1Deals = proxy.getMemberDeals(member1_name,member1_name);
            List<DealDTO> member2Deals = proxy.getMemberDeals(member2_name,member2_name);
            List<DealDTO> store1Deals = proxy.getStoreDeals(store_founder,storeName1);
            List<DealDTO> store2Deals = proxy.getStoreDeals(store_founder,storeName2);

            Assertions.assertEquals(member1Deals.get(0).products_amount.get("iphone 14"),4);
            Assertions.assertEquals(member2Deals.get(0).products_amount.get("iphone 14"),2);
            Assertions.assertEquals(store1Deals.get(0).products_amount.get("iphone 14"),4);
            Assertions.assertEquals(store2Deals.get(0).products_amount.get("iphone 14"),2);

            Assertions.assertEquals(member1Deals.get(0).products_prices.get("iphone 14"),3000.0);
            Assertions.assertEquals(member2Deals.get(0).products_prices.get("iphone 14"),3500.0);
            Assertions.assertEquals(store1Deals.get(0).products_prices.get("iphone 14"),3000.0);
            Assertions.assertEquals(store2Deals.get(0).products_prices.get("iphone 14"),3500.0);

            Assertions.assertEquals(member1Deals.get(0).productFinalPriceWithDiscount.get("iphone 14"),3000*4);
            Assertions.assertEquals(member2Deals.get(0).productFinalPriceWithDiscount.get("iphone 14"),(3500-(3500*0.2))*2);
            Assertions.assertEquals(store1Deals.get(0).productFinalPriceWithDiscount.get("iphone 14"),3000*4);
            Assertions.assertEquals(store2Deals.get(0).productFinalPriceWithDiscount.get("iphone 14"),(3500-(3500*0.2))*2);

            Assertions.assertEquals(member1Deals.get(0).productPriceMultipleAmount.get("iphone 14"),3000*4);
            Assertions.assertEquals(member2Deals.get(0).productPriceMultipleAmount.get("iphone 14"),3500*2);
            Assertions.assertEquals(store1Deals.get(0).productPriceMultipleAmount.get("iphone 14"),3000*4);
            Assertions.assertEquals(store2Deals.get(0).productPriceMultipleAmount.get("iphone 14"),3500*2);

            Assertions.assertEquals(member1Deals.get(0).date,store1Deals.get(0).date);
            Assertions.assertEquals(member2Deals.get(0).date,store2Deals.get(0).date);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void purchase_with_deal_discount_make_price_minus_success(){
        try{
            Integer discount1 = proxy.createProductDiscountPolicy(store_founder,storeName2,"iphone 14",40,false);
            Assertions.assertTrue(proxy.getAllStoreDiscountPolicies(store_founder,storeName2).isEmpty());

            Integer discount2 =  proxy.createProductDiscountPolicy(store_founder,storeName2,"iphone 14",70,false);
            Assertions.assertTrue(proxy.getAllStoreDiscountPolicies(store_founder,storeName2).isEmpty());

            proxy.createAdditionDiscountPolicy(store_founder,storeName2,discount1, discount2,true);
            Assertions.assertFalse(proxy.getAllStoreDiscountPolicies(store_founder,storeName2).isEmpty());



            proxy.addToCart(member1_name, storeName2, "iphone 14",4);
            proxy.addToCart(member2_name,storeName2,"iphone 14",2);

            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member1_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member2_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));


            Assertions.assertTrue(proxy.getCartContent(member1_name).isEmpty());
            Assertions.assertTrue(proxy.getCartContent(member2_name).isEmpty());

            Assertions.assertFalse(proxy.getStoreDeals(store_founder,storeName2).isEmpty());
            Assertions.assertFalse(proxy.getMemberDeals(member1_name,member1_name).isEmpty());
            Assertions.assertFalse(proxy.getMemberDeals(member2_name,member2_name).isEmpty());

            Assertions.assertEquals(proxy.getMemberDeals(member1_name,member1_name).get(0).totalPrice,proxy.getStoreDeals(store_founder,storeName2).get(0).totalPrice);
            List<DealDTO> member1Deals = proxy.getMemberDeals(member1_name,member1_name);
            List<DealDTO> member2Deals = proxy.getMemberDeals(member2_name,member2_name);
            List<DealDTO> storeDeals = proxy.getStoreDeals(store_founder,storeName2);

            Assertions.assertEquals(member1Deals.get(0).products_amount.get("iphone 14"),4);
            Assertions.assertEquals(member2Deals.get(0).products_amount.get("iphone 14"),2);
            Assertions.assertEquals(storeDeals.get(0).products_amount.get("iphone 14"),4);
            Assertions.assertEquals(storeDeals.get(1).products_amount.get("iphone 14"),2);

            Assertions.assertEquals(member1Deals.get(0).products_prices.get("iphone 14"),3500.0);
            Assertions.assertEquals(member2Deals.get(0).products_prices.get("iphone 14"),3500.0);
            Assertions.assertEquals(storeDeals.get(0).products_prices.get("iphone 14"),3500.0);
            Assertions.assertEquals(storeDeals.get(1).products_prices.get("iphone 14"),3500.0);

            Assertions.assertEquals(member1Deals.get(0).productFinalPriceWithDiscount.get("iphone 14"),0.0);
            Assertions.assertEquals(member2Deals.get(0).productFinalPriceWithDiscount.get("iphone 14"),0.0);
            Assertions.assertEquals(storeDeals.get(0).productFinalPriceWithDiscount.get("iphone 14"),0.0);
            Assertions.assertEquals(storeDeals.get(1).productFinalPriceWithDiscount.get("iphone 14"),0.0);

            Assertions.assertEquals(member1Deals.get(0).productPriceMultipleAmount.get("iphone 14"),3500*4);
            Assertions.assertEquals(member2Deals.get(0).productPriceMultipleAmount.get("iphone 14"),3500*2);
            Assertions.assertEquals(storeDeals.get(0).productPriceMultipleAmount.get("iphone 14"),3500*4);
            Assertions.assertEquals(storeDeals.get(1).productPriceMultipleAmount.get("iphone 14"),3500*2);

            Assertions.assertEquals(member1Deals.get(0).date,storeDeals.get(0).date);
            Assertions.assertEquals(member2Deals.get(0).date,storeDeals.get(0).date);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

}
