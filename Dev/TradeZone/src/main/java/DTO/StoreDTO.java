package DTO;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class StoreDTO {

    public String storeName;
    public String founderName;
    public List<String> ownersNames;
    public List<String> managersNames;
    public Map<ProductDTO, Integer> productsInfoAmount; //TODO: maybe productDTO instead of String

    public boolean isActive;


    public StoreDTO(String storeName, String founderName, List<String> ownersNames, List<String> managersNames, Map<ProductDTO, Integer> productsInfoAmount,boolean isActive) {
        this.storeName = storeName;
        this.founderName = founderName;
        this.ownersNames = ownersNames;
        this.managersNames = managersNames;
        this.productsInfoAmount = productsInfoAmount;
//        this.productsInfoAmount = sortProducts(productsInfoAmount);
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

    private Map<ProductDTO, Integer> sortProducts(Map<ProductDTO, Integer> products){
//        Map<ProductDTO, Integer> sorted = new TreeMap<>(Comparator.comparing(o -> (o.name)));
        Map<ProductDTO, Integer> sorted = new TreeMap<>(new Comparator<ProductDTO>() {
            @Override
            public int compare(ProductDTO o1, ProductDTO o2) {
                return o1.name.compareTo(o2.name);
            }
        });
        sorted.putAll(products);
        return sorted;
    }
}
