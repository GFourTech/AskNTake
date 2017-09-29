package com.askntake.admin.askntake;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Pojo.FilterPojo;
import AppUtils.Helper;

/**
 * Created by admin on 4/3/2017.
 */

public class BuyAndSellFileringActivity extends AppCompatActivity {

    ListView categories_list;
    String[] category_names;
    ArrayAdapter<String> adapter;
    CheckBox chkAll;
    TextView search_location_heading_textView;
    EditText search_product_heading_textView, text_from_editText, text_to_editText;
    RadioButton radio_kilometers_1, radio_miles_2,
            radio_distance_1, radio_distance_2, radio_distance_3,
            radio_distance_4, radio_24_hours, radio_7_days, radio_30_days,
            radio_All_Listings, radio_sort_by_distance, radio_sort_by_price_low_to_high, radio_sort_by_price_high_to_low, radio_sort_by_most_recently_published;

    CheckBox text_invoice_receipt, text_negotiable_textView, text_shipping_textView;
    LinearLayout apply_layout, reset_layout;
    Spinner priceType_spinner;

    double def_lat_value, def_long_value;
    String def_location;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_and_sell_filtering);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        categories_list = (ListView) findViewById(R.id.categories_list);
        category_names = getApplicationContext().getResources().getStringArray(R.array.categories_list);
        adapter = new ArrayAdapter<String>(this, R.layout.checked_row, category_names);
        categories_list.setAdapter(adapter);
        Helper helper = new Helper();
        helper.getListViewSize(categories_list);
        chkAll = (CheckBox) findViewById(R.id.all_categories_chbx);
        chkAll.setChecked(true);
        chkAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                CheckBox chk = (CheckBox) v;
                int itemCount = categories_list.getCount();
                for (int i = 0; i < itemCount; i++) {
                    categories_list.setItemChecked(i, chk.isChecked());
                }

            }
        });

        categories_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox selected_view = (CheckBox) categories_list.getChildAt(i);
                Toast.makeText(getApplicationContext(), "Selected Views" + selected_view.getText(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //

        search_product_heading_textView = (EditText) findViewById(R.id.search_product_heading_textView);
        search_location_heading_textView = (TextView) findViewById(R.id.search_location_heading_textView);
        text_from_editText = (EditText) findViewById(R.id.text_from_editText);
        text_to_editText = (EditText) findViewById(R.id.text_to_editText);

        radio_kilometers_1 = (RadioButton) findViewById(R.id.radio_kilometers_1);
        radio_miles_2 = (RadioButton) findViewById(R.id.radio_miles_2);
        radio_distance_1 = (RadioButton) findViewById(R.id.radio_distance_1);
        radio_distance_2 = (RadioButton) findViewById(R.id.radio_distance_2);
        radio_distance_3 = (RadioButton) findViewById(R.id.radio_distance_3);
        radio_distance_4 = (RadioButton) findViewById(R.id.radio_distance_4);

        radio_24_hours = (RadioButton) findViewById(R.id.radio_24_hours);
        radio_7_days = (RadioButton) findViewById(R.id.radio_7_days);
        radio_30_days = (RadioButton) findViewById(R.id.radio_30_days);
        radio_All_Listings = (RadioButton) findViewById(R.id.radio_All_Listings);

        radio_sort_by_distance = (RadioButton) findViewById(R.id.radio_sort_by_distance);
        radio_sort_by_price_low_to_high = (RadioButton) findViewById(R.id.radio_sort_by_price_low_to_high);
        radio_sort_by_price_high_to_low = (RadioButton) findViewById(R.id.radio_sort_by_price_high_to_low);
        radio_sort_by_most_recently_published = (RadioButton) findViewById(R.id.radio_sort_by_most_recently_published);

        text_invoice_receipt = (CheckBox) findViewById(R.id.text_invoice_receipt);
        text_negotiable_textView = (CheckBox) findViewById(R.id.text_negotiable_textView);
        text_shipping_textView = (CheckBox) findViewById(R.id.text_shipping_textView);

        priceType_spinner = (Spinner) findViewById(R.id.priceType_spinner);

        apply_layout = (LinearLayout) findViewById(R.id.apply_layout);
        reset_layout = (LinearLayout) findViewById(R.id.reset_layout);
        //check filters applyed or not
        new  setDefaulters().execute();

        apply_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FilterPojo filterPojo = new FilterPojo();

                filterPojo.setProductname(search_product_heading_textView.getText().toString());
                filterPojo.setPriceFrom(text_from_editText.getText().toString());
                filterPojo.setPriceTo(text_to_editText.getText().toString());
                filterPojo.setPriceType(priceType_spinner.getSelectedItem().toString());

                if (radio_miles_2.isChecked()) {
                    filterPojo.setDistanceIn("M");
                } else {
                    filterPojo.setDistanceIn("K");
                }

                if (radio_distance_1.isChecked()) {
                    filterPojo.setRange(1);
                }
                if (radio_distance_2.isChecked()) {
                    filterPojo.setRange(5);
                }
                if (radio_distance_3.isChecked()) {
                    filterPojo.setRange(10);
                }
                if (radio_distance_4.isChecked()) {
                    filterPojo.setRange(500);
                }

                if (radio_sort_by_distance.isChecked()) {
                    filterPojo.setSortByDistance(true);
                } else {
                    filterPojo.setSortByDistance(false);
                }

                if (radio_sort_by_price_low_to_high.isChecked()) {
                    filterPojo.setSortByPriceLtoH(true);
                } else {
                    filterPojo.setSortByPriceLtoH(false);
                }
                if (radio_sort_by_price_high_to_low.isChecked()) {
                    filterPojo.setSortByPriceHtoL(true);
                } else {
                    filterPojo.setSortByPriceHtoL(false);
                }
                if (text_invoice_receipt.isChecked())

                {
                    filterPojo.setInvoice(true);
                } else {
                    filterPojo.setInvoice(false);
                }
                if (text_negotiable_textView.isChecked()) {
                    filterPojo.setNegotiable(true);
                } else {
                    filterPojo.setNegotiable(false);
                }

                if (text_shipping_textView.isChecked()) {
                    filterPojo.setShipping(true);
                } else {
                    filterPojo.setShipping(false);
                }
                if (text_invoice_receipt.isChecked() && text_shipping_textView.isChecked() && text_negotiable_textView.isChecked()) {
                    filterPojo.setMarkedAs(true);
                } else {
                    filterPojo.setMarkedAs(false);
                }

                ArrayList<String> categories = new ArrayList<String>();
                filterPojo.setCategories(categories);

                Log.d("ddaaa", filterPojo.toString());

                Intent filterItent = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                filterItent.putExtra("filterdata", filterPojo);
                filterItent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(filterItent);
                finish();
            }
        });
        reset_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new  setDefaulters().execute();
            }
        });

    }




    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private class setDefaulters extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // progressDialog = ProgressDialog.show(getApplicationContext(), "", "Please wait...", false, false);



        }
        @Override
        protected String doInBackground(String... strings) {

            try {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkLocationPermission()) {

                        LocationManager mLocationManager = (LocationManager) getApplicationContext()
                                .getSystemService(getApplicationContext().LOCATION_SERVICE);

                        List<String> providers = mLocationManager.getProviders(true);
                        Location bestLocation = null;
                        for (String provider : providers) {
                            Location l = mLocationManager.getLastKnownLocation(provider);

                            if (l == null) {
                                continue;
                            } else {
                                bestLocation = l;
                            }
                            if (bestLocation == null
                                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                                bestLocation = l;
                            }
                        }

                        if (bestLocation!=null) {
                            def_lat_value = bestLocation.getLatitude();
                            def_long_value = bestLocation.getLongitude();

                        }

                    }

                } else {
                    TrackGPS curent_location = new TrackGPS(BuyAndSellFileringActivity.this);
                    if (curent_location.canGetLocation()) {
                        LocationManager mLocationManager = (LocationManager) getApplicationContext()
                                .getSystemService(getApplicationContext().LOCATION_SERVICE);

                        List<String> providers = mLocationManager.getProviders(true);
                        Location bestLocation = null;
                        for (String provider : providers) {
                            Location l = mLocationManager.getLastKnownLocation(provider);

                            if (l == null) {
                                continue;
                            } else {
                                bestLocation = l;
                            }
                            if (bestLocation == null
                                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                                bestLocation = l;
                            }
                        }

                        if (bestLocation!=null) {
                            def_lat_value = bestLocation.getLatitude();
                            def_long_value = bestLocation.getLongitude();

                        }


                    } else {
                        curent_location.showSettingsAlert();
                    }
                }
                def_location = getDisplyingAddress(def_lat_value, def_long_value);
            } catch (Exception e) {

                e.printStackTrace();
            }
            return "success";

        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
          //  progressDialog.dismiss();
            search_location_heading_textView.setText(def_location);
            //get Currency Type
            Helper helper = new Helper();
            ArrayAdapter myAdap = (ArrayAdapter) priceType_spinner.getAdapter();
            int priceTypePostion = 0;
            if (helper.getCurrencyType(getDefCountryCode(def_lat_value, def_long_value)) != null) {
                priceTypePostion = myAdap.getPosition(helper.getCurrencyType(getDefCountryCode(def_lat_value, def_long_value)));
            }
            if (priceTypePostion != -1) {
                priceType_spinner.setSelection(priceTypePostion);
            }
            //set values to view
            search_product_heading_textView.setText("");
            text_from_editText.setText("");
            text_to_editText.setText("");


            radio_kilometers_1.setChecked(false);
            radio_miles_2.setChecked(true);
            radio_distance_1.setChecked(false);
            radio_distance_2.setChecked(false);
            radio_distance_3.setChecked(false);
            radio_distance_4.setChecked(true);

            radio_24_hours.setChecked(false);
            radio_7_days.setChecked(false);
            radio_30_days.setChecked(false);
            radio_All_Listings.setChecked(true);

            radio_sort_by_distance.setChecked(true);
            radio_sort_by_price_low_to_high.setChecked(false);
            radio_sort_by_price_high_to_low.setChecked(false);
            radio_sort_by_most_recently_published.setChecked(false);

            text_invoice_receipt.setChecked(false);
            text_negotiable_textView.setChecked(false);
            text_shipping_textView.setChecked(false);
        }

    }

    private String getDisplyingAddress(Double lat_value, Double long_value) {

        Address locationAddressData = LocationAddressData(getApplicationContext(), lat_value, long_value);
        if (locationAddressData != null) {

            String address_data = "";
            if (locationAddressData.getLocality() != null) {
                address_data = locationAddressData.getLocality() + ",";
            }
            if (locationAddressData.getAdminArea() != null) {
                address_data = address_data + locationAddressData.getAdminArea() + ",";
            }

            if (locationAddressData.getCountryCode() != null) {
                address_data = address_data + locationAddressData.getCountryCode();
            }

            return address_data;
        } else {
            return "";
        }

    }

    private Address LocationAddressData(Context cntx, Double latitude,
                                        Double logitude) {

        Geocoder geocoder = new Geocoder(cntx, Locale.getDefault());
        Address address = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, logitude,
                    1);
            if (addressList != null && addressList.size() > 0) {
                address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }
                /*
                 * String locality=address.getLocality()).append("\n");
				 * sb.append(address.getPostalCode()).append("\n");
				 * sb.append(address.getCountryName());
				 */
                // result = sb.toString();
            }
        } catch (IOException e) {
            Log.e("testtag", "Unable connect to Geocoder", e);

        }

        return address;
    }

    private String getDefCountryCode(double lat_value, double long_value) {
        Address locationAddressData = LocationAddressData(getApplicationContext(), lat_value, long_value);

        if (locationAddressData != null) {
            if (locationAddressData.getCountryCode() != null) {
                return locationAddressData.getCountryCode();
            } else {
                return "IN";

            }

        } else {
            return "IN";
        }

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
