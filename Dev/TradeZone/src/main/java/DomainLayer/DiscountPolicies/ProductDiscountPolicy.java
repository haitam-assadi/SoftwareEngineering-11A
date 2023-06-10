package DomainLayer.DiscountPolicies;

import DomainLayer.BagConstraints.BagConstraint;
import DomainLayer.BagConstraints.PositiveBagConstraint;
import DomainLayer.BagConstraints.ProductBagConstraint;
import DomainLayer.Product;

import java.util.concurrent.ConcurrentHashMap;

public class ProductDiscountPolicy implements DiscountPolicy{

    int discountPercentage;
    Product product;
    BagConstraint bagConstraint;

    public ProductDiscountPolicy(Product product, int discountPercentage){
        this.product=product;
        this.discountPercentage= discountPercentage;
        this.bagConstraint= new PositiveBagConstraint();

    }

    public ProductDiscountPolicy(Product product, int discountPercentage, BagConstraint bagConstraint){
        this.product=product;
        this.discountPercentage=discountPercentage;
        this.bagConstraint= bagConstraint;
    }

    @Override
    public Double calculateDiscount(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent) {
        Double discountValue = 0.0;
        if(bagConstraint.checkConstraint(bagContent) && bagContent.containsKey(product.getName())){
            ConcurrentHashMap<Product, Integer> productTuple = bagContent.get(product.getName());
            int productAmount = productTuple.get(product);
            discountValue = productAmount*product.getPrice()*(discountPercentage/100.0);
        }
        return discountValue;
    }

    @Override
    public Double calculateDiscountForProduct(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent, String productName) {
        Double discountValue = 0.0;
        productName=productName.strip().toLowerCase();
        if(productName.equals(product.getName()) && bagConstraint.checkConstraint(bagContent) && bagContent.containsKey(product.getName())){
            ConcurrentHashMap<Product, Integer> productTuple = bagContent.get(product.getName());
            int productAmount = productTuple.get(product);
            discountValue = productAmount*product.getPrice()*(discountPercentage/100.0);
        }
        return discountValue;
    }

    @Override
    public boolean checkIfProductHaveDiscount(String productName) {
        productName=productName.strip().toLowerCase();
        return product.getName().equals(productName);
    }

    public String toString(){
        return discountPercentage+"% discount on "+product.getName()+" product.";
    }
}
