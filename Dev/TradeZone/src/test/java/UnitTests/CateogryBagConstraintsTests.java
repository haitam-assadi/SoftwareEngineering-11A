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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class CateogryBagConstraintsTests {



    private CategoryBagConstraint categoryBagConstraint;
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
    @ValueSource(ints = {1,2,4,5,10})
    public void checkRangeOfDaysConstraint_good_before_date_success(int additionYears){
        categoryBagConstraint = new CategoryBagConstraint(category, 2025+additionYears,5,15,2029+additionYears,7,20);
        ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent = new ConcurrentHashMap<>();
        ConcurrentHashMap<Product, Integer> innerHashMap = new ConcurrentHashMap<>();
        innerHashMap.put(product,30);
        bagContent.put(product1Name,innerHashMap);
        Assertions.assertTrue(categoryBagConstraint.checkRangeOfDaysConstraint(bagContent));
    }


    @ParameterizedTest
    @ValueSource(ints = {1,2,4,5,10})
    public void checkRangeOfDaysConstraint_good_after_date_success(int years){
        categoryBagConstraint = new CategoryBagConstraint(category, 2020-years,5,15,2022-years,7,20);
        ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent = new ConcurrentHashMap<>();
        ConcurrentHashMap<Product, Integer> innerHashMap = new ConcurrentHashMap<>();
        innerHashMap.put(product,30);
        bagContent.put(product1Name,innerHashMap);
        Assertions.assertTrue(categoryBagConstraint.checkRangeOfDaysConstraint(bagContent));
    }



    @ParameterizedTest
    @ValueSource(ints = {1,2,4,5})
    public void checkRangeOfDaysConstraint_bad_date_not_contain_product_success(int years){
        categoryBagConstraint = new CategoryBagConstraint(category, 2015+years,5,15,2030+years,7,20);
        ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent = new ConcurrentHashMap<>();
        Assertions.assertTrue(categoryBagConstraint.checkRangeOfDaysConstraint(bagContent));
    }


    @ParameterizedTest
    @ValueSource(ints = {1,2,4,5})
    public void checkRangeOfDaysConstraint_bad_date_contain_product_fail(int years){
        categoryBagConstraint = new CategoryBagConstraint(category, 2015+years,5,15,2030+years,7,20);
        ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent = new ConcurrentHashMap<>();
        ConcurrentHashMap<Product, Integer> innerHashMap = new ConcurrentHashMap<>();
        innerHashMap.put(product,30);
        bagContent.put(product1Name,innerHashMap);
        Assertions.assertFalse(categoryBagConstraint.checkRangeOfDaysConstraint(bagContent));
    }
}
