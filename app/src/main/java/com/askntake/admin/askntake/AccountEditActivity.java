package com.askntake.admin.askntake;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import AppUtils.AppConstants;
import AppUtils.DataKeyValues;
import AppUtils.ServiceHandler;
import AppUtils.Utility;

/**
 * Created by admin on 4/1/2017.
 */

public class AccountEditActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, ResultCallback<People.LoadPeopleResult>, GoogleApiClient.OnConnectionFailedListener {
    ImageView cross_img, apply_img, profile_picture, profile_navigation_icon,
            user_name_navigation, profile_password_navigation_icon, location_navigation_icon,
            email_navigation_icon, logout_navigation_icon, logout_img_logo;
    EditText profile_user_editText, email_edit_text;
    TextView location_text_View, logout_text_view;
    RadioGroup gender_radio_group;
    String UserId_Main;
    private GoogleApiClient mGoogleApiClient;
    boolean mSignInClicked;


    RelativeLayout profile_password_layout, apply_main_layout;
    RelativeLayout profile_logout_layout, profile_location_layout;

    ProgressDialog progressDialog;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String userChoosenTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.askntake.admin.askntake", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_account_edit_screen);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        cross_img = (ImageView) findViewById(R.id.cross_img);
        apply_img = (ImageView) findViewById(R.id.apply_img);
        profile_picture = (ImageView) findViewById(R.id.profile_picture);
        profile_navigation_icon = (ImageView) findViewById(R.id.profile_navigation_icon);
        user_name_navigation = (ImageView) findViewById(R.id.user_name_navigation);
        profile_password_navigation_icon = (ImageView) findViewById(R.id.profile_password_navigation_icon);
        location_navigation_icon = (ImageView) findViewById(R.id.location_navigation_icon);
        email_navigation_icon = (ImageView) findViewById(R.id.email_navigation_icon);
        logout_navigation_icon = (ImageView) findViewById(R.id.logout_navigation_icon);
        logout_img_logo = (ImageView) findViewById(R.id.logout_img_logo);
        profile_user_editText = (EditText) findViewById(R.id.profile_user_editText);
        email_edit_text = (EditText) findViewById(R.id.email_edit_text);
        gender_radio_group = (RadioGroup) findViewById(R.id.gender_radio_group);
        location_text_View = (TextView) findViewById(R.id.location_text_View);
        logout_text_view = (TextView) findViewById(R.id.logout_text_view);


        profile_password_layout = (RelativeLayout) findViewById(R.id.profile_password_layout);
        apply_main_layout = (RelativeLayout) findViewById(R.id.apply_main_layout);

        profile_logout_layout = (RelativeLayout) findViewById(R.id.profile_logout_layout);
        profile_location_layout = (RelativeLayout) findViewById(R.id.profile_location_layout);


        SharedPreferences sharedPreferences = getSharedPreferences(DataKeyValues.USER_DATA_PREF, MODE_PRIVATE);
        String name = sharedPreferences.getString("fb_username", null);
        String email = sharedPreferences.getString("fb_email", null);
        UserId_Main = sharedPreferences.getString(DataKeyValues.USER_USERID, null);


        if (sharedPreferences.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {


            profile_user_editText.setText(name);
            Glide.with(getApplicationContext()).load(AppConstants.IMG_BASE_URL+sharedPreferences.getString("image", null)).asBitmap().centerCrop().placeholder(R.mipmap.not_login_img).into(new BitmapImageViewTarget(profile_picture) {


                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    profile_picture.setImageDrawable(circularBitmapDrawable);
                }
            });
            profile_user_editText.setVisibility(View.VISIBLE);

        } else {
            Glide.with(getApplicationContext()).load(R.mipmap.not_login_img).into(profile_picture);

            profile_user_editText.setVisibility(View.VISIBLE);
        }

        email_edit_text.setText(email);

