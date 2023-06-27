package DataAccessLayer.Repositories;

import DomainLayer.StoreRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRulesRepository extends JpaRepository<StoreRules,Integer> {

    @Query(value = "SELECT * FROM store_rules WHERE notification_type = ? AND store_name = ?",nativeQuery = true)
    public StoreRules findByStoreNameAndNotificationType(String notificationType,String storeName);

    @Query(value = "SELECT id FROM store_rules WHERE store_name = ?",nativeQuery = true)
    public List<Integer> findIdsByStoreName(String storeName);

}
