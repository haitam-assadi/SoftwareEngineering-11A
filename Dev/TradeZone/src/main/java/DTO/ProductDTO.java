package DTO;

import java.util.List;
import java.util.stream.Stream;

public class ProductDTO {
    public String name;
    public String storeName;
    public Double price;
    public String description;

    public List<String> categories;
    public List<String> productDiscountPolicies;


    public ProductDTO(String name, String storeName, Double price, String description, List<String> categories, List<String> productDiscountPolicies){
        this.name = name;
        this.storeName=storeName;
        this.price = price;
        this.description = description;
        this.categories = categories;
        this.productDiscountPolicies = productDiscountPolicies;
    }
}
