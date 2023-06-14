package DataAccessLayer.Repositories;

import DataAccessLayer.CompositeKeys.RolesId;
import DomainLayer.StoreFounder;
import DomainLayer.StoreOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreOwnerRepository extends JpaRepository<StoreOwner, RolesId> {
    @Query(value = "SELECT * FROM store_owner WHERE member_name = ?",nativeQuery = true)
    public List<StoreOwner> findAllByMemberName(String memberName);

    @Query(value = "SELECT member_name FROM store_owner WHERE store_name = ? AND my_boss = ?",nativeQuery = true)
    public List<String> findAllByStoreNameAndBoss(String storeName,String myBoss);

}
