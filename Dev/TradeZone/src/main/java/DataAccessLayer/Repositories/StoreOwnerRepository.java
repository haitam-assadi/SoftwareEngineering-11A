package DataAccessLayer.Repositories;

import DataAccessLayer.CompositeKeys.RolesId;
import DomainLayer.Main;
import DomainLayer.StoreFounder;
import DomainLayer.StoreOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface StoreOwnerRepository extends JpaRepository<StoreOwner, RolesId> {
    @Query(value = "SELECT * FROM store_owner WHERE member_name = ?",nativeQuery = true)
    public List<StoreOwner> findAllByMemberName(String memberName);

    @Query(value = "SELECT member_name FROM store_owner WHERE store_name = ? AND my_boss = ?",nativeQuery = true)
    public List<String> findAllByStoreNameAndBoss(String storeName,String myBoss);

    @Query(value = "SELECT store_name FROM store_owner WHERE member_name = ?",nativeQuery = true)
    public List<String> getStoreNameByMemberName(String memberName);

    @Query(value = "SELECT my_boss_type, my_boss FROM store_owner WHERE member_name = ? AND store_name = ?",nativeQuery = true)
    public Map<String,String> findBossById(String memberName, String storeName);

}
