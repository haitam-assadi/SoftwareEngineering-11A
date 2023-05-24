package CommunicationLayer;

import DTO.*;
import DomainLayer.LoggerManager;
import ServiceLayer.ResponseT;
import ServiceLayer.SystemService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Server {

    private static Server server = null;
    private SystemService service;
    public static Server getInstance(){
        if(server == null){
            server = new Server();
        }
        return server;
    }

    private Server(){
        service = new SystemService();
    }


    public ResponseT<Boolean> initializeMarket(){
        return service.initializeMarket();
    }

    public ResponseT<String> enterMarket(){
        return service.enterMarket();
    }

    public ResponseT<List<String>> getAllGuests(){
        return service.getAllGuests();
    }
    public ResponseT<List<String>> getAllMembers(){
        return service.getAllMembers();
    }

    public ResponseT<List<String>> getAllLoggedInMembers(){
        return service.getAllLoggedInMembers();
    }

    public ResponseT<List<String>> getAllStoresNames(){
        return service.getAllStoresNames();
    }

    public ResponseT<Boolean> exitMarket(String userName){
        return service.exitMarket(userName);
    }

    public ResponseT<Boolean> register(String guestUserName, String newMemberUserName, String password){
        return service.register(guestUserName, newMemberUserName, password);
    }

    public ResponseT<String> login(String guestUserName, String MemberUserName, String password){
        return service.login(guestUserName, MemberUserName, password);
    }

    public ResponseT<StoreDTO> getStoreInfo(String userName, String storeName){
        return service.getStoreInfo(userName, storeName);
    }

    public ResponseT<ProductDTO> getProductInfoFromStore(String userName, String storeName, String productName){
        return service.getProductInfoFromStore(userName, storeName, productName);
    }

    public ResponseT<List<ProductDTO>> getProductInfoFromMarketByName(String userName, String productName){
        return service.getProductInfoFromMarketByName(userName, productName);
    }

    public ResponseT<List<ProductDTO>> getProductInfoFromMarketByCategory(String userName, String categoryName){
        return service.getProductInfoFromMarketByCategory(userName, categoryName);
    }

    public ResponseT<List<ProductDTO>> getProductInfoFromMarketByKeyword(String userName, String keyword){
        return service.getProductInfoFromMarketByKeyword(userName, keyword);
    }

    public ResponseT<List<ProductDTO>> filterByPrice(String userName, List<ProductDTO> productsInfo, Integer minPrice, Integer maxPrice){
        return service.filterByPrice(userName, productsInfo, minPrice, maxPrice);
    }

    public ResponseT<List<ProductDTO>> filterByCategory(String userName, List<ProductDTO> productsInfo, String categoryName){
        return service.filterByCategory(userName, productsInfo, categoryName);
    }

    public ResponseT<List<ProductDTO>> filterByProductRate(String userName, List<ProductDTO> productsInfo, Integer productRate){
        return service.filterByProductRate(userName, productsInfo, productRate);
    }

    public ResponseT<List<ProductDTO>> filterByStoreRate(String userName, List<ProductDTO> productsInfo, Integer storeRate){
        return service.filterByStoreRate(userName, productsInfo, storeRate);
    }

    public ResponseT<Boolean> addToCart(String userName, String storeName, String productName, Integer amount){
        return service.addToCart(userName, storeName, productName, amount);
    }
    public ResponseT<Boolean> removeFromCart(String userName, String storeName, String productName){
        return service.removeFromCart(userName, storeName, productName);
    }
    public ResponseT<Boolean> changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount){
        return service.changeProductAmountInCart(userName, storeName, productName, newAmount);
    }
    public ResponseT<List<BagDTO>> getCartContent(String userName){
        return service.getCartContent(userName);
    }
    public ResponseT<String> memberLogOut(String memberUserName){
        return service.memberLogOut(memberUserName);
    }
    public ResponseT<StoreDTO> createStore(String memberUserName, String newStoreName){
        return service.createStore(memberUserName, newStoreName);
    }
    public ResponseT<Boolean> addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, Double price, String description, Integer amount){
        return service.addNewProductToStock(memberUserName, storeName, nameProduct, category, price, description, amount);
    }
    public ResponseT<Boolean> removeProductFromStock(String memberUserName, String storeName, String productName){
        return service.removeProductFromStock(memberUserName, storeName, productName);
    }
    public ResponseT<Boolean> updateProductDescription(String memberUserName, String storeName, String productName, String newProductDescription){
        return service.updateProductDescription(memberUserName, storeName, productName, newProductDescription);
    }

    public ResponseT<Boolean> updateProductAmount(String memberUserName, String storeName, String productName, Integer newAmount){
        return service.updateProductAmount(memberUserName, storeName, productName, newAmount);
    }

    public ResponseT<Boolean> updateProductPrice(String memberUserName, String storeName, String productName, Double newPrice){
        return service.updateProductPrice(memberUserName, storeName, productName, newPrice);
    }

    public ResponseT<Boolean> appointOtherMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName){
        return service.appointOtherMemberAsStoreOwner(memberUserName, storeName, newOwnerUserName);
    }

    public ResponseT<Boolean> appointOtherMemberAsStoreManager(String memberUserName, String storeName, String newManagerUserName){
        return service.appointOtherMemberAsStoreManager(memberUserName, storeName, newManagerUserName);
    }

    public ResponseT<Boolean> AppointMemberAsSystemManager(String managerName,String otherMemberName){
        return service.AppointMemberAsSystemManager(managerName, otherMemberName);
    }

    public ResponseT<Boolean> changeManagerPermissions(String memberUserName, String storeName, String managerUserName){
        return service.changeManagerPermissions(memberUserName, storeName, managerUserName);
    }

    public ResponseT<Boolean> closeStore(String memberUserName, String storeName){
        return service.closeStore(memberUserName, storeName);
    }

    public ResponseT<List<MemberDTO>> getStoreWorkersInfo(String memberUserName, String storeName){
        return service.getStoreWorkersInfo(memberUserName, storeName);
    }

    public ResponseT<List<DealDTO>> getStoreDeals(String memberUserName, String storeName){
        return service.getStoreDeals(memberUserName, storeName);
    }

    public ResponseT<List<DealDTO>> getMemberDeals(String systemManagerUserName, String otherMemberUserName){
        return service.getMemberDeals(systemManagerUserName, otherMemberUserName);
    }

    public ResponseT<Boolean> purchaseCartByCreditCard(String userName, String cardNumber, String month, String year, String holder, String cvv, String id, String receiverName, String shipmentAddress, String shipmentCity, String shipmentCountry, String zipCode){
        return service.purchaseCartByCreditCard(userName, cardNumber, month, year, holder, cvv, id, receiverName, shipmentAddress, shipmentCity, shipmentCountry, zipCode);
    }

    public ResponseT<Boolean> removeOwnerByHisAppointer(String appointerUserName, String storeName, String ownerUserName){
        return service.removeOwnerByHisAppointer(appointerUserName, storeName, ownerUserName);
    }

    public ResponseT<Boolean> hasRole(String userName){
        return service.hasRole(userName);
    }

    public ResponseT<Map<String,List<StoreDTO>>> myStores(String memberUserName){
        return service.myStores(memberUserName);
    }

    public  ResponseT<Boolean> systemManagerCloseStore(String managerName, String storeName){
        return service.systemManagerCloseStore(managerName,storeName);
    }

    public ResponseT<Boolean> isSystemManager(String userName){
        return service.isSystemManager(userName);
    }

    public ResponseT<Integer> getProductAmountInStore(String userName, String storeName, String productName){
        return service.getProductAmountInStore(userName, storeName, productName);
    }
}