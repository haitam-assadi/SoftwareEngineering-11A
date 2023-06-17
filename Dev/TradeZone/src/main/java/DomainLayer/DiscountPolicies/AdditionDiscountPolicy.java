package DomainLayer.DiscountPolicies;

import DomainLayer.BagConstraints.BagConstraint;
import DomainLayer.BagConstraints.PositiveBagConstraint;
import DomainLayer.Product;

import java.util.concurrent.ConcurrentHashMap;

public class AdditionDiscountPolicy implements DiscountPolicy{
    DiscountPolicy firstDiscountPolicy;
    DiscountPolicy secondDiscountPolicy;
    BagConstraint bagConstraint;

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
