package com.askntake.admin.askntake;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import AppUtils.AppConstants;
import AppUtils.ConnectionDetector;
import AppUtils.DataKeyValues;
import AppUtils.ServiceHandler;
import AppUtils.Utility;
import Pojo.SharedPrefPojo;

/**
 * Created by gandh on 04-07-2017.
 */

public class ServiceRequest_UpdationActivity extends AppCompatActivity {


    Button list_now_req_button;
    TextView contact_details;
    EditText main_categories_list, main_sub_categories_list;
    TextInputLayout input_lay_description, input_la_your_name, input_la_phone_number, input_la_email, input_la_main_categories, input_la_main_sub_categories;
    EditText edit_description_service_req, edit_your_name, edit_phone_number, edit_email;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String service_id, UserId_Main;
    ProgressDialog progressDialog;

    String user_latitude = "";
    String user_longitude = "";

    String servicerequestId, description, category, subcategory, ownerId, email, image, firstname, lastname, email_owner,
            visitcount, contact, requestername, days;

    ConnectionDetector internetConnection;
    Dialog Internetdialog;

    boolean no_profile_image_flag, its_my_own_product;


    String lat, lang;
    ArrayList<String> Categorieslist;
    Set<String> set;

    ProgressDialog mProgressDialog;
    SharedPreferences requestPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_service_request_update);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_service_req_update);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestPref = getApplicationContext().getSharedPreferences("serviceReq", MODE_PRIVATE);
        lat = requestPref.getString("lat", null);
        lang = requestPref.getString("lang", null);
        set = requestPref.getStringSet("serv_req_categ_list", null);
        Categorieslist = new ArrayList<>();
        Categorieslist.addAll(set);
        Log.d("retrivesharedPreferences", "" + set);

        internetConnection = new ConnectionDetector(getApplicationContext());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        service_id = getIntent().getStringExtra("service_id");
        UserId_Main = getIntent().getStringExtra("user_id");
        its_my_own_product = getIntent().getBooleanExtra("its_my_own_product", false);

        list_now_req_button = (Button) findViewById(R.id.list_now_req_button);


        list_now_req_button = (Button) findViewById(R.id.list_now_req_button);
        contact_details = (TextView) findViewById(R.id.contact_details);

        main_categories_list = (EditText) findViewById(R.id.main_categories_list);
        main_sub_categories_list = (EditText) findViewById(R.id.main_sub_categories_list);

        input_lay_description = (TextInputLayout) findViewById(R.id.input_lay_description);
        input_la_your_name = (TextInputLayout) findViewById(R.id.input_la_your_name);
        input_la_phone_number = (TextInputLayout) findViewById(R.id.input_la_phone_number);
        input_la_email = (TextInputLayout) findViewById(R.id.input_la_email);
        input_la_main_categories = (TextInputLayout) findViewById(R.id.input_la_main_categories);
        input_la_main_sub_categories = (TextInputLayout) findViewById(R.id.input_la_main_sub_categories);

        edit_description_service_req = (EditText) findViewById(R.id.edit_description_service_req);
        edit_your_name = (EditText) findViewById(R.id.edit_your_name);
        edit_phone_number = (EditText) findViewById(R.id.edit_phone_number);
        edit_email = (EditText) findViewById(R.id.edit_email);


        edit_description_service_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                View promptsView = li.inflate(R.layout.description_popup, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ServiceRequest_UpdationActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);


                userInput.setText(edit_description_service_req.getText());

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text

                                        edit_description_service_req.setText(userInput.getText());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });


        list_now_req_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (main_categories_list.getText().toString() != null) {
                    if (main_sub_categories_list.getText().toString() != null) {
                        if (edit_description_service_req.getText().length() > 0) {
                            if (edit_your_name.getText().length() > 0) {
                                if (android.util.Patterns.PHONE.matcher(edit_phone_number.getText()).matches()) {
                                    if (edit_email.getText().toString().matches(emailPattern) && edit_email.getText().toString().length() > 0) {
                                        // new ServiceRquestAsyncTask().execute();
                                        runServer();
                                    } else {
                                        input_la_email.requestFocus();
                                        edit_email.setError("Please Enter your valid Email");
                                    }
                                } else {
                                    input_la_phone_number.requestFocus();
                                    edit_phone_number.setError("Please Enter your Phone Number");
                                }
                            } else {

                                input_la_your_name.requestFocus();
                                edit_your_name.setError("Please Povide your Name");
                            }
                        } else {
                            input_lay_description.requestFocus();
                            edit_description_service_req.setError("please provide Description");
                        }
                    } else {

                        Toast.makeText(getApplicationContext(), "Please select Sub Category", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), "Please select Category", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (internetConnection.isConnectingToInternet(getApplicationContext())) {
            new GetServiceRequestDetailsAsyncTask().execute();

        } else {
            InternetConnectionAlert();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent_description = new Intent(ServiceRequest_UpdationActivity.this, ServicesRequests_DescriptionActivity.class);
            intent_description.putExtra("service_id", service_id);
            intent_description.putExtra("user_id", UserId_Main);
            intent_description.putExtra("myownproduct", its_my_own_product);
            startActivity(intent_description);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class GetServiceRequestDetailsAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(ServiceRequest_UpdationActivity.this, "", "Please wait...", false, false);

        }

        @Override
        protected String doInBackground(String... params) {
            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(AppConstants.getServicerequestDescriptionUrl(UserId_Main, service_id), "GET", null);
            String responceStatus;
            try {

                if (jsonStr != null) {
                    JSONObject itemsObj = new JSONObject(jsonStr);
                    if (itemsObj.has("requestername")) {
                        requestername = itemsObj.getString("requestername");
                    }

                    if (itemsObj.has("servicerequestId")) {
                        servicerequestId = itemsObj.getString("servicerequestId");
                    }
                    if (itemsObj.has("description")) {
                        description = itemsObj.getString("description");
                    }
                    if (itemsObj.has("category")) {
                        category = itemsObj.getString("category");
                    }
                    if (itemsObj.has("subcategory")) {
                        subcategory = itemsObj.getString("subcategory");
                    }

                    if (itemsObj.has("category")) {
                        category = itemsObj.getString("category");
                    }
                    if (itemsObj.has("email")) {
                        email = itemsObj.getString("email");
                    }

                    if (itemsObj.has("owner")) {
                        JSONObject ownerObj = itemsObj.getJSONObject("owner");

                        if (ownerObj.has("ownerId")) {
                            ownerId = ownerObj.getString("ownerId");
                        }
                        if (ownerObj.has("email")) {
                            email_owner = ownerObj.getString("email");
                        }

                        if (ownerObj.has("image")) {
                            image = ownerObj.getString("image");
                        } else {
                            no_profile_image_flag = true;
                        }
                        if (ownerObj.has("firstname")) {
                            firstname = ownerObj.getString("firstname");
                        }

                        if (ownerObj.has("lastname")) {
                            lastname = ownerObj.getString("lastname");
                        }

                    }

                    if (itemsObj.has("visitcount")) {
                        visitcount = itemsObj.getString("visitcount");
                    }

                    if (itemsObj.has("contact")) {
                        contact = itemsObj.getString("contact");
                    }
                    if (itemsObj.has("days")) {
                        days = itemsObj.getString("days");
                    }
                    responceStatus = "success";

                } else {
                    responceStatus = "fail";
                }
            } catch (Exception e) {
                responceStatus = "fail";
                e.printStackTrace();
            }


            return responceStatus;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mProgressDialog.dismiss();

            main_categories_list.setText(category);
            main_sub_categories_list.setText(subcategory);
            edit_description_service_req.setText(description);
            edit_your_name.setText(requestername);
            edit_phone_number.setText(contact);
            edit_email.setText(email);


           /* if (its_my_own_product) {
                if (isFavorite_flag) {
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.faverate_img_selected));
                } else {
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.feverate_img));
                }
            }*/
        }

    }


    public void runServer() {
        SharedPrefPojo pojo = Utility.getData(getApplicationContext());

        user_latitude = pojo.getLattitude();
        user_longitude = pojo.getLangitude();
        // user_zipcode = pojo.getZipcode();
        // user_location_address = pojo.getArea();


        //Toast.makeText(getApplicationContext(), "lat: " + user_latitude + " lang: " + user_longitude + " Zipcode: " /*+ user_zipcode + " Address: " + user_location_address*/, Toast.LENGTH_SHORT).show();
        if (user_latitude != null && user_longitude != null) {
            if (internetConnection.isConnectingToInternet(getApplicationContext())) {
                Utility.resetUploadLocation(getApplicationContext());
                new ServiceRquestUpdateAsyncTask().execute();
            } else {
                InternetConnectionAlert();
            }
        } else {
            Intent myIntent = new Intent(getApplicationContext(), LocationSettingsActivity.class);
            myIntent.putExtra("requestFrom", "upload_scr");
            startActivity(myIntent);
        }
    }

    void InternetConnectionAlert() {

        Internetdialog = new Dialog(getApplicationContext()); /*android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);*/
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
                    Internetdialog.dismiss();

                }


            }
        });
        Internetdialog.show();

    }

    private class ServiceRquestUpdateAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ServiceRequest_UpdationActivity.this, "", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = getJSONObject();
            String service_result = serviceHandler.makeServiceCall(AppConstants.SERVICE_REQUEST_UPDATE_URL, "POST", requestObject);

            return service_result;
        }

        @Override
        protected void onPostExecute(String service_result) {
            progressDialog.dismiss();
            super.onPostExecute(service_result);

            boolean status = false;
            try {
                JSONObject jsObj = new JSONObject(service_result);
                if (jsObj.has("status")) {
                    status = jsObj.getBoolean("status");
                    if (status) {

                        Intent serviceRequestIntent = new Intent(ServiceRequest_UpdationActivity.this, ServiceRequestsActivity.class);
                        serviceRequestIntent.putExtra("lat", lat);
                        serviceRequestIntent.putExtra("lang", lang);
                        serviceRequestIntent.putStringArrayListExtra("Categorieslist", Categorieslist);
                        startActivity(serviceRequestIntent);
                        finish();

                        Toast.makeText(getApplicationContext(), jsObj.getString("message"), Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(getApplicationContext(), jsObj.getString("message"), Toast.LENGTH_SHORT).show();

                    }

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    private JSONObject getJSONObject() {
        JSONObject requestObject = null;
        try {


            requestObject = new JSONObject();

            requestObject.accumulate(DataKeyValues.SERVICE_REQUESTER_ID, service_id);
            requestObject.accumulate(DataKeyValues.REQUESTER_ID, UserId_Main);
            requestObject.accumulate(DataKeyValues.REQUESTER_NAME, edit_your_name.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.REQUESTER_DESCRIPTION, edit_description_service_req.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.REQUESTER_CATEGORY, main_categories_list.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.REQUESTER_SUB_CATEGORY, main_sub_categories_list.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.REQUESTER_CONTACT, edit_phone_number.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.REQUESTER_EMAIL_ID, edit_email.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.REQUESTER_LATITUDE, user_latitude);
            requestObject.accumulate(DataKeyValues.REQUESTER_LANGITUDE, user_longitude);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestObject;

    }

    @Override
    public void onBackPressed() {
        Intent intent_description = new Intent(ServiceRequest_UpdationActivity.this, ServicesRequests_DescriptionActivity.class);
        intent_description.putExtra("service_id", service_id);
        intent_description.putExtra("user_id", UserId_Main);
        intent_description.putExtra("myownproduct", its_my_own_product);
        startActivity(intent_description);
        finish();
    }
}
