package DomainLayer;

import java.util.List;

public abstract class User {

    protected String userName;
    protected Cart cart;
    protected List<Deal> userDeals;

    public User(String userName){
        this.userName = userName;
        this.cart = new Cart(this);
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


}
