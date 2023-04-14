package DomainLayer;

import java.util.concurrent.ConcurrentHashMap;

public class StoreManager extends Role{

    private AbstractStoreOwner myBoss;
    private ConcurrentHashMap<String, Store> managedStores;
}
