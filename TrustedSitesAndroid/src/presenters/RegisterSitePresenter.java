package presenters;

import utils.ApiHelpers;

import models.Site;

import com.trustedsitesandroid.R;
import com.trustedsitesandroid.RegisterSite.IRegisterSiteView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class RegisterSitePresenter {

	private ProgressDialog dialog;
	
	private IRegisterSiteView view;
	
	public RegisterSitePresenter(IRegisterSiteView view) {
		super();
		this.view = view;
	}
	
	public void init(){
		
		Log.d("RegisterSitePresenter", "long: " + view.getActivity().getIntent().getStringExtra("longitude"));
		view.setLatitude(view.getActivity().getIntent().getStringExtra("latitude"));
		view.setLongitude(view.getActivity().getIntent().getStringExtra("longitude"));
	}
	
	public void createSite(){
		
		String name = view.getNameSite().getText().toString();				
		String lat = view.getLatitude().getText().toString();
		String lng = view.getLongitude().getText().toString();
		String info = view.getInfoSite().getText().toString();
						
		if((name==null||name.equals(""))||(info==null||info.equals(""))){
			Dialog dialog=createDialogError(view.getResources().getString(R.string.register_error_info));
			dialog.show();
		}
		else{			
			Site site = new Site("", name, "http://graph.facebook.com/285931851535123/picture?type=thumbnail",
					 lat, lng, info, view.getConf().getNameUser(), view.getConf().getIdFacebook());					
			makeNewSiteRequest(site);
		}
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
	
	public ProgressDialog createProgressDialog() {
		dialog = new ProgressDialog(view.getActivity());
		dialog.setMessage(view.getResources().getString(R.string.accessing));
		dialog.setTitle(view.getResources().getString(R.string.progress));
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		return dialog;
	}
	
	public void makeNewSiteRequest(Site site) {
		
		dialog = createProgressDialog();
    	dialog.setProgress(0);
        dialog.setMax(100);
        dialog.show();
        
        new CreateSite().execute(site);
	        	       			 						 						
	}
	/**
	 * Params:  Datos que pasaremos al comenzar la tarea
	 * Progress: Parámetros que necesitaremos para actualizar la UI.
	 * Result: Dato que devolveremos una vez terminada la tarea.
	 */
	private class CreateSite extends AsyncTask<Site, Float, Boolean>{
			
        
		protected void onPreExecute() {

         }

         protected Boolean doInBackground(Site... site) {        	 
        	 try {        		
        		Log.i("RegisterSitePresenter", "Cargo lista mis Sitios");    	       
     	        ApiHelpers.registerSite(site[0], view.getConf().getIdFacebook());     	     
     			
     	        Log.i("RegisterSitePresenter", "Site dada de alta con exito");     			 				
        		 dialog.setProgress(100);
        		 return true;
			} catch (Exception e) {			
				Log.e("RegisterSitePresenter", "ERROR", e);
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
