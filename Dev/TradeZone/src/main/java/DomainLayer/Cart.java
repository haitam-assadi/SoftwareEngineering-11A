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
}
