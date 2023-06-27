package DataAccessLayer.Repositories.DiscountPolicies;

import DataAccessLayer.CompositeKeys.DiscountPolicyId;
import DomainLayer.DiscountPolicies.AllStoreDiscountPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllStoreDiscountPolicyRepository extends JpaRepository<AllStoreDiscountPolicy, DiscountPolicyId> {
}
