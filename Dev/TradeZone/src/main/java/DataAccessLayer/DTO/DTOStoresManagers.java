package DataAccessLayer.DTO;

import DataAccessLayer.DTO.compositeKeys.StoresManagersId;

import javax.persistence.*;
@Entity(name = "StoresManagers")
@Table(name = "stores_manager")
public class DTOStoresManagers {
    @EmbeddedId
    private StoresManagersId id;

    @ManyToOne
    @MapsId("managerName")
    private DTOMember manager;

    @ManyToOne
    @MapsId("storeName")
    private DTOStore store;

    @OneToOne
    @JoinColumn(name = "BossName")
    private DTOMember myBoss;

    public DTOStoresManagers(StoresManagersId id, DTOMember manager, DTOStore store, DTOMember myBoss) {
        this.id = id;
        this.manager = manager;
        this.store = store;
        this.myBoss = myBoss;
    }

    public DTOStoresManagers() {
    }
}
