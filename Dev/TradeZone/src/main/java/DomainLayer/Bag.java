package DomainLayer;

import DTO.BagDTO;
import DTO.DealDTO;
import DTO.ProductDTO;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Bag {

    private Store storeBag;
    private ConcurrentHashMap<String, ConcurrentHashMap<Product,Integer>> bagContent;

    public Bag(Store storeBag) {
        this.storeBag = storeBag;
        bagContent = new ConcurrentHashMap<>();
    }

    public boolean addProduct(String productName, Integer amount) throws Exception {
        productName = productName.strip().toLowerCase();
        if(bagContent.containsKey(productName))
            throw new Exception("bag already contains "+productName+" product");

        Product product =storeBag.getProductWithAmount(productName,amount);

        ConcurrentHashMap<Product,Integer> innerHashMap = new ConcurrentHashMap<>();
        innerHashMap.put(product,amount);
        bagContent.put(productName,innerHashMap);
        return true;
    }

    public boolean changeProductAmount(String productName, Integer newAmount) throws Exception {
        productName = productName.strip().toLowerCase();
        if(! bagContent.containsKey(productName))
            throw new Exception("bag does not contain "+productName+" product");
        Product product =storeBag.getProductWithAmount(productName,newAmount);
        bagContent.get(productName).put(product,newAmount);
        return true;
    }

    public boolean removeProduct(String productName) throws Exception {
        productName = productName.strip().toLowerCase();
        if(! bagContent.containsKey(productName))
            throw new Exception("bag does not contain "+productName+" product");
        bagContent.remove(productName);
        return bagContent.isEmpty()? false:true;
    }

    public Store getStoreBag() {
        return storeBag;
    }
    public ConcurrentHashMap<String,Integer> getProductWithAmount(){
        List<String> productsNameBag =  bagContent.keySet().stream().toList();
        ConcurrentHashMap<String,Integer> productNameAmount = new ConcurrentHashMap<>();
        for (int i=0;i<productsNameBag.size();i++){
            String productName = productsNameBag.get(i);
            ConcurrentHashMap<Product,Integer> productIntegerConcurrentHashMap = bagContent.get(productName);
            Integer productAmount = productIntegerConcurrentHashMap.get(productIntegerConcurrentHashMap.keys().nextElement());
            productNameAmount.put(productName,productAmount);
        }
        return productNameAmount;
    }
    public BagDTO getBagInfo() throws Exception {
        ConcurrentHashMap<ProductDTO, Integer> bagContent = new ConcurrentHashMap<>();
        for (ConcurrentHashMap<Product,Integer> curr: this.bagContent.values()) {
            Product currProduct = curr.keys().nextElement();
            bagContent.put(currProduct.getProductInfo(storeBag.getProductDiscountPolicies(currProduct.getName())), curr.values().stream().toList().get(0));
        }

        ConcurrentHashMap<String, Double> priceWithAmountWithoutDiscount = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Double> priceWithAmountWithDiscount = new ConcurrentHashMap<>();

        for(ProductDTO productDTO: bagContent.keySet()){
            String productName = productDTO.name;
            Double discountValue = storeBag.getDiscountForProductInBag(this.bagContent, productName);
            priceWithAmountWithoutDiscount.put(productName, productDTO.price*bagContent.get(productDTO));
            priceWithAmountWithDiscount.put(productName, priceWithAmountWithoutDiscount.get(productName)-discountValue);
        }

        return new BagDTO(storeBag.getStoreName(), bagContent, priceWithAmountWithoutDiscount, priceWithAmountWithDiscount);
    }

    public void validateStorePolicy(String userName) throws Exception {
        storeBag.validateStorePolicy(bagContent);
    }

    public void validateAllProductsAmounts() throws Exception {
        for(ConcurrentHashMap<Product,Integer> prodct_amount : bagContent.values()){
            for (Product p : prodct_amount.keySet()){
                storeBag.getProductWithAmount(p.getName(),prodct_amount.get(p));
            }
        }
    }


    public void validateStoreIsActive() throws Exception {
        if(!storeBag.isActive()){
            List<String> productsNamesList = bagContent.keySet().stream().toList();
            throw new Exception("store "+storeBag.getStoreName()+" is not active, you can't purchase these products "
                    +productsNamesList.toString()+ " .");
        }
    }

    public Double getBagPriceBeforeDiscount() throws Exception {
        Double totalBagPrice = 0.0;
        for (ConcurrentHashMap<Product,Integer> curr: this.bagContent.values()) {
            Product product = curr.keys().nextElement();
            totalBagPrice += product.getProductPrice(curr.get(product));
        }
        return totalBagPrice;
    }

    public Double getBagPriceAfterDiscount() throws Exception {
        Double totalBagPrice = 0.0;
        for (ConcurrentHashMap<Product,Integer> curr: this.bagContent.values()) {
            Product product = curr.keys().nextElement();
            totalBagPrice += product.getProductPrice(curr.get(product));
        }
        totalBagPrice -= storeBag.getDiscountForBag(bagContent);

        return totalBagPrice;
    }


    public boolean removeBagAmountFromStock() throws Exception {
        return storeBag.removeBagAmountFromStock(bagContent);
    }

    public boolean replaceBagAmountToStock() throws Exception {
        return storeBag.replaceBagAmountToStock(bagContent);
    }

    public void removeAllProducts() {
        bagContent = new ConcurrentHashMap<>();
    }
    public Deal createDeal(User user) throws Exception {
        ConcurrentHashMap<String, Double> products_prices = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Integer> products_amount = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Double> productPriceMultipleAmount = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Double> productFinalPriceWithDiscount = new ConcurrentHashMap<>();

        for (String productName: bagContent.keySet()){
            Product product = bagContent.get(productName).keys().nextElement();
            products_prices.put(productName, product.getPrice());
            products_amount.put(productName, bagContent.get(productName).get(product));
        }

        for(String productName: products_prices.keySet()){
            Double discountValue = storeBag.getDiscountForProductInBag(this.bagContent, productName);
            productPriceMultipleAmount.put(productName, products_prices.get(productName)*products_amount.get(productName));
            productFinalPriceWithDiscount.put(productName, productPriceMultipleAmount.get(productName)-discountValue);
        }
        Double totalPrice = getBagPriceAfterDiscount();

        Deal deal = new Deal(storeBag,user, LocalDate.now().toString(), products_prices, products_amount, productPriceMultipleAmount, productFinalPriceWithDiscount,totalPrice);
        storeBag.addDeal(deal);
        return deal;
    }
}