        /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                TrackGPS curent_location = new TrackGPS(AccountEditActivity.this);
                if (curent_location.canGetLocation()) {
                    double latitude = curent_location.getLatitude();
                    double longitude = curent_location.getLongitude();
                    String location_name = getDisplyingAddress(latitude, longitude);
                    location_text_View.setText(location_name);
                    // setGoogleMap(googleMap, latitude, longitude,location_name);

                } else {
                    curent_location.showSettingsAlert();
                }

            }

        } else {
            TrackGPS curent_location = new TrackGPS(AccountEditActivity.this);
            if (curent_location.canGetLocation()) {
                double longitude = curent_location.getLongitude();
                double latitude = curent_location.getLatitude();
                String location_name = getDisplyingAddress(latitude, longitude);
                location_text_View.setText(location_name);
                // setGoogleMap(googleMap, latitude, longitude, "My Location");

            } else {
                curent_location.showSettingsAlert();
            }
        }*/

        cross_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "reseted", Toast.LENGTH_SHORT).show();
            }
        });
      /*  apply_main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "apply_img", Toast.LENGTH_SHORT).show();
            }
        });*/

        apply_main_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


              /*  if (email_edit_text.getText().toString().length() > 0) {
                    if (email_edit_text.getText().toString().matches(emailPattern) && email_edit_text.getText().toString().length() > 0) {
                        new UpdateUserDataToServer().execute();
                    } else {
                        Toast.makeText(getApplicationContext(),"Please provide Valid Email",Toast.LENGTH_SHORT).show();

                    }
                } else {
                    new UpdateUserDataToServer().execute();
                }*/
            }
        });


        File imgFile = new File("/sdcard/ImageCapture/wallpaper.jpeg");

        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


            profile_picture.setImageBitmap(myBitmap);


        }
        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
                //Toast.makeText(getApplicationContext(), "profile_picture change ", Toast.LENGTH_SHORT).show();
            }
        });
        profile_navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), " go to profile_navigation_icon", Toast.LENGTH_SHORT).show();
            }
        });
        user_name_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "user_name_navigation", Toast.LENGTH_SHORT).show();
            }
        });
        profile_password_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pwdChangeIntent = new Intent(AccountEditActivity.this, AccountEditChangePassword.class);
                startActivity(pwdChangeIntent);

                //Toast.makeText(getApplicationContext(), "profile_password_navigation_icon", Toast.LENGTH_SHORT).show();
            }
        });
        profile_location_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(getApplicationContext(),"location_navigation_icon",Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(AccountEditActivity.this, LocationSettingsActivity.class);
                // myIntent.putExtra("updatelocation", true);
                // myIntent.putExtra("requestFrom", "account");
                startActivity(myIntent);
                finish();


            }
        });
        profile_logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LogoutAsync().execute();

                //  Toast.makeText(getApplicationContext(),"logout_navigation_icon",Toast.LENGTH_SHORT).show();
            }
        });
        email_navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "email_navigation_icon", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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

    // and longitude.................................///
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mSignInClicked = false;

        // updateUI(true);
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();
    }

    @Override
    public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class LogoutAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = getJSONObject();
            String service_result = serviceHandler.makeServiceCall(AppConstants.logoutapp(UserId_Main), "POST", requestObject);

            return service_result;
        }

        @Override
        protected void onPostExecute(String service_result) {
            super.onPostExecute(service_result);


            boolean status = false;
            try {
                JSONObject jsObj = new JSONObject(service_result);
                if (jsObj.has("status")) {
                    status = jsObj.getBoolean("status");
                    if (status) {

                        boolean gmail_logout = false;
                        SharedPreferences UserPref = AppConstants.preferencesData(getApplicationContext());
                        SharedPreferences.Editor prefEditor = UserPref.edit();
                        gmail_logout = UserPref.getBoolean(AppConstants.GMAIL_LOGIN, false);
                        prefEditor.clear();
                        prefEditor.commit();


                        SharedPreferences fb_data_pref = AppConstants.preferencesData(getApplicationContext());
                        SharedPreferences.Editor search_editor = fb_data_pref.edit();
                        search_editor.clear();
                        search_editor.commit();

                        //deleteCache(getApplicationContext());

                        trimCache();


                        LoginManager.getInstance().logOut();


                        if (mGoogleApiClient != null) {

                            try {

                                boolean sc=mGoogleApiClient.isConnected();

                                if (mGoogleApiClient.isConnected()) {

                                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                                    mGoogleApiClient.disconnect();
                                    //mGoogleApiClient.connect();
                                    // updateUI(false);
                                    System.err.println("LOG OUT ^^^^^^^^^^^^^^^^^^^^ SUCESS");

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            /*if (mGoogleApiClient.isConnected()) {

                                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                                mGoogleApiClient.disconnect();
                                //mGoogleApiClient.connect();
                                // updateUI(false);
                                System.err.println("LOG OUT ^^^^^^^^^^^^^^^^^^^^ SUCESS");

                            }*/
                        }


                        Intent intent = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                        intent.putExtra("gmail_logout", gmail_logout);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Login failed..", Toast.LENGTH_SHORT).show();

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
            requestObject.accumulate(DataKeyValues.MESSAGE, "message");
            requestObject.accumulate(DataKeyValues.RESPONSE, "response");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject;

    }


/* public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }*/

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void trimCache() {
        try {
            File dir = getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Camera"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Gallery"))
                        galleryIntent();
                } else {
                    //code for deny

                }
                break;
        }

    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery",
                "Remove Photo"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AccountEditActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(AccountEditActivity.this);

                if (items[item].equals("Camera")) {
                    userChoosenTask = "Camera";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Gallery")) {
                    userChoosenTask = "Gallery";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Remove Photo")) {
                    userChoosenTask = "Remove Photo";
                    File dir = new File(Environment.getExternalStorageDirectory(), "ImageCapture");
                    if (dir.isDirectory()) {
                        String[] children = dir.list();
                        for (int i = 0; i < children.length; i++) {
                            new File(dir, children[i]).delete();

//                            ivImage.setImageResource(R.mipmap.ic_launcher);
                        }
                        dir.delete();
                        profile_picture.setImageResource(R.mipmap.ic_launcher);
                    }

                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(intent, SELECT_FILE);
        //startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
       /* ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
             fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        profile_picture.setImageBitmap(thumbnail);
        BitmapDrawable drawable = (BitmapDrawable) profile_picture.getDrawable();
        final Bitmap bitmap = drawable.getBitmap();
        File sdCardDirectory = new File(Environment.getExternalStorageDirectory(), "ImageCapture");
        if (!sdCardDirectory.exists()) {
            sdCardDirectory.mkdirs();
        }
        // File sdCardDirectory = Environment.getExternalStorageDirectory();
        final File image = new File(sdCardDirectory, "wallpaper.jpeg");
        boolean success = false;


        FileOutputStream outStream;
        try {

            outStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);


            outStream.flush();
            outStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            // Toast.makeText(getApplicationContext(), "Image saved with success", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Error during image saving", Toast.LENGTH_LONG).show();
        }
    }

    // @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                int width = 150;
                int height = 150;
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 30, bytes);
                bm = Bitmap.createScaledBitmap(bm, width, height, true);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        profile_picture.setImageBitmap(bm);
        BitmapDrawable drawable = (BitmapDrawable) profile_picture.getDrawable();
        final Bitmap bitmap = drawable.getBitmap();
        File sdCardDirectory = new File(Environment.getExternalStorageDirectory(), "ImageCapture");
        if (!sdCardDirectory.exists()) {
            sdCardDirectory.mkdirs();
        }
        //File sdCardDirectory = Environment.getExternalStorageDirectory();
        final File image = new File(sdCardDirectory, "wallpaper.jpeg");
        boolean success = false;


        FileOutputStream outStream;
        try {

            outStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);


            outStream.flush();
            outStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            // Toast.makeText(getApplicationContext(), "success",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Error during image saving", Toast.LENGTH_LONG).show();
        }

    }


}
