package AppUtils;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by admin on 1/30/2017.
 */

public class ServiceHandler {


    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    Context cnt;

    public ServiceHandler() {

    }

    public ServiceHandler(Context cnt) {
        this.cnt = cnt;
    }

    public String makeServiceCall(String url, String method, JSONObject post_object) {
        String server_responce = null;
        if (method.equalsIgnoreCase("GET")) {
            try {
                server_responce = downloadContent(url);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (method.equalsIgnoreCase("POST")) {
            BufferedReader reader = null;
            String Error = null;
            try {

                // Defined URL  where to send data
                URL request_url = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) request_url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-type", "application/json");
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                String json ="";
                if (post_object!=null) {
                    json = post_object.toString();
                }
                wr.write(json);
                wr.flush();
                int code = conn.getResponseCode();

                if (code == 200) {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }
                else {
                    reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + "");
                }

                // Append Server Response To Content String
                server_responce = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return server_responce;
    }


    private String downloadContent(String myurl) throws IOException {
        InputStream is = null;
        // int length = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            conn.connect();
            int code = conn.getResponseCode();
            int response = conn.getResponseCode();
            Log.d("", "The response is: " + response);

            if (code == 200) {
                is = conn.getInputStream();
            } else {
                is = conn.getErrorStream();
            }


            // Convert the InputStream into a string
            String contentAsString = convertInputStreamToString(is);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String convertInputStreamToString(InputStream stram) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(stram));
        String line = "";
        String result = "";
        while ((line = br.readLine()) != null)
            result += line;

        stram.close();
        return result;
    }
}