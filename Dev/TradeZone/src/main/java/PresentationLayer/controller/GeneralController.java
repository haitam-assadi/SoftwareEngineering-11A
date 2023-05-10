package PresentationLayer.controller;

public class GeneralController {
    private String name;
    private boolean hasRole;

    private boolean logged;

    private static GeneralController controller = null;
    public static GeneralController getInstance(){
        if(controller == null){
            controller = new GeneralController();
        }
        return controller;
    }

    private GeneralController(){
        this.name = "";
        this.hasRole = false;
        this.logged = false;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public boolean getHasRole(){
        return this.hasRole;
    }

    public void setHasRole(boolean hasRole){
        this.hasRole = hasRole;
    }

    public boolean getLogged(){
        return this.logged;
    }

    public void setLogged(boolean logged){
        this.logged = logged;
    }
}
