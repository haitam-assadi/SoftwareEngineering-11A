package DomainLayer;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import CommunicationLayer.Server;
import DataAccessLayer.Controller.MemberMapper;
import DataAccessLayer.DALService;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.io.IOException;

enum  NotificationType{
    storeClosed,
    storeClosedBySystemManager,
    storeOpenedAfterClose,
    productBought,
    subscriptionRemoved,
    requestNotification,
    RemovedFromOwningStore,
    fillAppointContract,
    ownerDone,
    decisionForContract

}

public class NotificationService {

    private ConcurrentHashMap<String,ConcurrentHashMap<NotificationType, LinkedList<Member>>> storeRulesNotificator; // storeName,<type,[members]>
    private ConcurrentHashMap<String,ConcurrentHashMap<NotificationType, Member>> memberNotificator; // userName,<type,member>

    private static NotificationService instance;

    private boolean isStoreRulesNotificatorLoaded;
    private boolean isMemberNotificatorLoaded;
    public NotificationService(){
        storeRulesNotificator = new ConcurrentHashMap<>();
        memberNotificator = new ConcurrentHashMap<>();
        isMemberNotificatorLoaded = false;
        isStoreRulesNotificatorLoaded = false;
    }

    public static NotificationService getInstance(){
        if(instance == null){
            instance = new NotificationService();
        }
        return instance;
    }
    public void subscribe(String storeName,NotificationType notificationType,Member member) throws Exception {
        loadNotification();
        ConcurrentHashMap<NotificationType, LinkedList<Member>> type_memberList = new ConcurrentHashMap<>();
        if(storeRulesNotificator.containsKey(storeName)){
            type_memberList = storeRulesNotificator.get(storeName);
            if(type_memberList.containsKey(notificationType)){
                type_memberList.get(notificationType).add(member);
                storeRulesNotificator.put(storeName,type_memberList);
                if (Market.dbFlag) {
                    StoreRules storeRule = DALService.storeRulesRepository.findByStoreNameAndNotificationType(notificationType.toString(),storeName);
                    DALService.rolerNotificatorRepository.save(new RolerNotificator(storeRule, member.getUserName()));

                }
            }
            else{
                LinkedList<Member> members = new LinkedList<>();
                members.add(member);
                type_memberList.put(notificationType,members);
                storeRulesNotificator.put(storeName,type_memberList);
                if (Market.dbFlag) {
                    StoreRules storeRules = new StoreRules(storeName, notificationType);
                    DALService.storeRulesRepository.save(storeRules);
                    DALService.rolerNotificatorRepository.save(new RolerNotificator(storeRules,member.getUserName()));
                }
            }
        }else{
            LinkedList<Member> members = new LinkedList<>();
            members.add(member);
            type_memberList.put(notificationType,members);
            storeRulesNotificator.put(storeName,type_memberList);
            if (Market.dbFlag) {
                StoreRules storeRules = new StoreRules(storeName, notificationType);
                DALService.storeRulesRepository.save(storeRules);
                DALService.rolerNotificatorRepository.save(new RolerNotificator(storeRules,member.getUserName()));
            }
        }
    }

    public void subscribeMember(String newMemberUserName, NotificationType notificationType, Member member) throws Exception {
        loadNotification();
        ConcurrentHashMap<NotificationType,Member> type_member = new ConcurrentHashMap<>();
        if(memberNotificator.containsKey(newMemberUserName)){
            type_member = memberNotificator.get(newMemberUserName);
            if(!type_member.containsKey(notificationType)){
                type_member.put(notificationType,member);
                memberNotificator.put(newMemberUserName,type_member);
                if (Market.dbFlag)
                    DALService.memberNotificatorRepository.save(new MemberNotificator(newMemberUserName,notificationType));
            }
        }else{
            type_member.put(notificationType,member);
            memberNotificator.put(newMemberUserName,type_member);
            if (Market.dbFlag)
                DALService.memberNotificatorRepository.save(new MemberNotificator(newMemberUserName,notificationType));
        }
    }

    public void notify(String storeName,String msg,NotificationType notificationType) throws Exception {
        loadNotification();
        ConcurrentHashMap<NotificationType, LinkedList<Member>> type_memberList = storeRulesNotificator.get(storeName);
        if(type_memberList.containsKey(notificationType)) {
            LinkedList<Member> members = type_memberList.get(notificationType);
            for(Member member : members){
                member.send(msg);
            }
        }
    }

    public void notifyMember(String userName,String msg,NotificationType notificationType) throws Exception{
        loadNotification();
        ConcurrentHashMap<NotificationType, Member> type_member = memberNotificator.get(userName);
        if(type_member.containsKey(notificationType)) {
            type_member.get(notificationType).send(msg);
        }
    }

