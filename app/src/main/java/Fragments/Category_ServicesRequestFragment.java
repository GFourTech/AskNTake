package Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.askntake.admin.askntake.AddServiceActivity;
import com.askntake.admin.askntake.LocationSettingsActivity;
import com.askntake.admin.askntake.NavigationDrawerActivity;
import com.askntake.admin.askntake.R;
import com.askntake.admin.askntake.ServiceRequestsActivity;
import com.askntake.admin.askntake.TotalListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import Adapters.ServiceCategoriesFilterAdapter;
import AppUtils.AppConstants;
import AppUtils.Commons;
import AppUtils.DataKeyValues;
import AppUtils.Helper;
import AppUtils.ServiceHandler;
import AppUtils.Utils;

/**
 * Created by admin on 6/26/2017.
 */

public class Category_ServicesRequestFragment extends Fragment {

    Button submit_btn;
    ExpandableListView listView;

    String request = "serv_req_Category";


    Map<String, String> subList = new HashMap<>();

    //String[] testgroupData;

    //String[] testChildData;

    static CheckBox chkAll;
    String userMainId;
    Utils utils;
    Map<String, ArrayList<Integer>> positionsMap;
    ServiceCategoriesFilterAdapter adapter = null;
    String user_latitude = "0", user_longitude = "0";

    //    String search_location_to_set;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_category_servicerequests, null);

        utils = new Utils(getActivity());
        SharedPreferences fbpref = AppConstants.preferencesData(getActivity());
        userMainId = fbpref.getString(DataKeyValues.OWNER_ID, null);
        // new PopulateDataAsynchTask().execute();
        chkAll = (CheckBox) root_view.findViewById(R.id.all_categories_chbx);
        chkAll.setChecked(true);


        submit_btn = (Button) root_view.findViewById(R.id.submit_btn);
        listView = (ExpandableListView) root_view.findViewById(R.id.categ_expandable_list);

        /*for(int i=0; i < getListAdapter().getCount(); i++){
            listView.setItemChecked(i, isChecked);
        }
*/

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        final Map<String, Map<String, String>> categoriesList = Commons.getCategoriesServiceRequests(getActivity());

        ArrayList<String> positions = new ArrayList<>();
        positionsMap = getPositions(positions);
        adapter = new ServiceCategoriesFilterAdapter(getActivity(), /*testgroupData, testChildData,*/categoriesList, request, positionsMap);

        adapter.setmListener(new TotalListener() {
            @Override
            public void onTotalChanged(int sum) {
                // mTextView.setText("Total = " + sum);
            }

            @Override
            public void expandGroupEvent(int groupPosition, boolean isExpanded) {
                if (isExpanded)
                    listView.collapseGroup(groupPosition);
                else
                    listView.expandGroup(groupPosition);
            }
        });
        chkAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                CheckBox chk = (CheckBox) v;
                adapter.initCheckStates(chk.isChecked());

                adapter.notifyDataSetChanged();

            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //  ArrayList<String> subcategoriesPositions =adapter.getCheckedSubcategoriesPositios();
              /*  ArrayList<String> selectedSubcategories =adapter.getCheckedSubcategories();
                Log.i("selectedSubcategories",""+selectedSubcategories.size());
                ArrayList<String> subcategoriesPositions =adapter.getCheckedSubcategoriesPositios();
                Log.i("subcategoriesPositions",""+subcategoriesPositions.size());
                ArrayList<String> selectedCategories =adapter.getCheckedCategories();
                Log.i("selectedCategories",""+selectedCategories.size());
                filterPojoObj = new ServicesFiteringDataPojo("", selectedCategories, selectedSubcategories, "K", 0, "0",subcategoriesPositions);
                new SaveFiltersAsynchTask().execute();*/
                SharedPreferences user_LocationData = AppConstants.preferencesData(getActivity());

//                user_latitude = user_LocationData.getString(DataKeyValues.FILTERLATTITUDE, "0");
//                user_longitude = user_LocationData.getString(DataKeyValues.FILTERLONGITUDE, "0");

                user_latitude = user_LocationData.getString("user_latitude1", "0");
                user_longitude = user_LocationData.getString("user_longitude1", "0");
                //  search_location_to_set = user_LocationData.getString(DataKeyValues.SEARCH_SHOW_LOCATION, null);

//                if (user_latitude != null) {
//                    if (!(user_latitude.equalsIgnoreCase("0"))) {
                new KnowUserProviderStatusAsynchTask().execute();
//                    } else {
//                        runServer();
//                    }
//                } else {
//                    runServer();
//                }

            }
        });
        listView.setAdapter(adapter);
        Helper helper = new Helper();
        helper.getExpendableListViewSize(listView/*, 2*/);
        return root_view;
    }

    private class KnowUserProviderStatusAsynchTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            String response = "";
            try {
                ServiceHandler serviceHandler = new ServiceHandler();

                response = serviceHandler.makeServiceCall(AppConstants.BASE_URL + "rest/user/providerstatus", "POST", getRequestJsonObject());
                Log.i("responce", "" + response);
            } catch (Exception ex) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        utils.showToast("Server Not responding, Please check whether your server is running or not");
                    }
                });
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.has("status")) {
                        if (Boolean.parseBoolean(obj.getString("status"))) {
                            ArrayList<String> selectedCategories = adapter.getCheckedCategories();
                            Intent service_reqIntent = new Intent(getActivity(), ServiceRequestsActivity.class);
                            service_reqIntent.putExtra("lat", user_latitude);
                            service_reqIntent.putExtra("lang", user_longitude);
                            service_reqIntent.putStringArrayListExtra("Categorieslist", selectedCategories);
                            startActivity(service_reqIntent);
                        } else {

                            ShowAlert("Sorry, Service requests can only be seen by service providers");
                            // Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {

                }
            }
        }

        private JSONObject getRequestJsonObject() {


            JSONObject requestObject = null;
            try {
                requestObject = new JSONObject();
                requestObject.accumulate("userId", userMainId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return requestObject;
        }

    }

    private Map<String, ArrayList<Integer>> getPositions(ArrayList<String> positions) {
        Map<String, ArrayList<Integer>> mainObj = new LinkedHashMap<>();

        ArrayList<Integer> childs;
        for (int i = 0; i < positions.size(); i++) {

            String[] positionsArray = positions.get(i).split(",");

            String groupPosition = positionsArray[0];
            int childPosition = Integer.parseInt(positionsArray[1]);

            childs = mainObj.get(groupPosition);

            if (childs != null) {
                childs.add(childPosition);
            } else {
                childs = new ArrayList<>();
                childs.add(childPosition);
            }
            mainObj.put(groupPosition, childs);

        }


        return mainObj;
    }

    public static void uncheckAll() {
        chkAll.setChecked(false);
    }

    public static void checkAll() {
        chkAll.setChecked(true);
    }


    public void runServer() {
        Intent myIntent = new Intent(getActivity(), LocationSettingsActivity.class);
        myIntent.putExtra("requestFrom", "category");
        startActivity(myIntent);
    }

    private void ShowAlert(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        if (dialog != null) {
            dialog.setTitle("Ooooops....");

        }
        dialog.setCancelable(false);
        dialog.setMessage(message);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), NavigationDrawerActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        final AlertDialog alert = dialog.create();
        alert.show();

    }
}
