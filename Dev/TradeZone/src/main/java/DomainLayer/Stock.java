package DomainLayer;

import DTO.ProductDTO;
import DataAccessLayer.DALService;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table
public class Stock {

    @Id
    private String stockName;

    @Transient
    private ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> stockProducts;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Stock_Products", joinColumns = @JoinColumn(name = "stock_name"))
    @MapKeyJoinColumns({
            @MapKeyJoinColumn(name = "product_name", referencedColumnName = "productName"),
            @MapKeyJoinColumn(name = "store_name", referencedColumnName = "storeName")
    })
    @Column(name = "amount")
    private Map<Product,Integer> productAmount;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "stock_categories")
    @Column(name = "category")
    private Map<String,Category> stockCategories;

    @Transient
    private boolean isLoaded;

    public Stock(String stockName){
        this.stockName = stockName;
        stockProducts =new ConcurrentHashMap<>();
        stockCategories = new ConcurrentHashMap<>();
        productAmount = new ConcurrentHashMap<>();
        isLoaded = true;
    }
    public Stock(String stockName,boolean isLoaded){
        this.stockName = stockName;
        stockProducts =new ConcurrentHashMap<>();
        stockCategories = new ConcurrentHashMap<>();
        productAmount = new ConcurrentHashMap<>();
        this.isLoaded = isLoaded;
    }

    public Stock(){}

    public synchronized void assertStringIsNotNullOrBlank(String st) throws Exception {
        if(st==null || st.isBlank())
            throw new Exception("string is null or empty");
    }
    public synchronized void assertContainsProduct(String productName) throws Exception {
        if(!containsProduct(productName))
            throw new Exception("stock does not contain this product "+productName);
    }

    public synchronized void assertContainsCategory(String categoryName) throws Exception {
        if(!containsCategory(categoryName))
            throw new Exception("stock does not contain this category "+categoryName);
    }
    public synchronized void assertDoesNotContainProduct(String productName) throws Exception {
        if(containsProduct(productName))
            throw new Exception("stock does already contains this product "+productName);
    }

    @Transactional
    public synchronized boolean addNewProductToStock(String nameProduct,String category, Double price, String description, Integer amount) throws Exception {
        assertDoesNotContainProduct(nameProduct);
        assertStringIsNotNullOrBlank(category);
        assertStringIsNotNullOrBlank(description);

        nameProduct=nameProduct.strip().toLowerCase();
        category = category.strip().toLowerCase();
        Category productCategory;
        if(!containsCategory(category)) {
            productCategory = new Category(category, getStoreName());
            DALService.categoryRepository.save(productCategory);
            stockCategories.put(category,productCategory);
            DALService.stockRepository.save(this);
        }
        productCategory = stockCategories.get(category);
        Product product = new Product(nameProduct,stockName,productCategory,price,description);
        productCategory.putProductInCategory(product);
        ConcurrentHashMap<Product,Integer> productAmount = new ConcurrentHashMap<Product, Integer>();
        this.productAmount.put(product,amount);
        productAmount.put(product,amount);
        stockProducts.put(nameProduct,productAmount);
        DALService.saveProduct(this,productCategory,product);
        return true;
    }

    public synchronized boolean removeProductFromStock(String productName) throws Exception {
        assertContainsProduct(productName);
        productName=productName.strip().toLowerCase();
        Product product = stockProducts.get(productName).keys().nextElement();
        product.removeFromAllCategories();
        stockProducts.remove(productName);
        productAmount.remove(product);
        DALService.removeProduct(product,this);
        return true;
    }

    public synchronized boolean updateProductDescription(String productName, String newProductDescription) throws Exception {
        productName = productName.strip().toLowerCase();
        if(!stockProducts.containsKey(productName))
            throw new Exception("can't remove product from stock : productName "+ productName+" is not in the stock!");
        Product product = stockProducts.get(productName).keys().nextElement();
        product.setDescription(newProductDescription);
        return true;
    }

    public synchronized boolean updateProductAmount(String productName, Integer newAmount) throws Exception {
        productName = productName.strip().toLowerCase();
        if(!stockProducts.containsKey(productName))
            throw new Exception("can't remove product from stock : productName "+ productName+" is not in the stock!");
        Product product = stockProducts.get(productName).keys().nextElement();
        if(stockProducts.get(productName).get(product) == newAmount)
            throw new Exception("the amount of the product equals to the new amount");
        stockProducts.remove(productName);
        ConcurrentHashMap<Product,Integer> productAmount = new ConcurrentHashMap<Product, Integer>();
        this.productAmount.put(product,newAmount);
        productAmount.put(product,newAmount);
        stockProducts.put(productName,productAmount);
        DALService.stockRepository.save(this);
        return true;
    }


    public synchronized boolean updateProductPrice(String productName, Double newPrice) throws Exception {
        productName = productName.strip().toLowerCase();
        if(!stockProducts.containsKey(productName))
            throw new Exception("can't remove product from stock : productName "+ productName+" is not in the stock!");
        Product product = stockProducts.get(productName).keys().nextElement();
        if(Objects.equals(product.getPrice(), newPrice))
            throw new Exception("the price of the product equals to the new price");
        product.setPrice(newPrice);
        return true;
    }

    public synchronized Map<ProductDTO, Integer> getProductsInfoAmount(){
        Collection<ConcurrentHashMap<Product,Integer>> currentStockProducts = stockProducts.values();
        Map<ProductDTO, Integer> productsInfoAmount = new LinkedHashMap<>();
        for(ConcurrentHashMap<Product,Integer> curr_hash_map: currentStockProducts) {
            Product product = curr_hash_map.keys().nextElement();
            ProductDTO productDTO = curr_hash_map.keys().nextElement().getProductInfo();
            productsInfoAmount.put(productDTO, curr_hash_map.get(product));
        }
        return productsInfoAmount;
    }

    public synchronized ProductDTO getProductInfo(String productName) throws Exception {
        //TODO: do we allow return info about products with amount == 0 ?????
        if(!containsProduct(productName))
            throw new Exception(""+productName+"product does not exist in this store!");

        productName = productName.strip().toLowerCase();
        return stockProducts.get(productName).keys().nextElement().getProductInfo();
    }

    public synchronized Product getProduct(String productName) throws Exception {
        //TODO: do we allow return info about products with amount == 0 ?????
        if(!containsProduct(productName))
            throw new Exception(""+productName+"product does not exist in this store!");

        productName = productName.strip().toLowerCase();
        return stockProducts.get(productName).keys().nextElement();
    }


    public synchronized Category getCategory(String categoryName) throws Exception {
        assertContainsCategory(categoryName);
        categoryName = categoryName.strip().toLowerCase();
        return stockCategories.get(categoryName);
    }

    public synchronized Product getProductWithAmount(String productName, Integer amount) throws Exception {

        productName = productName.strip().toLowerCase();

        //TODO: do we allow return info about products with amount == 0 ?????
        if(!containsProduct(productName))
            throw new Exception(""+productName+" product does not exist in this store!");

        Integer currentProductAmount = ((Integer)stockProducts.get(productName).values().toArray()[0]);
        if( currentProductAmount < amount)
            throw new Exception(""+productName+" have only "+ currentProductAmount +" amount in stock!");

        if(amount < 0)
            throw new Exception("The amount must be positive, it can't be " + amount);


        return stockProducts.get(productName).keys().nextElement();
    }

    public synchronized boolean containsProduct(String productName) throws Exception {
        if(productName == null || productName.isBlank())
            throw new Exception("productName is null or empty!");

        productName = productName.strip().toLowerCase();
        if(!stockProducts.containsKey(productName))
            return false;

        return true;
    }

    public synchronized boolean containsCategory(String categoryName) throws Exception {
        if(categoryName == null || categoryName.isBlank())
            throw new Exception("categoryName is null or empty!");

        categoryName = categoryName.strip().toLowerCase();
        if(!stockCategories.containsKey(categoryName))
            return false;

        return true;
    }

    public synchronized List<ProductDTO> getProductsInfoByCategory(String categoryName) throws Exception {

        //TODO: do we allow return info about products with amount == 0 ?????
        if(!containsCategory(categoryName))
            throw new Exception(""+categoryName+"category does not exist in this store!");

        categoryName = categoryName.strip().toLowerCase();

        return stockCategories.get(categoryName).getProductsInfo();

    }

    public synchronized boolean containsKeyWord(String keyword) throws Exception {
        if(keyword == null || keyword.isBlank())
            throw new Exception("key word  is null or empty!");
        return true;
    }


    public synchronized List<ProductDTO> getProductInfoFromMarketByKeyword(String keyword) throws Exception {
        keyword = keyword.strip().toLowerCase();
        List<ProductDTO> filteredProductsByKeyWord = new LinkedList<>();
        for(String productName : stockProducts.keySet()){
            if(productName.contains(keyword)){
                filteredProductsByKeyWord.add(getProductInfo(productName));
            }
        }
        return filteredProductsByKeyWord;

    }

    //Currently added for tests:
    public synchronized void addToStockProducts(String productName, Product product, int amount){
        ConcurrentHashMap<Product,Integer> hash = new ConcurrentHashMap<Product, Integer>();
        hash.put(product, amount);
        this.stockProducts.put(productName, hash);
    }

    public synchronized void addToStockCategory(String categoryName, Category category){
        this.stockCategories.put(categoryName, category);
    }

    public synchronized int getCategoriesSize(){
        return this.stockCategories.size();
    }

    public synchronized int getProductAmount(String productName, Product product){
        return this.stockProducts.get(productName).get(product);
    }
    public String getStoreName(){
        return stockName;
    }


    public synchronized boolean removeBagAmountFromStock(ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent) throws Exception {

        for(String productName: bagContent.keySet()){
            if(!stockProducts.containsKey(productName))
                throw new Exception(productName +" product was removed stock");
            Product product = bagContent.get(productName).keys().nextElement();
            int stockAmount = stockProducts.get(productName).get(product);
            if(bagContent.get(productName).get(product) > stockAmount)
                throw new Exception("stock only have "+stockAmount+ "for product "+productName);
        }
        int counter = 6;
        String msg = "the products: [" ;
        for(String productName: bagContent.keySet()){
            Product product = bagContent.get(productName).keys().nextElement();
            int stockAmount = stockProducts.get(productName).get(product);
            int bagAmount = bagContent.get(productName).get(product);
            stockProducts.get(productName).put(product, stockAmount-bagAmount);
            if(counter == 0){
                counter = 6;
                msg += "]\n";
            }
            msg+="[ " + productName+ ", ";
            counter--;
        }
        if(counter!=0) msg+="]";
        msg+= " from the stock: " + stockName;
        NotificationService.getInstance().notify(stockName,msg,NotificationType.productBought);
        return true;
    }
    public synchronized boolean replaceBagAmountToStock(ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent) throws Exception {
        for(String productName: bagContent.keySet()){
            if(stockProducts.containsKey(productName)){
                Product product = bagContent.get(productName).keys().nextElement();
                int stockAmount = stockProducts.get(productName).get(product);
                int bagAmount = bagContent.get(productName).get(product);
                stockProducts.get(productName).put(product, stockAmount+bagAmount);
            }
        }
        return true;
    }


    public Integer getProductAmount(String productName) throws Exception {
        productName = productName.strip().toLowerCase();

        //TODO: do we allow return info about products with amount == 0 ?????
        if(!containsProduct(productName))
            throw new Exception(""+productName+" product does not exist in this store!");

        return  ((Integer)stockProducts.get(productName).values().toArray()[0]);
    }

    public void defineStockProductsMap() {
        stockProducts = new ConcurrentHashMap<>();
        for (Product p: productAmount.keySet()){
            ConcurrentHashMap<Product,Integer> product_amount = new ConcurrentHashMap<>();
            product_amount.put(p,productAmount.get(p));
            stockProducts.put(p.getName(),product_amount);
        }
    }

    public Map<Product, Integer> getProductAmount() {
        return productAmount;
    }

    public Map<String, Category> getStockCategories() {
        return stockCategories;
    }

    public void updateStock(Stock stock) {
        this.stockName = stock.stockName;
        this.stockProducts = new ConcurrentHashMap<>();
        this.productAmount = stock.productAmount;
        this.stockCategories = stock.stockCategories;
        for (Product product: productAmount.keySet()){
            ConcurrentHashMap<Product,Integer> product_amount = new ConcurrentHashMap<>();
            product_amount.put(product,productAmount.get(product));
            stockProducts.put(product.getName(),product_amount);
        }
        this.isLoaded = true;
    }
}
