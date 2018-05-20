package report.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import customfont.Customfont;
import customfont.Customfontregular;
import helper.Permission;
import helper.Utility;
import helper.WeatherPreference;
import report.activity.model.WeatherReport;
import report.weather.R;

/**
 * Created by samsung on 5/20/2018.
 */

public class DeatailWeatherReport extends Activity implements View.OnClickListener {


    @BindView(R.id.weatherimage)
    ImageView weatherimage;
    @BindView(R.id.temp)
    Customfont temp;
    @BindView(R.id.tempresult)
    Customfontregular tempresult;
    @BindView(R.id.maxtemp)
    Customfont maxtemp;
    @BindView(R.id.maxresult)
    Customfontregular maxresult;
    @BindView(R.id.mintemp)
    Customfont mintemp;
    @BindView(R.id.mintenoresult)
    Customfontregular mintenoresult;
    @BindView(R.id.humidty)
    Customfont humidty;
    @BindView(R.id.humidtyresult)
    Customfontregular humidtyresult;
    @BindView(R.id.wind)
    Customfont wind;
    @BindView(R.id.windpressure)
    Customfontregular windpressure;
    @BindView(R.id.pressure)
    Customfont pressure;
    @BindView(R.id.pressureresult)
    Customfontregular pressureresult;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.headertitle)
    Customfont headertitle;
    WeatherReport report;
    int pos;
    public static final String ITEM_NAME = "items";
    public static final String POSITION = "pos";
    @BindView(R.id.location)
    Customfont location;
    @BindView(R.id.locationresult)
    Customfontregular locationresult;
    ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instanc
    WeatherPreference pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.detailreport);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Utility.topbar(window, this);
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                report = (WeatherReport) getIntent().getSerializableExtra("report"); //Obtaining data
                pos = getIntent().getIntExtra("pos", 0);

            }
        } else {
            report = (WeatherReport) savedInstanceState.getSerializable(ITEM_NAME);
            pos = savedInstanceState.getInt(POSITION);
        }

        back.setOnClickListener(this);
        loaddetails();

    }

    public void loaddetails() {
        pref=WeatherPreference.getInstance(DeatailWeatherReport.this);

        if(pref.getDatabool(WeatherPreference.BOTH))
        {
            tempresult.setText(" "+String.format("%.2f",(report.getTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"C"
                    +" | "+String.format("%.2f", Permission.convertCelciusToFahrenheit(report.getTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"F"
            );
            maxresult.setText(" "+String.format("%.2f",(report.getMaxTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"C"
                    +" | "+String.format("%.2f", Permission.convertCelciusToFahrenheit(report.getMaxTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"F"
            );
            mintenoresult.setText(" "+String.format("%.2f",(report.getMinTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"C"
                    +" | "+String.format("%.2f", Permission.convertCelciusToFahrenheit(report.getMinTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"F"
            );


        }
        else if(pref.getDatabool(WeatherPreference.FREH))
        {
            tempresult.setText(" "+String.format("%.2f", Permission.convertCelciusToFahrenheit(report.getTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"F");
            maxresult.setText(" "+String.format("%.2f", Permission.convertCelciusToFahrenheit(report.getMaxTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"F");
            mintenoresult.setText(" "+String.format("%.2f", Permission.convertCelciusToFahrenheit(report.getMinTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"F");


        }
        else
        {
            tempresult.setText(" "+String.format("%.2f",(report.getTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"C");
            maxresult.setText(" "+String.format("%.2f",(report.getMaxTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"C");
            mintenoresult.setText(" "+String.format("%.2f",(report.getMinTemp()- Utility.KELVIN_ID))+(char)0x00B0 +"C");

        }


        humidtyresult.setText(" "+report.getHumidty()+ "%");

        windpressure.setText(" "+report.getWindRep().getSpeed()+ " mps");

        pressureresult.setText(" " + report.getPressure()+" hPa ");
        locationresult.setText(" "+report.getLoc().getName());
        imageLoader.displayImage(Utility.IMAGE_URL+report.getDesc().getIcon()+".png", weatherimage, null, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                //holder.img.setImageResource(R.drawable.);
            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                weatherimage.setImageBitmap(loadedImage);

            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
            }
        });



        if (pos == 0) {
            headertitle.setText("Today");
        } else {
            headertitle.setText(report.getDateStr());
        }

    }

/*
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable(ITEM_NAME,report);
        outState.putInt(POSITION,pos);
    }
*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ITEM_NAME, report);
        outState.putInt(POSITION, pos);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }

    }
}
