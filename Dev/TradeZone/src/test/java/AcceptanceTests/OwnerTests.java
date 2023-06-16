package AcceptanceTests;

import DTO.OwnerContractDTO;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class OwnerTests {
    private ProxyBridge proxy;
    private String user;
    String moslemUserName = "moslem123";
    String moslemPassword = "Aa123456";
    private String store_founder;
    private String member_name1;
    private String member_name2;
    private String member_name3;
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

        user = proxy.enterMarket();//guest default user name
        proxy.register(user,"haitam123","Hh1111456");
        member_name3 = proxy.login(user, "haitam123","Hh1111456");
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
    public void appoint_member_as_store_owner_with_accept_the_other_one_owner_success(){
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

            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name3));
            ownerContractDTOS =  proxy.getMyCreatedContracts(store_founder,storeName);
            ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertEquals(0, ownerContractDTO.alreadyAcceptedOwners.size());
            Assertions.assertEquals(2,ownerContractDTO.pendingOwners.size());
            Assertions.assertTrue(ownerContractDTO.pendingOwners.contains(member_name1));
            Assertions.assertTrue(ownerContractDTO.pendingOwners.contains(member_name2));
            Assertions.assertFalse(ownerContractDTO.isContractDone);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("in progress"));

            Assertions.assertTrue(proxy.fillOwnerContract(member_name1,storeName,member_name3,true));
            ownerContractDTOS =  proxy.getMyCreatedContracts(store_founder,storeName);
            ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertEquals(1, ownerContractDTO.alreadyAcceptedOwners.size());
            Assertions.assertEquals(1,ownerContractDTO.pendingOwners.size());

            Assertions.assertTrue(proxy.fillOwnerContract(member_name2,storeName,member_name3,true));
            ownerContractDTOS =  proxy.getAlreadyDoneContracts(store_founder,storeName);
            ownerContractDTO = ownerContractDTOS.get(1);
            Assertions.assertEquals(2, ownerContractDTO.alreadyAcceptedOwners.size());
            Assertions.assertEquals(0,ownerContractDTO.pendingOwners.size());

            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name2));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name3));

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_owner_with_one_owner_not_accept_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name2));

            List<OwnerContractDTO> ownerContractDTOS =  proxy.getMyCreatedContracts(store_founder,storeName);
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertTrue(ownerContractDTOS.size()==1);
            OwnerContractDTO ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.size()==1);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.contains(member_name1));
            Assertions.assertFalse(ownerContractDTO.isContractDone);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("in progress"));


            Assertions.assertTrue(proxy.fillOwnerContract(member_name1,storeName,member_name2,false));
            ownerContractDTOS =  proxy.getMyCreatedContracts(store_founder,storeName);
            Assertions.assertTrue(ownerContractDTOS.size()==0);
            ownerContractDTOS =  proxy.getAlreadyDoneContracts(store_founder,storeName);
            Assertions.assertTrue(ownerContractDTOS.size()==1);
            ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.isContractDone);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("contract is rejected by "+member_name1));


            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).size()==1);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_owner_with_deleting_one_owner_that_accepting_success(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name2));

            List<OwnerContractDTO> ownerContractDTOS =  proxy.getMyCreatedContracts(store_founder,storeName);
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertTrue(ownerContractDTOS.size()==1);
            OwnerContractDTO ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.size()==1);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.contains(member_name1));
            Assertions.assertFalse(ownerContractDTO.isContractDone);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("in progress"));


            Assertions.assertTrue(proxy.removeOwnerByHisAppointer(store_founder,storeName,member_name1));

            ownerContractDTOS =  proxy.getMyCreatedContracts(store_founder,storeName);
            Assertions.assertTrue(ownerContractDTOS.size()==0);
            ownerContractDTOS =  proxy.getAlreadyDoneContracts(store_founder,storeName);
            Assertions.assertTrue(ownerContractDTOS.size()==1);
            ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.isContractDone);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("owner "+ member_name1 +" has been removed before accepting, contract is rejected."));


            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).size()==0);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_store_owner_by_his_appointer_success(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name2));

            List<OwnerContractDTO> ownerContractDTOS =  proxy.getMyCreatedContracts(store_founder,storeName);
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertTrue(ownerContractDTOS.size()==1);
            OwnerContractDTO ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.size()==1);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.contains(member_name1));
            Assertions.assertFalse(ownerContractDTO.isContractDone);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("in progress"));


            Assertions.assertTrue(proxy.removeOwnerByHisAppointer(store_founder,storeName,member_name1));
            ownerContractDTOS =  proxy.getMyCreatedContracts(store_founder,storeName);
            Assertions.assertTrue(ownerContractDTOS.size()==0);
            ownerContractDTOS =  proxy.getAlreadyDoneContracts(store_founder,storeName);
            Assertions.assertTrue(ownerContractDTOS.size()==1);
            ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.isContractDone);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("owner "+ member_name1 +" has been removed before accepting, contract is rejected."));


            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).size()==0);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_owner_and_the_owners_he_owner_for_it_by_his_appointed_success(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(member_name1,storeName,member_name2));
            Assertions.assertTrue(proxy.fillOwnerContract(store_founder,storeName,member_name2,true));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name2));

            Assertions.assertTrue(proxy.removeOwnerByHisAppointer(store_founder,storeName,member_name1));

            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).isEmpty());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_owner_that_i_ancestor_for_him_success(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(member_name1,storeName,member_name2));
            Assertions.assertTrue(proxy.fillOwnerContract(store_founder,storeName,member_name2,true));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name2));

            Assertions.assertTrue(proxy.removeOwnerByHisAppointer(store_founder,storeName,member_name2));

            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertFalse(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name2));

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_owner_that_i_not_owner_for_the_store_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(member_name1,storeName,member_name2));
            Assertions.assertTrue(proxy.fillOwnerContract(store_founder,storeName,member_name2,true));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name2));

            Assertions.assertThrows(Exception.class, () -> proxy.removeOwnerByHisAppointer(member_name3,storeName,member_name2));

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_owner_that_i_not_ancestor_for_him_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name2));
            Assertions.assertTrue(proxy.fillOwnerContract(member_name1,storeName,member_name2,true));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name2));

            Assertions.assertThrows(Exception.class, () -> proxy.removeOwnerByHisAppointer(member_name1,storeName,member_name2));

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_owner_that_make_a_contract_but_not_done(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(member_name1,storeName,member_name2));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));

            List<OwnerContractDTO> ownerContractDTOS =  proxy.getMyCreatedContracts(member_name1,storeName);
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertTrue(ownerContractDTOS.size()==1);
            OwnerContractDTO ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertTrue(ownerContractDTO.alreadyAcceptedOwners.size()==0);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.size()==1);
            Assertions.assertTrue(ownerContractDTO.pendingOwners.contains(store_founder));
            Assertions.assertFalse(ownerContractDTO.isContractDone);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("in progress"));

            Assertions.assertTrue(proxy.removeOwnerByHisAppointer(store_founder,storeName,member_name1));

            Assertions.assertThrows(Exception.class, () -> proxy.getMyCreatedContracts(member_name1,storeName));
            Assertions.assertThrows(Exception.class, () -> proxy.getAlreadyDoneContracts(member_name1,storeName));

            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).isEmpty());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_owner_not_member_fail(){
        try{
            Assertions.assertThrows(Exception.class, () -> proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,"koko"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //circular appointment

    @Test
    public void appoint_member_as_store_owner_1circular_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertThrows(Exception.class, () -> proxy.appointOtherMemberAsStoreOwner(member_name1,storeName,member_name1));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    public void appoint_member_as_store_owner_2circular_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertThrows(Exception.class, () -> proxy.appointOtherMemberAsStoreOwner(member_name1,storeName,store_founder));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder,storeName).contains(member_name1));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    //circular appointment
    @Test
    public void appoint_member_as_store_owner_3circular_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(member_name1,storeName,member_name2));
            Assertions.assertTrue(proxy.fillOwnerContract(store_founder,storeName,member_name2,true));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name2));
            Assertions.assertThrows(Exception.class, () -> proxy.appointOtherMemberAsStoreOwner(member_name2,storeName,store_founder));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_my_owner_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(member_name1,storeName,member_name2));
            Assertions.assertTrue(proxy.fillOwnerContract(store_founder,storeName,member_name2,true));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name2));
            Assertions.assertThrows(Exception.class, () -> proxy.removeOwnerByHisAppointer(member_name2,storeName,member_name1));
            Assertions.assertThrows(Exception.class, () -> proxy.removeOwnerByHisAppointer(member_name2,storeName,store_founder));

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void create_contract_for_the_same_member_but_the_first_contract_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(member_name1,storeName,member_name2));
            Assertions.assertTrue(proxy.fillOwnerContract(store_founder,storeName,member_name2,false));

            List<OwnerContractDTO> ownerContractDTOS =  proxy.getMyCreatedContracts(member_name1,storeName);
            Assertions.assertTrue(ownerContractDTOS.isEmpty());
            ownerContractDTOS = proxy.getAlreadyDoneContracts(member_name1,storeName);
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            OwnerContractDTO ownerContractDTO = ownerContractDTOS.get(0);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("contract is rejected by "+store_founder));

            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertFalse(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name2));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(member_name1,storeName,member_name2));
            ownerContractDTOS =  proxy.getMyCreatedContracts(member_name1,storeName);
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertTrue(proxy.fillOwnerContract(store_founder,storeName,member_name2,true));

             ownerContractDTOS =  proxy.getMyCreatedContracts(member_name1,storeName);
            Assertions.assertTrue(ownerContractDTOS.isEmpty());
            ownerContractDTOS = proxy.getAlreadyDoneContracts(member_name1,storeName);
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            ownerContractDTO = ownerContractDTOS.get(1);
            Assertions.assertTrue(ownerContractDTO.contractStatus.equals("all owners have accepted this contract and it is done"));


        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_but_already_owner_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertThrows(Exception.class, () -> proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_but_already_have_a_contract_but_not_done_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name2));
            Assertions.assertThrows(Exception.class, () -> proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name2));

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }




    // II.4.6
    //appoint member to be store manager test
    @Test
    public void appoint_member_as_store_manager_from_founder_success(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreManager(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getStoreManagersNames(store_founder,storeName).contains(member_name1));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_owner_as_store_manager_fail(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertThrows(Exception.class, () ->proxy.appointOtherMemberAsStoreManager(store_founder,storeName,member_name1));
            }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_owner_and_managers_that_he_appoint_it_by_his_owner(){
        try{
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getMyCreatedContracts(store_founder,storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreManager(member_name1,storeName,member_name2));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).contains(member_name1));
            Assertions.assertTrue(proxy.getStoreManagersNames(member_name1, storeName).contains(member_name2));
            Assertions.assertTrue(proxy.removeOwnerByHisAppointer(store_founder,storeName,member_name1));
            Assertions.assertTrue(proxy.getStoreOwnersNames(store_founder, storeName).isEmpty());
            Assertions.assertTrue(proxy.getStoreManagersNames(member_name1, storeName).isEmpty());
            }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }



}
