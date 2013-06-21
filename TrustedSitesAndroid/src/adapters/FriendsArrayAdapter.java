package adapters;

import java.util.List;

import utils.Config;

import models.User;

import com.trustedsitesandroid.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsArrayAdapter  extends BaseAdapter  {
	
	private final Activity context;
	private final List<User> listFriends;
	private final Config conf;


	public FriendsArrayAdapter(Activity context, List<User> listFriends, Config conf) {
		this.context = context;
		this.listFriends = listFriends;
		this.conf = conf;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_row, null, true);	
				
		String url= listFriends.get(position).getUrlPhoto();
		if(url!=null){
			 new utils.DownloadImageTask((ImageView) rowView.findViewById(R.id.ImageView02)).execute(url);
		}
		
		TextView userTextView = (TextView) rowView.findViewById(R.id.text1);
		userTextView.setText(listFriends.get(position).getName());		
				
		if (conf.isSelectedThisFriend(listFriends.get(position).getIdFacebook())){
			rowView.setBackgroundColor(context.getResources().getColor(R.color.SelectionGreen));				
			rowView.getBackground().setLevel(2);
		}
		
		return rowView;
	}

	public int getCount() {
		return this.listFriends.size();
	}

	public User getItem(int position) {
		return listFriends.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
