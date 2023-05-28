package DataAccessLayer;

import DataAccessLayer.DTO.DTOCategory;
import DataAccessLayer.DTO.DTOProduct;
import DataAccessLayer.DTO.DTOStock;
import DataAccessLayer.DTO.DTOStore;
import DataAccessLayer.DTO.compositeKeys.ProductId;
import DataAccessLayer.Repositories.*;
import DomainLayer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class DALService {

    @Autowired
    public static MemberRepository memberRepository;

    @Autowired
    public static StoreRepository storeRepository;

    @Autowired
    public static StockRepository stockRepository;

    @Autowired
    public static ProductRepository productRepository;

    @Autowired
    public static CategoryRepository categoryRepository;
    @Autowired
    public static StoresManagersRepository storesManagersRepository;
    @Autowired
    public static StoresOwnersRepository storesOwnersRepository;

    public DALService(){
        super();
    }

    @Transactional
    public static DTOStore saveStore(Store store, Member member){
        Stock stock = store.getStock();
        stockRepository.save(stock.getStockDTO());
        return storeRepository.save(store.getStoreDTO(member));
    }

    @Transactional
    public static DTOStore updateActive(String storeName){
        DTOStore dtoStore = DALService.storeRepository.getById(storeName);
        dtoStore.setActive(false);
        return DALService.storeRepository.save(dtoStore);
    }

    @Transactional
    public static DTOProduct addProduct(Category category,
                                        String stockName,
                                        String storeName,
                                        Product product,
                                        int amount){
        DTOStore dtoStore = DALService.storeRepository.getById(storeName);
        DTOStock dtoStock = DALService.stockRepository.getById(stockName);
        DTOCategory dtoCategory = DALService.categoryRepository.save(category.getCategoryDTO(dtoStore,dtoStock));
        return DALService.productRepository.save(product.getDTOproduct(dtoStock,dtoCategory,amount));
    }

    @Transactional
    public static DTOCategory addCategory(Category category,
                                        String stockName,
                                        String storeName
                                        ){
        DTOStore dtoStore = DALService.storeRepository.getById(storeName);
        DTOStock dtoStock = DALService.stockRepository.getById(stockName);
        DTOCategory dtoCategory = DALService.categoryRepository.save(category.getCategoryDTO(dtoStore,dtoStock));
        //return DALService.productRepository.save(product.getDTOproduct(dtoStore,dtoStock,dtoCategory,amount));
        return dtoCategory;
    }


    @Transactional
    public static void removeProduct(String storeName,String productName) {
        ProductId productId = new ProductId(productName,storeName);
        DTOProduct dtoProduct = productRepository.getById(productId);
        productRepository.delete(dtoProduct);
    }

    @Transactional
    public static void updateProductAmount(String storeName,String productName,int amount) {
        ProductId productId = new ProductId(productName,storeName);
        DTOProduct dtoProduct = productRepository.getById(productId);
        dtoProduct.setAmount(amount);
        productRepository.save(dtoProduct);
    }

    @Transactional
    public static void updateProductPrice(String storeName,String productName,double price) {
        ProductId productId = new ProductId(productName,storeName);
        DTOProduct dtoProduct = productRepository.getById(productId);
        dtoProduct.setPrice(price);
        productRepository.save(dtoProduct);
    }

    @Transactional
    public static void updateProductDescription(String storeName,String productName,String desc) {
        ProductId productId = new ProductId(productName,storeName);
        DTOProduct dtoProduct = productRepository.getById(productId);
        dtoProduct.setDescription(desc);
        productRepository.save(dtoProduct);
    }
}
/*









///////////




package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<DTOStore, String> {

}


 */