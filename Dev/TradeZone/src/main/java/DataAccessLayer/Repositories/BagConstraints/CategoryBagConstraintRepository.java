package DataAccessLayer.Repositories.BagConstraints;

import DataAccessLayer.CompositeKeys.BagConstrainsId;
import DomainLayer.BagConstraints.CategoryBagConstraint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryBagConstraintRepository extends JpaRepository<CategoryBagConstraint, BagConstrainsId> {
}
