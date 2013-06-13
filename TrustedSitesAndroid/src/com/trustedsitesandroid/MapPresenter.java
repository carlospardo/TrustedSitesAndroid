package com.trustedsitesandroid;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import utils.ApiHelpers;

import models.Site;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trustedsitesandroid.Map.IMapView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MapPresenter {

	private IMapView view;

	List<Site> listSites = new ArrayList<Site>();

	private ProgressDialog dialog;

	public MapPresenter(IMapView view) {
		super();
		view.setLocationListener(new MyLocationListener());
		this.view = view;
	}

	public void initLocationUpdates() {

		LocationProvider provider = view.getLocationManager().getProvider(
				view.getLocationManager().getBestProvider(createBestCriteria(),false));
		provider = view.getLocationManager().getProvider(view.getLocationManager().NETWORK_PROVIDER);

		Location firstLocation = view.getLocationManager().getLastKnownLocation(provider.getName());
		if (firstLocation == null) {
			Log.d("MapPresenter", "FirstLocation con " + provider.getName() + " NO encontrada");
			firstLocation = view.getLocationManager().getLastKnownLocation(view.getLocationManager().NETWORK_PROVIDER);
			
			if (firstLocation == null) {
				Log.d("MapPresenter", "FirstLocation con NETWORK_PROVIDER NO encontrada");
				
			} else {				
				provider = view.getLocationManager().getProvider(view.getLocationManager().PASSIVE_PROVIDER);
				Log.d("MapPresenter", "FirstLocation con " + provider.getName()+ " encontrada. Lo seleccionamos");
				view.getLocationManager().requestLocationUpdates(provider.getName(), 100, 1, view.getlocationListener());
				
				float lat = (float) (firstLocation.getLatitude());
				float lng = (float) (firstLocation.getLongitude());
				LatLng latLng = new LatLng(lat, lng);
				view.getMap().animateCamera(CameraUpdateFactory.newLatLng(latLng));
			}
		} else {
			Log.d("MapPresenter", "FirstLocation con " + provider.getName()+ " encontrada. Lo seleccionamos");
			view.getLocationManager().requestLocationUpdates(provider.getName(), 100, 1, view.getlocationListener());
			
			float lat = (float) (firstLocation.getLatitude());
			float lng = (float) (firstLocation.getLongitude());
			LatLng latLng = new LatLng(lat, lng);
			view.getMap().animateCamera(CameraUpdateFactory.newLatLng(latLng));
		}
	}

	public void makeMapSitesRequest() {

		view.getMap().clear();
		dialog = createProgressDialog();
		dialog.setProgress(0);
		dialog.setMax(100);
		dialog.show();

		new GetSites().execute(view.getConf().getListSitesIds());
	}

	public AlertDialog createDialogInstallGoogleMaps() {
		Builder builder = new AlertDialog.Builder(view.getActivity());
		builder.setMessage(view.getResources().getString(R.string.install_google_maps));
		builder.setCancelable(false);
		builder.setPositiveButton(view.getResources().getString(R.string.install), view.getGoogleMapsListener());
		AlertDialog dialog = builder.create();
		return dialog;
	}

	public ProgressDialog createProgressDialog() {
		dialog = new ProgressDialog(view.getActivity());
		dialog.setMessage(view.getResources().getString(R.string.accessing));
		dialog.setTitle(view.getResources().getString(R.string.progress));
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		return dialog;
	}

	public Dialog createDialogError(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(view.getActivity());
		builder.setTitle(view.getResources().getString(R.string.error));
		builder.setMessage(message);
		builder.setPositiveButton(view.getResources()
				.getString(R.string.accept),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		return builder.create();
	}

	public View buildMarkerSite(View contents, Marker marker){
		
		String title = marker.getTitle();
		TextView txtTitle = ((TextView) contents.findViewById(R.id.txtInfoWindowTitle));

		// Spannable string allows us to edit the formatting of the text.
		SpannableString titleText = new SpannableString(title);
		titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
		txtTitle.setText(titleText);

		TextView txtType = ((TextView) contents.findViewById(R.id.txtInfoWindowEventType));
		txtType.setText(marker.getSnippet());

		boolean encontrado = false;
		for (int i = 0; i < listSites.size() && !encontrado; i++) {
			if (listSites.get(i).getIdMarker().equals(marker.getId())) {
				ImageView imageView = (ImageView) contents.findViewById(R.id.ivInfoWindowMain);					
				imageView.setImageBitmap(listSites.get(i).getBitmap());
				encontrado = true;
			}
		}
		return contents;	
	}
	public View buildMarkerPulsation(View contents, Marker marker){
			
		TextView txtType = ((TextView) contents.findViewById(R.id.txtInfoWindowEventType));
		txtType.setText(marker.getSnippet());
		
		TextView txtTitle = ((TextView) contents.findViewById(R.id.txtInfoWindowTitle));
		txtTitle.setText(null);
		
		ImageView imageView = (ImageView) contents.findViewById(R.id.ivInfoWindowMain);				
		imageView.setImageBitmap(null);
		return contents;		
	}
	
	private void addMarker(Site site) {
		
		LatLng latLng = new LatLng(Float.parseFloat(site.getPositionX()),
				Float.parseFloat(site.getPositionY()));
		
		Marker marker = view.getMap().addMarker(new MarkerOptions()
			.position(latLng)
			.title(site.getName())
			.snippet(site.getInfo() + "\n" + view.getResources().getString(R.string.owner)
					+ ": "+ site.getNameOwner())
			.icon(BitmapDescriptorFactory
			.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		
		Log.d("MapPresenter","idMarker: " + marker.getId());
		site.setIdMarker(marker.getId());

	}

	public static Criteria createBestCriteria() {

		Criteria c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_COARSE);
		c.setAltitudeRequired(false);
		c.setBearingRequired(false);
		c.setSpeedRequired(false);
		c.setCostAllowed(true);
		c.setPowerRequirement(Criteria.POWER_HIGH);
		return c;
	}

	/**
	 * Params: Datos que pasaremos al comenzar la tarea Progress: Parámetros que
	 * necesitaremos para actualizar la UI. Result: Dato que devolveremos una
	 * vez terminada la tarea.
	 */
	private class GetSites extends AsyncTask<List<String>, Float, Integer> {

		private boolean correct = false;

		protected void onPreExecute() {

		}

		protected Integer doInBackground(List<String>... listSitesIds) {
			try {
				Log.i("MapPresenter", "Cargo lista mis Sitios");
				listSites = ApiHelpers.getSitesList(listSitesIds[0]);

				Log.i("MapPresenter", "carga hecha");
				Log.d("MapPresenter", "listSites.size(): " + listSites.size());

				dialog.setProgress(50);
				correct = true;
			} catch (Exception e) {
				Log.e("MySitesPresenter", "ERROR", e);
				correct = false;
			}
			return 250;
		}

		protected void onProgressUpdate(Float... valores) {
		}

		protected void onPostExecute(Integer bytes) {
			if (correct) {
				Log.i("MapPresenter", "voy a cargar la lista");
				if (listSites != null) {
					new GetImageSites().execute(listSites);
				}
				dialog.setProgress(100);
				dialog.dismiss();
			} else {
				dialog.dismiss();
				Dialog dialogo = createDialogError(view.getResources().getString(R.string.server_error_info));
				dialogo.show();
			}
		}
	}

	private class GetImageSites extends AsyncTask<List<Site>, Float, Boolean> {

		protected void onPreExecute() {
		}

		protected Boolean doInBackground(List<Site>... listSites) {
			try {
				for (Site s : listSites[0]) {
					String urldisplay = s.getUrlPhoto();				
					Bitmap bitmap;
					InputStream in = new java.net.URL(urldisplay).openStream();
					bitmap = BitmapFactory.decodeStream(in);
					s.setBitmap(bitmap);
				}
				return true;

			} catch (Exception e) {
				Log.e("MySitesPresenter", "ERROR", e);
				return false;
			}
		}

		protected void onProgressUpdate(Float... valores) {
		}

		protected void onPostExecute(Boolean correct) {
			if (correct) {
				for (Site s : listSites) {
					addMarker(s);
				}
			}
		}
	}

	public class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			Log.d("MapPresenter","onLocationChanged");			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		public void onProviderEnabled(String provider) {
			Log.d("MapPresenter", "Esta habilitado el proveedor " + provider);
			Toast.makeText(view.getActivity(), view.getResources().getString(R.string.provider_enable) 
					+ provider, Toast.LENGTH_SHORT).show();
		}

		public void onProviderDisabled(String provider) {
			Log.d("MapPresenter", "Esta deshabilitado el proveedor " + provider);
			Toast.makeText(view.getActivity(), view.getResources().getString(R.string.provider_disable) 
					+ provider, Toast.LENGTH_SHORT).show();
		}
	}
}
