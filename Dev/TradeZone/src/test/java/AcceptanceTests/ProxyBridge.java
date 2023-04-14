package AcceptanceTests;

import java.util.Collection;
import java.util.List;

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
    public boolean addNewProductToStock(String memberUserName, String storeName, String product_name, int price, int amount) {
        if(realBridge!=null){
            return realBridge.addNewProductToStock(memberUserName, storeName, product_name, price, amount);
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

    public List<String> getStoreProducts(String storeName) {
        if(realBridge!=null){
            return realBridge.getStoreProducts(storeName);
        }
        throw new UnsupportedOperationException();
    }
}
