package DomainLayer.DiscountPolicies;

import DomainLayer.BagConstraints.BagConstraint;
import DomainLayer.BagConstraints.PositiveBagConstraint;
import DomainLayer.Category;
import DomainLayer.Product;

import javax.persistence.*;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table
@PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id"),
        @PrimaryKeyJoinColumn(name = "storeName", referencedColumnName = "storeName")
})
public class AllStoreDiscountPolicy extends DiscountPolicy{
    int discountPercentage;
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "constraintId", referencedColumnName = "id"),
            @JoinColumn(name = "constraintStoreName", referencedColumnName = "storeName")
    })
    BagConstraint bagConstraint;

    public AllStoreDiscountPolicy(){}
    public AllStoreDiscountPolicy(int discountPercentage){
        this.discountPercentage= discountPercentage;
        this.bagConstraint= new PositiveBagConstraint();

    }

    public AllStoreDiscountPolicy(int discountPercentage, BagConstraint bagConstraint){
        this.discountPercentage=discountPercentage;
        this.bagConstraint= bagConstraint;
    }

    @Override
    public Double calculateDiscount(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent) {
        if(bagConstraint.checkConstraint(bagContent)){
            Double bagPrice = 0.0;
            for (ConcurrentHashMap<Product, Integer> productTuple: bagContent.values()) {
                Product product = productTuple.keys().nextElement();
                int productAmount = productTuple.get(product);
                bagPrice += productAmount * product.getPrice();
            }
            return bagPrice * (discountPercentage/100.0);
        }
        return 0.0;
    }

    public String toString(){
        return discountPercentage+"% discount on all store products.";
    }
}
