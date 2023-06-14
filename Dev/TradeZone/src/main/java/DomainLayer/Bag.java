package DomainLayer;

import DTO.BagDTO;
import DTO.ProductDTO;
import DataAccessLayer.DALService;

import javax.persistence.*;
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
    public Bag(Store storeBag) {
        this.storeBag = storeBag;
        bagContent = new ConcurrentHashMap<>();
        productAmount = new ConcurrentHashMap<>();
    }
    public Bag(){}

    public boolean addProduct(String productName, Integer amount,boolean member) throws Exception {
        productName = productName.strip().toLowerCase();
        if(bagContent.containsKey(productName))
            throw new Exception("bag already contains "+productName+" product");

        Product product =storeBag.getProductWithAmount(productName,amount);

        ConcurrentHashMap<Product,Integer> innerHashMap = new ConcurrentHashMap<>();
        innerHashMap.put(product,amount);
        bagContent.put(productName,innerHashMap);
        productAmount.put(product,amount);
        if (member)
            DALService.addProduct(this,product);
        return true;
    }

    public boolean changeProductAmount(String productName, Integer newAmount,boolean member) throws Exception {
        productName = productName.strip().toLowerCase();
        if(! bagContent.containsKey(productName))
            throw new Exception("bag does not contain "+productName+" product");
        Product product =storeBag.getProductWithAmount(productName,newAmount);
        productAmount.put(findProductbyName(productName),newAmount);
        bagContent.get(productName).put(product,newAmount);
        if (member)
            DALService.bagRepository.save(this);
        return true;
    }

    public boolean removeProduct(String productName) throws Exception {
        productName = productName.strip().toLowerCase();
        if(! bagContent.containsKey(productName))
            throw new Exception("bag does not contain "+productName+" product");
        this.productAmount.remove(findProductbyName(productName));
        //DALService.bagRepository.save(this);
        bagContent.remove(productName);
        return bagContent.isEmpty()? false:true;
    }
    private Product findProductbyName(String productName){
        for (Product product: productAmount.keySet()){
            if (product.getName().equals(productName))
                return product;
        }
        return null;
    }

    public Store getStoreBag() {
        return storeBag;
    }
    public ConcurrentHashMap<String,Integer> getProductWithAmount(){
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
    public BagDTO getBagInfo(){
        ConcurrentHashMap<ProductDTO, Integer> bagContent = new ConcurrentHashMap<>();
        for (ConcurrentHashMap<Product,Integer> curr: this.bagContent.values()) {
            bagContent.put(curr.keys().nextElement().getProductInfo(), curr.values().stream().toList().get(0));
        }
        return new BagDTO(storeBag.getStoreName(), bagContent);
    }

    public void validateStorePolicy(String userName) throws Exception {
        storeBag.validateStorePolicy(bagContent);
    }

    public void validateAllProductsAmounts() throws Exception {
        for(ConcurrentHashMap<Product,Integer> prodct_amount : bagContent.values()){
            for (Product p : prodct_amount.keySet()){
                storeBag.getProductWithAmount(p.getName(),prodct_amount.get(p));
            }
        }
    }

    public Double getBagPriceBeforeDiscount() throws Exception {
        Double totalBagPrice = 0.0;
        for (ConcurrentHashMap<Product,Integer> curr: this.bagContent.values()) {
            Product product = curr.keys().nextElement();
            totalBagPrice += product.getProductPrice(curr.get(product));
        }
        return totalBagPrice;
    }

    public Double getBagPriceAfterDiscount() throws Exception {
        Double totalBagPrice = 0.0;
        for (ConcurrentHashMap<Product,Integer> curr: this.bagContent.values()) {
            Product product = curr.keys().nextElement();
            totalBagPrice += product.getProductPrice(curr.get(product));
        }
        totalBagPrice -= storeBag.getDiscountForBag(bagContent);
        return totalBagPrice;
    }


    public boolean removeBagAmountFromStock() throws Exception {
        return storeBag.removeBagAmountFromStock(bagContent);
    }

    public boolean replaceBagAmountToStock() throws Exception {
        return storeBag.replaceBagAmountToStock(bagContent);
    }

    public void removeAllProducts() {
        bagContent = new ConcurrentHashMap<>();
        productAmount = new ConcurrentHashMap<>();
        DALService.bagRepository.save(this);
        DALService.bagRepository.delete(this);
    }

    public Map<Product, Integer> getProductAmount() {
        return productAmount;
    }
}
