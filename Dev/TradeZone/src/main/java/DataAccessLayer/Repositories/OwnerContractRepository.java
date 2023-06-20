package DataAccessLayer.Repositories;

import DomainLayer.OwnerContract;
import DomainLayer.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnerContractRepository extends JpaRepository<OwnerContract,Integer> {

    public List<OwnerContract> findByStore(Store store);
}
