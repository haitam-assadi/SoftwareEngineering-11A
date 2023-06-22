package ServiceLayer;

import DTO.*;
import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.Controller.StoreMapper;
import DomainLayer.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SystemService {

    private Market market;
    private boolean dbFlag;


    public SystemService(boolean dbFlag){
//        JsonNode data = connectToExternalSystems();
//        String dataBaseUrl = data.get("dataBaseUrl").asText();
//        boolean dataBaseFlag = data.get("dataBaseLoadFlag").asBoolean();
//        String paymentUrl = data.get( "paymentServiceUrl").asText();
//        String shipmentUrl = data.get("shipmenServiceUrl").asText();
        this.dbFlag = dbFlag;
        market = new Market(dbFlag);
      //  if (!dbFlag){
            MemberMapper.initMapper();
            StoreMapper.initMapper();
      //  }
//        PaymentService payment = new PaymentService(paymentUrl);
//        market.setPaymentService(payment);

    }

    public ResponseT<String> initializeMarket(){

        try{
            market.loadData();
            String manager = market.firstManagerInitializer();
//            market.initMarketParsing();
            createMemberWithTwoStore("user1");
            return new ResponseT<>(manager,true);

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

        market.createProductDiscountPolicy(userName, userFirstStoreName, userFirstStoreProduct1, 30, true);
        market.createProductDiscountPolicy(userName, userFirstStoreName, userFirstStoreProduct2, 50, true);

        market.createStore(userName, userSecStoreName);
        market.addNewProductToStock(userName, userSecStoreName, userSecStoreProduct1, userSecStoreCategory1, 70.54, "new product", 100);
        market.addNewProductToStock(userName, userSecStoreName, userSecStoreProduct2, userSecStoreCategory1, 70.54, "new product", 100);
        market.addNewProductToStock(userName, userSecStoreName, userSecStoreProduct3, userSecStoreCategory1, 70.54, "new product", 100);
        market.addNewProductToStock(userName, userSecStoreName, userSecStoreProduct4, userSecStoreCategory2, 70.54, "new product", 100);
        market.addNewProductToStock(userName, userSecStoreName, userSecStoreProduct5, userSecStoreCategory2, 70.54, "new product", 100);
        market.addNewProductToStock(userName, userSecStoreName, userSecStoreProduct6, userSecStoreCategory2, 70.54, "new product", 100);


        market.addToCart(userName, userFirstStoreName, userFirstStoreProduct2, 34);

        String guest2 = market.enterMarket();
        market.register(guest2, "baraa", "Bb12345678");
        String guest3 = market.enterMarket();
        market.register(guest3, "alaa", "Bb12345678");
        market.register(market.enterMarket(), "ahmad", "Bb12345678");
//        market.login(guest2, "baraa", "Bb12345678");
        market.appointOtherMemberAsStoreOwner(userName, userFirstStoreName, "baraa");
        market.appointOtherMemberAsStoreOwner(userName, userFirstStoreName, "alaa");
        market.appointOtherMemberAsStoreOwner(userName, userFirstStoreName, "ahmad");

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

    public ResponseT<Boolean> fillOwnerContract(String memberUserName, String storeName, String newOwnerUserName, Boolean decisions){
        try{
             return new ResponseT<>(market.fillOwnerContract(memberUserName, storeName, newOwnerUserName,decisions));
        }catch(Exception e){
            return new ResponseT<>("fillOwnerContract: "+e.getMessage());
        }
    }

    public ResponseT<List<OwnerContractDTO>> getAlreadyDoneContracts(String memberUserName, String storeName){
        try{
            return new ResponseT<>(market.getAlreadyDoneContracts(memberUserName, storeName));
        }catch(Exception e){
            return new ResponseT<>("getAlreadyDoneContracts: "+e.getMessage());
        }
    }

    public ResponseT<List<OwnerContractDTO>> getMyCreatedContracts(String memberUserName, String storeName){
        try{
            return new ResponseT<>(market.getMyCreatedContracts(memberUserName, storeName));
        }catch(Exception e){
            return new ResponseT<>("getMyCreatedContracts: "+e.getMessage());
        }
    }

    public ResponseT<List<OwnerContractDTO>> getPendingContractsForOwner(String memberUserName, String storeName){
        try{
            return new ResponseT<>(market.getPendingContractsForOwner(memberUserName, storeName));
        }catch(Exception e){
            return new ResponseT<>("getPendingContractsForOwner: "+e.getMessage());
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

    public ResponseT<Boolean> AppointMemberAsSystemManager(String managerName,String otherMemberName){
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

    public ResponseT<List<DealDTO>> getMemberDeals(String memberUserName, String otherMemberUserName){
        try{
            return new ResponseT<>(market.getMemberDeals(memberUserName, otherMemberUserName));
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
            return new ResponseT<>("myStores: "+e.getMessage());
        }
    }

    public  ResponseT<Boolean> systemManagerCloseStore(String managerName, String storeName){
        try{
            return new ResponseT<>(market.systemManagerCloseStore(managerName,storeName));
        }catch (Exception e){
            return new ResponseT<>("systemManagerCloseStore: "+e.getMessage());
        }

    }

    public ResponseT<Boolean> isSystemManager(String userName){
        try{
            return new ResponseT<>(market.isSystemManager(userName));
        }catch (Exception e){
            return new ResponseT<>("isSystemManager: "+e.getMessage());
        }
    }

    public ResponseT<Integer> getProductAmountInStore(String userName, String storeName, String productName){
        try{
            return new ResponseT<>(market.getProductAmountInStore(userName,storeName,productName));
        }catch (Exception e){
            return new ResponseT<>("getProductAmountInStore: "+e.getMessage());
        }
    }

    public ResponseT<Set<String>> getAllSystemManagers(String managerName){
        try{
            return new ResponseT<>(market.getAllSystemManagers(managerName));
        }catch (Exception e){
            return new ResponseT<>("getAllSystemManagers: "+e.getMessage());
        }
    }

    public ResponseT<Boolean> removeMemberBySystemManager(String managerName,String memberName){
        try{
            return new ResponseT<>(market.removeMemberBySystemManager(managerName,memberName));
        }catch (Exception e){
            return new ResponseT<>("removeMemberBySystemManager: "+e.getMessage());
        }
    }
    ///////////////////////////////////

    public ResponseT<Boolean> addConstraintAsPaymentPolicy(String memberUserName, String storeName, Integer bagConstraintId){
        try{
            return new ResponseT<>(market.addConstraintAsPaymentPolicy(memberUserName,storeName,bagConstraintId));
        }catch (Exception e){
            return new ResponseT<>("addConstraintAsPaymentPolicy: "+e.getMessage());
        }
    }

    public ResponseT<Boolean> removeConstraintFromPaymentPolicies(String memberUserName, String storeName, Integer bagConstraintId){
        try{
            return new ResponseT<>(market.removeConstraintFromPaymentPolicies(memberUserName,storeName,bagConstraintId));
        }catch (Exception e){
            return new ResponseT<>("removeConstraintFromPaymentPolicies: "+e.getMessage());
        }
    }

    public ResponseT<List<String>> getAllPaymentPolicies(String memberUserName, String storeName){
        try{
            return new ResponseT<>(market.getAllPaymentPolicies(memberUserName,storeName));
        }catch (Exception e){
            return new ResponseT<>("getAllPaymentPolicies: "+e.getMessage());
        }

    }

    public ResponseT<List<String>> getAllBagConstraints(String memberUserName, String storeName) {
        try{
            return new ResponseT<>(market.getAllBagConstraints(memberUserName,storeName));
        }catch (Exception e){
            return new ResponseT<>("getAllBagConstraints: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createProductDiscountPolicy(String memberUserName, String storeName, String productName,  int discountPercentage, boolean addAsStoreDiscountPolicy) {
        try{
            return new ResponseT<>(market.createProductDiscountPolicy(memberUserName, storeName, productName, discountPercentage, addAsStoreDiscountPolicy));
        }catch (Exception e){
            return new ResponseT<>("createProductDiscountPolicy: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createProductDiscountPolicyWithConstraint(String memberUserName, String storeName, String productName,  int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) {
        try{
            return new ResponseT<>(market.createProductDiscountPolicyWithConstraint(memberUserName, storeName, productName, discountPercentage, bagConstraintId, addAsStoreDiscountPolicy));
        }catch (Exception e){
            return new ResponseT<>("createProductDiscountPolicyWithConstraint: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createCategoryDiscountPolicy(String memberUserName, String storeName, String categoryName,  int discountPercentage, boolean addAsStoreDiscountPolicy) {
        try{
            return new ResponseT<>(market.createCategoryDiscountPolicy(memberUserName, storeName, categoryName, discountPercentage, addAsStoreDiscountPolicy));
        }catch (Exception e){
            return new ResponseT<>("createCategoryDiscountPolicy: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createCategoryDiscountPolicyWithConstraint(String memberUserName, String storeName, String categoryName,  int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy)  {
        try{
            return new ResponseT<>(market.createCategoryDiscountPolicyWithConstraint(memberUserName, storeName, categoryName, discountPercentage, bagConstraintId, addAsStoreDiscountPolicy));
        }catch (Exception e){
            return new ResponseT<>("createCategoryDiscountPolicyWithConstraint: "+e.getMessage());
        }
    }


    public ResponseT<Integer> createAllStoreDiscountPolicy(String memberUserName, String storeName, int discountPercentage, boolean addAsStoreDiscountPolicy)  {
        try{
            return new ResponseT<>(market.createAllStoreDiscountPolicy(memberUserName, storeName, discountPercentage, addAsStoreDiscountPolicy));
        }catch (Exception e){
            return new ResponseT<>("createAllStoreDiscountPolicy: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createAllStoreDiscountPolicyWithConstraint(String memberUserName, String storeName, int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy)  {
        try{
            return new ResponseT<>(market.createAllStoreDiscountPolicyWithConstraint(memberUserName, storeName, discountPercentage, bagConstraintId, addAsStoreDiscountPolicy));
        }catch (Exception e){
            return new ResponseT<>("createAllStoreDiscountPolicyWithConstraint: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createAdditionDiscountPolicy(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, boolean addAsStoreDiscountPolicy) {
        try{
            return new ResponseT<>(market.createAdditionDiscountPolicy(memberUserName, storeName, firstDiscountPolicyId, secondDiscountPolicyId, addAsStoreDiscountPolicy));
        }catch (Exception e){
            return new ResponseT<>("createAdditionDiscountPolicy: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createAdditionDiscountPolicyWithConstraint(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) {
        try{
            return new ResponseT<>(market.createAdditionDiscountPolicyWithConstraint(memberUserName, storeName, firstDiscountPolicyId, secondDiscountPolicyId, bagConstraintId, addAsStoreDiscountPolicy));
        }catch (Exception e){
            return new ResponseT<>("createAdditionDiscountPolicyWithConstraint: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createMaxValDiscountPolicy(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, boolean addAsStoreDiscountPolicy) {
        try{
            return new ResponseT<>(market.createMaxValDiscountPolicy(memberUserName, storeName, firstDiscountPolicyId, secondDiscountPolicyId, addAsStoreDiscountPolicy));
        }catch (Exception e){
            return new ResponseT<>("createMaxValDiscountPolicy: "+e.getMessage());
        }
    }

    public ResponseT<Integer> createMaxValDiscountPolicyWithConstraint(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) {
        try{
            return new ResponseT<>(market.createMaxValDiscountPolicyWithConstraint(memberUserName, storeName, firstDiscountPolicyId, secondDiscountPolicyId, bagConstraintId, addAsStoreDiscountPolicy));
        }catch (Exception e){
            return new ResponseT<>("createMaxValDiscountPolicyWithConstraint: "+e.getMessage());
        }
    }


    public ResponseT<Boolean> addAsStoreDiscountPolicy(String memberUserName, String storeName, Integer discountPolicyId) {
        try{
            return new ResponseT<>(market.addAsStoreDiscountPolicy(memberUserName, storeName, discountPolicyId));
        }catch (Exception e){
            return new ResponseT<>("addAsStoreDiscountPolicy: "+e.getMessage());
        }
    }

    public ResponseT<Boolean> removeFromStoreDiscountPolicies(String memberUserName, String storeName, Integer discountPolicyId) {
        try{
            return new ResponseT<>(market.removeFromStoreDiscountPolicies(memberUserName, storeName, discountPolicyId));
        }catch (Exception e){
            return new ResponseT<>("removeFromStoreDiscountPolicies: "+e.getMessage());
        }
    }


    public ResponseT<List<String>> getAllCreatedDiscountPolicies(String memberUserName, String storeName) {
        try{
            return new ResponseT<>(market.getAllCreatedDiscountPolicies(memberUserName, storeName));
        }catch (Exception e){
            return new ResponseT<>("getAllCreatedDiscountPolicies: "+e.getMessage());
        }
    }

    public ResponseT<List<String>> getAllStoreDiscountPolicies(String memberUserName, String storeName) {
        try{
            return new ResponseT<>(market.getAllStoreDiscountPolicies(memberUserName, storeName));
        }catch (Exception e){
            return new ResponseT<>("getAllStoreDiscountPolicies: "+e.getMessage());
        }
    }

    public ResponseT<MemberDTO> getMemberInfo(String callerMemberName, String returnedMemberName) {
        try{
            return new ResponseT<>(market.getMemberInfo(callerMemberName, returnedMemberName));
        }catch (Exception e){
            return new ResponseT<>("getMemberInfo: "+e.getMessage());
        }
    }
    public ResponseT<Double> getCartPriceBeforeDiscount(String memberUserName) {
        try{
            return new ResponseT<>(market.getCartPriceBeforeDiscount(memberUserName));
        }catch (Exception e){
            return new ResponseT<>("getCartPriceBeforeDiscount: "+e.getMessage());
        }
    }

    public ResponseT<Double> getCartPriceAfterDiscount(String memberUserName) {
        try{
            return new ResponseT<>(market.getCartPriceAfterDiscount(memberUserName));
        }catch (Exception e){
            return new ResponseT<>("getCartPriceAfterDiscount: "+e.getMessage());
        }
    }



    //return 1=storeFounder, 2=storeOwner, 3=storeManager, -1= noRule
    public ResponseT<Integer> getRuleForStore(String storeName, String memberName){
        try{
            return new ResponseT<>(market.getRuleForStore(storeName,memberName));
        }catch (Exception e){
            return new ResponseT<>("getRuleForStore: "+e.getMessage());
        }

    }



    public ResponseT<Boolean> updateManagerPermissionsForStore(String ownerUserName, String storeName, String managerUserName, List<Integer> newPermissions){
        try{
            return new ResponseT<>(market.updateManagerPermissionsForStore(ownerUserName, storeName, managerUserName, newPermissions));
        }catch (Exception e){
            return new ResponseT<>("updateManagerPermissionsForStore: "+e.getMessage());
        }
    }

    public ResponseT<List<Integer>> getManagerPermissionsForStore(String ownerUserName, String storeName, String managerUserName){
        try{
            return new ResponseT<>(market.getManagerPermissionsForStore(ownerUserName, storeName, managerUserName));
        }catch (Exception e){
            return new ResponseT<>("getManagerPermissionsForStore: "+e.getMessage());
        }
    }

    //FOR ACCTEST OF STORE MANAGER
    public void takeDownSystemManagerAppointment(String storeName, String appointedMember) throws Exception {
        this.market.takeDownSystemManagerAppointment(storeName, appointedMember);
    }

    public ResponseT<List<String>> checkForAppendingMessages(String guestName){
        try{
            List<String> messages = market.checkForAppendingMessages(guestName);
            return new ResponseT<>(messages);
        }catch (Exception e){
            return new ResponseT<>("checkForAppendingMessages: " + e.getMessage());
        }
    }
    public ResponseT<List<String>> getAllPermissions(String ownerUserName, String storeName) {
        try{
            return new ResponseT<>(market.getAllPermissions(ownerUserName, storeName));
        }catch (Exception e){
            return new ResponseT<>("getAllPermissions: "+e.getMessage());
        }
    }

    public ResponseT<List<String>> getLiveMessages(String memberName){
        try{
            List<String> messages = market.getLiveMessages(memberName);
            return new ResponseT<>(messages);
        }catch (Exception e){
            return new ResponseT<>("get live messages: " + e.getMessage());
        }
    }

    public ResponseT<Boolean> clearMessages(String name) {
        try{
            market.clearMessages(name);
            return new ResponseT<>(true);
        }catch (Exception e){
            return new ResponseT<>("clear messages: " + e.getMessage());
        }

    }

    public void send(String member1Name, String message) throws Exception {
        market.send(member1Name, message);
    }

    public Set<String> getAppendingMessages(String memberUserName) {
        return market.getAppendingMessages(memberUserName);
    }

    private String getJSONFromFile(String filename) {
        String jsonText = "";
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new FileReader(filename));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonText += line + "\n";
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonText;
    }

    //WE MAY USE IT TO CONNECT TO A REMOTE DB

    private String getJSONFromURL(String strUrl) {
        String jsonText = "";

        try {
            URL url = new URL(strUrl);
            InputStream is = url.openStream();

            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonText += line + "\n";
            }

            is.close();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return jsonText;
    }

    private JsonNode connectToExternalSystems(){
        String strJson = getJSONFromFile("Dev/TradeZone/JsonFiles/externalSystemsData.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse the JSON string
            JsonNode jsonNode = objectMapper.readTree(strJson);
            return jsonNode;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }
    public ResponseT<Boolean> loadData(){
        try{
            market.loadData();
            return new ResponseT<>(true);
        }catch(Exception e){
            return new ResponseT<>("loadData: "+e.getMessage());
        }
    }

}
