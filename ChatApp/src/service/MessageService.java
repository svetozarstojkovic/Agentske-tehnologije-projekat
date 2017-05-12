package service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import util.SendJMSMessage;

@Path("messages")
public class MessageService {
	
	@POST
	@Path("/inform")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String pushMessageToUser(String chatPair) {
		
		System.out.println(chatPair);
		
		new SendJMSMessage(chatPair);
		
		return "success";
	}
	
	
}
