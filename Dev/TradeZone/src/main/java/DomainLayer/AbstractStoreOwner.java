package DomainLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractStoreOwner extends Role{

    private ConcurrentHashMap<String, List<StoreOwner>> appointedOwners;
    private ConcurrentHashMap<String, List<StoreManager>> appointedManagers;

    public AbstractStoreOwner(Member member) {
        super(member);
        appointedOwners = new ConcurrentHashMap<>();
        appointedManagers = new ConcurrentHashMap<>();
    }


    public boolean appointOtherMemberAsStoreOwner(Store store, Member otherMember) throws Exception {
        String storeName = store.getStoreName();
        otherMember.appointMemberAsStoreOwner(store, this);
        appointedOwners.putIfAbsent(storeName, new ArrayList<>());
        appointedOwners.get(storeName).add(otherMember.getStoreOwner());
        return true;
    }

    public boolean appointOtherMemberAsStoreManager(Store store, Member otherMember) throws Exception {
        String storeName = store.getStoreName();
        otherMember.appointMemberAsStoreManager(store, this);
        appointedManagers.putIfAbsent(storeName, new ArrayList<>());
        appointedManagers.get(storeName).add(otherMember.getStoreManager());
        return true;
    }

    public void removeOwnerByHisAppointer(Store store, Member otherMember, StoreOwner otherOwner) throws Exception {
        String storeName = store.getStoreName();
        if(appointedOwners.containsKey(storeName)){
            if(!appointedOwners.get(storeName).contains(otherOwner)) throw new Exception(""+otherMember.getUserName()+" is not owner for "+storeName + " by "+getUserName());
        if(!store.isAlreadyStoreOwner(otherMember.getUserName())) throw new Exception(""+otherMember.getUserName()+" is not owner for "+storeName);
        }else{
            otherOwner.removeOwnerByHisAppointer(store,this);
            appointedOwners.get(storeName).remove(otherOwner);
            store.removeOwner(otherMember.getUserName());
            String msg = "you have been removed from being store owner for " + storeName+ " by " + getUserName();
            NotificationService.getInstance().notifySingle(storeName,getUserName(),msg,NotificationType.RemovedFromOwningStore);
            NotificationService.getInstance().removeMember(storeName,otherMember);
        }
    }
}
