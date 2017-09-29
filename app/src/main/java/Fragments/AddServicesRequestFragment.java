package Fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.askntake.admin.askntake.LocationSettingsActivity;
import com.askntake.admin.askntake.NavigationDrawerActivity;
import com.askntake.admin.askntake.R;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import AppUtils.AppConstants;
import AppUtils.Commons;
import AppUtils.ConnectionDetector;
import AppUtils.DataKeyValues;
import AppUtils.ServiceHandler;
import AppUtils.Utility;
import Pojo.SharedPrefPojo;


/**
 * Created by on 04/05/2017.
 */

public class AddServicesRequestFragment extends Fragment {

    private GoogleMap googleMap;

    Button list_now_req_button;
    TextView contact_details;
    Spinner main_categories_list, main_sub_categories_list;
    TextInputLayout input_lay_description, input_la_your_name, input_la_phone_number, input_la_email;
    EditText edit_description_service_req, edit_your_name, edit_phone_number, edit_email;
    //LinearLayout map_linear_layout;


    Map<String, String> subList = new HashMap<>();

    String username, userId;

    ConnectionDetector internetConnection;
    Dialog Internetdialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;


    String user_latitude = "";
    String user_longitude = "";
    String user_zipcode = "";
    String user_location_address = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root_view = inflater.inflate(R.layout.fragment_add_service_request, null);

        internetConnection = new ConnectionDetector(getActivity());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        SharedPreferences login_data_pref = getActivity().getSharedPreferences(DataKeyValues.USER_DATA_PREF, Context.MODE_PRIVATE);
        username = login_data_pref.getString(DataKeyValues.USER_FIRSTNAME, null);
        userId = login_data_pref.getString(DataKeyValues.USER_USERID, null);

        list_now_req_button = (Button) root_view.findViewById(R.id.list_now_req_button);
        contact_details = (TextView) root_view.findViewById(R.id.contact_details);

        main_categories_list = (Spinner) root_view.findViewById(R.id.main_categories_list);
        main_sub_categories_list = (Spinner) root_view.findViewById(R.id.main_sub_categories_list);

        input_lay_description = (TextInputLayout) root_view.findViewById(R.id.input_lay_description);
        input_la_your_name = (TextInputLayout) root_view.findViewById(R.id.input_la_your_name);
        input_la_phone_number = (TextInputLayout) root_view.findViewById(R.id.input_la_phone_number);
        input_la_email = (TextInputLayout) root_view.findViewById(R.id.input_la_email);

        edit_description_service_req = (EditText) root_view.findViewById(R.id.edit_description_service_req);
        edit_your_name = (EditText) root_view.findViewById(R.id.edit_your_name);
        edit_phone_number = (EditText) root_view.findViewById(R.id.edit_phone_number);
        edit_email = (EditText) root_view.findViewById(R.id.edit_email);


