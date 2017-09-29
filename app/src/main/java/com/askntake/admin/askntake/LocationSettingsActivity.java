package com.askntake.admin.askntake;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import AppUtils.AppConstants;
import AppUtils.Commons;
import AppUtils.DataKeyValues;
import AppUtils.ServiceHandler;
import AppUtils.Utils;
import Fragments.ServicesFragment;

/**
 * Created by admin on 3/31/2017.
 */

public class LocationSettingsActivity extends AppCompatActivity {

    AutoCompleteTextView atvPlaces;
    DownloadTask placesDownloadTask;
    DownloadTask placeDetailsDownloadTask;
    ParserTask placesParserTask;
    ParserTask placeDetailsParserTask;
    Utils utils;

    GoogleMap googleMap;
    GoogleMap main_googleMap;

    final int PLACES = 0;
    final int PLACES_DETAILS = 1;
    Circle circle;
    Double radiusInMeters;
    SupportMapFragment supportMapFragment;
    LinearLayout map_linear_layout;
    RelativeLayout location_buttons_relative;
    Button button_my_location, button_current_location, set_location_btn;

    boolean updatelocation;

    String user_latitude = "0", search_location;
    String user_longitude = "0";

    String requestFrom, mainUserId;
    TrackGPS gps;


    ArrayList<String> Categorieslist;
    Set<String> set;
    SharedPreferences requestPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_location_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //get curent location and then set it to map

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        googleMap = supportMapFragment.getMap();
        map_linear_layout = (LinearLayout) findViewById(R.id.map_linear_layout);
        location_buttons_relative = (RelativeLayout) findViewById(R.id.location_buttons_relative);
        button_my_location = (Button) findViewById(R.id.button_my_location);
        button_current_location = (Button) findViewById(R.id.button_current_location);
        set_location_btn = (Button) findViewById(R.id.button_set_location);


