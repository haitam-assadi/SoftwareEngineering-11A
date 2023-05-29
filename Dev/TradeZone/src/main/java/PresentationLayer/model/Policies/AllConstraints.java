package PresentationLayer.model.Policies;

import java.util.List;

public class AllConstraints {
    private String storeName;
    private List<String> allBagConstraints;
    private List<String> activeBagConstraints;
    private List<String> allDiscountPolicies;
    private List<String> activeDiscountPolicies;

    public AllConstraints(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<String> getAllBagConstraints() {
        return allBagConstraints;
    }

    public void setAllBagConstraints(List<String> allBagConstraints) {
        this.allBagConstraints = allBagConstraints;
    }

    public List<String> getActiveBagConstraints() {
        return activeBagConstraints;
    }

    public void setActiveBagConstraints(List<String> activeBagConstraints) {
        this.activeBagConstraints = activeBagConstraints;
    }

    public List<String> getAllDiscountPolicies() {
        return allDiscountPolicies;
    }

    public void setAllDiscountPolicies(List<String> allDiscountPolicies) {
        this.allDiscountPolicies = allDiscountPolicies;
    }

    public List<String> getActiveDiscountPolicies() {
        return activeDiscountPolicies;
    }

    public void setActiveDiscountPolicies(List<String> activeDiscountPolicies) {
        this.activeDiscountPolicies = activeDiscountPolicies;
    }
}
