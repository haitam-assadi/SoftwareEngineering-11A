package AcceptanceTests;

import ServiceLayer.ResponseT;

import java.util.List;
import java.util.Map;

public class ProxyBridge implements Bridge{
    public RealBridge realBridge;

    public ProxyBridge(){
        realBridge = null;
    }

    public ProxyBridge(RealBridge realBridge){
        this.realBridge = realBridge;
    }


    @Override
    public ResponseT<Boolean> initializeMarket() {
        if(realBridge!=null){
            return realBridge.initializeMarket();
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<String> enterMarket() {
        if(realBridge!=null){
            return realBridge.enterMarket();
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean> exitMarket(String userName) {
        if(realBridge!=null){
            return realBridge.exitMarket(userName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean> register(String guestUserName, String newMemberUserName, String password) {
        if(realBridge!=null){
            return realBridge.register(guestUserName, newMemberUserName, password);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<String> login(String guestUserName, String MemberUserName, String password) {
        if(realBridge!=null){
            return realBridge.login(guestUserName, MemberUserName, password);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<String> memberLogOut(String memberUserName) {
        if(realBridge!=null){
            return realBridge.memberLogOut(memberUserName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<String> createStore(String memberUserName, String newStoreName) {
        if(realBridge!=null){
            return realBridge.createStore(memberUserName,newStoreName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean>  addNewProductToStock(String memberUserName, String storeName, String product_name,String category, int price, String description, int amount){        if(realBridge!=null){
            return realBridge.addNewProductToStock(memberUserName, storeName, product_name, category,price, description,amount);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean> removeProductFromStock(String memberUserName, String storeName, String productName) {
        if(realBridge!=null){
            return realBridge.removeProductFromStock(memberUserName, storeName, productName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean> addCategory(String userName, String categoryName, String storeName) {
        if(realBridge!=null){
            realBridge.addCategory(userName,categoryName, storeName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean> getCategory(String userName, String categoryName,String storeName) {
        if(realBridge!=null){
            realBridge.getCategory(userName, categoryName, storeName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean> updateProductName(String memberUserName, String storeName, String productName, String newName) {
        if(realBridge!=null){
            return realBridge.updateProductName(memberUserName, storeName, productName, newName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean> updateProductPrice(String memberUserName, String storeName, String productName, int price) {
        if(realBridge!=null){
            return realBridge.updateProductPrice(memberUserName, storeName, productName, price);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean> updateProductDescription(String memberUserName, String storeName, String productName, String newDescription) {
        if(realBridge!=null){
            return realBridge.updateProductDescription(memberUserName, storeName, productName, newDescription);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean> updateProductAmount(String memberUserName, String storeName, String productName, int amount) {
        if(realBridge!=null){
            return realBridge.updateProductAmount(memberUserName, storeName, productName, amount);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Integer> getProductPrice(String s) {
        if(realBridge!=null){
            return realBridge.getProductPrice(s);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<String> getProductDescription(String s) {
        if(realBridge!=null){
            return realBridge.getProductDescription(s);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Integer> getProductAmount(String storeName, String s) {
        if(realBridge!=null){
            return realBridge.getProductAmount(storeName, s);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean> appointMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName) {
        if(realBridge!=null){
            return realBridge.appointMemberAsStoreOwner(memberUserName, storeName, newOwnerUserName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<List<String>> getStoreOwners(String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreOwners(storeName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<String> getOwnerAppointer(String OwnerName, String storeName) {
        if(realBridge!=null){
            return realBridge.getOwnerAppointer(OwnerName,storeName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean> appointMemberAsStoreManager(String memberUserName, String storeName, String newOwnerUserName) {
        if(realBridge!=null){
            return realBridge.appointMemberAsStoreManager(memberUserName, storeName, newOwnerUserName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<List<String>> getStoreManagers(String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreManagers(storeName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<String> getManagerAppointer(String ManagerName, String storeName) {
        if(realBridge!=null){
            return realBridge.getManagerAppointer(ManagerName,storeName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<String> closeStore(String memberUserName, String storeName) {
        if(realBridge!=null){
            return realBridge.closeStore(memberUserName,storeName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean> canGetStoreInfo(String userName, String storeName) {
        if(realBridge!=null){
            return realBridge.canGetStoreInfo(userName,storeName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<String> getStoreNotification(String memberName, String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreNotification(memberName,storeName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Map<Integer, List<String>>> getStoreRulesInfo(String ownerName, String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreRulesInfo(ownerName,storeName);
        }
        return new ResponseT<>("proxy implementation");
    }

    public ResponseT<List<String>> getAllGuests() {
        if(realBridge!=null){
            return realBridge.getAllGuests();
        }
        return new ResponseT<>("proxy implementation");
    }

    public ResponseT<Integer> getUserCart(String user) {

        return new ResponseT<>("proxy implementation");
    }

    public ResponseT<List<String>> getAllOnlineMembers() {

        return new ResponseT<>("proxy implementation");
    }

    public ResponseT<List<String>> getAllMembers() {

        return new ResponseT<>("proxy implementation");
    }

    public ResponseT<String> getMemberPassword(String memberName) {

        return new ResponseT<>("proxy implementation");
    }

    public ResponseT<List<String>> getAllStores() {

        return new ResponseT<>("proxy implementation");
    }

    public ResponseT<String> getStoreFounder(String storeName) {

        return new ResponseT<>("proxy implementation");
    }

    public ResponseT<List<String>> getStoreProducts(String userName, String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreProducts(userName,storeName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Map<String, String>> getProductInfoFromMarketByName(String userName, String productName) {
        if(realBridge!=null){
            return realBridge.getProductInfoFromMarketByName(userName, productName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Map<String, List<String>>> getProductInfoFromMarketByCategory(String userName, String categoryName) {
        if(realBridge!=null){
            return realBridge.getProductInfoFromMarketByCategory(userName, categoryName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Map<String, List<String>>> getProductInfoFromMarketByKeyword(String userName, String keyword) {
        if(realBridge!=null){
            return realBridge.getProductInfoFromMarketByKeyword(userName, keyword);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean> addToCart(String userName, String storeName, String productName, Integer amount) {
        if(realBridge!=null){
            return realBridge.addToCart(userName, storeName, productName, amount);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<List<String>> getBag(String userName, String storeName) {
        if(realBridge!=null){
            return realBridge.getBag(userName, storeName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Integer> getProductAmountInCart(String userName, String storeName, String productName) {
        if(realBridge!=null){
            return realBridge.getProductAmountInCart(userName, storeName, productName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Map<String, List<String>>> getCartContent(String userName) {
        if(realBridge!=null){
            return realBridge.getCartContent(userName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean> removeProductFromCart(String userName, String storeName, String productName) {
        if(realBridge!=null){
            return realBridge.removeProductFromCart(userName, storeName, productName);
        }
        return new ResponseT<>("proxy implementation");
    }

    @Override
    public ResponseT<Boolean> changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount) {
        if(realBridge!=null){
            return realBridge.changeProductAmountInCart(userName, storeName, productName, newAmount);
        }
        return new ResponseT<>("proxy implementation");
    }
}
