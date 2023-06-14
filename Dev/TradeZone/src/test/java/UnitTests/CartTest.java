package UnitTests;

import DTO.ProductDTO;
import DomainLayer.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class CartTest {

    private Cart cart;

    @Mock
    User cartOwner;

    @Mock
    Store store;

    @Mock
    Product product;

    @Mock
    ProductDTO productDto;

    @BeforeAll
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        cart = new Cart(cartOwner);
    }

    @BeforeEach
    public void beforeEachTest(){
        cart = new Cart(cartOwner);
    }

    @Test
    void add_to_cart_success() throws Exception {
        //addToCart(Store store, String productName, Integer amount)
        Mockito.when(store.getStoreName()).thenReturn("Tnuva");
        Mockito.when(store.getProductWithAmount("milk 3%", 5)).thenReturn(product);
        Mockito.when(product.getProductInfo()).thenReturn(productDto);
        assertTrue(cart.addToCart(store, "milk 3%", 5,false));
        assertEquals(1, cart.getCartContent().size());
    }

    @Test
    void change_product_amount_in_cart_success() throws Exception {
        Mockito.when(store.getStoreName()).thenReturn("Tnuva");
        Mockito.when(store.getProductWithAmount("milk 3%", 5)).thenReturn(product);
        Mockito.when(store.getProductWithAmount("milk 3%", 30)).thenReturn(product);
        Mockito.when(product.getProductInfo()).thenReturn(productDto);
        cart.addToCart(store, "milk 3%", 5,false);
        assertTrue(cart.changeProductAmountInCart(store, "milk 3%", 30,false));
        assertEquals(1, cart.getCartContent().size());
    }

    @Test
    void change_product_amount_for_unavailable_store_in_cart_fail() {
        Mockito.when(store.getStoreName()).thenReturn("Tnuva");
        assertThrows(Exception.class,
                () -> {assertTrue(cart.changeProductAmountInCart(store, "milk 3%", 30,false));});
    }

    @Test
    void remove_from_cart_success() throws Exception {
        Mockito.when(store.getStoreName()).thenReturn("Tnuva");
        Mockito.when(store.getProductWithAmount("milk 3%", 5)).thenReturn(product);
        Mockito.when(store.getProductWithAmount("milk 3%", 30)).thenReturn(product);
        Mockito.when(product.getProductInfo()).thenReturn(productDto);
        cart.addToCart(store, "milk 3%", 5,false);
        assertTrue(cart.removeFromCart(store, "milk 3%",false));
        assertEquals(0, cart.getCartContent().size());
    }

    @Test
    void remove_from_unavailable_store_cart_fail() {
        Mockito.when(store.getStoreName()).thenReturn("Tnuva");
        assertThrows(Exception.class,
                () -> {assertTrue(cart.removeFromCart(store, "milk 3%",false));});
    }

    @Test
    void get_cart_price_success() throws Exception {
        double price = 25;
        Mockito.when(store.getStoreName()).thenReturn("Tnuva");
        Mockito.when(store.getProductWithAmount("milk 3%", 5)).thenReturn(product);
        Mockito.when(product.getProductInfo()).thenReturn(productDto);
        //totalBagPrice += product.getProductPrice(curr.get(product));
        Mockito.when(product.getProductPrice(5)).thenReturn(price);
        cart.addToCart(store, "milk 3%", 5,false);
        assertEquals(price, cart.getCartPriceAfterDiscount());
    }

    @Test
    void get_empty_cart_price_success() throws Exception {
        assertEquals(0, cart.getCartPriceAfterDiscount());
    }
}