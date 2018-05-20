package adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.ArrayList;

import helper.Permission;
import helper.Utility;
import helper.WeatherPreference;
import report.activity.model.WeatherReport;
import report.activity.model.Weatherdetails;
import report.weather.R;

/**
 * Created by samsung on 5/20/2018.
 */

public class Weatheradapter extends RecyclerView.Adapter<Weatheradapter.WeatherViewHolder> {
    public ArrayList<WeatherReport> recent;
    Weatherdetails passdetails;
    WeatherPreference pref;
    ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instanc
    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title,datetext,location;
        RelativeLayout parent;

        public WeatherViewHolder(View view) {
            super(view);
            img=view.findViewById(R.id.weatherimage);
            title=view.findViewById(R.id.tempresult);
            datetext=view.findViewById(R.id.date);
            location=view.findViewById(R.id.locationresult);

            parent=view.findViewById(R.id.parentlay);

        }
    }

    public Weatheradapter(ArrayList<WeatherReport> details, Activity act,Weatherdetails det) {

        recent = details;
        passdetails=det;
        pref=WeatherPreference.getInstance(act);
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weathitem, parent, false);

        return new WeatherViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WeatherViewHolder holder, final int position) {

        final WeatherReport get=recent.get(position);


        if(pref.getDatabool(WeatherPreference.BOTH))
        {
            holder.title.setText(" "+String.format("%.2f",(get.getTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"C"
            +" | "+String.format("%.2f", Permission.convertCelciusToFahrenheit(get.getTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"F"
            );

        }
        else if(pref.getDatabool(WeatherPreference.FREH))
        {
            holder.title.setText(" "+String.format("%.2f", Permission.convertCelciusToFahrenheit(get.getTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"F");
        }
        else
        {
            holder.title.setText(" "+String.format("%.2f",(get.getTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"C");

        }
        holder.location.setText(" "+get.getLoc().getName());

       holder.parent.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               passdetails.sendDetailReport(get,position);
           }
       });

        if(position==0)
        {
            holder.datetext.setText("Today");
        }
        else
        {
            holder.datetext.setText(get.getDateStr());
        }
        imageLoader.displayImage(Utility.IMAGE_URL+get.getDesc().getIcon()+".png", holder.img, null, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                //holder.img.setImageResource(R.drawable.);
            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.img.setImageBitmap(loadedImage);

            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
            }
        });
        //holder.img.setImageResource(add.getImageid());


    }



    @Override
    public int getItemCount() {
        return recent.size();
    }
}

