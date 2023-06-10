package PresentationLayer.model;

import DTO.StoreDTO;

import java.util.List;
import java.util.Map;

public class Member {
    private String memberName;
    private List<Deal> deals;
    private Map<String, List<StoreDTO>> memberStores;
    private boolean systemManager;
    public Member(String memberName, Map<String, List<StoreDTO>> memberStores, List<Deal> deals, boolean systemManager){
        this.memberName = memberName;
        this.deals = deals;
        this.memberStores = memberStores;
        this.systemManager = systemManager;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public void setDeals(List<Deal> deals) {
        this.deals = deals;
    }

    public Map<String, List<StoreDTO>> getMemberStores() {
        return memberStores;
    }

    public void setMemberStores(Map<String, List<StoreDTO>> memberStores) {
        this.memberStores = memberStores;
    }

    public boolean isSystemManager() {
        return systemManager;
    }

    public void setSystemManager(boolean systemManager) {
        this.systemManager = systemManager;
    }
}
