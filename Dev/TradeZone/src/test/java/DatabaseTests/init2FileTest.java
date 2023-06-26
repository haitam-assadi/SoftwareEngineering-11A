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
        proxy.realBridge.systemService.firstManagerInitializer();
        proxy.realBridge.systemService.initMarketParsing();
    }
    //TODO: data base must be create-drop
    //TODO: config file must be true for both data base flasg and init file flag
    //TODO: loaded file must be init file 2


    @Test
    public void init2FileTest(){
        try{


        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }

    }
}
