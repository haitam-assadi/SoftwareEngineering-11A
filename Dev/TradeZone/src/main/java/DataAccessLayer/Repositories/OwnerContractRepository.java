package DataAccessLayer.Repositories;

import DomainLayer.OwnerContract;
import DomainLayer.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerContractRepository extends JpaRepository<OwnerContract,Integer> {

    public OwnerContract findByStore(Store store);
}
