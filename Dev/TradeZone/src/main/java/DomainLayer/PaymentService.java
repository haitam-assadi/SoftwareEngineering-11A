package DomainLayer;

import java.util.TreeMap;

public class PaymentService {
    String url;
    boolean demo;
    int demoTransactionIds=1;

    public PaymentService(String url){
        this.url = url;
        demo = (url=="");
    }

    public int pay(Double price, String cardNumber, String month, String year, String holder, String ccv, String id) throws Exception {
        if(demo)
            return demoTransactionIds++;

        throw new Exception("not connected to any service");
    }
    public boolean cancelPay(int transactionId) throws Exception {
        if(demo)
            return true;
        throw new Exception("not connected to any service");
    }
}
