package DataAccessLayer.Repositories.DiscountPolicies;

import DataAccessLayer.CompositeKeys.DiscountPolicyId;
import DomainLayer.DiscountPolicies.MaxValDiscountPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaxValDiscountPolicyRepository extends JpaRepository<MaxValDiscountPolicy, DiscountPolicyId> {
}
