package DataAccessLayer.Repositories;

import DomainLayer.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface StoreRepository extends JpaRepository<Store,String> {
    @Query(value = "SELECT store_name FROM store",nativeQuery = true)
    public Set<String> getAllStoresNames();

    @Query(value = "SELECT is_active FROM store WHERE store_name = ?",nativeQuery = true)
    public boolean findIsActiveById(String storeName);
}
