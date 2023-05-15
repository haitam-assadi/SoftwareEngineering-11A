package DomainLayer;

public class ShipmentService {

    String url;
    boolean demo;
    int demoTransactionIds=1;
    public ShipmentService(String url){
        this.url = url;
        demo = (url=="");
    }


    public int supply(String receiverName,String shipmentAddress,String shipmentCity,String shipmentCountry,String zipCode) throws Exception {
        validateSupplyArgs(receiverName,shipmentAddress,shipmentCity,shipmentCountry,zipCode);
        if(demo)
            return demoTransactionIds++;

        throw new Exception("not connected to any service");
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
        throw new Exception("not connected to any service");
    }
}
