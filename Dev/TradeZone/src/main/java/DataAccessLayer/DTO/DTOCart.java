package DataAccessLayer.DTO;

import javax.persistence.*;

@Entity
@Table(name = "Carts")
public class DTOCart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "userName")
    private DTOMember dtoMember;


    public DTOCart(DTOMember dtoMember) {
        this.dtoMember = dtoMember;
    }

    public DTOCart() {
    }
}
