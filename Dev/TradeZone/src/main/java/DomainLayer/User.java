package DomainLayer;

import DTO.BagDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class User {

    @Id
    protected String userName;

    @OneToOne
    @JoinColumn(name = "cart_id")
    protected Cart cart;

    @Transient
    protected List<Deal> userDeals;

    public User(String userName){
        this.userName = userName;
        this.cart = new Cart(this);
        userDeals = new ArrayList<>();
    }
    public User(){}

    public String getUserName(){
        return userName;
    }



    public Cart getCart() {
        return cart;
    }

    public Cart setCart(Cart cart) {
        return this.cart;
    }




    public boolean addToCart(Store store, String productName, Integer amount, boolean member) throws Exception {
        return cart.addToCart(store,productName,amount,member);
    }

    public boolean changeProductAmountInCart(Store store, String productName, Integer newAmount, boolean member) throws Exception {
        return cart.changeProductAmountInCart(store,productName,newAmount,member);
    }

    public boolean removeFromCart(Store store, String productName, boolean member) throws Exception {
        return cart.removeFromCart(store,productName,member);
    }


    public List<BagDTO> getCartContent() throws Exception {
        return cart.getCartContent();
    }
}
