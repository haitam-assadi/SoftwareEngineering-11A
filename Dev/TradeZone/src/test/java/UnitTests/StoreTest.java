package UnitTests;

import DomainLayer.*;
import DomainLayer.DTO.DealDTO;
import DomainLayer.DTO.MemberDTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
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
    private Member member;

    @BeforeAll
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        store = new Store(founder);
    }

    @BeforeEach
    public void beforeEachTest(){
        store = new Store(founder);
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

    @org.junit.jupiter.api.Test
    void add_new_product_to_stock_with_negative_amount_fail() {
        //addNewProductToStock(String memberUserName,String nameProduct,String category, Double price, String details, Integer amount)
        assertThrows(Exception.class,
                () -> {store.addNewProductToStock("Adel", "milk 3%", "milk", 5.90, "fresh milk", -5);});
    }

    @org.junit.jupiter.api.Test
    void add_new_product_to_stock_with_negative_price_fail() {
        assertThrows(Exception.class,
                () -> {store.addNewProductToStock("Adel", "milk 3%", "milk", -5.90, "fresh milk", 20);});
    }

    @org.junit.jupiter.api.Test
    void add_new_product_to_stock_with_null_details_fail() {
        assertThrows(Exception.class,
                () -> {store.addNewProductToStock("Adel", "milk 3%", "milk", 5.90, null, 20);});
    }

    @org.junit.jupiter.api.Test
    void add_new_product_to_stock_with_empty_details_fail() {
        assertThrows(Exception.class,
                () -> {store.addNewProductToStock("Adel", "milk 3%", "milk", 5.90, "", 20);});
    }

    @org.junit.jupiter.api.Test
    void add_new_product_to_stock_with_too_long_details_fail() {
        String details = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor." +
                " Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes," +
                " nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem." +
                " Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate.";
        assertThrows(Exception.class,
                () -> {store.addNewProductToStock("Adel", "milk 3%", "milk", 5.90, details, 20);});
    }

    @org.junit.jupiter.api.Test
    void add_new_product_to_stock_without_founder_or_owner_fail() throws Exception {
        Mockito.when(owner.getUserName()).thenReturn("Adam");
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        store.setStoreOwner("Adam", owner);
        assertThrows(Exception.class,
                () -> {store.addNewProductToStock("Adel", "milk 3%", "milk", 5.90, "fresh milk", 5);});
    }

    @org.junit.jupiter.api.Test
    void add_new_product_to_stock_by_founder_success() throws Exception {
        Mockito.when(owner.getUserName()).thenReturn("Adam");
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        Mockito.when(stock.addNewProductToStock(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble() ,Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(true);
        store.setStoreOwner("Adam", owner);
        store.setStock(stock);
            assertTrue(store.addNewProductToStock("Mike", "milk 3%", "milk", 5.90, "fresh milk", 5));
    }

    @org.junit.jupiter.api.Test
    void add_new_product_to_stock_by_owner_success() throws Exception {
        Mockito.when(owner.getUserName()).thenReturn("Adam");
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        Mockito.when(stock.addNewProductToStock(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble() ,Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(true);
        store.setStoreOwner("Adam", owner);
        store.setStock(stock);
        assertTrue(store.addNewProductToStock("Adam", "milk 3%", "milk", 5.90, "fresh milk", 5));
    }

    /*
        Cases:
            1. function called by store owner - success
            2. function called by store founder - success
            3. function called by someone else rather than store owner or store founder - failure
    */

    @org.junit.jupiter.api.Test
    void remove_product_from_stock_by_store_owner_success() throws Exception {
        Mockito.when(stock.removeProductFromStock(Mockito.anyString())).thenReturn(true);
        store.setStock(stock);
        Mockito.when(owner.getUserName()).thenReturn("Adam");
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        store.setStoreOwner("Adam", owner);
        assertTrue(store.removeProductFromStock("Adam", "milk 3%"));
    }

    @org.junit.jupiter.api.Test
    void remove_product_from_stock_by_store_founder_success() throws Exception {
        Mockito.when(stock.removeProductFromStock(Mockito.anyString())).thenReturn(true);
        store.setStock(stock);
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        assertTrue(store.removeProductFromStock("Mike", "milk 3%"));
    }

    @org.junit.jupiter.api.Test
    void remove_product_from_stock_without_store_founder_or_owner_fail() throws Exception {
        Mockito.when(stock.removeProductFromStock(Mockito.anyString())).thenReturn(true);
        store.setStock(stock);
        Mockito.when(owner.getUserName()).thenReturn("Adam");
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        store.setStoreOwner("Adam", owner);
        assertThrows(Exception.class,
                () -> {assertTrue(store.removeProductFromStock("Sam", "milk 3%"));});
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

    @org.junit.jupiter.api.Test
    void update_product_description_with_null_description_fail() {
        //(String memberUserName, String productName, String newProductDescription)
        assertThrows(Exception.class,
                () -> {store.updateProductDescription("Adel", "milk 3%", null);});
    }

    @org.junit.jupiter.api.Test
    void update_product_description_with_empty_description_fail() {
        assertThrows(Exception.class,
                () -> {store.updateProductDescription("Adel", "milk 3%", "");});
    }

    @org.junit.jupiter.api.Test
    void update_product_description_with_too_long_description_fail() {
        String details = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor." +
                " Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes," +
                " nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem." +
                " Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate.";
        assertThrows(Exception.class,
                () -> {store.updateProductDescription("Adel", "milk 3%", details);});
    }

    @org.junit.jupiter.api.Test
    void update_product_description_without_owner_or_founder_fail() throws Exception {
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        assertThrows(Exception.class,
                () -> {store.updateProductDescription("Adel", "milk 3%", "fresh milk");});
    }

    @org.junit.jupiter.api.Test
    void update_product_description_by_owner_success() throws Exception {
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        Mockito.when(owner.getUserName()).thenReturn("Adam");
        Mockito.when(stock.updateProductDescription(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        store.setStock(stock);
        store.setStoreOwner("Adam", owner);
        assertTrue(store.updateProductDescription("Adam", "milk 3%", "fresh milk"));
    }

    @org.junit.jupiter.api.Test
    void update_product_description_by_founder_success() throws Exception {
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        Mockito.when(stock.updateProductDescription(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        store.setStock(stock);
        assertTrue(store.updateProductDescription("Mike", "milk 3%", "fresh milk"));
    }

    /*
        Cases:
            1. amount is negative - failure
            2. function called by someone else rather than store owner or store founder - failure
            3. function called by store owner - success
            4. function called by store founder - success
    */

    @org.junit.jupiter.api.Test
    void update_product_amount_with_negative_amount_fail() {
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        assertThrows(Exception.class,
                () -> {store.updateProductAmount("Mike", "milk 3%", -20);});
    }

    @org.junit.jupiter.api.Test
    void update_product_amount_without_owner_or_founder_fail() {
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        assertThrows(Exception.class,
                () -> {store.updateProductAmount("Adel", "milk 3%", 20);});
    }

    @org.junit.jupiter.api.Test
    void update_product_amount_called_by_owner_success() throws Exception {
        Mockito.when(stock.updateProductAmount(Mockito.anyString(), Mockito.anyInt())).thenReturn(true);
        store.setStock(stock);
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        Mockito.when(owner.getUserName()).thenReturn("Adam");
        store.setStoreOwner("Adam", owner);
        assertTrue(store.updateProductAmount("Adam", "milk 3%", 20));
    }

    @org.junit.jupiter.api.Test
    void update_product_amount_called_by_founder_success() throws Exception {
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        Mockito.when(stock.updateProductAmount(Mockito.anyString(), Mockito.anyInt())).thenReturn(true);
        store.setStock(stock);
        assertTrue(store.updateProductAmount("Mike", "milk 3%", 20));
    }

    /*
        Cases:
            1. price is negative - failure
            2. function called by someone else rather than store owner or store founder - failure
            3. function called by store owner - success
            4. function called by store founder - success
            5. price is zero - failure
    */

    @org.junit.jupiter.api.Test
    void update_product_with_price_fail() {
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        assertThrows(Exception.class,
                () -> {store.updateProductPrice("Mike", "milk 3%", -5.90);});
    }

    @org.junit.jupiter.api.Test
    void update_product_price_with_zero_fail() {
        double newPrice = 0;
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        assertThrows(Exception.class,
                () -> {store.updateProductPrice("Mike", "milk 3%", newPrice);});
    }

    @org.junit.jupiter.api.Test
    void update_product_price_without_owner_and_founder_fail() {
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        assertThrows(Exception.class,
                () -> {store.updateProductPrice("Adel", "milk 3%", 5.90);});
    }

    @org.junit.jupiter.api.Test
    void update_product_price_called_by_owner_success() throws Exception {
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        Mockito.when(owner.getUserName()).thenReturn("Adam");
        Mockito.when(stock.updateProductPrice(Mockito.anyString(), Mockito.anyDouble())).thenReturn(true);
        store.setStock(stock);
        store.setStoreOwner("Adam", owner);
        assertTrue(store.updateProductPrice("Adam", "milk 3%", 5.90));
    }

    @org.junit.jupiter.api.Test
    void update_product_price_called_by_founder_success() throws Exception {
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        Mockito.when(stock.updateProductPrice(Mockito.anyString(), Mockito.anyDouble())).thenReturn(true);
        store.setStock(stock);
        assertTrue(store.updateProductPrice("Mike", "milk 3%", 5.90));
    }

    /*
        Cases:
            1. function called by store founder - success
            2. function called by store owner - success
            3. function called by someone else rather than founder or owner - fail
    */

    @org.junit.jupiter.api.Test
    void is_already_store_owner_called_by_owner_success() throws Exception {
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        Mockito.when(owner.getUserName()).thenReturn("Adam");
        store.setStoreOwner("Adam", owner);
        assertTrue(store.isAlreadyStoreOwner("Adam"));
    }

    @org.junit.jupiter.api.Test
    void is_already_store_owner_called_by_founder_success() {
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        assertTrue(store.isAlreadyStoreOwner("Mike"));
    }

    @org.junit.jupiter.api.Test
    void is_already_store_owner_without_founder_or_owner_fail() throws Exception {
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        Mockito.when(owner.getUserName()).thenReturn("Adam");
        store.setStoreOwner("Adam", owner);
        assertFalse(store.isAlreadyStoreOwner("Adel"));
    }

    /*
        Cases:
            1. function called by store manager - success
            2. function called by someone else rather than founder or owner - fail
            3. function called by store owner - fail
    */

    @org.junit.jupiter.api.Test
    void is_already_store_manager_called_by_manager_success() {
        Mockito.when(manager.getUserName()).thenReturn("Asaf");
        store.setStoreManager("Asaf", manager);
        assertTrue(store.isAlreadyStoreManager("Asaf"));
    }

    @org.junit.jupiter.api.Test
    void is_already_store_manager_called_by_owner_fail() {
        Mockito.when(owner.getUserName()).thenReturn("Mike");
        assertFalse(store.isAlreadyStoreManager("Mike"));
    }

    @org.junit.jupiter.api.Test
    void is_already_store_manager_without_manager_fail() {
        Mockito.when(manager.getUserName()).thenReturn("Asaf");
        store.setStoreManager("Asaf", manager);
        assertFalse(store.isAlreadyStoreManager("Adel"));
    }

    /*
        Cases:
            1. function called with an appointed store owner - failure
            2. function called with a member that is not an owner already - success
    */

    @org.junit.jupiter.api.Test
    void appoint_appointed_owner_as_store_owner_fail() {
        Mockito.when(owner.getUserName()).thenReturn("Adam");
        store.setStoreOwner("Adam", owner);
        assertThrows(Exception.class,
                () -> {store.appointMemberAsStoreOwner(owner);});
    }

    @org.junit.jupiter.api.Test
    void appoint_member_as_store_owner_success() throws Exception {
        Mockito.when(owner.getUserName()).thenReturn("Adam");
        store.appointMemberAsStoreOwner(owner);
        ConcurrentHashMap<String, StoreOwner> owners = store.getStoreOwners();
        assertEquals(1, owners.size());
    }

    /*
        Cases:
            1. function called with an appointed store manager - failure
            2. function called with a member that is not a manager already - success
    */

    @org.junit.jupiter.api.Test
    void appoint_appointed_manager_as_store_manager_fail() {
        Mockito.when(manager.getUserName()).thenReturn("Asaf");
        store.setStoreManager("Asaf", manager);
        assertThrows(Exception.class,
                () -> {store.appointMemberAsStoreManager(manager);});
    }

    @org.junit.jupiter.api.Test
    void appoint_member_as_store_manager_success() throws Exception {
        Mockito.when(manager.getUserName()).thenReturn("Asaf");
        store.appointMemberAsStoreManager(manager);
        ConcurrentHashMap<String, StoreManager> managers = store.getStoreManagers();
        assertEquals(1, managers.size());
    }
    /*
        Cases:
            1. close store function called by the founder - success test
            2. close store function called by someone else than the founder - failure test
    */
    @org.junit.jupiter.api.Test
    void closeStore_with_store_founder_success() throws Exception {
        Mockito.when(founder.getUserName()).thenReturn("Mike");                     //Mike -> random name
        assertTrue(store.closeStore("Mike"));
    }

    @org.junit.jupiter.api.Test
    void closeStore_without_store_founder_fail() {
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        assertThrows(Exception.class,
                () -> {store.closeStore("Sam");});             //close function called by sam - not founder
    }

    /*
        Cases:
            1. method called by store owner - success test
            2. method called by someone else, not store owner - failure test
    */

    @org.junit.jupiter.api.Test
    void get_store_workers_info_called_by_owner_success() throws Exception {
        Mockito.when(founder.getMemberDTO()).thenReturn(new MemberDTO("Mike", "StoreFounder"));
        Mockito.when(owner.getMemberDTO()).thenReturn(new MemberDTO("Biden", "StoreOwner"));
        Mockito.when(owner.getUserName()).thenReturn("Biden");
        List<MemberDTO> members = new ArrayList<>();
        store.appointMemberAsStoreOwner(owner);
        members = store.getStoreWorkersInfo("Biden");
        assertEquals(2, members.size());
    }

    @org.junit.jupiter.api.Test
    void get_store_workers_info_without_owner_fail() throws Exception {
        Mockito.when(founder.getMemberDTO()).thenReturn(new MemberDTO("Mike", "StoreFounder"));
        Mockito.when(owner.getMemberDTO()).thenReturn(new MemberDTO("Biden", "StoreOwner"));
        Mockito.when(owner.getUserName()).thenReturn("Biden");
        List<MemberDTO> members = new ArrayList<>();
        store.appointMemberAsStoreOwner(owner);
        assertThrows(Exception.class,
                () -> {store.getStoreWorkersInfo("Sam");});             //close function called by sam - not owner
    }

    /*
        Cases:
            1. method called by store owner - success test
            2. method called by someone else, not store owner - failure test
    */

    @org.junit.jupiter.api.Test
    void test_get_store_deals_called_by_store_owner_success() throws Exception {
        Mockito.when(deal.getDealDTO()).thenReturn(dealDTO);
        Mockito.when(owner.getUserName()).thenReturn("Biden");
        store.appointMemberAsStoreOwner(owner);
        store.addDeal(deal);
        List<DealDTO> deals = store.getStoreDeals("Biden");
        assertEquals(1, deals.size());
    }

    @org.junit.jupiter.api.Test
    void test_get_store_deals_without_store_owner_fail() throws Exception {
        Mockito.when(deal.getDealDTO()).thenReturn(dealDTO);
        Mockito.when(owner.getUserName()).thenReturn("Biden");
        store.appointMemberAsStoreOwner(owner);
        store.addDeal(deal);
        assertThrows(Exception.class,
                () -> {store.getStoreDeals("Sam");});             //close function called by sam - not owner
    }

    /*
        Cases:
            1. the called member has a deal - success
            2. the called member has no deals - failure
    */

    @org.junit.jupiter.api.Test
    void get_member_deals_has_deals_success() {
        Mockito.when(deal.getDealUserName()).thenReturn("Adel");
        Mockito.when(deal.getDealDTO()).thenReturn(dealDTO);
        store.addDeal(deal);
        List<DealDTO> deals = new ArrayList<DealDTO>();
        deals = store.getMemberDeals("Adel", deals);
        assertEquals(1, deals.size());
    }

    @org.junit.jupiter.api.Test
    void get_member_deals_has_no_deals_success() {
        Mockito.when(deal.getDealUserName()).thenReturn("Adel");
        Mockito.when(deal.getDealDTO()).thenReturn(dealDTO);
        store.addDeal(deal);
        List<DealDTO> deals = new ArrayList<DealDTO>();
        deals = store.getMemberDeals("Ahmad", deals);
        assertEquals(0, deals.size());
    }
}