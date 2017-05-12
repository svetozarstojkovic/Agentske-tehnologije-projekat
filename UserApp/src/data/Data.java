package data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Host;
import model.Message;
import model.User;

public class Data {
	
	private static List<User> users = new ArrayList<>();
	private static List<User> activeUsers = new ArrayList<>();
	private static List<Host> hosts = new ArrayList<>();
	private static List<Message> messages = new ArrayList<>();
	
	static{
	
		User u1 = new User("1", "1", null);
		User u2 = new User("2", "2", null);
		User u3 = new User("3", "3", null);
		User u4 = new User("4", "4", null);
		User u5 = new User("5", "5", null);
		User u6 = new User("6", "6", null);
		User u7 = new User("7", "7", null);
		
		users.add(u1);
		users.add(u2);
		users.add(u3);
		users.add(u4);
		users.add(u5);
		users.add(u6);
		users.add(u7);
		
		Message message1 = new Message(u1,u2,new Date(System.currentTimeMillis()),"tema1", "neki tekst poruke1");
		Message message4 = new Message(u2,u1,new Date(System.currentTimeMillis()),"tema4", "neki tekst poruke4");
		Message message2 = new Message(u2,u3,new Date(System.currentTimeMillis()),"tema2", "neki tekst poruke2");
		Message message3 = new Message(u3,u4,new Date(System.currentTimeMillis()),"tema3", "neki tekst poruke3");
		
		messages.add(message1);
		messages.add(message2);
		messages.add(message3);
		messages.add(message4);
	}
	
	public static boolean addHost(Host host) {
		for (Host h : hosts) {
			if (h.getAddress().equals(host.getAddress())) {
				return false;
			}
		}
		hosts.add(host);
		return true;
	}
	
	public static boolean removeHost(Host host) {
		for (Host h : hosts) {
			if (h.getAddress().equals(host.getAddress())) {
				hosts.remove(h);
				return true;
			}
		}
		return false;
	}
	
	public static List<User> getAllUsers() {
		return users;
	}
	
	public static List<User> getActiveUsers() {
		return activeUsers;
	}
	
	public static boolean addUser(User user) {
		try{
			if (userExists(user)) {
				return false;
			}
			users.add(user);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	
	public static User getUser(String username) {
		for (User u : users) {
			if (u.getUsername().equals(username)){
				return u;
			}
		}
		return null;
	}
	
	public static User getActiveUser(String username) {
		for (User u : activeUsers) {
			if (u.getUsername().equals(username)){
				return u;
			}
		}
		return null;
	}
	
	public static boolean userExists(User user) {
		try{
			for (User u : users) {
				if (u.getUsername().equals(user.getUsername())) {
					if (u.getPassword().equals(user.getPassword())) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	public static boolean userLogged(User user) {
		for (User u : activeUsers) {
			if (u.getUsername().equals(user.getUsername())) {
				if (u.getPassword().equals(user.getPassword())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean logUser(User user) {
		if (userExists(user) && !userLogged(user)) {
			System.out.println("Successful logging: "+user.toString());
			activeUsers.add(user);
			return true;
		}
		System.out.println("Logging failed: "+user.toString());
		return false;
	}
	
	public static boolean logoutUser(String username) {
		for (User u : activeUsers) {
			if (u.getUsername().equals(username)) {
				activeUsers.remove(u);
				return true;
			}
		}
		return false;
	}
	
	public static List<Message> getMessages() {
		return messages;
	}
	
	public static List<Message> getMessagesForUsers(String username1, String username2) {
		List<Message> retValue = new ArrayList<>();
		for (Message message : messages) {
			if (message.getFrom().getUsername().equals(username1) && message.getTo().getUsername().equals(username2)) {
				retValue.add(message);
			} else if (message.getTo().getUsername().equals(username1) && message.getFrom().getUsername().equals(username2)) {
				retValue.add(message);
			}
		}
		return retValue;
	}
	
	public static boolean addMessage(Message message) {
		try{
			messages.add(message);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean informUsers(String username1, String username2) {
		try{
	    	URL url = new URL("http://localhost:8080/ChatApp/rest/messages/inform?username1="+username1+"&username2="+username2);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	
			// optional default is GET
			con.setRequestMethod("GET");
	
			//add request header
			//con.setRequestProperty("User-Agent", USER_AGENT);
	
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			
			System.out.println("Input stream is: "+in);
			
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	
			//print result
		

			
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
		return true;
	}
	
	
	public static List<Host> getHosts() {
		return hosts;
	}

}
