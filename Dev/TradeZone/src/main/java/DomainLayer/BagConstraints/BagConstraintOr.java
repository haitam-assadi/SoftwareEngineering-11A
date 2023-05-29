package DomainLayer.BagConstraints;

import DomainLayer.Product;
import DomainLayer.User;

import java.util.concurrent.ConcurrentHashMap;

public class BagConstraintOr implements BagConstraint {
    BagConstraint firstBagConstraint;
    BagConstraint secBagConstraint;

    public BagConstraintOr(BagConstraint firstBagConstraint, BagConstraint secBagConstraint){
        this.firstBagConstraint = firstBagConstraint;
        this.secBagConstraint = secBagConstraint;

    }
    @Override
    public boolean checkConstraint(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent){
        return firstBagConstraint.checkConstraint(bagContent) || secBagConstraint.checkConstraint(bagContent);
    }

    public String toString(){
        return "("+firstBagConstraint.toString()+" OR "+ secBagConstraint.toString()+")";
    }
}
