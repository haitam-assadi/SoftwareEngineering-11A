package DomainLayer;

import DomainLayer.DTO.ProductDTO;

import java.util.concurrent.ConcurrentHashMap;

public class StoreController {
    private ConcurrentHashMap<String, Store> stores;

    public StoreController() {
        stores = new ConcurrentHashMap<>();
    }
    //TODO: ahmed when you want to open a new store, check if the name of the store unique

    public boolean addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, Double price, String details, Integer amount) throws Exception {
        if(!stores.containsKey(storeName))
            throw new Exception("can't add new product to stock : storeName "+ storeName+" does not exists!");
        Store store = stores.get(storeName);
        return store.addNewProductToStock(memberUserName,nameProduct,category,price,details,amount);
    }

    public boolean removeProductFromStock(String memberUserName, String storeName, String productName) throws Exception {
        if(!stores.containsKey(storeName))
            throw new Exception("can't remove product from stock : storeName "+ storeName+" does not exists!");
        Store store = stores.get(storeName);
        return store.removeProductFromStock(memberUserName, productName);
    }

    public boolean updateProductDescription(String memberUserName, String storeName, String productName, String newProductDescription) throws Exception {
        if(!stores.containsKey(storeName))
            throw new Exception("can't remove product from stock : storeName "+ storeName+" does not exists!");
        Store store = stores.get(storeName);
        return store.updateProductDescription(memberUserName, productName, newProductDescription);
    }

    public boolean updateProductAmount(String memberUserName, String storeName, String productName, Integer newAmount) throws Exception {
        if(!stores.containsKey(storeName))
            throw new Exception("can't remove product from stock : storeName "+ storeName+" does not exists!");
        Store store = stores.get(storeName);
        return store.updateProductAmount(memberUserName, productName, newAmount);
    }

    public boolean updateProductPrice(String memberUserName, String storeName, String productName, Double newPrice) throws Exception {
        if(!stores.containsKey(storeName))
            throw new Exception("can't remove product from stock : storeName "+ storeName+" does not exists!");
        Store store = stores.get(storeName);
        return store.updateProductPrice(memberUserName, productName, newPrice);
    }
}
