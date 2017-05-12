package service;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import data.Data;
import dtos.ChatPair;
import dtos.MessageDTO;
import model.Message;
import model.User;
import requests.Requests;
import requests.SendJMSMessage;
import util.AddressUtil;
import util.Synchronize;

@Path("messages")
public class MessageService {
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getMessages() {
		return Data.getMessages();
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean addMessage(MessageDTO messageDTO){
		User userFrom = Data.getUser(messageDTO.getFrom());
		User userTo = Data.getUser(messageDTO.getTo());
		boolean output = Data.addMessage(new Message(userFrom, userTo, new Date(System.currentTimeMillis()), messageDTO.getSubject(), messageDTO.getContent()));
		
//		ChatPair cp = new ChatPair(messageDTO.getFrom(), messageDTO.getTo());
//		User activeUserTo = Data.getActiveUser(messageDTO.getTo());
//		if (activeUserTo != null) {
//			JSONObject json = new JSONObject();
//			try {
//				json.put("username1", userTo.getUsername());
//				json.put("username2", userFrom.getUsername());
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//
//			String location = "http://"+AddressUtil.getHostAndPort(activeUserTo.getHost().getAddress())+"/ChatApp/rest/messages/inform";
//			System.out.println(location);
//			RequestsUtil.makePostRequest(location, json.toString());
////			RequestsUtil.makePostRequest(location, userTo.getUsername()+"|"+userFrom.getUsername());
//		}
//		System.out.println("stigao ovde");
		
		 if (output) {
			Synchronize.sendChange("/synchronize/messages", Data.getMessages());
		 }
		 return output;
		//return getMessagesForUser(cp);
	}
	
	@POST
	@Path("/getForUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getMessagesForUser(ChatPair cp) {
		List<Message> messages = Data.getMessagesForUsers(cp.getUsername1(), cp.getUsername2());
		System.out.println("messages: "+messages);
		return messages;
	}
	

}
