package com.askntake.admin.askntake;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import AppUtils.AppConstants;
import AppUtils.DataKeyValues;
import AppUtils.ServiceHandler;
import AppUtils.Utility;

/**
 * Created by HP on 6/30/2017.
 */

public class ServicesRequests_DescriptionActivity extends AppCompatActivity {

    ProgressDialog mProgressDialog;

    boolean no_profile_image_flag, its_my_own_product;
    String UserId_Main, service_id, userFname;
    SharedPreferences fb_data_pref;

    GoogleMap googleMap;

    private Menu menu;
    private MenuItem menuItem;
    boolean isFavorite_flag = false;
    String favoritesStatus, reqMessage, distance;


    TextView edit_button, ser_requester_name, ser_requester_name_value, req_desc_textview, req_icon_view, req_desc_text,
            category_name, sub_category_name, contact_text, phone_num_txt, phone_num_txt_value, email_txt, email_txt_value,
            address_txt, distance_cal_txt;

    ImageView calling_button, email_button;

    Button but_getdirection, but_websiteview;

    LinearLayout phone_num_function, email_function, directions_lay;
    RelativeLayout main_relative_layout, edit_requester_name, request_description_lay, req_desc_lay,
            categ_sucateg_name_rel, contact_lay, address_lay, map_relative_layout;

