package DomainLayer.BagConstraints;

import DomainLayer.Product;

import java.util.concurrent.ConcurrentHashMap;

public class PositiveBagConstraint implements BagConstraint{

    public boolean checkConstraint(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent){
        return true;
    }

    @Override
    public boolean isPositiveBagConstraint(){
        return true;
    }
}
