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

    public boolean removeMemberAsStoreOwner(Store store, AbstractStoreOwner myBoss) throws Exception {
        String storeName = store.getStoreName();
        if(!store.isAlreadyStoreOwner(getUserName()))
            throw new Exception("member "+getUserName()+" is not store owner for store " + storeName);

        if(!responsibleForStores.containsKey(storeName))
            throw new Exception("member "+getUserName()+" is not store owner for store " + storeName);

        if(!myBossesForStores.containsKey(storeName))
            throw new Exception(""+getUserName()+" is founder for this store");

        if(!myBossesForStores.get(storeName).getUserName().equals(myBoss.getUserName()))
            throw new Exception(myBoss.getUserName()+" is not my boss for store "+ storeName);

        store.removeOwner(getUserName());
        myBossesForStores.remove(storeName);
        responsibleForStores.remove(storeName);

        if(appointedOwners.containsKey(storeName)){
            for(StoreOwner storeOwner: appointedOwners.get(storeName))
                storeOwner.removeMemberAsStoreOwner(store,this);
            appointedOwners.remove(storeName);
        }

        if(appointedManagers.containsKey(storeName)){
            for(StoreManager storeManager: appointedManagers.get(storeName))
                storeManager.removeMemberAsStoreManager(store,this);
            appointedManagers.remove(storeName);
        }

        return true;
    }

    public void loadOwner() throws Exception {
        if (!isLoaded) {
            if (Market.dbFlag) {
                List<String> storesNames = DALService.storesOwnersRepository.getStoreNameByMemberName(getUserName());
                for (String storeName : storesNames) {
                    responsibleForStores.put(storeName, StoreMapper.getInstance().getStore(storeName));
                    Store store = responsibleForStores.get(storeName);
                    store.addStoreOwner(getUserName(), this);//todo: check
                    List<String> appointedOwnersNames = DALService.storesOwnersRepository.findAllByStoreNameAndBoss(storeName, getUserName());
                    List<StoreOwner> owners = new LinkedList<>();
                    for (String ownerName : appointedOwnersNames) {
                        StoreOwner storeOwner = MemberMapper.getInstance().getStoreOwner(ownerName);
                        owners.add(storeOwner);
                        store.addStoreOwner(ownerName, storeOwner);
                    }
                    appointedOwners.put(storeName, owners);
                    List<String> appointedManagersNames = DALService.storesManagersRepository.findAllByStoreNameAndBoss(storeName, getUserName());
                    List<StoreManager> managers = new LinkedList<>();
                    for (String managerName : appointedManagersNames) {
                        StoreManager storeManager = MemberMapper.getInstance().getStoreManager(managerName);
                        managers.add(storeManager);
                        store.addStoreManager(managerName, storeManager);
                    }
                    appointedManagers.put(storeName, managers);
                    Map<String, String> bossTypeAndName = DALService.storesOwnersRepository.findBossById(getUserName(), storeName);
                    String bossType = "";
                    String bossName = "";
                    for (String s : bossTypeAndName.keySet()) {
                        if (s.equals("my_boss_type")) {
                            bossType = bossTypeAndName.get(s);
                        }
                        if (s.equals("my_boss")) {
                            bossName = bossTypeAndName.get(s);
                        }
                    }
                    System.out.println(bossName);
                    System.out.println(bossType);
                    if (bossType.equals(RoleEnum.StoreFounder.toString())) {
                        StoreFounder myBoss = MemberMapper.getInstance().getStoreFounder(bossName);
                        myBossesForStores.put(storeName, myBoss);
                        if (!store.alreadyHaveFounder()) {
                            store.setStoreFounder(myBoss);
                        }
                    }
                    if (bossType.equals(RoleEnum.StoreOwner.toString())) {
                        StoreOwner myBoss = MemberMapper.getInstance().getStoreOwner(bossName);
                        myBossesForStores.put(bossName, myBoss);
                        store.addStoreOwner(myBoss.getUserName(), myBoss);
                    }
                    //todo: chcek if should add the owners and managers to the store
                    this.member.subscribeOwnerForNotifications(storeName);
                }
            }
            isLoaded = true;
        }
    }
}
