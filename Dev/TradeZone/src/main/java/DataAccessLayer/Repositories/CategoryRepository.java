package DataAccessLayer.Repositories;

import DataAccessLayer.CompositeKeys.CategoryId;
import DomainLayer.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, CategoryId> {

    @Query(value = "SELECT store_name FROM category WHERE category_name = ?",nativeQuery = true)
    public List<String> findAllStoresNamesByCategoryName(String categoryName);
}
