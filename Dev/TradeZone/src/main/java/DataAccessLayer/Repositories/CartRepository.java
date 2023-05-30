package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<DTOCart, Integer> {
}
