package PresentationLayer.model;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static PresentationLayer.model.GeneralModel.round;

public class Bag {
    private String storeName;
    private List<Product> products;
    private double totalPrice;
    public ConcurrentHashMap<String, Double> productPriceMultipleAmount;
    public ConcurrentHashMap<String, Double> productFinalPriceWithDiscount;
    private String details;

    public Bag(String storeName, List<Product> products,
               ConcurrentHashMap<String, Double> productPriceMultipleAmount,
               ConcurrentHashMap<String, Double> productFinalPriceWithDiscount){
        this.storeName = storeName;
        this.products = products;
        totalPrice = 0;
        this.productPriceMultipleAmount = productPriceMultipleAmount;
        this.productFinalPriceWithDiscount = productFinalPriceWithDiscount;
        this.details = this.getDetails();
    }

    public double getTotalPrice() {
        // TODO: change the impl
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

    public ConcurrentHashMap<String, Double> getProductPriceMultipleAmount() {
        return productPriceMultipleAmount;
    }

    public void setProductPriceMultipleAmount(ConcurrentHashMap<String, Double> productPriceMultipleAmount) {
        this.productPriceMultipleAmount = productPriceMultipleAmount;
    }

    public ConcurrentHashMap<String, Double> getProductFinalPriceWithDiscount() {
        return productFinalPriceWithDiscount;
    }

    public void setProductFinalPriceWithDiscount(ConcurrentHashMap<String, Double> productFinalPriceWithDiscount) {
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
