package DataAccessLayer.Repositories.BagConstraints;

import DataAccessLayer.CompositeKeys.BagConstrainsId;
import DomainLayer.BagConstraints.BagConstraint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BagConstraintRepository extends JpaRepository<BagConstraint, BagConstrainsId> {

    public List<BagConstraint> findAllByBagConstrainsIdStoreName(String storeName);

}
