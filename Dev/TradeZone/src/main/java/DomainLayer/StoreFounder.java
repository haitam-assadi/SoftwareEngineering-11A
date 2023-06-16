package DomainLayer;

import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.Controller.StoreMapper;
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

    public void loadFounder() throws Exception {
        if (!isLoaded) {
            List<String> storesNames = DALService.storeFounderRepository.getStoreNameByMemberName(getUserName());
            for (String storeName : storesNames) {
                responsibleForStores.put(storeName, StoreMapper.getInstance().getStore(storeName));
                Store store = responsibleForStores.get(storeName);
                store.setStoreFounder(this);
                List<String> appointedOwnersNames = DALService.storesOwnersRepository.findAllByStoreNameAndBoss(storeName,getUserName());
                List<StoreOwner> owners = new LinkedList<>();
                for (String ownerName: appointedOwnersNames){
                    StoreOwner storeOwner = MemberMapper.getInstance().getStoreOwner(ownerName);
                    owners.add(storeOwner);
                    store.addStoreOwner(ownerName,storeOwner);
                }
                appointedOwners.put(storeName,owners);
                List<String> appointedManagersNames = DALService.storesManagersRepository.findAllByStoreNameAndBoss(storeName,getUserName());
                List<StoreManager> managers = new LinkedList<>();
                for (String managerName: appointedManagersNames){
                    StoreManager storeManager = MemberMapper.getInstance().getStoreManager(managerName);
                    managers.add(storeManager);
                    store.addStoreManager(managerName,storeManager);
                }
                appointedManagers.put(storeName,managers);
                //todo: chcek if should add the owners and managers to the store
                NotificationService.getInstance().subscribe(store.getStoreName(),NotificationType.productBought,this.member);
                NotificationService.getInstance().subscribe(store.getStoreName(),NotificationType.storeClosedBySystemManager,this.member);
            }
            isLoaded = true;
        }
    }
}