        requestPref = getApplicationContext().getSharedPreferences("serviceReq", MODE_PRIVATE);
        set = requestPref.getStringSet("serv_req_categ_list", null);
        Categorieslist = new ArrayList<>();
        try {
            Categorieslist.addAll(set);
            Log.d("retrivesharedPreferences", "" + set);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences _userid = AppConstants.preferencesData(getApplicationContext());
        mainUserId = _userid.getString("fb_id", null);

        updatelocation = getIntent().getBooleanExtra("updatelocation", false);

        String from_filter_location_latitude = _userid.getString(DataKeyValues.FILTERLATTITUDE, null);
        String from_filter_location_langitude = _userid.getString(DataKeyValues.FILTERLONGITUDE, null);

        String from_updation_location_latitude = getIntent().getStringExtra("upload_lattitude");
        String from_updation_location_langitude = getIntent().getStringExtra("upload_longitude");


        String from_ser_req_cur_lattitude = getIntent().getStringExtra("ser_req_cur_lattitude");
        String from_ser_req_cur_langitude = getIntent().getStringExtra("ser_req_cur_langitude");


        // Getting a reference to the AutoCompleteTextView
        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
        atvPlaces.setThreshold(1);


       /* if (from_updation_location_latitude != null && from_updation_location_langitude != null) {
            String location_name = getDisplyingAddress("" + from_updation_location_latitude, "" + from_updation_location_langitude);
            atvPlaces.setText(location_name);
            setGoogleMap(googleMap, Double.parseDouble(from_updation_location_latitude), Double.parseDouble(from_updation_location_langitude), location_name);
        }
        else {
            String location_name = getDisplyingAddress("" + from_filter_location_latitude, "" + from_filter_location_langitude);
            atvPlaces.setText(location_name);
           // setGoogleMap(googleMap, Double.parseDouble(from_filter_location_latitude), Double.parseDouble(from_filter_location_langitude), location_name);
            makeMapWithCurrentLocation();
        }*/
        if (from_filter_location_latitude == null && from_filter_location_langitude == null) {
            makeMapWithCurrentLocation();
        } else if (from_updation_location_latitude != null && from_updation_location_langitude != null) {
            String location_name = getDisplyingAddress("" + from_updation_location_latitude, "" + from_updation_location_langitude);
            atvPlaces.setText(location_name);
            setGoogleMap(googleMap, Double.parseDouble(from_updation_location_latitude), Double.parseDouble(from_updation_location_langitude), location_name);
        } else if (from_ser_req_cur_lattitude != null && from_ser_req_cur_langitude != null) {
            String location_name = getDisplyingAddress("" + from_ser_req_cur_lattitude, "" + from_ser_req_cur_langitude);
            atvPlaces.setText(location_name);
            setGoogleMap(googleMap, Double.parseDouble(from_ser_req_cur_lattitude), Double.parseDouble(from_ser_req_cur_langitude), location_name);
        } else {
            String location_name = getDisplyingAddress("" + from_filter_location_latitude, "" + from_filter_location_langitude);
            atvPlaces.setText(location_name);
            // setGoogleMap(googleMap, Double.parseDouble(from_filter_location_latitude), Double.parseDouble(from_filter_location_langitude), location_name);
            makeMapWithCurrentLocation();
        }

        requestFrom = getIntent().getStringExtra("requestFrom");

        set_location_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferences user_LocationData = AppConstants.preferencesData(getApplicationContext());
                SharedPreferences.Editor LocationData_editor = user_LocationData.edit();

                if (!user_latitude.equalsIgnoreCase("0") && !user_longitude.equalsIgnoreCase("0")) {
                    LocationData_editor.putString(DataKeyValues.LATITUDE_VALUE, user_latitude);
                    LocationData_editor.putString(DataKeyValues.LONGITUDE_VALUE, user_longitude);

                    LocationData_editor.putString(DataKeyValues.SET_LOC_LATTITUDE, user_latitude);
                    LocationData_editor.putString(DataKeyValues.SET_LOC_LONGITUDE, user_longitude);

                    if (requestFrom != null && !requestFrom.equalsIgnoreCase("upload_scr")) {
                        LocationData_editor.putString(DataKeyValues.FILTERLATTITUDE, user_latitude);
                        LocationData_editor.putString(DataKeyValues.FILTERLONGITUDE, user_longitude);
                    }
                    Address user_adrees_info = LocationAddressData(getApplicationContext(), user_latitude, user_longitude);

                    String area_of_user = "";
                    String user_zipcode = "";
                    String search_data = "";
                    String cuntryCode = null;
                    String only_Thoroughfare = "";
                    String user_city = "";
                    String user_state = "";
                    if (user_adrees_info != null) {

                        if (user_adrees_info.getThoroughfare() != null) {
                            search_data = user_adrees_info.getThoroughfare() + ",";
                            only_Thoroughfare = search_data;
                        }

                        if (user_adrees_info.getAdminArea() != null) {
                            search_data += user_adrees_info.getAdminArea() + ",";
                            user_state = user_adrees_info.getAdminArea();
                            LocationData_editor.putString(DataKeyValues.PRODUCT_UPLOAD_STATE, user_state);


                        } else {
                            LocationData_editor.putString(DataKeyValues.PRODUCT_UPLOAD_STATE, "");
                        }
                        if (user_adrees_info.getLocality() != null) {
                            LocationData_editor.putString(DataKeyValues.LOCATION, only_Thoroughfare + user_adrees_info.getLocality());
                            area_of_user = user_adrees_info.getLocality();
                            search_data += user_adrees_info.getLocality();
                            user_city = user_adrees_info.getLocality();
                            LocationData_editor.putString(DataKeyValues.PRODUCT_UPLOAD_CITY, user_city);
                        } else {
                            LocationData_editor.putString(DataKeyValues.LOCATION, atvPlaces.getText().toString());
                            LocationData_editor.putString(DataKeyValues.PRODUCT_UPLOAD_CITY, "");
                        }

                        if (user_adrees_info.getPostalCode() != null) {
                            LocationData_editor.putString(DataKeyValues.ZIPCODE, user_adrees_info.getPostalCode());
                            user_zipcode = user_adrees_info.getPostalCode();
                            search_data += "," + user_adrees_info.getPostalCode();
                        } else {
                            LocationData_editor.putString(DataKeyValues.ZIPCODE, "");
                        }

                        if (user_adrees_info.getCountryCode() != null) {
                            cuntryCode = user_adrees_info.getCountryCode();
                            LocationData_editor.putString(DataKeyValues.CUNTRY_CODE, cuntryCode);
                            SharedPreferences shPref = getApplicationContext().getSharedPreferences("SearchData", MODE_PRIVATE);
                            SharedPreferences.Editor editShPref = shPref.edit();
                            editShPref.putString(DataKeyValues.PRICE_TYPE, cuntryCode);
                            editShPref.putString(DataKeyValues.COMPLETE_ADDRESS_OF_LOCATION, search_data + "," + cuntryCode);

                            editShPref.commit();
                        }
                    }
                    if (requestFrom != null && !requestFrom.equalsIgnoreCase("upload_scr")) {
                        LocationData_editor.putString(DataKeyValues.UPLOAD_PRICE_TYPE, cuntryCode);
                    }

                    //String dsf=search_location;
                    LocationData_editor.putString(DataKeyValues.SEARCH_SHOW_LOCATION, search_data);
                    LocationData_editor.commit();
                    if (updatelocation) {
                        if (user_LocationData.getBoolean(DataKeyValues.LOGIN_STATUS, false)) {

                            if (user_LocationData.getString(DataKeyValues.OWNER_ID, null) != null) {
                                LocationData_editor.putString(DataKeyValues.MYLOCATION, area_of_user + " " + user_zipcode);
                                LocationData_editor.commit();
                                new UpdateLocationDataAsync(user_LocationData.getString(DataKeyValues.OWNER_ID, null), area_of_user, user_zipcode, cuntryCode, user_city, user_state).execute();


                            }

                        } else {

                            if (requestFrom != null) {

                                if (requestFrom.equalsIgnoreCase("upload_scr")) {
                                    /*Intent myIntent =new Intent(LocationSettingActivity.this,ItemUploadingActivity.class);
                                    startActivity(myIntent);*/

                                    finish();


                                } else if (requestFrom.equalsIgnoreCase("search")) {

                                    new LocationAsyncTask("setlocation").execute();
                                /*Intent myIntent = new Intent(LocationSettingsActivity.this, ServiceFilteringActivity.class);
                                startActivity(myIntent);
                                finish();*/
                                } else if (requestFrom.equalsIgnoreCase("set_location")) {
                                  /*  Intent myIntent = new Intent(LocationSettingsActivity.this, NavigationDrawerActivity.class);
                                    startActivity(myIntent);
                                    finish();*/
                                    new LocationAsyncTask("set_location").execute();
                                } else if (requestFrom.equalsIgnoreCase("display_scr")) {
                                    Intent myIntent = new Intent(LocationSettingsActivity.this, NavigationDrawerActivity.class);
                                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    myIntent.putExtra("EXIT", true);
                                    myIntent.putExtra("profile_location", "profile_location");
                                    startActivity(myIntent);
                                    finish();
                                } else if (requestFrom.equalsIgnoreCase("description_scr")) {
                                    Intent myIntent = new Intent(LocationSettingsActivity.this, AccountEditActivity.class);
                                    startActivity(myIntent);
                                    finish();
                                }

                            }

                        }
                    } else {
                        if (requestFrom != null) {
                            if (requestFrom.equalsIgnoreCase("upload_scr")) {
                                LocationData_editor = user_LocationData.edit();
                                LocationData_editor.putString(DataKeyValues.UPLOAD_LATITUDE, user_latitude);
                                LocationData_editor.putString(DataKeyValues.UPLOAD_LANGITUDE, user_longitude);

                                LocationData_editor.putString(DataKeyValues.FILTERLATTITUDE, user_latitude);
                                LocationData_editor.putString(DataKeyValues.FILTERLONGITUDE, user_longitude);


                                LocationData_editor.commit();
                                /*Intent myIntent =new Intent(LocationSettingActivity.this,ItemUploadingActivity.class);
                                startActivity(myIntent);*/
                                finish();
                            } else if (requestFrom.equalsIgnoreCase("set_location")) {

                                new LocationAsyncTask("set_location").execute();
                            } else if (requestFrom.equalsIgnoreCase("display_scr")) {

                                Intent myIntent = new Intent(LocationSettingsActivity.this, NavigationDrawerActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                myIntent.putExtra("EXIT", true);
                                myIntent.putExtra("profile_location", "profile_location");
                                startActivity(myIntent);
                                finish();
                            } else if (requestFrom.equalsIgnoreCase("display_scr")) {
                                Intent myIntent = new Intent(LocationSettingsActivity.this, AccountEditActivity.class);
                                startActivity(myIntent);
                                finish();
                            } else if (requestFrom.equalsIgnoreCase("search")) {

                                new LocationAsyncTask("setlocation").execute();
                                /*Intent myIntent = new Intent(LocationSettingsActivity.this, ServiceFilteringActivity.class);
                                startActivity(myIntent);
                                finish();*/
                            } else if (requestFrom.equalsIgnoreCase("description_scr")) {
                                Intent myIntent = new Intent(LocationSettingsActivity.this, AccountEditActivity.class);
                                startActivity(myIntent);
                                finish();
                            } else if (requestFrom.equalsIgnoreCase("category")) {
                                LocationData_editor.putString(DataKeyValues.FILTERLATTITUDE, user_latitude);
                                LocationData_editor.putString(DataKeyValues.FILTERLONGITUDE, user_longitude);
                                finish();
                            } else if (requestFrom.equalsIgnoreCase("service_request_display_scr")) {
                                LocationData_editor.putString(DataKeyValues.USER_REQ_LAT, user_latitude);
                                LocationData_editor.putString(DataKeyValues.USER_REQ_LANG, user_longitude);
                                Intent ReqIntent = new Intent(LocationSettingsActivity.this, ServiceRequestsActivity.class);
                                ReqIntent.putExtra("lat", user_latitude);
                                ReqIntent.putExtra("lang", user_longitude);
                                ReqIntent.putStringArrayListExtra("Categorieslist", Categorieslist);
                                startActivity(ReqIntent);
                                finish();
                            }
                        }

                    }
                } else {

                    if (requestFrom.equalsIgnoreCase("set_location")) {
                                  /*  Intent myIntent = new Intent(LocationSettingsActivity.this, NavigationDrawerActivity.class);
                                    startActivity(myIntent);
                                    finish();*/
                        new LocationAsyncTask("set_location").execute();
                    } else if (requestFrom.equalsIgnoreCase("upload_scr")) {

                        makeMapWithCurrentLocation();
                        LocationData_editor = user_LocationData.edit();
                        LocationData_editor.putString(DataKeyValues.UPLOAD_LATITUDE, user_latitude);
                        LocationData_editor.putString(DataKeyValues.UPLOAD_LANGITUDE, user_longitude);

                        LocationData_editor.commit();

                                /*Intent myIntent =new Intent(LocationSettingActivity.this,ItemUploadingActivity.class);
                                startActivity(myIntent);*/
                        finish();

                    } else if (requestFrom.equalsIgnoreCase("display_scr")) {

                        Intent myIntent = new Intent(LocationSettingsActivity.this, NavigationDrawerActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("EXIT", true);
                        myIntent.putExtra("profile_location", "profile_location");
                        startActivity(myIntent);
                        finish();
                    } else if (requestFrom.equalsIgnoreCase("display_scr")) {
                        Intent myIntent = new Intent(LocationSettingsActivity.this, AccountEditActivity.class);
                        startActivity(myIntent);
                        finish();
                    } else if (requestFrom.equalsIgnoreCase("search")) {

                        new LocationAsyncTask("setlocation").execute();
                                /*Intent myIntent = new Intent(LocationSettingsActivity.this, ServiceFilteringActivity.class);
                                startActivity(myIntent);
                                finish();*/
                    } else if (requestFrom.equalsIgnoreCase("description_scr")) {
                        Intent myIntent = new Intent(LocationSettingsActivity.this, AccountEditActivity.class);
                        startActivity(myIntent);
                        finish();
                    } else if (requestFrom.equalsIgnoreCase("category")) {
                        LocationData_editor.putString(DataKeyValues.FILTERLATTITUDE, user_latitude);
                        LocationData_editor.putString(DataKeyValues.FILTERLONGITUDE, user_longitude);
                        finish();
                    } else if (requestFrom.equalsIgnoreCase("service_request_display_scr")) {
                        LocationData_editor.putString(DataKeyValues.USER_REQ_LAT, user_latitude);
                        LocationData_editor.putString(DataKeyValues.USER_REQ_LANG, user_longitude);
                        Intent ReqIntent = new Intent(LocationSettingsActivity.this, ServiceRequestsActivity.class);
                        ReqIntent.putExtra("lat", user_latitude);
                        ReqIntent.putExtra("lang", user_longitude);
                        ReqIntent.putStringArrayListExtra("Categorieslist", Categorieslist);
                        startActivity(ReqIntent);
                        finish();
                    }

                }

            }
        });

