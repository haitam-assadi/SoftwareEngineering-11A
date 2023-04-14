package AcceptanceTests;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class SystemTests {

    private ProxyBridge proxy;
    @Mock
    private Bridge bridge;

    @BeforeAll
    public void setUp(){
        MockitoAnnotations.openMocks(this);
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
