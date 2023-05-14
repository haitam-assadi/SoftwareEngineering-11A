package PresentationLayer.model;

public class Purchase {
    String cardNumber;
    String month;
    String year;
    String holder;
    String cvv;
    String id;
    String receiverName;
    String shipmentAddress;
    String shipmentCity;
    String shipmentCountry;
    String zipCode;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getShipmentAddress() {
        return shipmentAddress;
    }

    public void setShipmentAddress(String shipmentAddress) {
        this.shipmentAddress = shipmentAddress;
    }

    public String getShipmentCity() {
        return shipmentCity;
    }

    public void setShipmentCity(String shipmentCity) {
        this.shipmentCity = shipmentCity;
    }

    public String getShipmentCountry() {
        return shipmentCountry;
    }

    public void setShipmentCountry(String shipmentCountry) {
        this.shipmentCountry = shipmentCountry;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
