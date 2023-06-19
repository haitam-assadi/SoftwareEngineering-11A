package DataAccessLayer.Repositories;

import DomainLayer.Bag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface BagRepository extends JpaRepository<Bag,Integer> {
    @Query(value = "SELECT store_name FROM bag WHERE bag_id = ?",nativeQuery = true)
    public String findStoreNameById(int id);
    //todo: check the build in query

    @Query(value = "SELECT product_name, amount FROM bag WHERE bag_id = ?",nativeQuery = true)
    public Map<String,Integer> findProductAmountById(int id);
}
