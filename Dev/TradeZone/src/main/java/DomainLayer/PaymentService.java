package DomainLayer;

import java.util.HashMap;
import java.util.TreeMap;

public class PaymentService extends ExternalService{


    public PaymentService(String url){
        serviceType="Payment";
        this.url = url;
        demo = (url=="");
    }

    public int pay(Double price, String cardNumber, String month, String year, String holder, String ccv, String id) throws Exception {
        validatePayArgs(price,cardNumber,month,year,holder,ccv,id);
        if(demo)
            return demoTransactionIds++;

        HashMap<String, String> action_params = new HashMap<>();
        action_params.put("action_type","pay");
        action_params.put("card_number",cardNumber);
        action_params.put("month",month);
        action_params.put("year",year);
        action_params.put("holder",holder);
        action_params.put("ccv",ccv);
        action_params.put("id",id);

        String serviceAnswer = sendPostRequest(action_params, serviceResTimeInSeconds).strip();

        if(!isNumeric(serviceAnswer))
            throw new Exception("transaction has failed");

        Integer transactionId = Integer.parseInt(serviceAnswer);

        if(transactionId<10000 || transactionId> 100000)
            throw new Exception("transaction has failed");

        return transactionId;
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

        HashMap<String, String> action_params = new HashMap<>();
        action_params.put("action_type","cancel_pay");
        action_params.put("transaction_id",Integer.toString(transactionId));

        String serviceAnswer = sendPostRequest(action_params, serviceResTimeInSeconds).strip();

        if(!isNumeric(serviceAnswer))
            throw new Exception("canceling transaction has failed");

        Integer numericServiceAnswer = Integer.parseInt(serviceAnswer);

        if(numericServiceAnswer != 1)
            throw new Exception("canceling transaction has failed");

        return true;
    }
}
