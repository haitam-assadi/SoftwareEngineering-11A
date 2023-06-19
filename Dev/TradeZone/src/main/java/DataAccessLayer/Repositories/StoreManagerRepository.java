package DataAccessLayer.Repositories;

import DataAccessLayer.CompositeKeys.RolesId;
import DomainLayer.StoreManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreManagerRepository extends JpaRepository<StoreManager, RolesId> {
    @Query(value = "SELECT member_name FROM store_manager WHERE store_name = ? AND my_boss = ?",nativeQuery = true)
    public List<String> findAllByStoreNameAndBoss(String storeName,String myBoss);

    @Query(value = "SELECT store_name FROM store_owner WHERE member_name = ?",nativeQuery = true)
    public List<String> getStoreNameByMemberName(String memberName);
    @Query(value = "SELECT my_boss FROM store_manager WHERE member_name = ? AND store_name = ?",nativeQuery = true)
    public String findBossById(String memberName,String storeName);

    @Query(value = "SELECT permission FROM manager_permission WHERE store_manager_member_name = ? AND store_manager_store_name = ?",nativeQuery = true)
    public List<String> findManagerPermissionsPerStore(String memberName,String storeName);

    @Query(value = "SELECT member_name FROM store_manager WHERE store_name = ?",nativeQuery = true)
    public List<String> findManagersNamesByStoreName(String storeName);
}
