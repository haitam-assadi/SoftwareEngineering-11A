package DataAccessLayer.Repositories.BagConstraints;

import DataAccessLayer.CompositeKeys.BagConstrainsId;
import DomainLayer.BagConstraints.BagConstraintOnlyIf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BagConstraintOnlyIfRepository extends JpaRepository<BagConstraintOnlyIf, BagConstrainsId> {
}
