package DomainLayer;

import java.util.concurrent.ConcurrentHashMap;
import DTO.MemberDTO;
import DataAccessLayer.CompositeKeys.RolesId;

import javax.persistence.*;

@MappedSuperclass
public abstract class Role {

    @EmbeddedId
    protected RolesId id;

    @Transient
    protected RoleEnum myRole;

    @Transient
    protected ConcurrentHashMap<String, AbstractStoreOwner> myBossesForStores;
    @Transient
    protected ConcurrentHashMap<String, Store> responsibleForStores;

    @Transient
    protected Member member;
    @OneToOne
    @JoinColumn(name = "myBoss")
    private Member boss;

    @Transient
    protected boolean isLoaded;

    public ConcurrentHashMap<String, Store> getResponsibleForStores() {
        return responsibleForStores;
    }

    public Role(Member member){
        this.member = member;
        myBossesForStores = new ConcurrentHashMap<>();
        responsibleForStores = new ConcurrentHashMap<>();
    }
    public Role() {
    }
    public void setBoss(Member boss) {
        this.boss = boss;
    }
    public void setId(RolesId id) {
        this.id = id;
    }
    public void setLoaded(boolean b){
        isLoaded = b;
    }

    public String getUserName(){
        return member.getUserName();
    }

    public boolean removeOwnerByHisAppointer(Store store,AbstractStoreOwner myBoss) throws Exception {
        String storeName = store.getStoreName();
        if(!responsibleForStores.containsKey(storeName))
            throw new Exception(""+getUserName()+" is not owner for this store");
        if(!myBossesForStores.containsKey(storeName))
            throw new Exception(""+getUserName()+" is founder for this store");
        if(!(myBossesForStores.get(storeName).getUserName() == myBoss.getUserName()))
            throw new Exception(""+myBoss.getUserName()+" is not the my appointer for this store");
        responsibleForStores.remove(storeName);
        myBossesForStores.remove(storeName);
        return true;
    }

    public boolean isMyBossForStore(String storeName, String memberName) throws Exception {
        if(storeName==null)
            throw new Exception("store name cant be null");
        if(memberName==null)
            throw new Exception("member name cant be null");

        storeName=storeName.strip().toLowerCase();
        memberName=memberName.strip().toLowerCase();
        if(!responsibleForStores.containsKey(storeName))
            throw new Exception(getUserName()+" is not a owner/manager for store "+ storeName);

        if(!myBossesForStores.get(storeName).getUserName().equals(memberName))
            return false;

        return true;
    }


    public boolean appointMemberAsStoreOwner(Store store, AbstractStoreOwner myBoss) throws Exception {
        String storeName = store.getStoreName();
        if(responsibleForStores.containsKey(storeName))
            throw new Exception(""+getUserName()+" is already owner for this store");

        responsibleForStores.put(storeName, store);
        myBossesForStores.put(storeName, myBoss);

        return true;
    }

    public boolean loadAppointMemberAsStoreOwner(Store store, AbstractStoreOwner myBoss) throws Exception {
        String storeName = store.getStoreName();
        responsibleForStores.put(storeName, store);
        myBossesForStores.put(storeName, myBoss);
        return true;
    }

    public boolean appointMemberAsStoreFounder(Store store) throws Exception {
        String storeName = store.getStoreName();
        if(responsibleForStores.containsKey(storeName))
            throw new Exception(""+getUserName()+" is already founder for this store");

        responsibleForStores.put(storeName, store);
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

    public MemberDTO getMemberDTO() throws Exception {
        return this.member.getMemberDTO();
    }


    //Currenly added for testing:
    public void addStore(String storeName, Store store){
        responsibleForStores.put(storeName, store);
    }

    public void removeOneStore(String storeName){
        myBossesForStores = new ConcurrentHashMap<>();
        responsibleForStores.remove(storeName);
    }
}
