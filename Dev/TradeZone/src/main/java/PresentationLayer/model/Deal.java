package PresentationLayer.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deal {
    private String storeName;
    private String date;
    private String userName;
    private Map<String, List<Double>> products; // <productName, <amount, price>>
    private double totalPrice;

    public Deal(String storeName, String date, String username,
                Map<String, List<Double>> products, double totalPrice){
        this.storeName = storeName;
        this.date = date;
        this.userName = username;
        this.products = products;
        this.totalPrice = totalPrice;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map<String, List<Double>> getProducts() {
        return products;
    }

    public void setProducts(Map<String, List<Double>> products) {
        this.products = products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
