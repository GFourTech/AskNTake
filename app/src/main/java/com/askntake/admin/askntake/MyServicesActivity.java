package com.askntake.admin.askntake;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import Adapters.PagerFragmentAdapter;
import Fragments.MyFavoriteServicesFragment;
import Fragments.MyServicesFragment;

/**
 * Created by admin on 3/29/2017.
 */

public class MyServicesActivity extends AppCompatActivity {


    ViewPager tabs_viewpager;
    TabLayout tabs_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myservices);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabs_viewpager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(tabs_viewpager);

        tabs_layout = (TabLayout) findViewById(R.id.tabs);
        tabs_layout.setupWithViewPager(tabs_viewpager);
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerFragmentAdapter adapter = new PagerFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyServicesFragment(), "MY SERVICES");
        adapter.addFragment(new MyFavoriteServicesFragment(), "FAVORITES");
        viewPager.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
