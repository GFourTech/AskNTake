package com.askntake.admin.askntake;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import AppUtils.AppConstants;
import AppUtils.DataKeyValues;
import AppUtils.ServiceHandler;

public class AccountEditChangePassword extends Activity {

    ImageView back_button;
    EditText new_password, confirm_password;
    Button updateBtn;

    Dialog Internetdialog, dialog_for_cancel;


    ProgressDialog mProgressDialog;
    boolean gmail_logout = false;
    SharedPreferences usrPref;

    String UserId_Main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        usrPref = AppConstants.preferencesData(getApplication());
        UserId_Main = usrPref.getString(DataKeyValues.USER_USERID, null);

        setContentView(R.layout.account_edit_change_pass_screen);


        new_password = (EditText) findViewById(R.id.new_password);

        confirm_password = (EditText) findViewById(R.id.confirm_password);

        updateBtn = (Button) findViewById(R.id.updateBtn);


        updateBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {


                if (new_password.getText().length() > 7) {
                    if (confirm_password.getText().length() > 7) {
                        if (new_password.getText().toString().equalsIgnoreCase(confirm_password.getText().toString())) {
                            new updatePwdAsyncTask().execute();

                        } else {
                            customToast("Password and confirm password should be same.");
                        }
                    } else {
                        confirm_password.requestFocus();
                        confirm_password.setError("Confirm Password must be at least 8 characters");
                    }

                } else {
                    new_password.requestFocus();
                    new_password.setError("New Password must be at least 8 characters");
                }
            }
        });
    }


    private class updatePwdAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(AccountEditChangePassword.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String result = "fail";
            String jsonStr = "";


            if (UserId_Main != null) {

                ServiceHandler serviceHandler = new ServiceHandler();
                JSONObject requestObject = getJSONObject();
                jsonStr = serviceHandler.makeServiceCall(AppConstants.updatePassword(UserId_Main), "POST", requestObject);

            }

            return jsonStr;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            mProgressDialog.dismiss();

            if (result.equalsIgnoreCase("success")) {

                customToast("Pasword updation success.");

            } else if (result.equalsIgnoreCase("fail")) {
                customToast("Pasword updation failed.");
            } else {
                customToast("Server Error.");
            }
        }


    }


    public void customToast(String message) {


        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.customtoast,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView custom_toast_message = (TextView) layout.findViewById(R.id.custom_toast_message);
        custom_toast_message.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private JSONObject getJSONObject() {
        JSONObject requestObject = null;
        try {

            requestObject = new JSONObject();

            requestObject.accumulate("currentPwd", "");
            requestObject.accumulate("password", new_password.getText().toString());
            requestObject.accumulate("confirm", confirm_password.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject;

    }

}
