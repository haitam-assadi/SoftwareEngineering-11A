package AcceptanceTests;

import java.util.List;

public class ProxyBridge implements Bridge{
    public RealBridge realBridge;

    public ProxyBridge(){
        realBridge = null;
    }

    public ProxyBridge(RealBridge realBridge){
        this.realBridge = realBridge;
    }


    @Override
    public String enterMarket() {
        if(realBridge!=null){
            return realBridge.enterMarket();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean exitMarket(String userName) {
        if(realBridge!=null){
            return realBridge.exitMarket(userName);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean register(String guestUserName, String newMemberUserName, String password) {
        if(realBridge!=null){
            return realBridge.register(guestUserName, newMemberUserName, password);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String login(String guestUserName, String MemberUserName, String password) {
        if(realBridge!=null){
            return realBridge.login(guestUserName, MemberUserName, password);
        }
        throw new UnsupportedOperationException();
    }

    public List<String> getAllgusts() {
        if(realBridge!=null){
            return realBridge.getAllguests();
        }
        throw new UnsupportedOperationException();
    }

    public int getUserCart(String user) {
        if(realBridge!=null){
            return realBridge.getUserCart();
        }
        throw new UnsupportedOperationException();
    }

    public List<String> getAllOnlineMembers() {
        if(realBridge!=null){
            return realBridge.getAllOnlineMembers();
        }
        throw new UnsupportedOperationException();
    }

    public List<String> getAllMembers() {
        if(realBridge!=null){
            return realBridge.getAllMembers();
        }
        throw new UnsupportedOperationException();
    }

    public String getMemberPassword(String memberName) {
        if(realBridge!=null){
            return realBridge.getMemberPassword( memberName);
        }
        throw new UnsupportedOperationException();
    }
}
