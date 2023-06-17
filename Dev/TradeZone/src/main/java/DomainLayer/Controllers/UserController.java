package DomainLayer.Controllers;

import DTO.DealDTO;
import DTO.MemberDTO;
import DTO.StoreDTO;
import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.DALService;
import DomainLayer.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserController {

    private final String GUEST_PREFIX = "guest_";
    private long guestsCounter;

    private ConcurrentHashMap<String, Member> loggedInMembers;
    private ConcurrentHashMap<String, Guest> guests;
    private ConcurrentHashMap<String, Member> members;
    private Set<String> membersNamesConcurrentSet;

    private ConcurrentHashMap<String, SystemManager> systemManagers;


    public UserController(){
        loggedInMembers = new ConcurrentHashMap<>();
        guests = new ConcurrentHashMap<>();
        members = new ConcurrentHashMap<>();
        membersNamesConcurrentSet = ConcurrentHashMap.newKeySet();
        guestsCounter=0;
        systemManagers = new ConcurrentHashMap<>();
    }

    public String firstManagerInitializer() throws Exception {
        String user = "systemmanager1";
        if(!isMember(user)) {
            Member member = MemberMapper.getInstance().getNewMember(user,"systemmanager1Pass");
            membersNamesConcurrentSet.add(user);
            members.put(user, member);
            if (Market.dbFlag){
                DALService.saveMember(member, member.getCart());
            }
            SystemManager systemManager = new SystemManager(member);
            member.setSystemManager(systemManager);
            systemManagers.put(user, systemManager);
            if (Market.dbFlag) {
               // DALService.saveMember(member, member.getCart());
                DALService.systemManagerRepository.save(systemManager);
            }
        }
        return user;
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
        if(isGuest(userName)) {
            guests.remove(userName);
            if (Market.dbFlag)
                DALService.cartRepository.deleteGuest();
        }
        else
            loggedInMemberExitMarket(userName);

        return true;
    }
    public boolean register(String guestUserName, String newMemberUserName, String password) throws Exception {
        registerValidateParameters(guestUserName, newMemberUserName, password);
        newMemberUserName = newMemberUserName.strip().toLowerCase();
        Member member = MemberMapper.getInstance().getNewMember(newMemberUserName,password);
        members.put(newMemberUserName,member);
        member.defineNotifications(newMemberUserName);
        membersNamesConcurrentSet.add(newMemberUserName);
        if (Market.dbFlag) {
            DALService.saveMember(member, member.getCart());
        }
        member.getCart().setMemberCart(member);
        if (Market.dbFlag)
            DALService.cartRepository.save(member.getCart());
        member.addCart(guests.get(guestUserName).getCart());
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
        member.loadMember();
        SystemManager systemManager = member.checkIsSystemManager();
        if (systemManager !=null){
            systemManagers.put(MemberUserName,systemManager);
        }
        if(!member.getPassword().equals(Security.Encode(password)))
            throw new Exception("incorrect password!");
        loggedInMembers.put(MemberUserName, member);
        guests.remove(guestUserName);
        member.Login();
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
        if(!members.containsKey(userName)) {
            // TODO: read from database AND add to members hashmap
//            Optional<Member> memberD = DALService.memberRepository.findById(userName);
//            if (memberD.isPresent()){
//                Member member = memberD.get();
//                if (member.checkIsSystemManager()){
//                    SystemManager systemManager = DALService.systemManagerRepository.findByMember(member);
//                    member.setSystemManager(systemManager);
//                    systemManagers.put(userName,systemManager);
//                }else{
//                    member.declareRoles();
//                }
//                members.put(userName,member);
//            }else{
                throw new Exception(userName + " does not exist!");
            //}
        }
        return members.get(userName);
    }

    private SystemManager getSystemManager(String managerName) throws Exception {
        managerName = managerName.toLowerCase();
        assertIsSystemManager(managerName);
        if(!systemManagers.containsKey(managerName))
            // TODO: read from database AND add to members hashmap
            throw new Exception("user needs to be read from database");

        return systemManagers.get(managerName);
    }

    public User getUser(String userName) throws Exception {
        userName = userName.strip().toLowerCase();

        if(isGuest(userName))
            return guests.get(userName);

        if(isMember(userName))
            return getMember(userName);

        throw new Exception("can't getUser: userName "+ userName+" does not exists!");
    }

    public List<DealDTO> getUserDeals(String userName) throws Exception {
        assertStringIsNotNullOrBlank(userName);
        userName=userName.strip().toLowerCase();
        if(!isGuest(userName))
            assertIsMember(userName);
        User user = getUser(userName);
        return user.getUserDeals();
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
        if(!membersNamesConcurrentSet.contains(memberUserName)){
            Member member = MemberMapper.getInstance().getMember(memberUserName);
            if (member == null)
                return false;
            membersNamesConcurrentSet.add(memberUserName);
            members.put(memberUserName,member);//check this
            return true;
        }

        return true;
    }

    public boolean assertIsSystemManager(String managerName) throws Exception {
        if(!isSystemManager(managerName)){
            throw new Exception("userName "+ managerName +" is not a system manager!");
        }else{
            return true;
        }
    }

    public void assertIsNotSystemManager(String memberUserName) throws Exception {
        if(isSystemManager(memberUserName)){
            throw new Exception("userName "+ memberUserName +" is a system manager!");
        }
    }

    private void assertNotSystemManager(String otherMemberName) throws Exception {
        if(isSystemManager(otherMemberName)){
            throw new Exception("userName "+ otherMemberName +" is already a system manager!");
        }
    }

    public boolean isSystemManager(String managerName) throws Exception {
        assertStringIsNotNullOrBlank(managerName);
        managerName = managerName.strip().toLowerCase();
        MemberMapper.getInstance().getSystemManager(managerName);
        return systemManagers.containsKey(managerName);
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



    @Transactional
    public Boolean AppointMemberAsSystemManager(String managerName, String otherMemberName) throws Exception {
        assertIsSystemManager(managerName);
        assertIsMemberLoggedIn(managerName);
        assertIsMember(otherMemberName);
        assertNotSystemManager(otherMemberName);
        SystemManager manager = getSystemManager(managerName);
        Member otherMember = getMember(otherMemberName);
        otherMember.assertHaveNoRule();
        SystemManager newManager = manager.AppointMemberAsSystemManager(otherMember);
        otherMember.setSystemManager(newManager);
        systemManagers.put(otherMemberName,newManager);
        if (Market.dbFlag) {
            DALService.memberRepository.save(otherMember);
            DALService.systemManagerRepository.save(newManager);
        }
        return true;
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



    public Map<String, List<StoreDTO>> myStores(String memberUserName) throws Exception {
        assertIsMemberLoggedIn(memberUserName);
        return getMember(memberUserName).myStores();
    }

    public String memberLogOut(String memberUserName) throws Exception {
        assertIsMemberLoggedIn(memberUserName);
        Member member = loggedInMembers.get(memberUserName);
        member.Logout();
        loggedInMembers.remove(memberUserName);
        String newGuest = loginAsGuest();
        return newGuest;
    }

    public boolean loggedInMemberExitMarket(String memberUserName) throws Exception {
        assertIsMemberLoggedIn(memberUserName);
        Member member = getMember(memberUserName);
        member.Logout();
        loggedInMembers.remove(memberUserName);
        return true;
    }

    public void validateStorePolicy(String userName) throws Exception {
        assertIsGuestOrLoggedInMember(userName);
        User user = getUser(userName);
        userName = userName.strip().toLowerCase();
        user.getCart().validateStorePolicy(userName);
    }

    public void validateAllProductsAmounts(String userName) throws Exception {
        assertIsGuestOrLoggedInMember(userName);
        User user = getUser(userName);
        user.getCart().validateAllProductsAmounts();
    }

    public void validateAllStoresIsActive(String userName) throws Exception {
        assertIsGuestOrLoggedInMember(userName);
        User user = getUser(userName);
        user.getCart().validateAllStoresIsActive();
    }

    public Double getCartPriceBeforeDiscount(String userName) throws Exception {
        User user = getUser(userName);
        return user.getCart().getCartPriceBeforeDiscount();
    }

    public Double getCartPriceAfterDiscount(String userName) throws Exception {
        User user = getUser(userName);
        return user.getCart().getCartPriceAfterDiscount();
    }

    public boolean updateStockAmount(String userName) throws Exception {
        assertIsGuestOrLoggedInMember(userName);
        User user = getUser(userName);
        return user.getCart().updateStockAmount();
    }

    public boolean removeOwnerByHisAppointer(String memberUserName, Store store, String ownerUserName) throws Exception {
        assertIsMemberLoggedIn(memberUserName); // assert
        assertIsMember(ownerUserName); // assert
        Member member = getMember(memberUserName);
        Member otherMember = getMember(ownerUserName);
        return member.removeOwnerByHisAppointer(store, otherMember);
    }

    public void assertIsOwner(String userName,Store store) throws Exception {
        assertIsMember(userName);
        assertIsMemberLoggedIn(userName);
        Member member = getMember(userName);
        member.assertIsOwnerForTheStore(store);
    }

    public boolean assertIsOwnerOrSystemManager(String userName,Store store) throws Exception{
        if(isSystemManager(userName)){
            return true;
        }
        else{
            assertIsOwner(userName,store);
        }
        return false;
    }

    public boolean IsOwnerOrSystemManager(String userName,Store store){
        try {
            assertIsOwnerOrSystemManager(userName,store);
            return true;
        }catch (Exception e){
            return false;
        }
    }


        private String nowTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public Set<String> getAllSystemManagers(String managerName) throws Exception {
        assertIsSystemManager(managerName);
        assertIsMemberLoggedIn(managerName);
        return systemManagers.keySet();
    }

    public boolean removeMemberBySystemManager(String managerName, String memberName) throws Exception {
        //todo: remove from data base
        memberName = memberName.toLowerCase();
        SystemManager systemManager = getSystemManager(managerName);
        assertIsMemberLoggedIn(managerName);
        assertIsNotSystemManager(memberName);
        assertIsMember(memberName);
        Member member = getMember(memberName);
        member.assertHaveNoRule();
        member.removeCart();
        members.remove(memberName);
        membersNamesConcurrentSet.remove(memberName);
        if(loggedInMembers.containsKey(memberName)){
            loggedInMembers.remove(memberName);
        }
        NotificationService.getInstance().unsubscribeMember(memberName);
        return true;
    }

    public MemberDTO getMemberInfo(String callerMemberName, String returnedMemberName) throws Exception {
        if(!isSystemManager(callerMemberName))
            throw new Exception("the caller member is not a system manager");
        Member member = members.get(returnedMemberName);
        return member.getMemberDTO();
    }


//    public boolean systemManagerCloseStore(String managerName, String storeName) throws Exception {
//        assertIsMemberLoggedIn(managerName);
//        SystemManager manager = getSystemManager(managerName);
//        return manager.closeStore(storeName);
//    }
//
//    private SystemManager getSystemManager(String managerName) throws Exception {
//        assertIsSystemManager(managerName);
//        return systemManagers.get(managerName);
//    }
}
