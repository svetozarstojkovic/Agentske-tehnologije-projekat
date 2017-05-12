package util;

import java.io.IOException;
import java.util.Collection;

import org.codehaus.jackson.map.ObjectMapper;

import data.Data;
import model.Host;
import requests.Requests;

public class Synchronize {
	
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
