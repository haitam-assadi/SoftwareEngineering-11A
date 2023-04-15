package AcceptanceTests;

import java.util.Collection;
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
    public boolean getCategory(String categoryName) {
        if(realBridge!=null){
            realBridge.getCategory(categoryName);
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
    public int getProductPrice(String s) {
        if(realBridge!=null){
            return realBridge.getProductPrice(s);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProductDescription(String s) {
        if(realBridge!=null){
            return realBridge.getProductDescription(s);
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
    public List<String> getStoreOwners(String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreOwners(storeName);
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

    public List<String> getAllgusts() {
        if(realBridge!=null){
            return realBridge.getAllguests();
        }
        throw new UnsupportedOperationException();
    }

    public int getUserCart(String user) {
        if(realBridge!=null){
            return realBridge.getUserCart();
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

    public String getStoreFounder(String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreFounder();
        }
        throw new UnsupportedOperationException();
    }

    public List<String> getStoreProducts(String userName, String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreProducts(storeName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getProductInfoFromMarketByName(String userName, String productName) {
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
}
