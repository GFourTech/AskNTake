package AppUtils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import Pojo.SharedPrefPojo;


public class Utility {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSION_ACCESS_COURSE_LOCATION  = 123;

    public static final int Call_Request = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission is necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkGpsPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission is necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_ACCESS_COURSE_LOCATION);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions( (Activity) context, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                            MY_PERMISSION_ACCESS_COURSE_LOCATION ); }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkCallPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CALL_PHONE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Call Permission is Necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, Call_Request);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, Call_Request);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static SharedPrefPojo getData(Context cnt){

        SharedPrefPojo pojo= new SharedPrefPojo();
        SharedPreferences fb_data_pref = cnt.getSharedPreferences(DataKeyValues.USER_DATA_PREF, cnt.MODE_PRIVATE);
        String user_latitude=fb_data_pref.getString(DataKeyValues.UPLOAD_LATITUDE, null);
        String user_longitude=fb_data_pref.getString(DataKeyValues.UPLOAD_LANGITUDE, null);
        String user_zipcode=fb_data_pref.getString(DataKeyValues.ZIPCODE, null);
        String user_address=fb_data_pref.getString(DataKeyValues.COMPLETE_ADDRESS_OF_LOCATION, null);

        pojo.setLangitude(user_longitude);
        pojo.setLattitude(user_latitude);
        pojo.setZipcode(user_zipcode);
        pojo.setArea(user_address);
        return pojo;

    }
    public static void resetUploadLocation(Context cnt){

        SharedPrefPojo pojo= new SharedPrefPojo();
        SharedPreferences fb_data_pref = cnt.getSharedPreferences(DataKeyValues.USER_DATA_PREF, cnt.MODE_PRIVATE);
        SharedPreferences.Editor LocationData_editor=fb_data_pref.edit();
        LocationData_editor.putString(DataKeyValues.UPLOAD_LATITUDE,null);
        LocationData_editor.putString(DataKeyValues.UPLOAD_LANGITUDE, null);
        LocationData_editor.commit();


    }

}
