package DataAccessLayer.Repositories.BagConstraints;

import DataAccessLayer.CompositeKeys.BagConstrainsId;
import DomainLayer.BagConstraints.PositiveBagConstraint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositiveBagConstraintRepository extends JpaRepository<PositiveBagConstraint, BagConstrainsId> {
}
