package PresentationLayer.model.Policies;

public class BagConstraint {
    private String storeName = "";
    private String constType = "";
    private String activate; // on off
    private String[] productName;
    private String[] categoryName;
    private String[] hour; // ???
    private String[] startDate; // ???
    private String[] endDate; // ???
    private String[] min; // - string
    private String[] max; // -
    private String[] firstID; // -
    private String[] secondID; // - String[]

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getConstType() {
        return constType;
    }

    public void setConstType(String constType) {
        this.constType = constType;
    }

    public String getActivate() {
        return activate;
    }

    public void setActivate(String activate) {
        this.activate = activate;
    }

    public String getProductName() {
        return productName[0];
    }

    public void setProductName(String[] productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName[0];
    }

    public void setCategoryName(String[] categoryName) {
        this.categoryName = categoryName;
    }

    public int getHour() {
        String[] parts = hour[0].split(":");
        return Integer.parseInt(parts[0]);
    }

    public int getMinutes() {
        String[] parts = hour[0].split(":");
        return Integer.parseInt(parts[1]);
    }

    public void setHour(String[] hour) {
        this.hour = hour;
    }

    public int getFromYear() {
        String[] parts = startDate[0].split("-");
        return Integer.parseInt(parts[0]);
    }

    public int getFromMonth() {
        String[] parts = startDate[0].split("-");
        return Integer.parseInt(parts[1]);
    }

    public int getFromDay() {
        String[] parts = startDate[0].split("-");
        return Integer.parseInt(parts[2]);
    }

    public String[] getStartDate() {
        return startDate;
    }

    public void setStartDate(String[] startDate) {
        this.startDate = startDate;
    }

    public int getToYear() {
        String[] parts = endDate[0].split("-");
        return Integer.parseInt(parts[0]);
    }

    public int getToMonth() {
        String[] parts = endDate[0].split("-");
        return Integer.parseInt(parts[1]);
    }

    public int getToDay() {
        String[] parts = endDate[0].split("-");
        return Integer.parseInt(parts[2]);
    }

    public String[] getEndDate() {
        return endDate;
    }

    public void setEndDate(String[] endDate) {
        this.endDate = endDate;
    }

    public int getMin() {
        return Integer.parseInt(min[0]);
    }

    public void setMin(String[] min) {
        this.min = min;
    }

    public int getMax() {
        return Integer.parseInt(max[0]);
    }

    public void setMax(String[] max) {
        this.max = max;
    }

    public int getFirstID() {
        return Integer.parseInt(firstID[0]);
    }

    public void setFirstID(String[] firstID) {
        this.firstID = firstID;
    }

    public int getSecondID() {
        return Integer.parseInt(secondID[0]);
    }

    public void setSecondID(String[] secondID) {
        this.secondID = secondID;
    }
}
