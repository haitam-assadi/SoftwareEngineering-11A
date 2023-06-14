package DomainLayer.BagConstraints;

import DomainLayer.Product;
import DomainLayer.User;

import javax.persistence.*;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table
@PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id"),
        @PrimaryKeyJoinColumn(name = "storeName", referencedColumnName = "storeName")
})
public class BagConstraintOnlyIf extends BagConstraint {
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "firstBagConstraintId", referencedColumnName = "id"),
            @JoinColumn(name = "firstBagConstraintStoreName", referencedColumnName = "storeName")
    })
    BagConstraint firstBagConstraint;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "secBagConstraintId", referencedColumnName = "id"),
            @JoinColumn(name = "secBagConstraintStoreName", referencedColumnName = "storeName")
    })
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
