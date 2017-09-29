package Fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.askntake.admin.askntake.AddServiceActivity;
import com.askntake.admin.askntake.MessagesActivity;
import com.askntake.admin.askntake.R;
import com.askntake.admin.askntake.SearchCategoriesActivity;
import com.askntake.admin.askntake.SocialMediaRegistrationsActivity;
import com.askntake.admin.askntake.TrackGPS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Adapters.ServicesCardAdapter;
import AppUtils.AppConstants;
import AppUtils.Commons;
import AppUtils.ConnectionDetector;
import AppUtils.DataKeyValues;
import AppUtils.GridSpacingItemDecoration;
import AppUtils.ServiceHandler;
import AppUtils.Utils;
import Pojo.ItemServicePojo;
import Pojo.ServicesFiteringDataPojo;

public class ServicesFragment extends Fragment implements SearchView.OnQueryTextListener {
    public static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 123;
    private RecyclerView recyler_services;
    private ServicesCardAdapter servicesCardAdapter;
    private List<ItemServicePojo> itemServicePojoList;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    BottomNavigationView bottomNavigation;
    SharedPreferences login_preferenes;

    ConnectionDetector internetConnection;
    TrackGPS gps;

    Dialog Internetdialog;
    ServicesFiteringDataPojo servicesFiteringDataPojo;
    String userMainId;
    String user_latitude = "0", user_longitude = "0";
    String set_user_latitude = "0", set_user_longitude = "0";
    double user_latitude1 = 0.0, user_longitude1 = 0.0;
    String profile_location;
    public static boolean isServiceFilter;
    boolean firstTime = true;

    private final int LOCATION_REQUEST_CODE = 2;

    TextView no_service_layout;


    //    Location location ;
    public static ServicesFragment newInstance(ServicesFiteringDataPojo servicesFiteringDataPojo, String profile_location, boolean serviceFilter) {
        ServicesFragment f = new ServicesFragment();
        Bundle bdl = new Bundle(2);
        bdl.putSerializable("filterdata", servicesFiteringDataPojo);
        bdl.putString("profile_location", profile_location);
        f.setArguments(bdl);
        isServiceFilter = serviceFilter;
        profile_location = profile_location;
        return f;
    }
   /* public ServicesFragment() {

        if(getArguments().getSerializable("filterdata")!=null){
            servicesFiteringDataPojo= (ServicesFiteringDataPojo)getArguments().getSerializable("filterdata");
            Log.i("pojo:",""+servicesFiteringDataPojo);
        }


    }*/
   /* public ServicesFragment(ServicesFiteringDataPojo servicesFiteringDataPojo) {

        this.servicesFiteringDataPojo = servicesFiteringDataPojo;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().getSerializable("filterdata") != null) {
            servicesFiteringDataPojo = (ServicesFiteringDataPojo) getArguments().getSerializable("filterdata");
            Log.i("pojo:", "" + servicesFiteringDataPojo);
        }
        if (getArguments().getString("profile_location") != null) {
            profile_location = getArguments().getString("profile_location");
            Log.i("profile_location:", "" + profile_location);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_services, null);


        no_service_layout = (TextView) root_view.findViewById(R.id.no_service_layout);

        internetConnection = new ConnectionDetector(getActivity());

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_LOCATION)!=PackageManager.PERMISSION_GRANTED)
            {
                gpstrack();
            }

        }else
        {*/
        askPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE);

        gpstrack();

