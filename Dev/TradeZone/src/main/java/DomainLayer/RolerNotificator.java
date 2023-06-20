package DomainLayer;

import javax.persistence.*;

@Entity
@Table
public class RolerNotificator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn
    private StoreRules storeRules;

    private String memberName;

    public RolerNotificator(){}
    public RolerNotificator(StoreRules storeRules,String memberName){
        this.storeRules = storeRules;
        this.memberName = memberName;
    }

    public int getId() {
        return id;
    }

    public StoreRules getStoreRules() {
        return storeRules;
    }

    public String getMemberName() {
        return memberName;
    }
}
