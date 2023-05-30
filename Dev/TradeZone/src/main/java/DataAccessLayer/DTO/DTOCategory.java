package DataAccessLayer.DTO;

import DataAccessLayer.DTO.compositeKeys.CategoryId;
import DataAccessLayer.DTO.compositeKeys.StoresOwnersId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Enity
@Table(name = "Category")
@IdClass(CategoryId.class)
public class DTOCategory {


    @Id
    private String categoryName;

    @Id
    private String storeName;

    @OneToOne
    @JoinColumn(name = "stock_id")
    private DTOStock dtoStock;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "cat_id", referencedColumnName = "categoryName"),
            @JoinColumn(name = "store_id", referencedColumnName = "storeName")
    })
    private List<DTOProduct> products = new ArrayList<>();


//    public DTOCategory(String categoryName, DTOStore dtoStore, DTOStock dtoStock) {
//        this.categoryName = categoryName;
//        this.dtoStore = dtoStore;
//        this.dtoStock = dtoStock;
//    }

    public DTOCategory(String categoryName, String storeName, DTOStock dtoStock) {
        this.categoryName = categoryName;
       this.storeName = storeName;
        this.dtoStock = dtoStock;
    }

    public DTOCategory() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getStoreName() {
        return storeName;
    }
}
