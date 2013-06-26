package com.trustedsitesandroid;

import presenters.InfoSitePresenter;
import models.Site;
import utils.Config;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoSite extends Activity {

	private Config conf;
	
	private IInfoSiteView view;
	private InfoSitePresenter presenter;
	
	private ImageView photo;
	private TextView nameSite;
	private TextView owner;
	private TextView latitude;
	private TextView longitude;
	private TextView infoSite;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_site);
		
		conf = new Config(InfoSite.this);
		
		view = new IInfoSiteView(this);
		presenter = new InfoSitePresenter(view);

		nameSite = (TextView) findViewById(R.id.nameSite);	
		photo = (ImageView) findViewById(R.id.photoSite);
		owner = (TextView) findViewById(R.id.owner);	
		latitude = (TextView) findViewById(R.id.latitude);
		longitude = (TextView) findViewById(R.id.longitude);
		infoSite = (TextView) findViewById(R.id.infoSite);	
		
		presenter.init();
		
		Button returnMap = (Button) findViewById(R.id.buttonReturn);

		returnMap.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	/**
	 * Clase Vista
	 */
	public class IInfoSiteView {

		private InfoSite activity;

		public IInfoSiteView(InfoSite activity) {
			this.activity = activity;
		}

		public Activity getActivity() {
			return activity;
		}
		
		public Resources getResources(){
			return activity.getResources();
		}
		public void setNameSite(String name){
			nameSite.setText(name);
		}
		public void setPhoto(Bitmap photo2){
			photo.setImageBitmap(photo2);
		}
		public void setPhoto(int photo2){
			photo.setImageResource(photo2);
		}
		public void setOwner(String name){
			owner.setText(name);
		}
		public TextView getNameSite(){
			return nameSite;
		}
		public void setLatitude(String lat){
			latitude.setText(lat);
		}
		public TextView getLatitude(){
			return latitude;
		}
		public void setLongitude(String lng){
			longitude.setText(lng);
		}
		public TextView getLongitude(){
			return longitude;
		}
		public void setInfoSite(String info){
			infoSite.setText(info);
		}
		public TextView getInfoSite(){
			return infoSite;
		}
		public  Context getBaseContext(){
			return getBaseContext();
		}
		public Config getConf(){
			return conf;
		}
	}
}
