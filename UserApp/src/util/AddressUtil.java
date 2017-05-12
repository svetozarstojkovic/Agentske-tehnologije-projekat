package util;

public class AddressUtil {
	
	public static String getHostAndPort(String location) {
		int index = location.indexOf("//");
		location = location.substring(index+2);
		
		return location.substring(0, location.indexOf("/"));
	}

}
