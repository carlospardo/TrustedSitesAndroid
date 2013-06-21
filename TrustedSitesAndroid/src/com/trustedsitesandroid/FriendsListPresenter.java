package com.trustedsitesandroid;

import java.util.ArrayList;
import java.util.List;

import models.User;
import utils.ApiHelpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.trustedsitesandroid.FriendsList.IFriendsListView;

public class FriendsListPresenter {

	private IFriendsListView view;
	
	private ProgressDialog dialog;	
	
	public FriendsListPresenter(IFriendsListView view){
		super();
		this.view = view;				
	}
	
	public ProgressDialog createProgressDialog(){
		dialog = new ProgressDialog(view.getActivity());		
		dialog.setMessage(view.getResources().getString(R.string.accessing));
		dialog.setTitle(view.getResources().getString(R.string.progress));
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);	
		return dialog;
	}

	public Dialog createDialogError(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(view.getActivity());

		builder.setTitle(view.getResources().getString(R.string.error));
		builder.setMessage(message);
		builder.setPositiveButton(view.getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return builder.create();
	}
	
	/** Metodo para solicitar los amigos de Facebook*/
	public void makeMyFriendsRequest(final Session session) {
	    
		dialog = createProgressDialog();
    	dialog.setProgress(0);
        dialog.setMax(100);
        dialog.show();
        
     // Make an API call to get user data and define a new callback to handle the response.
		Request request = Request.newMyFriendsRequest(session, new Request.GraphUserListCallback() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onCompleted(List<GraphUser> listUsers, Response response) {
				
				 // If the response is successful	     
	            if (session == Session.getActiveSession()) {	            
	                if (listUsers!= null) {	       
	                	
	                	listUsers.size();
	                	Log.d("FriendsListPresenter", "listUsers.size(): " + listUsers.size());
	                	List<String> listUserIds = new ArrayList<String>();
	                	for (GraphUser u : listUsers){
	                		listUserIds.add(u.getId());
	                	}
	                	dialog.setProgress(50);
	                	new GetFriendsList().execute(listUserIds); 	
	                 	                              	   	                    
	                }else{	
	                	dialog.dismiss();
	                	Log.e("FriendsListPresenter", "listUsers es null!");
	                	Dialog dialogo=createDialogError(view.getResources().getString(R.string.userFb_error_info));       		
	                	dialogo.show();
	                }
	            }
	            if (response.getError() != null) {
	            	dialog.dismiss();
	            	Log.e("FriendsListPresenter", "Error al conectar con FB: " + response.getError().getErrorMessage());
	            	Dialog dialogo=createDialogError(view.getResources().getString(R.string.userFb_error_info));       		
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
	private class GetFriendsList extends AsyncTask<List<String>, Float, Boolean>{
				
		List<User> listFriends;
        
		protected void onPreExecute() {

         }

         protected Boolean doInBackground(List<String>... listUserIds) {        	 
        	 try {        		
        		 Log.i("FriendsListPresenter", "Cargo lista Amigos");
        		 listFriends = ApiHelpers.getListFriends(listUserIds[0], view.getConf().getIdFacebook());
								
        		 Log.i("FriendsListPresenter", "carga hecha");
        		 Log.d("FriendsListPresenter", "listFriends.size(): " + listFriends.size());
				 				
        		// publishProgress(250/250f);
        		 dialog.setProgress(100);
        		 return true;
			} catch (Exception e) {			
				Log.e("FriendsListPresenter", "ERROR", e);
				return false;
			}       
         }

         protected void onProgressUpdate (Float... valores) {
         }

         protected void onPostExecute(Boolean correct) {
             if(correct){           	 
            	 Log.i("FriendsListPresenter", "voy a cargar la lista");
            	 view.setListFriends(listFriends);
            	 dialog.dismiss();	        	 	        	 
             }else{
            	 dialog.dismiss();
            	 Dialog dialogo= createDialogError(view.getResources().getString(R.string.server_error_info));       		            		
            	 dialogo.show();
             }                  			             
         }  
	}
}
