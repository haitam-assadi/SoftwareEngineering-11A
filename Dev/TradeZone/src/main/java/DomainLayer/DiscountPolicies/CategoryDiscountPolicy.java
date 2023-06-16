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

    @Override
    public Double calculateDiscountForProduct(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent, String productName) throws Exception {
        Double discountValue = 0.0;
        productName=productName.strip().toLowerCase();
        if(category.containsProduct(productName) && bagConstraint.checkConstraint(bagContent) && bagContent.containsKey(productName)){
            ConcurrentHashMap<Product, Integer> productTuple = bagContent.get(productName);
            Product product = productTuple.keys().nextElement();
            int productAmount = productTuple.get(product);
            discountValue = productAmount*product.getPrice()*(discountPercentage/100.0);
        }
        return discountValue;
    }

    @Override
    public boolean checkIfProductHaveDiscount(String productName) throws Exception {
        productName=productName.strip().toLowerCase();
        return category.containsProduct(productName);
    }

    public String toString(){
        String ret = discountPercentage+"% discount on "+category.getName()+" category.";
        if(!bagConstraint.isPositiveBagConstraint())
            ret = "if ("+bagConstraint.toString()+") then you get "+ret;


        return ret;

    }
}
