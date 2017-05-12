package websockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;

import data.Data;
import dtos.MessageDTO;
import model.Message;
import model.User;
import requests.Requests;
import util.Constants;
import util.Converter;
import util.Synchronize;

@MessageDriven(activationConfig = { 
        @ActivationConfigProperty(propertyName = "destinationType", 
                propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName="destination",
                propertyValue="jms/topic/informTopic")                
        })

@ServerEndpoint("/chat/{username}") 
public class ChatWebSocket implements MessageListener{
	
	private static Map<String, Session> sessions = new HashMap<>();
	
	/**
     * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was 
     * successful.
     */
    @OnOpen
    public synchronized void onOpen(@PathParam("username") String username, Session session){
        System.out.println(session.getId() + " has opened a connection"); 
		
		session.getAsyncRemote().sendText("Connection is fantastic and username is: "+username);    
		sessions.put(username, session);
		getUsers();

    }
    
    private synchronized void getUsers() {
    	System.out.println("getUsers()");
//    	String url = "http://localhost:8080/UserApp/rest/users/active";
//
//		String output = Requests.makeGetRequest(url);
		
//		String allUsers = Converter.getJSONString(Data.getUsers());
		
    	
    	List<User> offlineUsers = new ArrayList<>();
    	
    	for (User allUser : Data.getUsers()) {
    		boolean exists = false;
    		for (User activeUser : Data.getActiveUsers()) {
    			if (allUser.getUsername().equals(activeUser.getUsername())) {
    				exists = true;
    			}
    		}
    		
    		if (!exists) {
    			offlineUsers.add(allUser);
    		}
    	}
    	
    	String activeUsersString = Converter.getJSONString(Data.getActiveUsers());
    	String offlineUsersString = Converter.getJSONString(offlineUsers);
		
		for (String un : sessions.keySet()) {
			System.out.println("entered in for: "+un);
			if (sessions.get(un).isOpen()){
				sessions.get(un).getAsyncRemote().sendText("activeUsers"+activeUsersString);
				sessions.get(un).getAsyncRemote().sendText("offlineUsers"+offlineUsersString);
			}
		}
			 
    }
    
    public synchronized String getMessages(String username1, String username2) throws IOException{
    		System.out.println("getMessages("+username1+","+username2+")");
//    		JSONObject jsonObject = new JSONObject();
//	    	
//	    	try {
//				jsonObject.put("username1", username1);
//				jsonObject.put("username2", username2);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				System.out.println("json exception");
//				return "";
//			}
//	    	
//    		String url = "http://localhost:8080/UserApp/rest/messages/getForUser";
//	    	
//    		String output = Requests.makePostRequest(url, jsonObject);
    		
    		String output = Converter.getJSONString(Data.getMessages());

    		
    		System.out.println("Output: "+output);
			//print result
			System.out.println("Number of sessions: "+sessions.size());
			
//			if(sessions.get(username1) != null) {
//				System.out.println("Refresh usera1");
//				sessions.get(username1).getAsyncRemote().sendText("messages"+output);
//			
//			}
			
			informUsers();

			System.out.println("end of method getMessages()");
			return output;
			
    }
 
    /**
     * When a user sends a message to the server, this method will intercept the message
     * and allow us to react to it. For now the message is read as a String.
     * @throws IOException 
     */
    @OnMessage
    public synchronized void onMessage(String msg, Session session) throws IOException{
    	    	
    	System.out.println("entered on message: " + msg);
    	ObjectMapper mapper = new ObjectMapper();
    	MessageDTO message = new MessageDTO("","","","");
    	try {
    		message = mapper.readValue(msg, MessageDTO.class);
    	} catch (Exception e) {
			System.out.println(e);
			return;
		}
    	
        System.out.println("Session id	: " + session.getId());
        
        System.out.println("From	: "+message.getFrom());
        System.out.println("To		: "+message.getTo());
        System.out.println("Subject	: "+message.getSubject());
        System.out.println("Content	: "+message.getContent());
        
        if (!"".equals(message.getSubject()) && !"".equals(message.getContent())){
        	sendMessage(message.getFrom(), message.getTo(), message.getSubject(), message.getContent());
        } else {
        	getMessages(message.getFrom(), message.getTo());
        }
  

    	System.out.println("end of method");
    }
 
    private synchronized void sendMessage(String from, String to, String subject, String content) {
    	System.out.println("sendMessage()");
    	try {
    		System.out.println("sending message");
    		String url = "http://localhost:8080/UserApp/rest/messages/add";
            //URL obj = new URL("http://localhost:8080/UserApp1/rest/messages/add?from="+from+"&to="+to+"&subject="+subject+"&content="+content);
            
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("from", from);
            jsonObject.put("to", to);
            jsonObject.put("subject", subject);
            jsonObject.put("content", content);
            
            //String output = Requests.makePostRequest(url, jsonObject);
            User userFrom = Data.getUser(from);
    		User userTo = Data.getUser(to);
			boolean output = Data.addMessage(new Message(userFrom, userTo, new Date(System.currentTimeMillis()), subject, content));
			if (output) {
				Synchronize.sendChange("/synchronize/messages", Data.getMessages());
			}
            //Synchronize.sendChange("/synchronize/messages", Data.getMessages());
                		
    		//System.out.println("Message output is: "+output);
    		    		
//    		if (sessions.get(from) != null && sessions.get(from).isOpen()) {
//    			sessions.get(from).getAsyncRemote().sendText("messages"+output);
//    		}
    		
             
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    /**
     * The user closes the connection.
     * 
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session){
        System.out.println("Session " +session.getId()+" has ended");
        for (String str : sessions.keySet()) {
        	if (sessions.get(str) == session){
        		sessions.remove(str);
        	}
        }
    }
    
    
    private void informUsers() {
    	
		for (String un : sessions.keySet()) {
			System.out.println("entered in for: "+un);
			if (sessions.get(un).isOpen())
				if (sessions.get(un) != null && sessions.get(un).isOpen()) {
		    		String jsonMessages;
					try {
						jsonMessages = new ObjectMapper().writeValueAsString(Data.getMessages());
						sessions.get(un).getAsyncRemote().sendText("messages"+jsonMessages);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
                 	case Constants.MESSAGES:
                 		informUsers();
                 		break;
                 	case Constants.ALL_USERS:
                 		getUsers();
                 		break;
                 	case Constants.ACTIVE_USERS:
                 		getUsers();
                 		break;
                 	case Constants.HOSTS:
                 		System.out.println("hosts changed");
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
