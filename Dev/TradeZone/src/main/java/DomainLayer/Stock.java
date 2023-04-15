package DomainLayer;

import DomainLayer.DTO.ProductDTO;
import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Stock {
    private ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> stockProducts;
    private ConcurrentHashMap<String,Category> stockCategories;

    public Stock(){
        stockProducts =new ConcurrentHashMap<>();
        stockCategories = new ConcurrentHashMap<>();
    }
    public boolean addNewProductToStock(String nameProduct,String category, Double price, String description, Integer amount) throws Exception {
        if(stockProducts.containsKey(nameProduct))
            throw new Exception("can't add new product to stock : productName "+ nameProduct+" is in the stock!");
        if(!stockCategories.containsKey(category))
            stockCategories.put(category, new Category(category));
        Product product = new Product(nameProduct,this, category,price,description);
        stockCategories.get(category).putProductInCategory(product);
        ConcurrentHashMap<Product,Integer> productAmount = new ConcurrentHashMap<Product, Integer>();
        productAmount.put(product,amount);
        stockProducts.put(nameProduct,productAmount);
        return true;
    }

    public boolean removeProductFromStock(String productName) throws Exception {
        if(!stockProducts.containsKey(productName))
           throw new Exception("can't remove product from stock : productName "+ productName+" is not in the stock!");
        stockProducts.remove(productName);
        return true;
    }

    public boolean updateProductDescription(String productName, String newProductDescription) throws Exception {
        if(!stockProducts.containsKey(productName))
            throw new Exception("can't remove product from stock : productName "+ productName+" is not in the stock!");
        Product product = stockProducts.get(productName).keys().nextElement();
        product.setDescription(newProductDescription);
        stockCategories.get(product.getCategory()).updateProductDescriptionInCategory(product);
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
        if(product.getPrice() == newPrice)
            throw new Exception("the price of the product equals to the new price");
        product.setPrice(newPrice);
        stockCategories.get(product.getCategory()).updateProductDescriptionInCategory(product);
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

    public boolean containsProduct(String productName) throws Exception {
        if(productName == null || productName == "")
            throw new Exception("productName is null or empty!");

        productName = productName.strip().toLowerCase();
        if(!stockProducts.containsKey(productName))
            return false;

        return true;
    }

    public boolean containsCategory(String categoryName) throws Exception {
        if(categoryName == null || categoryName == "")
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
}
