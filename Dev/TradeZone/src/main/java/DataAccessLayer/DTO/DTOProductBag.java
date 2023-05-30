package DataAccessLayer.DTO;

import DataAccessLayer.DTO.compositeKeys.ProductbagId;

import javax.persistence.*;

@Entity
@Table(name = "product_bag")
public class DTOProductBag {
    @EmbeddedId
    private ProductbagId id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "productName", referencedColumnName = "productName",insertable = false,updatable = false),
            @JoinColumn(name = "storeName", referencedColumnName = "storeName",insertable = false,updatable = false)
    })
    private DTOProduct product;

    @ManyToOne
    @JoinColumn(name = "bagId",insertable = false,updatable = false)
    private DTOBag bag;

    private int amount;

    public DTOProductBag(ProductbagId id,DTOProduct product, DTOBag bag, int amount) {
        this.id = id;
        this.product = product;
        this.bag = bag;
        this.amount = amount;
    }

    public DTOProductBag() {
    }

    public ProductbagId getId() {
        return id;
    }

    public void setId(ProductbagId id) {
        this.id = id;
    }

    public DTOProduct getProduct() {
        return product;
    }

    public void setProduct(DTOProduct product) {
        this.product = product;
    }

    public DTOBag getBag() {
        return bag;
    }

    public void setBag(DTOBag bag) {
        this.bag = bag;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
