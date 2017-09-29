package AppUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by admin on 3/30/2017.
 */

public class AppConstants {


    public static final String SENDER_ID = "1018482755990";
    public static final String GCM_SENDER_ID = "894894372333";

    //Local
//    public static String BASE_URL = "http://10.0.0.22:8080/askntake/";
//    public static final String IMG_BASE_URL = "http://10.0.0.22:8080/";

//    public static String BASE_URL = "http://192.168.1.20:8080/askntake/";
//    public static final String IMG_BASE_URL = "http://192.168.1.20:8080/";

    //Live
//    public static String BASE_URL = "http://www.askntake.com/api/";
//    public static final String IMG_BASE_URL = "http://www.askntake.com/";

    public static String BASE_URL = "http://www.askntake.com/test/";
    public static final String IMG_BASE_URL = "http://www.askntake.com/";

    //Regustration URL
    public static final String REG_URL = BASE_URL + "rest/user/register";
    public static final String LOGIN_URL = BASE_URL + "rest/user/login";
    public static final String REVIEWS_URL = BASE_URL + "rest/service/reviews";
    public static final String GET_REVIEWS_URL = BASE_URL + "rest/service/getReviews";
    public static final String SERVICE_UPLOAD_URL = BASE_URL + "rest/user/uploadservice";
    public static final String SERVICE_REQUEST_UPLOAD_URL = BASE_URL + "rest/servicerequest/uploadservicerequest";
    public static final String SERVICE_REQUEST_UPDATE_URL = BASE_URL + "rest/servicerequest/updateservicerequest";
    public static final String PRODUCTS_DISPLAY_URL = BASE_URL + "rest/browse/-1/buyandsell/getProducts";
    //public static final String ITEMS_DISPLAY_URL = BASE_URL + "rest/browse/-1/services/getservices";
//    public static final String ITEMS_DISPLAY_FILTER_URL = BASE_URL + "rest/service/5/getServicesV2";

    public static final String SERVICES_UPDATION_URL = BASE_URL + "rest/user/updateservice";

    // public static final String SERVICE_DESCRIPTION_URL = BASE_URL + "rest/user/uploadservice";

    //public static final String ITEMS_DISPLAY_URL = BASE_URL + "rest/service/-1/getservices";

    public static final String CONTACT_US_URL = BASE_URL + "rest/user/query";
    public static final String TERMS_AND_CONDTIONS_URL = BASE_URL + "termsAndConditions.jsp";
    public static final String FAQ_URL = BASE_URL + "frequentlyAskedQuestions.jsp";
    public static final String PRIVACY_POLICY = BASE_URL + "privacyPolicy.jsp";
    public final static String GMAIL_LOGIN = "gmail_login";
    public static final String REPORT_THIS_ITEM_URL = BASE_URL + "rest/product/report";
    public static final String REPORT_THIS_SERVICE_URL = BASE_URL + "rest/service/report";
    public static final String REPORT_THIS_SERVICE_REQUEST_URL = BASE_URL + "rest/servicerequest/report";
    public static final String SERVICE_OWNER_CHAT_HISTORY = BASE_URL + "rest/servicechat/getServiceOwnerChatHistoryV1";
    public static final String SERVICE_USER_CHAT_HISTORY = BASE_URL + "rest/servicechat/getServiceOwnerChatHistoryV2";
    public static final String SERVICE_REQUESTER_CHAT_HISTORY = BASE_URL + "rest/servicerequestchat/getServiceRequesterChatHistory";
    public static final String SERVICE_REQUESTER_PROVIDER_CHAT_HISTORY = BASE_URL + "rest/servicerequestchat/getServiceRequesterChatHistoryV2";
    public static final String SERVICE_REQUESTER_USER_CHAT_HISTORY = BASE_URL + "rest/servicerequestchat/getServiceRequesterChatHistoryV1";

    /* public static String loadServicesUrl(String userId) {
         String ITEMS_DISPLAY_FILTER_URL = BASE_URL + "rest/service/"+userId+"/getServicesV2";

         return ITEMS_DISPLAY_FILTER_URL;
     }*/
    public static String readCountResponce(String userId) {
        return BASE_URL + "rest/chat/" + userId + "/getunreadmessage";
    }

    public static String loadServicesUrl(String userId) {
        String ITEMS_DISPLAY_FILTER_URL = BASE_URL + "rest/service/" + userId + "/getServicesV2";

        return ITEMS_DISPLAY_FILTER_URL;
    }

    public static String loadServiceRequestssUrl(String userId) {
        String SERVICE_REQUESTS_DISPLAY_URL = BASE_URL + "rest/servicerequest/" + userId + "/getServiceRequests";

        return SERVICE_REQUESTS_DISPLAY_URL;
    }

    public static String getFiltersUrl(String userId) {

        String GET_FILTERS_URL = BASE_URL + "rest/filters/" + userId + "/getfilters";

        return GET_FILTERS_URL;
    }

    public static String saveFiltersUrl(String userId) {

        String SAVE_FILTERS_URL = BASE_URL + "rest/filters/" + userId + "/save";

        return SAVE_FILTERS_URL;
    }


