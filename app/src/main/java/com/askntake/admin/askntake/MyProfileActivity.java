package com.askntake.admin.askntake;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import AppUtils.AppConstants;
import AppUtils.DataKeyValues;

/**
 * Created by admin on 3/29/2017.
 */

public class MyProfileActivity extends AppCompatActivity {

    Button button_my_services, button_my_buy_and_sell, button_my_service_requests, button_my_deals;
    ImageView iv_profile_pic;
    TextView tv_name_of_user, tv_location_of_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button_my_services = (Button) findViewById(R.id.button_my_services);
        button_my_buy_and_sell = (Button) findViewById(R.id.button_my_buy_and_sell);
        button_my_service_requests = (Button) findViewById(R.id.button_my_service_requests);
        button_my_deals = (Button) findViewById(R.id.button_my_deals);

        tv_name_of_user = (TextView) findViewById(R.id.tv_name_of_user);
        tv_location_of_user = (TextView) findViewById(R.id.tv_location_of_user);
        iv_profile_pic = (ImageView) findViewById(R.id.iv_profile_pic);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(200 * NavigationDrawerActivity.curScrenWidth / NavigationDrawerActivity.defaultScrenWidth
                , 200 * NavigationDrawerActivity.curScrenHeit / NavigationDrawerActivity.defaultScrenHeit);
        llp.gravity = Gravity.CENTER;
        llp.setMargins(0, 20 * NavigationDrawerActivity.curScrenHeit / NavigationDrawerActivity.defaultScrenHeit, 0, 0);
        iv_profile_pic.setLayoutParams(llp);
        button_my_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myServicesIntent = new Intent(getApplicationContext(), MyServicesActivity.class);
                startActivity(myServicesIntent);
            }
        });
        button_my_buy_and_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myServicesIntent = new Intent(getApplicationContext(), MyBuyAndSellActivity.class);
                startActivity(myServicesIntent);
            }
        });
        button_my_service_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myServicesIntent = new Intent(getApplicationContext(), MyServiceRequestActivity.class);
                startActivity(myServicesIntent);
            }
        });
        button_my_deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myServicesIntent = new Intent(getApplicationContext(), MyDealsActivity.class);
                startActivity(myServicesIntent);
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences(DataKeyValues.USER_DATA_PREF, MODE_PRIVATE);

        if (sharedPreferences.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {

            String user_name = sharedPreferences.getString("fb_username", null);
            String pic_url = sharedPreferences.getString("image", null);

            tv_name_of_user.setText(user_name);
            Glide.with(getApplicationContext()).load(AppConstants.IMG_BASE_URL+sharedPreferences.getString("image", null)).asBitmap().centerCrop().placeholder(R.mipmap.not_login_img).into(new BitmapImageViewTarget(iv_profile_pic) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    iv_profile_pic.setImageDrawable(circularBitmapDrawable);
                }
            });

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if (id == R.id.edi_my_account) {

            Intent filter_intent = new Intent(getApplicationContext(), AccountEditActivity.class);
            startActivity(filter_intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account_edit_settings, menu);
        return true;
    }


}
