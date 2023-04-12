package DomainLayer;

public class Member extends User{

    private String password;

    public Member(String userName, String password) {
        super(userName);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }


}
