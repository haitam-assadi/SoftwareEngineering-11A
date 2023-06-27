package DataAccessLayer.Repositories.BagConstraints;

import DataAccessLayer.CompositeKeys.BagConstrainsId;
import DomainLayer.BagConstraints.ProductBagConstraint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBagConstraintRepository extends JpaRepository<ProductBagConstraint, BagConstrainsId> {
}
