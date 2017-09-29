package AppUtils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by admin on 3/31/2017.
 */

public class Helper {

    public void getExpendableListViewSize(ExpandableListView myListView/*, int childCount1*/) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        int hight1 = myListAdapter.getCount();
       // int hhh2 = childCount;
        int size_of_cell = 0;
        int res= myListAdapter.getCount();
       for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            size_of_cell = listItem.getMeasuredHeight();
            totalHeight += listItem.getMeasuredHeight();
        }
        //totalHeight=totalHeight+(childCount*size_of_cell);

        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height =totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }

    public void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        int hight1 = myListAdapter.getCount();
        int size_of_cell = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            size_of_cell = listItem.getMeasuredHeight();
            totalHeight += listItem.getMeasuredHeight();
        }
        //totalHeight=totalHeight+(childCount*size_of_cell);

        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }




    public  String getCurrencyType(String cuntrey_code) {
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


}
