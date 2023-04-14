package DomainLayer;

import java.security.PrivateKey;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
enum RoleEnum {
    StoreOwner,
    StoreFounder,
    StoreManager
}
public class Member extends User{

    private boolean isSystemManager;
    private SystemManager systemManager;
    public ConcurrentHashMap<RoleEnum, Role> roles;
    private String password;

    public Member(String userName, String password) {
        super(userName);
        this.isSystemManager=false;
        systemManager = null;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public boolean appointOtherMemberAsStoreOwner(Store store, Member otherMember) throws Exception {
        AbstractStoreOwner owner = null;
        String storeName = store.getStoreName();
        if(roles.containsKey(RoleEnum.StoreFounder) && roles.get(RoleEnum.StoreFounder).haveStore(storeName))
            owner = (StoreFounder)roles.get(RoleEnum.StoreFounder);
        else if (roles.containsKey(RoleEnum.StoreOwner) && roles.get(RoleEnum.StoreOwner).haveStore(storeName))
            owner = (StoreOwner)roles.get(RoleEnum.StoreOwner);

        if(owner == null) throw new Exception(""+getUserName()+" is not owner for "+storeName);
        else{
            owner.appointOtherMemberAsStoreOwner(store,otherMember);
            return true;
        }
    }

    public boolean appointMemberAsStoreOwner(Store store, AbstractStoreOwner myBoss) throws Exception {
        roles.putIfAbsent(RoleEnum.StoreOwner, new StoreOwner(this));
        StoreOwner storeOwnerRole =  (StoreOwner) roles.get(RoleEnum.StoreOwner);

        storeOwnerRole.appointMemberAsStoreOwner(store,myBoss);
        store.appointMemberAsStoreOwner(storeOwnerRole);
        return true;
    }
    public StoreOwner getStoreOwner(){
        StoreOwner storeOwnerRole =  (StoreOwner) roles.get(RoleEnum.StoreOwner);
        return storeOwnerRole;
    }


}
