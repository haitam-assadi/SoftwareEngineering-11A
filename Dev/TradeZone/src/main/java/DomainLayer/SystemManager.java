package DomainLayer;

import java.util.LinkedList;
import java.util.List;

public class SystemManager{

    private Member member;

    private List<String> appointedSystemManagers;

    public SystemManager(Member member){
        this.member = member;
        appointedSystemManagers = new LinkedList<>();
    }

    public String getUserName(){
        return member.getUserName();
    }

    public SystemManager AppointMemberAsSystemManager(Member otherMember) {
        SystemManager systemManager = new SystemManager(otherMember);
        appointedSystemManagers.add(otherMember.getUserName());
        return systemManager;
    }
}
