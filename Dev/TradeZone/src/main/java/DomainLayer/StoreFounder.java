package DomainLayer;

import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.DALService;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "StoreFounder")
@PrimaryKeyJoinColumn(name = "member_name")
public class StoreFounder extends AbstractStoreOwner implements Serializable {


    public StoreFounder(Member member) {
        super(member);
        this.myRole = RoleEnum.StoreFounder;
    }
    public StoreFounder(){}

    @Override
    public String getUserName() {
        return super.getUserName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreFounder storeFounder = (StoreFounder) o;
        return getUserName().equals(storeFounder.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName());
    }

    public void loadFounder(){
        if (!isLoaded) {
            List<String> storesNames = DALService.storeFounderRepository.getStoreNameByMemberName(getUserName());
            for (String storeName : storesNames) {
                //responsibleForStores.put(store,getStore(store));
                List<String> appointedOwnersNames = DALService.storesOwnersRepository.findAllByStoreNameAndBoss(storeName,getUserName());
                List<StoreOwner> owners = new LinkedList<>();
                for (String ownerName: appointedOwnersNames){
                    //owners.add(MemberMapper.getInstance().getOwner(ownerName));
                }
                //appointedOwners.put(ownerName,owners);
                //todo: the same for store manager
            }
            isLoaded = true;
        }

    }
}
