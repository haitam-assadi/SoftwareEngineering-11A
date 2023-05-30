package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOCategory;
import DataAccessLayer.DTO.DTOProduct;
import DataAccessLayer.DTO.compositeKeys.ProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<DTOProduct, ProductId> {
    List<DTOProduct> findByStoreName(String store_name);

    List<DTOProduct> findByCategory(DTOCategory dtoCategory);

//    @Query(value = "SELECT p FROM product WHERE p.cat_id = :cat_id",nativeQuery = true)
//    Set<DTOProduct> findByCategory(String cat_id);


}
