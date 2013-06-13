package com.trustedsitesandroid;

import java.util.List;

import models.User;

import utils.Config;

import com.facebook.Session;

import adapters.FriendsArrayAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import android.app.ListActivity;

public class FriendsList extends ListActivity  {

	private static final int MENU_OPC1 = 1;
	private static final int MENU_OPC2 = 2;
	
	private static Config conf;
		
	private ListView listViewFriends;
	
	private IFriendsListView view;
	private FriendsListPresenter presenter;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_list);
		
		conf = new Config(FriendsList.this);	
        
		view = new IFriendsListView(this);
		presenter = new FriendsListPresenter(view);
		
		Session session = Session.getActiveSession();

		/**
	     * Si hay una sesion abierta cargamos lista de amigos  
	     */
	    if (session != null && session.isOpened()) {
	    	Log.i(getLocalClassName(), "La session es correcta");
    	    presenter.makeMyFriendsRequest(session);    	  
	    }	   
	}
	
	@Override
	public void onResume() {
		Log.i(getLocalClassName(), "entro en onResume");
	    super.onResume();
	    	  	    
	    Session session = Session.getActiveSession();
	    
	    Log.i(getLocalClassName(), "session= " + session);
	    Log.i(getLocalClassName(), "session.isClosed()=" + session.isClosed() + " session.isOpened()=" + session.isOpened());
	   	    
	    /**
	     * Si hay sesion pero esta cerrada, volvemos a a LoginView para que vuelva abrir la 
	     * sesion.
	     */
	    if (session!= null && !session.isOpened()){
	    
	    	Log.i(getLocalClassName(), "La session esta cerrada");
			Intent i = new Intent(FriendsList.this, LoginView.class);     	 
			startActivity(i);
			finish();
	    }
	    else if (session != null && session.isOpened()) {
	    	Log.i(getLocalClassName(), "La session es correcta");  
	    	Toast.makeText(FriendsList.this, getResources().getString(R.string.init_friends_list) , Toast.LENGTH_SHORT).show();
	    }
	    /**
	     * Si no hay sesion, volvemos a LoginView para que vuelva abrir la 
	     * sesion.
	     */
	    else{
	    	Log.i(getLocalClassName(), "NO HAY SESSION");
			conf.setAccessTokenFB(null);
			Intent i = new Intent(FriendsList.this, LoginView.class);     	 
			startActivity(i);
			finish();
	    }
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		User friend = (User) listViewFriends.getAdapter().getItem(position);
				
		if (v.getBackground() == null){		
			v.setBackgroundColor(getResources().getColor(R.color.SelectionGreen));
			conf.setFriendId(friend.getIdFacebook());
			v.getBackground().setLevel(2);
		}			
		else if (v.getBackground().getLevel() == 2){
			v.setBackgroundColor(Color.TRANSPARENT);
			conf.removeFriendId(friend.getIdFacebook());	
			v.getBackground().setLevel(1);
			if(SitesList.presenter != null){
				SitesList.presenter.deselectFriendsSites(friend.getIdFacebook());
			}
		}else{
			v.setBackgroundColor(getResources().getColor(R.color.SelectionGreen));
			conf.setFriendId(friend.getIdFacebook());	
			v.getBackground().setLevel(2);
		}
		Session session = Session.getActiveSession();
		if(SitesList.presenter != null){
			SitesList.presenter.makeMySitesRequest();
		}		 
		Log.i(getLocalClassName(), "size of friendList: " + conf.getListFriendsIds().size());
		Log.i(getLocalClassName(), "friendList: " + conf.getListFriendsIds());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, MENU_OPC1, MENU_OPC1, getResources().getString(R.string.update)).setIcon(R.drawable.update);
		menu.add(Menu.NONE, MENU_OPC2, MENU_OPC2, getResources().getString(R.string.logout)).setIcon(R.drawable.exit);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_OPC1:
			Session session = Session.getActiveSession();
			/**
		     * Si hay una sesion abierta cargamos lista de amigos  
		     */
		    if (session != null && session.isOpened()) {
		    	Log.i(getLocalClassName(), "La session es correcta");
	    	    presenter.makeMyFriendsRequest(session);    	  
		    }	
			return true;
		case MENU_OPC2:
			session = Session.getActiveSession();
			session.closeAndClearTokenInformation();
			conf.setAccessTokenFB(null);
			Intent i = new Intent(FriendsList.this, LoginView.class);     	 
			startActivity(i);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Clase Vista
	 */
	public class IFriendsListView{
		
		private FriendsList activity;
		
		public IFriendsListView(FriendsList activity){
			this.activity = activity;
		}
		
		public void setListFriends(List<User> listFriends){
			FriendsArrayAdapter friendsAdapter= new FriendsArrayAdapter(FriendsList.this, listFriends);
			listViewFriends = getListView();
			listViewFriends.setAdapter(friendsAdapter);
       	 	listViewFriends.setTextFilterEnabled(true);			
		}

		public Config getConf(){
			return conf;
		}
		public Resources getResources(){
			return activity.getResources();
		}
		
		public Activity getActivity(){
			return activity;
		}	
	}	
}
