package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import models.User;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.util.Log;

public class ApiHelpers {

	
	
	public static String SERVER_ADDRESS = "http://192.168.1.5:8080/";
	
	public static String APP_BASEURL = "PruebaSpringWicket/rest";
	
	public static String getContent(String URL) throws Exception {

		HttpGet request = new HttpGet(URL);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used. 
		int timeoutConnection = 100000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 200000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpClient httpclient = new DefaultHttpClient(httpParameters);

		request.setHeader("Content-Type", "application/json");
		ResponseHandler<String> handler = new BasicResponseHandler();
		
		String result = "";
		try {
			HttpResponse response = httpclient.execute(request);
            result=Userrequest(response); 
            if(response.getStatusLine().getStatusCode()==HttpStatus.SC_BAD_REQUEST){
				throw new Exception(result);
			}
			//result = httpclient.execute(request, handler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new Exception("El servidor no está disponible");
		}
		
		httpclient.getConnectionManager().shutdown();
		return result;
	}
	
	public static String putContent(String URL,  JSONObject o) throws Exception {     


		HttpParams httpParameters = new BasicHttpParams();
		HttpPut request = new HttpPut(URL);
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used. 
		int timeoutConnection = 100000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 200000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		String result = "";
		try {         

			StringEntity se = new StringEntity(o.toString());

			request.setEntity(se);
			request.setHeader("Content-Type", "application/json");
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			HttpResponse response = httpclient.execute(request);
            result=Userrequest(response);
            
            if(response.getStatusLine().getStatusCode()==HttpStatus.SC_BAD_REQUEST){
				throw new Exception(result);
			}
            
			//result = httpclient.execute(request, handler);
		} 
		catch (ClientProtocolException e) {         
			//TODO Auto-generated catch block     
		} 
		catch (IOException e) {         
			//TODO Auto-generated catch block     
		}

		return result;
	}
	
	public static String Userrequest(HttpResponse response){
	 String result = "";
        try     
        {
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;            
            while((line = reader.readLine()) != null){                 
            	str.append(line);                
            }
            in.close();
            result = str.toString();
            //updateData(result);         
        }
        catch(Exception ex)
        {
            //responsetxt.setText(ex.getMessage());
        }
        return result;
    }
	
	public static void register(User user)throws Exception{
		JSONObject userJSON = JSONParser.userToJSON(user);
		ApiHelpers.putContent(SERVER_ADDRESS + APP_BASEURL+"/users/register", userJSON);
		//ApiHelpers.getContent(SERVER_ADDRESS + APP_BASEURL+"/users/register");
		Log.i("ApiHelpers","llamada hecha!");
	}
	
}
