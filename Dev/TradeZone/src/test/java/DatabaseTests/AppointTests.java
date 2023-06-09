package DatabaseTests;

import DTO.OwnerContractDTO;
import DomainLayer.StoreFounder;
import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class AppointTests {

    private ProxyBridge proxy= new ProxyBridge(new RealBridge());
    private String user1;
    private String user2;
    private String user3;
    private String user4;
    String member1Name = "member1name";
    String member1Password = "Aa12345678";


    String member2Name = "member2name";
    String member2Password = "Bb129271346";

    String member3Name = "member3name";
    String member3Password = "kK129B71346";

    String store1Name = "store1";
    String store1bName = "store1b";

    String product1_store1 = "product1_store1";
    String category1_product1_store1 = "category1_product1_store1";
    String product2_store1 = "product2_store1";
    String category2_product2_store1 = "category2_product2_store1";

    String product1_store1b = "product1_store1b";
    String product2_store1b = "product2_store1b";
    String category_store1b = "category_store1b";

    String store2Name = "store2";
    String product1_store2 = "product1_store2";
    String product2_store2 = "product2_store2";
    String product3_store2 = "product3_store2";
    String category_store2 = "category_store2";


    String store2bName = "store2b";

    String store3Name = "store3";



    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("setup");
        proxy= new ProxyBridge(new RealBridge());
        proxy.loadData();
        user1 = proxy.enterMarket();
        user2 = proxy.enterMarket();
        user3 = proxy.enterMarket();
        user4 = proxy.enterMarket();
    }

    //login Test
    @Test
    public void appointOwner(){
        try{
            proxy.register(user1, member1Name, member1Password);
            proxy.register(user2, member2Name, member2Password);

            user1 = proxy.login(user1, member1Name, member1Password);
            user2 = proxy.login(user2, member2Name, member2Password);

            proxy.register(user3, member3Name, member3Password);
            user3 =proxy.login(user3, member3Name, member3Password);


            ////// create stores
            proxy.createStore(member1Name, store1Name);
            proxy.createStore(member1Name, store1bName);

            proxy.createStore(member2Name, store2Name);
            proxy.createStore(member2Name, store2bName);

            proxy.createStore(member3Name, store3Name);

            proxy.appointOtherMemberAsStoreOwner(member1Name,store1Name,member2Name);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertFalse(proxy.getStoreOwnersNames(member1Name,store1Name).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(member1Name, store1Name).contains(member2Name));

            proxy.appointOtherMemberAsStoreOwner(member1Name,store1Name,member3Name);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();


            Assertions.assertFalse(proxy.getMyCreatedContracts(member1Name,store1Name).isEmpty());

            Assertions.assertFalse(proxy.getStoreOwnersNames(member1Name,store1Name).isEmpty());
            Assertions.assertEquals(proxy.getStoreOwnersNames(member1Name,store1Name).size(),1);
            Assertions.assertTrue(proxy.getStoreOwnersNames(member1Name, store1Name).contains(member2Name));

            List<OwnerContractDTO> ownerContractDTOS =  proxy.getMyCreatedContracts(member1Name,store1Name);
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertTrue(ownerContractDTOS.size()==1);
            OwnerContractDTO ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertTrue(ownerContractDTO.triggerOwner.equals(member1Name));
            Assertions.assertTrue(ownerContractDTO.newOwner.equals(member3Name));
            Assertions.assertTrue(ownerContractDTO.storeName.equals(store1Name));
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.size()==1);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.contains(member2Name));
            Assertions.assertFalse(ownerContractDTO.isContractDone);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("in progress"));

            Assertions.assertTrue(proxy.fillOwnerContract(member2Name,store1Name,member3Name,true));

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            ownerContractDTOS =  proxy.getMyCreatedContracts(member1Name,store1Name);
            Assertions.assertTrue(ownerContractDTOS.size()==0);
            ownerContractDTOS =  proxy.getAlreadyDoneContracts(member1Name,store1Name);
            Assertions.assertTrue(ownerContractDTOS.size()==1);
            ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.size()==1);
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.contains(member2Name));
            Assertions.assertTrue(ownerContractDTO.pendingOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.isContractDone);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("all owners have accepted this contract and it is done"));

            Assertions.assertTrue(proxy.getStoreOwnersNames(member1Name, store1Name).contains(member3Name));

            proxy.removeOwnerByHisAppointer(member1Name,store1Name,member3Name);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertTrue(proxy.getStoreOwnersNames(member1Name, store1Name).contains(member2Name));
            Assertions.assertFalse(proxy.getStoreOwnersNames(member1Name, store1Name).contains(member3Name));

            proxy.appointOtherMemberAsStoreOwner(member2Name,store1Name,member3Name);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertFalse(proxy.getMyCreatedContracts(member2Name,store1Name).isEmpty());

            Assertions.assertFalse(proxy.getStoreOwnersNames(member2Name,store1Name).isEmpty());
            Assertions.assertEquals(proxy.getStoreOwnersNames(member2Name,store1Name).size(),1);
            List<String> owners = proxy.getStoreOwnersNames(member2Name,store1Name);
            System.out.println(owners.toString());
            Assertions.assertFalse(proxy.getStoreOwnersNames(member2Name, store1Name).contains(member3Name));

            ownerContractDTOS =  proxy.getMyCreatedContracts(member2Name,store1Name);
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertTrue(ownerContractDTOS.size()==1);
            ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertTrue(ownerContractDTO.triggerOwner.equals(member2Name));
            Assertions.assertTrue(ownerContractDTO.newOwner.equals(member3Name));
            Assertions.assertTrue(ownerContractDTO.storeName.equals(store1Name));
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.size()==1);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.contains(member1Name));
            Assertions.assertFalse(ownerContractDTO.isContractDone);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("in progress"));

            Assertions.assertTrue(proxy.fillOwnerContract(member1Name,store1Name,member3Name,true));

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            ownerContractDTOS =  proxy.getMyCreatedContracts(member2Name,store1Name);
            Assertions.assertTrue(ownerContractDTOS.size()==0);
            ownerContractDTOS =  proxy.getAlreadyDoneContracts(member2Name,store1Name);
            Assertions.assertTrue(ownerContractDTOS.size()==1);
            ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.size()==1);
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.contains(member1Name));
            Assertions.assertTrue(ownerContractDTO.pendingOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.isContractDone);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("all owners have accepted this contract and it is done"));

            Assertions.assertEquals(proxy.getStoreOwnersNames(member1Name,store1Name).size(),2);
            Assertions.assertTrue(proxy.getStoreOwnersNames(member1Name, store1Name).contains(member3Name));
            Assertions.assertTrue(proxy.getStoreOwnersNames(member1Name, store1Name).contains(member2Name));

