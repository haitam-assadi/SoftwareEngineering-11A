package DTO;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class StoreDTO {

    public String storeName;
    public String founderName;
    public List<String> ownersNames;
    public List<String> managersNames;
    public ConcurrentHashMap<ProductDTO, Integer> productsInfoAmount; //TODO: maybe productDTO instead of String

    public boolean isActive;


    public StoreDTO(String storeName, String founderName, List<String> ownersNames, List<String> managersNames, ConcurrentHashMap<ProductDTO, Integer> productsInfoAmount,boolean isActive) {
        this.storeName = storeName;
        this.founderName = founderName;
        this.ownersNames = ownersNames;
        this.managersNames = managersNames;
        this.productsInfoAmount = productsInfoAmount;
        this.isActive = isActive;
    }

    public String toString(){
        String st = "Store name: "+this.storeName+"\n";
        st = st + "Store founder: "+this.founderName+"\n";
       // st = st + "Store owners: "+ Arrays.toString(this.ownersNames)+"\n";
       // st = st + "Store managers: "+ Arrays.toString(this.managersNames)+"\n";
        // TODO: add products infoboo
        return st;
    }
}
