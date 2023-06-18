package DomainLayer;

import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.DALService;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "SystemManager")
public class SystemManager{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToOne
    @JoinColumn(name = "manager_name")
    private Member member;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "appointedSystemManagers", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "appointedManager")
    private Set<String> appointedSystemManagers;


    public SystemManager(Member member){
        this.member = member;
        appointedSystemManagers = new HashSet<>();
    }


    public SystemManager(){}
    public String getUserName(){
        return member.getUserName();
    }
    public void setMember(Member member) {
        this.member = member;
    }

    public SystemManager AppointMemberAsSystemManager(Member otherMember) {
        SystemManager systemManager = MemberMapper.getInstance().getNewSystemManager(otherMember);
        appointedSystemManagers.add(otherMember.getUserName());
        return systemManager;
    }



}
