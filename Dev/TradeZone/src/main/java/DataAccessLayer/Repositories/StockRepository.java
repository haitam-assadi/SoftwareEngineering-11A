package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<DTOStock,String> {

}
