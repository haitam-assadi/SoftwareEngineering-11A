package DomainLayer;

import DTO.OwnerContractDTO;
import DataAccessLayer.Controller.MemberMapper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table
public class OwnerContract {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int contractId;

    @Transient
    private AbstractStoreOwner triggerOwner;

    private String triggerOwnerName;
    @Enumerated(value = EnumType.STRING)
    private RoleEnum triggerOwnerType;

    @OneToOne
    @JoinColumn(name = "newOwnerName")
    private Member newOwner;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "storeOwnersDecisions")
    @Column(name = "otherOwner")
    private Map<String, Boolean> storeOwnersDecisions;
    private String declinedOwner;

    @OneToOne
    @JoinColumn(name = "storeName")
    private Store store;


    private String contractStatus;

    private boolean contractIsDone;

    @Transient
    private boolean isLoaded;

    public OwnerContract(AbstractStoreOwner triggerOwner, Member newOwner, Store store, ConcurrentHashMap<String, Boolean> storeOwnersDecisions){
        this.triggerOwner=triggerOwner;
        this.triggerOwnerName = triggerOwner.getUserName();
        this.triggerOwnerType = triggerOwner.myRole;
        this.newOwner=newOwner;
        this.store = store;
        this.storeOwnersDecisions = storeOwnersDecisions;
        this.contractIsDone= false;
        contractStatus = "in progress";
        declinedOwner = "";
        isLoaded = true;
    }

    public OwnerContract(){}



    public boolean fillOwnerContract(String memberUserName, Boolean decisions) throws Exception {
        if(decisions == null)
            throw new Exception("decision can't be null");
        assertStringIsNotNullOrBlank(memberUserName);
        memberUserName = memberUserName.strip().toLowerCase();
        loadContract();
        if(contractIsDone)
            throw new Exception("you cant fill done contracts");

        if(!storeOwnersDecisions.containsKey(memberUserName))
            throw new Exception("owner "+ memberUserName +" can't fill this contract");

        if(storeOwnersDecisions.get(memberUserName))
            throw new Exception("owner "+ memberUserName +" already filled this contract");

        storeOwnersDecisions.put(memberUserName, decisions);

        if(!decisions){
            declinedOwner = memberUserName;
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

            String msg = "you now owner for store " + store.getStoreName();
            NotificationService.getInstance().notifyMember(newOwner.userName,msg,NotificationType.ownerDone);

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

    public String getTriggerOwnerName() {
        return triggerOwnerName;
    }


    public OwnerContractDTO getOwnerContractInfo(){
        List<String> alreadyAcceptedOwners= new ArrayList<>();
        List<String> pendingOwners= new ArrayList<>();
        for(String ownerName: storeOwnersDecisions.keySet()){
            if(storeOwnersDecisions.get(ownerName) == false){
                pendingOwners.add(ownerName);
            }else if (storeOwnersDecisions.get(ownerName) == true){
                alreadyAcceptedOwners.add(ownerName);
            }
        }

        if (!declinedOwner.equals(""))
            pendingOwners.remove(declinedOwner);

        return new OwnerContractDTO(triggerOwnerName, newOwner.getUserName(), store.getStoreName(),
                contractStatus, alreadyAcceptedOwners, pendingOwners, contractIsDone);
    }

    public boolean contractIsPendingOnMember(String ownerUserName) throws Exception {
        assertStringIsNotNullOrBlank(ownerUserName);
        ownerUserName = ownerUserName.strip().toLowerCase();
        if(!contractIsDone &&storeOwnersDecisions.containsKey(ownerUserName) && storeOwnersDecisions.get(ownerUserName)==false)
            return true;

        return false;
    }

    public void involvedOwnerIsRemoved(String userName) throws Exception {
        assertStringIsNotNullOrBlank(userName);
        userName=userName.strip().toLowerCase();
        declinedOwner = userName;
        contractStatus = "owner "+userName +" has been removed before accepting, contract is rejected.";
        contractIsDone = true;
    }

    public void loadContract() throws Exception {
        if (isLoaded || !Market.dbFlag) return;
        if (triggerOwnerType.equals(RoleEnum.StoreFounder)){
            triggerOwner = MemberMapper.getInstance().getStoreFounder(triggerOwnerName);
        }
        if (triggerOwnerType.equals(RoleEnum.StoreOwner)){
            triggerOwner = MemberMapper.getInstance().getStoreOwner(triggerOwnerName);
        }
        isLoaded = true;
    }

    public Member getNewOwner() {
        return newOwner;
    }
}
