package com.askntake.admin.askntake;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import AppUtils.AppConstants;
import AppUtils.ServiceHandler;


public class ForgotPasswordActivity extends AppCompatActivity {

    Button reset_password;
    EditText edt_email;
    TextInputLayout input_layout_email;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        reset_password=(Button)findViewById(R.id.btn_reset_password);
        edt_email=(EditText)findViewById(R.id.edt_email_forgot);
        input_layout_email=(TextInputLayout)findViewById(R.id.input_layout_email_forgot_password);

        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_email.getText().toString().matches(emailPattern)&&edt_email.getText().toString().length()>0)
                {
                    //Toast.makeText(ForgotPasswordActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    new   ForgotPasswordAsync(edt_email.getText().toString().trim()).execute();

                }
                else
                {
                    input_layout_email.setError("Entered email address is invalid!");
                    edt_email.requestFocus();
                }
            }
        });
    }

    private class ForgotPasswordAsync extends AsyncTask<String,Void,String>
    {

        String emai_id;

        public ForgotPasswordAsync(String emai_id) {
            this.emai_id = emai_id;
        }

        @Override
        protected void onPreExecute() {

            mProgressDialog = ProgressDialog.show(ForgotPasswordActivity.this, "", "Please wait...", false, false);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            ServiceHandler serviceHandler=new ServiceHandler();
         //   JSONObject requestObject = getRequestObject();
            String content= serviceHandler.makeServiceCall(AppConstants.forgertPasswordUrl(emai_id),"POST",null);
            return content;
        }

        @Override
        protected void onPostExecute(String change_PswdObj) {
            super.onPostExecute(change_PswdObj);
            mProgressDialog.dismiss();


            if(change_PswdObj!=null)
            {

                try {

                    JSONObject change_PswdObject=new JSONObject(change_PswdObj);
                    if(change_PswdObject.has("status"))
                    {
                        if(change_PswdObject.getString("status").equalsIgnoreCase("true"))
                        {
                            showForgetPasswordLink("Please confirm the link sent to your email-Inbox/Spam");
                        }
                        else
                        {
                            showForgetPasswordLink("Entered Email id not found..");

                        }
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    showForgetPasswordLink("Entered Email id not found..");
                }

            }
            else {
                showForgetPasswordLink("Entered Email id not found..");
            }


        }
    }
    private JSONObject getRequestObject()
    {
        JSONObject jsonObject=null;
        try {

            jsonObject=new JSONObject();
            jsonObject.accumulate("","1");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return  jsonObject;
    }

    private void showForgetPasswordLink(String message)
    {

        AlertDialog.Builder dialog = new AlertDialog.Builder(ForgotPasswordActivity.this);
        if (dialog != null) {
            dialog.setTitle(message);

        }
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();

            }
        });
       /* dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

            }
        });*/


        final AlertDialog alert = dialog.create();
        alert.show();



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