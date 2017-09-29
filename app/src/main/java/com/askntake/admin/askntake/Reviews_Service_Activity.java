package com.askntake.admin.askntake;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import AppUtils.AppConstants;
import AppUtils.DataKeyValues;
import AppUtils.ServiceHandler;

/**
 * Created by on 24/04/2017.
 */

public class Reviews_Service_Activity extends AppCompatActivity {
    EditText comments_text;
    TextView name_txt;
    RatingBar ratingbar;
    Button submit_button;
    SharedPreferences fb_data_pref;
    ProgressDialog progressDialog;

    TextInputLayout input_layout_comment, input_layout_name;

    String value, name, userId, service_id, service_name;
    TextView service_name_value, service_user_name;


    float rating_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        comments_text = (EditText) findViewById(R.id.comments_text);
        service_user_name = (TextView) findViewById(R.id.service_user_name);
        name_txt = (TextView) findViewById(R.id.name_txt);
        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        submit_button = (Button) findViewById(R.id.submit_button);
        service_name_value = (TextView) findViewById(R.id.service_name_value);

        input_layout_comment = (TextInputLayout) findViewById(R.id.input_layout_comment);
//        input_layout_name = (TextInputLayout) findViewById(R.id.input_layout_name);

        fb_data_pref = AppConstants.preferencesData(getApplication());

        name = fb_data_pref.getString("fb_username", null);

//        userId = fb_data_pref.getString("registrationID", null);
//
//        service_id = getIntent().getStringExtra("service_id");

        service_id = getIntent().getStringExtra("service_id");

        userId = getIntent().getStringExtra("user_id");

        service_name = getIntent().getStringExtra("service_name");


        service_user_name.setText(name);

        service_name_value.setText(service_name);


        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                final int numStars = ratingbar.getNumStars();
                value = rating + "/" + numStars;
                rating_value = rating;

            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (comments_text.getText().toString().length() > 0) {
                    if (rating_value != 0.0) {

                        new ReviewAsyncTask().execute();


                    } else {
                        Toast.makeText(Reviews_Service_Activity.this, "please select rating", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    comments_text.requestFocus();
                    comments_text.setError("please comment");
                    //Toast.makeText(Reviews_Service_Activity.this, "please coment", Toast.LENGTH_SHORT).show();
                }

            }


        });


    }

    private class ReviewAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Reviews_Service_Activity.this, "", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = getJSONObject();
            String service_result = serviceHandler.makeServiceCall(AppConstants.REVIEWS_URL, "POST", requestObject);

            return service_result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            /*if(s.equalsIgnoreCase("status"))
            {
                Toast.makeText(getApplicationContext(),s.equalsIgnoreCase("message"),)
            }*/
            boolean status = false;
            try {
                JSONObject jsObj = new JSONObject(s);
                if (jsObj.has("status")) {
                    status = jsObj.getBoolean("status");
                    if (status) {


                        Intent descIntent = new Intent(getApplicationContext(), ServiceDescriptionActivity_Services.class);
                        startActivity(descIntent);
                        finish();
                        Toast.makeText(getApplicationContext(), jsObj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Uploading failed..", Toast.LENGTH_SHORT).show();

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private JSONObject getJSONObject() {
            JSONObject requestObject = null;
            try {

                requestObject = new JSONObject();
                requestObject.accumulate("userId", userId);
                requestObject.accumulate("serviceId", service_id);
                requestObject.accumulate("comment", comments_text.getText().toString());
                requestObject.accumulate("rating", rating_value);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return requestObject;

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
