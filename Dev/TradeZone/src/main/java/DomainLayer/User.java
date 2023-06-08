package DomainLayer;

import DTO.BagDTO;
import DTO.DealDTO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class User {

    protected String userName;
    protected Cart cart;
    protected List<Deal> userDeals;

    public User(String userName){
        this.userName = userName;
        this.cart = new Cart(this);
        userDeals = new ArrayList<>();
    }

    public String getUserName(){
        return userName;
    }



    public Cart getCart() {
        return cart;
    }

    public Cart setCart(Cart cart) {
        return this.cart;
    }

    public void removeCart(){
        cart.removeAllCart();
        cart = null;
    }


    public boolean addToCart(Store store, String productName, Integer amount) throws Exception {
        return cart.addToCart(store,productName,amount);
    }

    public boolean changeProductAmountInCart(Store store, String productName, Integer newAmount) throws Exception {
        return cart.changeProductAmountInCart(store,productName,newAmount);
    }

    public boolean removeFromCart(Store store, String productName) throws Exception {
        return cart.removeFromCart(store,productName);
    }


    public List<BagDTO> getCartContent() throws Exception {
        return cart.getCartContent();
    }
}
