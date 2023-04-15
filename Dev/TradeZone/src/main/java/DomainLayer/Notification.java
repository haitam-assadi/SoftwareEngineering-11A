package DomainLayer;

import java.util.Date;

public class Notification {
    String sender;
    String date;
    String description;

    public Notification(String sender, String date, String description){
        this.sender = sender;
        this.date = date;
        this.description = description;
    }
}
