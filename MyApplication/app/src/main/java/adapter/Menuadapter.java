package adapter;

/**
 * Created by samsung on 5/20/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;

import report.activity.model.Sidmenu;
import report.weather.R;


public class Menuadapter extends BaseAdapter {

	private Activity activity;
	ArrayList<Sidmenu> side=new ArrayList<>();
	
	private static LayoutInflater inflater=null;
	public Menuadapter(Activity act, ArrayList<Sidmenu> sidemenu){
		activity = act;

           side=sidemenu;
		//store.add("Pitch Zone");
		
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		/*imageloader=new ImageLoader(a);
		settypeface=new Utils(a);*/
	}

	public int getCount() {
		return side.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;

		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.sidebarlayout, null);
			holder = new ViewHolder();
            holder.nameTextView= (TextView) convertView.findViewById(R.id.sidementext);
			holder.sideImageView= (ImageView) convertView.findViewById(R.id.sidemenuimage);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.nameTextView.setText(side.get(position).getTitle());

		return convertView;
	}
	static class ViewHolder {
		private TextView nameTextView;
		private ImageView sideImageView;
	}
 
}