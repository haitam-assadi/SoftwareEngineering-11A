package PresentationLayer.model;

public class Worker {
    String name;
    String jobTitle;
    // Permissions

    public Worker(String name, String jobTitle /*, Map<id, string> Permissions*/){
        this.name = name;
        this.jobTitle = jobTitle;
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
}
