package DataAccessLayer.Repositories;

import DomainLayer.RolerNotificator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RolerNotificatorRepository extends JpaRepository<RolerNotificator,Integer> {

    @Query(value = "SELECT id FROM roler_notificator WHERE store_rules_id = ?", nativeQuery = true)
    public List<Integer> findIdsByStoreId(int id);
}