        // Adding textchange listener
        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Creating a DownloadTask to download Google Places matching "s"
                placesDownloadTask = new DownloadTask(PLACES);

                // Getting url to the Google Places Autocomplete api
                String url = getAutoCompleteUrl(s.toString());

                // Start downloading Google Places
                // This causes to execute doInBackground() of DownloadTask class
                placesDownloadTask.execute(url);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        button_my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences user_LocationData = AppConstants.preferencesData(getApplicationContext());
                if (!user_LocationData.getBoolean(DataKeyValues.LOGIN_STATUS, false)) {
                    //LogingScreenAlert("camerabutton");

                    Intent account_intent = new Intent(getApplicationContext(), SocialMediaRegistrationsActivity.class);
                    startActivity(account_intent);

                } else {

                    Location locationData = Commons.getLastKnownLocation(getApplicationContext());

                    if (user_LocationData.getString(DataKeyValues.MY_OWN_LATITUDE_VALUE, null) != null) {
                        if (user_LocationData.getString(DataKeyValues.MY_OWN_LONGITUDE_VALUE, null) != null) {

                            user_latitude = user_LocationData.getString(DataKeyValues.MY_OWN_LATITUDE_VALUE, null);
                            user_longitude = user_LocationData.getString(DataKeyValues.MY_OWN_LONGITUDE_VALUE, null);
                        }
                    } else {
                        if (locationData != null) {
                            user_latitude = String.valueOf(locationData.getLatitude());
                            user_longitude = String.valueOf(locationData.getLongitude());
                        } else {
                            user_latitude = "0";
                            user_longitude = "0";
                        }
                    }

                    Address user_adrees_info = DataKeyValues.LocationAddressData(getApplicationContext(), user_latitude, user_longitude);
                    String area_data = "";
                    if (user_adrees_info != null) {
                        if (user_adrees_info.getThoroughfare() != null) {
                            area_data = user_adrees_info.getThoroughfare();
                        }

                        if (user_adrees_info.getLocality() != null) {
                            area_data = area_data + "," + user_adrees_info.getLocality();
                        }
                        if (user_adrees_info.getAdminArea() != null) {
                            area_data = area_data + ", " + user_adrees_info.getAdminArea();
                        }
                        if (user_adrees_info.getPostalCode() != null) {

                            area_data = area_data + ", " + user_adrees_info.getPostalCode();
                        }
                        atvPlaces.setText(area_data);

                    } else {
                        atvPlaces.setText("");
                    }
                    button_my_location.setVisibility(View.GONE);
                    button_current_location.setVisibility(View.GONE);
                    map_linear_layout.setVisibility(View.VISIBLE);


                    if (user_latitude.equalsIgnoreCase("0") && user_longitude.equalsIgnoreCase("0")) {
                        updatelocation = true;
                        Toast.makeText(getApplicationContext(), "Please set your Location", Toast.LENGTH_SHORT).show();
                        setDefaultLocation();
                    } else {
                        IntializeMapWithUserDetails(Double.parseDouble(user_latitude), Double.parseDouble(user_longitude), area_data);
                    }
                }

