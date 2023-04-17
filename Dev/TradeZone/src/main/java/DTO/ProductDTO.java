package DTO;

public class ProductDTO {
    public String name;
    public String storeName;
    public Double price;
    public String description;

    public ProductDTO(String name, String storeName, Double price, String description){
        this.name = name;
        this.storeName=storeName;
        this.price = price;
        this.description = description;
    }
}
