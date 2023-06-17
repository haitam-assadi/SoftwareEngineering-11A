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
        }else if(Market.dbFlag){

            Optional<Product> product = DALService.productRepository.findById(productId);
            if (product.isEmpty()){
                return false;
            }else{
                product.get().setProductCategories();
                products.put(productId,product.get());
                return true;
            }
        }else return false;
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
        }else if(Market.dbFlag){
            Optional<Category> category = DALService.categoryRepository.findById(categoryId);
            if (category.isEmpty()){
                return false;
            }else{
                category.get().setCategoryProducts();
                categories.put(categoryId,category.get());
                return true;
            }
        }else return false;
    }

    public Category getCategory(CategoryId categoryId) throws Exception {
        if (isCategoryExists(categoryId)){
            return categories.get(categoryId);
        }else{
            return null;
//            throw new Exception("category: " + categoryId.getCategoryName() + " does not found");
        }
    }

    public void loadStock(String stockName) throws Exception {
        assertStringIsNotNullOrBlank(stockName);
        stockName = stockName.toLowerCase().strip();
        Optional<Stock> stockD = DALService.stockRepository.findById(stockName);
        if (!stockD.isPresent())
            throw new Exception("store does not exist! " + stockName);

        Stock dalStock = stockD.get();
        for (Product product: dalStock.getProductAmount().keySet()){
            ProductId productId = new ProductId(product.getName(), stockName);
            if (!products.containsKey(productId)){
                product.setProductCategories();
                products.put(productId,product);
            }
        }
        for (Category category: dalStock.getStockCategories().values()){
            CategoryId categoryId = new CategoryId(category.getCategoryName(), stockName);
            if (!categories.containsKey(categoryId)){
                category.setCategoryProducts();
                categories.put(categoryId, category);
            }
        }
        Stock BL_Stock = getStore(stockName).getStock();
        BL_Stock.updateStock(dalStock);

    }

    /*
    //TODO: MOSLEM: replaced with getNewProduct getNewStore ...
    //TODO: MOSLEM: reason: for example before aster creating new store, and before calling insert, someone called StoreMapper.getStore

    public void insertProduct(ProductId productId,Product product){
        //used only in run time, when adding new product to market
        products.put(productId,product);
    }
    public void insertCategory(CategoryId categoryId,Category category){
        //used only in run time, when adding new category to market
        categories.put(categoryId,category);
    }
    public void insertStore(String newStoreName, Store newStore) {
        storesNamesConcurrentSet.add(newStoreName);
        stores.put(newStoreName,newStore);
    }
*/

    public Store getNewStore(String newStoreName){
        Store newStore = new Store(newStoreName);
        storesNamesConcurrentSet.add(newStoreName);
        stores.put(newStoreName,newStore);
        return newStore;
    }

    public Category getNewCategory(String categoryName, String storeName){
        Category category = new Category(categoryName,storeName);
        categories.put(new CategoryId(categoryName,storeName),category);
        return category;
    }

    public Product getNewProduct(String name,String storeName,Category category, Double price,String description){
        Product product = new Product(name,storeName,category,price,description);
        products.put(new ProductId(name,storeName),product);
        return product;
    }
    public void removeProduct(Product product, Stock stock) {
        products.remove(new ProductId(product.getName(), stock.getStoreName()));
        if (Market.dbFlag)
            DALService.removeProduct(product,stock);
    }
}
