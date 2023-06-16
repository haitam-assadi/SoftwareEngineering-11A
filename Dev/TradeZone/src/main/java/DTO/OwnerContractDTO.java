package DTO;

import java.util.List;

public class OwnerContractDTO {
    public String triggerOwner;
    public String newOwner;
    public String storeName;
    public List<String> alreadyAcceptedOwners;
    public List<String> pendingOwners;
    public String contractStatus;
    public boolean isContractDone;

    public OwnerContractDTO(String triggerOwner, String newOwner, String storeName, String contractStatus, List<String> alreadyAcceptedOwners, List<String> pendingOwners, boolean isContractDone){
        this.triggerOwner = triggerOwner;
        this.newOwner=newOwner;
        this.storeName=storeName;
        this.contractStatus=contractStatus;
        this.alreadyAcceptedOwners = alreadyAcceptedOwners;
        this.pendingOwners= pendingOwners;
        this.isContractDone = isContractDone;
    }
}
