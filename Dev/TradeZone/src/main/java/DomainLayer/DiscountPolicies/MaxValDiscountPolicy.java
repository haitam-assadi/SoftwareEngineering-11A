package DomainLayer.DiscountPolicies;

import DomainLayer.BagConstraints.BagConstraint;
import DomainLayer.BagConstraints.PositiveBagConstraint;
import DomainLayer.Product;

import javax.persistence.*;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table
@PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id"),
        @PrimaryKeyJoinColumn(name = "storeName", referencedColumnName = "storeName")
})
public class MaxValDiscountPolicy extends DiscountPolicy{

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "firstDiscountPolicyId", referencedColumnName = "id"),
            @JoinColumn(name = "firstDiscountPolicyStoreName", referencedColumnName = "storeName")
    })
    DiscountPolicy firstDiscountPolicy;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "secondDiscountPolicyId", referencedColumnName = "id"),
            @JoinColumn(name = "secondDiscountPolicyStoreName", referencedColumnName = "storeName")
    })
    DiscountPolicy secondDiscountPolicy;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "constraintId", referencedColumnName = "id"),
            @JoinColumn(name = "constraintStoreName", referencedColumnName = "storeName")
    })
    BagConstraint bagConstraint;

    public MaxValDiscountPolicy(){}
    public MaxValDiscountPolicy(DiscountPolicy firstDiscountPolicy, DiscountPolicy secondDiscountPolicy){
        this.firstDiscountPolicy=firstDiscountPolicy;
        this.secondDiscountPolicy = secondDiscountPolicy;
        bagConstraint = new PositiveBagConstraint();
    }

    public MaxValDiscountPolicy(DiscountPolicy firstDiscountPolicy, DiscountPolicy secondDiscountPolicy, BagConstraint bagConstraint){
        this.firstDiscountPolicy=firstDiscountPolicy;
        this.secondDiscountPolicy = secondDiscountPolicy;
        this.bagConstraint=bagConstraint;
    }
    @Override
    public Double calculateDiscount(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent) {
        Double discountValue = 0.0;

        if (bagConstraint.checkConstraint(bagContent))
            discountValue = Double.max(firstDiscountPolicy.calculateDiscount(bagContent), secondDiscountPolicy.calculateDiscount(bagContent));

        return discountValue;
    }

    @Override
    public Double calculateDiscountForProduct(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent, String productName) throws Exception {
        Double discountValue = 0.0;

        if(bagConstraint.checkConstraint(bagContent)){

            Double firstDiscountValue = firstDiscountPolicy.calculateDiscount(bagContent);
            Double secDiscountValue = secondDiscountPolicy.calculateDiscount(bagContent);
            if (firstDiscountValue > secDiscountValue){
                return firstDiscountPolicy.calculateDiscountForProduct(bagContent,productName);
            }else {
                return secondDiscountPolicy.calculateDiscountForProduct(bagContent,productName);
            }
        }
        return discountValue;
    }

    @Override
    public boolean checkIfProductHaveDiscount(String productName) throws Exception {
        return firstDiscountPolicy.checkIfProductHaveDiscount(productName) || secondDiscountPolicy.checkIfProductHaveDiscount(productName);
    }

    public String toString(){
        String ret = "Max value discount between ("+ firstDiscountPolicy.toString()+") AND ("+secondDiscountPolicy.toString() +").";
        if(!bagConstraint.isPositiveBagConstraint())
            ret = "if ("+bagConstraint.toString()+") then you get "+ret;
        return ret;
    }
}
