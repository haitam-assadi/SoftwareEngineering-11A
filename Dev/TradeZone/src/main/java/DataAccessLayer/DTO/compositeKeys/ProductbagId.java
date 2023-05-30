package DataAccessLayer.DTO.compositeKeys;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ProductbagId implements Serializable {
    private String productName;
    private String storeName;

    private int bagId;

    public ProductbagId(String productName, String storeName, int bagId) {
        this.productName = productName;
        this.storeName = storeName;
        this.bagId = bagId;
    }

    public ProductbagId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductbagId productbagId = (ProductbagId) o;
        return productName.equals(productbagId.productName) && storeName.equals(productbagId.storeName)
                && bagId == productbagId.bagId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, storeName,bagId);
    }}
