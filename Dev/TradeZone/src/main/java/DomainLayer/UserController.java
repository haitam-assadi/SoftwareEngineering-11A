package DomainLayer;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserController {

    private final String GUEST_PREFIX = "guest_";
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

    public synchronized String loginAsGuest(){
        String guestUserName = this.GUEST_PREFIX + guestsCounter;
        guestsCounter++;
        guestUserName = guestUserName.strip().toLowerCase();
        guests.put(guestUserName, new Guest(guestUserName));
        return guestUserName;
    }


    public boolean exitMarket(String userName) throws Exception {
        assertIsGuestOrLoggedInMember(userName);
        userName = userName.strip().toLowerCase();
        if(isGuest(userName))
            guests.remove(userName);
        else
            loggedInMemberExitMarket(userName);

        return true;
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
        assertIsGuest(guestUserName);
        assertIsNotMember(newMemberUserName);
        asserIsValidUserName(newMemberUserName);
        assertIsValidPassword(password);
    }

    public boolean isValidPassword(String password) throws Exception {
        assertStringIsNotNullOrBlank(password);

        if(password.length() < 8 || !password.matches(".*[0-9]+.*") || !password.toLowerCase().matches(".*[a-z]+.*"))
            return false;

        return true;
    }

    public void assertIsValidPassword(String password) throws Exception {
        if(!isValidPassword(password))
            throw new Exception("password must have at least 8 characters, at least one digit and one char");
    }

    public boolean isValidUserName(String userName) throws Exception {
        assertStringIsNotNullOrBlank(userName);

        userName = userName.strip().toLowerCase();

        if(!Character.isAlphabetic(userName.charAt(0)))
            return false;

        if(userName.length() < 4)
            return false;

        if(userName.contains(" "))
            return false;

        return true;

    }

    public void asserIsValidUserName(String userName) throws Exception {
        if(!isValidUserName(userName))
            throw new Exception(""+ userName+" is not valid userName!");
    }


    public String login(String guestUserName, String MemberUserName, String password) throws Exception {
        loginValidateParameters(guestUserName, MemberUserName, password);

        guestUserName = guestUserName.strip().toLowerCase();
        MemberUserName = MemberUserName.strip().toLowerCase();
        Member member = getMember(MemberUserName);
        if(!member.getPassword().equals(password))
            throw new Exception("incorrect password!");

        loggedInMembers.put(MemberUserName, member);
        guests.remove(guestUserName);

        return MemberUserName;
    }

    private void loginValidateParameters(String guestUserName, String MemberUserName, String password) throws Exception {
        assertStringIsNotNullOrBlank(password);
        assertIsGuest(guestUserName);
        assertIsMember(MemberUserName);
        assertIsMemberLoggedOut(MemberUserName);
    }

    public void assertStringIsNotNullOrBlank(String st) throws Exception {
        if(st==null || st.isBlank())
            throw new Exception("string is null or empty");
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
        if(isGuest(userName))
            return;
        assertIsMember(userName);
        assertIsMemberLoggedIn(userName);
    }
    public List<String> getAllGuests(){
        return guests.keySet().stream().toList();
    }

    public List<String> getAllMambers(){
        return membersNamesConcurrentSet.stream().toList();
    }
    public List<String> getAllLoggedInMembers(){
        return loggedInMembers.keySet().stream().toList();
    }


    public boolean isGuest(String userName) throws Exception {
        assertStringIsNotNullOrBlank(userName);

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
        assertStringIsNotNullOrBlank(memberUserName);

        memberUserName = memberUserName.strip().toLowerCase();
        if(!membersNamesConcurrentSet.contains(memberUserName))
            return false;

        return true;
    }

    public void assertIsMember(String memberUserName) throws Exception {
        if(!isMember(memberUserName))
            throw new Exception("userName "+ memberUserName+" does not exists!");
    }

    public void assertIsNotMember(String memberUserName) throws Exception {
        if(isMember(memberUserName))
            throw new Exception(""+ memberUserName+" is already a member");
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

    public void assertIsMemberLoggedOut(String memberUserName) throws Exception {
        if(isMemberLoggedIn(memberUserName))
            throw new Exception(""+ memberUserName+" is already logged in");
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

    public String memberLogOut(String memberUserName) throws Exception {
        assertIsMemberLoggedIn(memberUserName);
        loggedInMembers.remove(memberUserName);
        String newGuest = loginAsGuest();
        return newGuest;
    }

    public boolean loggedInMemberExitMarket(String memberUserName) throws Exception {
        assertIsMemberLoggedIn(memberUserName);
        loggedInMembers.remove(memberUserName);
        return true;
    }
}
