package DataAccessLayer.Controller;

import DataAccessLayer.CompositeKeys.RolesId;
import DataAccessLayer.DALService;
import DomainLayer.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class MemberMapper {

    private Map<String, Member> members;
    private Map<String, SystemManager> systemManagers;
    private Map<String, StoreFounder> founders;
    private Map<String, StoreOwner> owners;
    private Map<String, StoreManager> storesManagers;
    private Set<String> membersNamesConcurrentSet;

    private static MemberMapper instance = null;

    public static MemberMapper initMapper(){
        instance = new MemberMapper();
        return instance;
    }

    private MemberMapper(){
        members = new ConcurrentHashMap<>();
        systemManagers = new ConcurrentHashMap<>();
        founders = new ConcurrentHashMap<>();
        owners = new ConcurrentHashMap<>();
        storesManagers = new ConcurrentHashMap<>();
        membersNamesConcurrentSet = new HashSet<>();
    }

    public static MemberMapper getInstance(){
        if (instance == null){
            instance = new MemberMapper();
        }
        return instance;
    }

    public void loadAllMembersNames() {
        membersNamesConcurrentSet = DALService.memberRepository.getAllMemberNames();
    }
    public Member getMember(String memberName) throws Exception {
        assertStringIsNotNullOrBlank(memberName);
        memberName = memberName.toLowerCase().strip();
        if (!membersNamesConcurrentSet.contains(memberName)){
            //throw new Exception("member does not exist! " + memberName);
            return null;
        }
        if (!members.containsKey(memberName)){
            Member member = new Member(memberName);
            members.put(memberName,member);
        }
        return members.get(memberName);
    }
    private void assertStringIsNotNullOrBlank(String st) throws Exception {
        if(st==null || st.isBlank())
            throw new Exception("string is null or empty");
    }

    public SystemManager getSystemManager(String managerName) throws Exception{
        //todo: check if we need input validation
        assertStringIsNotNullOrBlank(managerName);
        managerName = managerName.toLowerCase().strip();
        if (!systemManagers.containsKey(managerName)){
            systemManagers.put(managerName,new SystemManager(getMember(managerName)));
            SystemManager systemManager = systemManagers.get(managerName);
            systemManager.setIsloaded(false);
        }
        return systemManagers.get(managerName);
    }

//    public boolean isFounderExists(String founderName,String storeName) throws Exception{
//        if (founders.containsKey(founderName)) return true;
//        else{
//            Optional<StoreFounder> storeFounderD = DALService.storeFounderRepository.findById(new RolesId(founderName,storeName));
//            if (storeFounderD.isPresent()) return false;
//            else{
//                //StoreFounder storeFounder =
//            }
//        }
//    }
    public StoreFounder getStoreFounder(String founderName) throws Exception{
        //todo: check if we need input validation
        assertStringIsNotNullOrBlank(founderName);
        founderName = founderName.toLowerCase().strip();
        if (!founders.containsKey(founderName)){
            founders.put(founderName,new StoreFounder(getMember(founderName)));
            StoreFounder storeFounder = founders.get(founderName);
            storeFounder.setLoaded(false);
        }
        return founders.get(founderName);
    }

    public StoreOwner getStoreOwner(String ownerName) throws Exception{
        //todo: check if we need input validation
        assertStringIsNotNullOrBlank(ownerName);
        ownerName = ownerName.toLowerCase().strip();
        if (!owners.containsKey(ownerName)){
            owners.put(ownerName,new StoreOwner(getMember(ownerName)));
            StoreOwner storeOwner = owners.get(ownerName);
            storeOwner.setLoaded(false);
        }
        return owners.get(ownerName);
    }
    public StoreManager getStoreManager(String managerName) throws Exception {
        assertStringIsNotNullOrBlank(managerName);
        managerName = managerName.toLowerCase().strip();
        if (!storesManagers.containsKey(managerName)){
            storesManagers.put(managerName,new StoreManager(getMember(managerName)));
            StoreManager storeManager = storesManagers.get(managerName);
            storeManager.setLoaded(false);
        }
        return storesManagers.get(managerName);
    }

    public void insertMember(Member member) {
        membersNamesConcurrentSet.add(member.getUserName());
        members.put(member.getUserName(),member);
    }
    public void insertFounder(StoreFounder storeFounderRole) {
        founders.put(storeFounderRole.getUserName(),storeFounderRole);
    }
    public void insertOwner(StoreOwner storeOwner) {
        owners.put(storeOwner.getUserName(),storeOwner);
    }
    public void insertStoreManager(StoreManager storeManager) {
        storesManagers.put(storeManager.getUserName(),storeManager);
    }




//    public SystemManager getSystemManager(String managerName,Member manager){
//        if (!systemManagers.containsKey(managerName)){
//            systemManagers.put(managerName,DALService.systemManagerRepository.findByMember(manager));
//        }
//        return systemManagers.get(managerName);
//    }
}
