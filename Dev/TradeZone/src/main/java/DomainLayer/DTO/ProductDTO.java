package DomainLayer.DTO;

public class ProductDTO {
    private String name;
    private Integer price;
    private String details;
    private Integer amount;

    public ProductDTO(String name,Integer price,String details, Integer amount){
        this.name = name;
        this.price = price;
        this.details = details;
        this.amount = amount;
    }
    public String getName() {
        return name;
    }
    public Integer getPrice() {
        return price;
    }

    public Integer getAmount() {
        return amount;
    }
    public String getDetails() {
        return details;
    }
}
