package DomainLayer.DTO;

public class ProductDTO {
    private String name;
    private Double price;
    private String description;
    private Integer amount;

    public ProductDTO(String name,Double price,String description, Integer amount){
        this.name = name;
        this.price = price;
        this.description = description;
        this.amount = amount;
    }
    public String getName() {
        return name;
    }
    public Double getPrice() {
        return price;
    }

    public Integer getAmount() {
        return amount;
    }
    public String getDetails() {
        return description;
    }
}
