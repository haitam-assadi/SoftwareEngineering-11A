package DomainLayer;

import DTO.ProductDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Stock {
    private ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> stockProducts;
    private ConcurrentHashMap<String,Category> stockCategories;

    public Stock(){
        stockProducts =new ConcurrentHashMap<>();
        stockCategories = new ConcurrentHashMap<>();
    }

    public void assertStringIsNotNullOrBlank(String st) throws Exception {
        if(st==null || st.isBlank())
            throw new Exception("string is null or empty");
    }
    public void assertContainsProduct(String productName) throws Exception {

        if(!containsProduct(productName))
            throw new Exception("stock does not contain this product "+productName);
    }
    public void assertDoesNotContainProduct(String productName) throws Exception {
        if(containsProduct(productName))
            throw new Exception("stock does already contains this product "+productName);
    }

    public boolean addNewProductToStock(String nameProduct,String category, Double price, String description, Integer amount) throws Exception {
        assertDoesNotContainProduct(nameProduct);
        assertStringIsNotNullOrBlank(category);
        assertStringIsNotNullOrBlank(description);

        nameProduct=nameProduct.strip().toLowerCase();
        category = category.strip().toLowerCase();

        if(!containsCategory(category))
            stockCategories.put(category, new Category(category, this));

        Product product = new Product(nameProduct,this,stockCategories.get(category),price,description);
        stockCategories.get(category).putProductInCategory(product);
        ConcurrentHashMap<Product,Integer> productAmount = new ConcurrentHashMap<Product, Integer>();
        productAmount.put(product,amount);
        stockProducts.put(nameProduct,productAmount);
        return true;
    }

    public boolean removeProductFromStock(String productName) throws Exception {
        assertContainsProduct(productName);
        productName=productName.strip().toLowerCase();
        Product product = stockProducts.get(productName).keys().nextElement();
        product.removeFromAllCategories();
        stockProducts.remove(productName);
        return true;
    }

    public boolean updateProductDescription(String productName, String newProductDescription) throws Exception {
        if(!stockProducts.containsKey(productName))
            throw new Exception("can't remove product from stock : productName "+ productName+" is not in the stock!");
        Product product = stockProducts.get(productName).keys().nextElement();
        product.setDescription(newProductDescription);
        return true;
    }

    public boolean updateProductAmount(String productName, Integer newAmount) throws Exception {
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


    public boolean updateProductPrice(String productName, Double newPrice) throws Exception {
        if(!stockProducts.containsKey(productName))
            throw new Exception("can't remove product from stock : productName "+ productName+" is not in the stock!");
        Product product = stockProducts.get(productName).keys().nextElement();
        if(Objects.equals(product.getPrice(), newPrice))
            throw new Exception("the price of the product equals to the new price");
        product.setPrice(newPrice);
        return true;


    }

    public List<ProductDTO> getProductsInfo(){
        Collection<ConcurrentHashMap<Product,Integer>> currentStockProducts = stockProducts.values();
        List<ProductDTO> productsInfo = new ArrayList<>();
        for(ConcurrentHashMap<Product,Integer> curr_hash_map: currentStockProducts)
            productsInfo.add(curr_hash_map.keys().nextElement().getProductInfo());

        return productsInfo;
    }

    public ProductDTO getProductInfo(String productName) throws Exception {
        //TODO: do we allow return info about products with amount == 0 ?????
        if(!containsProduct(productName))
            throw new Exception(""+productName+"product does not exist in this store!");

        productName = productName.strip().toLowerCase();
        return stockProducts.get(productName).keys().nextElement().getProductInfo();
    }

    public Product getProduct(String productName) throws Exception {
        //TODO: do we allow return info about products with amount == 0 ?????
        if(!containsProduct(productName))
            throw new Exception(""+productName+"product does not exist in this store!");

        productName = productName.strip().toLowerCase();
        return stockProducts.get(productName).keys().nextElement();
    }

    public Product getProductWithAmount(String productName, Integer amount) throws Exception {
        //TODO: do we allow return info about products with amount == 0 ?????
        if(!containsProduct(productName))
            throw new Exception(""+productName+" product does not exist in this store!");

        Integer currentProductAmount = ((Integer)stockProducts.get(productName).values().toArray()[0]);
        if( currentProductAmount < amount)
            throw new Exception(""+productName+" have only "+ currentProductAmount +" amount in stock!");

        productName = productName.strip().toLowerCase();
        return stockProducts.get(productName).keys().nextElement();
    }

    public boolean containsProduct(String productName) throws Exception {
        if(productName == null || productName.isBlank())
            throw new Exception("productName is null or empty!");

        productName = productName.strip().toLowerCase();
        if(!stockProducts.containsKey(productName))
            return false;

        return true;
    }

    public boolean containsCategory(String categoryName) throws Exception {
        if(categoryName == null || categoryName.isBlank())
            throw new Exception("categoryName is null or empty!");

        categoryName = categoryName.strip().toLowerCase();
        if(!stockCategories.containsKey(categoryName))
            return false;

        return true;
    }


    public List<ProductDTO> getProductsInfoByCategory(String categoryName) throws Exception {

        //TODO: do we allow return info about products with amount == 0 ?????
        if(!containsCategory(categoryName))
            throw new Exception(""+categoryName+"category does not exist in this store!");

        categoryName = categoryName.strip().toLowerCase();

        return stockCategories.get(categoryName).getProductsInfo();

    }

    //Currently added for tests:
    public void addToStockProducts(String productName, Product product, int amount){
        ConcurrentHashMap<Product,Integer> hash = new ConcurrentHashMap<Product, Integer>();
        hash.put(product, amount);
        this.stockProducts.put(productName, hash);
    }

    public void addToStockCategory(String categoryName, Category category){
        this.stockCategories.put(categoryName, category);
    }

    public int getCategoriesSize(){
        return this.stockCategories.size();
    }

    public int getProductAmount(String productName, Product product){
        return this.stockProducts.get(productName).get(product);
    }
}
