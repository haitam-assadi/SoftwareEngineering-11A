package DomainLayer;

import DomainLayer.DTO.ProductDTO;
import DomainLayer.DTO.StoreDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Stock {
    private ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> stockProducts;
    private ConcurrentHashMap<String,Category> stockCategories;

    public Stock(){
        stockProducts =new ConcurrentHashMap<>();
    }
    public boolean addNewProductToStock(String nameProduct,String category, Integer price, String details, Integer amount) throws Exception {
        /*
        if(stockProducts.containsKey(nameProduct))
            throw new Exception("can't add new product to stock : productName "+ nameProduct+" is in the stock!");
        if(!stockCategories.containsKey(category))
            stockCategories.put(category, new Category(category));
        Product product = new Product(nameProduct,category,price,details,amount);
        stockCategories.get(category).putProductInCategory(product);
        stockProducts.put(nameProduct,product);

         */
        return true;
    }

    public boolean removeProductFromStock(String productName) throws Exception {
        if(!stockProducts.containsKey(productName))
           throw new Exception("can't remove product from stock : productName "+ productName+" is not in the stock!");
        stockProducts.remove(productName);
        return true;
    }

    public boolean updateProductDetails(String productName, String newProductDetails) throws Exception {
        /*
        if(!stockProducts.containsKey(productName))
            throw new Exception("can't remove product from stock : productName "+ productName+" is not in the stock!");
        Product product = stockProducts.get(productName).keys().nextElement();
        product.setDetails(newProductDetails);
        stockProducts.remove(productName);
        stockProducts.put(productName, product);
        stockCategories.get(product.getCategory()).updateProductDetailsInCategory(product);

         */
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
        isProduct(productName);
        productName = productName.strip().toLowerCase();
        return stockProducts.get(productName).keys().nextElement().getProductInfo();
    }

    public void isProduct(String productName) throws Exception {
        if(productName == null || productName == "")
            throw new Exception("productName is null or empty!");

        productName = productName.strip().toLowerCase();
        if(! stockProducts.containsKey(productName))
            throw new Exception(""+productName+"product does not exist in this store!");
    }
}
