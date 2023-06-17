package DomainLayer;

import DTO.DealDTO;
import DTO.MemberDTO;
import DTO.StoreDTO;
import DataAccessLayer.CompositeKeys.RolesId;
import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.Controller.StoreMapper;
import DataAccessLayer.DALService;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
enum RoleEnum {
    StoreOwner,
    StoreFounder,
    StoreManager
}
@Entity
@Table(name = "Member")
@PrimaryKeyJoinColumn(name = "userName")
public class Member extends User{


    @Transient
    private Map<RoleEnum, Role> roles;

    private boolean isSystemManager;

    @Transient
    private SystemManager systemManager;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pending_messages", joinColumns = @JoinColumn(name = "member_name"))
    @Column(name = "message")
    private Set<String> pendingMessages;

    private boolean isOnline;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_name"))
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Set<RoleEnum> memberRolesFlag;

    @Transient
    private boolean isLoaded;



    public void setSystemManager(SystemManager systemManager) throws Exception {
        loadMember();
        this.systemManager = systemManager;
        isSystemManager = true;
        DALService.memberRepository.save(this);
    }

    public SystemManager checkIsSystemManager() throws Exception {
        loadMember();
        return systemManager;
    }

    public Member(String userName, String password) {
        super(userName);
        this.password = password;
        roles = new ConcurrentHashMap<>();
        this.isSystemManager=false;
        systemManager = null;
        pendingMessages = new HashSet<>();
        isOnline = false;
        memberRolesFlag = new HashSet<>();
        isLoaded = true;
    }

    public Member(){
        super();
    }

    public Member(String userName){
        super.userName = userName;
        memberRolesFlag = new HashSet<>();
        pendingMessages = new HashSet<>();
        roles = new ConcurrentHashMap<>();
        isLoaded = false;
    }



    public String getPassword() throws Exception {
        loadMember();
        return password;
    }

    public MemberDTO getMemberDTO() throws Exception {
        loadMember();
        List<DealDTO> dealDTOList = new LinkedList<>();
        for (Deal deal : userDeals){
            dealDTOList.add(deal.getDealDTO());
        }
        return new MemberDTO(this.userName, myStores(),dealDTOList);
    }
    public boolean appointOtherMemberAsStoreOwner(Store store, Member otherMember) throws Exception {
        loadMember();
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
        loadMember();
        if(store.isAlreadyStoreOwner(getUserName()))
            throw new Exception("member"+getUserName()+" is already store owner");
        if(store.isAlreadyStoreManager(getUserName()))
            throw new Exception("member"+getUserName()+" is already store manager");
        StoreOwner storeOwner = MemberMapper.getInstance().getNewStoreOwner(this);
        addRole(RoleEnum.StoreOwner);
        roles.putIfAbsent(RoleEnum.StoreOwner,storeOwner);
        subscribeOwnerForNotifications(store.getStoreName());
        StoreOwner storeOwnerRole =  (StoreOwner) roles.get(RoleEnum.StoreOwner);
        storeOwnerRole.appointMemberAsStoreOwner(store,myBoss);
        store.appointMemberAsStoreOwner(storeOwnerRole);
        storeOwnerRole.setId(new RolesId(userName, store.getStoreName()));
        storeOwnerRole.setBoss(myBoss.member,myBoss.getRoleType());
        if (Market.dbFlag)
            DALService.saveStoreOwner(storeOwnerRole,store,this);
        return true;
    }


    public StoreOwner getStoreOwner() throws Exception {
        loadMember();
        StoreOwner storeOwnerRole =  (StoreOwner) roles.get(RoleEnum.StoreOwner);
        return storeOwnerRole;
    }

