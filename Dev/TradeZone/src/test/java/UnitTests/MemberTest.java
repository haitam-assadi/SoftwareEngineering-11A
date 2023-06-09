package UnitTests;

import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.Controller.StoreMapper;
import DomainLayer.*;
import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
class MemberTest {

    private Member member;
    @Mock
    private Store store;

    @Mock
    private Member anotherMember;

    @Mock
    private AbstractStoreOwner abstractOwner;

    @Mock
    private Role role;

    @BeforeAll
    public void setUp(){
        Market.dbFlag = false;
        StoreMapper.initMapper();
        MemberMapper.initMapper();
        MockitoAnnotations.openMocks(this);
        member = new Member("Adel", "12345");
    }

    @BeforeEach
    public void beforeEachTest(){
        Market.dbFlag = false;
        StoreMapper.initMapper();
        MemberMapper.initMapper();
        member = new Member("Adel", "12345");
    }

    /*
        Cases:
            1. function called by member that is not a founder or owner - failure
            2. member called the function is not a founder or owner of this particular store - failure
            3. function called by an owner - success
            4. function called by a founder - success
    */
    @Test
    void appoint_other_member_as_store_owner_without_founder_or_owner_fail() throws Exception {
        Mockito.when(store.getStoreName()).thenReturn("myStore");
        Mockito.when(abstractOwner.appointOtherMemberAsStoreOwner(Mockito.any(), Mockito.any())).thenReturn(true);
        assertThrows(Exception.class,
                () -> {member.appointOtherMemberAsStoreOwner(store, anotherMember);});
    }

    @Test
    void appoint_other_member_as_owner_called_by_founder_not_for_the_given_store_fail() throws Exception {
        Mockito.when(store.getStoreName()).thenReturn("myStore");
        member.addRole(member.getRoleEnum("StoreFounder"), role);
        assertThrows(Exception.class,
                () -> {member.appointOtherMemberAsStoreOwner(store, anotherMember);});
    }
    @Test
    void appoint_other_member_as_owner_called_by_owner_not_for_the_given_store_fail() throws Exception {
        Mockito.when(store.getStoreName()).thenReturn("myStore");
        member.addRole(member.getRoleEnum("StoreOwner"), role);
        assertThrows(Exception.class,
                () -> {member.appointOtherMemberAsStoreOwner(store, anotherMember);});
    }

    @Test
    void appoint_other_member_called_by_founder_success() throws Exception {
        /*
        member.addRole(member.getRoleEnum("StoreFounder"), role);
        role.addStore("myStore", store);
        //TODO: does not compile
        //member.setAbstractOwner(abstractOwner);
        Mockito.when(store.getStoreName()).thenReturn("myStore");
        Mockito.when(abstractOwner.appointOtherMemberAsStoreOwner(Mockito.any(), Mockito.any())).thenReturn(true);
        assertTrue(member.appointOtherMemberAsStoreOwner(store, anotherMember));

         */
    }

    @Test
    void appoint_other_member_called_by_owner_success() throws Exception {
        /*
        member.addRole(member.getRoleEnum("StoreOwner"), role);
        role.addStore("myStore", store);
        //TODO: does not compile
        //member.setAbstractOwner(abstractOwner);
        Mockito.when(store.getStoreName()).thenReturn("myStore");
        Mockito.when(abstractOwner.appointOtherMemberAsStoreOwner(Mockito.any(), Mockito.any())).thenReturn(true);
        assertTrue(member.appointOtherMemberAsStoreOwner(store, anotherMember));

         */
    }

    /*
        Cases:
            1. member appointed the given role - success
            2. member has not appointed to the given role - failure
    */
    @Test
    void contains_role_success() {
        try {
            member.addRole(member.getRoleEnum("StoreOwner"), role);
            assertTrue(member.containsRole("StoreOwner"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    void contains_role_fail() {
        try {
            assertFalse(member.containsRole("StoreOwner"));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }

    }

    /*
        Cases:
            1. function called by member that is not a founder or owner - failure
            2. member called the function is not a founder or owner of this particular store - failure
            3. function called by an owner - success
            4. function called by a founder - success
    */

    @Test
    void appoint_other_member_as_store_manager_without_founder_or_owner_fail() throws Exception {
        Mockito.when(store.getStoreName()).thenReturn("myStore");
        Mockito.when(abstractOwner.appointOtherMemberAsStoreOwner(Mockito.any(), Mockito.any())).thenReturn(true);
        assertThrows(Exception.class,
                () -> {member.appointOtherMemberAsStoreManager(store, anotherMember);});
    }

    @Test
    void appoint_other_member_as_manager_called_by_store_founder_not_for_the_given_store_fail() throws Exception {
        Mockito.when(store.getStoreName()).thenReturn("myStore");
        member.addRole(member.getRoleEnum("StoreFounder"), role);
        assertThrows(Exception.class,
                () -> {member.appointOtherMemberAsStoreManager(store, anotherMember);});
    }

    @Test
    void appoint_other_member_as_manager_called_by_store_owner_not_for_the_given_store_fail() throws Exception {
        Mockito.when(store.getStoreName()).thenReturn("myStore");
        member.addRole(member.getRoleEnum("StoreOwner"), role);
        assertThrows(Exception.class,
                () -> {member.appointOtherMemberAsStoreManager(store, anotherMember);});
    }

    @Test
    void appoint_other_member_as_manager_called_by_founder_success() throws Exception {
        /*
        member.addRole(member.getRoleEnum("StoreFounder"), role);
        role.addStore("myStore", store);
        //TODO: does not compile
        //member.setAbstractOwner(abstractOwner);
        Mockito.when(store.getStoreName()).thenReturn("myStore");
        Mockito.when(abstractOwner.appointOtherMemberAsStoreManager(Mockito.any(), Mockito.any())).thenReturn(true);
            assertTrue(member.appointOtherMemberAsStoreManager(store, anotherMember));

         */
    }

    @Test
    void appoint_other_member_as_manager_called_by_owner_success() throws Exception {
        /*
        member.addRole(member.getRoleEnum("StoreOwner"), role);
        role.addStore("myStore", store);
        //TODO: does not compile
        //member.setAbstractOwner(abstractOwner);
        Mockito.when(store.getStoreName()).thenReturn("myStore");
        Mockito.when(abstractOwner.appointOtherMemberAsStoreManager(Mockito.any(), Mockito.any())).thenReturn(true);
        assertTrue(member.appointOtherMemberAsStoreManager(store, anotherMember));

         */
    }
}