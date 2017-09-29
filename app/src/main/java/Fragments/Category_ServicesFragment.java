package Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.askntake.admin.askntake.NavigationDrawerActivity;
import com.askntake.admin.askntake.R;
import com.askntake.admin.askntake.ServiceFilteringActivity;
import com.askntake.admin.askntake.TotalListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
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
import Pojo.ServicesFiteringDataPojo;

/**
 * Created by admin on 6/26/2017.
 */

public class Category_ServicesFragment extends Fragment {

    Button submit_btn;
    ExpandableListView listView;

    String request = "serv_Category";


    Map<String, String> subList = new HashMap<>();

    //String[] testgroupData;

    //String[] testChildData;

    static CheckBox chkAll;
    ServicesFiteringDataPojo filterPojoObj;
    String userMainId;
    Utils utils;
    Map<String, ArrayList<Integer>> positionsMap;
    ServiceCategoriesFilterAdapter adapter = null;
    String user_latitude = "0", user_longitude = "0";
    String search_location_to_set;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_category_services, null);

        utils = new Utils(getActivity());
        SharedPreferences fbpref = AppConstants.preferencesData(getActivity());
        userMainId = fbpref.getString(DataKeyValues.OWNER_ID, null);
        new PopulateDataAsynchTask().execute();
        chkAll = (CheckBox) root_view.findViewById(R.id.all_categories_chbx);
        chkAll.setChecked(true);
        SharedPreferences user_LocationData = AppConstants.preferencesData(getActivity());
        search_location_to_set = user_LocationData.getString(DataKeyValues.SEARCH_SHOW_LOCATION, null);

        user_latitude = user_LocationData.getString(DataKeyValues.FILTERLATTITUDE, null);
        user_longitude = user_LocationData.getString(DataKeyValues.FILTERLONGITUDE, null);

        submit_btn = (Button) root_view.findViewById(R.id.submit_btn);
        listView = (ExpandableListView) root_view.findViewById(R.id.categ_expandable_list);

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        final Map<String, Map<String, String>> categoriesList = Commons.getCategories(getActivity());

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
                ArrayList<String> selectedSubcategories = adapter.getCheckedSubcategories();
                Log.i("selectedSubcategories", "" + selectedSubcategories.size());
                ArrayList<String> subcategoriesPositions = adapter.getCheckedSubcategoriesPositios();
                Log.i("subcategoriesPositions", "" + subcategoriesPositions.size());
                ArrayList<String> selectedCategories = adapter.getCheckedCategories();
                Log.i("selectedCategories", "" + selectedCategories.size());

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Filter_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("filtering_marker", true);
                editor.commit();

                filterPojoObj = new ServicesFiteringDataPojo("", selectedCategories, selectedSubcategories, "M", 500, "0", subcategoriesPositions, "0.0");
                new SaveFiltersAsynchTask().execute();
            }
        });
        listView.setAdapter(adapter);
        Helper helper = new Helper();
        helper.getExpendableListViewSize(listView/*, 2*/);
        return root_view;
    }

    private void resetSubCategories(Map<String, String> subcategoriesList) {
        subList = null;
        subList = subcategoriesList;
    }

    public static void uncheckAll() {
        chkAll.setChecked(false);
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
            // childs.clear();
          /*  if(mainObj.get(groupPosition)!=null){
                mainObj.get(groupPosition).add(childPosition);

            }else{
                mainObj.put(groupPosition,childs);
            }*/

        }


        return mainObj;
    }

    private class SaveFiltersAsynchTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            String response = "";
            try {
                ServiceHandler serviceHandler = new ServiceHandler();
                response = serviceHandler.makeServiceCall(AppConstants.saveFiltersUrl(userMainId), "POST", getRequestJsonObject());
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
                Intent myIntent = new Intent(getActivity(), NavigationDrawerActivity.class);
                myIntent.putExtra("FiltesApplied", true);
                myIntent.putExtra("filterPojoObj", filterPojoObj);
                myIntent.putExtra("isFirstTime", false);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.putExtra("EXIT", true);
                startActivity(myIntent);
                getActivity().finish();
            }
        }

        private JSONObject getRequestJsonObject() {


            JSONObject requestObject = null;
            try {
                requestObject = new JSONObject();
                requestObject.accumulate("requestFrom", "categories");
                if (filterPojoObj != null) {
                    requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, user_latitude);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, user_longitude);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_RANGE, filterPojoObj.getRange());
                    requestObject.accumulate(DataKeyValues.ITEM_GET_CATEGORY, filterPojoObj.getCategory());
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_NAME, filterPojoObj.getService_name());
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_PROVIDER_NAME, "");
                    requestObject.accumulate(DataKeyValues.ITEM_TIME_RANGE, filterPojoObj.getListed_with_in());
                    requestObject.accumulate(DataKeyValues.ITEM_GET_DISTANCEIN, filterPojoObj.getKilomitersOrMiles());

                    JSONArray jsonSubCategoriesArray = new JSONArray();
                    for (int i = 0; i < filterPojoObj.getSubCategoriesList().size(); i++) {
                        jsonSubCategoriesArray.put(filterPojoObj.getSubCategoriesList().get(i));
                    }
                    requestObject.accumulate(DataKeyValues.ITEM_GET_SUBCATEGORIES, jsonSubCategoriesArray);

                    JSONArray jsonCategoriesArray = new JSONArray();
                    for (int i = 0; i < filterPojoObj.getCategoriesList().size(); i++) {
                        jsonCategoriesArray.put(filterPojoObj.getCategoriesList().get(i));
                    }
                    requestObject.accumulate(DataKeyValues.ITEMS_GET_CATEGORIES, jsonCategoriesArray);


                    JSONArray jsonSategoriesPositionArray = new JSONArray();
                    for (int i = 0; i < filterPojoObj.getSubcategoriesPositions().size(); i++) {
                        jsonSategoriesPositionArray.put(filterPojoObj.getSubcategoriesPositions().get(i));
                    }
                    requestObject.accumulate(DataKeyValues.POSITIONS, jsonSategoriesPositionArray);

                } else {
                    requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, user_latitude);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, user_longitude);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_RANGE, "500");
                    requestObject.accumulate(DataKeyValues.ITEM_GET_CATEGORY, "");
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_NAME, "");
                    requestObject.accumulate(DataKeyValues.ITEM_SERVICE_PROVIDER_NAME, "");
                    requestObject.accumulate(DataKeyValues.ITEM_TIME_RANGE, 0);
                    requestObject.accumulate(DataKeyValues.ITEM_GET_DISTANCEIN, "M");
                    JSONArray jsonCategoriesArray = new JSONArray();
                    requestObject.accumulate(DataKeyValues.ITEMS_GET_CATEGORIES, jsonCategoriesArray);
                    JSONArray jsonSubCategoriesArray = new JSONArray();
                    requestObject.accumulate(DataKeyValues.ITEM_GET_SUBCATEGORIES, jsonSubCategoriesArray);
                    JSONArray jsonSategoriesPositionArray = new JSONArray();
                    requestObject.accumulate(DataKeyValues.POSITIONS, jsonSategoriesPositionArray);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return requestObject;
        }
    }

    private class PopulateDataAsynchTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            String response = "";
            InputStream inputStream = null;
            try {
                ServiceHandler serviceHandler = new ServiceHandler();

               /* String url = AppConstants.BASE_URL
                        + "rest/filters/" + userMainId + "/getfilters";*/


                String url = AppConstants.getFiltersUrl(userMainId);
                response = serviceHandler.makeServiceCall(url, "GET", null);
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
                    if (obj.has("positions")) {
                        ArrayList<String> positions = new ArrayList<>();
                        try {
                            JSONArray array = obj.getJSONArray("positions");

                            for (int i = 0; i < array.length(); i++) {
                                positions.add(array.get(i).toString());

                            }
                        } catch (Exception e) {
                            String position = obj.getString("positions");
                            positions.add(position);

                            e.printStackTrace();
                        }
                        positionsMap = getPositions(positions);
                        adapter.setData(positionsMap);
                        adapter.notifyDataSetChanged();

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }

               /*Intent myIntent=new Intent(ServiceFilteringActivity.this,NavigationDrawerActivity.class);
               myIntent.putExtra("FiltesApplied", true);
               myIntent.putExtra("filterPojoObj", filterPojoObj);
               myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               myIntent.putExtra("EXIT", true);
               startActivity(myIntent);
               finish();*/
                // Toast.makeText(getActivity(), "dataloaded", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public static void checkAll() {
        chkAll.setChecked(true);
    }


}
