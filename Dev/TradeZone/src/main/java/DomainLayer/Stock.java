package DomainLayer;

import DTO.ProductDTO;
import DataAccessLayer.DALService;
import DataAccessLayer.DTO.DTOStock;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Stock {

    private String stockName;
    private ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> stockProducts;
    private ConcurrentHashMap<String,Category> stockCategories;

    public Stock(String stockName){
        this.stockName = stockName;
        stockProducts =new ConcurrentHashMap<>();
        stockCategories = new ConcurrentHashMap<>();
    }

    public Stock(String stockName, ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> stockProducts, ConcurrentHashMap<String, Category> stockCategories) {
        this.stockName = stockName;
        this.stockProducts = stockProducts;
        this.stockCategories = stockCategories;
    }

    public DTOStock getStockDTO(){
        return new DTOStock(stockName);
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

    public synchronized boolean addNewProductToStock(String nameProduct,String category, Double price, String description, Integer amount) throws Exception {
        assertDoesNotContainProduct(nameProduct);
        assertStringIsNotNullOrBlank(category);
        assertStringIsNotNullOrBlank(description);

        nameProduct=nameProduct.strip().toLowerCase();
        category = category.strip().toLowerCase();
        boolean categoryflag = false;
        if(!containsCategory(category)) {
            stockCategories.put(category, new Category(category, this.getStoreName()));
            categoryflag = true;
        }

        Product product = new Product(nameProduct,this.getStoreName(),stockCategories.get(category),price,description);
        stockCategories.get(category).putProductInCategory(product);
        ConcurrentHashMap<Product,Integer> productAmount = new ConcurrentHashMap<Product, Integer>();
        productAmount.put(product,amount);
        stockProducts.put(nameProduct,productAmount);
        DALService.addProduct(stockCategories.get(category),stockName,getStoreName(),product,amount,categoryflag);
        //DALService.addCategory(stockCategories.get(category),this.stockName,this.getStoreName());
        return true;
    }



    public synchronized boolean removeProductFromStock(String productName) throws Exception {
        assertContainsProduct(productName);
        productName=productName.strip().toLowerCase();
        Product product = stockProducts.get(productName).keys().nextElement();
        product.removeFromAllCategories();
        stockProducts.remove(productName);
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
        productAmount.put(product,newAmount);
        stockProducts.put(productName, productAmount);
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

    public synchronized List<ProductDTO> getProductsInfo(){
        Collection<ConcurrentHashMap<Product,Integer>> currentStockProducts = stockProducts.values();
        List<ProductDTO> productsInfo = new ArrayList<>();
        for(ConcurrentHashMap<Product,Integer> curr_hash_map: currentStockProducts)
            productsInfo.add(curr_hash_map.keys().nextElement().getProductInfo());

        return productsInfo;
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
        return this.stockName;
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
        msg+= " from the store: " + this.stockName;
        NotificationService.getInstance().notify(this.stockName,msg,NotificationType.productBought);
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
}
