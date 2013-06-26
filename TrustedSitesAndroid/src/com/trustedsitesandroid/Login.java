package com.trustedsitesandroid;

import presenters.LoginPresenter;
import utils.Config;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Login  extends Activity{
	
	private static Config conf;
	
	private ILoginView view;
	private LoginPresenter presenter;
	
	private static final int MENU_OPC1 = 1;
			 
	/**variable que indica si hay un fragmento visible*/
	private boolean isResumed = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		view = new ILoginView(this);
		presenter = new LoginPresenter(view);
				
		/*creamos la instancia de UiLifecycleHelper para controlar la sesion*/
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		setContentView(R.layout.login);

		conf = new Config(Login.this);
		presenter.cleanConfFile();
		
		Log.d(getLocalClassName(), "todo montado");
				
	}
		
	/**Indicamos que hay un fragmento visible*/
	@Override
	public void onResume() {
		Log.i(getLocalClassName(), "entro en onResume");
	    super.onResume();
	    	  	    
	    Session session = Session.getActiveSession();
	    
	    Log.d(getLocalClassName(), "session= " + session);
	    Log.d(getLocalClassName(), "session.isClosed()=" + session.isClosed() + " session.isOpened()=" + session.isOpened());
	    Log.d(getLocalClassName(), "conf.getAccessTokenFB()=" + conf.getAccessTokenFB());
	    
	    /**
	     * Si hay sesion pero esta cerrada, volvemos a abrirla apartir del token
	     * que habíamos guardado y abrimos la aplicación
	     */
	    if (session!= null && !session.isOpened() && conf.getAccessTokenFB()!= null){
	    
	    	Log.i(getLocalClassName(), "Abrimos sesion a partir del token");
	    	AccessToken accessToken = AccessToken.createFromExistingAccessToken(conf.getAccessTokenFB(), session.getExpirationDate(),
	    			 null, AccessTokenSource.FACEBOOK_APPLICATION_NATIVE, session.getPermissions());
	    	
			session.open(accessToken, callback);		
			Log.i(getLocalClassName(), "session.isOpened()= " + session.isOpened());
			
	    }
	    /**
	     * Si hay una sesion abierta: si no tenemos el token actualizado -> lo actualizamos
	     * y abrimos la aplicacion  
	     */
	    else if (session != null && session.isOpened()) {
	    	
	    	if (conf.getAccessTokenFB()== null || conf.getAccessTokenFB()!=session.getAccessToken()){
	    		Log.i(getLocalClassName(), "Guardo el token: " + session.getAccessToken());
	    		conf.setAccessTokenFB(session.getAccessToken());
	    	}
    	    presenter.makeMeRequest(session); 
	    }
	    else{
	    	Log.i(getLocalClassName(), "NO HAY SESSION");

	    }
	    isResumed = true;
	}
	
	/**Indicamos que no hay ningun fragmento visible*/
	@Override
	public void onPause() {
		Log.i(getLocalClassName(), "entro en onPause");
	    super.onPause();
	    isResumed = false;
	}
		
	/** 
	 * Método que sera llamado cuando hay un cambio de estado de la sesion.
	 * Dependiendo del si la sesion esta abierta o no, mostramos una fragmento o otro.
	 */
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    // Only make changes if the activity is visible		
		Log.i(getLocalClassName(), "LA SESION A CAMBIADO: STATE.name" + state.name());
	    if (isResumed) {
	    	
	    	// If the session state is open:
	        if (state.isOpened()) {	   
	        	Log.i(getLocalClassName(), "ON SESSION STATE CHANGE: voy a autenticar!");
	        	presenter.makeMeRequest(session); 
	        } 
	        // If the session state is closed:
	        else if (state.isClosed()) {	           
	        	Log.i(getLocalClassName(), "ON SESSION STATE CHANGE: VOY A MOSTRAR LOGIN");	            
	        }
	    }
	}
		
	/**Usamos UiLifecycleHelper para hacer un seguimiento de la sesion y activar el listener*/
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	    	Log.i(getLocalClassName(), "entro en call");
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	/**Metodos utilizados por UiLifecycleHelper*/
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, MENU_OPC1, MENU_OPC1, getResources().getString(R.string.about)).setIcon(R.drawable.info);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_OPC1:
			Intent i = new Intent(Login.this, About.class);     	 			
			startActivity(i);
			return true;		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
		
	public class ILoginView {

		private Login activity;

		public ILoginView(Login activity) {
			this.activity = activity;
		}

		public Activity getActivity() {
			return activity;
		}
		
		public Resources getResources(){
			return activity.getResources();
		}
		public Config getConf() {
			return conf;
		}
		public void setConf(Config conf2) {
			conf = conf2;
		}
	}


}
