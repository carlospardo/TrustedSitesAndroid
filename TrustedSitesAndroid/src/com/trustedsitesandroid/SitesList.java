package com.trustedsitesandroid;

import java.util.ArrayList;
import java.util.List;

import adapters.SitesArrayAdapter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

public class SitesList extends ListActivity {
    
	private ListView listaSites;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sites_list);
		      
	}
	public void onResume(){
		super.onResume();
	
		List<String> miLista = new ArrayList<String>();
		miLista.add("Due Torri");
		miLista.add("Don Vito");
		miLista.add("Casa");
		miLista.add("Unibo");
		miLista.add("Cilta");
		miLista.add("Bar");
		miLista.add("Pub");
		SitesArrayAdapter sitesAdapter= new SitesArrayAdapter(SitesList.this, miLista);	
		listaSites = getListView();		
		listaSites.setAdapter(sitesAdapter);
		listaSites.setTextFilterEnabled(true);
	

	}

}
