package AcceptanceTests;

import DTO.BagDTO;
import DTO.ProductDTO;
import DTO.StoreDTO;
import ServiceLayer.ResponseT;
import ServiceLayer.SystemService;

import java.util.*;

// error:
// string  null, int -1, boolean false, LinkedList<?> empty

public class RealBridge implements Bridge{
    private SystemService systemService; //TODO: = new or getinstance()


    public RealBridge(){
        systemService = new SystemService();
    }

    @Override
    public boolean initializeMarket() {
        return systemService.initializeMarket();
    }

    @Override
    public String enterMarket() { // Done
        ResponseT<String> name = systemService.enterMarket();
        if(name.ErrorOccurred){
            return "";
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
    public boolean register(String guestUserName, String newMemberUserName, String password) throws Exception {
        ResponseT<Boolean> response = systemService.register(guestUserName,newMemberUserName,password);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public String login(String guestUserName, String MemberUserName, String password)  throws Exception {
        ResponseT<String> response = systemService.login(guestUserName, MemberUserName, password);
        if (response.ErrorOccurred){
            return "";
        }
        return response.getValue();
    }

    @Override
    public String memberLogOut(String memberUserName) {
        ResponseT<String> response = systemService.memberLogOut(memberUserName);
        if (response.ErrorOccurred){
            return "";
        }
        return response.getValue();
    }

    @Override
    public String createStore(String memberUserName, String newStoreName) {
        ResponseT<StoreDTO> store = systemService.createStore(memberUserName, newStoreName);
        if(store.ErrorOccurred){
            return "";
        }
        return store.getValue().storeName;
    }

    @Override
    public boolean addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, Double price, String description, int amount){
        ResponseT<Boolean> response = systemService.addNewProductToStock(memberUserName, storeName, nameProduct,
                category, price, description, amount);
        if (response.ErrorOccurred){
            return false;
        }
        return response.getValue();
    }

    @Override
    public boolean removeProductFromStock(String memberUserName, String storeName, String productName) {
        ResponseT<Boolean> response = systemService.removeProductFromStock(memberUserName, storeName, productName);
        if (response.ErrorOccurred){
            return false;
        }
        return response.getValue();
    }

    @Override
    public boolean addCategory(String userName, String categoryName, String storeName) { // TODO: add to market and service
        return false;
    }

    @Override
    public boolean getCategory(String userName, String categoryName,String storeName) { // TODO: add to market and service
        return false;
    }

    @Override
    public boolean updateProductName(String memberUserName, String storeName, String productName, String newName) { // TODO: add to market and service
            return false;
    }

    @Override
    public boolean updateProductPrice(String memberUserName, String storeName, String productName, Double price) {
        ResponseT<Boolean> response = systemService.updateProductPrice(memberUserName, storeName, productName, price);
        if (response.ErrorOccurred){
            return false;
        }
        return response.getValue();
    }

    @Override
    public boolean updateProductDescription(String memberUserName, String storeName, String productName, String newDescription) {
        ResponseT<Boolean> response = systemService.updateProductDescription(memberUserName,storeName,productName,newDescription);
        if (response.ErrorOccurred){
            return false;
        }
        return response.getValue();
    }

    @Override
    public boolean updateProductAmount(String memberUserName, String storeName, String productName, int amount) {
        ResponseT<Boolean> response = systemService.updateProductAmount(memberUserName,storeName,productName,amount);
        if (response.ErrorOccurred){
            return false;
        }
        return response.getValue();
    }

    @Override
    public int getProductAmount(String storeName, String productName) { // String userName
        // TODO: add amount field to ProductDTO or add a function get product amount to market and service
        return -1;
    }

    @Override
    public boolean appointOtherMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName) {
        ResponseT<Boolean> response = systemService.appointOtherMemberAsStoreOwner(memberUserName, storeName, newOwnerUserName);
        if (response.ErrorOccurred){
            return false;
        }
        return response.getValue();
    }

    @Override
    public String getOwnerAppointer(String OwnerName, String storeName) { // TODO: add to market and service
            return "";
    }

    @Override
    public boolean appointOtherMemberAsStoreManager(String memberUserName, String storeName, String newOwnerUserName) {
        ResponseT<Boolean> response = systemService.appointOtherMemberAsStoreManager(memberUserName, storeName, newOwnerUserName);
        if (response.ErrorOccurred){
            return false;
        }
        return response.getValue();
    }

    @Override
    public String getManagerAppointer(String ManagerName, String storeName) { // TODO: add to market and service
            return "";
    }

    @Override
    public String closeStore(String memberUserName, String storeName) {
        // TODO: closeStore in service returns boolean
            return "";
    }

    @Override
    public boolean canGetStoreInfo(String userName, String storeName) { // TODO: add to market and service
        ResponseT<StoreDTO> response = systemService.getStoreInfo(userName,storeName);
        if (response.ErrorOccurred){
            return false;
        }
        return true;
    }

    @Override
    public String getStoreNotification(String memberName, String storeName) { // TODO: add to market and service
            return "";
    }

    @Override
    public Map<Integer, List<String>> getStoreRulesInfo(String ownerName, String storeName) { // TODO: add to market and service
        // List<memberDTO> getStoreWorkersInfo
        return new HashMap<>();
    }

    @Override
    public String getStoreFounderName(String userName, String storeName) { // Done
        ResponseT<StoreDTO> storeInfo = systemService.getStoreInfo(userName, storeName);
        if(storeInfo.ErrorOccurred){
            return "";
        }
        return storeInfo.getValue().founderName;
    }

    @Override
    public List<String> getStoreOwnersNames(String userName, String storeName) { // Done
        ResponseT<StoreDTO> storeInfo = systemService.getStoreInfo(userName, storeName);
        if(storeInfo.ErrorOccurred){
            return new LinkedList<>(); // TODO:
        }
        return storeInfo.getValue().ownersNames;
    }

    @Override
    public List<String> getStoreManagersNames(String userName, String storeName) { // Done
        ResponseT<StoreDTO> storeInfo = systemService.getStoreInfo(userName, storeName);
        if(storeInfo.ErrorOccurred){
            return new LinkedList<>(); // TODO:
        }
        return storeInfo.getValue().managersNames;
    }

    @Override
    public Double getProductPrice(String userName, String storeName, String productName) { // Done
        ResponseT<ProductDTO> productInfo = systemService.getProductInfoFromStore(userName, storeName, productName);
        if(productInfo.ErrorOccurred){
            return -1.0; // TODO:
        }
        return productInfo.getValue().price;
    }

    @Override
    public String getProductDescription(String userName, String storeName, String productName) { // Done
        ResponseT<ProductDTO> productInfo = systemService.getProductInfoFromStore(userName, storeName, productName);
        if(productInfo.ErrorOccurred){
            return ""; // TODO:
        }
        return productInfo.getValue().description;
    }

    public List<String> getAllGuests() {
        ResponseT<List<String>> response = systemService.getAllGuests();
        if (response.ErrorOccurred){
            return new LinkedList<>();
        }
        return response.getValue();
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
        List<ProductDTO> storeProducts = store.getValue().productsInfo;
        for(ProductDTO p : storeProducts){
            ret.add(p.name);
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
            String storeName = p.storeName;
            if(ret.containsKey(storeName)){
                ret.get(storeName).add(p.name);
            }
            else{
                List<String> storeProducts = new LinkedList<>();
                storeProducts.add(p.name);
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
            String storeName = p.storeName;
            if(ret.containsKey(storeName)){
                ret.get(storeName).add(p.name);
            }
            else{
                List<String> storeProducts = new LinkedList<>();
                storeProducts.add(p.name);
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
            String storeName = p.storeName;
            if(ret.containsKey(storeName)){
                ret.get(storeName).add(p.name);
            }
            else{
                List<String> storeProducts = new LinkedList<>();
                storeProducts.add(p.name);
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
            String storeName = p.storeName;
            if(ret.containsKey(storeName)){
                ret.get(storeName).add(p.name);
            }
            else{
                List<String> storeProducts = new LinkedList<>();
                storeProducts.add(p.name);
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
            String storeName = p.storeName;
            if(ret.containsKey(storeName)){
                ret.get(storeName).add(p.name);
            }
            else{
                List<String> storeProducts = new LinkedList<>();
                storeProducts.add(p.name);
                ret.put(storeName, storeProducts);
            }
        }
        return ret;
    }

    @Override
    public boolean addToCart(String userName, String storeName, String productName, Integer amount) {
        ResponseT<Boolean> response = systemService.addToCart(userName,storeName,productName,amount);
        if (response.ErrorOccurred){
            return false;
        }
        return response.getValue();
    }

    @Override
    public List<String> getBag(String userName, String storeName) { // list<produceName>
        // TODO: add to market and service
            return new LinkedList<>();
    }

    @Override
    public int getProductAmountInCart(String userName, String storeName, String productName) {
        // call to get cart content
        return -1;
    }

    @Override
    public Map<String, List<String>> getCartContent(String userName) { // map: <bag.storeName, list<productName>>
        // TODO: what about product amount?
        return new HashMap<>();
    }

    @Override
    public boolean removeProductFromCart(String userName, String storeName, String productName) {
        ResponseT<Boolean> response = systemService.removeFromCart(userName,storeName,productName);
        if (response.ErrorOccurred){
            return false;
        }
        return response.getValue();
    }

    @Override
    public boolean changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount) throws Exception{
        ResponseT<Boolean> response = systemService.changeProductAmountInCart(userName,storeName,productName,newAmount);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }
}