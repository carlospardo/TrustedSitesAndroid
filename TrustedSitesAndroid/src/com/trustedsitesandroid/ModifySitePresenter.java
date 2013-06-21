package com.trustedsitesandroid;

import com.trustedsitesandroid.ModifySite.IModifySiteView;

import utils.ApiHelpers;

import models.Site;



import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class ModifySitePresenter {

	private ProgressDialog dialog;
	
	private Site site;
	
	private IModifySiteView view;
	
	public ModifySitePresenter(IModifySiteView view) {
		super();
		this.view = view;
	}
	
	public void init(){
		
		site = (Site) view.getActivity().getIntent().getSerializableExtra("site");
		Log.d("ModifySitePresenter", "nameSite: " + site.getName());
		view.setLatitude(site.getPositionX());
		view.setLongitude(site.getPositionY());
		
		view.setNameSite(site.getName());
		view.setInfoSite(site.getInfo());

	}
	
	public void updateSite(){
		
		String name = view.getNameSite().getText().toString();				
		String lat = view.getLatitude().getText().toString();
		String lng = view.getLongitude().getText().toString();
		String info = view.getInfoSite().getText().toString();
						
		if((name==null||name.equals(""))||(info==null||info.equals(""))){
			Dialog dialog=createDialogError(view.getResources().getString(R.string.register_error_info));
			dialog.show();
		}
		else{			
			Site newSite = new Site(site.getIdSite(), name, site.getUrlPhoto(),
					 lat, lng, info, site.getNameOwner(), view.getConf().getIdFacebook());					
			makeUpdateSiteRequest(newSite);
		}
	}
	public void removeSite(){
						
		makeRemoveSiteRequest(site.getIdSite());
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
	
	public void createRemoveDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(view.getActivity());

		builder.setTitle(view.getResources().getString(R.string.remove_site));
		builder.setMessage(view.getResources().getString(R.string.message_remove_site));
		builder.setNegativeButton(view.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.setPositiveButton(view.getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				removeSite();
			}
		});
		AlertDialog newDialog = builder.create();
		newDialog.show();
		

	}
	
	public ProgressDialog createProgressDialog() {
		dialog = new ProgressDialog(view.getActivity());
		dialog.setMessage(view.getResources().getString(R.string.accessing));
		dialog.setTitle(view.getResources().getString(R.string.progress));
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		return dialog;
	}
	
	public void makeUpdateSiteRequest(Site site) {
		
		dialog = createProgressDialog();
    	dialog.setProgress(0);
        dialog.setMax(100);
        dialog.show();
        
        new UpdateSite().execute(site);
	        	       			 						 						
	}
	/**
	 * Params:  Datos que pasaremos al comenzar la tarea
	 * Progress: Parámetros que necesitaremos para actualizar la UI.
	 * Result: Dato que devolveremos una vez terminada la tarea.
	 */
	private class UpdateSite extends AsyncTask<Site, Float, Boolean>{
			
        
		protected void onPreExecute() {

         }

         protected Boolean doInBackground(Site... site) {        	 
        	 try {        		  	       
     	        ApiHelpers.registerSite(site[0], view.getConf().getIdFacebook());     	     
     			
     	        Log.i("ModifySitePresenter", "Site actualizada con exito");     			 				
        		 dialog.setProgress(100);
        		 return true;
			} catch (Exception e) {			
				Log.e("ModifySitePresenter", "ERROR", e);
				return false;
			}                    
         }

         protected void onProgressUpdate (Float... valores) {
         }

         protected void onPostExecute(Boolean correct) {
             if(correct){              	   	    		    
    	    	 dialog.dismiss();	 
    	    	 view.getActivity().finish();
             }else{
            	 dialog.dismiss();
            	 Dialog dialogo= createDialogError(view.getResources().getString(R.string.server_error_info));       		            		
            	 dialogo.show();
             }                  			             
         }  
	}
	
	public void makeRemoveSiteRequest(String idSite) {
		
		dialog = createProgressDialog();
    	dialog.setProgress(0);
        dialog.setMax(100);
        dialog.show();
        
        new RemoveSite().execute(idSite);
	        	       			 						 						
	}
	/**
	 * Params:  Datos que pasaremos al comenzar la tarea
	 * Progress: Parámetros que necesitaremos para actualizar la UI.
	 * Result: Dato que devolveremos una vez terminada la tarea.
	 */
	private class RemoveSite extends AsyncTask<String, Float, Boolean>{
			
        
		protected void onPreExecute() {

         }

         protected Boolean doInBackground(String... idSite) {        	 
        	 try {      
        		ApiHelpers.removeSite(idSite[0], view.getConf().getIdFacebook()); 	     
        		view.getConf().removeSiteId(idSite[0]);
     	        Log.i("ModifySitePresenter", "Site eliminado con exito");     			 				
        		 dialog.setProgress(100);
        		 return true;
			} catch (Exception e) {			
				Log.e("ModifySitePresenter", "ERROR", e);
				return false;
			}                    
         }

         protected void onProgressUpdate (Float... valores) {
         }

         protected void onPostExecute(Boolean correct) {
             if(correct){              	   	    		    
    	    	 dialog.dismiss();
    	    	 
    	    	 view.getActivity().finish();
             }else{
            	 dialog.dismiss();
            	 Dialog dialogo= createDialogError(view.getResources().getString(R.string.server_error_info));       		            		
            	 dialogo.show();
             }                  			             
         }  
	}
}
