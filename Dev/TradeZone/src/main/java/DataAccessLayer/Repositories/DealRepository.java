package DataAccessLayer.Repositories;

import DomainLayer.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {
}
