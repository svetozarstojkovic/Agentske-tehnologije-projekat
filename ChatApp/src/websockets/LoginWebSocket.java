package websockets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.codehaus.jackson.map.ObjectMapper;

import data.Data;
import model.User;
import requests.Requests;
import util.Constants;
import util.Converter;

@MessageDriven(activationConfig = { 
        @ActivationConfigProperty(propertyName = "destinationType", 
                propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName="destination",
                propertyValue="jms/topic/informTopic")                
        })

@ServerEndpoint("/login") 
public class LoginWebSocket implements MessageListener{
	
	private static Map<String, Session> sessions = new HashMap<>();
	
	/**
     * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was 
     * successful.
     */
    @OnOpen
    public void onOpen(Session session){
        System.out.println(session.getId() + " has opened a connection"); 
//        try {
//            session.getBasicRemote().sendText("Connection Established");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }
 
    /**
     * When a user sends a message to the server, this method will intercept the message
     * and allow us to react to it. For now the message is read as a String.
     */
    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println("Message from " + session.getId() + ": " + message);
        
        try {
        	
        	System.out.println("before convert");
			User user = new ObjectMapper().readValue(message, User.class);

			String jsonUser = Converter.getJSONString(user);
			System.out.println("after convert: "+jsonUser);
			
			sessions.put(user.getUsername(), session);
			
			Requests.makePostRequest("http://localhost:8080/UserApp/rest/users/login", jsonUser);
			//System.out.println("Output is: " +output2);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    /**
     * The user closes the connection.
     * 
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session){
    	for (String str : sessions.keySet()) {
        	if (sessions.get(str) == session){
        		sessions.remove(str);
        	}
        }
        System.out.println("Session " +session.getId()+" has ended");
    }
    
    private void informUsers() {
    	
		for (String un : sessions.keySet()) {
			System.out.println("entered in for: "+un);
			if (sessions.get(un).isOpen())
				if (sessions.get(un) != null && sessions.get(un).isOpen()) {
		    		boolean logged = Data.userLogged(un);
		    		if (logged) {
		    			System.out.println("entered into if");
		    			sessions.get(un).getAsyncRemote().sendText("true");
		    		}
					
				}
		}
    	System.out.println("sending response");
    	
    }

    @Override
    public void onMessage(javax.jms.Message msg) {
    	 if (msg instanceof TextMessage) {
             TextMessage tm = (TextMessage) msg;
             try {
                 String text = tm.getText();
                 System.out.println(text+" has messaged you!");
                 
//                 String[] usernames = text.split("\\|");
//                 String you = usernames[0];
//                 String other = usernames[1];
                 
//                ObjectMapper mapper = new ObjectMapper();
//             	ChatPair chatPair;
//				try {
//					chatPair = mapper.readValue(text, ChatPair.class);
//					getMessages(chatPair.getUsername1(), chatPair.getUsername2());
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
                 
                 switch(text) {
                 	case Constants.ACTIVE_USERS:
                 		informUsers();
                 		break;
                 	default:
                 		System.out.println("None of it");
                	         
                 }
                 
                 //Thread.sleep(3000);
             } catch (JMSException e) {
                 e.printStackTrace();
             } 
         }
    	
    }


}
