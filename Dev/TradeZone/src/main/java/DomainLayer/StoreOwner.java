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
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "StoreOwner")
@PrimaryKeyJoinColumn(name = "member_name")
public class StoreOwner extends AbstractStoreOwner implements Serializable {


    public StoreOwner(Member member) {
        super(member);
        this.myRole=RoleEnum.StoreOwner;

    }
    public StoreOwner(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreOwner storeOwner = (StoreOwner) o;
        return getUserName().equals(storeOwner.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName());
    }

    @Override
    public String toString() {
        return "StoreOwner{" +
                "myRole=" + myRole.toString() +
                ", roleID=" + id.toString() +
                //", member=" + member.toString() +
                '}';
    }

    public void loadOwner() throws Exception {
        if (!isLoaded) {
            List<String> storesNames = DALService.storesOwnersRepository.getStoreNameByMemberName(getUserName());
            for (String storeName : storesNames) {
                responsibleForStores.put(storeName, StoreMapper.getInstance().getStore(storeName));
                Store store = responsibleForStores.get(storeName);
                List<String> appointedOwnersNames = DALService.storesOwnersRepository.findAllByStoreNameAndBoss(storeName,getUserName());
                List<StoreOwner> owners = new LinkedList<>();
                for (String ownerName: appointedOwnersNames){
                    owners.add(MemberMapper.getInstance().getStoreOwner(ownerName));
                }
                appointedOwners.put(storeName,owners);
                List<String> appointedManagersNames = DALService.storesManagersRepository.findAllByStoreNameAndBoss(storeName,getUserName());
                List<StoreManager> managers = new LinkedList<>();
                for (String managerName: appointedManagersNames){
                    managers.add(MemberMapper.getInstance().getStoreManager(managerName));
                }
                appointedManagers.put(storeName,managers);
                Map<String,String> bossTypeAndName = DALService.storesOwnersRepository.findBossById(getUserName(),storeName);
                String bossType = bossTypeAndName.keySet().stream().toList().get(0);
                String bossName = bossTypeAndName.get(bossType);
                if (bossType.equals(RoleEnum.StoreFounder.toString())){
                    StoreFounder myBoss = MemberMapper.getInstance().getStoreFounder(bossName);
                    myBossesForStores.put(storeName,myBoss);
                }
                if (bossType.equals(RoleEnum.StoreOwner.toString())){
                    StoreOwner myBoss = MemberMapper.getInstance().getStoreOwner(bossName);
                    myBossesForStores.put(bossName,myBoss);
                }
                //todo: chcek if should add the owners and managers to the store
            }
            isLoaded = true;
        }
    }
}
