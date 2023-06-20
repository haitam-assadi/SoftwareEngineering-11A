package UnitTests;

import DTO.*;
import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.Controller.StoreMapper;
import DomainLayer.*;

import static org.junit.jupiter.api.Assertions.*;

import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
class StoreTest {

    private Store store;

    @Mock
    private StoreFounder founder;
    @Mock
    private StoreOwner owner;

    @Mock
    private Deal deal;

    @Mock
    private DealDTO dealDTO;

    @Mock
    private Stock stock;

    @Mock
    private StoreManager manager;

    @Mock
    private Member member1name;
    @Mock
    private Member member2name;

    @BeforeAll
    public void setUp() throws Exception {
        Market.dbFlag = false;
        StoreMapper.initMapper();
        MemberMapper.initMapper();
        MockitoAnnotations.openMocks(this);
        member1name = new Member("member1","member1Pass");
        member2name = new Member("member2","member2Pass");
        store = new Store("store1");
        member1name.appointMemberAsStoreFounder(store);
        founder = new StoreFounder(member1name);
        //TODO: does not compile
        //store = new Store(founder);
    }

    @BeforeEach
    public void beforeEachTest() throws Exception {
        MockitoAnnotations.openMocks(this);
        StoreMapper.initMapper();
        MemberMapper.initMapper();
        member1name = new Member("member1","member1Pass");
        member2name = new Member("member2","member2Pass");
        store = new Store("store1");
        member1name.appointMemberAsStoreFounder(store);
        founder = new StoreFounder(member1name);
        //TODO: does not compile
        //store = new Store(founder);
    }

    /*
        Cases:
            1. amount is negative - failure
            2. price is negative - failure
            3. details is null - failure
            4. details is empty - failure
            5. details length is more than 300 letters - failure
            6. function called by someone else rather than storeFounder or storeOwner - failure
            7. function called by storeFounder - success
            8. function called by storeOwner -  success
    */

    @Test
    public void close_store_success() throws Exception {
            Assertions.assertTrue(store.isActive());
            Assertions.assertTrue(store.closeStore(founder.getUserName()));
            Assertions.assertFalse(store.isActive());
    }

    @Test
    public void close_store_fail(){
            Assertions.assertThrows(Exception.class, () -> store.closeStore(member2name.getUserName()));
    }


    @Test
    void add_new_product_to_stock_with_negative_amount_fail() {
        assertThrows(Exception.class,
                () -> {store.addNewProductToStock("Adel", "milk 3%", "milk", 5.90, "fresh milk", -5);});
    }

    @Test
    void add_new_product_to_stock_with_negative_price_fail() {
        assertThrows(Exception.class,
                () -> {store.addNewProductToStock("Adel", "milk 3%", "milk", -5.90, "fresh milk", 20);});
    }

    @Test
    void add_new_product_to_stock_with_null_description_fail() {
        assertThrows(Exception.class,
                () -> {store.addNewProductToStock("Adel", "milk 3%", "milk", 5.90, null, 20);});
    }

    @Test
    void add_new_product_to_stock_with_empty_description_fail() {
        assertThrows(Exception.class,
                () -> {store.addNewProductToStock("Adel", "milk 3%", "milk", 5.90, "", 20);});
    }

    @Test
    void add_new_product_to_stock_with_too_long_details_fail() {
        String details = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor." +
                " Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes," +
                " nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem." +
                " Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate.";
        assertThrows(Exception.class,
                () -> {store.addNewProductToStock("Adel", "milk 3%", "milk", 5.90, details, 20);});
    }

    @Test
    void add_new_product_to_stock_without_founder_or_owner_fail() {
        assertThrows(Exception.class,
                () -> {store.addNewProductToStock(member2name.getUserName(), "milk 3%", "milk", 5.90, "fresh milk", 5);});
    }

    @Test
    void add_new_product_to_stock_by_founder_success() throws Exception {
        assertTrue(store.addNewProductToStock(member1name.getUserName(), "milk 3%", "milk", 5.90, "fresh milk", 5));
    }

    @Test
    void add_new_product_to_stock_by_owner_success() throws Exception {
        StoreOwner storeOwner = new StoreOwner(member2name);
        store.setStoreOwner("member2",storeOwner);
        assertTrue(store.addNewProductToStock("member2", "milk 3%", "milk", 5.90, "fresh milk", 5));
        assertTrue(store.containsProduct("milk 3%"));

    }

    /*
        Cases:
            1. function called by store owner - success
            2. function called by store founder - success
            3. function called by someone else rather than store owner or store founder - failure
    */

