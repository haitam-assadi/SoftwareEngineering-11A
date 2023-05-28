package DataAccessLayer.DTO;

import javax.persistence.*;

@Entity
@Table(name = "Stock")
public class DTOStock {

    @Id
    private String stockName;

    public DTOStock(String stockName) {
        this.stockName = stockName;
    }

    protected DTOStock(){

    }
}
