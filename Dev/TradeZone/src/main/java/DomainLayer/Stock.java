package DomainLayer;

import DTO.ProductDTO;
import DataAccessLayer.CompositeKeys.CategoryId;
import DataAccessLayer.CompositeKeys.ProductId;
import DataAccessLayer.Controller.StoreMapper;
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
    Store store;
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

    public Stock(Store store){
        this.store = store;
        this.stockName = store.getStoreName();
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

    public void loadStock() throws Exception {
        if (!isLoaded){
            StoreMapper.getInstance().getStock(stockName);
            isLoaded = true;
        }
    }

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
            StoreMapper.getInstance().insertCategory(new CategoryId(category,getStoreName()),productCategory);
            if (Market.dbFlag)
                DALService.categoryRepository.save(productCategory);
            stockCategories.put(category,productCategory);
            if (Market.dbFlag)
                DALService.stockRepository.save(this);
        }
        productCategory = stockCategories.get(category);
        Product product = new Product(nameProduct,stockName,productCategory,price,description);
        StoreMapper.getInstance().insertProduct(new ProductId(nameProduct,stockName),product);
        productCategory.putProductInCategory(product);
        ConcurrentHashMap<Product,Integer> productAmount = new ConcurrentHashMap<Product, Integer>();
        this.productAmount.put(product,amount);
        productAmount.put(product,amount);
        stockProducts.put(nameProduct,productAmount);
        if (Market.dbFlag)
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
        StoreMapper.getInstance().removeProduct(new ProductId(productName,stockName));
        if (Market.dbFlag)
            DALService.removeProduct(product,this);
        return true;
    }

    public synchronized boolean updateProductDescription(String productName, String newProductDescription) throws Exception {
//        productName = productName.strip().toLowerCase();
//        if(!stockProducts.containsKey(productName))
//            throw new Exception("can't remove product from stock : productName "+ productName+" is not in the stock!");
        if (!containsProduct(productName)){
            throw new Exception("can't update product description : productName "+ productName+" is not in the stock!");
        }
        productName = productName.strip().toLowerCase();
        Product product = stockProducts.get(productName).keys().nextElement();
        product.setDescription(newProductDescription);
        return true;
    }

    public synchronized boolean updateProductAmount(String productName, int newAmount) throws Exception {
        //productName = productName.strip().toLowerCase();
//        if(!stockProducts.containsKey(productName))
//            throw new Exception("can't remove product from stock : productName "+ productName+" is not in the stock!");
        if (!containsProduct(productName)){
            throw new Exception("can't update product amount : productName "+ productName+" is not in the stock!");
        }
        productName = productName.strip().toLowerCase();
        Product product = stockProducts.get(productName).keys().nextElement();
        Integer currentProductAmount = ((Integer)stockProducts.get(productName).values().toArray()[0]);
        if (currentProductAmount == -1)
            currentProductAmount = StoreMapper.getInstance().updateProductAmountInStock(productName,stockName);

//        if(stockProducts.get(productName).get(product) == newAmount)
//            throw new Exception("the amount of the product equals to the new amount");
        if (currentProductAmount == newAmount){
            throw new Exception("the amount of the product equals to the new amount");
        }
        stockProducts.remove(productName);
        ConcurrentHashMap<Product,Integer> productAmount = new ConcurrentHashMap<Product, Integer>();
        this.productAmount.put(product,newAmount);
        productAmount.put(product,newAmount);
        stockProducts.put(productName,productAmount);
        if (Market.dbFlag)
            DALService.stockRepository.save(this);
        return true;
    }


    public synchronized boolean updateProductPrice(String productName, Double newPrice) throws Exception {
//        productName = productName.strip().toLowerCase();
//        if(!stockProducts.containsKey(productName))
//            throw new Exception("can't remove product from stock : productName "+ productName+" is not in the stock!");
        if (!containsProduct(productName)){
            throw new Exception("can't update product price : productName "+ productName+" is not in the stock!");
        }
        productName = productName.strip().toLowerCase();
        Product product = stockProducts.get(productName).keys().nextElement();
        if(Objects.equals(product.getPrice(), newPrice))
            throw new Exception("the price of the product equals to the new price");
        product.setPrice(newPrice);
        return true;
    }

    public synchronized Map<ProductDTO, Integer> getProductsInfoAmount() throws Exception {
        loadStock();
        Collection<ConcurrentHashMap<Product,Integer>> currentStockProducts = stockProducts.values();
        Map<ProductDTO, Integer> productsInfoAmount = new LinkedHashMap<>();
        for(ConcurrentHashMap<Product,Integer> curr_hash_map: currentStockProducts) {
            Product product = curr_hash_map.keys().nextElement();
            ProductDTO productDTO = curr_hash_map.keys().nextElement().getProductInfo(store.getProductDiscountPolicies(product.getName()));
            productsInfoAmount.put(productDTO, curr_hash_map.get(product));
        }
        return productsInfoAmount;
    }

    public synchronized ProductDTO getProductInfo(String productName, List<String> productDiscountPolicies) throws Exception {
        if(!containsProduct(productName))
            throw new Exception(""+productName+"product does not exist in this store!");

        productName = productName.strip().toLowerCase();
        return stockProducts.get(productName).keys().nextElement().getProductInfo(productDiscountPolicies);
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
        //the convince is that if the product amount in stock is -1 then the actual amount should be loaded:
        Integer currentProductAmount = ((Integer)stockProducts.get(productName).values().toArray()[0]);
        if (currentProductAmount == -1)
            currentProductAmount = StoreMapper.getInstance().updateProductAmountInStock(productName,stockName);

        if( currentProductAmount < amount)
            throw new Exception(""+productName+" have only "+ currentProductAmount +" amount in stock!");//todo: need to send another msg without telling the amount of the product in the store

        if(amount < 0)
            throw new Exception("The amount must be positive, it can't be " + amount);


        return stockProducts.get(productName).keys().nextElement();
    }

    public synchronized boolean containsProduct(String productName) throws Exception {
        if(productName == null || productName.isBlank())
            throw new Exception("productName is null or empty!");

        productName = productName.strip().toLowerCase();
        if(!stockProducts.containsKey(productName)) {
            //todo: needs repair
            Product product = StoreMapper.getInstance().getProduct(new ProductId(productName,stockName));
            //Product product = StoreMapper.getInstance().getProductWithoutLoading(new ProductId(productName,stockName));
            if (product == null){
                return false;
            }else{
                ConcurrentHashMap<Product,Integer> product_amount = new ConcurrentHashMap<>();
                product_amount.put(product,-1);
                stockProducts.put(productName,product_amount);
                productAmount.put(product,-1);
                return true;
            }
        }
        return true;
    }

    public synchronized boolean containsCategory(String categoryName) throws Exception {
        if(categoryName == null || categoryName.isBlank())
            throw new Exception("categoryName is null or empty!");

        categoryName = categoryName.strip().toLowerCase();
        if(!stockCategories.containsKey(categoryName)) {
            Category category = StoreMapper.getInstance().getCategory(new CategoryId(categoryName,stockName));
            if (category == null){
                return false;
            }else{
                stockCategories.put(categoryName,category);
                return true;
            }
        }

        return true;
    }

    public synchronized List<ProductDTO> getProductsInfoByCategory(String categoryName) throws Exception {

        //TODO: do we allow return info about products with amount == 0 ?????
        if(!containsCategory(categoryName))
            throw new Exception(""+categoryName+"category does not exist in this store!");

        categoryName = categoryName.strip().toLowerCase();

        List<Product> productList =  stockCategories.get(categoryName).getProducts();
        List<ProductDTO> productDTOList = new ArrayList<>();
        for(Product product: productList)
            productDTOList.add(getProductInfo(product.getName(), store.getProductDiscountPolicies(product.getName())));

        return productDTOList;

    }

    public synchronized boolean containsKeyWord(String keyword) throws Exception {
        if(keyword == null || keyword.isBlank())
            throw new Exception("key word  is null or empty!");
        return true;
    }


    public synchronized List<ProductDTO> getProductInfoFromMarketByKeyword(String keyword) throws Exception {
        containsKeyWord(keyword);
        keyword = keyword.strip().toLowerCase();
        loadStock();
        List<ProductDTO> filteredProductsByKeyWord = new LinkedList<>();
        for(String productName : stockProducts.keySet()){
            if(productName.contains(keyword)){
                filteredProductsByKeyWord.add(getProductInfo(productName, store.getProductDiscountPolicies(productName)));
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
        return store.getStoreName();
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
        msg+= " from the store: " + store.getStoreName();
        NotificationService.getInstance().notify(store.getStoreName(),msg,NotificationType.productBought);
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
        Integer currentProductAmount = ((Integer)stockProducts.get(productName).values().toArray()[0]);
        if (currentProductAmount == -1)
            currentProductAmount = StoreMapper.getInstance().updateProductAmountInStock(productName,stockName);
        return currentProductAmount;
        //return  ((Integer)stockProducts.get(productName).values().toArray()[0]);
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

    public void setProductAmountAfterLoadingAmount(Product product,String productName,int amount){
        productAmount.put(product,amount);
        stockProducts.get(productName).put(product,amount);
    }
}
