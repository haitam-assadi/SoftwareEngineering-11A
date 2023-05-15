package PresentationLayer.model;

import java.util.List;

public class Bag {
    private String storeName;
    private List<Product> products;
    private double totalPrice;
    private String details;

    public Bag(String storeName, List<Product> products){
        this.storeName = storeName;
        this.products = products;
        totalPrice = 0;
        this.details = this.getDetails();
    }

    public double getTotalPrice() {
        totalPrice = 0;
        for(Product p : products){
            totalPrice += p.getTotalPrice();
        }
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
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

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
