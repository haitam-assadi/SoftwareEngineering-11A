package DataAccessLayer.DTO;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Stores")
public class DTOStore {

    @Id
    private String storeName;

    @OneToOne
    @JoinColumn(name = "stockName")
    private DTOStock dtoStock;

    private boolean isActive;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "founder_id", referencedColumnName = "user_name" )
    private DTOMember dtoMember;

    @OneToMany(
            mappedBy = "store",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DTOStoresOwners> owners_stores = new ArrayList<>();

    @OneToMany(
            mappedBy = "store",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DTOStoresManagers> managers_stores = new ArrayList<>();

    public DTOStore(String storeName, DTOStock dtoStock, boolean isActive, DTOMember dtoMember) {
        this.storeName = storeName;
        this.dtoStock = dtoStock;
        this.isActive = isActive;
        this.dtoMember = dtoMember;
    }
//    public DTOStore(String storeName, DTOStock dtoStock) {
//        this.storeName = storeName;
//        this.dtoStock = dtoStock;
//    }

    protected DTOStore(){

    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setDtoStock(DTOStock dtoStock) {
        this.dtoStock = dtoStock;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setDtoMember(DTOMember dtoMember) {
        this.dtoMember = dtoMember;
    }

    public void setOwners_stores(List<DTOStoresOwners> owners_stores) {
        this.owners_stores = owners_stores;
    }

    public void setManagers_stores(List<DTOStoresManagers> managers_stores) {
        this.managers_stores = managers_stores;
    }

    public String getStoreName() {
        return storeName;
    }

    public DTOStock getDtoStock() {
        return dtoStock;
    }

    public boolean isActive() {
        return isActive;
    }

    public DTOMember getDtoMember() {
        return dtoMember;
    }
}
