package DomainLayer;

import DomainLayer.DTO.ProductDTO;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Stock {
    private ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> stockProducts;
    private ConcurrentHashMap<String,Category> stockCategories;

    public Stock(){
        stockProducts =new ConcurrentHashMap<>();
        stockCategories = new ConcurrentHashMap<>();
    }
    public boolean addNewProductToStock(String nameProduct,String category, Double price, String details, Integer amount) throws Exception {
        if(stockProducts.containsKey(nameProduct))
            throw new Exception("can't add new product to stock : productName "+ nameProduct+" is in the stock!");
        if(!stockCategories.containsKey(category))
            stockCategories.put(category, new Category(category));
        Product product = new Product(nameProduct,category,price,details);
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
}
