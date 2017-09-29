package com.askntake.admin.askntake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import AppUtils.DataKeyValues;

/**
 * Created by admin on 3/29/2017.
 */

public class CategoriesActivity extends AppCompatActivity {
    Button buy_sell_category_btn, services_category_btn, stores_category_btn, deals_category_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        buy_sell_category_btn = (Button) findViewById(R.id.buy_sell_category_btn);
        services_category_btn = (Button) findViewById(R.id.services_category_btn);
        stores_category_btn = (Button) findViewById(R.id.stores_category_btn);
        deals_category_btn = (Button) findViewById(R.id.deals_category_btn);

        buy_sell_category_btn.setVisibility(View.GONE);
        stores_category_btn.setVisibility(View.GONE);
        deals_category_btn.setVisibility(View.GONE);


        final SharedPreferences login_data_pref = getSharedPreferences(DataKeyValues.USER_DATA_PREF, Context.MODE_PRIVATE);


        buy_sell_category_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent category_intent = new Intent(getApplicationContext(), SearchCategoriesActivity.class);
                startActivity(category_intent);

            }
        });
        services_category_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserId_Main = login_data_pref.getString(DataKeyValues.USER_USERID, null);
                Intent category_intent = new Intent(getApplicationContext(), CategoriesSelecting.class);
                category_intent.putExtra("ownerId", UserId_Main);
                startActivity(category_intent);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
