package PresentationLayer.model;

import static PresentationLayer.model.GeneralModel.round;

public class Product {
    private String name;
    private Double price;
    private Double totalPrice;
    private String description;
    private int amount;
    private String category;

    public Product(String name, Double price, String description, int amount){
        this.name = name;
        this.price = price;
        this.description = description;
        this.amount = amount;
        totalPrice = price * amount;
    }

    public String getName(){
       return this.name;
    }

    public Double getPrice(){
        return this.price;
    }

    public String getDescription(){
        return this.description;
    }

    public int getAmount(){
        return this.amount;
    }

    public Double getTotalPrice() {
        totalPrice = round(price * amount, 2);
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {

        this.totalPrice = totalPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String printDetails(){
        String result = "";
        result = result + "Product: " + this.name + "\nPrice: " + this.price + "\nDescription: " +
                this.description + "\nAmount: " + this.amount + "\n";
        return result;
    }

}
