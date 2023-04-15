package DomainLayer;

import DomainLayer.DTO.BagDTO;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Cart {

    User cartOwner;
    private ConcurrentHashMap<String, Bag> bags;
    public Cart(User cartOwner){
        this.cartOwner = cartOwner;
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

    public List<BagDTO> getCartContent(User user) throws Exception {
        if(!cartOwner.equals(user))
            throw new Exception("user name : "+ user.userName +" is not the owner of the cart");
        List<BagDTO> bagsDTO = new LinkedList<>();
        List<Bag> listBags = bags.values().stream().toList();
        for (int i=0;i<listBags.size();i++){
            bagsDTO.add(new BagDTO(listBags.get(i).getCart(), listBags.get(i).getStoreBag(), listBags.get(i).getProductWithAmount()));
        }
        return bagsDTO;
    }
}
