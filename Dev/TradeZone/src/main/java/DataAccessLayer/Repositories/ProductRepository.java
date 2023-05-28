package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOProduct;
import DataAccessLayer.DTO.compositeKeys.ProductId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<DTOProduct, ProductId> {
}
