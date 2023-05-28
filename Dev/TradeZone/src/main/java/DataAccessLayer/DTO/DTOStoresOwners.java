package DataAccessLayer.DTO;


import DataAccessLayer.DTO.compositeKeys.StoresOwnersId;

import javax.persistence.*;

@Entity(name = "StoresOwners")
@Table(name = "stores_owners")
public class DTOStoresOwners {

    @EmbeddedId
    private StoresOwnersId id;

    @ManyToOne
    @MapsId("ownerName")
    private DTOMember owner;

    @ManyToOne
    @MapsId("storeName")
    private DTOStore store;

    @OneToOne
    @JoinColumn(name = "BossName")
    private DTOMember myBoss;

    public DTOStoresOwners(StoresOwnersId id, DTOMember owner, DTOStore store, DTOMember myBoss) {
        this.id = id;
        this.owner = owner;
        this.store = store;
        this.myBoss = myBoss;
    }

    public DTOStoresOwners() {
    }

}
