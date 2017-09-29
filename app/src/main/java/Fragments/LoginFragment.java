package Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.askntake.admin.askntake.ForgotPasswordActivity;
import com.askntake.admin.askntake.LocationSettingsActivity;
import com.askntake.admin.askntake.NavigationDrawerActivity;
import com.askntake.admin.askntake.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import AppUtils.AppConstants;
import AppUtils.DataKeyValues;
import AppUtils.ServiceHandler;

/**
 * Created by admin on 3/28/2017.
 */

public class LoginFragment extends Fragment {
    Button login;
    EditText edt_email, edt_password;
    TextView txt_forgot_password;
    TextInputLayout input_layout_email, input_layout_password;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    GoogleCloudMessaging gcm;
    String regid;
    String user_latitude_value, user_longitude_value;
    boolean isemailverified = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_login, null);
        txt_forgot_password = (TextView) root_view.findViewById(R.id.txt_login_forgot_password);
        login = (Button) root_view.findViewById(R.id.btn_signup_page);
        edt_email = (EditText) root_view.findViewById(R.id.edt_login_email);
        edt_password = (EditText) root_view.findViewById(R.id.edt_login_password);
        input_layout_email = (TextInputLayout) root_view.findViewById(R.id.input_layout_email_login);
        input_layout_password = (TextInputLayout) root_view.findViewById(R.id.input_layout_password_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_email.getText().toString().matches(emailPattern) && edt_email.getText().toString().length() > 0) {
                    if (edt_password.getText().toString().length() > 0) {

                        new LoginAsync().execute();

                    } else {
                        input_layout_password.setError("Enter Valid Password");
                        edt_email.requestFocus();
                    }

                } else {
                    input_layout_email.setError("Enter Valid Email");
                    edt_email.requestFocus();
                }
            }
        });
        txt_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgot_layout = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(forgot_layout);
            }
        });

        return root_view;
    }

    private class LoginAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = getJSONObject();
            String service_result = serviceHandler.makeServiceCall(AppConstants.LOGIN_URL, "POST", requestObject);

            return service_result;
        }

        @Override
        protected void onPostExecute(String service_result) {
            super.onPostExecute(service_result);
            progressDialog.dismiss();

            boolean status = false;
            String loginResponse = "";
            try {
                JSONObject jsObj = new JSONObject(service_result);
                if (jsObj.has("status")) {
                    status = jsObj.getBoolean("status");
                    if (status) {

                        if (jsObj.has("lat")) {
                            user_latitude_value = jsObj.getString("lat");
                        }
                        if (jsObj.has("lang")) {
                            user_longitude_value = jsObj.getString("lang");
                        }
                        int unread_messages_count = 0;
                        if (jsObj.has("unreadmessagescount")) {
                            unread_messages_count = jsObj.getInt("unreadmessagescount");
                        }
                        // Toast.makeText(getActivity(), "Login completed successfully..", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(DataKeyValues.USER_DATA_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor fb_editor = sharedPreferences.edit();
                        fb_editor.putString("fb_username", jsObj.getString(DataKeyValues.USER_FIRSTNAME));
                        fb_editor.putString("fb_email", jsObj.getString(DataKeyValues.USER_EMAIL));
                        fb_editor.putString(DataKeyValues.USER_IMAGE, jsObj.getString(DataKeyValues.USER_IMAGE));
                        //fb_editor.putString("fb_gender", fb_gender);
                        fb_editor.putString("fb_id", jsObj.getString("userId"));
                        fb_editor.putString("user_responce_lattitude", user_latitude_value);
                        fb_editor.putString("user_responce_langitude", user_longitude_value);
                        fb_editor.putString(DataKeyValues.MY_OWN_LATITUDE_VALUE, user_latitude_value);
                        fb_editor.putString(DataKeyValues.MY_OWN_LONGITUDE_VALUE, user_longitude_value);
                        fb_editor.putBoolean("login_status", status);
                        fb_editor.putString(DataKeyValues.USER_USERID, jsObj.getString(DataKeyValues.USER_USERID));
                        // fb_editor.putString("fb_gender", jsObj.getString(DataKeyValues.USER_GENDER));
                        fb_editor.putBoolean(DataKeyValues.GMAIL_LOGIN, false);
                        fb_editor.putString(DataKeyValues.OWNER_ZIPCODE, jsObj.getString(DataKeyValues.USER_ZIPCODE));
                        fb_editor.putString(DataKeyValues.OWNER_CITY, jsObj.getString(DataKeyValues.USER_CITY));
                        fb_editor.putString(DataKeyValues.OWNER_STATE, jsObj.getString(DataKeyValues.USER_STATE));
                        if (unread_messages_count != 0) {
                            fb_editor.putInt(DataKeyValues.UNREAD_MSG_COUNT, unread_messages_count);
                        }

                        fb_editor.putBoolean("isemailverified", isemailverified);


                        fb_editor.commit();


                        if (user_latitude_value.equalsIgnoreCase("0") && user_longitude_value.equalsIgnoreCase("0")) {
                            Intent myIntent = new Intent(getActivity(), LocationSettingsActivity.class);
                            myIntent.putExtra("updatelocation", true);
                            myIntent.putExtra("requestFrom", "display_scr");
                            startActivity(myIntent);
                            getActivity().finish();
                        } else {

                            Intent intent = new Intent(getActivity(), NavigationDrawerActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        // Toast.makeText(getActivity(), "successfully Login", Toast.LENGTH_SHORT).show();


                    } else if (jsObj.has("errorMessage")) {
                        loginResponse = jsObj.getString("errorMessage");
                        Toast.makeText(getActivity(), loginResponse, Toast.LENGTH_SHORT).show();
                    } else {
                        loginResponse = "fail";

                    }
                    // Toast.makeText(getActivity(), loginResponse, Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(getActivity(), loginResponse, Toast.LENGTH_SHORT).show();
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
            try {
                if (gcm == null) {
                    regid = "";
                    gcm = GoogleCloudMessaging.getInstance(getActivity());
                }
                regid = gcm.register(AppConstants.SENDER_ID);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

            requestObject = new JSONObject();
            requestObject.accumulate(DataKeyValues.LOGIN_EMAIL_ID, edt_email.getText().toString().trim().replace(" ", ""));
            requestObject.accumulate(DataKeyValues.LOGIN_PASSWORD, edt_password.getText().toString());
            requestObject.accumulate(DataKeyValues.LOGIN_GCMID, regid);
            requestObject.accumulate(DataKeyValues.LOGIN_DIVICETYPE, DataKeyValues.DIVICE_TYPE_VALUE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject;

    }

}