        final Map<String, Map<String, String>> categoriesList = Commons.getCategories(getActivity());
        Map<String, String> subcategories = null;
        ArrayList<String> categoriesArray = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> entry : categoriesList.entrySet()) {
            String category = entry.getKey();
            categoriesArray.add(category);
        }

        /*ArrayAdapter<CharSequence> product_adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.array_name, android.R.layout.simple_spinner_item);*/
        ArrayAdapter<String> product_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, categoriesArray);
        product_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final String[] category_array = getResources().getStringArray(R.array.array_name);


        main_categories_list.setAdapter(product_adapter);
        main_categories_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Map<String, String> subCategoriesList = categoriesList.get(main_categories_list.getSelectedItem().toString().trim());
                Log.i("subcategoriesSize", "" + subCategoriesList.size());

                resetSubCategories(subCategoriesList);
                ArrayList<String> subCategoriesArray = new ArrayList<>();
                for (Map.Entry<String, String> entry : subCategoriesList.entrySet()) {
                    String subCategory = entry.getKey();
                    subCategoriesArray.add(subCategory);
                }
                if (subCategoriesArray.size() > 0) {
                    main_sub_categories_list.setVisibility(view.VISIBLE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subCategoriesArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    main_sub_categories_list.setAdapter(adapter);
                } else {
                    main_sub_categories_list.setVisibility(view.GONE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        main_sub_categories_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //  Toast.makeText(getActivity(), "" + main_sub_categories_list.getSelectedItem().toString().trim(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edit_description_service_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.description_popup, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);


                userInput.setText(edit_description_service_req.getText());

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text

                                        edit_description_service_req.setText(userInput.getText());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });


        list_now_req_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (main_categories_list.getSelectedItem().toString() != null) {
                    if (main_sub_categories_list.getSelectedItem().toString() != null) {
                        if (edit_description_service_req.getText().length() > 0) {
                            if (edit_your_name.getText().length() > 0) {
                                if (android.util.Patterns.PHONE.matcher(edit_phone_number.getText()).matches()) {
                                    if (edit_email.getText().toString().matches(emailPattern) && edit_email.getText().toString().length() > 0) {
                                        // new ServiceRquestAsyncTask().execute();
                                        runServer();
                                    } else {
                                        input_la_email.requestFocus();
                                        edit_email.setError("Please Enter your valid Email");
                                    }
                                } else {
                                    input_la_phone_number.requestFocus();
                                    edit_phone_number.setError("Please Enter your Phone Number");
                                }
                            } else {

                                input_la_your_name.requestFocus();
                                edit_your_name.setError("Please Povide your Name");
                            }
                        } else {
                            input_lay_description.requestFocus();
                            edit_description_service_req.setError("please provide Description");
                        }
                    } else {

                        Toast.makeText(getActivity(), "Please select Sub Category", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(getActivity(), "Please select Category", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return root_view;
    }

    private void resetSubCategories(Map<String, String> subcategoriesList) {
        subList = null;
        subList = subcategoriesList;
    }

    public void runServer() {
        SharedPrefPojo pojo = Utility.getData(getActivity());

        user_latitude = pojo.getLattitude();
        user_longitude = pojo.getLangitude();
        // user_zipcode = pojo.getZipcode();
        // user_location_address = pojo.getArea();


        // Toast.makeText(getActivity(), "lat: " + user_latitude + " lang: " + user_longitude + " Zipcode: " /*+ user_zipcode + " Address: " + user_location_address*/, Toast.LENGTH_SHORT).show();
        if (user_latitude != null && user_longitude != null) {
            if (internetConnection.isConnectingToInternet(getActivity())) {
                Utility.resetUploadLocation(getActivity());
                new ServiceRquestAsyncTask().execute();
            } else {
                InternetConnectionAlert();
            }
        } else {
            Intent myIntent = new Intent(getActivity(), LocationSettingsActivity.class);
            myIntent.putExtra("requestFrom", "upload_scr");
            startActivity(myIntent);
        }
    }

    void InternetConnectionAlert() {

        Internetdialog = new Dialog(getActivity()); /*android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);*/
        Internetdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Internetdialog.setContentView(R.layout.net_connection_alert_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Internetdialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        Internetdialog.getWindow().setAttributes(lp);
        Internetdialog.setCancelable(false);
        Internetdialog.setCanceledOnTouchOutside(false);

        ImageView wifi_error_img = (ImageView) Internetdialog.findViewById(R.id.wifi_error_img);
        TextView wifi_retry_btn = (TextView) Internetdialog.findViewById(R.id.wifi_retry_btn);
        wifi_retry_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.butonanim));
                if (internetConnection.isConnectingToInternet(getActivity())) {
                    Internetdialog.dismiss();

                }


            }
        });
        Internetdialog.show();

    }

    private class ServiceRquestAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = getJSONObject();
            String service_result = serviceHandler.makeServiceCall(AppConstants.SERVICE_REQUEST_UPLOAD_URL, "POST", requestObject);

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

                        Intent loginIntent = new Intent(getActivity(), NavigationDrawerActivity.class);
                        startActivity(loginIntent);
                        getActivity().finish();

                        Toast.makeText(getActivity(), jsObj.getString("message"), Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(getActivity(), jsObj.getString("message"), Toast.LENGTH_SHORT).show();

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
            requestObject.accumulate(DataKeyValues.REQUESTER_ID, userId);
            requestObject.accumulate(DataKeyValues.REQUESTER_NAME, edit_your_name.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.REQUESTER_DESCRIPTION, edit_description_service_req.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.REQUESTER_CATEGORY, main_categories_list.getSelectedItem().toString().trim());
            requestObject.accumulate(DataKeyValues.REQUESTER_SUB_CATEGORY, main_sub_categories_list.getSelectedItem().toString().trim());
            requestObject.accumulate(DataKeyValues.REQUESTER_CONTACT, edit_phone_number.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.REQUESTER_EMAIL_ID, edit_email.getText().toString().trim());
            requestObject.accumulate(DataKeyValues.REQUESTER_LATITUDE, user_latitude);
            requestObject.accumulate(DataKeyValues.REQUESTER_LANGITUDE, user_longitude);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestObject;

    }
}
