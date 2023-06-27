package DataAccessLayer.CompositeKeys;//package DataAccessLayer.DTO.compositeKeys;

import java.io.Serializable;
import java.util.Objects;

public class ProductId implements Serializable {
    private String name;
    private String storeName;


    public ProductId(String productName, String storeName) {
        this.name = productName;
        this.storeName = storeName;
    }
    public ProductId(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductId product = (ProductId) o;
        return name.equals(product.name) && storeName.equals(product.storeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, storeName);
    }

    public String getName() {
        return name;
    }

    public String getStoreName() {
        return storeName;
    }
}
