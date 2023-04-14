package DomainLayer;

import DomainLayer.DTO.ProductDTO;
import DomainLayer.DTO.StoreDTO;

import java.util.concurrent.ConcurrentHashMap;

public class StoreController {
    private ConcurrentHashMap<String, Store> stores;

    public StoreController() {
        stores = new ConcurrentHashMap<>();
    }
    //TODO: ahmed when you want to open a new store, check if the name of the store unique

    public boolean addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, Integer price, String details, Integer amount) throws Exception {
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

    public boolean updateProductDetails(String memberUserName, String storeName, String productName, String newProductDetails) throws Exception {
        if(!stores.containsKey(storeName))
            throw new Exception("can't remove product from stock : storeName "+ storeName+" does not exists!");
        Store store = stores.get(storeName);
        return store.updateProductDetails(memberUserName, productName, newProductDetails);
    }


    public StoreDTO getStoreInfo(String storeName) throws Exception {
        isActiveStore(storeName);
        return stores.get(storeName).getStoreInfo();
    }

    public ProductDTO getProductInfoFromStore(String storeName, String productName) throws Exception {
        isActiveStore(storeName);
        return stores.get(storeName).getProductInfo(productName);
    }


    public void isStore(String storeName) throws Exception {
        if(storeName==null || storeName == "")
            throw new Exception("storeName is null or empty");
        storeName = storeName.strip().toLowerCase();

        if(!stores.containsKey(storeName))
            throw new Exception(""+ storeName+" does not exists!");
    }

    public void isActiveStore(String storeName) throws Exception {
        isStore(storeName);
        if(!stores.get(storeName).isActive())
            throw new Exception(""+ storeName+" is not Active!");
    }
}
