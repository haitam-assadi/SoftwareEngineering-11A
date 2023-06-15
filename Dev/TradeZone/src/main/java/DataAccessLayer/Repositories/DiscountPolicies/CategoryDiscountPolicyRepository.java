package DataAccessLayer.Repositories.DiscountPolicies;

import DataAccessLayer.CompositeKeys.DiscountPolicyId;
import DomainLayer.DiscountPolicies.CategoryDiscountPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDiscountPolicyRepository extends JpaRepository<CategoryDiscountPolicy, DiscountPolicyId> {
}
