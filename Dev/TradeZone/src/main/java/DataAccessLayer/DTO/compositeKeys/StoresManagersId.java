package DataAccessLayer.DTO.compositeKeys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StoresManagersId implements Serializable {
    @Column(name = "manager_name")
    private String managerName;

    @Column(name = "store_name")
    private String storeName;

    public StoresManagersId(String managerName, String storeName) {
        this.managerName = managerName;
        this.storeName = storeName;
    }

    public StoresManagersId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        StoresManagersId that = (StoresManagersId) o;
        return Objects.equals(managerName, that.managerName) &&
                Objects.equals(storeName, that.storeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(managerName,storeName);
    }
}
