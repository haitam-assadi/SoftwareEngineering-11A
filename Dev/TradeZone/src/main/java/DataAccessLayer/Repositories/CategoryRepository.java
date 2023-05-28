package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOCategory;
import DataAccessLayer.DTO.compositeKeys.CategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<DTOCategory, CategoryId> {
}
