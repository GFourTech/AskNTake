package com.askntake.admin.askntake;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import AppUtils.AppConstants;
import AppUtils.ConnectionDetector;
import AppUtils.DataKeyValues;
import AppUtils.ServiceHandler;
import AppUtils.Utility;
import Pojo.CommercAndOfficeServicePojo;
import Pojo.EventTypesPojo;
import Pojo.EventsServicePojo;
import Pojo.OneTimeRideServicePojo;
import Pojo.RestOtherServicesPojo;
import Pojo.RoomSharingServicePojo;
import Pojo.SharedPrefPojo;
import Pojo.SingleFamilyServicePojo;
import Pojo.TimingsPojo;

/**
 * Created by on 14/06/2017.
 */

public class ServiceUpdationActivity extends AppCompatActivity implements View.OnClickListener {


    public LinearLayout service_images_linear_layout, Linear_layout_gender, linear_lay_destination, linear_lay_time_travelling, map_linear_layout;
    HorizontalScrollView h_s_service_images;
    List<String> product_images;


    ConnectionDetector internetConnection;
    ProgressDialog mProgressDialog;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Dialog Internetdialog;

    HashMap<Integer, String> allimagesMap = new HashMap<Integer, String>();

    ImageView service_image_view;

    ArrayList<EventTypesPojo> eventTypesPojos = null;
    ArrayList<TimingsPojo> timingsPojos = null;

    TextInputLayout single_family_la_available_from, single_family_la_bed_rooms, single_family_la_bath_rooms, single_family_la_total_sq_feet, single_family_la_asking_price;
    EditText edit_single_family_available_from, edit_single_family_bedrooms, edit_single_family_bath_rooms, edit_single_family_total_sq_feet, edit_single_family_asking_price;
    Spinner single_family_spinner_price_type;


    TextInputLayout room_sharing_la_available_from, room_sharing_la_accommodates, room_sharing_la_bed_rooms, room_sharing_la_bath_rooms, room_sharing_la_asking_price;
    EditText edit_room_sharing_available_from, edit_room_sharing_accommodates, edit_room_sharing_bedrooms, edit_room_sharing_bath_rooms, edit_room_sharing_asking_price;
    Spinner room_sharing_gender_spin, room_sharing_price_type_spinner;


    TextInputLayout one_time_la_travel_date, one_time_la_destination_from, one_time_la_destination_to, one_time_la_travel_time;
    EditText edit_one_time_travel_date, edit_one_time_destination_from, edit_one_time_destination_to, edit_one_time_travel_time;
    Spinner one_time_spinner_time;


    TextInputLayout commercial_available_from, commercial_la_no_of_rooms, commercial_la_total_sq_feet, commercial_la_asking_price;
    EditText edit_commercial_available_from, edit_commercial_no_of_rooms, edit_commercial_total_sq_feet, edit_commercial_asking_price;
    Spinner commercial_spinner_price_type;


    String userChoosenTask;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public EditText edit_service_name;
    public EditText edit_provider_name;
    public EditText edit_about_this_service;
    public EditText edit_phone_number;
    public EditText edit_email;
    public EditText edit_web_link;
    public EditText edit_address;
    public TextInputLayout input_la_phone_number, input_la_email, input_la_web_link, input_la_address,
            input_la_date,
            input_timings_time;
    TextView text_gender, list_now_button;
    FrameLayout frame_layout;

    public Spinner spinner_Timings_mul,
            spinner_Timings_multi;

    public TextInputLayout input_la_provider_name, input_la_service_name, input_la_about_this_sevice,
            input_timings_time_mul;
    public EditText edit_date,
            edit_timing_time,
            edit_timings_time_mul;


    ArrayList<Boolean> imagesArray = new ArrayList<>();
    ArrayList<String> imagesURLArray = new ArrayList<>();
    ArrayList<Bitmap> bitmapArray = new ArrayList<>();


    Bitmap selected_image_bitmap = null;

    EditText Products_list, Products_sub_list;


    public View layout_rest_services_block, layout_one_time_ride_block, layout_room_sharing_block, layout_single_family_home_block, layout_commercial_and_office_block, layout_events_service_block;

    LinearLayout multiple_selection;

    String username, userId;

    String service_id, user_id;


    String user_latitude = "";
    String user_longitude = "";
    String user_zipcode = "";
    String user_location_address = "";

    boolean no_profile_image_flag, its_my_own_product;

    public LinearLayout rest_serv_timings_layout, rest_serv_linear_layout_timings,
            rest_serv_linear_layout_timings_to;

    public TextView txt_timings;

    Spinner rest_serv_spinner_day,
            rest_serv_spinner_Timings, event_type_spinner,
            rest_serv_spinner_Timings_to;


    TextInputLayout rest_serv_la_timings_time,
            rest_serv_la_timings_time_to;

    EditText edit_rest_serv_timings_time,
            edit_rest_serv_timings_time_to;


    String service_name, itemId, description, category, ownerId, email, image, firstname, lastname, zipcode,
            area, website, latitude, langitude, product_city, product_state, visitcount, favoritesCount, subcategory,
            day, daycount, fromtime, fromtimeType, totime, totimeType, eventType, contact, rating, avalableFrom, rooms, travelDate,
            timeOfLeaving, destinationFrom, destinationTo, availableFrom, accomodates, gender, askingprice,
            eventDate, timeTo, timeFrom, providr_name, bedrooms, bathrooms, areainsqft, price, priceType, id, eventID,
            upload_lattitude, upload_longitude;


    CommercAndOfficeServicePojo commercAndOfficeServicePojo = null;
    EventsServicePojo eventsServicePojo = null;
    OneTimeRideServicePojo oneTimeRideServicePojo = null;
    RestOtherServicesPojo restOtherServicesPojo = null;
    RoomSharingServicePojo roomSharingServicePojo = null;
    SingleFamilyServicePojo singleFamilyServicePojo = null;
    TimingsPojo timingsPojo = null;
    ArrayList<EventTypesPojo> eventTypesPojo = null;


    LinearLayout par_lay, event_parent_lay, type_of_event_lin, event_linear_lay;

    Button add_field_button, delete_button, add_event_button, delete_event_button;
    int visblecount = 1;
    int eventVisibleCount = 1;
    HashMap<Integer, String> imageUrlsMap = new HashMap<Integer, String>();