                //call my location service with lat long values
            }
        });
        button_current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //makeMapWithCurrentLocation();

                Location locationData = Commons.getLastKnownLocation(getApplicationContext());
                if (locationData != null) {
                    user_latitude = String.valueOf(locationData.getLatitude());
                    user_longitude = String.valueOf(locationData.getLongitude());
                } else {
                    user_latitude = "0";
                    user_longitude = "0";
                }

                Address user_adrees_info = DataKeyValues.LocationAddressData(getApplicationContext(), user_latitude, user_longitude);
                String area_data = "";
                if (user_adrees_info != null) {
                    if (user_adrees_info.getThoroughfare() != null) {
                        area_data = user_adrees_info.getThoroughfare();
                    }

                    if (user_adrees_info.getLocality() != null) {
                        area_data = area_data + "," + user_adrees_info.getLocality();
                    }
                    if (user_adrees_info.getAdminArea() != null) {
                        area_data = area_data + ", " + user_adrees_info.getAdminArea();
                    }
                    if (user_adrees_info.getPostalCode() != null) {

                        area_data = area_data + ", " + user_adrees_info.getPostalCode();
                    }

                    atvPlaces.setText(area_data);

                } else {
                    atvPlaces.setText("");
                }

                button_my_location.setVisibility(View.GONE);
                button_current_location.setVisibility(View.GONE);
                map_linear_layout.setVisibility(View.VISIBLE);


                IntializeMapWithUserDetails(Double.parseDouble(user_latitude), Double.parseDouble(user_longitude), area_data);
            }
        });

        // Setting an item click listener for the AutoCompleteTextView dropdown list
        atvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long id) {

                ListView lv = (ListView) arg0;
                SimpleAdapter adapter = (SimpleAdapter) arg0.getAdapter();

                HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(index);

                // Creating a DownloadTask to download Places details of the selected place
                placeDetailsDownloadTask = new DownloadTask(PLACES_DETAILS);

                // Getting url to the Google Places details api
                String url = getPlaceDetailsUrl(hm.get("reference"));

                // Start downloading Google Place Details
                // This causes to execute doInBackground() of DownloadTask class
                placeDetailsDownloadTask.execute(url);


            }
        });

        atvPlaces.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

