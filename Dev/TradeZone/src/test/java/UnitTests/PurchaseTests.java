package UnitTests;

import DomainLayer.*;
import DomainLayer.BagConstraints.BagConstraint;
import DomainLayer.DiscountPolicies.ProductDiscountPolicy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class PurchaseTests {
    private Store store;
    private Bag bag;
    private Cart cart;
    @Mock
    private StoreFounder founder;

    @Mock
    private Member member;

    private String product1Name = "product1";
    private String category1Name = "category1";

    @Mock
    private BagConstraint bagConstraint;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        member = new Member("member1","member1Pass");
        store = new Store("store1");
        member.appointMemberAsStoreFounder(store);
        founder = new StoreFounder(member);
        store.addNewProductToStock(founder.getUserName(),"product1","category1",20.3,"desc",85);

    }
    @Test
    public void validateStorePolicy(){
        try {
            store.createMaxProductAmountAllContentBagConstraint(member.getUserName(),product1Name,5,true);
            Assertions.assertFalse(store.getStoreBagConstIds().isEmpty());
            Assertions.assertEquals(store.getAllBagConstraints(member.getUserName()).get(0),"1. bag must contain maximum 5 product1 product");
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void validateAllProductsAmounts(){
        try {
            Assertions.assertEquals(store.getProductAmount(product1Name),85);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void getCartPriceAfterDiscount(){
        try {
            int product1Amount = 30;
            Product product1 =  store.getProduct(product1Name);
            ProductDiscountPolicy productDiscountPolicy = new ProductDiscountPolicy(product1, 20,bagConstraint);
            Mockito.when(bagConstraint.checkConstraint(Mockito.any())).thenReturn(true);
            ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent = new ConcurrentHashMap<>();
            ConcurrentHashMap<Product, Integer> innerHashMap = new ConcurrentHashMap<>();
            innerHashMap.put(product1,product1Amount);
            bagContent.put(product1Name,innerHashMap);
            Double ExpectedDiscountValue = product1.getPrice()*0.2*product1Amount;
            Assertions.assertEquals(productDiscountPolicy.calculateDiscount(bagContent), ExpectedDiscountValue);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void updateStockAmount(){
        try {


            int product1Amount = 30;
            Product product1 =  store.getProduct(product1Name);
            Mockito.when(bagConstraint.checkConstraint(Mockito.any())).thenReturn(true);
            ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent = new ConcurrentHashMap<>();
            ConcurrentHashMap<Product, Integer> innerHashMap = new ConcurrentHashMap<>();
            innerHashMap.put(product1,product1Amount);
            bagContent.put(product1Name,innerHashMap);
            store.removeBagAmountFromStock(bagContent);
            Assertions.assertEquals(store.getStock().getProductAmount(product1Name),85-30);

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

}
