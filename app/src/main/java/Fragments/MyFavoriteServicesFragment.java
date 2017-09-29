package Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.askntake.admin.askntake.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.MyFavoritesListAdapter;
import Adapters.MyServicesListAdapter;
import AppUtils.AppConstants;
import AppUtils.DataKeyValues;
import AppUtils.GridSpacingItemDecoration;
import AppUtils.ServiceHandler;
import Pojo.ItemServicePojo;

/**
 * Created by admin on 3/29/2017.
 */

public class MyFavoriteServicesFragment extends Fragment {
    private RecyclerView recycler_view_buy_and_sell_items;
    private MyFavoritesListAdapter myservicesAdapter;
    ProgressDialog progressDialog;
    List<ItemServicePojo> productItemsListData;
    String user_id;

    TextView no_favorites_layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_fav_services, null);
        recycler_view_buy_and_sell_items = (RecyclerView) root_view.findViewById(R.id.recycler_view_myservices);

        no_favorites_layout = (TextView) root_view.findViewById(R.id.no_favorites_layout);

        productItemsListData = new ArrayList<>();
        myservicesAdapter = new MyFavoritesListAdapter(getActivity(), productItemsListData);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recycler_view_buy_and_sell_items.setLayoutManager(mLayoutManager);
        recycler_view_buy_and_sell_items.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
        recycler_view_buy_and_sell_items.setItemAnimator(new DefaultItemAnimator());
        recycler_view_buy_and_sell_items.setAdapter(myservicesAdapter);

        SharedPreferences login_data_pref = getActivity().getSharedPreferences(DataKeyValues.USER_DATA_PREF, Context.MODE_PRIVATE);
        user_id = login_data_pref.getString(DataKeyValues.USER_USERID, null);

        new GetSellingItemsListAsync().execute();
        return root_view;
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private class GetSellingItemsListAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            ServiceHandler serviceHandler = new ServiceHandler();
            String result = serviceHandler.makeServiceCall(AppConstants.getFavServicesUrl(Long.parseLong(user_id)), "GET", null);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                String projuctsObj_Name = "services";
                JSONObject mainJsonObj;
                String pro_itemId = "";
                String pro_name = "";
                String pro_mainImage = "";
                JSONObject productObj = null;
                try {
                    mainJsonObj = new JSONObject(result);
                    JSONArray productsArray = null;

                    if (mainJsonObj.length() == 0) {

                        no_favorites_layout.setVisibility(View.VISIBLE);
                        recycler_view_buy_and_sell_items.setVisibility(View.GONE);

                    } else {
                        try {
                            productsArray = mainJsonObj.getJSONArray(projuctsObj_Name);
                        } catch (Exception e) {
                            no_favorites_layout.setVisibility(View.VISIBLE);
                            recycler_view_buy_and_sell_items.setVisibility(View.GONE);
                            productsArray = null;
                            try {
                                productObj = mainJsonObj.getJSONObject(projuctsObj_Name);
                            } catch (Exception e2) {
                                no_favorites_layout.setVisibility(View.VISIBLE);
                                recycler_view_buy_and_sell_items.setVisibility(View.GONE);
                                productObj = null;
                            }
                        }
                        if (productsArray != null) {

                            int j = productsArray.length();


                            if (j != 0) {
                                for (int i = 0; i < productsArray.length(); i++) {
                                    JSONObject productDataObj = productsArray.getJSONObject(i);


                                    if(productDataObj.length()!=0)
                                    {
                                        if (productDataObj.has("serviceId") && productDataObj.has("servicename") && productDataObj.has("mainImage")) {
                                            pro_itemId = productDataObj.getString("serviceId");
                                            pro_name = productDataObj.getString("servicename");
                                            pro_mainImage = productDataObj.getString("mainImage");
                                            //ItemServicePojo productsItemPojo=new ItemServicePojo(pro_itemId, pro_name, pro_mainImage, pro_price,pricetype,"3",false);
                                            ItemServicePojo productsItemPojo = new ItemServicePojo();
                                            productsItemPojo.setService_image(pro_mainImage);
                                            productsItemPojo.setService_name(pro_name);
                                            productsItemPojo.setService_id(pro_itemId);
                                            productItemsListData.add(productsItemPojo);
                                        }

                                    }
                                    else
                                    {
                                        no_favorites_layout.setVisibility(View.VISIBLE);
                                        recycler_view_buy_and_sell_items.setVisibility(View.GONE);
                                    }
                                }

                            } else {
                                no_favorites_layout.setVisibility(View.VISIBLE);
                                recycler_view_buy_and_sell_items.setVisibility(View.GONE);
                            }
                        } else if (productObj != null) {
                            pro_itemId = productObj.getString("serviceId");
                            pro_name = productObj.getString("servicename");
                            pro_mainImage = productObj.getString("mainImage");
                            // ProductItemsPojo productsItemPojo=new ProductItemsPojo(pro_itemId, pro_name, pro_mainImage, pro_price,pricetype,"3",false);
                            ItemServicePojo productsItemPojo = new ItemServicePojo();
                            productsItemPojo.setService_image(pro_mainImage);
                            productsItemPojo.setService_name(pro_name);
                            productsItemPojo.setService_id(pro_itemId);
                            productItemsListData.add(productsItemPojo);
                        }
                        myservicesAdapter.notifyDataSetChanged();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
            }

        }
    }

}