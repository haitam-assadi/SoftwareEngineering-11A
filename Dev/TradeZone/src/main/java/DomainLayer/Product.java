package DomainLayer;

public class Product {
    private String name;
    private String category;
    private Integer price;
    private String details;
    private Integer amount;

    public Product(String name,String category, Integer price,String details, Integer amount){
        this.name = name;
        this.category = category;
        this.price = price;
        this.details = details;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setDetails(String newProductDetails) {
        this.details = newProductDetails;
    }

    public String getCategory() {
        return category;
    }
}
