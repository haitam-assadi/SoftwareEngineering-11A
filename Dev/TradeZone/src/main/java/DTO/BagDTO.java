package DTO;

import java.util.concurrent.ConcurrentHashMap;

public class BagDTO {
    public String storeBag;
    public ConcurrentHashMap<ProductDTO, Integer> bagContent;
    public ConcurrentHashMap<String, Double> productPriceMultipleAmount;
    public ConcurrentHashMap<String, Double> productFinalPriceWithDiscount;

    public BagDTO(String storeBag, ConcurrentHashMap<ProductDTO,Integer> bagContent, ConcurrentHashMap<String, Double> productPriceMultipleAmount, ConcurrentHashMap<String, Double> productFinalPriceWithDiscount){
        this.storeBag = storeBag;
        this.bagContent=bagContent;
        this.productPriceMultipleAmount=productPriceMultipleAmount;
        this.productFinalPriceWithDiscount=productFinalPriceWithDiscount;
    }
}
