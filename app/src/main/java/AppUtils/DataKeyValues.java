package AppUtils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 3/30/2017.
 */

public class DataKeyValues {


    public final static String NO_PROFILE_IMG = "noimage";


    //Registration
    public final static String REG_EMAIL_ID = "email";
    public final static String REG_PASSWORD = "password";
    public final static String REG_FIRSTNAME = "firstname";
    public final static String REG_LASTNAME = "lastname";
    public final static String REG_IMAGE = "image";
    public final static String REG_FB = "fb";
    public final static String REG_FBID = "fbId";
    public final static String REG_REGTYPE = "regtype";
    public final static String REG_GENDER = "gender";
    public final static String REG_DIVICETYPE = "divicetype";
    public final static String REG_GCMID = "gcmid";
    public final static String OWNER_ID = "userId";

    //Registration


    public final static String SERVICE_REQUESTER_ID = "servicerequestid";
    public final static String REQUESTER_ID = "requesterid";
    public static final String REQUESTER_DESCRIPTION = "description";
    public static final String REQUESTER_CATEGORY = "category";
    public static final String REQUESTER_SUB_CATEGORY = "subcategory";
    public static final String REQUESTER_CONTACT = "contact";
    public final static String REQUESTER_EMAIL_ID = "email";
    public final static String REQUESTER_NAME = "requestername";
    public static final String REQUESTER_LATITUDE = "latitude";
    public static final String REQUESTER_LANGITUDE = "langitude";




    //Login
    public static final String LOGIN_EMAIL_ID = "username";
    public static final String LOGIN_PASSWORD = "password";
    public static final String LOGIN_GCMID = "gcmid";
    public static final String LOGIN_DIVICETYPE = "divicetype";
    public static final String DIVICE_TYPE_VALUE = "android";
    public static final String RESPONSE = "response";
    public static final String MESSAGE = "message";
    public final static String GMAIL_LOGIN = "gmail_login";
    public final static String MYLOCATION = "mylocation";
    public final static String SEARCH_SHOW_LOCATION = "search_show_location";
    public static final String MYLOGINPREFERENCES = "myLoginPreferences";
    public final static String MY_OWN_LATITUDE_VALUE = "my_own_locatio_lat";
    public final static String MY_OWN_LONGITUDE_VALUE = "my_own_locatio_long";
    public final static String LATITUDE_VALUE = "user_responce_lattitude";
    public final static String LONGITUDE_VALUE = "user_responce_langitude";
    public final static String FILTERLATTITUDE = "filterlat";
    public final static String FILTERLONGITUDE = "filterlong";

    public final static String SET_LOC_LATTITUDE = "setlattitude";
    public final static String SET_LOC_LONGITUDE = "setlongitude";


    public final static String PRODUCT_UPLOAD_STATE = "product_upload_state";
    public final static String PRODUCT_UPLOAD_CITY = "product_upload_city";
    //product getting service url and values starts here

    public final static String UNREAD_MSG_COUNT = "unread_message_count";

    public static final String ITEM_GET_LAT = "lat";
    public static final String PROFILE_LOCATION_ITEMS = "profile_location";
    public static final String ITEM_GET_LANG = "lang";
    public static final String ITEM_GET_RANGE = "range";
    public static final String ITEM_SERVICE_NAME = "servicename";
    public static final String ITEM_SERVICE_PROVIDER_NAME = "serviceprovidername";
    public static final String ITEM_TIME_RANGE = "timeRange";
    public final static String ZIPCODE = "user_zipcode";

