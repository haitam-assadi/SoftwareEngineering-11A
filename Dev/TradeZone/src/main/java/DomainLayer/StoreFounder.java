package DomainLayer;

import java.util.concurrent.ConcurrentHashMap;

public class StoreFounder extends AbstractStoreOwner{


    public StoreFounder(Member member) {
        super(member);
        this.myRole = RoleEnum.StoreFounder;
    }

    @Override
    public String getUserName() {
        return super.getUserName();
    }
}
