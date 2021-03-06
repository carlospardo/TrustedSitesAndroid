package utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {

	private final String SHARED_PREFS_FILE = "HMPrefs";
	private static final String FB_ACCESS_TOKEN = "accessTokenFb";
	private static final String ID_FACEBOOK= "idFacebook";
	private static final String NAME_USER= "nameUser";
	private static final String FRIENDS_IDS= "friendsIds";
	private static final String SITES_IDS= "sitesIds";
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

	public String getIdFacebook() {
		return getSettings().getString(ID_FACEBOOK, null);
	}

	public void setIdFacebook(String idFacebook) {
		SharedPreferences.Editor editor = getSettings().edit();
		editor.putString(ID_FACEBOOK, idFacebook);
		editor.commit();
	}
	
	public String getNameUser() {
		return getSettings().getString(NAME_USER, null);
	}

	public void setNameUser(String nameUser) {
		SharedPreferences.Editor editor = getSettings().edit();
		editor.putString(NAME_USER, nameUser);
		editor.commit();
	}
	
	private String getFriendsIds() {
		return getSettings().getString(FRIENDS_IDS, null);
	}

	public void setFriendsIds(String friendsIds) {
		SharedPreferences.Editor editor = getSettings().edit();
		editor.putString(FRIENDS_IDS, friendsIds);
		editor.commit();
	}
	
	public List<String> getListFriendsIds(){
		String friendsIds = getFriendsIds();
		List<String> listFriendsIds = new ArrayList<String>();
		if (friendsIds != null){
			String[] s = friendsIds.split(":");
			for (String id : s){
				if(!id.equals("")){
					listFriendsIds.add(id);
				}
			}
		}	
		return listFriendsIds;
	}
	public void setListFriendsIds(List<String> listFriendsIds) {	
		String friendsIds = ":";
		for (String id : listFriendsIds){
			friendsIds=friendsIds + id + ":";
		}
		setFriendsIds(friendsIds);
		
	}
	public void setFriendId(String id) {
		String friendsIds =getFriendsIds();
		if(friendsIds == null){
			friendsIds = ":";
		}
		if (!friendsIds.contains(id)){
			friendsIds=friendsIds + id + ":";
			setFriendsIds(friendsIds);
		}
	}
	public void removeFriendId(String id) {
		String friendsIds = getFriendsIds();
		if(friendsIds != null){
			friendsIds= friendsIds.replace(":"+id+":", ":");
			if (friendsIds.equals(":")){
				friendsIds = null;
			}
		}
		setFriendsIds(friendsIds);
	}
	public boolean isSelectedThisFriend(String id){
		String friendsIds = getFriendsIds();
		if(friendsIds != null){
			return friendsIds.contains(":"+id+":");
		}else{
			return false;
		}
	}

	private String getSitesIds() {
		return getSettings().getString(SITES_IDS, null);
	}

	public void setSitesIds(String sitesIds) {
		SharedPreferences.Editor editor = getSettings().edit();
		editor.putString(SITES_IDS, sitesIds);
		editor.commit();
	}
	
	public List<String> getListSitesIds(){
		String sitesIds = getSitesIds();
		List<String> listSitesIds = new ArrayList<String>();
		if (sitesIds != null){
			String[] s = sitesIds.split(":");
			for (String id : s){
				if(!id.equals("")){
					listSitesIds.add(id);
				}
			}
		}	
		return listSitesIds;
	}
	public void setListSitesIds(List<String> listSitesIds) {	
		String sitesIds = ":";
		for (String id : listSitesIds){
			sitesIds=sitesIds + id + ":";
		}
		setSitesIds(sitesIds);
		
	}
	public void setSiteId(String id) {
		String sitesIds =getSitesIds();
		if(sitesIds == null){
			sitesIds = ":";
		}
		if (!sitesIds.contains(id)){
			sitesIds=sitesIds + id + ":";
			setSitesIds(sitesIds);
		}
	}
	public void removeSiteId(String id) {
		String sitesIds = getSitesIds();
		if(sitesIds != null){
			sitesIds= sitesIds.replace(":"+id+":", ":");
			if (sitesIds.equals(":")){
				sitesIds = null;
			}
		}
		setSitesIds(sitesIds);
	}
	public boolean isSelectedThisSite(String idSite){
		String sitesIds = getSitesIds();
		if(sitesIds != null){
			return sitesIds.contains(":"+idSite+":");
		}else{
			return false;
		}
	}

}
