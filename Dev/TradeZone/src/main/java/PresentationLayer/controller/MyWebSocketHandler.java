package PresentationLayer.controller;

import CommunicationLayer.Server;
import PresentationLayer.model.GeneralModel;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static PresentationLayer.controller.IndexController.guestName;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private static MyWebSocketHandler handler = null;
    private final Map<String, WebSocketSession> sessions;

    public static MyWebSocketHandler getInstance(){
        if(handler == null){
            handler = new MyWebSocketHandler();
        }
        return handler;
    }

    public MyWebSocketHandler(){
        this.sessions =  new ConcurrentHashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        sessions.put(guestName, session);
    }

//    public void enter(String userName) throws Exception {
//        sessions.put(GeneralController.getInstance().getName(), new WebSocketSession() {
//        }
//    }


    protected void handleTextMessage(WebSocketSession session, TextMessage message, String recipientSessionId) throws Exception {
        // Handle incoming messages from clients
        String sessionId = session.getId();
        String payload = message.getPayload();
        // Process the message and determine the recipient user(s)

        // Example: Sending a message to a specific user
        WebSocketSession recipientSession = sessions.get(recipientSessionId);
        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage("Hello, recipient!"));
        }
    }

    public void sendMessage(String userName, String message) throws IOException {
        WebSocketSession recipientSession = sessions.get(userName);
        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage(message));
        }
        else{
            System.out.println("*********************************ERROR FROM sendMessage*********************************");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
    }
}

