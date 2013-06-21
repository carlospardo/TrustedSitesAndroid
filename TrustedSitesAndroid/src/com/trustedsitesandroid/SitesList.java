package com.trustedsitesandroid;

import java.util.List;

import models.Site;

import utils.Config;

import com.facebook.Session;

import adapters.SitesArrayAdapter;
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

public class SitesList extends ListActivity  {

	private static final int MENU_OPC1 = 1;
	private static final int MENU_OPC2 = 2;
	
	private static Config conf;
		
	private ListView listViewSites;
	
	private ISitesListView view;
	public static SitesListPresenter presenter;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sites_list);
		
		conf = new Config(SitesList.this);	
		
		Log.i(getLocalClassName(), "IDFacebook: " + conf.getIdFacebook());
		
		view = new ISitesListView(this);
		presenter = new SitesListPresenter(view);
		
		Session session = Session.getActiveSession();

	    if (session != null && session.isOpened()) {
	    	Log.i(getLocalClassName(), "La session es correcta");	
	    	presenter.makeMySitesRequest(); 
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
			Intent i = new Intent(SitesList.this, Login.class);     	 
			startActivity(i);
			finish();
	    }
		/**
	     * Si hay una sesion abierta cargamos lista de sitios  
	     */
	    else if (session != null && session.isOpened()) {
	    	Log.i(getLocalClassName(), "La session es correcta");
	    	Toast.makeText(SitesList.this, getResources().getString(R.string.init_sites_list) , Toast.LENGTH_SHORT).show();
	    	//presenter.makeMySitesRequest(session); 
	    }
	    /**
	     * Si no hay sesion, volvemos a LoginView para que vuelva abrir la 
	     * sesion.
	     */
	    else{
	    	Log.i(getLocalClassName(), "NO HAY SESSION");
			conf.setAccessTokenFB(null);
			Intent i = new Intent(SitesList.this, Login.class);     	 
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
	    	    presenter.makeMySitesRequest();    	  
		    }	
			return true;
		case MENU_OPC2:
			session = Session.getActiveSession();
			session.closeAndClearTokenInformation();
			conf.setAccessTokenFB(null);
			Intent i = new Intent(SitesList.this, Login.class);     	 
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
	public class ISitesListView{
		
		private SitesList activity;
		
		public ISitesListView(SitesList activity){
			this.activity = activity;
		}
		
		public void setListSites(List<Site> listSites){
			SitesArrayAdapter sitesAdapter= new SitesArrayAdapter(SitesList.this, listSites, conf);
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
