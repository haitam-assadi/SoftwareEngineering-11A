package DatabaseTests;

import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class init2FileTest {


    private ProxyBridge proxy= new ProxyBridge(new RealBridge());

    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("setup");
        proxy = new ProxyBridge(new RealBridge());
        proxy.realBridge.systemService.firstManagerInitializer();
        proxy.realBridge.systemService.initMarketParsing();
    }
    //TODO: data base must be create-drop
    //TODO: config file must be true for both data base flasg and init file flag
    //TODO: loaded file must be init file 2


    @Test
    public void init2FileTest(){
        try{
            initSystemServiceAndLoadDataAndLogIn();

            String guest1 = proxy.enterMarket();
            Assertions.assertThrows(Exception.class, ()-> proxy.register(guest1, "u1", "u1userpass"));
            Assertions.assertThrows(Exception.class, ()-> proxy.register(guest1, "u2", "u2userpass"));
            Assertions.assertThrows(Exception.class, ()-> proxy.register(guest1, "u3", "u3userpass"));
            Assertions.assertThrows(Exception.class, ()-> proxy.register(guest1, "u4", "u4userpass"));
            Assertions.assertThrows(Exception.class, ()-> proxy.register(guest1, "u5", "u5userpass"));
            Assertions.assertThrows(Exception.class, ()-> proxy.register(guest1, "u6", "u6userpass"));

            Assertions.assertEquals(proxy.login(guest1,"u1","u1userpass"),"u1");

            Assertions.assertTrue(proxy.getAllSystemManagers("u1").contains("u1"));

            Assertions.assertThrows(Exception.class, ()-> proxy.login(guest1, "u2", "u2userpass"));

            Assertions.assertTrue(proxy.getAllMembers().contains("u1"));
            Assertions.assertTrue(proxy.getAllMembers().contains("u2"));
            Assertions.assertTrue(proxy.getAllMembers().contains("u3"));
            Assertions.assertTrue(proxy.getAllMembers().contains("u4"));
            Assertions.assertTrue(proxy.getAllMembers().contains("u5"));
            Assertions.assertTrue(proxy.getAllMembers().contains("u6"));

            Assertions.assertFalse(proxy.getAllOnlineMembers().isEmpty());

            Assertions.assertTrue(proxy.getAllStoresNames().size()==1);
            Assertions.assertTrue(proxy.getAllStoresNames().contains("s1"));


            String guest2 =  proxy.enterMarket();
            Assertions.assertEquals(proxy.login(guest2,"u2","u2userpass"),"u2");
            Assertions.assertTrue(proxy.getManagerPermissionsForStore("u2","s1","u3").contains(1));
            Assertions.assertTrue(proxy.getManagerPermissionsForStore("u2","s1","u3").contains(2));




        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    private void initSystemServiceAndLoadDataAndLogIn() throws Exception {
        proxy= new ProxyBridge(new RealBridge());
        proxy.loadData();
    }
}
