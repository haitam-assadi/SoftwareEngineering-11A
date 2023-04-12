package DomainLayer;

public class Market {
    UserController userController;
    StoreController storeController;

    public Market(){
        this.userController = new UserController();
        this.storeController = new StoreController();
    }

    public String loginAsGuest(){
        return userController.loginAsGuest();
    }

    public void guestLogOut(String guestUserName) throws Exception {
        userController.guestLogOut(guestUserName);
    }

    public void register(String guestUserName, String newMemberUserName, String password) throws Exception {
        userController.register(guestUserName, newMemberUserName, password);
    }
    public String login(String guestUserName, String MemberUserName, String password) throws Exception {
        return userController.login(guestUserName, MemberUserName, password);
    }
}
