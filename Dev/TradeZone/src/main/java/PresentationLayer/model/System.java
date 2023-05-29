package PresentationLayer.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class System {
    private List<String> allGuests;
    private List<String> allMembers;
    private List<String> allOnlineMembers;
    private List<String> allStores;
    private Set<String> allSystemManagers;

    public System(){
        allGuests = new LinkedList<>();
        allMembers = new LinkedList<>();
        allOnlineMembers = new LinkedList<>();
        allStores = new LinkedList<>();
        allSystemManagers = new HashSet<>();
    }

    public List<String> getAllGuests() {
        return allGuests;
    }

    public void setAllGuests(List<String> allGuests) {
        this.allGuests = allGuests;
    }

    public List<String> getAllMembers() {
        return allMembers;
    }

    public void setAllMembers(List<String> allMembers) {
        this.allMembers = allMembers;
    }

    public List<String> getAllOnlineMembers() {
        return allOnlineMembers;
    }

    public void setAllOnlineMembers(List<String> allOnlineMembers) {
        this.allOnlineMembers = allOnlineMembers;
    }

    public List<String> getAllStores() {
        return allStores;
    }

    public void setAllStores(List<String> allStores) {
        this.allStores = allStores;
    }

    public Set<String> getAllSystemManagers() {
        return allSystemManagers;
    }

    public void setAllSystemManagers(Set<String> allSystemManagers) {
        this.allSystemManagers = allSystemManagers;
    }
}
