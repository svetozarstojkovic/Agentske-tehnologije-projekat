package requests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;

public class Requests {
		
	/**
	 * This method is used for all post requests
	 * @param url - location or rest resource
	 * @param jsonObject - object that is going to be send via post request
	 * @return returns string of whatever rest resource returns
	 */
	public static String makePostRequest(String url, JSONObject jsonObject) {
		try{
			HttpClient httpClient =  HttpClientBuilder.create().build(); //Use this instead 		
			HttpPost postMethod = new HttpPost(url);
			
			if (jsonObject == null) {
				System.out.println("jsonObject is null");
				return "";
			}
			
			if (jsonObject != null) {
				StringEntity requestEntity = new StringEntity(
					    jsonObject.toString(),
					    ContentType.APPLICATION_JSON);
				
				postMethod.setEntity(requestEntity);
			}
	
			HttpResponse rawResponse = httpClient.execute(postMethod);
			
			InputStream inputStream = rawResponse.getEntity().getContent();
			
			StringBuilder result = new StringBuilder();  
			
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		    String line = null;  
		    while ((line = br.readLine()) != null) {  
		        result.append(line + "\n");  
		    }
		    br.close();
			
		    postMethod.releaseConnection();
			System.out.println(result.toString());
			
			return result.toString();
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}

	}
	
	/**
	 * This method is used for all post requests
	 * @param url - location or rest resource
	 * @param jsonObject - object that is going to be send via post request
	 * @return returns string of whatever rest resource returns
	 */
	public static String makePostRequest(String url, String jsonString) {
		try{
			
			HttpClient httpClient =  HttpClientBuilder.create().build(); //Use this instead 	
			HttpPost postMethod = new HttpPost(url);
			
			int CONNECTION_TIMEOUT_MS = 1000; // Timeout in millis.
			RequestConfig requestConfig = RequestConfig.custom()
			    .setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
			    .setConnectTimeout(CONNECTION_TIMEOUT_MS)
			    .setSocketTimeout(CONNECTION_TIMEOUT_MS)
			    .build();
			
			postMethod.setConfig(requestConfig);
			
			if (jsonString == null) {
				System.out.println("jsonObject is null");
				return "";
			}
			System.out.println(jsonString);
			if (jsonString != null) {
				StringEntity requestEntity = new StringEntity(
					    jsonString,
					    ContentType.APPLICATION_JSON);
				
				postMethod.setEntity(requestEntity);
			}
			
			
				
			System.out.println("above rawResponse");
			
			
			HttpResponse rawResponse = httpClient.execute(postMethod);
			System.out.println("bellow rawResponse");
			InputStream inputStream = rawResponse.getEntity().getContent();
		
			
			StringBuilder result = new StringBuilder();  
			
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		    String line = null;  
		    while ((line = br.readLine()) != null) {  
		        result.append(line + "\n");  
		    }
		    br.close();
		    
		    postMethod.releaseConnection();
			
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
			
			request.releaseConnection();
			
			System.out.println(result.toString());
			
			return result.toString();
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}

	}
}
