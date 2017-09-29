package com.askntake.admin.askntake;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by admin on 6/27/2017.
 */

public class SelectionCategoryActivity extends AppCompatActivity {

    Button buy_sell_select_btn, services_select_btn, stores_select_btn, deals_select_btn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_category_sell);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        buy_sell_select_btn = (Button) findViewById(R.id.buy_sell_select_btn);
        services_select_btn = (Button) findViewById(R.id.services_select_btn);
        stores_select_btn = (Button) findViewById(R.id.stores_select_btn);
        deals_select_btn = (Button) findViewById(R.id.deals_select_btn);

        buy_sell_select_btn.setVisibility(View.GONE);
        stores_select_btn.setVisibility(View.GONE);
        deals_select_btn.setVisibility(View.GONE);


        buy_sell_select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent category_intent= new Intent(getApplicationContext(),SearchCategoriesActivity.class);
                startActivity(category_intent);*/
            }
        });
        services_select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent category_intent = new Intent(getApplicationContext(), AddServiceActivity.class);
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
