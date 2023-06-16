package DomainLayer;

import DTO.DealDTO;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Deal {

    private String storeName;
    private String userName;

    private String date;

    private ConcurrentHashMap<String, Double> products_prices;
    private ConcurrentHashMap<String, Integer> products_amount;
    private ConcurrentHashMap<String, Double> productPriceMultipleAmount;
    private ConcurrentHashMap<String, Double> productFinalPriceWithDiscount;
    private double totalPrice;


    public Deal(String storeName, String userName, String date, ConcurrentHashMap<String, Double> products_prices,
                ConcurrentHashMap<String, Integer> products_amount, ConcurrentHashMap<String, Double> productPriceMultipleAmount, ConcurrentHashMap<String, Double> productFinalPriceWithDiscount, double totalPrice){
        this.storeName = storeName;
        this.userName = userName;
        this.date = date;
        this.products_prices = products_prices;
        this.products_amount = products_amount;
        this.totalPrice=totalPrice;
        this.productPriceMultipleAmount=productPriceMultipleAmount;
        this.productFinalPriceWithDiscount=productFinalPriceWithDiscount;
    }

    public DealDTO getDealDTO() {
        return new DealDTO(this.storeName, this.date, this.userName, this.products_amount, this.products_prices,
                this.productPriceMultipleAmount, this.productFinalPriceWithDiscount, this.totalPrice);
    }

    public String getDealUserName(){
        return this.userName;
    }
}
