package helper;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import report.weather.R;


/**
 * Created by samsung on 5/20/2018.
 */


public class Utility {
    public static  String BASE_URL="http://api.openweathermap.org/data/2.5/forecast?";
    public static String APP_ID="9640f9549796b7ef4d56f238166bcb5f";
    public static String IMAGE_URL="http://openweathermap.org/img/w/";
    public static float KELVIN_ID=273.15f;


    public static void topbar(Window wind, Activity act)
    {
        wind.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        wind.setStatusBarColor(act.getResources().getColor(R.color.orange));

    }
}
