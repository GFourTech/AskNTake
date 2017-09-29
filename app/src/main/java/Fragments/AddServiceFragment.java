package Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.askntake.admin.askntake.LocationSettingsActivity;
import com.askntake.admin.askntake.NavigationDrawerActivity;
import com.askntake.admin.askntake.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import AppUtils.AppConstants;
import AppUtils.Commons;
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
 * Created by admin on 3/27/2017.
 */

public class AddServiceFragment extends Fragment implements View.OnClickListener {


    public LinearLayout service_images_linear_layout, Linear_layout_gender, linear_lay_destination, linear_lay_time_travelling, map_linear_layout;
    HorizontalScrollView h_s_service_images;

    ConnectionDetector internetConnection;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    Dialog Internetdialog;

    List<String> array_of_images = new ArrayList<String>();
    HashMap<Integer, String> allimagesMap = new HashMap<Integer, String>();

    ImageView service_image_view;

    ArrayList<EventTypesPojo> eventTypesPojos = null;
    ArrayList<TimingsPojo> timingsPojos = null;


    TextInputLayout single_family_la_available_from, single_family_la_bed_rooms, single_family_la_bath_rooms, single_family_la_total_sq_feet, single_family_la_asking_price;
    static EditText edit_single_family_available_from, edit_single_family_bedrooms, edit_single_family_bath_rooms, edit_single_family_total_sq_feet, edit_single_family_asking_price;
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
    public static EditText edit_travel_date;
    public EditText edit_phone_number;
    public static EditText edit_travel_time;
    public EditText edit_email;
    public EditText edit_web_link;
    public EditText edit_address;
    public TextInputLayout input_la_phone_number, input_la_email, input_la_web_link, input_la_address,
            input_la_date,
            input_timings_time;
    TextView text_gender, list_now_button;
    Fragment google_map;

    FrameLayout frame_layout;

    public Spinner spinner_Timings_mul,
            spinner_Timings_multi;

    public TextInputLayout input_la_provider_name, input_la_service_name, input_la_about_this_sevice,
            input_timings_time_mul;
    public EditText edit_date,
            edit_timing_time,
            edit_timings_time_mul;


    ArrayList<Boolean> imagesArray = new ArrayList<>();
    ArrayList<Bitmap> bitmapArray = new ArrayList<>();


    Bitmap selected_image_bitmap = null;

    Spinner Products_list, Products_sub_list;


    public View layout_rest_services_block, layout_one_time_ride_block, layout_room_sharing_block, layout_single_family_home_block, layout_commercial_and_office_block, layout_events_service_block;

    RelativeLayout type_of_event_lay, main_relative_layout;

    LinearLayout multiple_selection;
    CommercAndOfficeServicePojo commercAndOfficeServicePojo = null;
    EventsServicePojo eventsServicePojo = null;
    OneTimeRideServicePojo oneTimeRideServicePojo = null;
    RestOtherServicesPojo restOtherServicesPojo = null;
    RoomSharingServicePojo roomSharingServicePojo = null;
    static SingleFamilyServicePojo singleFamilyServicePojo = null;

    String username, userId;


    String user_latitude = "";
    String user_longitude = "";
    String user_zipcode = "";
    String user_location_address = "";
    String upload_price_type = "";
    String product_city = "";
    String product_state = "";

    Map<String, String> subList = new HashMap<>();

    public LinearLayout rest_serv_timings_layout, rest_serv_linear_layout_timings, rest_serv_linear_layout_timings_to;

    public TextView txt_timings;

    Spinner rest_serv_spinner_day,
            rest_serv_spinner_Timings,
            rest_serv_spinner_Timings_to, event_type_spinner;


    TextInputLayout rest_serv_la_timings_time,
            rest_serv_la_timings_time_to;

    EditText edit_rest_serv_timings_time,
            edit_rest_serv_timings_time_to;

    LinearLayout par_lay, event_parent_lay, type_of_event_lin, event_linear_lay;

    Button add_field_button, delete_button, add_event_button, delete_event_button;
    int visblecount = 1;
    int eventVisibleCount = 1;

