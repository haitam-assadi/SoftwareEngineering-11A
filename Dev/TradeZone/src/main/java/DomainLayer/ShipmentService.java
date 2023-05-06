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
        if(demo)
            return demoTransactionIds++;

        throw new Exception("not connected to any service");
    }

    public boolean cancelSupply(int supplyId) throws Exception {
        if(demo)
            return true;
        throw new Exception("not connected to any service");
    }
}
