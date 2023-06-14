package DataAccessLayer.Repositories;

import DataAccessLayer.CompositeKeys.RolesId;
import DomainLayer.StoreFounder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreFounderRepository extends JpaRepository<StoreFounder, RolesId> {
    @Query(value = "SELECT store_name FROM store_founder WHERE member_name = ?",nativeQuery = true)
        public List<String> getStoreNameByMemberName(String memberName);


    @Query(value = "SELECT * FROM store_founder WHERE member_name = ?",nativeQuery = true)
    public List<StoreFounder> findAllByMemberName(String memberName);
}
