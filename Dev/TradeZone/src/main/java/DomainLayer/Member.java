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
    private List<String> pendingMessages;

    private boolean isOnline;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_name"))
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Set<RoleEnum> memberRolesFlag;

    @Transient
    private boolean isLoaded;



    public void setSystemManager(SystemManager systemManager) {
        this.systemManager = systemManager;
        isSystemManager = true;
    }

    public SystemManager checkIsSystemManager() throws Exception {
        if (isSystemManager){
            if (systemManager==null){
                //todo:check if we should load appointedSystemManagers
                this.systemManager = MemberMapper.getInstance().getSystemManager(userName);
            }
        }
        return systemManager;
    }

    public Member(String userName, String password) {
        super(userName);
        this.password = password;
        roles = new ConcurrentHashMap<>();
        this.isSystemManager=false;
        systemManager = null;
        pendingMessages = new ArrayList<>();
        isOnline = false;
        memberRolesFlag = new HashSet<>();
        //isLoaded??
    }

    public Member(){
        super();
    }

    public Member(String userName){
        super.userName = userName;
        isLoaded = false;
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
            owner.appointOtherMemberAsStoreOwner(store,otherMember);
            return true;
        }
    }

    public boolean appointMemberAsStoreOwner(Store store, AbstractStoreOwner myBoss) throws Exception {
        if(store.isAlreadyStoreOwner(getUserName()))
            throw new Exception("member"+getUserName()+" is already store owner");
        if(store.isAlreadyStoreManager(getUserName()))
            throw new Exception("member"+getUserName()+" is already store manager");
        StoreOwner storeOwner =  new StoreOwner(this);
        addRole(RoleEnum.StoreOwner);
        roles.putIfAbsent(RoleEnum.StoreOwner,storeOwner);
        subscribeOwnerForNotifications(store.getStoreName());
        StoreOwner storeOwnerRole =  (StoreOwner) roles.get(RoleEnum.StoreOwner);
        storeOwnerRole.appointMemberAsStoreOwner(store,myBoss);
        store.appointMemberAsStoreOwner(storeOwnerRole);
        storeOwnerRole.setId(new RolesId(userName, store.getStoreName()));
        storeOwnerRole.setBoss(myBoss.member,myBoss.getRoleType());
        DALService.saveStoreOwner(storeOwnerRole,store,this);
        return true;
    }

    private boolean appointMemberAsStoreOwner(Store store, AbstractStoreOwner myBoss,Member member) throws Exception {
//        if(store.isAlreadyStoreOwner(getUserName()))
//            throw new Exception("member"+getUserName()+" is already store owner");
//        if(store.isAlreadyStoreManager(getUserName()))
//            throw new Exception("member"+getUserName()+" is already store manager");
        StoreOwner storeOwner =  new StoreOwner(member);
        addRole(RoleEnum.StoreOwner);
        roles.putIfAbsent(RoleEnum.StoreOwner,storeOwner);
        subscribeOwnerForNotifications(store.getStoreName());
        StoreOwner storeOwnerRole =  (StoreOwner) roles.get(RoleEnum.StoreOwner);
        storeOwnerRole.loadAppointMemberAsStoreOwner(store,myBoss);
        store.loadAppointMemberAsStoreOwner(storeOwnerRole);
        storeOwnerRole.setId(new RolesId(userName, store.getStoreName()));
        storeOwnerRole.setBoss(myBoss.member,myBoss.getRoleType());
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
        addRole(RoleEnum.StoreManager);
        roles.putIfAbsent(RoleEnum.StoreManager, new StoreManager(this));
        StoreManager storeManagerRole =  (StoreManager) roles.get(RoleEnum.StoreManager);

        storeManagerRole.appointMemberAsStoreManager(store,myBoss);
        store.appointMemberAsStoreManager(storeManagerRole);//TODO: CHECK IF OWNER IN THE STORE
        storeManagerRole.setId(new RolesId(userName, store.getStoreName()));
        storeManagerRole.setBoss(myBoss.member,myBoss.getRoleType());
        DALService.saveStoreManager(storeManagerRole,store,this);
        return true;
    }

    public StoreFounder appointMemberAsStoreFounder(Store store) throws Exception {
        if(store.alreadyHaveFounder())
            throw new Exception("store "+store.getStoreName()+" already have a founder");
        addRole(RoleEnum.StoreFounder);
        roles.putIfAbsent(RoleEnum.StoreFounder, new StoreFounder(this));
        StoreFounder storeFounderRole =  (StoreFounder) roles.get(RoleEnum.StoreFounder);
        MemberMapper.getInstance().insertFounder(storeFounderRole);
        storeFounderRole.appointMemberAsStoreFounder(store);
        store.setStoreFounderAtStoreCreation(storeFounderRole);
        StoreMapper.getInstance().insertStore(store.getStoreName(),store);
        NotificationService.getInstance().subscribe(store.getStoreName(),NotificationType.productBought,this);
        NotificationService.getInstance().subscribe(store.getStoreName(),NotificationType.storeClosedBySystemManager,this);

        //todo: add the permissions
        return storeFounderRole;
    }

    public Map<String, List<StoreDTO>> myStores() throws Exception {
        Map<String,List<StoreDTO>> memberStores = new ConcurrentHashMap<>();

        if(memberRolesFlag.contains(RoleEnum.StoreFounder)){
            if (!roles.containsKey(RoleEnum.StoreFounder)){
                StoreFounder storeFounder = MemberMapper.getInstance().getStoreFounder(userName);
                roles.put(RoleEnum.StoreFounder,storeFounder);
                storeFounder.loadFounder();
//                //todo: load role
//                List<StoreFounder> storeFounders = DALService.storeFounderRepository.findAllByMemberName(userName);
//                for (StoreFounder storeFounder: storeFounders){
//                    Optional<Store> storeD = DALService.storeRepository.findById(storeFounder.id.getStoreName());
//                    if (storeD.isPresent()){
//                        Store store = storeD.get();
//                        store.getStock().defineStockProductsMap();
//                        appointMemberAsStoreFounder(store);
//                        if (!store.getStoreOwners().isEmpty()){
//                            for (String ownerName: store.getStoreOwners().keySet()) {
//                                Optional<StoreOwner> storeOwnerD = DALService.storesOwnersRepository.findById(new RolesId(ownerName,store.getStoreName()));
//                                if (storeOwnerD.isPresent()){
//                                    StoreOwner storeOwner = storeOwnerD.get();
//                                    Optional<Member> member = DALService.memberRepository.findById(ownerName);
//                                    appointMemberAsStoreOwner(store,storeFounder,member.get());
//                                }
//                            }
//                        }
//                        System.out.println();
//                    }
//                }
            }
            Role role = roles.get(RoleEnum.StoreFounder);
            List<StoreDTO> stores = new LinkedList<>();
            for (Store store: role.getResponsibleForStores().values()) {
                stores.add(store.getStoreInfo());
            }
            memberStores.put("StoreFounder",stores);
        }
        if(memberRolesFlag.contains(RoleEnum.StoreOwner)){
            if(!roles.containsKey(RoleEnum.StoreOwner)){
                //todo: load role
                StoreOwner storeOwner = MemberMapper.getInstance().getStoreOwner(userName);
                roles.put(RoleEnum.StoreOwner,storeOwner);
                storeOwner.loadOwner();
            }
            Role role = roles.get(RoleEnum.StoreOwner);
            List<StoreDTO> stores = new LinkedList<>();
            for (Store store: role.getResponsibleForStores().values()) {
                stores.add(store.getStoreInfo());
            }
            memberStores.put("StoreOwner",stores);
        }
        if(memberRolesFlag.contains(RoleEnum.StoreManager)){
            if (!roles.containsKey(RoleEnum.StoreManager)){
                //todo: load role
                StoreManager storeManager = MemberMapper.getInstance().getStoreManager(userName);
                roles.put(RoleEnum.StoreOwner,storeManager);
                storeManager.loadManager();
            }
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
        }
    }

    public void Login() {
        //loadMember();
        isOnline = true;
        if(!pendingMessages.isEmpty()){
            StringBuilder msg = new StringBuilder("Attention: you got " + pendingMessages.size() + " messages:\n");
            for(String str: pendingMessages){
                msg.append("   - ").append(str);
            }
            pendingMessages.clear();
            NotificationService.getInstance().send(userName, msg.toString());
        }
    }

    public void Logout() {
        isOnline= false;
    }

    public void defineNotifications(String newMemberUserName) {
        NotificationService.getInstance().subscribeMember(newMemberUserName,NotificationType.requestNotification,this);
        NotificationService.getInstance().subscribeMember(newMemberUserName,NotificationType.subscriptionRemoved,this);
    }

    public void addCart(Cart cart) throws Exception {
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
        if(!roles.isEmpty()){
            throw new Exception(getUserName() + " have a role in a store");
        }
    }

    private void addRole(RoleEnum roleEnum){
        if (!roles.containsKey(roleEnum)){
            memberRolesFlag.add(roleEnum);
        }
    }

    public void loadMember(){
        if (!isLoaded) {
            Member member = DALService.memberRepository.findById(userName).get();
            this.password = member.password;
            this.isSystemManager = member.isSystemManager;
            this.memberRolesFlag = member.memberRolesFlag;
            this.pendingMessages = member.pendingMessages;
            this.isOnline = member.isOnline;
            this.roles = new ConcurrentHashMap<>();//todo: call the roler constructor
            this.systemManager = null;//todo: call system manager
            this.cart = member.cart;//todo: load cart lazily
            this.isLoaded = true;

        }
    }



    @Transactional
    public void removeCart(){
        cart.removeAllCart();
        Cart membercart = cart;
        cart = null;
        DALService.cartRepository.save(membercart);
        DALService.memberRepository.save(this);
        DALService.cartRepository.delete(membercart);
        DALService.memberRepository.delete(this);
    }






/*
    public void setAbstractOwner(AbstractStoreOwner owner){
        this.abstractOwner = owner;
    }*/
}
