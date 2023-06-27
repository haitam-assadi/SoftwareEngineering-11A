package UnitTests;

import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.Controller.StoreMapper;
import DomainLayer.*;
import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
class BagTest {

    private Bag bag;

    @Mock
    Store storeBag;

    @Mock
    Product product;

    @BeforeAll
    public void setUp(){
        Market.dbFlag = false;
        StoreMapper.initMapper();
        MemberMapper.initMapper();
        MockitoAnnotations.openMocks(this);
        bag = new Bag(storeBag,false);
    }

    @BeforeEach
    public void beforeEachTest(){
        Market.dbFlag = false;
        StoreMapper.initMapper();
        MemberMapper.initMapper();
        bag = new Bag(storeBag,false);
    }

    /*
        addProduct cases:
            1. bag already has the product - fail
            2. bag doesn't include product in content - success
    */

    @Test
    void add_product_success() throws Exception {
        Mockito.when(storeBag.getProductWithAmount(Mockito.anyString(), Mockito.anyInt())).thenReturn(product);
        assertTrue(bag.addProduct("milk 3%", 5));
    }

    @Test
    void add_existed_product_fail() throws Exception {
        Mockito.when(storeBag.getProductWithAmount(Mockito.anyString(), Mockito.anyInt())).thenReturn(product);
        assertTrue(bag.addProduct("milk 3%", 5));
        assertThrows(Exception.class,
                () -> {assertTrue(bag.addProduct("milk 3%", 5));});
    }



    @Test
    void change_unavailable_product_amount_fail() throws Exception {
        Mockito.when(storeBag.getProductWithAmount(Mockito.anyString(), Mockito.anyInt())).thenReturn(product);
        assertThrows(Exception.class,
                () -> {assertTrue(bag.changeProductAmount("milk 3%", 4));});
    }

    @Test
    void remove_unavailable_product_fail() throws Exception {
        Mockito.when(storeBag.getProductWithAmount(Mockito.anyString(), Mockito.anyInt())).thenReturn(product);
        bag.addProduct("Greek yogurt", 2);
        assertThrows(Exception.class,
                () -> {assertTrue(bag.removeProduct("milk 3%"));});
    }


    /*

    @Test
    void change_product_amount_success() throws Exception {
        Mockito.when(storeBag.getProductWithAmount(Mockito.anyString(), Mockito.anyInt())).thenReturn(product);
        bag.addProduct("milk 3%", 5);
        assertTrue(bag.changeProductAmount("milk 3%", 4));
    }

    @Test
    void remove_product_success() throws Exception {
        Mockito.when(storeBag.getProductWithAmount(Mockito.anyString(), Mockito.anyInt())).thenReturn(product);
        bag.addProduct("milk 3%", 5);
        bag.addProduct("Greek yogurt", 2);
        assertTrue(bag.removeProduct("milk 3%"));
    }

     */
}