    @Test
    void remove_product_from_stock_by_store_owner_success() throws Exception {
        StoreOwner storeOwner = new StoreOwner(member2name);
        store.setStoreOwner("member2",storeOwner);
        assertTrue(store.addNewProductToStock("member2", "milk 3%", "milk", 5.90, "fresh milk", 5));
        assertTrue(store.containsProduct("milk 3%"));
        assertTrue(store.removeProductFromStock(member2name.getUserName(),"milk 3%"));
        assertFalse(store.containsProduct("milk 3%"));

    }

    @Test
    void remove_product_from_stock_by_store_founder_success() throws Exception {
        assertTrue(store.addNewProductToStock(founder.getUserName(), "milk 3%", "milk", 5.90, "fresh milk", 5));
        assertTrue(store.containsProduct("milk 3%"));
        assertTrue(store.removeProductFromStock(founder.getUserName(),"milk 3%"));
        assertFalse(store.containsProduct("milk 3%"));
    }

    @Test
    void add_product_to_stock_by_store_owner_and_delete_it_by_his_founder_success() throws Exception {
        StoreOwner storeOwner = new StoreOwner(member2name);
        store.setStoreOwner("member2",storeOwner);
        assertTrue(store.addNewProductToStock("member2", "milk 3%", "milk", 5.90, "fresh milk", 5));
        assertTrue(store.containsProduct("milk 3%"));
        assertTrue(store.removeProductFromStock(founder.getUserName(),"milk 3%"));
        assertFalse(store.containsProduct("milk 3%"));
    }

    @Test
    void remove_product_from_stock_without_store_founder_or_owner_fail() throws Exception {
        assertTrue(store.addNewProductToStock(founder.getUserName(), "milk 3%", "milk", 5.90, "fresh milk", 5));
        assertTrue(store.containsProduct("milk 3%"));
        Assertions.assertThrows(Exception.class, () -> store.removeProductFromStock(member2name.getUserName(),"milk 3%"));
    }

    /*
        Cases:
            1. product description is equal to null - failure
            2. product description is empty - failure
            3. product description is too long (> 300) - failure
            4. function called by someone else rather than store owner or store founder - failure
            5. function called by store owner - success
            6. function called by store founder - success
    */

    @Test
    void update_product_description_with_null_description_fail() {
        assertThrows(Exception.class,
                () -> {store.updateProductDescription("Adel", "milk 3%", null);});
    }

    @Test
    void update_product_description_with_empty_description_fail() {
        assertThrows(Exception.class,
                () -> {store.updateProductDescription("Adel", "milk 3%", "");});
    }

    @Test
    void update_product_description_with_too_long_description_fail() {
        String details = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor." +
                " Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes," +
                " nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem." +
                " Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate.";
        assertThrows(Exception.class,
                () -> {store.updateProductDescription("Adel", "milk 3%", details);});
    }

    @Test
    void update_product_description_without_owner_or_founder_fail() throws Exception {
        assertThrows(Exception.class,
                () -> {store.updateProductDescription(member2name.getUserName(), "milk 3%", "fresh milk");});
    }

    @Test
    void update_product_description_by_owner_success() throws Exception {
        StoreOwner storeOwner = new StoreOwner(member2name);
        store.setStoreOwner("member2",storeOwner);
        assertTrue(store.addNewProductToStock("member2", "milk 3%", "milk", 5.90, "fresh milk", 5));
        assertTrue(store.containsProduct("milk 3%"));
        assertTrue(store.updateProductDescription("member2", "milk 3%", "fresh milk"));
    }

    @Test
    void update_product_description_by_founder_success() throws Exception {
        assertTrue(store.addNewProductToStock(founder.getUserName(), "milk 3%", "milk", 5.90, "fresh milk", 5));
        assertTrue(store.containsProduct("milk 3%"));
        assertTrue(store.updateProductDescription(founder.getUserName(), "milk 3%", "fresh milk"));
    }

    @Test
    void add_product_by_owner_and_update_product_description_by_founder_success() throws Exception {
        StoreOwner storeOwner = new StoreOwner(member2name);
        store.setStoreOwner("member2",storeOwner);
        assertTrue(store.addNewProductToStock("member2", "milk 3%", "milk", 5.90, "fresh milk", 5));
        assertTrue(store.containsProduct("milk 3%"));
        assertTrue(store.updateProductDescription(founder.getUserName(), "milk 3%", "fresh milk"));
    }

    /*
        Cases:
            1. amount is negative - failure
            2. function called by someone else rather than store owner or store founder - failure
            3. function called by store owner - success
            4. function called by store founder - success
    */

    @Test
    void update_product_amount_with_negative_amount_fail() {
        assertThrows(Exception.class,
                () -> {store.updateProductAmount(founder.getUserName(), "milk 3%", -20);});
    }

    @Test
    void update_product_amount_without_owner_or_founder_fail() {
        assertThrows(Exception.class,
                () -> {store.updateProductAmount(member2name.getUserName(), "milk 3%", 20);});
    }

