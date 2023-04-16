package DTO;

import java.util.HashMap;
import java.util.List;

public class DealDTO {
    public String storeName;
    public String date;
    public String username;
    public HashMap<String, Double> products_prices;              //<productName, productPrice>

    public HashMap<String, Integer> products_amount;              //<productName, productAmount>

    public double totalPrice;

    public DealDTO(String storeName, String date, String username, HashMap<String, Double> products_prices,
                   HashMap<String, Integer> products_amount, double totalPrice){
        this.storeName = storeName;
        this.date = date;
        this.username = username;
        this.products_prices = products_prices;
        this.products_amount = products_amount;
        this.totalPrice = totalPrice;
    }
}
