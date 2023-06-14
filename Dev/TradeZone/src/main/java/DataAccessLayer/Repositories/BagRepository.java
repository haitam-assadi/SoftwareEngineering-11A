package DataAccessLayer.Repositories;

import DomainLayer.Bag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BagRepository extends JpaRepository<Bag,Integer> {

}
