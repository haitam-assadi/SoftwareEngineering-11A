package DomainLayer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractStoreOwner extends Role{

    private ConcurrentHashMap<String, List<StoreOwner>> appointedOwners;
    private ConcurrentHashMap<String, List<StoreManager>> appointedManagers;
}
