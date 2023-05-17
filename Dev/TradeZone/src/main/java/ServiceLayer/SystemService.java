package ServiceLayer;

import DTO.*;
import DomainLayer.LoggerManager;
import DomainLayer.Market;
import DomainLayer.PaymentService;
import DomainLayer.ShipmentService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SystemService {

    private Market market;

    public SystemService(){
        market = new Market();
    }

    public ResponseT<Boolean> initializeMarket(){

        try{
            String manager = market.firstManagerInitializer();
            createMemberWithTwoStore(manager);
            return new ResponseT<>(true);

        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }

    }

    public void setPaymentService(PaymentService paymentService){
       market.setPaymentService(paymentService);
    }
    public void setShipmentService(ShipmentService shipmentService){
        market.setShipmentService(shipmentService);
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


        market.addToCart(userName, userFirstStoreName, userFirstStoreProduct2, 34);


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
            String loggerMsg ="\nregister(String guestUserName, String newMemberUserName, String password)\n"+ "in " + this.nowTime() + " the user " + guestUserName + " wants to make registeration "+ "register("+guestUserName+", " + newMemberUserName + "," + password + ")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            ResponseT<Boolean> res = new ResponseT<>(market.register(guestUserName, newMemberUserName, password));
            loggerMsg = "in " + this.nowTime() + " a new member joined .\n- Memer name: " + newMemberUserName+"\n";
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return res;
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in registeration: " + e.getMessage());
            return new ResponseT<>("register: "+e.getMessage());
        }
    }
    public ResponseT<String> login(String guestUserName, String MemberUserName, String password){
        try{
            String loggerMsg = "\nlogin(String guestUserName, String MemberUserName, String password)\n" +"in " + this.nowTime() + " the user " + guestUserName + " wants to make login "+ "login("+guestUserName+", " + MemberUserName + "," + password + ")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            ResponseT<String> res = new ResponseT<>(market.login(guestUserName, MemberUserName, password), true);
            loggerMsg = "in " + this.nowTime() + " a new member Logged in .\n- Memer name: " + MemberUserName+"\n";
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return res;
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in login: " + e.getMessage());
            return new ResponseT<>("login: "+e.getMessage());
        }
    }
    public ResponseT<StoreDTO> getStoreInfo(String userName, String storeName){
        try{
            String loggerMsg = "\ngetStoreInfo(String userName, String storeName)\n"+
                    "in " + this.nowTime() + " the user " + userName + " wants to get information about the store "+storeName+ "- getStoreInfo("+userName+", " + storeName +")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            ResponseT<StoreDTO> res = new ResponseT<>(market.getStoreInfo(userName, storeName));
            loggerMsg = "in " + this.nowTime() + " the user " + userName + " got information about the store "+storeName;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return res;

        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in getting store info: " + e.getMessage());
            return new ResponseT<>("getStoreInfo: "+e.getMessage());
        }
    }

    public ResponseT<ProductDTO> getProductInfoFromStore(String userName, String storeName, String productName){
        try{
            String loggerMsg = "\ngetProductInfoFromStore(String userName, String storeName, String productName)\n"+"in " + this.nowTime() + " the user " + userName + " wants to get information about product "+productName + " in the store "+storeName+ "- getProductInfoFromStore("+userName+", " + storeName +")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.getProductInfoFromStore(userName, storeName, productName));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in getting product info: " + e.getMessage());
            return new ResponseT<>("getProductInfoFromStore: "+e.getMessage());
        }
    }

    public ResponseT<List<ProductDTO>> getProductInfoFromMarketByName(String userName, String productName){
        try{
            String loggerMsg = "\ngetProductInfoFromStore(String userName, String storeName, String productName)\n"+"in " + this.nowTime() + " the user " + userName + " wants to get information about product in market my name"+productName +"- getProductInfoFromMarketByName("+userName+", " + productName +")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.getProductInfoFromMarketByName(userName,productName));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in getting products info: " + e.getMessage());
            return new ResponseT<>("getProductInfoFromMarketByName: "+e.getMessage());
        }
    }

    public ResponseT<List<ProductDTO>> getProductInfoFromMarketByCategory(String userName, String categoryName){
        try{
            String loggerMsg = "\ngetProductInfoFromMarketByCategory(String userName, String categoryName)\n"+"in " + this.nowTime() + " the user " + userName + " wants to get information about products in market by category {"+categoryName +"} - getProductInfoFromMarketByCategory("+userName+", " + categoryName +")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.getProductInfoFromMarketByCategory(userName,categoryName));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in getting product info by category: " + e.getMessage());
            return new ResponseT<>("getProductInfoFromMarketByCategory: "+e.getMessage());
        }
    }

    public ResponseT<List<ProductDTO>> getProductInfoFromMarketByKeyword(String userName, String keyword){
        try{
            String loggerMsg ="\ngetProductInfoFromMarketByKeyword(String userName, String keyword)\n"+ "in " + this.nowTime() + " the user " + userName + " wants to get information about products in market by keyword "+keyword +"} - getProductInfoFromMarketByCategory("+userName+", " + keyword +")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.getProductInfoFromMarketByKeyword(userName,keyword));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in getting product info ny keyword: " + e.getMessage());
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
            String loggerMsg ="\naddToCart(String userName, String storeName, String productName, Integer amount)\n"+ "in " + this.nowTime() + " the user " + userName + " tries to add product to his cart  - addToCart("+userName+", " + storeName+", "+productName+", " +amount+")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.addToCart(userName,storeName, productName,amount));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in adding product to cart: " + e.getMessage());
            return new ResponseT<>("addToCart: "+e.getMessage());
        }
    }
    public ResponseT<Boolean> removeFromCart(String userName, String storeName, String productName){
        try{
            String loggerMsg ="\nremoveFromCart(String userName, String storeName, String productName)\n"+ "in " + this.nowTime() + " the user " + userName + " tries to remove product from his cart  - removeFromCart("+userName+", " + storeName+", "+productName+")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.removeFromCart(userName,storeName, productName));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in remove product from cart: " + e.getMessage());
            return new ResponseT<>("removeFromCart: "+e.getMessage());
        }
    }
    public ResponseT<Boolean> changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount){
        try{
            String loggerMsg = "\n changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount)\n"+"in " + this.nowTime() + " the user " + userName + " tries to change a product amount in his cart  - changeProductAmountInCart("+userName+", " + storeName+", "+productName+", " + newAmount+")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.changeProductAmountInCart(userName,storeName, productName,newAmount));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in changing product amount in cart: " + e.getMessage());
            return new ResponseT<>("changeProductAmountInCart: "+e.getMessage());
        }
    }
    public ResponseT<List<BagDTO>> getCartContent(String userName){
        try{
            String loggerMsg ="\ngetCartContent(String userName)\n"+ "in " + this.nowTime() + " the user " + userName + " tries to get his cart content  - getCartContent("+userName+")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.getCartContent(userName));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in getting cart content: " + e.getMessage());
            return new ResponseT<>("getCartContent: "+e.getMessage());
        }
    }
    public ResponseT<String> memberLogOut(String memberUserName){
        try{
            String loggerMsg ="\nmemberLogOut(String memberUserName)\n"+ "in " + this.nowTime() + " the member " + memberUserName + " wants to make logout- "+ "logout("+memberUserName + ")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.memberLogOut(memberUserName), true);
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in logging out: " + e.getMessage());
            return new ResponseT<>("memberLogOut: "+e.getMessage());
        }
    }
    public ResponseT<StoreDTO> createStore(String memberUserName, String newStoreName){
        try{
            String loggerMsg = "\ncreateStore(String memberUserName, String newStoreName)\n"+"in " + this.nowTime() + " the member " + memberUserName + " tries to create store  - createStore("+memberUserName+", " + newStoreName+")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.createStore(memberUserName, newStoreName));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in creating store: " + e.getMessage());
            return new ResponseT<>("createStore: "+e.getMessage());
        }
    }
    public ResponseT<Boolean> addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, Double price, String description, Integer amount){
        try{
            String loggerMsg ="\n addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, Double price, String description, Integer amount)\n"+
                    "in " + this.nowTime() + " the member " + memberUserName + " tries to add product to store  - addNewProductToStock("+memberUserName+", "+storeName+", "+nameProduct+", "+category+", "+price +", "+description +", " + amount+")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.addNewProductToStock(memberUserName, storeName, nameProduct,category,price,description,amount));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in adding product to stock: " + e.getMessage());
            return new ResponseT<>("addNewProductToStock: "+e.getMessage());
        }
    }
    public ResponseT<Boolean> removeProductFromStock(String memberUserName, String storeName, String productName){
        try{
            String loggerMsg ="\nremoveProductFromStock(String memberUserName, String storeName, String productName)\n"+
                    "in " + this.nowTime() + " the member " + memberUserName + " tries to remove product from store  - removeProductFromStock("+memberUserName+", "+storeName+", "+productName+")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.removeProductFromStock(memberUserName, storeName, productName));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in removing product from store: " + e.getMessage());
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
            String loggerMsg ="\nappointOtherMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName)\n"
                    + "in " + this.nowTime() + " the member " + memberUserName + " tries to assign other user as store owner  - appointOtherMemberAsStoreOwner("+memberUserName+", "+storeName+", "+newOwnerUserName+")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.appointOtherMemberAsStoreOwner(memberUserName, storeName, newOwnerUserName));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in appointing member as store owner: " + e.getMessage());
            return new ResponseT<>("appointOtherMemberAsStoreOwner: "+e.getMessage());
        }
    }

    public ResponseT<Boolean> appointOtherMemberAsStoreManager(String memberUserName, String storeName, String newManagerUserName){
        try{
            String loggerMsg ="\nappointOtherMemberAsStoreManager(String memberUserName, String storeName, String newManagerUserName)\n"+
                    "in " + this.nowTime() + " the member " + memberUserName + " tries to assign other user as store manager  - appointOtherMemberAsStoreManager("+memberUserName+", "+storeName+", "+newManagerUserName+")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.appointOtherMemberAsStoreManager(memberUserName, storeName, newManagerUserName));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in appointing member as store manager: " + e.getMessage());
            return new ResponseT<>("appointOtherMemberAsStoreManager: "+e.getMessage());
        }
    }

    public ResponseT<Boolean> AppointMemberAsSystemManager(String managerName,String otherMemberName) throws Exception {
        try{
            String loggerMsg ="\nAppointMemberAsSystemManager(String managerName, String otherMemberName)\n"+
                    "in " + this.nowTime() + " the system manager " + managerName + " tries to assign other user as system manager  - AppointMemberAsSystemManager("+managerName+", "+otherMemberName+")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.AppointMemberAsSystemManager(managerName,otherMemberName));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in appointing member as system manager: " + e.getMessage());
            return new ResponseT<>("AppointMemberAsSystemManager: "+e.getMessage());
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
            String loggerMsg ="\ncloseStore(String memberUserName, String storeName)\n"+
                    "in " + this.nowTime() + " the member " + memberUserName + " tries close store  - closeStore("+memberUserName+", "+storeName+")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.closeStore(memberUserName, storeName));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in closing the store: " + e.getMessage());
            return new ResponseT<>("closeStore: "+e.getMessage());
        }
    }

    public ResponseT<List<MemberDTO>> getStoreWorkersInfo(String memberUserName, String storeName){
        try{
            String loggerMsg ="\ngetStoreWorkersInfo(String memberUserName, String storeName)\n"+
                    "in " + this.nowTime() + " the member " + memberUserName + " tries to get information about the workers in a store  - getStoreWorkersInfo("+memberUserName+", "+storeName+")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.getStoreWorkersInfo(memberUserName, storeName));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in getting store workers info: " + e.getMessage());
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

    public ResponseT<Boolean> purchaseCartByCreditCard(String userName, String cardNumber, String month, String year, String holder, String cvv, String id, String receiverName, String shipmentAddress, String shipmentCity, String shipmentCountry, String zipCode){
        try{
            String loggerMsg ="\npurchaseCartByCreditCard(String userName, String cardNumber, String month, String year, String holder, String cvv, String id, String receiverName, String shipmentAddress, String shipmentCity, String shipmentCountry, String zipCode)\n"+
                    "in " + this.nowTime() + " the user " + userName + " tries make purchase  - purchaseCartByCreditCard("+userName+", " + cardNumber+", " + month+", "+ year+", " +  holder+", " +  cvv+", " +  id + ", " + receiverName + ", " +  shipmentAddress +", " +shipmentCity + ", "+  shipmentCountry+", " + zipCode +")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.purchaseCartByCreditCard(userName, cardNumber, month, year, holder, cvv, id, receiverName, shipmentAddress, shipmentCity, shipmentCountry, zipCode));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in purchasing: " + e.getMessage());
            return new ResponseT<>("purchaseCartByCreditCard: "+e.getMessage());
        }
    }

    public ResponseT<Boolean> removeOwnerByHisAppointer(String appointerUserName, String storeName, String ownerUserName){
        try{
            String loggerMsg ="\nremoveOwnerByHisAppointer(String appointerUserName, String storeName, String ownerUserName)\n"+
                    "in " + this.nowTime() + " the member " + appointerUserName + " tries to remove other owner from being owner in a store  - removeOwnerByHisAppointer("+storeName+", "+ownerUserName+")" ;
            LoggerManager.getInstance().sendEventLog(loggerMsg);
            return new ResponseT<>(market.removeOwnerByHisAppointer(appointerUserName, storeName,ownerUserName));
        }catch(Exception e){
            LoggerManager.getInstance().sendErrorLog("\nfail in remove owner: " + e.getMessage());
            return new ResponseT<>("removeOwnerByHisAppointer: "+e.getMessage());
        }
    }
    public ResponseT<Integer> createMaxProductAmountAllContentBagConstraint(String memberUserName, String storeName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) {
        try{
            return new ResponseT<>(market.createMaxProductAmountAllContentBagConstraint(memberUserName, storeName,productName, amountLimit, addAsStorePaymentPolicy));
        }catch(Exception e){
            return new ResponseT<>("createMaxProductAmountAllContentBagConstraint: "+e.getMessage());
        }
    }


    public ResponseT<Integer> createMinProductAmountAllContentBagConstraint(String memberUserName, String storeName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) {
        try{
            return new ResponseT<>(market.createMinProductAmountAllContentBagConstraint(memberUserName, storeName,productName, amountLimit, addAsStorePaymentPolicy));
        }catch(Exception e){
            return new ResponseT<>("createMinProductAmountAllContentBagConstraint: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createMaxTimeAtDayProductBagConstraint(String memberUserName, String storeName, String productName, int hour, int minute, boolean addAsStorePaymentPolicy) {
        try{
            return new ResponseT<>(market.createMaxTimeAtDayProductBagConstraint(memberUserName, storeName,productName, hour, minute, addAsStorePaymentPolicy));
        }catch(Exception e){
            return new ResponseT<>("createMaxTimeAtDayProductBagConstraint: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createRangeOfDaysProductBagConstraint(String memberUserName, String storeName, String productName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy) {
        try{
            return new ResponseT<>(market.createRangeOfDaysProductBagConstraint(memberUserName, storeName,productName, fromYear, fromMonth, fromDay, toYear, toMonth, toDay, addAsStorePaymentPolicy));
        }catch(Exception e){
            return new ResponseT<>("createRangeOfDaysProductBagConstraint: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createMaxTimeAtDayCategoryBagConstraint(String memberUserName, String storeName, String categoryName, int hour, int minute, boolean addAsStorePaymentPolicy) {
        try{
            return new ResponseT<>(market.createMaxTimeAtDayCategoryBagConstraint(memberUserName, storeName,categoryName, hour, minute, addAsStorePaymentPolicy));
        }catch(Exception e){
            return new ResponseT<>("createMaxTimeAtDayCategoryBagConstraint: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createRangeOfDaysCategoryBagConstraint(String memberUserName, String storeName, String categoryName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy) {
        try{
            return new ResponseT<>(market.createRangeOfDaysCategoryBagConstraint(memberUserName, storeName,categoryName, fromYear, fromMonth, fromDay, toYear, toMonth, toDay, addAsStorePaymentPolicy));
        }catch(Exception e){
            return new ResponseT<>("createRangeOfDaysCategoryBagConstraint: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createAndBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) {
        try{
            return new ResponseT<>(market.createAndBagConstraint(memberUserName, storeName,firstBagConstraintId, secondBagConstraintId,addAsStorePaymentPolicy));
        }catch(Exception e){
            return new ResponseT<>("createAndBagConstraint: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createOrBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy){
        try{
            return new ResponseT<>(market.createOrBagConstraint(memberUserName, storeName,firstBagConstraintId, secondBagConstraintId,addAsStorePaymentPolicy));
        }catch(Exception e){
            return new ResponseT<>("createOrBagConstraint: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createOnlyIfBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy){
        try{
            return new ResponseT<>(market.createOnlyIfBagConstraint(memberUserName, storeName,firstBagConstraintId, secondBagConstraintId,addAsStorePaymentPolicy));
        }catch(Exception e){
            return new ResponseT<>("createOnlyIfBagConstraint: "+e.getMessage());
        }
    }

    private String nowTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public ResponseT<Boolean> hasRole(String memberUserName){
        try{
            return new ResponseT<>(market.hasRole(memberUserName));
        }catch(Exception e){
            return new ResponseT<>("hasRole: "+e.getMessage());
        }
    }

    public ResponseT<Map<String,List<StoreDTO>>> myStores(String memberUserName){
        try {
            return new ResponseT<>(market.myStores(memberUserName));
        }catch (Exception e){
            return new ResponseT<>("meStores: "+e.getMessage());
        }
    }

}
