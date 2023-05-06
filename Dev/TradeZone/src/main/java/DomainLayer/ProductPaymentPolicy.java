package DomainLayer;

import java.util.concurrent.ConcurrentHashMap;

public class ProductPaymentPolicy implements PaymentPolicy{
    @Override
    public boolean validatePolicy(User user, ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent) {
        return false;
    }
}
