package AcceptanceTests;

public interface Bridge {

    public String enterMarket();

    public boolean exitMarket(String userName);

    public boolean register(String guestUserName, String newMemberUserName, String password);
    public String login(String guestUserName, String MemberUserName, String password);



}
