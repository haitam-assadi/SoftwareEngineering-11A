package DomainLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractStoreOwner extends Role{

    private ConcurrentHashMap<String, List<StoreOwner>> appointedOwners;
    private ConcurrentHashMap<String, List<StoreManager>> appointedManagers;



    public boolean appointMemberAsStoreOwner(Store store, Member otherMember) throws Exception {
        String storeName = store.getStoreName();
        //TODO : CHECK IF Member is already owner
        otherMember.


        appointedOwners.putIfAbsent(storeName, new ArrayList<>());
        appointedOwners.get(storeName).add(otherMember);
    }
}
