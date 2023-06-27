package UnitTests;


import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.Controller.StoreMapper;
import DomainLayer.*;
import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class StoreDiscountPoliciesTests {


    private Store store;
    @Mock
    private StoreFounder founder;

    @Mock
    private Member member;

    private String product1Name = "product1";
    private String category1Name = "category1";



    @BeforeEach
    public void setUp() throws Exception {
        Market.dbFlag = false;
        StoreMapper.initMapper();
        MemberMapper.initMapper();
        MockitoAnnotations.openMocks(this);
        member = new Member("member1","member1Pass");
        store = new Store("store1");
        member.appointMemberAsStoreFounder(store);
        founder = new StoreFounder(member);
        store.addNewProductToStock(founder.getUserName(),"product1","category1",20.3,"desc",85);

    }


    @ParameterizedTest
    @ValueSource(ints = {0,13,25,40,55,73,88,100})
    public void createProductDiscountPolicy_good_percentage_success(int percentage){

        try {
            Integer bagConstraintId = store.createProductDiscountPolicy(founder.getUserName(), product1Name, percentage, false);
            Assertions.assertTrue(store.getCreatedDiscountPoliciesIds().contains(bagConstraintId));
            Assertions.assertFalse(store.getStoreDiscountPoliciesIds().contains(bagConstraintId));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }

    }

    @ParameterizedTest
    @ValueSource(ints = {-1000,-100,-50,-1,101,200,500})
    public void createProductDiscountPolicy_bad_percentage_fail(int percentage){

        try {
            Assertions.assertThrows(Exception.class,()-> store.createProductDiscountPolicy(founder.getUserName(), product1Name, percentage, false));
            Assertions.assertTrue(store.getCreatedDiscountPoliciesIds().isEmpty());
            Assertions.assertTrue(store.getStoreDiscountPoliciesIds().isEmpty());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }

    }


    @ParameterizedTest
    @ValueSource(ints = {0,25,55,88,100})
    public void createProductDiscountPolicy_addAsStoreDiscountPolicySuccess(int percentage){

        try {
            Integer bagConstraintId = store.createProductDiscountPolicy(founder.getUserName(), product1Name, percentage, true);
            Assertions.assertTrue(store.getCreatedDiscountPoliciesIds().contains(bagConstraintId));
            Assertions.assertTrue(store.getStoreDiscountPoliciesIds().contains(bagConstraintId));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    public void createProductDiscountPolicy_two_discounts_Success(){

        try {
            Integer bagConstraintId1 = store.createProductDiscountPolicy(founder.getUserName(), product1Name, 50, true);
            Integer bagConstraintId2 = store.createProductDiscountPolicy(founder.getUserName(), product1Name, 20, false);

            Assertions.assertTrue(store.getCreatedDiscountPoliciesIds().contains(bagConstraintId1));
            Assertions.assertTrue(store.getStoreDiscountPoliciesIds().contains(bagConstraintId1));

            Assertions.assertTrue(store.getCreatedDiscountPoliciesIds().contains(bagConstraintId2));
            Assertions.assertFalse(store.getStoreDiscountPoliciesIds().contains(bagConstraintId2));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }

    }
}
