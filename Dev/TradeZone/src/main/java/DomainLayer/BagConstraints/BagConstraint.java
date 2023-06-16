package DomainLayer.BagConstraints;

import DataAccessLayer.CompositeKeys.BagConstrainsId;
import DomainLayer.Product;
import DomainLayer.User;

import javax.persistence.*;
import javax.persistence.MappedSuperclass;
import java.util.concurrent.ConcurrentHashMap;
enum BagConstraintType {
    MaxTimeAtDay, // after 23:00 alcohol not allowed
    RangeOfDays, // from 23/4/2023 to 15/9/2023 alcohol not allowed (כולל)
    MinimumAge, // age < 16 alcohol not allowed

    MaxProductAmount,

    MinProductAmount

}

@Entity
@Table//(name = "bag_constraints")
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "constraint_type", discriminatorType = DiscriminatorType.STRING)
public abstract class BagConstraint {

    @EmbeddedId
    private BagConstrainsId bagConstrainsId;

    public BagConstraint(){
    }

    public boolean isPositiveBagConstraint(){
        return false;
    }
    public void setBagConstrainsId(BagConstrainsId bagConstrainsId){
        this.bagConstrainsId = bagConstrainsId;
    }
    public abstract boolean  checkConstraint(ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent);
}