    public boolean containsRole(String roleTitle) throws Exception {
        loadMember();
        for(RoleEnum role : this.roles.keySet()){
            if(role.toString().equals(roleTitle))
                return true;
        }
        return false;
    }
    public boolean appointOtherMemberAsStoreManager(Store store, Member otherMember) throws Exception {
        loadMember();
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
        loadMember();
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
        loadMember();
        if(store.isAlreadyStoreOwner(getUserName()))
            throw new Exception("member"+getUserName()+" is already store owner");
        if(store.isAlreadyStoreManager(getUserName()))
            throw new Exception("member"+getUserName()+" is already store manager");
        StoreManager storeManager = MemberMapper.getInstance().getNewStoreManager(this);
        addRole(RoleEnum.StoreManager);
        roles.putIfAbsent(RoleEnum.StoreManager, storeManager);
        StoreManager storeManagerRole =  (StoreManager) roles.get(RoleEnum.StoreManager);

        storeManagerRole.appointMemberAsStoreManager(store,myBoss);
        store.appointMemberAsStoreManager(storeManagerRole);//TODO: CHECK IF OWNER IN THE STORE
        storeManagerRole.setId(new RolesId(userName, store.getStoreName()));
        storeManagerRole.setBoss(myBoss.member,myBoss.getRoleType());
        if (Market.dbFlag)
            DALService.saveStoreManager(storeManagerRole,store,this);
            //todo: check insert to manager_permission table
        return true;
    }

    public StoreFounder appointMemberAsStoreFounder(Store store) throws Exception {
        loadMember();
        if(store.alreadyHaveFounder())
            throw new Exception("store "+store.getStoreName()+" already have a founder");
        addRole(RoleEnum.StoreFounder);
        StoreFounder storeFounder = MemberMapper.getInstance().getNewStoreFounder(this);
        roles.putIfAbsent(RoleEnum.StoreFounder,storeFounder);
        StoreFounder storeFounderRole =  (StoreFounder) roles.get(RoleEnum.StoreFounder);
        storeFounderRole.appointMemberAsStoreFounder(store);
        store.setStoreFounderAtStoreCreation(storeFounderRole);
        NotificationService.getInstance().subscribe(store.getStoreName(),NotificationType.productBought,this);
        NotificationService.getInstance().subscribe(store.getStoreName(),NotificationType.storeClosedBySystemManager,this);

        return storeFounderRole;
    }

