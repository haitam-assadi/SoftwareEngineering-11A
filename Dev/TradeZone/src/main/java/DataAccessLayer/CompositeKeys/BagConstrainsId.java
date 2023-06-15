package DataAccessLayer.CompositeKeys;

import java.io.Serializable;
import java.util.Objects;

public class BagConstrainsId implements Serializable {

    int id;
    String storeName;

    public BagConstrainsId(int id,String storeName){
        this.id = id;
        this.storeName =storeName;
    }

    public BagConstrainsId(){}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BagConstrainsId bagConstrainsId = (BagConstrainsId) o;
        return id == bagConstrainsId.id && storeName.equals(bagConstrainsId.storeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, storeName);
    }

    public int getId() {
        return id;
    }

    public String getStoreName() {
        return storeName;
    }
}
