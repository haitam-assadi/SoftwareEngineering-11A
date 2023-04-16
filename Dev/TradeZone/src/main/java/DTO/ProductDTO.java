package DTO;

public class ProductDTO {
    public String name;
    public Double price;
    public String description;

    public ProductDTO(String name, Double price, String description){
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
