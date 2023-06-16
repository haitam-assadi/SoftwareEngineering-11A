package AcceptanceTests;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.*;

import java.util.List;
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class StoreManagerTests {
    String member1Name = "member1";
    String member1Password = "Aa123456";

    String member2Name = "member2";
    String member2Password = "Aa123456";

    String member3Name = "member3";
    String member3Password = "Aa123456";

    private String storeFounder;

    private String newEmployee;

    private String storeName;

    private ProxyBridge proxy= new ProxyBridge(new RealBridge());

    private String user1;
    private String user2;

    private String user3;

    @BeforeAll
    public void setUp() throws Exception {
        System.out.println("setup");
        if(proxy.initializeMarket().isEmpty()){
            System.out.println("exception thrown");
        }
        user1 = proxy.enterMarket();
        proxy.register(user1, member1Name, member1Password);
        storeFounder = proxy.login(user1, member1Name, member1Password);
        storeName = proxy.createStore(storeFounder, "New Store");
        user2 = proxy.enterMarket();
        proxy.register(user2, member2Name, member2Password);
        user3 = proxy.enterMarket();
        proxy.register(user3, member3Name, member3Password);
    }

    /*
    1. appointing member as store manager and checking if he has the default permissions - success
    2. appoint member as store manager without being a store founder or store owner
    */

    /*
        case 1: return ManagerPermissions.getStoreDeals;
            case 2: return ManagerPermissions.manageStock;
            case 3: return ManagerPermissions.getWorkersInfo;
            case 4: return ManagerPermissions.manageStoreDiscountPolicies;
            default: return ManagerPermissions.manageStorePaymentPolicies;
    */

    @Test
    public void appoint_member_as_store_manager_by_store_owner_success(){
        try{
            user2 = proxy.enterMarket();
            String new_user = proxy.enterMarket();
            String new_name = "newname";
            String new_pass = "newpass12345";
            proxy.register(new_user, new_name, new_pass);
            proxy.login(new_user, new_name, new_pass);
            proxy.takeDownSystemManagerAppointment(storeName, member2Name);
            proxy.appointOtherMemberAsStoreOwner(storeFounder, storeName, new_name);
            proxy.login(user3, member3Name, member3Password);
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreManager(new_name, storeName, member3Name));
            Assertions.assertTrue(proxy.getStoreManagersNames(member1Name, storeName).contains(member3Name));
            //Founder can't get permissions of manager, because he didn't appoint him???
            Assertions.assertEquals(proxy.getManagerPermissionsForStore(new_name, storeName, member3Name).get(0), 1);
            user3 = proxy.memberLogOut(member3Name);
            proxy.takeDownSystemManagerAppointment(storeName, member3Name);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_registered_member_as_store_manager_success(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreManager(storeFounder, storeName, member2Name));
            Assertions.assertTrue(proxy.getStoreManagersNames(member1Name, storeName).contains(member2Name));
            Assertions.assertEquals(proxy.getManagerPermissionsForStore(storeFounder, storeName, member2Name).get(0), 1);
            proxy.takeDownSystemManagerAppointment(storeName, member2Name);

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_logged_in_member_as_store_manager_success(){
        try{
            proxy.login(user3, member3Name, member3Password);
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreManager(storeFounder, storeName, member3Name));
            Assertions.assertTrue(proxy.getStoreManagersNames(member1Name, storeName).contains(member3Name));
            Assertions.assertEquals(proxy.getManagerPermissionsForStore(storeFounder, storeName, member3Name).get(0), 1);
            user3 = proxy.memberLogOut(member3Name);
            proxy.takeDownSystemManagerAppointment(storeName, member3Name);
            proxy.takeDownSystemManagerAppointment(storeName, member2Name);

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


    @Test
    public void appoint_member_as_store_manager_by_another_store_manager_failure(){
        try{
            proxy.login(user2, member2Name, member2Password);
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreManager(storeFounder, storeName, member2Name));
            Exception exception = Assertions.assertThrows(Exception.class ,() -> proxy.appointOtherMemberAsStoreManager(member2Name, storeName, member3Name));

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
}
