package DomainLayer;

import DTO.BagDTO;
import DataAccessLayer.DALService;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "Carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cartId;

    @OneToOne
    @JoinColumn(name = "memberName")
    Member memberCart;

    @Transient
    User cartOwner;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cart_bags", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumns({
            @MapKeyJoinColumn(name = "bag_id", referencedColumnName = "bagId")
    })
    @Column(name = "bag")
    private Map<String, Bag> bags;
    public Cart(User cartOwner){
        this.cartOwner = cartOwner;
        bags = new ConcurrentHashMap<>();
    }
    public Cart(){}

    public Cart(Member cartOwner){
        this.memberCart = cartOwner;
    }

    public void setMemberCart(Member memberCart) {
        this.cartOwner = memberCart;
        this.memberCart = memberCart;
    }



    public boolean addToCart(Store store, String productName, Integer amount,boolean member) throws Exception {
        if(!store.containsProduct(productName))
            throw new Exception("the product dose not exist in the store");
        else {
            bags.putIfAbsent(store.getStoreName(), new Bag(store));
            if (member)
                if (Market.dbFlag)
                    DALService.modifyBag(this, bags.get(store.getStoreName()));
            return bags.get(store.getStoreName()).addProduct(productName, amount, member);
        }
        //        Bag bag = new Bag(store);
//        bag.addProduct(productName,amount);
//        bags.putIfAbsent(store.getStoreName(), bag);
//        return true;
    }

    public boolean changeProductAmountInCart(Store store, String productName, Integer newAmount,boolean member) throws Exception {
        if(!bags.containsKey(store.getStoreName()))
            throw new Exception("bag does not contain "+productName+" product");

        return bags.get(store.getStoreName()).changeProductAmount(productName, newAmount,member);
    }

    public boolean removeFromCart(Store store, String productName,boolean member) throws Exception {
        if(!bags.containsKey(store.getStoreName()))
            throw new Exception("bag does not contain "+productName+" product");
        if(!bags.get(store.getStoreName()).removeProduct(productName)){
            Bag bag = bags.get(store.getStoreName());
            bags.remove(store.getStoreName());
            if (member)
                if (Market.dbFlag)
                    DALService.modifyAndRemoveBag(this,bag);
        }
        return true;
    }

    public List<BagDTO> getCartContent() throws Exception {
        List<BagDTO> bagsDTO = new LinkedList<>();
        for(Bag bag: bags.values())
            bagsDTO.add(bag.getBagInfo());
        return bagsDTO;
    }

    public void validateStorePolicy(String userName) throws Exception {
        for(Bag bag : bags.values()){
            bag.validateStorePolicy(userName);
        }
    }

    public void validateAllProductsAmounts() throws Exception {
        for(Bag bag : bags.values()){
            bag.validateAllProductsAmounts();
        }
    }

    public void validateAllStoresIsActive() throws Exception {
        for(Bag bag : bags.values()){
            bag.validateStoreIsActive();
        }
    }


    public Double getCartPriceBeforeDiscount() throws Exception {
        Double totalPrice = 0.0;
        for(Bag bag : bags.values())
            totalPrice += bag.getBagPriceBeforeDiscount();

        return totalPrice;
    }

    public Double getCartPriceAfterDiscount() throws Exception {
        Double totalPrice = 0.0;
        for(Bag bag : bags.values())
            totalPrice += bag.getBagPriceAfterDiscount();

        return totalPrice;
    }

    public boolean updateStockAmount() throws Exception {
        List<Bag> updatedBagsList = new LinkedList<>();
        for(Bag bag: bags.values()){
            try {
                bag.removeBagAmountFromStock();
                updatedBagsList.add(bag);
            }catch (Exception e){
                for(Bag bagToReplace: updatedBagsList)
                    bagToReplace.replaceBagAmountToStock();
                throw e;
            }
        }
        for(Bag bag: bags.values()){
            Deal deal = bag.createDeal(this.cartOwner);
            this.cartOwner.addDeal(deal);
            //todo : should make sure that cartOwner = memberCart
            if (Market.dbFlag)
                DALService.saveDeal(deal,memberCart,bag.getStoreBag());
        }

        bags = new ConcurrentHashMap<>();
        return true;
    }

    public void removeAllCart() {
        for (Bag b: bags.values()){
            bags.remove(b.getStoreBag().getStoreName());
            if (Market.dbFlag)
                DALService.cartRepository.save(this);
            b.removeAllProducts();
        }
        bags = new ConcurrentHashMap<>();
        memberCart = null;

    }

    public Map<String, Bag> getBags() {
        return bags;
    }
}
