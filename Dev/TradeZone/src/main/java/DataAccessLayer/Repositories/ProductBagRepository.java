package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOProductBag;
import DataAccessLayer.DTO.compositeKeys.ProductbagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBagRepository extends JpaRepository<DTOProductBag, ProductbagId> {
}
