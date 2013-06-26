package presenters;

import models.User;
import utils.ApiHelpers;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.trustedsitesandroid.R;
import com.trustedsitesandroid.Tabs;
import com.trustedsitesandroid.Login.ILoginView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


public class LoginPresenter {

	private ProgressDialog dialog;
	
	private ILoginView view;
	
	public LoginPresenter(ILoginView view) {
		super();
		this.view = view;
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
	
	/** Metodo para solicitar los datos del usuario */
	public void makeMeRequest(final Session session) {

		dialog = createProgressDialog();
    	dialog.setProgress(0);
        dialog.setMax(100);
        dialog.show();

		// Make an API call to get user data and define a new callback to handle
		// the response.
		Request request = Request.newMeRequest(session,new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser userFb, Response response) {
						// If the response is successful
						if (session == Session.getActiveSession()) {
							if (userFb != null) {

								Log.d("LoginPresenter","userFb.getName()): "+ userFb.getName());
								String imageURL = "http://graph.facebook.com/"+ userFb.getId()
										+ "/picture?type=square";
								User myUser = new User(userFb.getId(), userFb.getName(), imageURL, "444.45",
										"222.23");
								view.getConf().setIdFacebook(myUser.getIdFacebook());
								view.getConf().setNameUser(myUser.getName());
								
								Log.i("LoginPresenter","ON RESUME: VOY A MOSTRAR APP");
								dialog.setProgress(50);
								new Register().execute(myUser);
							} else {
								dialog.dismiss();
								Log.e("LoginPresenter", "userFb es null!");
								Dialog dialogo = createDialogError(view.getResources()
										.getString(R.string.userFb_error_info));
								dialogo.show();
							}
						}
						if (response.getError() != null) {
							dialog.dismiss();
							Log.e("LoginPresenter",
									"Error al conectar con FB: "+ response.getError().getErrorMessage());
							Dialog dialogo = createDialogError(view.getResources().getString(R.string.userFb_error_info));
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
	private class Register extends AsyncTask<User, Float, Integer>{
		
		private boolean correct = false;
		
        protected void onPreExecute() {
         }

         protected Integer doInBackground(User... user) {
        	 
        	 try {
        		Log.i("LoginPresenter", "hago el registro");
				ApiHelpers.register(user[0]);
				Log.i("LoginPresenter", "registro hecho");
				 dialog.setProgress(100);
				 correct = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("LoginPresenter", "ERROR", e);
				correct = false;
			}
        	 Log.i("LoginPresenter", "250!"); 
             return 250;
         }

         protected void onProgressUpdate (Float... valores) {
         }

         protected void onPostExecute(Integer bytes) {
             if(correct){
            	 Log.d("LoginPresenter", "voy a cerrar");
	        	 dialog.dismiss();
	        	 Intent i = new Intent(view.getActivity(), Tabs.class);
	          	 view.getActivity().startActivity(i);
	          	 view.getActivity().finish();
             }else{
            	 Log.d("LoginPresenter", "error");
            	 dialog.dismiss();
            	 Dialog dialogo=createDialogError(view.getResources().getString(R.string.server_error_info));       		
            	 dialogo.show();
             }                  			             
         }  
	}

	public void cleanConfFile() {
		view.getConf().setFriendsIds(null);
		view.getConf().setSitesIds(null);	
	}
	
	
}
