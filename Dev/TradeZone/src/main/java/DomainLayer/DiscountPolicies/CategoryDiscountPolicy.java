package DomainLayer.DiscountPolicies;

import DomainLayer.BagConstraints.BagConstraint;
import DomainLayer.BagConstraints.PositiveBagConstraint;
import DomainLayer.Category;
import DomainLayer.Product;

import java.util.concurrent.ConcurrentHashMap;

public class CategoryDiscountPolicy implements DiscountPolicy{
    int discountPercentage;
    Category category;
    BagConstraint bagConstraint;

    public CategoryDiscountPolicy(Category category, int discountPercentage){
        this.category=category;
        this.discountPercentage= discountPercentage;
        this.bagConstraint= new PositiveBagConstraint();

    }

    public CategoryDiscountPolicy(Category category, int discountPercentage, BagConstraint bagConstraint){
        this.category=category;
        this.discountPercentage=discountPercentage;
        this.bagConstraint= bagConstraint;
    }

    @Override
    public Double calculateDiscount(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent) {
        Double discountValue = 0.0;
        if(bagConstraint.checkConstraint(bagContent)){
            for (ConcurrentHashMap<Product, Integer> productTuple: bagContent.values()) {
                Product product = productTuple.keys().nextElement();
                if (product.haveCategory(category)) {
                    int productAmount = productTuple.get(product);
                    discountValue += productAmount * product.getPrice() * (discountPercentage / 100.0);
                }
            }
        }
        return discountValue;
    }

    public String toString(){
        return discountPercentage+"% discount on "+category.getName()+" category.";
    }
}
