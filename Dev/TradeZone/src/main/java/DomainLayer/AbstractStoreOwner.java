package DomainLayer;

import DataAccessLayer.DALService;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@MappedSuperclass
public class AbstractStoreOwner extends Role{

    @Transient
    protected ConcurrentHashMap<String, List<StoreOwner>> appointedOwners;
    @Transient
    protected ConcurrentHashMap<String, List<StoreManager>> appointedManagers;

    public AbstractStoreOwner(Member member) {
        super(member);
        appointedOwners = new ConcurrentHashMap<>();
        appointedManagers = new ConcurrentHashMap<>();
    }
    public AbstractStoreOwner(){}


    public boolean appointOtherMemberAsStoreOwner(Store store, Member otherMember) throws Exception {
        store.loadStore();
        String storeName = store.getStoreName();
        otherMember.appointMemberAsStoreOwner(store, this);
        appointedOwners.putIfAbsent(storeName, new ArrayList<>());
        appointedOwners.get(storeName).add(otherMember.getStoreOwner());
        return true;
    }

    public boolean appointOtherMemberAsStoreManager(Store store, Member otherMember) throws Exception {
        store.loadStore();
        String storeName = store.getStoreName();
        otherMember.appointMemberAsStoreManager(store, this);
        appointedManagers.putIfAbsent(storeName, new ArrayList<>());
        appointedManagers.get(storeName).add(otherMember.getStoreManager());
        return true;
    }

    public void removeOwnerByHisAppointer(Store store, Member otherMember, StoreOwner otherOwner) throws Exception {
        String storeName = store.getStoreName();
        //store.loadStore();
        if(appointedOwners.containsKey(storeName)){
            if(!appointedOwners.get(storeName).contains(otherOwner)) throw new Exception(""+otherMember.getUserName()+" is not owner for "+storeName + " by "+getUserName());
            if(!store.isAlreadyStoreOwner(otherMember.getUserName())) throw new Exception(""+otherMember.getUserName()+" is not owner for "+storeName);
            otherOwner.removeOwnerByHisAppointer(store,this);
            appointedOwners.get(storeName).remove(otherOwner);
            store.removeOwner(otherMember.getUserName());
            String msg = "you have been removed from being store owner for " + storeName+ " by " + getUserName();
            NotificationService.getInstance().notifySingle(storeName,getUserName(),msg,NotificationType.RemovedFromOwningStore);
            NotificationService.getInstance().removeRule(storeName,otherMember);
//            for (StoreOwner storeOwner: otherOwner.getAppointedOwners().get(storeName)){
//                otherOwner.removeOwnerByHisAppointer(store,storeOwner.member,storeOwner);
//            }
            DALService.removeStoreOwner(store,otherOwner);
        }else{
            throw new Exception(getUserName() + "is not owner for this store");
        }
    }

    public void removeStore(String storeName) {
        removeOneStore(storeName);
        appointedOwners = new ConcurrentHashMap<>();
        appointedManagers = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, List<StoreOwner>> getAppointedOwners() {
        return appointedOwners;
    }

    public ConcurrentHashMap<String, List<StoreManager>> getAppointedManagers() {
        return appointedManagers;
    }
}
