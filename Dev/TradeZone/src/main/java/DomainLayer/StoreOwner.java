package DomainLayer;

import java.util.concurrent.ConcurrentHashMap;

public class StoreOwner extends AbstractStoreOwner{


    public StoreOwner(Member member) {
        super(member);
        this.myRole=RoleEnum.StoreOwner;
        
    }
}
