package DomainLayer;

import DomainLayer.DTO.MemberDTO;

public class Role {


    RoleEnum myRole;
    Member member;

    public String getUserName(){
        return member.getUserName();
    }

    public MemberDTO getMemberDTO() {
        return this.member.getMemberDTO(this.myRole.name());
    }

    public void addNotification(String sender, String date, String description) {
        this.member.addNotification(sender, date, description);
    }
}
