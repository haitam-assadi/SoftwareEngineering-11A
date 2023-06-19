package DomainLayer;


import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import CommunicationLayer.Server;
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
    public NotificationService(){
        storeRulesNotificator = new ConcurrentHashMap<>();
        memberNotificator = new ConcurrentHashMap<>();
    }

    public static NotificationService getInstance(){
        if(instance == null){
            instance = new NotificationService();
        }
        return instance;
    }
    public void subscribe(String storeName,NotificationType notificationType,Member member){
        ConcurrentHashMap<NotificationType, LinkedList<Member>> type_memberList = new ConcurrentHashMap<>();
        if(storeRulesNotificator.containsKey(storeName)){
            type_memberList = storeRulesNotificator.get(storeName);
            if(type_memberList.containsKey(notificationType)){
                type_memberList.get(notificationType).add(member);
                storeRulesNotificator.put(storeName,type_memberList);
            }
            else{
                LinkedList<Member> members = new LinkedList<>();
                members.add(member);
                type_memberList.put(notificationType,members);
                storeRulesNotificator.put(storeName,type_memberList);
            }
        }else{
            LinkedList<Member> members = new LinkedList<>();
            members.add(member);
            type_memberList.put(notificationType,members);
            storeRulesNotificator.put(storeName,type_memberList);
        }
    }

    public void subscribeMember(String newMemberUserName, NotificationType notificationType, Member member) {
        ConcurrentHashMap<NotificationType,Member> type_member = new ConcurrentHashMap<>();
        if(memberNotificator.containsKey(newMemberUserName)){
            type_member = memberNotificator.get(newMemberUserName);
            if(!type_member.containsKey(notificationType)){
                type_member.put(notificationType,member);
                memberNotificator.put(newMemberUserName,type_member);
            }
        }else{
            type_member.put(notificationType,member);
            memberNotificator.put(newMemberUserName,type_member);
        }
    }

    public void notify(String storeName,String msg,NotificationType notificationType) throws Exception {
        ConcurrentHashMap<NotificationType, LinkedList<Member>> type_memberList = storeRulesNotificator.get(storeName);
        if(type_memberList.containsKey(notificationType)) {
            LinkedList<Member> members = type_memberList.get(notificationType);
            for(Member member : members){
                member.send(msg);
            }
        }
    }

    public void notifyMember(String userName,String msg,NotificationType notificationType) throws Exception{
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

    }

    public void removeRule(String storeName, Member member) {
        ConcurrentHashMap<NotificationType, LinkedList<Member>> type_memberList = storeRulesNotificator.get(storeName);
        for (NotificationType notificationType : type_memberList.keySet()){
            type_memberList.get(notificationType).remove(member);
        }
//        type_memberList.get(NotificationType.storeClosed).remove(member);
//        type_memberList.get(NotificationType.productBought).remove(member);
//        type_memberList.get(NotificationType.RemovedFromOwningStore).remove(member);
//        type_memberList.get(NotificationType.storeOpenedAfterClose).remove(member);
    }

    public void removeAllRulers(String storeName){
        storeRulesNotificator.remove(storeName);
    }

    public void unsubscribeMember(String memberUserName){
        memberNotificator.remove(memberUserName);
    }


}
