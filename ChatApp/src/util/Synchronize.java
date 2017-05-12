package util;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;

import data.Data;
import model.Host;
import model.Message;
import model.User;
import requests.Requests;

@Path("synchronize")
public class Synchronize {
	
	public static void getAll() {
		getAllUsers();
		getActiveUsers();
		getMessages();
		getHosts();
	}
	
	public static void getAllUsers() {
		String allUsers = Requests.makeGetRequest("http://localhost:8080/UserApp/users/all");
		try {
			User[] users = new ObjectMapper().readValue(allUsers, User[].class);
			Data.setUsers(users);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void getActiveUsers() {
		String activeUsers = Requests.makeGetRequest("http://localhost:8080/UserApp/users/active");
		try {
			User[] users = new ObjectMapper().readValue(activeUsers, User[].class);
			Data.setActiveUsers(users);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void getMessages() {
		String messages = Requests.makeGetRequest("http://localhost:8080/UserApp/messages/all");
		try {
			Message[] users = new ObjectMapper().readValue(messages, Message[].class);
			Data.setMessages(users);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void getHosts() {
		String hosts = Requests.makeGetRequest("http://localhost:8080/UserApp/hosts/all");
		try {
			Host[] users = new ObjectMapper().readValue(hosts, Host[].class);
			Data.setHosts(users);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	@POST
	@Path("/allUsers")
	@Consumes(MediaType.APPLICATION_JSON)
	public void setAllUsers(String allUsers) {
		System.out.println("Synchronize/setAllUsers");
		try {
			User[] users = new ObjectMapper().readValue(allUsers, User[].class);
			Data.setUsers(users);
			new SendJMSMessage(Constants.ALL_USERS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@POST
	@Path("/activeUsers")
	@Consumes(MediaType.APPLICATION_JSON)
	public void setActiveUsers(String activeUsers) {
		System.out.println("Synchronize/setActiveUsers");
		try {
			User[] users = new ObjectMapper().readValue(activeUsers, User[].class);
			Data.setActiveUsers(users);
			new SendJMSMessage(Constants.ACTIVE_USERS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@POST
	@Path("/messages")
	@Consumes(MediaType.APPLICATION_JSON)
	public void setMessages(String messages) {
		System.out.println("Synchronize/messages");
		try {
			Message[] messageList = new ObjectMapper().readValue(messages, Message[].class);
			Data.setMessages(messageList);
			new SendJMSMessage(Constants.MESSAGES);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@POST
	@Path("/hosts")
	@Consumes(MediaType.APPLICATION_JSON)
	public void setHosts(String hosts) {
		System.out.println("setHosts(): "+hosts);
		try {
			Host[] hostList = new ObjectMapper().readValue(hosts, Host[].class);
			for (Host host : hostList) {
				System.out.println(host.getAddress());
			}
			Data.setHosts(hostList);
			System.out.println("Data.getHost().size(): "+Data.getHosts().size());
			//new SendJMSMessage(Constants.HOSTS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void sendChange(String url, Object collection) {
		
		String jsonInString;
		System.out.println("sendChange()");
		try {
			jsonInString = new ObjectMapper().writeValueAsString(collection);
			System.out.println(jsonInString);
			for (Host host : Data.getHosts()) {
				String hostAddress = AddressUtil.getHostAndPort(host.getAddress());
				System.out.println("sendChange() "+"http://"+hostAddress+"/ChatApp/rest"+url);
				Requests.makePostRequest("http://"+hostAddress+"/ChatApp/rest"+url, jsonInString);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
