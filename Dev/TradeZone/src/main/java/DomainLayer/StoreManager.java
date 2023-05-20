package DomainLayer;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
enum  ManagerPermissions{
    getStoreDeals,
    addNewProduct,
    removeProduct,
    updateProductInfo,
    getWorkersInfo,
    manageStoreDiscountPolicies,
    manageStorePaymentPolicies
}
public class StoreManager extends Role{

    private ConcurrentHashMap<String, Set<ManagerPermissions>> managedStoresPermissions;

    public StoreManager(Member member) {
        super(member);
        this.myRole = RoleEnum.StoreManager;
        this.managedStoresPermissions = new ConcurrentHashMap<>();
    }
    public boolean assertHasPermissionForStore(String storeName, ManagerPermissions permission) throws Exception {
        if(storeName==null)
            throw new Exception("storeName cant be null");
        storeName = storeName.strip().toLowerCase();
        if(! managedStoresPermissions.containsKey(storeName))
            throw new Exception(this.getUserName()+ " is not Manager for store "+ storeName);
        if(!managedStoresPermissions.get(storeName).contains(permission))
            throw new Exception(this.getUserName()+ " does not have permission to "+permission.toString() +" for store "+ storeName);

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
        return true;
    }

    public ManagerPermissions getPermissionById(Integer permissionId) throws Exception {
        if(permissionId == null)
            throw new Exception("permission Id cant be null");
        if(permissionId<1 || permissionId>7 )
            throw new Exception("permission Id have to be between 1 and 7");
        switch (permissionId){
            case 1: return ManagerPermissions.getStoreDeals;
            case 2: return ManagerPermissions.addNewProduct;
            case 3: return ManagerPermissions.removeProduct;
            case 4: return ManagerPermissions.updateProductInfo;
            case 5: return ManagerPermissions.getWorkersInfo;
            case 6: return ManagerPermissions.manageStoreDiscountPolicies;
            default: return ManagerPermissions.manageStorePaymentPolicies;
        }
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
        return true;
    }

    public void removeStore(String storeName){
        removeOneStore(storeName);
        managedStoresPermissions.remove(storeName);
    }
}
