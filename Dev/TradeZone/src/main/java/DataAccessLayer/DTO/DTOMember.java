package DataAccessLayer.DTO;


import DomainLayer.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Member")
public class DTOMember {
    @Id
    @Column(name = "user_name")
    private String userName;
    private String password;

    private boolean isOnline;
    private Integer cartId;
    private boolean isSystemManager;

    private boolean isStoreFounder;
    private boolean isStoreOwner;
    private boolean isStoreManager;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DTOStoresOwners> owners_stores = new ArrayList<>();

    @OneToMany(
            mappedBy = "manager",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DTOStoresManagers> managers_stores = new ArrayList<>();

    @OneToOne(mappedBy = "dtoMember",cascade = CascadeType.ALL)
    private DTOStore dtoStore;




    public DTOMember(String userName,
                     String password,
                     boolean isOnline,
                     Integer cartId,
                     boolean isSystemManager,
                     boolean isStoreFounder,
                     boolean isStoreOwner,
                     boolean isStoreManager) {
        this.userName = userName;
        this.password = password;
        this.isOnline = isOnline;
        this.cartId = cartId;
        this.isSystemManager = isSystemManager;
        this.isStoreFounder = isStoreFounder;
        this.isStoreOwner = isStoreOwner;
        this.isStoreManager = isStoreManager;
    }

    protected DTOMember() {

    }


    public Member loadMember(){
        //TODO: database
        return new Member(this.userName, this.password);
    }
}



