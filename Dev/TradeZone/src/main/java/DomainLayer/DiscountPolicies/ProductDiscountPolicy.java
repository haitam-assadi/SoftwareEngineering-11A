package DomainLayer.DiscountPolicies;

import DomainLayer.BagConstraints.BagConstraint;
import DomainLayer.BagConstraints.PositiveBagConstraint;
import DomainLayer.BagConstraints.ProductBagConstraint;
import DomainLayer.Product;

import javax.persistence.*;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table
@PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id"),
        @PrimaryKeyJoinColumn(name = "storeName", referencedColumnName = "storeName")
})
public class ProductDiscountPolicy extends DiscountPolicy{

    int discountPercentage;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "productName", referencedColumnName = "productName"),
            @JoinColumn(name = "productStoreName", referencedColumnName = "storeName")
    })
    Product product;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "constraintId", referencedColumnName = "id"),
            @JoinColumn(name = "constraintStoreName", referencedColumnName = "storeName")
    })
    BagConstraint bagConstraint;

    public ProductDiscountPolicy(){}

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

    public String toString(){
        return discountPercentage+"% discount on "+product.getName()+" product.";
    }
}
