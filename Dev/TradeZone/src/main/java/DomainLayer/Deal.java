package DomainLayer;

import DTO.DealDTO;
import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Entity
@Table
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String storeName;
    private String userName;

    private String date;

    @ElementCollection
    @CollectionTable(name = "deal_products_prices", joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "product_name")
    @Column(name = "price")
    private Map<String, Double> products_prices;

    @ElementCollection
    @CollectionTable(name = "deal_products_amount", joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "product_name")
    @Column(name = "amount")
    private Map<String, Integer> products_amount;

    @ElementCollection
    @CollectionTable(name = "dealProductPriceMultipleAmount",joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "product_name")
    @Column(name = "priceMulAmount")
    private Map<String, Double> productPriceMultipleAmount;
    @ElementCollection
    @CollectionTable(name = "dealProductFinalPriceWithDiscount", joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "product_name")
    @Column(name = "final_price")
    private Map<String, Double> productFinalPriceWithDiscount;
    private double totalPrice;

    public Deal(){}
    public Deal(String storeName, String userName, String date, Map<String, Double> products_prices,
                Map<String, Integer> products_amount, Map<String, Double> productPriceMultipleAmount, Map<String, Double> productFinalPriceWithDiscount, double totalPrice){
        this.storeName = storeName;
        this.userName = userName;
        this.date = date;
        this.products_prices = products_prices;
        this.products_amount = products_amount;
        this.totalPrice=totalPrice;
        this.productPriceMultipleAmount=productPriceMultipleAmount;
        this.productFinalPriceWithDiscount=productFinalPriceWithDiscount;
    }

    public DealDTO getDealDTO() {
        return new DealDTO(this.storeName, this.date, this.userName, this.products_amount, this.products_prices,
                this.productPriceMultipleAmount, this.productFinalPriceWithDiscount, this.totalPrice);
    }

    public String getDealUserName(){
        return this.userName;
    }

    public Long getId() {
        return id;
    }
}