//            Assertions.assertEquals(proxy.getOwnerAppointer(member2Name,store1Name),member1Name);
//            Assertions.assertEquals(proxy.getOwnerAppointer(member3Name,store1Name),member2Name);
            // TODO MEMBER 3 IS NOT FOUNDER FOR STORE
            Assertions.assertTrue(proxy.removeOwnerByHisAppointer(member1Name,store1Name,member2Name));

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(proxy.getStoreOwnersNames(member1Name,store1Name).size(),0);
            Assertions.assertFalse(proxy.getStoreOwnersNames(member1Name, store1Name).contains(member1Name));
            Assertions.assertEquals(proxy.getStoreFounderName(member1Name,store1Name),member1Name);

            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(member1Name,store1Name,member2Name));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreManager(member2Name,store1Name,member3Name));

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertEquals(proxy.getStoreManagersNames(member1Name,store1Name).size(),1);
            Assertions.assertTrue(proxy.getStoreManagersNames(member1Name, store1Name).contains(member3Name));

            List<Integer> permissions = proxy.getManagerPermissionsForStore(member1Name,store1Name,member3Name);

            Assertions.assertFalse(permissions.isEmpty());
            Assertions.assertEquals(permissions.size(),1);
            Assertions.assertEquals(permissions.get(0),1);

            permissions.add(2);

            proxy.updateManagerPermissionsForStore(member1Name,store1Name,member3Name,permissions);

            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertFalse(permissions.isEmpty());
            Assertions.assertEquals(permissions.size(),2);
            Assertions.assertEquals(permissions.get(0),1);
            Assertions.assertEquals(permissions.get(1),2);


            logOutMembers();
            initSystemServiceAndLoadDataAndLogIn();
            permissions = proxy.getManagerPermissionsForStore(member1Name,store1Name,member3Name);

            Assertions.assertFalse(permissions.isEmpty());
            Assertions.assertEquals(permissions.size(),2);
            Assertions.assertEquals(permissions.get(0),1);
            Assertions.assertEquals(permissions.get(1),2);

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    private void logOutMembers()throws Exception{
        proxy.memberLogOut(member1Name);
        proxy.memberLogOut(member2Name);
        proxy.memberLogOut(member3Name);
    }
    private void initSystemServiceAndLoadDataAndLogIn() throws Exception {
        proxy= new ProxyBridge(new RealBridge());
        proxy.loadData();
        user1 = proxy.enterMarket();
        user2 = proxy.enterMarket();
        user3 = proxy.enterMarket();
        user4 = proxy.enterMarket();
        user1 = proxy.login(user1, member1Name, member1Password);
        user2 = proxy.login(user2, member2Name, member2Password);
        user3 =proxy.login(user3, member3Name, member3Password);
    }



}
