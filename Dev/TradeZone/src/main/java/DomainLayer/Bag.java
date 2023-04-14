package DomainLayer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Bag {

    Cart cart;
    Store storeBag;
    private ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent;
}
