package DomainLayer;

import DomainLayer.DTO.ProductDTO;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Product {

    private Stock stock;
    private String name;
    private Double price;
    private String description ;
    private ConcurrentHashMap<String,Category> productCategories;

    public Product(String name,Stock stock,Category category, Double price,String description){
        this.name = name;
        this.stock=stock;
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
        return new ProductDTO(this.name,this.price, this.description);
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
}
