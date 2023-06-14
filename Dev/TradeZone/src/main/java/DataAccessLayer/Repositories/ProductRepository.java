package DataAccessLayer.Repositories;

import DataAccessLayer.CompositeKeys.ProductId;
import DomainLayer.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, ProductId> {
    @Query(value = "DELETE FROM bag_products WHERE product_name = '?'",nativeQuery = true)
    public Product deleteProduct(String productName);
}
