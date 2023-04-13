package AcceptanceTests;

import java.util.LinkedList;
import java.util.List;

public class RealBridge implements Bridge{
    @Override
    public String enterMarket() {
        return null;
    }

    @Override
    public boolean exitMarket(String userName) {
        return false;
    }

    @Override
    public boolean register(String guestUserName, String newMemberUserName, String password) {
        return false;
    }

    @Override
    public String login(String guestUserName, String MemberUserName, String password) {
        return null;
    }

    public List<String> getAllguests() {
        return new LinkedList<>();
    }

    public int getUserCart() {
        return -1;
    }

    public List<String> getAllOnlineMembers() {
        return new LinkedList<>();
    }

    public List<String> getAllMembers() {
        return new LinkedList<>();
    }

    public String getMemberPassword(String memberName) {
        return "";
    }
}
