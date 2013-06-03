package com.trustedsitesandroid;

import models.User;
import utils.ApiHelpers;
import utils.Config;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class LoginView  extends Activity{

	
	private Config conf;
	
	private ProgressDialog dialog;
	
	//private UserAndroid myUser;
	 
	/*variable que indica si hay un fragmento visible*/
	private boolean isResumed = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		/*creamos la instancia de UiLifecycleHelper para controlar la sesion*/
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		setContentView(R.layout.login_view);

		conf = new Config(LoginView.this);		
		
		dialog = new ProgressDialog(this);
		dialog.setMessage("Descargando...");
		dialog.setTitle("Progreso");
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(false);
		
		Log.i(getLocalClassName(), "todo montado");
		
	}
	
	/*Indicamos que hay un fragmento visible*/
	@Override
	public void onResume() {
		Log.i(getLocalClassName(), "entro en onResume");
	    super.onResume();
	    	  	    
	    Session session = Session.getActiveSession();
	    
	    Log.i(getLocalClassName(), "session= " + session);
	    Log.i(getLocalClassName(), "session.isClosed()=" + session.isClosed() + " session.isOpened()=" + session.isOpened());
	    Log.i(getLocalClassName(), "conf.getAccessTokenFB()=" + conf.getAccessTokenFB());
	    
	    if (session!= null && !session.isOpened() && conf.getAccessTokenFB()!= null){
	    
	    	Log.i(getLocalClassName(), "Abrimos sesion a partir del token");
	    	AccessToken accessToken = AccessToken.createFromExistingAccessToken(conf.getAccessTokenFB(), session.getExpirationDate(),
	    			 null, AccessTokenSource.FACEBOOK_APPLICATION_NATIVE, session.getPermissions());
	    	
			session.open(accessToken, callback);		
			 Log.i(getLocalClassName(), "session.isOpened()= " + session.isOpened());
			
	    }
	        
	    else if (session != null && session.isOpened()) {
	    	
	    	if (conf.getAccessTokenFB()== null && conf.getAccessTokenFB()!=session.getAccessToken()){
	    		Log.i(getLocalClassName(), "Guardo el token: " + session.getAccessToken());
	    		conf.setAccessTokenFB(session.getAccessToken());
	    	}
    	    this.makeMeRequest(session);   	    	  
    	    
//    	    if (myUser != null){
//    	    Log.i(getLocalClassName(), "ON RESUME: VOY A MOSTRAR APP");   	    
//    	    new Register().execute(myUser); 
//    	    }else{
//    	    	Log.e(getLocalClassName(), "myUser es null!");
//    	    }
	    }
	    else{
	    	Log.i(getLocalClassName(), "NO HAY SESSION");

	    }
	    isResumed = true;
	}
	
	/*Indicamos que no hay ningun fragmento visible*/
	@Override
	public void onPause() {
		Log.i(getLocalClassName(), "entro en onPause");
	    super.onPause();
	    isResumed = false;
	}
	
	/* Metodo para solicitar los datos del usuario*/
	@SuppressWarnings("unused")
	private void makeMeRequest(final Session session) {
	    // Make an API call to get user data and define a 
	    // new callback to handle the response.
	    Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
	        @Override
	        public void onCompleted(GraphUser userFb, Response response) {
	            // If the response is successful	     
	            if (session == Session.getActiveSession()) {	            
	                if (userFb != null) {	                	
	                    // Set the Textview's text to the user's name.
	                    Log.i(getLocalClassName(), "userFb.getName()): " + userFb.getName());	           
	                    String imageURL = "http://graph.facebook.com/"+userFb.getId()+"/picture?type=square";
                   	                    
	                    User myUser = new User(userFb.getId(), userFb.getName(), imageURL, "444.45", "222.23");
	                    
	                    
	                    if (myUser != null){
	                	    Log.i(getLocalClassName(), "ON RESUME: VOY A MOSTRAR APP");   	    	                	  
	                	    new Register().execute(myUser); 	                	   
	                    }else{	                	    
	                    	Log.e(getLocalClassName(), "myUser es null!");	                	    	                	   
	                    }
	                    
	                }else{
	                	session.closeAndClearTokenInformation();
	                	Log.e(getLocalClassName(), "userFb es null!");
	                }
	            }
	            if (response.getError() != null) {
	            	Log.i(getLocalClassName(), "erorr");
	                // Handle errors, will do so later.
	            }
	        }
	    });
	    request.executeAsync();
	} 
	
	/* 
	 * Método que sera llamado cuando hay un cambio de estado de la sesion.
	 * Dependiendo del si la sesion esta abierta o no, mostramos una fragmento o otro.
	 */
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    // Only make changes if the activity is visible
		
		Log.i(getLocalClassName(), "LA SESION A CAMBIADO: STATE.name" + state.name());
	    if (isResumed) {
	    	
	    	// If the session state is open:
	        if (state.isOpened()) {
	            	        	
	        	this.makeMeRequest(session);	        	 	        	 
//				if (myUser != null) {
//					Log.i(getLocalClassName(), "ON RESUME: VOY A MOSTRAR APP");
//					new Register().execute(myUser);
//				} else {			
//					Log.e(getLocalClassName(), "myUser es null!");
//				}

	        } 
	        // If the session state is closed:
	        else if (state.isClosed()) {	           
	        	Log.i(getLocalClassName(), "ON SESSION STATE CHANGE: VOY A MOSTRAR LOGIN");	            
	        }
	    }
	}
	
	
	/*Usamos UiLifecycleHelper para hacer un seguimiento de la sesion y activar el listener*/
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, 
	            SessionState state, Exception exception) {
	    	Log.i(getLocalClassName(), "entro en call");
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
	
	/*
	 * Params:  Datos que pasaremos al comenzar la tarea
	 * Progress: Parámetros que necesitaremos para actualizar la UI.
	 * Result: Dato que devolveremos una vez terminada la tarea.
	 */
	private class Register extends AsyncTask<User, Float, Integer>{
		
		//private Handler mHandler = new Handler();
		private boolean correct = false;
		
        protected void onPreExecute() {
            dialog.setProgress(0);
            dialog.setMax(100);
                dialog.show(); //Mostramos el diálogo antes de comenzar
         }

         protected Integer doInBackground(User... user) {
            /**
             * Simularemos que descargamos un fichero
             * mediante un sleep
             */
        	 
        	 try {
        		Log.i(getLocalClassName(), "hago el registro");
				ApiHelpers.register(user[0]);
				Log.i(getLocalClassName(), "registro hecho");
				 publishProgress(250/250f);
				 correct = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(getLocalClassName(), "ERROR", e);
				correct = false;
			}
        	 Log.i(getLocalClassName(), "250!"); 
             return 250;
         }

         protected void onProgressUpdate (Float... valores) {
             int p = Math.round(100*valores[0]);
             dialog.setProgress(p);
         }

         protected void onPostExecute(Integer bytes) {
             if(correct){
            	 Log.i(getLocalClassName(), "voy a cerrar");
	        	 dialog.dismiss();
	        	 Intent i = new Intent(LoginView.this, Tabs.class);
	          	 startActivity(i);
	          	 finish();
             }
                    			             
         }
   }


}
