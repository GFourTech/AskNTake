package com.askntake.admin.askntake;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import AppUtils.AppConstants;

public class HelpActivity extends AppCompatActivity {
    WebView webView;
    String showpage = null;

    //ImageView back_button,popup_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showpage = getIntent().getStringExtra("showpage");
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView = (WebView) findViewById(R.id.webview_term);


        if (showpage != null) {
            if (showpage.equalsIgnoreCase("terms")) {
                LoadHtmlPage(AppConstants.TERMS_AND_CONDTIONS_URL);

            } else if (showpage.equalsIgnoreCase("privacy")) {
                LoadHtmlPage(AppConstants.PRIVACY_POLICY);
            } else {
                LoadHtmlPage(AppConstants.FAQ_URL);
            }
        } else {
            //show faq
            LoadHtmlPage(AppConstants.FAQ_URL);
        }




    }


    void LoadHtmlPage(String url) {
        int color = getIntent().getIntExtra("BACKGROUND", R.color.logo_blue);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });
        webView.loadUrl(url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==android.R.id.home)
        {
            finish();
        }

        if (id == R.id.terms_n_conditions) {
            LoadHtmlPage(AppConstants.TERMS_AND_CONDTIONS_URL);

            return true;
        }
        if (id == R.id.privacy_policy) {
            LoadHtmlPage(AppConstants.PRIVACY_POLICY);

            return true;
        }
        if (id == R.id.contact_us) {

            Intent contact_us_intent = new Intent(getApplicationContext(), ContactUsActivity.class);
            startActivity(contact_us_intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
