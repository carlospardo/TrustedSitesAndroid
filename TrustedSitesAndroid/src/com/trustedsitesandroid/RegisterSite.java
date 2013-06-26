package com.trustedsitesandroid;

import presenters.RegisterSitePresenter;
import utils.Config;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterSite extends Activity {

	private Config conf;
	
	private IRegisterSiteView view;
	private RegisterSitePresenter presenter;
	
	private EditText nameSite;
	private EditText latitude;
	private EditText longitude;
	private EditText infoSite;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_site);
		
		conf = new Config(RegisterSite.this);
		
		view = new IRegisterSiteView(this);
		presenter = new RegisterSitePresenter(view);

		nameSite = (EditText) findViewById(R.id.nameSite);		
		latitude = (EditText) findViewById(R.id.latitude);
		longitude = (EditText) findViewById(R.id.longitude);
		infoSite = (EditText) findViewById(R.id.infoSite);	
		
		presenter.init();
		
		Button returnMap = (Button) findViewById(R.id.buttonReturn);

		returnMap.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
	
		Button createSite = (Button) findViewById(R.id.buttonCreateSite);

		createSite.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {

				presenter.createSite();
			}
		});
	}
	/**
	 * Clase Vista
	 */
	public class IRegisterSiteView {

		private RegisterSite activity;

		public IRegisterSiteView(RegisterSite activity) {
			this.activity = activity;
		}

		public Activity getActivity() {
			return activity;
		}
		
		public Resources getResources(){
			return activity.getResources();
		}
		
		public EditText getNameSite(){
			return nameSite;
		}
		public void setLatitude(String lat){
			latitude.setText(lat);
		}
		public EditText getLatitude(){
			return latitude;
		}
		public void setLongitude(String lng){
			longitude.setText(lng);
		}
		public EditText getLongitude(){
			return longitude;
		}
		public EditText getInfoSite(){
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