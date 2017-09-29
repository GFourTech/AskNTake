package com.askntake.admin.askntake;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.FacebookSdk;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import Adapters.ViewPagerImagesAdapter;
import AppUtils.AppConstants;
import AppUtils.ServiceHandler;

/**
 * Created by admin on 4/4/2017.
 */

public class ItemDescriptionActivity_BuyAndSell extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    ProgressDialog mProgressDialog;

    TextView chat_button, visitors_eye_count, visitors_faverates_count, item_price_value, item_name_value, item_description_value, seller_location_postal_code, seller_location_name;
    RelativeLayout main_relative_layout, inner_relative_layout, pager_layout, map_relative_layout, seller_data_layout;
    //ProgressBar progressBar_place_holder;
    ViewPager viewPager;
    ImageView visitors_eye_img, visitors_faverates_img, location_img, seller_img, invoice_img_option, negotiable_img_option, shipping_img_option;
    LinearLayout vistors_layout, location_data_layout, more_options_layout, options_linear_layout, social_sharing_layout;
    GoogleMap googleMap;
    TextView seller_name_dc_page, share_listing_text;
    View more_options_saeparator, img_seperator;
    TextView more_option_text;
    ImageView facebook_sharing, whatsapp_sharing, twitter_sharing, email_sharing, feverate_img;

    String item_name, item_description, item_type, item_category, item_invoice, item_negotiable, item_shipping, item_priceType, item_visitcount, item_price, owner_email, owner_firstname, favorite_visitcount, owner_Id;
    String owner_lastname, owner_zipcode, owner_area, owner_latitude, owner_langitude, owner_profile_img, ownerId;
    Dialog dialog;

    String product_owner_id, clicked_product_id;
    boolean feverate_img_flag;

    List<String> product_images;

    ViewPagerImagesAdapter pagerImagesAdapter;
    private int dotsCount;
    private ImageView[] dots;
    private LinearLayout pager_indicator;


    boolean no_profile_image_flag, its_my_own_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_item_decription_buy_sell);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get Intent Values
        its_my_own_product = getIntent().getBooleanExtra("myownproduct", false);
        product_owner_id = getIntent().getStringExtra("user_id");
        clicked_product_id = getIntent().getStringExtra("product_id");
        product_images = new ArrayList<>();

        chat_button = (TextView) findViewById(R.id.chat_button);
        visitors_eye_count = (TextView) findViewById(R.id.visitors_eye_count);
        visitors_faverates_count = (TextView) findViewById(R.id.visitors_faverates_count);
        item_price_value = (TextView) findViewById(R.id.item_price);
        item_name_value = (TextView) findViewById(R.id.item_name);
        item_description_value = (TextView) findViewById(R.id.item_description);
        seller_location_postal_code = (TextView) findViewById(R.id.seller_location_postal_code);
        seller_location_name = (TextView) findViewById(R.id.seller_location_name);

        //progressBar_place_holder = (ProgressBar) findViewById(R.id.progressBar_place_holder);

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 550 * NavigationDrawerActivity.curScrenHeit / NavigationDrawerActivity.defaultScrenHeit);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setLayoutParams(rlp);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        feverate_img = (ImageView) this.findViewById(R.id.visitors_eye_img);
        visitors_eye_img = (ImageView) findViewById(R.id.visitors_eye_img);
        visitors_faverates_img = (ImageView) findViewById(R.id.visitors_faverates_img);
        visitors_eye_count = (TextView) findViewById(R.id.visitors_eye_count);
        visitors_faverates_count = (TextView) findViewById(R.id.visitors_faverates_count);
        location_img = (ImageView) findViewById(R.id.location_img);
        seller_img = (ImageView) findViewById(R.id.profile_img_dc_pge);
        invoice_img_option = (ImageView) findViewById(R.id.invoice_img_option);
        negotiable_img_option = (ImageView) findViewById(R.id.negotiable_img_option);
        shipping_img_option = (ImageView) findViewById(R.id.shipping_img_option);
        more_options_saeparator = (View) findViewById(R.id.more_options_saeparator);
        img_seperator = (View) findViewById(R.id.img_seperator);
        more_option_text = (TextView) findViewById(R.id.more_option_text);
        seller_name_dc_page = (TextView) findViewById(R.id.seller_name_dc_page);
        share_listing_text = (TextView) findViewById(R.id.share_listing_text);

        inner_relative_layout = (RelativeLayout) findViewById(R.id.inner_relative_layout);
        pager_layout = (RelativeLayout) findViewById(R.id.pager_layout);
        map_relative_layout = (RelativeLayout) findViewById(R.id.map_relative_layout);
        seller_data_layout = (RelativeLayout) findViewById(R.id.seller_data_layout);

        more_options_layout = (LinearLayout) findViewById(R.id.more_options_layout);
        social_sharing_layout = (LinearLayout) findViewById(R.id.social_sharing_layout);
        location_data_layout = (LinearLayout) findViewById(R.id.location_data_layout);
        options_linear_layout = (LinearLayout) findViewById(R.id.options_linear_layout);

        facebook_sharing = (ImageView) findViewById(R.id.facebook_sharing);
        whatsapp_sharing = (ImageView) findViewById(R.id.whatsapp_sharing);
        twitter_sharing = (ImageView) findViewById(R.id.twitter_sharing);
        email_sharing = (ImageView) findViewById(R.id.email_sharing);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.google_map))
                .getMap();

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                // TODO Auto-generated method stub
               /* Intent myIntent=new Intent(ItemDescriptionActivity.this,MapDisplyingActivity.class);
                myIntent.putExtra("latitudeOfMap", Double.parseDouble(owner_latitude));
                myIntent.putExtra("longitudeOfMap", Double.parseDouble(owner_langitude));
                startActivity(myIntent);*/

            }
        });

        if (its_my_own_product) {
            chat_button.setText("EDIT");
        }

        viewPager.setOnPageChangeListener(this);
        new ItemDetailsAsyncTask().execute();

        more_options_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                dialog = new Dialog(ItemDescriptionActivity_BuyAndSell.this); /*android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);*/
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_more_options_layout_dc);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.transperent_bg_for_more_options);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(lp);
                RelativeLayout alert_rel_layout = (RelativeLayout) dialog.findViewById(R.id.alert_rel_layout);
                ImageView negotioable_img = (ImageView) dialog.findViewById(R.id.negotioable_img);
                TextView negotioable_option = (TextView) dialog.findViewById(R.id.negotioable_option);
                ImageView recipt_img = (ImageView) dialog.findViewById(R.id.recipt_img);
                TextView recipt_option = (TextView) dialog.findViewById(R.id.recipt_option);
                LinearLayout shipping_main_layout = (LinearLayout) dialog.findViewById(R.id.shipping_main_layout);
                ImageView shipping_img = (ImageView) dialog.findViewById(R.id.shipping_img);
                TextView shipping_option = (TextView) dialog.findViewById(R.id.shipping_option);
                ImageView tick_mark_recipt = (ImageView) dialog.findViewById(R.id.tick_mark_recipt);
                ImageView tick_mark_negotiable = (ImageView) dialog.findViewById(R.id.tick_mark_negotiable);
                ImageView tick_shipping = (ImageView) dialog.findViewById(R.id.tick_shipping);

                alert_rel_layout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

                if (item_invoice.equalsIgnoreCase("true")) {
                    recipt_img.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.ic_invoice_selected));
                    tick_mark_recipt.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.ic_action_check_blue));
                    recipt_option.setTextColor(getResources().getColor(R.color.logo_blue));
                }
                if (item_negotiable.equalsIgnoreCase("true")) {

                    negotioable_img.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.ic_negble_selected));
                    tick_mark_negotiable.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.ic_action_check_blue));
                    negotioable_option.setTextColor(getResources().getColor(R.color.logo_blue));
                }
                if (item_shipping.equalsIgnoreCase("true")) {

                    shipping_img.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.ic_shipping_selected));
                    tick_shipping.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.ic_action_check_blue));
                    shipping_option.setTextColor(getResources().getColor(R.color.logo_blue));
                }

                dialog.show();
            }
        });
        facebook_sharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //dialog.cancel();
                //Toast.makeText(getApplicationContext(), "sharing...", Toast.LENGTH_LONG).show();


            }
        });
        whatsapp_sharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                WhatsAppShare(arg0);
            }
        });

        twitter_sharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String originalMessage = "Look what I just found on Ask-n-Take!\n" + item_name + "\n\n" + item_description + "http://www.askntake.com/";

                String originalMessageEscaped = null;
                try {
                    originalMessageEscaped = String.format(
                            "https://twitter.com/intent/tweet?source=webclient&text=%s",
                            URLEncoder.encode(originalMessage, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (originalMessageEscaped != null) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(originalMessageEscaped));
                    startActivity(i);
                } else {
                    // Some Error
                }
            }
        });
        email_sharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PackageInfo packageinfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);

                    Intent intent = new Intent("android.intent.action.VIEW");
                    StringBuilder stringbuilder = new StringBuilder("mailto:");
                    stringbuilder.append("?subject=").append((new StringBuilder()).append(item_name + " On Ask-n-Take").toString());
                    stringbuilder.append("&body=").append(Uri.encode("I thought this might be perfect for you, and it's for sale on Ask-n-Take:\n\n" + item_name + "\n\n" + item_description + " " + "http://www.askntake.com/"));
                    intent.setData(Uri.parse(stringbuilder.toString()));
                    startActivity(intent);
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

    }


    public class ItemDetailsAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(ItemDescriptionActivity_BuyAndSell.this, "", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... arg0) {

            ServiceHandler sh = new ServiceHandler();


            String jsonStr = sh.makeServiceCall(AppConstants.getProductDescriptionUrl(product_owner_id, clicked_product_id), "GET", null);
            String responceStatus = "";
            try {

                if (jsonStr != null) {
                    JSONObject itemsObj = new JSONObject(jsonStr);
                    if (itemsObj.has("name")) {
                        item_name = itemsObj.getString("name");
                    }
                    if (itemsObj.has("description")) {
                        item_description = itemsObj.getString("description");
                    }
                    if (itemsObj.has("type")) {
                        item_type = itemsObj.getString("type");
                    }
                    if (itemsObj.has("price")) {
                        item_price = itemsObj.getString("price");
                    }
                    if (itemsObj.has("category")) {
                        item_category = itemsObj.getString("category");
                    }
                    if (itemsObj.has("invoice")) {
                        item_invoice = itemsObj.getString("invoice");
                    }
                    if (itemsObj.has("negotiable")) {
                        item_negotiable = itemsObj.getString("negotiable");
                    }
                    if (itemsObj.has("shipping")) {
                        item_shipping = itemsObj.getString("shipping");
                    }

                    if (itemsObj.has("priceType")) {
                        item_priceType = itemsObj.getString("priceType");
                    }

                    if (itemsObj.has("visitcount")) {
                        item_visitcount = itemsObj.getString("visitcount");
                    }

                    if (itemsObj.has("favoritesCount")) {
                        favorite_visitcount = itemsObj.getString("favoritesCount");
                    }
                    if (itemsObj.has("isFavorite")) {

                        if (itemsObj.getString("isFavorite").equalsIgnoreCase("true")) {
                            feverate_img_flag = true;
                        } else {
                            feverate_img_flag = false;
                        }
                    }

                    if (itemsObj.has("owner")) {
                        JSONObject ownerObj = itemsObj.getJSONObject("owner");

                        if (ownerObj.has("ownerId")) {
                            owner_Id = ownerObj.getString("ownerId");
                        }
                        if (ownerObj.has("email")) {
                            owner_email = ownerObj.getString("email");
                        }
                        if (ownerObj.has("firstname")) {
                            owner_firstname = ownerObj.getString("firstname");
                        }

                        if (ownerObj.has("lastname")) {
                            owner_lastname = ownerObj.getString("lastname");
                        }
                        if (ownerObj.has("zipcode")) {
                            owner_zipcode = ownerObj.getString("zipcode");
                        }
                        if (ownerObj.has("area")) {
                            owner_area = ownerObj.getString("area");
                        }
                        if (ownerObj.has("latitude")) {
                            owner_latitude = ownerObj.getString("latitude");
                        }
                        if (ownerObj.has("langitude")) {
                            owner_langitude = ownerObj.getString("langitude");
                        }

                        if (ownerObj.has("image")) {
                            owner_profile_img = ownerObj.getString("image");
                        } else {
                            no_profile_image_flag = true;
                        }

                        if (ownerObj.has("ownerId")) {
                            ownerId = ownerObj.getString("ownerId");
                        }

                    }

                    try {
                        JSONArray product_imagesArray = itemsObj.getJSONArray("images");
                        for (int i = 0; i < product_imagesArray.length(); i++) {
                            product_images.add(i, product_imagesArray.getString(i));
                        }

                    } catch (JSONException jj) {
                        jj.printStackTrace();
                        product_images.add(itemsObj.getString("images"));
                    }
                    responceStatus = "success";


                } else {
                    responceStatus = "fail";
                }

            } catch (Exception e) {

                responceStatus = "fail";
            }

            return responceStatus;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();


            if (result.equalsIgnoreCase("success")) {

                pagerImagesAdapter = new ViewPagerImagesAdapter(ItemDescriptionActivity_BuyAndSell.this, product_images);
                viewPager.setAdapter(pagerImagesAdapter);
                setUiPageViewController();
                viewPager.setCurrentItem(0);


                item_price_value.setText(item_priceType + " " + item_price);
                visitors_eye_count.setText(item_visitcount);
                visitors_faverates_count.setText(favorite_visitcount);
                item_name_value.setText(item_name + " (" + item_category + ")");
                item_description_value.setText(item_description);
                seller_location_postal_code.setText(owner_area);
                //seller_location_name.setText(owner_zipcode);
                seller_name_dc_page.setText(owner_firstname + owner_lastname);

                if (item_invoice.equalsIgnoreCase("true")) {


                    Glide.with(getApplicationContext()).load(R.drawable.ic_invoice_selected).into(invoice_img_option);


                }
                if (item_negotiable.equalsIgnoreCase("true")) {


                    Glide.with(getApplicationContext()).load(R.drawable.ic_negble_selected).into(negotiable_img_option);

                }
                if (item_shipping.equalsIgnoreCase("true")) {

                    Glide.with(getApplicationContext()).load(R.drawable.ic_shipping_selected).into(shipping_img_option);
                }


                Glide.with(getApplicationContext()).load(owner_profile_img).asBitmap().centerCrop().placeholder(R.mipmap.not_login_img).into(new BitmapImageViewTarget(seller_img) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        seller_img.setImageDrawable(circularBitmapDrawable);
                    }
                });

                // Glide.with(getApplicationContext()).load(owner_profile_img).into(seller_img);

                MarkerOptions mp = new MarkerOptions();
                mp.position(new LatLng(Double.parseDouble(owner_latitude), Double.parseDouble(owner_langitude)));
                mp.title("my position");
                googleMap.addMarker(mp).setVisible(false);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Double.parseDouble(owner_latitude), Double.parseDouble(owner_langitude)), 12));
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.getUiSettings().setAllGesturesEnabled(false);


            }

        }
    }

    private void setUiPageViewController() {

        dotsCount = pagerImagesAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_description_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.report_this_product) {
            //Toast.makeText(getApplicationContext(),"report",Toast.LENGTH_SHORT).show();
            Intent report_intent = new Intent(getApplicationContext(), ReportThisProductScreenActivity.class);
            startActivity(report_intent);

            return true;
        }
        if (item.getItemId() == R.id.share) {
            // Toast.makeText(getApplicationContext(),"report",Toast.LENGTH_SHORT).show();
            String shareBody = "Look what I just found on Ask-n-Take!\n" + item_name + "\n\n" + item_description + "\n" + "http://www.askntake.com/";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share with ..."));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void WhatsAppShare(View view) {

        PackageManager pm = getPackageManager();
        try {


            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");

            ///Uri screenshotUri = Uri.parse("https://mealvillagetest33.appspot.com/index.jsp");
            Uri screenshotUri = Uri.parse("http://www.askntake.com/");
            String shareBody = "Look what I just found on Ask-n-Take!\n" + item_name + "\n\n" + item_description + screenshotUri;
            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

    }


}
