package DomainLayer.DiscountPolicies;

import DomainLayer.Product;

import java.util.concurrent.ConcurrentHashMap;

public interface DiscountPolicy {

    Double calculateDiscount(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent);
}
