package PresentationLayer.model;

import java.util.List;

public class Bag {
    public String storeName;
    public List<Product> products;
    public String details;

    public Bag(String storeName, List<Product> products){
        this.storeName = storeName;
        this.products = products;
        this.details = this.getDetails();
    }

    public String getDetails(){
        String result = "";
        for(Product product : products){
            result = result + product.printDetails();
        }
        return result;
    }

    public String getStoreName(){
        return this.storeName;
    }
}
