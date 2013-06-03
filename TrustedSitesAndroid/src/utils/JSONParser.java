package utils;

import models.User;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class JSONParser {

	public static JSONObject userToJSON(User u){
		Gson gson = new Gson();
		JSONObject jObject = null;

		try {
			jObject = new JSONObject(gson.toJson(u, User.class));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return jObject;
	}

}
