package AppUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class ConnectionDetector {

    private Context _context;

    public ConnectionDetector(Context context) {
        this._context = context;
    }

    public boolean isConnectingToInternet(Context _context) {

        ConnectivityManager conMan = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //mobile
        State mobile = conMan.getNetworkInfo(0).getState();

        //wifi
        State wifi = conMan.getNetworkInfo(1).getState();


        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        //checks whether net is available or not
        if (mobile == State.CONNECTED || mobile == State.CONNECTING
                || !(info == null || !info.isConnected() || info.isRoaming())) {
            //mobile
            return true;
        } else if (wifi == State.CONNECTED || wifi == State.CONNECTING
                || !(info == null || !info.isConnected() || info.isRoaming())) {
            //wifi
            return true;
        } else {
            return false;
        }

    }
}