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

public class MessagesActivity extends AppCompatActivity {
    Button buy_sell_btn, button_my_services, button_my_service_requests, button_my_stores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buy_sell_btn = (Button) findViewById(R.id.buy_sell_btn);
        button_my_services = (Button) findViewById(R.id.button_my_services);

        button_my_service_requests = (Button) findViewById(R.id.button_my_service_requests);
        button_my_stores = (Button) findViewById(R.id.button_my_stores);

        buy_sell_btn.setVisibility(View.GONE);
        button_my_stores.setVisibility(View.GONE);


        final SharedPreferences login_data_pref = getSharedPreferences(DataKeyValues.USER_DATA_PREF, Context.MODE_PRIVATE);


        buy_sell_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chat_intent = new Intent(getApplicationContext(), BuyAndSellChatHistory.class);
                startActivity(chat_intent);
            }
        });
        button_my_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserId_Main = login_data_pref.getString(DataKeyValues.USER_USERID, null);
                // Intent chat_intent= new Intent(getApplicationContext(),ServicesChatHistory.class);
                Intent chat_intent = new Intent(getApplicationContext(), ServicesChatActivity.class);
                chat_intent.putExtra("ownerId", UserId_Main);
                startActivity(chat_intent);
            }
        });
        button_my_service_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserId_Main = login_data_pref.getString(DataKeyValues.USER_USERID, null);
                //Intent chat_intent= new Intent(getApplicationContext(),ServiceRequestsChatHistory.class);
                Intent chat_intent = new Intent(getApplicationContext(), ServicesRequestChatActivity.class);
                chat_intent.putExtra("ownerId", UserId_Main);
                startActivity(chat_intent);
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
