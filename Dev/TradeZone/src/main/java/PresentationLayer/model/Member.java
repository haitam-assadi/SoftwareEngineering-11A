package PresentationLayer.model;

import java.util.List;

public class Member {
    private String memberName;
    private List<Deal> deals;
    public Member(String memberName, List<Deal> deals){
        this.memberName = memberName;
        this.deals = deals;
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
}
