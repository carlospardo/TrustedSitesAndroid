package utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {

	private final String SHARED_PREFS_FILE = "HMPrefs";
	private static final String FB_ACCESS_TOKEN = "accessTokenFb";
	
	private Context mContext;

	public Config(Context context){
	 mContext = context;
	}
	
	private SharedPreferences getSettings(){
		 return mContext.getSharedPreferences(SHARED_PREFS_FILE, 0);
	}

	public String getAccessTokenFB() {
		return getSettings().getString(FB_ACCESS_TOKEN, null);
	}
	
	public void setAccessTokenFB(String accessTokenFb){
		SharedPreferences.Editor editor = getSettings().edit();
		editor.putString(FB_ACCESS_TOKEN, accessTokenFb);
		editor.commit();
	}

}