    public void notifySingle(String storeName,String user,String msg,NotificationType notificationType) throws Exception{
        ConcurrentHashMap<NotificationType, LinkedList<Member>> type_memberList = storeRulesNotificator.get(storeName);
        if(type_memberList.containsKey(notificationType)) {
            LinkedList<Member> members = type_memberList.get(notificationType);
            if (members != null && !members.isEmpty()) {
                for (Member member : members) {
                    if (member.getUserName().equals(user)) {
                        member.send(msg);
                    }
                }
            }
        }
    }
    public void send(String userName,String msg) throws Exception{
        Server.getInstance().sendMessage(userName,msg);
    }

    public void removeRule(String storeName, Member member) throws Exception {
        loadStoreRulesNotificator();
        ConcurrentHashMap<NotificationType, LinkedList<Member>> type_memberList = storeRulesNotificator.get(storeName);
        for (NotificationType notificationType : type_memberList.keySet()){
            type_memberList.get(notificationType).remove(member);
        }
//        type_memberList.get(NotificationType.storeClosed).remove(member);
//        type_memberList.get(NotificationType.productBought).remove(member);
//        type_memberList.get(NotificationType.RemovedFromOwningStore).remove(member);
//        type_memberList.get(NotificationType.storeOpenedAfterClose).remove(member);
    }

    public void removeAllRulers(String storeName) throws Exception {
        loadNotification();
        storeRulesNotificator.remove(storeName);

    }

    public void unsubscribeMember(String memberUserName) throws Exception {
        loadNotification();
        memberNotificator.remove(memberUserName);
        if (Market.dbFlag) {
            List<Integer> ids = DALService.memberNotificatorRepository.findIdsByMemberName(memberUserName);
            for (int id : ids)
                DALService.memberNotificatorRepository.deleteById(id);
        }
    }

    public void unsubscribeMemberInStore(String memberName,String storeName) throws Exception{
        loadNotification();
        ConcurrentHashMap<NotificationType, LinkedList<Member>> type_memberList = storeRulesNotificator.get(storeName);
        for (NotificationType notificationType: type_memberList.keySet()){
            for (Member member: type_memberList.get(notificationType)){
                if(member.getUserName().equals(memberName)){
                    storeRulesNotificator.get(storeName).get(notificationType).remove(member);
                }
            }
        }
        if (Market.dbFlag) {
            List<Integer> storeRulesIds = DALService.storeRulesRepository.findIdsByStoreName(storeName);
            for (Integer id : storeRulesIds) {
                List<Integer> roleNotificatorId = DALService.rolerNotificatorRepository.findIdsByStoreId(id);
                for (Integer roleId : roleNotificatorId) {
                    DALService.rolerNotificatorRepository.deleteById(roleId);
                }
            }
        }
    }

    public void loadNotification() throws Exception {
        if (!Market.dbFlag) return;
        loadMemberNotificator();
        loadStoreRulesNotificator();
    }
    public void loadMemberNotificator() throws Exception {
        if (Market.dbFlag && !isMemberNotificatorLoaded){
            List<MemberNotificator> memberNotificators = DALService.memberNotificatorRepository.findAll();
            for (MemberNotificator memberNotificator1: memberNotificators){
                String memberName = memberNotificator1.getMemberName();
                Member member = MemberMapper.getInstance().getMember(memberName);
                if (!this.memberNotificator.containsKey(memberName)){
                    ConcurrentHashMap<NotificationType,Member> ntm = new ConcurrentHashMap<>();
                    ntm.put(memberNotificator1.getNotificationType(),member);
                }
                this.memberNotificator.get(memberName).putIfAbsent(memberNotificator1.getNotificationType(),member);
            }
        }
        isMemberNotificatorLoaded = true;
    }

    public void loadStoreRulesNotificator() throws Exception{
        if (!Market.dbFlag || isStoreRulesNotificatorLoaded) return;
        List<StoreRules> storeRules = DALService.storeRulesRepository.findAll();
        List<RolerNotificator> rolerNotificators = DALService.rolerNotificatorRepository.findAll();
        for (StoreRules storeRule: storeRules){
            if (!storeRulesNotificator.containsKey(storeRule.getStoreName())){
                storeRulesNotificator.put(storeRule.getStoreName(),new ConcurrentHashMap<>());
            }
            ConcurrentHashMap<NotificationType, LinkedList<Member>> type_member_list =storeRulesNotificator.get(storeRule.getStoreName());
            LinkedList<Member> members = new LinkedList<>();
            for (RolerNotificator rolerNotificator: rolerNotificators){
                if (rolerNotificator.getStoreRules().id == storeRule.id){
                    members.add(MemberMapper.getInstance().getMember(rolerNotificator.getMemberName()));
                }
            }
            type_member_list.put(storeRule.getNotificationType(),members);
            storeRulesNotificator.put(storeRule.getStoreName(),type_member_list);
        }
        isStoreRulesNotificatorLoaded = true;
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<NotificationType, LinkedList<Member>>> getStoreRulesNotificator() {
        return storeRulesNotificator;
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<NotificationType, Member>> getMemberNotificator() {
        return memberNotificator;
    }


}
