package AcceptanceTests;

import DTO.ProductDTO;
import DTO.StoreDTO;
import DomainLayer.DTO.*;
import ServiceLayer.ResponseT;
import ServiceLayer.SystemService;

import java.util.*;

// error:
// string  null, int -1, boolean false, LinkedList<?> empty

public class RealBridge implements Bridge{
    private SystemService systemService; //TODO: = new or getinstance()

    @Override
    public boolean initializeMarket() { // TODO: add to market and service
        return true;
    }

    @Override
    public String enterMarket() { // Done
        ResponseT<String> name = systemService.enterMarket();
        if(name.ErrorOccurred){
            return null;
        }
        return name.getValue();
    }

    @Override
    public boolean exitMarket(String userName) { // Done
        ResponseT<Boolean> exit = systemService.exitMarket(userName);
        if (exit.ErrorOccurred){
            return false;
        }
        return exit.getValue();
    }

    @Override
    public boolean register(String guestUserName, String newMemberUserName, String password) {
        return false;
    }

    @Override
    public String login(String guestUserName, String MemberUserName, String password) {
        return null;
    }

    @Override
    public String memberLogOut(String memberUserName) {
        return null;
    }

    @Override
    public String createStore(String memberUserName, String newStoreName) { // ???
        ResponseT<StoreDTO> store = systemService.createStore(memberUserName, newStoreName);
        if(store.ErrorOccurred){
            return null;
        }
        return store.getValue().getStoreName();
    }

