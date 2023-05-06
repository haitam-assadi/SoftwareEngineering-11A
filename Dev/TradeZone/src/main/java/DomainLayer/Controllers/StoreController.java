package DomainLayer.Controllers;

import DTO.DealDTO;
import DTO.MemberDTO;
import DTO.ProductDTO;
import DTO.StoreDTO;
import DomainLayer.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class StoreController {
    private ConcurrentHashMap<String, Store> stores;

    public StoreController() {
        stores = new ConcurrentHashMap<>();
    }

    //TODO: isStore() method currentlu checking if storeName exists in hashmap, this does not work with lazyLoad
    //TODO: ahmed when you want to open a new store, check if the name of the store unique

    public boolean addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, Double price, String description, Integer amount) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.addNewProductToStock(memberUserName,nameProduct,category,price,description,amount);
    }

    public boolean removeProductFromStock(String memberUserName, String storeName, String productName) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.removeProductFromStock(memberUserName, productName);
    }

    public boolean updateProductDescription(String memberUserName, String storeName, String productName, String newProductDescription) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.updateProductDescription(memberUserName, productName, newProductDescription);
    }

    public boolean updateProductAmount(String memberUserName, String storeName, String productName, Integer newAmount) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.updateProductAmount(memberUserName, productName, newAmount);
    }

    public boolean updateProductPrice(String memberUserName, String storeName, String productName, Double newPrice) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.updateProductPrice(memberUserName, productName, newPrice);
    }


    public StoreDTO getStoreInfo(String storeName) throws Exception {
        storeName=storeName.strip().toLowerCase();
        isActiveStore(storeName);
        return stores.get(storeName).getStoreInfo();
    }

    public ProductDTO getProductInfoFromStore(String storeName, String productName) throws Exception {
        storeName = storeName.strip().toLowerCase();
        isActiveStore(storeName);
        return stores.get(storeName).getProductInfo(productName);
    }

    public List<ProductDTO> getProductInfoFromMarketByName(String productName) throws Exception {
        List<ProductDTO> productDTOList = new ArrayList<>();
        for(Store store: stores.values()) {
            String storeName = store.getStoreName();
            isActiveStore(storeName);
            if (store.containsProduct(productName))
                productDTOList.add(store.getProductInfo(productName));
        }
        return productDTOList;
    }

    public List<ProductDTO> getProductInfoFromMarketByCategory(String categoryName) throws Exception {
        List<ProductDTO> productDTOList = new ArrayList<>();
        for(Store store: stores.values()) {
            String storeName = store.getStoreName();
            isActiveStore(storeName);
            if (store.containsCategory(categoryName))
                productDTOList.addAll(store.getProductsInfoByCategory(categoryName));
        }
        return productDTOList;
    }

    public Store getStore(String storeName) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();

        if(!stores.containsKey(storeName))
            // TODO: read from database AND add to members hashmap
            throw new Exception("store needs to be read from database");

        return stores.get(storeName);
    }

    public void assertIsStore(String storeName) throws Exception {
        if(!isStore(storeName))
            throw new Exception("store: "+ storeName+" does not exists!");
    }
    public void assertIsNotStore(String storeName) throws Exception {
        if(isStore(storeName))
            throw new Exception("store: "+ storeName+" is already store");
    }

    public boolean isStore(String storeName) throws Exception {
        if(storeName==null || storeName.isBlank())
            throw new Exception("store name is null or empty");
        storeName = storeName.strip().toLowerCase();

        if(!stores.containsKey(storeName)) // TODO for Lazy load , maybe we need to change to set of names
            return false;

        return true;
    }

    public void isActiveStore(String storeName) throws Exception {
        assertIsStore(storeName);
        storeName=storeName.strip().toLowerCase();
        if(!stores.get(storeName).isActive())
            throw new Exception(""+ storeName+" is not Active!");
    }

    public boolean closeStore(String memberUserName, String storeName) throws Exception {
        assertIsStore(storeName);
        storeName=storeName.strip().toLowerCase();
        return this.stores.get(storeName).closeStore(memberUserName);
    }

    public List<MemberDTO> getStoreWorkersInfo(String memberUserName, String storeName) throws Exception {
        assertIsStore(storeName);
        storeName=storeName.strip().toLowerCase();
        return this.stores.get(storeName).getStoreWorkersInfo(memberUserName);
    }

    public List<DealDTO> getStoreDeals(String memberUserName, String storeName) throws Exception {
        assertIsStore(storeName);
        storeName=storeName.strip().toLowerCase();
        return this.stores.get(storeName).getStoreDeals(memberUserName);
    }

    public List<DealDTO> getMemberDeals(String otherMemberUserName) {
        List<DealDTO> deals = new ArrayList<DealDTO>();
        for(Store store : this.stores.values()){
            deals = store.getMemberDeals(otherMemberUserName, deals);
        }
        return deals;
    }

    public Store createStore(String newStoreName) throws Exception {
        assertIsNotStore(newStoreName);

        newStoreName = newStoreName.strip().toLowerCase();
        Store newStore = new Store(newStoreName);
        stores.put(newStoreName,newStore);
        return newStore;
    }


    public List<String> getAllStoresNames(){
        return stores.keySet().stream().toList();
    }


}
