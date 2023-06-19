package UnitTests;

import DTO.ProductDTO;
import DomainLayer.Bag;
import DomainLayer.Category;
import DomainLayer.Product;
import DomainLayer.Stock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class CategoryTest {

    private Category category;

    @Mock
    Stock stock;

    @Mock
    Product product;

    @Mock
    ProductDTO productDTO;


    @BeforeAll
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        category = new Category("milk", stock.getStoreName());
    }

    @BeforeEach
    public void beforeEachTest(){
        category = new Category("milk", stock.getStoreName());
    }


    @Test
    void put_product_in_category_success() throws Exception {
        Mockito.when(product.getName()).thenReturn("milk 3%");
        assertTrue(category.putProductInCategory(product));
    }

    @Test
    void put_existed_product_in_category_fail() throws Exception {
        Mockito.when(product.getName()).thenReturn("milk 3%");
        assertTrue(category.putProductInCategory(product));
        assertThrows(Exception.class,
                () -> {assertTrue(category.putProductInCategory(product));});
    }

    @Test
    void contains_existed_product_success() throws Exception {
        Mockito.when(product.getName()).thenReturn("milk 3%");
        category.putProductInCategory(product);
        assertTrue(category.containsProduct("milk 3%"));
    }
    @Test
    void contains_product_with_empty_name_fail_1() {
        assertThrows(Exception.class,
                () -> {assertTrue(category.containsProduct(""));});
    }

    @Test
    void contains_product_with_empty_name_fail_2() {
        assertThrows(Exception.class,
                () -> {assertTrue(category.containsProduct("     "));});
    }
    @Test
    void contains_product_with_null_value_fail() {
        assertThrows(Exception.class,
                () -> {assertTrue(category.containsProduct(null));});
    }
    @Test
    void contains_unavailable_product_fail() throws Exception {
        Mockito.when(product.getName()).thenReturn("milk 3%");;
        assertFalse(category.containsProduct("milk 3%"));
    }

    @Test
    void remove_existed_product_success() throws Exception {
        Mockito.when(product.getName()).thenReturn("milk 3%");
        Mockito.when(product.getProductInfo(Mockito.anyList())).thenReturn(productDTO);
        category.putProductInCategory(product);
        assertTrue(category.removeProduct("milk 3%"));
        //assertEquals(0, category.getProductsInfo().size());

    }

    @Test
    void remove_unavailable_product_fail() {
        Mockito.when(product.getName()).thenReturn("milk 3%");
        Mockito.when(product.getProductInfo(Mockito.anyList())).thenReturn(productDTO);
        assertThrows(Exception.class,
                () -> {assertTrue(category.removeProduct("milk 3%"));});
    }
}