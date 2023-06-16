package DomainLayer;

import DTO.BagDTO;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Cart {

    User cartOwner;
    private ConcurrentHashMap<String, Bag> bags;
    public Cart(User cartOwner){
        this.cartOwner = cartOwner;
        bags = new ConcurrentHashMap<>();
    }

    public boolean addToCart(Store store, String productName, Integer amount) throws Exception {
        if(!store.containsProduct(productName))
            throw new Exception("the product dose not exist in the store");
        else {
            bags.putIfAbsent(store.getStoreName(), new Bag(store));
            return bags.get(store.getStoreName()).addProduct(productName, amount);
        }
//        Bag bag = new Bag(store);
//        bag.addProduct(productName,amount);
//        bags.putIfAbsent(store.getStoreName(), bag);
//        return true;
    }

    public boolean changeProductAmountInCart(Store store, String productName, Integer newAmount) throws Exception {
        if(!bags.containsKey(store.getStoreName()))
            throw new Exception("bag does not contain "+productName+" product");

        return bags.get(store.getStoreName()).changeProductAmount(productName, newAmount);
    }

    public boolean removeFromCart(Store store, String productName) throws Exception {
        if(!bags.containsKey(store.getStoreName()))
            throw new Exception("bag does not contain "+productName+" product");
        if(!bags.get(store.getStoreName()).removeProduct(productName)){
            bags.remove(store.getStoreName());
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
        }

        bags = new ConcurrentHashMap<>();
        return true;
    }

    public void removeAllCart() {
        for (Bag b: bags.values()){
            b.removeAllProducts();
        }
        bags = new ConcurrentHashMap<>();
    }

    public void setUser(Member member) {
        this.cartOwner = member;
    }
}