    String latitude, langitude, servicerequestId, description, category, subcategory, ownerId, email, image, firstname, lastname,
            visitcount, contact, requestername, email_owner, days, address;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_description_service_requests);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fb_data_pref = AppConstants.preferencesData(getApplication());
        its_my_own_product = getIntent().getBooleanExtra("myownproduct", false);
        service_id = getIntent().getStringExtra("service_id");
        //user_id = getIntent().getStringExtra("user_id");
        distance = getIntent().getStringExtra("distance");


        UserId_Main = fb_data_pref.getString(DataKeyValues.USER_USERID, null);
        userFname = fb_data_pref.getString("fb_username", null);

        edit_button = (TextView) findViewById(R.id.edit_button);
        ser_requester_name = (TextView) findViewById(R.id.ser_requester_name);
        ser_requester_name_value = (TextView) findViewById(R.id.ser_requester_name_value);
        req_desc_textview = (TextView) findViewById(R.id.req_desc_textview);
        req_icon_view = (TextView) findViewById(R.id.req_icon_view);
        req_desc_text = (TextView) findViewById(R.id.req_desc_text);
        category_name = (TextView) findViewById(R.id.category_name);
        sub_category_name = (TextView) findViewById(R.id.sub_category_name);
        contact_text = (TextView) findViewById(R.id.contact_text);
        phone_num_txt = (TextView) findViewById(R.id.phone_num_txt);
        phone_num_txt_value = (TextView) findViewById(R.id.phone_num_txt_value);
        email_txt = (TextView) findViewById(R.id.email_txt);
        email_txt_value = (TextView) findViewById(R.id.email_txt_value);
        address_txt = (TextView) findViewById(R.id.address_txt);
        distance_cal_txt = (TextView) findViewById(R.id.distance_cal_txt);


        calling_button = (ImageView) findViewById(R.id.calling_button);
        email_button = (ImageView) findViewById(R.id.email_button);
        but_getdirection = (Button) findViewById(R.id.but_getdirection);
        but_websiteview = (Button) findViewById(R.id.but_websiteview);

        phone_num_function = (LinearLayout) findViewById(R.id.phone_num_function);
        email_function = (LinearLayout) findViewById(R.id.email_function);
        directions_lay = (LinearLayout) findViewById(R.id.directions_lay);

        main_relative_layout = (RelativeLayout) findViewById(R.id.main_relative_layout);
        edit_requester_name = (RelativeLayout) findViewById(R.id.edit_requester_name);
        request_description_lay = (RelativeLayout) findViewById(R.id.request_description_lay);
        req_desc_lay = (RelativeLayout) findViewById(R.id.req_desc_lay);
        categ_sucateg_name_rel = (RelativeLayout) findViewById(R.id.categ_sucateg_name_rel);
        contact_lay = (RelativeLayout) findViewById(R.id.contact_lay);
        address_lay = (RelativeLayout) findViewById(R.id.address_lay);
        map_relative_layout = (RelativeLayout) findViewById(R.id.map_relative_layout);


        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.google_map))
                .getMap();


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                // TODO Auto-generated method stub

                Intent myIntent = new Intent(ServicesRequests_DescriptionActivity.this, MapDisplyingActivity.class);
                myIntent.putExtra("latitudeOfMap", Double.parseDouble(latitude));
                myIntent.putExtra("longitudeOfMap", Double.parseDouble(langitude));
                ///myIntent.putExtra("area_of_location",owner_area);
                startActivity(myIntent);

            }
        });

        if (its_my_own_product) {
            edit_button.setText("EDIT");
        } else {
            edit_button.setText("CHAT");
        }


        req_desc_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (req_desc_text.getVisibility() == View.GONE) {

                    req_icon_view.setBackgroundResource(R.drawable.upwardsarrow);

                    req_desc_text.setVisibility(View.VISIBLE);


                } else {
                    req_icon_view.setBackgroundResource(R.drawable.downarrow);
                    req_desc_text.setVisibility(View.GONE);
                }
            }
        });


        email_function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendEmail();


            }
        });


        phone_num_function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callingIntent();
            }
        });


        but_getdirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(ServicesRequests_DescriptionActivity.this, MapsActivity.class);

                mapIntent.putExtra("latitudeOfMap", Double.parseDouble(latitude));
                mapIntent.putExtra("longitudeOfMap", Double.parseDouble(langitude));
                startActivity(mapIntent);

            }
        });
        edit_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String chatoredit = edit_button.getText().toString();

                if (chatoredit.equalsIgnoreCase("EDIT")) {
                    Intent myIntent = new Intent(ServicesRequests_DescriptionActivity.this, ServiceRequest_UpdationActivity.class);
                    myIntent.putExtra("service_id", service_id);
                    myIntent.putExtra("user_id", UserId_Main);
                    myIntent.putExtra("its_my_own_product",its_my_own_product);
                    startActivity(myIntent);
                    finish();
                } else if (chatoredit.equalsIgnoreCase("CHAT")) {
                    if (fb_data_pref.getBoolean("login_status", false)) {

					/*if(fb_data_pref.getBoolean("isemailverified", false))
                    {*/

                        //if(!owner_Id.equalsIgnoreCase(UserId_Main))
                        //{
                        String itemId = service_id;
                        Intent intent = new Intent(ServicesRequests_DescriptionActivity.this, ChatActivityServiceRequests.class);
                        intent.putExtra("chattingFrom", userFname);
                        intent.putExtra("chattingToName", firstname);
                        intent.putExtra("userIdFrom", UserId_Main);
                        intent.putExtra("userIdTo", ownerId);
                        intent.putExtra("itemId", itemId);
                        intent.putExtra("itemName", category + subcategory);
                        intent.putExtra("requestFrom", "description_scr");
                        intent.putExtra("itemImage", AppConstants.IMG_BASE_URL + image);
                        startActivity(intent);

                    /*}
                    else {
						Toast.makeText(getApplicationContext(), "Please Verify link from Your Email", Toast.LENGTH_LONG).show();

					}*/

                    } else {

                        //ownScreen=true;
//                    LogingScreenAlert("chatbg");
                        Toast.makeText(getApplicationContext(), "login to proceed", Toast.LENGTH_SHORT).show();

                    }
                }


            }
        });


        new ServiceRequestsDetailsAsyncTask().execute();

    }

    public class ServiceRequestsDetailsAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(ServicesRequests_DescriptionActivity.this, "", "Please wait...", false, false);

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
                    if (itemsObj.has("latitude")) {
                        latitude = itemsObj.getString("latitude");
                    }
                    if (itemsObj.has("langitude")) {
                        langitude = itemsObj.getString("langitude");
                    }
                    if (itemsObj.has("address")) {
                        address = itemsObj.getString("address");
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

            ser_requester_name_value.setText(requestername);
            category_name.setText(category);
            sub_category_name.setText("(" + subcategory + ")");
            req_desc_text.setText(description);
            email_txt_value.setText(" : "+email);
            phone_num_txt_value.setText(" : "+contact);
            distance_cal_txt.setText(distance);
            address_txt.setText(address);


           /* if (its_my_own_product) {
                if (isFavorite_flag) {
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.faverate_img_selected));
                } else {
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.feverate_img));
                }
            }*/

            MarkerOptions mp = new MarkerOptions();
            mp.position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(langitude)));
            mp.title("my position");
            googleMap.addMarker(mp).setVisible(false);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(latitude), Double.parseDouble(langitude)), 12));
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setAllGesturesEnabled(false);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utility.Call_Request:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + contact));

                    if (ActivityCompat.checkSelfPermission(ServicesRequests_DescriptionActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(callIntent);
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    //startActivityForResult(callIntent, REQUEST_CODE);
                } else {
                    //code for deny
                }
                break;
        }

    }

    public void callingIntent() {

        boolean result = Utility.checkCallPermission(ServicesRequests_DescriptionActivity.this);
        if (result) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + contact));

            if (ActivityCompat.checkSelfPermission(ServicesRequests_DescriptionActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }

    }

    protected void sendEmail()

    {

        Intent emailIntent = new Intent(ServicesRequests_DescriptionActivity.this, EmailActivity.class);

        startActivity(emailIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.ser_req_desc_menu_items, menu);

        if (its_my_own_product) {

            menu.findItem(R.id.desc_edit_menu).setVisible(true);
        } else {

            menu.findItem(R.id.desc_edit_menu).setVisible(false);
        }


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        this.menuItem = item;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;


            case R.id.show_popup_menu:
                // showPopup();
                View menuItemView = findViewById(R.id.show_popup_menu);
                PopupMenu popup = new PopupMenu(ServicesRequests_DescriptionActivity.this, menuItemView);

                //PopupMenu popup = new PopupMenu(ServiceDescriptionActivity_Services.this, popup_icon);
                popup.getMenuInflater().inflate(R.menu.desc_pop_up_menu_ser_req, popup.getMenu());

                if (its_my_own_product) {

                    popup.getMenu().findItem(R.id.desc_report).setVisible(false);
                    popup.getMenu().findItem(R.id.desc_delete).setVisible(true);

                } else {
                    popup.getMenu().findItem(R.id.desc_report).setVisible(true);
                    popup.getMenu().findItem(R.id.desc_delete).setVisible(false);
                }


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().toString().equalsIgnoreCase("Share")) {
                            String shareBody = "Look what I just found on Ask-n-Take!\n" + servicerequestId + "\n\n" + servicerequestId + "\n" + "http://www.askntake.com/";
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(sharingIntent, "Share with ..."));
                        }

                        if (item.getTitle().toString().equalsIgnoreCase("Report this Service Request")) {

                            if (!fb_data_pref.getBoolean("login_status", false)) {
                                Intent myIntent = new Intent(ServicesRequests_DescriptionActivity.this, SocialMediaRegistrationsActivity.class);
                                startActivity(myIntent);
                            } else {
                                Intent myIntent = new Intent(ServicesRequests_DescriptionActivity.this, ReportThisServiceRequestsScreenActivity.class);
                                myIntent.putExtra("service_id", service_id);
                                myIntent.putExtra("user_id", UserId_Main);
                                startActivity(myIntent);
                            }

                        }

                        if (item.getItemId() == R.id.desc_delete) {

                            if (!fb_data_pref.getBoolean("login_status", false)) {
                                Intent myIntent = new Intent(ServicesRequests_DescriptionActivity.this, SocialMediaRegistrationsActivity.class);
                                startActivity(myIntent);
                                finish();
                            } else {

                                deleteServiceRequest();
                                /*Intent myIntent = new Intent(ServiceDescriptionActivity_Services.this, ReportThisProductScreenActivity.class);
                                startActivity(myIntent);*/
                            }

                        }
                        return true;
                    }


                });

                popup.show();
                return true;

            case R.id.desc_edit_menu:

                Intent callupdationIntent = new Intent(ServicesRequests_DescriptionActivity.this, ServiceRequest_UpdationActivity.class);
                callupdationIntent.putExtra("service_id", service_id);
                callupdationIntent.putExtra("user_id", UserId_Main);
                callupdationIntent.putExtra("its_my_own_product",its_my_own_product);
                startActivity(callupdationIntent);
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteServiceRequest() {
        //deleteService

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ServicesRequests_DescriptionActivity.this);
        alertDialog.setMessage(R.string.delete_service_alert);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //new removeFromFaverotes().execute();
                // feverate_img.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.feverate_img));
                // menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.feverate_img));
                //new AddServiceToFavoriteAsync("remove").execute();
                new DeleteServiceAsyncTask().execute();


            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                dialog.cancel();
            }
        });
        alertDialog.show();


    }

    public class DeleteServiceAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(ServicesRequests_DescriptionActivity.this, "", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(AppConstants.deleteServiceRequest(UserId_Main, service_id), "GET", null);
            //String responceStatus = "";


            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            boolean status = false;

            String message;

            try {
                JSONObject jsObj = new JSONObject(result);
                if (jsObj.has("status")) {
                    status = jsObj.getBoolean("status");
                    if (status) {
                        message = jsObj.getString("message");
                        Intent homescreenIntent = new Intent(ServicesRequests_DescriptionActivity.this, NavigationDrawerActivity.class);
                        startActivity(homescreenIntent);
                        finish();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else if (jsObj.has("message")) {
                        message = jsObj.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
