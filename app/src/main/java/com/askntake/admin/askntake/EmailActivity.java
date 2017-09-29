package com.askntake.admin.askntake;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by admin on 6/28/2017.
 */

public class EmailActivity extends AppCompatActivity {


    View view;
    //private static final String EMAIL = "info@askntake.com";
    private static final String SUBJECT = "Please Help Me, Askntake";
    private String sen_email_to;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_nav_email);
        sen_email_to = getIntent().getStringExtra("sen_email_to");
        Intent intent = new Intent("android.intent.action.VIEW");
        StringBuilder stringbuilder = new StringBuilder("mailto:");
        stringbuilder.append(sen_email_to);
        // stringbuilder.append(EMAIL);
        stringbuilder.append("?subject=").append((new StringBuilder()).append(SUBJECT).toString());
        stringbuilder.append("&body=").append(Uri.encode(""));
        intent.setData(Uri.parse(stringbuilder.toString()));
        startActivity(intent);
    }
}
