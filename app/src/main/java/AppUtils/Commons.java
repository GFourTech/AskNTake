package AppUtils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by G4 on 5/9/2017.
 */

public class Commons {

    public static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 123;

    public static Map<String, Map<String, String>> getCategories(Context cnt) {
        List<Map<String, List<Map<String, String>>>> categoriesList = new ArrayList<>();
        Map<String, Map<String, String>> mainObj = new LinkedHashMap<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(cnt));
            JSONArray categoriesArray = obj.getJSONArray("categories");

            for (int i = 0; i < categoriesArray.length(); i++) {
                JSONObject categoryObj = categoriesArray.getJSONObject(i);
                String categoryName = categoryObj.getString("name");
                JSONArray subCategoriesJsonArray = categoryObj.getJSONArray("subcategories");
                Map<String, String> subcategoryMap = new LinkedHashMap<>();
                for (int j = 0; j < subCategoriesJsonArray.length(); j++) {

                    JSONObject subcategoryObj = subCategoriesJsonArray.getJSONObject(j);
                    String form = subcategoryObj.getString("form");
                    String name = subcategoryObj.getString("name");
                    subcategoryMap.put(name, form);

                }
                mainObj.put(categoryName, subcategoryMap);
                // categoriesList.add(mainObj);
            }
        } catch (Exception e) {

            return null;
        }
        return mainObj;
    }

    public static Map<String, Map<String, String>> getCategoriesCheckboxes(Context cnt) {
        List<Map<String, List<Map<String, String>>>> categoriesList = new ArrayList<>();
        Map<String, Map<String, String>> mainObj = new LinkedHashMap<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(cnt));
            JSONArray categoriesArray = obj.getJSONArray("categories");

            for (int i = 0; i < categoriesArray.length(); i++) {
                JSONObject categoryObj = categoriesArray.getJSONObject(i);
                String categoryName = categoryObj.getString("name");
                JSONArray subCategoriesJsonArray = categoryObj.getJSONArray("subcategories");
                Map<String, String> subcategoryMap = new LinkedHashMap<>();
                for (int j = 1; j < subCategoriesJsonArray.length(); j++) {

                    JSONObject subcategoryObj = subCategoriesJsonArray.getJSONObject(j);
                    String form = subcategoryObj.getString("form");
                    String name = subcategoryObj.getString("name");
                    subcategoryMap.put(name, form);

                }
                mainObj.put(categoryName, subcategoryMap);
                // categoriesList.add(mainObj);
            }
        } catch (Exception e) {

            return null;
        }
        return mainObj;
    }

    public static Map<String, Map<String, String>> getCategoriesServiceRequests(Context cnt) {
        Map<String, Map<String, String>> mainObj = new LinkedHashMap<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(cnt));
            JSONArray categoriesArray = obj.getJSONArray("categories");

            for (int i = 0; i < categoriesArray.length(); i++) {
                JSONObject categoryObj = categoriesArray.getJSONObject(i);
                String categoryName = categoryObj.getString("name");
                Map<String, String> subcategoryMap = new LinkedHashMap<>();
                mainObj.put(categoryName, subcategoryMap);
                // categoriesList.add(mainObj);
            }
        } catch (Exception e) {

            return null;
        }
        return mainObj;
    }

    public static String loadJSONFromAsset(Context cnt) {
        String json = null;
        try {
            InputStream is = cnt.getAssets().open("servicecategories.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static Location getLastKnownLocation(Context contx) {
        LocationManager mLocationManager = (LocationManager) contx
                .getSystemService(contx.LOCATION_SERVICE);

        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        Location l = null;
        for (String provider : providers) {
            try {
                l = mLocationManager.getLastKnownLocation(provider);
            } catch (SecurityException e) {
                e.printStackTrace();
            }

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
        if (bestLocation == null) {
            return null;
        } else {
            return bestLocation;
        }
    }

    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

}
