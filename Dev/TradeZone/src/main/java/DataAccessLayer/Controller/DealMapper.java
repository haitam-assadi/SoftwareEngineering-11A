package DataAccessLayer.Controller;

import DataAccessLayer.DALService;
import DomainLayer.Deal;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DealMapper {
    Map<Long, Deal> deals; // saved deals

    private static DealMapper instance = null;

    public static DealMapper initMapper(){
        instance = new DealMapper();
        return instance;
    }

    private DealMapper(){
        deals = new ConcurrentHashMap<>();
    }

    public static DealMapper getInstance(){
        if (instance == null){
            instance = new DealMapper();
        }
        return instance;
    }

    public Deal getDeal(long dealId){ //get deal after load or for load from db
        if (!deals.containsKey(dealId)){
            Optional<Deal> dealD = DALService.dealRepository.findById(dealId);
            if (dealD.isPresent()){
                deals.put(dealD.get().getId(),dealD.get());
            }else{
                return null;
            }
        }
        return deals.get(dealId);
    }

    public void insertDeal(Deal deal){
        deals.put(deal.getId(), deal);
    }

}
