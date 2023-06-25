package DomainLayer;

import DTO.BagDTO;
import DTO.DealDTO;

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

    //@Transient
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_deals")
    @Column(name = "deal")
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



    public Cart getCart() throws Exception {
        loadUser();
        return cart;
    }

    public abstract void loadUser() throws Exception;

    public boolean addToCart(Store store, String productName, Integer amount) throws Exception {
        loadUser();
        return cart.addToCart(store,productName,amount);
    }

    public boolean changeProductAmountInCart(Store store, String productName, Integer newAmount) throws Exception {
        loadUser();
        return cart.changeProductAmountInCart(store,productName,newAmount);
    }

    public boolean removeFromCart(Store store, String productName) throws Exception {
        loadUser();
        return cart.removeFromCart(store,productName);
    }


    public List<BagDTO> getCartContent() throws Exception {
        loadUser();
        return cart.getCartContent();
    }
    public boolean addDeal(Deal deal) throws Exception {
        loadUser();
        loadDeals();
        this.userDeals.add(deal);
        return true;
    }

    public List<DealDTO> getUserDeals() throws Exception {
        loadUser();
        loadDeals();
        List<DealDTO> deals = new ArrayList<>();
        for(Deal deal : this.userDeals)
            deals.add(deal.getDealDTO());

        return deals;
    }
     public abstract void loadDeals();
}
