package DataAccessLayer.Controller;

import DomainLayer.Product;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ProductMapper {
    private Map<String, Product> products;
    private Set<String> productsNames;

    public static ProductMapper instance = null;

    private ProductMapper(){
        products = new ConcurrentHashMap<>();
        productsNames = new HashSet<>();
    }

    public static ProductMapper getInstance(){
        if (instance == null){
            instance = new ProductMapper();
        }
        return instance;
    }


}
