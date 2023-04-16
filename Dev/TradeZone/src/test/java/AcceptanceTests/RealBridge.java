package AcceptanceTests;

import ServiceLayer.ResponseT;
import ServiceLayer.SystemService;

import java.util.List;
import java.util.Map;

public class RealBridge implements Bridge{

    private SystemService systemService;

    @Override
    public ResponseT<Boolean> initializeMarket() {
        systemService = new SystemService();
        return new ResponseT<>(true);
    }

    @Override
    public ResponseT<String> enterMarket() {
        if(systemService.enterMarket().ErrorOccurred)

        return systemService.enterMarket();
    }

    public ResponseT<List<String>> getAllGuests() {
        return systemService.getAllGuests();
    }

    @Override
    public ResponseT<Boolean> exitMarket(String userName) {
        return systemService.exitMarket(userName);
    }

    @Override
    public ResponseT<Boolean> register(String guestUserName, String newMemberUserName, String password) {
        return systemService.register(guestUserName, newMemberUserName, password);
    }

    @Override
    public ResponseT<String> login(String guestUserName, String MemberUserName, String password) {
        return systemService.login(guestUserName, MemberUserName, password);
    }

    @Override
    public ResponseT<String> memberLogOut(String memberUserName) {
        return null;
    }

    @Override
    public ResponseT<String> createStore(String memberUserName, String newStoreName) {
        return null;
    }

    @Override
    public ResponseT<Boolean> addNewProductToStock(String memberUserName, String storeName, String nameProduct, String category, int price, String description, int amount) {
        return null;
    }

    @Override
    public ResponseT<Boolean> removeProductFromStock(String memberUserName, String storeName, String productName) {
        return null;
    }

    @Override
    public ResponseT<Boolean> addCategory(String userName, String categoryName, String storeName) {
        return null;
    }

    @Override
    public ResponseT<Boolean> getCategory(String userName, String categoryName, String storeName) {
        return null;
    }

    @Override
    public ResponseT<Boolean> updateProductName(String memberUserName, String storeName, String productName, String newName) {
        return null;
    }

    @Override
    public ResponseT<Boolean> updateProductPrice(String memberUserName, String storeName, String productName, int price) {
        return null;
    }

    @Override
    public ResponseT<Boolean> updateProductDescription(String memberUserName, String storeName, String productName, String newDescription) {
        return null;
    }

    @Override
    public ResponseT<Boolean> updateProductAmount(String memberUserName, String storeName, String productName, int amount) {
        return null;
    }

    @Override
    // delete it
    public ResponseT<Integer> getProductPrice(String s) {
        return null;
    }

    @Override
    // delete it
    public ResponseT<String> getProductDescription(String s) {
        return null;
    }

    @Override
    public ResponseT<Integer> getProductAmount(String storeName, String s) {
        return null;
    }

    @Override
    public ResponseT<Boolean> appointMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName) {
        return null;
    }

    @Override
    // delete it
    public ResponseT<List<String>> getStoreOwners(String storeName) {
        return null;
    }

    @Override
    public ResponseT<String> getOwnerAppointer(String OwnerName, String storeName) {
        return null;
    }

    @Override
    public ResponseT<List<String>> getStoreProducts(String userName, String storeName) {
        return null;
    }

    @Override
    public ResponseT<Boolean> appointMemberAsStoreManager(String memberUserName, String storeName, String newOwnerUserName) {
        return null;
    }

    @Override
    // delete it
    public ResponseT<List<String>> getStoreManagers(String storeName) {
        return null;
    }

    @Override
    public ResponseT<String> getManagerAppointer(String ManagerName, String storeName) {
        return null;
    }

    @Override
    public ResponseT<String> closeStore(String memberUserName, String storeName) {
        return null;
    }

    @Override
    public ResponseT<Boolean> canGetStoreInfo(String userName, String storeName) {
        return null;
    }

    @Override
    public ResponseT<String> getStoreNotification(String memberName, String storeName) {
        return null;
    }

    @Override
    public ResponseT<Map<Integer, List<String>>> getStoreRulesInfo(String ownerName, String storeName) {
        return null;
    }

    @Override
    public ResponseT<Map<String, String>> getProductInfoFromMarketByName(String userName, String productName) {
        return null;
    }

    @Override
    public ResponseT<Map<String, List<String>>> getProductInfoFromMarketByCategory(String userName, String categoryName) {
        return null;
    }

    @Override
    public ResponseT<Map<String, List<String>>> getProductInfoFromMarketByKeyword(String userName, String keyword) {
        return null;
    }

    @Override
    public ResponseT<Boolean> addToCart(String userName, String storeName, String productName, Integer amount) {
        return null;
    }

    @Override
    // dont do the implement
    public ResponseT<List<String>> getBag(String userName, String storeName) {
        return null;
    }

    @Override
    public ResponseT<Integer> getProductAmountInCart(String userName, String storeName, String productName) {
        return null;
    }

    @Override
    public ResponseT<Map<String, List<String>>> getCartContent(String userName) {
        return null;
    }

    @Override
    public ResponseT<Boolean> removeProductFromCart(String userName, String storeName, String productName) {
        return null;
    }

    @Override
    public ResponseT<Boolean> changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount) {
        return null;
    }
}
