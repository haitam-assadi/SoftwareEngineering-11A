package AcceptanceTests;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    public boolean  addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, int price, String description, int amount){
        return false;
    }

    @Override
    public boolean removeProductFromStock(String memberUserName, String storeName, String productName) {
        return false;
    }

    @Override
    public boolean addCategory(String userName, String categoryName, String storeName) {
        return false;
    }

    @Override
    public boolean getCategory(String categoryName) {
        return false;
    }

    @Override
    public boolean updateProductName(String memberUserName, String storeName, String productName, String newName) {
        return false;
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
    public int getProductPrice(String s) {
        return 0;
    }

    @Override
    public String getProductDescription(String s) {
        return "";
    }

    @Override
    public int getProductAmount(String s) {
        return 0;
    }

    @Override
    public boolean appointMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName) {
        return false;
    }

    @Override
    public List<String> getStoreOwners(String storeName) {
        return null;
    }

    @Override
    public String getOwnerAppointer(String OwnerName, String storeName) {
        return null;
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

    public List<String> getStoreProducts(String userName, String storeName) {
        return new LinkedList<>();
    }

    @Override
    public Map<String, String> getProductInfoFromMarketByName(String userName, String productName) {
        return new HashMap<>();
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByCategory(String userName, String categoryName) {
        return new HashMap<>();
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByKeyword(String userName, String keyword) {
        return new HashMap<>();
    }
}
