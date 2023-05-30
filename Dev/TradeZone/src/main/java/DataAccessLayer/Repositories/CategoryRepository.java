package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOCategory;
import DataAccessLayer.DTO.compositeKeys.CategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<DTOCategory, CategoryId> {
    List<DTOCategory> findByStoreName(String store_name);
}
