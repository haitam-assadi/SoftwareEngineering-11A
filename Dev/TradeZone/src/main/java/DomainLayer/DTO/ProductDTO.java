package DomainLayer.DTO;

public class ProductDTO {
    private String name;
    private Double price;
    private String description;

    public ProductDTO(String name, Double price, String description){
        this.name = name;
        this.price = price;
        this.description = description;
    }
    public String getName() {
        return name;
    }
    public Double getPrice() {
        return price;
    }
    public String getDescription() {
        return description;
    }
}
