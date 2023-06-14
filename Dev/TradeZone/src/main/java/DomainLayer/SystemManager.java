package DomainLayer;

import DataAccessLayer.DALService;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "SystemManager")
public class SystemManager{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToOne
    @JoinColumn(name = "manager_name")
    private Member member;

    @Transient
    private List<String> appointedSystemManagers;

    @Transient
    private boolean isLoaded;

    public SystemManager(Member member){
        this.member = member;
        appointedSystemManagers = new LinkedList<>();
    }


    public SystemManager(){}
    public String getUserName(){
        return member.getUserName();
    }

    public SystemManager AppointMemberAsSystemManager(Member otherMember) {
        SystemManager systemManager = new SystemManager(otherMember);
        appointedSystemManagers.add(otherMember.getUserName());
        return systemManager;
    }

    public void setIsloaded(boolean b) {
        isLoaded = b;
    }

    public void loadSystemManager(){
        if (!isLoaded) {
            SystemManager systemManager = DALService.systemManagerRepository.findByMember(member);
            this.appointedSystemManagers = systemManager.appointedSystemManagers;
            isLoaded = true;
        }

    }
}
