package UnitTests;


import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.Controller.StoreMapper;
import DomainLayer.*;
import DomainLayer.BagConstraints.BagConstraintAnd;
import DomainLayer.BagConstraints.BagConstraintOr;
import DomainLayer.BagConstraints.CategoryBagConstraint;
import DomainLayer.BagConstraints.ProductBagConstraint;
import DomainLayer.DiscountPolicies.AdditionDiscountPolicy;
import DomainLayer.DiscountPolicies.DiscountPolicy;
import DomainLayer.DiscountPolicies.MaxValDiscountPolicy;
import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class AdditionDiscountPolicyTest {


    @Mock
    private DiscountPolicy firstDiscountPolicy;

    @Mock
    private DiscountPolicy secDiscountPolicy;
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
    private AdditionDiscountPolicy additionDiscountPolicy;



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


    @ParameterizedTest
    @CsvSource({"200.23,200.10", "1000.5, 205.7", "3000, 13.9", "10000.5, 20502.7"})
    public void calculateDiscount(Double firstDiscountValue, Double secDiscountValue){
        Mockito.when(firstDiscountPolicy.calculateDiscount(Mockito.any())).thenReturn(firstDiscountValue);
        Mockito.when(secDiscountPolicy.calculateDiscount(Mockito.any())).thenReturn(secDiscountValue);
        additionDiscountPolicy = new AdditionDiscountPolicy(firstDiscountPolicy,secDiscountPolicy);
        Double returnedDiscountVal = additionDiscountPolicy.calculateDiscount(new ConcurrentHashMap<>());
        Assertions.assertTrue(returnedDiscountVal.equals(firstDiscountValue+secDiscountValue));

    }


}