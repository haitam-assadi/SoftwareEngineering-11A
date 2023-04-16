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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @BeforeAll
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        //store = new Store(founder);
    }

    @BeforeEach
    public void beforeEachTest(){
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
        Mockito.when(owner.getUserName()).thenReturn("Biden");
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        store.appointMemberAsStoreOwner(owner);
        assertThrows(Exception.class,
                () -> {store.addNewProductToStock("Adel", "milk 3%", "milk", 5.90, "fresh milk", 5);});
    }

    @org.junit.jupiter.api.Test
    void add_new_product_to_stock_by_founder_success() throws Exception {
        Mockito.when(owner.getUserName()).thenReturn("Biden");
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        Mockito.when(stock.addNewProductToStock(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble() ,Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(true);
        store.appointMemberAsStoreOwner(owner);
        store.setStock(stock);
            assertTrue(store.addNewProductToStock("Mike", "milk 3%", "milk", 5.90, "fresh milk", 5));
    }

    @org.junit.jupiter.api.Test
    void add_new_product_to_stock_by_owner_success() throws Exception {
        Mockito.when(owner.getUserName()).thenReturn("Biden");
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        Mockito.when(stock.addNewProductToStock(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble() ,Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(true);
        store.appointMemberAsStoreOwner(owner);
        store.setStock(stock);
        assertTrue(store.addNewProductToStock("Biden", "milk 3%", "milk", 5.90, "fresh milk", 5));
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
        Mockito.when(owner.getUserName()).thenReturn("Biden");
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        store.appointMemberAsStoreOwner(owner);
        assertTrue(store.removeProductFromStock("Biden", "milk 3%"));
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
        Mockito.when(owner.getUserName()).thenReturn("Biden");
        Mockito.when(founder.getUserName()).thenReturn("Mike");
        store.appointMemberAsStoreOwner(owner);
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
    void updateProductDescription() {
    }

//    @org.junit.jupiter.api.Test
//    void updateProductDescription() {
//    }
//
//    @org.junit.jupiter.api.Test
//    void updateProductDescription() {
//    }
//
//    @org.junit.jupiter.api.Test
//    void updateProductDescription() {
//    }
//
//    @org.junit.jupiter.api.Test
//    void updateProductDescription() {
//    }
//
//    @org.junit.jupiter.api.Test
//    void updateProductDescription() {
//    }

    @org.junit.jupiter.api.Test
    void updateProductAmount() {
    }

    @org.junit.jupiter.api.Test
    void updateProductPrice() {
    }

    @org.junit.jupiter.api.Test
    void getStoreInfo() {
    }

    @org.junit.jupiter.api.Test
    void getProductInfo() {
    }

    @org.junit.jupiter.api.Test
    void isAlreadyStoreOwner() {
    }

    @org.junit.jupiter.api.Test
    void isAlreadyStoreManager() {
    }

    @org.junit.jupiter.api.Test
    void appointMemberAsStoreOwner() {
    }

    @org.junit.jupiter.api.Test
    void appointMemberAsStoreManager() {
    }

    @org.junit.jupiter.api.Test
    void isActive() {
    }

    @org.junit.jupiter.api.Test
    void getStoreName() {
    }

    @org.junit.jupiter.api.Test
    void getStock() {
    }

    @org.junit.jupiter.api.Test
    void getStoreFounder() {
    }

    @org.junit.jupiter.api.Test
    void getStoreOwners() {
    }

    @org.junit.jupiter.api.Test
    void getStoreManagers() {
    }

    @org.junit.jupiter.api.Test
    void getStoreDeals() {
    }

    @org.junit.jupiter.api.Test
    void getStoreDiscountPolicies() {
    }

    @org.junit.jupiter.api.Test
    void getStorePaymentPolicies() {
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
    void closeStore_without_store_founder_failure() {
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
    void getStoreWorkersInfo_with_owner_success() throws Exception {
        Mockito.when(founder.getMemberDTO()).thenReturn(new MemberDTO("Mike", "StoreFounder"));
        Mockito.when(owner.getMemberDTO()).thenReturn(new MemberDTO("Biden", "StoreOwner"));
        Mockito.when(owner.getUserName()).thenReturn("Biden");
        List<MemberDTO> members = new ArrayList<>();
        store.appointMemberAsStoreOwner(owner);
        members = store.getStoreWorkersInfo("Biden");
        assertEquals(2, members.size());
    }

    @org.junit.jupiter.api.Test
    void getStoreWorkersInfo_without_owner_failure() throws Exception {
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
    void testGetStoreDeals_with_store_owner() throws Exception {
        Mockito.when(deal.getDealDTO()).thenReturn(dealDTO);
        Mockito.when(owner.getUserName()).thenReturn("Biden");
        store.appointMemberAsStoreOwner(owner);
        store.addDeal(deal);
        List<DealDTO> deals = store.getStoreDeals("Biden");
        assertEquals(1, deals.size());
    }

    @org.junit.jupiter.api.Test
    void testGetStoreDeals_without_store_owner() throws Exception {
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
    void getMemberDeals_has_deals() {
        Mockito.when(deal.getDealUserName()).thenReturn("Adel");
        Mockito.when(deal.getDealDTO()).thenReturn(dealDTO);
        store.addDeal(deal);
        List<DealDTO> deals = new ArrayList<DealDTO>();
        deals = store.getMemberDeals("Adel", deals);
        assertEquals(1, deals.size());
    }

    @org.junit.jupiter.api.Test
    void getMemberDeals_has_no_deals() {
        Mockito.when(deal.getDealUserName()).thenReturn("Adel");
        Mockito.when(deal.getDealDTO()).thenReturn(dealDTO);
        store.addDeal(deal);
        List<DealDTO> deals = new ArrayList<DealDTO>();
        deals = store.getMemberDeals("Ahmad", deals);
        assertEquals(0, deals.size());
    }
}