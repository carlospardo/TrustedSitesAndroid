package adapters;

import java.util.List;

import com.trustedsitesandroid.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SitesArrayAdapter  extends BaseAdapter  {
	
	private final Activity context;
	private final List<String> listaSites;
	private TextView item;


	public SitesArrayAdapter(Activity context, List<String> listaSites) {
		this.context = context;
		this.listaSites = listaSites;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.site_row, null, true);	
		
		TextView userTextView = (TextView) rowView.findViewById(android.R.id.text1);
		userTextView.setText(listaSites.get(position));

		return rowView;
	}

	public int getCount() {
		return this.listaSites.size();
	}

	public String getItem(int position) {
		return listaSites.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