    @Test
    void update_product_amount_called_by_owner_success() throws Exception {
        StoreOwner storeOwner = new StoreOwner(member2name);
        store.setStoreOwner("member2",storeOwner);
        assertTrue(store.addNewProductToStock("member2", "milk 3%", "milk", 5.90, "fresh milk", 5));
        assertTrue(store.containsProduct("milk 3%"));
        assertTrue(store.updateProductAmount(member2name.getUserName(), "milk 3%", 20));
    }

    @Test
    void update_product_amount_called_by_founder_success() throws Exception {
        assertTrue(store.addNewProductToStock(founder.getUserName(), "milk 3%", "milk", 5.90, "fresh milk", 5));
        assertTrue(store.containsProduct("milk 3%"));
        assertTrue(store.updateProductAmount(founder.getUserName(), "milk 3%", 20));
    }

    /*
        Cases:
            1. price is negative - failure
            2. function called by someone else rather than store owner or store founder - failure
            3. function called by store owner - success
            4. function called by store founder - success
            5. price is zero - failure
    */

    @Test
    void update_product_with_price_fail() {
        assertThrows(Exception.class,
                () -> {store.updateProductPrice(founder.getUserName(), "milk 3%", -5.90);});
    }

    @Test
    void update_product_price_with_zero_fail() {
        double newPrice = 0;
        assertThrows(Exception.class,
                () -> {store.updateProductPrice(founder.getUserName(), "milk 3%", newPrice);});
    }

    @Test
    void update_product_price_without_owner_and_founder_fail() {
        assertThrows(Exception.class,
                () -> {store.updateProductPrice(member2name.getUserName(), "milk 3%", 5.90);});
    }

    @Test
    void update_product_price_equal_to_the_old_price_fail() {
        assertThrows(Exception.class,
                () -> {store.updateProductPrice(founder.getUserName(), "milk 3%", 5.90);});
    }

    @Test
    void update_product_price_called_by_owner_success() throws Exception {
        StoreOwner storeOwner = new StoreOwner(member2name);
        store.setStoreOwner("member2",storeOwner);
        assertTrue(store.addNewProductToStock("member2", "milk 3%", "milk", 5.90, "fresh milk", 5));
        assertTrue(store.containsProduct("milk 3%"));
        assertTrue(store.updateProductPrice(member2name.getUserName(), "milk 3%", 6.90));
    }

    @Test
    void update_product_price_called_by_founder_success() throws Exception {
        assertTrue(store.addNewProductToStock(founder.getUserName(), "milk 3%", "milk", 5.90, "fresh milk", 5));
        assertTrue(store.containsProduct("milk 3%"));
        assertTrue(store.updateProductPrice(founder.getUserName(), "milk 3%", 6.90));
    }

    /*
        Cases:
            1. function called by store founder - success
            2. function called by store owner - success
            3. function called by someone else rather than founder or owner - fail
    */

    @Test
    void is_already_store_owner_called_by_owner_success() throws Exception {
        StoreOwner storeOwner = new StoreOwner(member2name);
        store.setStoreOwner("member2",storeOwner);
        assertTrue(store.isAlreadyStoreOwner("member2"));
    }

    @Test
    void is_already_store_owner_called_by_founder_success() throws Exception {
        assertTrue(store.isAlreadyStoreOwner(founder.getUserName()));
    }

    @Test
    void is_already_store_owner_without_founder_or_owner_fail() throws Exception {
        assertFalse(store.isAlreadyStoreOwner(member2name.getUserName()));
    }

    /*
        Cases:
            1. function called by store manager - success
            2. function called by someone else rather than founder or owner - fail
            3. function called by store owner - fail
    */

    @Test
    void is_already_store_manager_called_by_manager_success() throws Exception {
        StoreManager storeManager = new StoreManager(member2name);
        store.setStoreManager("member2",storeManager);
        assertTrue(store.isAlreadyStoreManager("member2"));
    }

    @Test
    void is_already_store_manager_called_by_owner_fail() throws Exception {
        assertFalse(store.isAlreadyStoreManager(founder.getUserName()));
    }

    @Test
    void is_already_store_manager_without_manager_fail() throws Exception {
        assertFalse(store.isAlreadyStoreManager(member2name.getUserName()));
    }