//        }


        Location locationData = Commons.getLastKnownLocation(getActivity());

        if (locationData != null) {
            set_user_latitude = String.valueOf(locationData.getLatitude());
            set_user_longitude = String.valueOf(locationData.getLongitude());
        }

        /*try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/

        swipeRefreshLayout = (SwipeRefreshLayout) root_view.findViewById(R.id.swipeRefreshLayout);
        setHasOptionsMenu(true);
        itemServicePojoList = new ArrayList<>();
        recyler_services = (RecyclerView) root_view.findViewById(R.id.recyler_services);
        servicesCardAdapter = new ServicesCardAdapter(getActivity(), itemServicePojoList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyler_services.setLayoutManager(mLayoutManager);
        recyler_services.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
        recyler_services.setItemAnimator(new DefaultItemAnimator());
        recyler_services.setAdapter(servicesCardAdapter);

        SharedPreferences fbpref = AppConstants.preferencesData(getActivity());
        userMainId = fbpref.getString(DataKeyValues.OWNER_ID, null);

        if (userMainId == null) {
            userMainId = "-1";
        }
        SharedPreferences user_LocationData = AppConstants.preferencesData(getActivity());


        user_latitude = user_LocationData.getString(DataKeyValues.FILTERLATTITUDE, null);
        user_longitude = user_LocationData.getString(DataKeyValues.FILTERLONGITUDE, null);


        set_user_latitude = user_LocationData.getString(DataKeyValues.SET_LOC_LATTITUDE, null);
        set_user_longitude = user_LocationData.getString(DataKeyValues.SET_LOC_LONGITUDE, null);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemServicePojoList.clear();

                if (internetConnection.isConnectingToInternet(getActivity())) {
                    if (isServiceFilter) {
                        gpstrack();
                        new getItemsDispalyAysnc().execute();
                    } else {

                        gps = new TrackGPS(getActivity());

                        // check if GPS enabled
                        if (gps.canGetLocation()) {

                            user_latitude1 = gps.getLatitude();
                            user_longitude1 = gps.getLongitude();
                        }
                        SharedPreferences user_LocationData = AppConstants.preferencesData(getActivity());
                        SharedPreferences.Editor LocationData_editor = user_LocationData.edit();
                        LocationData_editor.putString("user_latitude1", String.valueOf(user_latitude1));
                        LocationData_editor.putString("user_longitude1", String.valueOf(user_longitude1));
                        LocationData_editor.apply();
                        new SaveFiltersAsynchTask().execute();
                    }

//                    Internetdialog.dismiss();
                } else {
                    InternetConnectionAlert();
                }

            }
        });
        bottomNavigation =
                (BottomNavigationView) root_view.findViewById(R.id.bottom_navigation);
        bottomNavigation.setVisibility(View.GONE);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleBottomNavigationItemSelected(item);
                return true;
            }
        });

        if (internetConnection.isConnectingToInternet(getActivity())) {
            if (isServiceFilter) {
                gpstrack();
                new getItemsDispalyAysnc().execute();
            } else {
                new SaveFiltersAsynchTask().execute();
            }

            // Internetdialog.dismiss();
        } else {
            InternetConnectionAlert();
        }
        //prepareStoresData();

        return root_view;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        final List<ItemServicePojo> filteredModelList = filter(itemServicePojoList, newText);
        servicesCardAdapter.setFilter(filteredModelList);
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<ItemServicePojo> filter(List<ItemServicePojo> models, String query) {
        query = query.toLowerCase();
        final List<ItemServicePojo> filteredModelList = new ArrayList<>();
        for (ItemServicePojo model : models) {
            final String text = model.getService_name().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                servicesCardAdapter.setFilter(itemServicePojoList);
                return true; // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true; // Return true to expand action view
            }
        });
    }

    private class getItemsDispalyAysnc extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", false, false);
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = prepareDefaultRequestObj();
            //String service_result = serviceHandler.makeServiceCall(AppConstants.ITEMS_DISPLAY_URL, "POST", requestObject);
            String service_result = serviceHandler.makeServiceCall(AppConstants.loadServicesUrl(userMainId), "POST", requestObject);
            return service_result;

        }

        @Override
        protected void onPostExecute(String result) {

            if (result == null) {
                Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_SHORT).show();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            } else {

                super.onPostExecute(result);
                progressDialog.dismiss();
                itemServicePojoList.clear();
                onItemsLoadComplete();
                JSONObject jsObj = null;

                try {
                    jsObj = new JSONObject(result);

                    if (jsObj.length() == 0) {


                        no_service_layout.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);

                    } else if (jsObj.has("services")) {
                        JSONObject innerJsonObject = jsObj.getJSONObject("services");
                        ItemServicePojo buyAndSellItems = new ItemServicePojo();
                        buyAndSellItems.setService_id(innerJsonObject.getString("serviceid"));
                        buyAndSellItems.setService_owner_id(innerJsonObject.getString("ownerid"));
                        buyAndSellItems.setService_name(innerJsonObject.getString("servicename"));
                        buyAndSellItems.setProvider_name(innerJsonObject.getString("providername"));
                        buyAndSellItems.setService_image(innerJsonObject.getString("mainImage"));
                        buyAndSellItems.setService_owner_online_status(innerJsonObject.getBoolean("ownerOnlineStatus"));
                        buyAndSellItems.setService_distance(innerJsonObject.getString("distance"));
                        buyAndSellItems.setService_distance_type(innerJsonObject.getString("distanceType"));
                        buyAndSellItems.setService_views_count(innerJsonObject.getString("views"));
                        buyAndSellItems.setService_favorites(innerJsonObject.getString("favorites"));
                        buyAndSellItems.setService_location(innerJsonObject.getString("area"));
                        buyAndSellItems.setDays_old(innerJsonObject.getString("days"));
                        buyAndSellItems.setRating(innerJsonObject.getString("rating"));
                        buyAndSellItems.setReviewsCount(innerJsonObject.getString("reviewsCount"));

                        itemServicePojoList.add(buyAndSellItems);
                        servicesCardAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Services Not Found", Toast.LENGTH_SHORT).show();
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    try {

                        JSONObject main_jsoObject = new JSONObject(result);


                        if (jsObj.has("services")) {

                            JSONArray productsArray = main_jsoObject.getJSONArray("services");

                            int j = productsArray.length();


                            if (j != 0) {
                                for (int i = 0; i < productsArray.length(); i++) {

                                    JSONObject innerJsonObject = productsArray.getJSONObject(i);
                                    ItemServicePojo buyAndSellItems = new ItemServicePojo();
                                    buyAndSellItems.setService_id(innerJsonObject.getString("serviceid"));
                                    buyAndSellItems.setService_owner_id(innerJsonObject.getString("ownerid"));
                                    buyAndSellItems.setService_name(innerJsonObject.getString("servicename"));
                                    buyAndSellItems.setProvider_name(innerJsonObject.getString("providername"));
                                    buyAndSellItems.setService_image(innerJsonObject.getString("mainImage"));
                                    buyAndSellItems.setService_owner_online_status(innerJsonObject.getBoolean("ownerOnlineStatus"));
                                    buyAndSellItems.setService_distance(innerJsonObject.getString("distance"));
                                    buyAndSellItems.setService_distance_type(innerJsonObject.getString("distanceType"));
                                    buyAndSellItems.setService_views_count(innerJsonObject.getString("views"));
                                    buyAndSellItems.setService_favorites(innerJsonObject.getString("favorites"));
                                    buyAndSellItems.setService_location(innerJsonObject.getString("area"));
                                    buyAndSellItems.setDays_old(innerJsonObject.getString("days"));
                                    buyAndSellItems.setRating(innerJsonObject.getString("rating"));
                                    buyAndSellItems.setReviewsCount(innerJsonObject.getString("reviewsCount"));

                                    itemServicePojoList.add(buyAndSellItems);
                                }

                                servicesCardAdapter.notifyDataSetChanged();
                            } else {
                                no_service_layout.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setVisibility(View.GONE);
                            }
                        }

                    } catch (Exception eq) {
                        eq.printStackTrace();

                    }

                }

            }

        }
    }

    private JSONObject prepareDefaultRequestObj() {

        JSONObject requestObject = null;
        try {
            requestObject = new JSONObject();


            if (servicesFiteringDataPojo != null && isServiceFilter) {
                requestObject.accumulate("requestFrom", "filter");
                requestObject.accumulate("filter", true);
                requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, user_latitude);
                requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, user_longitude);
                requestObject.accumulate(DataKeyValues.ITEM_GET_RANGE, servicesFiteringDataPojo.getRange());
                requestObject.accumulate(DataKeyValues.ITEM_GET_CATEGORY, servicesFiteringDataPojo.getCategory());
                requestObject.accumulate(DataKeyValues.ITEM_SERVICE_NAME, servicesFiteringDataPojo.getService_name());
                requestObject.accumulate(DataKeyValues.ITEM_SERVICE_PROVIDER_NAME, "");
                requestObject.accumulate(DataKeyValues.ITEM_TIME_RANGE, servicesFiteringDataPojo.getListed_with_in());
                requestObject.accumulate(DataKeyValues.ITEM_GET_DISTANCEIN, servicesFiteringDataPojo.getKilomitersOrMiles());
                requestObject.accumulate(DataKeyValues.ITEM_GET_RATING, servicesFiteringDataPojo.getRating());

                JSONArray jsonSubCategoriesArray = new JSONArray();
                for (int i = 0; i < servicesFiteringDataPojo.getSubCategoriesList().size(); i++) {
                    jsonSubCategoriesArray.put(servicesFiteringDataPojo.getSubCategoriesList().get(i));
                }
                requestObject.accumulate(DataKeyValues.ITEM_GET_SUBCATEGORIES, jsonSubCategoriesArray);
                JSONArray jsonSategoriesPositionArray = new JSONArray();
                for (int i = 0; i < servicesFiteringDataPojo.getSubcategoriesPositions().size(); i++) {
                    jsonSategoriesPositionArray.put(servicesFiteringDataPojo.getSubcategoriesPositions().get(i));
                }

                JSONArray jsonCategoriesArray = new JSONArray();
                for (int i = 0; i < servicesFiteringDataPojo.getCategoriesList().size(); i++) {
                    jsonCategoriesArray.put(servicesFiteringDataPojo.getCategoriesList().get(i));
                }
                requestObject.accumulate(DataKeyValues.ITEMS_GET_CATEGORIES, jsonCategoriesArray);


                requestObject.accumulate(DataKeyValues.POSITIONS, jsonSategoriesPositionArray);

            } else {

                if (set_user_latitude == null && set_user_longitude == null) {

                    requestObject.accumulate(DataKeyValues.PROFILE_LOCATION_ITEMS, profile_location);
//                requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, "17.432514");
//                requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, "78.447863");
//                requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, locationData.getLatitude());
//                requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, locationData.getLongitude());
                    requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, user_latitude1);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, user_longitude1);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_RANGE, "500");
                    requestObject.accumulate(DataKeyValues.ITEM_GET_CATEGORY, "");
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_NAME, "");
                    JSONArray jsonCategoriesArray = new JSONArray();
                    requestObject.accumulate(DataKeyValues.ITEMS_GET_CATEGORIES, jsonCategoriesArray);
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_PROVIDER_NAME, "");
                    requestObject.accumulate(DataKeyValues.ITEM_TIME_RANGE, 0);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_DISTANCEIN, "M");
                    JSONArray jsonSubCategoriesArray = new JSONArray();
                    requestObject.accumulate(DataKeyValues.ITEM_GET_SUBCATEGORIES, jsonSubCategoriesArray);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_RATING, "0.0");

                    final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(DataKeyValues.USER_DATA_PREF, getActivity().MODE_PRIVATE);
                    if (sharedPreferences.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {
                        if (firstTime) {
                            requestObject.accumulate("filter", false);
                        }
                    }

                    //requestObject.accumulate("filter", false);

                    JSONArray jsonSategoriesPositionArray = new JSONArray();
                    requestObject.accumulate(DataKeyValues.POSITIONS, jsonSategoriesPositionArray);
                    firstTime = false;
                } else {
                    requestObject.accumulate("requestFrom", "set_location");
                    requestObject.accumulate(DataKeyValues.PROFILE_LOCATION_ITEMS, profile_location);
//                requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, "17.432514");
//                requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, "78.447863");
//                requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, locationData.getLatitude());
//                requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, locationData.getLongitude());
                    requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, set_user_latitude);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, set_user_longitude);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_RANGE, "500");
                    requestObject.accumulate(DataKeyValues.ITEM_GET_CATEGORY, "");
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_NAME, "");
                    JSONArray jsonCategoriesArray = new JSONArray();
                    requestObject.accumulate(DataKeyValues.ITEMS_GET_CATEGORIES, jsonCategoriesArray);
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_PROVIDER_NAME, "");
                    requestObject.accumulate(DataKeyValues.ITEM_TIME_RANGE, 0);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_DISTANCEIN, "M");
                    JSONArray jsonSubCategoriesArray = new JSONArray();
                    requestObject.accumulate(DataKeyValues.ITEM_GET_SUBCATEGORIES, jsonSubCategoriesArray);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_RATING, "0.0");

                    JSONArray jsonSategoriesPositionArray = new JSONArray();
                    requestObject.accumulate(DataKeyValues.POSITIONS, jsonSategoriesPositionArray);
                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return requestObject;
    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    void onItemsLoadComplete() {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void handleBottomNavigationItemSelected(MenuItem item) {

        login_preferenes = this.getActivity().getSharedPreferences(DataKeyValues.USER_DATA_PREF, getContext().MODE_PRIVATE);

        if (login_preferenes.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {

            switch (item.getItemId()) {
                case R.id.action_add:
                    if (login_preferenes.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {
                        Intent addServiceIntent = new Intent(getActivity(), AddServiceActivity.class);
                        startActivity(addServiceIntent);
                    } else {
                        Intent registrationActivity = new Intent(getActivity(), SocialMediaRegistrationsActivity.class);
                        startActivity(registrationActivity);
                        getActivity().finish();
                    }
                    break;
                case R.id.action_messages:

                    Intent messagesIntent = new Intent(getActivity(), MessagesActivity.class);
                    startActivity(messagesIntent);
                    break;

                case R.id.action_categories:

                    Intent contactus_intent = new Intent(getActivity(), SearchCategoriesActivity.class);
                    //contactus_intent.putExtra("filterdata", filterPojo);
                    startActivity(contactus_intent);
                    break;
            }

        } else {
            Intent registrationActivity = new Intent(getActivity(), SocialMediaRegistrationsActivity.class);
            startActivity(registrationActivity);
        }

    }


    void InternetConnectionAlert() {

        Internetdialog = new Dialog(getActivity()); /*android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);*/
        Internetdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Internetdialog.setContentView(R.layout.net_connection_alert_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Internetdialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        Internetdialog.getWindow().setAttributes(lp);
        Internetdialog.setCancelable(false);
        Internetdialog.setCanceledOnTouchOutside(false);

        ImageView wifi_error_img = (ImageView) Internetdialog.findViewById(R.id.wifi_error_img);
        TextView wifi_retry_btn = (TextView) Internetdialog.findViewById(R.id.wifi_retry_btn);
        wifi_retry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.butonanim));
                if (internetConnection.isConnectingToInternet(getActivity())) {
                    if (isServiceFilter) {
                        gpstrack();

                        new getItemsDispalyAysnc().execute();
                    } else {
                        gps = new TrackGPS(getActivity());

                        // check if GPS enabled
                        if (gps.canGetLocation()) {

                            user_latitude1 = gps.getLatitude();
                            user_longitude1 = gps.getLongitude();
                        }
                        SharedPreferences user_LocationData = AppConstants.preferencesData(getActivity());
                        SharedPreferences.Editor LocationData_editor = user_LocationData.edit();
                        LocationData_editor.putString("user_latitude1", String.valueOf(user_latitude1));
                        LocationData_editor.putString("user_longitude1", String.valueOf(user_longitude1));
                        LocationData_editor.apply();
                        new SaveFiltersAsynchTask().execute();
                    }

                    Internetdialog.dismiss();

                }

            }
        });
        Internetdialog.show();
    }


    public void gpstrack() {
        gps = new TrackGPS(getActivity());

        // check if GPS enabled
        if (gps.canGetLocation()) {

            user_latitude1 = gps.getLatitude();
            user_longitude1 = gps.getLongitude();

            if (user_latitude1 == 0.0 && user_longitude1 == 0.0) {
                gpstrack();
            } else {
                user_latitude1 = gps.getLatitude();
                user_longitude1 = gps.getLongitude();

                Address user_adrees_info = LocationAddressData(getActivity(), String.valueOf(user_latitude1), String.valueOf(user_longitude1));
                SharedPreferences user_LocationData = AppConstants.preferencesData(getActivity());
                SharedPreferences.Editor LocationData_editor = user_LocationData.edit();
                LocationData_editor.putString("user_latitude1", String.valueOf(user_latitude1));
                LocationData_editor.putString("user_longitude1", String.valueOf(user_longitude1));
                LocationData_editor.apply();
                String cuntryCode = null;
                if (user_adrees_info != null) {
                    if (user_adrees_info.getCountryCode() != null) {
                        cuntryCode = user_adrees_info.getCountryCode();
                        LocationData_editor.putString(DataKeyValues.CUNTRY_CODE1, cuntryCode);
                        LocationData_editor.commit();
                        /*SharedPreferences shPref = getActivity().getSharedPreferences("SearchData", getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor editShPref = shPref.edit();
                        editShPref.putString(DataKeyValues.PRICE_TYPE, cuntryCode);
                        editShPref.commit();*/
                    }
                }

            }
            // Toast.makeText(getActivity(), "Your Location is - \nLat: " + user_latitude + "\nLong: " + user_longitude, Toast.LENGTH_LONG).show();
        } else {
            gps.showSettingsAlert();


        }
    }

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

    private class SaveFiltersAsynchTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            String response = "";
            InputStream inputStream = null;
            try {
                ServiceHandler serviceHandler = new ServiceHandler();

                response = serviceHandler.makeServiceCall(AppConstants.saveFiltersUrl(userMainId), "POST", getRequestJsonObject());
                Log.i("responce", "" + response);
            } catch (Exception ex) {
                final Utils utils = new Utils(getActivity());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        utils.showToast("Server Not responding, Please check whether your server is running or not");
                    }
                });
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {

                new getItemsDispalyAysnc().execute();
            }
        }

        private JSONObject getRequestJsonObject() {

            JSONObject requestObject = null;
            try {
                requestObject = new JSONObject();
                requestObject.accumulate("requestFrom", "filter");


                if (!isServiceFilter && set_user_latitude == null && set_user_longitude == null) {
                    Map<String, Map<String, String>> categoriesList = Commons.getCategories(getActivity());

                    requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, user_latitude1);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, user_longitude1);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_RANGE, "500");
                    requestObject.accumulate(DataKeyValues.ITEM_GET_CATEGORY, "");
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_NAME, "");
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_PROVIDER_NAME, "");
                    requestObject.accumulate(DataKeyValues.ITEM_TIME_RANGE, 0);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_DISTANCEIN, "M");
                    JSONArray jsonSubCategoriesArray = new JSONArray();

                    JSONArray jsonSategoriesPositionArray = new JSONArray();
                    jsonSategoriesPositionArray.put("0,0");
                    jsonSategoriesPositionArray.put("0,1");
                    jsonSategoriesPositionArray.put("1,0");
                    jsonSategoriesPositionArray.put("2,0");
                    jsonSategoriesPositionArray.put("2,1");
                    jsonSategoriesPositionArray.put("3,0");
                    jsonSategoriesPositionArray.put("3,1");
                    jsonSategoriesPositionArray.put("4,0");
                    jsonSategoriesPositionArray.put("4,1");
                    jsonSategoriesPositionArray.put("5,0");
                    jsonSategoriesPositionArray.put("5,1");
                    jsonSategoriesPositionArray.put("5,2");
                    jsonSategoriesPositionArray.put("6,0");
                    jsonSategoriesPositionArray.put("6,1");
                    jsonSategoriesPositionArray.put("6,2");
                    jsonSategoriesPositionArray.put("7,0");
                    jsonSategoriesPositionArray.put("7,1");
                    jsonSategoriesPositionArray.put("7,2");
                    jsonSategoriesPositionArray.put("8,0");
                    jsonSategoriesPositionArray.put("8,1");
                    jsonSategoriesPositionArray.put("8,2");
                    jsonSategoriesPositionArray.put("9,0");
                    jsonSategoriesPositionArray.put("9,1");
                    jsonSategoriesPositionArray.put("9,2");
                    jsonSategoriesPositionArray.put("9,3");
                    jsonSategoriesPositionArray.put("9,4");
                    jsonSategoriesPositionArray.put("10,0");
                    jsonSategoriesPositionArray.put("10,1");
                    jsonSategoriesPositionArray.put("10,2");
                    jsonSategoriesPositionArray.put("10,3");
                    jsonSategoriesPositionArray.put("11,0");
                    jsonSategoriesPositionArray.put("11,1");
                    jsonSategoriesPositionArray.put("11,2");
                    jsonSategoriesPositionArray.put("11,3");
                    jsonSategoriesPositionArray.put("12,0");
                    jsonSategoriesPositionArray.put("12,1");
                    jsonSategoriesPositionArray.put("13,0");
                    jsonSategoriesPositionArray.put("14,0");
                    jsonSategoriesPositionArray.put("14,1");
                    jsonSategoriesPositionArray.put("14,2");
                    jsonSategoriesPositionArray.put("15,0");
                    jsonSategoriesPositionArray.put("15,1");

                    requestObject.accumulate(DataKeyValues.POSITIONS, jsonSategoriesPositionArray);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_RATING, "0.0");

                    JSONArray jsonCategoriesArray = new JSONArray();
                   /* for (int i = 0; i < filterPojoObj.getCategoriesList().size(); i++) {
                        jsonCategoriesArray.put(filterPojoObj.getCategoriesList().get(i));
                    }*/
                    int counter = 0;
                    for (Map.Entry<String, Map<String, String>> entry : categoriesList.entrySet()) {

                        if (counter > 0) {
                            String category = entry.getKey();
                            jsonCategoriesArray.put(category);
                            int flag = 0;
                            for (Map.Entry<String, String> subData : entry.getValue().entrySet()) {

                                if (flag != 0) {
                                    jsonSubCategoriesArray.put(subData.getKey());
                                }
                                flag = flag + 1;
                            }
                        }
                        counter = counter + 1;
                    }
                    requestObject.accumulate(DataKeyValues.ITEMS_GET_CATEGORIES, jsonCategoriesArray);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_SUBCATEGORIES, jsonSubCategoriesArray);

                } else {
                    Map<String, Map<String, String>> categoriesList = Commons.getCategories(getActivity());

                    requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, set_user_latitude);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, set_user_longitude);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_RANGE, "500");
                    requestObject.accumulate(DataKeyValues.ITEM_GET_CATEGORY, "");
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_NAME, "");
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_PROVIDER_NAME, "");
                    requestObject.accumulate(DataKeyValues.ITEM_TIME_RANGE, 0);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_DISTANCEIN, "M");
                    JSONArray jsonSubCategoriesArray = new JSONArray();

                    JSONArray jsonSategoriesPositionArray = new JSONArray();
                    jsonSategoriesPositionArray.put("0,0");
                    jsonSategoriesPositionArray.put("0,1");
                    jsonSategoriesPositionArray.put("1,0");
                    jsonSategoriesPositionArray.put("2,0");
                    jsonSategoriesPositionArray.put("2,1");
                    jsonSategoriesPositionArray.put("3,0");
                    jsonSategoriesPositionArray.put("3,1");
                    jsonSategoriesPositionArray.put("4,0");
                    jsonSategoriesPositionArray.put("4,1");
                    jsonSategoriesPositionArray.put("5,0");
                    jsonSategoriesPositionArray.put("5,1");
                    jsonSategoriesPositionArray.put("5,2");
                    jsonSategoriesPositionArray.put("6,0");
                    jsonSategoriesPositionArray.put("6,1");
                    jsonSategoriesPositionArray.put("6,2");
                    jsonSategoriesPositionArray.put("7,0");
                    jsonSategoriesPositionArray.put("7,1");
                    jsonSategoriesPositionArray.put("7,2");
                    jsonSategoriesPositionArray.put("8,0");
                    jsonSategoriesPositionArray.put("8,1");
                    jsonSategoriesPositionArray.put("8,2");
                    jsonSategoriesPositionArray.put("9,0");
                    jsonSategoriesPositionArray.put("9,1");
                    jsonSategoriesPositionArray.put("9,2");
                    jsonSategoriesPositionArray.put("9,3");
                    jsonSategoriesPositionArray.put("9,4");
                    jsonSategoriesPositionArray.put("10,0");
                    jsonSategoriesPositionArray.put("10,1");
                    jsonSategoriesPositionArray.put("10,2");
                    jsonSategoriesPositionArray.put("10,3");
                    jsonSategoriesPositionArray.put("11,0");
                    jsonSategoriesPositionArray.put("11,1");
                    jsonSategoriesPositionArray.put("11,2");
                    jsonSategoriesPositionArray.put("11,3");
                    jsonSategoriesPositionArray.put("12,0");
                    jsonSategoriesPositionArray.put("12,1");
                    jsonSategoriesPositionArray.put("13,0");
                    jsonSategoriesPositionArray.put("14,0");
                    jsonSategoriesPositionArray.put("14,1");
                    jsonSategoriesPositionArray.put("14,2");
                    jsonSategoriesPositionArray.put("15,0");
                    jsonSategoriesPositionArray.put("15,1");

                    requestObject.accumulate(DataKeyValues.POSITIONS, jsonSategoriesPositionArray);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_RATING, "0.0");

                    JSONArray jsonCategoriesArray = new JSONArray();
                   /* for (int i = 0; i < filterPojoObj.getCategoriesList().size(); i++) {
                        jsonCategoriesArray.put(filterPojoObj.getCategoriesList().get(i));
                    }*/
                    int counter = 0;
                    for (Map.Entry<String, Map<String, String>> entry : categoriesList.entrySet()) {

                        if (counter > 0) {
                            String category = entry.getKey();
                            jsonCategoriesArray.put(category);
                            int flag = 0;
                            for (Map.Entry<String, String> subData : entry.getValue().entrySet()) {

                                if (flag != 0) {
                                    jsonSubCategoriesArray.put(subData.getKey());
                                }
                                flag = flag + 1;
                            }
                        }
                        counter = counter + 1;
                    }
                    requestObject.accumulate(DataKeyValues.ITEMS_GET_CATEGORIES, jsonCategoriesArray);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_SUBCATEGORIES, jsonSubCategoriesArray);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return requestObject;
        }

    }

   /* private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }
    private boolean canAccessCoreLocation() {
        return (hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
    }
    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), perm));
    }*/

    private void askPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        } else {
            //Toast.makeText(getActivity(), "Permissions alrady granted", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, proceed to the normal flow.

                    Toast.makeText(getActivity(), "Location Permissions granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "Location Permissions granted", Toast.LENGTH_SHORT).show();
                }
        }

    }

}
