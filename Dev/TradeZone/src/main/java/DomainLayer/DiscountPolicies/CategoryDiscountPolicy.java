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
public class CategoryDiscountPolicy extends DiscountPolicy{
    int discountPercentage;
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "categoryName", referencedColumnName = "categoryName"),
            @JoinColumn(name = "categoryStoreName", referencedColumnName = "storeName")
    })
    Category category;
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "constraintId", referencedColumnName = "id"),
            @JoinColumn(name = "constraintStoreName", referencedColumnName = "storeName")
    })
    BagConstraint bagConstraint;

    public CategoryDiscountPolicy(){}

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

    public BagConstraint getBagConstraint() {
        return bagConstraint;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setBagConstraint(BagConstraint bagConstraint) {
        this.bagConstraint = bagConstraint;
    }
}
