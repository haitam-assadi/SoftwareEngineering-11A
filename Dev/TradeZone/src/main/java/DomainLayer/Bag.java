package DomainLayer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Bag {

    Cart cart;
    Store storeBag;
    private ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent;

    public Bag(Store storeBag) {
        this.storeBag = storeBag;
    }

    public boolean addProduct(String productName, Integer amount) throws Exception {
        if(bagContent.containsKey(productName))
            throw new Exception("bag already contains "+productName+" product");

        Product product =storeBag.getProductWithAmount(productName,amount);

        ConcurrentHashMap<Product,Integer> innerHashMap = new ConcurrentHashMap<>();
        innerHashMap.put(product,amount);
        bagContent.put(productName,innerHashMap);
        return true;
    }

    public boolean changeProductAmount(String productName, Integer newAmount) throws Exception {
        if(! bagContent.containsKey(productName))
            throw new Exception("bag does not contain "+productName+" product");
        Product product =storeBag.getProductWithAmount(productName,newAmount);
        bagContent.get(product).put(product,newAmount);
        return true;
    }

    public boolean removeProduct(String productName) throws Exception {
        if(! bagContent.containsKey(productName))
            throw new Exception("bag does not contain "+productName+" product");
        bagContent.remove(productName);
        return true;
    }
}
