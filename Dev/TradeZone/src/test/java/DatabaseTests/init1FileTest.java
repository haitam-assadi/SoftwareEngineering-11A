package DatabaseTests;

import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class init1FileTest {

    private ProxyBridge proxy= new ProxyBridge(new RealBridge());


    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("setup");
        proxy.realBridge.systemService.initMarketParsing();
    }


    @Test
    public void init1FileTest(){
        try{

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    private void initSystemServiceAndLoadDataAndLogIn() throws Exception {
        proxy= new ProxyBridge(new RealBridge());
        proxy.loadData();
    }
}
