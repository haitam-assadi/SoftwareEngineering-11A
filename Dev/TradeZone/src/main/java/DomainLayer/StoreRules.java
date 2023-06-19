package DomainLayer;

import javax.persistence.*;

@Entity
@Table
public class StoreRules {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    String storeName;

    @Enumerated(value = EnumType.STRING)
    NotificationType notificationType;

    public StoreRules(){}
    public StoreRules(String storeName,NotificationType notificationType){
        this.storeName = storeName;
        this.notificationType = notificationType;
    }

    public int getId() {
        return id;
    }

    public String getStoreName() {
        return storeName;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }
}
