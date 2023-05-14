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
        validatePayArgs(price,cardNumber,month,year,holder,ccv,id);
        if(demo)
            return demoTransactionIds++;

        throw new Exception("not connected to any service");
    }

    public void validatePayArgs(Double price, String cardNumber, String month, String year, String holder, String ccv, String id) throws Exception {
        if(price==null)
            throw new Exception("Price cant be null");
        if(cardNumber==null)
            throw new Exception("Card Number cant be null");
        if(month==null)
            throw new Exception("month cant be null");
        if(year==null)
            throw new Exception("year cant be null");
        if(holder==null)
            throw new Exception("holder name cant be null");
        if(ccv==null)
            throw new Exception("ccv name cant be null");
        if(id==null)
            throw new Exception("id name cant be null");
        if(!cardNumber.matches("[0-9]+"))
            throw new Exception("Card Number must contain numbers only");
        if(!month.matches("[0-9]+"))
            throw new Exception("month must contain numbers only");
        if(!year.matches("[0-9]+"))
            throw new Exception("year must contain numbers only");
        if(!ccv.matches("[0-9]+"))
            throw new Exception("ccv must contain numbers only");
        if(!id.matches("[0-9]+"))
            throw new Exception("id must contain numbers only");
        if(Integer.parseInt(month) < 1 || Integer.parseInt(month)>12)
            throw new Exception("month have to be between 1 and 12");
        if(ccv.length() != 3)
            throw new Exception("ccv have to be 3 digits");
    }

    public boolean cancelPay(int transactionId) throws Exception {
        if(demo)
            return true;
        throw new Exception("not connected to any service");
    }
}
