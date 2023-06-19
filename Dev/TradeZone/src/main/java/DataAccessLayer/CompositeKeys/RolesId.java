package DataAccessLayer.CompositeKeys;//package DataAccessLayer.DTO.compositeKeys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RolesId implements Serializable {

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "store_name")
    private String storeName;

//    @OneToOne
//    @JoinColumn(name = "myBoss")
//    private Member member;

    public RolesId(){

    }

    public RolesId(String memberName, String storeName) {
        this.memberName = memberName;
        this.storeName = storeName;
    }

//    public RolesId(String memberName, String storeName,Member member) {
//        this.memberName = memberName;
//        this.storeName = storeName;
//        this.member = member;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        RolesId that = (RolesId) o;
        return Objects.equals(memberName, that.memberName) &&
                Objects.equals(storeName, that.storeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberName,storeName);
    }

    @Override
    public String toString() {
        return "RolesId{" +
                "memberName='" + memberName + '\'' +
                ", storeName='" + storeName + '\'' +
                '}';
    }

    public String getMemberName() {
        return memberName;
    }

    public String getStoreName() {
        return storeName;
    }
}
