package DataAccessLayer.Repositories.BagConstraints;

import DataAccessLayer.CompositeKeys.BagConstrainsId;
import DomainLayer.BagConstraints.BagConstraintOr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BagConstraintOrRepository extends JpaRepository<BagConstraintOr, BagConstrainsId> {
}
