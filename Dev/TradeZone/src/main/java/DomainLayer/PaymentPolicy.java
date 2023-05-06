package DomainLayer;

import java.util.concurrent.ConcurrentHashMap;

public interface PaymentPolicy {

    boolean validatePolicy(User user, ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent) throws Exception;
}
