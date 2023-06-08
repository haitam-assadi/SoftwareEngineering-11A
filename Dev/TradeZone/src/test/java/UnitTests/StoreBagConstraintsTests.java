package UnitTests;

import DomainLayer.Member;
import DomainLayer.Store;
import DomainLayer.StoreFounder;
import DomainLayer.StoreOwner;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class StoreBagConstraintsTests {

    private Store store;
    @Mock
    private StoreFounder founder;

    @Mock
    private Member member;

    private String product1Name = "product1";
    private String category1Name = "category1";



    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        member = new Member("member1","member1Pass");
        store = new Store("store1");
        member.appointMemberAsStoreFounder(store);
        founder = new StoreFounder(member);
        store.addNewProductToStock(founder.getUserName(),"product1","category1",20.3,"desc",85);

    }


    @ParameterizedTest
    @ValueSource(ints = {0,2,5,7,9,10,14,18,21,23})
    public void createMaxTimeAtDayProductBagConstraint_good_hour_success(int hour){
        try {
            Integer bagConstraintId = store.createMaxTimeAtDayProductBagConstraint(founder.getUserName(), product1Name, hour,30, false);
            Assertions.assertTrue(store.getCreatedBagConstIds().contains(bagConstraintId));
            Assertions.assertFalse(store.getStoreBagConstIds().contains(bagConstraintId));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {-10,-1,24,30,100})
    public void createMaxTimeAtDayProductBagConstraint_bad_hour_fail(int hour){
        try {
            Assertions.assertThrows(Exception.class,()-> store.createMaxTimeAtDayProductBagConstraint(founder.getUserName(), product1Name, hour,30, false));
            Assertions.assertTrue(store.getCreatedBagConstIds().isEmpty());
            Assertions.assertTrue(store.getStoreBagConstIds().isEmpty());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


    @ParameterizedTest
    @ValueSource(ints = {0,2,5,7,9,10,14,18,21,23})
    public void createMaxTimeAtDayProductBagConstraint_good_minute_success(int minute){
        try {
            Integer bagConstraintId = store.createMaxTimeAtDayProductBagConstraint(founder.getUserName(), product1Name, 5,minute, false);
            Assertions.assertTrue(store.getCreatedBagConstIds().contains(bagConstraintId));
            Assertions.assertFalse(store.getStoreBagConstIds().contains(bagConstraintId));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {-10,-1,60,85,90,100})
    public void createMaxTimeAtDayProductBagConstraint_bad_minute_fail(int minute){
        try {
            Assertions.assertThrows(Exception.class,()-> store.createMaxTimeAtDayProductBagConstraint(founder.getUserName(), product1Name, 5,minute, false));
            Assertions.assertTrue(store.getCreatedBagConstIds().isEmpty());
            Assertions.assertTrue(store.getStoreBagConstIds().isEmpty());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void createMaxTimeAtDayProductBagConstraint_add_as_store_payment_policy_success(){
        try {
            Integer bagConstraintId = store.createMaxTimeAtDayProductBagConstraint(founder.getUserName(), product1Name, 5,30, true);
            Assertions.assertTrue(store.getCreatedBagConstIds().contains(bagConstraintId));
            Assertions.assertTrue(store.getStoreBagConstIds().contains(bagConstraintId));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void createMaxTimeAtDayProductBagConstraint_two_constraints_added(){
        try {
            Integer bagConstraintId1 = store.createMaxTimeAtDayProductBagConstraint(founder.getUserName(), product1Name, 5,30, true);
            Integer bagConstraintId2 = store.createMaxTimeAtDayProductBagConstraint(founder.getUserName(), product1Name, 10,50, false);
            Assertions.assertTrue(store.getCreatedBagConstIds().contains(bagConstraintId1));
            Assertions.assertTrue(store.getStoreBagConstIds().contains(bagConstraintId1));
            Assertions.assertTrue(store.getCreatedBagConstIds().contains(bagConstraintId2));
            Assertions.assertFalse(store.getStoreBagConstIds().contains(bagConstraintId2));

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

}
