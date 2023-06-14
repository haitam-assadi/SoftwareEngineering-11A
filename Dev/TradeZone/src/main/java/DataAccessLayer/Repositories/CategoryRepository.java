package DataAccessLayer.Repositories;

import DataAccessLayer.CompositeKeys.CategoryId;
import DomainLayer.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, CategoryId> {
}
