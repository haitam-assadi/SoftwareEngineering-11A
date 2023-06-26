package DataAccessLayer;//package DataAccessLayer;

import DataAccessLayer.CompositeKeys.BagConstrainsId;
import DataAccessLayer.CompositeKeys.RolesId;
import DataAccessLayer.Repositories.*;
import DataAccessLayer.Repositories.BagConstraints.*;
import DataAccessLayer.Repositories.DiscountPolicies.*;
import DomainLayer.*;
import DomainLayer.BagConstraints.AllContentBagConstraint;
import DomainLayer.BagConstraints.BagConstraint;
import DomainLayer.BagConstraints.PositiveBagConstraint;
import DomainLayer.DiscountPolicies.ProductDiscountPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class DALService {

    @Autowired
    public static MemberRepository memberRepository;

    @Autowired
    public static StoreRepository storeRepository;

    @Autowired
    public static StockRepository stockRepository;

    @Autowired
    public static ProductRepository productRepository;

    @Autowired
    public static CategoryRepository categoryRepository;
    @Autowired
    public static StoreManagerRepository storesManagersRepository;
    @Autowired
    public static StoreOwnerRepository storesOwnersRepository;

    @Autowired
    public static StoreFounderRepository storeFounderRepository;

    @Autowired
    public static BagRepository bagRepository;

    @Autowired
    public static CartRepository cartRepository;

    @Autowired
    public static SystemManagerRepository systemManagerRepository;
    @Autowired
    public static BagConstraintRepository bagConstraintRepository;
    @Autowired
    public static AllContentBagConstraintRepository allContentBagConstraintRepository;
    @Autowired
    public static BagConstraintAndRepository bagConstraintAndRepository;
    @Autowired
    public static BagConstraintOnlyIfRepository bagConstraintOnlyIfRepository;
    @Autowired
    public static BagConstraintOrRepository bagConstraintOrRepository;
    @Autowired
    public static CategoryBagConstraintRepository categoryBagConstraintRepository;
    @Autowired
    public static PositiveBagConstraintRepository positiveBagConstraintRepository;
    @Autowired
    public static ProductBagConstraintRepository productBagConstraintRepository;
    @Autowired
    public static DiscountPolicyRepository discountPolicyRepository;
    @Autowired
    public static AdditionDiscountPolicyRepository additionDiscountPolicyRepository;
    @Autowired
    public static AllStoreDiscountPolicyRepository allStoreDiscountPolicyRepository;
    @Autowired
    public static CategoryDiscountPolicyRepository categoryDiscountPolicyRepository;
    @Autowired
    public static MaxValDiscountPolicyRepository maxValDiscountPolicyRepository;
    @Autowired
    public static ProductDiscountPolicyRepository productDiscountPolicyRepository;
    @Autowired
    public static DealRepository dealRepository;

    @Autowired
    public static OwnerContractRepository ownerContractRepository;

    @Autowired
    public static StoreRulesRepository storeRulesRepository;
    @Autowired
    public static RolerNotificatorRepository rolerNotificatorRepository;

    @Autowired
    public static MemberNotificatorRepository memberNotificatorRepository;
    public DALService(){
        super();
    }

    @Transactional
    public static void saveMember(Member member, Cart cart){
        cartRepository.save(cart);
        memberRepository.save(member);
    }


    @Transactional
    public static void saveStore(Store store, StoreFounder storeFounder,Member member){
        Stock stock = store.getStock();
        stockRepository.save(stock);
        RolesId founder = new RolesId(storeFounder.getUserName(), store.getStoreName());
        storeFounder.setId(founder);
        storeFounder.setBoss(member,storeFounder.getRoleType());
        storeFounderRepository.save(storeFounder);
        storeRepository.save(store);
        memberRepository.save(member);
    }

    @Transactional
    public static void saveProduct(Stock stock,Category category,Product product){
        productRepository.save(product);
        categoryRepository.save(category);
        stockRepository.save(stock);
    }

    @Transactional
    public static void saveCategory(Stock stock,Category category){
        categoryRepository.save(category);
        stockRepository.save(stock);
    }

    @Transactional
    public static void removeProductCategory(Product product, Category category) {
        productRepository.save(product);
        categoryRepository.save(category);
    }

    @Transactional
    public static void removeProduct(Product product, Stock stock) {
        stockRepository.save(stock);
        productRepository.delete(product);
    }

    @Transactional
    public static void saveStoreOwner(StoreOwner storeOwnerRole, Store store,Member member) {
        storesOwnersRepository.save(storeOwnerRole);
        storeRepository.save(store);
        memberRepository.save(member);
    }
    @Transactional
    public static void saveStoreManager(StoreManager storeManager, Store store,Member member) {
        storesManagersRepository.save(storeManager);
        storeRepository.save(store);
        memberRepository.save(member);
    }

    @Transactional
    public static void removeStoreOwner(Store store, StoreOwner otherOwner) {
        DALService.storeRepository.save(store);
        DALService.storesOwnersRepository.delete(otherOwner);
    }

    @Transactional
    public static void modifyBag(Cart cart, Bag bag) {
        DALService.bagRepository.save(bag);
        DALService.cartRepository.save(cart);
    }
    @Transactional
    public static void RemoveBag(Cart cart, Bag bag) {
        DALService.cartRepository.save(cart);
        DALService.bagRepository.delete(bag);
    }

    @Transactional
    public static void addProduct(Bag bag, Product product) {
        productRepository.save(product);
        bagRepository.save(bag);
    }

    @Transactional
    public static void saveDeal(Deal deal, Member member, Store store) {
        dealRepository.save(deal);
        storeRepository.save(store);
        memberRepository.save(member);
    }
    @Transactional
    public static void saveDealForStore(Deal deal, Store store) {
        dealRepository.save(deal);
        storeRepository.save(store);
    }

    @Transactional
    public static void saveContract(Store store,OwnerContract ownerContract){
        if (!Market.dbFlag) return;
        ownerContractRepository.save(ownerContract);
        storeRepository.save(store);
    }

    @Transactional
    public static void deleteContract(Store store,OwnerContract ownerContract){
        if (!Market.dbFlag) return;
        storeRepository.save(store);
        ownerContractRepository.delete(ownerContract);
    }

    @Transactional
    public static void saveDiscountPolicyWithPositiveConstraint(PositiveBagConstraint positiveBagConstraint, ProductDiscountPolicy productDiscountPolicy) {
        positiveBagConstraint.setBagConstrainsId(new BagConstrainsId(-1,""));
        DALService.bagConstraintRepository.save(positiveBagConstraint);
        DALService.productDiscountPolicyRepository.save(productDiscountPolicy);
    }


//
//    @Transactional
//    public static DTOStore updateActive(String storeName){
//        DTOStore dtoStore = DALService.storeRepository.getById(storeName);
//        dtoStore.setActive(false);
//        return DALService.storeRepository.save(dtoStore);
//    }
//
//    @Transactional
//    public static DTOProduct addProduct(Category category,
//                                        String stockName,
//                                        String storeName,
//                                        Product product,
//                                        int amount,
//                                        boolean categoryflag){
//        DTOStore dtoStore = DALService.storeRepository.getById(storeName);
//        DTOStock dtoStock = DALService.stockRepository.getById(stockName);
//        if(categoryflag) {
//            DTOCategory dtoCategory = DALService.categoryRepository.save(category.getCategoryDTO(dtoStore, dtoStock));
//            return DALService.productRepository.save(product.getDTOproduct(dtoStock, dtoCategory, amount));
//        }
//        else return DALService.productRepository.save(product.getDTOproduct(dtoStock, category.getCategoryDTO(dtoStore,dtoStock), amount));
//    }
//
//    @Transactional
//    public static DTOCategory addCategory(Category category,
//                                        String stockName,
//                                        String storeName
//                                        ){
//        DTOStore dtoStore = DALService.storeRepository.getById(storeName);
//        DTOStock dtoStock = DALService.stockRepository.getById(stockName);
//        DTOCategory dtoCategory = DALService.categoryRepository.save(category.getCategoryDTO(dtoStore,dtoStock));
//        //return DALService.productRepository.save(product.getDTOproduct(dtoStore,dtoStock,dtoCategory,amount));
//        return dtoCategory;
//    }
//
//
//    @Transactional
//    public static void removeProduct(String storeName,String productName) {
//        ProductId productId = new ProductId(productName,storeName);
//        DTOProduct dtoProduct = productRepository.getById(productId);
//        productRepository.delete(dtoProduct);
//    }
//
//    @Transactional
//    public static void updateProductAmount(String storeName,String productName,int amount) {
//        ProductId productId = new ProductId(productName,storeName);
//        DTOProduct dtoProduct = productRepository.getById(productId);
//        dtoProduct.setAmount(amount);
//        productRepository.save(dtoProduct);
//    }
//
//    @Transactional
//    public static void updateProductPrice(String storeName,String productName,double price) {
//        ProductId productId = new ProductId(productName,storeName);
//        DTOProduct dtoProduct = productRepository.getById(productId);
//        dtoProduct.setPrice(price);
//        productRepository.save(dtoProduct);
//    }
//
//    @Transactional
//    public static void updateProductDescription(String storeName,String productName,String desc) {
//        ProductId productId = new ProductId(productName,storeName);
//        DTOProduct dtoProduct = productRepository.getById(productId);
//        dtoProduct.setDescription(desc);
//        productRepository.save(dtoProduct);
//    }
//
//    @Transactional
//    public static Store DTOStore2Store(String storeName) throws Exception {
//        DTOStore dtoStore = DALService.storeRepository.getById(storeName);
//        List<DTOCategory> categories = categoryRepository.findByStoreName(storeName);
//        ConcurrentHashMap<String,Category> stockCategories = new ConcurrentHashMap<>();
//        ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> products = new ConcurrentHashMap<>();
//        for (DTOCategory dtoCategory: categories){
//            List<DTOProduct> category_products = productRepository.findByCategory(dtoCategory);
//            for (DTOProduct dtoProduct: category_products){
//                Category category = new Category(storeName,dtoCategory.getCategoryName());
//                Product product = new Product(dtoProduct.getProductName(),storeName,category,dtoProduct.getPrice(), dtoProduct.getDescription());
//                category.putProductInCategory(product);
//                stockCategories.put(category.getCategoryName(),category);
//                ConcurrentHashMap<Product,Integer> productAmount = new ConcurrentHashMap<>();
//                productAmount.put(product,dtoProduct.getAmount());
//                products.put(dtoProduct.getProductName(),productAmount);
//            }
//        }
//        Stock stock = new Stock(dtoStore.getDtoStock().getStockName(),products,stockCategories);
////        DTOMember dtoMember = storeRepository.findMemberByStoreName(storeName);
////        Member member = dtoMember.loadMember();
//        Store store = new Store(storeName,stock, dtoStore.isActive()/*,new StoreFounder(member)*/);
//        System.out.println("as;ldska;d");
//        return store;
//    }
//
//    @Transactional
//    public static void saveCart(Member member, String storeName, String productName, Integer amount) {
//        DTOCart dtoCart = new DTOCart(member.getDTOMember());
//        DALService.cartRepository.save(dtoCart);
//        DTOStore dtoStore = storeRepository.getById(storeName);
//        DTOBag dtoBag = bagRepository.save(new DTOBag(dtoCart,dtoStore));
//        DTOProduct dtoProduct = productRepository.getById(new ProductId(productName,storeName));
//        DTOProductBag dtoProductBag = new DTOProductBag(new ProductbagId(productName,storeName,dtoBag.getBagId()),dtoProduct,dtoBag,amount);
//        productBagRepository.save(dtoProductBag);
//    }
}
