package PresentationLayer.model;

import java.util.List;

public class Store {
    private String storeName;
    private boolean isActive;
    private List<Deal> deals;
    public Store(String storeName, boolean isActive, List<Deal> deals){
        this.storeName = storeName;
        this.isActive = isActive;
        this.deals = deals;
    }
    public String getStoreName(){
        return this.storeName;
    }

    public void setStoreName(String storeName){
        this.storeName = storeName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public void setDeals(List<Deal> deals) {
        this.deals = deals;
    }

}
