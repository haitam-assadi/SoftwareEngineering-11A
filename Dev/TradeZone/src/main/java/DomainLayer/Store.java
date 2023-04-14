package DomainLayer;

import DomainLayer.DTO.DealDTO;
import DomainLayer.DTO.MemberDTO;
import DomainLayer.DTO.ProductDTO;
import DomainLayer.DTO.StoreDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private String storeName;
    private Stock stock;
    private boolean isActive;
    private StoreFounder storeFounder;
    private ConcurrentHashMap<String, StoreOwner> storeOwners;
    private ConcurrentHashMap<String, StoreManager> storeManagers;

    //TODO:: maybe need to make it Concurrent
    private List<Deal> storeDeals;
    private List<DiscountPolicy> storeDiscountPolicies;
    private List<PaymentPolicy> storePaymentPolicies;

    public Store() {
        storeOwners = new ConcurrentHashMap<>();
        stock = new Stock();
    }

    public boolean addNewProductToStock(String memberUserName,String nameProduct,String category, Integer price, String details, Integer amount) throws Exception {
        if(amount < 0)
            throw new Exception("the amount of the product cannot be negative");
        if(price < 0)
            throw new Exception("the price of the product cannot be negative");
        if(details == null || details == "")
            throw new Exception("the details of the product cannot be null");
        if(details.length()>300)
            throw new Exception("the details of the product is too long");
        if(!storeFounder.getUserName().equals(memberUserName) || !storeOwners.containsKey(memberUserName))
            throw new Exception("can't add new product to stock : userName "+ memberUserName +" is not an owner to the store");
        return stock.addNewProductToStock(nameProduct,category,price,details,amount);
    }

    public boolean removeProductFromStock(String memberUserName, String productName) throws Exception {
        if(!storeFounder.getUserName().equals(memberUserName) || !storeOwners.containsKey(memberUserName))
            throw new Exception("can't add new product to stock : userName "+ memberUserName +" is not an owner to the store");
        return stock.removeProductFromStock(productName);
    }

    public boolean updateProductDetails(String memberUserName, String productName, String newProductDetails) throws Exception {
        if(newProductDetails == null || newProductDetails == "")
            throw new Exception("the details of the product cannot be null");
        if(newProductDetails.length()>300)
            throw new Exception("the details of the product is too long");
        if(!storeFounder.getUserName().equals(memberUserName) || !storeOwners.containsKey(memberUserName))
            throw new Exception("can't add new product to stock : userName "+ memberUserName +" is not an owner to the store");
        return stock.updateProductDetails(productName,newProductDetails);
    }
//    public StoreDTO getStoreInfo(){
//        return new StoreDTO();
//    }

    public boolean closeStore(String memberUserName) throws Exception {
        if(memberUserName.equals(this.storeFounder.getUserName())){
            this.isActive = false;
            String sender = this.storeFounder.getUserName();
            LocalDate local = java.time.LocalDate.now();
            String date = local.toString();
            String description = "The store has been closed.";
            for(StoreOwner owner : this.storeOwners.values()){
                owner.addNotification(sender, date, description);
            }
            for(StoreManager manager : this.storeManagers.values()){
                manager.addNotification(sender, date, description);
            }
            return true;
        }
        throw new Exception(memberUserName + "is not the founder of the store");
    }

    public List<MemberDTO> getStoreWorkersInfo(String memberUserName) throws Exception {
        if(this.storeOwners.containsKey(memberUserName)){
            List<MemberDTO> members = new ArrayList<MemberDTO>();
            members.add(this.storeFounder.getMemberDTO());
            for(StoreOwner owner : this.storeOwners.values()){
                members.add(owner.getMemberDTO());
            }
            for(StoreManager manager : this.storeManagers.values()){
                members.add(manager.getMemberDTO());
            }
            return members;
        }
        throw new Exception(memberUserName + "is not an owner of the store");
    }

    public List<DealDTO> getStoreDeals(String memberUserName) throws Exception {
        if(this.storeOwners.containsKey(memberUserName)){
            List<DealDTO> deals = new ArrayList<DealDTO>();
            for(Deal deal : this.storeDeals){
                deals.add(deal.getDealDTO());
            }
            return deals;
        }
        throw new Exception(memberUserName + "has to be an owner to get store deals.");
    }

    public String getStoreName(){
        return this.storeName;
    }

    public List<DealDTO> getMemberDeals(String otherMemberUserName, List<DealDTO> deals) {
        for(Deal deal : this.storeDeals){
            if(deal.getDealUserName().equals(otherMemberUserName)){
                deals.add(deal.getDealDTO());
            }
        }
        return deals;
    }
}
