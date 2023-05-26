package DomainLayer;

import DTO.ProductDTO;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Product {

    private String name;
    private Double price;
    private String description ;
    private String storeName;
    private ConcurrentHashMap<String,Category> productCategories;

    public Product(String name,String storeName,Category category, Double price,String description){
        this.name = name;
        this.storeName=storeName;
        this.price = price;
        this.description=description;
        productCategories=new ConcurrentHashMap<>();
        productCategories.put(category.getCategoryName(), category);
    }

    public String getName() {
        return name;
    }

    public void setDescription(String newProductDescription) {
        this.description = newProductDescription;
    }


    public ProductDTO getProductInfo(){
        List<String> categories = new LinkedList<>();
        categories.addAll(productCategories.keySet());
        return new ProductDTO(this.name, this.storeName, this.price, this.description,  categories);
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double newPrice) {
        this.price = newPrice;
    }
    public boolean removeFromAllCategories() throws Exception {
        for(Category category: productCategories.values())
            category.removeProduct(getName());

        return true;
    }


    public Double getProductPrice(int amount) throws Exception {
        return price*amount;
    }

    public boolean haveCategory(Category category){
        return productCategories.values().contains(category);
    }
}
