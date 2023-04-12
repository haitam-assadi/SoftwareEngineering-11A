package DomainLayer;

public abstract class User {

    protected String userName;
    protected Cart cart;

    public User(String userName){
        this.userName = userName;
        this.cart = new Cart(this);
    }



    public Cart getCart() {
        return cart;
    }

    public Cart setCart(Cart cart) {
        return this.cart;
    }


}
