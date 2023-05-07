package AcceptanceTests;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class PurchasingTests {

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

    @BeforeEach
    public void setUp() throws Exception {
        proxy = new ProxyBridge(new RealBridge());
        if (!proxy.initializeMarket()) {
            throw new Exception("");
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
        proxy.addNewProductToStock(store_founder, storeName1, "gaming chair 1", "Gaming chairs", 600.0, "red", 50);

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
        member_name = proxy.login(user, "Harry123", "Hh123456");


        // owner
        user = proxy.enterMarket();//guest default user name
        proxy.register(user, "Joe123", "Jj456789");
        owner = proxy.login(user, "Joe123", "Jj456789");

        // manager
        user = proxy.enterMarket();//guest default user name
        proxy.register(user, "Tom123", "Tt123456");
        manager = proxy.login(user, "Tom123", "Tt123456");
    }

    @Test
    public void purchase_cart_success(){
        try{
            proxy.addToCart(guest_name, storeName2, "gaming mouse 1", 3);
            proxy.addToCart(member_name, storeName2, "iphone 14",1);
            int gaming_mouse_amount_in_stock = 70;
            int iphone_14_amount_in_stock = 100;
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(guest_name,user1_cardNumber,user1_month,user1_year,user1_holder,user1_cvv,user1_id,user1_receiverName,user1_shipmentAddress,user1_shipmentCity,user1_shipmentCountry,user1_zipCode));
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(guest_name).isEmpty());
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
            //todo: we have to check that the amount in the stock decreased
            //Assertions.assertEquals(gaming_mouse_amount_in_stock-3,proxy.getProductAmount(storeName2,"gaming mouse 1"));
            //Assertions.assertEquals(iphone_14_amount_in_stock-1,proxy.getProductAmount(storeName2,"iphone 14"));
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    /**
     * two users wants to buy the same product,
     *one of them success -> the amount in the stock decreased, the second when he tries fail
     */
    @Test
    public void purchase_cart_fail_bad_amount(){
        try{
            proxy.addToCart(guest_name, storeName2, "gaming mouse 1", 60);
            proxy.addToCart(member_name, storeName2, "gaming mouse 1",15);
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(guest_name,user1_cardNumber,user1_month,user1_year,user1_holder,user1_cvv,user1_id,user1_receiverName,user1_shipmentAddress,user1_shipmentCity,user1_shipmentCountry,user1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(guest_name).isEmpty());
            Assertions.assertThrows(Exception.class,()-> proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertFalse(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    /**
     * user tries to buy more than one product, each product in a specific store,
     * the last store haven't the ordered amount.
     */
    @Test
    public void purchase_cart_fail_good_amount_bad_amount(){
        try{
            proxy.addToCart(guest_name, storeName2, "gaming mouse 1", 60);
            proxy.addToCart(member_name, storeName1, "iphone 14",3);
            proxy.addToCart(member_name, storeName2, "gaming mouse 1",15);
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(guest_name,user1_cardNumber,user1_month,user1_year,user1_holder,user1_cvv,user1_id,user1_receiverName,user1_shipmentAddress,user1_shipmentCity,user1_shipmentCountry,user1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(guest_name).isEmpty());
            Assertions.assertThrows(Exception.class,()-> proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertFalse(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    /**
     * user tries to buy more than one product, each product in a specific store,
     * the last store haven't the ordered amount.
     */
    /*@Test
    public void purchase_cart_fail_good_amount_bad_amount2(){
        try{
            proxy.addToCart(guest_name, storeName2, "gaming mouse 1", 60);
            proxy.addToCart(member_name, storeName1, "iphone 14",3);
            proxy.addToCart(member_name, storeName2, "gaming mouse 1",15);
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(guest_name,user1_cardNumber,user1_month,user1_year,user1_holder,user1_cvv,user1_id,user1_receiverName,user1_shipmentAddress,user1_shipmentCity,user1_shipmentCountry,user1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(guest_name).isEmpty());
            proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode);
            Assertions.assertFalse(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            try {
                Assertions.assertEquals(50, proxy.getProductAmount(storeName1, "iphone 14"));
            }catch (Exception e1){
                Assertions.fail(e1.getMessage());
                Assertions.fail(e.getMessage());
            }
        }
    }*/

    /*@Test
    public void purchase_cart_fail_wrong_card_details(){
        try{
            proxy.addToCart(member_name, storeName2, "gaming mouse 1",15);
            Assertions.assertThrows(Exception.class,()-> proxy.purchaseCartByCreditCard(member_name,user1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertFalse(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }*/

    /**
     * put a policy and buy the cart with good amount for the policy
     */
    @Test
    public void purchase_payment_policy_max_product_amount_success(){
        try{
            proxy.createMaxProductAmountAllContentBagConstraint(store_founder,storeName1,"iphone 14",5,true);
            proxy.addToCart(member_name, storeName1, "iphone 14",3);
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    /**
     * put a policy and buy the cart with bad amount for the policy
     */
  /*  @Test
    public void purchase_payment_policy_max_product_amount_fail(){
        try{
            proxy.createMaxProductAmountAllContentBagConstraint(store_founder,storeName1,"iphone 14",5,true);
            proxy.addToCart(member_name, storeName1, "iphone 14",10);
            Assertions.assertThrows(Exception.class,()-> proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertFalse(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }*/

    /**
     * put a policy and buy the cart with good amount for the policy
     */
    @Test
    public void purchase_payment_policy_min_product_amount_success(){
        try{
            proxy.createMinProductAmountAllContentBagConstraint(store_founder,storeName1,"iphone 14",5,true);
            proxy.addToCart(member_name, storeName1, "iphone 14",6);
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    /**
     * put a policy and buy the cart with bad amount for the policy
     */
  /*  @Test
    public void purchase_payment_policy_min_product_amount_fail(){
        try{
            proxy.createMinProductAmountAllContentBagConstraint(store_founder,storeName1,"iphone 14",5,true);
            proxy.addToCart(member_name, storeName1, "iphone 14",3);
            Assertions.assertThrows(Exception.class,()-> proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertFalse(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }*/

    /**
     * put a policy and buy the cart with time i cant buy the product for the policy
     */
    @Test
    public void purchase_payment_policy_product_time_at_day_success(){
        try{
            proxy.addToCart(member_name, storeName1, "iphone 14",6);
            proxy.createMaxTimeAtDayProductBagConstraint(store_founder,storeName1,"iphone 14",java.time.LocalTime.now().getHour()+1,java.time.LocalTime.now().getMinute(),true);
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    /**
     * put a policy and buy the cart with time i cant buy the product for the policy
     */
  /*  @Test
    public void purchase_payment_policy_max_product_time_at_day_fail(){
        try{
            proxy.addToCart(member_name, storeName1, "iphone 14",6);
            proxy.createMaxTimeAtDayProductBagConstraint(store_founder,storeName1,"iphone 14",java.time.LocalTime.now().getHour()-1,java.time.LocalTime.now().getMinute(),true);
            Assertions.assertThrows(Exception.class,()-> proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }*/

    /**
     * put a policy and buy the cart with range fo days that i ant buy the product for the policy
     */
    @Test
    public void purchase_payment_policy_range_of_days_product_success(){
        try{
            proxy.addToCart(member_name, storeName1, "iphone 14",6);
            proxy.createRangeOfDaysProductBagConstraint(store_founder,storeName1,"iphone 14",java.time.LocalDate.now().getYear()+1,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),java.time.LocalDate.now().getYear()+2,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),true);
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    /**
     * put a policy and buy the cart with range fo days that i ant buy the product for the policy
     */
   /* @Test
    public void purchase_payment_policy_range_of_days_product_fail(){
        try{
            proxy.addToCart(member_name, storeName1, "iphone 14",6);
            proxy.createRangeOfDaysProductBagConstraint(store_founder,storeName1,"iphone 14",java.time.LocalDate.now().getYear()-1,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),java.time.LocalDate.now().getYear()+1,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),true);
            Assertions.assertThrows(Exception.class,()-> proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }
*/

    /**
     * put a policy and buy the cart with time i cant buy the specific category for the policy
     */
    @Test
    public void purchase_payment_policy_max_category_time_at_day_success(){
        try{
            proxy.addToCart(member_name, storeName1, "iphone 14",6);
            proxy.createMaxTimeAtDayCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalTime.now().getHour()+1,java.time.LocalTime.now().getMinute(),true);
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }


    /**
     * put a policy and buy the cart with time i cant buy the specific category for the policy
     */
  /*  @Test
    public void purchase_payment_policy_max_category_time_at_day_fail(){
        try{
            proxy.addToCart(member_name, storeName1, "iphone 14",6);
            proxy.createMaxTimeAtDayCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalTime.now().getHour()-1,java.time.LocalTime.now().getMinute(),true);
            Assertions.assertThrows(Exception.class,()-> proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }*/

    /**
     * put a policy and buy the cart with range fo days that i ant buy the specific category for the policy
     */
    @Test
    public void purchase_payment_policy_range_of_days_category_success(){
        try{
            proxy.addToCart(member_name, storeName1, "iphone 14",6);
            proxy.createRangeOfDaysCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalDate.now().getYear()+1,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),java.time.LocalDate.now().getYear()+2,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),true);
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    /**
     * put a policy and buy the cart with range fo days that i ant buy the specific category for the policy
     */
  /*  @Test
    public void purchase_payment_policy_range_of_days_category_fail(){
        try{
            proxy.addToCart(member_name, storeName1, "iphone 14",6);
            proxy.createRangeOfDaysCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalDate.now().getYear()+1,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),java.time.LocalDate.now().getYear()+2,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),true);
            Assertions.assertThrows(Exception.class,()-> proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }*/

    /**
     * And of payment policies
     */
    @Test
    public void purchase_AND_payment_policy_success(){
        try{
            Integer paymentPolicy1 = proxy.createRangeOfDaysCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalDate.now().getYear()+1,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),java.time.LocalDate.now().getYear()+2,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),true);
            Integer paymentPolicy2 = proxy.createMaxTimeAtDayCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalTime.now().getHour()+1,java.time.LocalTime.now().getMinute(),true);
            proxy.createAndBagConstraint(store_founder,storeName1,paymentPolicy1,paymentPolicy2,true);
            proxy.addToCart(member_name, storeName1, "iphone 14",6);
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    /**
     * And of payment policies
     */
   /* @Test
    public void purchase_AND_payment_policy_fail(){
        try{
            Integer paymentPolicy1 = proxy.createRangeOfDaysCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalDate.now().getYear()-1,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),java.time.LocalDate.now().getYear()+1,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),true);
            Integer paymentPolicy2 = proxy.createMaxTimeAtDayCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalTime.now().getHour()-1,java.time.LocalTime.now().getMinute(),true);
            proxy.createAndBagConstraint(store_founder,storeName1,paymentPolicy1,paymentPolicy2,true);
            proxy.addToCart(member_name, storeName1, "iphone 14",6);
            Assertions.assertThrows(Exception.class,()-> proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }*/

    /**
     or of payment policies
     */
    @Test
    public void purchase_OR_payment_policy_success(){
        try{
            Integer paymentPolicy1 = proxy.createRangeOfDaysCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalDate.now().getYear()+1,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),java.time.LocalDate.now().getYear()+2,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),true);
            Integer paymentPolicy2 = proxy.createMaxTimeAtDayCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalTime.now().getHour()-1,java.time.LocalTime.now().getMinute(),true);
            proxy.createOrBagConstraint(store_founder,storeName1,paymentPolicy1,paymentPolicy2,true);
            proxy.addToCart(member_name, storeName1, "iphone 14",6);
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    /**
     * or of payment policies
     */
 /*   @Test
    public void purchase_OR_payment_policy_fail(){
        try{
            Integer paymentPolicy1 = proxy.createRangeOfDaysCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalDate.now().getYear()-1,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),java.time.LocalDate.now().getYear()+2,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),true);
            Integer paymentPolicy2 = proxy.createMaxTimeAtDayCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalTime.now().getHour()-1,java.time.LocalTime.now().getMinute(),true);
            proxy.createOrBagConstraint(store_founder,storeName1,paymentPolicy1,paymentPolicy2,true);
            proxy.addToCart(member_name, storeName1, "iphone 14",6);
            Assertions.assertThrows(Exception.class,()-> proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }*/

    /**
     * onlyif of payment policies
     */
    @Test
    public void purchase_OnlyIf_payment_policy_success(){
        try{
            Integer paymentPolicy1 = proxy.createRangeOfDaysCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalDate.now().getYear()+1,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),java.time.LocalDate.now().getYear()+2,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),true);
            Integer paymentPolicy2 = proxy.createMaxTimeAtDayCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalTime.now().getHour()+1,java.time.LocalTime.now().getMinute(),true);
            proxy.createOnlyIfBagConstraint(store_founder,storeName1,paymentPolicy1,paymentPolicy2,true);
            proxy.addToCart(member_name, storeName1, "iphone 14",6);
            Assertions.assertTrue(proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    /**
     * onlyif of payment policies
     */
 /*   @Test
    public void purchase_OnlyIf_payment_policy_fail(){
        try{
            Integer paymentPolicy1 = proxy.createRangeOfDaysCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalDate.now().getYear()+1,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),java.time.LocalDate.now().getYear()+2,java.time.LocalDate.now().getMonthValue(),java.time.LocalDate.now().getDayOfMonth(),true);
            Integer paymentPolicy2 = proxy.createMaxTimeAtDayCategoryBagConstraint(store_founder,storeName1,"Iphones",java.time.LocalTime.now().getHour()-1,java.time.LocalTime.now().getMinute(),true);
            proxy.createOrBagConstraint(store_founder,storeName1,paymentPolicy1,paymentPolicy2,true);
            proxy.addToCart(member_name, storeName1, "iphone 14",6);
            Assertions.assertThrows(Exception.class,()-> proxy.purchaseCartByCreditCard(member_name,member1_cardNumber,member1_month,member1_year,member1_holder,member1_cvv,member1_id,member1_receiverName,member1_shipmentAddress,member1_shipmentCity,member1_shipmentCountry,member1_zipCode));
            Assertions.assertTrue(proxy.getCartContent(member_name).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }*/
}
