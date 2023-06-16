package UnitTests;

import DomainLayer.*;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
enum  ManagerPermissions{
    getStoreDeals,
    manageStock,
    getWorkersInfo,
    manageStoreDiscountPolicies,
    manageStorePaymentPolicies
}
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class permissionsTests {
    private StoreManager storeManager;
    @Mock
    private Member member;

    @Mock
    private Store store;

    @Mock
    private AbstractStoreOwner storeOwner;

    @BeforeAll
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        storeManager = new StoreManager(member);
    }

    @BeforeEach
    public void beforeEachTest(){
        storeManager = new StoreManager(member);
    }

    @Test
    public void add_permission_for_store_with_null_name_failure(){
        try{
            assertThrows(Exception.class,
                    () -> {storeManager.addPermissionForStore(null, 1);});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_manager_permission_for_unavailable_store_failure(){
        try{
            assertThrows(Exception.class,
                    () -> {storeManager.addPermissionForStore("new store", 1);});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_manager_permission_for_store_twice_failure(){
        try{
            Mockito.when(store.getStoreName()).thenReturn("new store");
            storeManager.appointMemberAsStoreManager(store, storeOwner);
            storeManager.addPermissionForStore("new store", 2);
            assertThrows(Exception.class,
                    () -> {storeManager.addPermissionForStore("new store", 2);});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void add_manager_permission_for_store_success(){
        try{
            Mockito.when(store.getStoreName()).thenReturn("new store");
            Assertions.assertTrue(storeManager.appointMemberAsStoreManager(store, storeOwner));
            Assertions.assertTrue(storeManager.addPermissionForStore("new store", 2));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void get_permission_by_id_with_null_value_failure(){
        try{
            assertThrows(Exception.class,
                    () -> {storeManager.getPermissionById(null);});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void get_permission_by_id_with_value_out_of_bounds_failure(){
        try{
            assertThrows(Exception.class,
                    () -> {storeManager.getPermissionById(20);});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void get_permission_by_id_with_value_out_of_bounds_2_failure(){
        try{
            assertThrows(Exception.class,
                    () -> {storeManager.getPermissionById(0);});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void get_permission_by_id_success(){
        try{
            Assertions.assertEquals("manageStock", storeManager.getPermission(2));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void get_manager_permissions_for_store_success(){
        try{
            Mockito.when(store.getStoreName()).thenReturn("new store");
            Assertions.assertTrue(storeManager.appointMemberAsStoreManager(store, storeOwner));
            Assertions.assertTrue(storeManager.addPermissionForStore("new store", 2));
            List<Integer> result = storeManager.getManagerPermissionsForStore("new store");
            Assertions.assertEquals(2, result.size());
            Assertions.assertEquals(1, result.get(0));
            Assertions.assertEquals(2, result.get(1));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_manager_permissions_for_store_success(){
        try{
            Mockito.when(store.getStoreName()).thenReturn("new store");
            Assertions.assertTrue(storeManager.appointMemberAsStoreManager(store, storeOwner));
            Assertions.assertTrue(storeManager.addPermissionForStore("new store", 2));
            List<Integer> newPermissions = new ArrayList<>();
            newPermissions.add(0, 3);
            newPermissions.add(1, 4);
            storeManager.updateManagerPermissionsForStore("new store", newPermissions);
            List<Integer> result = storeManager.getManagerPermissionsForStore("new store");
            Assertions.assertEquals(2, result.size());
            Assertions.assertEquals(3, result.get(0));
            Assertions.assertEquals(4, result.get(1));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_manager_permissions_for_store_with_null_value_failure(){
        try{
            Mockito.when(store.getStoreName()).thenReturn("new store");
            Assertions.assertTrue(storeManager.appointMemberAsStoreManager(store, storeOwner));
            Assertions.assertTrue(storeManager.addPermissionForStore("new store", 2));
            List<Integer> newPermissions = new ArrayList<>();
            newPermissions.add(0, 3);
            newPermissions.add(1, 4);
            assertThrows(Exception.class,
                    () -> {storeManager.updateManagerPermissionsForStore(null, newPermissions);});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_manager_permissions_for_store_with_null_permissions_failure(){
        try{
            Mockito.when(store.getStoreName()).thenReturn("new store");
            Assertions.assertTrue(storeManager.appointMemberAsStoreManager(store, storeOwner));
            Assertions.assertTrue(storeManager.addPermissionForStore("new store", 2));
            assertThrows(Exception.class,
                    () -> {storeManager.updateManagerPermissionsForStore("new store", null);});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update_manager_permissions_for_unavailable_store_failure(){
        try{
            Mockito.when(store.getStoreName()).thenReturn("new store");
            List<Integer> newPermissions = new ArrayList<>();
            newPermissions.add(0, 3);
            newPermissions.add(1, 4);
            assertThrows(Exception.class,
                    () -> {storeManager.updateManagerPermissionsForStore("new store", newPermissions);});
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
}

