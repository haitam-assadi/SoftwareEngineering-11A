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
