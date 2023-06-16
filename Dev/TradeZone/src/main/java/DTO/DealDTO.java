package DTO;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DealDTO {
    public String storeName;
    public String date;
    public String username;
    public ConcurrentHashMap<String, Integer> products_amount;
    public ConcurrentHashMap<String, Double> products_prices;
    public ConcurrentHashMap<String, Double> productPriceMultipleAmount;
    public ConcurrentHashMap<String, Double> productFinalPriceWithDiscount;
    public double totalPrice;

    public DealDTO(String storeName, String date, String username, ConcurrentHashMap<String, Integer> products_amount, ConcurrentHashMap<String, Double> products_prices,
                   ConcurrentHashMap<String, Double> productPriceMultipleAmount, ConcurrentHashMap<String, Double> productFinalPriceWithDiscount, double totalPrice){
        this.storeName = storeName;
        this.date = date;
        this.username = username;
        this.totalPrice=totalPrice;
        this.products_amount=products_amount;
        this.products_prices = products_prices;
        this.productPriceMultipleAmount=productPriceMultipleAmount;
        this.productFinalPriceWithDiscount=productFinalPriceWithDiscount;
    }

}
