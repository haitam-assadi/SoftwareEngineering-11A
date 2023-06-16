package DataAccessLayer.Controller;

import DataAccessLayer.CompositeKeys.CategoryId;
import DataAccessLayer.CompositeKeys.ProductId;
import DataAccessLayer.DALService;
import DataAccessLayer.Repositories.StockRepository;
import DomainLayer.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StoreMapper {
    private Map<String, Store> stores;
    private Set<String> storesNamesConcurrentSet;
    private Map<ProductId, Product> products;
    private Map<CategoryId,Category> categories;
    public static StoreMapper instance = null;

    public static StoreMapper initMapper(){
        instance = new StoreMapper();
        return instance;
    }

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
            return null;
            //throw new Exception("store does not exist! " + storeName);
        }
        if (!stores.containsKey(storeName)){
            Store store = new Store(storeName,false);
            stores.put(storeName,store);
        }
        return stores.get(storeName);
    }

    public List<Store> getStoresByCategoryName(String categoryName) throws Exception {
        //in this way we make getStore(storeName) twice: first here second in storeController
        //we may should add flag if read the first time from db dont needs to be read again and check the
        //list in the class
        List<String>storesNames = DALService.categoryRepository.findAllStoresNamesByCategoryName(categoryName);
        Map<String,Store> stores = new ConcurrentHashMap<>();
        for (String storeName: storesNames){
            if (!stores.containsKey(storeName)){
                stores.put(storeName,getStore(storeName));
            }
        }
        return stores.values().stream().toList();
    }

    public List<Store> getStoresByProductKeyWord(String prefix) throws Exception {
        //in this way we make getStore(storeName) twice: first here second in storeController
        //we may should add flag if read the first time from db dont needs to be read again and check the
        //list in the class
        List<String> storesNames = DALService.productRepository.getStoresByProductKeyWord(prefix);
        Map<String,Store> stores = new ConcurrentHashMap<>();
        for (String storeName: storesNames){
            if (!stores.containsKey(storeName)){
                stores.put(storeName,getStore(storeName));
            }
        }
        return stores.values().stream().toList();
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
            //throw new Exception("product: " + productId.getName() + " does not found");
            return null;
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
            return null;
//            throw new Exception("category: " + categoryId.getCategoryName() + " does not found");
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

    public int updateProductAmountInStock(String productName,String stockName){
        //used in Stock.getProductWithAmount after we check that the product exists abd set the amount with -1
        int amount = DALService.stockRepository.getProductAmount(stockName,productName);
        stores.get(stockName).getStock().setProductAmountAfterLoadingAmount(products.get(new ProductId(productName,stockName)),productName,amount);
        return amount;
    }

    public void insertStore(String newStoreName, Store newStore) {
        storesNamesConcurrentSet.add(newStoreName);
        stores.put(newStoreName,newStore);
    }
    public void insertProduct(ProductId productId,Product product){
        //used only in run time, when adding new product to market
        products.put(productId,product);
    }
    public void insertCategory(CategoryId categoryId,Category category){
        //used only in run time, when adding new category to market
        categories.put(categoryId,category);
    }

    public void removeProduct(ProductId productId) {
        products.remove(productId);
    }
}