    /*
        Cases:
            1. function called with an appointed store owner - failure
            2. function called with a member that is not an owner already - success
    */

//    @org.junit.jupiter.api.Test
//    void appoint_appointed_owner_as_store_owner_fail() {
//        Mockito.when(owner.getUserName()).thenReturn("Adam");
//        store.setStoreOwner("Adam", owner);
//        assertThrows(Exception.class,
//                () -> {store.appointMemberAsStoreOwner(owner);});
//    }
//
//    @org.junit.jupiter.api.Test
//    void appoint_member_as_store_owner_success() throws Exception {
//        Mockito.when(owner.getUserName()).thenReturn("Adam");
//        store.appointMemberAsStoreOwner(owner);
//        ConcurrentHashMap<String, StoreOwner> owners = store.getStoreOwners();
//        assertEquals(1, owners.size());
//    }
//
//    /*
//        Cases:
//            1. function called with an appointed store manager - failure
//            2. function called with a member that is not a manager already - success
//    */
//
//    @org.junit.jupiter.api.Test
//    void appoint_appointed_manager_as_store_manager_fail() {
//        Mockito.when(manager.getUserName()).thenReturn("Asaf");
//        store.setStoreManager("Asaf", manager);
//        assertThrows(Exception.class,
//                () -> {store.appointMemberAsStoreManager(manager);});
//    }
//
//    @org.junit.jupiter.api.Test
//    void appoint_member_as_store_manager_success() throws Exception {
//        Mockito.when(manager.getUserName()).thenReturn("Asaf");
//        store.appointMemberAsStoreManager(manager);
//        ConcurrentHashMap<String, StoreManager> managers = store.getStoreManagers();
//        assertEquals(1, managers.size());
//    }
    /*
        Cases:
            1. close store function called by the founder - success test
            2. close store function called by someone else than the founder - failure test
    */
    @Test
    void closeStore_with_store_founder_success() throws Exception {
        assertTrue(store.closeStore(founder.getUserName()));
    }

    @Test
    void closeStore_without_store_founder_fail() {
        assertThrows(Exception.class,
                () -> {store.closeStore(member2name.getUserName());});             //close function called by sam - not founder
    }

    /*
        Cases:
            1. method called by store owner - success test
            2. method called by someone else, not store owner - failure test
    */

    @Test
    void get_store_workers_info_called_by_owner_success() throws Exception {
        StoreOwner storeOwner = new StoreOwner(member2name);
        store.setStoreOwner("member2",storeOwner);
        List<MemberDTO> members = new ArrayList<>();
        members = store.getStoreWorkersInfo(founder.getUserName());
        assertEquals(2, members.size());
        assertEquals(members.get(0).username,member1name.getUserName());
        assertEquals(members.get(1).username,member2name.getUserName());
    }

    @Test
    void get_store_workers_info_without_owner_or_founder_manager_fail() throws Exception {
        assertThrows(Exception.class,
                () -> {store.getStoreWorkersInfo(member2name.getUserName());});             //close function called by sam - not owner
    }

//    /*
//        Cases:
//            1. method called by store owner - success test
//            2. method called by someone else, not store owner - failure test
//    */
//
//    @org.junit.jupiter.api.Test
//    void test_get_store_deals_called_by_store_owner_success() throws Exception {
//        Mockito.when(deal.getDealDTO()).thenReturn(dealDTO);
//        Mockito.when(owner.getUserName()).thenReturn("Biden");
//        store.appointMemberAsStoreOwner(owner);
//        store.addDeal(deal);
//        List<DealDTO> deals = store.getStoreDeals("Biden");
//        assertEquals(1, deals.size());
//    }
//
//    @org.junit.jupiter.api.Test
//    void test_get_store_deals_without_store_owner_fail() throws Exception {
//        Mockito.when(deal.getDealDTO()).thenReturn(dealDTO);
//        Mockito.when(owner.getUserName()).thenReturn("Biden");
//        store.appointMemberAsStoreOwner(owner);
//        store.addDeal(deal);
//        assertThrows(Exception.class,
//                () -> {store.getStoreDeals("Sam");});             //close function called by sam - not owner
//    }
//
//    /*
//        Cases:
//            1. the called member has a deal - success
//            2. the called member has no deals - failure
//    */
//
//    @org.junit.jupiter.api.Test
//    void get_member_deals_has_deals_success() {
//        Mockito.when(deal.getDealUserName()).thenReturn("Adel");
//        Mockito.when(deal.getDealDTO()).thenReturn(dealDTO);
//        store.addDeal(deal);
//        List<DealDTO> deals = new ArrayList<DealDTO>();
//        deals = store.getMemberDeals("Adel", deals);
//        assertEquals(1, deals.size());
//    }
//
//    @org.junit.jupiter.api.Test
//    void get_member_deals_has_no_deals_success() {
//        Mockito.when(deal.getDealUserName()).thenReturn("Adel");
//        Mockito.when(deal.getDealDTO()).thenReturn(dealDTO);
//        store.addDeal(deal);
//        List<DealDTO> deals = new ArrayList<DealDTO>();
//        deals = store.getMemberDeals("Ahmad", deals);
//        assertEquals(0, deals.size());
//    }



}