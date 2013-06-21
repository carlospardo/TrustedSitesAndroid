package adapters;

import java.util.List;

import utils.Config;

import models.Site;

import com.trustedsitesandroid.FriendsList;
import com.trustedsitesandroid.MySites;
import com.trustedsitesandroid.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SitesArrayAdapter  extends BaseAdapter  {
	
	private final Activity context;
	private final List<Site> listSites;
	private final Config conf;


	public SitesArrayAdapter(Activity context, List<Site> listSites, Config conf) {
		this.context = context;
		this.listSites = listSites;
		this.conf = conf;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_row, null, true);	
				
		String url=listSites.get(position).getUrlPhoto();
//		if(url!=null){
//			 new utils.DownloadImageTask((ImageView) rowView.findViewById(R.id.ImageView02)).execute(url);
//		}
		
		ImageView bmImage = (ImageView) rowView.findViewById(R.id.ImageView02);
		bmImage.setImageResource(R.drawable.logo);
		
		TextView userTextView = (TextView) rowView.findViewById(R.id.text1);
		userTextView.setText(listSites.get(position).getName());		
		
		TextView ownerTextView = (TextView) rowView.findViewById(R.id.owner);
		ownerTextView.setText(listSites.get(position).getNameOwner());		
	
		if (conf.isSelectedThisSite(listSites.get(position).getIdSite())){
			rowView.setBackgroundColor(context.getResources().getColor(R.color.SelectionGreen));				
			rowView.getBackground().setLevel(2);
		}		
		return rowView;
	}

	public int getCount() {
		return this.listSites.size();
	}

	public Site getItem(int position) {
		return listSites.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
