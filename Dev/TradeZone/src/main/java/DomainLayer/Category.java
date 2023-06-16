package DomainLayer;

import DTO.ProductDTO;
import DataAccessLayer.CompositeKeys.CategoryId;
import DataAccessLayer.CompositeKeys.ProductId;
import DataAccessLayer.Controller.StoreMapper;
import DataAccessLayer.DALService;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "Category")
@IdClass(CategoryId.class)
public class Category {

    @Id
    private String categoryName;
    @Id
    private String storeName;
    @ManyToMany(mappedBy = "productCategories",fetch = FetchType.EAGER)
    @MapKey(name = "name")
    private Map<String, Product> categoryProducts;

    @Transient
    private boolean productsIsLoaded;

    public Category (String categoryName, String storeName){
        this.categoryName = categoryName;
        this.storeName = storeName;
        categoryProducts = new ConcurrentHashMap<>();
        productsIsLoaded = true;
    }
    public Category(){}

    public Category(CategoryId categoryId){
        productsIsLoaded = false;
        categoryName = categoryId.getCategoryName();
        storeName = categoryId.getStoreName();
        categoryProducts = new ConcurrentHashMap<>();
    }

    public void loadCategory(){
        if (!productsIsLoaded){
            Optional<Category> category = DALService.categoryRepository.findById(new CategoryId(categoryName,storeName));
            if (category.isPresent()){
                this.categoryName = category.get().categoryName;
                this.storeName = category.get().storeName;
                this.categoryProducts = category.get().categoryProducts;
                setCategoryProducts();
            }
        }
    }

    public void setCategoryProducts(){
        productsIsLoaded = true;
        for (String productName: categoryProducts.keySet().stream().toList()){
            categoryProducts.put(productName,StoreMapper.getInstance().
                    getProductWithoutLoading(new ProductId(productName,storeName)));
        }
    }

    public boolean putProductInCategory(Product product) throws Exception {
        if(categoryProducts.containsKey(product.getName()))
            throw new Exception("the product is already in the category");
        categoryProducts.put(product.getName(), product);
        return true;
    }

    public List<Product> getProducts(){
        List<Product> productList = new ArrayList<>();
        for (Product product:categoryProducts.values())
            productList.add(product);

        return productList;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public boolean containsProduct(String productName) throws Exception {
        if(productName == null || productName.isBlank())
            throw new Exception("productName is null or empty!");

        productName = productName.strip().toLowerCase();
        if(!categoryProducts.containsKey(productName))
            return false;

        return true;
    }
    public void assertContainsProduct(String productName) throws Exception {

        if(!containsProduct(productName))
            throw new Exception("stock does not contain this product "+productName);
    }
    public boolean removeProduct(String productName) throws Exception {
        assertContainsProduct(productName);
        productName = productName.strip().toLowerCase();
        categoryProducts.remove(productName);
        return true;
    }

    public String getName(){
        return categoryName;
    }
}
