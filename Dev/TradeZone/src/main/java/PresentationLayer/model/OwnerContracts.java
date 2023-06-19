package PresentationLayer.model;

import DTO.OwnerContractDTO;

import java.util.List;

public class OwnerContracts {

    private String ownerName;
    private String storeName;
    private List<OwnerContractDTO> inProgressContracts;
    private List<OwnerContractDTO> doneContracts;
    private List<OwnerContractDTO> pendingContracts;

    public OwnerContracts(String ownerName, String storeName) {
        this.ownerName = ownerName;
        this.storeName = storeName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<OwnerContractDTO> getInProgressContracts() {
        return inProgressContracts;
    }

    public void setInProgressContracts(List<OwnerContractDTO> inProgressContracts) {
        this.inProgressContracts = inProgressContracts;
    }

    public List<OwnerContractDTO> getDoneContracts() {
        return doneContracts;
    }

    public void setDoneContracts(List<OwnerContractDTO> doneContracts) {
        this.doneContracts = doneContracts;
    }

    public List<OwnerContractDTO> getPendingContracts() {
        return pendingContracts;
    }

    public void setPendingContracts(List<OwnerContractDTO> pendingContracts) {
        this.pendingContracts = pendingContracts;
    }
}
