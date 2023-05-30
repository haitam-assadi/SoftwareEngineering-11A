package DataAccessLayer.DTO;


import DataAccessLayer.DALService;
import DomainLayer.Member;
import DomainLayer.Store;

import javax.persistence.*;
import javax.transaction.Transactional;
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

    @OneToOne
    @JoinColumn(name = "cart_id")
    private DTOCart dtoCart;
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
                     //DTOCart dtoCart,
                     boolean isSystemManager,
                     boolean isStoreFounder,
                     boolean isStoreOwner,
                     boolean isStoreManager) {
        this.userName = userName;
        this.password = password;
        this.isOnline = isOnline;
        //this.dtoCart = dtoCart;
        this.isSystemManager = isSystemManager;
        this.isStoreFounder = isStoreFounder;
        this.isStoreOwner = isStoreOwner;
        this.isStoreManager = isStoreManager;
    }

    protected DTOMember() {

    }


    @Transactional
    public Member loadMember() throws Exception {
        //TODO: database
        Member member = new Member(this.userName, this.password,isSystemManager,false);
        if (isStoreFounder){
            List<DTOStore> myStores = DALService.storeRepository.findByDtoMember(this);
            List<Store> stores = new ArrayList<>();
            for (DTOStore dtoStore1:myStores){
                stores.add(DALService.DTOStore2Store(dtoStore1.getStoreName()));
            }
            member.addFounderRole(stores);
        }
        return member;
    }

    public String getUserName() {
        return userName;
    }
}



