package DomainLayer;

import DTO.DealDTO;
import DTO.MemberDTO;
import DTO.StoreDTO;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;
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

    private List<String> liveMessages;


    public void setSystemManager(SystemManager systemManager) {
        this.systemManager = systemManager;
        isSystemManager = true;
    }

    public boolean checkIsSystemManager(){
        return isSystemManager;
    }

    public Member(String userName, String password) {
        super(userName);
        this.password = password;
        roles = new ConcurrentHashMap<>();
        this.isSystemManager=false;
        systemManager = null;
        pendingMessages = new ArrayList<>();
        isOnline = false;
        liveMessages = new ArrayList<>();
    }



    public String getPassword() {
        return password;
    }

    public MemberDTO getMemberDTO() throws Exception {
        List<DealDTO> dealDTOList = new LinkedList<>();
        for (Deal deal : userDeals){
            dealDTOList.add(deal.getDealDTO());
        }
        return new MemberDTO(this.userName, myStores(),dealDTOList);
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
            if(store.isAlreadyStoreOwner(otherMember.getUserName()))
                throw new Exception("member "+otherMember.getUserName()+" is already store owner");
            if(store.isAlreadyStoreManager(otherMember.getUserName()))
                throw new Exception("member "+otherMember.getUserName()+" is already store manager");
            if(store.isContractExistsForNewOwner(otherMember.getUserName()))
                throw new Exception("member "+otherMember.getUserName()+" already have a contract for this store ownership");

            store.createContractForNewOwner(owner, otherMember);
            return true;
        }
    }

    public boolean appointMemberAsStoreOwner(Store store, AbstractStoreOwner myBoss) throws Exception {
        if(store.isAlreadyStoreOwner(getUserName()))
            throw new Exception("member"+getUserName()+" is already store owner");
        if(store.isAlreadyStoreManager(getUserName()))
            throw new Exception("member"+getUserName()+" is already store manager");
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


    public boolean removeOwnerByHisAppointer(Store store, Member otherMember) throws Exception {
        AbstractStoreOwner owner = null;
        String storeName = store.getStoreName();
        if(roles.containsKey(RoleEnum.StoreFounder) && roles.get(RoleEnum.StoreFounder).haveStore(storeName))
            owner = (StoreFounder)roles.get(RoleEnum.StoreFounder);
        else if (roles.containsKey(RoleEnum.StoreOwner) && roles.get(RoleEnum.StoreOwner).haveStore(storeName))
            owner = (StoreOwner)roles.get(RoleEnum.StoreOwner);

        if(owner == null) throw new Exception(""+getUserName()+" is not owner for "+storeName);
        else{
            owner.removeOwnerByHisAppointer(store,otherMember);
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

    public Map<String, List<StoreDTO>> myStores() throws Exception {
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
        NotificationService.getInstance().subscribe(storeName,NotificationType.decisionForContract,this);
        NotificationService.getInstance().subscribe(storeName,NotificationType.fillAppointContract,this);
        NotificationService.getInstance().subscribe(storeName,NotificationType.ownerDone,this);

    }


    public void send(String msg) throws IOException {
        if(isOnline){
            NotificationService.getInstance().send(userName,msg);
            liveMessages.add(msg);
        }else{
            pendingMessages.add(msg);
        }
    }

    public void Login() throws IOException {
        isOnline = true;
//        if(!pendingMessages.isEmpty()){
//            StringBuilder msg = new StringBuilder("Attention: you got " + pendingMessages.size() + " messages:\n");
//            for(String str: pendingMessages){
//                msg.append("   - ").append(str);
//            }
//            pendingMessages.clear();
//            NotificationService.getInstance().send(userName, msg.toString());
//        }
    }

    public void Logout() {
        isOnline= false;
    }

    public void defineNotifications(String newMemberUserName) {
        NotificationService.getInstance().subscribeMember(newMemberUserName,NotificationType.requestNotification,this);
        NotificationService.getInstance().subscribeMember(newMemberUserName,NotificationType.subscriptionRemoved,this);
    }

    public void addCart(Cart cart) {
        this.cart = cart;
        cart.setUser(this);
    }

    public void assertHaveNoRule() throws Exception {
        if(!roles.isEmpty()){
            throw new Exception(getUserName() + " have a role in a store");
        }
    }


    //FOR ACCTEST OF STORE MANAGER
    public void takeDownSystemManagerAppointment(){
        this.roles.remove(RoleEnum.StoreManager);
    }

    public List<String> checkForAppendingMessages() throws IOException {
        List<String> messages = new ArrayList<>();
        if(!pendingMessages.isEmpty()){
            StringBuilder msg = new StringBuilder("Attention: you got " + pendingMessages.size() + " new messages:\n");
            for(String str: pendingMessages){
                msg.append("   - ").append(str);
            }
            String message = msg.toString();
            messages.add(message);
            pendingMessages.clear();
        }
        return messages;
    }

    public List<String> getLiveMessages(){
        return this.liveMessages;
    }

    public void clearMessages() {
        liveMessages.clear();
        pendingMessages.clear();
    }

    //FOR ACC TEST:

    public List<String> getAppendingMessages(){
        return this.pendingMessages;
    }




/*
    public void setAbstractOwner(AbstractStoreOwner owner){
        this.abstractOwner = owner;
    }*/
}
