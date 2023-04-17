package ServiceLayer;

import DTO.*;
import DomainLayer.Market;

import java.util.List;

public class SystemService {

    private Market market;

    public SystemService(){
        market = new Market();
    }

    public boolean initializeMarket(){
        return true;
    }

    public ResponseT<String> enterMarket(){
        try{
            return new ResponseT<>(market.enterMarket(), true);
        }catch(Exception e){
            return new ResponseT<>("enterMarket: "+e.getMessage());
        }
    }

    public ResponseT<List<String>> getAllGuests(){
        try{
            return new ResponseT<>(market.getAllGuests());
        }catch(Exception e){
            return new ResponseT<>("enterMarket: "+e.getMessage());
        }
    }

    public ResponseT<Boolean> exitMarket(String userName){
        try{
            return new ResponseT<>(market.exitMarket(userName));
        }catch(Exception e){
            return new ResponseT<>("exitMarket: "+e.getMessage());
        }
    }

    public ResponseT<Boolean> register(String guestUserName, String newMemberUserName, String password){
        try{
            return new ResponseT<>(market.register(guestUserName, newMemberUserName, password));

        }catch(Exception e){
            return new ResponseT<>("register: "+e.getMessage());
        }
    }
    public ResponseT<String> login(String guestUserName, String MemberUserName, String password){
        try{
            return new ResponseT<>(market.login(guestUserName, MemberUserName, password));
        }catch(Exception e){
            return new ResponseT<>("login: "+e.getMessage());
        }
    }
    public ResponseT<StoreDTO> getStoreInfo(String userName, String storeName){
        try{
            return new ResponseT<>(market.getStoreInfo(userName, storeName));
        }catch(Exception e){
            return new ResponseT<>("getStoreInfo: "+e.getMessage());
        }
    }

    public ResponseT<ProductDTO> getProductInfoFromStore(String userName, String storeName, String productName){
        try{
            return new ResponseT<>(market.getProductInfoFromStore(userName, storeName, productName));
        }catch(Exception e){
            return new ResponseT<>("getProductInfoFromStore: "+e.getMessage());
        }
    }

    public ResponseT<List<ProductDTO>> getProductInfoFromMarketByName(String userName, String productName){
        try{
            return new ResponseT<>(market.getProductInfoFromMarketByName(userName,productName));
        }catch(Exception e){
            return new ResponseT<>("getProductInfoFromMarketByName: "+e.getMessage());
        }
    }

    public ResponseT<List<ProductDTO>> getProductInfoFromMarketByCategory(String userName, String categoryName){
        try{
            return new ResponseT<>(market.getProductInfoFromMarketByCategory(userName,categoryName));
        }catch(Exception e){
            return new ResponseT<>("getProductInfoFromMarketByCategory: "+e.getMessage());
        }
    }

    public ResponseT<List<ProductDTO>> getProductInfoFromMarketByKeyword(String userName, String keyword){
        try{
            return new ResponseT<>(market.getProductInfoFromMarketByKeyword(userName,keyword));
        }catch(Exception e){
            return new ResponseT<>("getProductInfoFromMarketByKeyword: "+e.getMessage());
        }
    }

    public ResponseT<List<ProductDTO>> filterByPrice(String userName, List<ProductDTO> productsInfo, Integer minPrice, Integer maxPrice){
        try{
            return new ResponseT<>(market.filterByPrice(userName,productsInfo, minPrice, maxPrice));
        }catch(Exception e){
            return new ResponseT<>("filterByPrice: "+e.getMessage());
        }
    }

    public ResponseT<List<ProductDTO>> filterByCategory(String userName, List<ProductDTO> productsInfo, String categoryName){
        try{
            return new ResponseT<>(market.filterByCategory(userName,productsInfo, categoryName));
        }catch(Exception e){
            return new ResponseT<>("filterByCategory: "+e.getMessage());
        }
    }

    public ResponseT<List<ProductDTO>> filterByProductRate(String userName, List<ProductDTO> productsInfo, Integer productRate){
        try{
            return new ResponseT<>(market.filterByProductRate(userName,productsInfo, productRate));
        }catch(Exception e){
            return new ResponseT<>("filterByProductRate: "+e.getMessage());
        }
    }

    public ResponseT<List<ProductDTO>> filterByStoreRate(String userName, List<ProductDTO> productsInfo, Integer storeRate){
        try{
            return new ResponseT<>(market.filterByStoreRate(userName,productsInfo, storeRate));
        }catch(Exception e){
            return new ResponseT<>("filterByStoreRate: "+e.getMessage());
        }
    }

