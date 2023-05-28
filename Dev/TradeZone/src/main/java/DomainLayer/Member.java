package DomainLayer;

import DTO.MemberDTO;
import DTO.StoreDTO;
import DataAccessLayer.DALService;
import DataAccessLayer.DTO.*;


import java.util.*;
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

    private boolean init;

    public void setSystemManager(SystemManager systemManager) {
        this.systemManager = systemManager;
        isSystemManager = true;
        DALService.memberRepository.save(getDTOMember());
    }

    public boolean checkIsSystemManager(){
        return isSystemManager;
    }

    public Member(String userName){
        super(userName);
    }

    public Member(String userName, String password) {
        super(userName);
        this.password = password;
        roles = new ConcurrentHashMap<>();
        this.isSystemManager=false;
        systemManager = null;
        pendingMessages = new ArrayList<>();
        isOnline = false;
    }

    public Member(String userName, String password, boolean isSystemManager, boolean isOnline) {
        super(userName);
        this.password = password;
        this.isSystemManager=isSystemManager;
        this.isOnline=isOnline;
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
        NotificationService.getInstance().subscribe(store.getStoreName(),NotificationType.storeClosedBySystemManager,this);

        //todo: add the permissions
        return true;
    }

    public Map<String, List<StoreDTO>> myStores() {
        Map<String,List<StoreDTO>> memberStores = new ConcurrentHashMap<>();

        if(roles.containsKey(RoleEnum.StoreFounder)){
            Role role = roles.get(RoleEnum.StoreFounder);
            List<StoreDTO> stores = new LinkedList<>();
            for (Store store: role.getResponsibleForStores().values()) {
                stores.add(store.getStoreInfo());
            }
            memberStores.put("StoreFounder",stores);
        }
        if(roles.containsKey(RoleEnum.StoreOwner)){
            Role role = roles.get(RoleEnum.StoreOwner);
            List<StoreDTO> stores = new LinkedList<>();
            for (Store store: role.getResponsibleForStores().values()) {
                stores.add(store.getStoreInfo());
            }
            memberStores.put("StoreOwner",stores);
        }
        if(roles.containsKey(RoleEnum.StoreManager)){
            Role role = roles.get(RoleEnum.StoreManager);
            List<StoreDTO> stores = new LinkedList<>();
            for (Store store: role.getResponsibleForStores().values()) {
                stores.add(store.getStoreInfo());
            }
            memberStores.put("StoreManager",stores);
        }
        return memberStores;
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
        NotificationService.getInstance().subscribe(storeName,NotificationType.storeClosedBySystemManager,this);
    }


    public void send(String msg){
        if(isOnline){
            NotificationService.getInstance().send(userName,msg);
        }else{
            pendingMessages.add(msg);
            DALService.memberRepository.save(getDTOMember());
        }
    }

    public void Login() {
        isOnline = true;
        if(!pendingMessages.isEmpty()){
            StringBuilder msg = new StringBuilder("Attention: you got " + pendingMessages.size() + " messages:\n");
            for(String str: pendingMessages){
                msg.append("   - ").append(str);
            }
            pendingMessages.clear();
            NotificationService.getInstance().send(userName, msg.toString());
        }

        DALService.memberRepository.save(getDTOMember());
    }

    public void Logout() {
        isOnline= false;
        DALService.memberRepository.save(getDTOMember());
    }

    public void defineNotifications(String newMemberUserName) {
        NotificationService.getInstance().subscribeMember(newMemberUserName,NotificationType.requestNotification,this);
        NotificationService.getInstance().subscribeMember(newMemberUserName,NotificationType.subscriptionRemoved,this);
    }

    public void addCart(Cart cart) {
        this.cart = cart;
    }

    public void assertHaveNoRule() throws Exception {
        if(!roles.isEmpty()){
            throw new Exception(getUserName() + " have a role in a store");
        }
    }

    public DTOMember getDTOMember(){
        boolean owner = false,founder = false,manager = false;
        if (roles.containsKey(RoleEnum.StoreOwner)) owner = true;
        if (roles.containsKey(RoleEnum.StoreManager)) manager = true;
        if(roles.containsKey(RoleEnum.StoreFounder)) founder = true;
        return new DTOMember(this.userName, this.password, this.isOnline, 1, this.isSystemManager,founder,owner,manager);
    }








/*
    public void setAbstractOwner(AbstractStoreOwner owner){
        this.abstractOwner = owner;
    }*/
}
