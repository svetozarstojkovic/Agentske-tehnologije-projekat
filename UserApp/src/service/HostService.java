package service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import data.Data;
import model.Host;
import util.Synchronize;

@Path("hosts")
public class HostService {
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Host> getHosts(){
		return Data.getHosts();
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean addHost(Host host) {
		System.out.println("entered addHost()");
		boolean output = Data.addHost(host);
		System.out.println("finished addingHost "+output);
		 if (output) {
			Synchronize.sendChange("/synchronize/hosts", Data.getHosts());
		 }
		 return output;
	}
	
	@POST
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean removeServer(Host host) {
		boolean output = Data.removeHost(host);
		if (output) {
			Synchronize.sendChange("/synchronize/hosts", Data.getHosts());
		}
		return output;
	}
	

}
