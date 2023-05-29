package DomainLayer.BagConstraints;

import DomainLayer.Product;
import DomainLayer.User;

import java.util.concurrent.ConcurrentHashMap;

public class BagConstraintOnlyIf implements BagConstraint {
    BagConstraint firstBagConstraint;
    BagConstraint secBagConstraint;

    public BagConstraintOnlyIf(BagConstraint firstBagConstraint, BagConstraint secBagConstraint){
        this.firstBagConstraint = firstBagConstraint;
        this.secBagConstraint = secBagConstraint;

    }
    @Override
    public boolean checkConstraint(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent){
        if(firstBagConstraint.checkConstraint(bagContent))
            return secBagConstraint.checkConstraint(bagContent);
        return true;
    }

    public String toString(){
        return "("+firstBagConstraint.toString()+" ONLY IF "+ secBagConstraint.toString()+")";
    }
}
