package AcceptanceTests;

import java.util.HashMap;
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
            return realBridge.initializeMarket();
        }
        return false;
    }

    @Override
    public String enterMarket() {
        if(realBridge!=null){
            return realBridge.enterMarket();
        }
        return "";
    }

    @Override
    public boolean exitMarket(String userName) {
        if(realBridge!=null){
            return realBridge.exitMarket(userName);
        }
        return false;
    }

    @Override
    public boolean register(String guestUserName, String newMemberUserName, String password) throws Exception {
        if(realBridge!=null){
            return realBridge.register(guestUserName, newMemberUserName, password);
        }
        return false;
    }

    @Override
    public String login(String guestUserName, String MemberUserName, String password) throws Exception {
        if(realBridge!=null){
            return realBridge.login(guestUserName, MemberUserName, password);
        }
        return "";
    }

    @Override
    public String memberLogOut(String memberUserName)  throws Exception {
        if(realBridge!=null){
            return realBridge.memberLogOut(memberUserName);
        }
        return "";
    }

    @Override
    public String createStore(String memberUserName, String newStoreName) {
        if(realBridge!=null){
            return realBridge.createStore(memberUserName,newStoreName);
        }
        return "";
    }

    @Override
    public boolean  addNewProductToStock(String memberUserName, String storeName, String product_name,String category, Double price, String description, int amount){        if(realBridge!=null){
        return realBridge.addNewProductToStock(memberUserName, storeName, product_name, category,price, description,amount);
    }
        return false;
    }

    @Override
    public boolean removeProductFromStock(String memberUserName, String storeName, String productName) {
        if(realBridge!=null){
            return realBridge.removeProductFromStock(memberUserName, storeName, productName);
        }
        return false;
    }

    @Override
    public boolean addCategory(String userName, String categoryName, String storeName) {
        if(realBridge!=null){
            realBridge.addCategory(userName,categoryName, storeName);
        }
        return false;
    }

    @Override
    public boolean getCategory(String userName, String categoryName,String storeName) {
        if(realBridge!=null){
            realBridge.getCategory(userName, categoryName, storeName);
        }
        return false;
    }

    @Override
    public boolean updateProductName(String memberUserName, String storeName, String productName, String newName) {
        if(realBridge!=null){
            return realBridge.updateProductName(memberUserName, storeName, productName, newName);
        }
        return false;
    }

    @Override
    public boolean updateProductPrice(String memberUserName, String storeName, String productName, Double price) {
        if(realBridge!=null){
            return realBridge.updateProductPrice(memberUserName, storeName, productName, price);
        }
        return false;
    }

    @Override
    public boolean updateProductDescription(String memberUserName, String storeName, String productName, String newDescription) {
        if(realBridge!=null){
            return realBridge.updateProductDescription(memberUserName, storeName, productName, newDescription);
        }
        return false;
    }

    @Override
    public boolean updateProductAmount(String memberUserName, String storeName, String productName, int amount) {
        if(realBridge!=null){
            return realBridge.updateProductAmount(memberUserName, storeName, productName, amount);
        }
        return false;
    }

    @Override
    public int getProductAmount(String storeName, String s) {
        if(realBridge!=null){
            return realBridge.getProductAmount(storeName, s);
        }
        return -1;
    }

    @Override
    public boolean appointOtherMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName) {
        if(realBridge!=null){
            return realBridge.appointOtherMemberAsStoreOwner(memberUserName, storeName, newOwnerUserName);
        }
        return false;
    }

    @Override
    public String getOwnerAppointer(String OwnerName, String storeName) {
        if(realBridge!=null){
            return realBridge.getOwnerAppointer(OwnerName,storeName);
        }
        return "";
    }

    @Override
    public boolean appointOtherMemberAsStoreManager(String memberUserName, String storeName, String newOwnerUserName) {
        if(realBridge!=null){
            return realBridge.appointOtherMemberAsStoreManager(memberUserName, storeName, newOwnerUserName);
        }
        return false;
    }

    @Override
    public String getManagerAppointer(String ManagerName, String storeName) {
        if(realBridge!=null){
            return realBridge.getManagerAppointer(ManagerName,storeName);
        }
        return "";
    }

    @Override
    public String closeStore(String memberUserName, String storeName) {
        if(realBridge!=null){
            return realBridge.closeStore(memberUserName,storeName);
        }
        return "";
    }

    @Override
    public boolean canGetStoreInfo(String userName, String storeName) {
        if(realBridge!=null){
            return realBridge.canGetStoreInfo(userName,storeName);
        }
        return false;
    }

    @Override
    public String getStoreNotification(String memberName, String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreNotification(memberName,storeName);
        }
        return "";
    }

    @Override
    public Map<Integer, List<String>> getStoreRulesInfo(String ownerName, String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreRulesInfo(ownerName,storeName);
        }
        return new HashMap<>();
    }

    @Override
    public String getStoreFounderName(String userName, String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreFounderName(userName, storeName);
        }
        return "";
    }

    @Override
    public List<String> getStoreOwnersNames(String userName, String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreOwnersNames(userName, storeName);
        }
        return new LinkedList<>();
    }

    @Override
    public List<String> getStoreManagersNames(String userName, String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreManagersNames(userName, storeName);
        }
        return new LinkedList<>();
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
        return "";
    }

    public List<String> getAllGuests() {
        if(realBridge!=null){
            return realBridge.getAllGuests();
        }
        return new LinkedList<>();
    }

    public int getUserCart(String user) {
        if(realBridge!=null){
            return realBridge.getUserCart(user);
        }
        return -1;
    }

    public List<String> getAllOnlineMembers() {
        if(realBridge!=null){
            return realBridge.getAllOnlineMembers();
        }
        return new LinkedList<>();
    }

    public List<String> getAllMembers() {
        if(realBridge!=null){
            return realBridge.getAllMembers();
        }
        return new LinkedList<>();
    }

    public String getMemberPassword(String memberName) {
        if(realBridge!=null){
            return realBridge.getMemberPassword( memberName);
        }
        return "";
    }

    public List<String> getAllStores() {
        if(realBridge!=null){
            return realBridge.getAllStores();
        }
        return new LinkedList<>();
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
        return new HashMap<>();
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByCategory(String userName, String categoryName) {
        if(realBridge!=null){
            return realBridge.getProductInfoFromMarketByCategory(userName, categoryName);
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByKeyword(String userName, String keyword) {
        if(realBridge!=null){
            return realBridge.getProductInfoFromMarketByKeyword(userName, keyword);
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, List<String>> filterByPrice(String userName, Map<String, List<String>> products, int minPrice, int maxPrice) {
        if(realBridge!=null){
            return realBridge.filterByPrice(userName, products, minPrice, maxPrice);
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, List<String>> filterByCategory(String userName, Map<String, List<String>> products, String categoryName) {
        if(realBridge!=null){
            return realBridge.filterByCategory(userName, products, categoryName);
        }
        return new HashMap<>();
    }

    @Override
    public boolean addToCart(String userName, String storeName, String productName, Integer amount) {
        if(realBridge!=null){
            return realBridge.addToCart(userName, storeName, productName, amount);
        }
        return false;
    }

    @Override
    public List<String> getBag(String userName, String storeName) {
        if(realBridge!=null){
            return realBridge.getBag(userName, storeName);
        }
        return new LinkedList<>();
    }

    @Override
    public int getProductAmountInCart(String userName, String storeName, String productName) {
        if(realBridge!=null){
            return realBridge.getProductAmountInCart(userName, storeName, productName);
        }
        return -1;
    }

    @Override
    public Map<String, List<String>> getCartContent(String userName) {
        if(realBridge!=null){
            return realBridge.getCartContent(userName);
        }
        return new HashMap<>();
    }

    @Override
    public boolean removeProductFromCart(String userName, String storeName, String productName) {
        if(realBridge!=null){
            return realBridge.removeProductFromCart(userName, storeName, productName);
        }
        return false;
    }

    @Override
    public boolean changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount) throws Exception{
        if(realBridge!=null){
            return realBridge.changeProductAmountInCart(userName, storeName, productName, newAmount);
        }
        return false;
    }
}