package DataAccessLayer.Repositories.DiscountPolicies;

import DataAccessLayer.CompositeKeys.DiscountPolicyId;
import DomainLayer.DiscountPolicies.ProductDiscountPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDiscountPolicyRepository extends JpaRepository<ProductDiscountPolicy, DiscountPolicyId> {
}
