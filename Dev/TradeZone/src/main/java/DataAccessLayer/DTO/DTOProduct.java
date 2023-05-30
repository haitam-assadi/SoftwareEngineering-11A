package DataAccessLayer.DTO;

import DataAccessLayer.DTO.compositeKeys.ProductId;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Product")
@IdClass(ProductId.class)
public class DTOProduct {

    @Id
    private String productName;
    @Id
    private String storeName;

    private Double price;

    private String description;

    @OneToOne
    @JoinColumn(name = "stock_id")
    private DTOStock dtoStock;

    private Integer amount;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "cat_id", referencedColumnName = "categoryName"),
            @JoinColumn(name = "store_id", referencedColumnName = "storeName")
    })
    private DTOCategory category;

    @ManyToMany
    @JoinTable(
            name = "bag_product",
            joinColumns = {@JoinColumn(name = "product_name", referencedColumnName = "productName"),
                    @JoinColumn(name = "store_name", referencedColumnName = "storeName")},
            inverseJoinColumns = @JoinColumn(name = "bag_id", referencedColumnName = "bagId")
    )
    private List<DTOBag> bags;


    public DTOProduct(String productName, String storeName, Double price, String description, DTOStock dtoStock, Integer amount,DTOCategory dtoCategory) {
        this.productName = productName;
        this.storeName = storeName;
        this.price = price;
        this.description = description;
        this.dtoStock = dtoStock;
        this.amount = amount;
        this.category = dtoCategory;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public DTOProduct(){

    }

    public String getProductName() {
        return productName;
    }

    public String getStoreName() {
        return storeName;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public Integer getAmount() {
        return amount;
    }

    public DTOCategory getCategory() {
        return category;
    }
}
