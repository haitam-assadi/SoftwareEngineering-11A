package UnitTests;



import DomainLayer.*;
import DomainLayer.BagConstraints.BagConstraintAnd;
import DomainLayer.BagConstraints.BagConstraintOr;
import DomainLayer.BagConstraints.CategoryBagConstraint;
import DomainLayer.BagConstraints.ProductBagConstraint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
public class BagConstraintOrTests {

    @Mock
    private ProductBagConstraint productBagConstraint;

    @Mock
    private CategoryBagConstraint categoryBagConstraint;
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
    private BagConstraintOr bagConstraintOr;



    @BeforeEach
    public void setUp() throws Exception {
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
    public void checkConstraint_both_false_returns_false(){
        Mockito.when(productBagConstraint.checkConstraint(Mockito.any())).thenReturn(false);
        Mockito.when(categoryBagConstraint.checkConstraint(Mockito.any())).thenReturn(false);

        bagConstraintOr = new BagConstraintOr(productBagConstraint,categoryBagConstraint);
        Assertions.assertFalse(bagConstraintOr.checkConstraint(new ConcurrentHashMap<>()));
    }

    @Test
    public void checkConstraint_first_true_returns_true(){
        Mockito.when(productBagConstraint.checkConstraint(Mockito.any())).thenReturn(true);
        Mockito.when(categoryBagConstraint.checkConstraint(Mockito.any())).thenReturn(false);

        bagConstraintOr = new BagConstraintOr(productBagConstraint,categoryBagConstraint);
        Assertions.assertTrue(bagConstraintOr.checkConstraint(new ConcurrentHashMap<>()));
    }

    @Test
    public void checkConstraint_sec_true_returns_true(){
        Mockito.when(productBagConstraint.checkConstraint(Mockito.any())).thenReturn(false);
        Mockito.when(categoryBagConstraint.checkConstraint(Mockito.any())).thenReturn(true);

        bagConstraintOr = new BagConstraintOr(productBagConstraint,categoryBagConstraint);
        Assertions.assertTrue(bagConstraintOr.checkConstraint(new ConcurrentHashMap<>()));
    }

    @Test
    public void checkConstraint_both_true_returns_true(){
        Mockito.when(productBagConstraint.checkConstraint(Mockito.any())).thenReturn(true);
        Mockito.when(categoryBagConstraint.checkConstraint(Mockito.any())).thenReturn(true);

        bagConstraintOr = new BagConstraintOr(productBagConstraint,categoryBagConstraint);
        Assertions.assertTrue(bagConstraintOr.checkConstraint(new ConcurrentHashMap<>()));
    }

}
