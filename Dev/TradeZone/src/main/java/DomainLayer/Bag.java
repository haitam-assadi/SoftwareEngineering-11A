package DomainLayer;

import DTO.BagDTO;
import DTO.DealDTO;
import DTO.ProductDTO;
import DataAccessLayer.CompositeKeys.ProductId;
import DataAccessLayer.Controller.StoreMapper;
import DataAccessLayer.DALService;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Entity
@Table
public class Bag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bagId;

    @OneToOne
    @JoinColumn(name = "storeName")
    private Store storeBag;

    @Transient
    private ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Bag_Products", joinColumns = @JoinColumn(name = "bag_id"))
    @MapKeyJoinColumns({
            @MapKeyJoinColumn(name = "product_name", referencedColumnName = "productName"),
            @MapKeyJoinColumn(name = "store_name", referencedColumnName = "storeName")
    })
    @Column(name = "amount")
    private Map<Product,Integer> productAmount;

    @Transient
    private boolean isLoaded;

    @Transient
    private boolean isPersistence;
    public Bag(Store storeBag,boolean isPersistence) {
        this.storeBag = storeBag;
        bagContent = new ConcurrentHashMap<>();
        productAmount = new ConcurrentHashMap<>();
        isLoaded = true;
        this.isPersistence = isPersistence;
    }

    public Bag(int bagId){
        this.bagId = bagId;
        this.storeBag = null;
        bagContent = new ConcurrentHashMap<>();
        productAmount = new ConcurrentHashMap<>();
        isLoaded = false;
        isPersistence = true;
    }
    public Bag(){}

    public void setLoaded(boolean b){
        isLoaded = b;
    }

    public boolean addProduct(String productName, Integer amount) throws Exception {
        loadBag();
        productName = productName.strip().toLowerCase();
        if(bagContent.containsKey(productName))
            throw new Exception("bag already contains "+productName+" product");

        Product product =storeBag.getProductWithAmount(productName,amount);

        ConcurrentHashMap<Product,Integer> innerHashMap = new ConcurrentHashMap<>();
        innerHashMap.put(product,amount);
        bagContent.put(productName,innerHashMap);
        productAmount.put(product,amount);
        if (Market.dbFlag && isPersistence)
            DALService.bagRepository.save(this);
        return true;
    }

    public boolean changeProductAmount(String productName, Integer newAmount) throws Exception {
        loadBag();
        productName = productName.strip().toLowerCase();
        if(! bagContent.containsKey(productName))
            throw new Exception("bag does not contain "+productName+" product");
        Product product =storeBag.getProductWithAmount(productName,newAmount);
        productAmount.put(findProductbyName(productName),newAmount);
        bagContent.get(productName).put(product,newAmount);
        if (Market.dbFlag && isPersistence)
            DALService.bagRepository.save(this);
        return true;
    }

    public boolean removeProduct(String productName) throws Exception {
        loadBag();
        productName = productName.strip().toLowerCase();
        if(! bagContent.containsKey(productName))
            throw new Exception("bag does not contain "+productName+" product");
        this.productAmount.remove(findProductbyName(productName));
        bagContent.remove(productName);
        if (Market.dbFlag && isPersistence)
            DALService.bagRepository.save(this);
        return bagContent.isEmpty()? false:true;
    }
    private Product findProductbyName(String productName) throws Exception {
        loadBag();
        for (Product product: productAmount.keySet()){
            if (product.getName().equals(productName))
                return product;
        }
        return null;
    }

    public Store getStoreBag() throws Exception {
        loadBag();
        return storeBag;
    }
    public ConcurrentHashMap<String,Integer> getProductWithAmount() throws Exception {
        loadBag();
        List<String> productsNameBag =  bagContent.keySet().stream().toList();
        ConcurrentHashMap<String,Integer> productNameAmount = new ConcurrentHashMap<>();
        for (int i=0;i<productsNameBag.size();i++){
            String productName = productsNameBag.get(i);
            ConcurrentHashMap<Product,Integer> productIntegerConcurrentHashMap = bagContent.get(productName);
            Integer productAmount = productIntegerConcurrentHashMap.get(productIntegerConcurrentHashMap.keys().nextElement());
            productNameAmount.put(productName,productAmount);
        }
        return productNameAmount;
    }
    public BagDTO getBagInfo() throws Exception {
        loadBag();
        ConcurrentHashMap<ProductDTO, Integer> bagContent = new ConcurrentHashMap<>();
        for (ConcurrentHashMap<Product,Integer> curr: this.bagContent.values()) {
            Product currProduct = curr.keys().nextElement();
            bagContent.put(currProduct.getProductInfo(storeBag.getProductDiscountPolicies(currProduct.getName())), curr.values().stream().toList().get(0));
        }

        ConcurrentHashMap<String, Double> priceWithAmountWithoutDiscount = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Double> priceWithAmountWithDiscount = new ConcurrentHashMap<>();

        for(ProductDTO productDTO: bagContent.keySet()){
            String productName = productDTO.name;
            Double discountValue = storeBag.getDiscountForProductInBag(this.bagContent, productName);
            priceWithAmountWithoutDiscount.put(productName, productDTO.price*bagContent.get(productDTO));
            priceWithAmountWithDiscount.put(productName, priceWithAmountWithoutDiscount.get(productName)-discountValue);
        }

        return new BagDTO(storeBag.getStoreName(), bagContent, priceWithAmountWithoutDiscount, priceWithAmountWithDiscount);
    }

    public void validateStorePolicy(String userName) throws Exception {
        loadBag();
        storeBag.validateStorePolicy(bagContent);
    }

    public void validateAllProductsAmounts() throws Exception {
        loadBag();
        for(ConcurrentHashMap<Product,Integer> prodct_amount : bagContent.values()){
            for (Product p : prodct_amount.keySet()){
                storeBag.getProductWithAmount(p.getName(),prodct_amount.get(p));
            }
        }
    }


    public void validateStoreIsActive() throws Exception {
        loadBag();
        if(!storeBag.isActive()){
            List<String> productsNamesList = bagContent.keySet().stream().toList();
            throw new Exception("store "+storeBag.getStoreName()+" is not active, you can't purchase these products "
                    +productsNamesList.toString()+ " .");
        }
    }

    public Double getBagPriceBeforeDiscount() throws Exception {
        loadBag();
        Double totalBagPrice = 0.0;
        for (ConcurrentHashMap<Product,Integer> curr: this.bagContent.values()) {
            Product product = curr.keys().nextElement();
            totalBagPrice += product.getProductPrice(curr.get(product));
        }
        return totalBagPrice;
    }

    public Double getBagPriceAfterDiscount() throws Exception {
        loadBag();
        Double totalBagPrice = 0.0;
        for (ConcurrentHashMap<Product,Integer> curr: this.bagContent.values()) {
            Product product = curr.keys().nextElement();
            totalBagPrice += product.getProductPrice(curr.get(product));
        }
        totalBagPrice -= storeBag.getDiscountForBag(bagContent);
        return totalBagPrice;
    }


    public boolean removeBagAmountFromStock() throws Exception {
        loadBag();
        return storeBag.removeBagAmountFromStock(bagContent);
    }

    public boolean replaceBagAmountToStock() throws Exception {
        loadBag();
        return storeBag.replaceBagAmountToStock(bagContent);
    }

    public void removeAllProducts() throws Exception {
        loadBag();
        bagContent = new ConcurrentHashMap<>();
        productAmount = new ConcurrentHashMap<>();
        if (Market.dbFlag && isPersistence) {
            DALService.bagRepository.save(this);
            DALService.bagRepository.delete(this);
        }
    }

    public Map<Product, Integer> getProductAmount() throws Exception {
        loadBag();
        return productAmount;
    }
    public Deal createDeal(User user) throws Exception {
        loadBag();
        ConcurrentHashMap<String, Double> products_prices = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Integer> products_amount = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Double> productPriceMultipleAmount = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Double> productFinalPriceWithDiscount = new ConcurrentHashMap<>();

        for (String productName: bagContent.keySet()){
            Product product = bagContent.get(productName).keys().nextElement();
            products_prices.put(productName, product.getPrice());
            products_amount.put(productName, bagContent.get(productName).get(product));
        }

        for(String productName: products_prices.keySet()){
            Double discountValue = storeBag.getDiscountForProductInBag(this.bagContent, productName);
            productPriceMultipleAmount.put(productName, products_prices.get(productName)*products_amount.get(productName));
            Double priceWithDiscount = productPriceMultipleAmount.get(productName)-discountValue;
            if(priceWithDiscount < 0.0)
                priceWithDiscount = 0.0;
            productFinalPriceWithDiscount.put(productName, priceWithDiscount);
        }
        Double totalPrice = getBagPriceAfterDiscount();

        Deal deal = new Deal(storeBag.getStoreName(),user.userName, LocalDate.now().toString(), products_prices, products_amount, productPriceMultipleAmount, productFinalPriceWithDiscount,totalPrice);
        storeBag.addDeal(deal);
        return deal;
    }


    public void loadBag() throws Exception {
        if (isLoaded || !isPersistence ||!Market.dbFlag) return;
        String storeName = DALService.bagRepository.findStoreNameById(bagId);
        Store store = StoreMapper.getInstance().getStore(storeName);
        this.storeBag = store;
        Map<String,Integer> productAmount = DALService.bagRepository.findProductAmountById(bagId);
        //todo: check and assure that the return
        for (String productName: productAmount.keySet()){
            ConcurrentHashMap<Product,Integer> product_Amount = new ConcurrentHashMap<>();
            Product product = StoreMapper.getInstance().getProduct(new ProductId(productName,storeName));
            int amount = productAmount.get(productName);
            product_Amount.put(product,amount);
            bagContent.put(productName,product_Amount);
            this.productAmount.put(product,amount);
        }
        isLoaded = true;
    }
}