    public static String getProductDescriptionUrl(String userEmail, String itemId) {
        String url = "";
        url = BASE_URL + "rest/product/" + userEmail + "/" + itemId;
        return url;
    }

    public static String ServiceMessagesReadStatus() {
        return BASE_URL + "rest/servicechat/readServiceMessageReadStatus";
    }

    public static String ServiceRequestMessagesReadStatus() {
        return BASE_URL + "rest/servicerequestchat/readServiceRequestMessageReadStatus";
    }

    public static String getServiceDescriptionUrl(String userId, String serviceId) {
        String url = "";
        url = BASE_URL + "rest/service/" + userId + "/" + serviceId;
        return url;
    }

    public static String getServicerequestDescriptionUrl(String userId, String serviceId) {
        String url = "";
        url = BASE_URL + "rest/servicerequest/" + userId + "/" + serviceId;
        return url;
    }

    //public static final String SERVICE_DESCRIPTION_URL = BASE_URL + "rest/user/uploadservice";

    public static String forgertPasswordUrl(String emailid) {
        return BASE_URL + "rest/user/" + emailid + "/forgetpassword";

    }

    public static String getMyServicesUrl(long userid) {
        return BASE_URL + "rest/service/" + userid + "/getmyservices";

    }

    public static String getMyServiceRequestsUrl(long userid) {
        return BASE_URL + "rest/servicerequest/" + userid + "/getmyservicerequests";

    }

    public static String getFavServicesUrl(long userid) {
        return BASE_URL + "rest/service/" + userid + "/getfavorites";
    }

    public static SharedPreferences preferencesData(Context mContext) {
        SharedPreferences fb_data_pref = mContext.getSharedPreferences(DataKeyValues.USER_DATA_PREF, mContext.MODE_PRIVATE);
        return fb_data_pref;
    }

    public static File getCompressedFile(Context cnt, String originalImagePath, String image_number) {
        File newFile = new File(cnt.getCacheDir(), image_number + "image_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(newFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Bitmap bitmap = null;
        try {
            bitmap = downsampleBitmap(cnt, originalImagePath, 10);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i("hii", bitmap.getWidth() + "," + bitmap.getHeight());
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        try {
            outStream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            outStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newFile;
    }

    public static String UpdateUserLocation(String userId) {

        String update_location_url = BASE_URL + "rest/user/" + userId + "/updatelocation";

        return update_location_url;
    }


    public static Bitmap downsampleBitmap(Context context, String mCurrentPhotoPath, int sampleSize) throws FileNotFoundException, IOException {
        //The new size we want to scale to
        final int REQUIRED_SIZE = 150;

        Bitmap resizedBitmap = null;
        try {


            BitmapFactory.Options outBitmap = new BitmapFactory.Options();
            outBitmap.inJustDecodeBounds = false; // the decoder will return a bitmap
            outBitmap.inDither = false;                     //Disable Dithering mode
            outBitmap.inPurgeable = true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
            outBitmap.inInputShareable = true; //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
            //			outBitmap.inSampleSize = 8;

            int scale = 4;
            while (outBitmap.outWidth / scale / 2 >= REQUIRED_SIZE && outBitmap.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            outBitmap.inSampleSize = scale;

            resizedBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, outBitmap);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return resizedBitmap;
    }

    public static String addItemToFavoriteList(String userId, String itemId, String servicetype) {

        String addItemUrl = BASE_URL + "rest/user/" + userId + "/" + itemId + "/" + servicetype;

        return addItemUrl;

    }

    public static String addServiceToFavoriteList(String userId, String serviceId, String servicetype) {

        String addItemUrl = BASE_URL + "rest/service/" + userId + "/" + serviceId + "/" + servicetype;

        return addItemUrl;

    }

    public static String removeServiceToFavoriteList(String userId, String serviceId, String remservicetype) {

        String remItemUrl = BASE_URL + "rest/service/" + userId + "/" + serviceId + "/" + remservicetype;

        return remItemUrl;

    }

    public static String deleteService(String userId, String serviceId) {

        String deleteItemUrl = BASE_URL + "rest/service/" + userId + "/" + serviceId + "/delete";

        return deleteItemUrl;

    }

    public static String deleteServiceRequest(String userId, String serviceId) {

        String deleteItemUrl = BASE_URL + "rest/servicerequest/" + userId + "/" + serviceId + "/delete";

        return deleteItemUrl;

    }

    public static String updatePassword(String userId) {

        String updatepwdurl = BASE_URL + "rest/user/" + userId + "/" + "updatePwd";

        return updatepwdurl;

    }

    public static String logoutapp(String userId) {

        String logoutappurl = BASE_URL + "rest/user/" + userId + "/" + "logout_user";

        return logoutappurl;

    }

    public static String SendOnlineStatus() {
        return BASE_URL + "rest/chat/onlinestatus";
    }

    public static String UpdateUserData() {
        String update_url = BASE_URL + "rest/user/updateProfile";
        return update_url;
    }

}
