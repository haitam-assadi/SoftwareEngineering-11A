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
    public RolerNotificator(StoreRules storeRules){
        this.storeRules = storeRules;
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
