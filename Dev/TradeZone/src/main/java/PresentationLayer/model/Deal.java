package PresentationLayer.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static PresentationLayer.model.GeneralModel.round;

public class Deal {
    private String storeName;
    private String date;
    private String userName;
    private Map<String, List<Double>> products; // <productName, <amount, price>>
    private Map<String, Double> productPriceMultipleAmount;
    private Map<String, Double> productFinalPriceWithDiscount;
    private double totalPrice;

    public Deal(String storeName, String date, String username,
                Map<String, List<Double>> products, double totalPrice,
                Map<String, Double> productPriceMultipleAmount,
                Map<String, Double> productFinalPriceWithDiscount){
        this.storeName = storeName;
        this.date = date;
        this.userName = username;
        this.products = products;
        this.totalPrice = totalPrice;
        this.productPriceMultipleAmount = productPriceMultipleAmount;
        this.productFinalPriceWithDiscount = productFinalPriceWithDiscount;
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

    public Map<String, Double> getProductPriceMultipleAmount() {
        return productPriceMultipleAmount;
    }

    public void setProductPriceMultipleAmount(Map<String, Double> productPriceMultipleAmount) {
        this.productPriceMultipleAmount = productPriceMultipleAmount;
    }

    public Map<String, Double> getProductFinalPriceWithDiscount() {
        return productFinalPriceWithDiscount;
    }

    public void setProductFinalPriceWithDiscount(Map<String, Double> productFinalPriceWithDiscount) {
        this.productFinalPriceWithDiscount = productFinalPriceWithDiscount;
    }

    public double pTotalPrice(String productName){
        return round(this.productPriceMultipleAmount.getOrDefault(productName, -1.0), 2);
    }

    public double pTotalDiscountPrice(String productName){
        return round(this.productFinalPriceWithDiscount.getOrDefault(productName, -1.0), 2);
    }

    public boolean hasDiscount(String productName){
        double fPrice = this.productPriceMultipleAmount.getOrDefault(productName, -1.0);
        double sPrice = this.productFinalPriceWithDiscount.getOrDefault(productName, -1.0);
        return (fPrice != sPrice);
    }
}
