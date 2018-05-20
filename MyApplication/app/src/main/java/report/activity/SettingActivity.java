package report.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import adapter.Settingadapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import customfont.Customfont;
import helper.Utility;
import helper.WeatherPreference;
import report.activity.model.Settinginfo;
import report.activity.model.Settinterface;
import report.weather.R;

/**
 * Created by samsung on 5/20/2018.
 */

public class SettingActivity extends Activity  implements  View.OnClickListener{
    @BindView(R.id.settinglist)
    ListView settinglist;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.headertitle)
    Customfont headertitle;
    @BindView(R.id.save)
    Customfont save;
   Settingadapter adapter;
    ArrayList<Settinginfo>settInfo=new ArrayList<>();
    public static final String SETTING_MENU="menu";
    WeatherPreference pref;
    SharedPreferences.Editor edit;
    boolean bothbool=false;
    boolean frehbool=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.setting);
        pref=WeatherPreference.getInstance(SettingActivity.this);


        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Utility.topbar(window, this);
        }

        if(pref.getDatabool(WeatherPreference.BOTH))
        {
        bothbool=true;
        }
        else if(pref.getDatabool(WeatherPreference.FREH))
        {
            frehbool=true;
        }
        if(savedInstanceState==null) {
            Settinginfo cel = new Settinginfo();
            if(bothbool||frehbool)
            {
                cel.setCheck(false);
            }
            else {
                cel.setCheck(true);
            }
            cel.setType(true);
            cel.setSettingName("Celsius");
            Settinginfo fa = new Settinginfo();
            fa.setCheck(frehbool);
            fa.setType(true);
            fa.setSettingName("Fahrenheit");
            Settinginfo both = new Settinginfo();
            both.setCheck(bothbool);
            both.setType(true);
            both.setSettingName("Celsius & Fahrenheit");

            Settinginfo loc = new Settinginfo();
            if(pref.getDatabool(WeatherPreference.LOCATION))
            loc.setCheck(true);
            else
            loc.setCheck(false);
            loc.setType(false);
            loc.setSettingName("Enable Location");
            settInfo.add(cel);
            settInfo.add(fa);
            settInfo.add(both);
            settInfo.add(loc);
        }
        else
        {
            settInfo= (ArrayList<Settinginfo>) savedInstanceState.getSerializable(SETTING_MENU);
        }

        adapter=new Settingadapter(SettingActivity.this, settInfo, new Settinterface() {
            @Override
            public void settinginterface(Settinginfo info, int pos) {
                if(settInfo.get(pos).isCheck())
                {
                    settInfo.get(pos).setCheck(false);
                }
                else
                {
                    settInfo.get(pos).setCheck(true);
                    if(settInfo.get(pos).isType())
                    {
                    for (int i = 0; i < settInfo.size(); i++) {
                        if (i != pos) {
                            if (settInfo.get(i).isType()) {
                                settInfo.get(i).setCheck(false);
                            }
                        }
                    }

                    }

                }
                adapter.notifyDataSetChanged();
            }
        });
        settinglist.setAdapter(adapter);

        back.setOnClickListener(this);
        save.setOnClickListener(this);

loaddetails();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SETTING_MENU,settInfo);
    }

    public void loaddetails(){
        headertitle.setText("Setting");
        save.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id)
        {
            case R.id.back:
                finish();
                break;
            case R.id.save:
                for(int i=0; i<settInfo.size(); i++)
                {
                    switch (i)
                    {
                        case 0:
                            if(settInfo.get(i).isCheck())
                            {
                             pref.saveBooleandata(WeatherPreference.CELSIS,true);
                            }
                            else
                            {
                                pref.saveBooleandata(WeatherPreference.CELSIS,false);
                            }
                            break;
                        case 1:
                            if(settInfo.get(i).isCheck())
                            {
                                pref.saveBooleandata(WeatherPreference.FREH,true);
                            }
                            else
                            {
                                pref.saveBooleandata(WeatherPreference.FREH,false);
                            }
                            break;
                        case 2:
                            if(settInfo.get(i).isCheck())
                            {
                                pref.saveBooleandata(WeatherPreference.BOTH,true);
                            }
                            else
                            {
                                pref.saveBooleandata(WeatherPreference.BOTH,false);
                            }
                            break;
                        case 3:
                            if(settInfo.get(i).isCheck())
                            {
                                pref.saveBooleandata(WeatherPreference.LOCATION,true);
                            }
                            else
                            {
                                pref.saveBooleandata(WeatherPreference.LOCATION,false);
                            }
                            break;



                    }
                }

                break;
            default:
                break;
        }
        finish();

    }
}
