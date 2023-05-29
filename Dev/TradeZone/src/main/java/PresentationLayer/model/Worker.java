package PresentationLayer.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Worker {
    String name;
    String jobTitle;
    // Permissions
    Map<Integer, Map<String, Boolean>> permissions; // <permission ID, <"permission", have/not have>>

    public Worker(String name, String jobTitle /*, Map<id, string> Permissions*/){
        this.name = name;
        this.jobTitle = jobTitle;
        this.permissions = new LinkedHashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Map<Integer, Map<String, Boolean>> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<Integer, Map<String, Boolean>> permissions) {
        this.permissions = permissions;
    }

    public void buildAndSetPermissions(String[] allPermissions, List<Integer> IDs) {
        Map<Integer, Map<String, Boolean>> permissions = new LinkedHashMap<>();
        int id = 1;
        for(String s : allPermissions){
            Map<String, Boolean> innerMap = new LinkedHashMap<>();
            if(IDs.contains(id)){
                innerMap.put(s, true);
            }
            else{
                innerMap.put(s, false);
            }
            permissions.put(id, innerMap);
            id += 1;
        }
        this.permissions = permissions;
    }
}