    @Override
    public boolean addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, int price, String description, int amount){
        return false;
    }

    @Override
    public boolean removeProductFromStock(String memberUserName, String storeName, String productName) {
        return false;
    }

    @Override
    public boolean addCategory(String userName, String categoryName, String storeName) { // TODO: add to market and service
        return true;
    }

    @Override
    public boolean getCategory(String userName, String categoryName,String storeName) { // TODO: add to market and service
        return true;
    }

    @Override
    public boolean updateProductName(String memberUserName, String storeName, String productName, String newName) { // TODO: add to market and service
        return true;
    }

    @Override
    public boolean updateProductPrice(String memberUserName, String storeName, String productName, int price) {
        return false;
    }

    @Override
    public boolean updateProductDescription(String memberUserName, String storeName, String productName, String newDescription) {
        return false;
    }

    @Override
    public boolean updateProductAmount(String memberUserName, String storeName, String productName, int amount) {
        return false;
    }

    @Override
    public int getProductAmount(String storeName, String productName) { // String userName
        // TODO: add amount field to ProductDTO or add a function get product amount to market and service
        return 0;
    }

    @Override
    public boolean appointMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName) {
        return false;
    }

    @Override
    public String getOwnerAppointer(String OwnerName, String storeName) { // TODO: add to market and service
        return null;
    }

    @Override
    public boolean appointMemberAsStoreManager(String memberUserName, String storeName, String newOwnerUserName) {
        return false;
    }

    @Override
    public String getManagerAppointer(String ManagerName, String storeName) { // TODO: add to market and service
        return null;
    }

    @Override
    public String closeStore(String memberUserName, String storeName) {
        // TODO: closeStore in service returns boolean
        return "";
    }

    @Override
    public boolean canGetStoreInfo(String userName, String storeName) { // TODO: add to market and service
        return true;
    }

    @Override
    public String getStoreNotification(String memberName, String storeName) { // TODO: add to market and service
        return null;
    }

    @Override
    public Map<Integer, List<String>> getStoreRulesInfo(String ownerName, String storeName) { // TODO: add to market and service
        // List<memberDTO> getStoreWorkersInfo
        return null;
    }

    @Override
    public String getStoreFounderName(String userName, String storeName) { // Done
        ResponseT<StoreDTO> storeInfo = systemService.getStoreInfo(userName, storeName);
        if(storeInfo.ErrorOccurred){
            return null;
        }
        return storeInfo.getValue().getFounderName();
    }

    @Override
    public List<String> getStoreOwnersNames(String userName, String storeName) { // Done
        ResponseT<StoreDTO> storeInfo = systemService.getStoreInfo(userName, storeName);
        if(storeInfo.ErrorOccurred){
            return new LinkedList<>(); // TODO:
        }
        return storeInfo.getValue().getOwnersNames();
    }

    @Override
    public List<String> getStoreManagersNames(String userName, String storeName) { // Done
        ResponseT<StoreDTO> storeInfo = systemService.getStoreInfo(userName, storeName);
        if(storeInfo.ErrorOccurred){
            return new LinkedList<>(); // TODO:
        }
        return storeInfo.getValue().getManagersNames();
    }

    @Override
    public Double getProductPrice(String userName, String storeName, String productName) { // Done
        ResponseT<ProductDTO> productInfo = systemService.getProductInfoFromStore(userName, storeName, productName);
        if(productInfo.ErrorOccurred){
            return -1.0; // TODO:
        }
        return productInfo.getValue().getPrice();
    }

    @Override
    public String getProductDescription(String userName, String storeName, String productName) { // Done
        ResponseT<ProductDTO> productInfo = systemService.getProductInfoFromStore(userName, storeName, productName);
        if(productInfo.ErrorOccurred){
            return null; // TODO:
        }
        return productInfo.getValue().getDescription();
    }

    public List<String> getAllguests() {
        return new LinkedList<>();
    }

    public int getUserCart(String user) {
        // TODO: we should delete it but it has 14 uses
        return -1;
    }

    public List<String> getAllOnlineMembers() { // TODO: add to market and service
        return new LinkedList<>();
    }

    public List<String> getAllMembers() { // TODO: add to market and service
        return new LinkedList<>();
    }

    public String getMemberPassword(String memberName) { // TODO: add to market and service
        return "";
    }

    public List<String> getAllStores() { // TODO: add to market and service
        return new LinkedList<>();
    }

    public List<String> getStoreProducts(String userName, String storeName) { // Done
        ResponseT<StoreDTO> store = systemService.getStoreInfo(userName, storeName);
        if(store.ErrorOccurred){
            return new LinkedList<>(); // TODO:
        }
        List<String> ret = new LinkedList<>();
        List<ProductDTO> storeProducts = store.getValue().getProductsInfo();
        for(ProductDTO p : storeProducts){
            ret.add(p.getName());
        }
        return ret;
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByName(String userName, String productName) { // map <storeName, List<productName>>
        ResponseT<List<ProductDTO>> products = systemService.getProductInfoFromMarketByName(userName, productName);
        if(products.ErrorOccurred){
            return new HashMap<>();
        }
        Map<String, List<String>> ret = new HashMap<>();
        for(ProductDTO p : products.getValue()){
            String storeName = p.getStoreName();
            if(ret.containsKey(storeName)){
                ret.get(storeName).add(p.getName());
            }
            else{
                List<String> storeProducts = new LinkedList<>();
                storeProducts.add(p.getName());
                ret.put(storeName, storeProducts);
            }
        }
        return ret;
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByCategory(String userName, String categoryName) { // map <storeName, List<productName>>
        ResponseT<List<ProductDTO>> products = systemService.getProductInfoFromMarketByCategory(userName, categoryName);
        if(products.ErrorOccurred){
            return new HashMap<>();
        }
        Map<String, List<String>> ret = new HashMap<>();
        for(ProductDTO p : products.getValue()){
            String storeName = p.getStoreName();
            if(ret.containsKey(storeName)){
                ret.get(storeName).add(p.getName());
            }
            else{
                List<String> storeProducts = new LinkedList<>();
                storeProducts.add(p.getName());
                ret.put(storeName, storeProducts);
            }
        }
        return ret;
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByKeyword(String userName, String keyword) { // map <storeName, List<productName>>
        ResponseT<List<ProductDTO>> products = systemService.getProductInfoFromMarketByKeyword(userName, keyword);
        if(products.ErrorOccurred){
            return new HashMap<>();
        }
        Map<String, List<String>> ret = new HashMap<>();
        for(ProductDTO p : products.getValue()){
            String storeName = p.getStoreName();
            if(ret.containsKey(storeName)){
                ret.get(storeName).add(p.getName());
            }
            else{
                List<String> storeProducts = new LinkedList<>();
                storeProducts.add(p.getName());
                ret.put(storeName, storeProducts);
            }
        }
        return ret;
    }

    @Override
    public Map<String, List<String>> filterByPrice(String userName, Map<String, List<String>> products, int minPrice, int maxPrice) { // map <storeName, List<productName>>
        // build List<ProductDTO> productsInfo
        List<ProductDTO> productsInfo = new LinkedList<>();
        for(String storeName : products.keySet()){
            for(String productName : products.get(storeName)){
                ResponseT<ProductDTO> response = systemService.getProductInfoFromStore(userName, storeName, productName);
                if(response.ErrorOccurred){
                    return new HashMap<>();
                }
                productsInfo.add(response.getValue());
            }
        }
        // filter
        ResponseT<List<ProductDTO>> filtered = systemService.filterByPrice(userName, productsInfo, minPrice, maxPrice);
        if(filtered.ErrorOccurred){
            return new HashMap<>();
        }
        Map<String, List<String>> ret = new HashMap<>();
        for(ProductDTO p : filtered.getValue()){
            String storeName = p.getStoreName();
            if(ret.containsKey(storeName)){
                ret.get(storeName).add(p.getName());
            }
            else{
                List<String> storeProducts = new LinkedList<>();
                storeProducts.add(p.getName());
                ret.put(storeName, storeProducts);
            }
        }
        return ret;
    }

    @Override
    public Map<String, List<String>> filterByCategory(String userName, Map<String, List<String>> products, String categoryName) { // map <storeName, List<productName>>
        // build List<ProductDTO> productsInfo
        List<ProductDTO> productsInfo = new LinkedList<>();
        for(String storeName : products.keySet()){
            for(String productName : products.get(storeName)){
                ResponseT<ProductDTO> response = systemService.getProductInfoFromStore(userName, storeName, productName);
                if(response.ErrorOccurred){
                    return new HashMap<>();
                }
                productsInfo.add(response.getValue());
            }
        }
        // filter
        ResponseT<List<ProductDTO>> filtered = systemService.filterByCategory(userName, productsInfo, categoryName);
        if(filtered.ErrorOccurred){
            return new HashMap<>();
        }
        Map<String, List<String>> ret = new HashMap<>();
        for(ProductDTO p : filtered.getValue()){
            String storeName = p.getStoreName();
            if(ret.containsKey(storeName)){
                ret.get(storeName).add(p.getName());
            }
            else{
                List<String> storeProducts = new LinkedList<>();
                storeProducts.add(p.getName());
                ret.put(storeName, storeProducts);
            }
        }
        return ret;
    }

    @Override
    public boolean addToCart(String userName, String storeName, String productName, Integer amount) {
        return true;
    }

    @Override
    public List<String> getBag(String userName, String storeName) { // list<produceName>
        // TODO: add to market and service
        return new LinkedList<>();
    }

    @Override
    public int getProductAmountInCart(String userName, String storeName, String productName) {
        // call to get cart content
        return 0;
    }

    @Override
    public Map<String, List<String>> getCartContent(String userName) { // map: <bag.storeName, list<productName>>
        // TODO: what about product amount?
        return new HashMap<>();
    }

    @Override
    public boolean removeProductFromCart(String userName, String storeName, String productName) {
        return false;
    }

    @Override
    public boolean changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount) {
        return false;
    }
}