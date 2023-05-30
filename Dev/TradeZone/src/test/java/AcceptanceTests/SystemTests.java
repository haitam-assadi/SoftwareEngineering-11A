package AcceptanceTests;
import PresentationLayer.SpringbootHtmlApplication;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SpringbootHtmlApplication.class)
public class SystemTests {

    private ProxyBridge proxy;

    @BeforeAll
    public void setUp(){
        proxy = new ProxyBridge(new RealBridge());
    }

    @Test
    public void initialize_market_success(){
        try{
            Assertions.assertTrue(!proxy.initializeMarket().isEmpty());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

}