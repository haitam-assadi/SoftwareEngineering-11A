package DomainLayer.DiscountPolicies;

import DomainLayer.BagConstraints.BagConstraint;
import DomainLayer.BagConstraints.PositiveBagConstraint;
import DomainLayer.Product;

import java.util.concurrent.ConcurrentHashMap;

public class MaxValDiscountPolicy implements DiscountPolicy{

    DiscountPolicy firstDiscountPolicy;
    DiscountPolicy secondDiscountPolicy;
    BagConstraint bagConstraint;
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
}
