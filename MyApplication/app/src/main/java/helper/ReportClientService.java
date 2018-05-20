package helper;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static helper.Utility.APP_ID;
import static helper.Utility.BASE_URL;

/**
 * Created by samsung on 5/20/2018.
 */

public class ReportClientService {


    public String getWeatherReport(String currentlocaint) {
        HttpURLConnection con = null ;
        InputStream is = null;

        try {
            Log.e("url",""+BASE_URL + currentlocaint+"&A&APPID="+APP_ID);
            con = (HttpURLConnection) ( new URL(BASE_URL + currentlocaint+"&A&APPID="+APP_ID)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }

}
