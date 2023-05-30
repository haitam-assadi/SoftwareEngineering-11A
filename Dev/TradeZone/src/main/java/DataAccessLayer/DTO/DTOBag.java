package DataAccessLayer.DTO;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Bags")
public class DTOBag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bagId;


    @OneToOne
    @JoinColumn(name = "cart_id")
    private DTOCart cart;


    @OneToOne
    @JoinColumn(name = "store_name")
    private DTOStore store;

    @OneToMany(mappedBy = "bag")
    private List<DTOProductBag> productBags;
//    @ManyToMany(mappedBy = "bags")
//    private List<DTOProduct> products;

    public DTOBag(DTOCart cart, DTOStore store) {
        this.cart = cart;
        this.store = store;
    }

    public DTOBag() {
    }

    public int getBagId() {
        return bagId;
    }

    public DTOCart getCart() {
        return cart;
    }

    public DTOStore getStore() {
        return store;
    }
}
