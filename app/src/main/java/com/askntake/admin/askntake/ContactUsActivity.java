package com.askntake.admin.askntake;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import AppUtils.AppConstants;
import AppUtils.DataKeyValues;
import AppUtils.ServiceHandler;

public class ContactUsActivity extends AppCompatActivity {


    EditText email, content;
    Spinner subject_spinner;
    //AutoCompleteTextView subject_auto_TextView;
    Button send_button;
    String email_text;
    ProgressDialog mProgressDialog;
    String selected_subject_item = null;
    TextInputLayout email_input_layout, content_input_layout;
    ProgressDialog progressDialog;

    SharedPreferences fb_data_pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        email_input_layout = (TextInputLayout) findViewById(R.id.email_input_layout);
        content_input_layout = (TextInputLayout) findViewById(R.id.content_input_layout);
        send_button = (Button) findViewById(R.id.send_button);
        content = (EditText) findViewById(R.id.content_editText);
        subject_spinner = (Spinner) findViewById(R.id.subject_spinner);
        email = (EditText) findViewById(R.id.email_editText);

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(ContactUsActivity.this);
                View promptsView = li.inflate(R.layout.description_popup, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ContactUsActivity.this);

                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                final TextView description_tv = (TextView) promptsView
                        .findViewById(R.id.description_tv);

                description_tv.setText("Enter your Valuable Contetnt");


                userInput.setText(content.getText());

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        content.setText(userInput.getText());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();

            }
        });

        fb_data_pref =AppConstants.preferencesData(getApplication());
        email_text = fb_data_pref.getString("fb_email", null);
        if (email_text != null) {
            email.setText(email_text);
        } else {
            email.setText("");
        }


        subject_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub


                selected_subject_item = arg0.getItemAtPosition(arg2).toString();
                //String.valueOf(subject_spinner.getSelectedItem());

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        send_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (email.getText().toString().length() > 0) {
                    if (isEmailValid(email.getText().toString())) {

                        if (selected_subject_item != null) {
                            if (content.getText().length() > 0) {


                                new ContactUsDataAsyncTask().execute();

                            } else {
                                content.requestFocus();
                                content_input_layout.setError("We need you to fill in all of the fields!");
                            }
                        } else {
                            //error on subject
                            //Toast.makeText(getApplicationContext(), "subject wrong", Toast.LENGTH_SHORT).show();
                            subject_spinner.requestFocus();
                            //subject_auto_TextView.setError("Please select a subject from the list provided!");

                        }
                    } else {
                        email.requestFocus();
                        email_input_layout.setError("We need you to fill in all of the fields!");
                    }
                } else {

                    //Toast.makeText(getApplicationContext(), "Email Wrong", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    email.setError("Entered email address is invalid!");
                    //error on Email

                }

            }
        });

    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public class ContactUsDataAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog  = ProgressDialog.show(ContactUsActivity.this, "", "Please wait...", false, false);


        }

        @Override
        protected String doInBackground(String... strings) {
            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = contactUsRequest();
            String service_result = serviceHandler.makeServiceCall(AppConstants.CONTACT_US_URL, "POST", requestObject);

            return service_result;

        }


        @Override
        protected void onPostExecute(String service_result) {
            super.onPostExecute(service_result);
            progressDialog.dismiss();
            boolean status = false;
            try {
                JSONObject jsObj = new JSONObject(service_result);
                if (jsObj.has("status")) {
                    status = jsObj.getBoolean("status");

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (status) {


                Toast.makeText(getApplicationContext(), "your message has been sent correctly", Toast.LENGTH_SHORT).show();


                finish();
            } else {
                //				openDialog(ServiceResponceMessage+" ");Intent
                Toast.makeText(getApplicationContext(), "Data Insertion Failed", Toast.LENGTH_SHORT).show();

            }
        }


        public JSONObject contactUsRequest() {

            JSONObject requestObject = null;
            try {


                requestObject = new JSONObject();
                requestObject.accumulate(DataKeyValues.CONTACT_US_EMAIL, email_text);
                requestObject.accumulate(DataKeyValues.CONTACT_US_SUBJECT, selected_subject_item);
                requestObject.accumulate(DataKeyValues.CONTACT_US_CONTENT, content.getText().toString());

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


