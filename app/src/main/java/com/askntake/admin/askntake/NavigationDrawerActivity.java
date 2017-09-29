package com.askntake.admin.askntake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.json.JSONObject;

import Adapters.PagerFragmentAdapter;
import AppUtils.AppConstants;
import AppUtils.ConnectionDetector;
import AppUtils.DataKeyValues;
import AppUtils.ServiceHandler;
import AppUtils.Utility;
import Fragments.ItemsDisplayingFragment_BuyAndSell;
import Fragments.ServicesFragment;
import Pojo.FilterPojo;
import Pojo.ServicesFiteringDataPojo;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    ViewPager tabs_viewpager;
    TabLayout tabs_layout;
    BottomNavigationView bottomNavigation;
    int selected_tab_position;
    SharedPreferences login_preferenes;
    public static int defaultScrenWidth = 800, defaultScrenHeit = 1280;
    public static int curScrenHeit;
    public static int curScrenWidth;
    LinearLayout lnr_layout_navigation, location_setting_linear, filtering_linear_lay;
    FilterPojo filterPojo;
    ImageView header_image;
    DrawerLayout drawer;
    ConnectionDetector internetConnection;
    ServicesFiteringDataPojo servicesFiteringDataPojo;
    boolean isFirstTime;
    String profile_location;
    boolean filtering_marker;
    TextView filter_text;
    ImageView filtering_image;

    int unread_messages_count = 0;
    MenuItem element;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login_preferenes = getSharedPreferences(DataKeyValues.USER_DATA_PREF, MODE_PRIVATE);

        login_preferenes = AppConstants.preferencesData(getApplicationContext());
        String user_main_id = login_preferenes.getString(DataKeyValues.OWNER_ID, null);
        //   login_preferenes = getApplicationContext().getSharedPreferences("FbData", MODE_PRIVATE);
        //  String user_main_id=login_preferenes.getString("registrationID", null);
        if (user_main_id != null) {
            new getReadCount(user_main_id).execute();
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        curScrenHeit = displaymetrics.heightPixels;
        curScrenWidth = displaymetrics.widthPixels;
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //swipe tabs
        tabs_viewpager = (ViewPager) findViewById(R.id.viewpager);
        filter_text = (TextView) findViewById(R.id.filter_text);
        filtering_image = (ImageView) findViewById(R.id.filtering_image);

        filtering_linear_lay = (LinearLayout) findViewById(R.id.filtering_linear_lay);

        //get filer data
        filterPojo = (FilterPojo) getIntent().getSerializableExtra("filterdata");
        profile_location = getIntent().getStringExtra("profile_location");

        final SharedPreferences filterpreferences = getSharedPreferences("Filter_data", MODE_PRIVATE);
        filtering_marker = filterpreferences.getBoolean("filtering_marker", false);

        if (filtering_marker) {
            filtering_linear_lay.setVisibility(View.VISIBLE);

        } else {
            filtering_linear_lay.setVisibility(View.GONE);
        }
        //registerReceiver(broadcastReceiver, new IntentFilter("SERVICE_MESSAGE_RECEIVED"));
        registerReceiver(broadcastReceiver, new IntentFilter("CHAT_MESSAGE_RECEIVED_IN_HISTORY"));


        filtering_linear_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor pref = getSharedPreferences("Filter_data", 0).edit();
                pref.clear();
                pref.commit();
                filtering_linear_lay.setVisibility(View.GONE);

                SharedPreferences user_LocationData = AppConstants.preferencesData(getApplicationContext());
                SharedPreferences.Editor LocationData_editor = user_LocationData.edit();

                LocationData_editor.putString(DataKeyValues.FILTERLATTITUDE, null);
                LocationData_editor.putString(DataKeyValues.FILTERLONGITUDE, null);

                LocationData_editor.putString(DataKeyValues.SET_LOC_LATTITUDE, null);
                LocationData_editor.putString(DataKeyValues.SET_LOC_LONGITUDE, null);

                LocationData_editor.apply();
                LocationData_editor.commit();

                setupViewPager(tabs_viewpager, false);

                //serviceFilteringActivity.setDefaulters();
                //new SaveFiltersAsynchTask().execute();


            }
        });

        servicesFiteringDataPojo = (ServicesFiteringDataPojo) getIntent().getSerializableExtra("filterPojoObj");
        isFirstTime = getIntent().getBooleanExtra("isFirstTime", true);

        if (isFirstTime) {
            setupViewPager(tabs_viewpager, false);
            filtering_linear_lay.setVisibility(View.GONE);
        } else {
            if (!filtering_marker) {
                setupViewPager(tabs_viewpager, true);
            } else {
                setupViewPager(tabs_viewpager, filtering_marker);
            }

        }

        tabs_layout = (TabLayout) findViewById(R.id.tabs);
        tabs_layout.setupWithViewPager(tabs_viewpager);
        lnr_layout_navigation = (LinearLayout) findViewById(R.id.lnr_layout_navigation);
        location_setting_linear = (LinearLayout) findViewById(R.id.location_setting_linear);

        internetConnection = new ConnectionDetector(getApplicationContext());
        location_setting_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent location_intent = new Intent(getApplicationContext(), LocationSettingsActivity.class);
                location_intent.putExtra("requestFrom", "set_location");
                startActivity(location_intent);
                finish();
            }
        });

        //end swipe tabs

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setVisibility(View.VISIBLE);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleBottomNavigationItemSelected(item);
                return true;
            }
        });
        tabs_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selected_tab_position = tab.getPosition();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menuNav = navigationView.getMenu();
        element = menuNav.findItem(R.id.nav_chat);
        View header = navigationView.getHeaderView(0);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(250 * curScrenWidth / defaultScrenWidth, 250 * curScrenHeit / defaultScrenHeit);
        llp.setMargins(20, 0, 0, 0);
        header_image = (ImageView) header.findViewById(R.id.imageView);
        header_image.setLayoutParams(llp);

        TextView tv_user_name = (TextView) header.findViewById(R.id.tv_user_name);

        llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(50, 0, 0, 0);
        llp.gravity = Gravity.CENTER_VERTICAL;
        tv_user_name.setLayoutParams(llp);


        TextView login_status_text = (TextView) header.findViewById(R.id.login_status_text);


        final SharedPreferences sharedPreferences = getSharedPreferences(DataKeyValues.USER_DATA_PREF, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {


            tv_user_name.setText(sharedPreferences.getString("fb_username", null));
            Glide.with(getApplicationContext()).load(AppConstants.IMG_BASE_URL+sharedPreferences.getString("image", null)).asBitmap().centerCrop().placeholder(R.mipmap.not_login_img).into(new BitmapImageViewTarget(header_image) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    header_image.setImageDrawable(circularBitmapDrawable);
                }
            });
            login_status_text.setVisibility(View.GONE);

        } else {
            Glide.with(getApplicationContext()).load(R.mipmap.not_login_img).into(header_image);

            login_status_text.setVisibility(View.VISIBLE);
        }

        header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (sharedPreferences.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {
                    Intent account_intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                    startActivity(account_intent);
                    drawer.closeDrawers();
                } else {
                    Intent account_intent = new Intent(getApplicationContext(), SocialMediaRegistrationsActivity.class);
                    startActivity(account_intent);
                    drawer.closeDrawers();
                }
            }
        });


      /*  lnr_layout_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent account_intent=new Intent(getApplicationContext(),AccountEditActivity.class);
                startActivity(account_intent);
            }
        });*/


    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        SharedPreferences user_LocationData = AppConstants.preferencesData(getApplicationContext());
        SharedPreferences.Editor LocationData_editor = user_LocationData.edit();

        LocationData_editor.putString(DataKeyValues.SET_LOC_LATTITUDE, null);
        LocationData_editor.putString(DataKeyValues.SET_LOC_LONGITUDE, null);

        LocationData_editor.putString(DataKeyValues.FILTERLATTITUDE, null);
        LocationData_editor.putString(DataKeyValues.FILTERLONGITUDE, null);

        LocationData_editor.apply();
        LocationData_editor.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }


    public FilterPojo getFilterData() {

        return filterPojo;
    }

    public String singleSelectedCategory() {

        return getIntent().getStringExtra("selected_single_category");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {

            if (selected_tab_position == 0) {

                if (login_preferenes.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {
                    Intent filter_intent = new Intent(getApplicationContext(), ServiceFilteringActivity.class);
                    startActivity(filter_intent);
                    // finish();
                } else {
                    Intent account_intent = new Intent(getApplicationContext(), SocialMediaRegistrationsActivity.class);
                    startActivity(account_intent);
                    //  finish();
                }
               /* Intent filter_intent = new Intent(getApplicationContext(), ServiceFilteringActivity.class);
                startActivity(filter_intent);
*/
            } else if (selected_tab_position == 1) {
                Intent filter_intent = new Intent(getApplicationContext(), BuyAndSellFileringActivity.class);
                startActivity(filter_intent);
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_camera) {

            Intent my_profile_intent = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
            startActivity(my_profile_intent);
            finish();
        }
/*        else if (id == R.id.nav_gallery) {



            if (login_preferenes.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {
                Intent addServiceIntent = new Intent(getApplicationContext(), SelectionCategoryActivity.class);
                startActivity(addServiceIntent);
            } else {
                Intent registrationActivity = new Intent(getApplicationContext(), SocialMediaRegistrationsActivity.class);
                startActivity(registrationActivity);
            }


            *//*if (login_preferenes.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {
                Intent addServiceIntent = new Intent(getApplicationContext(), AddServiceActivity.class);
                startActivity(addServiceIntent);
            } else {
                Intent registrationActivity = new Intent(getApplicationContext(), SocialMediaRegistrationsActivity.class);
                startActivity(registrationActivity);
            }*//*


            drawer.closeDrawers();

        }*/
        else if (id == R.id.nav_chat) {

            if (login_preferenes.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {
                Intent messagesIntent = new Intent(getApplicationContext(), MessagesActivity.class);
                startActivity(messagesIntent);
            } else {
                Intent registrationActivity = new Intent(getApplicationContext(), SocialMediaRegistrationsActivity.class);
                startActivity(registrationActivity);
            }
            drawer.closeDrawers();

        } else if (id == R.id.nav_myprofile) {

            if (login_preferenes.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {
                Intent my_profile_intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                startActivity(my_profile_intent);
            } else {
                Intent registrationActivity = new Intent(getApplicationContext(), SocialMediaRegistrationsActivity.class);
                startActivity(registrationActivity);
            }
            drawer.closeDrawers();

        } /*else if (id == R.id.nav_favorites) {

            *//*Intent contactus_intent = new Intent(getApplicationContext(), CategoriesActivity.class);
            contactus_intent.putExtra("filterdata", filterPojo);
            startActivity(contactus_intent);*//*


            drawer.closeDrawers();


        }*/ else if (id == R.id.nav_contactus) {
            Intent contactus_intent = new Intent(getApplicationContext(), ContactUsActivity.class);
            startActivity(contactus_intent);
            drawer.closeDrawers();

        } else if (id == R.id.nav_help) {
            Intent help_intent = new Intent(getApplicationContext(), HelpActivity.class);
            startActivity(help_intent);
            drawer.closeDrawers();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //other methods

    //set tabs method
    private void setupViewPager(ViewPager viewPager, boolean isServiceFilter) {
        PagerFragmentAdapter adapter = new PagerFragmentAdapter(getSupportFragmentManager());
        Bundle args = new Bundle();
      /*  ServicesFragment servicesFragment=new ServicesFragment();

        Bundle args = new Bundle();
        args.putSerializable("filterdata", servicesFiteringDataPojo);
        servicesFragment.setArguments(args); */


        ItemsDisplayingFragment_BuyAndSell buyAndSellFragment = new ItemsDisplayingFragment_BuyAndSell();
        buyAndSellFragment.setArguments(args);

        adapter.addFragment(ServicesFragment.newInstance(servicesFiteringDataPojo, profile_location, isServiceFilter), "SERVICES");
        adapter.addFragment(buyAndSellFragment, "BUY & SELL");
//        adapter.addFragment(new StroresFragment(), "STORES");
//        adapter.addFragment(new DealsFragment(), "DEALS");
        viewPager.setAdapter(adapter);
    }

    private void handleBottomNavigationItemSelected(MenuItem item) {


        if (login_preferenes.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {

            switch (item.getItemId()) {
                case R.id.action_add:
                    if (login_preferenes.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {
                        Intent addServiceIntent = new Intent(getApplicationContext(), SelectionCategoryActivity.class);
                        startActivity(addServiceIntent);
                    } else {
                        Intent registrationActivity = new Intent(getApplicationContext(), SocialMediaRegistrationsActivity.class);
                        startActivity(registrationActivity);
                    }
                    break;
                case R.id.action_messages:

                    Intent messagesIntent = new Intent(getApplicationContext(), MessagesActivity.class);
                    startActivity(messagesIntent);
                    break;
                case R.id.action_categories:

                    Intent contactus_intent = new Intent(getApplicationContext(), CategoriesActivity.class);
                    contactus_intent.putExtra("filterdata", filterPojo);
                    startActivity(contactus_intent);

                    break;
            }

        } else {
            Intent registrationActivity = new Intent(getApplicationContext(), SocialMediaRegistrationsActivity.class);
            startActivity(registrationActivity);
        }


    }

    @SuppressWarnings("deprecation")
    @Override
    public void onResume() {
        super.onResume();

        registerReceiver(broadcastReceiver, new IntentFilter("CHAT_MESSAGE_RECEIVED_IN_HISTORY"));
        //registerReceiver(broadcastReceiver, new IntentFilter("SERVICE_MESSAGE_RECEIVED"));


    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            int unreadcount = 0;

            if (b.getString("unreadcount") != null) {
                unreadcount = Integer.parseInt(b.getString("unreadcount"));
                Log.i("inread messages: ", "" + unreadcount);
               /* login_preferenes = getApplicationContext().getSharedPreferences("FbData", MODE_PRIVATE);
                fb_editor = login_preferenes.edit();
                fb_editor.putInt(DataStoring.UNREAD_MSG_COUNT, unreadcount);
                fb_editor.commit();*/
            }

          /*  listAdapter = new NavigationExpendAdapter(getApplicationContext(), listDataHeader, listDataChild,navigation_images,unreadcount);
            navList.setAdapter(listAdapter);*/

        }


    };

    @Override
    protected void onRestart() {
        super.onRestart();
        login_preferenes = AppConstants.preferencesData(getApplicationContext());
        String user_main_id = login_preferenes.getString(DataKeyValues.OWNER_ID, null);
        //   login_preferenes = getApplicationContext().getSharedPreferences("FbData", MODE_PRIVATE);
        //  String user_main_id=login_preferenes.getString("registrationID", null);
        if (user_main_id != null) {
            new getReadCount(user_main_id).execute();
        }
    }

    public class getReadCount extends AsyncTask<String, Void, String> {

        String user_maidn_id = null;

        public getReadCount(String userId) {

            this.user_maidn_id = userId;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            ServiceHandler sh = new ServiceHandler();
            String readcount = "exception";
            if (user_maidn_id != null) {
                String jsonResponce = sh.makeServiceCall(AppConstants.readCountResponce(user_maidn_id), "GET", null);
                try {
                    if (jsonResponce != null) {
                        JSONObject read_Responce = new JSONObject(jsonResponce);
                        readcount = read_Responce.getString("message");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    readcount = "exception";
                }
            } else {
                readcount = "exception";
            }

            return readcount;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (!result.equalsIgnoreCase("exception")) {

                unread_messages_count = Integer.parseInt(result);
                if (unread_messages_count != 0) {
                    // ShortcutBadger.applyCount(getApplicationContext(), unread_messages_count);
                    String counter = Integer.toString(unread_messages_count);
                   /* String before = element.getTitle().toString();
                    String s = before + "   "+counter+" ";
                    SpannableString sColored = new SpannableString( s );

                    sColored.setSpan(new BackgroundColorSpan( Color.RED ), s.length()-(counter.length()+2), s.length(), 0);
                    sColored.setSpan(new ForegroundColorSpan( Color.WHITE ), s.length()-(counter.length()+2), s.length(), 0);

                    element.setTitle(sColored);*/
                    setCounterToOption(counter);
                } else {
                    removeCounterToOption();
                }
               /* else{
                    ShortcutBadger.removeCount(getApplicationContext());
                }*/

            }

        }

    }

    public void setCounterToOption(String count) {

        String before = element.getTitle().toString();
        boolean flag = true;
        before = before.trim();
        char lastChar = before.charAt(before.length() - 1);
        while (Character.isDigit(lastChar)) {
            before = before.substring(0, before.length() - 1);
            before = before.trim();
            lastChar = before.charAt(before.length() - 1);
        }

        String s = before + "   " + count + " ";
        SpannableString sColored = new SpannableString(s);
        sColored.setSpan(new BackgroundColorSpan(Color.RED), s.length() - (count.length() + 2), s.length(), 0);
        sColored.setSpan(new ForegroundColorSpan(Color.WHITE), s.length() - (count.length() + 2), s.length(), 0);

        element.setTitle(sColored);
    }

    public void removeCounterToOption() {
        String before = element.getTitle().toString();
        // String s = before;
        //SpannableString sColored = new SpannableString( s );

        //sColored.setSpan(new BackgroundColorSpan( Color.RED ), s.length()-(count.length()+2), s.length(), 0);
        //sColored.setSpan(new ForegroundColorSpan( Color.WHITE ), s.length()-(count.length()+2), s.length(), 0);

        element.setTitle(before);
    }
}
