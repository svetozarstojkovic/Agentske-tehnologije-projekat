package data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;

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
		
//		users.add(u1);
//		users.add(u2);
//		users.add(u3);
//		users.add(u4);
//		users.add(u5);
//		users.add(u6);
//		users.add(u7);
		
		Message message1 = new Message(u1,u2,new Date(System.currentTimeMillis()),"tema1", "neki tekst poruke1");
		Message message4 = new Message(u2,u1,new Date(System.currentTimeMillis()),"tema4", "neki tekst poruke4");
		Message message2 = new Message(u2,u3,new Date(System.currentTimeMillis()),"tema2", "neki tekst poruke2");
		Message message3 = new Message(u3,u4,new Date(System.currentTimeMillis()),"tema3", "neki tekst poruke3");
		
		messages.add(message1);
		messages.add(message2);
		messages.add(message3);
		messages.add(message4);
	}


	public static List<User> getUsers() {
		return users;
	}

	public static void setUsers(List<User> users) {
		Data.users = users;
	}
	
	public static void setUsers(User[] users) {
		Data.users.clear();
		for (User user : users) {
			Data.users.add(user);
		}
	}
	
	public static User getUser(String username) {
		for (User u : users) {
			if (u.getUsername().equals(username)){
				return u;
			}
		}
		return null;
	}
	
	public static boolean userExists(String username) {

		for (User u : users) {
			if (u.getUsername().equals(username)) {
				return true;
			}
		}

		return false;
	}

	public static List<User> getActiveUsers() {
		return activeUsers;
	}

	public static void setActiveUsers(List<User> activeUsers) {
		Data.activeUsers = activeUsers;
	}
	
	public static boolean userLogged(String username) {
		for (User u : activeUsers) {
			if (u.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	public static void setActiveUsers(User[] activeUsers) {
		Data.activeUsers.clear();
		for (User user : activeUsers) {
			Data.activeUsers.add(user);
		}
	}

	public static List<Host> getHosts() {
		return hosts;
	}

	public static void setHosts(List<Host> hosts) {
		Data.hosts = hosts;
	}
	
	public static void setHosts(Host[] hosts) {
		Data.hosts.clear();
		for (Host host : hosts) {
			Data.hosts.add(host);
		}
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

	public static void setMessages(List<Message> messages) {
		Data.messages = messages;
	}
	
	public static boolean addMessage(Message message) {
		try{
			messages.add(message);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static void setMessages(Message[] messages) {
		Data.messages.clear();
		for (Message message : messages) {
			Data.messages.add(message);
		}
	}
	
	

}
