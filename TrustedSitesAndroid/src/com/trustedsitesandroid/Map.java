package com.trustedsitesandroid;

import models.Site;
import utils.Config;

import com.facebook.Session;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.LocationListener;
import android.location.LocationManager;

import android.net.Uri;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Map extends FragmentActivity implements OnMapClickListener, OnMapLongClickListener, 
												InfoWindowAdapter, OnInfoWindowClickListener {

	private static final int MENU_OPC1 = 1;
	private static final int MENU_OPC2 = 2;
	
	private GoogleMap map;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private Marker markerSelect;
	private IMapView view;
	private MapPresenter presenter;
	
	private static Config conf;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		conf = new Config(Map.this);	
		
		view = new IMapView(this);
		presenter = new MapPresenter(view);
	
		setUpMap();
	
	}

	/* actualizamos la ubicacion */
	@Override
	protected void onResume() {
		super.onResume();
		presenter.initLocationUpdates();
		presenter.makeMapSitesRequest();	
		Toast.makeText(Map.this, getResources().getString(R.string.init_map) , Toast.LENGTH_SHORT).show();
	}

	/* detenemos el listener */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(locationListener);
	}

	@Override
	protected void onStop() {
		super.onStop();
		locationManager.removeUpdates(locationListener);
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		
		if(marker.getTitle() == null){
			double lat = marker.getPosition().latitude;
			double lng = marker.getPosition().longitude;
			Log.d(getLocalClassName(),"lat: " + lat + " lng: " + lng);
			
			
			Intent i = new Intent(Map.this, RegisterSite.class);     	 			
			i.putExtra("latitude", Double.toString(lat));
			i.putExtra("longitude", Double.toString(lng));
			startActivity(i);
			
		}else{
			
		

			Site site = presenter.getSiteByIdMarker(marker.getId());
			/**
			 * Editamos el site
			 */
			if (site.getOwnerId().equals(conf.getIdFacebook())){
				Intent i = new Intent(Map.this, ModifySite.class);     
				System.out.println("site.getBitmap(): " + site.getBitmap());
				i.putExtra("site", site);
				startActivity(i);
			}else{
				Intent i = new Intent(Map.this, InfoSite.class);     
				System.out.println("site.getBitmap(): " + site.getBitmap());
				i.putExtra("site", site);
				startActivity(i);
			}
			
			
		}
	}
	
	@Override
	public void onMapClick(LatLng punto) {
		
		if (markerSelect != null) {
			markerSelect.remove();
			markerSelect = null;
		}
	}
	
	 @Override
	public void onMapLongClick(LatLng punto) {
		
		Log.d(getLocalClassName(), "Evento OnMapClickListener.");
		
		if(markerSelect != null){
			markerSelect.remove();
			markerSelect = null;
		}
		
		Log.d(getLocalClassName(),"Creo marcador de posicion");
		Marker marker = view.getMap().addMarker(new MarkerOptions()
							.position(punto)
							.snippet(getResources().getString(R.string.create_site)));												
		markerSelect = marker;						
		marker.showInfoWindow();										
		
	}
	 
	//Para que el ActivityGroup reciba el evento onKeyDown.
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	@Override
	public View getInfoContents(Marker marker) {
				
		if (marker.getTitle() != null){
			Log.d(getLocalClassName(),"es un sitio");
			if(markerSelect != null){
				Log.d(getLocalClassName(),"VOY A ELIMINAR");
				markerSelect.remove();
				markerSelect = null;
			}
			View contents = this.getLayoutInflater().inflate(R.layout.site_window, null);
			
			contents = presenter.buildMarkerSite(contents, marker);
			
			return contents;
		}
		else{
			Log.d(getLocalClassName(),"es una pulsacion");
			
			View contents = this.getLayoutInflater().inflate(R.layout.site_window_create, null);
			contents = presenter.buildMarkerPulsation(contents, marker);
					
			return contents;
		}
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		
		return null;
	}
	
	private void setUpMap() {

		if (map == null) {
			SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			map = fragment.getMap();
		
			if (isGoogleMapsInstalled()) {
				if (map != null) {
					locationManager = (LocationManager) this
							.getSystemService(Context.LOCATION_SERVICE);
					// seteamos para que este el boton de buscar nuestra ubicacion activado
					map.setMyLocationEnabled(true);
					map.setOnMapLongClickListener(this);
					map.setOnMapClickListener(this);
			        map.setInfoWindowAdapter(this);
			        map.setOnInfoWindowClickListener(this);
					presenter.initLocationUpdates();
				}
			} else {
				AlertDialog dialog = presenter.createDialogInstallGoogleMaps();
				dialog.show();
			}
		}
	}

	@SuppressWarnings("unused")
	public boolean isGoogleMapsInstalled() {
		try {
			ApplicationInfo info = getPackageManager().getApplicationInfo(
					"com.google.android.apps.maps", 0);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

	public OnClickListener getGoogleMapsListener() {
		return new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("market://details?id=com.google.android.apps.maps"));
				startActivity(intent);

				// Finish the activity so they can't circumvent the check
				finish();
			}
		};
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
		    	presenter.makeMapSitesRequest();    	  
		    }	
			return true;
		case MENU_OPC2:
			session = Session.getActiveSession();
			session.closeAndClearTokenInformation();
			conf.setAccessTokenFB(null);
			Intent i = new Intent(Map.this, Login.class);    
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
	public class IMapView {

		private Map activity;

		public IMapView(Map activity) {
			this.activity = activity;
		}

		public Activity getActivity() {
			return activity;
		}

		public void setLocationManager(LocationManager locationManager2) {
			locationManager = locationManager2;
		}

		public LocationManager getLocationManager() {
			return locationManager;
		}

		public void setLocationListener(LocationListener locationListener2) {
			locationListener = locationListener2;
		}

		public LocationListener getlocationListener() {
			return locationListener;
		}

		public void setMap(GoogleMap map2) {
			map = map2;
		}

		public GoogleMap getMap() {
			return map;
		}

		public OnClickListener getGoogleMapsListener() {
			return getGoogleMapsListener();
		}
		
		public Resources getResources(){
			return activity.getResources();
		}
		public void setMarkerSelect(Marker marker){
			markerSelect=marker;
		}
		public Marker getMarkerSelect(){
			return markerSelect;
		}
		public Config getConf(){
			return conf;
		}
	}
}
