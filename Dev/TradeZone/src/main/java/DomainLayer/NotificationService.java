package DomainLayer;


import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

enum  NotificationType{
    storeClosed,
    storeOpenedAfterClose,
    productBought,
    subscriptionRemoved,
    requestNotification,
    RemovedFromOwningStore

}
public class NotificationService {

    private ConcurrentHashMap<String,ConcurrentHashMap<NotificationType, LinkedList<Member>>> membersNotificator; // storeName,<type,[members]>
    private static NotificationService instance;
    public NotificationService(){
        membersNotificator = new ConcurrentHashMap<>();
    }

    public static NotificationService getInstance(){
        if(instance == null){
            instance = new NotificationService();
        }
        return instance;
    }
    public void subscribe(String storeName,NotificationType notificationType,Member member){
        ConcurrentHashMap<NotificationType, LinkedList<Member>> type_memberList = new ConcurrentHashMap<>();
        if(membersNotificator.containsKey(storeName)){
            type_memberList = membersNotificator.get(storeName);
            if(type_memberList.containsKey(notificationType)){
                type_memberList.get(notificationType).add(member);
            }
        }else{
            LinkedList<Member> members = new LinkedList<>();
            members.add(member);
            type_memberList.put(notificationType,members);
            membersNotificator.put(storeName,type_memberList);
        }
    }

    public void notify(String storeName,String msg,NotificationType notificationType){
        ConcurrentHashMap<NotificationType, LinkedList<Member>> type_memberList = membersNotificator.get(storeName);
        type_memberList.get(notificationType).forEach(member -> member.send(msg));
    }

    public void notifySingle(String storeName,String user,String msg,NotificationType notificationType){
        ConcurrentHashMap<NotificationType, LinkedList<Member>> type_memberList = membersNotificator.get(storeName);
        LinkedList<Member> members = type_memberList.get(notificationType);
        for(Member member : members){
            if(member.getUserName().equals(user)){
                member.send(msg);
            }
        }
    }
    public void send(String userName,String msg){

    }

    public void removeMember(String storeName, Member member) {
        ConcurrentHashMap<NotificationType, LinkedList<Member>> type_memberList = membersNotificator.get(storeName);
        type_memberList.get(NotificationType.storeClosed).remove(member);
        type_memberList.get(NotificationType.productBought).remove(member);
        type_memberList.get(NotificationType.RemovedFromOwningStore).remove(member);
        type_memberList.get(NotificationType.storeOpenedAfterClose).remove(member);
    }
}
