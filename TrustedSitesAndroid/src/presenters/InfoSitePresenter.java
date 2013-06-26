package presenters;

import utils.ApiHelpers;

import models.Site;

import com.trustedsitesandroid.InfoSite;
import com.trustedsitesandroid.R;
import com.trustedsitesandroid.InfoSite.IInfoSiteView;
import com.trustedsitesandroid.R.drawable;
import com.trustedsitesandroid.R.string;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class InfoSitePresenter {

	private ProgressDialog dialog;
	
	private Site site;
	
	private IInfoSiteView view;
	
	public InfoSitePresenter(IInfoSiteView view) {
		super();
		this.view = view;
	}
	
	public void init(){
			
		site = (Site) view.getActivity().getIntent().getSerializableExtra("site");
		Log.d("InfoSitePresenter", "nameSite: " + site.getName());
		view.setLatitude(site.getPositionX());
		view.setLongitude(site.getPositionY());
		
		view.setNameSite(site.getName());
		view.setOwner(site.getNameOwner());
		view.setInfoSite(site.getInfo());
		System.out.println("site.getBitmap(): " + site.getBitmap());
		
		//view.setPhoto(site.getBitmap());
		view.setPhoto(R.drawable.logo);
		
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
	
	
}
