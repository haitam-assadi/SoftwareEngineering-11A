package DomainLayer.DiscountPolicies;

import DataAccessLayer.CompositeKeys.BagConstrainsId;
import DataAccessLayer.CompositeKeys.DiscountPolicyId;
import DomainLayer.Product;

import javax.persistence.*;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DiscountPolicy {

    @EmbeddedId
    private DiscountPolicyId discountPolicyId;

    public DiscountPolicy(){
    }

    public void setDiscountPolicyId(DiscountPolicyId discountPolicyId) {
        this.discountPolicyId = discountPolicyId;
    }

    public DiscountPolicyId getDiscountPolicyId() {
        return discountPolicyId;
    }

    public abstract Double calculateDiscount(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent);
}
