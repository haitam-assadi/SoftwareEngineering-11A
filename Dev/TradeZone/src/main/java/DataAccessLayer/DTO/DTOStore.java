package DataAccessLayer.DTO;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Stores")
public class DTOStore {

    @Id
    private String storeName;
    private String stockName;

    public DTOStore(String storeName, String stockName) {
        this.storeName = storeName;
        this.stockName = stockName;
    }

    protected DTOStore(){

    }
}
