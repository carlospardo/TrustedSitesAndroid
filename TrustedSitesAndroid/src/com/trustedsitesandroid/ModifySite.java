package com.trustedsitesandroid;

import presenters.ModifySitePresenter;
import utils.Config;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ModifySite extends Activity {

	private Config conf;
	
	private IModifySiteView view;
	private ModifySitePresenter presenter;
	
	private EditText nameSite;
	private EditText latitude;
	private EditText longitude;
	private EditText infoSite;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_site);
		
		conf = new Config(ModifySite.this);
		
		view = new IModifySiteView(this);
		presenter = new ModifySitePresenter(view);

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
		
		Button removeSite = (Button) findViewById(R.id.buttonRemoveSite);

		removeSite.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				presenter.createRemoveDialog();
			}
		});
	
		Button updateSite = (Button) findViewById(R.id.buttonUpdateSite);

		updateSite.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {

				presenter.updateSite();
			}
		});
	}
	/**
	 * Clase Vista
	 */
	public class IModifySiteView {

		private ModifySite activity;

		public IModifySiteView(ModifySite activity) {
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
		public void setInfoSite(String info){
			infoSite.setText(info);
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