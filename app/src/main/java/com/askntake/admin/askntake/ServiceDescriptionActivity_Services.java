package com.askntake.admin.askntake;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.ReviewAdapter;
import Adapters.ViewPagerImagesAdapter;
import AppUtils.AppConstants;
import AppUtils.DataKeyValues;
import AppUtils.ServiceHandler;
import AppUtils.Utility;
import Pojo.CommercAndOfficeServicePojo;
import Pojo.CustomerReviewPojo;
import Pojo.EventTypesPojo;
import Pojo.EventsServicePojo;
import Pojo.ItemServicePojo;
import Pojo.OneTimeRideServicePojo;
import Pojo.RestOtherServicesPojo;
import Pojo.RoomSharingServicePojo;
import Pojo.SingleFamilyServicePojo;
import Pojo.TimingsPojo;


/**
 * Created by on 24/04/2017.
 */

public class ServiceDescriptionActivity_Services extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    ProgressDialog mProgressDialog;
    /*For Reviews*/

    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private ReviewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<CustomerReviewPojo> custRevList;
    private List<CustomerReviewPojo> newRevList = new ArrayList<>();
    protected Handler handler;

    String value = "";
    String eventValue = "";
    boolean review = false;

    private Menu menu;
    private MenuItem menuItem;


    TextView chat_button, item_name, item_name_value, provider_name_value, provider_name, txt_favorites_name, num_fav_count, txt_reviews, review_counting_view, distance_txt, desc_textview, icon_view, desc_text, contact_text,
            phone_num_txt, phone_num_txt_value, email_txt, email_txt_value, address_txt, distance_cal_txt, timings_textview, timings_icon_view,
            cust_review_textview, review_count_view, reviews_icon_view, dyn_desc_textview, dyn_icon_view, dyn_desc_text;
    RatingBar ratingbar, reviews_ratingbar;
    //ViewPager viewPager;
    GoogleMap googleMap;
    RelativeLayout pager_layout, desc_lay, dynamic_desc_lay, cusomer_reviews_layout;
    Button but_getdirection, but_websiteview, write_review_txt;

    LinearLayout phone_num_function, email_function;


    public View layout_single_family_home_dynamic, layout_rest_services_block, layout_one_time_ride_dynamic,
            layout_room_sharing_dynamic, layout_commercial_and_office_dynamic, layout_events_service_block_dynamic;


    List<String> product_images;

    private ViewPager viewPager;
    private ImagePagerAdapter adapter;

    //ViewPagerImagesAdapter pagerImagesAdapter;
    private int dotsCount;
    private ImageView[] dots;
    private LinearLayout pager_indicator;
    boolean no_profile_image_flag, its_my_own_product;
    String service_id, user_id, distance, distance_type;

    String favoritesStatus, reqMessage;

    SharedPreferences fb_data_pref;
    ImageView calling_button, email_button;

    FrameLayout dynamic_frame_layout;

    String service_name, itemId, description, category, ownerId, email, image, firstname, lastname, zipcode,
            area, website, latitude, langitude, product_city, product_state, visitcount, favoritesCount, subcategory,
            day, daycount, fromtime, totime, eventType, contact, rating, reviewsCount, avalableFrom, rooms,
            eventDate, timeTo, timeFrom, providr_name, bedrooms, bathrooms, areainsqft, price, priceType,
            availableFrom, accomodates, gender, askingprice,
            travelDate, timeOfLeaving, destinationFrom, destinationTo;

    boolean isFavorite_flag = false;
    String UserId_Main, userFname;

    TextView traveldate_value, dest_from_value, dest_to_value, time_leaving_value,
            available_from_dyn_comm_ofc_value, comm_and_ofc_no_of_rooms_vlue, comm_and_ofc_total_sq_value, comm_and_ofc_price_text_value, comm_and_ofc_price_type_text_value,
            available_from_dyn_room_sharing_value, edit_room_sharing_dyn_accommodates_value, room_sharing_bedrooms_vlue, room_sharing_bath_rooms_vlue, gender_text_value, price_text_value,
            available_from_dyn_single_fam_value, edit_single_family_bedrooms_vlue, single_fam_bath_rooms_vlue, single_fam_total_sq_value, single_fam_price_text_value, single_fam_price_type_text_value,
            events_type_value, loadmore_view;

    TextView rest_serv_tv_day0;

    CommercAndOfficeServicePojo commercAndOfficeServicePojo = null;
    EventsServicePojo eventsServicePojo = null;
    EventTypesPojo eventTypesPojo = null;

    OneTimeRideServicePojo oneTimeRideServicePojo = null;
    //    RestOtherServicesPojo restOtherServicesPojo = null;
    RoomSharingServicePojo roomSharingServicePojo = null;
    SingleFamilyServicePojo singleFamilyServicePojo = null;
    //    ArrayList<TimingsPojo> timingsPojos = null;
    TimingsPojo timingsPojo = null;

    public boolean isLoadingFirstTime = true;

    public int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_service_description_services);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dynamic_frame_layout = (FrameLayout) findViewById(R.id.dynamic_frame_layout);

        rest_serv_tv_day0 = (TextView) findViewById(R.id.rest_serv_tv_day0);
        /*rest_serv_tv_day1 = (TextView) findViewById(R.id.rest_serv_tv_day1);
        rest_serv_tv_day2 = (TextView) findViewById(R.id.rest_serv_tv_day2);
        rest_serv_tv_day3 = (TextView) findViewById(R.id.rest_serv_tv_day3);
        rest_serv_tv_day4 = (TextView) findViewById(R.id.rest_serv_tv_day4);
        rest_serv_tv_day5 = (TextView) findViewById(R.id.rest_serv_tv_day5);
        rest_serv_tv_day6 = (TextView) findViewById(R.id.rest_serv_tv_day6);*/


        layout_single_family_home_dynamic = findViewById(R.id.layout_single_family_home_dynamic);
        layout_rest_services_block = findViewById(R.id.layout_rest_services_block);
        layout_one_time_ride_dynamic = findViewById(R.id.layout_one_time_ride_dynamic);
        layout_room_sharing_dynamic = findViewById(R.id.layout_room_sharing_dynamic);
        layout_commercial_and_office_dynamic = findViewById(R.id.layout_commercial_and_office_dynamic);
        layout_events_service_block_dynamic = findViewById(R.id.layout_events_service_block_dynamic);

        /*private RecyclerView recyclerView;
        private ReviewsAdapter adapter;
        private List<CustomerReviewPojo> customerReviewPojoList;*/


        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        loadmore_view = (TextView) findViewById(R.id.loadmore_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        custRevList = new ArrayList<CustomerReviewPojo>();
        handler = new Handler();

        loaddata();

        fb_data_pref = AppConstants.preferencesData(getApplication());
        its_my_own_product = getIntent().getBooleanExtra("myownproduct", false);
        service_id = getIntent().getStringExtra("service_id");
        //user_id = getIntent().getStringExtra("user_id");
        distance = getIntent().getStringExtra("distance");
        distance_type = getIntent().getStringExtra("distance_type");
        UserId_Main = fb_data_pref.getString(DataKeyValues.USER_USERID, null);
        userFname = fb_data_pref.getString("fb_username", null);
        product_images = new ArrayList<>();

        // popup_icon = (ImageView) findViewById(R.id.show_popup_menu);
        // feverate_img = (ImageView) findViewById(R.id.favourite_menu);

        chat_button = (TextView) findViewById(R.id.chat_button);
        item_name = (TextView) findViewById(R.id.item_name);
        item_name_value = (TextView) findViewById(R.id.item_name_value);

        phone_num_function = (LinearLayout) findViewById(R.id.phone_num_function);
        email_function = (LinearLayout) findViewById(R.id.email_function);

        provider_name = (TextView) findViewById(R.id.provider_name);
        provider_name_value = (TextView) findViewById(R.id.provider_name_value);
        txt_favorites_name = (TextView) findViewById(R.id.txt_favorites_name);
        num_fav_count = (TextView) findViewById(R.id.num_fav_count);
        txt_reviews = (TextView) findViewById(R.id.txt_reviews);
        review_counting_view = (TextView) findViewById(R.id.review_counting_view);
        distance_txt = (TextView) findViewById(R.id.distance_txt);
        desc_textview = (TextView) findViewById(R.id.desc_textview);
        dyn_desc_textview = (TextView) findViewById(R.id.dyn_desc_textview);

        desc_text = (TextView) findViewById(R.id.desc_text);
        dyn_desc_text = (TextView) findViewById(R.id.dyn_desc_text);

        icon_view = (TextView) findViewById(R.id.icon_view);
        dyn_icon_view = (TextView) findViewById(R.id.dyn_icon_view);

        contact_text = (TextView) findViewById(R.id.contact_text);
        phone_num_txt = (TextView) findViewById(R.id.phone_num_txt);
        phone_num_txt_value = (TextView) findViewById(R.id.phone_num_txt_value);
        email_txt = (TextView) findViewById(R.id.email_txt);
        email_txt_value = (TextView) findViewById(R.id.email_txt_value);
        address_txt = (TextView) findViewById(R.id.address_txt);
        distance_cal_txt = (TextView) findViewById(R.id.distance_cal_txt);
        // timings_textview = (TextView) findViewById(R.id.timings_textview);
        // timings_icon_view = (TextView) findViewById(R.id.timings_icon_view);
        cust_review_textview = (TextView) findViewById(R.id.cust_review_textview);
        review_count_view = (TextView) findViewById(R.id.review_count_view);
        reviews_icon_view = (TextView) findViewById(R.id.reviews_icon_view);
        events_type_value = (TextView) findViewById(R.id.events_type_value);
        pager_layout = (RelativeLayout) findViewById(R.id.pager_layout);
        desc_lay = (RelativeLayout) findViewById(R.id.desc_lay);
        dynamic_desc_lay = (RelativeLayout) findViewById(R.id.dynamic_desc_lay);
        cusomer_reviews_layout = (RelativeLayout) findViewById(R.id.cusomer_reviews_layout);

        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        reviews_ratingbar = (RatingBar) findViewById(R.id.reviews_ratingbar);
        // weekdays_spinner = (Spinner) findViewById(R.id.weekdays_spinner);

        but_getdirection = (Button) findViewById(R.id.but_getdirection);
        but_websiteview = (Button) findViewById(R.id.but_websiteview);
        write_review_txt = (Button) findViewById(R.id.write_review_txt);


        if (its_my_own_product) {
            write_review_txt.setVisibility(View.GONE);
        } else {
            write_review_txt.setVisibility(View.VISIBLE);
        }


        traveldate_value = (TextView) findViewById(R.id.traveldate_value);

        available_from_dyn_comm_ofc_value = (TextView) findViewById(R.id.available_from_dyn_comm_ofc_value);

        available_from_dyn_room_sharing_value = (TextView) findViewById(R.id.available_from_dyn_room_sharing_value);


        available_from_dyn_single_fam_value = (TextView) findViewById(R.id.available_from_dyn_single_fam_value);
        edit_single_family_bedrooms_vlue = (TextView) findViewById(R.id.edit_single_family_bedrooms_vlue);
        single_fam_bath_rooms_vlue = (TextView) findViewById(R.id.single_fam_bath_rooms_vlue);
        single_fam_total_sq_value = (TextView) findViewById(R.id.single_fam_total_sq_value);
        single_fam_price_text_value = (TextView) findViewById(R.id.single_fam_price_text_value);
        single_fam_price_type_text_value = (TextView) findViewById(R.id.single_fam_price_type_text_value);


        calling_button = (ImageView) findViewById(R.id.calling_button);

        email_button = (ImageView) findViewById(R.id.email_button);


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

        but_websiteview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (website != null && !website.isEmpty()) {

                    if (!(website.startsWith("http://") || website.startsWith("https://"))) {
                        website = "http://" + website;
                    }

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "No Weblink Provided", Toast.LENGTH_SHORT).show();
                }
            }
        });

        but_getdirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(ServiceDescriptionActivity_Services.this, MapsActivity.class);
                mapIntent.putExtra("latitudeOfMap", Double.parseDouble(latitude));
                mapIntent.putExtra("longitudeOfMap", Double.parseDouble(langitude));
                startActivity(mapIntent);

            }
        });

        write_review_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent review_intent = new Intent(ServiceDescriptionActivity_Services.this, Reviews_Service_Activity.class);
                review_intent.putExtra("service_id", service_id);
                review_intent.putExtra("user_id", UserId_Main);
                review_intent.putExtra("service_name", service_name);
                startActivity(review_intent);
                finish();
            }
        });


        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 550 * NavigationDrawerActivity.curScrenHeit / NavigationDrawerActivity.defaultScrenHeit);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setLayoutParams(rlp);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);


        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.google_map))
                .getMap();


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                // TODO Auto-generated method stub

                Intent myIntent = new Intent(ServiceDescriptionActivity_Services.this, MapDisplyingActivity.class);
                myIntent.putExtra("latitudeOfMap", Double.parseDouble(latitude));
                myIntent.putExtra("longitudeOfMap", Double.parseDouble(langitude));
                ///myIntent.putExtra("area_of_location",owner_area);
                startActivity(myIntent);

            }
        });


        if (its_my_own_product) {
            chat_button.setText("EDIT");
        }


        chat_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String chatoredit = chat_button.getText().toString();

                if (chatoredit.equalsIgnoreCase("EDIT")) {
                    Intent myIntent = new Intent(ServiceDescriptionActivity_Services.this, ServiceUpdationActivity.class);
                    myIntent.putExtra("service_id", service_id);
                    myIntent.putExtra("user_id", UserId_Main);
                    myIntent.putExtra("its_my_own_product", its_my_own_product);
                    myIntent.putExtra("upload_lattitude", latitude);
                    myIntent.putExtra("upload_longitude", langitude);
                    startActivity(myIntent);
                    finish();
                } else if (chatoredit.equalsIgnoreCase("CHAT")) {
                    if (fb_data_pref.getBoolean("login_status", false)) {

					/*if(fb_data_pref.getBoolean("isemailverified", false))
                    {*/

                        //if(!owner_Id.equalsIgnoreCase(UserId_Main))
                        //{
                        String itemId = service_id;
                        Intent intent = new Intent(ServiceDescriptionActivity_Services.this, ChatActivityServices.class);
                        intent.putExtra("chattingFrom", userFname);
                        intent.putExtra("chattingToName", firstname);
                        intent.putExtra("userIdFrom", UserId_Main);
                        intent.putExtra("userIdTo", ownerId);
                        intent.putExtra("itemId", itemId);
                        intent.putExtra("itemName", service_name);
                        intent.putExtra("requestFrom", "description_scr");
                        intent.putExtra("itemImage", /*AppConstants.IMG_BASE_URL +*/ product_images.get(0));
                        startActivity(intent);

                    /*}
                    else {
						Toast.makeText(getApplicationContext(), "Please Verify link from Your Email", Toast.LENGTH_LONG).show();

					}*/

                    } else {

                        //ownScreen=true;
//                    LogingScreenAlert("chatbg");
                        // Toast.makeText(getApplicationContext(), "login to proceed", Toast.LENGTH_SHORT).show();
                        Intent account_intent = new Intent(getApplicationContext(), SocialMediaRegistrationsActivity.class);
                        startActivity(account_intent);
                    }
                }


            }
        });


        desc_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (desc_text.getVisibility() == View.GONE) {

                    icon_view.setBackgroundResource(R.drawable.upwardsarrow);

                    desc_text.setVisibility(View.VISIBLE);

//                    desc_text.setAnimation(R.anim.slide_down);

                } else {
                    icon_view.setBackgroundResource(R.drawable.downarrow);
                    desc_text.setVisibility(View.GONE);
                }
            }
        });

        cusomer_reviews_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mRecyclerView.getVisibility() == View.GONE) {
                    reviews_icon_view.setBackgroundResource(R.drawable.upwardsarrow);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    tvEmptyView.setVisibility(View.GONE);

                    // new ReviewAsyncTask().execute();


                    new GetReviewsAsync().execute();
                } else {
                    reviews_icon_view.setBackgroundResource(R.drawable.downarrow);
                    mRecyclerView.setVisibility(View.GONE);
                    tvEmptyView.setVisibility(View.GONE);
                    loadmore_view.setVisibility(View.GONE);
                }
            }
        });

        dynamic_desc_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dyn_desc_text.getVisibility() == View.GONE) {

                    dyn_icon_view.setBackgroundResource(R.drawable.upwardsarrow);

                    dyn_desc_text.setVisibility(View.VISIBLE);

//                    desc_text.setAnimation(R.anim.slide_down);

                } else {
                    dyn_icon_view.setBackgroundResource(R.drawable.downarrow);
                    dyn_desc_text.setVisibility(View.GONE);
                }
            }
        });

        loadmore_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GetReviewsAsync().execute();
            }
        });

        viewPager.setOnPageChangeListener(this);
        new ServicesDetailsAsyncTask().execute();


    }

    private void loaddata() {

//        for (int i = 1; i <= 5; i++) {

        new GetReviewsAsync().execute();
        // custRevList.add(new CustomerReviewPojo("Student " + i, "androidstudent" + i + "@gmail.com"));

//        }


    }


    public class GetReviewsAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            count = count + 1;
            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = getReviewsRequest(count);
            String service_result = serviceHandler.makeServiceCall(AppConstants.GET_REVIEWS_URL, "POST", requestObject);

            return service_result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            custRevList.clear();
            JSONObject jsObj = null;

            try {
                jsObj = new JSONObject(s);

                if (jsObj.has("reviews")) {
                    JSONObject innerJsonObject = jsObj.getJSONObject("reviews");
                    CustomerReviewPojo customerReviewPojoItems = new CustomerReviewPojo();
                    customerReviewPojoItems.setComment(innerJsonObject.getString("comment"));
                    customerReviewPojoItems.setReview(innerJsonObject.getDouble("rating"));
                    customerReviewPojoItems.setName(innerJsonObject.getString("reviewername"));
                    customerReviewPojoItems.setDate(innerJsonObject.getString("date"));

                    custRevList.add(customerReviewPojoItems);

                }

            } catch (JSONException e) {
                e.printStackTrace();

                try {

                    JSONObject main_jsoObject = new JSONObject(s);


                    if (jsObj.has("reviews")) {

                        JSONArray productsArray = main_jsoObject.getJSONArray("reviews");
                        for (int i = 0; i < productsArray.length(); i++) {

                            JSONObject innerJsonObject = productsArray.getJSONObject(i);
                            CustomerReviewPojo customerReviewPojoItems = new CustomerReviewPojo();
                            customerReviewPojoItems.setComment(innerJsonObject.getString("comment"));
                            customerReviewPojoItems.setReview(innerJsonObject.getDouble("rating"));
                            customerReviewPojoItems.setName(innerJsonObject.getString("reviewername"));
                            customerReviewPojoItems.setDate(innerJsonObject.getString("date"));


                            custRevList.add(customerReviewPojoItems);
                        }

                    }

                } catch (Exception eq) {
                    eq.printStackTrace();

                }


            }
            mRecyclerView.setHasFixedSize(true);


            mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            getNextItems(custRevList);
            mAdapter = new ReviewAdapter(newRevList, mRecyclerView);
            mRecyclerView.setAdapter(mAdapter);


            if (custRevList.isEmpty()) {
                //mRecyclerView.setVisibility(View.GONE);
                tvEmptyView.setVisibility(View.VISIBLE);
                loadmore_view.setVisibility(View.GONE);

            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                tvEmptyView.setVisibility(View.GONE);
                loadmore_view.setVisibility(View.VISIBLE);
            }


            mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    Log.i("LoadMore", "onLoadMore called");
                    //add progress item
                    newRevList.add(null);
                    mAdapter.notifyDataSetChanged();

                    Log.i("LoadMore", "Loading new data... (" + 5 + ") posts");
                    //remove progress item
                    if (newRevList.size() > 0) {
                        newRevList.remove(newRevList.size() - 1);
                    }

                    // getNextItems(custRevList);

                    if (!isLoadingFirstTime) {
                        new GetReviewsAsync1().execute();
                        isLoadingFirstTime = false;
                    }

                    //  mAdapter.notifyDataSetChanged();
                    //add items one by one

                    // mAdapter.setLoaded();
                }
            });


        }

        private void getNextItems(List<CustomerReviewPojo> custRevList) {
            newRevList.addAll(custRevList);
            //custRevList.clear();

        }

        private JSONObject getReviewsRequest(int count) {
            JSONObject requestObject = null;
            try {

                requestObject = new JSONObject();
                requestObject.accumulate("userId", UserId_Main);
                requestObject.accumulate("serviceId", service_id);
                requestObject.accumulate("clickcount", count);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return requestObject;

        }

    }

    public class GetReviewsAsync1 extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            count = count + 1;
            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = getReviewsRequest(count);
            String service_result = serviceHandler.makeServiceCall(AppConstants.GET_REVIEWS_URL, "POST", requestObject);

            return service_result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            custRevList.clear();
            JSONObject jsObj = null;

            try {
                jsObj = new JSONObject(s);

                if (jsObj.has("reviews")) {
                    JSONObject innerJsonObject = jsObj.getJSONObject("reviews");
                    CustomerReviewPojo customerReviewPojoItems = new CustomerReviewPojo();
                    customerReviewPojoItems.setComment(innerJsonObject.getString("comment"));
                    customerReviewPojoItems.setReview(innerJsonObject.getDouble("rating"));
                    customerReviewPojoItems.setName(innerJsonObject.getString("serviceId"));
                    customerReviewPojoItems.setDate(innerJsonObject.getString("userId"));

                    custRevList.add(customerReviewPojoItems);

                }

            } catch (JSONException e) {
                e.printStackTrace();

                try {

                    JSONObject main_jsoObject = new JSONObject(s);


                    if (jsObj.has("reviews")) {

                        JSONArray productsArray = main_jsoObject.getJSONArray("reviews");
                        for (int i = 0; i < productsArray.length(); i++) {

                            JSONObject innerJsonObject = productsArray.getJSONObject(i);
                            CustomerReviewPojo customerReviewPojoItems = new CustomerReviewPojo();
                            customerReviewPojoItems.setComment(innerJsonObject.getString("comment"));
                            customerReviewPojoItems.setReview(innerJsonObject.getDouble("rating"));
                            customerReviewPojoItems.setName(innerJsonObject.getString("serviceId"));
                            customerReviewPojoItems.setDate(innerJsonObject.getString("userId"));


                            custRevList.add(customerReviewPojoItems);
                        }

                    }

                } catch (Exception eq) {
                    eq.printStackTrace();

                }


            }
            if (custRevList.isEmpty()) {
                // mRecyclerView.setVisibility(View.GONE);
                tvEmptyView.setVisibility(View.VISIBLE);
                loadmore_view.setVisibility(View.GONE);

            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                tvEmptyView.setVisibility(View.GONE);
                loadmore_view.setVisibility(View.GONE);
                getNextItems(custRevList);
                mAdapter.notifyDataSetChanged();
                mAdapter.setLoaded();

            }
        }

        private void getNextItems(List<CustomerReviewPojo> custRevList) {
            newRevList.addAll(custRevList);
            custRevList.clear();

        }

        private JSONObject getReviewsRequest(int count) {
            JSONObject requestObject = null;
            try {

                requestObject = new JSONObject();
                requestObject.accumulate(DataKeyValues.USER_USERID, UserId_Main);
                requestObject.accumulate(DataKeyValues.SERVICE_ID, service_id);
                requestObject.accumulate("clickcount", count);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return requestObject;

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.desc_menu_items, menu);
        if (its_my_own_product) {
            if (isFavorite_flag) {
                menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.faverate_img_selected));
            } else {
                menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.feverate_img));
            }
        }
        if (its_my_own_product) {
            menu.findItem(R.id.favourite_menu).setVisible(false);
            menu.findItem(R.id.desc_edit_menu).setVisible(true);
        } else {
            menu.findItem(R.id.favourite_menu).setVisible(true);
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
                PopupMenu popup = new PopupMenu(ServiceDescriptionActivity_Services.this, menuItemView);

                //PopupMenu popup = new PopupMenu(ServiceDescriptionActivity_Services.this, popup_icon);
                popup.getMenuInflater().inflate(R.menu.desc_pop_up_menu, popup.getMenu());

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
                            String shareBody = "Look what I just found on Ask-n-Take!\n" + item_name + "\n\n" + desc_text + "\n" + "http://www.askntake.com/";
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(sharingIntent, "Share with ..."));
                        }

                        if (item.getTitle().toString().equalsIgnoreCase("Report this Service")) {

                            if (!fb_data_pref.getBoolean("login_status", false)) {
                                Intent myIntent = new Intent(ServiceDescriptionActivity_Services.this, SocialMediaRegistrationsActivity.class);
                                startActivity(myIntent);
                            } else {
                                Intent myIntent = new Intent(ServiceDescriptionActivity_Services.this, ReportThisServiceScreenActivity.class);
                                myIntent.putExtra("service_id", service_id);
                                myIntent.putExtra("user_id", UserId_Main);
                                startActivity(myIntent);
                            }

                        }

                        if (item.getItemId() == R.id.desc_delete) {

                            if (!fb_data_pref.getBoolean("login_status", false)) {
                                Intent myIntent = new Intent(ServiceDescriptionActivity_Services.this, SocialMediaRegistrationsActivity.class);
                                startActivity(myIntent);
                            } else {

                                deleteService();
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

                Intent callupdationIntent = new Intent(ServiceDescriptionActivity_Services.this, ServiceUpdationActivity.class);
                callupdationIntent.putExtra("service_id", service_id);
                callupdationIntent.putExtra("user_id", UserId_Main);
                callupdationIntent.putExtra("its_my_own_product", its_my_own_product);
                callupdationIntent.putExtra("upload_lattitude", latitude);
                callupdationIntent.putExtra("upload_longitude", langitude);
                startActivity(callupdationIntent);
                finish();
                return true;

            case R.id.favourite_menu:

                if (fb_data_pref.getBoolean("login_status", false)) {
                    if (isFavorite_flag) {
                        menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.feverate_img));
                        // feverate_img.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.feverate_img));

                        isFavorite_flag = false;
                        //Toast.makeText(getApplicationContext(), "Show Alert", Toast.LENGTH_SHORT).show();
                        ShowFavoritesRemoveAlert();


                        //	new AddItemToFavoriteAsync().execute();

                    } else {
                        menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.faverate_img_selected));



                       /* feverate_img.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.drawable.faverate_img_selected));*/
                        isFavorite_flag = true;
                        new AddServiceToFavoriteAsync("favoriteservice").execute();
                    }

                } else {
                    Intent myIntent = new Intent(ServiceDescriptionActivity_Services.this, SocialMediaRegistrationsActivity.class);
                    startActivity(myIntent);

                }


                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public class ServicesDetailsAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(ServiceDescriptionActivity_Services.this, "", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(AppConstants.getServiceDescriptionUrl(UserId_Main, service_id), "GET", null);
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

                    //timingsPojos = new ArrayList<TimingsPojo>();

                    try {

                        if (itemsObj.has("servicetimings")) {

                            timingsPojo = new TimingsPojo();

                            JSONObject innerTimingsJsonObject = itemsObj.getJSONObject("servicetimings");

                            if (innerTimingsJsonObject.has("day")) {
                                day = innerTimingsJsonObject.getString("day");
                            }

                            if (innerTimingsJsonObject.has("daycount")) {
                                daycount = innerTimingsJsonObject.getString("daycount");
                            }

                            if (innerTimingsJsonObject.has("fromtime")) {
                                fromtime = innerTimingsJsonObject.getString("fromtime") + " " + innerTimingsJsonObject.getString("fromtimeType");
                            }

                            if (innerTimingsJsonObject.has("totime")) {
                                totime = innerTimingsJsonObject.getString("totime") + " " + innerTimingsJsonObject.getString("totimeType");
                            }

                            value = value + day + "  " + fromtime + "  " + "TO" + "   " + totime + "\n";


                            timingsPojo.setWeek_day(day);
                            timingsPojo.setDaycount(daycount);
                            timingsPojo.setFromTime(fromtime);
                            timingsPojo.setToTime(totime);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {

                            if (itemsObj.has("servicetimings")) {
                                JSONArray timingArray = itemsObj.getJSONArray("servicetimings");
                                for (int i = 0; i < timingArray.length(); i++) {

                                    JSONObject innerJsonObject = timingArray.getJSONObject(i);

                                    TimingsPojo pojo = new TimingsPojo();

                                    if (innerJsonObject.has("day")) {
                                        day = innerJsonObject.getString("day");
                                        if (day.equalsIgnoreCase("MONDAY")) {
                                            StringBuffer sb = new StringBuffer(day);
                                            sb = sb.append("        ");
                                            //day=day+"      ";
                                            day = String.valueOf(sb);     //+"   ";
                                        } else if (day.equalsIgnoreCase("TUESDAY")) {
                                            StringBuffer sb = new StringBuffer(day);
                                            sb = sb.append("       ");
                                            day = String.valueOf(sb);
                                            // day="TUESDAY"+"  ";
                                        } else if (day.equalsIgnoreCase("THURSDAY")) {

                                            StringBuffer sb = new StringBuffer(day);
                                            sb = sb.append("    ");
                                            day = String.valueOf(sb);
                                            // day="THURSDAY"+" ";
                                        } else if (day.equalsIgnoreCase("FRIDAY")) {
                                            StringBuffer sb = new StringBuffer(day);
                                            sb = sb.append("           ");
                                            day = String.valueOf(sb);
                                            //day="FRIDAY"+"   ";
                                        } else if (day.equalsIgnoreCase("SATURDAY")) {
                                            StringBuffer sb = new StringBuffer(day);
                                            sb = sb.append("    ");
                                            day = String.valueOf(sb);
                                            //day="SATURDAY"+" ";
                                        } else if (day.equalsIgnoreCase("SUNDAY")) {
                                            StringBuffer sb = new StringBuffer(day);
                                            sb = sb.append("         ");
                                            day = String.valueOf(sb);
                                            //day="SUNDAY"+"   ";
                                        }
                                    }

                                    if (innerJsonObject.has("daycount")) {
                                        daycount = innerJsonObject.getString("daycount");
                                    }

                                    if (innerJsonObject.has("fromtime")) {
                                        fromtime = innerJsonObject.getString("fromtime") + " " + innerJsonObject.getString("fromtimeType");
                                    }

                                    if (innerJsonObject.has("totime")) {
                                        totime = innerJsonObject.getString("totime") + " " + innerJsonObject.getString("totimeType");
                                    }
                                    value = value + day + "  " + fromtime + "  " + "TO" + "   " + totime + "\n" + "\n";

                                    pojo.setWeek_day(day);
                                    pojo.setFromTime(fromtime);
                                    pojo.setToTime(totime);
                                    pojo.setDaycount(daycount);
                                    // timingsPojos.add(timingsPojo);

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
                            eventTypesPojo = new EventTypesPojo();


                            JSONObject eventObj = itemsObj.getJSONObject("event");

                            if (eventObj.has("eventType")) {
                                eventType = eventObj.getString("eventType");
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
                                        eventsServicePojo.setEvent_type(eventType);
                                        eventTypesPojo.setEvent_date(eventDate);
                                        eventTypesPojo.setFromTime(timeFrom);
                                        eventTypesPojo.setToTime(timeTo);

                                        eventValue = "<span>EventType:" + " " + eventsServicePojo.getEvent_type() + "<br>" + "<br>" +
                                                "EventDate: " + " " + eventTypesPojo.getEvent_date() + "<br>" + "<br>" +
                                                "TimeFrom: " + " " + eventTypesPojo.getFromTime() + "<br>" + "<br>" +
                                                "TimeTo: " + " " + eventTypesPojo.getToTime() + "<br></span>";


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

                                            eventsServicePojo.setEvent_type(eventType);
                                            eventTypesPojo.setEvent_date(eventDate);
                                            eventTypesPojo.setFromTime(timeFrom);
                                            eventTypesPojo.setToTime(timeTo);

                                            tempmyleventvalues +=
                                                    "EventDate: " + " " + eventTypesPojo.getEvent_date() + "<br>" + "<br>" +
                                                            "TimeFrom: " + " " + eventTypesPojo.getFromTime() + "<br>" + "<br>" +
                                                            "TimeTo: " + " " + eventTypesPojo.getToTime() + "<br>" + "<br>" + "<br>";

                                        }
                                        eventValue = "EventType:" + " " + eventsServicePojo.getEvent_type() + "<br>" + "<br>" +
                                                "<span>" + tempmyleventvalues + "</span>";

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

                            commercAndOfficeServicePojo.setAvailable_from_date(avalableFrom);
                            commercAndOfficeServicePojo.setNumber_of_rooms(rooms);
                            commercAndOfficeServicePojo.setTotalsq_feet(areainsqft);
                            commercAndOfficeServicePojo.setPrice(price);
                            commercAndOfficeServicePojo.setPrice_type(priceType);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ////////////////////////////////////////////////////

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

                            singleFamilyServicePojo.setAvailable_from_date(avalableFrom);
                            singleFamilyServicePojo.setNo_bedrooms(bedrooms);
                            singleFamilyServicePojo.setNo_bathrooms(bathrooms);
                            singleFamilyServicePojo.setTotalsq_feet(areainsqft);
                            singleFamilyServicePojo.setPrice(price);
                            singleFamilyServicePojo.setPrice_type(priceType);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /////////////////////////////////////////////

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

                            roomSharingServicePojo.setAvailable_from_date(availableFrom);
                            roomSharingServicePojo.setAccommadates(accomodates);
                            roomSharingServicePojo.setNo_bedrooms(bedrooms);
                            roomSharingServicePojo.setNo_bathrooms(bathrooms);
                            roomSharingServicePojo.setGender(gender);
                            roomSharingServicePojo.setPrice(askingprice);
                            roomSharingServicePojo.setPrice_type(priceType);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ////////////////////////////////////////////

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


                            oneTimeRideServicePojo.setTraveldate(travelDate);
                            oneTimeRideServicePojo.setTravel_time(timeOfLeaving);
                            oneTimeRideServicePojo.setDestination_from(destinationFrom);
                            oneTimeRideServicePojo.setDestination_to(destinationTo);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ////////////////////////////////////////////

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
                    if (itemsObj.has("isFavorite")) {

                        if (itemsObj.getString("isFavorite").equalsIgnoreCase("true")) {
                            isFavorite_flag = true;
                        } else {
                            isFavorite_flag = false;
                        }
                    }

                    if (itemsObj.has("subcategory")) {
                        subcategory = itemsObj.getString("subcategory");
                    }

                    if (itemsObj.has("rating")) {
                        rating = itemsObj.getString("rating");
                    }
                    if (itemsObj.has("reviewsCount")) {
                        reviewsCount = itemsObj.getString("reviewsCount");
                    }

                    if (itemsObj.has("contact")) {
                        contact = itemsObj.getString("contact");

                    }

                    if (itemsObj.has("website")) {
                        website = itemsObj.getString("website");
                    }

                    if (itemsObj.has("review")) {
                        review = itemsObj.getBoolean("review");
                    }

                    try {
                        JSONArray product_imagesArray = itemsObj.getJSONArray("images");
                        for (int i = 0; i < product_imagesArray.length(); i++) {
                            product_images.add(i, product_imagesArray.getString(i));
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

                adapter = new ImagePagerAdapter();
                viewPager.setAdapter(adapter);

//                pagerImagesAdapter = new ViewPagerImagesAdapter(ServiceDescriptionActivity_Services.this, product_images);
//                viewPager.setAdapter(pagerImagesAdapter);
                setUiPageViewController();
                viewPager.setCurrentItem(0);

                item_name_value.setText(service_name);
                provider_name_value.setText(providr_name);
                num_fav_count.setText(favoritesCount);
                desc_text.setText(description);
                email_txt_value.setText(" : " + email);
                phone_num_txt_value.setText(" : " + contact);
                if (distance != null && distance_type != null) {
                    distance_txt.setText(distance + " " + distance_type);
                    distance_cal_txt.setText(distance + " " + distance_type);
                } else {
                    distance_txt.setVisibility(View.GONE);
                    distance_cal_txt.setVisibility(View.GONE);
                }

                address_txt.setText(area);
                ratingbar.setRating(Float.parseFloat(rating));
                reviews_ratingbar.setRating(Float.parseFloat(rating));
                review_counting_view.setText(reviewsCount);
                review_count_view.setText(reviewsCount);

                if (!its_my_own_product) {
                    if (isFavorite_flag) {
                        menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.faverate_img_selected));
                    } else {
                        menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.feverate_img));
                    }
                } /*else {
                    if (isFavorite_flag) {
                        menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.faverate_img_selected));
                    } else {
                        menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.feverate_img));
                    }
                }*/

                if (!review) {
                    write_review_txt.setVisibility(View.VISIBLE);
                } else {
                    write_review_txt.setVisibility(View.GONE);
                }


                if (singleFamilyServicePojo != null) {

                    dyn_desc_textview.setText("Single Family Homes");

                    layout_single_family_home_dynamic.setVisibility(View.GONE);
                    layout_rest_services_block.setVisibility(View.GONE);
                    layout_one_time_ride_dynamic.setVisibility(View.GONE);
                    layout_room_sharing_dynamic.setVisibility(View.GONE);
                    layout_commercial_and_office_dynamic.setVisibility(View.GONE);
                    layout_events_service_block_dynamic.setVisibility(View.GONE);

//                    dyn_desc_text.setVisibility(View.VISIBLE);

                    available_from_dyn_single_fam_value.setText(singleFamilyServicePojo.getAvailable_from_date());
                    edit_single_family_bedrooms_vlue.setText(singleFamilyServicePojo.getNo_bedrooms());
                    single_fam_bath_rooms_vlue.setText(singleFamilyServicePojo.getNo_bathrooms());
                    single_fam_total_sq_value.setText(singleFamilyServicePojo.getTotalsq_feet());
                    single_fam_price_text_value.setText(singleFamilyServicePojo.getPrice());
                    single_fam_price_type_text_value.setText(singleFamilyServicePojo.getPrice_type());

                    dyn_desc_text.setText("Avalable From:" + available_from_dyn_single_fam_value.getText().toString() + "\n" + "Bedrooms: " + edit_single_family_bedrooms_vlue.getText().toString() + "\n" + "Bathrooms: " + single_fam_bath_rooms_vlue.getText().toString() + "\n" +
                            "Total Sq: " + single_fam_total_sq_value.getText().toString() + "\n" + "Asking Price: " + single_fam_price_text_value.getText().toString() + " " + single_fam_price_type_text_value.getText().toString());


                }

                if (commercAndOfficeServicePojo != null) {

                    dyn_desc_textview.setText("Commercial and Office Spaces");

                    layout_single_family_home_dynamic.setVisibility(View.GONE);
                    layout_rest_services_block.setVisibility(View.GONE);
                    layout_one_time_ride_dynamic.setVisibility(View.GONE);
                    layout_room_sharing_dynamic.setVisibility(View.GONE);
                    layout_commercial_and_office_dynamic.setVisibility(View.GONE);
                    layout_events_service_block_dynamic.setVisibility(View.GONE);


                    String tempText = "<span>Avalable From:" + " " + commercAndOfficeServicePojo.getAvailable_from_date() + "<br>" +
                            "Rooms:" + " " + commercAndOfficeServicePojo.getNumber_of_rooms() + "<br>" +
                            "Area in Sqft:" + " " + commercAndOfficeServicePojo.getTotalsq_feet() + "<br>" +
                            "Price:" + " " + commercAndOfficeServicePojo.getPrice() + " " + commercAndOfficeServicePojo.getPrice_type() + "<br></span>";

                    available_from_dyn_comm_ofc_value.setText(Html.fromHtml(tempText));
                    dyn_desc_text.setText(available_from_dyn_comm_ofc_value.getText());

                }
                ////////////////////////////////////

                if (roomSharingServicePojo != null) {

                    dyn_desc_textview.setText("Room Sharing");

                    layout_single_family_home_dynamic.setVisibility(View.GONE);
                    layout_rest_services_block.setVisibility(View.GONE);
                    layout_one_time_ride_dynamic.setVisibility(View.GONE);
                    layout_room_sharing_dynamic.setVisibility(View.GONE);
                    layout_commercial_and_office_dynamic.setVisibility(View.GONE);
                    layout_events_service_block_dynamic.setVisibility(View.GONE);


                    String tempText = "<span>Avalable From:" + " " + roomSharingServicePojo.getAvailable_from_date() + "<br>" +
                            "Accomodates:" + " " + roomSharingServicePojo.getAccommadates() + "<br>" +
                            "Bath Rooms:" + " " + roomSharingServicePojo.getNo_bathrooms() + "<br>" +
                            "Bed Rooms:" + " " + roomSharingServicePojo.getNo_bedrooms() + "<br>" +
                            "Gender:" + " " + roomSharingServicePojo.getGender() + "<br>" +
                            "Price:" + " " + roomSharingServicePojo.getPrice() + " " + roomSharingServicePojo.getPrice_type() + "<br></span>";

                    available_from_dyn_room_sharing_value.setText(Html.fromHtml(tempText));
                    dyn_desc_text.setText(available_from_dyn_room_sharing_value.getText());
                }

                /////////////////////////////////

                if (timingsPojo != null) {

                    dyn_desc_textview.setText("Timings");

                    layout_single_family_home_dynamic.setVisibility(View.GONE);
                    layout_rest_services_block.setVisibility(View.GONE);
                    layout_one_time_ride_dynamic.setVisibility(View.GONE);
                    layout_room_sharing_dynamic.setVisibility(View.GONE);
                    layout_commercial_and_office_dynamic.setVisibility(View.GONE);
                    layout_events_service_block_dynamic.setVisibility(View.GONE);

                    //rest_serv_tv_day0.setText(timingsPojo.getWeek_day()+"  "+timingsPojo.getFromTime()+"   "+"To"+"   "+timingsPojo.getToTime());

                    rest_serv_tv_day0.setText(value);
                    dyn_desc_text.setText(rest_serv_tv_day0.getText());


                }

                if (oneTimeRideServicePojo != null) {

                    dyn_desc_textview.setText("One Time Rides");

                    layout_single_family_home_dynamic.setVisibility(View.GONE);
                    layout_rest_services_block.setVisibility(View.GONE);
                    layout_one_time_ride_dynamic.setVisibility(View.GONE);
                    layout_room_sharing_dynamic.setVisibility(View.GONE);
                    layout_commercial_and_office_dynamic.setVisibility(View.GONE);
                    layout_events_service_block_dynamic.setVisibility(View.GONE);


                    String tempText = "<span>Travel date:" + " " + oneTimeRideServicePojo.getTraveldate() + "<br>" +
                            "Destination From: " + " " + oneTimeRideServicePojo.getDestination_from() + "<br>" +
                            "Destination To: " + " " + oneTimeRideServicePojo.getDestination_to() + "<br>" +
                            "Time of leaving: " + " " + oneTimeRideServicePojo.getTravel_time() + "<br></span>";

                    traveldate_value.setText(Html.fromHtml(tempText));
                    dyn_desc_text.setText(traveldate_value.getText());


                }


                ///////////////////////////////////////

                if (eventsServicePojo != null) {

                    dyn_desc_textview.setText("Event Timings");

                    layout_single_family_home_dynamic.setVisibility(View.GONE);
                    layout_rest_services_block.setVisibility(View.GONE);
                    layout_one_time_ride_dynamic.setVisibility(View.GONE);
                    layout_room_sharing_dynamic.setVisibility(View.GONE);
                    layout_commercial_and_office_dynamic.setVisibility(View.GONE);
                    layout_events_service_block_dynamic.setVisibility(View.GONE);


                    //rest_serv_tv_day0.setText(timingsPojo.getWeek_day()+"  "+timingsPojo.getFromTime()+"   "+"To"+"   "+timingsPojo.getToTime());

                    events_type_value.setText(Html.fromHtml(eventValue));
                    dyn_desc_text.setText(events_type_value.getText());

                }
                /////////////////////////////////////
                //String bedroom_value=singleFamilyServicePojo.getNo_bedrooms();

                //Glide.with(getApplicationContext()).load(owner_profile_img).into(seller_img);

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
    }

    private void setUiPageViewController() {

        //dotsCount = pagerImagesAdapter.getCount();
        dotsCount = product_images.size();            //2;//ImagePagerAdapter.getCount();

        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void ShowFavoritesRemoveAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ServiceDescriptionActivity_Services.this);
        alertDialog.setMessage(R.string.delete_alert);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //new removeFromFaverotes().execute();
                // feverate_img.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.feverate_img));
                menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.feverate_img));
                new RemoveServiceToFavoriteAsync("removefavoriteservice").execute();


            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                /*feverate_img.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.faverate_img_selected));*/

                menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.faverate_img_selected));
                isFavorite_flag = true;
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    private class AddServiceToFavoriteAsync extends AsyncTask<String, Void, String> {
        String servicetype;

        public AddServiceToFavoriteAsync(String servicetype) {
            this.servicetype = servicetype;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            ServiceHandler serviceHandler = new ServiceHandler();
            // JSONObject requestObject = getJSONObject();
            String jsonStr = serviceHandler.makeServiceCall(AppConstants.addServiceToFavoriteList(UserId_Main, service_id, servicetype), "POST", null);

            return jsonStr;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {

                String message;

                try {
                    JSONObject mainJsonObj = new JSONObject(result);

                    if (mainJsonObj.has("status")) {
                        message = mainJsonObj.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        message = mainJsonObj.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private JSONObject getJSONObject() {

        String req_status = "";


        JSONObject requestObject = null;
        try {
            if (favoritesStatus != null) {
                JSONObject fav_Obj = new JSONObject(favoritesStatus);

                if (fav_Obj.has("status")) {
                    if (fav_Obj.getString("status").equalsIgnoreCase("true")) {
                        req_status = "success";
                    } else {
                        req_status = "fail";
                    }
                }
                if (fav_Obj.has("message")) {
                    reqMessage = fav_Obj.getString("message");
                } else {
                    reqMessage = "";
                }

            } else {
                req_status = "fail";
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return requestObject;
    }

    private class RemoveServiceToFavoriteAsync extends AsyncTask<String, Void, String> {
        String servicetype;

        public RemoveServiceToFavoriteAsync(String servicetype) {
            this.servicetype = servicetype;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            ServiceHandler serviceHandler = new ServiceHandler();
            // JSONObject requestObject = getJSONObject();
            String jsonStr = serviceHandler.makeServiceCall(AppConstants.removeServiceToFavoriteList(UserId_Main, service_id, servicetype), "POST", null);
            return jsonStr;


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if (result != null) {


                String message;

                try {
                    JSONObject mainJsonObj = new JSONObject(result);

                    if (mainJsonObj.has("status")) {
                        message = mainJsonObj.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        message = mainJsonObj.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utility.Call_Request:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + contact));

                    if (ActivityCompat.checkSelfPermission(ServiceDescriptionActivity_Services.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

        boolean result = Utility.checkCallPermission(ServiceDescriptionActivity_Services.this);
        if (result) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + contact));

            if (ActivityCompat.checkSelfPermission(ServiceDescriptionActivity_Services.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    /*public class ReviewAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = getReviewsRequest();
            String service_result = serviceHandler.makeServiceCall(AppConstants.GET_REVIEWS_URL, "POST", requestObject);
            return service_result;

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
*/

    private JSONObject getJsonObject() {
        JSONObject requestObject = null;
        try {

            requestObject = new JSONObject();
            requestObject.accumulate(DataKeyValues.USER_USERID, UserId_Main);
            requestObject.accumulate(DataKeyValues.SERVICE_ID, service_id);
//            requestObject.accumulate("comment", comments_text.getText().toString());
//            requestObject.accumulate("rating", rating_value);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject;
    }

    protected void sendEmail()

    {

        Intent emailIntent = new Intent(ServiceDescriptionActivity_Services.this, EmailActivity.class);
        emailIntent.putExtra("sen_email_to", email);
        startActivity(emailIntent);
    }

    public void deleteService() {
        //deleteService

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ServiceDescriptionActivity_Services.this);
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
            mProgressDialog = ProgressDialog.show(ServiceDescriptionActivity_Services.this, "", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(AppConstants.deleteService(UserId_Main, service_id), "GET", null);
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

                        Intent homescreenIntent = new Intent(ServiceDescriptionActivity_Services.this, NavigationDrawerActivity.class);
                        startActivity(homescreenIntent);
                        finish();
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

    private class ImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return product_images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = ServiceDescriptionActivity_Services.this;

            final String names[] = product_images.toArray(new String[0]);

            ImageView imageView = new ImageView(context);

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setTag(position);


            Picasso.with(getApplicationContext()).load(/*AppUtils.IMG_BASE_URL+*/names[position]).placeholder(R.drawable.progress_animation).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    //Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                    Intent myintent = new Intent(ServiceDescriptionActivity_Services.this, ItemFullImagesActivity.class);
                    myintent.putExtra("itemImagesArray", names);
                    myintent.putExtra("clickedItemPosition", Integer.parseInt(v.getTag().toString()));
                    myintent.putExtra("sizeOfItemArray", names.length);
                    myintent.putExtra("mImagesDot", dotsCount);
                    startActivity(myintent);

                }
            });
            /*	Picasso.with(getApplicationContext()).load(AppUtils.IMG_BASE_URL+itemImages[position]).placeholder(R.drawable.progress_animation).into(imageView,new ImageLoadedCallback(progressBar_place_holder) {
                @Override
                public void onSuccess() {
                    if (this.progressBar != null) {
                        this.progressBar.setVisibility(View.GONE);
                     }
                }
          });*/
            ((ViewPager) container).addView(imageView, 0);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);

        }

    }


}
