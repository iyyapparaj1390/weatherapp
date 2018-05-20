package helper;

/**
 * Created by samsung on 5/20/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;




public class WeatherPreference {
    private static WeatherPreference Weather;
    private SharedPreferences sharedPreferences;
    public static final String CELSIS="cel";
    public static final String FREH="fre";
    public static final String LOCATION="loc";
    public static final String BOTH="both";


    public static WeatherPreference getInstance(Context context) {
        if (Weather == null) {
            Weather = new WeatherPreference(context);
        }
        return Weather;
    }

    private WeatherPreference(Context context) {
        sharedPreferences = context.getSharedPreferences("weather",Context.MODE_PRIVATE);
    }

    public void saveData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.commit();
    }
    public void saveBooleandata(String key,boolean value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putBoolean(key, value);
        prefsEditor.commit();
    }


    public String getData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }
    public boolean getDatabool(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getBoolean(key, false);
        }
        return false;
    }
}
// You can get YourPrefrence instance like:


