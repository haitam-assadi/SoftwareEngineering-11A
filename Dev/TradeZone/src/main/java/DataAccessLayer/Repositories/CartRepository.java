package DataAccessLayer.Repositories;

import DomainLayer.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface CartRepository extends JpaRepository<Cart,Integer> {

    @Query(value = "DELETE FROM carts WHERE cart_id > 1 AND member_name is NULL",nativeQuery = true)
    public void deleteGuest();

    @Query(value = "SELECT cart_id FROM carts WHERE member_name = ?",nativeQuery = true)
    public int findIdByMember(String memberName);
    //todo: check the build in query

    @Query(value = "SELECT bag_key, bags_bag_id FROM cart_bags WHERE cart_id = ?",nativeQuery = true)
    public Map<String,Integer> findStoresNamesAndBagsId(int id);
}
