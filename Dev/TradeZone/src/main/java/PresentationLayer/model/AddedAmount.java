package PresentationLayer.model;

public class AddedAmount {
    private int amount;

    private String name;

    public String getProductName(){
        return this.name;
    }

    public void setProductName(String productName){
        this.name = productName;
    }

    public int getAmount(){
        return this.amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }
}
