package com.askntake.admin.askntake;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import Adapters.ServiceCategoriesFilterAdapter;
import AppUtils.AppConstants;
import AppUtils.Commons;
import AppUtils.DataKeyValues;
import AppUtils.Helper;
import AppUtils.ServiceHandler;
import AppUtils.Utils;
import Pojo.ServicesFiteringDataPojo;

/**
 * Created by admin on 3/30/2017.
 */

public class ServiceFilteringActivity extends AppCompatActivity {

    ExpandableListView listView;

    Map<String, String> subList = new HashMap<>();

    RadioGroup miles_kilometers_layout;
    EditText search_product_heading_textView;
    TextView text_listed_within_textView;

    RadioButton radio_distance_1, radio_distance_2, radio_distance_3, radio_distance_4, radio_30_days, radio_7_days, radio_24_hours, radio_miles_2, radio_kilometers_1,
            radio_All_Listings;

    RatingBar rating_layout_filter;

    View listed_within_view;
    TextView search_location_heading_textView;
    static CheckBox chkAll;
    String search_location_to_set;

    RelativeLayout reset_main_layout, apply_main_layout;

    float rating_value;

    String request = "Filtering";

    String user_latitude = "0", user_longitude = "0";
    ArrayList<String> list_of_categories;
    ServicesFiteringDataPojo filterPojoObj;
    Utils utils;
    String userMainId;
    Map<String, ArrayList<Integer>> positionsMap;
    ServiceCategoriesFilterAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_service_filtering);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        utils = new Utils(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        search_product_heading_textView = (EditText) findViewById(R.id.search_product_heading_textView);

        SharedPreferences fbpref = AppConstants.preferencesData(getApplicationContext());
        userMainId = fbpref.getString(DataKeyValues.OWNER_ID, null);
        new PopulateDataAsynchTask().execute();

        miles_kilometers_layout = (RadioGroup) findViewById(R.id.miles_kilometers_layout);
        radio_kilometers_1 = (RadioButton) findViewById(R.id.radio_kilometers_1);
        radio_miles_2 = (RadioButton) findViewById(R.id.radio_miles_2);

        radio_distance_2 = (RadioButton) findViewById(R.id.radio_distance_2);

        radio_distance_3 = (RadioButton) findViewById(R.id.radio_distance_3);

        radio_distance_4 = (RadioButton) findViewById(R.id.radio_distance_4);

        RelativeLayout apply_main_layout = (RelativeLayout) findViewById(R.id.apply_main_layout);

        RelativeLayout reset_main_layout = (RelativeLayout) findViewById(R.id.reset_main_layout);

        text_listed_within_textView = (TextView) findViewById(R.id.text_listed_within_textView);
        listed_within_view = (View) findViewById(R.id.listed_within_view);

        rating_layout_filter = (RatingBar) findViewById(R.id.rating_layout_filter);

        radio_distance_1 = (RadioButton) findViewById(R.id.radio_distance_1);
        search_location_heading_textView = (TextView) findViewById(R.id.search_location_heading_textView);

        SharedPreferences user_LocationData = AppConstants.preferencesData(getApplicationContext());
        search_location_to_set = user_LocationData.getString(DataKeyValues.SEARCH_SHOW_LOCATION, null);


        user_latitude = user_LocationData.getString(DataKeyValues.FILTERLATTITUDE, null);
        user_longitude = user_LocationData.getString(DataKeyValues.FILTERLONGITUDE, null);

        if (search_location_to_set != null) {
            search_location_heading_textView.setText(search_location_to_set);
        }
        chkAll = (CheckBox) findViewById(R.id.all_categories_chbx);
        // chkAll.setChecked(true);
        listView = (ExpandableListView) findViewById(R.id.categ_expandable_list);

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        final Map<String, Map<String, String>> categoriesList = Commons.getCategories(ServiceFilteringActivity.this);
        ArrayList<String> positions = new ArrayList<>();
        positionsMap = getPositions(positions);
        adapter = new ServiceCategoriesFilterAdapter(ServiceFilteringActivity.this, categoriesList, request, positionsMap);

        search_location_heading_textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(ServiceFilteringActivity.this, LocationSettingsActivity.class);
//                myIntent.putExtra("lattitude_search",);
//                myIntent.putExtra("laongitude_search",);
                myIntent.putExtra("requestFrom", "search");
                startActivity(myIntent);
                finish();

            }
        });
        chkAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                CheckBox chk = (CheckBox) v;
                adapter.initCheckStates(chk.isChecked());
                adapter.notifyDataSetChanged();

            }
        });
        reset_main_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                adapter.initCheckStates(true);
                adapter.notifyDataSetChanged();
                setDefaulters();


            }

        });

        adapter.setmListener(new TotalListener() {
            @Override
            public void onTotalChanged(int sum) {
                // mTextView.setText("Total = " + sum);
            }

            @Override
            public void expandGroupEvent(int groupPosition, boolean isExpanded) {
                if (isExpanded)
                    listView.collapseGroup(groupPosition);
                else
                    listView.expandGroup(groupPosition);
            }
        });
        listView.setAdapter(adapter);
        Helper helper = new Helper();
        helper.getExpendableListViewSize(listView/*, 2*/);


        miles_kilometers_layout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radio_kilometers_1.isChecked()) {

                    radio_distance_1.setText("Nearby (1 KM)");
                    radio_distance_2.setText("In my area(5 KM)");
                    radio_distance_3.setText("In my town or city(10 KM)");
                    radio_distance_4.setText("For (10+ KM)");

                } else {

                    radio_distance_1.setText("Nearby (1 mile)");
                    radio_distance_2.setText("In my area(5 miles)");
                    radio_distance_3.setText("In my town or city(10 miles)");
                    radio_distance_4.setText("For (10+ miles)");

                }

            }
        });

        RadioGroup listed_radio_layout = (RadioGroup) findViewById(R.id.listed_radio_layout);

        radio_24_hours = (RadioButton) findViewById(R.id.radio_24_hours);

        radio_7_days = (RadioButton) findViewById(R.id.radio_7_days);

        radio_30_days = (RadioButton) findViewById(R.id.radio_30_days);

        radio_All_Listings = (RadioButton) findViewById(R.id.radio_All_Listings);


     /*   if (rating_layout_filter.getRating() != 0.0) {
            rating_value = rating_layout_filter.getRating();
        } else {
            rating_value = 0.0f;
        }
*/

        apply_main_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //set lat long values


                int range = 10;
                if (radio_distance_1.isChecked()) {
                    range = 1;
                }
                if (radio_distance_2.isChecked()) {
                    range = 5;
                }

                if (radio_distance_3.isChecked()) {
                    range = 10;
                }

                if (radio_distance_4.isChecked()) {
                    range = 500;
                }

                //listed with in
                String listed_with_in_text = "all";

                if (radio_24_hours.isChecked()) {
                    listed_with_in_text = "1";
                }
                if (radio_7_days.isChecked()) {
                    listed_with_in_text = "7";
                }
                if (radio_30_days.isChecked()) {
                    listed_with_in_text = "30";
                }
                if (radio_All_Listings.isChecked()) {
                    listed_with_in_text = "0";
                }
                String distanceIn = "K";
                if (radio_miles_2.isChecked()) {
                    distanceIn = "M";
                }
                String service_name = "";
                if (search_product_heading_textView.getText().toString() != null) {
                    service_name = search_product_heading_textView.getText().toString();
                }
                ArrayList<String> selectedSubcategories = adapter.getCheckedSubcategories();
                Log.i("selectedSubcategories", "" + selectedSubcategories.size());
                ArrayList<String> subcategoriesPositions = adapter.getCheckedSubcategoriesPositios();
                Log.i("subcategoriesPositions", "" + subcategoriesPositions.size());
                ArrayList<String> selectedCategories = adapter.getCheckedCategories();
                Log.i("selectedCategories", "" + selectedCategories.size());

                if (rating_layout_filter.getRating() != 0.0) {
                    rating_value = rating_layout_filter.getRating();
                } else {
                    rating_value = 0.0f;
                }

                SharedPreferences sharedPreferences = getSharedPreferences("Filter_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("filtering_marker", true);
                editor.commit();


                filterPojoObj = new ServicesFiteringDataPojo(service_name, selectedCategories, selectedSubcategories, distanceIn, range, listed_with_in_text, subcategoriesPositions, String.valueOf(rating_value));
                new SaveFiltersAsynchTask().execute();
            }
        });
    }

    public void setDefaulters() {

        chkAll.setChecked(true);
        radio_distance_1.setChecked(false);
        radio_distance_2.setChecked(false);
        radio_distance_3.setChecked(false);
        radio_distance_4.setChecked(true);

        radio_24_hours.setChecked(false);
        radio_7_days.setChecked(false);
        radio_30_days.setChecked(false);
        radio_All_Listings.setChecked(true);

        radio_distance_4.setChecked(true);
        radio_All_Listings.setChecked(true);
        search_product_heading_textView.setText("");

        radio_kilometers_1.setChecked(true);
        radio_miles_2.setChecked(false);

        rating_layout_filter.setRating(Float.parseFloat("0.0"));
    }

    public static void uncheckAll() {
        chkAll.setChecked(false);
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    ProgressDialog progressDialog = null;


    private class PopulateDataAsynchTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(ServiceFilteringActivity.this, "", "Please wait...", false, false);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            String response = "";
            InputStream inputStream = null;
            try {
                ServiceHandler serviceHandler = new ServiceHandler();

               /* String url = AppConstants.BASE_URL
                        + "rest/filters/" + userMainId + "/getfilters";*/

                String url = AppConstants.getFiltersUrl(userMainId);
                response = serviceHandler.makeServiceCall(url, "GET", null);
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
        protected void onPostExecute(String response) {
            if (response != null) {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);

                    String firstTime_lattitude = "";
                    String firstTime_longitude = "";


                    if (obj.has("servicename")) {
                        String servicename = obj.getString("servicename");
                        search_product_heading_textView.setText(servicename);
                    }
                    if (obj.has("distanceIn")) {

                        String distance = obj.getString("distanceIn");
                        if (distance.equalsIgnoreCase("M")) {
                            radio_miles_2.setChecked(true);
                        } else {
                            radio_kilometers_1.setChecked(true);
                        }
                    }

                    if (obj.has("range")) {
                        String range = obj.getString("range");
                        if (range.equals("5")) {
                            radio_distance_1.setChecked(false);
                            radio_distance_2.setChecked(true);
                            radio_distance_4.setChecked(false);
                            radio_distance_3.setChecked(false);
                        } else if (range.equals("10")) {
                            radio_distance_2.setChecked(false);
                            radio_distance_1.setChecked(false);
                            radio_distance_4.setChecked(false);
                            radio_distance_3.setChecked(true);
                        } else if (range.equals("1")) {
                            radio_distance_2.setChecked(false);
                            radio_distance_1.setChecked(true);
                            radio_distance_4.setChecked(false);
                            radio_distance_3.setChecked(false);
                        } else {
                            radio_distance_2.setChecked(false);
                            radio_distance_1.setChecked(false);
                            radio_distance_4.setChecked(true);
                            radio_distance_3.setChecked(false);
                        }
                    }

                    if (obj.has("rating")) {

                        String rating = obj.getString("rating");
                        rating_layout_filter.setRating(Float.parseFloat(rating));


                    }
                    if (obj.has("timeRange")) {
                        String timeRange = obj.getString("timeRange");
                        if (timeRange.equals("1")) {
                            radio_24_hours.setChecked(true);
                            radio_7_days.setChecked(false);
                            radio_30_days.setChecked(false);
                            radio_All_Listings.setChecked(false);
                        } else if (timeRange.equals("7")) {
                            radio_24_hours.setChecked(false);
                            radio_7_days.setChecked(true);
                            radio_30_days.setChecked(false);
                            radio_All_Listings.setChecked(false);
                        } else if (timeRange.equals("30")) {
                            radio_24_hours.setChecked(false);
                            radio_7_days.setChecked(false);
                            radio_30_days.setChecked(true);
                            radio_All_Listings.setChecked(false);
                        } else {
                            radio_24_hours.setChecked(false);
                            radio_7_days.setChecked(false);
                            radio_30_days.setChecked(false);
                            radio_All_Listings.setChecked(true);
                        }
                    }
                    if (obj.has("positions")) {
                        ArrayList<String> positions = new ArrayList<>();
                        try {
                            JSONArray array = obj.getJSONArray("positions");

                            for (int i = 0; i < array.length(); i++) {
                                positions.add(array.get(i).toString());

                            }
                        } catch (Exception e) {

                            String position = obj.getString("positions");
                            positions.add(position);
                            e.printStackTrace();
                        }
                        positionsMap = getPositions(positions);

                        if (positions.size() == 42) {
                            chkAll.setChecked(true);
                        } else {
                            chkAll.setChecked(false);
                        }
                        adapter.setData(positionsMap);
                        adapter.notifyDataSetChanged();


                    }

                    if (obj.has("lat")) {
                        firstTime_lattitude = obj.getString("lat");
                    }
                    if (obj.has("lang")) {
                        firstTime_longitude = obj.getString("lang");
                    }

                    SharedPreferences user_LocationData = AppConstants.preferencesData(getApplicationContext());
                    SharedPreferences.Editor LocationData_editor = user_LocationData.edit();
                    if (!firstTime_lattitude.equalsIgnoreCase("0") && !firstTime_longitude.equalsIgnoreCase("0")) {

                        LocationData_editor.putString(DataKeyValues.FILTERLATTITUDE, firstTime_lattitude);
                        LocationData_editor.putString(DataKeyValues.FILTERLONGITUDE, firstTime_longitude);

                    }
                    Address user_adrees_info = DataKeyValues.LocationAddressData(getApplicationContext(), firstTime_lattitude, firstTime_longitude);
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
                        search_location_heading_textView.setText(area_data);

                    } else {
                        //search_location_heading_textView.setText(area_data);
                        search_location_heading_textView.setText("PLease select location");
                    }
                } catch (Exception e) {

                }

               /*Intent myIntent=new Intent(ServiceFilteringActivity.this,NavigationDrawerActivity.class);
               myIntent.putExtra("FiltesApplied", true);
               myIntent.putExtra("filterPojoObj", filterPojoObj);
               myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               myIntent.putExtra("EXIT", true);
               startActivity(myIntent);
               finish();*/
                // Toast.makeText(getApplicationContext(), "dataloaded", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public class SaveFiltersAsynchTask extends AsyncTask<Void, Void, String> {

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
                runOnUiThread(new Runnable() {
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
                Intent myIntent = new Intent(ServiceFilteringActivity.this, NavigationDrawerActivity.class);
                myIntent.putExtra("FiltesApplied", true);
                myIntent.putExtra("filterPojoObj", filterPojoObj);
                myIntent.putExtra("isFirstTime", false);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.putExtra("EXIT", true);
                //myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // myIntent.putExtra("EXIT", true);
                startActivity(myIntent);
                finish();
            }
        }

        private JSONObject getRequestJsonObject() {


            JSONObject requestObject = null;
            try {
                requestObject = new JSONObject();
                requestObject.accumulate("requestFrom", "filter");

                if (filterPojoObj != null) {
                    requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, user_latitude);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, user_longitude);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_RANGE, filterPojoObj.getRange());
                    requestObject.accumulate(DataKeyValues.ITEM_GET_CATEGORY, filterPojoObj.getCategory());
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_NAME, filterPojoObj.getService_name());
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_PROVIDER_NAME, "");
                    requestObject.accumulate(DataKeyValues.ITEM_TIME_RANGE, filterPojoObj.getListed_with_in());
                    requestObject.accumulate(DataKeyValues.ITEM_GET_DISTANCEIN, filterPojoObj.getKilomitersOrMiles());
                    requestObject.accumulate(DataKeyValues.ITEM_GET_RATING, filterPojoObj.getRating());
                    JSONArray jsonSubCategoriesArray = new JSONArray();
                    for (int i = 0; i < filterPojoObj.getSubCategoriesList().size(); i++) {
                        jsonSubCategoriesArray.put(filterPojoObj.getSubCategoriesList().get(i));
                    }
                    requestObject.accumulate(DataKeyValues.ITEM_GET_SUBCATEGORIES, jsonSubCategoriesArray);


                    JSONArray jsonCategoriesArray = new JSONArray();
                    for (int i = 0; i < filterPojoObj.getCategoriesList().size(); i++) {
                        jsonCategoriesArray.put(filterPojoObj.getCategoriesList().get(i));
                    }
                    requestObject.accumulate(DataKeyValues.ITEMS_GET_CATEGORIES, jsonCategoriesArray);


                    JSONArray jsonSategoriesPositionArray = new JSONArray();
                    for (int i = 0; i < filterPojoObj.getSubcategoriesPositions().size(); i++) {
                        jsonSategoriesPositionArray.put(filterPojoObj.getSubcategoriesPositions().get(i));
                    }
                    requestObject.accumulate(DataKeyValues.POSITIONS, jsonSategoriesPositionArray);

                } else {
                    requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, user_latitude);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, user_longitude);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_RANGE, "500");
                    requestObject.accumulate(DataKeyValues.ITEM_GET_CATEGORY, "");
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_NAME, "");
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_PROVIDER_NAME, "");
                    requestObject.accumulate(DataKeyValues.ITEM_TIME_RANGE, 0);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_DISTANCEIN, "M");
                    JSONArray jsonSubCategoriesArray = new JSONArray();
                    requestObject.accumulate(DataKeyValues.ITEM_GET_SUBCATEGORIES, jsonSubCategoriesArray);
                    JSONArray jsonSategoriesPositionArray = new JSONArray();
                    requestObject.accumulate(DataKeyValues.POSITIONS, jsonSategoriesPositionArray);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_RATING, "0.0");

                    JSONArray jsonCategoriesArray = new JSONArray();
                    for (int i = 0; i < filterPojoObj.getCategoriesList().size(); i++) {
                        jsonCategoriesArray.put(filterPojoObj.getCategoriesList().get(i));
                    }
                    requestObject.accumulate(DataKeyValues.ITEMS_GET_CATEGORIES, jsonCategoriesArray);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return requestObject;
        }


    }

    private Map<String, ArrayList<Integer>> getPositions(ArrayList<String> positions) {
        Map<String, ArrayList<Integer>> mainObj = new LinkedHashMap<>();

        ArrayList<Integer> childs;
        for (int i = 0; i < positions.size(); i++) {
            String[] positionsArray = positions.get(i).split(",");
            String groupPosition = positionsArray[0];
            int childPosition = Integer.parseInt(positionsArray[1]);

            childs = mainObj.get(groupPosition);

            if (childs != null) {
                childs.add(childPosition);
            } else {
                childs = new ArrayList<>();
                childs.add(childPosition);
            }
            mainObj.put(groupPosition, childs);
        }
        return mainObj;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if (item.getItemId() == R.id.popup_toggle) {

            View menuItemView = findViewById(R.id.popup_toggle);
            PopupMenu popup = new PopupMenu(ServiceFilteringActivity.this, menuItemView);

            //PopupMenu popup = new PopupMenu(ServiceDescriptionActivity_Services.this, popup_icon);
            popup.getMenuInflater().inflate(R.menu.popup_defaultfilter, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if (item.getItemId() == R.id.set_defaults) {

                        adapter.initCheckStates(true);
                        adapter.notifyDataSetChanged();
                        setDefaulters();

                    }
                    return true;
                }


            });
            popup.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
