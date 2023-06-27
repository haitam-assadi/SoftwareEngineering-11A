package DomainLayer;

import javax.persistence.*;

@Entity
@Table
public class MemberNotificator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String memberName;
    @Enumerated(value = EnumType.STRING)
    private NotificationType notificationType;

    public MemberNotificator(){}
    public MemberNotificator(String memberName,NotificationType notificationType){
        this.memberName = memberName;
        this.notificationType = notificationType;
    }

    public int getId() {
        return id;
    }

    public String getMemberName() {
        return memberName;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }
}
