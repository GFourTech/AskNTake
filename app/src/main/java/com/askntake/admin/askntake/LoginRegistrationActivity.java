package com.askntake.admin.askntake;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import Adapters.PagerFragmentAdapter;
import Fragments.AddServiceFragment;
import Fragments.LoginFragment;
import Fragments.RegistrationFragment;
import Fragments.ServicesFragment;

/**
 * Created by admin on 3/28/2017.
 */

public class LoginRegistrationActivity extends AppCompatActivity {
    ViewPager tabs_viewpager;
    TabLayout tabs_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        boolean login_for = getIntent().getBooleanExtra("for_login", false);
        tabs_viewpager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(tabs_viewpager);
        if (!login_for) {
            tabs_viewpager.setCurrentItem(1);
        }


        tabs_layout = (TabLayout) findViewById(R.id.tabs);
        tabs_layout.setupWithViewPager(tabs_viewpager);


    }

    public void changeRegistrationTabToLogin() {
        tabs_viewpager.setCurrentItem(0);
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerFragmentAdapter adapter = new PagerFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginFragment(), "Login");
        adapter.addFragment(new RegistrationFragment(), "Registration");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}