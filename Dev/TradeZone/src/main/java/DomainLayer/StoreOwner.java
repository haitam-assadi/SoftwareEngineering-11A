package DomainLayer;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "StoreOwner")
@PrimaryKeyJoinColumn(name = "member_name")
public class StoreOwner extends AbstractStoreOwner implements Serializable {


    public StoreOwner(Member member) {
        super(member);
        this.myRole=RoleEnum.StoreOwner;

    }
    public StoreOwner(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreOwner storeOwner = (StoreOwner) o;
        return getUserName().equals(storeOwner.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName());
    }

    @Override
    public String toString() {
        return "StoreOwner{" +
                "myRole=" + myRole.toString() +
                ", roleID=" + id.toString() +
                //", member=" + member.toString() +
                '}';
    }
}
