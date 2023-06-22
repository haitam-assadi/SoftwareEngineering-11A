package UnitTests;

import DTO.ProductDTO;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
class StockTest {
    private Stock stock;

    @Mock
    Product product;

    @Mock
    Category category;

    @Mock
    ProductDTO productDTO;
    @BeforeAll
    public void setUp(){
        Market.dbFlag = false;
        StoreMapper.initMapper();
        MemberMapper.initMapper();
        MockitoAnnotations.openMocks(this);
        Store store = new Store("store1"); //TODO mock store
        stock = new Stock(store);
    }

    @BeforeEach
    public void beforeEachTest(){
        Market.dbFlag = false;
        StoreMapper.initMapper();
        MemberMapper.initMapper();
        Store store = new Store("store1"); //TODO mock store
        stock = new Stock(store);
    }

    /*
        Cases:
            1. product is already exists - fail
            2. adding a new  product to existed category - success
            3. adding a new product to a new category - success
    */
    @Test
    void add_new_product_to_stock_again_fail() {
        //(String nameProduct,String category, Double price, String description, Integer amount)
        stock.addToStockProducts("milk 3%", product, 10);
        assertThrows(Exception.class,
                () -> {stock.addNewProductToStock("milk 3%", "milk", 5.90, "fresh milk", 10);});
    }

    @Test
    void add_new_product_to_new_category_success() throws Exception {
        stock.addNewProductToStock("milk 3%", "milk", 5.90, "fresh milk", 10);
        assertEquals(1, stock.getCategoriesSize());
    }

    @Test
    void add_new_product_to_existed_category_success() throws Exception {
        /*
        stock.addToStockCategory("milk", category);
        assertTrue(stock.addNewProductToStock("milk 3%", "milk", 5.90, "fresh milk", 10));
         */
    }

    /*
        Cases:
            1. product does not exist - fail
            2. product exists - success
    */

    @Test
    void remove_product_from_stock_success() throws Exception {
        stock.addToStockCategory("milk", category);
        stock.addToStockProducts("milk 3%", product, 10);
        assertTrue(stock.removeProductFromStock("milk 3%"));
    }

    @Test
    void remove_product_that_doesnt_exist_from_stock_fail() {
        assertThrows(Exception.class,
                () -> {stock.removeProductFromStock("milk 3%");});
    }

    /*
        Cases:
            1. product does not exist - fail
            2. product exists - success
    */

    @Test
    void update_product_description_success() throws Exception {
        stock.addToStockCategory("milk", category);
        stock.addToStockProducts("milk 3%", product, 10);
        //TODO: does not compile
        //Mockito.when(product.getCategory()).thenReturn("milk");
        //TODO: does not compile
        //Mockito.when(category.updateProductDescriptionInCategory(Mockito.any())).thenReturn(true);
        assertTrue(stock.updateProductDescription("milk 3%", "fresh fresh milk"));
    }

    @Test
    void update_product_description_that_doesnt_exist_fail() {
        assertThrows(Exception.class,
                () -> {stock.updateProductDescription("milk 3%", "fresh fresh milk");});
    }

    /*
        Cases:
            1. product does not exist - fail
            2. product exists - success
            3. new amount is equal to current amount - fail
    */

    @Test
    void update_product_amount_success() throws Exception {
        stock.addNewProductToStock("milk 3%","milk",10.0,"description",10);
        stock.updateProductAmount("milk 3%",50);
        assertEquals(50,stock.getProductAmount("milk 3%"));
    }

    @Test
    void update_amount_for_product_that_doesnt_exist_fail() {
        assertThrows(Exception.class,
                () -> {stock.updateProductAmount("milk 3%", 20);});
    }

    @Test
    void update_product_amount_to_current_amount_fail() {
        stock.addToStockCategory("milk", category);
        stock.addToStockProducts("milk 3%", product, 10);
        assertThrows(Exception.class,
                () -> {stock.updateProductAmount("milk 3%", 10);});

    }

    /*
        Cases:
            1. product does not exist - fail
            2. product exists - success
            3. new price is equal to current price - fail
    */

    @Test
    void update_product_price_success() throws Exception {
        stock.addToStockCategory("milk", category);
        stock.addToStockProducts("milk 3%", product, 10);
        //TODO: does not compile
        //Mockito.when(product.getCategory()).thenReturn("milk");
        Mockito.when(product.getPrice()).thenReturn(110.0);
        //TODO: does not compile
        //Mockito.when(category.updateProductDescriptionInCategory(Mockito.any())).thenReturn(true);
        assertTrue(stock.updateProductPrice("milk 3%", 120.0));
    }

    @Test
    void update_price_for_product_that_doesnt_exist_fail() {
        assertThrows(Exception.class,
                () -> {stock.updateProductPrice("milk 3%", 120.0);});
    }

