package DomainLayer;

import DTO.OwnerContractDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class OwnerContract {

    private AbstractStoreOwner triggerOwner;
    private Member newOwner;
    private ConcurrentHashMap<String, Boolean> storeOwnersDecisions;
    private Store store;


    private String contractStatus;

    private boolean contractIsDone;

    public OwnerContract(AbstractStoreOwner triggerOwner, Member newOwner, Store store, ConcurrentHashMap<String, Boolean> storeOwnersDecisions){
        this.triggerOwner=triggerOwner;
        this.newOwner=newOwner;
        this.store = store;
        this.storeOwnersDecisions = storeOwnersDecisions;
        this.contractIsDone= false;
        contractStatus = "in progress";
    }


    public boolean fillOwnerContract(String memberUserName, Boolean decisions) throws Exception {
        if(decisions == null)
            throw new Exception("decision can't be null");
        assertStringIsNotNullOrBlank(memberUserName);
        memberUserName = memberUserName.strip().toLowerCase();

        if(contractIsDone)
            throw new Exception("you cant fill done contracts");

        if(!storeOwnersDecisions.containsKey(memberUserName))
            throw new Exception("owner "+ memberUserName +" can't fill this contract");

        if(storeOwnersDecisions.get(memberUserName) != null)
            throw new Exception("owner "+ memberUserName +" already filled this contract");

        storeOwnersDecisions.put(memberUserName, decisions);

        if(!decisions){
            contractIsDone = true;
            contractStatus = "contract is rejected by "+memberUserName;
            return true;
        }

        boolean allDecisionsIsTrue = true;
        for(boolean ownerDecision: storeOwnersDecisions.values()){
            if(!ownerDecision){
                allDecisionsIsTrue = false;
                break;
            }
        }
        if (allDecisionsIsTrue){
            triggerOwner.appointOtherMemberAsStoreOwner(store,newOwner);
            contractStatus = "all owners have accepted this contract and it is done";
            contractIsDone = true;
        }
        return true;
    }

    public boolean getContractIsDone(){
        return contractIsDone;
    }


    public void assertStringIsNotNullOrBlank(String st) throws Exception {
        if(st==null || st.isBlank())
            throw new Exception("string is null or empty");
    }

    public String getTriggerOwnerName(){
        return triggerOwner.getUserName();
    }


    public OwnerContractDTO getOwnerContractInfo(){
        List<String> alreadyAcceptedOwners= new ArrayList<>();
        List<String> pendingOwners= new ArrayList<>();
        for(String ownerName: storeOwnersDecisions.keySet()){
            if(storeOwnersDecisions.get(ownerName) == null){
                pendingOwners.add(ownerName);
            }else if (storeOwnersDecisions.get(ownerName) == true){
                alreadyAcceptedOwners.add(ownerName);
            }
        }
        return new OwnerContractDTO(triggerOwner.getUserName(), newOwner.getUserName(), store.getStoreName(),
                contractStatus, alreadyAcceptedOwners, pendingOwners, contractIsDone);
    }

    public boolean contractIsPendingOnMember(String ownerUserName) throws Exception {
        assertStringIsNotNullOrBlank(ownerUserName);
        ownerUserName = ownerUserName.strip().toLowerCase();
        if(storeOwnersDecisions.containsKey(ownerUserName) && storeOwnersDecisions.get(ownerUserName)==null)
            return true;

        return false;
    }

    public void involvedOwnerIsRemoved(String userName){
        contractStatus = "owner "+userName +" has been removed before accepting, contract is rejected.";
        contractIsDone = true;
    }


}
