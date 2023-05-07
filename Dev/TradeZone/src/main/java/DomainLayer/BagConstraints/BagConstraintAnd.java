package DomainLayer.BagConstraints;

import DomainLayer.Product;
import DomainLayer.User;

import java.util.concurrent.ConcurrentHashMap;

public class BagConstraintAnd implements BagConstraint {
    BagConstraint firstBagConstraint;
    BagConstraint secBagConstraint;

    public BagConstraintAnd(BagConstraint firstBagConstraint, BagConstraint secBagConstraint){
        this.firstBagConstraint = firstBagConstraint;
        this.secBagConstraint = secBagConstraint;

    }
    @Override
    public boolean checkConstraint(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent){
        return firstBagConstraint.checkConstraint(bagContent) && secBagConstraint.checkConstraint(bagContent);
    }
}
