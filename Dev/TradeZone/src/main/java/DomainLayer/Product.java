package DomainLayer;

import DomainLayer.DTO.ProductDTO;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Product {

    private Stock stock;
    private String name;
    private String category;
    private Double price;
    private String description ;
    private ConcurrentHashMap<String,Category> productCategories;

    public Product(String name,Stock stock,String category, Double price,String description){
        this.name = name;
        this.stock=stock;
        this.category = category;
        this.price = price;
        this.description=description;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String newProductDescription) {
        this.description = newProductDescription;
    }

    public String getCategory() {
        return category;
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
}
