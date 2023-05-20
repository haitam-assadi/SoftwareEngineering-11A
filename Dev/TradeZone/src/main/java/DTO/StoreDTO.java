package DTO;

import java.util.List;

public class StoreDTO {

    public String storeName;
    public String founderName;
    public List<String> ownersNames;
    public List<String> managersNames;
    public List<ProductDTO> productsInfo; //TODO: maybe productDTO instead of String

    public boolean isActive;


    public StoreDTO(String storeName, String founderName, List<String> ownersNames, List<String> managersNames, List<ProductDTO> productsInfo,boolean isActive) {
        this.storeName = storeName;
        this.founderName = founderName;
        this.ownersNames = ownersNames;
        this.managersNames = managersNames;
        this.productsInfo = productsInfo;
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
