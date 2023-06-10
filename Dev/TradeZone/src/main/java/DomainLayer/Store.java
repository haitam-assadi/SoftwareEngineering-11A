package DomainLayer;

import DTO.DealDTO;
import DTO.MemberDTO;
import DTO.ProductDTO;
import DTO.StoreDTO;
import DomainLayer.BagConstraints.*;
import DomainLayer.DiscountPolicies.*;

import java.util.*;
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
    private ConcurrentHashMap<Integer,BagConstraint> createdBagConstraints;
    Integer bagConstraintsIdCounter;

    private ConcurrentHashMap<Integer,DiscountPolicy> createdDiscountPolicies;
    Integer discountPoliciesIdCounter;
    private ConcurrentHashMap<Integer,DiscountPolicy> storeDiscountPolicies;
    private ConcurrentHashMap<Integer,BagConstraint> storePaymentPolicies;

    public Store(String storeName) {
        this.storeName = storeName;
        stock = new Stock(this);
        isActive = true;
        storeFounder = null;
        storeOwners = new ConcurrentHashMap<>();
        storeManagers = new ConcurrentHashMap<>();
        storeDeals = new ArrayList<>();

        storeDiscountPolicies = new ConcurrentHashMap<>();
        createdDiscountPolicies = new ConcurrentHashMap<>();
        discountPoliciesIdCounter=1;

        storePaymentPolicies = new ConcurrentHashMap<>();
        createdBagConstraints = new ConcurrentHashMap<>();
        bagConstraintsIdCounter =1;
    }
    public void setStoreFounderAtStoreCreation(StoreFounder storeFounder) throws Exception {
        if(this.alreadyHaveFounder()){
            throw new Exception("store already have a founder");
        }
        this.storeFounder = storeFounder;
    }

    public boolean alreadyHaveFounder(){
        return storeFounder!=null;
    }

    public boolean isOwnerOrFounder(String memberUserName) throws Exception {
        assertStringIsNotNullOrBlank(memberUserName);
        memberUserName = memberUserName.strip().toLowerCase();
        if(!storeFounder.getUserName().equals(memberUserName) && !storeOwners.containsKey(memberUserName))
            return false;

        return true;
    }

    public boolean isManager(String memberUserName) throws Exception {
        assertStringIsNotNullOrBlank(memberUserName);
        memberUserName = memberUserName.strip().toLowerCase();
        if(!storeManagers.containsKey(memberUserName))
            return false;

        return true;
    }
    public void assertIsOwnerOrFounder(String memberUserName) throws Exception {
        if(!isOwnerOrFounder(memberUserName))
            throw new Exception("userName "+ memberUserName +" is not an owner to the store");
    }


    public void assertIsOwnerOrFounderOrAuthorizedManager(String memberUserName, ManagerPermissions permission) throws Exception {
        if (memberUserName==null)
            throw new Exception("memberUserName cant be null");
        memberUserName = memberUserName.strip().toLowerCase();

        if(isOwnerOrFounder(memberUserName))
            return;

        if(isManager(memberUserName)){
            storeManagers.get(memberUserName).assertHasPermissionForStore(storeName,permission);
        }
        else{
            throw new Exception(memberUserName +" is not a founder/owner or an authorized manager");
        }
    }

    public void assertIsManager(String memberUserName) throws Exception {
        if(!isManager(memberUserName))
            throw new Exception("userName "+ memberUserName +" is not a manager to the store");
    }


    public void assertStringIsNotNullOrBlank(String st) throws Exception {
        if(st==null || st.isBlank())
            throw new Exception("string is null or empty");
    }

    public boolean addNewProductToStock(String memberUserName,String nameProduct,String category, Double price, String description, Integer amount) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStock);
        if(amount == null || amount < 0)
            throw new Exception("the amount of the product cannot be negative or null");
        if(price==null ||  price < 0)
            throw new Exception("the price of the product cannot be negative or null");
        if(description.length()>300)
            throw new Exception("the description of the product is too long");


        return stock.addNewProductToStock(nameProduct,category,price,description,amount);
    }

    public boolean removeProductFromStock(String memberUserName, String productName) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStock);
        return stock.removeProductFromStock(productName);
    }

    public boolean updateProductDescription(String memberUserName, String productName, String newProductDescription) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStock);
        if(newProductDescription == null || newProductDescription.isBlank())
            throw new Exception("the Description of the product cannot be null");
        if(newProductDescription.length()>300)
            throw new Exception("the Description of the product is too long");
        if(!storeFounder.getUserName().equals(memberUserName) && !storeOwners.containsKey(memberUserName))
            throw new Exception("can't add new product to stock : userName "+ memberUserName +" is not an owner to the store");
        return stock.updateProductDescription(productName,newProductDescription);
    }

    public boolean updateProductAmount(String memberUserName, String productName, Integer newAmount) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStock);
        if(newAmount < 0)
            throw new Exception("the amount of the product cannot be negative");
        if(!storeFounder.getUserName().equals(memberUserName) && !storeOwners.containsKey(memberUserName))
            throw new Exception("can't add new product to stock : userName "+ memberUserName +" is not an owner to the store");
        return stock.updateProductAmount(productName,newAmount);
    }

    public boolean updateProductPrice(String memberUserName, String productName, Double newPrice) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStock);
        if(newPrice <= 0)
            throw new Exception("the price of the product cannot be negative");
        if(!storeFounder.getUserName().equals(memberUserName) && !storeOwners.containsKey(memberUserName))
            throw new Exception("can't add new product to stock : userName "+ memberUserName +" is not an owner to the store");
        return stock.updateProductPrice(productName,newPrice);
    }

    public StoreDTO getStoreInfo() throws Exception {
        List<String> ownersNames = this.storeOwners.values().stream().map(Role::getUserName).toList();
        List<String> managersNames = this.storeManagers.values().stream().map(Role::getUserName).toList();
        return new StoreDTO(storeName, storeFounder.getUserName(), ownersNames, managersNames, stock.getProductsInfoAmount(),isActive);
    }
    public ProductDTO getProductInfo(String productName) throws Exception {
        List<String> productDiscountPolicies = getProductDiscountPolicies(productName);
        return stock.getProductInfo(productName, productDiscountPolicies);
    }

    public List<String> getProductDiscountPolicies(String productName) throws Exception {
        assertStringIsNotNullOrBlank(productName);
        stock.assertContainsProduct(productName);
        productName=productName.strip().toLowerCase();

        List<String> productDiscountPolicies = new ArrayList<>();
        for(Integer discountId: storeDiscountPolicies.keySet())
            if(storeDiscountPolicies.get(discountId).checkIfProductHaveDiscount(productName))
                productDiscountPolicies.add(storeDiscountPolicies.get(discountId).toString());

        return productDiscountPolicies;
    }

    public Integer getProductAmount(String productName) throws Exception {
        return stock.getProductAmount(productName);
    }

    public Product getProduct(String productName) throws Exception {
        return stock.getProduct(productName);
    }

    public Product getProductWithAmount(String productName, Integer amount) throws Exception {
        return stock.getProductWithAmount(productName, amount);
    }

    public boolean containsProduct(String productName) throws Exception {
        return stock.containsProduct(productName);
    }

    public List<ProductDTO> getProductsInfoByCategory(String categoryName) throws Exception {
        return stock.getProductsInfoByCategory(categoryName);
    }

    public boolean containsCategory(String categoryName) throws Exception {
        return stock.containsCategory(categoryName);
    }

    public List<ProductDTO> getProductInfoFromMarketByKeyword(String keyword) throws Exception {
        return stock.getProductInfoFromMarketByKeyword(keyword);
    }

    public boolean containsKeyWord(String keyword) throws Exception {
        return stock.containsKeyWord(keyword);
    }

    public boolean isAlreadyStoreOwner(String memberUserName){
        if(storeFounder.getUserName().equals(memberUserName))
            return true;

        else if (storeOwners.keySet().contains(memberUserName))
            return true;

        else return false;
    }

    public boolean isAlreadyStoreManager(String memberUserName) {
        return storeManagers.keySet().contains(memberUserName);
    }

    public boolean appointMemberAsStoreOwner(StoreOwner storeOwner) throws Exception {
        if(storeOwners.containsKey(storeOwner.getUserName()))
            throw new Exception(""+storeOwner.getUserName()+" is already owner for this store");
        storeOwners.put(storeOwner.getUserName(), storeOwner);
        return true;
    }

    public boolean appointMemberAsStoreManager(StoreManager storeManager) throws Exception {
        if(storeManagers.containsKey(storeManager.getUserName()))
            throw new Exception(""+storeManager.getUserName()+" is already manager for this store");
        storeManagers.put(storeManager.getUserName(), storeManager);
        return true;
    }


    public boolean addPermissionForStoreManager(String ownerUserName, String managerUserName, Integer permissionId) throws Exception {
        assertIsOwnerOrFounder(ownerUserName);
        assertIsManager(managerUserName);
        managerUserName = managerUserName.strip().toLowerCase();
        StoreManager storeManager = storeManagers.get(managerUserName);
        if(!storeManager.isMyBossForStore(getStoreName(), ownerUserName))
            throw new Exception(ownerUserName+ " is not a boss for "+managerUserName+" in store "+getStoreName());

        return storeManager.addPermissionForStore(getStoreName(),permissionId);
    }


    public boolean updateManagerPermissionsForStore(String ownerUserName, String managerUserName, List<Integer> newPermissions) throws Exception {
        assertIsOwnerOrFounder(ownerUserName);
        assertIsManager(managerUserName);
        managerUserName = managerUserName.strip().toLowerCase();
        StoreManager storeManager = storeManagers.get(managerUserName);
        if(!storeManager.isMyBossForStore(getStoreName(), ownerUserName))
            throw new Exception(ownerUserName+ " is not a boss for "+managerUserName+" in store "+getStoreName());

        return storeManager.updateManagerPermissionsForStore(getStoreName(), newPermissions);
    }


    public List<Integer> getManagerPermissionsForStore(String ownerUserName, String managerUserName) throws Exception {
        assertStringIsNotNullOrBlank(ownerUserName);
        assertStringIsNotNullOrBlank(managerUserName);
        ownerUserName = ownerUserName.strip().toLowerCase();
        managerUserName = managerUserName.strip().toLowerCase();

        if(!ownerUserName.equals(managerUserName))
            assertIsOwnerOrFounder(ownerUserName);

        assertIsManager(managerUserName);
        managerUserName = managerUserName.strip().toLowerCase();
        StoreManager storeManager = storeManagers.get(managerUserName);
        if(!storeManager.isMyBossForStore(getStoreName(), ownerUserName))
            throw new Exception(ownerUserName+ " is not a boss for "+managerUserName+" in store "+getStoreName());

        return storeManager.getManagerPermissionsForStore(getStoreName());
    }

    public List<String> getAllPermissions(String ownerUserName) throws Exception {
        return StoreManager.getAllPermissions();
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




    public boolean closeStore(String memberUserName) throws Exception {
        if(memberUserName.equals(this.storeFounder.getUserName())){
            this.isActive = false;
            String msg = "store: " + storeName + " has been closed by " + memberUserName + " at " + java.time.LocalTime.now();
            NotificationService.getInstance().notify(storeName,msg,NotificationType.storeClosed);
            return true;
        }
        throw new Exception(memberUserName + "is not the founder of the store");
    }

    public List<MemberDTO> getStoreWorkersInfo(String memberUserName) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.getWorkersInfo);
        List<MemberDTO> members = new ArrayList<MemberDTO>();
        members.add(this.storeFounder.getMemberDTO());
        for(StoreOwner owner : this.storeOwners.values()){
            members.add(owner.getMemberDTO());
        }
        for(StoreManager manager : this.storeManagers.values()){
            members.add(manager.getMemberDTO());
        }
        return members;
    }

    public List<DealDTO> getStoreDeals(String memberUserName) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.getStoreDeals);
        List<DealDTO> deals = new ArrayList<DealDTO>();
        for(Deal deal : this.storeDeals){
            deals.add(deal.getDealDTO());
        }
        return deals;
    }

    public List<DealDTO> getMemberDeals(String otherMemberUserName, List<DealDTO> deals) {
        for(Deal deal : this.storeDeals){
            if(deal.getDealUserName().equals(otherMemberUserName)){
                deals.add(deal.getDealDTO());
            }
        }
        return deals;
    }

    //Currently added for testing
    public void addDeal(Deal deal){
        this.storeDeals.add(deal);
    }

    public void setStock(Stock stock){
        this.stock = stock;
    }

    public void setStoreManager(String managerName, StoreManager manager) {
        this.storeManagers.put(managerName, manager);
    }

    public void setStoreOwner(String ownerName, StoreOwner owner) {
        this.storeOwners.put(ownerName, owner);
    }

    public void validateStorePolicy(ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent) throws Exception {
        for(BagConstraint bagConstraint : storePaymentPolicies.values())
            if(!bagConstraint.checkConstraint(bagContent))
                throw new Exception("Bag does not follow store: "+storeName+" payment policy: "+bagConstraint.toString());
    }




    public Double getDiscountForBag(ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent) throws Exception {
        Double totalBagDiscount = 0.0;
        for(DiscountPolicy discountPolicy : storeDiscountPolicies.values()){
            totalBagDiscount+= discountPolicy.calculateDiscount(bagContent);
        }
        return totalBagDiscount;
    }

    public void removeOwner(String userName) {
        storeOwners.remove(userName);
    }

    public boolean removeBagAmountFromStock(ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent) throws Exception {
        return stock.removeBagAmountFromStock(bagContent);
    }

    public boolean replaceBagAmountToStock(ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent) throws Exception {
        return stock.replaceBagAmountToStock(bagContent);
    }




    public Integer createProductDiscountPolicy(String memberUserName, String productName,  int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);
        stock.assertContainsProduct(productName);

        if(discountPercentage<0 || discountPercentage>100)
            throw new Exception("discount percentage can't be less than 0 or more than 100");

        Product product = stock.getProduct(productName);
        ProductDiscountPolicy productDiscountPolicy = new ProductDiscountPolicy(product,discountPercentage);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, productDiscountPolicy);
        this.discountPoliciesIdCounter++;

        if(addAsStoreDiscountPolicy)
            storeDiscountPolicies.put(currentDisPolIdCounter, productDiscountPolicy);

        return currentDisPolIdCounter;
    }

    public Integer createProductDiscountPolicyWithConstraint(String memberUserName, String productName,  int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);
        stock.assertContainsProduct(productName);

        if(discountPercentage<0 || discountPercentage>100)
            throw new Exception("discount percentage can't be less than 0 or more than 100");

        if(bagConstraintId == null)
            throw new Exception("Bag constraint id cant be null");

        if(!createdBagConstraints.containsKey(bagConstraintId))
            throw new Exception(bagConstraintId+ " is not bag constraint id");

        Product product = stock.getProduct(productName);
        BagConstraint bagConstraint = createdBagConstraints.get(bagConstraintId);
        ProductDiscountPolicy productDiscountPolicy = new ProductDiscountPolicy(product,discountPercentage, bagConstraint);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, productDiscountPolicy);
        this.discountPoliciesIdCounter++;

        if(addAsStoreDiscountPolicy)
            storeDiscountPolicies.put(currentDisPolIdCounter, productDiscountPolicy);

        return currentDisPolIdCounter;
    }

    public Integer createCategoryDiscountPolicy(String memberUserName, String categoryName,  int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);
        stock.assertContainsCategory(categoryName);

        if(discountPercentage<0 || discountPercentage>100)
            throw new Exception("discount percentage can't be less than 0 or more than 100");

        Category category = stock.getCategory(categoryName);
        CategoryDiscountPolicy categoryDiscountPolicy = new CategoryDiscountPolicy(category,discountPercentage);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, categoryDiscountPolicy);
        this.discountPoliciesIdCounter++;

        if(addAsStoreDiscountPolicy)
            storeDiscountPolicies.put(currentDisPolIdCounter, categoryDiscountPolicy);

        return currentDisPolIdCounter;
    }

    public Integer createCategoryDiscountPolicyWithConstraint(String memberUserName, String categoryName,  int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);
        stock.assertContainsCategory(categoryName);

        if(discountPercentage<0 || discountPercentage>100)
            throw new Exception("discount percentage can't be less than 0 or more than 100");

        if(bagConstraintId == null)
            throw new Exception("Bag constraint id cant be null");

        if(!createdBagConstraints.containsKey(bagConstraintId))
            throw new Exception(bagConstraintId+ " is not bag constraint id");

        Category category = stock.getCategory(categoryName);
        BagConstraint bagConstraint = createdBagConstraints.get(bagConstraintId);
        CategoryDiscountPolicy categoryDiscountPolicy = new CategoryDiscountPolicy(category,discountPercentage, bagConstraint);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, categoryDiscountPolicy);
        this.discountPoliciesIdCounter++;

        if(addAsStoreDiscountPolicy)
            storeDiscountPolicies.put(currentDisPolIdCounter, categoryDiscountPolicy);

        return currentDisPolIdCounter;
    }


    public Integer createAllStoreDiscountPolicy(String memberUserName,int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);

        if(discountPercentage<0 || discountPercentage>100)
            throw new Exception("discount percentage can't be less than 0 or more than 100");

        AllStoreDiscountPolicy allStoreDiscountPolicy = new AllStoreDiscountPolicy(discountPercentage);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, allStoreDiscountPolicy);
        this.discountPoliciesIdCounter++;

        if(addAsStoreDiscountPolicy)
            storeDiscountPolicies.put(currentDisPolIdCounter, allStoreDiscountPolicy);

        return currentDisPolIdCounter;
    }

    public Integer createAllStoreDiscountPolicyWithConstraint(String memberUserName,int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);

        if(discountPercentage<0 || discountPercentage>100)
            throw new Exception("discount percentage can't be less than 0 or more than 100");

        if(bagConstraintId == null)
            throw new Exception("Bag constraint id cant be null");

        if(!createdBagConstraints.containsKey(bagConstraintId))
            throw new Exception(bagConstraintId+ " is not bag constraint id");

        BagConstraint bagConstraint = createdBagConstraints.get(bagConstraintId);
        AllStoreDiscountPolicy allStoreDiscountPolicy = new AllStoreDiscountPolicy(discountPercentage, bagConstraint);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, allStoreDiscountPolicy);
        this.discountPoliciesIdCounter++;

        if(addAsStoreDiscountPolicy)
            storeDiscountPolicies.put(currentDisPolIdCounter, allStoreDiscountPolicy);

        return currentDisPolIdCounter;
    }

    public Integer createAdditionDiscountPolicy(String memberUserName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);

        if(firstDiscountPolicyId == null || secondDiscountPolicyId==null)
            throw new Exception("Discount Policy id cant be null");

        if(!createdDiscountPolicies.containsKey(firstDiscountPolicyId))
            throw new Exception(firstDiscountPolicyId+ " is not Discount Policy id");

        if(!createdDiscountPolicies.containsKey(secondDiscountPolicyId))
            throw new Exception(secondDiscountPolicyId+ " is not Discount Policy id");

        DiscountPolicy firstDiscountPolicy = createdDiscountPolicies.get(firstDiscountPolicyId);
        DiscountPolicy secondDiscountPolicy = createdDiscountPolicies.get(secondDiscountPolicyId);
        AdditionDiscountPolicy additionDiscountPolicy = new AdditionDiscountPolicy(firstDiscountPolicy, secondDiscountPolicy);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, additionDiscountPolicy);
        this.discountPoliciesIdCounter++;
        if(addAsStoreDiscountPolicy)
            storeDiscountPolicies.put(currentDisPolIdCounter, additionDiscountPolicy);

        return currentDisPolIdCounter;
    }

    public Integer createAdditionDiscountPolicyWithConstraint(String memberUserName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);

        if(firstDiscountPolicyId == null || secondDiscountPolicyId==null)
            throw new Exception("Discount Policy id cant be null");

        if(!createdDiscountPolicies.containsKey(firstDiscountPolicyId))
            throw new Exception(firstDiscountPolicyId+ " is not Discount Policy id");

        if(!createdDiscountPolicies.containsKey(secondDiscountPolicyId))
            throw new Exception(secondDiscountPolicyId+ " is not Discount Policy id");

        if(bagConstraintId == null)
            throw new Exception("Bag constraint id cant be null");

        if(!createdBagConstraints.containsKey(bagConstraintId))
            throw new Exception(bagConstraintId+ " is not bag constraint id");


        BagConstraint bagConstraint = createdBagConstraints.get(bagConstraintId);
        DiscountPolicy firstDiscountPolicy = createdDiscountPolicies.get(firstDiscountPolicyId);
        DiscountPolicy secondDiscountPolicy = createdDiscountPolicies.get(secondDiscountPolicyId);
        AdditionDiscountPolicy additionDiscountPolicy = new AdditionDiscountPolicy(firstDiscountPolicy, secondDiscountPolicy, bagConstraint);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, additionDiscountPolicy);
        this.discountPoliciesIdCounter++;
        if(addAsStoreDiscountPolicy)
            storeDiscountPolicies.put(currentDisPolIdCounter, additionDiscountPolicy);

        return currentDisPolIdCounter;
    }

    public Integer createMaxValDiscountPolicy(String memberUserName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);

        if(firstDiscountPolicyId == null || secondDiscountPolicyId==null)
            throw new Exception("Discount Policy id cant be null");

        if(!createdDiscountPolicies.containsKey(firstDiscountPolicyId))
            throw new Exception(firstDiscountPolicyId+ " is not Discount Policy id");

        if(!createdDiscountPolicies.containsKey(secondDiscountPolicyId))
            throw new Exception(secondDiscountPolicyId+ " is not Discount Policy id");

        DiscountPolicy firstDiscountPolicy = createdDiscountPolicies.get(firstDiscountPolicyId);
        DiscountPolicy secondDiscountPolicy = createdDiscountPolicies.get(secondDiscountPolicyId);
        MaxValDiscountPolicy maxValDiscountPolicy = new MaxValDiscountPolicy(firstDiscountPolicy, secondDiscountPolicy);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, maxValDiscountPolicy);
        this.discountPoliciesIdCounter++;
        if(addAsStoreDiscountPolicy)
            storeDiscountPolicies.put(currentDisPolIdCounter, maxValDiscountPolicy);

        return currentDisPolIdCounter;
    }

    public Integer createMaxValDiscountPolicyWithConstraint(String memberUserName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);

        if(firstDiscountPolicyId == null || secondDiscountPolicyId==null)
            throw new Exception("Discount Policy id cant be null");

        if(!createdDiscountPolicies.containsKey(firstDiscountPolicyId))
            throw new Exception(firstDiscountPolicyId+ " is not Discount Policy id");

        if(!createdDiscountPolicies.containsKey(secondDiscountPolicyId))
            throw new Exception(secondDiscountPolicyId+ " is not Discount Policy id");

        if(bagConstraintId == null)
            throw new Exception("Bag constraint id cant be null");

        if(!createdBagConstraints.containsKey(bagConstraintId))
            throw new Exception(bagConstraintId+ " is not bag constraint id");


        BagConstraint bagConstraint = createdBagConstraints.get(bagConstraintId);
        DiscountPolicy firstDiscountPolicy = createdDiscountPolicies.get(firstDiscountPolicyId);
        DiscountPolicy secondDiscountPolicy = createdDiscountPolicies.get(secondDiscountPolicyId);
        MaxValDiscountPolicy maxValDiscountPolicy = new MaxValDiscountPolicy(firstDiscountPolicy, secondDiscountPolicy, bagConstraint);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, maxValDiscountPolicy);
        this.discountPoliciesIdCounter++;
        if(addAsStoreDiscountPolicy)
            storeDiscountPolicies.put(currentDisPolIdCounter, maxValDiscountPolicy);

        return currentDisPolIdCounter;
    }




    public boolean addAsStoreDiscountPolicy(String memberUserName, Integer discountPolicyId) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);
        if(discountPolicyId == null)
            throw new Exception("Discount Policy Id cant be null");

        if(!createdDiscountPolicies.containsKey(discountPolicyId))
            throw new Exception(discountPolicyId+ " is not Discount Policy Id");

        DiscountPolicy discountPolicy = createdDiscountPolicies.get(discountPolicyId);
        storeDiscountPolicies.put(discountPolicyId, discountPolicy);

        return true;
    }

    public boolean removeFromStoreDiscountPolicies(String memberUserName, Integer discountPolicyId) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);
        if(discountPolicyId == null)
            throw new Exception("Discount Policy Id cant be null");

        if(!createdDiscountPolicies.containsKey(discountPolicyId))
            throw new Exception(discountPolicyId+ " is not Discount Policy Id");

        if(!storeDiscountPolicies.containsKey(discountPolicyId))
            throw new Exception(discountPolicyId+ " is not Store Discount Policy");

        storeDiscountPolicies.remove(discountPolicyId);

        return true;
    }


    public List<String> getAllCreatedDiscountPolicies(String memberUserName) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);
        List<String> allCreatedDiscountPolicies = new LinkedList<>();
        for(Integer discountPolicyId : createdDiscountPolicies.keySet().stream().toList().stream().sorted().toList())
            allCreatedDiscountPolicies.add(discountPolicyId+". "+ createdDiscountPolicies.get(discountPolicyId).toString());

        return allCreatedDiscountPolicies;
    }

    public List<String> getAllStoreDiscountPolicies(String memberUserName) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);
        List<String> allStoreDiscountPolicies = new LinkedList<>();
        for(Integer discountPolicyId : storeDiscountPolicies.keySet().stream().toList().stream().sorted().toList())
            allStoreDiscountPolicies.add(discountPolicyId+". "+ storeDiscountPolicies.get(discountPolicyId).toString());

        return allStoreDiscountPolicies;
    }




