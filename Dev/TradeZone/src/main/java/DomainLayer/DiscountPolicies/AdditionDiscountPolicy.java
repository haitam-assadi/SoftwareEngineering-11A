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
public class AdditionDiscountPolicy extends DiscountPolicy{
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

    public AdditionDiscountPolicy(){}

    public AdditionDiscountPolicy(DiscountPolicy firstDiscountPolicy, DiscountPolicy secondDiscountPolicy){
        this.firstDiscountPolicy=firstDiscountPolicy;
        this.secondDiscountPolicy = secondDiscountPolicy;
        bagConstraint = new PositiveBagConstraint();
    }

    public AdditionDiscountPolicy(DiscountPolicy firstDiscountPolicy, DiscountPolicy secondDiscountPolicy, BagConstraint bagConstraint){
        this.firstDiscountPolicy=firstDiscountPolicy;
        this.secondDiscountPolicy = secondDiscountPolicy;
        this.bagConstraint=bagConstraint;
    }
    @Override
    public Double calculateDiscount(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent) {
        Double discountValue = 0.0;

        if (bagConstraint.checkConstraint(bagContent))
            discountValue = firstDiscountPolicy.calculateDiscount(bagContent) + secondDiscountPolicy.calculateDiscount(bagContent);

        return discountValue;
    }

    @Override
    public Double calculateDiscountForProduct(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent, String productName) throws Exception {
        Double discountValue = 0.0;

        if (bagConstraint.checkConstraint(bagContent))
            discountValue = firstDiscountPolicy.calculateDiscountForProduct(bagContent, productName) + secondDiscountPolicy.calculateDiscountForProduct(bagContent, productName);

        return discountValue;
    }

    @Override
    public boolean checkIfProductHaveDiscount(String productName) throws Exception {
        return firstDiscountPolicy.checkIfProductHaveDiscount(productName) || secondDiscountPolicy.checkIfProductHaveDiscount(productName);
    }


    public String toString(){
        String ret =  "("+ firstDiscountPolicy.toString()+") in addition ("+secondDiscountPolicy.toString() +").";
        if(!bagConstraint.isPositiveBagConstraint())
            ret = "if ("+bagConstraint.toString()+") then you get "+ret;
        return ret;
    }
}
