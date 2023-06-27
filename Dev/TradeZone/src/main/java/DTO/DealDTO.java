package DTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DealDTO {
    public String storeName;
    public String date;
    public String username;
    public Map<String, Integer> products_amount;
    public Map<String, Double> products_prices;
    public Map<String, Double> productPriceMultipleAmount;
    public Map<String, Double> productFinalPriceWithDiscount;
    public double totalPrice;

    public DealDTO(String storeName, String date, String username, Map<String, Integer> products_amount, Map<String, Double> products_prices,
                   Map<String, Double> productPriceMultipleAmount, Map<String, Double> productFinalPriceWithDiscount, double totalPrice){
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
