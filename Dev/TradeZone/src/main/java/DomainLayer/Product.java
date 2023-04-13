package DomainLayer;

public class Product {
    private String name;
    private Integer price;
    private String details;
    private Integer amount;

    public Product(String name,Integer price,String details, Integer amount){
        this.name = name;
        this.price = price;
        this.details = details;
        this.amount = amount;
    }
}
