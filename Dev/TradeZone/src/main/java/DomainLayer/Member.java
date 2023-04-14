package DomainLayer;

import DomainLayer.DTO.MemberDTO;

import java.security.PrivateKey;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
enum RoleEnum {
    SystemManager,
    StoreOwner,
    StoreFounder,
    StoreManager
}
public class Member extends User{

    private ConcurrentHashMap<RoleEnum, Role> roles;

    private List<Notification> userNotifications;
    private String password;

    public Member(String userName, String password) {
        super(userName);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public MemberDTO getMemberDTO(String jobTitle) {
        return new MemberDTO(this.userName, jobTitle);
    }

    public void addNotification(String sender, String date, String description){
        this.userNotifications.add(new Notification(sender, date, description));
    }

    public boolean containsRole(String roleTitle) {
        for(RoleEnum role : this.roles.keySet()){
            if(role.toString().equals(roleTitle))
                return true;
        }
        return false;
    }
}
