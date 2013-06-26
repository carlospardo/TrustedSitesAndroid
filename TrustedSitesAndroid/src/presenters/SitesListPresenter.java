package presenters;

import java.util.ArrayList;
import java.util.List;

import models.Site;

import utils.ApiHelpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.trustedsitesandroid.R;
import com.trustedsitesandroid.SitesList;
import com.trustedsitesandroid.R.string;
import com.trustedsitesandroid.SitesList.ISitesListView;

public class SitesListPresenter {

	private ISitesListView view;
	
	List<Site> listSites = new ArrayList<Site>();
	
	private ProgressDialog dialog;	
	
	public SitesListPresenter(ISitesListView view){
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
	
	public void deselectFriendsSites(String idFacebook){
		if(listSites != null){
			for(Site s : listSites){
				if(s.getOwnerId().equals(idFacebook)){
					view.getConf().removeSiteId(s.getIdSite());
				}
			}
		}
	}
	
	/** Metodo para solicitar los amigos de Facebook*/
	@SuppressWarnings("unchecked")
	public void makeMySitesRequest() {
		
		if (dialog == null){				
			dialog = createProgressDialog();
	    	dialog.setProgress(0);
	        dialog.setMax(100);
	        dialog.show();
			
		}else{
			if(!dialog.isShowing()){
		        System.out.println("entrooooo");
		    	dialog.setProgress(0);
		        dialog.setMax(100);
		        dialog.show();
			}
		}
	        
        new GetSitesList().execute(view.getConf().getListFriendsIds());
	        	       			 						 						
	}
	/**
	 * Params:  Datos que pasaremos al comenzar la tarea
	 * Progress: Parámetros que necesitaremos para actualizar la UI.
	 * Result: Dato que devolveremos una vez terminada la tarea.
	 */
	private class GetSitesList extends AsyncTask<List<String>, Float, Boolean>{
			
		
		protected void onPreExecute() {

         }

         protected Boolean doInBackground(List<String>... listFriendsIds) {        	 
        	 try {        		
        		Log.i("SitesListPresenter", "Cargo lista de ids de amigos");
        		Log.i("SitesListPresenter", "listFriendsIds[0].size(): " + listFriendsIds[0].size());
        		Log.i("SitesListPresenter", "listFriendsIds[0]: " + listFriendsIds[0]);

    			listSites = ApiHelpers.getSitesNamesList(listFriendsIds[0]);
    			Log.i("SitesListPresenter", "carga hecha");
     			Log.d("SitesListPresenter", "listSites.size(): " + listSites.size());
    		   			 				
        		 dialog.setProgress(100);
        		 return true;
			} catch (Exception e) {			
				Log.e("SitesListPresenter", "ERROR", e);
				return false;
			}       

         }

         protected void onProgressUpdate (Float... valores) {
         }

         protected void onPostExecute(Boolean correct) {
             if(correct){              	            	
            	 Log.i("SitesListPresenter", "voy a cargar la lista");	    	
    	    	 view.setListSites(listSites);             	 
    	    	 dialog.dismiss();	     
             }else{
            	 dialog.dismiss();
            	 Dialog dialogo= createDialogError(view.getResources().getString(R.string.server_error_info));       		            		
            	 dialogo.show();
             }                  			             
         }  
	}

}