    public ResponseT<Boolean> addToCart(String userName, String storeName, String productName, Integer amount){
        try{
            return new ResponseT<>(market.addToCart(userName,storeName, productName,amount));
        }catch(Exception e){
            return new ResponseT<>("addToCart: "+e.getMessage());
        }
    }
    public ResponseT<Boolean> removeFromCart(String userName, String storeName, String productName){
        try{
            return new ResponseT<>(market.removeFromCart(userName,storeName, productName));
        }catch(Exception e){
            return new ResponseT<>("removeFromCart: "+e.getMessage());
        }
    }
    public ResponseT<Boolean> changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount){
        try{
            return new ResponseT<>(market.changeProductAmountInCart(userName,storeName, productName,newAmount));
        }catch(Exception e){
            return new ResponseT<>("changeProductAmountInCart: "+e.getMessage());
        }
    }
    public ResponseT<List<BagDTO>> getCartContent(String userName){
        try{
            return new ResponseT<>(market.getCartContent(userName));
        }catch(Exception e){
            return new ResponseT<>("getCartContent: "+e.getMessage());
        }
    }
    public ResponseT<String> memberLogOut(String memberUserName){
        try{
            return new ResponseT<>(market.memberLogOut(memberUserName));
        }catch(Exception e){
            return new ResponseT<>("memberLogOut: "+e.getMessage());
        }
    }
    public ResponseT<StoreDTO> createStore(String memberUserName, String newStoreName){
        try{
            return new ResponseT<>(market.createStore(memberUserName, newStoreName));
        }catch(Exception e){
            return new ResponseT<>("createStore: "+e.getMessage());
        }
    }
    public ResponseT<Boolean> addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, Double price, String description, Integer amount){
        try{
            return new ResponseT<>(market.addNewProductToStock(memberUserName, storeName, nameProduct,category,price,description,amount));
        }catch(Exception e){
            return new ResponseT<>("addNewProductToStock: "+e.getMessage());
        }
    }
    public ResponseT<Boolean> removeProductFromStock(String memberUserName, String storeName, String productName){
        try{
            return new ResponseT<>(market.removeProductFromStock(memberUserName, storeName, productName));
        }catch(Exception e){
            return new ResponseT<>("removeProductFromStock: "+e.getMessage());
        }
    }
    public ResponseT<Boolean> updateProductDescription(String memberUserName, String storeName, String productName, String newProductDescription){
        try{
            return new ResponseT<>(market.updateProductDescription(memberUserName, storeName, productName,newProductDescription));
        }catch(Exception e){
            return new ResponseT<>("updateProductDescription: "+e.getMessage());
        }
    }

    public ResponseT<Boolean> updateProductAmount(String memberUserName, String storeName, String productName, Integer newAmount){
        try{
            return new ResponseT<>(market.updateProductAmount(memberUserName, storeName, productName,newAmount));
        }catch(Exception e){
            return new ResponseT<>("updateProductAmount: "+e.getMessage());
        }
    }

    public ResponseT<Boolean> updateProductPrice(String memberUserName, String storeName, String productName, Double newPrice){
        try{
            return new ResponseT<>(market.updateProductPrice(memberUserName, storeName, productName,newPrice));
        }catch(Exception e){
            return new ResponseT<>("updateProductPrice: "+e.getMessage());
        }
    }

    public ResponseT<Boolean> appointOtherMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName){
        try{
            return new ResponseT<>(market.appointOtherMemberAsStoreOwner(memberUserName, storeName, newOwnerUserName));
        }catch(Exception e){
            return new ResponseT<>("appointOtherMemberAsStoreOwner: "+e.getMessage());
        }
    }

    public ResponseT<Boolean> appointOtherMemberAsStoreManager(String memberUserName, String storeName, String newManagerUserName){
        try{
            return new ResponseT<>(market.appointOtherMemberAsStoreManager(memberUserName, storeName, newManagerUserName));
        }catch(Exception e){
            return new ResponseT<>("appointOtherMemberAsStoreManager: "+e.getMessage());
        }
    }

    public ResponseT<Boolean> changeManagerPermissions(String memberUserName, String storeName, String managerUserName){
        try{
            return new ResponseT<>(market.changeManagerPermissions(memberUserName, storeName, managerUserName));
        }catch(Exception e){
            return new ResponseT<>("changeManagerPermissions: "+e.getMessage());
        }
    }

    public ResponseT<Boolean> closeStore(String memberUserName, String storeName){
        try{
            return new ResponseT<>(market.closeStore(memberUserName, storeName));
        }catch(Exception e){
            return new ResponseT<>("closeStore: "+e.getMessage());
        }
    }

    public ResponseT<List<MemberDTO>> getStoreWorkersInfo(String memberUserName, String storeName){
        try{
            return new ResponseT<>(market.getStoreWorkersInfo(memberUserName, storeName));
        }catch(Exception e){
            return new ResponseT<>("getStoreWorkersInfo: "+e.getMessage());
        }
    }

    public ResponseT<List<DealDTO>> getStoreDeals(String memberUserName, String storeName){
        try{
            return new ResponseT<>(market.getStoreDeals(memberUserName, storeName));
        }catch(Exception e){
            return new ResponseT<>("getStoreDeals: "+e.getMessage());
        }
    }

    public ResponseT<List<DealDTO>> getMemberDeals(String systemManagerUserName, String otherMemberUserName){
        try{
            return new ResponseT<>(market.getMemberDeals(systemManagerUserName, otherMemberUserName));
        }catch(Exception e){
            return new ResponseT<>("getMemberDeals: "+e.getMessage());
        }
    }
}
