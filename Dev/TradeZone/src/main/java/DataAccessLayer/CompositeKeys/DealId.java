//package DataAccessLayer.CompositeKeys;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//public class DealId implements Serializable {
//
//    private String userName;
//    private String storeName;
//
//    public DealId(String userName, String storeName) {
//        this.userName = userName;
//        this.storeName = storeName;
//    }
//
//    public DealId() {
//
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        DealId dealId = (DealId) o;
//        return userName.equals(dealId.userName) && storeName.equals(dealId.storeName);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(userName, storeName);
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public String getStoreName() {
//        return storeName;
//    }
//}
