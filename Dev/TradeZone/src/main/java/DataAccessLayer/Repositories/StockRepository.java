package DataAccessLayer.Repositories;

import DomainLayer.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface StockRepository extends JpaRepository<Stock,String> {

    @Query(value = "SELECT amount FROM stock_products WHERE stock_name = ? AND product_name = ?",nativeQuery = true)
    public int getProductAmount(String stockName,String productName);
}
