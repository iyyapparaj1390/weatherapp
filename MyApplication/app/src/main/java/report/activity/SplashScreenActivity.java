package report.activity;

/**
 * Created by samsung on 5/20/2018.
 */


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import helper.Permission;
import helper.Utility;
import report.weather.R;
public class SplashScreenActivity extends Activity {


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.splashscreen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Utility.topbar(window,this);
        }


        Thread startmenu=new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(4000);
                    runOnUiThread(new Runnable() {
                        public void run() {

                            startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                            finish();

                        }
                    });
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        startmenu.start();

    }
}
