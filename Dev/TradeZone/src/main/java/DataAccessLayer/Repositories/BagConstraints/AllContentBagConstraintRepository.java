package DataAccessLayer.Repositories.BagConstraints;

import DataAccessLayer.CompositeKeys.BagConstrainsId;
import DomainLayer.BagConstraints.AllContentBagConstraint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllContentBagConstraintRepository extends JpaRepository<AllContentBagConstraint, BagConstrainsId> {
}
