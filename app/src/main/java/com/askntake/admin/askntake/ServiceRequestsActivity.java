package com.askntake.admin.askntake;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Adapters.ServiceRequestsCardAdapter;
import AppUtils.AppConstants;
import AppUtils.ConnectionDetector;
import AppUtils.DataKeyValues;
import AppUtils.GridSpacingItemDecoration;
import AppUtils.ServiceHandler;
import Pojo.ServiceRequestsPojo;

public class ServiceRequestsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView recyler_service_requests;
    private ServiceRequestsCardAdapter serviceRequestsCardAdapter;
    private List<ServiceRequestsPojo> serviceRequestsPojoList;
    //ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences login_preferenes;
    LinearLayout location_setting_linear;

    String userMainId;

    ConnectionDetector internetConnection;

    String lat, lang;
    ArrayList<String> Categorieslist;
    String[] categ_list;
    Dialog Internetdialog;
    TextView no_service_requests;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_service_requests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_service_requests);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        no_service_requests = (TextView) findViewById(R.id.no_service_requests);

        login_preferenes = AppConstants.preferencesData(getApplication());

        userMainId = login_preferenes.getString(DataKeyValues.USER_USERID, null);


        lat = getIntent().getStringExtra("lat");
        lang = getIntent().getStringExtra("lang");
        Categorieslist = getIntent().getStringArrayListExtra("Categorieslist");

        SharedPreferences serviceReq = getApplicationContext().getSharedPreferences("serviceReq", MODE_PRIVATE);
        SharedPreferences.Editor serviceReq_editor = serviceReq.edit();
        serviceReq_editor.putString("lat", lat);
        serviceReq_editor.putString("lang", lang);
        Set<String> set = new HashSet<String>();
        set.addAll(Categorieslist);
        serviceReq_editor.putStringSet("serv_req_categ_list", set);
        serviceReq_editor.apply();

        categ_list = new String[Categorieslist.size()];
        categ_list = Categorieslist.toArray(categ_list);

        for (String s : categ_list)
            Log.i("Categories", s);


        internetConnection = new ConnectionDetector(ServiceRequestsActivity.this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        serviceRequestsPojoList = new ArrayList<>();
        location_setting_linear = (LinearLayout) findViewById(R.id.location_setting_linear);
        recyler_service_requests = (RecyclerView) findViewById(R.id.recyler_service_requests);
        serviceRequestsCardAdapter = new ServiceRequestsCardAdapter(ServiceRequestsActivity.this, serviceRequestsPojoList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyler_service_requests.setLayoutManager(mLayoutManager);
        recyler_service_requests.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
        recyler_service_requests.setItemAnimator(new DefaultItemAnimator());
        recyler_service_requests.setAdapter(serviceRequestsCardAdapter);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                serviceRequestsPojoList.clear();

                if (internetConnection.isConnectingToInternet(ServiceRequestsActivity.this)) {
                    new getItemsDispalyAysnc().execute();
//                    Internetdialog.dismiss();
                } else {
                    InternetConnectionAlert();
                }

            }
        });

        location_setting_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent location_intent = new Intent(ServiceRequestsActivity.this, LocationSettingsActivity.class);
                location_intent.putExtra("ser_req_cur_lattitude", lat);
                location_intent.putExtra("ser_req_cur_langitude", lang);
                location_intent.putExtra("requestFrom", "service_request_display_scr");
                startActivity(location_intent);
                finish();
            }
        });

        if (internetConnection.isConnectingToInternet(ServiceRequestsActivity.this)) {
            new getItemsDispalyAysnc().execute();
            // Internetdialog.dismiss();
        } else {
            InternetConnectionAlert();
        }

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<ServiceRequestsPojo> filteredModelList = filter(serviceRequestsPojoList, newText);
        serviceRequestsCardAdapter.setFilter(filteredModelList);
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<ServiceRequestsPojo> filter(List<ServiceRequestsPojo> models, String query) {
        query = query.toLowerCase();
        final List<ServiceRequestsPojo> filteredModelList = new ArrayList<>();
        for (ServiceRequestsPojo model : models) {
            final String text = model.getCategory().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    private class getItemsDispalyAysnc extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = prepareDefaultRequestObj();
            String service_result = serviceHandler.makeServiceCall(AppConstants.loadServiceRequestssUrl(userMainId), "POST", requestObject);
            return service_result;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //progressDialog.dismiss();
            serviceRequestsPojoList.clear();
            onItemsLoadComplete();
            JSONObject jsObj = null;

            try {
                jsObj = new JSONObject(result);

                if (jsObj.length() == 0) {

                    no_service_requests.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);

                } else if (jsObj.has("servicerequests")) {
                    JSONObject innerJsonObject = jsObj.getJSONObject("servicerequests");
                    ServiceRequestsPojo serviceRequestsItems = new ServiceRequestsPojo();
                    serviceRequestsItems.setService_id(innerJsonObject.getString("serviceRequestid"));
                    serviceRequestsItems.setService_owner_id(innerJsonObject.getString("ownerId"));
                    serviceRequestsItems.setCategory(innerJsonObject.getString("category"));
                    serviceRequestsItems.setSubcategory(innerJsonObject.getString("subcategory"));
                    serviceRequestsItems.setRequester_name(innerJsonObject.getString("requestername"));
                    serviceRequestsItems.setService_image(innerJsonObject.getString("requesterimage"));
                    serviceRequestsItems.setService_owner_online_status(innerJsonObject.getBoolean("requesterOnlineStatus"));
                    serviceRequestsItems.setService_distance(innerJsonObject.getString("distance"));
                    serviceRequestsItems.setDays_old(innerJsonObject.getString("days"));
                    serviceRequestsItems.setService_views_count(innerJsonObject.getString("views"));
                    serviceRequestsItems.setService_location(innerJsonObject.getString("address"));


                    serviceRequestsPojoList.add(serviceRequestsItems);
                    serviceRequestsCardAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();

                try {

                    JSONObject main_jsoObject = new JSONObject(result);


                    if (jsObj.has("servicerequests")) {

                        JSONArray productsArray = main_jsoObject.getJSONArray("servicerequests");

                        int j = productsArray.length();


                        if (j != 0) {
                            for (int i = 0; i < productsArray.length(); i++) {

                                JSONObject innerJsonObject = productsArray.getJSONObject(i);
                                ServiceRequestsPojo serviceRequestsItems = new ServiceRequestsPojo();
                                serviceRequestsItems.setService_id(innerJsonObject.getString("serviceRequestid"));
                                serviceRequestsItems.setService_owner_id(innerJsonObject.getString("ownerId"));
                                serviceRequestsItems.setCategory(innerJsonObject.getString("category"));
                                serviceRequestsItems.setSubcategory(innerJsonObject.getString("subcategory"));
                                serviceRequestsItems.setRequester_name(innerJsonObject.getString("requestername"));
                                serviceRequestsItems.setService_image(innerJsonObject.getString("requesterimage"));
                                serviceRequestsItems.setService_owner_online_status(innerJsonObject.getBoolean("requesterOnlineStatus"));
                                serviceRequestsItems.setService_distance(innerJsonObject.getString("distance"));
                                serviceRequestsItems.setDays_old(innerJsonObject.getString("days"));
                                serviceRequestsItems.setService_views_count(innerJsonObject.getString("views"));
                                serviceRequestsItems.setService_location(innerJsonObject.getString("address"));

                                serviceRequestsPojoList.add(serviceRequestsItems);
                            }

                            serviceRequestsCardAdapter.notifyDataSetChanged();
                        } else {
                            no_service_requests.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.GONE);

                        }

                    }

                } catch (Exception eq) {
                    eq.printStackTrace();

                }

            }

        }
    }

    private JSONObject prepareDefaultRequestObj() {


        JSONObject requestObject = null;
        try {


            requestObject = new JSONObject();
            requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, lat);
            requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, lang);
            JSONArray jsonCategoriesArray = new JSONArray(Arrays.asList(categ_list));
            requestObject.accumulate(DataKeyValues.ITEMS_GET_CATEGORIES, jsonCategoriesArray);


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


    void InternetConnectionAlert() {

        Internetdialog = new Dialog(ServiceRequestsActivity.this); /*android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);*/
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
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.butonanim));
                if (internetConnection.isConnectingToInternet(getApplicationContext())) {

                    new getItemsDispalyAysnc().execute();
                    Internetdialog.dismiss();

                }

            }
        });
        Internetdialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;


            case R.id.action_search:


                final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                searchView.setOnQueryTextListener(this);

                MenuItemCompat.setOnActionExpandListener(item,
                        new MenuItemCompat.OnActionExpandListener() {
                            @Override
                            public boolean onMenuItemActionCollapse(MenuItem item) {
                                // Do something when collapsed
                                serviceRequestsCardAdapter.setFilter(serviceRequestsPojoList);
                                return true; // Return true to collapse action view
                            }

                            @Override
                            public boolean onMenuItemActionExpand(MenuItem item) {
                                // Do something when expanded
                                return true; // Return true to expand action view
                            }
                        });
                return true;

        }
        return true;

    /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        serviceRequestsCardAdapter.setFilter(serviceRequestsPojoList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }*/


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}