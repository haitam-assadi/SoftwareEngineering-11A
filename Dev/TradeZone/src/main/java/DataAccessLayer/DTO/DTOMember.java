package DataAccessLayer.DTO;


import DomainLayer.Member;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    protected DTOMember() {

    }


    public Member loadMember(){
        //TODO: database
        return new Member(this.userName, this.password);
    }
}



