package DomainLayer;

import DTO.DealDTO;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Deal {

    private Store store;
    private User user;

    private String date;

    private ConcurrentHashMap<String, Double> products_prices;
    private ConcurrentHashMap<String, Integer> products_amount;
    private ConcurrentHashMap<String, Double> productPriceMultipleAmount;
    private ConcurrentHashMap<String, Double> productFinalPriceWithDiscount;
    private double totalPrice;


    public Deal(Store store, User user, String date, ConcurrentHashMap<String, Double> products_prices,
                ConcurrentHashMap<String, Integer> products_amount, ConcurrentHashMap<String, Double> productPriceMultipleAmount, ConcurrentHashMap<String, Double> productFinalPriceWithDiscount, double totalPrice){
        this.store = store;
        this.user = user;
        this.date = date;
        this.products_prices = products_prices;
        this.products_amount = products_amount;
        this.totalPrice=totalPrice;
        this.productPriceMultipleAmount=productPriceMultipleAmount;
        this.productFinalPriceWithDiscount=productFinalPriceWithDiscount;
    }

    public DealDTO getDealDTO() {
        return new DealDTO(this.store.getStoreName(), this.date, this.user.userName, this.products_amount, this.products_prices,
                this.productPriceMultipleAmount, this.productFinalPriceWithDiscount, this.totalPrice);
    }

    public String getDealUserName(){
        return this.user.userName;
    }
}
