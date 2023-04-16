package DomainLayer;

import DomainLayer.DTO.BagDTO;
import DomainLayer.DTO.ProductDTO;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Bag {

    private Store storeBag;
    private ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent;

    public Bag(Store storeBag) {
        this.storeBag = storeBag;
        bagContent = new ConcurrentHashMap<>();
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

    public Store getStoreBag() {
        return storeBag;
    }
    public ConcurrentHashMap<String,Integer> getProductWithAmount(){
        List<String> productsNameBag =  bagContent.keySet().stream().toList();
        ConcurrentHashMap<String,Integer> productNameAmount = new ConcurrentHashMap<>();
        for (int i=0;i<productsNameBag.size();i++){
            String productName = productsNameBag.get(i);
            ConcurrentHashMap<Product,Integer> productIntegerConcurrentHashMap = bagContent.get(productName);
            Integer productAmount = productIntegerConcurrentHashMap.get(productIntegerConcurrentHashMap.keys().nextElement());
            productNameAmount.put(productName,productAmount);
        }
        return productNameAmount;
    }
    public BagDTO getBagInfo(){
        ConcurrentHashMap<ProductDTO, Integer> bagContent = new ConcurrentHashMap<>();
        for (ConcurrentHashMap<Product,Integer> curr: this.bagContent.values()) {
            bagContent.put(curr.keys().nextElement().getProductInfo(), curr.values().stream().toList().get(0));
        }
        return new BagDTO(storeBag.getStoreName(), bagContent);
    }
}
