package com.askntake.admin.askntake;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import Adapters.PagerFragmentAdapter;
import Fragments.AddServiceFragment;
import Fragments.AddServicesRequestFragment;
import AppUtils.ConnectionDetector;

/**
 * Created by admin on 3/27/2017.
 */

public class AddServiceActivity extends AppCompatActivity {

    ViewPager tabs_viewpager;
    TabLayout tabs_layout;
    BottomNavigationView bottomNavigation;
    ConnectionDetector internetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        internetConnection=new ConnectionDetector(getApplicationContext());

        //swipe tabs
        tabs_viewpager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(tabs_viewpager);

        tabs_layout = (TabLayout) findViewById(R.id.tabs);
        tabs_layout.setupWithViewPager(tabs_viewpager);
        bottomNavigation =
                (BottomNavigationView) findViewById(R.id.bottom_navigation);



        bottomNavigation.setVisibility(View.GONE);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                handleBottomNavigationItemSelected(item);
                return true;
            }
        });



    }


    //set tabs method
    private void setupViewPager(ViewPager viewPager) {
        PagerFragmentAdapter adapter = new PagerFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new AddServiceFragment(), "Service");
        adapter.addFragment(new AddServicesRequestFragment(), "Service Request");
        viewPager.setAdapter(adapter);
    }

    private void handleBottomNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                break;
            case R.id.action_messages:

                //move to chat
                break;
            case R.id.action_categories:

                Intent contactus_intent = new Intent(getApplicationContext(), SearchCategoriesActivity.class);
               // contactus_intent.putExtra("filterdata", filterPojo);
                startActivity(contactus_intent);
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            ShowAlert();
        }
        return super.onOptionsItemSelected(item);
    }
    private void ShowAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(AddServiceActivity.this);
        if (dialog != null) {
            dialog.setTitle("Hands up!");

        }
        dialog.setCancelable(false);
        dialog.setMessage("Abandon lisiting?");
        dialog.setPositiveButton("ABANDON", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setNegativeButton("KEEP LISTING", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

            }
        });


        final AlertDialog alert = dialog.create();
        alert.show();

    }

   /* @Override
    public void onBackPressed() {


        ShowAlert();
        super.onBackPressed();
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return super.onKeyDown(keyCode, event);
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            ShowAlert();
            return true; // shows you consumed the event with your implementation
        }else
            return false;
    }
}
