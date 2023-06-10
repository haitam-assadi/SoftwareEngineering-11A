package DomainLayer;

import java.util.HashMap;

public class ShipmentService extends ExternalService{

    public ShipmentService(String url){
        serviceType="Shipment";
        this.url = url;
        demo = (url=="");
    }


    public int supply(String receiverName,String shipmentAddress,String shipmentCity,String shipmentCountry,String zipCode) throws Exception {
        validateSupplyArgs(receiverName,shipmentAddress,shipmentCity,shipmentCountry,zipCode);
        if(demo)
            return demoTransactionIds++;

        HashMap<String, String> action_params = new HashMap<>();
        action_params.put("action_type","supply");
        action_params.put("name",receiverName);
        action_params.put("address",shipmentAddress);
        action_params.put("city",shipmentCity);
        action_params.put("country",shipmentCountry);
        action_params.put("zip",zipCode);

        String serviceAnswer = sendPostRequest(action_params, serviceResTimeInSeconds).strip();

        if(!isNumeric(serviceAnswer))
            throw new Exception("transaction has failed");

        Integer transactionId = Integer.parseInt(serviceAnswer);

        if(transactionId<10000 || transactionId> 100000)
            throw new Exception("transaction has failed");

        return transactionId;
    }
    public void validateSupplyArgs(String receiverName,String shipmentAddress,String shipmentCity,String shipmentCountry,String zipCode) throws Exception {
        if(receiverName==null)
            throw new Exception("Receiver Name cant be null");
        if(shipmentAddress==null)
            throw new Exception("Shipment Address Name cant be null");
        if(shipmentCity==null)
            throw new Exception("Shipment City Name cant be null");
        if(shipmentCountry==null)
            throw new Exception("Shipment Country Name cant be null");
        if(zipCode==null)
            throw new Exception("zip Code Name cant be null");
        if(!zipCode.matches("[0-9]+"))
            throw new Exception("zip Code must contain numbers only");
    }

    public boolean cancelSupply(int supplyId) throws Exception {
        if(demo)
            return true;
        HashMap<String, String> action_params = new HashMap<>();
        action_params.put("action_type","cancel_supply");
        action_params.put("transaction_id",Integer.toString(supplyId));

        String serviceAnswer = sendPostRequest(action_params, serviceResTimeInSeconds).strip();

        if(!isNumeric(serviceAnswer))
            throw new Exception("canceling supply transaction has failed");

        Integer numericServiceAnswer = Integer.parseInt(serviceAnswer);

        if(numericServiceAnswer != 1)
            throw new Exception("canceling supply transaction has failed");

        return true;
    }
}
