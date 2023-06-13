package DomainLayer.BagConstraints;

import DomainLayer.Product;
import DomainLayer.User;

import java.util.concurrent.ConcurrentHashMap;
enum BagConstraintType {
    MaxTimeAtDay, // after 23:00 alcohol not allowed
    RangeOfDays, // from 23/4/2023 to 15/9/2023 alcohol not allowed (כולל)
    MinimumAge, // age < 16 alcohol not allowed

    MaxProductAmount,

    MinProductAmount

}
public interface BagConstraint {

    public default boolean isPositiveBagConstraint(){
        return false;
    }
    boolean checkConstraint(ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent);
}
