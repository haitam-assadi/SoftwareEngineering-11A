package DataAccessLayer.Repositories;

import DomainLayer.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart,Integer> {

    @Query(value = "DELETE FROM carts WHERE cart_id > 1 AND member_name is NULL",nativeQuery = true)
    public void deleteGuest();
}
