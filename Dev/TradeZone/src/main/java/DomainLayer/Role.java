package DomainLayer;

import java.util.concurrent.ConcurrentHashMap;

public class Role {


    RoleEnum myRole;

    private ConcurrentHashMap<String, AbstractStoreOwner> myBossesForStores;
    private ConcurrentHashMap<String, Store> responsibleForStores;
    Member member;

    public String getUserName(){
        return member.getUserName();
    }


    public boolean haveStore(String storeName){
        // check if null or empty
        storeName = storeName.strip().toLowerCase();
        return responsibleForStores.containsKey(storeName);
    }
}
