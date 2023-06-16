package DomainLayer;

import DTO.DealDTO;
import DTO.MemberDTO;
import DTO.ProductDTO;
import DTO.StoreDTO;
import DataAccessLayer.CompositeKeys.BagConstrainsId;
import DataAccessLayer.CompositeKeys.DiscountPolicyId;
import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.DALService;
import DTO.*;
import DomainLayer.BagConstraints.*;
import DomainLayer.DiscountPolicies.*;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Entity
public class Store {

    @Id
    private String storeName;

    @OneToOne
    @JoinColumn(name = "stockName")
    private Stock stock;
    private boolean isActive;

    @Transient
    private StoreFounder storeFounder;

//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "store_owners", joinColumns = @JoinColumn(name = "store_name"))
//    @MapKeyJoinColumns({
//            @MapKeyJoinColumn(name = "member_name", referencedColumnName = "memberName"),
//            @MapKeyJoinColumn(name = "store_name", referencedColumnName = "storeName")
//    })
//    @Column(name = "owner")
    @Transient
    private Map<String, StoreOwner> storeOwners;


//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "store_managers", joinColumns = @JoinColumn(name = "store_name"))
//    @MapKeyJoinColumns({
//            @MapKeyJoinColumn(name = "member_name", referencedColumnName = "memberName"),
//            @MapKeyJoinColumn(name = "store_name", referencedColumnName = "storeName")
//    })
//    @Column(name = "manager")
    @Transient
    private Map<String, StoreManager> storeManagers;

    //TODO:: maybe need to make it Concurrent
    @Transient
    private List<Deal> storeDeals;
    @Transient
    private ConcurrentHashMap<Integer,BagConstraint> createdBagConstraints;
    @Transient
    private Integer bagConstraintsIdCounter;
    @Transient
    private ConcurrentHashMap<Integer,DiscountPolicy> createdDiscountPolicies;
    @Transient
    Integer discountPoliciesIdCounter;
    @Transient
    private ConcurrentHashMap<Integer,DiscountPolicy> storeDiscountPolicies;
    @Transient
    private ConcurrentHashMap<Integer,BagConstraint> storePaymentPolicies;
    @Transient
    private ConcurrentHashMap<String, OwnerContract> newOwnersContracts;
    @Transient
    private List<OwnerContract> alreadyDoneContracts;

    @Transient
    private boolean isLoaded;

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

