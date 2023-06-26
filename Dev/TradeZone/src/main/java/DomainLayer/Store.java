package DomainLayer;

import DTO.DealDTO;
import DTO.MemberDTO;
import DTO.ProductDTO;
import DTO.StoreDTO;
import DataAccessLayer.CompositeKeys.BagConstrainsId;
import DataAccessLayer.CompositeKeys.CategoryId;
import DataAccessLayer.CompositeKeys.DiscountPolicyId;
import DataAccessLayer.CompositeKeys.ProductId;
import DataAccessLayer.Controller.DealMapper;
import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.Controller.StoreMapper;
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
    //@Transient
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "store_deals")
    @Column(name = "deal")
    private List<Deal> storeDeals;
    @Transient
    private ConcurrentHashMap<Integer,BagConstraint> createdBagConstraints;
    @Transient
    private Integer bagConstraintsIdCounter;
    @Transient
    private ConcurrentHashMap<Integer,DiscountPolicy> createdDiscountPolicies;
    @Transient
    Integer discountPoliciesIdCounter;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "storeDiscountPolicies")
    @Column(name = "DiscountPolicy")
    private Map<Integer,DiscountPolicy> storeDiscountPolicies;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "storePaymentPolicies")
    @Column(name = "BagConstraint")
    private Map<Integer,BagConstraint> storePaymentPolicies;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "newOwnersContracts")
    @Column(name = "OwnerContract")
    private Map<String, OwnerContract> newOwnersContracts;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "alreadyDoneContracts")
    @Column(name = "OwnerContract")
    private List<OwnerContract> alreadyDoneContracts;

    @Transient
    private boolean isLoaded;

    @Transient
    private boolean founderLoaded;

    @Transient
    private boolean ownersLoaded;
    @Transient
    private boolean managersLoaded;
    @Transient
    private boolean createdBagConstraintsLoaded;
    @Transient
    private boolean isActiveLoaded;
    @Transient
    private boolean createdDiscountPoliciesLoaded;

    @Transient
    private boolean storeDiscountPoliciesLoaded;
    @Transient
    private boolean storePaymentPoliciesLoaded;

    @Transient
    private boolean isContractsLaoded;

    @Transient
    private boolean storeDealsLoaded;

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
        founderLoaded = true;
        ownersLoaded = true;
        managersLoaded = true;
        isActiveLoaded = true;
        createdBagConstraintsLoaded = true;
        createdDiscountPoliciesLoaded = true;
        storeDiscountPoliciesLoaded = true;
        storePaymentPoliciesLoaded = true;
        isContractsLaoded = true;
        storeDealsLoaded = true;
    }
    public Store(String storeName,boolean isLoaded) {
        this.storeName = storeName;
        stock = new Stock(this,isLoaded);
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
        this.founderLoaded = isLoaded;
        this.ownersLoaded = isLoaded;
        this.managersLoaded = isLoaded;
        isActiveLoaded = isLoaded;
        createdBagConstraintsLoaded = isLoaded;
        createdDiscountPoliciesLoaded = isLoaded;
        storeDiscountPoliciesLoaded = isLoaded;
        storePaymentPoliciesLoaded = isLoaded;
        isContractsLaoded = isLoaded;
        storeDealsLoaded = isLoaded;
    }

    public Store(){}


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
        loadStoreFounder();
        loadStoreOwners();
        assertStringIsNotNullOrBlank(memberUserName);
        memberUserName = memberUserName.strip().toLowerCase();
        if(!storeFounder.getUserName().equals(memberUserName) && !storeOwners.containsKey(memberUserName))
            return false;

        return true;
    }

    public boolean isManager(String memberUserName) throws Exception {
        loadStoreManagers();
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
            //todo: should load manager in assertHasPermissionForStore?
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

        return stock.updateProductDescription(productName,newProductDescription);
    }

    public boolean updateProductAmount(String memberUserName, String productName, Integer newAmount) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStock);
        if(newAmount < 0)
            throw new Exception("the amount of the product cannot be negative");
        return stock.updateProductAmount(productName,newAmount);
    }

    public boolean updateProductPrice(String memberUserName, String productName, Double newPrice) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStock);
        if(newPrice <= 0)
            throw new Exception("the price of the product cannot be negative");
        return stock.updateProductPrice(productName,newPrice);
    }

    public StoreDTO getStoreInfo() throws Exception {
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
        //TODO: MOSLEM: load all store or relevant fields
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

    public boolean isAlreadyStoreOwner(String memberUserName) throws Exception {
        loadStoreFounder();
        if(storeFounder.getUserName().equals(memberUserName))
            return true;
        loadStoreOwners();
        if (storeOwners.keySet().contains(memberUserName))
            return true;

        return false;
    }

    public boolean isAlreadyStoreManager(String memberUserName) throws Exception {
        loadStoreManagers();
        return storeManagers.keySet().contains(memberUserName);
    }

    public boolean appointMemberAsStoreOwner(StoreOwner storeOwner) throws Exception {
        loadStoreOwners();
        if(storeOwners.containsKey(storeOwner.getUserName()))
            throw new Exception(""+storeOwner.getUserName()+" is already owner for this store");
        storeOwners.put(storeOwner.getUserName(), storeOwner);
        return true;
    }

    public boolean appointMemberAsStoreManager(StoreManager storeManager) throws Exception {
        loadStoreManagers();
        if(storeManagers.containsKey(storeManager.getUserName()))
            throw new Exception(""+storeManager.getUserName()+" is already manager for this store");
        storeManagers.put(storeManager.getUserName(), storeManager);
        return true;
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

        assertIsManager(managerUserName);
        isManager(ownerUserName);
        managerUserName = managerUserName.strip().toLowerCase();
        StoreManager storeManager = storeManagers.get(managerUserName);
        if(!ownerUserName.equals(managerUserName) && !isOwnerOrFounder(ownerUserName) && !isManager(ownerUserName))
            throw new Exception(ownerUserName+ " cannot get the manager permission for the store");

        return storeManager.getManagerPermissionsForStore(getStoreName());
    }

    public List<String> getAllPermissions(String ownerUserName) throws Exception {
        return StoreManager.getAllPermissions();
    }

    public boolean isActive() {
        loadIsActive();
        return isActive;
    }

    public String getStoreName() {
        return storeName;
    }

    public Stock getStock() {
        return stock;
    }

    public StoreFounder getStoreFounder() throws Exception {
        loadStoreFounder();
        return storeFounder;
    }

    public Map<String, StoreOwner> getStoreOwners() throws Exception {
        loadStoreOwners();
        return storeOwners;
    }

    public Map<String, StoreManager> getStoreManagers() throws Exception {
        loadStoreManagers();
        return storeManagers;
    }




    public boolean closeStore(String memberUserName) throws Exception {
        loadIsActive();
        if (!isActive) throw new Exception("the store " + storeName + " already closed!");
        loadStoreFounder();
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
        loadStoreFounder();
        loadStoreOwners();
        loadStoreManagers();
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
        loadStoreDeals();
        List<DealDTO> deals = new ArrayList<DealDTO>();
        for(Deal deal : this.storeDeals){
            deals.add(deal.getDealDTO());
        }
        return deals;
    }

    //Currently added for testing
    public void addDeal(Deal deal){
        loadStoreDeals();
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
        loadStorePaymentPolicies();
        for(BagConstraint bagConstraint : storePaymentPolicies.values())
            if(!bagConstraint.checkConstraint(bagContent))
                throw new Exception("Bag does not follow store: "+storeName+" payment policy: "+bagConstraint.toString());
    }




    public Double getDiscountForBag(ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent) throws Exception {
        loadStoreDiscountPolicies();
        Double totalBagDiscount = 0.0;
        for(DiscountPolicy discountPolicy : storeDiscountPolicies.values()){
            for(String productName: bagContent.keySet())
                totalBagDiscount+= discountPolicy.calculateDiscountForProduct(bagContent,productName);
        }
        return totalBagDiscount;
    }

    public Double getDiscountForProductInBag(ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent, String productName) throws Exception {
        loadStoreDiscountPolicies();
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



    public void removeManager(String userName) throws Exception {
        loadStoreManagers();
        //TODO: MOSLEM: update S manager
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
        loadCreatedDiscountPolicies();
        Product product = stock.getProduct(productName);
        ProductDiscountPolicy productDiscountPolicy = new ProductDiscountPolicy(product,discountPercentage);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, productDiscountPolicy);
        productDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.saveDiscountPolicyWithPositiveConstraint((PositiveBagConstraint) productDiscountPolicy.getBagConstraint(),productDiscountPolicy);
        this.discountPoliciesIdCounter++;
        if(addAsStoreDiscountPolicy) {
            loadStoreDiscountPolicies();
            storeDiscountPolicies.put(currentDisPolIdCounter, productDiscountPolicy);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentDisPolIdCounter;
    }

    public Integer createProductDiscountPolicyWithConstraint(String memberUserName, String productName,  int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);
        stock.assertContainsProduct(productName);

        if(discountPercentage<0 || discountPercentage>100)
            throw new Exception("discount percentage can't be less than 0 or more than 100");

        if(bagConstraintId == null)
            throw new Exception("Bag constraint id cant be null");
        loadCreatedBagConstraints();
        if(!createdBagConstraints.containsKey(bagConstraintId))
            throw new Exception(bagConstraintId+ " is not bag constraint id");
        loadCreatedDiscountPolicies();
        Product product = stock.getProduct(productName);
        BagConstraint bagConstraint = createdBagConstraints.get(bagConstraintId);
        ProductDiscountPolicy productDiscountPolicy = new ProductDiscountPolicy(product,discountPercentage, bagConstraint);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, productDiscountPolicy);
        productDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.productDiscountPolicyRepository.save(productDiscountPolicy);
        this.discountPoliciesIdCounter++;

        if(addAsStoreDiscountPolicy) {
            loadStoreDiscountPolicies();
            storeDiscountPolicies.put(currentDisPolIdCounter, productDiscountPolicy);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentDisPolIdCounter;
    }

    public Integer createCategoryDiscountPolicy(String memberUserName, String categoryName,  int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);
        stock.assertContainsCategory(categoryName);

        if(discountPercentage<0 || discountPercentage>100)
            throw new Exception("discount percentage can't be less than 0 or more than 100");
        loadCreatedDiscountPolicies();
        Category category = stock.getCategory(categoryName);
        CategoryDiscountPolicy categoryDiscountPolicy = new CategoryDiscountPolicy(category,discountPercentage);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, categoryDiscountPolicy);
        categoryDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.saveDiscountPolicyWithPositiveConstraint((PositiveBagConstraint) categoryDiscountPolicy.getBagConstraint(),categoryDiscountPolicy);
        this.discountPoliciesIdCounter++;

        if(addAsStoreDiscountPolicy) {
            loadStoreDiscountPolicies();
            storeDiscountPolicies.put(currentDisPolIdCounter, categoryDiscountPolicy);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentDisPolIdCounter;
    }

    public Integer createCategoryDiscountPolicyWithConstraint(String memberUserName, String categoryName,  int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);
        stock.assertContainsCategory(categoryName);

        if(discountPercentage<0 || discountPercentage>100)
            throw new Exception("discount percentage can't be less than 0 or more than 100");

        if(bagConstraintId == null)
            throw new Exception("Bag constraint id cant be null");
        loadCreatedBagConstraints();
        if(!createdBagConstraints.containsKey(bagConstraintId))
            throw new Exception(bagConstraintId+ " is not bag constraint id");
        loadCreatedDiscountPolicies();
        Category category = stock.getCategory(categoryName);
        BagConstraint bagConstraint = createdBagConstraints.get(bagConstraintId);
        CategoryDiscountPolicy categoryDiscountPolicy = new CategoryDiscountPolicy(category,discountPercentage, bagConstraint);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, categoryDiscountPolicy);
        categoryDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.categoryDiscountPolicyRepository.save(categoryDiscountPolicy);
        this.discountPoliciesIdCounter++;

        if(addAsStoreDiscountPolicy) {
            loadStoreDiscountPolicies();
            storeDiscountPolicies.put(currentDisPolIdCounter, categoryDiscountPolicy);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentDisPolIdCounter;
    }

    public Integer createAllStoreDiscountPolicy(String memberUserName,int discountPercentage, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);

        if(discountPercentage<0 || discountPercentage>100)
            throw new Exception("discount percentage can't be less than 0 or more than 100");
        loadCreatedDiscountPolicies();
        AllStoreDiscountPolicy allStoreDiscountPolicy = new AllStoreDiscountPolicy(discountPercentage);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, allStoreDiscountPolicy);
        allStoreDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.saveDiscountPolicyWithPositiveConstraint((PositiveBagConstraint) allStoreDiscountPolicy.getBagConstraint(),allStoreDiscountPolicy);
        this.discountPoliciesIdCounter++;

        if(addAsStoreDiscountPolicy) {
            loadStoreDiscountPolicies();
            storeDiscountPolicies.put(currentDisPolIdCounter, allStoreDiscountPolicy);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentDisPolIdCounter;
    }

    public Integer createAllStoreDiscountPolicyWithConstraint(String memberUserName,int discountPercentage, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);

        if(discountPercentage<0 || discountPercentage>100)
            throw new Exception("discount percentage can't be less than 0 or more than 100");

        if(bagConstraintId == null)
            throw new Exception("Bag constraint id cant be null");
        loadCreatedBagConstraints();
        if(!createdBagConstraints.containsKey(bagConstraintId))
            throw new Exception(bagConstraintId+ " is not bag constraint id");
        loadCreatedDiscountPolicies();
        BagConstraint bagConstraint = createdBagConstraints.get(bagConstraintId);
        AllStoreDiscountPolicy allStoreDiscountPolicy = new AllStoreDiscountPolicy(discountPercentage, bagConstraint);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, allStoreDiscountPolicy);
        allStoreDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.allStoreDiscountPolicyRepository.save(allStoreDiscountPolicy);
        this.discountPoliciesIdCounter++;

        if(addAsStoreDiscountPolicy) {
            loadStoreDiscountPolicies();
            storeDiscountPolicies.put(currentDisPolIdCounter, allStoreDiscountPolicy);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentDisPolIdCounter;
    }

    public Integer createAdditionDiscountPolicy(String memberUserName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);

        if(firstDiscountPolicyId == null || secondDiscountPolicyId==null)
            throw new Exception("Discount Policy id cant be null");
        loadCreatedDiscountPolicies();
        if(!createdDiscountPolicies.containsKey(firstDiscountPolicyId))
            throw new Exception(firstDiscountPolicyId+ " is not Discount Policy id");
        loadCreatedBagConstraints();
        if(!createdDiscountPolicies.containsKey(secondDiscountPolicyId))
            throw new Exception(secondDiscountPolicyId+ " is not Discount Policy id");

        DiscountPolicy firstDiscountPolicy = createdDiscountPolicies.get(firstDiscountPolicyId);
        DiscountPolicy secondDiscountPolicy = createdDiscountPolicies.get(secondDiscountPolicyId);
        AdditionDiscountPolicy additionDiscountPolicy = new AdditionDiscountPolicy(firstDiscountPolicy, secondDiscountPolicy);
        int currentDisPolIdCounter =this.discountPoliciesIdCounter;
        createdDiscountPolicies.put(currentDisPolIdCounter, additionDiscountPolicy);
        additionDiscountPolicy.setDiscountPolicyId(new DiscountPolicyId(currentDisPolIdCounter,storeName));
        if (Market.dbFlag)
            DALService.saveDiscountPolicyWithPositiveConstraint((PositiveBagConstraint) additionDiscountPolicy.getBagConstraint(),additionDiscountPolicy);
        this.discountPoliciesIdCounter++;
        if(addAsStoreDiscountPolicy) {
            loadStoreDiscountPolicies();
            storeDiscountPolicies.put(currentDisPolIdCounter, additionDiscountPolicy);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentDisPolIdCounter;
    }

    public Integer createAdditionDiscountPolicyWithConstraint(String memberUserName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);

        if(firstDiscountPolicyId == null || secondDiscountPolicyId==null)
            throw new Exception("Discount Policy id cant be null");
        loadCreatedDiscountPolicies();
        if(!createdDiscountPolicies.containsKey(firstDiscountPolicyId))
            throw new Exception(firstDiscountPolicyId+ " is not Discount Policy id");

        if(!createdDiscountPolicies.containsKey(secondDiscountPolicyId))
            throw new Exception(secondDiscountPolicyId+ " is not Discount Policy id");

        if(bagConstraintId == null)
            throw new Exception("Bag constraint id cant be null");
        loadCreatedBagConstraints();
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
        if(addAsStoreDiscountPolicy) {
            loadStoreDiscountPolicies();
            storeDiscountPolicies.put(currentDisPolIdCounter, additionDiscountPolicy);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentDisPolIdCounter;
    }

    public Integer createMaxValDiscountPolicy(String memberUserName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);

        if(firstDiscountPolicyId == null || secondDiscountPolicyId==null)
            throw new Exception("Discount Policy id cant be null");
        loadCreatedDiscountPolicies();
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
            DALService.saveDiscountPolicyWithPositiveConstraint((PositiveBagConstraint) maxValDiscountPolicy.getBagConstraint(),maxValDiscountPolicy);
        this.discountPoliciesIdCounter++;
        if(addAsStoreDiscountPolicy) {
            loadStoreDiscountPolicies();
            storeDiscountPolicies.put(currentDisPolIdCounter, maxValDiscountPolicy);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentDisPolIdCounter;
    }

    public Integer createMaxValDiscountPolicyWithConstraint(String memberUserName, Integer firstDiscountPolicyId, Integer secondDiscountPolicyId, Integer bagConstraintId, boolean addAsStoreDiscountPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);

        if(firstDiscountPolicyId == null || secondDiscountPolicyId==null)
            throw new Exception("Discount Policy id cant be null");
        loadCreatedDiscountPolicies();
        if(!createdDiscountPolicies.containsKey(firstDiscountPolicyId))
            throw new Exception(firstDiscountPolicyId+ " is not Discount Policy id");

        if(!createdDiscountPolicies.containsKey(secondDiscountPolicyId))
            throw new Exception(secondDiscountPolicyId+ " is not Discount Policy id");

        if(bagConstraintId == null)
            throw new Exception("Bag constraint id cant be null");
        loadCreatedBagConstraints();
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
        if(addAsStoreDiscountPolicy) {
            loadStoreDiscountPolicies();
            storeDiscountPolicies.put(currentDisPolIdCounter, maxValDiscountPolicy);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentDisPolIdCounter;
    }




    public boolean addAsStoreDiscountPolicy(String memberUserName, Integer discountPolicyId) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);
        if(discountPolicyId == null)
            throw new Exception("Discount Policy Id cant be null");
        loadStoreDiscountPolicies();
        if(!createdDiscountPolicies.containsKey(discountPolicyId))
            throw new Exception(discountPolicyId+ " is not Discount Policy Id");

        DiscountPolicy discountPolicy = createdDiscountPolicies.get(discountPolicyId);
        storeDiscountPolicies.put(discountPolicyId, discountPolicy);
        if (Market.dbFlag)
            DALService.storeRepository.save(this);
        return true;
    }

    public boolean removeFromStoreDiscountPolicies(String memberUserName, Integer discountPolicyId) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);
        if(discountPolicyId == null)
            throw new Exception("Discount Policy Id cant be null");
        loadStoreDiscountPolicies();
        if(!createdDiscountPolicies.containsKey(discountPolicyId))
            throw new Exception(discountPolicyId+ " is not Discount Policy Id");

        if(!storeDiscountPolicies.containsKey(discountPolicyId))
            throw new Exception(discountPolicyId+ " is not Store Discount Policy");

        storeDiscountPolicies.remove(discountPolicyId);
        if (Market.dbFlag)
            DALService.storeRepository.save(this);
        return true;
    }


    public List<String> getAllCreatedDiscountPolicies(String memberUserName) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);
        loadCreatedDiscountPolicies();
        List<String> allCreatedDiscountPolicies = new LinkedList<>();
        for(Integer discountPolicyId : createdDiscountPolicies.keySet().stream().toList().stream().sorted().toList())
            allCreatedDiscountPolicies.add(discountPolicyId+". "+ createdDiscountPolicies.get(discountPolicyId).toString());

        return allCreatedDiscountPolicies;
    }

    public List<String> getAllStoreDiscountPolicies(String memberUserName) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStoreDiscountPolicies);
        loadStoreDiscountPolicies();
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

        loadCreatedBagConstraints();
        Product product = stock.getProduct(productName);
        ProductBagConstraint productBagConstraint = new ProductBagConstraint(product, hour,minute);
        int currentBagConstraintsIdCounter =this.bagConstraintsIdCounter;
        createdBagConstraints.put(currentBagConstraintsIdCounter, productBagConstraint);
        productBagConstraint.setBagConstrainsId(new BagConstrainsId(currentBagConstraintsIdCounter,storeName));
        if (Market.dbFlag)
            DALService.productBagConstraintRepository.save(productBagConstraint);
        this.bagConstraintsIdCounter++;
        if(addAsStorePaymentPolicy) {
            loadStorePaymentPolicies();
            storePaymentPolicies.put(currentBagConstraintsIdCounter, productBagConstraint);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }

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

        loadCreatedBagConstraints();
        Product product = stock.getProduct(productName);
        ProductBagConstraint productBagConstraint = new ProductBagConstraint(product, fromYear,fromMonth,fromDay, toYear,toMonth,toDay);
        int currentBagConstraintsIdCounter =this.bagConstraintsIdCounter;
        createdBagConstraints.put(currentBagConstraintsIdCounter, productBagConstraint);
        productBagConstraint.setBagConstrainsId(new BagConstrainsId(currentBagConstraintsIdCounter,storeName));
        if (Market.dbFlag)
            DALService.productBagConstraintRepository.save(productBagConstraint);
        this.bagConstraintsIdCounter++;
        if(addAsStorePaymentPolicy) {
            loadStorePaymentPolicies();
            storePaymentPolicies.put(currentBagConstraintsIdCounter, productBagConstraint);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
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
        loadCreatedBagConstraints();
        Category category = stock.getCategory(categoryName);
        CategoryBagConstraint categoryBagConstraint = new CategoryBagConstraint(category, hour,minute);
        int currentBagConstraintsIdCounter =this.bagConstraintsIdCounter;
        createdBagConstraints.put(currentBagConstraintsIdCounter, categoryBagConstraint);
        categoryBagConstraint.setBagConstrainsId(new BagConstrainsId(bagConstraintsIdCounter,storeName));
        if (Market.dbFlag)
            DALService.categoryBagConstraintRepository.save(categoryBagConstraint);
        this.bagConstraintsIdCounter++;
        if(addAsStorePaymentPolicy) {
            loadStorePaymentPolicies();
            storePaymentPolicies.put(currentBagConstraintsIdCounter, categoryBagConstraint);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
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

        loadCreatedBagConstraints();
        Category category = stock.getCategory(categoryName);
        CategoryBagConstraint categoryBagConstraint = new CategoryBagConstraint(category, fromYear,fromMonth,fromDay, toYear,toMonth,toDay);
        int currentBagConstraintsIdCounter =this.bagConstraintsIdCounter;
        createdBagConstraints.put(currentBagConstraintsIdCounter, categoryBagConstraint);
        categoryBagConstraint.setBagConstrainsId(new BagConstrainsId(bagConstraintsIdCounter,storeName));
        if (Market.dbFlag)
            DALService.categoryBagConstraintRepository.save(categoryBagConstraint);
        this.bagConstraintsIdCounter++;
        if(addAsStorePaymentPolicy) {
            loadStorePaymentPolicies();
            storePaymentPolicies.put(currentBagConstraintsIdCounter, categoryBagConstraint);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentBagConstraintsIdCounter;
    }




    //AllContentBagConstraint
    public Integer createMaxProductAmountAllContentBagConstraint(String memberUserName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);

        stock.assertContainsProduct(productName);

        if(amountLimit<0)
            throw new Exception("amount limit cant be less than 0");

        loadCreatedBagConstraints();
        Product product = stock.getProduct(productName);
        AllContentBagConstraint allContentBagConstraint = new AllContentBagConstraint(product, amountLimit, "MaxProductAmount");
        int currentBagConstraintsIdCounter =this.bagConstraintsIdCounter;
        createdBagConstraints.put(currentBagConstraintsIdCounter, allContentBagConstraint);
        allContentBagConstraint.setBagConstrainsId(new BagConstrainsId(currentBagConstraintsIdCounter,storeName));
        if (Market.dbFlag)
            DALService.allContentBagConstraintRepository.save(allContentBagConstraint);
        this.bagConstraintsIdCounter++;
        if(addAsStorePaymentPolicy) {
            loadStorePaymentPolicies();
            storePaymentPolicies.put(currentBagConstraintsIdCounter, allContentBagConstraint);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentBagConstraintsIdCounter;
    }

    public Integer createMinProductAmountAllContentBagConstraint(String memberUserName, String productName, int amountLimit, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);

        stock.assertContainsProduct(productName);
        if(amountLimit<0)
            throw new Exception("amount limit cant be less than 0");
        loadCreatedBagConstraints();
        Product product = stock.getProduct(productName);
        AllContentBagConstraint allContentBagConstraint = new AllContentBagConstraint(product, amountLimit, "MinProductAmount");
        int currentBagConstraintsIdCounter =this.bagConstraintsIdCounter;
        createdBagConstraints.put(currentBagConstraintsIdCounter, allContentBagConstraint);
        allContentBagConstraint.setBagConstrainsId(new BagConstrainsId(currentBagConstraintsIdCounter,storeName));
        if (Market.dbFlag)
            DALService.allContentBagConstraintRepository.save(allContentBagConstraint);
        this.bagConstraintsIdCounter++;
        if(addAsStorePaymentPolicy) {
            loadStorePaymentPolicies();
            storePaymentPolicies.put(currentBagConstraintsIdCounter, allContentBagConstraint);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentBagConstraintsIdCounter;
    }

    public Integer createAndBagConstraint(String memberUserName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);

        if(firstBagConstraintId == null || secondBagConstraintId==null)
            throw new Exception("Bag constraints cant be null");

        loadCreatedBagConstraints();

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
        if(addAsStorePaymentPolicy) {
            loadStorePaymentPolicies();
            storePaymentPolicies.put(currentBagConstraintsIdCounter, bagConstraintAnd);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentBagConstraintsIdCounter;
    }
    public Integer createOrBagConstraint(String memberUserName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);

        if(firstBagConstraintId == null || secondBagConstraintId==null)
            throw new Exception("Bag constraints cant be null");

        loadCreatedBagConstraints();
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
        if(addAsStorePaymentPolicy) {
            loadStorePaymentPolicies();
            storePaymentPolicies.put(currentBagConstraintsIdCounter, bagConstraintOr);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentBagConstraintsIdCounter;
    }

    public Integer createOnlyIfBagConstraint(String memberUserName, Integer firstBagConstraintId, Integer secondBagConstraintId, boolean addAsStorePaymentPolicy) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);

        if(firstBagConstraintId == null || secondBagConstraintId==null)
            throw new Exception("Bag constraints cant be null");

        loadCreatedBagConstraints();
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
        if(addAsStorePaymentPolicy) {
            loadStorePaymentPolicies();
            storePaymentPolicies.put(currentBagConstraintsIdCounter, bagConstraintOnlyIf);
            if (Market.dbFlag)
                DALService.storeRepository.save(this);
        }
        return currentBagConstraintsIdCounter;
    }

    public boolean addConstraintAsPaymentPolicy(String memberUserName, Integer bagConstraintId) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);
        if(bagConstraintId == null)
            throw new Exception("Bag constraints cant be null");

        loadStorePaymentPolicies();
        if(!createdBagConstraints.containsKey(bagConstraintId))
            throw new Exception(bagConstraintId+ " is not bag constraint id");

        BagConstraint bagConstraint = createdBagConstraints.get(bagConstraintId);
        storePaymentPolicies.put(bagConstraintId, bagConstraint);
        if (Market.dbFlag)
            DALService.storeRepository.save(this);
        return true;
    }

    public boolean removeConstraintFromPaymentPolicies(String memberUserName, Integer bagConstraintId) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);
        if(bagConstraintId == null)
            throw new Exception("Bag constraints cant be null");
        loadStorePaymentPolicies();
        if(!createdBagConstraints.containsKey(bagConstraintId))
            throw new Exception(bagConstraintId+ " is not bag constraint id");

        if(!storePaymentPolicies.containsKey(bagConstraintId))
            throw new Exception(bagConstraintId+ " is not payment policy id");

        storePaymentPolicies.remove(bagConstraintId);
        if (Market.dbFlag)
            DALService.storeRepository.save(this);
        return true;
    }

    public List<String> getAllPaymentPolicies(String memberUserName) throws Exception {
        assertIsOwnerOrFounderOrAuthorizedManager(memberUserName, ManagerPermissions.manageStorePaymentPolicies);
        loadStorePaymentPolicies();
        List<String> allPaymentPolicies = new LinkedList<>();
        for(Integer policyId : storePaymentPolicies.keySet().stream().toList().stream().sorted().toList())
            allPaymentPolicies.add(policyId+". "+ storePaymentPolicies.get(policyId).toString());

        return allPaymentPolicies;
    }

    public List<String> getAllBagConstraints(String memberUserName) throws Exception {
        // moslem complete from here start
        loadCreatedBagConstraints();
        List<String> allBagConstraints = new LinkedList<>();
        for(Integer bagConstraintId : createdBagConstraints.keySet().stream().toList().stream().sorted().toList())
            allBagConstraints.add(bagConstraintId+". "+ createdBagConstraints.get(bagConstraintId).toString());

        return allBagConstraints;
    }

    public List<Integer> getCreatedBagConstIds() throws Exception {
        loadCreatedBagConstraints();
        return createdBagConstraints.keySet().stream().toList();
    }

    public List<Integer> getStoreBagConstIds() throws Exception {
        loadStorePaymentPolicies();
        return storePaymentPolicies.keySet().stream().toList();
    }


    public List<Integer> getCreatedDiscountPoliciesIds() throws Exception {
        loadCreatedDiscountPolicies();
        return createdDiscountPolicies.keySet().stream().toList();
    }

    public List<Integer> getStoreDiscountPoliciesIds() throws Exception {
        loadStoreDiscountPolicies();
        return storeDiscountPolicies.keySet().stream().toList();
    }

    /********************* end of discounts and constraints *****************************/

    public boolean hasRole(String memberUserName) throws Exception {
        //new implementation
        loadStoreFounder();
        if (storeFounder.getUserName().equals(memberUserName)) return true;
        loadStoreOwners();
        if (storeOwners.containsKey(memberUserName)) return true;
        loadStoreManagers();
        if (storeManagers.containsKey(memberUserName)) return true;
        return false;
    }

    //this function is not required in v4 so don't look at it
    public boolean systemManagerCloseStore(String managerName) throws Exception {
        loadStoreFounder();
        loadStoreOwners();
        loadStoreManagers();
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


    public void loadStore() throws Exception {
        if (isLoaded || !Market.dbFlag)
            return;
        loadIsActive();
        loadStoreFounder();
        loadStoreOwners();
        loadStoreManagers();
        loadCreatedBagConstraints();
        loadCreatedDiscountPolicies();
        loadStorePaymentPolicies();
        loadStoreDiscountPolicies();
        loadStoreDeals();
        //TODO: MOSLEM:, 2 fields contracts
        isLoaded = true;
    }
    public void loadStoreFounder() throws Exception {
        if (founderLoaded || !Market.dbFlag)
            return;
        String founderName = DALService.storeFounderRepository.findFounderNameByStoreName(storeName);
        this.storeFounder = MemberMapper.getInstance().getStoreFounder(founderName);
        founderLoaded = true;

    }

    public void loadStoreOwners() throws Exception{
        if (ownersLoaded || !Market.dbFlag)
            return;
        List<String> ownersNames = DALService.storesOwnersRepository.findOwnersNamesByStoreName(storeName);
        storeOwners = new ConcurrentHashMap<>();
        for (String ownerName : ownersNames) {
            StoreOwner storeOwner = MemberMapper.getInstance().getStoreOwner(ownerName);
            storeOwners.put(ownerName, storeOwner);
        }
        ownersLoaded = true;
    }

    public void loadStoreManagers() throws Exception{
        if (managersLoaded || !Market.dbFlag)
            return;
        storeManagers = new ConcurrentHashMap<>();
        List<String> managersNames = DALService.storesManagersRepository.findManagersNamesByStoreName(storeName);
        for (String managerName : managersNames) {
            StoreManager storeManager = MemberMapper.getInstance().getStoreManager(managerName);
            storeManagers.put(managerName, storeManager);
        }
        managersLoaded = true;
    }

    public void loadCreatedBagConstraints() throws Exception {
        if (createdBagConstraintsLoaded || !Market.dbFlag)
            return;
        List<BagConstraint> bagConstraints = DALService.bagConstraintRepository.findAllByBagConstrainsIdStoreName(storeName);
        int maxId = 0;
        for (BagConstraint bagConstraint: bagConstraints){

            if (bagConstraint instanceof ProductBagConstraint){
                Product product = ((ProductBagConstraint) bagConstraint).getProduct();
                //todo: maybe should be getProductWithoutLoading
                ((ProductBagConstraint) bagConstraint).setProduct(StoreMapper.getInstance().getProduct(new ProductId(product.getName(),storeName)));
            }

            if (bagConstraint instanceof AllContentBagConstraint){
                Product product = ((AllContentBagConstraint) bagConstraint).getProduct();
                //todo: maybe should be getProductWithoutLoading
                ((AllContentBagConstraint) bagConstraint).setProduct(StoreMapper.getInstance().getProduct(new ProductId(product.getName(),storeName)));
            }

            if (bagConstraint instanceof CategoryBagConstraint){
                Category category = ((CategoryBagConstraint) bagConstraint).getCategory();
                //todo: maybe should be getCategoryWithoutLoading
                ((CategoryBagConstraint) bagConstraint).setCategory(StoreMapper.getInstance().getCategory(new CategoryId(category.getName(),storeName)));
            }

            int id = bagConstraint.getBagConstrainsId().getId();
            if (id<0) continue;
            createdBagConstraints.put(id,bagConstraint);
            if (maxId < id){
                maxId = id;
            }
        }
        this.bagConstraintsIdCounter = maxId+1;
        createdBagConstraintsLoaded = true;
    }

    public void loadStorePaymentPolicies() throws Exception {
        if (storePaymentPoliciesLoaded || !Market.dbFlag) return;
        loadCreatedBagConstraints();
        List<Integer> storePaymentPoliciesIds = DALService.storeRepository.findPaymentPolicyIdByStoreName(storeName);
        for (Integer id: storePaymentPoliciesIds){
            storePaymentPolicies.put(id,createdBagConstraints.get(id));
        }
        isLoaded = true;
    }


    public void loadCreatedDiscountPolicies() throws Exception {
        if (createdDiscountPoliciesLoaded || !Market.dbFlag)
            return;
        loadCreatedBagConstraints();
        List<DiscountPolicy> discountPolicies = DALService.discountPolicyRepository.findAllByDiscountPolicyIdStoreName(storeName);
        int maxId = 0;
        for (DiscountPolicy discountPolicy: discountPolicies){

            if (discountPolicy instanceof ProductDiscountPolicy){
                BagConstraint bagConstraint = ((ProductDiscountPolicy) discountPolicy).getBagConstraint();
                int constraintId = bagConstraint.getBagConstrainsId().getId();
                if (constraintId>0){
                    ((ProductDiscountPolicy) discountPolicy).setBagConstraint(createdBagConstraints.get(constraintId));
                }
                Product product = ((ProductDiscountPolicy) discountPolicy).getProduct();
                ((ProductDiscountPolicy) discountPolicy).setProduct(StoreMapper.getInstance().getProduct(new ProductId(product.getName(),storeName)));
            }

            if (discountPolicy instanceof AllStoreDiscountPolicy){
                BagConstraint bagConstraint = ((AllStoreDiscountPolicy) discountPolicy).getBagConstraint();
                int constraintId = bagConstraint.getBagConstrainsId().getId();
                if (constraintId>0){
                    ((AllStoreDiscountPolicy) discountPolicy).setBagConstraint(createdBagConstraints.get(constraintId));
                }
            }

            if (discountPolicy instanceof CategoryDiscountPolicy){
                BagConstraint bagConstraint = ((CategoryDiscountPolicy) discountPolicy).getBagConstraint();
                int constraintId = bagConstraint.getBagConstrainsId().getId();
                if (constraintId>0){
                    ((CategoryDiscountPolicy) discountPolicy).setBagConstraint(createdBagConstraints.get(constraintId));
                }
                Category category = ((CategoryDiscountPolicy) discountPolicy).getCategory();
                ((CategoryDiscountPolicy) discountPolicy).setCategory(StoreMapper.getInstance().getCategory(new CategoryId(category.getName(),storeName)));
            }

            int id = discountPolicy.getDiscountPolicyId().getId();
            createdDiscountPolicies.put(id,discountPolicy);
            if (maxId < id){
                maxId = id;
            }
        }
        this.discountPoliciesIdCounter = maxId+1;
        createdDiscountPoliciesLoaded = true;
    }

    public void loadStoreDiscountPolicies() throws Exception {
        if (storeDiscountPoliciesLoaded || !Market.dbFlag)
            return;
        loadCreatedDiscountPolicies();
        List<Integer> storeDiscountPoliciesIds = DALService.storeRepository.findDiscountPolicyIdByStoreName(storeName);
        for (Integer id: storeDiscountPoliciesIds){
            storeDiscountPolicies.put(id,createdDiscountPolicies.get(id));
        }
        storeDiscountPoliciesLoaded = true;
    }

    public void loadContracts(){
        if (isContractsLaoded || !Market.dbFlag)
            return;
        List<OwnerContract> ownerContracts = DALService.ownerContractRepository.findByStore(this);
        List<Integer> newOwnersContractsIds = DALService.storeRepository.findNewOwnersContractsIdByStoreName(storeName);
        List<Integer> alreadyDoneContractsIds = DALService.storeRepository.findAlreadyContractsIdByStoreName(storeName);
        for (OwnerContract ownerContract: ownerContracts){
            if (newOwnersContractsIds.contains(ownerContract.contractId)){
                newOwnersContracts.put(ownerContract.getNewOwner().getUserName(),ownerContract);
            }
            if (alreadyDoneContractsIds.contains(ownerContract.contractId)){
                alreadyDoneContracts.add(ownerContract);
            }
        }
        isContractsLaoded = true;
    }

    public void loadStoreDeals(){
        if (storeDealsLoaded || !Market.dbFlag) return;
        List<Long> dealsIds = DALService.storeRepository.findStoreDealsIdsByStoreName(storeName);
        List<Deal> deals = new LinkedList<>();
        for (long id: dealsIds){
            deals.add(DealMapper.getInstance().getDeal(id));
        }
        this.storeDeals = deals;
        storeDealsLoaded = true;
    }



    private void loadIsActive(){
        if (!isLoaded && Market.dbFlag)
            this.isActive = DALService.storeRepository.findIsActiveById(storeName);
    }

//    public void addStoreOwner(String ownerName,StoreOwner storeOwner){
//        if(!storeOwners.containsKey(ownerName))
//            storeOwners.put(ownerName,storeOwner);
//    }
//    public void addStoreManager(String managerName,StoreManager storeManager){
//        if(!storeManagers.containsKey(managerName))
//            storeManagers.put(managerName,storeManager);
//    }

    //return 1=storeFounder, 2=storeOwner, 3=storeManager, -1= noRule
    public int getRuleForStore(String memberName) throws Exception {
        loadStoreFounder();
        loadStoreOwners();
        loadStoreManagers();
        memberName = memberName.strip().toLowerCase();
        return
                storeFounder.getUserName().equals(memberName) ? 1
                : storeOwners.containsKey(memberName) ? 2
                : storeManagers.containsKey(memberName) ? 3
                : -1;
    }

    public boolean isContractExistsForNewOwner(String newOwnerName) throws Exception {
        loadContracts();
        assertStringIsNotNullOrBlank(newOwnerName);
        newOwnerName = newOwnerName.strip().toLowerCase();
        return newOwnersContracts.containsKey(newOwnerName);
    }


    public boolean createContractForNewOwner(AbstractStoreOwner triggerOwner, Member newOwner) throws Exception {
        loadStoreFounder();
        loadStoreOwners();
        loadContracts();
        ConcurrentHashMap<String, Boolean> storeOwnersDecisions = new ConcurrentHashMap<>();
        if(!storeFounder.getUserName().equals(triggerOwner.getUserName()))
            storeOwnersDecisions.put(storeFounder.getUserName(),false);

        for (String storeOwnerName: this.storeOwners.keySet()){
            if(!storeOwnerName.equals(triggerOwner.getUserName()))
                storeOwnersDecisions.put(storeOwnerName,false);
        }

        if(storeOwnersDecisions.keySet().size() == 0 ){
            triggerOwner.appointOtherMemberAsStoreOwner(this,newOwner);
            String msg = " "+ triggerOwner.getUserName() +" appoint you as store owner for store : " + storeName;
            NotificationService.getInstance().notifyMember(newOwner.userName,msg,NotificationType.ownerDone);
            return true;
        }

        OwnerContract ownerContract = new OwnerContract(triggerOwner, newOwner,this, storeOwnersDecisions);
        newOwnersContracts.put(newOwner.getUserName(),ownerContract);

        for (String storeOwnerNameToDes: storeOwnersDecisions.keySet()){
            String msg = " " + triggerOwner.getUserName() + " want to appoint " + newOwner.getUserName() + " for store "+ storeName +" ,please confirm the appointment";
            NotificationService.getInstance().notifyMember(storeOwnerNameToDes,msg,NotificationType.fillAppointContract);
        }
        DALService.saveContract(this,ownerContract);
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
        loadContracts();
        OwnerContract ownerContract = newOwnersContracts.get(newOwnerUserName);
        System.out.println("asdnklasmdlasd");
        ownerContract.fillOwnerContract(memberUserName,decisions);
        System.out.println("asdnklasmdlasd");
        if (ownerContract.getContractIsDone()){
            newOwnersContracts.remove(newOwnerUserName);
            alreadyDoneContracts.add(ownerContract);
        }

        String msg = memberUserName + " is fill to the contract for " + newOwnerUserName;
        NotificationService.getInstance().notifyMember(ownerContract.getTriggerOwnerName(),msg,NotificationType.decisionForContract);
        DALService.saveContract(this,ownerContract);
        return true;
    }


    public List<OwnerContractDTO> getAlreadyDoneContracts(String memberUserName) throws Exception {
        assertIsOwnerOrFounder(memberUserName);
        loadContracts();
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
        loadContracts();
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
        loadContracts();
        memberUserName=memberUserName.strip().toLowerCase();
        List<OwnerContractDTO> ownerPendingContracts = new ArrayList<>();
        for(OwnerContract ownerContract: this.newOwnersContracts.values()){
            if(ownerContract.contractIsPendingOnMember(memberUserName))
                ownerPendingContracts.add(ownerContract.getOwnerContractInfo());
        }

        return ownerPendingContracts;
    }

    public void removeOwner(String userName) throws Exception {
        loadStoreOwners();
        loadContracts();
        //todo: moslem (ana moslem) update stor owners
        storeOwners.remove(userName);

        for(OwnerContract ownerContract: this.alreadyDoneContracts.stream().toList()){
            if(ownerContract.getTriggerOwnerName().equals(userName)) {
                alreadyDoneContracts.remove(ownerContract);
                DALService.deleteContract(this,ownerContract);
            }
        }

        for(String otherUserName: this.newOwnersContracts.keySet().stream().toList()){
            OwnerContract ownerContract = newOwnersContracts.get(otherUserName);
            if(ownerContract.getTriggerOwnerName().equals(userName)) {
                newOwnersContracts.remove(otherUserName);
                DALService.deleteContract(this,ownerContract);
            }
        }

        for(String otherUserName: this.newOwnersContracts.keySet().stream().toList()){
            OwnerContract ownerContract = newOwnersContracts.get(otherUserName);
            if(ownerContract.contractIsPendingOnMember(userName)){
                ownerContract.involvedOwnerIsRemoved(userName);
                newOwnersContracts.remove(otherUserName);
                alreadyDoneContracts.add(ownerContract);
                DALService.saveContract(this,ownerContract);
            }
        }
    }

    //todo: do:
    // 3- deals
    // 4- todos

}
