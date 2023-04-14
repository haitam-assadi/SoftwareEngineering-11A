package DomainLayer;

public class Role {


    RoleEnum myRole;
    Member member;

    public String getUserName(){
        return member.getUserName();
    }
}
