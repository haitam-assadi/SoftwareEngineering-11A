package PresentationLayer.model;

public class ItemDetails {
    private int amount;
    private String name;
    private String storeName;

    public int getAmount(){
        return this.amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
