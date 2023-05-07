package DomainLayer;

import DTO.BagDTO;

import java.util.ArrayList;
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
        bags.putIfAbsent(store.getStoreName(), new Bag(store));
        return bags.get(store.getStoreName()).addProduct(productName, amount);
    }

    public boolean changeProductAmountInCart(Store store, String productName, Integer newAmount) throws Exception {
        if(!bags.containsKey(store.getStoreName()))
            throw new Exception("bag does not contain "+productName+" product");

        return bags.get(store.getStoreName()).changeProductAmount(productName, newAmount);
    }

    public boolean removeFromCart(Store store, String productName) throws Exception {
        if(!bags.containsKey(store.getStoreName()))
            throw new Exception("bag does not contain "+productName+" product");

        return bags.get(store.getStoreName()).removeProduct(productName);
    }

    public List<BagDTO> getCartContent(){
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


    public Double getCartPrice() throws Exception {
        Double totalPrice = 0.0;
        for(Bag bag : bags.values())
            totalPrice += bag.getBagPrice();

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
        //TODO: create deal for each store before deleting bags
        bags = new ConcurrentHashMap<>();
        return true;
    }
}
