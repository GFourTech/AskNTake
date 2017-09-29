package Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.askntake.admin.askntake.LoginRegistrationActivity;
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

public class RegistrationFragment extends Fragment {
    Button sign_up;
    EditText edt_email, edt_password, edt_first_name, edt_lastname;
    TextInputLayout input_layout_email, input_layout_password, input_layout_first_name, input_layout_lastname;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    GoogleCloudMessaging gcm;
    String regid;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_signup, null);
        edt_email = (EditText) root_view.findViewById(R.id.edt_signup_email);
        edt_password = (EditText) root_view.findViewById(R.id.edt_signup_password);
        edt_first_name = (EditText) root_view.findViewById(R.id.edt_signup_firstname);
        edt_lastname = (EditText) root_view.findViewById(R.id.edt_signup_lastname);
        input_layout_email = (TextInputLayout) root_view.findViewById(R.id.input_layout_email);
        input_layout_password = (TextInputLayout) root_view.findViewById(R.id.input_layout_password);
        input_layout_first_name = (TextInputLayout) root_view.findViewById(R.id.input_layout_firstname);
        input_layout_lastname = (TextInputLayout) root_view.findViewById(R.id.input_layout_lastname);
        sign_up = (Button) root_view.findViewById(R.id.btn_signup_page);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_email.getText().toString().matches(emailPattern) && edt_email.getText().toString().length() > 0) {
                    if (edt_password.getText().toString().length() > 7) {
                        if (edt_first_name.getText().toString().length() > 0) {
                            if (edt_lastname.getText().toString().length() > 0) {
                                new RegistrationAsync().execute();
                            } else {
                                input_layout_lastname.setError("Enter Lastname");
                                edt_lastname.requestFocus();
                            }

                        } else {
                            input_layout_first_name.setError("Enter Firstname");
                            edt_first_name.requestFocus();
                        }

                    } else {
                        input_layout_password.setError("Enter minimum 8 letter password");
                        edt_password.requestFocus();
                    }
                } else

                {
                    input_layout_email.setError("Enter Valid Email");
                    edt_email.requestFocus();
                }


            }
        });


        return root_view;

    }


    private class RegistrationAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = getJSONObject();
            String service_result = serviceHandler.makeServiceCall(AppConstants.REG_URL, "POST", requestObject);

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
                    if (status) {

                        if (jsObj.getString("message").equalsIgnoreCase("Registration success")) {

                            Toast.makeText(getActivity(), jsObj.getString("message"), Toast.LENGTH_SHORT).show();

                            String registrationID = jsObj.getString("userId");
                            showEmailVerificationAlert();
                        } else {
                            Toast.makeText(getActivity(), jsObj.getString("message"), Toast.LENGTH_SHORT).show();


                        }
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
            requestObject.accumulate(DataKeyValues.REG_EMAIL_ID, edt_email.getText().toString().trim().replace(" ", ""));
            requestObject.accumulate(DataKeyValues.REG_PASSWORD, edt_password.getText().toString());
            requestObject.accumulate(DataKeyValues.REG_FIRSTNAME, edt_first_name.getText().toString());
            requestObject.accumulate(DataKeyValues.REG_LASTNAME, edt_lastname.getText().toString());
            requestObject.accumulate(DataKeyValues.REG_IMAGE, "noimage");
            requestObject.accumulate(DataKeyValues.REG_FB, "");
            requestObject.accumulate(DataKeyValues.REG_FBID, "");
            requestObject.accumulate(DataKeyValues.REG_REGTYPE, "manual");
            requestObject.accumulate(DataKeyValues.REG_GCMID, regid);
            requestObject.accumulate(DataKeyValues.REG_GENDER, "male");
            requestObject.accumulate(DataKeyValues.REG_DIVICETYPE, "android");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject;

    }

    private void showEmailVerificationAlert() {


        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        if (dialog != null) {
            dialog.setTitle("Please confirm the link sent to your email-Inbox/Spam");

        }
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                LoginRegistrationActivity activity = (LoginRegistrationActivity) getActivity();
                activity.changeRegistrationTabToLogin();

            }
        });


        final AlertDialog alert = dialog.create();
        alert.show();


    }

}
