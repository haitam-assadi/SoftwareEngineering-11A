package DomainLayer.DiscountPolicies;

import DomainLayer.Product;

import java.util.concurrent.ConcurrentHashMap;

public interface DiscountPolicy {

    Double calculateDiscount(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent);
    Double calculateDiscountForProduct(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent, String productName) throws Exception;

    boolean checkIfProductHaveDiscount(String productName) throws Exception;
}
