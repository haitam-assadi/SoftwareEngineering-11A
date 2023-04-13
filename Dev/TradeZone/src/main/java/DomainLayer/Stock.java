package DomainLayer;

import DomainLayer.DTO.ProductDTO;

import java.util.concurrent.ConcurrentHashMap;

public class Stock {
    private ConcurrentHashMap<String, ProductDTO> stockProducts;

    public Stock(){
        stockProducts =new ConcurrentHashMap<>();
    }
    public boolean addNewProductToStock(ProductDTO newProduct, Integer amount) throws Exception {
        if(stockProducts.containsKey(newProduct.getName()))
            throw new Exception("can't add new product to stock : productName "+ newProduct.getName()+" is in the stock!");
        stockProducts.put(newProduct.getName(), newProduct);
        //TODO: TO MAKE THE PRODUCT THE FIELDS OF PRODUCT

        return true;
    }
}
