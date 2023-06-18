package UnitTests;


import DomainLayer.*;
import DomainLayer.BagConstraints.CategoryBagConstraint;
import DomainLayer.BagConstraints.ProductBagConstraint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class ProductBagContraintTests {

    private ProductBagConstraint productBagConstraint;

    private Store store;
    @Mock
    private Member member;

    private String product1Name = "product1";
    private String category1Name = "category1";
    @Mock
    private StoreFounder founder;
    private Product product;
    private Category category;



    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        member = new Member("member1","member1Pass");
        store = new Store("store1");
        member.appointMemberAsStoreFounder(store);
        founder = new StoreFounder(member);
        store.addNewProductToStock(founder.getUserName(),"product1","category1",20.3,"desc",85);
        category = store.getStock().getCategory(category1Name);
        product = store.getStock().getProduct(product1Name);
    }


    @ParameterizedTest
    @ValueSource(ints = {1,5,10})
    public void checkMaxTimeAtDayConstraint_before_hour_success(int addedMinutes){
        int hour = LocalTime.now().getHour()+1;
        hour=hour%24;
        int minute = (LocalTime.now().getMinute()+addedMinutes)%60;
        if((minute<=30 && hour==1) || hour==0)
            return;
        productBagConstraint = new ProductBagConstraint(product, hour, minute);
        ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent = new ConcurrentHashMap<>();
        ConcurrentHashMap<Product, Integer> innerHashMap = new ConcurrentHashMap<>();
        innerHashMap.put(product,30);
        bagContent.put(product1Name,innerHashMap);
        Assertions.assertTrue(productBagConstraint.checkMaxTimeAtDayConstraint(bagContent));
    }



    @ParameterizedTest
    @ValueSource(ints = {1,5,10})
    public void checkMaxTimeAtDayConstraint_after_hour_product_not_exists_success(int addedMinutes){
        int hour = LocalTime.now().getHour()-1;
        hour=hour%24;
        int minute = (LocalTime.now().getMinute()-addedMinutes)%60;
        if((hour ==22 && minute>=30) & hour==23)
            return;
        productBagConstraint = new ProductBagConstraint(product, hour, minute);
        ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent = new ConcurrentHashMap<>();
        //ConcurrentHashMap<Product, Integer> innerHashMap = new ConcurrentHashMap<>();
        //innerHashMap.put(product,30);
        //bagContent.put(product1Name,innerHashMap);
        Assertions.assertTrue(productBagConstraint.checkMaxTimeAtDayConstraint(bagContent));
    }



    @ParameterizedTest
    @ValueSource(ints = {1,5,10})
    public void checkMaxTimeAtDayConstraint_after_hour_product_exists_fail(int addedMinutes){
        int hour = LocalTime.now().getHour()-1;
        hour=hour%24;
        int minute = (LocalTime.now().getMinute()-addedMinutes)%60;
        if((hour ==22 && minute>=30) & hour==23)
            return;
        productBagConstraint = new ProductBagConstraint(product, hour, minute);
        ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent = new ConcurrentHashMap<>();
        ConcurrentHashMap<Product, Integer> innerHashMap = new ConcurrentHashMap<>();
        innerHashMap.put(product,30);
        bagContent.put(product1Name,innerHashMap);
        Assertions.assertFalse(productBagConstraint.checkMaxTimeAtDayConstraint(bagContent));
    }
}