    @Test
    void update_product_price_to_current_amount_fail() {
        stock.addToStockCategory("milk", category);
        stock.addToStockProducts("milk 3%", product, 10);
        Mockito.when(product.getPrice()).thenReturn(120.0);
        assertThrows(Exception.class,
                () -> {stock.updateProductPrice("milk 3%", 120.0);});

    }

    /*
        Cases:
            1. product does not exist - fail
            2. product exists - success
    */

    @Test
    void get_product_info_success() throws Exception {
        /*
        stock.addToStockCategory("milk", category);
        stock.addToStockProducts("milk 3%", product, 10);
        Mockito.when(productDTO.name).thenReturn("milk 3%");
        Mockito.when(product.getProductInfo()).thenReturn(productDTO);
        ProductDTO other = stock .getProductInfo("milk 3%");
        assertEquals(other.name, "milk 3%");

         */
    }

    @Test
    void get_info_for_missing_product_fail() {
        assertThrows(Exception.class,
                () -> {stock.getProductInfo("milk 3%", new ArrayList<>());});
    }

    /*
        Cases:
            1. product does not exist - fail
            2. product exists - success
    */

    @Test
    void get_product_success() throws Exception {
        stock.addToStockCategory("milk", category);
        stock.addToStockProducts("milk 3%", product, 10);
        //TODO: does not compile
        //Mockito.when(product.getCategory()).thenReturn("milk");
        Product other = stock.getProduct("milk 3%");
        //TODO: does not compile
        //assertEquals(other.getCategory(), product.getCategory());
    }

    @Test
    void get_missing_product_fail() {
        assertThrows(Exception.class,
                () -> {stock.getProduct("milk 3%");});
    }

    /*
        Cases:
            1. product does not exist - fail
            2. product exists - success
            3. product's current amount is lesser than needed amount
    */

    @Test
    void get_product_with_amount_success() throws Exception {
        stock.addToStockCategory("milk", category);
        stock.addToStockProducts("milk 3%", product, 10);
        //TODO: does not compile
        //Mockito.when(product.getCategory()).thenReturn("milk");
        Product other = stock.getProductWithAmount("milk 3%", 5);
        //TODO: does not compile
        //assertEquals(other.getCategory(), product.getCategory());
    }

    @Test
    void get_product_with_current_amount_less_than_needed_fail() {
        stock.addToStockCategory("milk", category);
        stock.addToStockProducts("milk 3%", product, 10);
        assertThrows(Exception.class,
                () -> {stock.getProductWithAmount("milk 3%", 20);});
    }

    @Test
    void get_missing_product_with_amount_fail() {
        assertThrows(Exception.class,
                () -> {stock.getProductWithAmount("milk 3%", 10);});
    }

    /*
        Cases:
            1. product does not exist - fail
            2. product exists - success
            3. given product name is equal to null
            4. given product name is empty
    */

    @Test
    void contains_product_success() throws Exception {
        stock.addToStockCategory("milk", category);
        stock.addToStockProducts("milk 3%", product, 10);
        assertTrue(stock.containsProduct("milk 3%"));
    }

    @Test
    void contains_missing_product_fail() throws Exception {
        assertFalse(stock.containsProduct("milk 3%"));
    }

    @Test
    void contains_product_with_empty_name_fail() {
        assertThrows(Exception.class,
                () -> {stock.containsProduct("");});
    }

    @Test
    void contains_product_with_null_fail() {
        assertThrows(Exception.class,
                () -> {stock.containsProduct(null);});
    }

    /*
        Cases:
            1. product does not exist - fail
            2. product exists - success
            3. given product name is equal to null
            4. given product name is empty
    */

    @Test
    void contains_category_success() throws Exception {
        stock.addToStockCategory("milk", category);
        stock.addToStockProducts("milk 3%", product, 10);
        assertTrue(stock.containsCategory("milk"));
    }

    @Test
    void contains_missing_category_fail() throws Exception {
            assertFalse(stock.containsCategory("milk"));
    }

    @Test
    void contains_category_with_empty_name_fail() {
        assertThrows(Exception.class,
                () -> {stock.containsCategory("");});
    }

    @Test
    void contains_category_with_null_fail() {
        assertThrows(Exception.class,
                () -> {stock.containsCategory(null);});
    }

    /*
        Cases:
            1. product does not exist - fail
            2. product exists - success
    */

    @Test
    void get_products_info_by_category_success() throws Exception {
        stock.addToStockProducts("milk 3%", product, 10);
        stock.addToStockCategory("milk", category);
        stock.addToStockProducts("milk 3%", product, 10);
        List<ProductDTO> list = new ArrayList<>();
        list.add(productDTO);
        List<ProductDTO> result = stock.getProductsInfoByCategory("milk");
        assertEquals(0, result.size());
    }

    @Test
    void get_products_info_by_missing_category_fail() {
        assertThrows(Exception.class,
                () -> {stock.getProductsInfoByCategory("milk");});
    }
}