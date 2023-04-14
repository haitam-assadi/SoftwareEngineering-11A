package DomainLayer;

import DomainLayer.DTO.ProductDTO;
import DomainLayer.DTO.StoreDTO;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private String storeName;
    private Stock stock;
    private boolean isActive;
    private StoreFounder storeFounder;
    private ConcurrentHashMap<String, StoreOwner> storeOwners;
    private ConcurrentHashMap<String, StoreManager> storeManagers;

    //TODO:: maybe need to make it Concurrent
    private List<Deal> storeDeals;
    private List<DiscountPolicy> storeDiscountPolicies;
    private List<PaymentPolicy> storePaymentPolicies;

    public Store() {
        storeOwners = new ConcurrentHashMap<>();
        stock = new Stock();
    }

    public boolean addNewProductToStock(String memberUserName,String nameProduct,String category, Integer price, String details, Integer amount) throws Exception {
        if(amount < 0)
            throw new Exception("the amount of the product cannot be negative");
        if(price < 0)
            throw new Exception("the price of the product cannot be negative");
        if(details == null || details == "")
            throw new Exception("the details of the product cannot be null");
        if(details.length()>300)
            throw new Exception("the details of the product is too long");
        if(!storeFounder.getUserName().equals(memberUserName) || !storeOwners.containsKey(memberUserName))
            throw new Exception("can't add new product to stock : userName "+ memberUserName +" is not an owner to the store");
        return stock.addNewProductToStock(nameProduct,category,price,details,amount);
    }

    public boolean removeProductFromStock(String memberUserName, String productName) throws Exception {
        if(!storeFounder.getUserName().equals(memberUserName) || !storeOwners.containsKey(memberUserName))
            throw new Exception("can't add new product to stock : userName "+ memberUserName +" is not an owner to the store");
        return stock.removeProductFromStock(productName);
    }

    public boolean updateProductDetails(String memberUserName, String productName, String newProductDetails) throws Exception {
        if(newProductDetails == null || newProductDetails == "")
            throw new Exception("the details of the product cannot be null");
        if(newProductDetails.length()>300)
            throw new Exception("the details of the product is too long");
        if(!storeFounder.getUserName().equals(memberUserName) || !storeOwners.containsKey(memberUserName))
            throw new Exception("can't add new product to stock : userName "+ memberUserName +" is not an owner to the store");
        return stock.updateProductDetails(productName,newProductDetails);
    }
    public StoreDTO getStoreInfo(){
        List<String> ownersNames = this.storeOwners.values().stream().map(Role::getUserName).toList();
        List<String> managersNames = this.storeManagers.values().stream().map(Role::getUserName).toList();
        return new StoreDTO(storeName, storeFounder.getUserName(), ownersNames, managersNames, stock.getProductsInfo());
    }
    public ProductDTO getProductInfo(String productName) throws Exception {
        return stock.getProductInfo(productName);
    }

    public boolean isActive() {
        return isActive;
    }

    public String getStoreName() {
        return storeName;
    }

    public Stock getStock() {
        return stock;
    }

    public StoreFounder getStoreFounder() {
        return storeFounder;
    }

    public ConcurrentHashMap<String, StoreOwner> getStoreOwners() {
        return storeOwners;
    }

    public ConcurrentHashMap<String, StoreManager> getStoreManagers() {
        return storeManagers;
    }

    public List<Deal> getStoreDeals() {
        return storeDeals;
    }

    public List<DiscountPolicy> getStoreDiscountPolicies() {
        return storeDiscountPolicies;
    }

    public List<PaymentPolicy> getStorePaymentPolicies() {
        return storePaymentPolicies;
    }
}
