package report.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.Menuadapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import fragements.AlertFragement;
import fragements.WeatherFragement;
import helper.Permission;
import helper.Utility;
import report.activity.model.Sidmenu;
import report.weather.R;

/**
 * Created by samsung on 5/20/2018.
 */


public class HomeActivity extends AppCompatActivity implements AlertFragement.Dialoglistenr {
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.custom_toolbar_title)
    TextView customToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.frame)
    FrameLayout frame;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    HashMap<String, List<String>> expandableListDetail;
    Fragment fragment;
    private static final String WEATHER_FRAGEMENT = "weatherFragement";
    private static final String SIDE_MENU_NAV = "side";

    @BindView(R.id.header)
    ImageView header;
    @BindView(R.id.list)
    ListView list;
    Menuadapter adapter;
    ArrayList<Sidmenu>sidmenus=new ArrayList<>();

/*
    List<String> expandableListTitle;
    ReportClientService  weatherData;
    String location="Chennai,IN";
    ArrayList<WeatherReport>reporDetails=new ArrayList<>();
*/


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actionbarlay);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Utility.topbar(window, this);
        }

        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            loadfraggement(new WeatherFragement());
            settoolbar();
            setUpNavigationView();
            setlistview();
            Sidmenu menu=new Sidmenu();
            menu.setTitle("Settings");
            sidmenus.add(menu);



        }
        else{
            sidmenus= (ArrayList<Sidmenu>) savedInstanceState.getSerializable(SIDE_MENU_NAV);
        }
        adapter = new Menuadapter(this,sidmenus);
        list.setAdapter(adapter);
        Permission.verifyStoragePermissions(this);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
 list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerLayout.closeDrawers();
                startActivity(new Intent(HomeActivity.this,SettingActivity.class));
            }
        });


    }

    void settoolbar() {
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


    }


    void setlistview() {
//        expandableListDetail = Expandablelist.getData();
//
//        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
//        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
//        list.setAdapter(expandableListAdapter);

    }

    private void setUpNavigationView() {


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        //SettingActivity the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        //loadfraggement(new Dashboardfragement());
    }

    private void loadfraggement(Fragment fra) {
        fragment = fra;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, WEATHER_FRAGEMENT);
        fragmentTransaction.commitAllowingStateLoss();
        //fragmentTransaction.commitAllowingStateosLs();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        else{
            new AlertFragement().show(getSupportFragmentManager(), "tag"); // or getFragmentManager() in API 11+

            //Permission.alert(HomeActivity.this);
        }

        //super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SIDE_MENU_NAV,sidmenus);
    }

    @Override
    public void onYes() {
        finish();
    }

    @Override
    public void onNo() {

    }
}
