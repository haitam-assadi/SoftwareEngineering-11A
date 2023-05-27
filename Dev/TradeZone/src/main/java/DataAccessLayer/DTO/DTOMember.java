package DataAccessLayer.DTO;


import javax.persistence.Entity;

@Entity
@Table(name = "Member")
public class DTOMember {
    @Id
    private String userName;
    private String password;

    private boolean isOnline;
    private Integer cartId;
    private boolean isSystemManager;

    public DTOMember(String userName, String password, boolean isOnline, Integer cartId, boolean isSystemManager) {
        this.userName = userName;
        this.password = password;
        this.isOnline = isOnline;
        this.cartId = cartId;
        this.isSystemManager = isSystemManager;
    }

    public DTOMember() {
    }
}



