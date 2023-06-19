package DataAccessLayer.Repositories.BagConstraints;

import DataAccessLayer.CompositeKeys.BagConstrainsId;
import DomainLayer.BagConstraints.BagConstraintAnd;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BagConstraintAndRepository extends JpaRepository<BagConstraintAnd, BagConstrainsId> {
}
