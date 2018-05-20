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
import android.widget.CheckBox;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.Set;

import report.activity.model.Settinginfo;
import report.activity.model.Settinterface;
import report.weather.R;


public class Settingadapter extends BaseAdapter {

    private Activity activity;
    ArrayList<Settinginfo> setting=new ArrayList<>();
    Settinterface inter;

    private static LayoutInflater inflater=null;
    public Settingadapter(Activity act, ArrayList<Settinginfo> setInfo,Settinterface in){
        activity = act;
        setting=setInfo;
        inter=in;



        //store.add("Pitch Zone");

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		/*imageloader=new ImageLoader(a);
		settypeface=new Utils(a);*/
    }

    public int getCount() {
        return setting.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.settingitem, null);
            holder = new ViewHolder();
            holder.settingName= (TextView) convertView.findViewById(R.id.settingname);
            holder.settingCheck= (CheckBox) convertView.findViewById(R.id.checkset);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.settingName.setText(setting.get(position).getSettingName());
        holder.settingCheck.setChecked(setting.get(position).isCheck());
        holder.settingCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inter.settinginterface(setting.get(position),position);
            }
        });

        return convertView;
    }
    static class ViewHolder {
        private TextView settingName;
        private CheckBox settingCheck;
    }

}