package DomainLayer;

import DomainLayer.DTO.ProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Category {

    private Stock stock;
    private String categoryName;
    private ConcurrentHashMap<String,Product> categoryProducts;

    public Category (String categoryName, Stock stock){
        this.categoryName = categoryName;
        this.stock = stock;
        categoryProducts = new ConcurrentHashMap<>();
    }

    public boolean putProductInCategory(Product product) throws Exception {
        if(categoryProducts.containsKey(product.getName()))
            throw new Exception("the product is already in the category");
        categoryProducts.put(product.getName(), product);
        return true;
    }

    public List<ProductDTO> getProductsInfo(){
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product:categoryProducts.values())
            productDTOList.add(product.getProductInfo());

        return productDTOList;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public boolean containsProduct(String productName) throws Exception {
        if(productName == null || productName.isBlank())
            throw new Exception("productName is null or empty!");

        productName = productName.strip().toLowerCase();
        if(!categoryProducts.containsKey(productName))
            return false;

        return true;
    }
    public void assertContainsProduct(String productName) throws Exception {

        if(!containsProduct(productName))
            throw new Exception("stock does not contain this product "+productName);
    }
    public boolean removeProduct(String productName) throws Exception {
        assertContainsProduct(productName);
        productName = productName.strip().toLowerCase();
        categoryProducts.remove(productName);
        return true;
    }
}
