package AcceptanceTests;

import java.util.Collection;
import java.util.LinkedList;
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
    public boolean initializeMarket() {
        if(realBridge!=null){
            realBridge.initializeMarket();
        }
        return true;
        //throw new UnsupportedOperationException();
    }

    @Override
    public String enterMarket() {
        if(realBridge!=null){
            return realBridge.enterMarket();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean exitMarket(String userName) {
        if(realBridge!=null){
            return realBridge.exitMarket(userName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean register(String guestUserName, String newMemberUserName, String password) {
        if(realBridge!=null){
            return realBridge.register(guestUserName, newMemberUserName, password);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String login(String guestUserName, String MemberUserName, String password) {
        if(realBridge!=null){
            return realBridge.login(guestUserName, MemberUserName, password);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String memberLogOut(String memberUserName) {
        if(realBridge!=null){
            return realBridge.memberLogOut(memberUserName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String createStore(String memberUserName, String newStoreName) {
        if(realBridge!=null){
            return realBridge.createStore(memberUserName,newStoreName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean  addNewProductToStock(String memberUserName, String storeName, String product_name,String category, int price, String description, int amount){        if(realBridge!=null){
        return realBridge.addNewProductToStock(memberUserName, storeName, product_name, category,price, description,amount);
    }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeProductFromStock(String memberUserName, String storeName, String productName) {
        if(realBridge!=null){
            return realBridge.removeProductFromStock(memberUserName, storeName, productName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addCategory(String userName, String categoryName, String storeName) {
        if(realBridge!=null){
            realBridge.addCategory(userName,categoryName, storeName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getCategory(String userName, String categoryName,String storeName) {
        if(realBridge!=null){
            realBridge.getCategory(userName, categoryName, storeName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean updateProductName(String memberUserName, String storeName, String productName, String newName) {
        if(realBridge!=null){
            return realBridge.updateProductName(memberUserName, storeName, productName, newName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean updateProductPrice(String memberUserName, String storeName, String productName, int price) {
        if(realBridge!=null){
            return realBridge.updateProductPrice(memberUserName, storeName, productName, price);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean updateProductDescription(String memberUserName, String storeName, String productName, String newDescription) {
        if(realBridge!=null){
            return realBridge.updateProductDescription(memberUserName, storeName, productName, newDescription);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean updateProductAmount(String memberUserName, String storeName, String productName, int amount) {
        if(realBridge!=null){
            return realBridge.updateProductAmount(memberUserName, storeName, productName, amount);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public int getProductAmount(String storeName, String s) {
        if(realBridge!=null){
            return realBridge.getProductAmount(storeName, s);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean appointMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName) {
        if(realBridge!=null){
            return realBridge.appointMemberAsStoreOwner(memberUserName, storeName, newOwnerUserName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOwnerAppointer(String OwnerName, String storeName) {
        if(realBridge!=null){
            return realBridge.getOwnerAppointer(OwnerName,storeName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean appointMemberAsStoreManager(String memberUserName, String storeName, String newOwnerUserName) {
        if(realBridge!=null){
            return realBridge.appointMemberAsStoreManager(memberUserName, storeName, newOwnerUserName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String getManagerAppointer(String ManagerName, String storeName) {
        if(realBridge!=null){
            return realBridge.getManagerAppointer(ManagerName,storeName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String closeStore(String memberUserName, String storeName) {
        if(realBridge!=null){
            return realBridge.closeStore(memberUserName,storeName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canGetStoreInfo(String userName, String storeName) {
        if(realBridge!=null){
            return realBridge.canGetStoreInfo(userName,storeName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStoreNotification(String memberName, String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreNotification(memberName,storeName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Integer, List<String>> getStoreRulesInfo(String ownerName, String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreRulesInfo(ownerName,storeName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStoreFounderName(String userName, String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreFounderName(userName, storeName);
        }
        return null;
    }

    @Override
    public List<String> getStoreOwnersNames(String userName, String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreOwnersNames(userName, storeName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getStoreManagersNames(String userName, String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreManagersNames(userName, storeName);
        }
        return null;
    }

    @Override
    public Double getProductPrice(String userName, String storeName, String productName) {
        if(realBridge!=null){
            return realBridge.getProductPrice(userName, storeName, productName);
        }
        return -1.0;
    }

    @Override
    public String getProductDescription(String userName, String storeName, String productName) {
        if(realBridge!=null){
            return realBridge.getProductDescription(userName, storeName, productName);
        }
        return null;
    }

    public List<String> getAllgusts() {
        if(realBridge!=null){
            return realBridge.getAllguests();
        }
        throw new UnsupportedOperationException();
    }

    public int getUserCart(String user) {
        if(realBridge!=null){
            return realBridge.getUserCart(user);
        }
        throw new UnsupportedOperationException();
    }

    public List<String> getAllOnlineMembers() {
        if(realBridge!=null){
            return realBridge.getAllOnlineMembers();
        }
        throw new UnsupportedOperationException();
    }

    public List<String> getAllMembers() {
        if(realBridge!=null){
            return realBridge.getAllMembers();
        }
        throw new UnsupportedOperationException();
    }

    public String getMemberPassword(String memberName) {
        if(realBridge!=null){
            return realBridge.getMemberPassword( memberName);
        }
        throw new UnsupportedOperationException();
    }

    public List<String> getAllStores() {
        if(realBridge!=null){
            return realBridge.getAllStores();
        }
        throw new UnsupportedOperationException();
    }

    public List<String> getStoreProducts(String userName, String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreProducts(userName,storeName);
        }
        return new LinkedList<>();
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByName(String userName, String productName) {
        if(realBridge!=null){
            return realBridge.getProductInfoFromMarketByName(userName, productName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByCategory(String userName, String categoryName) {
        if(realBridge!=null){
            return realBridge.getProductInfoFromMarketByCategory(userName, categoryName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByKeyword(String userName, String keyword) {
        if(realBridge!=null){
            return realBridge.getProductInfoFromMarketByKeyword(userName, keyword);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, List<String>> filterByPrice(String userName, Map<String, List<String>> products, int minPrice, int maxPrice) {
        if(realBridge!=null){
            return realBridge.filterByPrice(userName, products, minPrice, maxPrice);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, List<String>> filterByCategory(String userName, Map<String, List<String>> products, String categoryName) {
        if(realBridge!=null){
            return realBridge.filterByCategory(userName, products, categoryName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addToCart(String userName, String storeName, String productName, Integer amount) {
        if(realBridge!=null){
            return realBridge.addToCart(userName, storeName, productName, amount);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getBag(String userName, String storeName) {
        if(realBridge!=null){
            return realBridge.getBag(userName, storeName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public int getProductAmountInCart(String userName, String storeName, String productName) {
        if(realBridge!=null){
            return realBridge.getProductAmountInCart(userName, storeName, productName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, List<String>> getCartContent(String userName) {
        if(realBridge!=null){
            return realBridge.getCartContent(userName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeProductFromCart(String userName, String storeName, String productName) {
        if(realBridge!=null){
            return realBridge.removeProductFromCart(userName, storeName, productName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount) {
        if(realBridge!=null){
            return realBridge.changeProductAmountInCart(userName, storeName, productName, newAmount);
        }
        throw new UnsupportedOperationException();    }
}