package DatabaseTests;

import DTO.StoreDTO;
import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class init1FileTest {

    private ProxyBridge proxy;


    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("setup");
        //proxy.realBridge.systemService.firstManagerInitializer();
        //proxy.login(aaaa, "systemmanager1", "systemmanager1Pass");
        proxy= new ProxyBridge(new RealBridge());
        proxy.realBridge.systemService.initMarketParsing();
    }
    //TODO: data base must be create-drop
    //TODO: config file must be true for both data base flasg and init file flag
    //TODO: loaded file must be init file 1

    @Test
    public void init1FileTest(){
        try{
            initSystemServiceAndLoadDataAndLogIn();
            String guest0 = proxy.enterMarket();
            Assertions.assertThrows(Exception.class, ()-> proxy.register(guest0, "u1", "user1pass"));
            Assertions.assertThrows(Exception.class, ()-> proxy.register(guest0, "u2", "user2pass"));
            Assertions.assertThrows(Exception.class, ()-> proxy.register(guest0, "u3", "user3pass"));
            Assertions.assertTrue(proxy.register(guest0, "u4", "user4pass"));

            Assertions.assertTrue(proxy.getAllMembers().contains("u1"));
            Assertions.assertTrue(proxy.getAllMembers().contains("u2"));
            Assertions.assertTrue(proxy.getAllMembers().contains("u3"));

            Assertions.assertTrue(proxy.getAllOnlineMembers().isEmpty());

            Assertions.assertTrue(proxy.getAllStoresNames().size()==1);
            Assertions.assertTrue(proxy.getAllStoresNames().contains("s1"));

            String guest1 = proxy.enterMarket();
            String guest2 = proxy.enterMarket();
            String guest3 = proxy.enterMarket();
            String guest4 = proxy.enterMarket();

            Assertions.assertThrows(Exception.class, ()-> proxy.memberLogOut("u1"));
            Assertions.assertThrows(Exception.class, ()-> proxy.memberLogOut("u2"));
            Assertions.assertThrows(Exception.class, ()-> proxy.memberLogOut("u3"));
            Assertions.assertThrows(Exception.class, ()-> proxy.login(guest1,"u1", "user1passWrong"));
            Assertions.assertThrows(Exception.class, ()-> proxy.login(guest2,"u2", "user2passWrong"));
            Assertions.assertThrows(Exception.class, ()-> proxy.login(guest3,"u3", "user3passWrong"));

            Assertions.assertTrue(proxy.login(guest1,"u1", "user1pass").equals("u1"));
            Assertions.assertTrue(proxy.login(guest2,"u2", "user2pass").equals("u2"));
            Assertions.assertTrue(proxy.login(guest3,"u3", "user3pass").equals("u3"));
            Assertions.assertThrows(Exception.class, ()-> proxy.login(guest4, "u5","fdgrtghhdf345"));
            proxy.memberLogOut("u1");
            proxy.memberLogOut("u2");
            proxy.memberLogOut("u3");
            String guest5 = proxy.enterMarket();
            String guest6 = proxy.enterMarket();
            String guest7 = proxy.enterMarket();
            Assertions.assertTrue(proxy.login(guest5,"u1", "user1pass").equals("u1"));
            Assertions.assertTrue(proxy.login(guest6,"u2", "user2pass").equals("u2"));
            Assertions.assertTrue(proxy.login(guest7,"u3", "user3pass").equals("u3"));

            proxy.getStoreInfo("u1", "s1");
            proxy.getStoreInfo("u2", "s1");
            proxy.getStoreInfo("u3", "s1");
            proxy.addNewProductToStock("u1", "s1","u1_product","category", 100.0,"des", 100);
            proxy.addNewProductToStock("u2", "s1","u2_product","category", 100.0,"des", 100);
            Assertions.assertThrows(Exception.class, ()-> proxy.addNewProductToStock("u3", "s1","u3_product","category", 100.0,"des", 100));
            Assertions.assertThrows(Exception.class, ()-> proxy.appointOtherMemberAsStoreOwner("u2","s1", "u3"));
            Assertions.assertTrue(proxy.getAlreadyDoneContracts("u1","s1").isEmpty());
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").size()==1);
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").get(0).triggerOwner.equals("u1"));
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").get(0).newOwner.equals("u3"));
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").get(0).storeName.equals("s1"));
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").get(0).pendingOwners.size()==1);
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").get(0).pendingOwners.get(0).equals("u2"));
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").get(0).alreadyAcceptedOwners.size()==0);
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").get(0).isContractDone==false);



            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").size()==1);
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").get(0).triggerOwner.equals("u1"));
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").get(0).newOwner.equals("u3"));
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").get(0).storeName.equals("s1"));
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").get(0).pendingOwners.size()==1);
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").get(0).pendingOwners.get(0).equals("u2"));
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").get(0).alreadyAcceptedOwners.size()==0);
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").get(0).isContractDone==false);
            Assertions.assertTrue(proxy.hasRole("u1"));
            Assertions.assertTrue(proxy.hasRole("u2"));
            Assertions.assertFalse(proxy.hasRole("u3"));
            Map<String, List<StoreDTO>> map_u1 =  proxy.myStores("u1");
            Assertions.assertTrue(map_u1.keySet().size()==1);
            Assertions.assertTrue(map_u1.keySet().stream().toList().get(0).equals("StoreFounder"));
            Assertions.assertTrue(map_u1.values().stream().toList().get(0).get(0).storeName.equals("s1"));


            Map<String, List<StoreDTO>> map_u2 =  proxy.myStores("u2");
            Assertions.assertTrue(map_u2.keySet().size()==1);
            Assertions.assertTrue(map_u2.keySet().stream().toList().get(0).equals("StoreOwner"));
            Assertions.assertTrue(map_u2.values().stream().toList().get(0).get(0).storeName.equals("s1"));

            Map<String, List<StoreDTO>> map_u3 =  proxy.myStores("u3");
            Assertions.assertTrue(map_u3.keySet().size()==0);

            Assertions.assertTrue(proxy.getRuleForStore("s1","u1").equals(1));

            Assertions.assertTrue(proxy.getRuleForStore("s1","u2").equals(2));
            Assertions.assertTrue(proxy.getRuleForStore("s1","u3").equals(-1));
            /////---------------------------------------------------------------------------------------------------------------
            /////---------------------------------------------------------------------------------------------------------------
            /////---------------------------------------------------------------------------------------------------------------
            /////---------------------------------------------------------------------------------------------------------------
            proxy.memberLogOut("u1");
            proxy.memberLogOut("u2");
            proxy.memberLogOut("u3");
            initSystemServiceAndLoadDataAndLogIn();

            String guest10 = proxy.enterMarket();
            Assertions.assertThrows(Exception.class, ()-> proxy.register(guest10, "u1", "user1pass"));
            Assertions.assertThrows(Exception.class, ()-> proxy.register(guest10, "u2", "user2pass"));
            Assertions.assertThrows(Exception.class, ()-> proxy.register(guest10, "u3", "user3pass"));
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllMembers().contains("u1"));
            Assertions.assertTrue(proxy.getAllMembers().contains("u2"));
            Assertions.assertTrue(proxy.getAllMembers().contains("u3"));

            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllOnlineMembers().isEmpty());

            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllStoresNames().size()==1);
            Assertions.assertTrue(proxy.getAllStoresNames().contains("s1"));


            initSystemServiceAndLoadDataAndLogIn();
            String guest11 = proxy.enterMarket();
            String guest12 = proxy.enterMarket();
            String guest13 = proxy.enterMarket();
            Assertions.assertTrue(proxy.login(guest11,"u1", "user1pass").equals("u1"));
            Assertions.assertTrue(proxy.login(guest12,"u2", "user2pass").equals("u2"));
            Assertions.assertTrue(proxy.login(guest13,"u3", "user3pass").equals("u3"));


            initSystemServiceAndLoadDataAndLogIn();
            proxy.getStoreInfo("u1", "s1");
            initSystemServiceAndLoadDataAndLogIn();
            proxy.getStoreInfo("u2", "s1");
            initSystemServiceAndLoadDataAndLogIn();
            proxy.getStoreInfo("u3", "s1");
            initSystemServiceAndLoadDataAndLogIn();
            proxy.addNewProductToStock("u1", "s1","u1_product1","category", 100.0,"des", 100);
            initSystemServiceAndLoadDataAndLogIn();
            proxy.addNewProductToStock("u2", "s1","u2_product2","category", 100.0,"des", 100);
            Assertions.assertThrows(Exception.class, ()-> proxy.addNewProductToStock("u3", "s1","u3_product","category", 100.0,"des", 100));
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertThrows(Exception.class, ()-> proxy.appointOtherMemberAsStoreOwner("u2","s1", "u3"));

            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAlreadyDoneContracts("u1","s1").isEmpty());

            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").size()==1);
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").get(0).triggerOwner.equals("u1"));
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").get(0).newOwner.equals("u3"));
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").get(0).storeName.equals("s1"));
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").get(0).pendingOwners.size()==1);
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").get(0).pendingOwners.get(0).equals("u2"));
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").get(0).alreadyAcceptedOwners.size()==0);
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").get(0).isContractDone==false);

            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").size()==1);
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").get(0).triggerOwner.equals("u1"));
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").get(0).newOwner.equals("u3"));
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").get(0).storeName.equals("s1"));
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").get(0).pendingOwners.size()==1);
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").get(0).pendingOwners.get(0).equals("u2"));
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").get(0).alreadyAcceptedOwners.size()==0);
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").get(0).isContractDone==false);

            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.hasRole("u1"));
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.hasRole("u2"));
            Assertions.assertFalse(proxy.hasRole("u3"));
            initSystemServiceAndLoadDataAndLogIn();
            map_u1 =  proxy.myStores("u1");
            Assertions.assertTrue(map_u1.keySet().size()==1);
            Assertions.assertTrue(map_u1.keySet().stream().toList().get(0).equals("StoreFounder"));
            Assertions.assertTrue(map_u1.values().stream().toList().get(0).get(0).storeName.equals("s1"));

            initSystemServiceAndLoadDataAndLogIn();
            map_u2 =  proxy.myStores("u2");
            Assertions.assertTrue(map_u2.keySet().size()==1);
            Assertions.assertTrue(map_u2.keySet().stream().toList().get(0).equals("StoreOwner"));
            Assertions.assertTrue(map_u2.values().stream().toList().get(0).get(0).storeName.equals("s1"));

            initSystemServiceAndLoadDataAndLogIn();
            map_u3 =  proxy.myStores("u3");
            Assertions.assertTrue(map_u3.keySet().size()==0);

            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getRuleForStore("s1","u1").equals(1));

            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getRuleForStore("s1","u2").equals(2));
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getRuleForStore("s1","u3").equals(-1));
            initSystemServiceAndLoadDataAndLogIn();

            proxy.fillOwnerContract("u2","s1","u3",true);

            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllOnlineMembers().contains("u1"));
            Assertions.assertTrue(proxy.getAllOnlineMembers().contains("u2"));
            Assertions.assertTrue(proxy.getAllOnlineMembers().contains("u3"));

            initSystemServiceAndLoadDataAndLogIn();

            Assertions.assertTrue(proxy.getAlreadyDoneContracts("u1","s1").size()==1);
            Assertions.assertTrue(proxy.getAlreadyDoneContracts("u1","s1").get(0).triggerOwner.equals("u1"));
            Assertions.assertTrue(proxy.getAlreadyDoneContracts("u1","s1").get(0).newOwner.equals("u3"));
            Assertions.assertTrue(proxy.getAlreadyDoneContracts("u1","s1").get(0).storeName.equals("s1"));
            Assertions.assertTrue(proxy.getAlreadyDoneContracts("u1","s1").get(0).pendingOwners.size()==0);
            Assertions.assertTrue(proxy.getAlreadyDoneContracts("u1","s1").get(0).alreadyAcceptedOwners.size()==1);
            Assertions.assertTrue(proxy.getAlreadyDoneContracts("u1","s1").get(0).isContractDone);

            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getMyCreatedContracts("u1","s1").isEmpty());
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u2","s1").size()==0);

            initSystemServiceAndLoadDataAndLogIn();
            proxy.addNewProductToStock("u3", "s1","u3_product","category", 100.0,"des", 100);
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertThrows(Exception.class, ()-> proxy.appointOtherMemberAsStoreOwner("u2","s1", "u3"));
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.hasRole("u1"));
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.hasRole("u2"));
            Assertions.assertTrue(proxy.hasRole("u3"));
            initSystemServiceAndLoadDataAndLogIn();
            map_u1 =  proxy.myStores("u1");
            Assertions.assertTrue(map_u1.keySet().size()==1);
            Assertions.assertTrue(map_u1.keySet().stream().toList().get(0).equals("StoreFounder"));
            Assertions.assertTrue(map_u1.values().stream().toList().get(0).get(0).storeName.equals("s1"));

            initSystemServiceAndLoadDataAndLogIn();
            map_u2 =  proxy.myStores("u2");
            Assertions.assertTrue(map_u2.keySet().size()==1);
            Assertions.assertTrue(map_u2.keySet().stream().toList().get(0).equals("StoreOwner"));
            Assertions.assertTrue(map_u2.values().stream().toList().get(0).get(0).storeName.equals("s1"));

            initSystemServiceAndLoadDataAndLogIn();
            map_u3 =  proxy.myStores("u3");
            Assertions.assertTrue(map_u3.keySet().size()==1);
            Assertions.assertTrue(map_u3.keySet().stream().toList().get(0).equals("StoreOwner"));
            Assertions.assertTrue(map_u3.values().stream().toList().get(0).get(0).storeName.equals("s1"));

            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getRuleForStore("s1","u1").equals(1));

            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getRuleForStore("s1","u2").equals(2));
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getRuleForStore("s1","u3").equals(2));
            initSystemServiceAndLoadDataAndLogIn();
            proxy.removeProductFromStock("u3", "s1","u1_product");
            initSystemServiceAndLoadDataAndLogIn();
            proxy.removeProductFromStock("u3", "s1","u2_product");
            initSystemServiceAndLoadDataAndLogIn();
            proxy.removeProductFromStock("u3", "s1","u3_product");
            initSystemServiceAndLoadDataAndLogIn();
            StoreDTO storeDTO = proxy.getStoreInfo("u3","s1");
            Assertions.assertTrue(storeDTO.productsInfoAmount.keySet().size()==2);
            Assertions.assertTrue(proxy.appointOtherMemberAsStoreOwner("u3","s1","u4"));

            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getAllOnlineMembers().contains("u1"));
            Assertions.assertTrue(proxy.getAllOnlineMembers().contains("u2"));
            Assertions.assertTrue(proxy.getAllOnlineMembers().contains("u3"));

            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertTrue(proxy.getPendingContractsForOwner("u1","s1").size()==1);
            initSystemServiceAndLoadDataAndLogIn();
            Assertions.assertThrows(Exception.class, ()-> proxy.removeOwnerByHisAppointer("u2","s1","u3"));

            //appointOtherMemberAsStoreOwner
            //removeOwnerByHisAppointer
            //appointOtherMemberAsStoreManager
            // create some policies




        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    private void initSystemServiceAndLoadDataAndLogIn() throws Exception {
        proxy= new ProxyBridge(new RealBridge());
        proxy.loadData();
    }
}
