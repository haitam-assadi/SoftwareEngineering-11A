package DataAccessLayer.Controller;

import DataAccessLayer.CompositeKeys.CategoryId;
import DataAccessLayer.CompositeKeys.ProductId;
import DataAccessLayer.DALService;
import DomainLayer.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StoreMapper {
    private Map<String, Store> stores;
    private Set<String> storesNamesConcurrentSet;
    private Map<ProductId, Product> products;
    private Map<CategoryId,Category> categories;
    public static StoreMapper instance = null;

    private StoreMapper(){
        stores = new ConcurrentHashMap<>();
        products = new ConcurrentHashMap<>();
        categories = new ConcurrentHashMap<>();
        storesNamesConcurrentSet = new HashSet<>();
    }

    public static StoreMapper getInstance(){
        if (instance == null){
            instance = new StoreMapper();
        }
        return instance;
    }

    public void loadAllStoresNames() {
        storesNamesConcurrentSet = DALService.storeRepository.getAllStoresNames();
    }

    public Store getStore(String storeName) throws Exception {
        assertStringIsNotNullOrBlank(storeName);
        storeName = storeName.toLowerCase().strip();
        if (!storesNamesConcurrentSet.contains(storeName)){
            throw new Exception("store does not exist! " + storeName);
        }
        if (!stores.containsKey(storeName)){
            Store store = new Store(storeName,false);
            stores.put(storeName,store);
        }
        return stores.get(storeName);
    }
    private void assertStringIsNotNullOrBlank(String st) throws Exception {
        if(st==null || st.isBlank())
            throw new Exception("string is null or empty");
    }

    public boolean isProductExists(ProductId productId){
        if (products.containsKey(productId)){
            return true;
        }else{
            Optional<Product> product = DALService.productRepository.findById(productId);
            if (product.isEmpty()){
                return false;
            }else{
                product.get().setProductCategories();
                products.put(productId,product.get());
                return true;
            }
        }
    }

    public Product getProduct(ProductId productId) throws Exception {
        if (isProductExists(productId)){
            return products.get(productId);
        }else{
            throw new Exception("product: " + productId.getName() + " does not found");
        }
    }

    public Product getProductWithoutLoading(ProductId productId){
        if (!products.containsKey(productId)){
            Product product = new Product(productId);
            products.put(productId,product);
        }
        return products.get(productId);
    }
    public Category getCategoryWithoutLoading(CategoryId categoryId){
        if (!categories.containsKey(categoryId)){
            Category category = new Category(categoryId);
            categories.put(categoryId,category);
        }
        return categories.get(categoryId);
    }

    public boolean isCategoryExists(CategoryId categoryId){
        if (categories.containsKey(categoryId)){
            return true;
        }else{
            Optional<Category> category = DALService.categoryRepository.findById(categoryId);
            if (category.isEmpty()){
                return false;
            }else{
                category.get().setCategoryProducts();
                categories.put(categoryId,category.get());
                return true;
            }
        }
    }

    public Category getCategory(CategoryId categoryId) throws Exception {
        if (isCategoryExists(categoryId)){
            return categories.get(categoryId);
        }else{
            throw new Exception("category: " + categoryId.getCategoryName() + " does not found");
        }
    }

    public Stock getStock(String stockName) throws Exception {
        assertStringIsNotNullOrBlank(stockName);
        stockName = stockName.toLowerCase().strip();
        if (!storesNamesConcurrentSet.contains(stockName)){
            throw new Exception("store does not exist! " + stockName);
        }
        Optional<Stock> stockD = DALService.stockRepository.findById(stockName);
        if (stockD.isPresent()){
            Stock stock = stockD.get();
            for (Product product: stock.getProductAmount().keySet()){
                ProductId productId = new ProductId(product.getName(), stockName);
                if (!products.containsKey(productId)){
                    //todo: check what happen in product.productCategories
                    product.setProductCategories();
                    products.put(productId,product);
                }
            }
            for (Category category: stock.getStockCategories().values()){
                CategoryId categoryId = new CategoryId(category.getCategoryName(), stockName);
                if (!categories.containsKey(categoryId)){
                    category.setCategoryProducts();
                    categories.put(categoryId, category);
                }
            }
            //todo: we can make the categories and products appoint to other.
            Store store = stores.get(stockName);
            store.getStock().updateStock(stock);
            return store.getStock();
        }else{
            throw new Exception("store does not exist! " + stockName);
        }
    }
}
