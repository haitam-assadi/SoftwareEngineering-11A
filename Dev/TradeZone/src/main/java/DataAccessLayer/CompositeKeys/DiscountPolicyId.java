package DataAccessLayer.CompositeKeys;

import java.io.Serializable;
import java.util.Objects;

public class DiscountPolicyId implements Serializable {

    int id;
    String storeName;

    public DiscountPolicyId(int id,String storeName){
        this.id = id;
        this.storeName =storeName;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountPolicyId discountPolicyId = (DiscountPolicyId) o;
        return id == discountPolicyId.id && storeName.equals(discountPolicyId.storeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, storeName);
    }

    public int getId() {
        return id;
    }

    public String getStoreName() {
        return storeName;
    }
}