    public static final String ITEM_GET_SORTBYPRICEHTOL = "sortByPriceHtoL";
    public static final String ITEM_GET_SORTBYPRICELTOH = "sortByPriceLtoH";
    public static final String ITEM_GET_SORTBYDISTANCE = "sortByDistance";
    public static final String ITEM_GET_SORTBYTIME = "sortByTime";
    public static final String ITEM_GET_PRODUCTNAME = "productname";
    public static final String ITEM_GET_PRICEFROM = "priceFrom";
    public static final String ITEM_GET_PRICETO = "priceTo";
    public static final String ITEM_GET_PRICETYPE = "priceType";
    public static final String ITEM_GET_TIMERANGE = "timeRange";
    public static final String ITEM_GET_INVOICE = "invoice";
    public static final String ITEM_GET_NEGOTIABLE = "negotiable";
    public static final String ITEM_GET_SHIPPING = "shipping";
    public static final String ITEM_GET_MARKEDAS = "markedAs";
    public static final String ITEM_GET_DISTANCEIN = "distanceIn";
    public static final String ITEM_GET_RATING = "rating";
    public static final String ITEM_GET_CATEGORY = "category";
    public static final String ITEM_GET_SUBCATEGORIES = "subcategories";
    public static final String ITEMS_GET_CATEGORIES = "categories";
    public static final String POSITIONS = "positions";

    //product getting service url and values ends here

    public static final String CONTACT_US_EMAIL = "email";
    public static final String CONTACT_US_SUBJECT = "subject";
    public static final String CONTACT_US_CONTENT = "content";

    public final static String UPLOAD_PRICE_TYPE = "upload_price_type";
    public final static String CUNTRY_CODE = "cuntry_code";
    public final static String CUNTRY_CODE1 = "cuntry_code1";
    public final static String LOGIN_STATUS = "login_status";

    ///user login data
    public static final String USER_REQ_LAT = "lat";
    public static final String USER_REQ_LANG = "lang";
    public static final String USER_FIRSTNAME = "firstname";
    public static final String USER_LASTNAME = "lastname";
    public static final String USER_USERID = "userId";
    public static final String USER_EMAIL = "email";
    public static final String USER_IMAGE = "image";
    public static final String USER_CITY = "city";
    public static final String USER_STATE = "state";
    public static final String USER_ZIPCODE = "zipcode";
    public static final String USER_GENDER = "gender";
    public static final String USER_DATA_PREF = "user_data_pref";
    public static final String USER_LOGIN_STATUS = "login_status";

    // Item upload data
    public static final String UPLOAD_DATA_PREF = "upload_data_pref";
    public static final String UPLOAD_USERID = "userid";
    public static final String UPLOAD_NAME = "name";
    public static final String UPLOAD_DESCRIPTION = "description";
    public static final String UPLOAD_PROVIDER = "providername";
    public static final String UPLOAD_CATEGORY = "category";
    public static final String UPLOAD_SUBCATEGORY = "subcategory";
    public static final String UPLOAD_CONTACT = "contact";
    public static final String UPLOAD_EMAIL = "email";
    public static final String UPLOAD_WEBSITE = "website";
    public static final String UPLOAD_ADDRESS = "address";
    public static final String UPLOAD_SERVICE_ZIPCODE = "servicezipcode";
    public static final String UPLOAD_SERVICEAREA = "servicearea";
    public static final String UPLOAD_LATITUDE = "latitude";
    public static final String UPLOAD_LANGITUDE = "langitude";
    public static final String UPLOAD_SINGLEFAMILYROOM = "singlefamilyroom";
    public static final String UPLOAD_COMMERCIAL_OFFICE = "commercialandofficialspace";
    public static final String UPLOAD_ROOMSHARING = "roomshare";
    public static final String UPLOAD_ONETIMERIDE = "onetimeride";

