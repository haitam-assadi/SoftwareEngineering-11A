package UnitTests;



import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.Controller.StoreMapper;
import DomainLayer.*;
import DomainLayer.BagConstraints.BagConstraint;
import DomainLayer.BagConstraints.CategoryBagConstraint;
import DomainLayer.BagConstraints.ProductBagConstraint;
import DomainLayer.DiscountPolicies.AllStoreDiscountPolicy;
import DomainLayer.DiscountPolicies.ProductDiscountPolicy;
import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class AllStoreDiscountPolicyTests {


    private Store store;
    @Mock
    private Member member;

    private String product1Name = "product1";
    private String category1Name = "category1";


    private String product2Name = "product2";
    private String category2Name = "category2";
    @Mock
    private StoreFounder founder;
    private Product product1;
    private Category category1;

    private Product product2;
    private Category category2;

    @Mock
    private BagConstraint bagConstraint;
    private AllStoreDiscountPolicy allStoreDiscountPolicy;


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
        store.addNewProductToStock(founder.getUserName(),"product2","category2",55.7,"desc",34);
        category1 = store.getStock().getCategory(category1Name);
        product1 = store.getStock().getProduct(product1Name);
        category2 = store.getStock().getCategory(category2Name);
        product2 = store.getStock().getProduct(product2Name);
    }

    @Test
    public void calculateDiscountEmptyCartReturnZero(){
        allStoreDiscountPolicy = new AllStoreDiscountPolicy(50);
        ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent = new ConcurrentHashMap<>();
        Assertions.assertTrue(allStoreDiscountPolicy.calculateDiscount(bagContent).equals(0.0));
    }

    @Test
    public void calculateDiscountProductExistsConstraintNotFollowedReturnZero(){
        allStoreDiscountPolicy = new AllStoreDiscountPolicy(50,bagConstraint);
        Mockito.when(bagConstraint.checkConstraint(Mockito.any())).thenReturn(false);
        ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent = new ConcurrentHashMap<>();
        ConcurrentHashMap<Product, Integer> innerHashMap = new ConcurrentHashMap<>();
        innerHashMap.put(product1,30);
        bagContent.put(product1Name,innerHashMap);
        Assertions.assertTrue(allStoreDiscountPolicy.calculateDiscount(bagContent).equals(0.0));
    }

    @Test
    public void calculateDiscountProductExistsReturnDiscountVal(){
        int product1Amount = 30;
        int product2Amount = 10;
        allStoreDiscountPolicy = new AllStoreDiscountPolicy(50,bagConstraint);
        Mockito.when(bagConstraint.checkConstraint(Mockito.any())).thenReturn(true);
        ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent = new ConcurrentHashMap<>();
        ConcurrentHashMap<Product, Integer> innerHashMap = new ConcurrentHashMap<>();
        innerHashMap.put(product1,product1Amount);
        bagContent.put(product1Name,innerHashMap);

        ConcurrentHashMap<Product, Integer> innerHashMap2 = new ConcurrentHashMap<>();
        innerHashMap2.put(product2,product2Amount);
        bagContent.put(product2Name,innerHashMap2);
        Double ExpectedDiscountValue = product1.getPrice()*0.5*product1Amount + product2.getPrice()*0.5*product2Amount;

        Assertions.assertTrue(allStoreDiscountPolicy.calculateDiscount(bagContent).equals(ExpectedDiscountValue));
    }
}