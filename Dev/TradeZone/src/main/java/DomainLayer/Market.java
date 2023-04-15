package DomainLayer;

import DomainLayer.DTO.*;
import jdk.jshell.spi.ExecutionControl;

import java.util.List;

public class Market {
    UserController userController;
    StoreController storeController;

    public Market(){
        this.userController = new UserController();
        this.storeController = new StoreController();
    }
    //TODO: Implement this requirements: 2.5, 3.3(maybe), 4.12(for storeManager)

    public String enterMarket(){
        return userController.loginAsGuest();
    }

    public boolean exitMarket(String userName) throws Exception {
        return userController.exitMarket(userName);
    }

    public boolean register(String guestUserName, String newMemberUserName, String password) throws Exception {
        return userController.register(guestUserName, newMemberUserName, password);
    }
    public String login(String guestUserName, String MemberUserName, String password) throws Exception {
        return userController.login(guestUserName, MemberUserName, password);
    }
    public StoreDTO getStoreInfo(String userName, String storeName) throws Exception {
        userController.isGuestOrLoggedInMember(userName);
        return storeController.getStoreInfo(storeName);
    }
    public ProductDTO getProductInfoFromStore(String userName, String storeName, String productName) throws Exception {
        userController.isGuestOrLoggedInMember(userName);
        return storeController.getProductInfoFromStore(storeName, productName);
    }

    public List<ProductDTO> getProductInfoFromMarketByName(String userName, String productName) throws Exception {
        userController.isGuestOrLoggedInMember(userName);
        return storeController.getProductInfoFromMarketByName(productName);
    }

    public List<ProductDTO> getProductInfoFromMarketByCategory(String userName, String categoryName) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
    }

    public List<ProductDTO> getProductInfoFromMarketByKeyword(String userName, String keyword) throws ExecutionControl.NotImplementedException {
        // TODO: low priority Don't test it
        throw new ExecutionControl.NotImplementedException("");
    }

    public List<ProductDTO> filterByPrice(String userName, List<ProductDTO> productsInfo, Integer minPrice, Integer maxPrice) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
    }

    public List<ProductDTO> filterByCategory(String userName, List<ProductDTO> productsInfo, String categoryName) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
    }

    public List<ProductDTO> filterByProductRate(String userName, List<ProductDTO> productsInfo, Integer productRate) throws ExecutionControl.NotImplementedException {
        // TODO: low priority Don't test it
        throw new ExecutionControl.NotImplementedException("");
    }

    public List<ProductDTO> filterByStoreRate(String userName, List<ProductDTO> productsInfo, Integer storeRate) throws ExecutionControl.NotImplementedException {
        // TODO: low priority Don't test it
        throw new ExecutionControl.NotImplementedException("");
    }

    public boolean addToCart(String userName, String storeName, String productName, Integer amount) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
    }
    public boolean removeFromCart(String userName, String storeName, String productName) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
    }
    public boolean changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
    }
    public List<BagDTO> getCartContent(String userName) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
    }
    public boolean memberLogOut(String memberUserName) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
    }
    public StoreDTO createStore(String memberUserName, String newStoreName) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
    }
    public boolean addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, Double price, String details, Integer amount) throws Exception {
        return storeController.addNewProductToStock(memberUserName,storeName,nameProduct,category,price,details,amount);
    }
    public boolean removeProductFromStock(String memberUserName, String storeName, String productName) throws Exception {
        return storeController.removeProductFromStock(memberUserName, storeName, productName);
    }
    public boolean updateProductDescription(String memberUserName, String storeName, String productName, String newProductDescription) throws Exception {
        return storeController.updateProductDescription(memberUserName, storeName, productName, newProductDescription);
    }

    public boolean updateProductAmount(String memberUserName, String storeName, String productName, Integer newAmount) throws Exception {
        return storeController.updateProductAmount(memberUserName, storeName, productName, newAmount);
    }

    public boolean updateProductPrice(String memberUserName, String storeName, String productName, Double newPrice) throws Exception {
        return storeController.updateProductPrice(memberUserName, storeName, productName, newPrice);
    }

    public boolean appointOtherMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName) throws Exception {
        Store store = storeController.getStore(storeName); // TODO: MAYBE WE NEED TO CHECK IF STORE IS ACTIVE
        return userController.appointOtherMemberAsStoreOwner(memberUserName,store,newOwnerUserName);
    }

    public boolean appointOtherMemberAsStoreManager(String memberUserName, String storeName, String newManagerUserName) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
    }

    public boolean changeManagerPermissions(String memberUserName, String storeName, String managerUserName) throws ExecutionControl.NotImplementedException {
        // TODO: low priority , DON'T test it, dont forget to change function parameters
        throw new ExecutionControl.NotImplementedException("");
    }

    public boolean closeStore(String memberUserName, String storeName) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
    }

    public List<MemberDTO> getStoreOwnersInfo(String memberUserName, String storeName) throws ExecutionControl.NotImplementedException {
        // TODO: low priority , DON'T test it, dont forget to change function parameters
        throw new ExecutionControl.NotImplementedException("");
    }

    public List<DealDTO> getStoreDeals(String memberUserName, String storeName) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
    }

    public List<DealDTO> getMemberDeals(String systemManagerUserName, String otherMemberUserName) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
    }

}
