package com.askntake.admin.askntake;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.ReportGridDisplyAdapter;
import Pojo.ProductItemsPojo;
import AppUtils.AppConstants;
import AppUtils.DataKeyValues;
import AppUtils.ServiceHandler;

/**
 * Created by admin on 4/13/2017.
 */

public class ReportThisProductScreenActivity  extends AppCompatActivity {
    public Integer[] mThumbIds = {
            R.drawable.report_1, R.drawable.report_2,
            R.drawable.report_3, R.drawable.report_4,
            R.drawable.report_5, R.drawable.report_6,
            R.drawable.report_7, R.drawable.report_8,
            R.drawable.report_9
    };
    public static int defaultScrenWidth=800,defaultScrenHeit=1280;
    public static int curScrenHeit;
    public static int curScrenWidth;
    GridView catogiry_filtering_grid;
    String clicked_item_id,grid_Items;
    String user_Id;
    ProgressDialog mProgressDialog;
    public String[] reportNames={"Wrong caregory","People or animals","Joke","Fake item","Explicit content","Does not match with the image","Food","Drugs or medicines","Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        curScrenHeit = displaymetrics.heightPixels;
        curScrenWidth = displaymetrics.widthPixels;
        setContentView(R.layout.report_this_product_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DataKeyValues DataKeyValues=new DataKeyValues();
        SharedPreferences fb_data_pref = getApplicationContext().getSharedPreferences(DataKeyValues.USER_DATA_PREF, MODE_PRIVATE);
        user_Id=fb_data_pref.getString(DataKeyValues.USER_USERID, null);


        SharedPreferences itemIdPref=getApplicationContext().getSharedPreferences("itemIdPref", MODE_PRIVATE);
        clicked_item_id= itemIdPref.getString("clicked_id", null);


        catogiry_filtering_grid=(GridView)findViewById(R.id.filtering_grid);
        catogiry_filtering_grid.setAdapter(new ReportGridDisplyAdapter(this,mThumbIds));


        catogiry_filtering_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub



                if(position<8)
                {
                    grid_Items=reportNames[position];
                    new HttpAsyncTaskGridClick().execute();

                }
                else
                {
                    Intent myIntent=new Intent(ReportThisProductScreenActivity.this,ReportingOtherTextActivity.class);
                    startActivity(myIntent);
                }


            }

        });

        String	other_text=getIntent().getStringExtra("other_text");
        if(other_text!=null)
        {
            grid_Items=other_text;

                new HttpAsyncTaskGridClick().execute();
        }

    }
    private class HttpAsyncTaskGridClick extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = getJSONObject();
            String service_result = serviceHandler.makeServiceCall(AppConstants.REPORT_THIS_ITEM_URL, "POST", requestObject);

             return service_result;
        }

        @Override
        protected void onPostExecute(String service_result) {
            super.onPostExecute(service_result);
            try {
                JSONObject jsObj=new JSONObject(service_result);
                if (jsObj.has("status")) {
                    if(jsObj.getBoolean("status"))
                    {

                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.customtoast,
                                (ViewGroup) findViewById(R.id.toast_layout_root));

                        layout.setBackgroundColor(getResources().getColor(R.color.black));
                        TextView custom_toast_message=(TextView)layout.findViewById(R.id.custom_toast_message);
                        custom_toast_message.setPadding(30*curScrenWidth/defaultScrenWidth, 50*curScrenHeit/defaultScrenHeit, 30*curScrenWidth/defaultScrenWidth, 50*curScrenHeit/defaultScrenHeit);
                        custom_toast_message.setText(R.string.report_listing_success_text);
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();


                        finish();



                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Data Insertion Failed", Toast.LENGTH_SHORT).show();

                    }

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private JSONObject getJSONObject() {
        JSONObject requestObject = null;
        try {

            requestObject = new JSONObject();
            requestObject.accumulate("userId",user_Id);
            requestObject.accumulate("productId", clicked_item_id);
            requestObject.accumulate("report", grid_Items);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject;

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
