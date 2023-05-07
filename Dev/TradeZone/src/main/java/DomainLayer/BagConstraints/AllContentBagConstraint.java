package DomainLayer.BagConstraints;

import DomainLayer.Product;
import DomainLayer.User;

import java.util.concurrent.ConcurrentHashMap;

public class AllContentBagConstraint implements BagConstraint {

    BagConstraintType cartPPType;
    Product product;
    int amountLimit;

    public AllContentBagConstraint(Product product, int amountLimit, String cartPPType){
        this.product=product;
        this.amountLimit = amountLimit;

        if(cartPPType == "MaxProductAmount")
            this.cartPPType = BagConstraintType.MaxProductAmount;
        if(cartPPType == "MinProductAmount")
            this.cartPPType = BagConstraintType.MinProductAmount;
    }
    @Override
    public boolean checkConstraint(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent){
        switch (this.cartPPType){
            case MaxProductAmount:
                return checkMaxProductAmountConstraint(bagContent);
            case MinProductAmount:
                return checkMinProductAmountConstraint(bagContent);
            default:
                return true;
        }
    }

    public boolean checkMaxProductAmountConstraint(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent){
        if (bagContent.containsKey(product.getName())){
            int productAmount = bagContent.get(product.getName()).get(product);
            if(productAmount> amountLimit)
                return false;

        }
        return true;
    }
    public boolean checkMinProductAmountConstraint(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent){
        if (bagContent.containsKey(product.getName())){
            int productAmountInCart = bagContent.get(product.getName()).get(product);
            if(productAmountInCart<amountLimit)
                return false;
        }else
            return false;

        return true;
    }
}
