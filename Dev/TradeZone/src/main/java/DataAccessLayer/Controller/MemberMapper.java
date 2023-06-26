package DataAccessLayer.Controller;

import DataAccessLayer.CompositeKeys.RolesId;
import DataAccessLayer.DALService;
import DomainLayer.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class MemberMapper {

    private Map<String, Member> members;
    private Map<String, SystemManager> systemManagers;
    private Map<String, StoreFounder> founders;
    private Map<String, StoreOwner> owners;
    private Map<String, StoreManager> storesManagers;
    private Set<String> membersNamesConcurrentSet;

    private Set<String> onlineMembers;

    private static MemberMapper instance = null;

    public static MemberMapper initMapper(){
        instance = new MemberMapper();
        return instance;
    }

    private MemberMapper(){
        members = new ConcurrentHashMap<>();
        onlineMembers = new HashSet<>();
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
        if (Market.dbFlag)
            membersNamesConcurrentSet = DALService.memberRepository.getAllMemberNames();
    }

    public void loadAllSystemManagers() throws Exception {
        if (!Market.dbFlag) return;
        List<SystemManager> managers =  DALService.systemManagerRepository.findAll();
        //todo: check if this load the member also
        for (SystemManager systemManager: managers){
            Member member = getMember(systemManager.getUserName());
            members.put(systemManager.getUserName(),member);
            systemManager.setMember(member);
            systemManagers.put(systemManager.getUserName(),systemManager);
        }
    }

    public void loadAllOnlineMembersNames(){
        if (Market.dbFlag){
            onlineMembers = DALService.memberRepository.getAllOnlineMembersNames();
        }

    }
    public Member getMember(String memberName) throws Exception {
        assertStringIsNotNullOrBlank(memberName);
        memberName = memberName.toLowerCase().strip();
        if (!membersNamesConcurrentSet.contains(memberName)){
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

    public boolean checkIsSystemManager(String managerName) throws Exception{
        assertStringIsNotNullOrBlank(managerName);
        managerName = managerName.toLowerCase().strip();
        return systemManagers.containsKey(managerName);
    }

    public SystemManager getSystemManager(String managerName) throws Exception{
        if (checkIsSystemManager(managerName))
            return systemManagers.get(managerName);
        else return null;
    }

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

    public Member getNewMember(String memberName,String password){
        Member member = new Member(memberName, Security.Encode(password));
        membersNamesConcurrentSet.add(memberName);
        members.put(memberName,member);
        return member;
    }
    public StoreFounder getNewStoreFounder(Member member){
        StoreFounder storeFounder = new StoreFounder(member);
        founders.put(storeFounder.getUserName(),storeFounder);
        return storeFounder;
    }

    public StoreOwner getNewStoreOwner(Member member){
        StoreOwner storeOwner = new StoreOwner(member);
        owners.put(storeOwner.getUserName(),storeOwner);
        return storeOwner;
    }

    public StoreManager getNewStoreManager(Member member){
        StoreManager storeManager = new StoreManager(member);
        storesManagers.put(storeManager.getUserName(),storeManager);
        return storeManager;
    }

    public SystemManager getNewSystemManager(Member member){
        SystemManager systemManager = new SystemManager(member);
        systemManagers.put(member.getUserName(),systemManager);
        return systemManager;
    }

    public List<String> getMembersNames(){
        return membersNamesConcurrentSet.stream().toList();
    }

    public Set<String> getOnlineMembers() {
        return onlineMembers;
    }

    public void removeOnlineMember(String memberUserName) {
        onlineMembers.remove(memberUserName);
    }

    public void removeMember(String memberName) {
        membersNamesConcurrentSet.remove(memberName);
        members.remove(memberName);
    }

    public Map<String, SystemManager> getSystemManagers() {
        return systemManagers;
    }
}
