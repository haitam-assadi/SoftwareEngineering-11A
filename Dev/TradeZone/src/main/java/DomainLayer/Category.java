package DomainLayer;

import DomainLayer.DTO.ProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Category {

    private Stock stock;
    private String categoryName;
    private ConcurrentHashMap<String,Product> categoryProducts;

    public Category (String categoryName){
        this.categoryName = categoryName;
        categoryProducts = new ConcurrentHashMap<>();
    }

    public boolean putProductInCategory(Product product) throws Exception {
        if(categoryProducts.containsKey(product.getName()))
            throw new Exception("the product is already in the category");
        categoryProducts.put(product.getName(), product);
        return true;
    }

    public boolean updateProductDescriptionInCategory(Product product) throws Exception {
        if(!categoryProducts.containsKey(product.getName()))
            throw new Exception("the product is not in the category");
        categoryProducts.remove(product.getName());
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
}
