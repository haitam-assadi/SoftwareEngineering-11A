package DomainLayer.DTO;

import java.util.Arrays;

public class StoreDTO {

    String storeName;
    String founderName;
    String[] ownersNames;
    String[] managersNames;
    String[] productsInfo; //TODO: maybe productDTO instead of String


    public StoreDTO(String storeName, String founderName, String[] ownersNames, String[] managersNames, String[] productsInfo) {
        this.storeName = storeName;
        this.founderName = founderName;
        this.ownersNames = ownersNames;
        this.managersNames = managersNames;
        this.productsInfo = productsInfo;
    }

    public String toString(){
        String st = "Store name: "+this.storeName+"\n";
        st = st + "Store founder: "+this.founderName+"\n";
        st = st + "Store owners: "+ Arrays.toString(this.ownersNames)+"\n";
        st = st + "Store managers: "+ Arrays.toString(this.managersNames)+"\n";
        // TODO: add products info
        return st;
    }
}
