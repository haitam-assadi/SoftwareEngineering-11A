package DomainLayer;

import DomainLayer.DTO.MemberDTO;

import java.security.PrivateKey;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
enum RoleEnum {
    StoreOwner,
    StoreFounder,
    StoreManager
}
public class Member extends User{

    private ConcurrentHashMap<RoleEnum, Role> roles;

    private List<Notification> userNotifications;
    private boolean isSystemManager;
    private SystemManager systemManager;
    private String password;

    private AbstractStoreOwner abstractOwner;

    public Member(String userName, String password) {
        super(userName);
        this.isSystemManager=false;
        systemManager = null;
        this.password = password;
        this.roles = new ConcurrentHashMap<RoleEnum, Role>();
    }

    public String getPassword() {
        return password;
    }

    public MemberDTO getMemberDTO(String jobTitle) {
        return new MemberDTO(this.userName, jobTitle);
    }
    public boolean appointOtherMemberAsStoreOwner(Store store, Member otherMember) throws Exception {
        //AbstractStoreOwner owner = null;
        String storeName = store.getStoreName();
        if(roles.containsKey(RoleEnum.StoreFounder) && roles.get(RoleEnum.StoreFounder).haveStore(storeName))
            this.abstractOwner = (StoreFounder)roles.get(RoleEnum.StoreFounder);
        else if (roles.containsKey(RoleEnum.StoreOwner) && roles.get(RoleEnum.StoreOwner).haveStore(storeName))
            this.abstractOwner = (StoreOwner)roles.get(RoleEnum.StoreOwner);

        if(this.abstractOwner == null) throw new Exception(""+getUserName()+" is not owner for "+storeName);
        else{
            this.abstractOwner.appointOtherMemberAsStoreOwner(store,otherMember);
            return true;
        }
    }

    public boolean appointMemberAsStoreOwner(Store store, AbstractStoreOwner myBoss) throws Exception {
        if(store.isAlreadyStoreOwner(getUserName()))
            throw new Exception("member"+getUserName()+" is already store owner");
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

    public void addNotification(String sender, String date, String description){
        this.userNotifications.add(new Notification(sender, date, description));
    }

    public boolean containsRole(String roleTitle) {
        for(RoleEnum role : this.roles.keySet()){
            if(role.toString().equals(roleTitle))
                return true;
        }
        return false;
    }
    public boolean appointOtherMemberAsStoreManager(Store store, Member otherMember) throws Exception {
        //AbstractStoreOwner owner = null;
        String storeName = store.getStoreName();
        if(roles.containsKey(RoleEnum.StoreFounder) && roles.get(RoleEnum.StoreFounder).haveStore(storeName))
            this.abstractOwner = (StoreFounder)roles.get(RoleEnum.StoreFounder);
        else if (roles.containsKey(RoleEnum.StoreOwner) && roles.get(RoleEnum.StoreOwner).haveStore(storeName))
            this.abstractOwner = (StoreOwner)roles.get(RoleEnum.StoreOwner);

        if(this.abstractOwner == null) throw new Exception(""+getUserName()+" is not owner for "+storeName);
        else{
            this.abstractOwner.appointOtherMemberAsStoreManager(store,otherMember);
            return true;
        }
    }


    public boolean appointMemberAsStoreManager(Store store, AbstractStoreOwner myBoss) throws Exception {
        if(store.isAlreadyStoreOwner(getUserName()))
            throw new Exception("member"+getUserName()+" is already store owner");
        if(store.isAlreadyStoreManager(getUserName()))
            throw new Exception("member"+getUserName()+" is already store manager");
        roles.putIfAbsent(RoleEnum.StoreManager, new StoreManager(this));
        StoreManager storeManagerRole =  (StoreManager) roles.get(RoleEnum.StoreManager);

        storeManagerRole.appointMemberAsStoreManager(store,myBoss);
        store.appointMemberAsStoreManager(storeManagerRole);//TODO: CHECK IF OWNER IN THE STORE
        return true;
    }
    public StoreManager getStoreManager(){
        StoreManager storeManagerRole =  (StoreManager) roles.get(RoleEnum.StoreManager);
        return storeManagerRole;
    }

    //Currently added for testing
    public void addRole(RoleEnum roleEnum, Role role){
        this.roles.put(roleEnum, role);
    }

    public RoleEnum getRoleEnum(String role){
        switch (role){
            case "StoreOwner":
                return RoleEnum.StoreOwner;
            case "StoreFounder":
                return RoleEnum.StoreFounder;
            default: return null;
        }
    }

    public void setAbstractOwner(AbstractStoreOwner owner){
        this.abstractOwner = owner;
    }
}
