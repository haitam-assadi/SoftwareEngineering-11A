package DomainLayer;

import DTO.ProductDTO;
import DataAccessLayer.CompositeKeys.CategoryId;
import DataAccessLayer.CompositeKeys.ProductId;
import DataAccessLayer.Controller.StoreMapper;
import DataAccessLayer.DALService;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Entity
@Table(name = "Product")
@IdClass(ProductId.class)
public class Product {

    @Id
    @Column(name = "productName")
    private String name;

    @Id
    private String storeName;
    private Double price;
    private String description ;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ProductCategory",
            joinColumns = {
                    @JoinColumn(name = "productName", referencedColumnName = "productName"),
                    @JoinColumn(name = "storeName", referencedColumnName = "storeName")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "categoryName"),
                    @JoinColumn(name = "category.storeName")
            }
    )
    private Map<String, Category> productCategories;

    @Transient
    private boolean productLoaded;

    public void setProductCategories(){
        productLoaded = true;
        for (String categoryName: productCategories.keySet().stream().toList()){
            productCategories.put(categoryName, StoreMapper.getInstance()
                    .getCategoryWithoutLoading(new CategoryId(categoryName,storeName)));
        }
    }

    public Product(String name,String storeName,Category category, Double price,String description){
        this.name = name;
        this.storeName=storeName;
        this.price = price;
        this.description=description;
        productCategories=new ConcurrentHashMap<>();
        productCategories.put(category.getCategoryName(), category);
        productLoaded = true;
        // insert product is called as transaction in its only usage
    }

    public Product(){}

    public Product(ProductId productId){
        productLoaded = false;
        this.name = productId.getName();
        this.storeName = productId.getStoreName();
        productCategories = new ConcurrentHashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setDescription(String newProductDescription) {
        //TODO: MOSLEM:update is added , do we need load before ? so we dont overwrite other fields
        loadProduct();
        this.description = newProductDescription;
        updateProduct();
    }


    public ProductDTO getProductInfo(List<String> productDiscountPolicies){
        loadProduct();
        List<String> categories = new LinkedList<>();
        categories.addAll(productCategories.keySet());
        return new ProductDTO(this.name, this.storeName, this.price, this.description,  categories, productDiscountPolicies);
    }

    public Double getPrice() {
        loadProduct();
        return price;
    }

    public void setPrice(Double newPrice) {
        //TODO: MOSLEM:update is added , do we need load before ? so we dont overwrite other fields
        loadProduct();
        this.price = newPrice;
        updateProduct();

    }


    public boolean removeFromAllCategories() throws Exception {
        loadProduct();
        for(Category category: productCategories.values()) {
            category.removeProduct(getName());
            productCategories.remove(category.getName());
            updateProductCategoryTransaction(category);
        }
        //TODO: MOSLEM: recheck with moslem , no specific reason needs only to check category.removeProduct always called properly
        //TODO: MOSLEM: ask moslem, can we replace updateProductCategoryTransaction to get a list of categories, and call it here
        return true;
    }


    public Double getProductPrice(int amount) throws Exception {
        loadProduct();
        return price*amount;
    }

    public boolean haveCategory(Category category){
        loadProduct();
        return productCategories.values().contains(category);
    }


    private void updateProduct(){
        if (Market.dbFlag)
            DALService.productRepository.save(this);
    }
    private void updateProductCategoryTransaction(Category category){
        if (Market.dbFlag)
            DALService.removeProductCategory(this,category);
    }

    private void loadProduct(){
        if (!productLoaded && Market.dbFlag){
            Optional<Product> product = DALService.productRepository.findById(new ProductId(name, storeName));
            if (product.isPresent()) {
                this.name = product.get().name;
                this.storeName = product.get().storeName;
                this.price = product.get().getPrice();
                this.description = product.get().description;
                this.productCategories = product.get().productCategories;
                setProductCategories();
            }
        }
    }
    //remove product is transaction in StoreMapper and is ready
    //insert new product is transaction and is ready
    //load is ready
}
