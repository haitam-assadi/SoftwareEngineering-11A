package DataAccessLayer.Repositories;

import DomainLayer.StoreRules;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRulesRepository extends JpaRepository<StoreRules,Integer> {
}
