package DomainLayer.DiscountPolicies;

import DomainLayer.BagConstraints.BagConstraint;
import DomainLayer.BagConstraints.PositiveBagConstraint;
import DomainLayer.Category;
import DomainLayer.Product;

import java.util.concurrent.ConcurrentHashMap;

public class AllStoreDiscountPolicy implements DiscountPolicy{
    int discountPercentage;
    BagConstraint bagConstraint;

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
}
