package DomainLayer;

import DomainLayer.DTO.DealDTO;

import java.util.HashMap;

public class Deal {

    private Store store;
    private User user;

    private String date;

    private HashMap<String, Double> products_prices;              //<productName, productPrice>

    private HashMap<String, Integer> products_amount;              //<productName, productAmount>

    private double totalPrice;


    public Deal(Store store, User user, String date, HashMap<String, Double> products_prices,  HashMap<String,
            Integer> products_amount, double totalPrice){
        this.store = store;
        this.user = user;
        this.date = date;
        this.products_prices = products_prices;
        this.products_amount = products_amount;
        this.totalPrice = totalPrice;
    }

    public DealDTO getDealDTO() {
        //DealDTO(String storeName, String date, String username, HashMap<String, Double> products_prices,
        //                   HashMap<String, Integer> products_amount, double totalPrice){
        return new DealDTO(this.store.getStoreName(), this.date, this.user.userName, this.products_prices, this.products_amount,
                this.totalPrice);
    }

    public String getDealUserName(){
        return this.user.userName;
    }
}
