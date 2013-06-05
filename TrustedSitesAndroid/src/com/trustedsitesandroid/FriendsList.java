package com.trustedsitesandroid;

import java.util.ArrayList;
import java.util.List;

import models.User;

import utils.ApiHelpers;
import utils.Config;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class FriendsList extends Activity {

	private static Config conf;
	
	private ProgressDialog dialog;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_list);
		
//		conf = new Config(FriendsList.this);	
//        Bundle bundle = getIntent().getExtras();
//        conf.setAccessTokenFB(bundle.getString("acessTokenFB"));
        
        dialog = new ProgressDialog(this);		
		dialog.setMessage(getResources().getString(R.string.accessing));
		dialog.setTitle(getResources().getString(R.string.progress));
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(false);
		
	}
	
	@Override
	public void onResume() {
		Log.i(getLocalClassName(), "entro en onResume");
	    super.onResume();
	    	  	    
	    Session session = Session.getActiveSession();
	    
	    Log.i(getLocalClassName(), "session= " + session);
	    Log.i(getLocalClassName(), "session.isClosed()=" + session.isClosed() + " session.isOpened()=" + session.isOpened());
	   
	    
	    /**
	     * Si hay sesion pero esta cerrada, volvemos a a LoginView para que vuelva abrir la 
	     * sesion.
	     */
	    if (session!= null && !session.isOpened()){
	    
	    	Log.i(getLocalClassName(), "La session esta cerrada");
       	 	Intent i = new Intent(FriendsList.this, LoginView.class);     	 
       	 	startActivity(i);
         	finish();			
	    }
	    /**
	     * Si hay una sesion abierta cargamos lista de amigos  
	     */
	    else if (session != null && session.isOpened()) {
	    	Log.i(getLocalClassName(), "La session es correcta");
    	    this.makeMyFriendsRequest(session);    	  
	    }
	    else{
	    	Log.i(getLocalClassName(), "NO HAY SESSION");
	    	Intent i = new Intent(FriendsList.this, LoginView.class);     	 
       	 	startActivity(i);
         	finish();
	    }
	}
	
	/** Metodo para solicitar los amigos de Facebook*/
	@SuppressWarnings("unused")
	private void makeMyFriendsRequest(final Session session) {
	    // Make an API call to get user data and define a new callback to handle the response.
	    Request request = Request.newMyFriendsRequest(session, new Request.GraphUserListCallback() {
			
			@Override
			public void onCompleted(List<GraphUser> listUsers, Response response) {
				
				 // If the response is successful	     
	            if (session == Session.getActiveSession()) {	            
	                if (listUsers!= null) {	       
	                	
	                	listUsers.size();
	                	Log.i(getLocalClassName(), "listUsers.size(): " + listUsers.size());
	                	List<String> listUserIds = new ArrayList<String>();
	                	for (GraphUser u : listUsers){
	                		listUserIds.add(u.getId());
	                	}
	                	
	                	if(listUsers.size()!=0){
		                	Log.i(getLocalClassName(), "listUsers.get(0).getName();: " + listUsers.get(0).getName());
		                	Log.i(getLocalClassName(), "listUsers.get(0).getId(): " + listUsers.get(0).getId());
	                	}
	                	listUserIds = new ArrayList<String>();
	                	listUserIds.add("123");
	                	listUserIds.add("544794145");
	                	new GetFriendsList().execute(listUserIds); 	
	                 	                              	   	                    
	                }else{	                
	                	Log.e(getLocalClassName(), "listUsers es null!");
	                	Dialog dialogo=createDialogError(getResources().getString(R.string.userFb_error_info));       		
	                	dialogo.show();
	                }
	            }
	            if (response.getError() != null) {
	            	Log.e(getLocalClassName(), "Error al conectar con FB: " + response.getError().getErrorMessage());
	            	Dialog dialogo=createDialogError(getResources().getString(R.string.userFb_error_info));       		
                	dialogo.show();
	            }
				
			}
		}); 
	    request.executeAsync();
	}
	
	/**
	 * Params:  Datos que pasaremos al comenzar la tarea
	 * Progress: Parámetros que necesitaremos para actualizar la UI.
	 * Result: Dato que devolveremos una vez terminada la tarea.
	 */
	private class GetFriendsList extends AsyncTask<List<String>, Float, Integer>{
		
		//private Handler mHandler = new Handler();
		private boolean correct = false;
		
        protected void onPreExecute() {
            dialog.setProgress(0);
            dialog.setMax(100);
                dialog.show(); //Mostramos el diálogo antes de comenzar
         }

         protected Integer doInBackground(List<String>... listUserIds) {
        	 
        	 try {
        		Log.i(getLocalClassName(), "Cargo lista Amigos");
        		List<User> listFriends = ApiHelpers.getListFriends(listUserIds[0], "1523497691");
				
				Log.i(getLocalClassName(), "carga hecha");
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
            	 Log.i(getLocalClassName(), "voy a cargar la lista");
	        	 dialog.dismiss();
	        	 
	        	 
             }else{
            	 dialog.dismiss();
            	 Dialog dialogo=createDialogError(getResources().getString(R.string.server_error_info));       		
            	 dialogo.show();
             }                  			             
         }  
	}
	
	private Dialog createDialogError(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(getResources().getString(R.string.error));
		builder.setMessage(message);
		builder.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return builder.create();
	}
	
}
