package DomainLayer;

public class StoreOwner extends AbstractStoreOwner{


    public StoreOwner(Member member) {
        super(member);
        this.myRole=RoleEnum.StoreOwner;
        
    }


    public boolean removeMemberAsStoreOwner(Store store, AbstractStoreOwner myBoss) throws Exception {
        String storeName = store.getStoreName();
        if(!store.isAlreadyStoreOwner(getUserName()))
            throw new Exception("member "+getUserName()+" is not store owner for store " + storeName);

        if(!responsibleForStores.containsKey(storeName))
            throw new Exception("member "+getUserName()+" is not store owner for store " + storeName);

        if(!myBossesForStores.containsKey(storeName))
            throw new Exception(""+getUserName()+" is founder for this store");

        if(!myBossesForStores.get(storeName).getUserName().equals(myBoss.getUserName()))
            throw new Exception(myBoss.getUserName()+" is not my boss for store "+ storeName);

        store.removeOwner(getUserName());
        myBossesForStores.remove(storeName);
        responsibleForStores.remove(storeName);

        if(appointedOwners.containsKey(storeName)){
            for(StoreOwner storeOwner: appointedOwners.get(storeName))
                storeOwner.removeMemberAsStoreOwner(store,this);
            appointedOwners.remove(storeName);
        }

        if(appointedManagers.containsKey(storeName)){
            for(StoreManager storeManager: appointedManagers.get(storeName))
                storeManager.removeMemberAsStoreManager(store,this);
            appointedManagers.remove(storeName);
        }

        return true;
    }
}
