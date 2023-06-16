package AcceptanceTests;

import DTO.OwnerContractDTO;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class OwnerTests {
    private ProxyBridge proxy;
    private String user;
    String moslemUserName = "moslem123";
    String moslemPassword = "Aa123456";
    private String store_founder;
    private String member_name1;
    private String member_name2;
    private String storeName;

    @BeforeEach
    public void setUp() throws Exception {
        proxy = new ProxyBridge(new RealBridge());
        if(proxy.initializeMarket().isEmpty()){
            throw new Exception(""); // should change
        }

        // founder
        user = proxy.enterMarket();//guest default user name
        proxy.register(user,moslemUserName,moslemPassword);
        store_founder = proxy.login(user,moslemUserName,moslemPassword);
        storeName = proxy.createStore(store_founder, "Moslem Store");


        user = proxy.enterMarket();//guest default user name
        proxy.register(user,"brain123","asdf123456");
        member_name1 = proxy.login(user,"brain123","asdf123456");


        user = proxy.enterMarket();//guest default user name
        proxy.register(user,"harry123","Hh1111456");
//        member_name2 = "harry123"; // delete?
        member_name2 = proxy.login(user, "harry123","Hh1111456");
    }

    @Test
    public void appoint_member_as_store_owner_from_owner_success(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
//            List<String> alreadyAcceptedOwners = new LinkedList<>();
//            OwnerContractDTO ownerContractDTO  = new OwnerContractDTO(store_founder,member_name1,storeName,"contract not done",alreadyAcceptedOwners,alreadyAcceptedOwners,false);
//            Assertions.assertEquals(proxy.getMyCreatedContracts(store_founder,storeName).get(0).triggerOwner,ownerContractDTO.triggerOwner);
            //should check also managing policy to be as the founder/the owner that makes him owner
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_owner_with_accept_the_other_owners_success(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name2));

            List<OwnerContractDTO> ownerContractDTOS =  proxy.getMyCreatedContracts(store_founder,storeName);
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertTrue(ownerContractDTOS.size()==1);
            OwnerContractDTO ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertTrue(ownerContractDTO.triggerOwner.equals(store_founder));
            Assertions.assertTrue(ownerContractDTO.newOwner.equals(member_name2));
            Assertions.assertTrue(ownerContractDTO.storeName.equals(storeName));
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.size()==1);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.contains(member_name1));
            Assertions.assertFalse(ownerContractDTO.isContractDone);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("in progress"));


            Assertions.assertTrue(proxy.fillOwnerContract(member_name1,storeName,member_name2,true));
            ownerContractDTOS =  proxy.getMyCreatedContracts(store_founder,storeName);
            Assertions.assertTrue(ownerContractDTOS.size()==0);
            ownerContractDTOS =  proxy.getAlreadyDoneContracts(store_founder,storeName);
            Assertions.assertTrue(ownerContractDTOS.size()==1);
            ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.size()==1);
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.contains(member_name1));
            Assertions.assertTrue(ownerContractDTO.pendingOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.isContractDone);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("all owners have accepted this contract and it is done"));


            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name2));
//            List<String> alreadyAcceptedOwners = new LinkedList<>();
//            OwnerContractDTO ownerContractDTO  = new OwnerContractDTO(store_founder,member_name1,storeName,"contract not done",alreadyAcceptedOwners,alreadyAcceptedOwners,false);
//            Assertions.assertEquals(proxy.getMyCreatedContracts(store_founder,storeName).get(0).triggerOwner,ownerContractDTO.triggerOwner);
            //should check also managing policy to be as the founder/the owner that makes him owner
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

}