///////////////////////////////////////////////////////////////////////////////////////////////////
    //ProductBagConstraint
    public Integer createMaxTimeAtDayProductBagConstraint(String memberUserName, String productName, int hour, int minute, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);

        stock.assertContainsProduct(productName);
        if(hour<0 || hour>23)
            throw new Exception("hour can't be less than 0 or more than 23");

        if(minute<0 || minute>59)
            throw new Exception("minute can't be less than 0 or more than 59");

        Product product = stock.getProduct(productName);
        ProductBagConstraint productBagConstraint = new ProductBagConstraint(product, hour,minute);
        int currentBagConstraintsIdCounter =this.bagConstraintsIdCounter;
        createdBagConstraints.put(currentBagConstraintsIdCounter, productBagConstraint);
        this.bagConstraintsIdCounter++;
        if(addAsStorePaymentPolicy)
            storePaymentPolicies.put(currentBagConstraintsIdCounter, productBagConstraint);

        return currentBagConstraintsIdCounter;
    }

    public Integer createRangeOfDaysProductBagConstraint(String memberUserName, String productName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);

        stock.assertContainsProduct(productName);

        if(fromYear <0 ||toYear<0)
            throw new Exception("year can't be less than 0");

        if(fromMonth<1 || fromMonth>12 ||  toMonth<1 || toMonth>12)
            throw new Exception("month can't be less than 1 or more than 12");

        if(fromDay<1 || fromDay>31 ||  toDay<1 || toDay>31)
            throw new Exception("day can't be less than 1 or more than 31");

        Product product = stock.getProduct(productName);
        ProductBagConstraint productBagConstraint = new ProductBagConstraint(product, fromYear,fromMonth,fromDay, toYear,toMonth,toDay);
        int currentBagConstraintsIdCounter =this.bagConstraintsIdCounter;
        createdBagConstraints.put(currentBagConstraintsIdCounter, productBagConstraint);
        this.bagConstraintsIdCounter++;
        if(addAsStorePaymentPolicy)
            storePaymentPolicies.put(currentBagConstraintsIdCounter, productBagConstraint);

        return currentBagConstraintsIdCounter;
    }



    //CategoryBagConstraint
    public Integer createMaxTimeAtDayCategoryBagConstraint(String memberUserName, String categoryName, int hour, int minute, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);

        stock.assertContainsCategory(categoryName);

        if(hour<0 || hour>23)
            throw new Exception("hour can't be less than 0 or more than 23");

        if(minute<0 || minute>59)
            throw new Exception("minute can't be less than 0 or more than 59");

        Category category = stock.getCategory(categoryName);
        CategoryBagConstraint categoryBagConstraint = new CategoryBagConstraint(category, hour,minute);
        int currentBagConstraintsIdCounter =this.bagConstraintsIdCounter;
        createdBagConstraints.put(currentBagConstraintsIdCounter, categoryBagConstraint);
        this.bagConstraintsIdCounter++;
        if(addAsStorePaymentPolicy)
            storePaymentPolicies.put(currentBagConstraintsIdCounter, categoryBagConstraint);
        return currentBagConstraintsIdCounter;
    }

    public Integer createRangeOfDaysCategoryBagConstraint(String memberUserName, String categoryName, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);

        stock.assertContainsCategory(categoryName);

        if(fromYear <0 ||toYear<0)
            throw new Exception("year can't be less than 0");

        if(fromMonth<1 || fromMonth>12 ||  toMonth<1 || toMonth>12)
            throw new Exception("month can't be less than 1 or more than 12");

        if(fromDay<1 || fromDay>31 ||  toDay<1 || toDay>31)
            throw new Exception("day can't be less than 1 or more than 31");



        Category category = stock.getCategory(categoryName);
        CategoryBagConstraint categoryBagConstraint = new CategoryBagConstraint(category, fromYear,fromMonth,fromDay, toYear,toMonth,toDay);
        int currentBagConstraintsIdCounter =this.bagConstraintsIdCounter;
        createdBagConstraints.put(currentBagConstraintsIdCounter, categoryBagConstraint);
        this.bagConstraintsIdCounter++;
        if(addAsStorePaymentPolicy)
            storePaymentPolicies.put(currentBagConstraintsIdCounter, categoryBagConstraint);

        return currentBagConstraintsIdCounter;
    }




    //AllContentBagConstraint
    public Integer createMaxProductAmountAllContentBagConstraint(String memberUserName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);

        stock.assertContainsProduct(productName);

        if(amountLimit<0)
            throw new Exception("amount limit cant be less than 0");


        Product product = stock.getProduct(productName);
        AllContentBagConstraint allContentBagConstraint = new AllContentBagConstraint(product, amountLimit, "MaxProductAmount");
        int currentBagConstraintsIdCounter =this.bagConstraintsIdCounter;
        createdBagConstraints.put(currentBagConstraintsIdCounter, allContentBagConstraint);
        this.bagConstraintsIdCounter++;
        if(addAsStorePaymentPolicy)
            storePaymentPolicies.put(currentBagConstraintsIdCounter, allContentBagConstraint);

        return currentBagConstraintsIdCounter;
    }

    public Integer createMinProductAmountAllContentBagConstraint(String memberUserName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);

        stock.assertContainsProduct(productName);
        if(amountLimit<0)
            throw new Exception("amount limit cant be less than 0");

        Product product = stock.getProduct(productName);
        AllContentBagConstraint allContentBagConstraint = new AllContentBagConstraint(product, amountLimit, "MinProductAmount");
        int currentBagConstraintsIdCounter =this.bagConstraintsIdCounter;
        createdBagConstraints.put(currentBagConstraintsIdCounter, allContentBagConstraint);
        this.bagConstraintsIdCounter++;
        if(addAsStorePaymentPolicy)
            storePaymentPolicies.put(currentBagConstraintsIdCounter, allContentBagConstraint);

        return currentBagConstraintsIdCounter;
    }

    public Integer createAndBagConstraint(String memberUserName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);

        if(firstBagConstraintId == null || secondBagConstraintId==null)
            throw new Exception("Bag constraints cant be null");

        if(!createdBagConstraints.containsKey(firstBagConstraintId))
            throw new Exception(firstBagConstraintId+ " is not bag constraint id");

        if(!createdBagConstraints.containsKey(secondBagConstraintId))
            throw new Exception(secondBagConstraintId+ " is not bag constraint id");

        BagConstraint firstBagConstraint = createdBagConstraints.get(firstBagConstraintId);
        BagConstraint secondBagConstraint = createdBagConstraints.get(secondBagConstraintId);
        BagConstraintAnd bagConstraintAnd = new BagConstraintAnd(firstBagConstraint, secondBagConstraint);
        int currentBagConstraintsIdCounter =this.bagConstraintsIdCounter;
        createdBagConstraints.put(currentBagConstraintsIdCounter, bagConstraintAnd);
        this.bagConstraintsIdCounter++;
        if(addAsStorePaymentPolicy)
            storePaymentPolicies.put(currentBagConstraintsIdCounter, bagConstraintAnd);

        return currentBagConstraintsIdCounter;
    }
    public Integer createOrBagConstraint(String memberUserName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);

        if(firstBagConstraintId == null || secondBagConstraintId==null)
            throw new Exception("Bag constraints cant be null");

        if(!createdBagConstraints.containsKey(firstBagConstraintId))
            throw new Exception(firstBagConstraintId+ " is not bag constraint id");

        if(!createdBagConstraints.containsKey(secondBagConstraintId))
            throw new Exception(secondBagConstraintId+ " is not bag constraint id");

        BagConstraint firstBagConstraint = createdBagConstraints.get(firstBagConstraintId);
        BagConstraint secondBagConstraint = createdBagConstraints.get(secondBagConstraintId);
        BagConstraintOr bagConstraintOr = new BagConstraintOr(firstBagConstraint, secondBagConstraint);
        int currentBagConstraintsIdCounter =this.bagConstraintsIdCounter;
        createdBagConstraints.put(currentBagConstraintsIdCounter, bagConstraintOr);
        this.bagConstraintsIdCounter++;
        if(addAsStorePaymentPolicy)
            storePaymentPolicies.put(currentBagConstraintsIdCounter, bagConstraintOr);

        return currentBagConstraintsIdCounter;
    }

    public Integer createOnlyIfBagConstraint(String memberUserName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);

        if(firstBagConstraintId == null || secondBagConstraintId==null)
            throw new Exception("Bag constraints cant be null");

        if(!createdBagConstraints.containsKey(firstBagConstraintId))
            throw new Exception(firstBagConstraintId+ " is not bag constraint id");

        if(!createdBagConstraints.containsKey(secondBagConstraintId))
            throw new Exception(secondBagConstraintId+ " is not bag constraint id");

        BagConstraint firstBagConstraint = createdBagConstraints.get(firstBagConstraintId);
        BagConstraint secondBagConstraint = createdBagConstraints.get(secondBagConstraintId);
        BagConstraintOnlyIf bagConstraintOnlyIf = new BagConstraintOnlyIf(firstBagConstraint, secondBagConstraint);
        int currentBagConstraintsIdCounter =this.bagConstraintsIdCounter;
        createdBagConstraints.put(currentBagConstraintsIdCounter, bagConstraintOnlyIf);
        this.bagConstraintsIdCounter++;
        if(addAsStorePaymentPolicy)
            storePaymentPolicies.put(currentBagConstraintsIdCounter, bagConstraintOnlyIf);

        return currentBagConstraintsIdCounter;
    }

    public boolean addConstraintAsPaymentPolicy(String memberUserName, Integer bagConstraintId) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);
        if(bagConstraintId == null)
            throw new Exception("Bag constraints cant be null");

        if(!createdBagConstraints.containsKey(bagConstraintId))
            throw new Exception(bagConstraintId+ " is not bag constraint id");

        BagConstraint bagConstraint = createdBagConstraints.get(bagConstraintId);
        storePaymentPolicies.put(bagConstraintId, bagConstraint);

        return true;
    }

    public boolean removeConstraintFromPaymentPolicies(String memberUserName, Integer bagConstraintId) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);
        if(bagConstraintId == null)
            throw new Exception("Bag constraints cant be null");

        if(!createdBagConstraints.containsKey(bagConstraintId))
            throw new Exception(bagConstraintId+ " is not bag constraint id");

        if(!storePaymentPolicies.containsKey(bagConstraintId))
            throw new Exception(bagConstraintId+ " is not payment policy id");

        storePaymentPolicies.remove(bagConstraintId);

        return true;
    }

    public List<String> getAllPaymentPolicies(String memberUserName) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);
        List<String> allPaymentPolicies = new LinkedList<>();
        for(Integer policyId : storePaymentPolicies.keySet().stream().toList().stream().sorted().toList())
            allPaymentPolicies.add(policyId+". "+ storePaymentPolicies.get(policyId).toString());

        return allPaymentPolicies;
    }

    public List<String> getAllBagConstraints(String memberUserName) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);
        List<String> allBagConstraints = new LinkedList<>();
        for(Integer bagConstraintId : createdBagConstraints.keySet().stream().toList().stream().sorted().toList())
            allBagConstraints.add(bagConstraintId+". "+ createdBagConstraints.get(bagConstraintId).toString());

        return allBagConstraints;
    }

    public boolean hasRole(String memberUserName){
        return storeFounder.getUserName().equals(memberUserName) || storeOwners.containsKey(memberUserName) || storeManagers.containsKey(memberUserName);
    }

    public boolean systemManagerCloseStore(String managerName) {
        storeFounder.removeStore(storeName);
        for (StoreOwner storeOwner: storeOwners.values()){
            storeOwner.removeStore(storeName);
        }
        for(StoreManager storeManager: storeManagers.values()){
            storeManager.removeStore(storeName);
        }
        isActive = false;
        storeFounder = null;
        storeOwners = new ConcurrentHashMap<>();
        storeManagers = new ConcurrentHashMap<>();

        String msg = "store: " + storeName + " has been closed for ever by the system manager" + managerName + " at " + java.time.LocalTime.now();
        NotificationService.getInstance().notify(storeName,msg,NotificationType.storeClosedBySystemManager);
        NotificationService.getInstance().removeAllRulers(storeName);
        return true;
    }

    public List<Integer> getCreatedBagConstIds(){
        return createdBagConstraints.keySet().stream().toList();
    }

    public List<Integer> getStoreBagConstIds(){
        return storePaymentPolicies.keySet().stream().toList();
    }


    public List<Integer> getCreatedDiscountPoliciesIds(){
        return createdDiscountPolicies.keySet().stream().toList();
    }

    public List<Integer> getStoreDiscountPoliciesIds(){
        return storeDiscountPolicies.keySet().stream().toList();
    }



    //return 1=storeFounder, 2=storeOwner, 3=storeManager, -1= noRule
    public int getRuleForStore(String memberName) throws Exception {
        memberName = memberName.strip().toLowerCase();
        return
                storeFounder.getUserName().equals(memberName) ? 1
                : storeOwners.containsKey(memberName) ? 2
                : storeManagers.containsKey(memberName) ? 3
                : -1;
    }



}
