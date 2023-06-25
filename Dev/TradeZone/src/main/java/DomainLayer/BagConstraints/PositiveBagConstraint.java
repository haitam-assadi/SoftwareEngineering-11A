package DomainLayer.BagConstraints;

import DataAccessLayer.CompositeKeys.BagConstrainsId;
import DomainLayer.Product;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table
@PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id"),
        @PrimaryKeyJoinColumn(name = "storeName", referencedColumnName = "storeName")
})
public class PositiveBagConstraint extends BagConstraint{
    public static int pBId = -1;
    public PositiveBagConstraint(){}
    public boolean checkConstraint(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent){
        return true;
    }

    @Override
    public void setBagConstrainsId(BagConstrainsId bagConstrainsId) {
        PositiveBagConstraint.pBId --;
        super.setBagConstrainsId(bagConstrainsId);
    }

    @Override
    public boolean isPositiveBagConstraint(){
        return true;
    }
}
