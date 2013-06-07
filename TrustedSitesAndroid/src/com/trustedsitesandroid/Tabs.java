package com.trustedsitesandroid;

import utils.Config;
import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.TabHost;


@SuppressWarnings("deprecation")
public class Tabs extends TabActivity {

	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);

        
        TabHost tabHost = getTabHost();
        	
        /*Obtenemos los recursos*/
        Resources res = getResources();
        
        String my_sites = getResources().getString(R.string.my_sites);
        String friends = getResources().getString(R.string.friends);
        String sites = getResources().getString(R.string.sites);
        String map = getResources().getString(R.string.map);
        /*Se utilizara para abrir cada pestaña*/
        Intent intent = new Intent().setClass(this, MySites.class);
        /*Recurso para propiedades de ventana: se configura la pestaña con sus propiedades*/
        TabHost.TabSpec spec = tabHost.newTabSpec(my_sites).setIndicator(my_sites, res.getDrawable(R.drawable.ic_my_sites)).setContent(intent);
        /*se carga la pestaña en el contenedor TabHost*/    
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, FriendsList.class);
        spec = tabHost.newTabSpec(friends).setIndicator(friends, res.getDrawable(R.drawable.ic_friends)).setContent(intent);       
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, SitesList.class);
        spec = tabHost.newTabSpec(sites).setIndicator(sites, res.getDrawable(R.drawable.ic_sites)).setContent(intent);       
        tabHost.addTab(spec);      
        intent = new Intent().setClass(this, Map.class);
        spec = tabHost.newTabSpec(map).setIndicator(map, res.getDrawable(R.drawable.ic_map)).setContent(intent);       
        tabHost.addTab(spec);
        
        tabHost.setCurrentTab(1);
    }
    

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
//    /** Called when the user clicks the Send button */
//    public void sendMessage(View view) {
//        // Do something in response to button
//    	Intent intent = new Intent(this, DisplayMessageActivity.class);
//    	EditText editText = (EditText) findViewById(R.id.edit_message);
//    	String message = editText.getText().toString();
//    	intent.putExtra(EXTRA_MESSAGE, message);
//    	startActivity(intent);
//    }
}
