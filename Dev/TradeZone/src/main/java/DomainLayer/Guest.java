package DomainLayer;

public class Guest extends User{

    public Guest(String userName) {
        super(userName);
    }

    @Override
    public void loadUser() throws Exception {
        return;
    }

}
