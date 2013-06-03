package com.trustedsitesandroid;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import utils.Config;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import de.mindpipe.android.logging.log4j.LogConfigurator;

import android.os.Bundle;
import android.os.Environment;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {

	private static final int LOGIN = 0;
//	private static final int APP = 1;
//	private static final int SETTINGS = 2;
	
	private static final int SETTINGS = 1;
	private static final int FRAGMENT_COUNT = SETTINGS +1;

	//private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
	
	/*variable que indica si hay un fragmento visible*/
	private boolean isResumed = false;
	
	/*variable para representar el menu para el fragmento de opciones*/
	private MenuItem settings;
	
	private Config conf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			    
		/*creamos la instancia de UiLifecycleHelper para controlar la sesion*/
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		conf = new Config(MainActivity.this);
		
			
//		FragmentManager fm = getSupportFragmentManager();
//	    fragments[LOGIN] = fm.findFragmentById(R.id.loginView);
//	    //fragments[APP] = fm.findFragmentById(R.id.tabHost);
//	    fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);

//	    /*	Ocultamos los fragmentos */
//	    FragmentTransaction transaction = fm.beginTransaction();
//	    for(int i = 0; i < fragments.length; i++) {
//	        transaction.hide(fragments[i]);
//	    }
//	    transaction.commit();
//	    
		
	}
	
//	/*metodo responsable de mostrar un fragmento y ocultar los demás*/
//	private void showFragment(int fragmentIndex, boolean addToBackStack) {
//	    FragmentManager fm = getSupportFragmentManager();
//	    FragmentTransaction transaction = fm.beginTransaction();
//	    for (int i = 0; i < fragments.length; i++) {
//	        if (i == fragmentIndex) {
//	            transaction.show(fragments[i]);
//	        } else {
//	            transaction.hide(fragments[i]);
//	        }
//	    }
//	    if (addToBackStack) {
//	        transaction.addToBackStack(null);
//	    }
//	    transaction.commit();
	
//	}
	
	/*Indicamos que hay un fragmento visible*/
	@Override
	public void onResume() {
	    super.onResume();
	    isResumed = true;
	}
	
	/*Indicamos que no hay ningun fragmento visible*/
	@Override
	public void onPause() {
	    super.onPause();
	    isResumed = false;
	}
	
	/* 
	 * Método que sera llamado cuando hay un cambio de estado de la sesion.
	 * Dependiendo del si la sesion esta abierta o no, mostramos una fragmento o otro.
	 */
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    // Only make changes if the activity is visible
		
		System.out.println("LA SESION A CAMBIADO: STATE.name" + state.name());
	    if (isResumed) {
	        FragmentManager manager = getSupportFragmentManager();	        
	        // Get the number of entries in the back stack
	        int backStackSize = manager.getBackStackEntryCount();
	        // Clear the back stack
	        for (int i = 0; i < backStackSize; i++) {
	            manager.popBackStack();	
	        }
	        if (state.isOpened()) {
	            // If the session state is open:
	            // Show the authenticated fragment
	            
	        	//showFragment(APP, false);
	        	System.out.println("ON SESSION STATE CHANGE: VOY A MOSTRAR APP");
	            Intent i = new Intent(this, Tabs.class);
				startActivity(i);
				finish();
	        } else if (state.isClosed()) {
	            // If the session state is closed:
	            // Show the login fragment
	        	System.out.println("ON SESSION STATE CHANGE: VOY A MOSTRAR LOGIN");
	            //showFragment(LOGIN, false);
	        	Intent i = new Intent(this,LoginView.class);
				startActivity(i);
				finish();
	            
	        }
	    }
	}
	
//	/* Para asegurarnos de que mostramos el fragmento correcto */
//	@Override
//	protected void onResumeFragments() {
//	    super.onResumeFragments();
//	    Session session = Session.getActiveSession();
//
//	    System.out.println("ON RESUME FRAGMENTS!!!!");
//	    System.out.println("ssesion is open: " + session.isOpened());
//	    if (session != null && session.isOpened()) {
//	        // if the session is already open,
//	        // try to show the selection fragment
//	        //showFragment(APP, false);
//	    	System.out.println("ON RESUMEFRAGMENTS: VOY A MOSTRAR APP");
//	        Intent i = new Intent(this, Tabs.class);
//			startActivity(i);
//			finish();
//	    } else {
//	        // otherwise present the splash screen
//	        // and ask the person to login.
//	    	System.out.println("ON SESSION STATE CHANGE: VOY A MOSTRAR LOGIN");
//	        showFragment(LOGIN, false);
//	    }
//	}
	
	/*Usamos UiLifecycleHelper para hacer un seguimiento de la sesion y activar el listener*/
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, 
	            SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	/*Metodos utilizados por UiLifecycleHelper*/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	/*Metodo para preparar el menu de opciones*/
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    // only add the menu when the selection fragment is showing
//	    if (fragments[APP].isVisible()) {
//	        if (menu.size() == 0) {
//	            settings = menu.add(R.string.settings);
//	        }
//	        return true;
//	    } else {
//	        menu.clear();
//	        settings = null;
//	    }
	    return false;
	}
	
	/*Metodo para mostrar el fragmento de opciones cuando clicamos en la opciones settings*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.equals(settings)) {
	    	System.out.println("VOY A MOSTRAR SETTINGS");
	        //showFragment(SETTINGS, true);
	        return true;
	    }
	    return false;
	}

}