    @Nullable
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.add_service_page, null);

        internetConnection = new ConnectionDetector(getActivity());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        SharedPreferences login_data_pref = getActivity().getSharedPreferences(DataKeyValues.USER_DATA_PREF, Context.MODE_PRIVATE);
        username = login_data_pref.getString(DataKeyValues.USER_FIRSTNAME, null);
        userId = login_data_pref.getString(DataKeyValues.USER_USERID, null);

        // fb_editor.putString(, jsObj.getString(DataKeyValues.USER_USERID));

        frame_layout = (FrameLayout) root_view.findViewById(R.id.frame_layout);

        h_s_service_images = (HorizontalScrollView) root_view.findViewById(R.id.h_s_service_images);

        // price_dynamic_layout = (LinearLayout) root_view.findViewById(R.id.price_dynamic_layout);
        map_linear_layout = (LinearLayout) root_view.findViewById(R.id.map_linear_layout);
        service_images_linear_layout = (LinearLayout) root_view.findViewById(R.id.service_images_linear_layout);
        linear_lay_destination = (LinearLayout) root_view.findViewById(R.id.linear_lay_destination);
        Linear_layout_gender = (LinearLayout) root_view.findViewById(R.id.Linear_layout_gender);
        linear_lay_time_travelling = (LinearLayout) root_view.findViewById(R.id.linear_lay_time_travelling);


        edit_service_name = (EditText) root_view.findViewById(R.id.edit_service_name);
        edit_provider_name = (EditText) root_view.findViewById(R.id.edit_provider_name);
        edit_email = (EditText) root_view.findViewById(R.id.edit_email);
        edit_web_link = (EditText) root_view.findViewById(R.id.edit_web_link);
        edit_phone_number = (EditText) root_view.findViewById(R.id.edit_phone_number);
        edit_address = (EditText) root_view.findViewById(R.id.edit_address);
        edit_about_this_service = (EditText) root_view.findViewById(R.id.edit_about_this_service);


        input_la_address = (TextInputLayout) root_view.findViewById(R.id.input_la_address);
        input_la_web_link = (TextInputLayout) root_view.findViewById(R.id.input_la_web_link);
        input_la_email = (TextInputLayout) root_view.findViewById(R.id.input_la_email);
        input_la_service_name = (TextInputLayout) root_view.findViewById(R.id.input_la_service_name);
        input_la_provider_name = (TextInputLayout) root_view.findViewById(R.id.input_la_provider_name);
        input_la_about_this_sevice = (TextInputLayout) root_view.findViewById(R.id.input_la_about_this_sevice);
        input_la_phone_number = (TextInputLayout) root_view.findViewById(R.id.input_la_phone_number);

        edit_about_this_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.description_popup, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);


                userInput.setText(edit_about_this_service.getText());

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text

                                        edit_about_this_service.setText(userInput.getText());
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


        txt_timings = (TextView) root_view.findViewById(R.id.txt_timings);
        text_gender = (TextView) root_view.findViewById(R.id.text_gender);
        list_now_button = (TextView) root_view.findViewById(R.id.list_now_button);


        layout_rest_services_block = root_view.findViewById(R.id.layout_rest_services_block);
        layout_one_time_ride_block = root_view.findViewById(R.id.layout_one_time_ride_block);
        layout_room_sharing_block = root_view.findViewById(R.id.layout_room_sharing_block);
        layout_single_family_home_block = root_view.findViewById(R.id.layout_single_family_home_block);
        layout_commercial_and_office_block = root_view.findViewById(R.id.layout_commercial_and_office_block);
        layout_events_service_block = root_view.findViewById(R.id.layout_events_service_block);


        ////////EVENTS//////////////////////////////


        event_type_spinner = (Spinner) root_view.findViewById(R.id.event_type_spinner);

        multiple_selection = (LinearLayout) root_view.findViewById(R.id.multiple_selection);


        ///////////////////////////Events//////////////////

        commercial_available_from = (TextInputLayout) root_view.findViewById(R.id.commercial_available_from);
        commercial_la_no_of_rooms = (TextInputLayout) root_view.findViewById(R.id.commercial_la_no_of_rooms);
        commercial_la_total_sq_feet = (TextInputLayout) root_view.findViewById(R.id.commercial_la_total_sq_feet);
        commercial_la_asking_price = (TextInputLayout) root_view.findViewById(R.id.commercial_la_asking_price);
        commercial_spinner_price_type = (Spinner) root_view.findViewById(R.id.commercial_spinner_price_type);


        edit_commercial_available_from = (EditText) root_view.findViewById(R.id.edit_commercial_available_from);
        edit_commercial_no_of_rooms = (EditText) root_view.findViewById(R.id.edit_commercial_no_of_rooms);
        edit_commercial_total_sq_feet = (EditText) root_view.findViewById(R.id.edit_commercial_total_sq_feet);
        edit_commercial_asking_price = (EditText) root_view.findViewById(R.id.edit_commercial_asking_price);


        single_family_la_available_from = (TextInputLayout) root_view.findViewById(R.id.single_family_la_available_from);
        single_family_la_bed_rooms = (TextInputLayout) root_view.findViewById(R.id.single_family_la_bed_rooms);
        single_family_la_bath_rooms = (TextInputLayout) root_view.findViewById(R.id.single_family_la_bath_rooms);
        single_family_la_total_sq_feet = (TextInputLayout) root_view.findViewById(R.id.single_family_la_total_sq_feet);
        single_family_la_asking_price = (TextInputLayout) root_view.findViewById(R.id.single_family_la_asking_price);

        single_family_spinner_price_type = (Spinner) root_view.findViewById(R.id.single_family_spinner_price_type);


        edit_single_family_available_from = (EditText) root_view.findViewById(R.id.edit_single_family_available_from);
        edit_single_family_bedrooms = (EditText) root_view.findViewById(R.id.edit_single_family_bedrooms);
        edit_single_family_bath_rooms = (EditText) root_view.findViewById(R.id.edit_single_family_bath_rooms);
        edit_single_family_total_sq_feet = (EditText) root_view.findViewById(R.id.edit_single_family_total_sq_feet);
        edit_single_family_asking_price = (EditText) root_view.findViewById(R.id.edit_single_family_asking_price);

       /* edit_single_family_available_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });*/


        room_sharing_la_available_from = (TextInputLayout) root_view.findViewById(R.id.room_sharing_la_available_from);
        room_sharing_la_accommodates = (TextInputLayout) root_view.findViewById(R.id.room_sharing_la_accommodates);
        room_sharing_la_bed_rooms = (TextInputLayout) root_view.findViewById(R.id.room_sharing_la_bed_rooms);
        room_sharing_la_bath_rooms = (TextInputLayout) root_view.findViewById(R.id.room_sharing_la_bath_rooms);
        room_sharing_la_asking_price = (TextInputLayout) root_view.findViewById(R.id.room_sharing_la_asking_price);

        room_sharing_gender_spin = (Spinner) root_view.findViewById(R.id.room_sharing_gender_spin);
        room_sharing_price_type_spinner = (Spinner) root_view.findViewById(R.id.room_sharing_price_type_spinner);


        edit_room_sharing_available_from = (EditText) root_view.findViewById(R.id.edit_room_sharing_available_from);
        edit_room_sharing_accommodates = (EditText) root_view.findViewById(R.id.edit_room_sharing_accommodates);
        edit_room_sharing_bedrooms = (EditText) root_view.findViewById(R.id.edit_room_sharing_bedrooms);
        edit_room_sharing_bath_rooms = (EditText) root_view.findViewById(R.id.edit_room_sharing_bath_rooms);
        edit_room_sharing_asking_price = (EditText) root_view.findViewById(R.id.edit_room_sharing_asking_price);

        one_time_la_travel_date = (TextInputLayout) root_view.findViewById(R.id.one_time_la_travel_date);
        one_time_la_destination_from = (TextInputLayout) root_view.findViewById(R.id.one_time_la_destination_from);
        one_time_la_destination_to = (TextInputLayout) root_view.findViewById(R.id.one_time_la_destination_to);
        one_time_la_travel_time = (TextInputLayout) root_view.findViewById(R.id.one_time_la_travel_time);
        one_time_spinner_time = (Spinner) root_view.findViewById(R.id.one_time_spinner_time);


        edit_one_time_travel_date = (EditText) root_view.findViewById(R.id.edit_one_time_travel_date);
        edit_one_time_destination_from = (EditText) root_view.findViewById(R.id.edit_one_time_destination_from);
        edit_one_time_destination_to = (EditText) root_view.findViewById(R.id.edit_one_time_destination_to);
        edit_one_time_travel_time = (EditText) root_view.findViewById(R.id.edit_one_time_travel_time);


        par_lay = (LinearLayout) root_view.findViewById(R.id.par_lay);
        add_field_button = (Button) root_view.findViewById(R.id.add_field_button);
        add_event_button = (Button) root_view.findViewById(R.id.add_event_button);


        event_parent_lay = (LinearLayout) root_view.findViewById(R.id.event_parent_lay);
        type_of_event_lin = (LinearLayout) root_view.findViewById(R.id.type_of_event_lin);
        event_linear_lay = (LinearLayout) root_view.findViewById(R.id.event_linear_lay);


        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = layoutInflater.inflate(R.layout.fields, null);
        delete_button = (Button) rowView.findViewById(R.id.delete_button);
        // Add the new row before the add field button.
        //int i = par_lay.getChildCount();


        // if (par_lay.getChildCount() <= 7) {
        rowView.setId(par_lay.getChildCount());
        par_lay.addView(rowView, par_lay.getChildCount());
        rowView.setTag("visble");


        LayoutInflater event_layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View event_rowView = event_layoutInflater.inflate(R.layout.field_events, null);
        delete_event_button = (Button) event_rowView.findViewById(R.id.delete_event_button);

        event_rowView.setId(event_parent_lay.getChildCount());
        event_parent_lay.addView(event_rowView, event_parent_lay.getChildCount());
        event_rowView.setTag("visble");


        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(100, 100);
      /*  service_image1 = (ImageView) root_view.findViewById(R.id.service_image1);
        service_image2 = (ImageView) root_view.findViewById(R.id.service_image2);
        service_image3 = (ImageView) root_view.findViewById(R.id.service_image3);
        service_image4 = (ImageView) root_view.findViewById(R.id.service_image4);
        service_image5 = (ImageView) root_view.findViewById(R.id.service_image5);
        service_image1.setLayoutParams(llp);
        service_image2.setLayoutParams(llp);
        service_image3.setLayoutParams(llp);
        service_image4.setLayoutParams(llp);
        service_image5.setLayoutParams(llp);*/
        addServiceImages();


        Products_list = (Spinner) root_view.findViewById(R.id.main_products_list);
        Products_sub_list = (Spinner) root_view.findViewById(R.id.products_sub_list);


        final Map<String, Map<String, String>> categoriesList = Commons.getCategories(getActivity());
        Map<String, String> subcategories = null;
        ArrayList<String> categoriesArray = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> entry : categoriesList.entrySet()) {
            String category = entry.getKey();
            categoriesArray.add(category);
        }

        /*ArrayAdapter<CharSequence> product_adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.array_name, android.R.layout.simple_spinner_item);*/
        ArrayAdapter<String> product_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, categoriesArray);
        product_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final String[] category_array = getResources().getStringArray(R.array.array_name);


        Products_list.setAdapter(product_adapter);
        Products_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Map<String, String> subCategoriesList = categoriesList.get(Products_list.getSelectedItem().toString().trim());
                Log.i("subcategoriesSize", "" + subCategoriesList.size());

                resetSubCategories(subCategoriesList);
                ArrayList<String> subCategoriesArray = new ArrayList<>();
                for (Map.Entry<String, String> entry : subCategoriesList.entrySet()) {
                    String subCategory = entry.getKey();
                    subCategoriesArray.add(subCategory);
                }
                if (subCategoriesArray.size() > 0) {
                    Products_sub_list.setVisibility(view.VISIBLE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subCategoriesArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Products_sub_list.setAdapter(adapter);
                } else {
                    subCategoriesArray.add("");

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subCategoriesArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Products_sub_list.setAdapter(adapter);
                    Products_sub_list.setVisibility(view.GONE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        Products_sub_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String formConstant = subList.get(Products_sub_list.getSelectedItem().toString().trim());

                //  Toast.makeText(getActivity(), "" + Products_sub_list.getSelectedItem().toString().trim(), Toast.LENGTH_SHORT).show();
                initDynamicForms(formConstant);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        add_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.field_events, null);
                // Add the new row before the add field button.
                //int i = par_lay.getChildCount();
                delete_event_button = (Button) rowView.findViewById(R.id.delete_event_button);
                delete_event_button.setOnClickListener(AddServiceFragment.this);
                if (eventVisibleCount < 5) {
                    // rowView.setId(par_lay.getChildCount());
                    rowView.setTag("visble");
                    event_parent_lay.addView(rowView, event_parent_lay.getChildCount());
                    add_event_button.setVisibility(View.VISIBLE);
                    eventVisibleCount++;
                }
                if (eventVisibleCount > 6) {
                    add_event_button.setVisibility(View.GONE);
                }

                // Toast.makeText(getActivity(), "count is:" + event_parent_lay.getChildCount(), Toast.LENGTH_SHORT).show();

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
                    //  Toast.makeText(getActivity(), "count is:" + event_parent_lay.getChildCount(), Toast.LENGTH_SHORT).show();

                } else {

                }

            }
        });


        add_field_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.fields, null);
                // Add the new row before the add field button.
                //int i = par_lay.getChildCount();
                delete_button = (Button) rowView.findViewById(R.id.delete_button);
                delete_button.setOnClickListener(AddServiceFragment.this);
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
                if (visblecount == 0) {
                    delete_button.setVisibility(View.GONE);
                }

                // Toast.makeText(getActivity(), "count is:" + par_lay.getChildCount(), Toast.LENGTH_SHORT).show();

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
                    if (visblecount == 0) {
                        delete_button.setVisibility(View.GONE);
                    }
                    //  Toast.makeText(getActivity(), "count is:" + par_lay.getChildCount(), Toast.LENGTH_SHORT).show();

                } else {

                }

            }
        });


        String[] price_data = getResources().getStringArray(R.array.doller_names);

        //SharedPreferences shPref = getActivity().getSharedPreferences("SearchData", getActivity().MODE_PRIVATE);
        SharedPreferences user_LocationData = AppConstants.preferencesData(getActivity());
        String cuntryCode = user_LocationData.getString(DataKeyValues.CUNTRY_CODE, null);
        if (cuntryCode != null) {
            commercial_spinner_price_type.setSelection(Arrays.asList(price_data).indexOf(DataKeyValues.getCurrencyType(cuntryCode)));
            single_family_spinner_price_type.setSelection(Arrays.asList(price_data).indexOf(DataKeyValues.getCurrencyType(cuntryCode)));
            room_sharing_price_type_spinner.setSelection(Arrays.asList(price_data).indexOf(DataKeyValues.getCurrencyType(cuntryCode)));
        } else {
            commercial_spinner_price_type.setSelection(Arrays.asList(price_data).indexOf("INR"));
            single_family_spinner_price_type.setSelection(Arrays.asList(price_data).indexOf("INR"));
            room_sharing_price_type_spinner.setSelection(Arrays.asList(price_data).indexOf("INR"));
        }


        list_now_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (imagesArray.get(0) == true || imagesArray.get(1) == true || imagesArray.get(2) == true || imagesArray.get(3) == true || imagesArray.get(4) == true) {
                    if (edit_service_name.getText().length() > 0) {
                        if (Products_list.getSelectedItem().toString() != null && Products_list.getSelectedItemPosition() != 0) {

                            if ((Products_sub_list.getSelectedItem().toString() != null && Products_sub_list.getSelectedItemPosition() != 0) || Products_list.getSelectedItemPosition() == 2 || Products_list.getSelectedItemPosition() == 14) {

                                if (edit_provider_name.getText().length() > 0) {

                                    if (edit_about_this_service.getText().length() > 0) {

                                        if (edit_phone_number.getText().length() > 0) {

                                            if (edit_email.getText().toString().matches(emailPattern) && edit_email.getText().toString().length() > 0) {

                                                String[] rentals = getResources().getStringArray(R.array.rentals);

                                                String[] rides = getResources().getStringArray(R.array.rides);

                                                String[] events = getResources().getStringArray(R.array.events);


                                                if (Products_sub_list.getSelectedItem().toString().equalsIgnoreCase(rentals[2])) {


                                                    String[] price_data = getResources().getStringArray(R.array.doller_names);
                                                    SharedPreferences user_LocationData = AppConstants.preferencesData(getActivity());
                                                    String cuntryCode = user_LocationData.getString(DataKeyValues.CUNTRY_CODE, null);
                                                    if (cuntryCode != null) {
                                                        commercial_spinner_price_type.setSelection(Arrays.asList(price_data).indexOf(DataKeyValues.getCurrencyType(cuntryCode)));
                                                    } else {
                                                        commercial_spinner_price_type.setSelection(Arrays.asList(price_data).indexOf("INR"));
                                                    }


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

                                                                    //String price = edit_asking_price_value + spinner_price_type_value;
                                                                    commercAndOfficeServicePojo = getCommercialPojo(availDate, roomscount, totaArea, price, priceType);
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


                                                } else if (Products_sub_list.getSelectedItem().toString().equalsIgnoreCase(rides[0])) {

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

                                                                    oneTimeRideServicePojo = getOneTimeRideServicePojo(travelDate, destfrom, destto, travel_time);
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


                                                } else if (Products_sub_list.getSelectedItem().toString().equalsIgnoreCase(rentals[1])) {


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


                                                                        roomSharingServicePojo = getRoomSharingServicePojo(availDate, accommodates, bedroomcount, bathroomcount, gendervalue, price, priceType);
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


                                                } else if (Products_sub_list.getSelectedItem().toString().equalsIgnoreCase(rentals[0])) {


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
                                                                        //String price = edit_asking_price_value +" "+ spinner_price_type_value;

                                                                        singleFamilyServicePojo = getSingleFamilyServicePojo(availDate, bedroomcount, bathroomcount, totaArea, price, pricetype);
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

                                                else if (Products_sub_list.getSelectedItem().toString().equalsIgnoreCase(events[0]) || Products_sub_list.getSelectedItem().toString().equalsIgnoreCase(events[1]) || Products_sub_list.getSelectedItem().toString().equalsIgnoreCase(events[2])) {


                                                    String event_type = event_type_spinner.getSelectedItem().toString().trim();
                                                    eventTypesPojos = new ArrayList<EventTypesPojo>();

                                                    for (int i = 0; i < event_parent_lay.getChildCount(); ++i) {
                                                        View rootview = event_parent_lay.getChildAt(i);
                                                        //    rootview.setVisibility(View.GONE);
                                                        // Toast.makeText(getActivity(), rootview.getTag().toString(), Toast.LENGTH_SHORT).show();

                                                        if (!rootview.getTag().equals("gone")) {

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


                                                            EventTypesPojo eventTypesPojoObj = new EventTypesPojo();
                                                            eventTypesPojoObj.setEvent_date(edit_date.getText().toString().trim());
                                                            String from_time_value = edit_timing_time.getText().toString();
                                                            String from_time_type = spinner_Timings_mul.getSelectedItem().toString();
                                                            eventTypesPojoObj.setFromTime(from_time_value + " " + from_time_type);
                                                            String to_time_value = edit_timings_time_mul.getText().toString();
                                                            String to_time_type = spinner_Timings_multi.getSelectedItem().toString();
                                                            eventTypesPojoObj.setToTime(to_time_value + " " + to_time_type);


                                                            if ((edit_date != null && edit_timing_time != null && edit_timings_time_mul != null) && !(edit_date.getText().toString().isEmpty() && edit_timing_time.getText().toString().isEmpty() && edit_timings_time_mul.getText().toString().isEmpty())) {

                                                                eventTypesPojos.add(eventTypesPojoObj);

                                                               /* Toast.makeText(getActivity(), edit_date.getText().toString() + "\n" + edit_timing_time.getText().toString() + "\n" +
                                                                        spinner_Timings_mul.getSelectedItem().toString() + "\n" + to_time_value + "\n" +
                                                                        to_time_type, Toast.LENGTH_SHORT).show();*/

                                                            }

                                                        }

                                                    }


                                                    eventsServicePojo = getEventsServicePojo(event_type, eventTypesPojos);
                                                    runServer();

                                                }

                                                /////////////////////////////////////Timingsssssss//////////////////////////////////////////////


                                                else {

                                                    timingsPojos = new ArrayList<TimingsPojo>();


                                                    for (int i = 0; i < par_lay.getChildCount(); ++i) {
                                                        View rootview = par_lay.getChildAt(i);
                                                        //    rootview.setVisibility(View.GONE);
                                                        // Toast.makeText(getActivity(), rootview.getTag().toString(), Toast.LENGTH_SHORT).show();

                                                        if (!rootview.getTag().equals("gone")) {

                                                            rest_serv_la_timings_time = (TextInputLayout) rootview.findViewById(R.id.rest_serv_la_timings_time);
                                                            rest_serv_la_timings_time_to = (TextInputLayout) rootview.findViewById(R.id.rest_serv_la_timings_time_to);
                                                            rest_serv_spinner_day = (Spinner) rootview.findViewById(R.id.rest_serv_spinner_day);

                                                           /* LinearLayout.LayoutParams llp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                                                            llp.gravity= Gravity.CENTER_VERTICAL;
                                                            rest_serv_spinner_day.setLayoutParams(llp);*/

                                                            rest_serv_spinner_Timings = (Spinner) rootview.findViewById(R.id.rest_serv_spinner_Timings);
                                                            rest_serv_spinner_Timings_to = (Spinner) rootview.findViewById(R.id.rest_serv_spinner_Timings_to);
                                                            edit_rest_serv_timings_time = (EditText) rootview.findViewById(R.id.edit_rest_serv_timings_time);
                                                            // edit_rest_serv_timings_time.setLayoutParams(llp);
                                                            edit_rest_serv_timings_time_to = (EditText) rootview.findViewById(R.id.edit_rest_serv_timings_time_to);
                                                            rest_serv_timings_layout = (LinearLayout) rootview.findViewById(R.id.rest_serv_timings_layout);
                                                            // edit_rest_serv_timings_time.setLayoutParams(llp);
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

                                                               /* Toast.makeText(getActivity(), rest_serv_spinner_day_value + "\n" + edit_rest_serv_timings_time_to_value + "\n" +
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

                                Toast.makeText(getActivity(), "Please select subcategory of Service.", Toast.LENGTH_SHORT).show();

                            }


                        } else {

                            /*main_products_list.requestFocus();
                            main_products_list.setError("Please provide Provider Name");*/

                            Toast.makeText(getActivity(), "Please select category of Service.", Toast.LENGTH_SHORT).show();

                        }


                    } else {
                        input_la_service_name.requestFocus();
                        edit_service_name.setError("Please provide Service Name");
                    }
                } else {
                    Toast.makeText(getActivity(), "Please select images", Toast.LENGTH_SHORT).show();
                }


            }
        });


        return root_view;
    }


    private void resetSubCategories(Map<String, String> subcategoriesList) {
        subList = null;
        subList = subcategoriesList;
    }

    private void initDynamicForms(String constant) {

        if (constant.equals("1")) {
            layout_rest_services_block.setVisibility(View.GONE);
            layout_one_time_ride_block.setVisibility(View.GONE);
            layout_room_sharing_block.setVisibility(View.GONE);
            layout_single_family_home_block.setVisibility(View.GONE);
            layout_commercial_and_office_block.setVisibility(View.GONE);
            layout_events_service_block.setVisibility(View.VISIBLE);
            final String[] event_type = getResources().getStringArray(R.array.event_type);


            event_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (event_type_spinner.getSelectedItem().toString().trim().equalsIgnoreCase(event_type[0])) {
                        //  single_event_rela_layout.setVisibility(View.VISIBLE);


                        for (int i = 0; i < event_parent_lay.getChildCount(); ++i) {
                            View rootview = event_parent_lay.getChildAt(i);
                            rootview.setVisibility(View.GONE);
                            //  Toast.makeText(getActivity(), rootview.getTag().toString(), Toast.LENGTH_SHORT).show();
                            rootview.setTag("gone");

                        }
                        LayoutInflater event_layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View event_rowView = event_layoutInflater.inflate(R.layout.field_events, null);
                        delete_event_button = (Button) event_rowView.findViewById(R.id.delete_event_button);

                        event_rowView.setId(event_parent_lay.getChildCount());
                        event_parent_lay.addView(event_rowView, event_parent_lay.getChildCount());
                        event_rowView.setTag("visble");

                        delete_event_button.setVisibility(View.GONE);
                        add_event_button.setVisibility(View.GONE);


                    }
                    if (event_type_spinner.getSelectedItem().toString().trim().equalsIgnoreCase(event_type[1])) {
                        // multiple_selection.setVisibility(View.VISIBLE);

                        delete_event_button.setVisibility(View.VISIBLE);
                        add_event_button.setVisibility(View.VISIBLE);

                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } else if (constant.equals("2")) {
            layout_rest_services_block.setVisibility(View.GONE);
            layout_one_time_ride_block.setVisibility(View.GONE);
            layout_room_sharing_block.setVisibility(View.GONE);
            layout_single_family_home_block.setVisibility(View.VISIBLE);
            layout_commercial_and_office_block.setVisibility(View.GONE);
            layout_events_service_block.setVisibility(View.GONE);
        } else if (constant.equals("3")) {
            layout_rest_services_block.setVisibility(View.GONE);
            layout_one_time_ride_block.setVisibility(View.GONE);
            layout_room_sharing_block.setVisibility(View.VISIBLE);
            layout_single_family_home_block.setVisibility(View.GONE);
            layout_commercial_and_office_block.setVisibility(View.GONE);
            layout_events_service_block.setVisibility(View.GONE);
        } else if (constant.equals("4")) {
            layout_rest_services_block.setVisibility(View.GONE);
            layout_one_time_ride_block.setVisibility(View.GONE);
            layout_room_sharing_block.setVisibility(View.GONE);
            layout_single_family_home_block.setVisibility(View.GONE);
            layout_commercial_and_office_block.setVisibility(View.VISIBLE);
            layout_events_service_block.setVisibility(View.GONE);
        } else if (constant.equals("5")) {
            layout_rest_services_block.setVisibility(View.GONE);
            layout_one_time_ride_block.setVisibility(View.VISIBLE);
            layout_room_sharing_block.setVisibility(View.GONE);
            layout_single_family_home_block.setVisibility(View.GONE);
            layout_commercial_and_office_block.setVisibility(View.GONE);
            layout_events_service_block.setVisibility(View.GONE);
        } else {

            layout_rest_services_block.setVisibility(View.VISIBLE);
            layout_one_time_ride_block.setVisibility(View.GONE);
            layout_room_sharing_block.setVisibility(View.GONE);
            layout_single_family_home_block.setVisibility(View.GONE);
            layout_commercial_and_office_block.setVisibility(View.GONE);
            layout_events_service_block.setVisibility(View.GONE);
        }

    }


    private void addServiceImages() {

        for (int i = 0; i < 5; i++) {

            imagesArray.add(false);
            service_images_linear_layout.setOrientation(LinearLayout.HORIZONTAL);
            service_image_view = new ImageView(getActivity());
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(100, 100);
            llp.setMargins(10, 0, 10, 0);
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

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.usepicture);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

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
        // new ServiceUploadAsync().execute();

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
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
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
                if (visblecount == 0) {
                    delete_button.setVisibility(View.GONE);
                }
                // Toast.makeText(getActivity(), "count is:" + par_lay.getChildCount(), Toast.LENGTH_SHORT).show();

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
                // Toast.makeText(getActivity(), "count is:" + event_parent_lay.getChildCount(), Toast.LENGTH_SHORT).show();


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

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
           /* if (singleFamilyServicePojo != null) {
                edit_single_family_available_from.setText(day + "-" + (month + 1) + "-" + year);
            } else {
                edit_travel_date.setText(day + "-" + (month + 1) + "-" + year);

            }*/

            if (singleFamilyServicePojo != null) {
                edit_single_family_available_from.setText(day + "-" + (month + 1) + "-" + year);
            }

        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            if (hourOfDay > 12) {
                hourOfDay -= 12;
            }
            edit_travel_time.setText(hourOfDay + ":" + minute);

        }
    }

    private class ServiceUploadAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = getJSONObject();
            String service_result = serviceHandler.makeServiceCall(AppConstants.SERVICE_UPLOAD_URL, "POST", requestObject);

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


                        Intent loginIntent = new Intent(getActivity(), NavigationDrawerActivity.class);
                        startActivity(loginIntent);
                        getActivity().finish();

                        Toast.makeText(getActivity(), jsObj.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), jsObj.getString("message"), Toast.LENGTH_SHORT).show();

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
            requestObject.accumulate(DataKeyValues.UPLOAD_USERID, userId);
            requestObject.accumulate(DataKeyValues.UPLOAD_NAME, edit_service_name.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_PROVIDER, edit_provider_name.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_DESCRIPTION, edit_about_this_service.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_CATEGORY, Products_list.getSelectedItem().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_SUBCATEGORY, Products_sub_list.getSelectedItem().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_CONTACT, edit_phone_number.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_EMAIL, edit_email.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_WEBSITE, edit_web_link.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_ADDRESS, edit_address.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.UPLOAD_LATITUDE, user_latitude);
            requestObject.accumulate(DataKeyValues.UPLOAD_LANGITUDE, user_longitude);
            requestObject.accumulate(DataKeyValues.UPLOAD_SERVICE_ZIPCODE, user_zipcode);
            requestObject.accumulate(DataKeyValues.UPLOAD_SERVICEAREA, user_location_address);
            if (commercAndOfficeServicePojo != null) {
                JSONObject commercial_office_object = new JSONObject();


                commercial_office_object.accumulate(DataKeyValues.UPLOAD_AVAILABLE_FROM_DATE, commercAndOfficeServicePojo.getAvailable_from_date());
                commercial_office_object.accumulate(DataKeyValues.UPLOAD_ROOMS, commercAndOfficeServicePojo.getNumber_of_rooms());
                commercial_office_object.accumulate(DataKeyValues.UPLOAD_AREA_SQ_FEET, commercAndOfficeServicePojo.getTotalsq_feet());
                commercial_office_object.accumulate(DataKeyValues.UPLOAD_PRICE, commercAndOfficeServicePojo.getPrice());
                commercial_office_object.accumulate(DataKeyValues.UPLOAD_PRICETYPE, commercAndOfficeServicePojo.getPrice_type());

                requestObject.accumulate(DataKeyValues.UPLOAD_COMMERCIAL_OFFICE, commercial_office_object);
            } else if (singleFamilyServicePojo != null) {
                JSONObject singlefamilyroom = new JSONObject();

                singlefamilyroom.accumulate(DataKeyValues.UPLOAD_AVAILABLE_FROM_DATE, singleFamilyServicePojo.getAvailable_from_date());
                singlefamilyroom.accumulate(DataKeyValues.UPLOAD_BEDROOMS, singleFamilyServicePojo.getNo_bedrooms());
                singlefamilyroom.accumulate(DataKeyValues.UPLOAD_BATHROOMS, singleFamilyServicePojo.getNo_bathrooms());
                singlefamilyroom.accumulate(DataKeyValues.UPLOAD_AREA_SQ_FEET, singleFamilyServicePojo.getTotalsq_feet());
                singlefamilyroom.accumulate(DataKeyValues.UPLOAD_PRICE, singleFamilyServicePojo.getPrice());
                singlefamilyroom.accumulate(DataKeyValues.UPLOAD_PRICETYPE, singleFamilyServicePojo.getPrice_type());

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

                requestObject.accumulate(DataKeyValues.UPLOAD_ROOMSHARING, roomshare);
            } else if (oneTimeRideServicePojo != null) {
                JSONObject onetimeride = new JSONObject();

                onetimeride.accumulate("travelDate", oneTimeRideServicePojo.getTraveldate());
                onetimeride.accumulate("destinationFrom", oneTimeRideServicePojo.getDestination_from());
                onetimeride.accumulate("destinationTo", oneTimeRideServicePojo.getDestination_to());
                onetimeride.accumulate("timeOfLeaving", oneTimeRideServicePojo.getTravel_time());


                requestObject.accumulate(DataKeyValues.UPLOAD_ONETIMERIDE, onetimeride);
            } else if (restOtherServicesPojo != null) {


                JSONArray serivcetimings = new JSONArray();
                for (int i = 0; i < timingsPojos.size(); i++) {
                    JSONObject timingsObject = new JSONObject();

                    timingsObject.accumulate("day", timingsPojos.get(i).getWeek_day());
                    timingsObject.accumulate("daycount", timingsPojos.get(i).getDaycount());
                    timingsObject.accumulate("fromtime", timingsPojos.get(i).getFromTime());
                    timingsObject.accumulate("totime", timingsPojos.get(i).getToTime());

                    serivcetimings.put(i, timingsObject);
                }

                requestObject.accumulate("serivcetimings", serivcetimings);

            } else if (eventsServicePojo != null) {

                JSONObject event = new JSONObject();

                event.accumulate("eventType", eventsServicePojo.getEvent_type());

                JSONArray eventDetails = new JSONArray();

                for (int i = 0; i < eventTypesPojos.size(); i++) {

                    JSONObject etsObject = new JSONObject();

                    etsObject.accumulate("eventDate", eventTypesPojos.get(i).getEvent_date());
                    etsObject.accumulate("timeFrom", eventTypesPojos.get(i).getFromTime());
                    etsObject.accumulate("timeTo", eventTypesPojos.get(i).getToTime());

                    eventDetails.put(i, etsObject);
                }
                event.accumulate("eventDetails", eventDetails);

                requestObject.accumulate("event", event);

            }


            for (int i = 0; i < bitmapArray.size(); i++) {
                String encodedImage = createBase64(bitmapArray.get(i));
                Log.i("Name ", encodedImage);
                array_of_images.add(encodedImage);
                // allimagesMap.put(i + 1, encodedImage);
            }
            try {
                JSONArray imgArray = new JSONArray();

                for (int i = 0; i < array_of_images.size(); i++) {
                    imgArray.put(array_of_images.get(i));
                }
                requestObject.accumulate("images", imgArray);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }



            /*try {
                JSONArray inner_images_Array = new JSONArray();
                for (Map.Entry<Integer, String> entry : allimagesMap.entrySet()) {
                    JSONObject inner_obj = new JSONObject();
                    inner_obj.accumulate("number", entry.getKey());
                    inner_obj.accumulate("imagebasecode", entry.getValue());
                    inner_images_Array.put(inner_obj);
                }
                requestObject.accumulate("allimages", inner_images_Array);
                requestObject.accumulate("images", null);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject;

    }


    private void changeOrRemoveImageAlert(final int selected_img_postion) {
        final CharSequence[] items = {"Change Picture", "Remove Picture"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.usepicture);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Change Picture")) {

                    imagesArray.add(selected_img_postion, false);
                    selectImage();

                } else if (items[item].equals("Remove Picture")) {

                    ImageView selectedImage = (ImageView) service_images_linear_layout.getChildAt(selected_img_postion);
                    selectedImage.setImageResource(R.mipmap.ic_camera);
                    imagesArray.add(selected_img_postion, false);
                }
            }
        });
        builder.show();
    }

    public String createBase64(Bitmap bitimage) {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitimage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

        return encodedImage;
    }

    private CommercAndOfficeServicePojo getCommercialPojo(String availDate, String roomscount, String totaArea, String price, String priceType) {
        CommercAndOfficeServicePojo pojo = new CommercAndOfficeServicePojo();

        pojo.setAvailable_from_date(availDate);
        pojo.setNumber_of_rooms(roomscount);
        pojo.setPrice(price);
        pojo.setPrice_type(priceType);
        pojo.setTotalsq_feet(totaArea);

        return pojo;

    }

    private OneTimeRideServicePojo getOneTimeRideServicePojo(String traveldate, String destination_from, String destination_to, String travel_time) {
        OneTimeRideServicePojo pojo = new OneTimeRideServicePojo();

        pojo.setTraveldate(traveldate);
        pojo.setDestination_from(destination_from);
        pojo.setDestination_to(destination_to);
        pojo.setTravel_time(travel_time);

        return pojo;

    }

    private RoomSharingServicePojo getRoomSharingServicePojo(String availDate, String accommodates, String bedroomcount, String bathroomcount, String gendervalue, String price, String priceType) {
        RoomSharingServicePojo pojo = new RoomSharingServicePojo();

        pojo.setAvailable_from_date(availDate);
        pojo.setAccommadates(accommodates);
        pojo.setNo_bedrooms(bedroomcount);
        pojo.setNo_bathrooms(bathroomcount);
        pojo.setGender(gendervalue);
        pojo.setPrice(price);
        pojo.setPrice_type(priceType);

        return pojo;

    }

    private SingleFamilyServicePojo getSingleFamilyServicePojo(String availDate, String bedroomcount, String bathroomcount, String totaArea, String price, String pricetype) {
        SingleFamilyServicePojo pojo = new SingleFamilyServicePojo();


        pojo.setAvailable_from_date(availDate);
        pojo.setNo_bedrooms(bedroomcount);
        pojo.setNo_bathrooms(bathroomcount);
        pojo.setTotalsq_feet(totaArea);
        pojo.setPrice(price);
        pojo.setPrice_type(pricetype);

        return pojo;

    }

    private RestOtherServicesPojo getRestOtherServicesPojo(ArrayList<TimingsPojo> timingsPojos1) {
        RestOtherServicesPojo pojo = new RestOtherServicesPojo();

        pojo.setTimings(timingsPojos1);

        return pojo;

    }

    private EventsServicePojo getEventsServicePojo(String event_type, ArrayList<EventTypesPojo> eventTypesPojos) {
        EventsServicePojo pojo = new EventsServicePojo();


//        pojo.setServices(typeOfServicesPojo);
        pojo.setEvent_type(event_type);
        pojo.setEvents(eventTypesPojos);


        return pojo;

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
                    Internetdialog.dismiss();

                }


            }
        });
        Internetdialog.show();

    }

    public void runServer() {
        SharedPrefPojo pojo = Utility.getData(getActivity());

        user_latitude = pojo.getLattitude();
        user_longitude = pojo.getLangitude();
        user_zipcode = pojo.getZipcode();
        user_location_address = pojo.getArea();


        //  Toast.makeText(getActivity(), "lat: " + user_latitude + " lang: " + user_longitude + " Zipcode: " + user_zipcode + " Address: " + user_location_address, Toast.LENGTH_SHORT).show();
        if (user_latitude != null && user_longitude != null) {
            if (internetConnection.isConnectingToInternet(getActivity())) {
                Utility.resetUploadLocation(getActivity());
                new ServiceUploadAsync().execute();
            } else {
                InternetConnectionAlert();
            }
        } else {
            Intent myIntent = new Intent(getActivity(), LocationSettingsActivity.class);
            myIntent.putExtra("requestFrom", "upload_scr");
            startActivity(myIntent);
        }
    }
}