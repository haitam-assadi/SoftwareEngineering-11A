package DataAccessLayer.Repositories.DiscountPolicies;

import DataAccessLayer.CompositeKeys.DiscountPolicyId;
import DomainLayer.DiscountPolicies.AdditionDiscountPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdditionDiscountPolicyRepository extends JpaRepository<AdditionDiscountPolicy, DiscountPolicyId> {
}
