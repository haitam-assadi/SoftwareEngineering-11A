package DomainLayer;

import DTO.MemberDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
enum RoleEnum {
    StoreOwner,
    StoreFounder,
    StoreManager
}
public class Member extends User{

    private ConcurrentHashMap<RoleEnum, Role> roles;

    private boolean isSystemManager;
    private SystemManager systemManager;
    private String password;

    private List<String> pendingMessages;

    private boolean isOnline;

    public Member(String userName, String password) {
        super(userName);
        this.password = password;
        roles = new ConcurrentHashMap<>();
        this.isSystemManager=false;
        systemManager = null;
        pendingMessages = new ArrayList<>();
        isOnline = false;
    }

    public String getPassword() {
        return password;
    }

    public MemberDTO getMemberDTO(String jobTitle) {
        return new MemberDTO(this.userName, jobTitle);
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
        if(store.isAlreadyStoreOwner(getUserName()))
            throw new Exception("member"+getUserName()+" is already store owner");
        StoreOwner storeOwner =  new StoreOwner(this);
        roles.putIfAbsent(RoleEnum.StoreOwner,storeOwner);
        subscribeOwnerForNotifications(store.getStoreName());
        StoreOwner storeOwnerRole =  (StoreOwner) roles.get(RoleEnum.StoreOwner);
        storeOwnerRole.appointMemberAsStoreOwner(store,myBoss);
        store.appointMemberAsStoreOwner(storeOwnerRole);
        return true;
    }

    public StoreOwner getStoreOwner(){
        StoreOwner storeOwnerRole =  (StoreOwner) roles.get(RoleEnum.StoreOwner);
        return storeOwnerRole;
    }

    public boolean containsRole(String roleTitle) {
        for(RoleEnum role : this.roles.keySet()){
            if(role.toString().equals(roleTitle))
                return true;
        }
        return false;
    }
    public boolean appointOtherMemberAsStoreManager(Store store, Member otherMember) throws Exception {
        AbstractStoreOwner owner = null;
        String storeName = store.getStoreName();
        if(roles.containsKey(RoleEnum.StoreFounder) && roles.get(RoleEnum.StoreFounder).haveStore(storeName))
            owner = (StoreFounder)roles.get(RoleEnum.StoreFounder);
        else if (roles.containsKey(RoleEnum.StoreOwner) && roles.get(RoleEnum.StoreOwner).haveStore(storeName))
            owner = (StoreOwner)roles.get(RoleEnum.StoreOwner);

        if(owner == null) throw new Exception(""+getUserName()+" is not owner for "+storeName);
        else{
            owner.appointOtherMemberAsStoreManager(store,otherMember);
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

    public boolean appointMemberAsStoreFounder(Store store) throws Exception {
        if(store.alreadyHaveFounder())
            throw new Exception("store "+store.getStoreName()+" already have a founder");

        roles.putIfAbsent(RoleEnum.StoreFounder, new StoreFounder(this));
        StoreFounder storeFounderRole =  (StoreFounder) roles.get(RoleEnum.StoreFounder);

        storeFounderRole.appointMemberAsStoreFounder(store);
        store.setStoreFounderAtStoreCreation(storeFounderRole);
        NotificationService.getInstance().subscribe(store.getStoreName(),NotificationType.productBought,this);
        //todo: add the permissions
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

    public boolean removeOwnerByHisAppointer(Store store, Member otherMember) throws Exception {
        AbstractStoreOwner owner = null;
        StoreOwner otherOwner = null;
        String storeName = store.getStoreName();
        if(roles.containsKey(RoleEnum.StoreFounder) && roles.get(RoleEnum.StoreFounder).haveStore(storeName))
            owner = (StoreFounder)roles.get(RoleEnum.StoreFounder);
        else if (roles.containsKey(RoleEnum.StoreOwner) && roles.get(RoleEnum.StoreOwner).haveStore(storeName))
            owner = (StoreOwner)roles.get(RoleEnum.StoreOwner);
        if(owner == null) throw new Exception(""+getUserName()+" is not owner for "+storeName);
        otherOwner = otherMember.getStoreOwner();
        if(otherOwner == null) throw new Exception(""+otherOwner.getUserName()+" is not owner");
        else{
            owner.removeOwnerByHisAppointer(store,otherMember,otherOwner);
            return true;
        }
    }

    public void assertIsOwnerForTheStore(Store store) throws Exception {
        AbstractStoreOwner owner = null;
        String storeName = store.getStoreName();
        if(roles.containsKey(RoleEnum.StoreFounder) && roles.get(RoleEnum.StoreFounder).haveStore(storeName))
            owner = (StoreFounder)roles.get(RoleEnum.StoreFounder);
        else if (roles.containsKey(RoleEnum.StoreOwner) && roles.get(RoleEnum.StoreOwner).haveStore(storeName))
            owner = (StoreOwner)roles.get(RoleEnum.StoreOwner);
        if(owner == null) throw new Exception(""+getUserName()+" is not owner for "+storeName);
    }

    private void subscribeOwnerForNotifications(String storeName) {
        NotificationService.getInstance().subscribe(storeName,NotificationType.storeClosed,this);
        NotificationService.getInstance().subscribe(storeName,NotificationType.productBought,this);
        NotificationService.getInstance().subscribe(storeName,NotificationType.RemovedFromOwningStore,this);
        NotificationService.getInstance().subscribe(storeName,NotificationType.storeOpenedAfterClose,this);
    }


    public void send(String msg){
        if(isOnline){
            NotificationService.getInstance().send(userName,msg);
        }else{
            pendingMessages.add(msg);
        }
    }

    public void Login() {
        isOnline = true;
    }

    public void Logout() {
        isOnline= false;
    }

    public void defineNotifications(String newMemberUserName) {
        NotificationService.getInstance().subscribe(newMemberUserName,NotificationType.requestNotification,this);
        NotificationService.getInstance().subscribe(newMemberUserName,NotificationType.subscriptionRemoved,this);
    }



/*
    public void setAbstractOwner(AbstractStoreOwner owner){
        this.abstractOwner = owner;
    }*/
}
