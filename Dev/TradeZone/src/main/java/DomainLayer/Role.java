package DomainLayer;

import java.util.concurrent.ConcurrentHashMap;

public abstract class Role {


    RoleEnum myRole;

    private ConcurrentHashMap<String, AbstractStoreOwner> myBossesForStores;
    private ConcurrentHashMap<String, Store> responsibleForStores;
    Member member;

    public Role(Member member){
        this.member = member;
    }
    public String getUserName(){
        return member.getUserName();
    }


    public boolean appointMemberAsStoreOwner(Store store, AbstractStoreOwner myBoss) throws Exception {
        String storeName = store.getStoreName();
        if(responsibleForStores.containsKey(storeName))
            throw new Exception(""+getUserName()+" is already owner for this store");

        responsibleForStores.put(storeName, store);
        myBossesForStores.put(storeName, myBoss);
        return true;
    }




    public boolean haveStore(String storeName){
        // check if null or empty
        storeName = storeName.strip().toLowerCase();
        return responsibleForStores.containsKey(storeName);
    }
}
