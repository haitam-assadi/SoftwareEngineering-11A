package AcceptanceTests;

import java.util.LinkedList;
import java.util.List;

public class RealBridge implements Bridge{

    @Override
    public boolean initializeMarket() {
        return true;
    }

    @Override
    public String enterMarket() {
        return null;
    }

    @Override
    public boolean exitMarket(String userName) {
        return false;
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
    public String createStore(String memberUserName, String newStoreName) {
        return null;
    }

    @Override
    public boolean addNewProductToStock(String memberUserName, String storeName, String product_name, int price, int amount) {
        return false;
    }

    @Override
    public boolean removeProductFromStock(String memberUserName, String storeName, String productName) {
        return false;
    }

    public List<String> getAllguests() {
        return new LinkedList<>();
    }

    public int getUserCart() {
        return -1;
    }

    public List<String> getAllOnlineMembers() {
        return new LinkedList<>();
    }

    public List<String> getAllMembers() {
        return new LinkedList<>();
    }

    public String getMemberPassword(String memberName) {
        return "";
    }

    public List<String> getAllStores() {
        return new LinkedList<>();
    }

    public String getStoreFounder() {
        return "";
    }

    public List<String> getStoreProducts(String storeName) {
        return new LinkedList<>();
    }
}
