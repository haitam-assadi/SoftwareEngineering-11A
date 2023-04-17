package AcceptanceTests;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class SystemTests {

    private ProxyBridge proxy;

    @BeforeAll
    public void setUp(){
        proxy = new ProxyBridge();
    }

    @Test
    public void initialize_market_success(){
        try{
            Assertions.assertTrue(proxy.initializeMarket());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

}