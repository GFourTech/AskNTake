package Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.askntake.admin.askntake.NavigationDrawerActivity;
import com.askntake.admin.askntake.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.BuyAndSellItemsAdapter;
import Pojo.BuyAndSellItems;
import Pojo.FilterPojo;
import AppUtils.AppConstants;
import AppUtils.ConnectionDetector;
import AppUtils.DataKeyValues;
import AppUtils.GridSpacingItemDecoration;
import AppUtils.ServiceHandler;

/**
 * Created by admin on 4/3/2017.
 */

public class ItemsDisplayingFragment_BuyAndSell extends Fragment implements SearchView.OnQueryTextListener {


    private RecyclerView recycler_view_buy_and_sell;
    private BuyAndSellItemsAdapter buyAndSellItemsAdapter;
    private List<BuyAndSellItems> buyAndSellItemsList;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    FilterPojo filterData;
    NavigationDrawerActivity activity;
    ConnectionDetector internetConnection;

    Dialog Internetdialog;

    public ItemsDisplayingFragment_BuyAndSell() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_items_displaying_buyandsell, null);


        internetConnection = new ConnectionDetector(getActivity());
        setHasOptionsMenu(true);


        swipeRefreshLayout = (SwipeRefreshLayout) root_view.findViewById(R.id.swipeRefreshLayout);
        recycler_view_buy_and_sell = (RecyclerView) root_view.findViewById(R.id.recycler_view_buy_and_sell);

        buyAndSellItemsList = new ArrayList<>();
        buyAndSellItemsAdapter = new BuyAndSellItemsAdapter(getActivity(), buyAndSellItemsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recycler_view_buy_and_sell.setLayoutManager(mLayoutManager);
        recycler_view_buy_and_sell.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
        recycler_view_buy_and_sell.setItemAnimator(new DefaultItemAnimator());
        recycler_view_buy_and_sell.setAdapter(buyAndSellItemsAdapter);

        activity = (NavigationDrawerActivity) getActivity();
        filterData = activity.getFilterData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buyAndSellItemsList.clear();

                if (internetConnection.isConnectingToInternet(getActivity())) {
                    new getItemsDispalyAysnc(filterData).execute();
                    //                  Internetdialog.dismiss();
                } else {
                    InternetConnectionAlert();
                }

            }
        });

        if (internetConnection.isConnectingToInternet(getActivity())) {
            new getItemsDispalyAysnc(filterData).execute();
//            Internetdialog.dismiss();
        } else {
            InternetConnectionAlert();
        }

        return root_view;
    }


    private void prepareAlbums() {

        for (int i = 0; i < 10; i++) {
            BuyAndSellItems buyAndSellItems = new BuyAndSellItems();
            buyAndSellItems.setItem_name("Item " + 1);
            buyAndSellItems.setItem_price("20");
            buyAndSellItemsList.add(buyAndSellItems);
        }

        buyAndSellItemsAdapter.notifyDataSetChanged();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<BuyAndSellItems> filteredModelList = filter(buyAndSellItemsList, newText);
        buyAndSellItemsAdapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<BuyAndSellItems> filter(List<BuyAndSellItems> models, String query) {
        query = query.toLowerCase();
        final List<BuyAndSellItems> filteredModelList = new ArrayList<>();
        for (BuyAndSellItems model : models) {
            final String text = model.getItem_name().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        buyAndSellItemsAdapter.setFilter(buyAndSellItemsList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    private class getItemsDispalyAysnc extends AsyncTask<String, Void, String> {
        FilterPojo filterData;

        getItemsDispalyAysnc(FilterPojo filterData) {
            this.filterData = filterData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", false, false);
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            ServiceHandler serviceHandler = new ServiceHandler();

            JSONObject requestObject = null;

            if (filterData != null) {
                requestObject = prepareFilterRequestObj(filterData);
            } else {
                requestObject = prepareDefaultRequestObj(activity.singleSelectedCategory());
            }
            String service_result = serviceHandler.makeServiceCall(AppConstants.PRODUCTS_DISPLAY_URL, "POST", requestObject);
            //String service_result = serviceHandler.makeServiceCall("http://www.askntake.com/api/rest/browse/-1/buyandsell/getProducts", "POST", requestObject);
//            String service_result = serviceHandler.makeServiceCall("http://www.askntake.com/api/rest/browse/-1/buyandsell/getProducts", "POST", requestObject);
            return service_result;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // progressDialog.dismiss();
            buyAndSellItemsList.clear();
            onItemsLoadComplete();
            JSONObject jsObj = null;

            try {

                jsObj = new JSONObject(result);
                if (jsObj.has("products")) {
                    JSONObject innerjsonObject = jsObj.getJSONObject("products");
                    BuyAndSellItems buyAndSellItems = new BuyAndSellItems();
                    buyAndSellItems.setItem_id(innerjsonObject.getString("itemid"));
                    buyAndSellItems.setItem_name(innerjsonObject.getString("name"));
                    buyAndSellItems.setItem_price(innerjsonObject.getString("price"));
                    buyAndSellItems.setItem_price_type(innerjsonObject.getString("priceType"));
                    buyAndSellItems.setItem_image_url(innerjsonObject.getString("mainImage"));
                    buyAndSellItems.setItem_distance(innerjsonObject.getString("distance"));
                    buyAndSellItems.setOnline_status(innerjsonObject.getString("ownerOnlineStatus"));
                    buyAndSellItems.setItem_owner_id(innerjsonObject.getString("ownerid"));

                    buyAndSellItemsList.add(buyAndSellItems);
                    buyAndSellItemsAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();

                try {

                    JSONObject main_jsoObject = new JSONObject(result);


                    if (jsObj.has("products")) {

                        JSONArray productsArray = main_jsoObject.getJSONArray("products");
                        for (int i = 0; i < productsArray.length(); i++) {

                            JSONObject innerJsonObject = productsArray.getJSONObject(i);
                            BuyAndSellItems buyAndSellItems = new BuyAndSellItems();
                            buyAndSellItems.setItem_id(innerJsonObject.getString("itemid"));
                            buyAndSellItems.setItem_name(innerJsonObject.getString("name"));
                            buyAndSellItems.setItem_price(innerJsonObject.getString("price"));
                            buyAndSellItems.setItem_price_type(innerJsonObject.getString("priceType"));
                            buyAndSellItems.setItem_image_url(innerJsonObject.getString("mainImage"));
                            buyAndSellItems.setItem_distance(innerJsonObject.getString("distance"));
                            buyAndSellItems.setOnline_status(innerJsonObject.getString("ownerOnlineStatus"));
                            buyAndSellItems.setItem_owner_id(innerJsonObject.getString("ownerid"));
                            buyAndSellItemsList.add(buyAndSellItems);
                        }

                        buyAndSellItemsAdapter.notifyDataSetChanged();

                    }

                } catch (Exception eq) {
                    eq.printStackTrace();

                }

            }

        }
    }


    private JSONObject prepareDefaultRequestObj(String singleSelectedCategory) {


        JSONObject requestObject = null;
        try {


            requestObject = new JSONObject();
            requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, "37.5407246");
            requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, "-77.4360481");
            requestObject.accumulate(DataKeyValues.ITEM_GET_RANGE, "500");
            requestObject.accumulate(DataKeyValues.ITEM_GET_SORTBYPRICEHTOL, false);
            requestObject.accumulate(DataKeyValues.ITEM_GET_SORTBYPRICELTOH, false);
            requestObject.accumulate(DataKeyValues.ITEM_GET_SORTBYTIME, false);
            requestObject.accumulate(DataKeyValues.ITEM_GET_SORTBYDISTANCE, true);
            requestObject.accumulate(DataKeyValues.ITEM_GET_PRODUCTNAME, "");
            requestObject.accumulate(DataKeyValues.ITEM_GET_PRICEFROM, "-1");
            requestObject.accumulate(DataKeyValues.ITEM_GET_PRICETO, "-1");
            requestObject.accumulate(DataKeyValues.ITEM_GET_PRICETYPE, "USD");
            requestObject.accumulate(DataKeyValues.ITEM_GET_TIMERANGE, "0");
            requestObject.accumulate(DataKeyValues.ITEM_GET_INVOICE, false);
            requestObject.accumulate(DataKeyValues.ITEM_GET_NEGOTIABLE, false);
            requestObject.accumulate(DataKeyValues.ITEM_GET_SHIPPING, false);
            requestObject.accumulate(DataKeyValues.ITEM_GET_MARKEDAS, false);
            requestObject.accumulate(DataKeyValues.ITEM_GET_DISTANCEIN, "M");

            JSONArray jsonCategoriesArray = new JSONArray();
            if (singleSelectedCategory != null) {
                jsonCategoriesArray.put(singleSelectedCategory);
            }
            requestObject.accumulate(DataKeyValues.ITEM_GET_SUBCATEGORIES, jsonCategoriesArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return requestObject;
    }


    private JSONObject prepareFilterRequestObj(FilterPojo filterPojo) {


        JSONObject requestObject = null;
        try {


            DataKeyValues DataKeyValues = new DataKeyValues();
            requestObject = new JSONObject();
            requestObject.accumulate(DataKeyValues.ITEM_GET_LAT, filterPojo.getLat());
            requestObject.accumulate(DataKeyValues.ITEM_GET_LANG, filterPojo.getLang());
            requestObject.accumulate(DataKeyValues.ITEM_GET_RANGE, filterPojo.getRange());
            requestObject.accumulate(DataKeyValues.ITEM_GET_SORTBYPRICEHTOL, filterPojo.isSortByPriceHtoL());
            requestObject.accumulate(DataKeyValues.ITEM_GET_SORTBYPRICELTOH, filterPojo.isSortByPriceLtoH());
            requestObject.accumulate(DataKeyValues.ITEM_GET_SORTBYTIME, filterPojo.isSortByTime());
            requestObject.accumulate(DataKeyValues.ITEM_GET_SORTBYDISTANCE, filterPojo.isSortByDistance());
            requestObject.accumulate(DataKeyValues.ITEM_GET_PRODUCTNAME, filterPojo.getProductname());
            requestObject.accumulate(DataKeyValues.ITEM_GET_PRICEFROM, filterPojo.getPriceFrom());
            requestObject.accumulate(DataKeyValues.ITEM_GET_PRICETO, filterPojo.getPriceTo());
            if (filterPojo.getPriceType() != null) {

                if (!filterPojo.getPriceType().equalsIgnoreCase("INR")) {
                    requestObject.accumulate(DataKeyValues.ITEM_GET_PRICETYPE, filterPojo.getPriceType());

                } else {
                    requestObject.accumulate(DataKeyValues.ITEM_GET_PRICETYPE, "Rs");

                }

            } else {
                requestObject.accumulate(DataKeyValues.ITEM_GET_PRICETYPE, "Rs");

            }
            requestObject.accumulate(DataKeyValues.ITEM_GET_TIMERANGE, filterPojo.getTimeRange());
            requestObject.accumulate(DataKeyValues.ITEM_GET_INVOICE, filterPojo.isInvoice());
            requestObject.accumulate(DataKeyValues.ITEM_GET_NEGOTIABLE, filterPojo.isNegotiable());
            requestObject.accumulate(DataKeyValues.ITEM_GET_SHIPPING, filterPojo.isShipping());
            requestObject.accumulate(DataKeyValues.ITEM_GET_MARKEDAS, filterPojo.isMarkedAs());
            requestObject.accumulate(DataKeyValues.ITEM_GET_DISTANCEIN, filterPojo.getDistanceIn());
            JSONArray jsonCategoriesArray = new JSONArray();

            for (int i = 0; i < filterPojo.getCategories().size(); i++) {
                jsonCategoriesArray.put(filterPojo.getCategories().get(i));
            }


            requestObject.accumulate(DataKeyValues.ITEM_GET_SUBCATEGORIES, jsonCategoriesArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return requestObject;
    }

    void onItemsLoadComplete() {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
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

                    new getItemsDispalyAysnc(filterData).execute();
                    Internetdialog.dismiss();

                }


            }
        });
        Internetdialog.show();

    }

}
