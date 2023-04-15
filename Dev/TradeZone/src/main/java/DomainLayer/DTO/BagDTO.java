package DomainLayer.DTO;

import DomainLayer.Cart;
import DomainLayer.Store;

import java.util.concurrent.ConcurrentHashMap;

public class BagDTO {
    private Cart cart;
    private Store storeBag;
    ConcurrentHashMap<String,Integer> productNameAmount;

    public BagDTO(Cart cart, Store storeBag, ConcurrentHashMap<String,Integer> productNameAmount){
        this.cart = cart;
        this.storeBag = storeBag;
        this.productNameAmount = productNameAmount;
    }
}
