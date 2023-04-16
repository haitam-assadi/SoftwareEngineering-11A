package DTO;

import java.util.concurrent.ConcurrentHashMap;

public class BagDTO {
    public String storeBag;
    public ConcurrentHashMap<ProductDTO, Integer> bagContent;

    public BagDTO(String storeBag, ConcurrentHashMap<ProductDTO,Integer> bagContent){
        this.storeBag = storeBag;
        this.bagContent=bagContent;
    }
}
