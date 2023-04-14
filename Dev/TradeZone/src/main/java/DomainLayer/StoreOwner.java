package DomainLayer;

import java.util.concurrent.ConcurrentHashMap;

public class StoreOwner extends AbstractStoreOwner{

    private AbstractStoreOwner myBoss;
    private ConcurrentHashMap<String, Store> ownedStores;

}
