package DTO;

import java.util.List;
import java.util.Map;

public class MemberDTO {
    public String username;
    public Map<String, List<StoreDTO>> memberStores;
    public List<DealDTO> memberDeals;


    public MemberDTO(String username, Map<String, List<StoreDTO>> memberStores, List<DealDTO> memberDeals){
        this.username = username;
        this.memberDeals = memberDeals;
        this.memberStores = memberStores;
    }
}
