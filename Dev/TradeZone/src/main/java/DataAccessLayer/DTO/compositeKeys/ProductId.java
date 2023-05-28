package DataAccessLayer.DTO.compositeKeys;

import DataAccessLayer.DTO.DTOStore;

import java.io.Serializable;
import java.util.Objects;

public class ProductId implements Serializable {
    private String productName;
    //private DTOStore dtoStore;
    private String storeName;


    public ProductId(String productName, String storeName) {
        this.productName = productName;
        this.storeName = storeName;
    }
    public ProductId(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductId product = (ProductId) o;
        return productName.equals(product.productName) && storeName.equals(product.storeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, storeName);
    }
}
