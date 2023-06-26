package DomainLayer;

import DTO.BagDTO;
import DataAccessLayer.Controller.DealMapper;
import DataAccessLayer.DALService;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @Transient
    private boolean isLoaded;
    @Transient
    private boolean isPersistence;
    public Cart(User cartOwner){
        this.cartOwner = cartOwner;
        bags = new ConcurrentHashMap<>();
        isLoaded = true;
        isPersistence = false;
    }
    public Cart(){}


//    public Cart(Member cartOwner){
//        this.memberCart = cartOwner;
//    }

    public void setMemberCart(Member memberCart) {
        this.cartOwner = memberCart;
        this.memberCart = memberCart;
        isPersistence = true;
    }

    public void setLoaded(boolean b){
        this.isLoaded = b;
    }


    public boolean addToCart(Store store, String productName, Integer amount) throws Exception {
        loadCart();
        if(!store.containsProduct(productName))
            throw new Exception("the product dose not exist in the store");
        else {
            bags.putIfAbsent(store.getStoreName(), new Bag(store,isPersistence));
            bags.get(store.getStoreName()).addProduct(productName, amount);
            if (Market.dbFlag && isPersistence)
                DALService.modifyBag(this, bags.get(store.getStoreName()));
            return true;
        }
    }

    public boolean changeProductAmountInCart(Store store, String productName, Integer newAmount) throws Exception {
        loadCart();
        if(!bags.containsKey(store.getStoreName()))
            throw new Exception("bag does not contain "+productName+" product");

        return bags.get(store.getStoreName()).changeProductAmount(productName, newAmount);
    }

    public boolean removeFromCart(Store store, String productName) throws Exception {
        loadCart();
        if(!bags.containsKey(store.getStoreName()))
            throw new Exception("bag does not contain "+productName+" product");
        if(!bags.get(store.getStoreName()).removeProduct(productName)){
            Bag bag = bags.get(store.getStoreName());
            bags.remove(store.getStoreName());
            if (Market.dbFlag && isPersistence)
                DALService.RemoveBag(this,bag);
        }
        return true;
    }

    public List<BagDTO> getCartContent() throws Exception {
        loadCart();
        List<BagDTO> bagsDTO = new LinkedList<>();
        for(Bag bag: bags.values())
            bagsDTO.add(bag.getBagInfo());
        return bagsDTO;
    }

    public void validateStorePolicy(String userName) throws Exception {
        loadCart();
        for(Bag bag : bags.values()){
            bag.validateStorePolicy(userName);
        }
    }

    public void validateAllProductsAmounts() throws Exception {
        loadCart();
        for(Bag bag : bags.values()){
            bag.validateAllProductsAmounts();
        }
    }

    public void validateAllStoresIsActive() throws Exception {
        loadCart();
        for(Bag bag : bags.values()){
            bag.validateStoreIsActive();
        }
    }


    public Double getCartPriceBeforeDiscount() throws Exception {
        loadCart();
        Double totalPrice = 0.0;
        for(Bag bag : bags.values())
            totalPrice += bag.getBagPriceBeforeDiscount();

        return totalPrice;
    }

    public Double getCartPriceAfterDiscount() throws Exception {
        loadCart();
        Double totalPrice = 0.0;
        for(Bag bag : bags.values())
            totalPrice += bag.getBagPriceAfterDiscount();

        if(totalPrice < 0.0)
            totalPrice = 0.0;

        return totalPrice;
    }

    public boolean updateStockAmount() throws Exception {
        loadCart();
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
            if (Market.dbFlag && isPersistence) //todo: check if should check is persistence here
                DALService.saveDeal(deal,memberCart,bag.getStoreBag());
            if (Market.dbFlag && !isPersistence)
                DALService.saveDealForStore(deal,bag.getStoreBag());
            if (Market.dbFlag) DealMapper.getInstance().insertDeal(deal);
        }
        bags = new ConcurrentHashMap<>();
        if (isPersistence && Market.dbFlag){
            DALService.cartRepository.save(this);
        }
        return true;
    }

    public void removeAllCart() throws Exception{
        loadCart();
        for (Bag b: bags.values().stream().toList()){
            bags.remove(b.getStoreBag().getStoreName());
            if (Market.dbFlag && isPersistence)
                DALService.cartRepository.save(this);
            b.removeAllProducts();
        }
        bags = new ConcurrentHashMap<>();
        memberCart = null;
    }
    public Map<String, Bag> getBags() {
        loadCart();
        return bags;
    }

    public void loadCart(){
        if (isLoaded|| !isPersistence ||!Market.dbFlag) return;
        this.cartId = DALService.cartRepository.findIdByMember(memberCart.userName);
        List<Object[]> storeBags_bagsId = DALService.cartRepository.findStoreNameAndBagIdByCartId(cartId);
        for (Object[] objects: storeBags_bagsId){
            int bag_id = (Integer) objects[0];
            String storeName = (String) objects[1];
            bags.put(storeName,new Bag(bag_id));
        }
        isLoaded = true;
    }

    public void setUser(Member member) {
        this.cartOwner = member;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
