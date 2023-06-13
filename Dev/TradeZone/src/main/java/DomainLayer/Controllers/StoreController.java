package DomainLayer.Controllers;

import DTO.DealDTO;
import DTO.MemberDTO;
import DTO.ProductDTO;
import DTO.StoreDTO;
import DomainLayer.BagConstraints.BagConstraint;
import DomainLayer.Category;
import DomainLayer.DiscountPolicies.*;
import DomainLayer.Product;
import DomainLayer.Store;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class StoreController {
    private ConcurrentHashMap<String, Store> stores;

    public StoreController() {
        stores = new ConcurrentHashMap<>();
    }

    //TODO: isStore() method currentlu checking if storeName exists in hashmap, this does not work with lazyLoad
    //TODO: ahmed when you want to open a new store, check if the name of the store unique

    public boolean addNewProductToStock(String memberUserName, String storeName, String nameProduct,String category, Double price, String description, Integer amount) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.addNewProductToStock(memberUserName,nameProduct,category,price,description,amount);
    }

    public boolean removeProductFromStock(String memberUserName, String storeName, String productName) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.removeProductFromStock(memberUserName, productName);
    }

    public boolean updateProductDescription(String memberUserName, String storeName, String productName, String newProductDescription) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.updateProductDescription(memberUserName, productName, newProductDescription);
    }

    public boolean updateProductAmount(String memberUserName, String storeName, String productName, Integer newAmount) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.updateProductAmount(memberUserName, productName, newAmount);
    }

    public boolean updateProductPrice(String memberUserName, String storeName, String productName, Double newPrice) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.updateProductPrice(memberUserName, productName, newPrice);
    }


    public StoreDTO getStoreInfo(String storeName) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        return stores.get(storeName).getStoreInfo();
    }

    public ProductDTO getProductInfoFromStore(String storeName, String productName) throws Exception {
        storeName = storeName.strip().toLowerCase();
        isActiveStore(storeName);
        return stores.get(storeName).getProductInfo(productName);
    }

    public Integer getProductAmountInStore(String storeName, String productName) throws Exception {
        storeName = storeName.strip().toLowerCase();
        isActiveStore(storeName);
        return stores.get(storeName).getProductAmount(productName);
    }

    public List<ProductDTO> getProductInfoFromMarketByName(String productName) throws Exception {
        List<ProductDTO> productDTOList = new ArrayList<>();
        for(Store store: stores.values()) {
            String storeName = store.getStoreName();
            //isActiveStore(storeName);
            if(IsActiveStore(storeName)) {
                if (store.containsProduct(productName))
                    productDTOList.add(store.getProductInfo(productName));
            }
        }
        return productDTOList;
    }

    public List<ProductDTO> getProductInfoFromMarketByCategory(String categoryName) throws Exception {
        List<ProductDTO> productDTOList = new ArrayList<>();
        for(Store store: stores.values()) {
            String storeName = store.getStoreName();
            //isActiveStore(storeName);
            if(IsActiveStore(storeName)) {
                if (store.containsCategory(categoryName))
                    productDTOList.addAll(store.getProductsInfoByCategory(categoryName));
            }
        }
        return productDTOList;
    }

    public List<ProductDTO> getProductInfoFromMarketByKeyword(String keyword) throws Exception {
        List<ProductDTO> productDTOList = new ArrayList<>();
        for(Store store: stores.values()) {
            String storeName = store.getStoreName();
            //isActiveStore(storeName);
            if(IsActiveStore(storeName)) {
                if(store.containsKeyWord(keyword))
                    productDTOList.addAll(store.getProductInfoFromMarketByKeyword(keyword));
            }
        }
        return productDTOList;
    }

    public Store getStore(String storeName) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();

        if(!stores.containsKey(storeName))
            // TODO: read from database AND add to members hashmap
            throw new Exception("store needs to be read from database");

        return stores.get(storeName);
    }

    public void assertIsStore(String storeName) throws Exception {
        if(!isStore(storeName))
            throw new Exception("store: "+ storeName+" does not exists!");
    }
    public void assertIsNotStore(String storeName) throws Exception {
        if(isStore(storeName))
            throw new Exception("store: "+ storeName+" is already store");
    }

    public boolean isStore(String storeName) throws Exception {
        if(storeName==null || storeName.isBlank())
            throw new Exception("store name is null or empty");
        storeName = storeName.strip().toLowerCase();

        if(!stores.containsKey(storeName)) // TODO for Lazy load , maybe we need to change to set of names
            return false;

        return true;
    }

    public void isActiveStore(String storeName) throws Exception {
        storeName=storeName.strip().toLowerCase();
        assertIsStore(storeName);
        if(!stores.get(storeName).isActive())
            throw new Exception(""+ storeName+" is not Active!");
    }

    private boolean IsActiveStore(String storeName) throws Exception {
        storeName=storeName.strip().toLowerCase();
        assertIsStore(storeName);
        return stores.get(storeName).isActive();
    }

    public boolean closeStore(String memberUserName, String storeName) throws Exception {
        assertIsStore(storeName);
        storeName=storeName.strip().toLowerCase();
        return this.stores.get(storeName).closeStore(memberUserName);
    }

    public List<MemberDTO> getStoreWorkersInfo(String memberUserName, String storeName) throws Exception {
        assertIsStore(storeName);
        storeName=storeName.strip().toLowerCase();
        return this.stores.get(storeName).getStoreWorkersInfo(memberUserName);
    }

    public List<DealDTO> getStoreDeals(String memberUserName, String storeName) throws Exception {
        storeName=storeName.strip().toLowerCase();
        assertIsStore(storeName);
        return this.stores.get(storeName).getStoreDeals(memberUserName);
    }

    public List<DealDTO> getMemberDeals(String otherMemberUserName) {
        List<DealDTO> deals = new ArrayList<DealDTO>();
        for(Store store : this.stores.values()){
            deals = store.getMemberDeals(otherMemberUserName, deals);
        }
        return deals;
    }

    public Store createStore(String newStoreName) throws Exception {
        assertIsNotStore(newStoreName);

        newStoreName = newStoreName.strip().toLowerCase();
        Store newStore = new Store(newStoreName);
        stores.put(newStoreName,newStore);
        return newStore;
    }


    public List<String> getAllStoresNames(){
        return stores.keySet().stream().toList();
    }


    public boolean assertisActive(String storeName) throws Exception {
        storeName=storeName.strip().toLowerCase();
        assertIsStore(storeName);
        return stores.get(storeName).isActive();
    }







    ///////////








    //ProductBagConstraint
    public Integer createMaxTimeAtDayProductBagConstraint(String memberUserName, String storeName, String productName, int hour, int minute, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createMaxTimeAtDayProductBagConstraint(memberUserName, productName, hour,minute, addAsStorePaymentPolicy);
    }

    public Integer createRangeOfDaysProductBagConstraint(String memberUserName, String storeName, String productName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createRangeOfDaysProductBagConstraint(memberUserName, productName, fromYear,fromMonth,fromDay,toYear,toMonth,toDay, addAsStorePaymentPolicy);
    }



    //CategoryBagConstraint
    public Integer createMaxTimeAtDayCategoryBagConstraint(String memberUserName, String storeName, String categoryName, int hour, int minute, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createMaxTimeAtDayCategoryBagConstraint(memberUserName, categoryName, hour,minute, addAsStorePaymentPolicy);
    }

    public Integer createRangeOfDaysCategoryBagConstraint(String memberUserName, String storeName, String categoryName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createRangeOfDaysCategoryBagConstraint(memberUserName, categoryName, fromYear,fromMonth,fromDay,toYear,toMonth,toDay, addAsStorePaymentPolicy);
    }


    //AllContentBagConstraint
    public Integer createMaxProductAmountAllContentBagConstraint(String memberUserName, String storeName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createMaxProductAmountAllContentBagConstraint(memberUserName, productName, amountLimit, addAsStorePaymentPolicy);
    }

    public Integer createMinProductAmountAllContentBagConstraint(String memberUserName, String storeName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createMinProductAmountAllContentBagConstraint(memberUserName, productName, amountLimit, addAsStorePaymentPolicy);
    }


    // and, or, only if bag constraints
    public Integer createAndBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createAndBagConstraint(memberUserName, firstBagConstraintId, secondBagConstraintId, addAsStorePaymentPolicy);
    }
    public Integer createOrBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createOrBagConstraint(memberUserName, firstBagConstraintId, secondBagConstraintId, addAsStorePaymentPolicy);
    }

    public Integer createOnlyIfBagConstraint(String memberUserName, String storeName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createOnlyIfBagConstraint(memberUserName, firstBagConstraintId, secondBagConstraintId, addAsStorePaymentPolicy);
    }



    public boolean addConstraintAsPaymentPolicy(String memberUserName, String storeName, Integer bagConstraintId) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.addConstraintAsPaymentPolicy(memberUserName, bagConstraintId);
    }

    public boolean removeConstraintFromPaymentPolicies(String memberUserName, String storeName, Integer bagConstraintId) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);

        return store.removeConstraintFromPaymentPolicies(memberUserName, bagConstraintId);
    }

    public List<String> getAllPaymentPolicies(String memberUserName, String storeName) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);

        return store.getAllPaymentPolicies(memberUserName);
    }

    public List<String> getAllBagConstraints(String memberUserName, String storeName) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);

        return store.getAllBagConstraints(memberUserName);
    }
    //////////////////////////////aa
    public Integer createProductDiscountPolicy(String memberUserName, String storeName, String productName,  int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createProductDiscountPolicy(memberUserName, productName, discountPercentage, addAsStoreDiscountPolicy);
    }

    public Integer createProductDiscountPolicyWithConstraint(String memberUserName, String storeName, String productName,  int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createProductDiscountPolicyWithConstraint(memberUserName, productName, discountPercentage, bagConstraintId, addAsStoreDiscountPolicy);
    }

    public Integer createCategoryDiscountPolicy(String memberUserName, String storeName, String categoryName,  int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createCategoryDiscountPolicy(memberUserName, categoryName, discountPercentage, addAsStoreDiscountPolicy);
    }

    public Integer createCategoryDiscountPolicyWithConstraint(String memberUserName, String storeName, String categoryName,  int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createCategoryDiscountPolicyWithConstraint(memberUserName, categoryName, discountPercentage, bagConstraintId, addAsStoreDiscountPolicy);
    }


    public Integer createAllStoreDiscountPolicy(String memberUserName, String storeName, int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createAllStoreDiscountPolicy(memberUserName, discountPercentage, addAsStoreDiscountPolicy);
    }

    public Integer createAllStoreDiscountPolicyWithConstraint(String memberUserName, String storeName, int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createAllStoreDiscountPolicyWithConstraint(memberUserName, discountPercentage, bagConstraintId, addAsStoreDiscountPolicy);
    }

    public Integer createAdditionDiscountPolicy(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createAdditionDiscountPolicy(memberUserName, firstDiscountPolicyId, secondDiscountPolicyId, addAsStoreDiscountPolicy);
    }

    public Integer createAdditionDiscountPolicyWithConstraint(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createAdditionDiscountPolicyWithConstraint(memberUserName, firstDiscountPolicyId, secondDiscountPolicyId, bagConstraintId, addAsStoreDiscountPolicy);
    }

    public Integer createMaxValDiscountPolicy(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createMaxValDiscountPolicy(memberUserName, firstDiscountPolicyId, secondDiscountPolicyId, addAsStoreDiscountPolicy);
    }

    public Integer createMaxValDiscountPolicyWithConstraint(String memberUserName, String storeName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.createMaxValDiscountPolicyWithConstraint(memberUserName, firstDiscountPolicyId, secondDiscountPolicyId, bagConstraintId, addAsStoreDiscountPolicy);
    }



    public boolean addAsStoreDiscountPolicy(String memberUserName, String storeName, Integer discountPolicyId) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.addAsStoreDiscountPolicy(memberUserName, discountPolicyId);
    }

    public boolean removeFromStoreDiscountPolicies(String memberUserName, String storeName, Integer discountPolicyId) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.removeFromStoreDiscountPolicies(memberUserName, discountPolicyId);
    }


    public List<String> getAllCreatedDiscountPolicies(String memberUserName, String storeName) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.getAllCreatedDiscountPolicies(memberUserName);
    }

    public List<String> getAllStoreDiscountPolicies(String memberUserName, String storeName) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = stores.get(storeName);
        return store.getAllStoreDiscountPolicies(memberUserName);
    }

    //////////////////////////////aa



    private String nowTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public boolean hasRole(String memberUserName){
        for(Store store : stores.values()){
            return store.hasRole(memberUserName);
        }
        return false;
    }


    public boolean systemManagerCloseStore(String storeName) throws Exception {
        assertIsStore(storeName);
        storeName=storeName.strip().toLowerCase();
        return this.stores.get(storeName).systemManagerCloseStore(storeName);
    }

    public List<ProductDTO> filterByCategory(List<ProductDTO> productsInfo, String categoryName) throws Exception {
        List<ProductDTO> filteredProducts = new LinkedList<>();
        categoryName = categoryName.strip().toLowerCase();
        for (ProductDTO productDTO : productsInfo){
            if(productDTO.categories.contains(categoryName))
                filteredProducts.add(productDTO);
        }
        return filteredProducts;
    }

    public List<ProductDTO> filterByPrice(List<ProductDTO> productsInfo,  Integer minPrice, Integer maxPrice) throws Exception {
        if(maxPrice < minPrice){
            throw new Exception("the min price is bigger than the max price");
        }
        if(minPrice < 0)
            throw new Exception("the min price is minus");
        List<ProductDTO> filteredProducts = new LinkedList<>();
        for (ProductDTO productDTO : productsInfo){
            if(productDTO.price >= minPrice && productDTO.price <= maxPrice)
                filteredProducts.add(productDTO);
        }
        return filteredProducts;
    }

    //return 1=storeFounder, 2=storeOwner, 3=storeManager, -1= noRule
    public int getRuleForStore(String storeName, String memberName) throws Exception {
        assertIsStore(storeName);
        storeName = storeName.strip().toLowerCase();
        Store store = getStore(storeName);
        return store.getRuleForStore(memberName);
    }

    public void takeDownSystemManagerAppointment(String storeName, String appointedMember) {
        Store store = this.stores.get(storeName);
        store.removeManager(appointedMember);
    }

}