        this.newOwnersContracts = new ConcurrentHashMap<>();
        this.alreadyDoneContracts = new ArrayList<>();
        isLoaded = true;
    }
    public Store(String storeName,boolean isLoaded) {
        this.storeName = storeName;
        stock = new Stock(storeName,isLoaded);
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
        this.newOwnersContracts = new ConcurrentHashMap<>();
        this.alreadyDoneContracts = new ArrayList<>();

        this.isLoaded = isLoaded;
    }

    public Store(){}

    public void setLoaded(boolean loaded){
        isLoaded = loaded;
    }

    public void setStoreFounderAtStoreCreation(StoreFounder storeFounder) throws Exception {
        if(this.alreadyHaveFounder()){
            throw new Exception("store already have a founder");
        }
        this.storeFounder = storeFounder;
    }

    public void setStoreFounder(StoreFounder storeFounder){
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
        //todo: need load store owners and managers and founders
        loadStore();
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

    public boolean loadAppointMemberAsStoreOwner(StoreOwner storeOwner) throws Exception {
        storeOwners.put(storeOwner.getUserName(), storeOwner);
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
        if(!ownerUserName.equals(managerUserName) && !storeManager.isMyBossForStore(getStoreName(), ownerUserName))
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

    public Map<String, StoreOwner> getStoreOwners() {
        return storeOwners;
    }

    public Map<String, StoreManager> getStoreManagers() {
        return storeManagers;
    }




    public boolean closeStore(String memberUserName) throws Exception {
        if(memberUserName.equals(this.storeFounder.getUserName())){
            this.isActive = false;
            String msg = "store: " + storeName + " has been closed by " + memberUserName + " at " + java.time.LocalTime.now();
            NotificationService.getInstance().notify(storeName,msg,NotificationType.storeClosed);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
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

    public List<DealDTO> getStoreDeals(String memberUserName, boolean isSystemManager) throws Exception {
        if(!isSystemManager)
            assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.getStoreDeals);
        List<DealDTO> deals = new ArrayList<DealDTO>();
        for(Deal deal : this.storeDeals){
            deals.add(deal.getDealDTO());
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
//        Double totalBagDiscount = 0.0;
//        for(DiscountPolicy discountPolicy : storeDiscountPolicies.values()){
//            totalBagDiscount+= discountPolicy.calculateDiscount(bagContent);
//        }
//        return totalBagDiscount;

        // should calculate the same value
        Double totalBagDiscount = 0.0;
        for(DiscountPolicy discountPolicy : storeDiscountPolicies.values()){
            for(String productName: bagContent.keySet())
                totalBagDiscount+= discountPolicy.calculateDiscountForProduct(bagContent,productName);
        }
        return totalBagDiscount;
    }

    public Double getDiscountForProductInBag(ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent, String productName) throws Exception {
        assertStringIsNotNullOrBlank(productName);
        stock.assertContainsProduct(productName);
        productName=productName.strip().toLowerCase();
        Double totalBagDiscount = 0.0;
        for(DiscountPolicy discountPolicy : storeDiscountPolicies.values()){
            if(bagContent.containsKey(productName))
                totalBagDiscount+= discountPolicy.calculateDiscountForProduct(bagContent,productName);
        }
        return totalBagDiscount;
    }



    public void removeManager(String userName) {
        storeManagers.remove(userName);
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
        productDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.productDiscountPolicyRepository.save(productDiscountPolicy);
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
        productDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.productDiscountPolicyRepository.save(productDiscountPolicy);
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
        categoryDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.categoryDiscountPolicyRepository.save(categoryDiscountPolicy);
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
        categoryDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.categoryDiscountPolicyRepository.save(categoryDiscountPolicy);
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
        allStoreDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.allStoreDiscountPolicyRepository.save(allStoreDiscountPolicy);
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
        allStoreDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.allStoreDiscountPolicyRepository.save(allStoreDiscountPolicy);
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
        additionDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.additionDiscountPolicyRepository.save(additionDiscountPolicy);
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
        additionDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.additionDiscountPolicyRepository.save(additionDiscountPolicy);
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
        maxValDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.maxValDiscountPolicyRepository.save(maxValDiscountPolicy);
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
        maxValDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.maxValDiscountPolicyRepository.save(maxValDiscountPolicy);
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
        productBagConstraint.setBagConstrainsId(new BagConstrainsId(currentBagConstraintsIdCounter,storeName));
        if (Market.dbFlag)
            DALService.productBagConstraintRepository.save(productBagConstraint);
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
        productBagConstraint.setBagConstrainsId(new BagConstrainsId(currentBagConstraintsIdCounter,storeName));
        if (Market.dbFlag)
            DALService.productBagConstraintRepository.save(productBagConstraint);
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
        categoryBagConstraint.setBagConstrainsId(new BagConstrainsId(bagConstraintsIdCounter,storeName));
        if (Market.dbFlag)
            DALService.categoryBagConstraintRepository.save(categoryBagConstraint);
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
        categoryBagConstraint.setBagConstrainsId(new BagConstrainsId(bagConstraintsIdCounter,storeName));
        if (Market.dbFlag)
            DALService.categoryBagConstraintRepository.save(categoryBagConstraint);
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
        allContentBagConstraint.setBagConstrainsId(new BagConstrainsId(currentBagConstraintsIdCounter,storeName));
        if (Market.dbFlag)
            DALService.allContentBagConstraintRepository.save(allContentBagConstraint);
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
        allContentBagConstraint.setBagConstrainsId(new BagConstrainsId(currentBagConstraintsIdCounter,storeName));
        if (Market.dbFlag)
            DALService.allContentBagConstraintRepository.save(allContentBagConstraint);
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
        bagConstraintAnd.setBagConstrainsId(new BagConstrainsId(currentBagConstraintsIdCounter,storeName));
        if (Market.dbFlag)
            DALService.bagConstraintAndRepository.save(bagConstraintAnd);
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
        bagConstraintOr.setBagConstrainsId(new BagConstrainsId(currentBagConstraintsIdCounter,storeName));
        if (Market.dbFlag)
            DALService.bagConstraintOrRepository.save(bagConstraintOr);
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
        bagConstraintOnlyIf.setBagConstrainsId(new BagConstrainsId(currentBagConstraintsIdCounter,storeName));
        if (Market.dbFlag)
            DALService.bagConstraintOnlyIfRepository.save(bagConstraintOnlyIf);
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

    public void loadStore() throws Exception {
        if (!isLoaded) {
            if (Market.dbFlag) {
                Optional<Store> storeD = DALService.storeRepository.findById(storeName);
                if (storeD.isPresent()) {
                    Store store = storeD.get();
                    //String founderName
                    this.isActive = store.isActive;
                    String founderName = DALService.storeFounderRepository.findFounderNameByStoreName(storeName);
                    this.storeFounder = MemberMapper.getInstance().getStoreFounder(founderName);
                    //todo: check founder.add resposiple for stores?
                    this.storeFounder.responsibleForStores.put(storeName, this);
                    List<String> ownersNames = DALService.storesOwnersRepository.findOwnersNamesByStoreName(storeName);
                    for (String ownerName : ownersNames) {
                        StoreOwner storeOwner = MemberMapper.getInstance().getStoreOwner(ownerName);
                        storeOwners.put(ownerName, storeOwner);
                        //todo: check owner.add resposiple for stores?
                        storeOwner.responsibleForStores.put(storeName, this);
                    }
                    List<String> managersNames = DALService.storesManagersRepository.findManagersNamesByStoreName(storeName);
                    for (String managerName : managersNames) {
                        StoreManager storeManager = MemberMapper.getInstance().getStoreManager(managerName);
                        storeManagers.put(managerName, storeManager);
                        //todo: check add manager.resposipleforstores?
                        storeManager.responsibleForStores.put(storeName, this);
                    }
                }
            }
            isLoaded = true;
        }
    }

    public void addStoreOwner(String ownerName,StoreOwner storeOwner){
        if(!storeOwners.containsKey(ownerName))
            storeOwners.put(ownerName,storeOwner);
    }
    public void addStoreManager(String managerName,StoreManager storeManager){
        if(!storeManagers.containsKey(managerName))
            storeManagers.put(managerName,storeManager);
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

    public boolean isContractExistsForNewOwner(String newOwnerName) throws Exception {
        assertStringIsNotNullOrBlank(newOwnerName);
        newOwnerName = newOwnerName.strip().toLowerCase();
        return newOwnersContracts.containsKey(newOwnerName);
    }


    public boolean createContractForNewOwner(AbstractStoreOwner triggerOwner, Member newOwner) throws Exception {
        ConcurrentHashMap<String, Boolean> storeOwnersDecisions = new ConcurrentHashMap<>();
        if(!storeFounder.getUserName().equals(triggerOwner.getUserName()))
            storeOwnersDecisions.put(storeFounder.getUserName(),false);

        for (String storeOwnerName: this.storeOwners.keySet()){
            if(!storeOwnerName.equals(triggerOwner.getUserName()))
                storeOwnersDecisions.put(storeOwnerName,false);
        }

        if(storeOwnersDecisions.keySet().size() == 0 ){
            triggerOwner.appointOtherMemberAsStoreOwner(this,newOwner);
            return true;
        }

        OwnerContract ownerContract = new OwnerContract(triggerOwner, newOwner,this, storeOwnersDecisions);
        newOwnersContracts.put(newOwner.getUserName(),ownerContract);
        return true;
    }

    public boolean fillOwnerContract(String memberUserName, String newOwnerUserName, Boolean decisions) throws Exception {
        if(decisions == null)
            throw new Exception("decision can't be null");
        assertStringIsNotNullOrBlank(newOwnerUserName);
        newOwnerUserName = newOwnerUserName.strip().toLowerCase();
        assertIsOwnerOrFounder(memberUserName);
        memberUserName = memberUserName.strip().toLowerCase();

        if(isOwnerOrFounder(newOwnerUserName))
            throw new Exception("member "+ newOwnerUserName +" is already owner for store "+getStoreName());
        if(!isContractExistsForNewOwner(newOwnerUserName))
            throw new Exception("member "+ newOwnerUserName +" does not have ownership contract for store "+getStoreName());


        OwnerContract ownerContract = newOwnersContracts.get(newOwnerUserName);
        ownerContract.fillOwnerContract(memberUserName,decisions);
        if (ownerContract.getContractIsDone()){
            newOwnersContracts.remove(newOwnerUserName);
            alreadyDoneContracts.add(ownerContract);
        }
        return true;
    }


    public List<OwnerContractDTO> getAlreadyDoneContracts(String memberUserName) throws Exception {
        assertIsOwnerOrFounder(memberUserName);
        memberUserName=memberUserName.strip().toLowerCase();
        List<OwnerContractDTO> ownerContracts = new ArrayList<>();
        for(OwnerContract ownerContract: this.alreadyDoneContracts){
            if(ownerContract.getTriggerOwnerName().equals(memberUserName))
                ownerContracts.add(ownerContract.getOwnerContractInfo());
        }

        return ownerContracts;
    }

    public List<OwnerContractDTO> getMyCreatedContracts(String memberUserName) throws Exception {
        assertIsOwnerOrFounder(memberUserName);
        memberUserName=memberUserName.strip().toLowerCase();
        List<OwnerContractDTO> ownerContracts = new ArrayList<>();
        for(OwnerContract ownerContract: this.newOwnersContracts.values()){
            if(ownerContract.getTriggerOwnerName().equals(memberUserName))
                ownerContracts.add(ownerContract.getOwnerContractInfo());
        }

        return ownerContracts;
    }

    public List<OwnerContractDTO> getPendingContractsForOwner(String memberUserName) throws Exception {
        assertIsOwnerOrFounder(memberUserName);
        memberUserName=memberUserName.strip().toLowerCase();
        List<OwnerContractDTO> ownerPendingContracts = new ArrayList<>();
        for(OwnerContract ownerContract: this.newOwnersContracts.values()){
            if(ownerContract.contractIsPendingOnMember(memberUserName))
                ownerPendingContracts.add(ownerContract.getOwnerContractInfo());
        }

        return ownerPendingContracts;
    }

    public void removeOwner(String userName) throws Exception {
        storeOwners.remove(userName);

        for(OwnerContract ownerContract: this.alreadyDoneContracts.stream().toList()){
            if(ownerContract.getTriggerOwnerName().equals(userName))
                alreadyDoneContracts.remove(ownerContract);
        }

        for(String otherUserName: this.newOwnersContracts.keySet().stream().toList()){
            OwnerContract ownerContract = newOwnersContracts.get(otherUserName);
            if(ownerContract.getTriggerOwnerName().equals(userName))
                newOwnersContracts.remove(otherUserName);
        }

        for(String otherUserName: this.newOwnersContracts.keySet().stream().toList()){
            OwnerContract ownerContract = newOwnersContracts.get(otherUserName);
            if(ownerContract.contractIsPendingOnMember(userName)){
                ownerContract.involvedOwnerIsRemoved(userName);
                newOwnersContracts.remove(otherUserName);
                alreadyDoneContracts.add(ownerContract);
            }
        }
    }


}
