package service;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import data.Data;
import model.Host;
import model.User;
import util.Synchronize;

@Path("users")
public class UserService {
	
	
	 @GET
	 @Path("/all") 
	 @Produces(MediaType.APPLICATION_JSON)
	 public List<User> getAllUsers() {
		 return Data.getAllUsers();
	 }
	 
	 @GET
	 @Path("/active") 
	 @Produces(MediaType.APPLICATION_JSON)
	 public List<User> getActiveUsers() {
		 return Data.getActiveUsers();
	 }
	 	 
	 @POST
	 @Path("/register") 
	 @Produces(MediaType.APPLICATION_JSON)
	 public boolean addUser(User user) {
		 System.out.print("addUserPOST()");
		 boolean output = Data.addUser(user);
		 if (output) {
			Synchronize.sendChange("/synchronize/allUsers", Data.getAllUsers());
		 }
		 return output;
	 }
	 
	 
	 @POST
	 @Path("/login") 
	 @Consumes(MediaType.APPLICATION_JSON)
	 @Produces(MediaType.APPLICATION_JSON)
	 public void logUser(User user) {	
		 
	 Data.addHost(user.getHost());
	 boolean output = Data.logUser(user);
		 if (output) {
			Synchronize.sendChange("/synchronize/activeUsers", Data.getActiveUsers());
			Synchronize.sendChange("/synchronize/allUsers", Data.getAllUsers());
			//Synchronize.sendChange("/synchronize/messages", Data.getMessages());
			Synchronize.sendChange("/synchronize/hosts", Data.getHosts());
			System.out.println("done synchronizing");
		 }		
		 
	 }
	 
	 @POST
	 @Path("/logout") 
	 @Produces(MediaType.APPLICATION_JSON)
	 public boolean logoutUser(String username) {
		 boolean output = Data.logoutUser(username);
		 if (output) {
			Synchronize.sendChange("/synchronize/activeUsers", Data.getActiveUsers());
		 }
		 return output;
	 }
	 
	 

}