    public static final String UPLOAD_BEDROOMS = "bedrooms";
    public static final String UPLOAD_BATHROOMS = "bathrooms";
    public static final String UPLOAD_AVAILABLE_FROM_DATE = "avalableFrom";
    public static final String UPLOAD_NO_OF_ROOMS = "noofrooms";
    public static final String UPLOAD_ROOMS = "rooms";
    public static final String UPLOAD_TOTAL_SQ_FEET = "totalsqft";
    public static final String UPLOAD_AREA_SQ_FEET = "areainsqft";
    public static final String UPLOAD_ASKING_PRICE = "askingprice";
    public static final String UPLOAD_PRICE = "price";
    public static final String UPLOAD_ACCOMMODATES = "accommodates";
    public static final String UPLOAD_GENDER = "gender";
    public static final String UPLOAD_DEST_FROM = "destfrom";
    public static final String UPLOAD_DEST_TO = "destto";
    public static final String UPLOAD_TRAVEL_TIME = "traveltime";
    public static final String UPLOAD_SERVICE_STATE = "service_upload_state";
    public static final String UPLOAD_SERVICE_CITY = "service_upload_state";
    public final static String UPLOAD_SERVICE_CUNTRY_CODE = "service_upload_countrtcode";
    public final static String PRICE_TYPE = "pricetype";
    public final static String UPLOAD_PRICETYPE = "priceType";
    public final static String COMPLETE_ADDRESS_OF_LOCATION = "complete_address_location";
   // public final static String PREV_IMAGE_URL = "availableImages";

    public static final String UPLOAD_TYPESERVICES = "typeservices";
    public static final String UPLOAD_FROM_DAY = "fromday";
    public static final String UPLOAD_FROM_TIME = "fromtime";
    public static final String UPLOAD_TO_DAY = "today";
    public static final String UPLOAD_TO_TIME = "totime";


    public final static String LOCATION = "user_location_address";

    public final static String OWNER_ZIPCODE = "owner_zipcode";
    public final static String OWNER_CITY = "owner_city";
    public final static String OWNER_STATE = "owner_state";

    public final static String SERVICE_ID = "serviceid";

    public final static String SEARCH_APPLYED = "search_applyed";


