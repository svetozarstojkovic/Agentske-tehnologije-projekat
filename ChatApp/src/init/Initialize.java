package init;

import java.lang.management.ManagementFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.ObjectName;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import model.Host;
import requests.Requests;
import util.Converter;
import util.Synchronize;

@Singleton
@Startup
public class Initialize{

	@PostConstruct
	public void init() {
		System.out.println("post construct");
		

		
//		try {
//			//http port
//			String port = ManagementFactory.getPlatformMBeanServer().getAttribute(new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http"), "port").toString();
//			//http adress
//			String host = ManagementFactory.getPlatformMBeanServer().getAttribute(new ObjectName("jboss.as:interface=public"), "inet-address").toString();
//		
//			Requests.makePostRequest("http://localhost:8080/UserApp/hosts/add", Converter.getJSONObject(new Host(host+":"+port, "some stupid name")));
//		
//			Synchronize.getAll();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
	}
	
	@PreDestroy
	public void preDestroy() {
		System.out.println("preDestroy");
//		try {
//			//http port
//			String port = ManagementFactory.getPlatformMBeanServer().getAttribute(new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http"), "port").toString();
//			//http adress
//			String host = ManagementFactory.getPlatformMBeanServer().getAttribute(new ObjectName("jboss.as:interface=public"), "inet-address").toString();
//		
//			Requests.makePostRequest("http://localhost:8080/UserApp/hosts/remove", Converter.getJSONObject(new Host(host+":"+port, "some stupid name")));
//		
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
	}
	
	
	

}