//                if(motionEvent.getRawX() <= (atvPlaces.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
//                {
//                    Toast.makeText(LocationSettingsActivity.this, "Left Icon Clicked Here", Toast.LENGTH_SHORT).show();
//
//                    return true;
//                }


                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (atvPlaces.getRight() - atvPlaces.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        map_linear_layout.setVisibility(View.GONE);
                        location_buttons_relative.setVisibility(View.VISIBLE);
                        button_current_location.setVisibility(View.VISIBLE);
                        button_my_location.setVisibility(View.VISIBLE);
//                        button_set_location.setVisibility(View.GONE);
                        atvPlaces.setText("");

                        return true;
                    }
                }

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            if (requestFrom.equalsIgnoreCase("set_location")) {
                Intent myIntent = new Intent(LocationSettingsActivity.this, NavigationDrawerActivity.class);
                startActivity(myIntent);
                finish();

            } else if (requestFrom.equalsIgnoreCase("search")) {

                Intent myIntent = new Intent(LocationSettingsActivity.this, ServiceFilteringActivity.class);
                startActivity(myIntent);
                finish();
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private String getAutoCompleteUrl(String place) {

        // Obtain browser key from https://code.google.com/apis/console


        // String key = "key=AIzaSyDRX_kDSRtU94rl2IghxwYlCmc-dnVn6T0";


        //String key = "key=AIzaSyBkaK9EHTznVbr9JsxOw-ZjPtU8MktNlXc";


        //final key

        //String key = "key=AIzaSyA1Cx2nCuGPIZ0YeVi_AbkS7UpP9BAh4_Q";

        String key = "key=AIzaSyB1Eyak7Fhh9jTRbuv_9R54Lsd75XYrxcw";

        // place to be be searched
        String input = "input=" + place;

        // place type to be searched
        String types = "types=geocode";

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = input + "&" + types + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

        return url;
    }


    private String getPlaceDetailsUrl(String ref) {

        // Obtain browser key from https://code.google.com/apis/console

        //String key = "key=AIzaSyDRX_kDSRtU94rl2IghxwYlCmc-dnVn6T0";


        //String key = "key=AIzaSyBkaK9EHTznVbr9JsxOw-ZjPtU8MktNlXc";


        //final key

        //String key = "key=AIzaSyA1Cx2nCuGPIZ0YeVi_AbkS7UpP9BAh4_Q";

        String key = "key=AIzaSyB1Eyak7Fhh9jTRbuv_9R54Lsd75XYrxcw";


        // reference of place
        String reference = "reference=" + ref;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = reference + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        private int downloadType = 0;

        // Constructor
        public DownloadTask(int type) {
            this.downloadType = type;
        }

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            switch (downloadType) {
                case PLACES:
                    // Creating ParserTask for parsing Google Places
                    placesParserTask = new ParserTask(PLACES);

                    // Start parsing google places json data
                    // This causes to execute doInBackground() of ParserTask class
                    placesParserTask.execute(result);

                    break;

                case PLACES_DETAILS:
                    // Creating ParserTask for parsing Google Places
                    placeDetailsParserTask = new ParserTask(PLACES_DETAILS);

                    // Starting Parsing the JSON string
                    // This causes to execute doInBackground() of ParserTask class
                    placeDetailsParserTask.execute(result);
            }
        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> implements OnMapReadyCallback {

        int parserType = 0;

        public ParserTask(int type) {
            this.parserType = type;
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<HashMap<String, String>> list = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                switch (parserType) {
                    case PLACES:
                        PlaceJSONParser placeJsonParser = new PlaceJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeJsonParser.parse(jObject);
                        break;
                    case PLACES_DETAILS:
                        PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeDetailsJsonParser.parse(jObject);
                }

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            switch (parserType) {
                case PLACES:
                    String[] from = new String[]{"description"};
                    int[] to = new int[]{android.R.id.text1};

                    // Creating a SimpleAdapter for the AutoCompleteTextView
                    SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

                    // Setting the adapter
                    atvPlaces.setAdapter(adapter);
                    break;
                case PLACES_DETAILS:
                    HashMap<String, String> hm = result.get(0);

                    user_latitude = hm.get("lat");
                    user_longitude = hm.get("lng");
                    // Getting latitude from the parsed data
                    double latitude = Double.parseDouble(hm.get("lat"));

                    // Getting longitude from the parsed data
                    double longitude = Double.parseDouble(hm.get("lng"));

                    // Getting reference to the SupportMapFragment of the reviews_view.xml                    //SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

                    //googleMap = supportMapFragment.getMap();
                    map_linear_layout.setVisibility(View.VISIBLE);
                    location_buttons_relative.setVisibility(View.GONE);
                    googleMap.clear();
                    // MapFragment mapFragment = (MapFragment) getFragmentManager()
                    //    .findFragmentById(R.id.map);
                    // mapFragment.getMapAsync(this);
                    setGoogleMap(googleMap, latitude, longitude, atvPlaces.getText().toString());

                    // LatLng point = new LatLng(latitude, longitude);

                    //  CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(point);
                    //  CameraUpdate cameraZoom = CameraUpdateFactory.zoomBy(5);

                    // Showing the user input location in the Google Map


                    // googleMap.moveCamera(cameraPosition);
                    //googleMap.animateCamera(cameraZoom);

                    // MarkerOptions options = new MarkerOptions();
                    //options.position(point);
                    //options.title("Position");
                    //options.snippet("Latitude:"+latitude+",Longitude:"+longitude);

                    // Adding the marker in the Google Map
                    //googleMap.addMarker(options);


                    //
                    //googleMap.clear();


                    break;
            }
        }


        @Override
        public void onMapReady(GoogleMap map) {
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // map.setMyLocationEnabled(true);
            map.setTrafficEnabled(true);
            map.setIndoorEnabled(true);
            map.setBuildingsEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
        }

    }

    private void setGoogleMap(GoogleMap googleMap, double lat_val, double long_val, String data_on_marker) {
        hideKeyboard(LocationSettingsActivity.this);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        MarkerOptions marker = new MarkerOptions().position(new LatLng(lat_val, long_val)).title(data_on_marker).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat_val, long_val), 16.3f));
        radiusInMeters = 0.2 * 1000;
        int strokeColor = Color.parseColor("#59008abd");
        int shadeColor = Color.parseColor("#59008abd");
        CircleOptions circleOptions = new CircleOptions().center(new LatLng(lat_val, long_val)).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);


        circle = googleMap.addCircle(new CircleOptions().center(new LatLng(lat_val, long_val))
                .strokeColor(Color.parseColor("#59008abd")).strokeWidth(2).fillColor(Color.parseColor("#59008abd")).radius(radiusInMeters));

        ValueAnimator vAnimator = new ValueAnimator();
        vAnimator.setIntValues(0, 100);
        vAnimator.setDuration(2000);
        vAnimator.setEvaluator(new IntEvaluator());
        vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                // Log.e("", "" + animatedFraction);
                circle.setRadius(animatedFraction * radiusInMeters);
            }
        });
        vAnimator.start();
        googleMap.addMarker(marker).showInfoWindow();


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void makeMapWithCurrentLocation() {

        map_linear_layout.setVisibility(View.VISIBLE);
        location_buttons_relative.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {

                Location locationData = Commons.getLastKnownLocation(getApplicationContext());
                SharedPreferences fb_data_pref = AppConstants.preferencesData(getApplicationContext());

                if (fb_data_pref.getString(DataKeyValues.FILTERLATTITUDE, null) != null &&
                        !fb_data_pref.getString(DataKeyValues.FILTERLATTITUDE, null).equalsIgnoreCase("0") &&
                        fb_data_pref.getString(DataKeyValues.FILTERLONGITUDE, null) != null &&
                        !fb_data_pref.getString(DataKeyValues.FILTERLONGITUDE, null).equalsIgnoreCase("0")) {

                    user_latitude = fb_data_pref.getString(DataKeyValues.FILTERLATTITUDE, null);
                    user_longitude = fb_data_pref.getString(DataKeyValues.FILTERLONGITUDE, null);

                    String location_name = getDisplyingAddress("" + user_latitude, "" + user_longitude);
                    atvPlaces.setText(location_name);
                    setGoogleMap(googleMap, Double.parseDouble(user_latitude), Double.parseDouble(user_longitude), location_name);
                } else {
                    TrackGPS curent_location = new TrackGPS(LocationSettingsActivity.this);
                    if (curent_location.canGetLocation()) {
                        double latitude = curent_location.getLatitude();
                        double longitude = curent_location.getLongitude();
                        String location_name = getDisplyingAddress("" + latitude, "" + longitude);
                        atvPlaces.setText(location_name);
                        user_latitude = String.valueOf(latitude);
                        user_longitude = String.valueOf(longitude);
                        setGoogleMap(googleMap, latitude, longitude, location_name);


                    } else {
                        curent_location.showSettingsAlert();
                    }
                }

            }

        } else {

            {

                Location locationData = Commons.getLastKnownLocation(getApplicationContext());
                double lat = locationData.getLatitude();
                double lang = locationData.getLongitude();
                SharedPreferences fb_data_pref = AppConstants.preferencesData(getApplicationContext());

                if (fb_data_pref.getString(DataKeyValues.FILTERLATTITUDE, null) != null &&
                        !fb_data_pref.getString(DataKeyValues.FILTERLATTITUDE, null).equalsIgnoreCase("0") &&
                        fb_data_pref.getString(DataKeyValues.FILTERLONGITUDE, null) != null &&
                        !fb_data_pref.getString(DataKeyValues.FILTERLONGITUDE, null).equalsIgnoreCase("0")) {

                    user_latitude = fb_data_pref.getString(DataKeyValues.FILTERLATTITUDE, null);
                    user_longitude = fb_data_pref.getString(DataKeyValues.FILTERLONGITUDE, null);

                    String location_name = getDisplyingAddress("" + user_latitude, "" + user_longitude);
                    atvPlaces.setText(location_name);
                    setGoogleMap(googleMap, Double.parseDouble(user_latitude), Double.parseDouble(user_longitude), location_name);
                } else {
                    TrackGPS curent_location = new TrackGPS(LocationSettingsActivity.this);
                    if (curent_location.canGetLocation()) {
                        double latitude = curent_location.getLatitude();
                        double longitude = curent_location.getLongitude();
                        String location_name = getDisplyingAddress("" + latitude, "" + longitude);
                        atvPlaces.setText(location_name);
                        user_latitude = String.valueOf(latitude);
                        user_longitude = String.valueOf(longitude);
                        setGoogleMap(googleMap, latitude, longitude, location_name);


                    } else {
                        curent_location.showSettingsAlert();
                    }
                }
            }





            /*    //////////////////////////////////////////////////

            TrackGPS curent_location = new TrackGPS(LocationSettingsActivity.this);
            if (curent_location.canGetLocation()) {
                double longitude = curent_location.getLongitude();
                double latitude = curent_location.getLatitude();
                String location_name = getDisplyingAddress("" + latitude, "" + longitude);
                atvPlaces.setText(location_name);
                setGoogleMap(googleMap, latitude, longitude, "My Location");

                user_latitude = String.valueOf(latitude);
                user_longitude = String.valueOf(longitude);


            } else {
                curent_location.showSettingsAlert();
            }*/
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //makeMapWithCurrentLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //makeMapWithCurrentLocation();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //makeMapWithCurrentLocation();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeMapWithCurrentLocation();

                } else {
                    Toast.makeText(getApplicationContext(), "Denied", Toast.LENGTH_SHORT).show();

                }
                break;

        }
    }

    private String getDisplyingAddress(String lat_value, String long_value) {

        Address locationAddressData = LocationAddressData(getApplicationContext(), lat_value, long_value);
        if (locationAddressData != null) {

            String address_data = "";
            if (locationAddressData.getLocality() != null) {
                address_data = locationAddressData.getLocality() + ",";
            }
            if (locationAddressData.getAdminArea() != null) {
                address_data = address_data + locationAddressData.getAdminArea() + ",";
            }

            if (locationAddressData.getCountryCode() != null) {
                address_data = address_data + locationAddressData.getCountryCode();
            }

            return address_data;
        } else {
            return "";
        }

    }

    // and longitude.................................///
    private Address LocationAddressData(Context cntx, String latitude, String logitude) {

        Geocoder geocoder = new Geocoder(cntx, Locale.getDefault());
        Address address = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    Double.parseDouble(latitude), Double.parseDouble(logitude),
                    1);
            if (addressList != null && addressList.size() > 0) {
                address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }
                /*
                 * String locality=address.getLocality()).append("\n");
				 * sb.append(address.getPostalCode()).append("\n");
				 * sb.append(address.getCountryName());
				 */
                // result = sb.toString();
            }
        } catch (IOException e) {
            Log.e("testtag", "Unable connect to Geocoder", e);

        }

        return address;
    }

    public class UpdateLocationDataAsync extends AsyncTask<String, Void, String> {
        String userId;
        String area_of_user;
        String zipcode;
        String cuntrycode;
        String city;
        String state;

        public UpdateLocationDataAsync(String userId, String area_of_user, String zipcode, String cuntrycode, String city, String state) {
            this.userId = userId;
            this.area_of_user = area_of_user;
            this.zipcode = zipcode;
            this.cuntrycode = cuntrycode;
            this.city = city;
            this.state = state;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONObject reqObj = new JSONObject();
            try {
                reqObj.accumulate("latitude", user_latitude);
                reqObj.accumulate("longitude", user_longitude);
                reqObj.accumulate("area", area_of_user);
                reqObj.accumulate("zipcode", zipcode);
                reqObj.accumulate("countrycode", cuntrycode);
                //	reqObj.accumulate("city", city);
                //	reqObj.accumulate("state", state);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ServiceHandler serviceHandler = new ServiceHandler();
            String errorResponce = "";
//            String jsonResponce = connerctServer.ConnectServerPost(AppUtils.UpdateUserLocation(userId), reqObj);
            String service_result = serviceHandler.makeServiceCall(AppConstants.UpdateUserLocation(userId), "POST", reqObj);
            if (service_result != null) {
                try {
                    JSONObject resObj = new JSONObject(service_result);
                    if (service_result.equalsIgnoreCase("exception")) {
                        //CustomToast("Server Problem");

                        errorResponce = "fail";

                    } else {
                        if (resObj.has("status")) {
                            if (resObj.getString("status").equalsIgnoreCase("true")) {
                                SharedPreferences fb_data_pref = getApplicationContext().getSharedPreferences(DataKeyValues.USER_DATA_PREF, MODE_PRIVATE);
                                SharedPreferences.Editor fb_editor = fb_data_pref.edit();
                                fb_editor.putString(DataKeyValues.MY_OWN_LATITUDE_VALUE, user_latitude);
                                fb_editor.putString(DataKeyValues.MY_OWN_LONGITUDE_VALUE, user_longitude);

                                fb_editor.putString(DataKeyValues.OWNER_ZIPCODE, zipcode);
                                fb_editor.putString(DataKeyValues.OWNER_CITY, city);
                                fb_editor.putString(DataKeyValues.OWNER_STATE, state);
                                fb_editor.commit();
                            } else {
                                errorResponce = "fail";
                                //CustomToast("Server Problem");
                            }
                        }
                    }

                } catch (Exception e) {
                    //CustomToast("Server Problem");
                    errorResponce = "fail";

                }
            }


            return errorResponce;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            updatelocation = false;
            if (result.equalsIgnoreCase("errorResponce")) {
                Toast.makeText(getApplicationContext(), "Server Problem", Toast.LENGTH_SHORT).show();
//                CustomToast("Location Details Updated successfully");
            } else {
                Toast.makeText(getApplicationContext(), "Location Details Updated successfully", Toast.LENGTH_SHORT).show();
                //CustomToast("Location Details Updated successfully");

            }

            if (requestFrom != null) {
                if (requestFrom.equalsIgnoreCase("upload_scr")) {
                    /*Intent myIntent =new Intent(LocationSettingActivity.this,ItemUploadingActivity.class);
                    startActivity(myIntent);*/
                    finish();
                } else if (requestFrom.equalsIgnoreCase("display_scr")) {
                    Intent myIntent = new Intent(LocationSettingsActivity.this, NavigationDrawerActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    myIntent.putExtra("EXIT", true);
                    myIntent.putExtra("profile_location", "profile_location");

                    startActivity(myIntent);
                    finish();
                } else if (requestFrom.equalsIgnoreCase("account")) {
                    Intent myIntent = new Intent(LocationSettingsActivity.this, AccountEditActivity.class);
                    startActivity(myIntent);
                    finish();
                }


            } else {
                finish();
            }

        }

    }

    void IntializeMapWithUserDetails(Double user_latitudeOfMap, Double user_longitudeOfMap, String areaOnMap) {

        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        main_googleMap = fm.getMap();
        main_googleMap.clear();

        main_googleMap.getUiSettings().setZoomControlsEnabled(false);


        MarkerOptions marker = new MarkerOptions().position(new LatLng(user_latitudeOfMap, user_longitudeOfMap)).title(areaOnMap).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        main_googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(user_latitudeOfMap, user_longitudeOfMap), 16.3f));

        radiusInMeters = 0.2 * 1000;
        int strokeColor = Color.parseColor("#59008abd");
        int shadeColor = Color.parseColor("#59008abd");
        CircleOptions circleOptions = new CircleOptions().center(new LatLng(user_latitudeOfMap, user_longitudeOfMap)).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);


        circle = main_googleMap.addCircle(new CircleOptions().center(new LatLng(user_latitudeOfMap, user_longitudeOfMap))
                .strokeColor(Color.parseColor("#59008abd")).strokeWidth(2).fillColor(Color.parseColor("#59008abd")).radius(radiusInMeters));

        ValueAnimator vAnimator = new ValueAnimator();
        vAnimator.setIntValues(0, 100);
        vAnimator.setDuration(2000);
        vAnimator.setEvaluator(new IntEvaluator());
        vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                // Log.e("", "" + animatedFraction);
                circle.setRadius(animatedFraction * radiusInMeters);
            }
        });
        vAnimator.start();

        // Marker mPerth = main_googleMap.addMarker(marker);

        main_googleMap.addMarker(marker).showInfoWindow();
        //m.setPosition(new LatLng(user_latitudeOfMap, user_longitudeOfMap));


        //googleMap.addMarker(marker);
        LatLng value = new LatLng(user_latitudeOfMap, user_longitudeOfMap);


        //animateMarker(mPerth,value ,true);


    }

    private void setDefaultLocation() {
        Location locationData = Commons.getLastKnownLocation(getApplicationContext());
        SharedPreferences fb_data_pref = AppConstants.preferencesData(getApplicationContext());

        if (updatelocation) {

            if (fb_data_pref.getString(DataKeyValues.MY_OWN_LATITUDE_VALUE, null) != null &&
                    !fb_data_pref.getString(DataKeyValues.MY_OWN_LONGITUDE_VALUE, null).equalsIgnoreCase("0") &&
                    fb_data_pref.getString(DataKeyValues.MY_OWN_LATITUDE_VALUE, null) != null &&
                    !fb_data_pref.getString(DataKeyValues.MY_OWN_LONGITUDE_VALUE, null).equalsIgnoreCase("0")) {

                user_latitude = fb_data_pref.getString(DataKeyValues.MY_OWN_LATITUDE_VALUE, null);
                user_longitude = fb_data_pref.getString(DataKeyValues.MY_OWN_LONGITUDE_VALUE, null);
                //call alert


            } else {
                if (locationData != null) {
                    user_latitude = String.valueOf(locationData.getLatitude());
                    user_longitude = String.valueOf(locationData.getLongitude());

                    defaultLocationAlert();
                } else {
                    user_latitude = "0";
                    user_longitude = "0";
                    defaultLocationAlert();
                }
            }

        } else {
            if (fb_data_pref.getString(DataKeyValues.FILTERLATTITUDE, null) != null &&
                    !fb_data_pref.getString(DataKeyValues.FILTERLATTITUDE, null).equalsIgnoreCase("0") &&
                    fb_data_pref.getString(DataKeyValues.FILTERLONGITUDE, null) != null &&
                    !fb_data_pref.getString(DataKeyValues.FILTERLONGITUDE, null).equalsIgnoreCase("0")) {

                user_latitude = fb_data_pref.getString(DataKeyValues.FILTERLATTITUDE, null);
                user_longitude = fb_data_pref.getString(DataKeyValues.FILTERLONGITUDE, null);
            } else {
                if (fb_data_pref.getString(DataKeyValues.LATITUDE_VALUE, null) != null &&
                        !fb_data_pref.getString(DataKeyValues.LATITUDE_VALUE, null).equalsIgnoreCase("0") &&
                        fb_data_pref.getString(DataKeyValues.LONGITUDE_VALUE, null) != null &&
                        !fb_data_pref.getString(DataKeyValues.LONGITUDE_VALUE, null).equalsIgnoreCase("0")) {

                    user_latitude = fb_data_pref.getString(DataKeyValues.LATITUDE_VALUE, null);
                    user_longitude = fb_data_pref.getString(DataKeyValues.LONGITUDE_VALUE, null);

                } else if (locationData != null) {
                    user_latitude = String.valueOf(locationData.getLatitude());
                    user_longitude = String.valueOf(locationData.getLongitude());
                } else {
                    user_latitude = "0";
                    user_longitude = "0";
                }
            }
        }

        Address user_adrees_info = DataKeyValues.LocationAddressData(getApplicationContext(), user_latitude, user_longitude);
        String area_data = "";
        String cuntryCode = "";
        if (user_adrees_info != null) {
            if (user_adrees_info.getThoroughfare() != null) {
                area_data = user_adrees_info.getThoroughfare();
            }

            if (user_adrees_info.getLocality() != null) {
                area_data = area_data + "," + user_adrees_info.getLocality();
            }
            if (user_adrees_info.getAdminArea() != null) {
                area_data = area_data + ", " + user_adrees_info.getAdminArea();
            }
            if (user_adrees_info.getPostalCode() != null) {

                area_data = area_data + ", " + user_adrees_info.getPostalCode();
            }
            if (user_adrees_info.getCountryCode() != null) {
                cuntryCode = user_adrees_info.getCountryCode();
            }
            atvPlaces.setText(area_data);

            search_location = area_data;
        } else {
            atvPlaces.setText("");
        }


        IntializeMapWithUserDetails(Double.parseDouble(user_latitude), Double.parseDouble(user_longitude), area_data);


    }

    public void defaultLocationAlert() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LocationSettingsActivity.this);
        alertDialog.setMessage(R.string.default_location_alert_msg);
        alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                atvPlaces.setText("");
                dialog.cancel();


            }
        });


        alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //
                SharedPreferences user_LocationData = AppConstants.preferencesData(getApplicationContext());
                SharedPreferences.Editor LocationData_editor = user_LocationData.edit();
                Address user_adrees_info = DataKeyValues.LocationAddressData(getApplicationContext(), user_latitude, user_longitude);

                String area_of_user = "";
                String user_zipcode = "";
                String search_data = "IN";
                String cuntryCode = null;
                String user_state = "";
                String user_city = "";
                if (user_adrees_info != null) {

                    if (user_adrees_info.getThoroughfare() != null) {
                        search_data = user_adrees_info.getThoroughfare() + ",";
                    }

                    if (user_adrees_info.getAdminArea() != null) {
                        search_data += user_adrees_info.getAdminArea() + ",";
                        user_state = user_adrees_info.getAdminArea();
                    }
                    if (user_adrees_info.getLocality() != null) {
                        LocationData_editor.putString(DataKeyValues.LOCATION, user_adrees_info.getLocality());
                        area_of_user = user_adrees_info.getLocality();
                        search_data += user_adrees_info.getLocality();
                        user_city = user_adrees_info.getLocality();
                    } else {
                        LocationData_editor.putString(DataKeyValues.LOCATION, atvPlaces.getText().toString());
                    }

                    if (user_adrees_info.getPostalCode() != null) {
                        LocationData_editor.putString(DataKeyValues.ZIPCODE, user_adrees_info.getPostalCode());
                        user_zipcode = user_adrees_info.getPostalCode();
                        search_data += "," + user_adrees_info.getPostalCode();
                    } else {
                        LocationData_editor.putString(DataKeyValues.ZIPCODE, "");
                    }

                    if (user_adrees_info.getCountryCode() != null) {
                        cuntryCode = user_adrees_info.getCountryCode();
                        LocationData_editor.putString(DataKeyValues.CUNTRY_CODE, cuntryCode);
                        SharedPreferences shPref = getApplicationContext().getSharedPreferences("SearchData", MODE_PRIVATE);
                        SharedPreferences.Editor editShPref = shPref.edit();
                        editShPref.putString(DataKeyValues.PRICE_TYPE, cuntryCode);
                        editShPref.commit();
                    }
                }
                LocationData_editor.commit();
                if (user_LocationData.getBoolean(DataKeyValues.LOGIN_STATUS, false)) {

                    if (user_LocationData.getString(DataKeyValues.OWNER_ID, null) != null) {
                        LocationData_editor.putString(DataKeyValues.MYLOCATION, area_of_user + " " + user_zipcode);
                        LocationData_editor.commit();
                        new UpdateLocationDataAsync(user_LocationData.getString(DataKeyValues.OWNER_ID, null), area_of_user, user_zipcode, cuntryCode, user_city, user_state).execute();
                    }

                } else {

                    if (requestFrom != null) {
                        if (requestFrom.equalsIgnoreCase("upload_scr")) {
                            /*Intent myIntent =new Intent(LocationSettingActivity.this,ItemUploadingActivity.class);
                            startActivity(myIntent);*/
                            finish();
                        } else if (requestFrom.equalsIgnoreCase("display_scr")) {
                            Intent myIntent = new Intent(LocationSettingsActivity.this, NavigationDrawerActivity.class);
                            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            myIntent.putExtra("EXIT", true);
                            myIntent.putExtra("profile_location", "profile_location");
                            startActivity(myIntent);

                            finish();
                        } else if (requestFrom.equalsIgnoreCase("display_scr")) {
                            Intent myIntent = new Intent(LocationSettingsActivity.this, AccountEditActivity.class);
                            startActivity(myIntent);
                            finish();
                        } else if (requestFrom.equalsIgnoreCase("description_scr")) {
                            Intent myIntent = new Intent(LocationSettingsActivity.this, AccountEditActivity.class);
                            startActivity(myIntent);
                            finish();
                        } else if (requestFrom.equalsIgnoreCase("description_scr")) {
                            Intent myIntent = new Intent(LocationSettingsActivity.this, AccountEditActivity.class);
                            startActivity(myIntent);
                            finish();
                        }

                    }

                }
            }
        });


        // Showing Alert Message
        alertDialog.show();
    }


    public class LocationAsyncTask extends AsyncTask<String, Void, String> {
        String key;


        public LocationAsyncTask(String key) {
            this.key = key;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            String response = "";
            InputStream inputStream = null;

            JSONObject reqObj = new JSONObject();
            try {
                reqObj.accumulate(DataKeyValues.ITEM_GET_LAT, user_latitude);
                reqObj.accumulate(DataKeyValues.ITEM_GET_LANG, user_longitude);
                reqObj.accumulate("requestFrom", key);


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ServiceHandler serviceHandler = new ServiceHandler();

                response = serviceHandler.makeServiceCall(AppConstants.saveFiltersUrl(mainUserId), "POST", reqObj);
                Log.i("responce", "" + response);
            } catch (Exception ex) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        utils.showToast("Server Not responding, Please check whether your server is running or not");
                    }
                });
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equalsIgnoreCase("errorResponce")) {
                Toast.makeText(getApplicationContext(), "Server Problem", Toast.LENGTH_SHORT).show();
//                CustomToast("Location Details Updated successfully");
            } else if (key.equalsIgnoreCase("set_location")) {
                //ServicesFragment.isServiceFilter=true;
                Intent myIntent = new Intent(LocationSettingsActivity.this, NavigationDrawerActivity.class);
                myIntent.putExtra("isFirstTime", false);
                startActivity(myIntent);
                finish();
            } else {

                Address user_adrees_info = DataKeyValues.LocationAddressData(getApplicationContext(), user_latitude, user_longitude);


                Intent myIntent = new Intent(LocationSettingsActivity.this, ServiceFilteringActivity.class);
                //myIntent.putExtra("area_of_user_search",area_of_user+","+area_of_user);
                startActivity(myIntent);
                finish();

            }


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (requestFrom.equalsIgnoreCase("set_location")) {
            Intent myIntent = new Intent(LocationSettingsActivity.this, NavigationDrawerActivity.class);
            startActivity(myIntent);
            finish();

        } else if (requestFrom.equalsIgnoreCase("search")) {

            Intent myIntent = new Intent(LocationSettingsActivity.this, ServiceFilteringActivity.class);
            startActivity(myIntent);
            finish();
        } else {
            finish();
        }
    }
}