    public Map<String, List<StoreDTO>> myStores() throws Exception {
        loadMember();
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

    public StoreManager getStoreManager() throws Exception {
        loadMember();
        StoreManager storeManagerRole =  (StoreManager) roles.get(RoleEnum.StoreManager);
        return storeManagerRole;
    }

    //Currently added for testing
    public void addRole(RoleEnum roleEnum, Role role) throws Exception {
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
        loadMember();
        AbstractStoreOwner owner = null;
        String storeName = store.getStoreName();
        if(roles.containsKey(RoleEnum.StoreFounder) && roles.get(RoleEnum.StoreFounder).haveStore(storeName))
            owner = (StoreFounder)roles.get(RoleEnum.StoreFounder);
        else if (roles.containsKey(RoleEnum.StoreOwner) && roles.get(RoleEnum.StoreOwner).haveStore(storeName))
            owner = (StoreOwner)roles.get(RoleEnum.StoreOwner);
        if(owner == null) throw new Exception(""+getUserName()+" is not owner for "+storeName);
    }



    public void subscribeOwnerForNotifications(String storeName) {
        NotificationService.getInstance().subscribe(storeName,NotificationType.storeClosed,this);
        NotificationService.getInstance().subscribe(storeName,NotificationType.productBought,this);
        NotificationService.getInstance().subscribe(storeName,NotificationType.RemovedFromOwningStore,this);
        NotificationService.getInstance().subscribe(storeName,NotificationType.storeOpenedAfterClose,this);
        NotificationService.getInstance().subscribe(storeName,NotificationType.storeClosedBySystemManager,this);
    }


    public void send(String msg) throws Exception {
        loadMember();
        if(isOnline){
            NotificationService.getInstance().send(userName,msg);
        }else{
            pendingMessages.add(msg);
            DALService.memberRepository.save(this);
        }
    }

    public void Login() throws Exception {
        loadMember();
        isOnline = true;
        if(!pendingMessages.isEmpty()){
            StringBuilder msg = new StringBuilder("Attention: you got " + pendingMessages.size() + " messages:\n");
            for(String str: pendingMessages){
                msg.append("   - ").append(str);
            }
            pendingMessages.clear();
            NotificationService.getInstance().send(userName, msg.toString());
        }
        if (Market.dbFlag)
            DALService.memberRepository.save(this);
    }

    public void Logout() throws Exception {
        loadMember();
        isOnline= false;
        if (Market.dbFlag)
            DALService.memberRepository.save(this);
    }

    public void defineNotifications(String newMemberUserName) {
        NotificationService.getInstance().subscribeMember(newMemberUserName,NotificationType.requestNotification,this);
        NotificationService.getInstance().subscribeMember(newMemberUserName,NotificationType.subscriptionRemoved,this);
    }

    public void addCart(Cart cart) throws Exception {
        //loadMember();
        for (Bag bag: cart.getBags().values()){
            for (Product product: bag.getProductAmount().keySet()) {
                this.addToCart(bag.getStoreBag(), product.getName(),bag.getProductAmount().get(product),true);
            }
        }
    }

    public void declareRoles(){
        roles = new ConcurrentHashMap<>();
    }

    public void assertHaveNoRule() throws Exception {
        loadMember();
        if(!roles.isEmpty()){
            throw new Exception(getUserName() + " have a role in a store");
        }
    }

    private void addRole(RoleEnum roleEnum) throws Exception {
        loadMember();
        if (!roles.containsKey(roleEnum)){
            memberRolesFlag.add(roleEnum);
        }
    }

    public void loadMember() throws Exception {
        if (!isLoaded) {
            if (Market.dbFlag) {
                Member member = DALService.memberRepository.findById(userName).get();
                this.password = member.password;
                this.isSystemManager = member.isSystemManager;
                this.memberRolesFlag = member.memberRolesFlag; //todo: check that member.memberRolesFlag is enumerated values
                this.pendingMessages = member.pendingMessages;
                this.isOnline = member.isOnline;
                this.roles = new ConcurrentHashMap<>();
                this.loadRoles();
                this.cart = new Cart(member);
                this.cart.setLoaded(false);
                this.cart.setMemberCart(this);
                this.isLoaded = true;
            }
        }
    }

    private void loadRoles() throws Exception {
        if (isSystemManager){
            this.systemManager = MemberMapper.getInstance().getSystemManager(userName);
        }
        else {
            if (memberRolesFlag.contains(RoleEnum.StoreFounder)) {
                StoreFounder storeFounder = MemberMapper.getInstance().getStoreFounder(userName);
                roles.put(RoleEnum.StoreFounder, storeFounder);
            }
            if (memberRolesFlag.contains(RoleEnum.StoreOwner)) {
                StoreOwner storeOwner = MemberMapper.getInstance().getStoreOwner(userName);
                roles.put(RoleEnum.StoreOwner, storeOwner);
            }
            if (memberRolesFlag.contains(RoleEnum.StoreManager)) {
                StoreManager storeManager = MemberMapper.getInstance().getStoreManager(userName);
                roles.put(RoleEnum.StoreManager, storeManager);
            }
        }
    }



    @Transactional
    public void removeCart() throws Exception {
        loadMember();
        cart.removeAllCart();
        Cart membercart = cart;
        cart = null;
        if (Market.dbFlag) {
            DALService.cartRepository.save(membercart);
            DALService.memberRepository.save(this);
            DALService.cartRepository.delete(membercart);
            DALService.memberRepository.delete(this);
        }
    }






/*
    public void setAbstractOwner(AbstractStoreOwner owner){
        this.abstractOwner = owner;
    }*/
}
