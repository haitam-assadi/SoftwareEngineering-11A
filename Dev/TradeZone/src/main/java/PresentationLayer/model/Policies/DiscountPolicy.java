package PresentationLayer.model.Policies;

public class DiscountPolicy {
    private String storeName = "";
    private String discountType = "";
    private String activate; // on off
    private String constraintId;
    private String productName;
    private String categoryName;
    private String[] percent;
    private String[] firstID;
    private String[] secondID;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getActivate() {
        return activate;
    }

    public void setActivate(String activate) {
        this.activate = activate;
    }

    public String getConstraintId() {
        return constraintId;
    }

    public void setConstraintId(String constraintId) {
        this.constraintId = constraintId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getPercent(int i) {
        return Integer.parseInt(percent[i]);
    }

    public void setPercent(String[] percent) {
        this.percent = percent;
    }

    public int getFirstID(int i) {
        return Integer.parseInt(firstID[i]);
    }

    public void setFirstID(String[] firstID) {
        this.firstID = firstID;
    }

    public int getSecondID(int i) {
        return Integer.parseInt(secondID[i]);
    }

    public void setSecondID(String[] secondID) {
        this.secondID = secondID;
    }
}
