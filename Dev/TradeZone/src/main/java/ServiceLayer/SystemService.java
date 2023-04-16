package ServiceLayer;

import DomainLayer.DTO.*;
import DomainLayer.Market;

import java.util.List;

public class SystemService {

    private Market market;

    public SystemService(){
        market = new Market();
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

    public ProductDTO getProductInfoFromStore(String userName, String storeName, String productName){
        try{

        }catch(Exception e){

        }
        return null;
    }

    public List<ProductDTO> getProductInfoFromMarketByName(String userName, String productName){
        try{

        }catch(Exception e){

        }
        return null;
    }

    public List<ProductDTO> getProductInfoFromMarketByCategory(String userName, String categoryName){
        try{

        }catch(Exception e){

        }
        return null;
    }

    public List<ProductDTO> getProductInfoFromMarketByKeyword(String userName, String keyword){
        try{

        }catch(Exception e){

        }
        return null;
    }

    public List<ProductDTO> filterByPrice(String userName, List<ProductDTO> productsInfo, Integer minPrice, Integer maxPrice){
        try{

        }catch(Exception e){

        }
        return null;
    }

    public List<ProductDTO> filterByCategory(String userName, List<ProductDTO> productsInfo, String categoryName){
        try{

        }catch(Exception e){

        }
        return null;
    }

    public List<ProductDTO> filterByProductRate(String userName, List<ProductDTO> productsInfo, Integer productRate){
        try{

        }catch(Exception e){

        }
        return null;
    }

    public List<ProductDTO> filterByStoreRate(String userName, List<ProductDTO> productsInfo, Integer storeRate){
        try{

        }catch(Exception e){

        }
        return null;
    }

    public boolean addToCart(String userName, String storeName, String productName, Integer amount){
        try{

        }catch(Exception e){

        }
        return false;
    }
    public boolean removeFromCart(String userName, String storeName, String productName){
        try{

        }catch(Exception e){

        }
        return false;
    }
    public boolean changeProductAmountInCart(String userName, String storeName, String productName, Integer newAmount){
        try{

        }catch(Exception e){

        }
        return false;
    }
    public List<BagDTO> getCartContent(String userName){
        try{

        }catch(Exception e){

        }
        return null;
    }
    public String memberLogOut(String memberUserName){
        try{

        }catch(Exception e){

        }
        return null;
    }
    public StoreDTO createStore(String memberUserName, String newStoreName){
        try{

        }catch(Exception e){

        }
        return null;
    }
    public boolean addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, Double price, String description, Integer amount){
        try{

        }catch(Exception e){

        }
        return false;
    }
    public boolean removeProductFromStock(String memberUserName, String storeName, String productName){
        try{

        }catch(Exception e){

        }
        return false;
    }
    public boolean updateProductDescription(String memberUserName, String storeName, String productName, String newProductDescription){
        try{

        }catch(Exception e){

        }
        return false;
    }

    public boolean updateProductAmount(String memberUserName, String storeName, String productName, Integer newAmount){
        try{

        }catch(Exception e){

        }
        return false;
    }

    public boolean updateProductPrice(String memberUserName, String storeName, String productName, Double newPrice){
        try{

        }catch(Exception e){

        }
        return false;
    }

    public boolean appointOtherMemberAsStoreOwner(String memberUserName, String storeName, String newOwnerUserName){
        try{

        }catch(Exception e){

        }
        return false;
    }

    public boolean appointOtherMemberAsStoreManager(String memberUserName, String storeName, String newManagerUserName){
        try{

        }catch(Exception e){

        }
        return false;
    }

    public boolean changeManagerPermissions(String memberUserName, String storeName, String managerUserName){
        try{

        }catch(Exception e){

        }
        return false;
    }

    public boolean closeStore(String memberUserName, String storeName){
        try{

        }catch(Exception e){

        }
        return false;
    }

    public List<MemberDTO> getStoreWorkersInfo(String memberUserName, String storeName){
        try{

        }catch(Exception e){

        }
        return null;
    }

    public List<DealDTO> getStoreDeals(String memberUserName, String storeName){
        try{

        }catch(Exception e){

        }
        return null;
    }

    public List<DealDTO> getMemberDeals(String systemManagerUserName, String otherMemberUserName){
        try{

        }catch(Exception e){

        }
        return null;
    }
}
