package DomainLayer;

import DomainLayer.DTO.ProductDTO;

import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private String storeName;
    private Stock stock;
    private String storeFounder;
    private ConcurrentHashMap<String, StoreOwner> storeOwners;


    public Store() {
        storeOwners = new ConcurrentHashMap<>();
        stock = new Stock();
    }

    public boolean addNewProductToStock(String memberUserName, ProductDTO newProduct, Integer price, Integer amount) throws Exception {
        if(amount < 0)
            throw new Exception("the amount of the product cannot be negative");
        if(newProduct.getPrice() < 0)
            throw new Exception("the price of the product cannot be negative");
        if(!storeFounder.equals(memberUserName) || !storeOwners.containsKey(memberUserName))
            throw new Exception("can't add new product to stock : userName "+ memberUserName +" is not an owner to the store");
        return stock.addNewProductToStock(newProduct,amount);
    }
}
