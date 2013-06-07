package adapters;

import java.util.List;

import models.Site;

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
	private TextView item;


	public SitesArrayAdapter(Activity context, List<Site> listSites) {
		this.context = context;
		this.listSites = listSites;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_row, null, true);	
				
		String url=listSites.get(position).getUrlPhoto();
		if(url!=null){
			 new utils.DownloadImageTask((ImageView) rowView.findViewById(R.id.ImageView02)).execute(url);
		}
		
		TextView userTextView = (TextView) rowView.findViewById(android.R.id.text1);
		userTextView.setText(listSites.get(position).getName());		
		
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
