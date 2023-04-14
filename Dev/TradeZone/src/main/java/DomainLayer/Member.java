package DomainLayer;

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
    private String password;

    public Member(String userName, String password) {
        super(userName);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }


}