    public static Address LocationAddressData(Context cntx, String latitude,
                                              String logitude) {

        Geocoder geocoder = new Geocoder(cntx, Locale.getDefault());
        Address address = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    Double.parseDouble(latitude), Double.parseDouble(logitude),
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

    public static String getCurrencyType(String cuntrey_code) {
        String currencyType = "INR";
        if (cuntrey_code.equalsIgnoreCase("Rs")) {
            cuntrey_code = "IN";
        }
        switch (cuntrey_code) {

            case "IN":
                currencyType = "INR";
                break;
            case "JP":
                currencyType = "Yen";
                break;
            case "US":
                currencyType = "USD";
                break;
            case "NP":
                currencyType = "NPR";
                break;
            case "MM":
                currencyType = "MMK";
                break;
            case "MY":
                currencyType = "MYR";
                break;
            case "NZ":
                currencyType = "NZD";
                break;
            case "SA":
                currencyType = "SAR";
                break;
            case "AE":
                currencyType = "AED";
                break;
            case "KW":
                currencyType = "KWD";
                break;
            case "OM":
                currencyType = "OMR";
                break;
            case "QA":
                currencyType = "QAR";
                break;
            case "ID":
                currencyType = "IDR";
                break;
            case "PH":
                currencyType = "PHP";
                break;

            case "ZA":
                currencyType = "ZAR";
                break;
            case "AR":
                currencyType = "ARS";
                break;
            case "MX":
                currencyType = "MXN";
                break;
            case "CO":
                currencyType = "COP";
                break;

            case "GB":
                currencyType = "GBP";
                break;

            case "BR":
                currencyType = "BRL";
                break;
            case "AU":
                currencyType = "AUD";
                break;
            case "CA":
                currencyType = "CAD";
                break;
            case "SG":
                currencyType = "SGD";
                break;
            case "CN":
                currencyType = "Yuan";
                break;
            case "LK":
                currencyType = "LKR";
                break;
            case "DE":
                currencyType = "DEM";
                break;
            case "FR":
                currencyType = "FRF";
                break;
            case "IT":
                currencyType = "ITL";
                break;
            case "GR":
                currencyType = "GRD";
                break;
            case "PT":
                currencyType = "PTE";
                break;

            case "NO":
                currencyType = "NOK";
                break;

            case "RU":
                currencyType = "RUB";
                break;

            case "ES":
                currencyType = "ESP";
                break;

            case "CH":
                currencyType = "CHF";
                break;

            case "IE":
                currencyType = "EUR";
                break;
            case "AT":
                currencyType = "ATS";
                break;
            case "SK":
                currencyType = "EUR";
                break;
            case "CY":
                currencyType = "EUR";
                break;
            case "MT":
                currencyType = "EUR";
                break;

            case "SI":
                currencyType = "EUR";
                break;
            case "FI":
                currencyType = "EUR";
                break;
            case "EE":
                currencyType = "EUR";
                break;
            case "BE":
                currencyType = "BEF";
                break;
            case "LV":
                currencyType = "EUR";
                break;

            case "LT":
                currencyType = "EUR";
                break;
            case "ME":
                currencyType = "EUR";
                break;
            case "MC":
                currencyType = "EUR";
                break;

            case "LU":
                currencyType = "EUR";
                break;

            case "XK":
                currencyType = "EUR";
                break;
            case "VA":
                currencyType = "EUR";
                break;
            case "ZW":
                currencyType = "EUR";
                break;
            case "AD":
                currencyType = "EUR";
                break;
            case "SM":
                currencyType = "EUR";
                break;

            case "RE":
                currencyType = "EUR";
                break;

            case "GP":
                currencyType = "EUR";
                break;

            case "MQ":
                currencyType = "EUR";
                break;

            case "BL":
                currencyType = "EUR";
                break;

            case "YT":
                currencyType = "EUR";
                break;

            case "AX":
                currencyType = "EUR";
                break;

            case "PM":
                currencyType = "EUR";
                break;

            case "MF":
                currencyType = "EUR";
                break;

            case "NL":
                currencyType = "ANG";
                break;

            default:
                break;
        }

        return currencyType;
    }

    public static String getCountreyCode(String currencyType) {
        String country_code = "IN";
        switch (currencyType) {

            case "INR":
                country_code = "IN";
                break;
            case "Yen":
                country_code = "JP";
                break;
            case "USD":
                country_code = "US";
                break;
            case "NPR":
                country_code = "NP";
                break;
            case "MMK":
                country_code = "MM";
                break;
            case "NZD":
                currencyType = "NZ";
                break;

            case "MYR":
                country_code = "MY";
                break;
            case "SAR":
                country_code = "SA";
                break;
            case "AED":
                country_code = "AE";
                break;
            case "KWD":
                country_code = "KW";
                break;
            case "OMR":
                country_code = "OM";
                break;

            case "QAR":
                country_code = "QA";
                break;
            case "IDR":
                country_code = "ID";
                break;
            case "PHP":
                country_code = "PH";
                break;
            case "ANG":
                country_code = "NL";
                break;
            case "ZAR":
                country_code = "ZA";
                break;

            case "ARS":
                country_code = "AR";
                break;
            case "MXN":
                country_code = "MX";
                break;
            case "COP":
                country_code = "CO";
                break;

            case "GBP":
                country_code = "GB";
                break;

            case "BRL":
                country_code = "BR";
                break;
            case "AUD":
                country_code = "AU";
                break;
            case "CAD":
                country_code = "CA";
                break;
            case "SGD":
                country_code = "SG";
                break;
            case "Yuan":
                country_code = "CN";
                break;

            case "LKR":
                country_code = "LK";
                break;

            case "ATS":
                country_code = "AT";
                break;

            case "BEF":
                country_code = "BE";
                break;

            case "FRF":
                country_code = "FR";
                break;

            case "DEM":
                country_code = "DE";
                break;

            case "ITL":
                country_code = "IT";
                break;

            case "GRD":
                country_code = "GR";
                break;

            case "NOK":
                country_code = "NO";
                break;

            case "PTE":
                country_code = "PT";
                break;

            case "RUB":
                country_code = "RU";
                break;

            case "ESP":
                country_code = "ES";
                break;

            case "CHF":
                country_code = "CH";
                break;

            case "EUR":
                country_code = "PM";
                break;

            default:
                break;
        }

        return country_code;
    }


}
