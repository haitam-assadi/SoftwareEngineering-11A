package UnitTests;

import DTO.DealDTO;
import DTO.OwnerContractDTO;
import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.Controller.StoreMapper;
import DomainLayer.*;
import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class AppointOwnerTests {
        private Store store;

        @Mock
        private StoreFounder founder;
        @Mock
        private StoreOwner owner;

        @Mock
        private Deal deal;

        @Mock
        private DealDTO dealDTO;

        @Mock
        private Stock stock;

        @Mock
        private StoreManager manager;

        @Mock
        private Member member1name;
        @Mock
        private Member member2name;
        @Mock
        private Member member3name;

        @BeforeAll
        public void setUp() throws Exception {
            Market.dbFlag = false;
            StoreMapper.initMapper();
            MemberMapper.initMapper();
            MockitoAnnotations.openMocks(this);
            member1name = new Member("member1","member1Pass");
            member2name = new Member("member2","member2Pass");
            member3name = new Member("member3","member3Pass");
            store = new Store("store1");
            member1name.appointMemberAsStoreFounder(store);
            founder = new StoreFounder(member1name);
            //TODO: does not compile
            //store = new Store(founder);
        }

        @BeforeEach
        public void beforeEachTest() throws Exception {
            Market.dbFlag = false;
            StoreMapper.initMapper();
            MemberMapper.initMapper();
            MockitoAnnotations.openMocks(this);
            member1name = new Member("member1","member1Pass");
            member2name = new Member("member2","member2Pass");
            member3name = new Member("member3","member3Pass");
            store = new Store("store1");
            member1name.appointMemberAsStoreFounder(store);
            founder = new StoreFounder(member1name);
            //TODO: does not compile
            //store = new Store(founder);
        }

    @Test
    public void appoint_member_as_store_owner_success(){
        try {
            Assertions.assertTrue(member1name.appointOtherMemberAsStoreOwner(store,member2name));
            Assertions.assertTrue(member1name.appointOtherMemberAsStoreOwner(store,member3name));
            List<OwnerContractDTO> ownerContractDTOS = store.getMyCreatedContracts(member1name.getUserName());
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertTrue(store.getAlreadyDoneContracts(member1name.getUserName()).isEmpty());
            Assertions.assertTrue(store.fillOwnerContract(member2name.getUserName(),member3name.getUserName(),true));
//            ownerContractDTOS = store.getAlreadyDoneContracts(member1name.getUserName());
//            Assertions.assertFalse(ownerContractDTOS.isEmpty());
//            Assertions.assertEquals(ownerContractDTOS.get(0).contractStatus,"all owners have accepted this contract and it is done");
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_owner_fail(){
        try {
            Assertions.assertTrue(member1name.appointOtherMemberAsStoreOwner(store,member2name));
            Assertions.assertTrue(member1name.appointOtherMemberAsStoreOwner(store,member3name));
            List<OwnerContractDTO> ownerContractDTOS = store.getMyCreatedContracts(member1name.getUserName());
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertTrue(store.getAlreadyDoneContracts(member1name.getUserName()).isEmpty());
            Assertions.assertTrue(store.fillOwnerContract(member2name.getUserName(),member3name.getUserName(),false));
            ownerContractDTOS = store.getAlreadyDoneContracts(member1name.getUserName());
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertEquals(ownerContractDTOS.get(0).contractStatus,"contract is rejected by "+member2name.getUserName());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_owner_but_the_appointed_is_not_owner_fail(){
        try {
            Assertions.assertThrows(Exception.class, () -> member2name.appointOtherMemberAsStoreOwner(store,member3name));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_owner_but_already_have_contract_fail(){
        try {
            Assertions.assertTrue(member1name.appointOtherMemberAsStoreOwner(store,member2name));
            Assertions.assertTrue(member1name.appointOtherMemberAsStoreOwner(store,member3name));
            List<OwnerContractDTO> ownerContractDTOS = store.getMyCreatedContracts(member1name.getUserName());
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertTrue(store.getAlreadyDoneContracts(member1name.getUserName()).isEmpty());
            Assertions.assertThrows(Exception.class, () -> member1name.appointOtherMemberAsStoreOwner(store,member3name));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void appoint_member_as_store_owner_but_already_owner_fail(){
        try {
            Assertions.assertTrue(member1name.appointOtherMemberAsStoreOwner(store,member2name));
            Assertions.assertThrows(Exception.class, () -> member1name.appointOtherMemberAsStoreOwner(store,member2name));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_owner_by_his_appointer_success(){
        try {
            Assertions.assertTrue(member1name.appointOtherMemberAsStoreOwner(store,member2name));
            Assertions.assertTrue(member1name.appointOtherMemberAsStoreOwner(store,member3name));
            List<OwnerContractDTO> ownerContractDTOS = store.getMyCreatedContracts(member1name.getUserName());
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertTrue(store.getAlreadyDoneContracts(member1name.getUserName()).isEmpty());
            Assertions.assertTrue(store.fillOwnerContract(member2name.getUserName(),member3name.getUserName(),true));
            ownerContractDTOS = store.getAlreadyDoneContracts(member1name.getUserName());
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertEquals(ownerContractDTOS.get(0).contractStatus,"all owners have accepted this contract and it is done");
            Assertions.assertTrue(member1name.removeOwnerByHisAppointer(store,member3name));
            Assertions.assertFalse(store.getStoreOwners().containsKey(member3name.getUserName()));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_member_not_owner_fail(){
        try {
            Assertions.assertTrue(member1name.appointOtherMemberAsStoreOwner(store,member2name));
            Assertions.assertThrows(Exception.class, () -> member1name.removeOwnerByHisAppointer(store,member3name));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_member_not_owner_for_owner_fail(){
        try {
            Assertions.assertTrue(member1name.appointOtherMemberAsStoreOwner(store,member2name));
            Assertions.assertThrows(Exception.class, () -> member3name.removeOwnerByHisAppointer(store,member2name));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_owner_by_his_appointer_chain_success(){
        try {
            Assertions.assertTrue(member1name.appointOtherMemberAsStoreOwner(store,member2name));
            Assertions.assertTrue(member2name.appointOtherMemberAsStoreOwner(store,member3name));
            List<OwnerContractDTO> ownerContractDTOS = store.getMyCreatedContracts(member2name.getUserName());
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertTrue(store.getAlreadyDoneContracts(member2name.getUserName()).isEmpty());
            Assertions.assertTrue(store.fillOwnerContract(member1name.getUserName(),member3name.getUserName(),true));
            ownerContractDTOS = store.getAlreadyDoneContracts(member2name.getUserName());
            Assertions.assertFalse(ownerContractDTOS.isEmpty());
            Assertions.assertEquals(ownerContractDTOS.get(0).contractStatus,"all owners have accepted this contract and it is done");
            Assertions.assertTrue(member1name.removeOwnerByHisAppointer(store,member2name));
            Assertions.assertFalse(store.getStoreOwners().containsKey(member2name.getUserName()));
            Assertions.assertFalse(store.getStoreOwners().containsKey(member3name.getUserName()));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void remove_owner_by_his_appointer_chain_and_manager_success(){
        try {
            Assertions.assertTrue(member1name.appointOtherMemberAsStoreOwner(store,member2name));
            Assertions.assertTrue(member2name.appointOtherMemberAsStoreManager(store,member3name));
            Assertions.assertTrue(member1name.removeOwnerByHisAppointer(store,member2name));
            Assertions.assertFalse(store.getStoreOwners().containsKey(member2name.getUserName()));
            Assertions.assertFalse(store.getStoreManagers().containsKey(member3name.getUserName()));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }



}
