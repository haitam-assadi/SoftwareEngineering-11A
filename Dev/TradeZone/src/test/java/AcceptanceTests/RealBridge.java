package AcceptanceTests;

import DTO.BagDTO;
import DTO.MemberDTO;
import DTO.ProductDTO;
import DTO.StoreDTO;
import ServiceLayer.ResponseT;
import ServiceLayer.SystemService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// error:
// string  null, int -1, boolean false, LinkedList<?> empty

public class RealBridge implements Bridge{
    private SystemService systemService; //TODO: = new or getinstance()


    public RealBridge(){
        systemService = new SystemService();
    }

    @Override
    public boolean initializeMarket() throws Exception {
        ResponseT<Boolean> response = systemService.initializeMarket();
        if(response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public String enterMarket() throws Exception { // Done
        ResponseT<String> response = systemService.enterMarket();
        if(response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public boolean exitMarket(String userName) throws Exception { // Done
        ResponseT<Boolean> response = systemService.exitMarket(userName);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public boolean register(String guestUserName, String newMemberUserName, String password) throws Exception {
        ResponseT<Boolean> response = systemService.register(guestUserName,newMemberUserName,password);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public String login(String guestUserName, String MemberUserName, String password)  throws Exception {
        ResponseT<String> response = systemService.login(guestUserName, MemberUserName, password);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public String memberLogOut(String memberUserName) throws Exception {
        ResponseT<String> response = systemService.memberLogOut(memberUserName);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public String createStore(String memberUserName, String newStoreName) throws Exception {
        ResponseT<StoreDTO> response = systemService.createStore(memberUserName, newStoreName);
        if(response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue().storeName;
    }

    @Override
    public boolean addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, Double price, String description, int amount) throws Exception {
        ResponseT<Boolean> response = systemService.addNewProductToStock(memberUserName, storeName, nameProduct,
                category, price, description, amount);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public boolean removeProductFromStock(String memberUserName, String storeName, String productName) throws Exception {
        ResponseT<Boolean> response = systemService.removeProductFromStock(memberUserName, storeName, productName);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public boolean addCategory(String userName, String categoryName, String storeName) { // TODO: add to market and service 15
        return false;
    }

    @Override
    public boolean getCategory(String userName, String categoryName,String storeName) { // TODO: add to market and service 5
        return false;
    }

    @Override
    public boolean updateProductName(String memberUserName, String storeName, String productName, String newName) { // TODO: add to market and service 2
            return false;
    }

    @Override
    public boolean updateProductPrice(String memberUserName, String storeName, String productName, Double price) throws Exception {
        ResponseT<Boolean> response = systemService.updateProductPrice(memberUserName, storeName, productName, price);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public boolean updateProductDescription(String memberUserName, String storeName, String productName, String newDescription) throws Exception {
        ResponseT<Boolean> response = systemService.updateProductDescription(memberUserName,storeName,productName,newDescription);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public boolean updateProductAmount(String memberUserName, String storeName, String productName, int amount) throws Exception {


        ResponseT<Boolean> response = systemService.updateProductAmount(memberUserName,storeName,productName,amount);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public int getProductAmount(String storeName, String productName) { // String userName // TODO: add to market and service
        // TODO: add amount field to ProductDTO or add a function get product amount to market and service
        return -1;
    }

    @Override
    public boolean appointOtherMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName) throws Exception {
        ResponseT<Boolean> response = systemService.appointOtherMemberAsStoreOwner(memberUserName, storeName, newOwnerUserName);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public String getOwnerAppointer(String OwnerName, String storeName) { // TODO: add to market and service
            return "";
    }

    @Override
    public boolean appointOtherMemberAsStoreManager(String memberUserName, String storeName, String newOwnerUserName) throws Exception {
        ResponseT<Boolean> response = systemService.appointOtherMemberAsStoreManager(memberUserName, storeName, newOwnerUserName);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public String getManagerAppointer(String ManagerName, String storeName) { // TODO: add to market and service
            return "";
    }

    @Override
    public boolean closeStore(String memberUserName, String storeName) throws Exception {
        // I changed the return type, it was String
        // TODO: closeStore in service returns boolean
        ResponseT<Boolean> response = systemService.closeStore(memberUserName, storeName);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
//            return "";
    }

    @Override
    public boolean canGetStoreInfo(String userName, String storeName) throws Exception { // TODO: add to market and service
        ResponseT<StoreDTO> response = systemService.getStoreInfo(userName,storeName);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return true;
    }
    @Override
    public Map<Integer, List<String>> getStoreRulesInfo(String ownerName, String storeName) throws Exception { // TODO: add to market and service
        // List<memberDTO> getStoreWorkersInfo
        ResponseT<List<MemberDTO>> response = systemService.getStoreWorkersInfo(ownerName, storeName);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        Map<Integer, List<String>> rulesInfo = new HashMap<>();
        for(MemberDTO member : response.getValue()){
            if(member.jobTitle.equals("StoreFounder")){
                if(!rulesInfo.containsKey(0))
                    rulesInfo.put(0, new LinkedList<>());
                rulesInfo.get(0).add(member.username);
            }
            else if (member.jobTitle.equals("StoreOwner")){
                if(!rulesInfo.containsKey(1))
                    rulesInfo.put(1, new LinkedList<>());
                rulesInfo.get(1).add(member.username);
            }
            else if (member.jobTitle.equals("StoreManager")){
                if(!rulesInfo.containsKey(2))
                    rulesInfo.put(2, new LinkedList<>());
                rulesInfo.get(2).add(member.username);
            }
        }
        return rulesInfo;
    }

    @Override
    public String getStoreFounderName(String userName, String storeName) throws Exception { // Done
        ResponseT<StoreDTO> response = systemService.getStoreInfo(userName, storeName);
        if(response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue().founderName;
    }

    @Override
    public List<String> getStoreOwnersNames(String userName, String storeName) throws Exception { // Done
        ResponseT<StoreDTO> response = systemService.getStoreInfo(userName, storeName);
        if(response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue().ownersNames;
    }

    @Override
    public List<String> getStoreManagersNames(String userName, String storeName) throws Exception { // Done
        ResponseT<StoreDTO> response = systemService.getStoreInfo(userName, storeName);
        if(response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue().managersNames;
    }

    @Override
    public Double getProductPrice(String userName, String storeName, String productName) throws Exception { // Done
        ResponseT<ProductDTO> response = systemService.getProductInfoFromStore(userName, storeName, productName);
        if(response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue().price;
    }

    @Override
    public String getProductDescription(String userName, String storeName, String productName) throws Exception { // Done
        ResponseT<ProductDTO> response = systemService.getProductInfoFromStore(userName, storeName, productName);
        if(response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue().description;
    }

    public List<String> getAllGuests() throws Exception {
        ResponseT<List<String>> response = systemService.getAllGuests();
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    public List<String> getAllMembers() throws Exception {
        ResponseT<List<String>> response = systemService.getAllMembers();
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }



    public List<String> getAllOnlineMembers() throws Exception {
        ResponseT<List<String>> response = systemService.getAllLoggedInMembers();
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }


    public List<String> getAllStoresNames()  throws Exception{
        ResponseT<List<String>> response = systemService.getAllStoresNames();
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    public List<String> getStoreProducts(String userName, String storeName) throws Exception { // Done
        ResponseT<StoreDTO> response = systemService.getStoreInfo(userName, storeName);
        if(response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        List<String> ret = new LinkedList<>();
        List<ProductDTO> storeProducts = response.getValue().productsInfo;
        for(ProductDTO p : storeProducts){
            ret.add(p.name);
        }
        return ret;
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByName(String userName, String productName) throws Exception { // map <storeName, List<productName>>
        ResponseT<List<ProductDTO>> response = systemService.getProductInfoFromMarketByName(userName, productName);
        if(response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        Map<String, List<String>> ret = new HashMap<>();
        for(ProductDTO p : response.getValue()){
            String storeName = p.storeName;
            if(ret.containsKey(storeName)){
                ret.get(storeName).add(p.name);
            }
            else{
                List<String> storeProducts = new LinkedList<>();
                storeProducts.add(p.name);
                ret.put(storeName, storeProducts);
            }
        }
        return ret;
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByCategory(String userName, String categoryName) throws Exception { // map <storeName, List<productName>>
        ResponseT<List<ProductDTO>> response = systemService.getProductInfoFromMarketByCategory(userName, categoryName);
        if(response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        Map<String, List<String>> ret = new HashMap<>();
        for(ProductDTO p : response.getValue()){
            String storeName = p.storeName;
            if(ret.containsKey(storeName)){
                ret.get(storeName).add(p.name);
            }
            else{
                List<String> storeProducts = new LinkedList<>();
                storeProducts.add(p.name);
                ret.put(storeName, storeProducts);
            }
        }
        return ret;
    }

    @Override
    public Map<String, List<String>> getProductInfoFromMarketByKeyword(String userName, String keyword) throws Exception { // map <storeName, List<productName>>
        ResponseT<List<ProductDTO>> response = systemService.getProductInfoFromMarketByKeyword(userName, keyword);
        if(response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        Map<String, List<String>> ret = new HashMap<>();
        for(ProductDTO p : response.getValue()){
            String storeName = p.storeName;
            if(ret.containsKey(storeName)){
                ret.get(storeName).add(p.name);
            }
            else{
                List<String> storeProducts = new LinkedList<>();
                storeProducts.add(p.name);
                ret.put(storeName, storeProducts);
            }
        }
        return ret;
    }

    @Override
    public Map<String, List<String>> filterByPrice(String userName, Map<String, List<String>> products, int minPrice, int maxPrice) throws Exception { // map <storeName, List<productName>>
        // build List<ProductDTO> productsInfo
        List<ProductDTO> productsInfo = new LinkedList<>();
        for(String storeName : products.keySet()){
            for(String productName : products.get(storeName)){
                ResponseT<ProductDTO> response = systemService.getProductInfoFromStore(userName, storeName, productName);
                if(response.ErrorOccurred){
                    throw new Exception(response.errorMessage);
                }
                productsInfo.add(response.getValue());
            }
        }
        // filter
        ResponseT<List<ProductDTO>> response2 = systemService.filterByPrice(userName, productsInfo, minPrice, maxPrice);
        if(response2.ErrorOccurred){
            throw new Exception(response2.errorMessage);
        }
        Map<String, List<String>> ret = new HashMap<>();
        for(ProductDTO p : response2.getValue()){
            String storeName = p.storeName;
            if(ret.containsKey(storeName)){
                ret.get(storeName).add(p.name);
            }
            else{
                List<String> storeProducts = new LinkedList<>();
                storeProducts.add(p.name);
                ret.put(storeName, storeProducts);
            }
        }
        return ret;
    }

    @Override
    public Map<String, List<String>> filterByCategory(String userName, Map<String, List<String>> products, String categoryName) throws Exception { // map <storeName, List<productName>>
        // build List<ProductDTO> productsInfo
        List<ProductDTO> productsInfo = new LinkedList<>();
        for(String storeName : products.keySet()){
            for(String productName : products.get(storeName)){
                ResponseT<ProductDTO> response = systemService.getProductInfoFromStore(userName, storeName, productName);
                if(response.ErrorOccurred){
                    throw new Exception(response.errorMessage);
                }
                productsInfo.add(response.getValue());
            }
        }
        // filter
        ResponseT<List<ProductDTO>> response2 = systemService.filterByCategory(userName, productsInfo, categoryName);
        if(response2.ErrorOccurred){
            throw new Exception(response2.errorMessage);
        }
        Map<String, List<String>> ret = new HashMap<>();
        for(ProductDTO p : response2.getValue()){
            String storeName = p.storeName;
            if(ret.containsKey(storeName)){
                ret.get(storeName).add(p.name);
            }
            else{
                List<String> storeProducts = new LinkedList<>();
                storeProducts.add(p.name);
                ret.put(storeName, storeProducts);
            }
        }
        return ret;
    }

    @Override
    public boolean addToCart(String userName, String storeName, String productName, Integer amount) throws Exception {
        ResponseT<Boolean> response = systemService.addToCart(userName,storeName,productName,amount);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public List<String> getBag(String userName, String storeName) throws Exception {
        ResponseT<List<BagDTO>> response = systemService.getCartContent(userName);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        ConcurrentHashMap<ProductDTO, Integer> bagContent = null;
        List<String> productNames = new ArrayList<>();
        for(BagDTO bagDTO: response.getValue())
            if(bagDTO.storeBag.equals(storeName))
                bagContent=bagDTO.bagContent;

        if(bagContent !=null ){
            for(ProductDTO productDTO: bagContent.keySet())
                productNames.add(productDTO.name);
        }
        return productNames;
    }

    @Override
    public int getProductAmountInCart(String userName, String storeName, String productName) throws Exception {
        ResponseT<List<BagDTO>> response = systemService.getCartContent(userName);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        ConcurrentHashMap<ProductDTO, Integer> bagContent = null;
        int productAmount =-1;
        for(BagDTO bagDTO: response.getValue())
            if(bagDTO.storeBag.equals(storeName))
                bagContent=bagDTO.bagContent;

        if(bagContent !=null ){
            for(ProductDTO productDTO: bagContent.keySet()){
                if(productDTO.name.equals(productName)){
                    productAmount = bagContent.get(productDTO);
                    break;
                }
            }
        }
        return productAmount;
    }

    @Override
    public Map<String, List<String>> getCartContent(String userName) throws Exception { // map: <bag.storeName, list<productName>>
        ResponseT<List<BagDTO>> response = systemService.getCartContent(userName);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        Map<String, List<String>> cartContent = new HashMap<>();
        List<String> productNames;
        for(BagDTO bagDTO: response.getValue()){
            productNames = new ArrayList<>();
            cartContent.put(bagDTO.storeBag, productNames);
            for(ProductDTO productDTO: bagDTO.bagContent.keySet())
                productNames.add(productDTO.name);
        }

        return cartContent;
    }

    @Override
    public boolean removeProductFromCart(String userName, String storeName, String productName) throws Exception {
        ResponseT<Boolean> response = systemService.removeFromCart(userName,storeName,productName);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }

    @Override
    public boolean changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount) throws Exception{
        ResponseT<Boolean> response = systemService.changeProductAmountInCart(userName,storeName,productName,newAmount);
        if (response.ErrorOccurred){
            throw new Exception(response.errorMessage);
        }
        return response.getValue();
    }
}