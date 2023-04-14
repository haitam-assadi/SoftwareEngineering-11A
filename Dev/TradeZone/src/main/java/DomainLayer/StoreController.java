package DomainLayer;

import DomainLayer.DTO.DealDTO;
import DomainLayer.DTO.MemberDTO;
import DomainLayer.DTO.ProductDTO;

import java.util.ArrayList;
import java.util.List;
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

    public boolean closeStore(String memberUserName, String storeName) throws Exception {
        if(this.stores.containsKey(storeName)){
            return this.stores.get(storeName).closeStore(memberUserName);
        }
        throw new Exception("There is no store with the name: " + storeName);
    }

    public List<MemberDTO> getStoreWorkersInfo(String memberUserName, String storeName) throws Exception {
        if(this.stores.containsKey(storeName)){
            return this.stores.get(storeName).getStoreWorkersInfo(memberUserName);
        }
        throw new Exception("There is no store with the name: " + storeName);
    }

    public List<DealDTO> getStoreDeals(String memberUserName, String storeName) throws Exception {
        if(this.stores.containsKey(storeName)){
            return this.stores.get(storeName).getStoreDeals(memberUserName);
        }
        throw new Exception("There is no store with the name: " + storeName);
    }

    public List<DealDTO> getMemberDeals(String otherMemberUserName) {
        List<DealDTO> deals = new ArrayList<DealDTO>();
        for(Store store : this.stores.values()){
            deals = store.getMemberDeals(otherMemberUserName, deals);
        }
        return deals;
    }
}
