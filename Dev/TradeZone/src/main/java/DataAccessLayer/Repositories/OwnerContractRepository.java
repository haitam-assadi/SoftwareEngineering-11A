package DataAccessLayer.Repositories;

import DomainLayer.OwnerContract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerContractRepository extends JpaRepository<OwnerContract,Integer> {
}
