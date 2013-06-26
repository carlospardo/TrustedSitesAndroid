package presenters;

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
import com.trustedsitesandroid.MySites.IMySitesView;

public class MySitesPresenter {

	private IMySitesView view;
	
	private ProgressDialog dialog;	
	
	public MySitesPresenter(IMySitesView view){
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
	public void makeMySitesRequest() {
		
		dialog = createProgressDialog();
    	dialog.setProgress(0);
        dialog.setMax(100);
        dialog.show();
        
        new GetSitesList().execute(view.getConf().getIdFacebook());
	        	       			 						 						
	}
	/**
	 * Params:  Datos que pasaremos al comenzar la tarea
	 * Progress: Parámetros que necesitaremos para actualizar la UI.
	 * Result: Dato que devolveremos una vez terminada la tarea.
	 */
	private class GetSitesList extends AsyncTask<String, Float, Boolean>{
			
		List<Site> listSites;
        
		protected void onPreExecute() {

         }

         protected Boolean doInBackground(String... idFacebook) {        	 
        	 try {        		
        		Log.i("MySitesPresenter", "Cargo lista mis Sitios");    	       
     	        listSites = ApiHelpers.getMySites(idFacebook[0]);     	     
     			
     	        Log.i("MySitesPresenter", "carga hecha");
     			Log.d("MySitesPresenter", "listSites.size(): " + listSites.size());
     			 				
        		 dialog.setProgress(100);
        		 return true;
			} catch (Exception e) {			
				Log.e("MySitesPresenter", "ERROR", e);
				return false;
			}                    
         }

         protected void onProgressUpdate (Float... valores) {
         }

         protected void onPostExecute(Boolean correct) {
             if(correct){              	 
    	    	 Log.i("MySitesPresenter", "voy a cargar la lista");	    	
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
