package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import data.Data;
import model.User;
import requests.Requests;
import util.Converter;

@Path("users")
public class UserService {
		 
	 @POST
	 @Path("/register") 
	 @Produces(MediaType.APPLICATION_JSON)
	 public boolean register(String user) {
	 
		String url = "http://localhost:8080/UserApp/rest/users/register";
		
		try {
			User u = new ObjectMapper().readValue(user, User.class);
			if ("true".equals(new Requests().makePostRequest(url, Converter.getJSONObject(u)))){
				return true;
			} else {
				return false;
			}
		 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	 }
	 
	 public void removeUser(User user) {
		 System.out.println("Work in progress");
	 }


}
