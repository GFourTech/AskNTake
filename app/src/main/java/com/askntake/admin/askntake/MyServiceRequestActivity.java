package com.askntake.admin.askntake;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.MyServiceRequestsListAdapter;
import Adapters.MyServicesListAdapter;
import AppUtils.AppConstants;
import AppUtils.DataKeyValues;
import AppUtils.GridSpacingItemDecoration;
import AppUtils.ServiceHandler;
import Pojo.ItemServicePojo;
import Pojo.ServiceRequestsPojo;

/**
 * Created by admin on 3/29/2017.
 */

public class MyServiceRequestActivity extends AppCompatActivity {
    private RecyclerView recycler_view_my_service_requests;
    private MyServiceRequestsListAdapter myservicesAdapter;
    ProgressDialog progressDialog;
    List<ServiceRequestsPojo> productItemsListData;
    String user_id;
    TextView no_my_services_requests_layout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_myservice_requests);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        no_my_services_requests_layout = (TextView) findViewById(R.id.no_my_services_requests_layout);


        recycler_view_my_service_requests = (RecyclerView) findViewById(R.id.recycler_view_myservicerequests);

        productItemsListData = new ArrayList<>();
        myservicesAdapter = new MyServiceRequestsListAdapter(MyServiceRequestActivity.this, productItemsListData);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MyServiceRequestActivity.this, 2);
        recycler_view_my_service_requests.setLayoutManager(mLayoutManager);
        recycler_view_my_service_requests.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
        recycler_view_my_service_requests.setItemAnimator(new DefaultItemAnimator());
        recycler_view_my_service_requests.setAdapter(myservicesAdapter);

        SharedPreferences login_data_pref = getSharedPreferences(DataKeyValues.USER_DATA_PREF, Context.MODE_PRIVATE);
        user_id = login_data_pref.getString(DataKeyValues.USER_USERID, null);


        new GetSellingItemsListAsync().execute();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetSellingItemsListAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            ServiceHandler serviceHandler = new ServiceHandler();
            String result = serviceHandler.makeServiceCall(AppConstants.getMyServiceRequestsUrl(Long.parseLong(user_id)), "GET", null);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if (result != null) {


                String projuctsObj_Name = "servicerequests";
                JSONObject mainJsonObj;
                String pro_itemId = "";
                String pro_name = "";
                String pro_mainImage = "";
                JSONObject productObj = null;
                try {
                    mainJsonObj = new JSONObject(result);
                    JSONArray productsArray = null;


                    if (mainJsonObj.length() == 0) {

                        no_my_services_requests_layout.setVisibility(View.VISIBLE);
                        recycler_view_my_service_requests.setVisibility(View.GONE);

                    } else {
                        try {
                            productsArray = mainJsonObj.getJSONArray(projuctsObj_Name);
                        } catch (Exception e) {
                            no_my_services_requests_layout.setVisibility(View.VISIBLE);
                            recycler_view_my_service_requests.setVisibility(View.GONE);
                            productsArray = null;
                            try {
                                productObj = mainJsonObj.getJSONObject(projuctsObj_Name);

                            } catch (Exception e2) {
                                no_my_services_requests_layout.setVisibility(View.VISIBLE);
                                recycler_view_my_service_requests.setVisibility(View.GONE);
                                productObj = null;
                            }

                        }

                        if (productsArray != null) {
                            for (int i = 0; i < productsArray.length(); i++) {
                                JSONObject productDataObj = productsArray.getJSONObject(i);

                                int j = productDataObj.length();


                                if (productDataObj.has("serviceRequestid") && productDataObj.has("category") && productDataObj.has("requesterimage")) {
                                    pro_itemId = productDataObj.getString("serviceRequestid");
                                    pro_name = productDataObj.getString("category");
                                    pro_mainImage = productDataObj.getString("requesterimage");

                                    //ItemServicePojo productsItemPojo=new ItemServicePojo(pro_itemId, pro_name, pro_mainImage, pro_price,pricetype,"3",false);
                                    ServiceRequestsPojo productsItemPojo = new ServiceRequestsPojo();
                                    productsItemPojo.setService_image(AppConstants.IMG_BASE_URL + pro_mainImage);
                                    productsItemPojo.setService_name(pro_name);
                                    productsItemPojo.setService_id(pro_itemId);
                                    //  productsItemPojo.setService_owner_id();
                                    productItemsListData.add(productsItemPojo);
                                }
                            }
                        } else if (productObj != null) {
                            pro_itemId = productObj.getString("serviceRequestid");
                            pro_name = productObj.getString("category");
                            pro_mainImage = productObj.getString("requesterimage");
                            // ProductItemsPojo productsItemPojo=new ProductItemsPojo(pro_itemId, pro_name, pro_mainImage, pro_price,pricetype,"3",false);
                            ServiceRequestsPojo productsItemPojo = new ServiceRequestsPojo();
                            productsItemPojo.setService_image(AppConstants.IMG_BASE_URL + pro_mainImage);
                            productsItemPojo.setService_name(pro_name);
                            productsItemPojo.setService_id(pro_itemId);

                            productItemsListData.add(productsItemPojo);
                        }
                        myservicesAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
