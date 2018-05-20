package fragements;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import adapter.Weatheradapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import helper.Permission;
import helper.ReportClientService;
import helper.WeatherPreference;
import report.activity.DeatailWeatherReport;
import report.activity.model.Cloud;
import report.activity.model.Listenertask;
import report.activity.model.LocationInfo;
import report.activity.model.WeatherDescription;
import report.activity.model.WeatherReport;
import report.activity.model.Weatherdetails;
import report.activity.model.Wind;
import report.weather.R;

/**
 * Created by samsung on 5/20/2018.
 */

public class WeatherFragement extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, Listenertask{

    @BindView(R.id.weatherlist)
    RecyclerView weatherlist;
    Unbinder unbinder;
    ReportClientService weatherData;
    WeatherreportData callWeather;
    String location = "q=Chennai,IN";
    ArrayList<WeatherReport> reporDetails = new ArrayList<>();
    Weatheradapter adapter;
    private static final String STATE_ITEMS = "weatherlist";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;
    @BindView(R.id.progressBar_cyclic)
    ProgressBar progressBarCyclic;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    private double previouslat = 0;
    private double previouslang = 0;
    WeatherPreference pref;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    boolean serviceRunning = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather, container, false);

        unbinder = ButterKnife.bind(this, view);
        weatherData = new ReportClientService();
        pref = WeatherPreference.getInstance(getActivity());
        if(serviceRunning)
        {
            progressBarCyclic.setVisibility(View.VISIBLE);
        }
        else{
            progressBarCyclic.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pref = WeatherPreference.getInstance(getActivity());
        if (savedInstanceState != null) {
            reporDetails = (ArrayList<WeatherReport>) savedInstanceState.getSerializable(STATE_ITEMS);
            onchange();
        } else {

            loadwebservice();

        }
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                previouslat = 0;
                previouslang = 0;
                try
                {
                    if(reporDetails.size()>0)
                    {
                        reporDetails.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {

                }
                progressBarCyclic.setVisibility(View.VISIBLE);

                loadwebservice();
                swipeToRefresh.setRefreshing(false);
            }
        });

    }

    void loadwebservice() {
        if (Permission.checknetwork(getActivity())) {
            callWeather = new WeatherreportData(this);
            if (!pref.getDatabool(WeatherPreference.LOCATION)) {
                if (mGoogleApiClient != null) {
                    if (mGoogleApiClient.isConnected()) {
                        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                        mGoogleApiClient.disconnect();
                    }
                }


                callWeather.execute(new String[]{location});
            } else
                locationenabled();


        } else {
            Permission.showmessage("Please check your internet connection", getActivity());
        }
    }

    void locationenabled() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                      //  LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        }

        mGoogleApiClient.connect();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        locationenabled();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }


    public void onchange() {
        adapter = new Weatheradapter(reporDetails, getActivity(), new Weatherdetails() {
            @Override
            public void sendDetailReport(WeatherReport report, int p) {


                Intent star_detail = new Intent(getActivity(), DeatailWeatherReport.class);
                star_detail.putExtra("report", report);
                star_detail.putExtra("pos", p);
                getActivity().startActivity(star_detail);


                //Permission.showmessage("" + p, getActivity());
            }
        });

        weatherlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        weatherlist.setAdapter(adapter);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(mGoogleApiClient.isConnected()) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (location == null) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            100
                    );

                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            } else {


                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                if (pref.getDatabool(WeatherPreference.LOCATION)) {
                    callFromLocation();
                }

            }
        }

    }

    public void callFromLocation() {
        if (previouslat == 0) {
            previouslat = currentLatitude;
            previouslang = currentLongitude;
            callWebService();


        } else {
            Location locA = new Location("locA");
            locA.setLatitude(currentLatitude);
            locA.setLongitude(currentLongitude);
            Location locB = new Location("locB");
            locB.setLatitude(previouslat);
            locB.setLongitude(previouslang);
            if (locA.distanceTo(locB) > 500) {
                callWebService();
            }


        }
    }

    public void callWebService() {
        if (Permission.checknetwork(getActivity())) {
            callWeather = new WeatherreportData(this);
            callWeather.execute("lat=" + previouslat + "&lon=" + previouslang);


        } else {
            Permission.showmessage("Please check your internet connection", getActivity());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        if (pref.getDatabool(WeatherPreference.LOCATION)) {
            callFromLocation();
        }
        //  Toast.makeText(getActivity(), currentLatitude + " WORKS1 " + currentLongitude + "", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onResume() {
        super.onResume();
        if (pref.getDatabool(WeatherPreference.LOCATION)) {
            locationenabled();
/*
            if (mGoogleApiClient != null)
                mGoogleApiClient.connect();
*/
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (pref.getDatabool(WeatherPreference.LOCATION)) {
            if (mGoogleApiClient != null) {
                if (mGoogleApiClient.isConnected()) {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                    mGoogleApiClient.disconnect();
                }
            }
        }

    }

    @Override
    public void serviceStarted() {
        serviceRunning=true;
        progressBarCyclic.setVisibility(View.VISIBLE);

    }

    @Override
    public void serviceStopped() {
        serviceRunning=false;
        progressBarCyclic.setVisibility(View.GONE);

    }



    private class WeatherreportData extends AsyncTask<String, Void, String> {

        Listenertask task;
        public WeatherreportData(Listenertask listenerTask)
        {
            task=listenerTask;
        }
        @Override
        protected void onPreExecute() {
            reporDetails.clear();


            super.onPreExecute();
            task.serviceStarted();
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = ((new ReportClientService()).getWeatherReport(strings[0]));
            //Log.e("data", ""data);


            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            task.serviceStopped();
            try {
                ArrayList<String> datalist = new ArrayList<>();
                JSONObject obj = new JSONObject(s);
                if (getString("cod", obj).equalsIgnoreCase("200")) {
                    JSONArray listofarray = getJsonarray("list", obj);
                    for (int i = 0; i < listofarray.length(); i++) {

                        JSONObject individuallist = listofarray.getJSONObject(i);
                        boolean addWeather = true;
                        if (datalist.size() == 0) {
                            addWeather = true;
                            // datalist.add(getdate(Long.parseLong(getString("dt",individuallist))));
                        } else if (datalist.size() == 4) {
                            addWeather = false;
                            break;
                        } else {
                            for (int j = 0; j < datalist.size(); j++) {

                                if (datalist.get(j).equalsIgnoreCase(getdate(Long.parseLong(getString("dt", individuallist))))) {
                                    addWeather = false;
                                    break;
                                }
                            }

                        }
                        if (addWeather) {

                            datalist.add(getdate(Long.parseLong(getString("dt", individuallist))));

                            WeatherReport report = new WeatherReport();
                            Cloud cloud = new Cloud();
                            Wind wind = new Wind();
                            LocationInfo info = new LocationInfo();
                            WeatherDescription desc = new WeatherDescription();
                            JSONObject main = getObject("main", individuallist);
                            JSONArray weatherarray = getJsonarray("weather", individuallist);
                            JSONObject objWeather;
                            if (weatherarray.length() > 0) {
                                objWeather = weatherarray.getJSONObject(0);
                            } else {
                                objWeather = new JSONObject();
                            }
                            JSONObject cloudobj = getObject("clouds", individuallist);
                            JSONObject windobj = getObject("wind", individuallist);
                            JSONObject locobj = getObject("city", obj);
                            JSONObject cord = getObject("coord", locobj);


                            report.setTemp(getFloat("temp", main));
                            report.setMaxTemp(getFloat("temp_max", main));
                            report.setMinTemp(getFloat("temp_min", main));
                            report.setPressure(getFloat("pressure", main));
                            report.setSeaLevel(getFloat("sea_level", main));
                            report.setGrndLevel(getFloat("grnd_level", main));
                            report.setHumidty(getString("humidity", main));
                            report.setDateStr(getdate(Long.parseLong(getString("dt", individuallist))));

                            desc.setDescription(getString("description", objWeather));
                            desc.setIcon(getString("icon", objWeather));
                            desc.setId(getString("id", objWeather));
                            desc.setMain(getString("main", objWeather));
                            report.setDesc(desc);

                            cloud.setAll(getString("all", cloudobj));
                            report.setCloudRep(cloud);

                            wind.setSpeed(getString("speed", windobj));
                            wind.setDegree(getString("deg", windobj));
                            report.setWindRep(wind);

                            info.setLang(getString("lon", cord));
                            info.setLat(getString("lat", cord));
                            info.setName(getString("name", locobj));
                            info.setPopulation(getString("population", locobj));
                            info.setCode(getString("country", locobj));
                            report.setLoc(info);
                            reporDetails.add(report);


                        }
                        //  id":801,"main":"Clouds","description":"few clouds","icon":"02d"
//                            "temp":304.21,
//                                "temp_min":302.986,
//                                "temp_max":304.21,
//                                "pressure":1020.02,
//                                "sea_level":1020.57,
//                                "grnd_level":1020.02,
//                                "humidity":94,
//                                "temp_kf":1.22
//                        }


                    }
                    onchange();
/*
                    adapter=new Weatheradapter(reporDetails, new Weatherdetails() {
                        @Override
                        public void sendDetailReport(WeatherReport report, int p) {


                            Intent star_detail=new Intent(getActivity(), DeatailWeatherReport.class);
                            star_detail.putExtra("en", reporDetails);
                            star_detail.putExtra("pos",p);
                            getActivity().startActivity(star_detail);


                            Permission.showmessage(""+p,getActivity());
                        }
                    });

                    weatherlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    weatherlist.setAdapter(adapter);
*/
                    //Permission.showmessage("" + reporDetails.size(), getActivity());
                } else {
                    Toast.makeText(getActivity(), getString("message", obj), Toast.LENGTH_LONG).show();
                }


            } catch (Exception e) {
                Log.e("exception", e.getMessage());
            }
        }
    }

    JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        if (jObj.has(tagName)) {
            return jObj.getJSONObject(tagName);
        } else {
            return new JSONObject();
        }
    }

    JSONArray getJsonarray(String tagName, JSONObject jObj) throws JSONException {
        if (jObj.has(tagName)) {
            return jObj.getJSONArray(tagName);
        } else {
            return new JSONArray();
        }
    }

    String getString(String tagName, JSONObject jObj) throws JSONException {
        if (jObj.has(tagName)) {

            return jObj.getString(tagName);
        } else {
            return "";
        }
    }

    float getFloat(String tagName, JSONObject jObj) throws JSONException {
        if (jObj.has(tagName)) {

            return (float) jObj.getDouble(tagName);
        } else {
            return 0;
        }
    }

    String getdate(long millis) {
        //  Permission.showmessage(""+millis,HomeActivity.this);
        Date date = new Date(millis * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);
        Log.e("date", formattedDate);
        return formattedDate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100
            );

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                //  mRequestingLocationUpdates = true;
                // setButtonsEnabledState();
            }
        });

    }
        @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_ITEMS, reporDetails);
    }


}
