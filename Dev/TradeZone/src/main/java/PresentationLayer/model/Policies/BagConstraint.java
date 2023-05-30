package PresentationLayer.model.Policies;

public class BagConstraint {
    private String storeName = "";
    private String constType = "";
    private String activate; // on off
    private String[] productName;
    private String[] categoryName;
    private String[] hour;
    private String[] startDate;
    private String[] endDate;
    private String[] min;
    private String[] max;
    private String[] firstID;
    private String[] secondID;

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

    public String getProductName(int i) {
        return productName[i];
    }

    public void setProductName(String[] productName) {
        this.productName = productName;
    }

    public String getCategoryName(int i) {
        return categoryName[i];
    }

    public void setCategoryName(String[] categoryName) {
        this.categoryName = categoryName;
    }

    public int getHour(int i) {
        String[] parts = hour[i].split(":");
        return Integer.parseInt(parts[0]);
    }

    public int getMinutes(int i) {
        String[] parts = hour[i].split(":");
        return Integer.parseInt(parts[1]);
    }

    public void setHour(String[] hour) {
        this.hour = hour;
    }

    public int getFromYear(int i) {
        String[] parts = startDate[i].split("-");
        return Integer.parseInt(parts[0]);
    }

    public int getFromMonth(int i) {
        String[] parts = startDate[i].split("-");
        return Integer.parseInt(parts[1]);
    }

    public int getFromDay(int i) {
        String[] parts = startDate[i].split("-");
        return Integer.parseInt(parts[2]);
    }

    public String[] getStartDate() {
        return startDate;
    }

    public void setStartDate(String[] startDate) {
        this.startDate = startDate;
    }

    public int getToYear(int i) {
        String[] parts = endDate[i].split("-");
        return Integer.parseInt(parts[0]);
    }

    public int getToMonth(int i) {
        String[] parts = endDate[i].split("-");
        return Integer.parseInt(parts[1]);
    }

    public int getToDay(int i) {
        String[] parts = endDate[i].split("-");
        return Integer.parseInt(parts[2]);
    }

    public String[] getEndDate() {
        return endDate;
    }

    public void setEndDate(String[] endDate) {
        this.endDate = endDate;
    }

    public int getMin(int i) {
        return Integer.parseInt(min[i]);
    }

    public void setMin(String[] min) {
        this.min = min;
    }

    public int getMax(int i) {
        return Integer.parseInt(max[i]);
    }

    public void setMax(String[] max) {
        this.max = max;
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
