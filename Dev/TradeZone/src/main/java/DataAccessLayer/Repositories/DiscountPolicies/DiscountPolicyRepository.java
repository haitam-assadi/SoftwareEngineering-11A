package DataAccessLayer.Repositories.DiscountPolicies;

import DataAccessLayer.CompositeKeys.BagConstrainsId;
import DataAccessLayer.CompositeKeys.DiscountPolicyId;
import DomainLayer.BagConstraints.BagConstraint;
import DomainLayer.DiscountPolicies.DiscountPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountPolicyRepository extends JpaRepository<DiscountPolicy, DiscountPolicyId> {

    public List<DiscountPolicy> findAllByDiscountPolicyIdStoreName(String storeName);

}
