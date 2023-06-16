package DomainLayer;

import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.Controller.StoreMapper;
import DataAccessLayer.DALService;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
enum  ManagerPermissions{
    getStoreDeals,
    manageStock,
    getWorkersInfo,
    manageStoreDiscountPolicies,
    manageStorePaymentPolicies
}

@Entity
@Table(name = "StoreManager")
@PrimaryKeyJoinColumn(name = "member_name")
public class StoreManager extends Role implements Serializable {

    @Transient
    private ConcurrentHashMap<String, Set<ManagerPermissions>> managedStoresPermissions;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "manager_permission")
    @Column(name = "permission")
    @Enumerated(EnumType.STRING)
    protected Set<ManagerPermissions> managerPermissions;

    public StoreManager(Member member) {
        super(member);
        this.myRole = RoleEnum.StoreManager;
        this.managedStoresPermissions = new ConcurrentHashMap<>();
        managerPermissions = new HashSet<>();
    }
    public StoreManager(){
    }
    public boolean assertHasPermissionForStore(String storeName, ManagerPermissions permission) throws Exception {
        if(!hasPermissionForStore(storeName,permission))
            throw new Exception(this.getUserName()+ " does not have permission to "+permission.toString() +" for store "+ storeName);

        return true;
    }

    public boolean hasPermissionForStore(String storeName, ManagerPermissions permission) throws Exception {
        if(storeName==null)
            throw new Exception("storeName cant be null");
        storeName = storeName.strip().toLowerCase();
        if(! managedStoresPermissions.containsKey(storeName))
            throw new Exception(this.getUserName()+ " is not Manager for store "+ storeName);
        if(!managedStoresPermissions.get(storeName).contains(permission))
            return false;

        return true;
    }

    public boolean addPermissionForStore(String storeName, Integer permissionId) throws Exception {
        if(storeName==null)
            throw new Exception("storeName cant be null");
        storeName = storeName.strip().toLowerCase();
        if(! managedStoresPermissions.containsKey(storeName))
            throw new Exception(this.getUserName()+ " is not Manager for store "+ storeName);
        //for
        ManagerPermissions permission = getPermissionById(permissionId);

        //remove if
        if(managedStoresPermissions.get(storeName).contains(permission))
            throw new Exception(this.getUserName()+ " already have permission to "+permission.toString() +" for store "+ storeName);

        managedStoresPermissions.get(storeName).add(permission);
        managerPermissions.add(permission);
        return true;
    }

    public ManagerPermissions getPermissionById(Integer permissionId) throws Exception {
        if(permissionId == null)
            throw new Exception("permission Id cant be null");
        if(permissionId<1 || permissionId>5 )
            throw new Exception("permission Id have to be between 1 and 5");
        switch (permissionId){
            case 1: return ManagerPermissions.getStoreDeals;
            case 2: return ManagerPermissions.manageStock;
            case 3: return ManagerPermissions.getWorkersInfo;
            case 4: return ManagerPermissions.manageStoreDiscountPolicies;
            default: return ManagerPermissions.manageStorePaymentPolicies;
        }
    }

    public static List<String> getAllPermissions(){
        List<String> allPermissions = new ArrayList<>();
        allPermissions.add("Get store deals.");
        allPermissions.add("Manage stock.");
        allPermissions.add("Get workers information.");
        allPermissions.add("Manage store discount policies.");
        allPermissions.add("Manage store payment policies.");
        return allPermissions;
    }

    public List<Integer> getManagerPermissionsForStore(String storeName) throws Exception {
        List<Integer> allPermissions = new ArrayList<>();
        for (int i=1; i<=5; i++){
            if(hasPermissionForStore(storeName, getPermissionById(i)))
                allPermissions.add(i);
        }
        return allPermissions;
    }

    public boolean updateManagerPermissionsForStore(String storeName, List<Integer> newPermissions) throws Exception {
        if(storeName==null)
            throw new Exception("storeName cant be null");
        if(newPermissions==null)
            throw new Exception("newPermissions cant be null");
        storeName = storeName.strip().toLowerCase();
        if(! managedStoresPermissions.containsKey(storeName))
            throw new Exception(this.getUserName()+ " is not Manager for store "+ storeName);

        Set<ManagerPermissions> newPermSet = new HashSet<ManagerPermissions>();
        for(Integer permissionId :newPermissions)
            newPermSet.add(getPermissionById(permissionId));

        managedStoresPermissions.put(storeName, newPermSet);

        return true;
    }

    public boolean appointMemberAsStoreManager(Store store, AbstractStoreOwner myBoss) throws Exception {
        super.appointMemberAsStoreManager(store,myBoss);
        String storeName= store.getStoreName();
        if(storeName==null)
            throw new Exception("storeName cant be null");
        storeName = storeName.strip().toLowerCase();
        if(managedStoresPermissions.containsKey(storeName))
            throw new Exception(this.getUserName()+ " is already Manager for store "+ storeName);

        managedStoresPermissions.put(storeName, new HashSet<ManagerPermissions>());
        managedStoresPermissions.get(storeName).add(ManagerPermissions.getStoreDeals);
        managerPermissions.add(ManagerPermissions.getStoreDeals);
        return true;
    }

    public void removeStore(String storeName){
        removeOneStore(storeName);
        managedStoresPermissions.remove(storeName);
    }

    public boolean removeMemberAsStoreManager(Store store, AbstractStoreOwner myBoss) throws Exception {
        String storeName = store.getStoreName();
        if(!store.isAlreadyStoreManager(getUserName()))
            throw new Exception("member "+getUserName()+" is not store Manager for store " + storeName);

        if(!responsibleForStores.containsKey(storeName))
            throw new Exception("member "+getUserName()+" is not store Manager for store " + storeName);

        if(!myBossesForStores.get(storeName).getUserName().equals(myBoss.getUserName()))
            throw new Exception(myBoss.getUserName()+" is not my boss for store "+ storeName);

        store.removeManager(getUserName());
        myBossesForStores.remove(storeName);
        responsibleForStores.remove(storeName);
        managedStoresPermissions.remove(storeName);

        return true;
    }

    public void loadManager() throws Exception {
        if (!isLoaded) {
            if (Market.dbFlag) {
                List<String> storesNames = DALService.storesManagersRepository.getStoreNameByMemberName(getUserName());
                for (String storeName : storesNames) {
                    responsibleForStores.put(storeName, StoreMapper.getInstance().getStore(storeName));
                    Store store = responsibleForStores.get(storeName);
                    store.addStoreManager(getUserName(), this);
                    Map<String, String> bossTypeAndName = DALService.storesOwnersRepository.findBossById(getUserName(), storeName);
                    String bossType = bossTypeAndName.keySet().stream().toList().get(0);
                    String bossName = bossTypeAndName.get(bossType);
                    if (bossType.equals(RoleEnum.StoreFounder.toString())) {
                        StoreFounder myBoss = MemberMapper.getInstance().getStoreFounder(bossName);
                        myBossesForStores.put(storeName, myBoss);
                        if (!store.alreadyHaveFounder()) {
                            store.setStoreFounder(myBoss);
                        }
                    }
                    if (bossType.equals(RoleEnum.StoreOwner.toString())) {
                        StoreOwner myBoss = MemberMapper.getInstance().getStoreOwner(bossName);
                        myBossesForStores.put(bossName, myBoss);
                        store.addStoreOwner(myBoss.getUserName(), myBoss);
                    }
                    //todo: chcek if should add the owners and managers to the store
                    List<String> managerStorePermissions = DALService.storesManagersRepository.findManagerPermissionsPerStore(getUserName(), storeName);
                    Set<ManagerPermissions> managerPermissions1 = new HashSet<>();
                    for (String permission : managerStorePermissions) {
                        managerPermissions.add(convertPermissionString2PermissionType(permission));
                        managerPermissions1.add(convertPermissionString2PermissionType(permission));
                    }
                    managedStoresPermissions.put(storeName, managerPermissions1);
                }
            }
            isLoaded = true;
        }
    }

    private ManagerPermissions convertPermissionString2PermissionType(String permission) throws Exception{
        if (ManagerPermissions.getStoreDeals.toString().equals(permission)) return ManagerPermissions.getStoreDeals;
        if (ManagerPermissions.manageStock.toString().equals(permission)) return ManagerPermissions.manageStock;
        if (ManagerPermissions.getWorkersInfo.toString().equals(permission)) return ManagerPermissions.getWorkersInfo;
        if (ManagerPermissions.manageStoreDiscountPolicies.toString().equals(permission)) return ManagerPermissions.manageStoreDiscountPolicies;
        if (ManagerPermissions.manageStorePaymentPolicies.toString().equals(permission)) return ManagerPermissions.manageStorePaymentPolicies;
        throw new Exception("permission " + permission + " not found");
    }
}
