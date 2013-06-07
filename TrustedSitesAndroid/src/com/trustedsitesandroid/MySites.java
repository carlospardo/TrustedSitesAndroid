package com.trustedsitesandroid;

import java.util.List;

import models.Site;

import utils.Config;

import com.facebook.Session;

import adapters.SitesArrayAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import android.app.ListActivity;

public class MySites extends ListActivity  {

	private static final int MENU_OPC1 = 1;
	private static final int MENU_OPC2 = 2;
	
	private static Config conf;
		
	private ListView listViewSites;
	
	private IMySitesView view;
	private MySitesPresenter presenter;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_sites);
		
		conf = new Config(MySites.this);	
		
		Log.i(getLocalClassName(), "IDFacebook: " + conf.getIdFacebook());
		
		view = new IMySitesView(this);
		presenter = new MySitesPresenter(view);
		
		Session session = Session.getActiveSession();

		/**
	     * Si hay una sesion abierta cargamos lista de amigos  
	     */
	    if (session != null && session.isOpened()) {
	    	Log.i(getLocalClassName(), "La session es correcta");	    
    	    presenter.makeMySitesRequest(session);    	  
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
			Intent i = new Intent(MySites.this, LoginView.class);     	 
			startActivity(i);
			finish();
	    }
	    else if (session != null && session.isOpened()) {
	    	Log.i(getLocalClassName(), "La session es correcta");  	  
	    }
	    /**
	     * Si no hay sesion, volvemos a LoginView para que vuelva abrir la 
	     * sesion.
	     */
	    else{
	    	Log.i(getLocalClassName(), "NO HAY SESSION");
			conf.setAccessTokenFB(null);
			Intent i = new Intent(MySites.this, LoginView.class);     	 
			startActivity(i);
			finish();
	    }
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Site site = (Site) listViewSites.getAdapter().getItem(position);
				
		if (v.getBackground() == null){		
			v.setBackgroundColor(getResources().getColor(R.color.SelectionGreen));
			conf.setSiteId(site.getIdSite());
			v.getBackground().setLevel(2);
		}			
		else if (v.getBackground().getLevel() == 2){
			v.setBackgroundColor(Color.TRANSPARENT);
			conf.removeSiteId(site.getIdSite());	
			v.getBackground().setLevel(1);		
		}else{
			v.setBackgroundColor(getResources().getColor(R.color.SelectionGreen));
			conf.setSiteId(site.getIdSite());	
			v.getBackground().setLevel(2);
		}

		Log.i(getLocalClassName(), "size of siteList: " + conf.getListSitesIds().size());
		Log.i(getLocalClassName(), "SiteList: " + conf.getListSitesIds());
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
	    	    presenter.makeMySitesRequest(session);    	  
		    }	
			return true;
		case MENU_OPC2:
			session = Session.getActiveSession();
			session.closeAndClearTokenInformation();
			conf.setAccessTokenFB(null);
			Intent i = new Intent(MySites.this, LoginView.class);     	 
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
	public class IMySitesView{
		
		private MySites activity;
		
		public IMySitesView(MySites activity){
			this.activity = activity;
		}
		
		public void setListSites(List<Site> listSites){
			SitesArrayAdapter sitesAdapter= new SitesArrayAdapter(MySites.this, listSites);
			listViewSites = getListView();
			listViewSites.setAdapter(sitesAdapter);
       	 	listViewSites.setTextFilterEnabled(true);
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