    HashMap<Integer, String> all_images_main_object = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_service_update);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Products_list = (EditText) findViewById(R.id.categories_main_list);
        Products_sub_list = (EditText) findViewById(R.id.categories_sub_main_list);


        internetConnection = new ConnectionDetector(getApplicationContext());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        service_id = getIntent().getStringExtra("service_id");
        user_id = getIntent().getStringExtra("user_id");
        its_my_own_product = getIntent().getBooleanExtra("its_my_own_product", false);
        upload_lattitude = getIntent().getStringExtra("upload_lattitude");
        upload_longitude = getIntent().getStringExtra("upload_longitude");


        SharedPreferences login_data_pref = getApplicationContext().getSharedPreferences(DataKeyValues.USER_DATA_PREF, Context.MODE_PRIVATE);
        username = login_data_pref.getString(DataKeyValues.USER_EMAIL, null);
        userId = login_data_pref.getString(DataKeyValues.USER_USERID, null);

        product_images = new ArrayList<>();
        frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
        h_s_service_images = (HorizontalScrollView) findViewById(R.id.h_s_service_images);

        map_linear_layout = (LinearLayout) findViewById(R.id.map_linear_layout);
        service_images_linear_layout = (LinearLayout) findViewById(R.id.service_images_linear_layout);
        linear_lay_destination = (LinearLayout) findViewById(R.id.linear_lay_destination);
        Linear_layout_gender = (LinearLayout) findViewById(R.id.Linear_layout_gender);
        linear_lay_time_travelling = (LinearLayout) findViewById(R.id.linear_lay_time_travelling);


        edit_service_name = (EditText) findViewById(R.id.edit_service_name);
        edit_provider_name = (EditText) findViewById(R.id.edit_provider_name);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_web_link = (EditText) findViewById(R.id.edit_web_link);
        edit_phone_number = (EditText) findViewById(R.id.edit_phone_number);
        edit_address = (EditText) findViewById(R.id.edit_address);
        edit_about_this_service = (EditText) findViewById(R.id.edit_about_this_service);


        input_la_address = (TextInputLayout) findViewById(R.id.input_la_address);
        input_la_web_link = (TextInputLayout) findViewById(R.id.input_la_web_link);
        input_la_email = (TextInputLayout) findViewById(R.id.input_la_email);
        input_la_service_name = (TextInputLayout) findViewById(R.id.input_la_service_name);
        input_la_provider_name = (TextInputLayout) findViewById(R.id.input_la_provider_name);
        input_la_about_this_sevice = (TextInputLayout) findViewById(R.id.input_la_about_this_sevice);
        input_la_phone_number = (TextInputLayout) findViewById(R.id.input_la_phone_number);


        edit_about_this_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(ServiceUpdationActivity.this);
                View promptsView = li.inflate(R.layout.description_popup, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ServiceUpdationActivity.this);

                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);


                userInput.setText(edit_about_this_service.getText());

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        edit_about_this_service.setText(userInput.getText());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();

            }
        });


        txt_timings = (TextView) findViewById(R.id.txt_timings);
        text_gender = (TextView) findViewById(R.id.text_gender);
        list_now_button = (TextView) findViewById(R.id.list_now_button);


        layout_rest_services_block = findViewById(R.id.layout_rest_services_block);
        layout_one_time_ride_block = findViewById(R.id.layout_one_time_ride_block);
        layout_room_sharing_block = findViewById(R.id.layout_room_sharing_block);
        layout_single_family_home_block = findViewById(R.id.layout_single_family_home_block);
        layout_commercial_and_office_block = findViewById(R.id.layout_commercial_and_office_block);
        layout_events_service_block = findViewById(R.id.layout_events_service_block);

        ////////EVENTS//////////////////////////////


        event_type_spinner = (Spinner) findViewById(R.id.event_type_spinner);

        multiple_selection = (LinearLayout) findViewById(R.id.multiple_selection);


        ///////////////////////////Events//////////////////


        commercial_available_from = (TextInputLayout) findViewById(R.id.commercial_available_from);
        commercial_la_no_of_rooms = (TextInputLayout) findViewById(R.id.commercial_la_no_of_rooms);
        commercial_la_total_sq_feet = (TextInputLayout) findViewById(R.id.commercial_la_total_sq_feet);
        commercial_la_asking_price = (TextInputLayout) findViewById(R.id.commercial_la_asking_price);
        commercial_spinner_price_type = (Spinner) findViewById(R.id.commercial_spinner_price_type);


        edit_commercial_available_from = (EditText) findViewById(R.id.edit_commercial_available_from);
        edit_commercial_no_of_rooms = (EditText) findViewById(R.id.edit_commercial_no_of_rooms);
        edit_commercial_total_sq_feet = (EditText) findViewById(R.id.edit_commercial_total_sq_feet);
        edit_commercial_asking_price = (EditText) findViewById(R.id.edit_commercial_asking_price);


        single_family_la_available_from = (TextInputLayout) findViewById(R.id.single_family_la_available_from);
        single_family_la_bed_rooms = (TextInputLayout) findViewById(R.id.single_family_la_bed_rooms);
        single_family_la_bath_rooms = (TextInputLayout) findViewById(R.id.single_family_la_bath_rooms);
        single_family_la_total_sq_feet = (TextInputLayout) findViewById(R.id.single_family_la_total_sq_feet);
        single_family_la_asking_price = (TextInputLayout) findViewById(R.id.single_family_la_asking_price);

        single_family_spinner_price_type = (Spinner) findViewById(R.id.single_family_spinner_price_type);


        edit_single_family_available_from = (EditText) findViewById(R.id.edit_single_family_available_from);
        edit_single_family_bedrooms = (EditText) findViewById(R.id.edit_single_family_bedrooms);
        edit_single_family_bath_rooms = (EditText) findViewById(R.id.edit_single_family_bath_rooms);
        edit_single_family_total_sq_feet = (EditText) findViewById(R.id.edit_single_family_total_sq_feet);
        edit_single_family_asking_price = (EditText) findViewById(R.id.edit_single_family_asking_price);


        room_sharing_la_available_from = (TextInputLayout) findViewById(R.id.room_sharing_la_available_from);
        room_sharing_la_accommodates = (TextInputLayout) findViewById(R.id.room_sharing_la_accommodates);
        room_sharing_la_bed_rooms = (TextInputLayout) findViewById(R.id.room_sharing_la_bed_rooms);
        room_sharing_la_bath_rooms = (TextInputLayout) findViewById(R.id.room_sharing_la_bath_rooms);
        room_sharing_la_asking_price = (TextInputLayout) findViewById(R.id.room_sharing_la_asking_price);

        room_sharing_gender_spin = (Spinner) findViewById(R.id.room_sharing_gender_spin);
        room_sharing_price_type_spinner = (Spinner) findViewById(R.id.room_sharing_price_type_spinner);


        edit_room_sharing_available_from = (EditText) findViewById(R.id.edit_room_sharing_available_from);
        edit_room_sharing_accommodates = (EditText) findViewById(R.id.edit_room_sharing_accommodates);
        edit_room_sharing_bedrooms = (EditText) findViewById(R.id.edit_room_sharing_bedrooms);
        edit_room_sharing_bath_rooms = (EditText) findViewById(R.id.edit_room_sharing_bath_rooms);
        edit_room_sharing_asking_price = (EditText) findViewById(R.id.edit_room_sharing_asking_price);

        one_time_la_travel_date = (TextInputLayout) findViewById(R.id.one_time_la_travel_date);
        one_time_la_destination_from = (TextInputLayout) findViewById(R.id.one_time_la_destination_from);
        one_time_la_destination_to = (TextInputLayout) findViewById(R.id.one_time_la_destination_to);
        one_time_la_travel_time = (TextInputLayout) findViewById(R.id.one_time_la_travel_time);
        one_time_spinner_time = (Spinner) findViewById(R.id.one_time_spinner_time);


        edit_one_time_travel_date = (EditText) findViewById(R.id.edit_one_time_travel_date);
        edit_one_time_destination_from = (EditText) findViewById(R.id.edit_one_time_destination_from);
        edit_one_time_destination_to = (EditText) findViewById(R.id.edit_one_time_destination_to);
        edit_one_time_travel_time = (EditText) findViewById(R.id.edit_one_time_travel_time);


        par_lay = (LinearLayout) findViewById(R.id.par_lay);
        add_field_button = (Button) findViewById(R.id.add_field_button);
        add_event_button = (Button) findViewById(R.id.add_event_button);


        event_parent_lay = (LinearLayout) findViewById(R.id.event_parent_lay);
        type_of_event_lin = (LinearLayout) findViewById(R.id.type_of_event_lin);
        event_linear_lay = (LinearLayout) findViewById(R.id.event_linear_lay);


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = layoutInflater.inflate(R.layout.fields, null);
        delete_button = (Button) rowView.findViewById(R.id.delete_button);


        LayoutInflater event_layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View event_rowView = event_layoutInflater.inflate(R.layout.field_events, null);
        delete_event_button = (Button) event_rowView.findViewById(R.id.delete_event_button);


        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(100, 100);

        addServiceImages();


        add_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.field_events, null);
                // Add the new row before the add field button.
                //int i = par_lay.getChildCount();
                delete_event_button = (Button) rowView.findViewById(R.id.delete_event_button);
                delete_event_button.setOnClickListener(ServiceUpdationActivity.this);
                if (eventVisibleCount < 5) {
                    // rowView.setId(par_lay.getChildCount());
                    rowView.setTag("visble,0");
                    event_parent_lay.addView(rowView, event_parent_lay.getChildCount());
                    add_event_button.setVisibility(View.VISIBLE);
                    eventVisibleCount++;
                }
                if (eventVisibleCount > 5) {
                    add_event_button.setVisibility(View.GONE);
                }

                // Toast.makeText(getApplicationContext(), "count is:" + event_parent_lay.getChildCount(), Toast.LENGTH_SHORT).show();

            }
        });

        delete_event_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (eventVisibleCount > 1) {
                    // par_lay.removeViewInLayout((View) v.getParent());
                    ViewParent tempv = v.getParent();
                    View tempv2 = (View) tempv;
                    tempv2.setTag("gone");
                    ViewParent tempv3 = tempv2.getParent();
                    View tempv4 = (View) tempv3;
                    tempv4.setTag("gone");

                    ViewParent tempv5 = tempv4.getParent();
                    View tempv6 = (View) tempv5;
                    tempv6.setTag("gone");

                    tempv6.setVisibility(View.GONE);

                    //  par_lay.removeView(tempv4);
                    eventVisibleCount--;
                    if (eventVisibleCount < 5) {
                        add_event_button.setVisibility(View.VISIBLE);
                    }
                    // Toast.makeText(getApplicationContext(), "count is:" + event_parent_lay.getChildCount(), Toast.LENGTH_SHORT).show();

                } else {

                }

            }
        });


        add_field_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.fields, null);
                // Add the new row before the add field button.
                //int i = par_lay.getChildCount();
                delete_button = (Button) rowView.findViewById(R.id.delete_button);
                delete_button.setOnClickListener(this);
                if (visblecount < 7) {
                    // rowView.setId(par_lay.getChildCount());
                    rowView.setTag("visble");
                    par_lay.addView(rowView, par_lay.getChildCount());
                    add_field_button.setVisibility(View.VISIBLE);
                    visblecount++;
                }
                if (visblecount > 6) {
                    add_field_button.setVisibility(View.GONE);
                }

                //  Toast.makeText(getApplicationContext(), "count is:" + par_lay.getChildCount(), Toast.LENGTH_SHORT).show();

            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (visblecount > 1) {
                    // par_lay.removeViewInLayout((View) v.getParent());
                    ViewParent tempv = v.getParent();
                    View tempv2 = (View) tempv;
                    tempv2.setTag("gone");
                    ViewParent tempv3 = tempv2.getParent();
                    View tempv4 = (View) tempv3;
                    tempv4.setTag("gone");

                    ViewParent tempv5 = tempv4.getParent();
                    View tempv6 = (View) tempv5;
                    tempv6.setTag("gone");

                    tempv6.setVisibility(View.GONE);

                    //  par_lay.removeView(tempv4);
                    visblecount--;
                    if (visblecount < 7) {
                        add_field_button.setVisibility(View.VISIBLE);
                    }
                    //  Toast.makeText(getApplicationContext(), "count is:" + par_lay.getChildCount(), Toast.LENGTH_SHORT).show();

                } else {

                }

            }
        });


        list_now_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (imagesArray.get(0) == true || imagesArray.get(1) == true || imagesArray.get(2) == true || imagesArray.get(3) == true || imagesArray.get(4) == true) {
                    if (edit_service_name.getText().length() > 0) {


                        if (edit_provider_name.getText().length() > 0) {

                            if (edit_about_this_service.getText().length() > 0) {

                                if (edit_phone_number.getText().length() > 0) {

                                    if (edit_email.getText().toString().matches(emailPattern) && edit_email.getText().toString().length() > 0) {

                                        String[] rentals = getResources().getStringArray(R.array.rentals);

                                        String[] rides = getResources().getStringArray(R.array.rides);

                                        String[] events = getResources().getStringArray(R.array.events);


                                        if (Products_sub_list.getText().toString().equalsIgnoreCase(rentals[2])) {


                                            if (edit_commercial_available_from.getText() != null && !edit_commercial_available_from.getText().toString().isEmpty() && edit_commercial_available_from.getText().length() > 0)
                                            // if(edit_commercial_available_from.getText().length()>0)
                                            {
                                                if (edit_commercial_no_of_rooms.getText() != null && !edit_commercial_no_of_rooms.getText().toString().isEmpty()) {
                                                    if (edit_commercial_total_sq_feet.getText() != null && !edit_commercial_total_sq_feet.getText().toString().isEmpty()) {

                                                        if (edit_commercial_asking_price.getText() != null && !edit_commercial_asking_price.getText().toString().isEmpty()) {

                                                            String availDate = edit_commercial_available_from.getText().toString();
                                                            String roomscount = edit_commercial_no_of_rooms.getText().toString();
                                                            String totaArea = edit_commercial_total_sq_feet.getText().toString();
                                                            String price = edit_commercial_asking_price.getText().toString();
                                                            String priceType = commercial_spinner_price_type.getSelectedItem().toString();
                                                            String key = id;


                                                            //String price = edit_asking_price_value + spinner_price_type_value;
                                                            commercAndOfficeServicePojo = getCommercialPojo(availDate, roomscount, totaArea, price, priceType, key);
                                                            runServer();
                                                        } else {
                                                            commercial_la_asking_price.requestFocus();
                                                            edit_commercial_asking_price.setError("Please provide Price");
                                                        }
                                                    } else {
                                                        commercial_la_total_sq_feet.requestFocus();
                                                        edit_commercial_total_sq_feet.setError("Please provide size");
                                                    }

                                                } else {

                                                    commercial_la_no_of_rooms.requestFocus();
                                                    edit_commercial_no_of_rooms.setError("Please provide no of rooms");

                                                }
                                            } else {
                                                commercial_available_from.requestFocus();
                                                edit_commercial_available_from.setError("Please provide Date:");

                                            }


                                        } else if (Products_sub_list.getText().toString().equalsIgnoreCase(rides[0])) {

                                            if (edit_one_time_travel_date.getText() != null && !edit_one_time_travel_date.getText().toString().isEmpty() && edit_one_time_travel_date.getText().length() > 0) {
                                                if (edit_one_time_destination_from.getText() != null && !edit_one_time_destination_from.getText().toString().isEmpty() && edit_one_time_destination_from.getText().length() > 0) {
                                                    if (edit_one_time_destination_to.getText() != null && !edit_one_time_destination_to.getText().toString().isEmpty() && edit_one_time_destination_to.getText().length() > 0) {
                                                        if (edit_one_time_travel_time.getText() != null && !edit_one_time_travel_time.getText().toString().isEmpty() && edit_one_time_travel_time.getText().length() > 0) {
                                                            String travelDate = edit_one_time_travel_date.getText().toString();
                                                            String destfrom = edit_one_time_destination_from.getText().toString();
                                                            String destto = edit_one_time_destination_to.getText().toString();
                                                            String edit_travel_time_value = edit_one_time_travel_time.getText().toString();
                                                            String spinner_time_value = one_time_spinner_time.getSelectedItem().toString();
                                                            String travel_time = edit_travel_time_value + "  " + spinner_time_value;
                                                            String key = id;

                                                            oneTimeRideServicePojo = getOneTimeRideServicePojo(travelDate, destfrom, destto, travel_time, key);
                                                            runServer();
                                                        } else {
                                                            one_time_la_travel_time.requestFocus();
                                                            edit_one_time_travel_time.setError("Please enter time");
                                                        }
                                                    } else {
                                                        one_time_la_destination_to.requestFocus();
                                                        edit_one_time_destination_to.setError("Please enter to Destination value");

                                                    }
                                                } else {
                                                    one_time_la_destination_from.requestFocus();
                                                    edit_one_time_destination_from.setError("Please enter From Destination value");

                                                }

                                            } else {

                                                one_time_la_travel_date.requestFocus();
                                                edit_one_time_travel_date.setError("Please enter Date");


                                            }


                                        } else if (Products_sub_list.getText().toString().equalsIgnoreCase(rentals[1])) {

                                            if (edit_room_sharing_available_from.getText() != null && !edit_room_sharing_available_from.getText().toString().isEmpty() && edit_room_sharing_available_from.getText().length() > 0) {
                                                if (edit_room_sharing_accommodates.getText() != null && !edit_room_sharing_accommodates.getText().toString().isEmpty() && edit_room_sharing_accommodates.getText().length() > 0) {
                                                    if (edit_room_sharing_bedrooms.getText() != null && !edit_room_sharing_bedrooms.getText().toString().isEmpty() && edit_room_sharing_bedrooms.getText().length() > 0) {
                                                        if (edit_room_sharing_bath_rooms.getText() != null && !edit_room_sharing_bath_rooms.getText().toString().isEmpty() && edit_room_sharing_bath_rooms.getText().length() > 0) {
                                                            if (edit_room_sharing_asking_price.getText() != null && !edit_room_sharing_asking_price.getText().toString().isEmpty() && edit_room_sharing_asking_price.getText().length() > 0) {


                                                                String availDate = edit_room_sharing_available_from.getText().toString();
                                                                String accommodates = edit_room_sharing_accommodates.getText().toString();
                                                                String bedroomcount = edit_room_sharing_bedrooms.getText().toString();
                                                                String bathroomcount = edit_room_sharing_bath_rooms.getText().toString();
                                                                String gendervalue = room_sharing_gender_spin.getSelectedItem().toString();
                                                                String price = edit_room_sharing_asking_price.getText().toString();
                                                                String priceType = room_sharing_price_type_spinner.getSelectedItem().toString();
                                                                String key = id;

                                                                roomSharingServicePojo = getRoomSharingServicePojo(availDate, accommodates, bedroomcount, bathroomcount, gendervalue, price, priceType, key);
                                                                runServer();
                                                            } else {

                                                                room_sharing_la_asking_price.requestFocus();
                                                                edit_room_sharing_asking_price.setError("Please enter Price");
                                                            }

                                                        } else {
                                                            room_sharing_la_bath_rooms.requestFocus();
                                                            edit_room_sharing_bath_rooms.setError("Please Enter Bathroom Count");
                                                        }

                                                    } else {
                                                        room_sharing_la_bed_rooms.requestFocus();
                                                        edit_room_sharing_bedrooms.setError("Please Enter Bedroom Count");
                                                    }
                                                } else {
                                                    room_sharing_la_accommodates.requestFocus();
                                                    edit_room_sharing_accommodates.setError("Please Enter accomodates value");
                                                }


                                            } else {
                                                room_sharing_la_available_from.requestFocus();
                                                edit_room_sharing_available_from.setError("Please Enter Available Date");
                                            }


                                        } else if (Products_sub_list.getText().toString().equalsIgnoreCase(rentals[0])) {


                                            if (edit_single_family_available_from.getText() != null && !edit_single_family_available_from.getText().toString().isEmpty() && edit_single_family_available_from.getText().length() > 0) {
                                                if (edit_single_family_bedrooms.getText() != null && !edit_single_family_bedrooms.getText().toString().isEmpty() && edit_single_family_bedrooms.getText().length() > 0) {
                                                    if (edit_single_family_bath_rooms.getText() != null && !edit_single_family_bath_rooms.getText().toString().isEmpty() && edit_single_family_bath_rooms.getText().length() > 0) {
                                                        if (edit_single_family_total_sq_feet.getText() != null && !edit_single_family_total_sq_feet.getText().toString().isEmpty() && edit_single_family_total_sq_feet.getText().length() > 0) {
                                                            if (edit_single_family_asking_price.getText() != null && !edit_single_family_asking_price.getText().toString().isEmpty() && edit_single_family_asking_price.getText().length() > 0) {
                                                                String availDate = edit_single_family_available_from.getText().toString();
                                                                String bedroomcount = edit_single_family_bedrooms.getText().toString();
                                                                String bathroomcount = edit_single_family_bath_rooms.getText().toString();
                                                                String totaArea = edit_single_family_total_sq_feet.getText().toString();
                                                                String price = edit_single_family_asking_price.getText().toString();
                                                                String pricetype = single_family_spinner_price_type.getSelectedItem().toString();
                                                                String key = id;

                                                                singleFamilyServicePojo = getSingleFamilyServicePojo(availDate, bedroomcount, bathroomcount, totaArea, price, pricetype, key);
                                                                runServer();
                                                            } else {
                                                                single_family_la_asking_price.requestFocus();
                                                                edit_single_family_asking_price.setError("Please enter Price");
                                                            }

                                                        } else {
                                                            single_family_la_total_sq_feet.requestFocus();
                                                            edit_single_family_total_sq_feet.setError("Please enter Square feets");
                                                        }

                                                    } else {
                                                        single_family_la_bath_rooms.requestFocus();
                                                        edit_single_family_bath_rooms.setError("Please Enter Bathrooms Count");
                                                    }

                                                } else {
                                                    single_family_la_bed_rooms.requestFocus();
                                                    edit_single_family_bedrooms.setError("Please enter Bedrooms Count");
                                                }

                                            } else {
                                                single_family_la_available_from.requestFocus();
                                                edit_single_family_available_from.setError("Please Enter Available Date");
                                            }

                                        }

                                        ///////////Events

                                        else if (Products_sub_list.getText().toString().equalsIgnoreCase(events[0]) || Products_sub_list.getText().toString().equalsIgnoreCase(events[1]) || Products_sub_list.getText().toString().equalsIgnoreCase(events[2])) {


                                            String event_type = event_type_spinner.getSelectedItem().toString().trim();
                                            String event_id = eventID;
                                            eventTypesPojos = new ArrayList<EventTypesPojo>();


                                            for (int i = 0; i < event_parent_lay.getChildCount(); ++i) {
                                                View rootview = event_parent_lay.getChildAt(i);
                                                //    rootview.setVisibility(View.GONE);
                                                // Toast.makeText(getApplicationContext(), rootview.getTag().toString(), Toast.LENGTH_SHORT).show();

                                                if (!rootview.getTag().equals("gone")) {

                                                    multiple_selection = (LinearLayout) rootview.findViewById(R.id.multiple_selection);

                                                    input_la_date = (TextInputLayout) rootview.findViewById(R.id.input_la_date);

                                                    edit_date = (EditText) rootview.findViewById(R.id.edit_date);

                                                    input_timings_time = (TextInputLayout) rootview.findViewById(R.id.input_timings_time);

                                                    edit_timing_time = (EditText) rootview.findViewById(R.id.edit_timing_time);

                                                    spinner_Timings_mul = (Spinner) rootview.findViewById(R.id.spinner_Timings_mul);

                                                    input_timings_time_mul = (TextInputLayout) rootview.findViewById(R.id.input_timings_time_mul);

                                                    edit_timings_time_mul = (EditText) rootview.findViewById(R.id.edit_timings_time_mul);

                                                    spinner_Timings_multi = (Spinner) rootview.findViewById(R.id.spinner_Timings_multi);


                                                    String id = rootview.getTag().toString().split(",")[1];
                                                    // String key = id;

                                                    EventTypesPojo eventTypesPojoObj = new EventTypesPojo();
                                                    eventTypesPojoObj.setEvent_date(edit_date.getText().toString().trim());
                                                    String from_time_value = edit_timing_time.getText().toString();
                                                    String from_time_type = spinner_Timings_mul.getSelectedItem().toString();
                                                    eventTypesPojoObj.setFromTime(from_time_value + " " + from_time_type);
                                                    String to_time_value = edit_timings_time_mul.getText().toString();
                                                    String to_time_type = spinner_Timings_multi.getSelectedItem().toString();
                                                    eventTypesPojoObj.setToTime(to_time_value + " " + to_time_type);
                                                    eventTypesPojoObj.setId(id);

                                                    if ((edit_date != null && edit_timing_time != null && edit_timings_time_mul != null) && !(edit_date.getText().toString().isEmpty() && edit_timing_time.getText().toString().isEmpty() && edit_timings_time_mul.getText().toString().isEmpty())) {

                                                        eventTypesPojos.add(eventTypesPojoObj);

                                                       /* Toast.makeText(getApplicationContext(), edit_date.getText().toString() + "\n" + edit_timing_time.getText().toString() + "\n" +
                                                                spinner_Timings_mul.getSelectedItem().toString() + "\n" + to_time_value + "\n" +
                                                                to_time_type, Toast.LENGTH_SHORT).show();*/

                                                    }

                                                }

                                            }


                                            eventsServicePojo = getEventsServicePojo(event_type, event_id, eventTypesPojos);

                                            runServer();

                                        } else {

                                            timingsPojos = new ArrayList<TimingsPojo>();


                                            for (int i = 0; i < par_lay.getChildCount(); ++i) {
                                                View rootview = par_lay.getChildAt(i);
                                                //    rootview.setVisibility(View.GONE);
                                                // Toast.makeText(getApplicationContext(), rootview.getTag().toString(), Toast.LENGTH_SHORT).show();

                                                if (!rootview.getTag().equals("gone")) {

                                                    rest_serv_la_timings_time = (TextInputLayout) rootview.findViewById(R.id.rest_serv_la_timings_time);
                                                    rest_serv_la_timings_time_to = (TextInputLayout) rootview.findViewById(R.id.rest_serv_la_timings_time_to);
                                                    rest_serv_spinner_day = (Spinner) rootview.findViewById(R.id.rest_serv_spinner_day);
                                                    rest_serv_spinner_Timings = (Spinner) rootview.findViewById(R.id.rest_serv_spinner_Timings);
                                                    rest_serv_spinner_Timings_to = (Spinner) rootview.findViewById(R.id.rest_serv_spinner_Timings_to);
                                                    edit_rest_serv_timings_time = (EditText) rootview.findViewById(R.id.edit_rest_serv_timings_time);
                                                    edit_rest_serv_timings_time_to = (EditText) rootview.findViewById(R.id.edit_rest_serv_timings_time_to);
                                                    rest_serv_timings_layout = (LinearLayout) rootview.findViewById(R.id.rest_serv_timings_layout);
                                                    rest_serv_linear_layout_timings = (LinearLayout) rootview.findViewById(R.id.rest_serv_linear_layout_timings);
                                                    rest_serv_linear_layout_timings_to = (LinearLayout) rootview.findViewById(R.id.rest_serv_linear_layout_timings_to);

                                                    TimingsPojo timingsPojoObj = new TimingsPojo();
                                                    String daycount = rest_serv_spinner_day.getSelectedItemPosition() + "".trim();
                                                    String rest_serv_spinner_day_value = rest_serv_spinner_day.getSelectedItem().toString().trim();
                                                    timingsPojoObj.setWeek_day(rest_serv_spinner_day_value);
                                                    timingsPojoObj.setDaycount(daycount);
                                                    String edit_rest_serv_timings_time_value = edit_rest_serv_timings_time.getText().toString();
                                                    String rest_serv_spinner_Timings_value = rest_serv_spinner_Timings.getSelectedItem().toString();
                                                    timingsPojoObj.setFromTime(edit_rest_serv_timings_time_value + " " + rest_serv_spinner_Timings_value);
                                                    String edit_rest_serv_timings_time_to_value = edit_rest_serv_timings_time_to.getText().toString();
                                                    String rest_serv_spinner_Timings_to_value = rest_serv_spinner_Timings_to.getSelectedItem().toString();
                                                    timingsPojoObj.setToTime(edit_rest_serv_timings_time_to_value + " " + rest_serv_spinner_Timings_to_value);
                                                    // timingsPojos.add(timingsPojoObj);

                                                    if ((edit_rest_serv_timings_time_value != null && edit_rest_serv_timings_time_to_value != null) && !(edit_rest_serv_timings_time_value.isEmpty() && edit_rest_serv_timings_time_to_value.isEmpty())) {
                                                        timingsPojos.add(timingsPojoObj);

                                                       /* Toast.makeText(getApplicationContext(), rest_serv_spinner_day_value + "\n" + edit_rest_serv_timings_time_to_value + "\n" +
                                                                rest_serv_spinner_Timings_value + "\n" + edit_rest_serv_timings_time_to_value + "\n" +
                                                                rest_serv_spinner_Timings_to_value, Toast.LENGTH_SHORT).show();*/

                                                    }

                                                }

                                            }
                                            restOtherServicesPojo = getRestOtherServicesPojo(timingsPojos);
                                            runServer();

                                        }


                                    } else {
                                        input_la_email.requestFocus();
                                        edit_email.setError("Please provide Valid Email");
                                    }
                                } else {
                                    input_la_phone_number.requestFocus();
                                    edit_phone_number.setError("Please provide Phone Number");
                                }
                            } else {
                                input_la_about_this_sevice.requestFocus();
                                edit_about_this_service.setError("Please provide About the Service");
                            }
                        } else {
                            input_la_provider_name.requestFocus();
                            edit_provider_name.setError("Please provide Provider Name");
                        }

                    } else {
                        input_la_service_name.requestFocus();
                        edit_service_name.setError("Please provide Service Name");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please select images", Toast.LENGTH_SHORT).show();
                }


            }
        });


        if (internetConnection.isConnectingToInternet(getApplicationContext())) {
            new GetServiceDetailsAsyncTask().execute();

        } else {
            InternetConnectionAlert();
        }

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.delete_button) {
            if (visblecount > 1) {
                ViewParent tempv = view.getParent();
                View tempv2 = (View) tempv;
                tempv2.setTag("gone");
                ViewParent tempv3 = tempv2.getParent();
                View tempv4 = (View) tempv3;
                tempv4.setTag("gone");

                ViewParent tempv5 = tempv4.getParent();
                View tempv6 = (View) tempv5;
                tempv6.setTag("gone");

                tempv6.setVisibility(View.GONE);

                visblecount--;
                if (visblecount < 7) {
                    add_field_button.setVisibility(View.VISIBLE);
                }
                // Toast  .makeText(getApplicationContext(), "count is:" + par_lay.getChildCount(), Toast.LENGTH_SHORT).show();

            }// par_lay.removeViewInLayout((View) v.getParent());

        } else if (view.getId() == R.id.delete_event_button) {
            if (eventVisibleCount > 1) {
                ViewParent tempv = view.getParent();
                View tempv2 = (View) tempv;
                tempv2.setTag("gone");
                ViewParent tempv3 = tempv2.getParent();
                View tempv4 = (View) tempv3;
                tempv4.setTag("gone");

                ViewParent tempv5 = tempv4.getParent();
                View tempv6 = (View) tempv5;
                tempv6.setTag("gone");

                tempv6.setVisibility(View.GONE);

                eventVisibleCount--;
                if (eventVisibleCount < 5) {
                    add_event_button.setVisibility(View.VISIBLE);
                }
                //Toast.makeText(getApplicationContext(), "count is:" + event_parent_lay.getChildCount(), Toast.LENGTH_SHORT).show();


            }
        } else {
            ImageView selectedImage = (ImageView) view;
            String selectedImage_postion = selectedImage.getTag().toString();
            if (imagesArray.get(Integer.parseInt(selectedImage_postion))) {
                changeOrRemoveImageAlert(Integer.parseInt(selectedImage_postion));
            } else {
                selectImage();
            }
        }


    }

    private void addServiceImages() {

        for (int i = 0; i < 5; i++) {

            imagesArray.add(false);
            service_images_linear_layout.setOrientation(LinearLayout.HORIZONTAL);
            service_image_view = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(125, 125);
            service_image_view.setLayoutParams(llp);
            service_image_view.setImageResource(R.mipmap.ic_camera);
            service_image_view.setTag("" + i);
            service_image_view.setId(i);
            service_image_view.setOnClickListener(this);
            service_images_linear_layout.addView(service_image_view);
            h_s_service_images.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Camera"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Gallery"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void changeOrRemoveImageAlert(final int selected_img_postion) {
        final CharSequence[] items = {"Change Picture", "Remove Picture"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceUpdationActivity.this);

        builder.setTitle(R.string.usepicture);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ServiceUpdationActivity.this);

                if (items[item].equals("Change Picture")) {

                    imagesArray.add(selected_img_postion, false);
                    selectImage();

                } else if (items[item].equals("Remove Picture")) {

                    ImageView selectedImage = (ImageView) service_images_linear_layout.getChildAt(selected_img_postion);
                    selectedImage.setImageResource(R.mipmap.ic_camera);
                    imagesArray.add(selected_img_postion, false);
                    imageUrlsMap.remove(selected_img_postion + 1);
                }
            }
        });
        builder.show();
    }


    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceUpdationActivity.this);
        builder.setTitle(R.string.usepicture);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getApplicationContext());

                if (items[item].equals("Camera")) {
                    userChoosenTask = "Camera";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Gallery")) {
                    userChoosenTask = "Gallery";
                    if (result)
                        galleryIntent();

                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                selected_image_bitmap = onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                selected_image_bitmap = onCaptureImageResult(data);
            if (selected_image_bitmap != null) {
                for (int i = 0; i < imagesArray.size(); i++) {
                    if (!imagesArray.get(i)) {
                        ImageView selectedImage = (ImageView) service_images_linear_layout.getChildAt(i);
                        selectedImage.setImageBitmap(selected_image_bitmap);
                        imagesArray.add(i, true);
                        bitmapArray.add(selected_image_bitmap);
                        all_images_main_object.put(i + 1, createBase64(selected_image_bitmap));
                        imageUrlsMap.remove(i + 1);
                        break;
                    }
                }
            }
        }
    }

    private Bitmap onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

        int width = 125;
        int height = 125;
        thumbnail = Bitmap.createScaledBitmap(thumbnail, width, height, true);

        //getJSONObject();

        thumbnail = thumbnail;
        /*main_image_assign.setImageBitmap(thumbnail);
        BitmapDrawable drawable = (BitmapDrawable) main_image_assign.getDrawable();
        final Bitmap bitmap = drawable.getBitmap();
        File sdCardDirectory = new File(Environment.getExternalStorageDirectory(), "Ask-n-Take");
        if (!sdCardDirectory.exists()) {
            sdCardDirectory.mkdirs();
        }
        final File image = new File(sdCardDirectory, "wallpaper.jpeg");
        boolean success = false;


        FileOutputStream outStream;
        try {

            outStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

            outStream.flush();
            outStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            // Toast.makeText(getApplicationContext(), "Image saved with success", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(),"Error during image saving", Toast.LENGTH_LONG).show();
        }*/


        return thumbnail;
    }

    private Bitmap onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                int width = 125;
                int height = 125;
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 30, bytes);
                bm = Bitmap.createScaledBitmap(bm, width, height, true);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //  main_image_assign.setImageBitmap(bm);
        // BitmapDrawable drawable = (BitmapDrawable) main_image_assign.getDrawable();
        //final Bitmap bitmap = drawable.getBitmap();
       /* File sdCardDirectory = new File(Environment.getExternalStorageDirectory(), "Ask-n-Take");
        if (!sdCardDirectory.exists()) {
            sdCardDirectory.mkdirs();
        }
        //File sdCardDirectory = Environment.getExternalStorageDirectory();
        final File image = new File(sdCardDirectory, "wallpaper.jpeg");
        boolean success = false;


        FileOutputStream outStream;
        try {

            outStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

            outStream.flush();
            outStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            // Toast.makeText(getApplicationContext(), "success",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(),
                    "Error during image saving", Toast.LENGTH_LONG).show();
        }*/
        return bm;
    }

    public String createBase64(Bitmap bitimage) {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitimage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

        return encodedImage;
    }


    private CommercAndOfficeServicePojo getCommercialPojo(String availDate, String roomscount, String totaArea, String price, String priceType, String key) {
        CommercAndOfficeServicePojo pojo = new CommercAndOfficeServicePojo();

        pojo.setAvailable_from_date(availDate);
        pojo.setNumber_of_rooms(roomscount);
        pojo.setPrice(price);
        pojo.setPrice_type(priceType);
        pojo.setTotalsq_feet(totaArea);
        pojo.setId(key);

        return pojo;

    }

    private OneTimeRideServicePojo getOneTimeRideServicePojo(String traveldate, String destination_from, String destination_to, String travel_time, String key) {
        OneTimeRideServicePojo pojo = new OneTimeRideServicePojo();

        pojo.setTraveldate(traveldate);
        pojo.setDestination_from(destination_from);
        pojo.setDestination_to(destination_to);
        pojo.setTravel_time(travel_time);
        pojo.setId(key);

        return pojo;

    }

    private RoomSharingServicePojo getRoomSharingServicePojo(String availDate, String accommodates, String bedroomcount, String bathroomcount, String gendervalue, String price, String priceType, String key) {
        RoomSharingServicePojo pojo = new RoomSharingServicePojo();

        pojo.setAvailable_from_date(availDate);
        pojo.setAccommadates(accommodates);
        pojo.setNo_bedrooms(bedroomcount);
        pojo.setNo_bathrooms(bathroomcount);
        pojo.setGender(gendervalue);
        pojo.setPrice(price);
        pojo.setPrice_type(priceType);
        pojo.setId(key);

        return pojo;

    }

    private SingleFamilyServicePojo getSingleFamilyServicePojo(String availDate, String bedroomcount, String bathroomcount, String totaArea, String price, String pricetype, String key) {
        SingleFamilyServicePojo pojo = new SingleFamilyServicePojo();


        pojo.setAvailable_from_date(availDate);
        pojo.setNo_bedrooms(bedroomcount);
        pojo.setNo_bathrooms(bathroomcount);
        pojo.setTotalsq_feet(totaArea);
        pojo.setPrice(price);
        pojo.setPrice_type(pricetype);
        pojo.setId(key);

        return pojo;

    }

    private RestOtherServicesPojo getRestOtherServicesPojo(ArrayList<TimingsPojo> timingsPojos1) {
        RestOtherServicesPojo pojo = new RestOtherServicesPojo();

        pojo.setTimings(timingsPojos1);

        return pojo;

    }

    private EventsServicePojo getEventsServicePojo(String event_type, String event_id, ArrayList<EventTypesPojo> eventTypesPojos) {
        EventsServicePojo pojo = new EventsServicePojo();


//        pojo.setServices(typeOfServicesPojo);

        pojo.setEvent_type(event_type);
        pojo.setEventId(event_id);
        pojo.setEvents(eventTypesPojos);


        return pojo;

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

    private class ServiceUpdateAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = getJSONObject();
            String service_result = serviceHandler.makeServiceCall(AppConstants.SERVICES_UPDATION_URL, "POST", requestObject);

            return service_result;
        }

        @Override
        protected void onPostExecute(String service_result) {
            super.onPostExecute(service_result);

            boolean status = false;
            try {

                JSONObject jsObj = new JSONObject(service_result);
                if (jsObj.has("status")) {
                    status = jsObj.getBoolean("status");
                    if (status) {

                        Intent loginIntent = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                        startActivity(loginIntent);
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


            requestObject.accumulate(DataKeyValues.SERVICE_ID, service_id);
            requestObject.accumulate(DataKeyValues.UPLOAD_USERID, userId);
            requestObject.accumulate(DataKeyValues.UPLOAD_NAME, edit_service_name.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_PROVIDER, edit_provider_name.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_DESCRIPTION, edit_about_this_service.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_CATEGORY, Products_list.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_SUBCATEGORY, Products_sub_list.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_CONTACT, edit_phone_number.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_EMAIL, edit_email.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_WEBSITE, edit_web_link.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_ADDRESS, edit_address.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_LATITUDE, user_latitude);
            requestObject.accumulate(DataKeyValues.UPLOAD_LANGITUDE, user_longitude);
            requestObject.accumulate(DataKeyValues.UPLOAD_SERVICE_ZIPCODE, user_zipcode);
            requestObject.accumulate(DataKeyValues.UPLOAD_SERVICEAREA, user_location_address);
            requestObject.accumulate("imageupdatestatus", true);
            requestObject.accumulate("images", null);
           /* if (product_images.size() > 0) {
                JSONArray imageurl_object = new JSONArray();
                List<String> tempproduct_images = new ArrayList<>();
                for (int i = 0; i < product_images.size(); ++i) {
                    String url = product_images.get(i);
                    int index = url.indexOf("/askntake");
                    url = url.substring(index);
                    imageurl_object.put(url);


                }
                requestObject.accumulate(DataKeyValues.PREV_IMAGE_URL, imageurl_object);
            }*/
            for (Map.Entry<Integer, String> entry : imageUrlsMap.entrySet()) {

                //System.out.printf("%s -> %s%n", entry.getKey(), entry.getValue());

                URL url = null;
                try {
                    url = new URL(entry.getValue());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);

                    if (myBitmap != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] byteArrayImage = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                        Log.i("Name ", encodedImage);
                        all_images_main_object.put(entry.getKey(), encodedImage);
                        baos.close();
                    }
                } catch (Exception e) {

                }
            }
            JSONArray inner_images_Array = new JSONArray();
            for (Map.Entry<Integer, String> entry : all_images_main_object.entrySet()) {
                JSONObject inner_obj = new JSONObject();
                inner_obj.accumulate("number", entry.getKey());
                inner_obj.accumulate("imagebasecode", entry.getValue());
                inner_images_Array.put(inner_obj);
            }

            requestObject.accumulate("allimages", inner_images_Array);


            if (commercAndOfficeServicePojo != null) {
                JSONObject commercial_office_object = new JSONObject();


                commercial_office_object.accumulate(DataKeyValues.UPLOAD_AVAILABLE_FROM_DATE, commercAndOfficeServicePojo.getAvailable_from_date());
                commercial_office_object.accumulate(DataKeyValues.UPLOAD_ROOMS, commercAndOfficeServicePojo.getNumber_of_rooms());
                commercial_office_object.accumulate(DataKeyValues.UPLOAD_AREA_SQ_FEET, commercAndOfficeServicePojo.getTotalsq_feet());
                commercial_office_object.accumulate(DataKeyValues.UPLOAD_PRICE, commercAndOfficeServicePojo.getPrice());
                commercial_office_object.accumulate(DataKeyValues.UPLOAD_PRICETYPE, commercAndOfficeServicePojo.getPrice_type());
                commercial_office_object.accumulate("id", commercAndOfficeServicePojo.getId());

                requestObject.accumulate(DataKeyValues.UPLOAD_COMMERCIAL_OFFICE, commercial_office_object);
            } else if (singleFamilyServicePojo != null) {
                JSONObject singlefamilyroom = new JSONObject();

                singlefamilyroom.accumulate(DataKeyValues.UPLOAD_AVAILABLE_FROM_DATE, singleFamilyServicePojo.getAvailable_from_date());
                singlefamilyroom.accumulate(DataKeyValues.UPLOAD_BEDROOMS, singleFamilyServicePojo.getNo_bedrooms());
                singlefamilyroom.accumulate(DataKeyValues.UPLOAD_BATHROOMS, singleFamilyServicePojo.getNo_bathrooms());
                singlefamilyroom.accumulate(DataKeyValues.UPLOAD_AREA_SQ_FEET, singleFamilyServicePojo.getTotalsq_feet());
                singlefamilyroom.accumulate(DataKeyValues.UPLOAD_PRICE, singleFamilyServicePojo.getPrice());
                singlefamilyroom.accumulate(DataKeyValues.UPLOAD_PRICETYPE, singleFamilyServicePojo.getPrice_type());
                singlefamilyroom.accumulate("id", singleFamilyServicePojo.getId());


                requestObject.accumulate(DataKeyValues.UPLOAD_SINGLEFAMILYROOM, singlefamilyroom);
            } else if (roomSharingServicePojo != null) {
                JSONObject roomshare = new JSONObject();

                roomshare.accumulate("availableFrom", roomSharingServicePojo.getAvailable_from_date());
                roomshare.accumulate("accomodates", roomSharingServicePojo.getAccommadates());
                roomshare.accumulate("bedrooms", roomSharingServicePojo.getNo_bedrooms());
                roomshare.accumulate("bathrooms", roomSharingServicePojo.getNo_bathrooms());
                roomshare.accumulate("gender", roomSharingServicePojo.getGender());
                roomshare.accumulate("askingprice", roomSharingServicePojo.getPrice());
                roomshare.accumulate("priceType", roomSharingServicePojo.getPrice_type());
                roomshare.accumulate("id", roomSharingServicePojo.getId());


                requestObject.accumulate(DataKeyValues.UPLOAD_ROOMSHARING, roomshare);
            } else if (oneTimeRideServicePojo != null) {
                JSONObject onetimeride = new JSONObject();

                onetimeride.accumulate("travelDate", oneTimeRideServicePojo.getTraveldate());
                onetimeride.accumulate("destinationFrom", oneTimeRideServicePojo.getDestination_from());
                onetimeride.accumulate("destinationTo", oneTimeRideServicePojo.getDestination_to());
                onetimeride.accumulate("timeOfLeaving", oneTimeRideServicePojo.getTravel_time());
                onetimeride.accumulate("id", oneTimeRideServicePojo.getId());


                requestObject.accumulate(DataKeyValues.UPLOAD_ONETIMERIDE, onetimeride);
            } else if (restOtherServicesPojo != null) {


                JSONArray serivcetimings = new JSONArray();
                for (int i = 0; i < timingsPojos.size(); i++) {
                    JSONObject timingsObject = new JSONObject();

                    timingsObject.accumulate("day", timingsPojos.get(i).getWeek_day());
                    timingsObject.accumulate("daycount", timingsPojos.get(i).getDaycount());
                    timingsObject.accumulate("fromtime", timingsPojos.get(i).getFromTime());
                    timingsObject.accumulate("totime", timingsPojos.get(i).getToTime());
                    timingsObject.accumulate("id", timingsPojos.get(i).getId());

                    serivcetimings.put(i, timingsObject);
                }

                requestObject.accumulate("serivcetimings", serivcetimings);

            } else if (eventsServicePojo != null) {

                JSONObject event = new JSONObject();

                event.accumulate("eventType", eventsServicePojo.getEvent_type());
                event.accumulate("id", eventsServicePojo.getEventId());

                JSONArray eventDetails = new JSONArray();

                for (int i = 0; i < eventTypesPojos.size(); i++) {

                    JSONObject etsObject = new JSONObject();

                    etsObject.accumulate("eventDate", eventTypesPojos.get(i).getEvent_date());
                    etsObject.accumulate("timeFrom", eventTypesPojos.get(i).getFromTime());
                    etsObject.accumulate("timeTo", eventTypesPojos.get(i).getToTime());
                    etsObject.accumulate("id", eventTypesPojos.get(i).getId());
                    eventDetails.put(i, etsObject);
                }
                event.accumulate("eventDetails", eventDetails);

                requestObject.accumulate("event", event);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject;

    }

    public void runServer() {
        SharedPrefPojo pojo = Utility.getData(getApplicationContext());

        user_latitude = pojo.getLattitude();
        user_longitude = pojo.getLangitude();
        user_zipcode = pojo.getZipcode();
        user_location_address = pojo.getArea();


        // Toast.makeText(getApplicationContext(), "lat: " + user_latitude + " lang: " + user_longitude + " Zipcode: " + user_zipcode + " Address: " + user_location_address, Toast.LENGTH_SHORT).show();
        if (user_latitude != null && user_longitude != null) {
            if (internetConnection.isConnectingToInternet(getApplicationContext())) {
                Utility.resetUploadLocation(getApplicationContext());
                new ServiceUpdateAsync().execute();
            } else {
                InternetConnectionAlert();
            }
        } else {
            Intent myIntent = new Intent(getApplicationContext(), LocationSettingsActivity.class);
            myIntent.putExtra("requestFrom", "upload_scr");
            myIntent.putExtra("upload_lattitude", latitude);
            myIntent.putExtra("upload_longitude", langitude);
            startActivity(myIntent);
        }
    }

    public class GetServiceDetailsAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(ServiceUpdationActivity.this, "", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(AppConstants.getServiceDescriptionUrl(user_id, service_id), "GET", null);
            String responceStatus = "";
            try {

                if (jsonStr != null) {
                    JSONObject itemsObj = new JSONObject(jsonStr);
                    if (itemsObj.has("name")) {
                        service_name = itemsObj.getString("name");
                    }
                    if (itemsObj.has("providername")) {
                        providr_name = itemsObj.getString("providername");
                    }
                    if (itemsObj.has("itemId")) {
                        itemId = itemsObj.getString("itemId");
                    }
                    if (itemsObj.has("description")) {
                        description = itemsObj.getString("description");
                    }

                    if (itemsObj.has("category")) {
                        category = itemsObj.getString("category");
                    }


                    /////////////////////////////////

                    try {


                        if (itemsObj.has("roomShare")) {


                            roomSharingServicePojo = new RoomSharingServicePojo();

                            JSONObject roomshareObj = itemsObj.getJSONObject("roomShare");

                            if (roomshareObj.has("availableFrom")) {
                                availableFrom = roomshareObj.getString("availableFrom");
                            }
                            if (roomshareObj.has("accomodates")) {
                                accomodates = roomshareObj.getString("accomodates");
                            }
                            if (roomshareObj.has("bedrooms")) {
                                bedrooms = roomshareObj.getString("bedrooms");
                            }
                            if (roomshareObj.has("bathrooms")) {
                                bathrooms = roomshareObj.getString("bathrooms");
                            }

                            if (roomshareObj.has("gender")) {
                                gender = roomshareObj.getString("gender");
                            }
                            if (roomshareObj.has("askingprice")) {
                                askingprice = roomshareObj.getString("askingprice");
                            }
                            if (roomshareObj.has("priceType")) {
                                priceType = roomshareObj.getString("priceType");
                            }

                            if (roomshareObj.has("id")) {
                                id = roomshareObj.getString("id");

                            }


                            roomSharingServicePojo.setAvailable_from_date(availableFrom);
                            roomSharingServicePojo.setAccommadates(accomodates);
                            roomSharingServicePojo.setNo_bedrooms(bedrooms);
                            roomSharingServicePojo.setNo_bathrooms(bathrooms);
                            roomSharingServicePojo.setGender(gender);
                            roomSharingServicePojo.setPrice(askingprice);
                            roomSharingServicePojo.setPrice_type(priceType);
                            roomSharingServicePojo.setId(id);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //////////////////////////////////


                    try {


                        if (itemsObj.has("onetimeride")) {


                            oneTimeRideServicePojo = new OneTimeRideServicePojo();

                            JSONObject oneTimeObj = itemsObj.getJSONObject("onetimeride");

                            if (oneTimeObj.has("travelDate")) {
                                travelDate = oneTimeObj.getString("travelDate");
                            }

                            if (oneTimeObj.has("timeOfLeaving")) {
                                timeOfLeaving = oneTimeObj.getString("timeOfLeaving");
                            }

                            if (oneTimeObj.has("destinationFrom")) {
                                destinationFrom = oneTimeObj.getString("destinationFrom");
                            }
                            if (oneTimeObj.has("destinationTo")) {
                                destinationTo = oneTimeObj.getString("destinationTo");
                            }

                            if (oneTimeObj.has("id")) {
                                id = oneTimeObj.getString("id");

                            }


                            oneTimeRideServicePojo.setTraveldate(travelDate);
                            oneTimeRideServicePojo.setTravel_time(timeOfLeaving);
                            oneTimeRideServicePojo.setDestination_from(destinationFrom);
                            oneTimeRideServicePojo.setDestination_to(destinationTo);
                            oneTimeRideServicePojo.setId(id);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //////////////////////////////////


                    try {

                        if (itemsObj.has("servicetimings")) {
                            timingsPojos = new ArrayList<TimingsPojo>();
                            timingsPojo = new TimingsPojo();

                            JSONObject innerTimingsJsonObject = itemsObj.getJSONObject("servicetimings");

                            if (innerTimingsJsonObject.has("day")) {
                                day = innerTimingsJsonObject.getString("day");
                            }

                            if (innerTimingsJsonObject.has("daycount")) {
                                daycount = innerTimingsJsonObject.getString("daycount");
                            }

                            if (innerTimingsJsonObject.has("fromtime")) {
                                fromtime = innerTimingsJsonObject.getString("fromtime");
                            }
                            if (innerTimingsJsonObject.has("fromtimeType")) {
                                fromtimeType = innerTimingsJsonObject.getString("fromtimeType");
                            }

                            if (innerTimingsJsonObject.has("totime")) {
                                totime = innerTimingsJsonObject.getString("totime");
                            }
                            if (innerTimingsJsonObject.has("totimeType")) {
                                totimeType = innerTimingsJsonObject.getString("totimeType");
                            }

                            if (innerTimingsJsonObject.has("id")) {
                                id = innerTimingsJsonObject.getString("id");

                            }


                            timingsPojo.setWeek_day(day);
                            timingsPojo.setDaycount(daycount);
                            timingsPojo.setFromTime(fromtime + " " + fromtimeType);
                            timingsPojo.setToTime(totime + " " + totimeType);
                            timingsPojo.setId(id);
                            timingsPojos.add(timingsPojo);

                            /*TimingsPojo timingsPojo = new TimingsPojo();

                            timingsPojo.setDaycount(innerTimingsJsonObject.getString("daycount"));
                            timingsPojo.setWeek_day(innerTimingsJsonObject.getString("day"));
                            timingsPojo.setFromTime(innerTimingsJsonObject.getString("fromtime"));
                            timingsPojo.setToTime(innerTimingsJsonObject.getString("totime"));
                            itemServiceTimingsPojoList.add(timingsPojo);*/
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {


                            if (itemsObj.has("servicetimings")) {
                                timingsPojos = new ArrayList<TimingsPojo>();
                                JSONArray timingArray = itemsObj.getJSONArray("servicetimings");
                                for (int i = 0; i < timingArray.length(); i++) {
                                    timingsPojo = new TimingsPojo();
                                    JSONObject innerJsonObject = timingArray.getJSONObject(i);

                                    if (innerJsonObject.has("day")) {
                                        day = innerJsonObject.getString("day");
                                    }

                                    if (innerJsonObject.has("daycount")) {
                                        daycount = innerJsonObject.getString("daycount");
                                    }

                                    if (innerJsonObject.has("fromtime")) {
                                        fromtime = innerJsonObject.getString("fromtime");
                                    }
                                    if (innerJsonObject.has("fromtimeType")) {
                                        fromtimeType = innerJsonObject.getString("fromtimeType");
                                    }

                                    if (innerJsonObject.has("totime")) {
                                        totime = innerJsonObject.getString("totime");
                                    }
                                    if (innerJsonObject.has("totimeType")) {
                                        totimeType = innerJsonObject.getString("totimeType");
                                    }

                                    if (innerJsonObject.has("id")) {
                                        id = innerJsonObject.getString("id");

                                    }

                                    timingsPojo.setWeek_day(day);
                                    timingsPojo.setDaycount(daycount);
                                    timingsPojo.setFromTime(fromtime + " " + fromtimeType);
                                    timingsPojo.setToTime(totime + " " + totimeType);
                                    timingsPojo.setId(id);
                                    timingsPojos.add(timingsPojo);

                                }
                            }

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }


                    //////////////////////////
                    ////Events/////////////

                    try {


                        if (itemsObj.has("event")) {

                            eventsServicePojo = new EventsServicePojo();
                            eventTypesPojo = new ArrayList<EventTypesPojo>();
                            EventTypesPojo tempevent = new EventTypesPojo();

                            JSONObject eventObj = itemsObj.getJSONObject("event");

                            if (eventObj.has("eventType")) {
                                eventType = eventObj.getString("eventType");
                            }
                            if (eventObj.has("id")) {
                                eventID = eventObj.getString("id");
                            }

                            if (eventType.equalsIgnoreCase("Single Day")) {
                                try {
                                    if (eventObj.has("eventDetails")) {
                                        JSONObject singleEventJsonObject = eventObj.getJSONObject("eventDetails");

                                        if (singleEventJsonObject.has("eventDate")) {
                                            eventDate = singleEventJsonObject.getString("eventDate");
                                        }

                                        if (singleEventJsonObject.has("timeFrom")) {
                                            timeFrom = singleEventJsonObject.getString("timeFrom") + " " + singleEventJsonObject.getString("timeFromType");

                                        }
                                        if (singleEventJsonObject.has("timeTo")) {
                                            timeTo = singleEventJsonObject.getString("timeTo") + " " + singleEventJsonObject.getString("timeToType");

                                        }


                                        if (singleEventJsonObject.has("id")) {
                                            id = singleEventJsonObject.getString("id");

                                        }


                                        eventsServicePojo.setEvent_type(eventType);
                                        tempevent.setEvent_date(eventDate);
                                        tempevent.setFromTime(timeFrom);
                                        tempevent.setToTime(timeTo);
                                        tempevent.setId(id);
                                        eventTypesPojo.add(tempevent);
                                        eventsServicePojo.setEvents(eventTypesPojo);
                                        eventsServicePojo.setEventId(eventID);
                                        //eventsServicePojo.setId(id);


                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (eventType.equalsIgnoreCase("Multiple Days")) {
                                try {


                                    if (eventObj.has("eventDetails")) {
                                        JSONArray multipleEventArray = eventObj.getJSONArray("eventDetails");
                                        String tempmyleventvalues = "";
                                        for (int i = 0; i < multipleEventArray.length(); i++) {


                                            tempevent = new EventTypesPojo();
                                            JSONObject eventJsonInnerObject = multipleEventArray.getJSONObject(i);

                                            if (eventJsonInnerObject.has("eventDate")) {
                                                eventDate = eventJsonInnerObject.getString("eventDate");
                                            }

                                            if (eventJsonInnerObject.has("timeFrom")) {
                                                timeFrom = eventJsonInnerObject.getString("timeFrom") + " " + eventJsonInnerObject.getString("timeFromType");
                                            }
                                            if (eventJsonInnerObject.has("timeTo")) {
                                                timeTo = eventJsonInnerObject.getString("timeTo") + " " + eventJsonInnerObject.getString("timeToType");
                                            }
                                            if (eventJsonInnerObject.has("id")) {
                                                id = eventJsonInnerObject.getString("id");
                                            }

                                            tempevent.setEvent_date(eventDate);
                                            tempevent.setFromTime(timeFrom);
                                            tempevent.setToTime(timeTo);
                                            tempevent.setId(id);
                                            eventTypesPojo.add(tempevent);


                                        }
                                        eventsServicePojo.setEvent_type(eventType);
                                        eventsServicePojo.setEvent_type(eventID);
                                        eventsServicePojo.setEvents(eventTypesPojo);
                                    }


                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    /////////////////////////////////////

                    try {


                        if (itemsObj.has("commercialAndOfficeSpace")) {


                            commercAndOfficeServicePojo = new CommercAndOfficeServicePojo();

                            JSONObject comandofficeObj = itemsObj.getJSONObject("commercialAndOfficeSpace");

                            if (comandofficeObj.has("avalableFrom")) {
                                avalableFrom = comandofficeObj.getString("avalableFrom");
                            }

                            if (comandofficeObj.has("rooms")) {
                                rooms = comandofficeObj.getString("rooms");
                            }

                            if (comandofficeObj.has("areainsqft")) {
                                areainsqft = comandofficeObj.getString("areainsqft");
                            }
                            if (comandofficeObj.has("price")) {
                                price = comandofficeObj.getString("price");
                            }
                            if (comandofficeObj.has("priceType")) {
                                priceType = comandofficeObj.getString("priceType");
                            }

                            if (comandofficeObj.has("id")) {
                                id = comandofficeObj.getString("id");

                            }

                            commercAndOfficeServicePojo.setAvailable_from_date(avalableFrom);
                            commercAndOfficeServicePojo.setNumber_of_rooms(rooms);
                            commercAndOfficeServicePojo.setTotalsq_feet(areainsqft);
                            commercAndOfficeServicePojo.setPrice(price);
                            commercAndOfficeServicePojo.setPrice_type(priceType);
                            commercAndOfficeServicePojo.setId(id);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    /////////////////////////////////////////


                    try {


                        if (itemsObj.has("singleFamilyHome")) {


                            singleFamilyServicePojo = new SingleFamilyServicePojo();

                            JSONObject singlefamObj = itemsObj.getJSONObject("singleFamilyHome");

                            if (singlefamObj.has("avalableFrom")) {
                                avalableFrom = singlefamObj.getString("avalableFrom");
                            }
                            if (singlefamObj.has("bedrooms")) {
                                bedrooms = singlefamObj.getString("bedrooms");
                            }
                            if (singlefamObj.has("bathrooms")) {
                                bathrooms = singlefamObj.getString("bathrooms");
                            }

                            if (singlefamObj.has("areainsqft")) {
                                areainsqft = singlefamObj.getString("areainsqft");
                            }
                            if (singlefamObj.has("price")) {
                                price = singlefamObj.getString("price");
                            }
                            if (singlefamObj.has("priceType")) {
                                priceType = singlefamObj.getString("priceType");
                            }

                            if (singlefamObj.has("id")) {
                                id = singlefamObj.getString("id");

                            }


                            singleFamilyServicePojo.setAvailable_from_date(avalableFrom);
                            singleFamilyServicePojo.setNo_bedrooms(bedrooms);
                            singleFamilyServicePojo.setNo_bathrooms(bathrooms);
                            singleFamilyServicePojo.setTotalsq_feet(areainsqft);
                            singleFamilyServicePojo.setPrice(price);
                            singleFamilyServicePojo.setPrice_type(priceType);
                            singleFamilyServicePojo.setId(id);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ////////////////////////////////////////////////////

                    if (itemsObj.has("owner")) {
                        JSONObject ownerObj = itemsObj.getJSONObject("owner");

                        if (ownerObj.has("ownerId")) {
                            ownerId = ownerObj.getString("ownerId");
                        }
                        if (ownerObj.has("email")) {
                            email = ownerObj.getString("email");
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
                        if (ownerObj.has("zipcode")) {
                            zipcode = ownerObj.getString("zipcode");
                        }
                        if (ownerObj.has("area")) {
                            area = ownerObj.getString("area");
                        }
                        if (ownerObj.has("latitude")) {
                            latitude = ownerObj.getString("latitude");
                        }
                        if (ownerObj.has("langitude")) {
                            langitude = ownerObj.getString("langitude");
                        }
                        if (ownerObj.has("product_city")) {
                            product_city = ownerObj.getString("product_city");
                        }
                        if (ownerObj.has("product_state")) {
                            product_state = ownerObj.getString("product_state");
                        }


                    }


                    if (itemsObj.has("visitcount")) {
                        visitcount = itemsObj.getString("visitcount");
                    }
                    if (itemsObj.has("favoritesCount")) {
                        favoritesCount = itemsObj.getString("favoritesCount");
                    }


                    if (itemsObj.has("subcategory")) {
                        subcategory = itemsObj.getString("subcategory");
                    }

                    if (itemsObj.has("rating")) {
                        rating = itemsObj.getString("rating");
                    }

                    if (itemsObj.has("contact")) {
                        contact = itemsObj.getString("contact");

                    }

                    if (itemsObj.has("website")) {
                        website = itemsObj.getString("website");
                    }

                    try {
                        JSONArray product_imagesArray = itemsObj.getJSONArray("images");
                        for (int i = 0; i < product_imagesArray.length(); i++) {
                            product_images.add(i, product_imagesArray.getString(i));
                            //  imagesURLArray.add(0,product_images.get(0) );
                        }

                    } catch (JSONException jj) {
                        jj.printStackTrace();
                        product_images.add(itemsObj.getString("images"));
                    }
                    responceStatus = "success";


                } else {
                    responceStatus = "fail";
                }

            } catch (Exception e) {

                responceStatus = "fail";
            }

            return responceStatus;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();


            if (result.equalsIgnoreCase("success")) {


                edit_service_name.setText(service_name);
                edit_provider_name.setText(providr_name);
                edit_about_this_service.setText(description);
                edit_email.setText(email);
                edit_phone_number.setText(contact);
                edit_web_link.setText(website);
                edit_address.setText(area);

                Products_list.setText(category);

                if (subcategory == "" || subcategory.length() == 0) {
                    Products_sub_list.setVisibility(View.GONE);

                } else {
                    Products_sub_list.setText(subcategory);
                }

                if (product_images != null) {
                    switch (product_images.size()) {
                        case 1:

                            Picasso.with(getApplicationContext()).load(product_images.get(0)).placeholder(R.drawable.progress_animation).into((ImageView) service_images_linear_layout.getChildAt(0));
                            //img_status_1=true;
                            imagesArray.add(0, true);

                            imageUrlsMap.put(1, product_images.get(0));
                            break;
                        case 2:
                            Picasso.with(getApplicationContext()).load(product_images.get(0)).placeholder(R.drawable.progress_animation).into((ImageView) service_images_linear_layout.getChildAt(0));
                            Picasso.with(getApplicationContext()).load(product_images.get(1)).placeholder(R.drawable.progress_animation).into((ImageView) service_images_linear_layout.getChildAt(1));
                            imagesArray.add(0, true);
                            imagesArray.add(1, true);
                            imageUrlsMap.put(1, product_images.get(0));
                            imageUrlsMap.put(2, product_images.get(1));
//                            img_status_1=true;
//                            img_status_2=true;
                            break;
                        case 3:
                            Picasso.with(getApplicationContext()).load(product_images.get(0)).placeholder(R.drawable.progress_animation).into((ImageView) service_images_linear_layout.getChildAt(0));
                            Picasso.with(getApplicationContext()).load(product_images.get(1)).placeholder(R.drawable.progress_animation).into((ImageView) service_images_linear_layout.getChildAt(1));
                            Picasso.with(getApplicationContext()).load(product_images.get(2)).placeholder(R.drawable.progress_animation).into((ImageView) service_images_linear_layout.getChildAt(2));
                            imagesArray.add(0, true);
                            imagesArray.add(1, true);
                            imagesArray.add(2, true);

                            imageUrlsMap.put(1, product_images.get(0));
                            imageUrlsMap.put(2, product_images.get(1));
                            imageUrlsMap.put(3, product_images.get(2));
                            break;
                        case 4:
                            Picasso.with(getApplicationContext()).load(product_images.get(0)).placeholder(R.drawable.progress_animation).into((ImageView) service_images_linear_layout.getChildAt(0));
                            Picasso.with(getApplicationContext()).load(product_images.get(1)).placeholder(R.drawable.progress_animation).into((ImageView) service_images_linear_layout.getChildAt(1));
                            Picasso.with(getApplicationContext()).load(product_images.get(2)).placeholder(R.drawable.progress_animation).into((ImageView) service_images_linear_layout.getChildAt(2));
                            Picasso.with(getApplicationContext()).load(product_images.get(3)).placeholder(R.drawable.progress_animation).into((ImageView) service_images_linear_layout.getChildAt(3));
                            imagesArray.add(0, true);
                            imagesArray.add(1, true);
                            imagesArray.add(2, true);
                            imagesArray.add(3, true);
                            imageUrlsMap.put(1, product_images.get(0));
                            imageUrlsMap.put(2, product_images.get(1));
                            imageUrlsMap.put(3, product_images.get(2));
                            imageUrlsMap.put(4, product_images.get(3));
                            break;
                        case 5:
                            Picasso.with(getApplicationContext()).load(product_images.get(0)).placeholder(R.drawable.progress_animation).into((ImageView) service_images_linear_layout.getChildAt(0));
                            Picasso.with(getApplicationContext()).load(product_images.get(1)).placeholder(R.drawable.progress_animation).into((ImageView) service_images_linear_layout.getChildAt(1));
                            Picasso.with(getApplicationContext()).load(product_images.get(2)).placeholder(R.drawable.progress_animation).into((ImageView) service_images_linear_layout.getChildAt(2));
                            Picasso.with(getApplicationContext()).load(product_images.get(3)).placeholder(R.drawable.progress_animation).into((ImageView) service_images_linear_layout.getChildAt(3));
                            Picasso.with(getApplicationContext()).load(product_images.get(4)).placeholder(R.drawable.progress_animation).into((ImageView) service_images_linear_layout.getChildAt(4));

                            imagesArray.add(0, true);
                            imagesArray.add(1, true);
                            imagesArray.add(2, true);
                            imagesArray.add(3, true);
                            imagesArray.add(4, true);
                            imageUrlsMap.put(1, product_images.get(0));
                            imageUrlsMap.put(2, product_images.get(1));
                            imageUrlsMap.put(3, product_images.get(2));
                            imageUrlsMap.put(4, product_images.get(3));
                            imageUrlsMap.put(5, product_images.get(4));
                        default:
                            break;
                    }
                }


                if (roomSharingServicePojo != null) {
                    layout_single_family_home_block.setVisibility(View.GONE);
                    layout_rest_services_block.setVisibility(View.GONE);
                    layout_one_time_ride_block.setVisibility(View.GONE);
                    layout_room_sharing_block.setVisibility(View.VISIBLE);
                    layout_commercial_and_office_block.setVisibility(View.GONE);
                    layout_events_service_block.setVisibility(View.GONE);


                    edit_room_sharing_available_from.setText(availableFrom);

                    edit_room_sharing_accommodates.setText(accomodates);

                    edit_room_sharing_bedrooms.setText(bedrooms);


                    edit_room_sharing_bath_rooms.setText(bathrooms);

                    String[] gendertypes = getResources().getStringArray(R.array.gender);
                    for (int i = 0; i < gendertypes.length; ++i) {

                        if (gendertypes[i].equalsIgnoreCase(gender)) {
                            room_sharing_gender_spin.setSelection(i);
                            break;
                        }
                    }

                    edit_room_sharing_asking_price.setText(askingprice);


                    String[] dollernames = getResources().getStringArray(R.array.doller_names);
                    for (int i = 0; i < dollernames.length; ++i) {

                        if (dollernames[i].equalsIgnoreCase(priceType)) {
                            room_sharing_price_type_spinner.setSelection(i);
                            break;
                        }
                    }
                }
                if (singleFamilyServicePojo != null) {

                    layout_single_family_home_block.setVisibility(View.VISIBLE);
                    layout_rest_services_block.setVisibility(View.GONE);
                    layout_one_time_ride_block.setVisibility(View.GONE);
                    layout_room_sharing_block.setVisibility(View.GONE);
                    layout_commercial_and_office_block.setVisibility(View.GONE);
                    layout_events_service_block.setVisibility(View.GONE);


                    EditText bedroomsobj = (EditText) findViewById(R.id.edit_single_family_bedrooms);
                    bedroomsobj.setText(bedrooms);

                    EditText Availfromobj = (EditText) findViewById(R.id.edit_single_family_available_from);
                    Availfromobj.setText(avalableFrom);

                    EditText bathroomsobj = (EditText) findViewById(R.id.edit_single_family_bath_rooms);
                    bathroomsobj.setText(bathrooms);

                    EditText totsqftobj = (EditText) findViewById(R.id.edit_single_family_total_sq_feet);
                    totsqftobj.setText(areainsqft);

                    EditText askpriceobj = (EditText) findViewById(R.id.edit_single_family_asking_price);
                    askpriceobj.setText(price);
                    Spinner askpricetypeobj = (Spinner) findViewById(R.id.single_family_spinner_price_type);

                    String[] dollernames = getResources().getStringArray(R.array.doller_names);
                    for (int i = 0; i < dollernames.length; ++i) {

                        if (dollernames[i].equalsIgnoreCase(priceType)) {
                            askpricetypeobj.setSelection(i);
                            break;
                        }
                    }

                }


                if (oneTimeRideServicePojo != null) {

                    layout_single_family_home_block.setVisibility(View.GONE);
                    layout_rest_services_block.setVisibility(View.GONE);
                    layout_one_time_ride_block.setVisibility(View.VISIBLE);
                    layout_room_sharing_block.setVisibility(View.GONE);
                    layout_commercial_and_office_block.setVisibility(View.GONE);
                    layout_events_service_block.setVisibility(View.GONE);


                    int index;

                    edit_one_time_travel_date.setText(travelDate);

                    edit_one_time_destination_from.setText(destinationFrom);

                    edit_one_time_destination_to.setText(destinationTo);


                    String temptimeType = timeOfLeaving.substring(timeOfLeaving.length() - 2);
                    String temptime = timeOfLeaving.substring(0, timeOfLeaving.length() - 2);

                    if (temptimeType.equalsIgnoreCase("am"))
                        index = 0;
                    else
                        index = 1;
                    edit_one_time_travel_time.setText(temptime);
                    one_time_spinner_time.setSelection(index);


                }

                if (commercAndOfficeServicePojo != null) {

                    layout_single_family_home_block.setVisibility(View.GONE);
                    layout_rest_services_block.setVisibility(View.GONE);
                    layout_one_time_ride_block.setVisibility(View.GONE);
                    layout_room_sharing_block.setVisibility(View.GONE);
                    layout_commercial_and_office_block.setVisibility(View.VISIBLE);
                    layout_events_service_block.setVisibility(View.GONE);


                    edit_commercial_available_from.setText(avalableFrom);

                    edit_commercial_no_of_rooms.setText(rooms);

                    edit_commercial_total_sq_feet.setText(areainsqft);

                    edit_commercial_asking_price.setText(price);


                    String[] dollernames = getResources().getStringArray(R.array.doller_names);
                    for (int i = 0; i < dollernames.length; ++i) {

                        if (dollernames[i].equalsIgnoreCase(priceType)) {
                            commercial_spinner_price_type.setSelection(i);
                            break;
                        }
                    }

                }

                if (eventsServicePojo != null) {

                    layout_single_family_home_block.setVisibility(View.GONE);
                    layout_rest_services_block.setVisibility(View.GONE);
                    layout_one_time_ride_block.setVisibility(View.GONE);
                    layout_room_sharing_block.setVisibility(View.GONE);
                    layout_commercial_and_office_block.setVisibility(View.GONE);
                    layout_events_service_block.setVisibility(View.VISIBLE);

                    eventVisibleCount = eventTypesPojo.size();

                    String eventtype = eventsServicePojo.getEvent_type();
                    eventTypesPojo = eventsServicePojo.getEvents();

                    int index;

                    if (eventtype.equalsIgnoreCase("Single Day"))
                        index = 0;
                    else
                        index = 1;

                    event_type_spinner.setSelection(index);


                    for (int i = 0; i < eventVisibleCount; ++i) {


                        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View rootview = layoutInflater.inflate(R.layout.field_events, null);
                        delete_event_button = (Button) rootview.findViewById(R.id.delete_event_button);
                        delete_event_button.setOnClickListener(ServiceUpdationActivity.this);
                        rootview.setId(event_parent_lay.getChildCount());
                        event_parent_lay.addView(rootview, event_parent_lay.getChildCount());
                        rootview.setTag("visble," + eventTypesPojo.get(i).getId());

                        multiple_selection = (LinearLayout) rootview.findViewById(R.id.multiple_selection);

                        // event_type_spinner = (Spinner) rootview.findViewById(R.id.event_type_spinner);

                        input_la_date = (TextInputLayout) rootview.findViewById(R.id.input_la_date);

                        edit_date = (EditText) rootview.findViewById(R.id.edit_date);

                        input_timings_time = (TextInputLayout) rootview.findViewById(R.id.input_timings_time);

                        edit_timing_time = (EditText) rootview.findViewById(R.id.edit_timing_time);

                        spinner_Timings_mul = (Spinner) rootview.findViewById(R.id.spinner_Timings_mul);

                        input_timings_time_mul = (TextInputLayout) rootview.findViewById(R.id.input_timings_time_mul);

                        edit_timings_time_mul = (EditText) rootview.findViewById(R.id.edit_timings_time_mul);

                        spinner_Timings_multi = (Spinner) rootview.findViewById(R.id.spinner_Timings_multi);


                        edit_date.setText(eventTypesPojo.get(i).getEvent_date());

                        //from trime
                        String temp = eventTypesPojo.get(i).getFromTime();
                        String temptimeType = temp.substring(temp.length() - 2);
                        String temptime = temp.substring(0, temp.length() - 2);

                        if (temptimeType.equalsIgnoreCase("am"))
                            index = 0;
                        else
                            index = 1;
                        edit_timing_time.setText(temptime);
                        spinner_Timings_mul.setSelection(index);

                        /// to time
                        temp = eventTypesPojo.get(i).getToTime();
                        temptimeType = temp.substring(temp.length() - 2);
                        temptime = temp.substring(0, temp.length() - 2);

                        if (temptimeType.equalsIgnoreCase("am"))
                            index = 1;
                        else
                            index = 0;
                        spinner_Timings_multi.setSelection(index);
                        edit_timings_time_mul.setText(temptime);


                    }
                }

                if (timingsPojos != null) {

                    layout_single_family_home_block.setVisibility(View.GONE);
                    layout_rest_services_block.setVisibility(View.VISIBLE);
                    layout_one_time_ride_block.setVisibility(View.GONE);
                    layout_room_sharing_block.setVisibility(View.GONE);
                    layout_commercial_and_office_block.setVisibility(View.GONE);
                    layout_events_service_block.setVisibility(View.GONE);

                    visblecount = timingsPojos.size();
                    for (int i = 0; i < visblecount; ++i) {

                        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View rootview = layoutInflater.inflate(R.layout.fields, null);
                        delete_button = (Button) rootview.findViewById(R.id.delete_button);
                        delete_button.setOnClickListener(ServiceUpdationActivity.this);
                        rootview.setId(par_lay.getChildCount());
                        par_lay.addView(rootview, par_lay.getChildCount());
                        rootview.setTag("visble");
                        rest_serv_la_timings_time = (TextInputLayout) rootview.findViewById(R.id.rest_serv_la_timings_time);
                        rest_serv_la_timings_time_to = (TextInputLayout) rootview.findViewById(R.id.rest_serv_la_timings_time_to);
                        rest_serv_spinner_day = (Spinner) rootview.findViewById(R.id.rest_serv_spinner_day);
                        rest_serv_spinner_Timings = (Spinner) rootview.findViewById(R.id.rest_serv_spinner_Timings);
                        rest_serv_spinner_Timings_to = (Spinner) rootview.findViewById(R.id.rest_serv_spinner_Timings_to);
                        edit_rest_serv_timings_time = (EditText) rootview.findViewById(R.id.edit_rest_serv_timings_time);
                        edit_rest_serv_timings_time_to = (EditText) rootview.findViewById(R.id.edit_rest_serv_timings_time_to);
                        rest_serv_timings_layout = (LinearLayout) rootview.findViewById(R.id.rest_serv_timings_layout);
                        rest_serv_linear_layout_timings = (LinearLayout) rootview.findViewById(R.id.rest_serv_linear_layout_timings);
                        rest_serv_linear_layout_timings_to = (LinearLayout) rootview.findViewById(R.id.rest_serv_linear_layout_timings_to);


                        int index = getDayIndex(timingsPojos.get(i).getWeek_day());
                        rest_serv_spinner_day.setSelection(index);

                        //from trime
                        String temp = timingsPojos.get(i).getFromTime();
                        String temptimeType = temp.substring(temp.length() - 2);
                        String temptime = temp.substring(0, temp.length() - 2);

                        if (temptimeType.equalsIgnoreCase("am"))
                            index = 0;
                        else
                            index = 1;
                        edit_rest_serv_timings_time.setText(temptime);
                        rest_serv_spinner_Timings.setSelection(index);

                        /// to time
                        temp = timingsPojos.get(i).getToTime();
                        temptimeType = temp.substring(temp.length() - 2);
                        temptime = temp.substring(0, temp.length() - 2);

                        if (temptimeType.equalsIgnoreCase("am"))
                            index = 1;
                        else
                            index = 0;
                        rest_serv_spinner_Timings_to.setSelection(index);
                        edit_rest_serv_timings_time_to.setText(temptime);

                    }

                }

            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            Intent intent_description = new Intent(ServiceUpdationActivity.this, ServiceDescriptionActivity_Services.class);
            intent_description.putExtra("service_id", service_id);
            intent_description.putExtra("user_id", user_id);
            intent_description.putExtra("myownproduct", its_my_own_product);
            startActivity(intent_description);
            finish();


        }


        return super.onOptionsItemSelected(item);
    }

    public int getDayIndex(String week) {

        if (week.equalsIgnoreCase("monday"))
            return 0;
        if (week.equalsIgnoreCase("tuesday"))
            return 1;
        if (week.equalsIgnoreCase("wednesday"))
            return 2;
        if (week.equalsIgnoreCase("thursday"))
            return 3;
        if (week.equalsIgnoreCase("friday"))
            return 4;
        if (week.equalsIgnoreCase("saturday"))
            return 5;
        if (week.equalsIgnoreCase("sunday"))
            return 6;

        return 0;
    }

    @Override
    public void onBackPressed() {

        Intent intent_description = new Intent(ServiceUpdationActivity.this, ServiceDescriptionActivity_Services.class);
        intent_description.putExtra("service_id", service_id);
        intent_description.putExtra("user_id", user_id);
        intent_description.putExtra("myownproduct", its_my_own_product);
        startActivity(intent_description);
        finish();
    }
}

