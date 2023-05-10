package PresentationLayer.model;

public class Product {
    private String name;
    private Double price;
    private String description;
    private int amount;

    public Product(String name, Double price, String description, int amount){
        this.name = name;
        this.price = price;
        this.description = description;
        this.amount = amount;
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

    public String printDetails(){
        String result = "";
        result = result + "Product: " + this.name + "\nPrice: " + this.price + "\nDescription: " +
                this.description + "\nAmount: " + this.amount + "\n";
        return result;
    }

}
