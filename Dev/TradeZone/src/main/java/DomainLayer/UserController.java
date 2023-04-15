package DomainLayer;

import DomainLayer.DTO.DealDTO;
import DomainLayer.DTO.ProductDTO;
import jdk.jshell.spi.ExecutionControl;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserController {
    private long guestsCounter;

    private ConcurrentHashMap<String, Member> loggedInMembers;
    private ConcurrentHashMap<String, Guest> guests;
    private ConcurrentHashMap<String, Member> members;
    private Set<String> membersNamesConcurrentSet;

    public UserController(){
        loggedInMembers = new ConcurrentHashMap<>();
        guests = new ConcurrentHashMap<>();
        members = new ConcurrentHashMap<>();
        membersNamesConcurrentSet = ConcurrentHashMap.newKeySet();
        guestsCounter=0;
    }

    //TODO: CHECK WERE WE DO THE MEMBER logged in
    public synchronized String loginAsGuest(){
        String guestUserName = "Guest_"+ guestsCounter;
        guestsCounter++;
        guests.put(guestUserName, new Guest(guestUserName));
        return guestUserName;
    }

    public synchronized boolean guestLogOut(String guestUserName) throws Exception {
        if (guestUserName == null || guestUserName == "")
            throw new Exception("Can't log out: guestUserName is null or empty");

        if(!guests.containsKey(guestUserName))
            throw new Exception("Can't log out "+guestUserName+", because this user name does not Exist");

        // TODO: check if we want to delete all guest pointers( his cart products)
        guests.remove(guestUserName);
        return true;
    }

    public boolean exitMarket(String userName) throws Exception {
        throw new ExecutionControl.NotImplementedException("");
    }
    public boolean register(String guestUserName, String newMemberUserName, String password) throws Exception {
        registerValidateParameters(guestUserName, newMemberUserName, password);
        newMemberUserName = newMemberUserName.strip().toLowerCase();
        members.put(newMemberUserName, new Member(newMemberUserName, password));
        membersNamesConcurrentSet.add(newMemberUserName);
        //TODO: add user to database
        return true;
    }

    private void registerValidateParameters(String guestUserName, String newMemberUserName, String password) throws Exception {
        if(guestUserName==null || guestUserName == "")
            throw new Exception("can't register: guestUserName is null or empty");
        if(newMemberUserName==null || newMemberUserName == "")
            throw new Exception("can't register: newMemberUserName is null or empty");
        if(password==null || password == "")
            throw new Exception("can't register: password is null or empty");

        guestUserName = guestUserName.strip().toLowerCase();
        newMemberUserName = newMemberUserName.strip().toLowerCase();

        if(! guests.containsKey(guestUserName))
            throw new Exception("can't register: "+ guestUserName+" is not a guest!");
        if(membersNamesConcurrentSet.contains(newMemberUserName))
            throw new Exception("can't register: userName "+ newMemberUserName+" already exists!");
        if(!Character.isAlphabetic(newMemberUserName.charAt(0)))
            throw new Exception("can't register: "+ newMemberUserName+" is not valid userName!");
        if(password.length() < 8 || !password.matches(".*[0-9]+.*") || !password.toLowerCase().matches(".*[a-z]+.*"))
            throw new Exception("can't register: password must have at least 8 characters, at least one digit and one char");

    }

    public String login(String guestUserName, String MemberUserName, String password) throws Exception {
        loginValidateParameters(guestUserName, MemberUserName, password);

        MemberUserName = MemberUserName.strip().toLowerCase();
        Member member = getMember(MemberUserName);
        if(!member.getPassword().equals(password))
            throw new Exception("incorrect password!");

        loggedInMembers.put(MemberUserName, member);
        return MemberUserName;
    }

    private void loginValidateParameters(String guestUserName, String MemberUserName, String password) throws Exception {
        if(guestUserName==null || guestUserName == "")
            throw new Exception("can't login: guestUserName is null or empty");
        if(MemberUserName==null || MemberUserName == "")
            throw new Exception("can't login: newMemberUserName is null or empty");
        if(password==null || password == "")
            throw new Exception("can't login: password is null or empty");

        guestUserName = guestUserName.strip().toLowerCase();
        MemberUserName = MemberUserName.strip().toLowerCase();

        if(! guests.containsKey(guestUserName))
            throw new Exception("can't login: "+ guestUserName+" is not a guest!");

        if(!membersNamesConcurrentSet.contains(MemberUserName))
            throw new Exception("can't login: userName "+ MemberUserName+" does not exists!");

        if(!loggedInMembers.keySet().contains(MemberUserName))
            //TODO synchronization check
            throw new Exception("can't login:"+ MemberUserName+" already logged in!");

    }

    public Member getMember(String userName) throws Exception {
        assertIsMember(userName);

        userName = userName.strip().toLowerCase();
        if(!members.containsKey(userName))
            // TODO: read from database AND add to members hashmap
            throw new Exception("user needs to be read from database");

        return members.get(userName);
    }
    public User getUser(String userName) throws Exception {
        if(isGuest(userName))
            return guests.get(userName);

        if(isMember(userName))
            return getMember(userName);

        throw new Exception("can't getUser: userName "+ userName+" does not exists!");
    }

    public void assertIsGuestOrLoggedInMember(String userName) throws Exception {
        if(! (isGuest(userName) || isMember(userName)))
            throw new Exception(""+ userName+" is not a user!");

        if(!isMemberLoggedIn(userName))
            //TODO synchronization check
            throw new Exception("Member:"+ userName+" is not logged in!");
    }


    public boolean isGuest(String userName) throws Exception {
        if(userName==null || userName == "")
            throw new Exception("userName is null or empty");

        userName = userName.strip().toLowerCase();
        if(!guests.containsKey(userName) )
            return false;

        return true;
    }
    public void assertIsGuest(String userName) throws Exception {
        if(!isGuest(userName))
            throw new Exception("userName "+ userName+" does not exists!");
    }

    public boolean isMember(String memberUserName) throws Exception {
        if(memberUserName==null || memberUserName == "")
            throw new Exception("memberUserName is null or empty");

        memberUserName = memberUserName.strip().toLowerCase();
        if(!membersNamesConcurrentSet.contains(memberUserName))
            return false;

        return true;
    }

    public void assertIsMember(String memberUserName) throws Exception {
        if(!isMember(memberUserName))
            throw new Exception("userName "+ memberUserName+" does not exists!");
    }

    public boolean isMemberLoggedIn(String memberUserName) throws Exception {
        assertIsMember(memberUserName);
        memberUserName = memberUserName.strip().toLowerCase();

        if(!loggedInMembers.containsKey(memberUserName))
            return false;

        return true;
    }

    public void assertIsMemberLoggedIn(String memberUserName) throws Exception {
        if(!isMemberLoggedIn(memberUserName))
            throw new Exception(""+ memberUserName+" is not logged in");
    }

    public boolean appointOtherMemberAsStoreOwner(String memberUserName, Store store, String newOwnerUserName) throws Exception {
        assertIsMemberLoggedIn(memberUserName); // assert
        assertIsMember(newOwnerUserName); // assert
        Member member = getMember(memberUserName);
        Member otherMember = getMember(newOwnerUserName);
        return member.appointOtherMemberAsStoreOwner(store, otherMember);
    }

    public boolean appointOtherMemberAsStoreManager(String memberUserName, Store store, String newManagerUserName) throws Exception {
        assertIsMemberLoggedIn(memberUserName);
        assertIsMember(newManagerUserName);
        Member member = getMember(memberUserName);
        Member otherMember = getMember(newManagerUserName);
        return member.appointOtherMemberAsStoreManager(store, otherMember);
    }

    public void checkMemberRole(String systemManagerUserName, String otherMemberUserName) throws Exception {
        if(!this.members.containsKey(systemManagerUserName)){
            throw new Exception(systemManagerUserName + "has to be a member to get member's deals.");
        }
        if(!this.members.containsKey(otherMemberUserName)){
            throw new Exception(otherMemberUserName + "has to be a member to get his deals.");
        }
        if(!this.members.get(systemManagerUserName).containsRole("SystemManager")){
            throw new Exception(systemManagerUserName + "has to be a system manager to get member's deals.");
        }
    }

}
