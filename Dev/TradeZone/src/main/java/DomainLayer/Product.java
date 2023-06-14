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
    }

    public Product(){}

    public Product(ProductId productId){
        productLoaded = false;
        this.name = productId.getName();
        this.storeName = productId.getStoreName();
        productCategories = new ConcurrentHashMap<>();
    }

    public void loadProduct(){
        if (!productLoaded){
            Optional<Product> product = DALService.productRepository.findById(new ProductId(name,storeName));
            if (product.isPresent()){
                this.name = product.get().name;
                this.storeName = product.get().storeName;
                this.price = product.get().getPrice();
                this.description = product.get().description;
                this.productCategories = product.get().productCategories;
                setProductCategories();
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setDescription(String newProductDescription) {
        this.description = newProductDescription;
        DALService.productRepository.save(this);
    }


    public ProductDTO getProductInfo(){
        //loadProduct();
        List<String> categories = new LinkedList<>();
        categories.addAll(productCategories.keySet());
        return new ProductDTO(this.name, this.storeName, this.price, this.description,  categories);
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double newPrice) {
        this.price = newPrice;
        DALService.productRepository.save(this);
    }
    public boolean removeFromAllCategories() throws Exception {
        for(Category category: productCategories.values()) {
            category.removeProduct(getName());
            productCategories.remove(category.getName());
            DALService.removeProductCategory(this,category);
        }
        return true;
    }


    public Double getProductPrice(int amount) throws Exception {
        return price*amount;
    }

    public boolean haveCategory(Category category){
        return productCategories.values().contains(category);
    }
}
