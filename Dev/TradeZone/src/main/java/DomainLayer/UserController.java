package DomainLayer;

import DomainLayer.DTO.ProductDTO;
import jdk.jshell.spi.ExecutionControl;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserController {
    private long guestsCounter;

    private ConcurrentHashMap<String, Member> onlineMembers;
    private ConcurrentHashMap<String, Guest> guests;
    private ConcurrentHashMap<String, Member> members;
    private Set<String> membersNamesConcurrentSet;

    public UserController(){
        onlineMembers = new ConcurrentHashMap<>();
        guests = new ConcurrentHashMap<>();
        members = new ConcurrentHashMap<>();
        membersNamesConcurrentSet = ConcurrentHashMap.newKeySet();
        guestsCounter=0;
    }

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

        onlineMembers.put(MemberUserName, member);
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

        if(!onlineMembers.keySet().contains(MemberUserName))
            //TODO synchronization check
            throw new Exception("can't login:"+ MemberUserName+" already logged in!");

    }

    private Member getMember(String UserName) throws Exception {
        if(!membersNamesConcurrentSet.contains(UserName))
            throw new Exception("can't getMember: userName "+ UserName+" does not exists!");
        if(!members.containsKey(UserName))
            // TODO: read from database AND add to members hashmap
            throw new ExecutionControl.NotImplementedException("");

        return members.get(UserName);
    }




}
