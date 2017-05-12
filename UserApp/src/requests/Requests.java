package requests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.core.AsynchronousDispatcher;

public class Requests {
	
	/**
	 * This method is used for all post requests
	 * @param url - location or rest resource
	 * @param jsonObject - object that is going to be send via post request
	 * @return returns string of whatever rest resource returns
	 */
	public static String makePostRequest(String url, String string) {
		try{
			HttpClient httpClient =  HttpClientBuilder.create().build(); //Use this instead 		
			HttpPost postMethod = new HttpPost(url);
			
				StringEntity requestEntity = new StringEntity(
					    string,
					    ContentType.APPLICATION_JSON);
				
				postMethod.setEntity(requestEntity);
			

			HttpResponse rawResponse = httpClient.execute(postMethod);
			
			
			InputStream inputStream = rawResponse.getEntity().getContent();
			
			StringBuilder result = new StringBuilder();  
			
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			
		    String line = null;  
		    while ((line = br.readLine()) != null) {  
		        result.append(line + "\n");  
		    }
		    br.close();
		    
		    System.out.println("Result is: "+result.toString());
		    return result.toString();
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}


		
	}

	/**
	 * This method is used for all get requests
	 * @param url - location of rest resource
	 * @return returns string of whatever rest resource returns
	 */
	public static String makeGetRequest(String url) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);
		
			HttpResponse response = client.execute(request);

			System.out.println("Response Code : "
			                + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			System.out.println(result.toString());
			
			
			return result.toString();
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}
		
	}

	
}
