package DomainLayer;

import java.util.concurrent.ConcurrentHashMap;
import DomainLayer.DTO.MemberDTO;

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

    public boolean appointMemberAsStoreManager(Store store, AbstractStoreOwner myBoss) throws Exception {
        String storeName = store.getStoreName();
        if(responsibleForStores.containsKey(storeName))
            throw new Exception(""+getUserName()+" is already manager or owner for this store");
        responsibleForStores.put(storeName, store);
        myBossesForStores.put(storeName, myBoss);
        return true;
    }

    public MemberDTO getMemberDTO() {
        return this.member.getMemberDTO(this.myRole.name());
    }

    public void addNotification(String sender, String date, String description) {
        this.member.addNotification(sender, date, description);
    }

    //Currenly added for testing:
    public void addStore(String storeName, Store store){
        responsibleForStores.put(storeName, store);
    }
}
