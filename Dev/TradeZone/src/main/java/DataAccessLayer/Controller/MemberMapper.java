package DataAccessLayer.Controller;

import DataAccessLayer.DALService;
import DomainLayer.Member;
import DomainLayer.StoreFounder;
import DomainLayer.SystemManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class MemberMapper {

    private Map<String, Member> members;
    private Map<String, SystemManager> systemManagers;
    private Map<String, StoreFounder> founders;
    private Set<String> membersNamesConcurrentSet;

    public static MemberMapper instance = null;

    private MemberMapper(){
        members = new ConcurrentHashMap<>();
        systemManagers = new ConcurrentHashMap<>();
        founders = new ConcurrentHashMap<>();
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
            throw new Exception("member does not exist! " + memberName);
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


}
