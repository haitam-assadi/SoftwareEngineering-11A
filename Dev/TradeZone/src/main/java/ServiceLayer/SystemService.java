package ServiceLayer;

import DTO.*;
import DomainLayer.Market;

import java.util.LinkedList;
import java.util.List;

public class SystemService {

    private Market market;

    public SystemService(){
        market = new Market();
    }

    public ResponseT<Boolean> initializeMarket(){

        try{
            createMemberWithTwoStore("user1");
            return new ResponseT<>(true);

        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }

    }

    private void createMemberWithTwoStore(String userName) throws Exception {
        String guestUserName = market.enterMarket();
        String userPassword = "Aa12345678";

        String userFirstStoreName = userName+"_first_Store";
        String userSecStoreName = userName+"_Second_Store";

        String userFirstStoreProduct1 = userFirstStoreName+"_product1";
        String userFirstStoreProduct2 = userFirstStoreName+"_product2";
        String userFirstStoreProduct3 = userFirstStoreName+"_product3";
        String userFirstStoreProduct4 = userFirstStoreName+"_product4";
        String userFirstStoreProduct5 = userFirstStoreName+"_product5";
        String userFirstStoreProduct6 = userFirstStoreName+"_product6";

        String userFirstStoreCategory1 = userFirstStoreName+"_category1";
        String userFirstStoreCategory2 = userFirstStoreName+"_category2";



        String userSecStoreProduct1 = userSecStoreName+"_product1";
        String userSecStoreProduct2 = userSecStoreName+"_product2";
        String userSecStoreProduct3 = userSecStoreName+"_product3";
        String userSecStoreProduct4 = userSecStoreName+"_product4";
        String userSecStoreProduct5 = userSecStoreName+"_product5";
        String userSecStoreProduct6 = userSecStoreName+"_product6";

        String userSecStoreCategory1 = userSecStoreName+"_category1";
        String userSecStoreCategory2 = userSecStoreName+"_category2";

        market.register(guestUserName, userName, userPassword);
        market.login(guestUserName, userName, userPassword);
        market.createStore(userName, userFirstStoreName);

        market.addNewProductToStock(userName, userFirstStoreName, userFirstStoreProduct1, userFirstStoreCategory1, 70.54, "new product", 100);
        market.addNewProductToStock(userName, userFirstStoreName, userFirstStoreProduct2, userFirstStoreCategory1, 70.54, "new product", 100);
        market.addNewProductToStock(userName, userFirstStoreName, userFirstStoreProduct3, userFirstStoreCategory1, 70.54, "new product", 100);
        market.addNewProductToStock(userName, userFirstStoreName, userFirstStoreProduct4, userFirstStoreCategory2, 70.54, "new product", 100);
        market.addNewProductToStock(userName, userFirstStoreName, userFirstStoreProduct5, userFirstStoreCategory2, 70.54, "new product", 100);
        market.addNewProductToStock(userName, userFirstStoreName, userFirstStoreProduct6, userFirstStoreCategory2, 70.54, "new product", 100);


        market.createStore(userName, userSecStoreName);
        market.addNewProductToStock(userName, userSecStoreName, userSecStoreProduct1, userSecStoreCategory1, 70.54, "new product", 100);
        market.addNewProductToStock(userName, userSecStoreName, userSecStoreProduct2, userSecStoreCategory1, 70.54, "new product", 100);
        market.addNewProductToStock(userName, userSecStoreName, userSecStoreProduct3, userSecStoreCategory1, 70.54, "new product", 100);
        market.addNewProductToStock(userName, userSecStoreName, userSecStoreProduct4, userSecStoreCategory2, 70.54, "new product", 100);
        market.addNewProductToStock(userName, userSecStoreName, userSecStoreProduct5, userSecStoreCategory2, 70.54, "new product", 100);
        market.addNewProductToStock(userName, userSecStoreName, userSecStoreProduct6, userSecStoreCategory2, 70.54, "new product", 100);
        String exitGuest = market.memberLogOut(userName);
        market.exitMarket(exitGuest);
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
            return new ResponseT<>("getAllGuests: "+e.getMessage());
        }
    }
    public ResponseT<List<String>> getAllMembers(){
        try{
            return new ResponseT<>(market.getAllMembers());
        }catch(Exception e){
            return new ResponseT<>("getAllMembers: "+e.getMessage());
        }
    }



    public ResponseT<List<String>> getAllLoggedInMembers(){
        try{
            return new ResponseT<>(market.getAllLoggedInMembers());
        }catch(Exception e){
            return new ResponseT<>("getAllLoggedInMembers: "+e.getMessage());
        }
    }


    public ResponseT<List<String>> getAllStoresNames(){
        try{
            return new ResponseT<>(market.getAllStoresNames());
        }catch(Exception e){
            return new ResponseT<>("getAllStoresNames: "+e.getMessage());
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
            return new ResponseT<>(market.login(guestUserName, MemberUserName, password), true);
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
            return new ResponseT<>(market.memberLogOut(memberUserName), true);
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

    public ResponseT<Boolean> purchaseCartByCreditCard(String userName, String cardNumber, String month, String year, String holder, String cvv, String id, String receiverName, String shipmentAddress, String shipmentCity, String shipmentCountry, String zipCode) {
        try{
            return new ResponseT<>(market.purchaseCartByCreditCard(userName, cardNumber, month, year, holder, cvv, id, receiverName, shipmentAddress, shipmentCity, shipmentCountry, zipCode));
        }catch(Exception e){
            return new ResponseT<>("purchaseCartByCreditCard: "+e.getMessage());
        }
    }
}
