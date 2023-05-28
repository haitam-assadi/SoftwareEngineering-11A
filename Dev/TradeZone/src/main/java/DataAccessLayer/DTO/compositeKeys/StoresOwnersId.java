package DataAccessLayer.DTO.compositeKeys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StoresOwnersId implements Serializable {

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "store_name")
    private String storeName;

    public StoresOwnersId(){

    }

    public StoresOwnersId(String ownerName, String storeName) {
        this.ownerName = ownerName;
        this.storeName = storeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        StoresOwnersId that = (StoresOwnersId) o;
        return Objects.equals(ownerName, that.ownerName) &&
                Objects.equals(storeName, that.storeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerName,storeName);
    }
}
